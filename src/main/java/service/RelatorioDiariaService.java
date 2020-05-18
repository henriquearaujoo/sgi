package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import model.ContaBancaria;
import model.Gestao;
import model.RelatorioDiaria;
import model.Saving;
import repositorio.RelatorioDiariaRepositorio;
import repositorio.SavingRepositorio;
import util.Filtro;

public class RelatorioDiariaService implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private RelatorioDiariaRepositorio repositorio;

	public RelatorioDiariaService() {
	}

	public List<RelatorioDiaria> getDiaria(Filtro filtro) {
		return repositorio.getDiaria(filtro);
	}

	public List<Gestao> getGestao() {
		return repositorio.getGestao();
	}
	
	public List<ContaBancaria> getFornecedor() {
		return repositorio.getFornecedor();
	}
	
}
