package model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "cotacao")
public class Cotacao implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Fornecedor fornecedor;
	@Column(precision = 10, scale = 2)
	private BigDecimal valor;
	@Column(name = "valor_desconto", precision = 10, scale = 2)
	private BigDecimal valorDesconto;
	@ManyToOne
	private Compra compra;
	
	
	@ManyToOne
	private ItemCompra itemCompra;
	@Column
	private Double quantidade;
	
	@Column
	private Boolean houvePedido;
	
	@Column
	private Boolean houveEscolha;
	
	
	
	
	public Cotacao(){
		valor = BigDecimal.ZERO;
		valorDesconto = BigDecimal.ZERO;
	}

	public Long getId() {
		return id;
	}
	
	public Cotacao(Long id, BigDecimal valorDesconto, BigDecimal valor){
		
		this.id = id;
		this.valorDesconto = valorDesconto;
		this.valor = valor;
		
	}
	
	public Cotacao(Long id,Boolean houveEscolha){
		
		this.id = id;
		this.houveEscolha = houveEscolha;
		
		
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
		Cotacao other = (Cotacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public ItemCompra getItemCompra() {
		return itemCompra;
	}

	public void setItemCompra(ItemCompra itemCompra) {
		this.itemCompra = itemCompra;
	}

	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public Boolean getHouvePedido() {
		return houvePedido;
	}

	public void setHouvePedido(Boolean houvePedido) {
		this.houvePedido = houvePedido;
	}
	
	public Boolean getHouveEscolha() {
		return houveEscolha;
	}

	public void setHouveEscolha(Boolean houveEscolha) {
		this.houveEscolha = houveEscolha;
	}
	

}
