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
public class InstrumentoColeta implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue
	private long id;
	
	@Column
	private Integer nFormularios;
	
	@Column
	private Integer nComunidade;
	
	@Enumerated(EnumType.STRING)
	private TipoColeta tipoColeta;
	
	@ManyToOne
	private RelatorioCampo relatorioCampo;

	public Integer getnFormularios() {
		return nFormularios;
	}

	public void setnFormularios(Integer nFormularios) {
		this.nFormularios = nFormularios;
	}

	public Integer getnComunidade() {
		return nComunidade;
	}

	public void setnComunidade(Integer nComunidade) {
		this.nComunidade = nComunidade;
	}

	public TipoColeta getTipoColeta() {
		return tipoColeta;
	}

	public void setTipoColeta(TipoColeta tipoColeta) {
		this.tipoColeta = tipoColeta;
	}

	public RelatorioCampo getRelatorioCampo() {
		return relatorioCampo;
	}

	public void setRelatorioCampo(RelatorioCampo relatorioCampo) {
		this.relatorioCampo = relatorioCampo;
	}

	
	
}
