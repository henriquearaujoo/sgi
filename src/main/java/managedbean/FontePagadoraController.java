package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.FontePagadora;
import service.FontePagadoraService;
import util.Filtro;

@Named(value = "fonte_pagadora_controller")
@ViewScoped
public class FontePagadoraController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FontePagadoraService fontePagadoraService;

	private FontePagadora fontePagadora = new FontePagadora();
	
	private Filtro filtro = new Filtro();

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
			return "parceiros?faces-redirect=true";
		} catch (Exception e) {
			return "parceiros?faces-redirect=true&sucesso=0";
		}
	}
	
	public void filtrarFonte() {
		this.fontes = fontePagadoraService.filtrarParceiro(fontePagadora);
	}
	
	public void limparFiltro() {
		fontePagadora = new FontePagadora();
		this.fontes = fontePagadoraService.findAll();
	}
	
	public List<FontePagadora> fonteAutoComplete(String query) {
		return fontePagadoraService.fonteAutoComplete(query);
	}
	
	public List<FontePagadora> cnpjAutoComplete(String query) {
		return fontePagadoraService.cnpjAutoComplete(query);
	}
	
	public List<FontePagadora> razaoSocialAutoComplete(String query) {
		return fontePagadoraService.razaoSocialAutoComplete(query);
	}

	private String attributePartner = "";
	
	public void remover() {
		try {
			fontePagadoraService.remover(fontePagadora);
			FacesContext.getCurrentInstance().addMessage("messages",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Fonte removida com sucesso!"));
			this.initListagem();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro!",
					attributePartner + " não pode ser excluído, existem planejamentos associados a ele."));
		}
	}
	
	public void removeListener(ActionEvent event) {
		String p = (String) event.getComponent().getAttributes().get("partner");
		this.attributePartner = p;
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

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

}
