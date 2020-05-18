package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("entrega")
public class Entregas extends Atividade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
