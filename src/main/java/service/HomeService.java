package service;

import java.io.Serializable;

import javax.inject.Inject;

import repositorio.AutorizacaoRepositorio;

public class HomeService implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HomeService() {}
	
	@Inject
	private AutorizacaoRepositorio autorizacaoRepositorio;
	
	public boolean verificarSolicitacoes(Long id) {
		return autorizacaoRepositorio.verificaSolicitacoes(id);
	}
	
	
	
	
}
