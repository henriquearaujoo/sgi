/**
 * 
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author developer
 * @version 1.0
 * Classe para objetos do tipo CotacaoParent que serão referência para classe Cotacao
 * 
 */


@Entity
@Table(name= "cotacao_parent")
public class CotacaoParent implements Serializable{

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "data_emissao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEmisao;
	
	@Column(name = "data_emissao_pedido")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEmisaoPedido;
	
	@Column(name = "data_pgto_pedido")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPagamentoPedido;
	
	@ManyToOne
	private Compra compra;
	
	@ManyToOne
	private Fornecedor fornecedor;
	
	@Column
	private String observacao;
	
	@Enumerated(EnumType.STRING)
	private TipoParcelamento tipoParcelamento;
	
	
	
	@Column(name="quantidade_parcela")
	private Integer quantidadeParcela;
	
	@Column(name = "condicao_pagamento_enum")
	@Enumerated(EnumType.STRING)
	private CondicaoPagamento condicaoPagamentoEnum;

	@Column(name = "valor_total_sem_desconto", precision = 10, scale = 2)
	private BigDecimal valorTotalSemDesconto  = BigDecimal.ZERO;;
	
	@Column(name = "valor_total_com_desconto", precision = 10, scale = 2)
	private BigDecimal valorTotalComDesconto = BigDecimal.ZERO;
	
	@Column(name = "valor_total_desconto", precision = 10, scale = 2)
	private BigDecimal valorTotalDesconto  = BigDecimal.ZERO;
	
	@Column(name = "valor_total_desconto_porcentaagem", precision = 10, scale = 2)
	private BigDecimal valorTotalDescontoPorcentagem  = BigDecimal.ZERO;;
	
	
	public CotacaoParent() {
		this.valorTotalSemDesconto = BigDecimal.ZERO;
		this.valorTotalComDesconto = BigDecimal.ZERO;
		this.valorTotalDesconto = BigDecimal.ZERO;
		this.valorTotalDescontoPorcentagem = BigDecimal.ZERO;
	}
                                 
	public Long getId() {        
		return id;               
	}                                                          


	public void setId(Long id) {
		this.id = id;
	}


	public Date getDataEmisao() {
		return dataEmisao;
	}


	public void setDataEmisao(Date dataEmisao) {
		this.dataEmisao = dataEmisao;
	}


	public Compra getCompra() {
		return compra;
	}


	public void setCompra(Compra compra) {
		this.compra = compra;
	}


	public Fornecedor getFornecedor() {
		return fornecedor;
	}


	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}


	public String getObservacao() {
		return observacao;
	}


	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}


	public TipoParcelamento getTipoParcelamento() {
		return tipoParcelamento;
	}


	public void setTipoParcelamento(TipoParcelamento tipoParcelamento) {
		this.tipoParcelamento = tipoParcelamento;
	}


	public Integer getQuantidadeParcela() {
		return quantidadeParcela;
	}


	public void setQuantidadeParcela(Integer quantidadeParcela) {
		this.quantidadeParcela = quantidadeParcela;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CotacaoParent other = (CotacaoParent) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public BigDecimal getValorTotalSemDesconto() {
		return valorTotalSemDesconto;
	}


	public void setValorTotalSemDesconto(BigDecimal valorTotalSemDesconto) {
		this.valorTotalSemDesconto = valorTotalSemDesconto;
	}


	public BigDecimal getValorTotalComDesconto() {
		return valorTotalComDesconto;
	}


	public void setValorTotalComDesconto(BigDecimal valorTotalComDesconto) {
		this.valorTotalComDesconto = valorTotalComDesconto;
	}


	public BigDecimal getValorTotalDesconto() {
		return valorTotalDesconto;
	}


	public void setValorTotalDesconto(BigDecimal valorTotalDesconto) {
		this.valorTotalDesconto = valorTotalDesconto;
	}


	public BigDecimal getValorTotalDescontoPorcentagem() {
		return valorTotalDescontoPorcentagem;
	}


	public void setValorTotalDescontoPorcentagem(BigDecimal valorTotalDescontoPorcentagem) {
		this.valorTotalDescontoPorcentagem = valorTotalDescontoPorcentagem;
	}

	public Date getDataEmisaoPedido() {
		return dataEmisaoPedido;
	}

	public void setDataEmisaoPedido(Date dataEmisaoPedido) {
		this.dataEmisaoPedido = dataEmisaoPedido;
	}

	public CondicaoPagamento getCondicaoPagamentoEnum() {
		return condicaoPagamentoEnum;
	}

	public void setCondicaoPagamentoEnum(CondicaoPagamento condicaoPagamentoEnum) {
		this.condicaoPagamentoEnum = condicaoPagamentoEnum;
	}

	public Date getDataPagamentoPedido() {
		return dataPagamentoPedido;
	}

	public void setDataPagamentoPedido(Date dataPagamentoPedido) {
		this.dataPagamentoPedido = dataPagamentoPedido;
	}

	
	
	
}
