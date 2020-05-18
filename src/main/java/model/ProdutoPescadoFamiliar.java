package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "produto_pescado_familiar")
public class ProdutoPescadoFamiliar implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private ProdutoPescado produto;
	
	//Tipo de uso
	@Column
	private Boolean consumo;
	
	@Column
	private Boolean venda;
	
	@Column
	private String cliente;
	
	@Column
	private Boolean vendidoEmConjunto;
	
	@Column
	private Boolean evitaPescar;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFim;
	
	@ManyToOne
	private Familiar familiar;
	
	@Transient
	private Boolean selecionado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Familiar getFamiliar() {
		return familiar;
	}

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	public Boolean getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	
	public Boolean getVendidoEmConjunto() {
		return vendidoEmConjunto;
	}

	public void setVendidoEmConjunto(Boolean vendidoEmConjunto) {
		this.vendidoEmConjunto = vendidoEmConjunto;
	}

	public ProdutoPescado getProduto() {
		return produto;
	}

	public void setProduto(ProdutoPescado produto) {
		this.produto = produto;
	}

	public Boolean getConsumo() {
		return consumo;
	}

	public void setConsumo(Boolean consumo) {
		this.consumo = consumo;
	}

	public Boolean getVenda() {
		return venda;
	}

	public void setVenda(Boolean venda) {
		this.venda = venda;
	}

	public Boolean getEvitaPescar() {
		return evitaPescar;
	}

	public void setEvitaPescar(Boolean evitaPescar) {
		this.evitaPescar = evitaPescar;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	
}
