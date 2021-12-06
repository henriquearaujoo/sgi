package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.FontePagadora;
import repositorio.FontePagadoraRepositorio;
import util.Filtro;

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
	
	public List<FontePagadora> fonteAutoComplete(String query) {
		return fontePagadoraRespositorio.getFonteAutoComplete(query);
	}
	
	public List<FontePagadora> cnpjAutoComplete(String query) {
		return fontePagadoraRespositorio.getCnpjAutoComplete(query);
	}
	
	public List<FontePagadora> razaoSocialAutoComplete(String query) {
		return fontePagadoraRespositorio.getRazaoSocialAutoComplete(query);
	}
	
	public List<FontePagadora> filtrarParceiro(FontePagadora fontePagadora) {
		return fontePagadoraRespositorio.filtroParceiro(fontePagadora);
	}

}
