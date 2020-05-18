package model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "imposto")
public class Imposto implements Serializable{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@ManyToOne
	private PagamentoLancamento pagamentoLancamento;
	
	@ManyToOne
	private GuiaImposto guia;
	
	@Enumerated(EnumType.STRING)
	private TipoImposto tipo;
	
	@Column(name = "valor", precision = 10, scale = 2)
	private BigDecimal valor;
	
	public Imposto(){}
	
//	public Imposto(Long id, Long idLancamento,BigDecimal valor, TipoImposto tipo) {
//		this.id = id;
//		this.lancamento = new Lancamento();
//		this.lancamento.setId(idLancamento);
//		this.valor = valor;
//		this.tipo = tipo;
//	}
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Imposto other = (Imposto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public TipoImposto getTipo() {
		return tipo;
	}

	public void setTipo(TipoImposto tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public GuiaImposto getGuia() {
		return guia;
	}

	public void setGuia(GuiaImposto guia) {
		this.guia = guia;
	}

	public PagamentoLancamento getPagamentoLancamento() {
		return pagamentoLancamento;
	}

	public void setPagamentoLancamento(PagamentoLancamento pagamentoLancamento) {
		this.pagamentoLancamento = pagamentoLancamento;
	}	
	
}
