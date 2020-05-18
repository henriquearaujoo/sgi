package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.FontePagadora;
import service.FontePagadoraService;

@Named(value = "fonte_pagadora_controller")
@ViewScoped
public class FontePagadoraController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private FontePagadoraService fontePagadoraService;
	
	private FontePagadora fontePagadora = new FontePagadora();
	
	private String id;
	
	public void init() {
		if(id != null) 
		fontePagadora = fontePagadoraService.findByid(new Long(id));
	}
	
	public List<FontePagadora> findAll(){
		return fontePagadoraService.findAll();
	}
	
	public String salvar() {
			fontePagadora = fontePagadoraService.salvar(fontePagadora);
			return "fonte_pagadora?faces-redirect=true";
		}

	public FontePagadora getFontePagadora() {
		return fontePagadora;
	}

	public void setFontePagadora(FontePagadora fontePagadora) {
		this.fontePagadora = fontePagadora;
	}
	
	
	
	

}
