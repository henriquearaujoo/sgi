package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import model.FontePagadora;
import service.FontePagadoraService;

@Named(value = "fonte_pagadora_controller")
@ViewScoped
public class FontePagadoraController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FontePagadoraService fontePagadoraService;

	@Inject
	private FontePagadora fontePagadora = new FontePagadora();

	private List<FontePagadora> fontes = new ArrayList<>();

	public void initListagem() {
		this.fontes = fontePagadoraService.findAll();
	}

	public List<FontePagadora> findAll() {
		return fontePagadoraService.findAll();
	}

	public String salvar() {
		try {
			fontePagadoraService.salvar(this.fontePagadora);
			return "fonte_pagadora?faces-redirect=true";
		} catch (Exception e) {
			return "fonte_pagadora?faces-redirect=true&sucesso=0";
		}
	}

	public void remover() {
		try {
			fontePagadoraService.remover(fontePagadora);
			FacesContext.getCurrentInstance().addMessage("messages",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Fonte removida com sucesso!"));
			this.initListagem();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro!",
					"Não foi possível remover a fonte desejada. Provavelmente, ela ainda está associada a algum orçamento."));
		}
	}

	public FontePagadora getFontePagadora() {
		return fontePagadora;
	}

	public void setFontePagadora(FontePagadora fontePagadora) {
		this.fontePagadora = fontePagadora;
	}

	public List<FontePagadora> getFontes() {
		return this.fontes;
	}

	public void setFontes(List<FontePagadora> fontes) {
		this.fontes = fontes;
	}

}
