package service;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import anotacoes.Transactional;
import model.Gestao;
import model.Localidade;
import model.PlanoDeTrabalho;
import model.Projeto;
import model.UnidadeConservacao;
import model.User;
import model.UserPlanoTrabalho;
import model.UserProjeto;
import repositorio.GestaoRepositorio;
import repositorio.LocalRepositorio;
import repositorio.PlanoTrabalhoRepositorio;
import repositorio.ProjetoRepositorio;
import repositorio.UsuarioRepository;
import util.Filtro;

/**
 * Autor: Italo Almeida Classe: Gestao de Projeto Objetivo: Gerir a regra de
 * negocio referente a instancia do objeto
 */

public class PlanoDeTrabalhoService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PlanoTrabalhoRepositorio repositorio;

	@Inject
	private ProjetoRepositorio projetoRepositorio;

	@Inject
	private GestaoRepositorio gestaoRepositorio;

	@Inject
	private LocalRepositorio localRepositorio;

	@Inject
	private UsuarioRepository usuarioRepositorio;

	public PlanoDeTrabalhoService() {
	}

	public List<User> getUsuario(String nome) {
		return usuarioRepositorio.getUsuario(nome);
	}

	public List<Localidade> buscaLocalidade(String s) {
		return localRepositorio.buscarLocalidade(s);
	}

	public List<UnidadeConservacao> buscarUC(String s) {
		return localRepositorio.buscarUC(s);
	}

	public List<Projeto> getProjetoByPlano(Long id) {
		return projetoRepositorio.getProjetoByPlano(id);
	}

	public List<Gestao> getGestaoAutoComplete(String query) {
		return gestaoRepositorio.buscarGestaoAutocomplete(query);
	}
	
	
	@Transactional
	public Boolean excluirUsuarioDoPlano(UserPlanoTrabalho usuarioPlano){	
		repositorio.removerUsuarioPlano(usuarioPlano);
		return true;
	}
	

	@Transactional
	public void salvar(PlanoDeTrabalho plano, User usuario) {
		repositorio.salvar(plano, usuario);
	}
	
	@Transactional
	public Boolean salvarUsuarioNoPlano(UserPlanoTrabalho usuarioPlano){
		if(repositorio.verificarUsuarioNoProjeto(usuarioPlano) > 0){
			addMessage("", "Esse usuário já está no projeto.", FacesMessage.SEVERITY_ERROR);
			return false;
			
		}else{
			repositorio.salvarUsuarioNoProjeto(usuarioPlano);
			return true;
		}
	}
	
	public List<UserPlanoTrabalho> getUsuariosProjetos(PlanoDeTrabalho planoDeTrabalho){
		return repositorio.getUsuariosProjetos(planoDeTrabalho);
	}
	
	
	public void addMessage(String summary, String detail, Severity severity) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

	public void remover(PlanoDeTrabalho plano) {
		repositorio.remover(plano);
	}

	public PlanoDeTrabalho findPlanoById(Long id) {
		return repositorio.getPlanoPorId(id);
	}

	public List<PlanoDeTrabalho> getPLanos(Filtro filtro, User usuario) {
		return repositorio.getPlanosFiltroPorUsuario(filtro, usuario);
	}

	@Transactional
	public UnidadeConservacao findUcById(Long id) {
		return localRepositorio.findByIdUnidadeDeConservacao(id);
	}

}
