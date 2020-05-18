package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Metas;
import model.Objetivo;
import repositorio.MetasRepositorio;

public class MetasService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private MetasRepositorio repositorio;
	
	
	@Transactional
	public boolean salvar(Metas metas) {
		try{
			repositorio.salvar(metas);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	@Transactional
	public boolean update(Metas metas) {
		try{
			repositorio.update(metas);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public Metas findById(Long id) {
		return repositorio.findById(id);
	}
	
	public List<Metas> findMetasByObjetivo(Objetivo objetivo) {
		return repositorio.findMetasByObjetivo(objetivo);
		
	}

	public List<Metas> findMetasByIdProjeto(Long id) {
		return repositorio.findMetasByIdProjeto(id);
	}
	
	public void remover(Metas metas) {
		repositorio.remover(metas);
	}

}
