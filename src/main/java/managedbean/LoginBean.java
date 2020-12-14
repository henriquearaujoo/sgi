package managedbean;

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

	@Inject
	private UsuarioRepository repository;

	private Gestao gestao;

	private Colaborador colaborador;

	private Email email = new Email();

	@Inject
	private GestaoService gestaoService;

	private List<Colaborador> colaboradorList = new ArrayList<>();

	@Inject
	private ColaboradorService colaboradorService;

	private String emailUsuario;
	private String nomeUsuario;
	private String senhaUsuario;
	private Boolean senhaAuto;
	private String toEmail;
	private String senha = "";
	private String toReturn = "";

	private Boolean teste;

	// public String getWelcome() {
	// return "Bem vindo !";
	// }

	public String getWelcome() {
		return "Bem vindo " + this.sessao.getUsuario().getNomeUsuario() + "!";
	}

	public String getNome() {
		return this.sessao.getUsuario().getNomeUsuario();
	}

	public Boolean verificaGestao(Long id) {
		return teste = gestaoService.verificaGestao(id);

	}

	public String gerarNovaSenha() {
		String[] carct = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h",
				"i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C",
				"D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
				"Y", "Z" };
		for (int x = 0; x < 10; x++) {
			int j = (int) (Math.random() * carct.length);
			senha += carct[j];
		}
		System.out.println("Gerar senha  " + senha);
		return senha;
	}

	@Inject
	private LoginService loginService;

	public void recuperarNovaSenha() throws AddressException, MessagingException {
		FacesContext context = FacesContext.getCurrentInstance();
		usuario = repository.getEmail(toEmail);

		if (usuario != null) {
			gerarNovaSenha();

			loginService.updateSenhaAutomatica(usuario, senha);
			prepararEmail(senha);

			email.setToEmail(toEmail);
			email.EnviarEmailSimples();
//			System.out.println("com shar " + toReturn);
//			System.out.println("com rec " + senha);

			FacesMessage message = new FacesMessage("Um email com uma nova senha foi enviado!");
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			context.addMessage(null, message);

		} else {
			FacesMessage message = new FacesMessage("Email nao existe");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
			System.out.println("derror");
		}
	}

	public void prepararEmail(String senha) {

		email.setFromEmail("comprasgi@fas-amazonas.org");
		email.setSubject("Recuperação de senha");
		email.setSenhaEmail("FAS123fas");
		email.setContent(
				"Sua nova senha para acessar o sistema é: "+ senha +"\n");
	}

	public LoginBean() {
	}

	public String logar() {
		FacesContext context = FacesContext.getCurrentInstance();
		usuario = repository.getUsuario(nomeUsuario, getCriptografada());
//		usuario = loginService.getUsuario(nomeUsuario, senhaUsuario);

		if (usuario != null) {
			sessao.setNomeUsuario(nomeUsuario);
			sessao.setLogado(true);
			sessao.setDataLogin(new Date());
			sessao.setUsuario(usuario);
			sessao.setGestor(gestaoService.verificaGestao(usuario.getColaborador().getId()));

			return "/main/home_new.xhtml?faces-redirect=true";
		} else {
			FacesMessage message = new FacesMessage("Usuario/Senha inválido");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
		}
		return null;
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

	public String getCriptografada() {

		MessageDigest algorithm = null;

		try {
			algorithm = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		byte messageDigest[] = null;

		try {
			messageDigest = algorithm.digest(senhaUsuario.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StringBuilder hexString = new StringBuilder();

		for (byte b : messageDigest) {
			hexString.append(String.format("%02X", 0xFF & b));
		}

		senhaUsuario = hexString.toString();

		return senhaUsuario;
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/main/login.xhtml?faces-redirect=true";
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
	public String redirecionaPage(String refencia) {

		if (!refencia.equals("pagamento") && !refencia.equals("projetos")) {
			if (this.sessao.getUsuario().getPerfil().getDescricao().equals("contabil")) {
				return "page_bloqueio?faces-redirect=true";
			}
		}

		switch (refencia) {
		case "compras":
			return "compras?faces-redirect=true";
		case "home":
			return "home_new?faces-redirect=true";
		case "pagamento":
			if (this.sessao.getUsuario().getPerfil().getDescricao().equals("contabil")) {
				return "gerenciamentoLancamento?faces-redirect=true";
			} else {
				return "pagamento?faces-redirect=true";
			}
		case "logistica":
			return "controle_expedicao?faces-redirect=true";
		case "rh":
			return "cadastro_colaborador?faces-redirect=true";
		case "config":
			return "cadastro_usuarios_att?faces-redirect=true";
		case "liberar":
			return "liberar_projeto?faces-redirect=true";
		case "pbf":
			return "cadastro_familiar?faces-redirect=true";
		case "tuto":
			return "tutorial?faces-redirect=true";
		case "versao":
			return "versao?faces-redirect=true";
		default:
			return "projetos?faces-redirect=true";

		}

	}
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
