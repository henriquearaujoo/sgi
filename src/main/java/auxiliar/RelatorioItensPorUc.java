package auxiliar;

import java.util.Date;

public class RelatorioItensPorUc {
	
	
	private String pedido;
	private Date emissao;
	private String uc;
	private String comunidade;
	private String produto;
	private String categoria;
	private String tipo;
	private Double quantidade;
	private Double valorSemDesconto;
	private Double valorComDesconto;
	private Double totalSemDesconto;
	private Double totalComDesconto;
	
	
	public RelatorioItensPorUc(){}


	public String getPedido() {
		return pedido;
	}


	public void setPedido(String pedido) {
		this.pedido = pedido;
	}


	public Date getEmissao() {
		return emissao;
	}


	public void setEmissao(Date emissao) {
		this.emissao = emissao;
	}


	public String getUc() {
		return uc;
	}


	public void setUc(String uc) {
		this.uc = uc;
	}


	public String getComunidade() {
		return comunidade;
	}


	public void setComunidade(String comunidade) {
		this.comunidade = comunidade;
	}


	public String getProduto() {
		return produto;
	}


	public void setProduto(String produto) {
		this.produto = produto;
	}


	public String getCategoria() {
		return categoria;
	}


	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public Double getQuantidade() {
		return quantidade;
	}


	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}


	public Double getValorSemDesconto() {
		return valorSemDesconto;
	}


	public void setValorSemDesconto(Double valorSemDesconto) {
		this.valorSemDesconto = valorSemDesconto;
	}


	public Double getValorComDesconto() {
		return valorComDesconto;
	}


	public void setValorComDesconto(Double valorComDesconto) {
		this.valorComDesconto = valorComDesconto;
	}


	public Double getTotalSemDesconto() {
		return totalSemDesconto;
	}


	public void setTotalSemDesconto(Double totalSemDesconto) {
		this.totalSemDesconto = totalSemDesconto;
	}


	public Double getTotalComDesconto() {
		return totalComDesconto;
	}


	public void setTotalComDesconto(Double totalComDesconto) {
		this.totalComDesconto = totalComDesconto;
	}

		
	
	
}
