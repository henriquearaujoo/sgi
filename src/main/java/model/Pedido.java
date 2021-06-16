package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import operator.LancamentoIterface;
import service.CompraService;
import service.PedidoService;
import util.CDILocator;
import util.Email;

@Entity
@DiscriminatorValue("pedido")
public class Pedido extends Lancamento implements LancamentoIterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Compra compra;
	
	
	
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemPedido> itensPedidos = new ArrayList<ItemPedido>();
	
	@ManyToOne
	private SolicitacaoExpedicao solicitacaoExpedicao;
	
	@Column
	private Boolean itemExcluido;
	
	@Column
	private Boolean substituto;
	
	@Column
	private String codigoSubstituo;
	
	@ManyToOne
	private ControleExpedicao controleExpedicao;
	
	@ManyToOne
	private TermoExpedicao termo;
	
	@ManyToOne
	private User usuarioGeradorPedido;
	
	@Column
	private BigDecimal valorDevolucao;
	
	@Transient
	private String nomeFornecedor;
	@Transient
	private String destino;
	@Transient
	private String comunidadeDestino;
	

	public Pedido(Long id, String nomeFornecedor, String destino, String notaFiscal,BigDecimal valorPedido, String descricao){
		setId(id);
		this.nomeFornecedor = nomeFornecedor;
		this.destino = destino;
		setValorTotalComDesconto(valorPedido);
		setNotaFiscal(notaFiscal);
		setDescricao(descricao);
	}
	

	@Transient
	private Long idFornecedor;
	
	
	public Pedido(){}




	public Compra getCompra() {
		return compra;
	}




	public void setCompra(Compra compra) {
		this.compra = compra;
	}









	public List<ItemPedido> getItensPedidos() {
		return itensPedidos;
	}




	public void setItensPedidos(List<ItemPedido> itensPedidos) {
		this.itensPedidos = itensPedidos;
	}




	public Long getIdFornecedor() {
		return idFornecedor;
	}




	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}




	public SolicitacaoExpedicao getSolicitacaoExpedicao() {
		return solicitacaoExpedicao;
	}




	public void setSolicitacaoExpedicao(SolicitacaoExpedicao solicitacaoExpedicao) {
		this.solicitacaoExpedicao = solicitacaoExpedicao;
	}




	public ControleExpedicao getControleExpedicao() {
		return controleExpedicao;
	}




	public void setControleExpedicao(ControleExpedicao controleExpedicao) {
		this.controleExpedicao = controleExpedicao;
	}




	public TermoExpedicao getTermo() {
		return termo;
	}




	public void setTermo(TermoExpedicao termo) {
		this.termo = termo;
	}




	public User getUsuarioGeradorPedido() {
		return usuarioGeradorPedido;
	}




	public void setUsuarioGeradorPedido(User usuarioGeradorPedido) {
		this.usuarioGeradorPedido = usuarioGeradorPedido;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getComunidadeDestino() {
		return comunidadeDestino;
	}

	public void setComunidadeDestino(String comunidadeDestino) {
		this.comunidadeDestino = comunidadeDestino;
	}

	public Boolean getSubstituto() {
		return substituto;
	}

	public void setSubstituto(Boolean substituto) {
		this.substituto = substituto;
	}

	public String getCodigoSubstituo() {
		return codigoSubstituo;
	}

	public void setCodigoSubstituo(String codigoSubstituo) {
		this.codigoSubstituo = codigoSubstituo;
	}

	public BigDecimal getValorDevolucao() {
		return valorDevolucao;
	}

	public void setValorDevolucao(BigDecimal valorDevolucao) {
		this.valorDevolucao = valorDevolucao;
	}

	public Boolean getItemExcluido() {
		return itemExcluido;
	}

	public void setItemExcluido(Boolean itemExcluido) {
		this.itemExcluido = itemExcluido;
	}




	@Override
	public void autorizar(Lancamento lancamento) {
		
	}




	@Override
	public void desautorizar(Lancamento lancamento, String texto) {
		
	}




	@Override
	public void imprimir(Lancamento lancamento) {
		PedidoService pedidoService = CDILocator.getBean(PedidoService.class);
		CompraService compraService = CDILocator.getBean(CompraService.class);
		pedidoService.imprimirPedido(pedidoService.getPedidoById(lancamento.getId()), compraService);
		
	}

	
	
}
