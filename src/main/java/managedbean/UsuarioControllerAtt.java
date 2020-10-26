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

	@Inject
	private User usuario;

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

	public String salvar() {
		try {
			usuarioService.salvar(usuario);
			return "cadastro_usuarios_att?faces-redirect=true&sucesso=1";
		} catch (Exception e) {
			return "cadastro_usuarios_att?faces-redirect=true&sucesso=0";
		}
	}

	// Metodos de Delete

	public void delete(User usuario) {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			usuarioService.remover(usuario);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário removido com Sucesso!", "");
			context.addMessage("msg", msg);
			this.carregarUsuarios();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover o Usuário", "");
			context.addMessage("msg", msg);
		}
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
