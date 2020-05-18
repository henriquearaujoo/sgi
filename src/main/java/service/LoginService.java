package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.Query;

import anotacoes.Transactional;
import model.Indicador;
import model.Projeto;
import model.User;
import repositorio.IndicadorRepositorio;
import repositorio.UsuarioRepository;

public class LoginService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioRepository userRepositorio;
	
	public LoginService() {
	}

	@Transactional
	public void updateSenhaAutomatica(User usuario, String senha) {
		usuario.setSenha(senha);
		usuario.setSenhaAuto(true);
		userRepositorio.update(usuario);
	}

	@Transactional
	public void cancelarTrocaDesenha(User usuario, Boolean verNovamente) {
		if (verNovamente != null) {
			if (verNovamente) {
				usuario.setSenhaAuto(!verNovamente);
				userRepositorio.update(usuario);
			}
		}

	}
	
	public User getUsuario(String nome, String senha){
		return userRepositorio.getUsuario(nome, senha);
	}

}
