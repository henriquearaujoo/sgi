package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import model.ContaBancaria;
import model.Gestao;
import model.Saving;
import repositorio.SavingRepositorio;
import util.Filtro;

public class SavingService implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private SavingRepositorio repositorio;

	public SavingService() {
	}

	public List<Saving> getSaving(Filtro filtro) {
		return repositorio.getSaving(filtro);
	}

	public List<Gestao> getGestao() {
		return repositorio.getGestao();
	}
	
	public List<ContaBancaria> getFornecedor() {
		return repositorio.getFornecedor();
	}
	
}
