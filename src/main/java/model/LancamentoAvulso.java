package model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("lanc_av")
public class LancamentoAvulso extends Lancamento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Transient
	private String bancoo = "Banco1";
	
	@Transient
	private String recebedor = "Tec1";
	
	@Transient
	private String categoria = "familiar";
	
	@Transient
	private String moeda = "R$";
	
	@Transient
	private String valor = "1500";
	
	@Transient
	private String vencto = "01/07/2016";
	
	@Transient
	private String dtEmissao = "01/07/2016";
	
	@Transient
	private String descricaooo = "A vista";
	
	@Transient
	private String classificacao22 = "A vista";
	
	@Transient
	private String Ndoc = "16552";
	
	@Transient
	private String contaEnvolvida = "16552";
	
	@Transient
	private Boolean saidaPrestacao;
	
	
	@Column
	private Long idAdiantamento;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPagamentoPrestacao;
	

	@Transient
	private String acao;

	
	public LancamentoAvulso(){}

	public String getBancoo() {
		return bancoo;
	}

	public void setBancoo(String bancoo) {
		this.bancoo = bancoo;
	}

	public String getRecebedor() {
		return recebedor;
	}

	public void setRecebedor(String recebedor) {
		this.recebedor = recebedor;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getMoeda() {
		return moeda;
	}

	public void setMoeda(String moeda) {
		this.moeda = moeda;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getVencto() {
		return vencto;
	}

	public void setVencto(String vencto) {
		this.vencto = vencto;
	}

	public String getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(String dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public String getDescricaooo() {
		return descricaooo;
	}

	public void setDescricaooo(String descricaooo) {
		this.descricaooo = descricaooo;
	}

	public String getClassificacao22() {
		return classificacao22;
	}

	public void setClassificacao22(String classificacao22) {
		this.classificacao22 = classificacao22;
	}

	public String getNdoc() {
		return Ndoc;
	}

	public void setNdoc(String ndoc) {
		Ndoc = ndoc;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	

	public String getContaEnvolvida() {
		return contaEnvolvida;
	}

	public void setContaEnvolvida(String contaEnvolvida) {
		this.contaEnvolvida = contaEnvolvida;
	}

	public Boolean getSaidaPrestacao() {
		return saidaPrestacao;
	}

	public void setSaidaPrestacao(Boolean saidaPrestacao) {
		this.saidaPrestacao = saidaPrestacao;
	}

	public Long getIdAdiantamento() {
		return idAdiantamento;
	}

	public void setIdAdiantamento(Long idAdiantamento) {
		this.idAdiantamento = idAdiantamento;
	}

	public Date getDataPagamentoPrestacao() {
		return dataPagamentoPrestacao;
	}

	public void setDataPagamentoPrestacao(Date dataPagamentoPrestacao) {
		this.dataPagamentoPrestacao = dataPagamentoPrestacao;
	}

	/*public Date getDataPagamentoPrestacao() {
		return dataPagamentoPrestacao;
	}

	public void setDataPagamentoPrestacao(Date dataPagamentoPrestacao) {
		this.dataPagamentoPrestacao = dataPagamentoPrestacao;
	}
	
	public Long getIdAdiantamento() {
		return idAdiantamento;
	}

	public void setIdAdiantamento(Long idAdiantamento) {
		this.idAdiantamento = idAdiantamento;
	}*/

	

	
	
	
	
	

}
