package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import model.Colaborador;
import model.Gestao;
import repositorio.ColaboradorRepositorio;

public class ServiceUtil implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// TODO: 
	@Inject
	private ColaboradorRepositorio colaboradorRepositorio;
	
	public List<Gestao> findGestao() {
		return colaboradorRepositorio.findGestao();
	}
	
	public List<Colaborador> findColaborador(String s) {
		return colaboradorRepositorio.buscarColaborador(s);
	}
}
