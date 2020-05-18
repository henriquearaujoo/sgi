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
import javax.persistence.Transient;

@Entity
@Table(name = "produto_florestal_nao_madeireiro_familiar")
public class ProdutoFlorestalNaoMadeireiroFamiliar implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private ProdutoFlorestalNaoMadeireiro produto;
	
	//Tipo de ambiente
	@Column
	private Boolean varzea;
	
	@Column
	private Boolean terraFirme;
	
	//Tipo de uso
	@Column
	private Boolean consumo;
	
	@Column
	private Boolean venda;
	
	@Column
	private String cliente;
	
	@Column
	private Boolean vendidoEmConjunto;
	
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

	public ProdutoFlorestalNaoMadeireiro getProduto() {
		return produto;
	}

	public void setProduto(ProdutoFlorestalNaoMadeireiro produto) {
		this.produto = produto;
	}

	public Boolean getVarzea() {
		return varzea;
	}

	public void setVarzea(Boolean varzea) {
		this.varzea = varzea;
	}

	public Boolean getTerraFirme() {
		return terraFirme;
	}

	public void setTerraFirme(Boolean terraFirme) {
		this.terraFirme = terraFirme;
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
	
	
}
