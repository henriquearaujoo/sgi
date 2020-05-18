package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Cidade")
public class Cidade extends Localidade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Cidade() {

	};

}
