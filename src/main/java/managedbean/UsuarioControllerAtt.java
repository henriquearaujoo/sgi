package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.primefaces.PrimeFaces;

import model.Colaborador;
import model.MenuLateral;
import model.Perfil;
import model.User;
import service.SolicitacaoViagemService;
import service.UsuarioService;

import util.Email;
import util.MakeMenu;
import util.UsuarioSessao;
import util.Util;

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

	private Email email;
	
	// pop-up do usuário
	public void popupUsuario() {
		HashMap<String, Object> options = new HashMap<String, Object>();
		options.put("height", "90%");
		options.put("width", "50%");
		options.put("contentWidth", "100%");
		options.put("contentHeight", "100%");
		options.put("resizable", false);
		PrimeFaces.current().dialog().openDynamic("home/usuario.xhtml", options, null);
	}

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
			this.usuarios = usuarioService.getUsuarios();
		} else {
			this.usuarios.add(usuarioService.getUsuario(usuarioSessao.getUsuario().getId()));
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

	public String salvar() throws AddressException, MessagingException {
		try {

			// Checar se já existe usuário com o email informado e não está ativo para
			// edição.
			User checkEmailUser = this.usuarioService.findUsuarioByEmail(this.usuario.getEmail());
			if (checkEmailUser != null && checkEmailUser.getAtivo() == false) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Um usuário já está cadastrado com esse email!",
								"Caso não tenha acesso a essa conta, entre em contato com o suporte do SGI."));
				return null;
			}

			// Se não existir usuário com o email informado

			// Verificar se o id é nulo para usuário novo
			if (this.usuario.getId() == null) {
				// novo usuário: gerar senha aleatória.
				String novaSenha = Util.gerarNovaSenha();
				this.usuario.setSenha(novaSenha);
				this.usuario.setSenhaAuto(true);
				this.usuario.setAtivo(true);

				// Enviar email para o usuário informando a nova senha para acesso.
				this.email = new Email();
				this.email.setFromEmail("comprasgi@fas-amazonas.org");
				this.email.setSubject("Dados para acesso ao SGI");
				this.email.setSenhaEmail("FAS123fas");
				this.email.setContent("Olá, " + this.usuario.getColaborador().getNome()
						+ "! Seja bem-vindo(a) ao SGI.\n\nSeu usuário é: " + this.usuario.getNomeUsuario()
						+ ".\nA senha para acessar o sistema é: " + novaSenha + ".\n\nAtenciosamente,\n\nPGT-FAS");

				this.email.setToEmail(this.usuario.getEmail());
				this.email.EnviarEmailSimples();
			}

			// Se não for usuário novo, basta salvar os dados vindos do formulário
			usuarioService.salvar(usuario);

			// Retornar com sucesso
			return "cadastro_usuarios_att?faces-redirect=true&sucesso=1";
		} catch (Exception e) {
			return "cadastro_usuarios_att?faces-redirect=true&sucesso=0";
		}
	}

	// Metodos de Delete

	public void remover(User usuario) {
		try {
			usuario.setAtivo(false);
			usuarioService.remover(usuario);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário removido com Sucesso!", "");
			FacesContext.getCurrentInstance().addMessage("msg", msg);
			this.carregarUsuarios();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover o Usuário",
					e.getMessage());
			FacesContext.getCurrentInstance().addMessage("msg", msg);
		}
	}

	// metodos de redirect
	public String novo() {
		return "cadastro_usuarios_edit_att?faces-redirect=true";
	}

	public String redirect() {
		return "cadastro_usuarios_edit";
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

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

}
