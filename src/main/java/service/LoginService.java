package service;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import anotacoes.Transactional;
import model.User;
import repositorio.UsuarioRepository;
import util.Email;
import util.UsuarioSessao;

public class LoginService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioRepository userRepositorio;

	@Inject
	private UsuarioSessao sessao;

	@Inject
	private User usuario;

	Email email = new Email();

//	private Email email = new Email();
	private String senha = "";

	public LoginService() {
	}
	
	//mTXaPMXLdB

	public void updateSenhaAutomatica(User usuario, String senha) {
		usuario.setSenhav4(senha);
		usuario.setSenhaAuto(true);	
		userRepositorio.updateSenha(usuario);
	}

	@Transactional
	public void cancelarTrocaDeSenha(User usuario, Boolean verNovamente) {
		if (verNovamente != null) {
			if (verNovamente) {
				usuario.setSenhaAuto(!verNovamente);
				userRepositorio.updateSenha(usuario);
			}
		}
	}

	public User getUsuario(String nome, String senha) {
		return userRepositorio.getUsuario(nome, senha);
	}

	public void getLogin(String nomeUsuario, String senhaUsuario, GestaoService gestaoService) throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();

		String senhaCriptografada = getCriptografada(senhaUsuario);
		usuario = userRepositorio.getUsuario(nomeUsuario, senhaCriptografada);
//		usuario = getUsuario(nomeUsuario, senhaUsuario);

		if (usuario != null) {
			sessao.setNomeUsuario(nomeUsuario);
			sessao.setLogado(true);
			sessao.setDataLogin(new Date());
			sessao.setUsuario(usuario);
			sessao.setGestor(gestaoService.verificaGestao(usuario.getColaborador().getId()));

			context.getExternalContext().redirect("home_new.xhtml");
			// return "/main/home_new.xhtml?faces-redirect=true";
		} else {
			FacesMessage message = new FacesMessage("Usuario/Senha inválido");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
//			return null;
		}

	}

	public void getNovaSenha(User user, String toEmail) throws AddressException, MessagingException {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		usuario = userRepositorio.getEmail(toEmail);
		
		if (usuario == null) {
			message.setSummary("Insira um email válido!");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
		} else {
			final String novaSenha = gerarNovaSenha();

			updateSenhaAutomatica(usuario, novaSenha);
			prepararEmail(novaSenha);

			email.setToEmail(toEmail);
			email.EnviarEmailSimples();
			
			message.setSummary("Um email com uma nova senha foi enviado!");
			message.setSeverity(FacesMessage.SEVERITY_INFO);
			context.addMessage(null, message);
		}
	}

	public void getLogout() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();

		context.getExternalContext().redirect("login.xhtml");
	}

	public String getCriptografada(String senhaUsuario) {

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
			e.printStackTrace();
		}

		StringBuilder hexString = new StringBuilder();

		for (byte b : messageDigest) {
			hexString.append(String.format("%02X", 0xFF & b));
		}

		senhaUsuario = hexString.toString();

		return senhaUsuario;
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

	public Email prepararEmail(String senha) {

		email.setFromEmail("comprasgi@fas-amazonas.org");
		email.setSubject("Recuperação de senha");
		email.setSenhaEmail("FAS123fas");
		email.setContent("Sua nova senha para acessar o sistema é: " + senha + "\n");
		return email;
	}

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
}
