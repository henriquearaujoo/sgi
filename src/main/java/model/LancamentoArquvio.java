package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import util.Arquivo;

@Entity
@Table(name = "lancamento_arquivo")
public class LancamentoArquvio implements Serializable{

	
	/**
	 * Desconsidetar essa classe ela foi criada erroneamente
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Lancamento lancamento;
	@ManyToOne
	private Arquivo arquivo;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	public LancamentoArquvio(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	
	
	
}
