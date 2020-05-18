package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class LogAprovacao extends Log implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	@ManyToOne
	private Lancamento lancamento;
	
	@ManyToOne
	private SolicitacaoViagem solicitacaoViagem;
	
	
	
	
	
	
	
}
