package model;

import java.math.BigDecimal;

public class CotacaoAuxiliar {
	
	
	private Long fornecedorId;
	private Long compraId;
	private String fornecedor;
	private BigDecimal totalValor;
	private BigDecimal totalDesconto;
	
	public CotacaoAuxiliar(){}

	
	public BigDecimal getTotalValor() {
		return totalValor;
	}

	public void setTotalValor(BigDecimal totalValor) {
		this.totalValor = totalValor;
	}

	public BigDecimal getTotalDesconto() {
		return totalDesconto;
	}

	public void setTotalDesconto(BigDecimal totalDesconto) {
		this.totalDesconto = totalDesconto;
	}


	public Long getFornecedorId() {
		return fornecedorId;
	}


	public void setFornecedorId(Long fornecedorId) {
		this.fornecedorId = fornecedorId;
	}


	public Long getCompraId() {
		return compraId;
	}


	public void setCompraId(Long compraId) {
		this.compraId = compraId;
	}


	public String getFornecedor() {
		return fornecedor;
	}


	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	

}
