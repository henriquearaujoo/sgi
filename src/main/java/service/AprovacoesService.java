package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import model.Colaborador;
import model.LancamentoAuxiliar;
import model.User;
import repositorio.AprovacoesRepositorio;
import util.Filtro;

public class AprovacoesService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Inject
	private AprovacoesRepositorio aprovacoesRepositorio;
	

	public List<LancamentoAuxiliar> filtrar(Filtro filtro) {
		return aprovacoesRepositorio.filtar(filtro);
	}

	public void reprovaLancamentos(List<LancamentoAuxiliar> listLancamento) {
		// TODO Auto-generated method stub
	}

	public void aprovarLancamentos(List<LancamentoAuxiliar> listLancamento) {
		// TODO Auto-generated method stub
		
	}
	
	public List<Colaborador> getColaboradorAutoComplete(String query) {
		return aprovacoesRepositorio.getColaboradorAutoComplete(query);
	}

	public void newApprover(Filtro filtro) {
		
	}
	
	public Boolean verificaAprovacoesPendentes(User usuario) {
		return  null;
	}	
}
