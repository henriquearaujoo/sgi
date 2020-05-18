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
import model.RelatorioDiaria;
import service.RelatorioDiariaService;
import util.Filtro;

@Named(value = "relatorio_diaria_controller")
@ViewScoped
public class RelatorioDiariaController implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	private List<RelatorioDiaria> lista = new ArrayList<>();
	private List<Gestao> listaGestao = new ArrayList<>();
	private List<ContaBancaria> listaFornecedor = new ArrayList<>();
	private @Inject RelatorioDiariaService service;
	private Filtro filtro = new Filtro();

	public RelatorioDiariaController() {

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
		carregarDiaria();
	}

	public void modificarTexto(String field) {

	}

	public void carregarDiaria() {
		lista = service.getDiaria(filtro);
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

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public List<RelatorioDiaria> getLista() {
		return lista;
	}

	public void setLista(List<RelatorioDiaria> lista) {
		this.lista = lista;
	}

}
