package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import operator.LancamentoIterface;
import util.Email;

@Entity
@DiscriminatorValue("Diaria")
public class Diaria extends Lancamento implements LancamentoIterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private TipoDiaria tipoDiaria;

	@ManyToOne
	private Colaborador profissional;

	@Column
	private Integer diasnocampo;

	@Column
	private int quantidadeIntegral;

	@Column
	private int quantidadeParcial;

	@Column
	private Double valorIntegral;

	@Column(precision = 10, scale = 2)
	private BigDecimal valorIntegral1;

	@Column
	private Double totalIntegral;

	@Column(precision = 10, scale = 2)
	private BigDecimal totalIntegral1;

	@Column
	private Double valorParcial;

	@Column(precision = 10, scale = 2)
	private BigDecimal valorParcial1;

	@Column
	private Double totalParcial;

	// TOTAL PARCIAL = quantidade_parcial * valor parcial
	@Column(precision = 10, scale = 2)
	private BigDecimal totalParcial1;

	// @Column(precision = 10, scale = 2)
	// private BigDecimal suprimento;

	@ManyToOne
	private SolicitacaoViagem solicitacaoViagem;

	@OneToMany(mappedBy = "diaria", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Trecho> trecho = new ArrayList<>();

	@Column(name = "data_ida_v2")
	@Temporal(TemporalType.DATE)
	private Date dataIda;

	@Column(name = "data_volta_v2")
	@Temporal(TemporalType.DATE)
	private Date dataVolta;

	// Adicionado para Solicitação de diaria para identificar meia diaria by:
	// Christophe Alexander 05/10/2018
	// @Column(name = "hospedagem",nullable = false,columnDefinition = "false")
	@Column
	private Boolean hospedagem;

	public Boolean getHospedagem() {
		return hospedagem;
	}

	public void setHospedagem(Boolean hospedagem) {
		this.hospedagem = hospedagem;
	}

	// end
	public Diaria() {
		this.quantidadeIntegral = 0;
		this.quantidadeParcial = 0;
		this.valorIntegral1 = BigDecimal.ZERO;
		this.valorParcial1 = BigDecimal.ZERO;
		this.totalIntegral1 = BigDecimal.ZERO;
		this.totalParcial1 = BigDecimal.ZERO;

	}

	public List<Trecho> getTrecho() {
		return trecho;
	}

	public void setTrecho(List<Trecho> trecho) {
		this.trecho = trecho;
	}

	public Date getDataIda() {
		return dataIda;
	}

	public void setDataIda(Date dataIda) {
		this.dataIda = dataIda;
	}

	public Date getDataVolta() {
		return dataVolta;
	}

	public void setDataVolta(Date dataVolta) {
		this.dataVolta = dataVolta;
	}

	public SolicitacaoViagem getSolicitacaoViagem() {
		return solicitacaoViagem;
	}

	public void setSolicitacaoViagem(SolicitacaoViagem solicitacaoViagem) {
		this.solicitacaoViagem = solicitacaoViagem;
	}

	public TipoDiaria getTipoDiaria() {
		return tipoDiaria;
	}

	public void setTipoDiaria(TipoDiaria tipoDiaria) {
		this.tipoDiaria = tipoDiaria;
	}

	public Colaborador getProfissional() {
		return profissional;
	}

	public void setProfissional(Colaborador profissional) {
		this.profissional = profissional;
	}

	public int getQuantidadeIntegral() {
		return quantidadeIntegral;
	}

	public void setQuantidadeIntegral(int quantidadeIntegral) {
		this.quantidadeIntegral = quantidadeIntegral;
	}

	public Double getValorIntegral() {
		return valorIntegral;
	}

	public void setValorIntegral(Double valorIntegral) {
		this.valorIntegral = valorIntegral;
	}

	public Double getTotalIntegral() {
		return totalIntegral;
	}

	public void setTotalIntegral(Double totalIntegral) {
		this.totalIntegral = totalIntegral;
	}

	public int getQuantidadeParcial() {
		return quantidadeParcial;
	}

	public void setQuantidadeParcial(int quantidadeParcial) {
		this.quantidadeParcial = quantidadeParcial;
	}

	public Double getValorParcial() {
		return valorParcial;
	}

	public void setValorParcial(Double valorParcial) {
		this.valorParcial = valorParcial;
	}

	public Double getTotalParcial() {
		return totalParcial;
	}

	public void setTotalParcial(Double totalParcial) {
		this.totalParcial = totalParcial;
	}

	public BigDecimal getValorIntegral1() {
		return valorIntegral1;
	}

	public void setValorIntegral1(BigDecimal valorIntegral1) {
		this.valorIntegral1 = valorIntegral1;
	}

	public BigDecimal getTotalIntegral1() {
		return totalIntegral1;
	}

	public void setTotalIntegral1(BigDecimal totalIntegral1) {
		this.totalIntegral1 = totalIntegral1;
	}

	public BigDecimal getValorParcial1() {
		return valorParcial1;
	}

	public void setValorParcial1(BigDecimal valorParcial1) {
		this.valorParcial1 = valorParcial1;
	}

	public BigDecimal getTotalParcial1() {
		return totalParcial1;
	}

	public void setTotalParcial1(BigDecimal totalParcial1) {
		this.totalParcial1 = totalParcial1;
	}

	public Integer getDiasnocampo() {
		return diasnocampo;
	}

	public void setDiasnocampo(Integer diasnocampo) {
		this.diasnocampo = diasnocampo;
	}

	@Override
	public void autorizar(Lancamento lancamento) {
		System.out.println("Email diária");

	}

	@Override
	public void desautorizar(Lancamento lancamento, String texto) {
		System.out.println("Desautorizar diária");

	}

	@Override
	public void imprimir(Lancamento lancamento) {
		// TODO Auto-generated method stub
		
	}

}
