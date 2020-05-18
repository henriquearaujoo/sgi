package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("sup")
public class Superintendencia extends Gestao{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Superintendencia(){
		super();
	}
	
	public Superintendencia(Long id, String nome, String type){
		super(id, nome, type);
	}
	
	
	

}
