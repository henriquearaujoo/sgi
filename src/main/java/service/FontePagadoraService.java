package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.FontePagadora;
import model.Versao;
import repositorio.FontePagadoraRepositorio;

public class FontePagadoraService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Inject
	private FontePagadoraRepositorio fontePagadoraRespositorio;
	
	public FontePagadoraService() {
		
	}
	
	public List<FontePagadora> findAll(){
		return fontePagadoraRespositorio.findAll();
	}
	
	@Transactional
	public FontePagadora salvar(FontePagadora fontePagadora){
		return fontePagadoraRespositorio.salvarFonte(fontePagadora);
		 
	}

	public FontePagadora findByid(Long id) {
		return fontePagadoraRespositorio.findById(id);
	}

}
