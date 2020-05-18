package model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;



@Entity
@Table(name = "investimento_projeto")
public class InvestimentoProjeto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3888994843270766228L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private GrupoDeInvestimento grupoDeInvestimento;
	
	
	@ManyToOne
	private Acao acao;
	
	
	@Column
	private Integer quantidade;
	
	
	@Column
	private BigDecimal valorPrevisto;
	
	
	public InvestimentoProjeto(){}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public GrupoDeInvestimento getGrupoDeInvestimento() {
		return grupoDeInvestimento;
	}


	public void setGrupoDeInvestimento(GrupoDeInvestimento grupoDeInvestimento) {
		this.grupoDeInvestimento = grupoDeInvestimento;
	}


	public Integer getQuantidade() {
		return quantidade;
	}


	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}


	public BigDecimal getValorPrevisto() {
		return valorPrevisto;
	}


	public void setValorPrevisto(BigDecimal valorPrevisto) {
		this.valorPrevisto = valorPrevisto;
	}


	public Acao getAcao() {
		return acao;
	}


	public void setAcao(Acao acao) {
		this.acao = acao;
	}
	
	
	
	
	

	
	
	
	
	
}
