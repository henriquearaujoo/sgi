package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class RepresentanteRelatorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

	@Id 
	@GeneratedValue
	private long id;
	
	
	@Column
	private String nome;
	
	@Column
	private String instituicao;
	
	
	@ManyToOne
	private RelatorioCampo relatorioCampo;


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getInstituicao() {
		return instituicao;
	}


	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}


	public RelatorioCampo getRelatorioCampo() {
		return relatorioCampo;
	}


	public void setRelatorioCampo(RelatorioCampo relatorioCampo) {
		this.relatorioCampo = relatorioCampo;
	}
	
	
	
}
