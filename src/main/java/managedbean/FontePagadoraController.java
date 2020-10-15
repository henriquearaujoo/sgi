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

	@Inject
	private String edicaoSucesso;

	public String getEdicaoSucesso() {
		return edicaoSucesso;
	}

	public void setEdicaoSucesso(String edicaoSucesso) {
		this.edicaoSucesso = edicaoSucesso;
	}

	private List<FontePagadora> fontes = new ArrayList<>();

	public void initListagem() {
		this.fontes = fontePagadoraService.findAll();
		if (!this.edicaoSucesso.isEmpty() && this.edicaoSucesso.equals("1")) {
			FacesContext.getCurrentInstance().addMessage("messages",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Fonte alterada com sucesso!"));
		} else {
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro!", "Não foi possível alterar a fonte desejada."));
		}
	}

	public List<FontePagadora> findAll() {
		return fontePagadoraService.findAll();
	}

	public String salvar() {
		try {
			fontePagadoraService.salvar(this.fontePagadora);
			return "fonte_pagadora?faces-redirect=true";
		} catch (Exception e) {
			return "fonte_pagadora_cadastro";
		}
	}

	public void remover() {
		try {
			fontePagadoraService.remover(fontePagadora);
			FacesContext.getCurrentInstance().addMessage("messages",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Fonte removida com sucesso!"));
			this.fontes.remove(fontePagadora);
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
