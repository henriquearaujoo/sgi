package service;

import java.io.Serializable;
import java.util.ArrayList;
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
	
	public List<String> fonteAutoComplete(String query) {
		List<FontePagadora> fontes = fontePagadoraRespositorio.getFonteAutoComplete(query);
		List<String> fontesString = new ArrayList<String>();
		fontes.stream().forEach(f -> fontesString.add(f.getNome()));
		return fontesString;
	}
	
	public List<String> cnpjAutoComplete(String query) {
		List<FontePagadora> cnpjs = fontePagadoraRespositorio.getCnpjAutoComplete(query);
		List<String> cnpjString = new ArrayList<>();
		cnpjs.stream().forEach(f -> cnpjString.add(f.getCnpj()));
		return cnpjString;
	}
	
	public List<String> razaoSocialAutoComplete(String query) {
		List<FontePagadora> razaoSocial = fontePagadoraRespositorio.getRazaoSocialAutoComplete(query);
		List<String> razaoSocialString = new ArrayList<>();
		razaoSocial.stream().forEach(f -> razaoSocialString.add(f.getRazaoSocial()));
		return razaoSocialString;
	}
	
	public List<FontePagadora> filtrarParceiro(FontePagadora fontePagadora) {
		return fontePagadoraRespositorio.filtroParceiro(fontePagadora);
	}

}
