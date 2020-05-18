package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import anotacoes.Transactional;
import model.ColaboradorVI;
import model.Endereco;
import model.Gestao;
import model.Localidade;
import repositorio.ColaboradorVIRepositorio;
import repositorio.GestaoRepositorio;
import util.Filtro;

public class ColaboradorVIService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	ColaboradorVIRepositorio colaboradorRepositorio;
	
	@Inject
	private GestaoRepositorio gestaoRepositorio;
	
	public List<ColaboradorVI> findAll(Filtro filtro){
		return colaboradorRepositorio.findAll(filtro);
	}
	
	public List<ColaboradorVI> findByFiltro(Filtro filtro){
		return colaboradorRepositorio.findByFiltro(filtro);
	}

	public List<Gestao> findGestao() {
		return colaboradorRepositorio.findGestao();
	}

	public List<Localidade> findLocalidade() {
		return colaboradorRepositorio.findLocalidade();
	}

	public Endereco findEnderecoByCep(String cep) {
		Endereco endereco = new Endereco();
		try{
			return colaboradorRepositorio.findEnderecoByCep(cep);
		}catch(NoResultException e){
			endereco.setCep(cep);
			return endereco;
		}
	}
	
	@Transactional
	public Boolean salvar(ColaboradorVI colaborador){
		colaboradorRepositorio.salvar(colaborador);
		return true;
		
	}
	
	

	public ColaboradorVI findColaboradorById(Long colaborador) {
		return colaboradorRepositorio.findColaboradorById(colaborador);
	}
	
	public void remove(ColaboradorVI colaborador){
		colaboradorRepositorio.remover(colaborador);
	}

	public List<Gestao> getGestaoByQuery(String query) {
		return gestaoRepositorio.buscarGestaoAutocomplete(query);
	}

	
}