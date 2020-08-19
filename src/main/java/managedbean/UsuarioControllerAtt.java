package managedbean;

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

@Named(value = "usuario_controller")
@ViewScoped
public class UsuarioControllerAtt implements Serializable {

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

	private List<User> usuarios = new ArrayList<>();

	private User usuario = new User();

	// metodo para buscar por string parcial o colaborador
	public List<Colaborador> completeColaborador(String s) {
		return viagemService.buscarColaboradores(s);
	}

	// metodos de listagem
	public List<User> listaUsuarios() {
		return usuarioService.getUsuarios();
	}

	public boolean poderEditar() {
		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin"))
			return true;

		return false;
	}

	public List<User> listaUsuarioLogado() {
		List<User> listaUsuario = new ArrayList<>();

		if (usuarioSessao.getUsuario().getNomeUsuario().equals("admin")) {
			listaUsuario = listaUsuarios();
		} else {
			listaUsuario.add(usuarioSessao.getUsuario());
		}

		return listaUsuario;
	}

//	public boolean poderEditar() {
//		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")) {
//			return true;
//		} else {
//			return false;
//		}
//	}

	public void carregarUsuarios() {
		if (usuarioSessao.getUsuario().getNomeUsuario().equals("admin")) {
			usuarios = usuarioService.getUsuarios();
		} else {
			usuarios.add(usuarioSessao.getUsuario());
		}
	}

	public List<Perfil> listaPerfil() {
		return usuarioService.getListPerfil();
	}

	private MenuLateral menu = new MenuLateral();

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public String listagemUsuarios() {
		return "cadastro_usuarios_att?faces-redirect=true";
	}

	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuConfigurações();
	}

	// and medotos de listagem

	public String insert() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (usuarioService.salvar(usuario)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário salvo com Sucesso!", "");
			context.addMessage("msg", msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar Usuário", "");
			context.addMessage("msg", msg);
			return "";
		}

		return "cadastro_usuarios_att?faces-redirect=true";
	}

	// Metodos de Delete

	public String delete(User usuario) {
		FacesContext context = FacesContext.getCurrentInstance();

		if (usuarioService.update(usuario)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário salvo com Sucesso!", "");
			context.addMessage("msg", msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar Usuário", "");
			context.addMessage("msg", msg);
		}
		return "cadastro_usuarios";
	}

	// metodos de redirect
	public String novo() {
		return "cadastro_usuarios_edit_att?faces-redirect=true";
	}

	public String redirect() {
		return "cadastro_usuarios_edit";
	}

	@PostConstruct
	public void init() {
		carregarUsuarios();
	}

	// Getter and Setter
	// ----------------------------------------------------------------------------------------
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

	public List<User> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<User> usuarios) {
		this.usuarios = usuarios;
	}

}
