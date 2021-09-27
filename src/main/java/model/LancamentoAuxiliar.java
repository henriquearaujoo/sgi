package model;

import java.math.BigDecimal;

public class LancamentoAuxiliar extends Lancamento{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Long AcaoId;
	
	

	private String tipo;
	private String tipoContaRecebedor;
	private String tipoContaPagador;
	
	private String nomeGestao;

	private Long idPagamento;
	
	private String doacao;
	
	private String componente;
	
	private String subComponente;
	
	private String codigoAcao;
	
	private String fonte;
	
	private BigDecimal valorPagoAcao;
	
	private BigDecimal valorLancamento;
	
	private Long idAcaoLancamentoId;
	
	private String codigoLancamento;
	
	private String labelFornecedor;
	
	private String contaPagadorLbl;
	
	private String contaRecebedorLbl;
	
	private String compraID;
	
	private BigDecimal valorPrestadoConta;

	private Integer statusPagamento;
	
	private String status;
	
	private String nomeProjeto;
	
	private String sinalizador;
	
	private Long idAdiantamentoDePrestacao;
	
	private String statusAdDePrestacao;
	
	private String statusAd;
	
	private String acaoV1;
	
	private String fonteV1;
	
	private Long idTermo;
	
	private BigDecimal valorNulo;
	
	private BigDecimal totalEntrada;
	private BigDecimal totalSaida;
	
	private int quantidadeAcoes = 0;
	private int quantidadePagamentos = 0;
	private int quantidadeArquivos = 0;
	
	private Boolean podeEditarAdiantamento;
	
	private String tipoLancamento;
	
	private String requisitoDeDoador;
	
	private String dataEmissaoString;
	
	private String categoriaDespesaNome;
	
	//Adicionado para implementar a unificação das aprovaações
	
	private Boolean isSolicitacaoViagem;

	public LancamentoAuxiliar(){}
	
	public Boolean getIsSolicitacaoViagem() {
		return isSolicitacaoViagem;
	}


	public void setIsSolicitacaoViagem(Boolean isSolicitacaoViagem) {
		this.isSolicitacaoViagem = isSolicitacaoViagem;
	}


	public String getCategoriaDespesaNome() {
		return categoriaDespesaNome;
	}


	public void setCategoriaDespesaNome(String categoriaDespesaNome) {
		this.categoriaDespesaNome = categoriaDespesaNome;
	}


	public String getRequisitoDeDoador() {
		return requisitoDeDoador;
	}


	public void setRequisitoDeDoador(String requisitoDeDoador) {
		this.requisitoDeDoador = requisitoDeDoador;
	}

	public String getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(String tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
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

	public String getDoacao() {
		return doacao;
	}

	public void setDoacao(String doacao) {
		this.doacao = doacao;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Long getAcaoId() {
		return AcaoId;
	}

	public void setAcaoId(Long acaoId) {
		AcaoId = acaoId;
	}

	public String getCodigoAcao() {
		return codigoAcao;
	}

	public void setCodigoAcao(String codigoAcao) {
		this.codigoAcao = codigoAcao;
	}

	public String getFonte() {
		return fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	public BigDecimal getValorPagoAcao() {
		return valorPagoAcao;
	}

	public void setValorPagoAcao(BigDecimal valorPagoAcao) {
		this.valorPagoAcao = valorPagoAcao;
	}

	public BigDecimal getValorLancamento() {
		return valorLancamento;
	}

	public void setValorLancamento(BigDecimal valorLancamento) {
		this.valorLancamento = valorLancamento;
	}

	public Long getIdAcaoLancamentoId() {
		return idAcaoLancamentoId;
	}

	public void setIdAcaoLancamentoId(Long idAcaoLancamentoId) {
		this.idAcaoLancamentoId = idAcaoLancamentoId;
	}

	public String getCodigoLancamento() {
		return codigoLancamento;
	}

	public void setCodigoLancamento(String codigoLancamento) {
		this.codigoLancamento = codigoLancamento;
	}

	public String getLabelFornecedor() {
		return labelFornecedor;
	}

	public void setLabelFornecedor(String labelFornecedor) {
		this.labelFornecedor = labelFornecedor;
	}

	public String getContaPagadorLbl() {
		return contaPagadorLbl;
	}

	public void setContaPagadorLbl(String contaPagadorLbl) {
		this.contaPagadorLbl = contaPagadorLbl;
	}

	public String getContaRecebedorLbl() {
		return contaRecebedorLbl;
	}

	public void setContaRecebedorLbl(String contaRecebedorLbl) {
		this.contaRecebedorLbl = contaRecebedorLbl;
	}

	public String getCompraID() {
		return compraID;
	}

	public void setCompraID(String compraID) {
		this.compraID = compraID;
	}

	public int getQuantidadeAcoes() {
		return quantidadeAcoes;
	}

	public void setQuantidadeAcoes(int quantidadeAcoes) {
		this.quantidadeAcoes = quantidadeAcoes;
	}

	public int getQuantidadePagamentos() {
		return quantidadePagamentos;
	}

	public void setQuantidadePagamentos(int quantidadePagamentos) {
		this.quantidadePagamentos = quantidadePagamentos;
	}

	public int getQuantidadeArquivos() {
		return quantidadeArquivos;
	}

	public void setQuantidadeArquivos(int quantidadeArquivos) {
		this.quantidadeArquivos = quantidadeArquivos;
	}

	public BigDecimal getValorPrestadoConta() {
		return valorPrestadoConta;
	}

	public void setValorPrestadoConta(BigDecimal valorPrestadoConta) {
		this.valorPrestadoConta = valorPrestadoConta;
	}

	public Boolean getPodeEditarAdiantamento() {
		return podeEditarAdiantamento;
	}

	public void setPodeEditarAdiantamento(Boolean podeEditarAdiantamento) {
		this.podeEditarAdiantamento = podeEditarAdiantamento;
	}

	public Integer getStatusPagamento() {
		return statusPagamento;
	}

	public void setStatusPagamento(Integer statusPagamento) {
		this.statusPagamento = statusPagamento;
	}

	public String getNomeProjeto() {
		return nomeProjeto;
	}

	public void setNomeProjeto(String nomeProjeto) {
		this.nomeProjeto = nomeProjeto;
	}

	public String getSinalizador() {
		return sinalizador;
	}

	public void setSinalizador(String sinalizador) {
		this.sinalizador = sinalizador;
	}

	public Long getIdAdiantamentoDePrestacao() {
		return idAdiantamentoDePrestacao;
	}

	public void setIdAdiantamentoDePrestacao(Long idAdiantamentoDePrestacao) {
		this.idAdiantamentoDePrestacao = idAdiantamentoDePrestacao;
	}

	public String getStatusAdDePrestacao() {
		return statusAdDePrestacao;
	}

	public void setStatusAdDePrestacao(String statusAdDePrestacao) {
		this.statusAdDePrestacao = statusAdDePrestacao;
	}

	public String getStatusAd() {
		return statusAd;
	}

	public void setStatusAd(String statusAd) {
		this.statusAd = statusAd;
	}

	public BigDecimal getValorNulo() {
		return valorNulo;
	}

	public void setValorNulo(BigDecimal valorNulo) {
		this.valorNulo = valorNulo;
	}

	public BigDecimal getTotalEntrada() {
		return totalEntrada;
	}

	public void setTotalEntrada(BigDecimal totalEntrada) {
		this.totalEntrada = totalEntrada;
	}

	public BigDecimal getTotalSaida() {
		return totalSaida;
	}

	public void setTotalSaida(BigDecimal totalSaida) {
		this.totalSaida = totalSaida;
	}

	public Long getIdPagamento() {
		return idPagamento;
	}

	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAcaoV1() {
		return acaoV1;
	}

	public void setAcaoV1(String acaoV1) {
		this.acaoV1 = acaoV1;
	}

	public String getFonteV1() {
		return fonteV1;
	}

	public void setFonteV1(String fonteV1) {
		this.fonteV1 = fonteV1;
	}

	public String getNomeGestao() {
		return nomeGestao;
	}

	public void setNomeGestao(String nomeGestao) {
		this.nomeGestao = nomeGestao;
	}

	public Long getIdTermo() {
		return idTermo;
	}

	public void setIdTermo(Long idTermo) {
		this.idTermo = idTermo;
	}


	

	public String getTipoContaPagador() {
		return tipoContaPagador;
	}

	public void setTipoContaPagador(String tipoContaPagador) {
		this.tipoContaPagador = tipoContaPagador;
	}

	public String getTipoContaRecebedor() {
		return tipoContaRecebedor;
	}

	public void setTipoContaRecebedor(String tipoContaRecebedor) {
		this.tipoContaRecebedor = tipoContaRecebedor;
	}

	public String getDataEmissaoString() {
		return dataEmissaoString;
	}

	public void setDataEmissaoString(String dataEmissaoString) {
		this.dataEmissaoString = dataEmissaoString;
	}

	
	
}
