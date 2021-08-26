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
import model.Gestao;
import model.MenuLateral;
import model.Perfil;
import model.User;
import service.ServiceUtil;
import service.SolicitacaoViagemService;
import service.UsuarioService;

import util.Email;
import util.Filtro;
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
	
	@Inject
	private ServiceUtil serviceUtil;

	private List<User> listaFiltroUsuarios;

	private List<User> usuarios = new ArrayList<>();

	@Inject
	private User usuario;

	private Email email;
	
	private Long idUser;
	
	private Filtro filtro = new Filtro();
	
	// pop-up do usuário
//	public void popupUsuario() {
//		usuarioService.popupUsuario();
//	}
	
	public void init() {
		if(idUser != null) {
			this.usuario = usuarioService.findUsuarioById(idUser);
		}
	}
	
	public void setFields() {
		usuario.setEmail(usuario.getColaborador().getEmail());
		usuario.setGestao(usuario.getColaborador().getGestao());
	}
	
	public List<Gestao> getListGestao() {
		return serviceUtil.findGestao();
	}
	
	public List<Gestao> getListGestaoV2(String s) {
		return serviceUtil.findGestaoV2(s);
	}
	
	public List<Colaborador> completeListColaborador(String s) {
		return serviceUtil.findColaborador(s);
	}

	// metodo para buscar por string parcial o colaborador
	public List<Colaborador> completeColaborador(String s) {
		return viagemService.buscarColaboradores(s);
	}
	
	public List<Colaborador> completeColaboradorV2(String s) {
		return usuarioService.completeColaborador(s);
	}
	
	public List<User> completeUser(String s) {
		return usuarioService.completeUser(s);
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
	
	public void filtrar() {
		usuarios = usuarioService.filtrarUsuario(filtro);
	}
	
	public boolean perfil() {
		if(usuarioSessao.getUsuario().getPerfil().getId() == 1 || 
				usuarioSessao.getUsuario().getPerfil().getId() == 5) {
			return true;
		}
		return false;
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
	
	private String senha;
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public boolean editPass() {
		User currentUser = usuarioSessao.getUsuario();
		if (this.usuario.getId().longValue() == currentUser.getId().longValue()) {
			return true;
		}
		return false;
	}

	// and medotos de listagem

	public String salvar() throws AddressException, MessagingException {
		try {

			// Checar se já existe usuário com o email informado e não está ativo para
			// edição.
			User checkEmailUser = this.usuarioService.findUsuarioByEmail(this.usuario);
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
//				this.email = new Email();
//				this.email.setFromEmail("comprasgi@fas-amazonas.org");
//				this.email.setSubject("Dados para acesso ao SGI");
//				this.email.setSenhaEmail("FAS123fas");
//				this.email.setContent("Olá, " + this.usuario.getColaborador().getNome()
//						+ "! Seja bem-vindo(a) ao SGI.\n\nSeu usuário é: " + this.usuario.getNomeUsuario()
//						+ ".\nA senha para acessar o sistema é: " + novaSenha + ".\n\nAtenciosamente,\n\nPGT-FAS");
//
//				this.email.setToEmail(this.usuario.getEmail());
//				this.email.EnviarEmailSimples();
			}

			// Se não for usuário novo, basta salvar os dados vindos do formulário
			usuario.setSenhav4(senha);
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

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

}
