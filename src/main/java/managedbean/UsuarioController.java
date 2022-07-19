package managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Colaborador;
import model.MenuLateral;
import model.Perfil;
import model.User;
import service.SolicitacaoViagemService;
import service.UsuarioService;
import util.MakeMenu;
import util.UsuarioSessao;

@Named(value = "usuario_controllerOLD")
@ViewScoped
public class UsuarioController implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private SolicitacaoViagemService viagemService;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private UsuarioSessao usuarioSessao;
	
	private List<User> listaFiltroUsuarios;
	
	private User usuario = new User() ;

	// metodo para buscar por string parcial o colaborador
	public List<Colaborador> completeColaborador(String s) {
		return viagemService.buscarColaboradores(s);
			
	}
	
	
	// metodos de listagem
	public List<User> listaUsuarios(){
		return usuarioService.getUsuarios();
	}
	
	public List<User> listaUsuarioLogado(){
		List<User> listaUsuario = new ArrayList<>();
		
		if(usuarioSessao.getUsuario().getNomeUsuario().equals("admin")){
			listaUsuario = listaUsuarios();
		}else{
			listaUsuario.add(usuarioSessao.getUsuario());
		}
		
		
		return listaUsuario;
	}
	
	
	public List<Perfil> listaPerfil(){
		return usuarioService.getListPerfil();
	}
	
	
	private MenuLateral menu = new MenuLateral();
	
	
	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}
	
	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuConfiguracoes();
	}
	
	//and medotos de listagem
	
	public String insert(){
		FacesContext context = FacesContext.getCurrentInstance();
		if(usuarioService.salvar(usuario)){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário salvo com Sucesso!", "");
			context.addMessage("msg", msg);
		}else{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar Usuário","");
			context.addMessage("msg", msg);
		}
			
		return "cadastro_usuarios";
	}
	
	//Metodos de Delete
	
	public String delete(User usuario){
		FacesContext context = FacesContext.getCurrentInstance();
		
		if(usuarioService.remover(usuario)){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário salvo com Sucesso!", "");
			context.addMessage("msg", msg);
		}else{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar Usuário","");
			context.addMessage("msg", msg);
		}
		return "cadastro_usuarios";
	}
	
	
	//metodos de redirect
	public void novo(){
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("cadastro_usuarios_edit.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String redirect(){
		return "cadastro_usuarios_edit";
	}
	
	
	
	@PostConstruct
	public void init(){
		String ide = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idUser");
		if((ide != null)&&(!(ide.equals("null"))))
			usuario = usuarioService.getUsuario(new Long(ide));
	}
	
	
	//Getter and Setter ----------------------------------------------------------------------------------------
	public User getUsuario() {
		return usuario;
	}


	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}




	public List<User> getListaFiltroUsuarios() {
		return listaFiltroUsuarios;
	}




	public void setListaFiltroUsuarios(List<User> listaFiltroUsuarios) {
		this.listaFiltroUsuarios = listaFiltroUsuarios;
	}


	public MenuLateral getMenu() {
		return menu;
	}


	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}




}
