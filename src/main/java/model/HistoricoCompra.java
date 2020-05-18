package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="historico_compra")
public class HistoricoCompra implements Serializable{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Colaborador colaborador;
	@Enumerated(EnumType.STRING)
	private StatusCompra statusCompra;
	@ManyToOne
	private Compra compra;
	@Column
	private String observacao;
	
	public HistoricoCompra(){}
	
	

	

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
		HistoricoCompra other = (HistoricoCompra) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}





	public Long getId() {
		return id;
	}





	public void setId(Long id) {
		this.id = id;
	}





	public Colaborador getColaborador() {
		return colaborador;
	}





	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}





	public StatusCompra getStatusCompra() {
		return statusCompra;
	}





	public void setStatusCompra(StatusCompra statusCompra) {
		this.statusCompra = statusCompra;
	}





	public Compra getCompra() {
		return compra;
	}





	public void setCompra(Compra compra) {
		this.compra = compra;
	}





	public String getObservacao() {
		return observacao;
	}





	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}




	
}
