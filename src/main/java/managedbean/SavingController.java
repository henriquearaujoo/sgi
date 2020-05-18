package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Componente;
import model.ContaBancaria;
import model.Gestao;
import model.Saving;
import service.SavingService;
import util.Filtro;

@Named(value = "saving_controller")
@ViewScoped
public class SavingController implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	
	private List<Saving> lista = new ArrayList<>();
	private List<Gestao> listaGestao = new ArrayList<>();
	private List<ContaBancaria> listaFornecedor = new ArrayList<>();
	private @Inject SavingService service;
	private Filtro filtro = new Filtro();
	 

	public SavingController() {

	}

	@PostConstruct
	public void init() {
		carregarFornecedor();
		carregarGestao();
	}
	
	public void limpar() {}{
		filtro = new Filtro();
	}
	
	public void filtrar() {
		carregarSaving();
	}
	
	public void modificarTexto(String field) {

	}
	
	public void carregarSaving() {
		lista = service.getSaving(filtro);
	}
	
	public void carregarGestao() {
		listaGestao = service.getGestao();
	}
	
	public void carregarFornecedor() {
		listaFornecedor = service.getFornecedor();
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Componente[] getComponentes() {
		return Componente.values();
	}

	public List<Saving> getLista() {
		return lista;
	}

	public void setLista(List<Saving> lista) {
		this.lista = lista;
	}

	public List<Gestao> getListaGestao() {
		return listaGestao;
	}

	public void setListaGestao(List<Gestao> listaGestao) {
		this.listaGestao = listaGestao;
	}

	public List<ContaBancaria> getListaFornecedor() {
		return listaFornecedor;
	}

	public void setListaFornecedor(List<ContaBancaria> listaFornecedor) {
		this.listaFornecedor = listaFornecedor;
	}

	public SavingService getService() {
		return service;
	}

	public void setService(SavingService service) {
		this.service = service;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

}
