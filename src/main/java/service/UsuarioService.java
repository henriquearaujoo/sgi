package service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.PrimeFaces;

import anotacoes.Transactional;
import model.Colaborador;
import model.Perfil;
import model.Projeto;
import model.User;
import model.UserProjeto;
import repositorio.ColaboradorRepositorio;
import repositorio.UsuarioRepository;
import util.Filtro;

public class UsuarioService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioRepository repositorio;

	@Inject
	private ColaboradorRepositorio colaboradorRepositorio;

	public UsuarioService() {

	}

	public Perfil getPerfil(Long id) {
		return repositorio.getPerfil(id);
	}

	public List<Perfil> getListPerfil() {
		return repositorio.getListPerfil();
	}

	@Transactional
	public Boolean Salvar(User usuario) {
		try {
			usuario.setAtivo(true);
			repositorio.salvar(usuario);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public List<User> getUsuarios() {
		return repositorio.getUsuarios(new Filtro());
	}

	public List<Colaborador> getColaborador() {
		return colaboradorRepositorio.findAll();
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public User getUsuario(Long id) {
		return repositorio.getUsuarioById(id);
	}
	
	public User findUsuarioById(Long id) {
		return repositorio.getUsuario(id);
	}

	@Transactional
	public boolean salvar(User usuario) {
		try {
			usuario.setAtivo(true);
			repositorio.salvar(usuario);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Transactional
	public boolean update(User usuario) {
		try {
			repositorio.salvar(usuario);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Transactional
	public boolean remover(User usuario) {
		try {
			repositorio.remover(usuario);
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public boolean remover(UserProjeto usuarioProjeto) {
		try {
			repositorio.remover(usuarioProjeto);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Transactional
	public boolean updateSenha(User usuario, String senha) {
		try {
			usuario.setSenhaAuto(false);
			usuario.setSenhav4(senha);
			repositorio.updateSenha(usuario);
			addMessage("", "Senha alterada com sucesso", FacesMessage.SEVERITY_INFO);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<User> findUsuarioByProjeto(Projeto projetoSelecionado) {
		return repositorio.findUsuarioByProjeto(projetoSelecionado);
	}
	
	public User findUsuarioByEmail(String email) {
		return repositorio.findUsuarioByEmail(email);
	}

	public UserProjeto findUsuarioProjeto(Projeto projetoSelecionado, User usuario) {
		return repositorio.findUsuarioProjeto(projetoSelecionado, usuario);
	}
	
//	public void popupUsuario() {
//		HashMap<String, Object> options = new HashMap<String, Object>();
//		options.put("height", "98%");
//		options.put("width", "35%");
//		options.put("contentWidth", "100%");
//		options.put("contentHeight", "100%");
//		options.put("resizable", false);
//		options.put("draggable", true);
//		options.put("showEffect", "clip");
//		PrimeFaces.current().dialog().openDynamic("home/usuario.xhtml", options, null);
//	}

}
