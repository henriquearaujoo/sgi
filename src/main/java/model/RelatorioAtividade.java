package model;

import java.io.Serializable;
import java.math.BigDecimal;

import util.Util;

public class RelatorioAtividade implements Serializable{

	private String nomeAtividade;
	private String responsavel;
	private String localizador;
	private BigDecimal vlOrcado;
	private BigDecimal vlExecutado;
	private BigDecimal saldo;
	private Double percentualFinanceiro;
	private Integer qtdPlanejada;
	private Integer qtdExecutada;
	private Integer pendentes;
	private Double percentualFisico;
	private BigDecimal entrada;
	private BigDecimal saida;
	
	
	public RelatorioAtividade(Object[] obj){
		
		this.nomeAtividade = Util.nullEmptyObjectUtil(obj[0]);
		
		this.responsavel = Util.nullEmptyObjectUtil(obj[1]);
		
		this.localizador = Util.nullEmptyObjectUtil(obj[2]);
		
		this.vlOrcado = obj[3] != null ? new BigDecimal(obj[3].toString()) : BigDecimal.ZERO;
		
		this.entrada = obj[7] != null ? new BigDecimal(obj[7].toString()) : BigDecimal.ZERO;
		this.saida = obj[8] != null ? new BigDecimal(obj[8].toString()) : BigDecimal.ZERO;
		
		this.vlExecutado = saida.subtract(entrada); 
		
		//this.vlExecutado = obj[4] != null ? new BigDecimal(obj[4].toString()) : BigDecimal.ZERO;
		
		this.saldo = vlOrcado.subtract(vlExecutado);
		
		this.qtdPlanejada = Integer.parseInt(obj[5] != null ? obj[5].toString() : "0");
		
		this.qtdExecutada = Integer.parseInt(obj[6] != null ? obj[6].toString() : "0");
		
		this.pendentes = qtdPlanejada - qtdExecutada;
		
		this.percentualFisico = (qtdExecutada.doubleValue() / qtdPlanejada) * 100;
		
		this.percentualFinanceiro = (vlExecutado.doubleValue() / vlOrcado.doubleValue()) * 100;
		
	}
	
	public RelatorioAtividade(String nomeAtividade, String responsavel, String localizador, BigDecimal vlOrcado,
			BigDecimal vlExecutado, BigDecimal saldo, Double percentualFinanceiro, Integer qtdPlanejada,
			Integer qtdExecutada, Integer pendentes, Double percentualFisico) {
	
		this.nomeAtividade = nomeAtividade;
		this.responsavel = responsavel; 
		this.localizador = localizador;
		this.vlOrcado = vlOrcado;
		this.vlExecutado = vlExecutado;
		this.saldo = saldo;
		this.percentualFinanceiro = percentualFinanceiro;
		this.qtdPlanejada = qtdPlanejada;
		this.qtdExecutada = qtdExecutada;
		this.pendentes = pendentes;
		this.percentualFisico = percentualFisico;
	}
	
	
	public String getNomeAtividade() {
		return nomeAtividade;
	}
	
	public void setNomeAtividade(String nomeAtividade) {
		this.nomeAtividade = nomeAtividade;
	}
	
	public String getResponsavel() {
		return responsavel;
	}
	
	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}
	public String getLocalizador() {
		return localizador;
	}
	public void setLocalizador(String localizador) {
		this.localizador = localizador;
	}
	public BigDecimal getVlOrcado() {
		return vlOrcado;
	}
	public void setVlOrcado(BigDecimal vlOrcado) {
		this.vlOrcado = vlOrcado;
	}
	public BigDecimal getVlExecutado() {
		return vlExecutado;
	}
	public void setVlExecutado(BigDecimal vlExecutado) {
		this.vlExecutado = vlExecutado;
	}
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	public Double getPercentualFinanceiro() {
		return percentualFinanceiro;
	}
	public void setPercentualFinanceiro(Double percentualFinanceiro) {
		this.percentualFinanceiro = percentualFinanceiro;
	}
	public Integer getQtdPlanejada() {
		return qtdPlanejada;
	}
	public void setQtdPlanejada(Integer qtdPlanejada) {
		this.qtdPlanejada = qtdPlanejada;
	}
	public Integer getQtdExecutada() {
		return qtdExecutada;
	}
	public void setQtdExecutada(Integer qtdExecutada) {
		this.qtdExecutada = qtdExecutada;
	}
	public Integer getPendentes() {
		return pendentes;
	}
	public void setPendentes(Integer pendentes) {
		this.pendentes = pendentes;
	}
	public Double getPercentualFisico() {
		return percentualFisico;
	}
	public void setPercentualFisico(Double percentualFisico) {
		this.percentualFisico = percentualFisico;
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
	
	
	
}
