package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("polo")
public class Polo extends Localidade{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public Polo(){}
	
}
