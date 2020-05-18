package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class EntregasRealizadas implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private TipoEntrega tipo;
	
	@Column
	private Double pevistos;
	
	@Column
	private Double entregues;

	@ManyToOne
	private RelatorioCampo relatorioCampo;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoEntrega getTipo() {
		return tipo;
	}

	public void setTipo(TipoEntrega tipo) {
		this.tipo = tipo;
	}

	public Double getPevistos() {
		return pevistos;
	}

	public void setPevistos(Double pevistos) {
		this.pevistos = pevistos;
	}

	public Double getEntregues() {
		return entregues;
	}

	public RelatorioCampo getRelatorioCampo() {
		return relatorioCampo;
	}

	public void setRelatorioCampo(RelatorioCampo relatorioCampo) {
		this.relatorioCampo = relatorioCampo;
	}

	public void setEntregues(Double entregues) {
		this.entregues = entregues;
	}
	
	
	
}
