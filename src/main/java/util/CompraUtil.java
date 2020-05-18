package util;

import javax.inject.Inject;

import model.Compra;
import repositorio.LancamentoRepository;

public class CompraUtil {

	// Compara a quantidade de itens para verificar se a solicitação de compra
	// foi concluída
	public static boolean ComparaQuantidadeItens(Compra compra, LancamentoRepository lancamentoRepository,
			int sizeList) {

		int qtdItemCompra = lancamentoRepository.getQuantidadeItemCompra(compra);
		int qtdItemPedido = lancamentoRepository.getQtdItemPedido(compra) + sizeList;

		if ((qtdItemCompra == qtdItemPedido) || (qtdItemPedido > qtdItemCompra)) {
			return true;
		} else {
			return false;
		}

		// return qtdItemCompra == qtdItemPedido;
	}

	public static boolean ComparaQuantidadeItens(Compra compra, LancamentoRepository lancamentoRepository) {

		int qtdItemCompra = lancamentoRepository.getQuantidadeItemCompra(compra);
		int qtdItemPedido = lancamentoRepository.getQtdItemPedido(compra);

		if ((qtdItemCompra == qtdItemPedido) || (qtdItemPedido > qtdItemCompra)) {
			return true;
		} else {
			return false;
		}

		// return qtdItemCompra == qtdItemPedido;
	}

	public static boolean ComparaQuantidadeItensTYPEAB(Compra compra, LancamentoRepository lancamentoRepository,
			int sizeList, Long idPedido) {

		int qtdItemCompra = lancamentoRepository.getQuantidadeItemCompra(compra);
		int qtdItemPedido = lancamentoRepository.getQtdItemPedido(compra, idPedido) + sizeList;

		if ((qtdItemCompra == qtdItemPedido) || (qtdItemPedido > qtdItemCompra)) {
			return true;
		} else {
			return false;
		}

		// return qtdItemCompra == qtdItemPedido;
	}

}
