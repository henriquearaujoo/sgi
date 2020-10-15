package service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Evento;
import model.FontePagadora;
import model.Versao;
import repositorio.FontePagadoraRepositorio;

public class FontePagadoraService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private FontePagadoraRepositorio fontePagadoraRespositorio;

	public FontePagadoraService() {

	}

	public List<FontePagadora> findAll() {
		return fontePagadoraRespositorio.findAll();
	}

	@Transactional
	public void salvar(FontePagadora fontePagadora) {
		fontePagadoraRespositorio.salvar(fontePagadora);
	}

	@Transactional
	public void remover(FontePagadora fontePagadora) {
		fontePagadoraRespositorio.remover(fontePagadora);
	}

	public FontePagadora findByid(Long id) {
		return fontePagadoraRespositorio.findById(id);
	}

}
