package model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("mun")
public class Municipio extends Localidade{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	
	
	@Column(name = "numero_ibge")
	private String numeroIbge;
	
	@Column
	private Boolean municipioDeCompra;
	
	@Transient
	private String nomeEstado = "";
	
	
	
	public Municipio(){}
	
	public Municipio(Long id, String nome){
		setId(id);
		setNome(nome);
	
	}
	
	public Municipio(Long id, String nome, Integer codigo){
		setId(id);
		setNome(nome);
		setCodigo(codigo);
	}


	public String getNumeroIbge() {
		return numeroIbge;
	}


	public void setNumeroIbge(String numeroIbge) {
		this.numeroIbge = numeroIbge;
	}





	public String getNomeEstado() {
		return nomeEstado;
	}





	public void setNomeEstado(String nomeEstado) {
		this.nomeEstado = nomeEstado;
	}

	public Boolean getMunicipioDeCompra() {
		return municipioDeCompra;
	}

	public void setMunicipioDeCompra(Boolean municipioDeCompra) {
		this.municipioDeCompra = municipioDeCompra;
	}
	
	
}
