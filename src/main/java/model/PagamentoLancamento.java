package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "pagamento_lancamento")
public class PagamentoLancamento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private LancamentoAcao lancamentoAcao;
	@Temporal(TemporalType.DATE)
	private Date dataEmissao;
	@Temporal(TemporalType.DATE)
	private Date dataPagamento;
	@Column(precision = 10, scale = 2)
	private BigDecimal valor;
	@Enumerated(EnumType.STRING)
	private StatusPagamentoLancamento stt;
	@Column(name = "quantidade_parcela")
	private Integer quantidadeParcela;
	@Column
	private Integer numeroDaParcela;
	@Enumerated(EnumType.STRING)
	private TipoParcelamento tipoParcelamento;

	@Enumerated(EnumType.STRING)
	private DespesaReceita despesaReceita;

	@ManyToOne
	private ContaBancaria conta;

	@ManyToOne
	private ContaBancaria contaRecebedor;
	

	@Column
	private String tipoLancamento = "";
	
	@Column
	private Boolean reclassificado;
	
	@Column
	private String tipoContaRecebedor;
	
	@Column
	private String tipoContaPagador;
	
	
	@OneToMany(mappedBy = "pagamentoLancamento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Imposto> impostos = new ArrayList<Imposto>();

	public PagamentoLancamento() {
	}

	public PagamentoLancamento(Long id, LancamentoAcao lancamentoAcao, Date dataEmissao,
			Date dataPagamento, BigDecimal valor, StatusPagamentoLancamento stt) {
		super();
		this.id = id;
		this.lancamentoAcao = lancamentoAcao;
		this.dataEmissao = dataEmissao;
		this.dataPagamento = dataPagamento;
		this.valor = valor;
		this.stt = stt;
	}

	// public List<PagamentoLancamento> getPagamentosINLancamento(Long id){
	// StringBuilder jpql = new StringBuilder("SELECT NEW ");
	// jpql.append(" PagamentoLancamento(pa.id, pa.valor, pa.dataPagamento,");
	// jpql.append("pa.numeroDaParcela, pa.quantidadeParcela,");
	// jpql.append("pa.conta.nomeConta, pa.contaRecebedor.nomeConta
	// ,pa.lancamentoAcao.acao.codigo,");
	// jpql.append("pa.lancamentoAcao.fontePagadora.nome) FROM
	// PagamentoLancamento pa where pa.lancamento.id = :id");
	// Query query = this.manager.createQuery(jpql.toString());
	// query.setParameter("id", id);
	// return query.getResultList().size() > 0 ? query.getResultList() : new
	// ArrayList<>();
	// }

	
	@Transient
	private String nomePagador = "";
	
	@Transient
	private String nomerecebedor = "";
	
	@Transient
	private String codigoAcao = "";

	@Transient
	private String fontePagadora = "";
	
	public PagamentoLancamento(Long id, BigDecimal valor, Date dataPagameno, Integer numeroDaParcela,
			Integer quantidadeParcela, String nomePagador, String nomeRecebedor, String codigoAcao, String fontePagadora) {
		
		this.id = id;
		this.valor = valor;
		this.dataPagamento = dataPagameno;
		this.numeroDaParcela = numeroDaParcela;
		this.quantidadeParcela = quantidadeParcela;
		this.nomePagador = nomePagador;
		this.nomerecebedor =  nomeRecebedor;
		this.codigoAcao = codigoAcao;
		this.fontePagadora = fontePagadora;
	
	}

	public PagamentoLancamento(PagamentoLancamento pagamento, Integer numeroParcela) {
		this.lancamentoAcao = pagamento.getLancamentoAcao();
		this.quantidadeParcela = pagamento.quantidadeParcela;
		this.numeroDaParcela = numeroParcela;
		this.valor = new BigDecimal(pagamento.getValor().doubleValue() / quantidadeParcela);
		this.dataEmissao = pagamento.getDataEmissao();
		this.tipoParcelamento = pagamento.getTipoParcelamento();
		this.conta = pagamento.getConta();
		this.stt = pagamento.getStt();
		this.contaRecebedor = pagamento.getContaRecebedor();
		this.tipoLancamento = pagamento.getTipoLancamento();
		this.despesaReceita = pagamento.getDespesaReceita();
		this.tipoContaPagador = pagamento.getConta().getTipo();
		this.tipoContaRecebedor = pagamento.getContaRecebedor() != null ? pagamento.getContaRecebedor().getTipo() : null;

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
		PagamentoLancamento other = (PagamentoLancamento) obj;
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

	public LancamentoAcao getLancamentoAcao() {
		return lancamentoAcao;
	}

	public void setLancamentoAcao(LancamentoAcao lancamentoAcao) {
		this.lancamentoAcao = lancamentoAcao;
	}


	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public StatusPagamentoLancamento getStt() {
		return stt;
	}

	public void setStt(StatusPagamentoLancamento stt) {
		this.stt = stt;
	}

	public ContaBancaria getConta() {
		return conta;
	}

	public void setConta(ContaBancaria conta) {
		this.conta = conta;
	}

	public Integer getQuantidadeParcela() {
		return quantidadeParcela;
	}

	public void setQuantidadeParcela(Integer quantidadeParcela) {
		this.quantidadeParcela = quantidadeParcela;
	}

	public Integer getNumeroDaParcela() {
		return numeroDaParcela;
	}

	public void setNumeroDaParcela(Integer numeroDaParcela) {
		this.numeroDaParcela = numeroDaParcela;
	}

	public TipoParcelamento getTipoParcelamento() {
		return tipoParcelamento;
	}

	public void setTipoParcelamento(TipoParcelamento tipoParcelamento) {
		this.tipoParcelamento = tipoParcelamento;
	}

	public ContaBancaria getContaRecebedor() {
		return contaRecebedor;
	}

	public void setContaRecebedor(ContaBancaria contaRecebedor) {
		this.contaRecebedor = contaRecebedor;
	}

	public String getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(String tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	public DespesaReceita getDespesaReceita() {
		return despesaReceita;
	}

	public void setDespesaReceita(DespesaReceita despesaReceita) {
		this.despesaReceita = despesaReceita;
	}

	public String getNomePagador() {
		return nomePagador;
	}

	public void setNomePagador(String nomePagador) {
		this.nomePagador = nomePagador;
	}

	public String getNomerecebedor() {
		return nomerecebedor;
	}

	public void setNomerecebedor(String nomerecebedor) {
		this.nomerecebedor = nomerecebedor;
	}

	public String getCodigoAcao() {
		return codigoAcao;
	}

	public void setCodigoAcao(String codigoAcao) {
		this.codigoAcao = codigoAcao;
	}

	public String getFontePagadora() {
		return fontePagadora;
	}

	public void setFontePagadora(String fontePagadora) {
		this.fontePagadora = fontePagadora;
	}

	public Boolean getReclassificado() {
		return reclassificado;
	}

	public void setReclassificado(Boolean reclassificado) {
		this.reclassificado = reclassificado;
	}

	public String getTipoContaRecebedor() {
		return tipoContaRecebedor;
	}

	public void setTipoContaRecebedor(String tipoContaRecebedor) {
		this.tipoContaRecebedor = tipoContaRecebedor;
	}

	public String getTipoContaPagador() {
		return tipoContaPagador;
	}

	public void setTipoContaPagador(String tipoContaPagador) {
		this.tipoContaPagador = tipoContaPagador;
	}

	public List<Imposto> getImpostos() {
		return impostos;
	}

	public void setImpostos(List<Imposto> impostos) {
		this.impostos = impostos;
	}

}
