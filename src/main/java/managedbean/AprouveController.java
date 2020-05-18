package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.context.PrimeFacesContext;

import model.Aprouve;
import model.MenuLateral;
import model.User;
import repositorio.AprouveRepositorio;
import util.MakeMenu;
import util.UsuarioSessao;


@Named(value = "aprouveController")
@ViewScoped
public class AprouveController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private @Inject Aprouve aprouve;
	private @Inject AprouveRepositorio repositorio;
	
	@Inject
	private UsuarioSessao usuarioSessao;
	private List<Aprouve> lista = new ArrayList<>();  
	private MenuLateral menu = new MenuLateral();
	private List<User> allUsuario = new ArrayList<>();
	private List<Aprouve> listaFiltro;
	 
	
	public UsuarioSessao getUsuarioSessao() {
		return usuarioSessao;
	}

	public void setUsuarioSessao(UsuarioSessao usuarioSessao) {
		this.usuarioSessao = usuarioSessao;
	}

	public List<Aprouve> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<Aprouve> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

	public AprouveController(){}
	
	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage("msg-aprovadores", message);
	}
	
	public void salvar(){
		repositorio.salvar(aprouve);
		carregarLista();
		aprouve = new Aprouve();
		
		addMessage("", "Salvo com Sucesso!", FacesMessage.SEVERITY_INFO);
		updateComponente("form_lista_aprovador:msg-aprovadores");
		updateComponente("form_lista_aprovador:listaAprovadores");
	}
	
	public Boolean verificaPrivilegio() {
		if(usuarioSessao.getUsuario().getPerfil().getId().equals(new Long("1"))) {
			return true;
		}else
			return false;
	}
	
	public void updateComponente(String id) {
		PrimeFaces current = PrimeFaces.current();
		current.ajax().update(id);
	}
	
	public void excecutarScript(String script) {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript(script);
	}
	
	public void openDialog() {
		if(verificaPrivilegio()) {
			updateComponente("form_cadastro_aprovador");
			excecutarScript("PF('dlg_cadastro_aprovador').show()");
		}else {
			addMessage("", "Você não tem permissão para esta ação! ", FacesMessage.SEVERITY_WARN);
			updateComponente("form_lista_aprovador:msg-aprovadores");
		}
		
	}
	
	public void removerAprouve(Aprouve aprouve) {
		if(verificaPrivilegio()) {
			repositorio.remover(aprouve);
			carregarLista();
			addMessage("", "Excluido com Sucesso!", FacesMessage.SEVERITY_INFO);
			updateComponente("form_lista_aprovador:msg-aprovadores");
			updateComponente("form_lista_aprovador:listaAprovadores");
		}else {
			addMessage("", "Você não tem permissão para esta ação! ", FacesMessage.SEVERITY_WARN);
			updateComponente("form_lista_aprovador:msg-aprovadores");
		}
	}
	
	@PostConstruct
	public void carregarLista(){
		lista = repositorio.getTodos();
	}
	

	
	public List<User> completeUsuario(String query) {
		allUsuario = new ArrayList<User>();
		allUsuario = repositorio.getUsuario(query);
		return allUsuario;
	}
	
	public List<MenuLateral> getMenus() {		
		return MakeMenu.getMenuConfigurações();
	}
	
	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}


	public Aprouve getAprouve() {
		return aprouve;
	}


	public void setAprouve(Aprouve aprouve) {
		this.aprouve = aprouve;
	}


	public List<Aprouve> getLista() {
		return lista;
	}


	public void setLista(List<Aprouve> lista) {
		this.lista = lista;
	}


	public MenuLateral getMenu() {
		return menu;
	}


	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}
	
}
