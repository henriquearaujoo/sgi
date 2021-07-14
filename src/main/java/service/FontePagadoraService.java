package service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
		Long totalFontes = fontePagadoraRespositorio.maxFontes();
		List<FontePagadora> fonte = fontePagadoraRespositorio.findAll();
		for(FontePagadora f : fonte) {
			if(fontePagadora.getNome() == f.getNome()) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "FONTE JÁ EXISTENTE");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} else {
				fontePagadora.setId(totalFontes + 1L);
				fontePagadoraRespositorio.salvar(fontePagadora);
				break;
			}
		}
	}

	@Transactional
	public void remover(FontePagadora fontePagadora) {
		fontePagadoraRespositorio.remover(fontePagadora);
	}

	public FontePagadora findByid(Long id) {
		return fontePagadoraRespositorio.findById(id);
	}

}
