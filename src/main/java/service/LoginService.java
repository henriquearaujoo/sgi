package service;

import java.io.Serializable;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.User;
import repositorio.UsuarioRepository;

public class LoginService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioRepository userRepositorio;
	
	public LoginService() {
	}

	@Transactional
	public void updateSenhaAutomatica(User usuario, String senha) {
		usuario.setSenhav4(senha);
		usuario.setSenhaAuto(true);	
		userRepositorio.updateSenha(usuario);
	}

	@Transactional
	public void cancelarTrocaDesenha(User usuario, Boolean verNovamente) {
		if (verNovamente != null) {
			if (verNovamente) {
				usuario.setSenhaAuto(!verNovamente);
				userRepositorio.updateSenha(usuario);
			}
		}

	}
	
	public User getUsuario(String nome, String senha){
		return userRepositorio.getUsuario(nome, senha);
	}

}
