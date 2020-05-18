package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("nuc")
public class Nucleo extends Localidade{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	
}
