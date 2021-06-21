package model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import operator.LancamentoIterface;
import service.AdiantamentoService;
import service.CompraService;
import service.SolicitacaoPagamentoService;
import util.CDILocator;
import util.Email;

@Entity
@DiscriminatorValue("SolicitacaoPagamento")
public class SolicitacaoPagamento extends Lancamento implements LancamentoIterface {

	private static final long serialVersionUID = 1L;

	public SolicitacaoPagamento() {

	}

	public SolicitacaoPagamento(Long id, String nomeGestao, String nomeSolicitante, String nomeDestino, Date data,
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

	public SolicitacaoPagamento(Long id, String nomeGestao, String nomeSolicitante, String nomeDestino, Date data,
			StatusCompra status, String nomeFornecedor, String descricao, BigDecimal valorComDesconto,
			StatusAdiantamento statusAdiantamento) {
		setId(id);
		setNomeGestao(nomeGestao);
		setNomeSolicitante(nomeSolicitante);
		setNomeDestino(nomeDestino);
		setDataEmissao(data);
		setStatusCompra(status);
		setNomeFornecedor(nomeFornecedor);
		setDescricao(descricao);
		setValorTotalComDesconto(valorComDesconto);
		setStatusAdiantamento(statusAdiantamento);
	}

	@Override
	public void autorizar(Lancamento lancamento) {
		
	}

	@Override
	public void desautorizar(Lancamento lancamento, String texto) {
		

	}

	@Override
	public void imprimir(Lancamento lancamento) {
		CompraService compraService = CDILocator.getBean(CompraService.class);

		if (lancamento.getTipov4().equals("SP")) {
			SolicitacaoPagamentoService service = CDILocator.getBean(SolicitacaoPagamentoService.class);
			service.imprimir(lancamento.getId(), compraService);
		} else {
			AdiantamentoService service = CDILocator.getBean(AdiantamentoService.class);
			service.imprimir(lancamento.getId(), compraService);
		}

	}

}
