package service;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

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
	
	
	
	public UsuarioService(){
		
	}
	
	public Perfil getPerfil(Long id){
		return repositorio.getPerfil(id);
	}
	
	public List<Perfil> getListPerfil(){
		return repositorio.getListPerfil();
	}
	
	@Transactional
	public Boolean Salvar(User usuario){
		try{
			usuario.setAtivo(true);
			repositorio.salvar(usuario);
			return true;
		}catch(Exception e){
			return false;
		}
		
		
	}
	
	
	
	public List<User> getUsuarios(){
		return repositorio.getUsuarios(new Filtro());
	}
	
	public List<Colaborador> getColaborador(){
		return colaboradorRepositorio.findAll();
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public User getUsuario(Long id) {
		return repositorio.getUsuarioById(id);
	}
	
	@Transactional
	public boolean salvar(User usuario) {
		try{
			usuario.setAtivo(true);
			repositorio.salvar(usuario);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	@Transactional
	public boolean update(User usuario) {
		try{
			repositorio.update(usuario);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	
	
	@Transactional
	public boolean updateSenha(User usuario, String senha) {
		try{
			usuario.setSenhaAuto(false);
			usuario.setSenha(senha);
			repositorio.updateSenha(usuario);
			addMessage("", "Senha alterada com sucesso", FacesMessage.SEVERITY_INFO);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public List<User> findUsuarioByProjeto(Projeto projetoSelecionado) {
		return repositorio.findUsuarioByProjeto(projetoSelecionado);
	}
	
	public UserProjeto findUsuarioProjeto(Projeto projetoSelecionado, User usuario) {
		return repositorio.findUsuarioProjeto(projetoSelecionado, usuario);
	}
	
	@Transactional
	public void remover(UserProjeto usuarioProjeto) {
		repositorio.remover(usuarioProjeto);
		
	}

	
	
}
