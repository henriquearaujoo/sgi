package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("coord")
public class Coordenadoria extends Gestao{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	private Superintendencia superintendencia;

	public Coordenadoria(){}
	
	public Coordenadoria(Long id, String nome, String type){
		setId(id);
		setNome(nome);
		setType(type);
	}

	public Superintendencia getSuperintendencia() {
		return superintendencia;
	}

	public void setSuperintendencia(Superintendencia superintendencia) {
		this.superintendencia = superintendencia;
	}

}
