package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Familiar;
import model.Objetivo;
import repositorio.ObjetivoRepositorio;

public class ObjetivoService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ObjetivoRepositorio repositorio;
	
	@Transactional
	public boolean salvar(Objetivo objetivo) {
		try{
			repositorio.salvar(objetivo);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	@Transactional
	public boolean update(Objetivo objetivo) {
		try{
			repositorio.update(objetivo);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public Objetivo getId(Long id) {
		return repositorio.getId(id);
	}

	public List<Objetivo> findObjetivoByIdProjeto(Long id) {
		return repositorio.findObjetivoByIdProjeto(id);
	}
	
	@Transactional
	public void remover(Objetivo objetivo) {
		repositorio.remover(objetivo);
	}

}
