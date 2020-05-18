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
public class Publico implements Serializable{

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
	private int quantidade; 
	
	@Enumerated(EnumType.STRING)
	private  TipoPublico tipoPublico;

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public RelatorioCampo getRelatorioCampo() {
		return relatorioCampo;
	}

	public void setRelatorioCampo(RelatorioCampo relatorioCampo) {
		this.relatorioCampo = relatorioCampo;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public TipoPublico getTipoPublico() {
		return tipoPublico;
	}

	public void setTipoPublico(TipoPublico tipoPublico) {
		this.tipoPublico = tipoPublico;
	}
	
	
	
}
