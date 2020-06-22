package model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
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
@Table(name = "item_pedido")
public class ItemPedido implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Cotacao cotacao;
	@Column(name = "item_compra_id")
	private Long idItemCompra;
	@Column(name = "valor_unitario_com_desconto", precision = 10, scale = 2)
	private BigDecimal valorUnitarioComDesconto;
	@Column(name = "valor_unitario_sem_desconto", precision = 10, scale = 2)
	private BigDecimal valorUnitario;
	@Column
	private Double quantidade;
	@ManyToOne
	private Pedido pedido;
	@Column
	private String descricaoProduto;
	@Column
	private Boolean cancelado;
	@Column
	private String descricaoComplementar;
	@Enumerated(EnumType.STRING)
	private TipoGestao tipoGestao;
	@ManyToOne
	private Gestao gestao;
	@Enumerated(EnumType.STRING)
	private TipoLocalidade tipoLocalidade;
	@ManyToOne
	private Localidade localidade;
	@Column
	private String unidade;
	@Column
	private String tipo;
	@Column
	private String categoria;
	@Transient
	private Long idCotacao;
	@Transient
	private BigDecimal valorTotal;
	@Transient
	private BigDecimal valorTotalPedido;
	@Transient
	private String dataEmissaoPedido;
	@Transient
	private Boolean bootUrgencia;
	@Transient
	private String dataEmissaoSC;
	@Transient
	private String dataConclusaoSC;
	@Transient
	private String statusCompra;
	@Transient
	private String dataAprovacao;
	@Transient
	private String nomeSolicitante;
	@Transient
	private String nomeGestao;
	@Transient
	private String nomeComprador;
	@Transient
	private String nomeDestino;
	@Transient
	private String categoriaSC;
	@Transient
	private String nomeFornecedor;
	@Transient
	private Long idSC;
	@Transient
	private String notaFiscal;
	@Transient
	private String dataPagamento;
	@Transient
	private Long idPedido;

	@Transient
	private String orcamento;

	@Transient
	private String componente;

	@Transient
	private String subComponente;

	@Transient
	private String rubrica;

	@Transient
	private String descricaoProdutoComplementar;

	@Transient
	private String fonte;

	@Transient
	private String projeto;

	public String getDescricaoProdutoComplementar() {
		return descricaoProdutoComplementar;
	}

	public void setDescricaoProdutoComplementar(String descricaoProdutoComplementar) {
		this.descricaoProdutoComplementar = descricaoProdutoComplementar;
	}

	public String getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(String orcamento) {
		this.orcamento = orcamento;
	}

	public String getComponente() {
		return componente;
	}

	public void setComponente(String componente) {
		this.componente = componente;
	}

	public String getSubComponente() {
		return subComponente;
	}

	public void setSubComponente(String subComponente) {
		this.subComponente = subComponente;
	}

	public String getRubrica() {
		return rubrica;
	}

	public void setRubrica(String rubrica) {
		this.rubrica = rubrica;
	}

	public String getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(String notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	public String getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(String dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public ItemPedido() {
	}

	public ItemPedido(Long id, String descricaoProduto, Double quantidade, BigDecimal valorUnitario,
			BigDecimal valorUnitarioComDesconto, String unidade, String descricaoComplementar) {
		this.id = id;
		this.descricaoProduto = descricaoProduto;
		this.quantidade = quantidade;
		this.valorUnitario = valorUnitario;
		this.valorUnitarioComDesconto = valorUnitarioComDesconto;
		valorTotal = BigDecimal.valueOf(quantidade.doubleValue()).multiply(this.valorUnitario);
		this.unidade = unidade;
		this.descricaoComplementar = descricaoComplementar;
	}

	public ItemPedido(Long id, Double quantidade) {
		this.id = id;
		this.quantidade = quantidade;
	}

	public ItemPedido(String descricao) {
		descricaoProduto = descricao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	public Cotacao getCotacao() {
		return cotacao;
	}

	public void setCotacao(Cotacao cotacao) {
		this.cotacao = cotacao;
	}

	public Long getIdItemCompra() {
		return idItemCompra;
	}

	public void setIdItemCompra(Long idItemCompra) {
		this.idItemCompra = idItemCompra;
	}

	public Long getIdCotacao() {
		return idCotacao;
	}

	public void setIdCotacao(Long idCotacao) {
		this.idCotacao = idCotacao;
	}

	public TipoGestao getTipoGestao() {
		return tipoGestao;
	}

	public void setTipoGestao(TipoGestao tipoGestao) {
		this.tipoGestao = tipoGestao;
	}

	public Gestao getGestao() {
		return gestao;
	}

	public void setGestao(Gestao gestao) {
		this.gestao = gestao;
	}

	public TipoLocalidade getTipoLocalidade() {
		return tipoLocalidade;
	}

	public void setTipoLocalidade(TipoLocalidade tipoLocalidade) {
		this.tipoLocalidade = tipoLocalidade;
	}

	public Localidade getLocalidade() {
		return localidade;
	}

	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public BigDecimal getValorUnitarioComDesconto() {
		return valorUnitarioComDesconto;
	}

	public void setValorUnitarioComDesconto(BigDecimal valorUnitarioComDesconto) {
		this.valorUnitarioComDesconto = valorUnitarioComDesconto;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
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

	public String getDataAprovacao() {
		return dataAprovacao;
	}

	public void setDataAprovacao(String dataAprovacao) {
		this.dataAprovacao = dataAprovacao;
	}

	public String getDataEmissaoPedido() {
		return dataEmissaoPedido;
	}

	public void setDataEmissaoPedido(String dataEmissaoPedido) {
		this.dataEmissaoPedido = dataEmissaoPedido;
	}

	public String getDataEmissaoSC() {
		return dataEmissaoSC;
	}

	public void setDataEmissaoSC(String dataEmissaoSC) {
		this.dataEmissaoSC = dataEmissaoSC;
	}

	public Long getIdSC() {
		return idSC;
	}

	public void setIdSC(Long idSC) {
		this.idSC = idSC;
	}

	public Long getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

	public String getNomeSolicitante() {
		return nomeSolicitante;
	}

	public void setNomeSolicitante(String nomeSolicitante) {
		this.nomeSolicitante = nomeSolicitante;
	}

	public String getCategoriaSC() {
		return categoriaSC;
	}

	public void setCategoriaSC(String categoriaSC) {
		this.categoriaSC = categoriaSC;
	}

	public String getStatusCompra() {
		return statusCompra;
	}

	public void setStatusCompra(String statusCompra) {
		this.statusCompra = statusCompra;
	}

	public String getNomeGestao() {
		return nomeGestao;
	}

	public void setNomeGestao(String nomeGestao) {
		this.nomeGestao = nomeGestao;
	}

	public String getNomeComprador() {
		return nomeComprador;
	}

	public void setNomeComprador(String nomeComprador) {
		this.nomeComprador = nomeComprador;
	}

	public String getNomeDestino() {
		return nomeDestino;
	}

	public void setNomeDestino(String nomeDestino) {
		this.nomeDestino = nomeDestino;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
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

	public String getDataConclusaoSC() {
		return dataConclusaoSC;
	}

	public void setDataConclusaoSC(String dataConclusaoSC) {
		this.dataConclusaoSC = dataConclusaoSC;
	}

	public Boolean getCancelado() {
		return cancelado;
	}

	public void setCancelado(Boolean cancelado) {
		this.cancelado = cancelado;
	}

	public BigDecimal getValorTotalPedido() {
		return valorTotalPedido;
	}

	public void setValorTotalPedido(BigDecimal valorTotalPedido) {
		this.valorTotalPedido = valorTotalPedido;
	}

	public String getFonte() {
		return fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	public String getProjeto() {
		return projeto;
	}

	public void setProjeto(String projeto) {
		this.projeto = projeto;
	}
}