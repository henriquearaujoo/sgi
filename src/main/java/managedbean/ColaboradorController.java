package managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;

import com.sun.faces.config.InitFacesContext;

import anotacoes.Transactional;
import exception.NegocioException;
import model.Cargo;
import model.Colaborador;
import model.ContaBancaria;
import model.Endereco;
import model.EstadoCivil;
import model.Gestao;
import model.Localidade;
import model.MenuLateral;
import service.ColaboradorService;
import service.EnderecoService;
import util.Arquivo;
import util.DataUtil;
import util.Filtro;
import util.UsuarioSessao;

@Named(value = "colaboradorController")
@ViewScoped
public class ColaboradorController implements Serializable {

	/**
	 * 
	 */
	
	public ColaboradorController(){
		
	}
	
	private static final long serialVersionUID = 1L;

	@Inject
	ColaboradorService colaboradorService;
	
	@Inject
	EnderecoService enderecoService;
	private @Inject UsuarioSessao usuarioSessao;
	private Long idInitColaborador;
	private Colaborador colaborador = new Colaborador();
	private Endereco endereco = new Endereco();
	private MenuLateral menu = new MenuLateral();
	private Filtro filtro = new Filtro();
	private List<Colaborador> listaColaborador;
	
	
	public void init() {
		if(idInitColaborador != null)
			this.colaborador = colaboradorService.findColaboradorById(idInitColaborador);
	}
	
	public void filtrar() {
		listaColaborador = new ArrayList<Colaborador>();
		listaColaborador = colaboradorService.findByFiltro(filtro);
	}
	
	public void initListagem() {
		this.listaColaborador = colaboradorService.findAll();
	}
	
	
	public List<Colaborador> getListaColaborador() {
		return listaColaborador;
	}

	public void setListaColaborador(List<Colaborador> listaColaborador) {
		this.listaColaborador = listaColaborador;
	}

	public Filtro getFiltro() {
		return filtro;
	}



	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public void limpar() {
		filtro = new Filtro();
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
//metodos de percistencia
	
	public String salvar() throws NegocioException {
//		colaborador.getArquivo().setNome(colaborador.getNome());
		colaboradorService.salvar(colaborador);
		return "cadastro_colaborador?faces-redirect=true";
	}
	
	
	@Transactional
	public String excluir() throws IOException {
		if (colaborador.getId() == null) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("cadastro_colaborador.xhtml");
		} else {
			colaboradorService.remove(colaborador);
			addMessage("", "Excluido com sucesso", FacesMessage.SEVERITY_INFO);
		}
		return "cadastro_colaborador?faces-redirect=true";
	}
	
	public void editar(Colaborador colaborador){
		this.colaborador = colaboradorService.findColaboradorById(colaborador.getId());
		//System.err.println(colaborador.getCpf());
	}
//Metodos de List
	public EstadoCivil[] listarEstadosCivis(){
		return EstadoCivil.values();
	}
	public List<Gestao> listaGestao() {
		return colaboradorService.findGestao();
	}
	
	public List<Cargo> listaCargo() {
		return colaboradorService.findCargo();
	}
	
	public List<Gestao> completeGestao(String query) {
		return colaboradorService.getGestaoByQuery(query);
	}
	
	public List<Localidade> listaLocalidade() {
		return colaboradorService.findLocalidade();
	}

	public List<Colaborador> findAll() {
		if (usuarioSessao.getNomeUsuario().equals("ana.menezes") || usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")) {
			return colaboradorService.findAll();
		}else{
			return new ArrayList<>();
		}	
	}
	
	public List<Colaborador> findByFiltro() {
		if (usuarioSessao.getNomeUsuario().equals("ana.menezes") || usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")) {
			return colaboradorService.findAll();
		}else{
			return new ArrayList<>();
		}	
	}
	
	
//upload de arquivo
//	@Inject
//	ArquivoResource arqui;
	
	public void handleFileUpload(FileUploadEvent event) throws IOException {		 
		arq.setArquivo(event.getFile().getContents());
		arq.setNome(colaborador.getNome());
		colaborador.setArquivo(arq);
//		arqui.anexo(new Long(2));
        
    }
	
//Metodos Ref. Menu lateral
	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public List<MenuLateral> getMenus() {

		List<MenuLateral> lista = new ArrayList<>();
		MenuLateral menu = new MenuLateral("Colaboradores", "cadastro_colaborador","fa fa-user-plus");
		//MenuLateral menu1 = new MenuLateral("Usuários", "cadastro_usuarios","fa fa-user-plus");
		lista.add(menu);
		//lista.add(menu1);

		return lista;
	}
//Metodo Ref. Wizard
	public String onFlowProcess(FlowEvent event) {

		return event.getNewStep();
	}

	
	
//Metodos Gets e Sets	
	
	private Arquivo arq = new Arquivo();

	public Arquivo getArq() {
		return arq;
	}

	public void setArq(Arquivo arq) {
		this.arq = arq;
	}
	
	public Long getIdInitColaborador() {
		return idInitColaborador;
	}

	public void setIdInitColaborador(Long idInitColaborador) {
		this.idInitColaborador = idInitColaborador;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}
	
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}
}