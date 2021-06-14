package managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import model.Colaborador;
import model.Gestao;
import model.User;
import repositorio.UsuarioRepository;
import service.ColaboradorService;
import service.GestaoService;
import service.LoginService;
import util.Email;
import util.UsuarioSessao;

@Named(value = "loginBean")
@RequestScoped
public class LoginBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private User usuario;

	@Inject
	private UsuarioSessao sessao;

	private Gestao gestao;

	private Colaborador colaborador;

	private Email email = new Email();

	@Inject
	private GestaoService gestaoService;
	
	@Inject
	private LoginService loginService;

	@Inject
	private ColaboradorService colaboradorService;

	private List<Colaborador> colaboradorList = new ArrayList<>();

	private String emailUsuario;
	private String nomeUsuario;
	private String senhaUsuario;
	private Boolean senhaAuto;
	private String toEmail;
	private String toReturn = "";

	private Boolean teste;

	// public String getWelcome() {
	// return "Bem vindo !";
	// }
	
	public LoginBean() {
	}

	public String getWelcome() {
		return "Bem vindo " + this.sessao.getUsuario().getNomeUsuario() + "!";
	}

	public String getNome() {
		return this.sessao.getUsuario().getNomeUsuario();
	}

	public Boolean verificaGestao(Long id) {
		return teste = gestaoService.verificaGestao(id);

	}

//	@Inject
//	private UsuarioRepository userRepositorio;
	
	public void recuperarNovaSenha() throws AddressException, MessagingException {
		loginService.getNovaSenha(usuario, toEmail);
	}

	public void logar() throws IOException {
		loginService.getLogin(nomeUsuario, senhaUsuario, gestaoService);
	}
	
	public void redirecionaPage(String page) throws IOException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.getExternalContext().redirect(page+"_new.xhtml");
	}

	// Trabalhando com dialog do gestao//

	// public void editarGestao(User usuario) {
	//
	//
	// carregarColaborador();
	// this.gestao =
	// gestaoService.findByColaborador(usuario.getColaborador().getId());
	//
	// if (gestao instanceof Superintendencia) {
	// gestao.setType("sup");
	// } else if (gestao instanceof Coordenadoria) {
	// gestao.setType("coord");
	// } else if (gestao instanceof Regional) {
	// gestao.setType("reg");
	// }
	//
	// }
	//
	// public List<Colaborador> carregarColaborador() {
	// return colaboradorList = colaboradorService.findAll();
	// }

	// public String salvarHome() {
	// gestao = gestaoService.salvar(gestao);
	// return "home?faces-redirect=true";
	// }

	// Finalizando com dialog de gestao

	
	public void logout() throws IOException {
		loginService.getLogout();
		
//		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
//		return "/main/login.xhtml?faces-redirect=true";
	}

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public UsuarioSessao getSessao() {
		return sessao;
	}

	public void setSessao(UsuarioSessao sessao) {
		this.sessao = sessao;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getSenhaUsuario() {
		return senhaUsuario;
	}

	public void setSenhaUsuario(String senhaUsuario) {
		this.senhaUsuario = senhaUsuario;
	}

	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public List<Colaborador> getColaboradorList() {
		return colaboradorList;
	}

	public void setColaboradorList(List<Colaborador> colaboradorList) {
		this.colaboradorList = colaboradorList;
	}

	public Gestao getGestao() {
		return gestao;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public void setGestao(Gestao gestao) {
		this.gestao = gestao;
	}

	// private List<MenuLateral> menus = new ArrayList<>();
	//

	//
	// public List<MenuLateral> getMenus() {
	// return menus;
	// }

	public Boolean getTeste() {
		return teste;
	}

	public void setTeste(Boolean teste) {
		this.teste = teste;
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public Boolean getSenhaAuto() {
		return senhaAuto;
	}

	public void setSenhaAuto(Boolean senhaAuto) {
		this.senhaAuto = senhaAuto;
	}

}
