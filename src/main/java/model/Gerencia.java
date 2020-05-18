package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("gerencia")
public class Gerencia extends Gestao{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	private Superintendencia superintendencia;
	
	public Gerencia(){
		
	}
	
	public Gerencia(Long id, String nome, String type){
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
