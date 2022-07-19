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

import model.*;
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
	private List<MenuLateral> menus;
	private String classMenu1;
	private String classMenu2;
	private String classMenu3;
	private String classMenu4;
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
		this.menus = new ArrayList<MenuLateral>();
		this.paramBusca = "";
		this.classMenu1 = "enable";
		this.classMenu2 = "disable";
		this.classMenu3 = "disable";
		this.classMenu4 = "disable";
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
		current.executeScript("PF('trocasenha').hide(); stop();");
	}

	public void mudarClasse(final Integer index) {
		switch (index) {
			case 1: {
				this.classMenu1 = "enable";
				this.classMenu2 = "disable";
				this.classMenu3 = "disable";
				this.classMenu4 = "disable";
				break;
			}
			case 2: {
				this.classMenu1 = "disable";
				this.classMenu2 = "enable";
				this.classMenu3 = "disable";
				this.classMenu4 = "disable";
				break;
			}
			case 3: {
				this.classMenu1 = "disable";
				this.classMenu2 = "disable";
				this.classMenu3 = "enable";
				this.classMenu4 = "disable";
				break;
			}
			default: {
				this.classMenu1 = "disable";
				this.classMenu2 = "disable";
				this.classMenu3 = "disable";
				this.classMenu4 = "enable";
				break;
			}
		}
	}

	public void cancelarTrocaDeSenha() {
		usuario = sessao.getUsuario();
		//loginService.cancelarTrocaDeSenha(usuario, checado);
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
	public List<MenuLateral> carregarMenus() {
		this.menus = new ArrayList<MenuLateral>();
		MenuLateral menu = new MenuLateral();
		menu = new MenuLateral();
		menu.setNome("Solicita\u00e7\u00e3o de pagamento");
		menu.setIcon("check-signing.svg");
		menu.setEndereco("pagamento.xhtml");
		menu.setDescricao("Tela de solicita\u00e7\u00e3o de pagamento. Para ir a tela de Solicita\u00e7\u00e3o de pagamento");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Solicita\u00e7\u00e3o de viagem");
		menu.setIcon("road-with-two-placeholders.svg");
		menu.setEndereco("SolicitacaoViagem.xhtml");
		menu.setDescricao("Tela de solicita\u00e7\u00e3o de viagens. Para ir a tela Solicita\u00e7\u00e3o de viagem");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Compras");
		menu.setIcon("shopping-cart-moving-symbol.svg");
		menu.setEndereco("compras.xhtml");
		menu.setDescricao("Tela de solicita\u00e7\u00e3o de compras. Para ir a tela Compras");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Presta\u00e7\u00e3o de contas");
		menu.setIcon("prest_conta.svg");
		menu.setEndereco("prestacaodecontas.xhtml");
		menu.setDescricao("Tela de presta\u00e7\u00e3o de contas. Para ir a tela ");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Log\u00edstica");
		menu.setIcon("log.svg");
		menu.setEndereco("controle_expedicao.xhtml");
		menu.setDescricao("Tela de controle de expedi\u00e7\u00e3o de pedidos de compra. Para ir a tela ");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Termos");
		menu.setIcon("termo.svg");
		menu.setEndereco("termo_expedicao.xhtml");
		menu.setDescricao("Tela de termos de expedi\u00e7\u00e3o de pedidos de compra. Para ir a tela ");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Concilia\u00e7\u00e3o Financeira");
		menu.setIcon("lancamentos.svg");
		menu.setEndereco("lancamentosV2.xhtml");
		menu.setDescricao("Lan\u00e7amentos v\u00e1lidados e provisionados para contas a paga/receber. Para ir a tela ");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Valida\u00e7\u00e3o de lan\u00e7amentos");
		menu.setIcon("liberacao_lancamento.svg");
		menu.setEndereco("liberacao_lancamento.xhtml");
		menu.setDescricao("Tela de valida\u00e7\u00e3o de lan\u00e7amentos. Para ir a tela ");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Relat\u00f3rios");
		menu.setIcon("relatorio.svg");
		menu.setEndereco("relatorios_fin.xhtml");
		menu.setDescricao("Tela de relat\u00f3rios. Para ir a tela ");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Doa\u00e7\u00f5es");
		menu.setIcon("give-money.svg");
		menu.setEndereco("orcamentos.xhtml");
		menu.setDescricao("Tela de cadastro e acompanhamento financeiro de doa\u00e7\u00f5es. Para ir a tela ");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Projetos");
		menu.setIcon("businessmen-communication.svg");
		menu.setEndereco("projetos.xhtml");
		menu.setDescricao("Tela de cadastro de Projetos. Para ir a tela de projetos");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Plano de trabalho");
		menu.setIcon("plano_trabalho.svg");
		menu.setEndereco("planos.xhtml");
		menu.setDescricao("Tela de planos de trabalho. Para ir a tela ");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Custo de pessoal");
		menu.setIcon("users-group.svg");
		menu.setEndereco("custo_pessoal.xhtml");
		menu.setDescricao("Tela de custo de pessoal. Para ir a tela ");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Contas");
		menu.setIcon("conta_bancaria.svg");
		menu.setEndereco("contas.xhtml");
		menu.setDescricao("Tela de cadastro de contas. Para ir a tela ");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Bancos");
		menu.setIcon("banco.svg");
		menu.setEndereco("bancos.xhtml");
		menu.setDescricao("Tela de cadastro de bancos. Para ir a tela ");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Lan\u00e7amentos OLD");
		menu.setIcon("lancamentos.svg");
		menu.setEndereco("lancamentos.xhtml");
		menu.setDescricao("Lan\u00e7amentos no formato de a\u00e7\u00e3o e fontes. Para ir a tela ");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Lan\u00e7amentos diversos");
		menu.setIcon("lancamentos.svg");
		menu.setEndereco("lancamentos_diversos.xhtml");
		menu.setDescricao("Lan\u00e7amentos diversos. Para ir a tela ");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Solicita\u00e7\u00e3o de adiantamentos");
		menu.setIcon("lancamentos.svg");
		menu.setEndereco("adiantamentos.xhtml");
		menu.setDescricao("Solicita\u00e7\u00e3o de adiantamentos. Para ir a tela ");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Reclassifica\u00e7\u00e3o");
		menu.setIcon("lancamentos.svg");
		menu.setEndereco("reclassificacao.xhtml");
		menu.setDescricao("Reclassifica\u00e7\u00e3o de lan\u00e7amentos. Para ir a tela ");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Controladoria");
		menu.setIcon("lancamentos.svg");
		menu.setEndereco("recursos_projetos.xhtml");
		menu.setDescricao("Tela de Controladoria de recursos. Para ir a tela ");
		this.menus.add(menu);
		menu = new MenuLateral();
		menu.setNome("Produtos");
		menu.setIcon("lancamentos.svg");
		menu.setEndereco("produtos.xhtml");
		menu.setDescricao("Tela de Produtos. Para ir a tela ");
		this.menus.add(menu);
		return this.menus;
	}
	public void buscarMenu() {
		final List<MenuLateral> listaAuxiliar = new ArrayList<MenuLateral>();
		final MenuLateral auxMenu = new MenuLateral();
		if (this.menus.isEmpty()) {
			this.carregarMenus();
		}
		for (final MenuLateral menuLateral : this.menus) {
			if (menuLateral.getNome().toLowerCase().contains(this.paramBusca.toLowerCase())) {
				listaAuxiliar.add(menuLateral);
			}
		}
		(this.menus = new ArrayList<MenuLateral>()).addAll(listaAuxiliar);
		if (this.paramBusca == null || this.paramBusca.equals("")) {
			this.carregarMenus();
		}
	}

	public List<MenuLateral> getMenus() {
		return this.menus;
	}

	public void setMenus(final List<MenuLateral> menus) {
		this.menus = menus;
	}

	public String getClassMenu1() {
		return this.classMenu1;
	}

	public void setClassMenu1(final String classMenu1) {
		this.classMenu1 = classMenu1;
	}

	public String getClassMenu2() {
		return this.classMenu2;
	}

	public void setClassMenu2(final String classMenu2) {
		this.classMenu2 = classMenu2;
	}

	public String getClassMenu3() {
		return this.classMenu3;
	}

	public void setClassMenu3(final String classMenu3) {
		this.classMenu3 = classMenu3;
	}

	public String getClassMenu4() {
		return this.classMenu4;
	}

	public void setClassMenu4(final String classMenu4) {
		this.classMenu4 = classMenu4;
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
