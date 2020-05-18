package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("sede")
public class Sede extends Localidade{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	
}
