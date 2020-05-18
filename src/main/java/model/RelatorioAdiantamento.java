package model;

import java.io.Serializable;
import java.math.BigDecimal;

public class RelatorioAdiantamento implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7192254748754892883L;
	
	
	private String dtPagto; 
	private String dtEmissao;
	private String pagador;
	private String recebedor;
	private BigDecimal entrada;
	private BigDecimal saida;
	private BigDecimal saldo;
	private String descricao;
	private String numeroDocumento;
	
	
	public RelatorioAdiantamento(){}


	public String getDtPagto() {
		return dtPagto;
	}


	public void setDtPagto(String dtPagto) {
		this.dtPagto = dtPagto;
	}


	public String getDtEmissao() {
		return dtEmissao;
	}


	public void setDtEmissao(String dtEmissao) {
		this.dtEmissao = dtEmissao;
	}


	public String getPagador() {
		return pagador;
	}


	public void setPagador(String pagador) {
		this.pagador = pagador;
	}


	public String getRecebedor() {
		return recebedor;
	}


	public void setRecebedor(String recebedor) {
		this.recebedor = recebedor;
	}


	public BigDecimal getEntrada() {
		return entrada;
	}


	public void setEntrada(BigDecimal entrada) {
		this.entrada = entrada;
	}


	public BigDecimal getSaida() {
		return saida;
	}


	public void setSaida(BigDecimal saida) {
		this.saida = saida;
	}


	public BigDecimal getSaldo() {
		return saldo;
	}


	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public String getNumeroDocumento() {
		return numeroDocumento;
	}


	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}


}
