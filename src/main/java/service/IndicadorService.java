package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Indicador;
import model.Projeto;
import repositorio.IndicadorRepositorio;

public class IndicadorService implements Serializable{


	private static final long serialVersionUID = 1L;
	
	@Inject
	private IndicadorRepositorio repositorio; 

	public IndicadorService() {
	}

	@Transactional
	public Indicador salvarIndicador(Indicador indicador){
		return repositorio.salvarIndicador(indicador);
	}

	@Transactional
	public void removerIndicador(Indicador indicador){
		repositorio.removerIndicador(indicador);
	}

	public Indicador findIndicadorById(Long id){
		return repositorio.findIndicadorById(id);
	}

	public List<Indicador> findIndicadorByProjeto(Projeto projeto) {
		return repositorio.findIndicadorByProjeto(projeto);
	}


}



