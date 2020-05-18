package model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("aplicacao_recurso")
public class AplicacaoRecurso extends Lancamento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Orcamento orcamento;

	@Column
	private Long idContaAplicacao;

	public AplicacaoRecurso() {

	}
	
	public AplicacaoRecurso(Long id, String nomeGestao, String nomeSolicitante, String nomeDestino, Date data,
			StatusCompra status, String nomeFornecedor, String descricao, BigDecimal valorComDesconto) {
		setId(id);
		setNomeGestao(nomeGestao);
		setNomeSolicitante(nomeSolicitante);
		setNomeDestino(nomeDestino);
		setDataEmissao(data);
		setStatusCompra(status);
		setNomeFornecedor(nomeFornecedor);
		setDescricao(descricao);
		setValorTotalComDesconto(valorComDesconto);
	}

	public Long getIdContaAplicacao() {
		return idContaAplicacao;
	}

	public void setIdContaAplicacao(Long idContaAplicacao) {
		this.idContaAplicacao = idContaAplicacao;
	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}
}
