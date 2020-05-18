package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Trecho implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long id;
	
	@Column(name = "data_ida")
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	@ManyToOne
	private Localidade destino;
	
	@ManyToOne
	private Localidade origem;
	
	@ManyToOne
	private Fornecedor empresa;
	
	@ManyToOne
	private Diaria diaria;
	
	
	@Enumerated(EnumType.STRING) 
	private TipoLocalidade tipoLocalidadeOrigem;
	
	
	public Diaria getDiaria() {
		return diaria;
	}

	public void setDiaria(Diaria diaria) {
		this.diaria = diaria;
	}

	public TipoLocalidade getTipoLocalidadeOrigem() {
		return tipoLocalidadeOrigem;
	}

	public void setTipoLocalidadeOrigem(TipoLocalidade tipoLocalidadeOrigem) {
		this.tipoLocalidadeOrigem = tipoLocalidadeOrigem;
	}

	public TipoLocalidade getTipoLocalidadeDestino() {
		return tipoLocalidadeDestino;
	}

	public void setTipoLocalidadeDestino(TipoLocalidade tipoLocalidadeDestino) {
		this.tipoLocalidadeDestino = tipoLocalidadeDestino;
	}

	@Enumerated(EnumType.STRING) 
	private TipoLocalidade tipoLocalidadeDestino;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Fornecedor getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Fornecedor empresa) {
		this.empresa = empresa;
	} 
	
	
	public Localidade getDestino() {
		return destino;
	}

	public void setDestino(Localidade destino) {
		this.destino = destino;
	}

	public Localidade getOrigem() {
		return origem;
	}

	public void setOrigem(Localidade origem) {
		this.origem = origem;
	}
	
}
