package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DespesaViagem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue
	private Long id; 
	
	@ManyToOne
	private SolicitacaoViagem solicitacaoViagem;
	
	@Enumerated(EnumType.STRING)
	private TipoDespesa tipoDespesa;
	
	@Enumerated(EnumType.STRING)
	private AtividadeDespesa atividadeDespesa;
	
	@Enumerated(EnumType.STRING)
	private UnidadeMedida unidadeMedida;
	
	@ManyToOne
	private CategoriaDespesaClass categoriaDeDespesa;
	
	@Column
	private Double totalReal;
	
	@Column
	private String observacao;
	
	@Column
	private int quantidade;
	
	@Column
	private Double valorPrevisto ;
	
	@Column
	private Double totalPrevisto ;
	
	
	@Column
	private Double valorReal;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SolicitacaoViagem getSolicitacaoViagem() {
		return solicitacaoViagem;
	}

	public void setSolicitacaoViagem(SolicitacaoViagem solicitacaoViagem) {
		this.solicitacaoViagem = solicitacaoViagem;
	}

	public TipoDespesa getTipoDespesa() {
		return tipoDespesa;
	}

	public void setTipoDespesa(TipoDespesa tipoDespesa) {
		this.tipoDespesa = tipoDespesa;
	}

	public AtividadeDespesa getAtividadeDespesa() {
		return atividadeDespesa;
	}

	public void setAtividadeDespesa(AtividadeDespesa atividadeDespesa) {
		this.atividadeDespesa = atividadeDespesa;
	}

	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public Double getValorPrevisto() {
		return valorPrevisto;
	}

	public void setValorPrevisto(Double valorPrevisto) {
		this.valorPrevisto = valorPrevisto;
	}

	public Double getTotalPrevisto() {
		return totalPrevisto;
	}

	public void setTotalPrevisto(Double totalPrevisto) {
		this.totalPrevisto = totalPrevisto;
	}

	public Double getValorReal() {
		return valorReal;
	}

	public void setValorReal(Double valorReal) {
		this.valorReal = valorReal;
	}

	public Double getTotalReal() {
		return totalReal;
	}

	public void setTotalReal(Double totalReal) {
		this.totalReal = totalReal;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
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
		DespesaViagem other = (DespesaViagem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public CategoriaDespesaClass getCategoriaDeDespesa() {
		return categoriaDeDespesa;
	}

	public void setCategoriaDeDespesa(CategoriaDespesaClass categoriaDeDespesa) {
		this.categoriaDeDespesa = categoriaDeDespesa;
	}

	
}
