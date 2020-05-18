package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AvaliacaoBens implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue
	private long id;
	
	@ManyToOne
	private RelatorioCampo relatorioCampo;
	
	@Column
	private Integer nFuncionando;

	@Enumerated(EnumType.STRING)
	private TipoBemAvaliado tipoBemAvaliado;
	
	@Column
	private Integer nManuntencao;
	
	@Column
	private Integer nDefeito;
	
	@Column
	private Integer nSemUso;
	
	@Column
	private Integer nDivergentes;
	
	@Column
	private String Comunidades;
	
	@Column
	private String Observação;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getnFuncionando() {
		return nFuncionando;
	}

	public void setnFuncionando(Integer nFuncionando) {
		this.nFuncionando = nFuncionando;
	}

	public TipoBemAvaliado getTipoBemAvaliado() {
		return tipoBemAvaliado;
	}

	public void setTipoBemAvaliado(TipoBemAvaliado tipoBemAvaliado) {
		this.tipoBemAvaliado = tipoBemAvaliado;
	}

	public Integer getnManuntencao() {
		return nManuntencao;
	}

	public void setnManuntencao(Integer nManuntencao) {
		this.nManuntencao = nManuntencao;
	}

	public Integer getnDefeito() {
		return nDefeito;
	}

	public void setnDefeito(Integer nDefeito) {
		this.nDefeito = nDefeito;
	}

	public Integer getnSemUso() {
		return nSemUso;
	}

	public void setnSemUso(Integer nSemUso) {
		this.nSemUso = nSemUso;
	}

	public Integer getnDivergentes() {
		return nDivergentes;
	}

	public void setnDivergentes(Integer nDivergentes) {
		this.nDivergentes = nDivergentes;
	}

	public String getComunidades() {
		return Comunidades;
	}

	public void setComunidades(String comunidades) {
		Comunidades = comunidades;
	}

	public String getObservação() {
		return Observação;
	}

	public void setObservação(String observação) {
		Observação = observação;
	}
	
	public RelatorioCampo getRelatorioCampo() {
		return relatorioCampo;
	}

	public void setRelatorioCampo(RelatorioCampo relatorioCampo) {
		this.relatorioCampo = relatorioCampo;
	}

}
