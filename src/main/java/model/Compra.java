package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import operator.LancamentoIterface;
import service.CompraService;
import util.CDILocator;
import util.Email;

@Entity
@DiscriminatorValue("compra")
public class Compra extends Lancamento implements LancamentoIterface {

	private static final long serialVersionUID = 1L;
	 
	@OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<HistoricoCompra> historicos = new ArrayList<HistoricoCompra>();
	
	@OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemCompra> itens = new ArrayList<ItemCompra>();
	
	@OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CompraMunicipio> listCompraMunicipio = new ArrayList<>();
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEstimadaConclusao;
 	
	public Compra(Long id,String nomeGestao, String nomeSolicitante, String nomeDestino, Date data, StatusCompra status, Boolean bootUrgencia, String categoria) {
		super(id, data, status);
		//setId(id);
		//setDataEmissao(data);
		//setStatusCompra(status);
		setNomeGestao(nomeGestao);
		setNomeSolicitante(nomeSolicitante);
		setNomeDestino(nomeDestino);
		setNomeCategoria(categoria);
		setBootUrgencia(bootUrgencia);
		
		/*this.nomeGestao = nomeGestao;
		this.nomeSolicitante = nomeSolicitante;
		this.nomeDestino = nomeDestino;*/
	}

	public Compra(){}

	public List<ItemCompra> getItens() {
		return itens;
	}

	public void setItens(List<ItemCompra> itens) {
		this.itens = itens;
	}

	public List<HistoricoCompra> getHistoricos() {
		return historicos;
	}

	public void setHistoricos(List<HistoricoCompra> historicos) {
		this.historicos = historicos;
	}

	public List<CompraMunicipio> getListCompraMunicipio() {
		return listCompraMunicipio;
	}

	public void setListCompraMunicipio(List<CompraMunicipio> listCompraMunicipio) {
		this.listCompraMunicipio = listCompraMunicipio;
	}

	public Date getDataEstimadaConclusao() {
		return dataEstimadaConclusao;
	}

	public void setDataEstimadaConclusao(Date dataEstimadaConclusao) {
		this.dataEstimadaConclusao = dataEstimadaConclusao;
	}

	@Override
	public void autorizar(Lancamento lancamento) {
	}

	@Override
	public void desautorizar(Lancamento lancamento, String texto) {
	}

	@Override
	public void imprimir(Lancamento lancamento) {
		CompraService compraService = CDILocator.getBean(CompraService.class);
		compraService.imprimirCompra(compraService.getCompraById(lancamento.getId()));		
	}

	

}
