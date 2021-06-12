package managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.PrimeFaces;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

import model.ColaboradorVI;
import model.Evento;
import model.Post;
import model.TimeLineEx;
import model.User;
import model.Utilidade;
import service.ColaboradorVIService;
import service.EventoService;
import service.HomeService;
import service.LoginService;
import service.PostService;
import service.UsuarioService;
import service.UtilidadeService;
import util.DataUtil;
import util.Filtro;
import util.UsuarioSessao;

@Named(value = "home_controller")
@ViewScoped
public class HomeController implements Serializable {

	private static final long serialVersionUID = 1L;

	private TimelineModel model;

	private ScheduleModel eventModel;

	@Inject
	private Post post;

	private boolean selectable = true;
	private boolean zoomable = true;
	private boolean moveable = true;
	private boolean stackEvents = true;
	private String eventStyle = "box";
	private boolean axisOnTop;
	private boolean showCurrentTime = true;
	private boolean showNavigation = false;

	private String novaSenha;

	private String paramBusca = "";

	private Filtro filtro = new Filtro();

	private List<Post> posts;
	private List<Utilidade> utilidades;
	private List<ColaboradorVI> colaboradores;

	private Boolean showCalendar = true;

	@Inject
	private UsuarioSessao sessao;

	@Inject
	private HomeService homeService;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private LoginService loginService;

	@Inject
	protected EventoService eventService;

	@Inject
	protected PostService postService;

	@Inject
	protected UtilidadeService utilidadeService;

	@Inject
	protected ColaboradorVIService colaboradorService;

	@Inject
	private Evento evento;

	private String link = "#";

	private Boolean checado;

	private User usuario;

	private Date initDateTimeLine;
	private Date endDateTimeLine;

	public HomeController() {
	}

	public void init() throws IOException {

		if (homeService.verificarSolicitacoes(sessao.getIdColadorador())) {
			HttpServletResponse resp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			resp.sendRedirect(req.getContextPath() + "/main/autorizacoes.xhtml");
		}

		openDialogSenha();
		// initTimeLine();
		carregarListPost();
		// carregarUtilidades();
		// carregarColaboradores();
		initSchedule();

		System.out.println("Home");
	}

	public void carregarColaboradores() {
		colaboradores = new ArrayList<>();
		colaboradores = colaboradorService.findAll(filtro);
	}

	public void carregarUtilidades() {
		utilidades = new ArrayList<>();
		utilidades = utilidadeService.getAll(filtro);
	}

	public void salvarNovaSenha() {
		PrimeFaces current = PrimeFaces.current();
		usuario = sessao.getUsuario();
		usuarioService.updateSenha(usuario, novaSenha);
		current.executeScript("PF('trocasenha').hide();");
	}

	public void cancelarTrocaDeSenha() {
		usuario = sessao.getUsuario();
		loginService.cancelarTrocaDeSenha(usuario, checado);
	}

	public void openDialogSenha() {

		if (sessao.getUsuario().getSenhaAuto() != null)
			if (sessao.getUsuario().getSenhaAuto()) {
				PrimeFaces current = PrimeFaces.current();
				current.executeScript("PF('trocasenha').show();");
			}

	}

	public void salvarPost() {
		postService.salvar(post, sessao.getUsuario());
		carregarListPost();
		post = new Post();
	}

	public void carregarListPost() {
		posts = new ArrayList<Post>();
		posts = postService.getAll(new Filtro());
	}

	public void initRangeShowList() {
		model = new TimelineModel();
		initDateTimeLine = DataUtil.getDataInicioTimiLine(new Date());
		endDateTimeLine = DataUtil.getDataFimTimiLine(new Date());

	}

	public void initTimeLine() {

		initRangeShowList();
		List<Evento> eventos = eventService.getResumeList();

		TimelineEvent event = new TimelineEvent();
		for (Evento evento : eventos) {
			event = new TimeLineEx(evento);
			model.add(event);
		}

	}

	public void initSchedule() {
		eventModel = new DefaultScheduleModel();
		DefaultScheduleEvent event = new DefaultScheduleEvent();
		List<Evento> eventos = eventService.getResumeList();
		for (Evento evento : eventos) {
			event = new DefaultScheduleEvent();
			event.setTitle(evento.getTitulo());
			event.setStartDate(evento.getDataInicio());
			event.setEndDate(evento.getDataFim());
			event.setDescription(evento.getDescricao());
			event.setData(evento);
			eventModel.addEvent(event);
		}
	}

	private ScheduleEvent event = new DefaultScheduleEvent();

	public void onEventSelect(SelectEvent selectEvent) {
		event = (ScheduleEvent) selectEvent.getObject();
	}

	public void onDateSelect(SelectEvent selectEvent) {
		event = (ScheduleEvent) selectEvent.getObject();
	}

	public void onEventMove(ScheduleEntryMoveEvent event) {
		// FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event
		// moved", "Delta:" + ((Object) event).getDeltaAsDuration());

		// addMessage(message);
	}

	public void onEventResize(ScheduleEntryResizeEvent event) {
		// FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event
		// resized", "Start-Delta:" + event.getDeltaStartAsDuration() + ", End-Delta: "
		// + event.getDeltaEndAsDuration());

		// addMessage(message);
	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void onSelect(TimelineSelectEvent e) {
		TimelineEvent timelineEvent = e.getTimelineEvent();
		TimeLineEx ex = (TimeLineEx) timelineEvent;
		this.evento = ex.getEvento();

		link = evento.getLinkReuniao() != null && !evento.getLinkReuniao().equals("") ? evento.getLinkReuniao() : "#";

	}

	public Boolean getChecado() {
		return checado;
	}

	public void setChecado(Boolean checado) {
		this.checado = checado;
	}

	public String getParamBusca() {
		return paramBusca;
	}

	public void setParamBusca(String paramBusca) {
		this.paramBusca = paramBusca;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public TimelineModel getModel() {
		return model;
	}

	public void setModel(TimelineModel model) {
		this.model = model;
	}

	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	public boolean isZoomable() {
		return zoomable;
	}

	public void setZoomable(boolean zoomable) {
		this.zoomable = zoomable;
	}

	public boolean isMoveable() {
		return moveable;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}

	public boolean isStackEvents() {
		return stackEvents;
	}

	public void setStackEvents(boolean stackEvents) {
		this.stackEvents = stackEvents;
	}

	public String getEventStyle() {
		return eventStyle;
	}

	public void setEventStyle(String eventStyle) {
		this.eventStyle = eventStyle;
	}

	public boolean isAxisOnTop() {
		return axisOnTop;
	}

	public void setAxisOnTop(boolean axisOnTop) {
		this.axisOnTop = axisOnTop;
	}

	public boolean isShowCurrentTime() {
		return showCurrentTime;
	}

	public void setShowCurrentTime(boolean showCurrentTime) {
		this.showCurrentTime = showCurrentTime;
	}

	public boolean isShowNavigation() {
		return showNavigation;
	}

	public void setShowNavigation(boolean showNavigation) {
		this.showNavigation = showNavigation;
	}

	public Date getInitDateTimeLine() {
		return initDateTimeLine;
	}

	public void setInitDateTimeLine(Date initDateTimeLine) {
		this.initDateTimeLine = initDateTimeLine;
	}

	public Date getEndDateTimeLine() {
		return endDateTimeLine;
	}

	public void setEndDateTimeLine(Date endDateTimeLine) {
		this.endDateTimeLine = endDateTimeLine;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public List<Utilidade> getUtilidades() {
		return utilidades;
	}

	public void setUtilidades(List<Utilidade> utilidades) {
		this.utilidades = utilidades;
	}

	public List<ColaboradorVI> getColaboradores() {
		return colaboradores;
	}

	public void setColaboradores(List<ColaboradorVI> colaboradores) {
		this.colaboradores = colaboradores;
	}

	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}

	public Boolean getShowCalendar() {
		return showCalendar;
	}

	public void setShowCalendar(Boolean showCalendar) {
		this.showCalendar = showCalendar;
	}

}
