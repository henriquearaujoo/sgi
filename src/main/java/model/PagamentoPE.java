package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import util.DataUtil;

/**
 * Classe auxiliar para preenchimento de pagamentos provisionados e não provisionados
 *  
 *  
 *  Pagamento (PE) = provisionado e efetivados
 * 
 * */


public class PagamentoPE implements Serializable{
	
	private Long id;
	private Date dataEmissao;
	private Date dataPagamento;
	private StatusPagamentoLancamento status;
	private BigDecimal valor;
	private BigDecimal valorLancamento;
	private String codigoLancamento;
	private String conta;
	private String parcela;
	private String fornecedor;
	private String fonteRecurso;
	private String localidade;
	private String acao;
	private Long idConta;
	
	private String contaRecebedor;
	private String compraID;
	
	private Long idPagamento;
	
	private String numeroDocumento;
	
	private String descricao;
	
	
	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public String getNumeroDocumento() {
		return numeroDocumento;
	}


	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}


	public PagamentoPE(Object[] obj){
		this.dataEmissao = DataUtil.converteDataSql(obj[0].toString());
		this.dataPagamento = DataUtil.converteDataSql(obj[1].toString());
		this.acao = obj[2].toString();
		this.fonteRecurso = obj[3].toString();
		this.valorLancamento = BigDecimal.ZERO;
		this.status  = StatusPagamentoLancamento.valueOf(obj[4].toString());
		this.valor = new BigDecimal(obj[5].toString());
		this.conta = obj[6].toString();
		this.codigoLancamento = obj[7].toString();
		this.parcela = obj[8].toString();
		this.fornecedor = obj[9] != null ? obj[9].toString() : "";
		this.localidade = obj[10] != null ? obj[10].toString() :  "";
		this.idPagamento = Long.valueOf(obj[11].toString());
		this.contaRecebedor = obj[12] != null ? obj[12].toString() : "";
		this.compraID = obj[13] != null ? obj[13].toString() : "";
		this.numeroDocumento = obj[14] != null ? obj[14].toString() : "";
		this.descricao = obj[15] != null ? obj[15].toString() : "";
//		this.idLancamento = Long.valueOf(obj[16].toString());
	}
	
	public PagamentoPE(Object[] obj, String args){
		this.dataEmissao = DataUtil.converteDataSql(obj[4].toString());
		this.dataPagamento = DataUtil.converteDataSql(obj[3].toString());
		//this.acao = obj[2].toString();
		//this.fonteRecurso = obj[3].toString();
		//this.valorLancamento = BigDecimal.ZERO;
		this.id = new Long(obj[5].toString());
		this.status  = StatusPagamentoLancamento.valueOf(obj[9].toString());
		this.valor = new BigDecimal(obj[8].toString());
		this.conta = obj[0].toString();
		this.codigoLancamento = obj[5].toString();
		this.parcela = obj[7].toString();
		this.fornecedor = obj[1] != null ? obj[1].toString() : "";
		//this.localidade = obj[10] != null ? obj[10].toString() :  "";
		//this.idPagamento = Long.valueOf(obj[11].toString());
		this.contaRecebedor = obj[1] != null ? obj[1].toString() : "";
		//this.compraID = obj[13] != null ? obj[13].toString() : "";
		this.numeroDocumento = obj[10] != null ? obj[10].toString() : "";
		this.descricao = obj[6] != null ? obj[6].toString() : "";
		//this.idLancamento = Long.valueOf(obj[1].toString());
		this.idConta = Long.valueOf(obj[11].toString());
		this.idPagamento = Long.valueOf(obj[12].toString());
	}
	
	private Long idLancamento;
	
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
	public StatusPagamentoLancamento getStatus() {
		return status;
	}
	public void setStatus(StatusPagamentoLancamento status) {
		this.status = status;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public BigDecimal getValorLancamento() {
		return valorLancamento;
	}
	public void setValorLancamento(BigDecimal valorLancamento) {
		this.valorLancamento = valorLancamento;
	}
	public String getCodigoLancamento() {
		return codigoLancamento;
	}
	public void setCodigoLancamento(String codigoLancamento) {
		this.codigoLancamento = codigoLancamento;
	}
	public String getConta() {
		return conta;
	}
	public void setConta(String conta) {
		this.conta = conta;
	}
	public String getParcela() {
		return parcela;
	}
	public void setParcela(String parcela) {
		this.parcela = parcela;
	}
	public String getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
	public String getFonteRecurso() {
		return fonteRecurso;
	}
	public void setFonteRecurso(String fonteRecurso) {
		this.fonteRecurso = fonteRecurso;
	}
	public String getLocalidade() {
		return localidade;
	}
	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}
	public String getAcao() {
		return acao;
	}
	public void setAcao(String acao) {
		this.acao = acao;
	}


	public Long getIdPagamento() {
		return idPagamento;
	}


	public void setIdPagamento(Long idPagamento) {
		this.idPagamento = idPagamento;
	}


	public String getContaRecebedor() {
		return contaRecebedor;
	}


	public void setContaRecebedor(String contaRecebedor) {
		this.contaRecebedor = contaRecebedor;
	}


	public String getCompraID() {
		return compraID;
	}


	public void setCompraID(String compraID) {
		this.compraID = compraID;
	}


	public Long getIdLancamento() {
		return idLancamento;
	}


	public void setIdLancamento(Long idLancamento) {
		this.idLancamento = idLancamento;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idPagamento == null) ? 0 : idPagamento.hashCode());
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
		PagamentoPE other = (PagamentoPE) obj;
		if (idPagamento == null) {
			if (other.idPagamento != null)
				return false;
		} else if (!idPagamento.equals(other.idPagamento))
			return false;
		return true;
	}


	public Long getIdConta() {
		return idConta;
	}


	public void setIdConta(Long idConta) {
		this.idConta = idConta;
	}



	
	
	
	
	
	
}
