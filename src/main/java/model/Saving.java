package model;

import java.math.BigDecimal;
import java.util.Date;

public class Saving {
	
	private Date dataPagamento;
	private Date dataEmissao;
	private String gestao;
	private String localidade;
	private String fornecedor;
	private String produto;
	private BigDecimal valorComDesconto;
	private BigDecimal valorSemDesconto;
	private BigDecimal desconto;
	private Double quantidade;
	private Long pedido;

	
	public Saving() {}

	
	

	public Saving(Date dataPagamento, Date dataEmissao, String gestao, String localidade, String fornecedor,
			String produto, BigDecimal valorComDesconto, BigDecimal valorSemDesconto, BigDecimal desconto,
			Double quantidade, Long pedido) {
		super();
		this.dataPagamento = dataPagamento;
		this.dataEmissao = dataEmissao;
		this.gestao = gestao;
		this.localidade = localidade;
		this.fornecedor = fornecedor;
		this.produto = produto;
		this.valorComDesconto = valorComDesconto;
		this.valorSemDesconto = valorSemDesconto;
		this.desconto = desconto;
		this.quantidade = quantidade;
		this.pedido = pedido;
	}




	public Date getDataPagamento() {
		return dataPagamento;
	}


	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}


	public Date getDataEmissao() {
		return dataEmissao;
	}


	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}


	public String getGestao() {
		return gestao;
	}


	public void setGestao(String gestao) {
		this.gestao = gestao;
	}


	public String getLocalidade() {
		return localidade;
	}


	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}


	public String getFornecedor() {
		return fornecedor;
	}


	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}


	public String getProduto() {
		return produto;
	}


	public void setProduto(String produto) {
		this.produto = produto;
	}


	public BigDecimal getValorComDesconto() {
		return valorComDesconto;
	}


	public void setValorComDesconto(BigDecimal valorComDesconto) {
		this.valorComDesconto = valorComDesconto;
	}


	public BigDecimal getValorSemDesconto() {
		return valorSemDesconto;
	}


	public void setValorSemDesconto(BigDecimal valorSemDesconto) {
		this.valorSemDesconto = valorSemDesconto;
	}


	public BigDecimal getDesconto() {
		return desconto;
	}


	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}


	public Double getQuantidade() {
		return quantidade;
	}


	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}


	public Long getPedido() {
		return pedido;
	}


	public void setPedido(Long pedido) {
		this.pedido = pedido;
	}
	
	
	

}
