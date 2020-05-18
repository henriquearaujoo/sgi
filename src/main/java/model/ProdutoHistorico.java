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

@Entity
@Table(name = "produto_historico")
public class ProdutoHistorico implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 990114009967074119L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(name = "descricao", length=2048)
	private String descricao;
	@Column
	private String tipo;
	@Column
	private  String categoria;
	@Column
	private String unidadeDeMedida;
	@Column
	private Boolean inativo;
	@Enumerated(EnumType.STRING)
	private TipoProduto tipoProduto;
	@Enumerated(EnumType.STRING)
	private UnidadeMedida unidadeMedidaEnum;
	@ManyToOne
	private UnidadeDeCompra unidadeDeCompra;
	@ManyToOne
	private CategoriaDespesaClass categoriaDespesa;
	@Column
	private Long idProduto; 
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEdicao;
	
	@ManyToOne
	private User usuario;
	
	
	public ProdutoHistorico(Produto produto, User usuario){
		if (produto.getId() != null) 
		this.idProduto =  produto.getId();
		if (produto.getDescricao() != null)
		this.descricao = produto.getDescricao();
		if (produto.getTipo() != null)
		this.tipo = produto.getTipo();
		if (produto.getUnidadeDeCompra() != null)
		this.unidadeDeCompra = produto.getUnidadeDeCompra();
		if (produto.getCategoriaDespesa() != null)
		this.categoriaDespesa = produto.getCategoriaDespesa();
		if (produto.getInativo() != null)
		this.inativo = produto.getInativo();
		
		this.usuario = usuario;
			
		this.dataEdicao = new Date();
	
	}
	
	public  ProdutoHistorico(){}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public String getCategoria() {
		return categoria;
	}


	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}




	public String getTipo() {
		return tipo;
	}




	public void setTipo(String tipo) {
		this.tipo = tipo;
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
		ProdutoHistorico other = (ProdutoHistorico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}




	public String getUnidadeDeMedida() {
		return unidadeDeMedida;
	}




	public void setUnidadeDeMedida(String unidadeDeMedida) {
		this.unidadeDeMedida = unidadeDeMedida;
	}


	public CategoriaDespesaClass getCategoriaDespesa() {
		return categoriaDespesa;
	}


	public void setCategoriaDespesa(CategoriaDespesaClass categoriaDespesa) {
		this.categoriaDespesa = categoriaDespesa;
	}



	public UnidadeMedida getUnidadeMedidaEnum() {
		return unidadeMedidaEnum;
	}



	public void setUnidadeMedidaEnum(UnidadeMedida unidadeMedidaEnum) {
		this.unidadeMedidaEnum = unidadeMedidaEnum;
	}



	public UnidadeDeCompra getUnidadeDeCompra() {
		return unidadeDeCompra;
	}



	public void setUnidadeDeCompra(UnidadeDeCompra unidadeDeCompra) {
		this.unidadeDeCompra = unidadeDeCompra;
	}



	public Boolean getInativo() {
		return inativo;
	}



	public void setInativo(Boolean inativo) {
		this.inativo = inativo;
	}

	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}

	public Date getDataEdicao() {
		return dataEdicao;
	}

	public void setDataEdicao(Date dataEdicao) {
		this.dataEdicao = dataEdicao;
	}

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}
	
	
	
	
	
	
	
}
