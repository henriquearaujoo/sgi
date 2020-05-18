package model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("tarifa_bancaria")
public class TarifaBancaria extends Lancamento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TarifaBancaria() {

	}

	// SolicitacaoPagamento(c.id, c.gestao.nome, c.solicitante.nome,
	// c.localidade.mascara, c.dataEmissao,c.statusCompra)
	public TarifaBancaria(Long id, String nomeGestao, String nomeSolicitante, String nomeDestino, Date data,
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

}
