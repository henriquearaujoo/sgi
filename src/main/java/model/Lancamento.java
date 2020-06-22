package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import util.ArquivoLancamento;

@Entity
@Table(name = "lancamento")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public  class Lancamento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	@Enumerated(EnumType.STRING)
	private TipoGestao tipoGestao;
	@ManyToOne
	private Gestao gestao;
	@Enumerated(EnumType.STRING) 
	private TipoLocalidade tipoLocalidade;
	@ManyToOne
	private Localidade localidade;
	@Column
	private String codigo;
	
	@Column
	private Boolean bootUrgencia;
	
	@Column(name = "data_emissao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEmissao;
	
	@Enumerated(EnumType.STRING)
	private StatusAdiantamento statusAdiantamento;

	@Column
	private String versionLancamento;
	
	@Column(name = "tipo_v4")
	private String tipov4;
	
	
	@Column(name = "data_doc_fiscal")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataDocumentoFiscal;
	
	@Column(name = "data_conclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataConlusao;
	
	@ManyToOne
	private CategoriaDespesaClass CategoriaDespesaClass;
	
	@ManyToOne
	private CategoriaFinanceira categoriaFinanceira;
	
	@Transient
	private CategoriadeDespesa categoriaDespesa;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_documento_fiscal")
	private TipoDeDocumentoFiscal tipoDocumentoFiscal;
	
	@OneToMany(mappedBy = "lancamento", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LancamentoAcao> lancamentosAcoes = new ArrayList<LancamentoAcao>();
	
	@OneToMany(mappedBy = "lancamento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ArquivoLancamento> arquivos = new ArrayList<ArquivoLancamento>();
	
	@OneToMany(mappedBy = "lancamento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<LogStatus> logs = new ArrayList<LogStatus>();
	
	
	@Column
	private Long idReembolso;
	
	@Column
	private Long idTarifado;

	@Column
	private Boolean  reembolsado;
	
	@Column(name = "data_validacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataValidacao;
	
	
	
	public Date getDataValidacao() {
		return dataValidacao;
	}

	public void setDataValidacao(Date dataValidacao) {
		this.dataValidacao = dataValidacao;
	}
	
	
//	@Enumerated(EnumType.STRING)
//	private StatusLancamento statusLancamento;
	
	@ManyToOne
	private Fornecedor  fornecedor;
	
	@Column(name = "data_pagamento")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPagamento;
	
	@Column(name = "data_entrega")
	@Temporal(TemporalType.DATE)
	private Date dataEntrega;
	
	@Column(name = "valor_total_sem_desconto", precision = 10, scale = 2)
	private BigDecimal valorTotalSemDesconto;
	
	@Column(name = "valor_total_com_desconto", precision = 10, scale = 2)
	private BigDecimal valorTotalComDesconto = BigDecimal.ZERO;
	
	@Column(name = "total_desconto", precision = 10, scale = 2)
	private BigDecimal totalDeDesonto;
	
	//@Column(name =  "condicao_pagamento")
	@Transient
	private String condicaoPagamento;
	
	@Column(name = "descricao", columnDefinition = "TEXT")
	private String descricao;
	
	@Column(name = "observacao", columnDefinition = "TEXT")
	private String observacao;
	
	@Column(name = "justificativa", columnDefinition = "TEXT")
	private String justificativa;
	
	@Column(name = "data_vencimento")
	@Temporal(TemporalType.TIMESTAMP)
	private  Date dataVencimento;
	
	@ManyToOne
	private Colaborador solicitante;
	
	@Column(name = "condicao_pagamento_enum")
	@Enumerated(EnumType.STRING)
	private CondicaoPagamento condicaoPagamentoEnum;
	
	@Column(name="quantidade_parcela")
	private Integer quantidadeParcela;
	
	@Enumerated(EnumType.STRING)
	private TipoParcelamento tipoParcelamento;

	
//	@Column(name = "data_aprovacao")
//	@Temporal(TemporalType.DATE)
//	private Date dataAprovacao;
	
	@ManyToOne
	private ContaBancaria contaPagador;
	

	@Column
	private String numeroDocumento;
	
	@Column
	private Long idFonteReembolso;
	
	@Column
	private Long idDoacaoReembolso;
	
	@Column
	private Long idRubricaOrcamento;
	
	@Enumerated(EnumType.STRING)
	private StatusCompra statusCompra; // Refatorar variável para o nome status 
	
	@ManyToOne
	private ContaBancaria contaRecebedor;
	
	@Enumerated(EnumType.STRING)
	private DespesaReceita depesaReceita;
	
	@Column(name = "nota_fiscal")
	private String notaFiscal;
	
//	@Temporal(TemporalType.TIMESTAMP)
//	private Date fechamentoAdiantamento;


	@Column
	private String tipoLancamento = ""; // prestação de conta ou normal ou adiantamento 
	
	//Dados transients para geração de relatório de prestação de contas;
	
	@Transient
	private String dtPagto;
	@Transient 
	private String dtEmissao;
	@Transient
	private String pagador;
	@Transient
	private String recebedor;
	@Transient
	private BigDecimal entrada;
	@Transient
	private BigDecimal saida;
	@Transient
	private BigDecimal saldo;
	
	
	@Transient
	private String nomeGestao = "";
	
	@Transient
	private String nomeSolicitante = "";
	
	@Transient
	private String nomeDestino = "";
	
	@Transient
	private String nomeFornecedor = "";
	
	@Transient
	private String nomeCategoria = "";
	
	@Transient
	private String nomeLocalidade = "";
	
	@Transient
	private Boolean selecionado = false;
	
	@Transient
	private Long idCompra;
	
//	@Transient
//	private Projeto projeto;

	public Lancamento() {
	}

	public Lancamento(Long id, Date data, StatusCompra status){
		this.id = id;
		this.dataEmissao = data;
		this.statusCompra = status;
	}
	

	
	public Lancamento(Long id, String gestao, String solicitante, Date emissao, StatusCompra status, String fornecedor, 
			String descricao, String observacao, String localidade, BigDecimal valor, String tipov4, Long idCompra){
		this.id = id;
		this.nomeGestao = gestao;
		this.nomeSolicitante = solicitante;
		this.dataEmissao = emissao;
		this.statusCompra = status;
		this.nomeFornecedor = fornecedor;
		this.descricao = descricao;
		this.observacao = observacao;
		this.nomeLocalidade = localidade;
		this.valorTotalComDesconto = valor;
		this.tipov4 = tipov4;
		this.idCompra = idCompra;
	
	}
	
//	"SELECT NEW Lancamento(l.id, l.gestao.nome, l.solicitante.nome, ");
//	jpql.append("l.dataEmissao, l.statusCompra, forn.nomeFantasia, l.descricao, l.observacao, loc.mascara, l.valorTotalComDesconto) from Lancamento l ");
	

	public Long getId() {
		//if (this.id == null) return 0l;
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
		Lancamento other = (Lancamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public TipoGestao getTipoGestao() {
		return tipoGestao;
	}

	public CategoriadeDespesa getCategoriaDespesa() {
		return categoriaDespesa;
	}


	public void setCategoriaDespesa(CategoriadeDespesa categoriaDespesa) {
		this.categoriaDespesa = categoriaDespesa;
	}
	
	public List<ArquivoLancamento> getArquivos() {
		return arquivos;
	}
	
	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	

	public void setArquivos(List<ArquivoLancamento> arquivos) {
		this.arquivos = arquivos;
	}
	
	public void setTipoGestao(TipoGestao tipoGestao) {
		this.tipoGestao = tipoGestao;
	}


	public Gestao getGestao() {
		return gestao;
	}

	public StatusCompra getStatusCompra() {
		return statusCompra;
	}

	public void setStatusCompra(StatusCompra statusCompra) {
		this.statusCompra = statusCompra;
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


	public String getCodigo() {
		return codigo;
	}


	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}


	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}


	public List<LancamentoAcao> getLancamentosAcoes() {
		return lancamentosAcoes;
	}


	public void setLancamentosAcoes(List<LancamentoAcao> lancamentosAcoes) {
		this.lancamentosAcoes = lancamentosAcoes;
	}


//	public StatusLancamento getStatusLancamento() {
//		return statusLancamento;
//	}
//
//
//	public void setStatusLancamento(StatusLancamento statusLancamento) {
//		this.statusLancamento = statusLancamento;
//	}


	public Fornecedor getFornecedor() {
		return fornecedor;
	}


	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}


	public Date getDataPagamento() {
		return dataPagamento;
	}


	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}


	public Date getDataEntrega() {
		return dataEntrega;
	}


	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}


	public BigDecimal getValorTotalSemDesconto() {
		return valorTotalSemDesconto;
	}


	public void setValorTotalSemDesconto(BigDecimal valorTotalSemDesconto) {
		this.valorTotalSemDesconto = valorTotalSemDesconto;
	}


	public BigDecimal getValorTotalComDesconto() {
		return valorTotalComDesconto;
	}


	public void setValorTotalComDesconto(BigDecimal valorTotalComDesconto) {
		this.valorTotalComDesconto = valorTotalComDesconto;
	}


	public BigDecimal getTotalDeDesonto() {
		return totalDeDesonto;
	}


	public void setTotalDeDesonto(BigDecimal totalDeDesonto) {
		this.totalDeDesonto = totalDeDesonto;
	}


	public String getCondicaoPagamento() {
		return condicaoPagamento;
	}


	public void setCondicaoPagamento(String condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public Colaborador getSolicitante() {
		return solicitante;
	}


	public void setSolicitante(Colaborador solicitante) {
		this.solicitante = solicitante;
	}


	public CondicaoPagamento getCondicaoPagamentoEnum() {
		return condicaoPagamentoEnum;
	}


	public void setCondicaoPagamentoEnum(CondicaoPagamento condicaoPagamentoEnum) {
		this.condicaoPagamentoEnum = condicaoPagamentoEnum;
	}


	public Integer getQuantidadeParcela() {
		return quantidadeParcela;
	}


	public void setQuantidadeParcela(Integer quantidadeParcela) {
		this.quantidadeParcela = quantidadeParcela;
	}

	public TipoParcelamento getTipoParcelamento() {
		return tipoParcelamento;
	}


	public void setTipoParcelamento(TipoParcelamento tipoParcelamento) {
		this.tipoParcelamento = tipoParcelamento;
	}


//	public Date getDataAprovacao() {
//		return dataAprovacao;
//	}
//
//
//	public void setDataAprovacao(Date dataAprovacao) {
//		this.dataAprovacao = dataAprovacao;
//	}


	public DespesaReceita getDepesaReceita() {
		return depesaReceita;
	}


	public void setDepesaReceita(DespesaReceita depesaReceita) {
		this.depesaReceita = depesaReceita;
	}


	public String getNumeroDocumento() {
		return numeroDocumento;
	}


	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}


	public ContaBancaria getContaPagador() {
		return contaPagador;
	}


	public void setContaPagador(ContaBancaria contaPagador) {
		this.contaPagador = contaPagador;
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


	public String getDtPagto() {
		return dtPagto;
	}


	public void setDtPagto(String dtPagto) {
		this.dtPagto = dtPagto;
	}


	public String getDtEmissao() {
		return dtEmissao;
	}


	public void setDtEmissao(String dtEmissao) {
		this.dtEmissao = dtEmissao;
	}


	public String getPagador() {
		return pagador;
	}


	public void setPagador(String pagador) {
		this.pagador = pagador;
	}


	public String getRecebedor() {
		return recebedor;
	}


	public void setRecebedor(String recebedor) {
		this.recebedor = recebedor;
	}


	public BigDecimal getEntrada() {
		return entrada;
	}


	public void setEntrada(BigDecimal entrada) {
		this.entrada = entrada;
	}


	public BigDecimal getSaida() {
		return saida;
	}


	public void setSaida(BigDecimal saida) {
		this.saida = saida;
	}


	public BigDecimal getSaldo() {
		return saldo;
	}


	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}


	public String getNotaFiscal() {
		return notaFiscal;
	}


	public void setNotaFiscal(String notaFiscal) {
		this.notaFiscal = notaFiscal;
	}


	public CategoriaDespesaClass getCategoriaDespesaClass() {
		return CategoriaDespesaClass;
	}


	public void setCategoriaDespesaClass(CategoriaDespesaClass categoriaDespesaClass) {
		CategoriaDespesaClass = categoriaDespesaClass;
	}


	public String getObservacao() {
		return observacao;
	}


	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getNomeGestao() {
		return nomeGestao;
	}

	public void setNomeGestao(String nomeGestao) {
		this.nomeGestao = nomeGestao;
	}

	public String getNomeSolicitante() {
		return nomeSolicitante;
	}

	public void setNomeSolicitante(String nomeSolicitante) {
		this.nomeSolicitante = nomeSolicitante;
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

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public Date getDataConlusao() {
		return dataConlusao;
	}

	public void setDataConlusao(Date dataConlusao) {
		this.dataConlusao = dataConlusao;
	}

//	public Date getFechamentoAdiantamento() {
//		return fechamentoAdiantamento;
//	}
//
//	public void setFechamentoAdiantamento(Date fechamentoAdiantamento) {
//		this.fechamentoAdiantamento = fechamentoAdiantamento;
//	}

	

	

	public StatusAdiantamento getStatusAdiantamento() {
		return statusAdiantamento;
	}

	public void setStatusAdiantamento(StatusAdiantamento statusAdiantamento) {
		this.statusAdiantamento = statusAdiantamento;
	}

	public String getVersionLancamento() {
		return versionLancamento;
	}

	public void setVersionLancamento(String versionLancamento) {
		this.versionLancamento = versionLancamento;
	}

	public TipoDeDocumentoFiscal getTipoDocumentoFiscal() {
		return tipoDocumentoFiscal;
	}

	public void setTipoDocumentoFiscal(TipoDeDocumentoFiscal tipoDocumentoFiscal) {
		this.tipoDocumentoFiscal = tipoDocumentoFiscal;
	}

	public Date getDataDocumentoFiscal() {
		return dataDocumentoFiscal;
	}

	public void setDataDocumentoFiscal(Date dataDocumentoFiscal) {
		this.dataDocumentoFiscal = dataDocumentoFiscal;
	}

	public String getNomeCategoria() {
		return nomeCategoria;
	}

	public void setNomeCategoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}

	public CategoriaFinanceira getCategoriaFinanceira() {
		return categoriaFinanceira;
	}

	public void setCategoriaFinanceira(CategoriaFinanceira categoriaFinanceira) {
		this.categoriaFinanceira = categoriaFinanceira;
	}
	
	public Long getIdReembolso() {
		return idReembolso;
	}

	public void setIdReembolso(Long idReembolso) {
		this.idReembolso = idReembolso;
	}

	public Long getIdTarifado() {
		return idTarifado;
	}

	public void setIdTarifado(Long idTarifado) {
		this.idTarifado = idTarifado;
	}

	public Boolean getReembolsado() {
		return reembolsado;
	}

	public void setReembolsado(Boolean reembolsado) {
		this.reembolsado = reembolsado;
	}

	public Long getIdFonteReembolso() {
		return idFonteReembolso;
	}

	public void setIdFonteReembolso(Long idFonteReembolso) {
		this.idFonteReembolso = idFonteReembolso;
	}

	public Long getIdDoacaoReembolso() {
		return idDoacaoReembolso;
	}

	public void setIdDoacaoReembolso(Long idDoacaoReembolso) {
		this.idDoacaoReembolso = idDoacaoReembolso;
	}

	public Long getIdRubricaOrcamento() {
		return idRubricaOrcamento;
	}

	public void setIdRubricaOrcamento(Long idRubricaOrcamento) {
		this.idRubricaOrcamento = idRubricaOrcamento;
	}

	public String getTipov4() {
		return tipov4;
	}

	public void setTipov4(String tipov4) {
		this.tipov4 = tipov4;
	}
	
	public Boolean getBootUrgencia() {
		return bootUrgencia;
	}

	public void setBootUrgencia(Boolean bootUrgencia) {
		this.bootUrgencia = bootUrgencia;
	}

	public Boolean getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public String getNomeLocalidade() {
		return nomeLocalidade;
	}

	public void setNomeLocalidade(String nomeLocalidade) {
		this.nomeLocalidade = nomeLocalidade;
	}

	public Long getIdCompra() {
		return idCompra;
	}

	public void setIdCompra(Long idCompra) {
		this.idCompra = idCompra;
	}
	
}
