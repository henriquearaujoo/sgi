package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "produto")
public class Produto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 990114009967074119L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column
	private String ncm;
	@Column(name = "descricao", length=2048)
	private String descricao;
	@Column
	private String tipo;
	@Column
	private  String categoria;
	@Column
	private String unidadeDeMedida;
	@Column
	private String marca; 
	
	@Column
	private Boolean inativo;

	
	@Column
	private Boolean saneado;
	
	@Column
	private Boolean preCadastro;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPrecadasto;
	
	@Enumerated(EnumType.STRING)
	private TipoProduto tipoProduto;
	
	@Enumerated(EnumType.STRING)
	private UnidadeMedida unidadeMedidaEnum;
		
	@ManyToOne
	private UnidadeDeCompra unidadeDeCompra;
	
	@ManyToOne
	private CategoriaDespesaClass categoriaDespesa;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;
	
	@Column
	private Long idUsuario;
	
	@OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ObservacaoProduto> listObservacao = new ArrayList<>();
	
	public  Produto(){}

	

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNcm() {
		return ncm;
	}


	public void setNcm(String ncm) {
		this.ncm = ncm;
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
		Produto other = (Produto) obj;
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




	public String getMarca() {
		return marca;
	}




	public void setMarca(String marca) {
		this.marca = marca;
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



	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}



	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}



	public Date getDataCriacao() {
		return dataCriacao;
	}



	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}



	public Boolean getPreCadastro() {
		return preCadastro;
	}



	public void setPreCadastro(Boolean preCadastro) {
		this.preCadastro = preCadastro;
	}



	public Date getDataPrecadasto() {
		return dataPrecadasto;
	}



	public void setDataPrecadasto(Date dataPrecadasto) {
		this.dataPrecadasto = dataPrecadasto;
	}



	public Long getIdUsuario() {
		return idUsuario;
	}



	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}



	public List<ObservacaoProduto> getListObservacao() {
		return listObservacao;
	}



	public void setListObservacao(List<ObservacaoProduto> listObservacao) {
		this.listObservacao = listObservacao;
	}



	public Boolean getSaneado() {
		return saneado;
	}



	public void setSaneado(Boolean saneado) {
		this.saneado = saneado;
	}
	
	
	
	
	
	
	
}
