package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Utilidade;
import repositorio.UtilidadeRepositorio;
import util.Filtro;

public class UtilidadeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UtilidadeRepositorio repositorio;

	public UtilidadeService() {
	}

	@Transactional
	public Utilidade salvar(Utilidade utilidade) {
		return repositorio.salvar(utilidade);
	}

	@Transactional
	public void remover(Utilidade utilidade) {
		repositorio.remover(utilidade);
	}

	public List<Utilidade> getAll(Filtro filtro) {
		return repositorio.getAll(filtro);
	}

	public Utilidade findById(Long id) {
		return repositorio.findById(id);
	}

	public List<Utilidade> getResumeList() {
		return repositorio.getResumeList();
	}

}
