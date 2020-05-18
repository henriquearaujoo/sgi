package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

import util.DataUtil;

public class Adiantamento implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long idAdiantamento;
	private String tipoAdiantamento;
	private Date dataEmissao;
	private Date dataPagamento;
	private String numeroDocumento;
	private String historico;
	private String pagador;
	private Boolean pago;
	private BigDecimal valorAdiantado;
	private BigDecimal valorAPagar;
	private BigDecimal saldo;
	private BigDecimal valorPrestado;
	private BigDecimal valorEntrada;
	


	


	public BigDecimal getValorEntrada() {
		return valorEntrada;
	}



	public void setValorEntrada(BigDecimal valorEntrada) {
		this.valorEntrada = valorEntrada;
	}


	private Integer diasEmAberto;
	private String nomeGestao;
	private String favorecido;
	private String descricao;
	private String dataValidacao;
	private StatusAdiantamento statusAdiantamento;
	
	
	//alterado para relatiro de adiantemnto 14/09/2018
	@Transient
	private String codigo;
	
	@Transient 
	private String  orcamentoTitulo;
	
	@Transient
	private String nomeProjeto;
	
	@Transient
	private String componenteNome;
	
	@Transient
	private String subComponenteNome;
	
	@Transient
	private String nomeRubrica;
	
	@Transient
	private String nomePagador;
	
	@Transient
	private String nomeRecebedor;
	
	//end
	
	public String getNomePagador() {
		return nomePagador;
	}



	public void setNomePagador(String nomePagador) {
		this.nomePagador = nomePagador;
	}



	public String getNomeRecebedor() {
		return nomeRecebedor;
	}



	public void setNomeRecebedor(String nomeRecebedor) {
		this.nomeRecebedor = nomeRecebedor;
	}



	public Adiantamento(){}
	


	public Adiantamento(Long idAdiantamento, String tipoAdiantamento, Date dataEmissao, Date dataPagamento,
			String numeroDocumento, String pagador, Boolean pago, BigDecimal valorAdiantado, BigDecimal valorAPagar,
			BigDecimal saldo) {
	
		this.idAdiantamento = idAdiantamento;
		this.tipoAdiantamento = tipoAdiantamento;
		this.dataEmissao = dataEmissao;
		this.dataPagamento = dataPagamento;
		this.numeroDocumento = numeroDocumento;
		this.pagador = pagador;
		this.pago = pago;
		this.valorAdiantado = valorAdiantado;
		this.valorAPagar = valorAPagar;
		this.saldo = saldo;
	}
	
	public Adiantamento(Object[] object){
		this.nomeGestao = object[0] !=  null ? object[0].toString() : "";
		this.dataEmissao = DataUtil.converteDataSql(object[1].toString());
		this.numeroDocumento = object[2] !=  null ? object[2].toString() : "";
		this.favorecido = object[3] !=  null ? object[3].toString() : "";
		this.descricao = object[4] !=  null ? object[4].toString() : "";
		this.valorAdiantado = object[5] !=  null ? new BigDecimal(object[5].toString())  : BigDecimal.ZERO;
		this.diasEmAberto =  object[6] !=  null ? new Integer(object[6].toString())  : 0;
		this.idAdiantamento =  object[7] !=  null ? new Long(object[7].toString())  : new Long(0);
		this.dataValidacao = object[8] != null? object[8].toString() : new String();
		this.valorPrestado = object[9] != null ? new BigDecimal(object[9].toString())  : BigDecimal.ZERO;
		this.statusAdiantamento = object[10] != null? StatusAdiantamento.valueOf(object[10].toString()):null;
		this.codigo = object[11] != null? object[11].toString() : "";
		this.orcamentoTitulo = object[12] != null? object[12].toString() : "";
		this.nomeProjeto = object[13] != null? object[13].toString() : "";
		this.componenteNome = object[14] != null? object[14].toString() : "";
		this.subComponenteNome = object[15] != null? object[15].toString() : "";
		this.nomeRubrica = object[16] != null? object[16].toString() : "";
		this.nomePagador = object[17] != null? object[17].toString() : "";
		this.nomeRecebedor = object[18] != null? object[18].toString() : "";
		this.valorEntrada = object[19] !=  null ? new BigDecimal(object[19].toString())  : BigDecimal.ZERO;
		this.saldo = valorAdiantado.subtract(valorPrestado).add(valorEntrada);
		
	}
	
	
	public String getCodigo() {
		return codigo;
	}



	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}



	public String getOrcamentoTitulo() {
		return orcamentoTitulo;
	}



	public void setOrcamentoTitulo(String orcamentoTitulo) {
		this.orcamentoTitulo = orcamentoTitulo;
	}



	public String getNomeProjeto() {
		return nomeProjeto;
	}



	public void setNomeProjeto(String nomeProjeto) {
		this.nomeProjeto = nomeProjeto;
	}



	public String getComponenteNome() {
		return componenteNome;
	}



	public void setComponenteNome(String componenteNome) {
		this.componenteNome = componenteNome;
	}



	public String getSubComponenteNome() {
		return subComponenteNome;
	}



	public void setSubComponenteNome(String subComponenteNome) {
		this.subComponenteNome = subComponenteNome;
	}



	public String getNomeRubrica() {
		return nomeRubrica;
	}



	public void setNomeRubrica(String nomeRubrica) {
		this.nomeRubrica = nomeRubrica;
	}



	public StatusAdiantamento getStatusAdiantamento() {
		return statusAdiantamento;
	}


	public void setStatusAdiantamento(StatusAdiantamento statusAdiantamento) {
		this.statusAdiantamento = statusAdiantamento;
	}


	public BigDecimal getValorPrestado() {
		return valorPrestado;
	}


	public void setValorPrestado(BigDecimal valorPrestado) {
		this.valorPrestado = valorPrestado;
	}
	
	public String getDataValidacao() {
		return dataValidacao;
	}


	public void setDataValidacao(String dataValidacao) {
		this.dataValidacao = dataValidacao;
	}

	public String getHistorico() {
		return historico;
	}


	public void setHistorico(String historico) {
		this.historico = historico;
	}
	
	public Long getIdAdiantamento() {
		return idAdiantamento;
	}


	public void setIdAdiantamento(Long idAdiantamento) {
		this.idAdiantamento = idAdiantamento;
	}


	public String getTipoAdiantamento() {
		return tipoAdiantamento;
	}


	public void setTipoAdiantamento(String tipoAdiantamento) {
		this.tipoAdiantamento = tipoAdiantamento;
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


	public String getNumeroDocumento() {
		return numeroDocumento;
	}


	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}


	public String getPagador() {
		return pagador;
	}


	public void setPagador(String pagador) {
		this.pagador = pagador;
	}


	public Boolean getPago() {
		return pago;
	}


	public void setPago(Boolean pago) {
		this.pago = pago;
	}


	public BigDecimal getValorAdiantado() {
		return valorAdiantado;
	}


	public void setValorAdiantado(BigDecimal valorAdiantado) {
		this.valorAdiantado = valorAdiantado;
	}


	public BigDecimal getValorAPagar() {
		return valorAPagar;
	}


	public void setValorAPagar(BigDecimal valorAPagar) {
		this.valorAPagar = valorAPagar;
	}


	public BigDecimal getSaldo() {
		return saldo;
	}


	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}


	public Integer getDiasEmAberto() {
		return diasEmAberto;
	}


	public void setDiasEmAberto(Integer diasEmAberto) {
		this.diasEmAberto = diasEmAberto;
	}


	public String getNomeGestao() {
		return nomeGestao;
	}


	public void setNomeGestao(String nomeGestao) {
		this.nomeGestao = nomeGestao;
	}


	public String getFavorecido() {
		return favorecido;
	}


	public void setFavorecido(String favorecido) {
		this.favorecido = favorecido;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
	
	
	
}
