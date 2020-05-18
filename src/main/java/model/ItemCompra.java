package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import service.CompraService;
import util.CDILocator;

@Entity
@Table(name = "item_compra")
public class ItemCompra implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private Double quantidade;
	@ManyToOne
	private Produto produto;
	@ManyToOne
	private Compra compra;
	
	@Column
	private String unidade;
	
	@Column
	private Boolean cancelado;
	
	@Column
	private UnidadeDeCompra unidadeM;
	
	
	@Transient
	private String tipo;
	@Transient
	private String categoria;
	@Transient
	private String descricao;
	
	@Column
	private String descricaoComplementar;
	
//	@ManyToOne
//	private LancamentoAcao lancamentoAcao;
	
	@Transient
	private Boolean bootUrgencia;
	
	@Transient
	private BigDecimal valor;
	@Transient
	private BigDecimal valorDesconto;	
	
	@Transient
	private BigDecimal t1 = BigDecimal.ZERO;
	@Transient
	private BigDecimal t2 = BigDecimal.ZERO;
	@Transient
	private BigDecimal t3 = BigDecimal.ZERO;
	@Transient
	private BigDecimal t4 = BigDecimal.ZERO;
	@Transient
	private BigDecimal t5 = BigDecimal.ZERO;
	@Transient
	private BigDecimal t6 = BigDecimal.ZERO;
	
	
	@Transient
	private BigDecimal p1 = BigDecimal.ZERO;
	@Transient
	private BigDecimal p2 = BigDecimal.ZERO;
	@Transient
	private BigDecimal p3 = BigDecimal.ZERO;
	@Transient
	private BigDecimal p4 = BigDecimal.ZERO;
	@Transient
	private BigDecimal p5 = BigDecimal.ZERO;
	@Transient
	private BigDecimal p6 = BigDecimal.ZERO;
	
	
	
	@Transient
	private Long sc;
	@Transient
	private Date dataEmissaoCompra;
	@Transient
	private String categoriaItem;
	@Transient
	private String observacaoSC;
	@Transient
	private String gestaoSC;
	@Transient
	private String destinoSC;
	@Transient
	private String descricaoItem;
	@Transient
	private String solicitante;
	@Transient
	private Date dataAprovacao;
	@Transient
	private String aprovador;
	@Transient
	private StatusCompra statusCompra;	
	
	public ItemCompra(){}
	
	@Transient
	private CompraService compraService = CDILocator.getBean(CompraService.class);
	
	public ItemCompra(Long id, String descricao, Double quantidade,String categoria,String tipo, String unidade){
		this.id = id;
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.categoria = categoria;
		this.tipo = tipo;
		this.unidade = unidade;
	}
	
	public ItemCompra(Long id, String descricao, Double quantidade,String categoria,String tipo, String unidade, String descricaoComplementar){
		this.id = id;
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.categoria = categoria;
		this.tipo = tipo;
		this.unidade = unidade;
		this.descricaoComplementar = descricaoComplementar;
	}
	
	public ItemCompra(Long sc, Date dataEmissaoCompra, String categoriaItem,
			String observacaoSC, StatusCompra status, String gestaoSC, String destinoSC, Double quantidade, String unidade,String descricaoItem,
			String solicitante, Date dataAprovacao, String aprovador) {
		this.sc = sc;
		this.dataEmissaoCompra = dataEmissaoCompra;
		this.categoriaItem = categoriaItem;
		this.observacaoSC = observacaoSC;
		this.statusCompra = status;
		this.gestaoSC = gestaoSC;
		this.destinoSC = destinoSC;
		this.descricaoItem = descricaoItem;
		this.quantidade = quantidade;
		this.unidade = unidade;
		this.solicitante = solicitante;
		this.dataAprovacao = dataAprovacao;
		this.aprovador = aprovador;
	}
	
	public ItemCompra(Long sc, Date dataEmissaoCompra, String categoriaItem,
			String observacaoSC, StatusCompra status, String gestaoSC, String destinoSC, Double quantidade, String unidade,String descricaoItem,
			String solicitante, String aprovador, Boolean bootUrgencia) {
		this.sc = sc;
		this.dataEmissaoCompra = dataEmissaoCompra;
		this.categoriaItem = categoriaItem;
		this.observacaoSC = observacaoSC;
		this.statusCompra = status;
		this.gestaoSC = gestaoSC;
		this.destinoSC = destinoSC;
		this.descricaoItem = descricaoItem;
		this.quantidade = quantidade;
		this.unidade = unidade;
		this.solicitante = solicitante;
		this.aprovador = aprovador;
		this.bootUrgencia = bootUrgencia;
//		Date data = compraService.getDataAprovacao(sc);
//		if (data != null) {
//			this.dataAprovacao = data;
//		}
		
	}
	
	public ItemCompra(Long sc, Date dataEmissaoCompra, String categoriaItem,
			String observacaoSC, StatusCompra status, String gestaoSC, String destinoSC, Double quantidade, String unidade,String descricaoItem,
			String solicitante, String aprovador, Boolean bootUrgencia, String tipo) {
		this.sc = sc;
		this.dataEmissaoCompra = dataEmissaoCompra;
		this.categoriaItem = categoriaItem;
		this.observacaoSC = observacaoSC;
		this.statusCompra = status;
		this.gestaoSC = gestaoSC;
		this.destinoSC = destinoSC;
		this.descricaoItem = descricaoItem;
		this.quantidade = quantidade;
		this.unidade = unidade;
		this.solicitante = solicitante;
		this.aprovador = aprovador;
		this.bootUrgencia = bootUrgencia;
		this.tipo = tipo;
//		Date data = compraService.getDataAprovacao(sc);
//		if (data != null) {
//			this.dataAprovacao = data;
//		}
		
	}

	public ItemCompra(Long id,Double quantidade){
		this.id = id;
		this.quantidade = quantidade;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public Compra getCompra() {
		return compra;
	}
	public void setCompra(Compra compra) {
		this.compra = compra;
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
		ItemCompra other = (ItemCompra) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public Double getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getP1() {
		return p1;
	}

	public void setP1(BigDecimal p1) {
		this.p1 = p1;
	}

	public BigDecimal getP2() {
		return p2;
	}

	public void setP2(BigDecimal p2) {
		this.p2 = p2;
	}

	public BigDecimal getP3() {
		return p3;
	}

	public void setP3(BigDecimal p3) {
		this.p3 = p3;
	}

	public BigDecimal getP4() {
		return p4;
	}

	public void setP4(BigDecimal p4) {
		this.p4 = p4;
	}

	public BigDecimal getP5() {
		return p5;
	}

	public void setP5(BigDecimal p5) {
		this.p5 = p5;
	}

	public BigDecimal getP6() {
		return p6;
	}

	public void setP6(BigDecimal p6) {
		this.p6 = p6;
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

	public BigDecimal getT1() {
		return t1;
	}

	public void setT1(BigDecimal t1) {
		this.t1 = t1;
	}

	public BigDecimal getT2() {
		return t2;
	}

	public void setT2(BigDecimal t2) {
		this.t2 = t2;
	}

	public BigDecimal getT3() {
		return t3;
	}

	public void setT3(BigDecimal t3) {
		this.t3 = t3;
	}

	public BigDecimal getT4() {
		return t4;
	}

	public void setT4(BigDecimal t4) {
		this.t4 = t4;
	}

	public BigDecimal getT5() {
		return t5;
	}

	public void setT5(BigDecimal t5) {
		this.t5 = t5;
	}

	public BigDecimal getT6() {
		return t6;
	}

	public void setT6(BigDecimal t6) {
		this.t6 = t6;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public Date getDataEmissaoCompra() {
		return dataEmissaoCompra;
	}

	public void setDataEmissaoCompra(Date dataEmissaoCompra) {
		this.dataEmissaoCompra = dataEmissaoCompra;
	}

	public String getCategoriaItem() {
		return categoriaItem;
	}

	public void setCategoriaItem(String categoriaItem) {
		this.categoriaItem = categoriaItem;
	}

	public String getObservacaoSC() {
		return observacaoSC;
	}

	public void setObservacaoSC(String observacaoSC) {
		this.observacaoSC = observacaoSC;
	}

	public String getGestaoSC() {
		return gestaoSC;
	}

	public void setGestaoSC(String gestaoSC) {
		this.gestaoSC = gestaoSC;
	}

	public String getDestinoSC() {
		return destinoSC;
	}

	public void setDestinoSC(String destinoSC) {
		this.destinoSC = destinoSC;
	}

	public String getDescricaoItem() {
		return descricaoItem;
	}

	public void setDescricaoItem(String descricaoItem) {
		this.descricaoItem = descricaoItem;
	}

	public String getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}

	public Date getDataAprovacao() {
		return dataAprovacao;
	}

	public void setDataAprovacao(Date dataAprovacao) {
		this.dataAprovacao = dataAprovacao;
	}

	public String getAprovador() {
		return aprovador;
	}

	public void setAprovador(String aprovador) {
		this.aprovador = aprovador;
	}

	public StatusCompra getStatusCompra() {
		return statusCompra;
	}

	public void setStatusCompra(StatusCompra statusCompra) {
		this.statusCompra = statusCompra;
	}

	public Long getSc() {
		return sc;
	}

	public void setSc(Long sc) {
		this.sc = sc;
	}

	public Boolean getBootUrgencia() {
		return bootUrgencia;
	}

	public void setBootUrgencia(Boolean bootUrgencia) {
		this.bootUrgencia = bootUrgencia;
	}

	public String getDescricaoComplementar() {
		return descricaoComplementar;
	}

	public void setDescricaoComplementar(String descricaoComplementar) {
		this.descricaoComplementar = descricaoComplementar;
	}

	public Boolean getCancelado() {
		return cancelado;
	}

	public void setCancelado(Boolean cancelado) {
		this.cancelado = cancelado;
	}

	public UnidadeDeCompra getUnidadeM() {
		return unidadeM;
	}

	public void setUnidadeM(UnidadeDeCompra unidadeM) {
		this.unidadeM = unidadeM;
	}
	
}
