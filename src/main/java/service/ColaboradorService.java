package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import anotacoes.Transactional;
import model.Cargo;
import model.Colaborador;
import model.Endereco;
import model.Gestao;
import model.Localidade;
import repositorio.ColaboradorRepositorio;
import repositorio.GestaoRepositorio;
import util.Filtro;

public class ColaboradorService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	ColaboradorRepositorio colaboradorRepositorio;
	
	@Inject
	private GestaoRepositorio gestaoRepositorio;
	
	public List<Colaborador> findAll(){
		return colaboradorRepositorio.findAll();
	}
	
	public List<Colaborador> findByFiltro(Filtro filtro){
		return colaboradorRepositorio.findByFiltro(filtro);
	}

	public List<Gestao> findGestao() {
		return colaboradorRepositorio.findGestao();
	}
	
	public List<Cargo> findCargo() {
		return colaboradorRepositorio.findCargo();
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
	public Boolean salvar(Colaborador colaborador){
		colaboradorRepositorio.salvar(colaborador);
		return true;
		
	}
	
	

	public Colaborador findColaboradorById(Long colaborador) {
		return colaboradorRepositorio.findColaboradorById(colaborador);
	}
	
	public void remove(Colaborador colaborador){
		colaboradorRepositorio.remover(colaborador);
	}

	public List<Gestao> getGestaoByQuery(String query) {
		return gestaoRepositorio.buscarGestaoAutocomplete(query);
	}

	
}