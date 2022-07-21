package service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import anotacoes.Transactional;
import model.Acao;
import model.Aprouve;
import model.Compra;
import model.ControleExpedicao;
import model.Cotacao;
import model.FontePagadora;
import model.Fornecedor;
import model.Gestao;
import model.ItemPedido;
import model.Lancamento;
import model.LancamentoAcao;
import model.LogStatus;
import model.Pedido;
import model.StatusCompra;
import model.User;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import repositorio.AcaoRepositorio;
import repositorio.AprouveRepositorio;
import repositorio.ControleExpedicaoRepositorio;
import repositorio.CotacaoRepository;
import repositorio.FontePagadoraRepositorio;
import repositorio.FornecedorRepositorio;
import repositorio.GestaoRepositorio;
import repositorio.LancamentoRepository;
import repositorio.PedidoRepositorio;
import util.ArquivoLancamento;
import util.CompraUtil;
import util.ReportUtil;
import util.Util;

/**
 * Autor: Italo Almeida Classe: Gestao de Projeto Objetivo: Gerir a regra de
 * negocio referente a instancia do objeto
 */

public class PedidoService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private LancamentoRepository lancRepository;
	@Inject
	private PedidoRepositorio repositorio;
	@Inject
	private FornecedorRepositorio fornecedorRepositorio;
	@Inject
	private AcaoRepositorio acaoRepositorio;
	@Inject
	private FontePagadoraRepositorio fonteRespositorio;
	@Inject
	private GestaoRepositorio gestaoRepositorio;
	@Inject
	private ControleExpedicaoRepositorio expedicaoRepositorio;
	@Inject
	private CotacaoRepository cotacaoRepositorio;
	@Inject
	private AprouveRepositorio aprouveRepositorio;

	@Transactional
	public boolean salvar(Pedido pedido) {
		try {

			List<LancamentoAcao> lList = lancRepository.getLancamentosDoPedido(pedido);

			for (LancamentoAcao lancamentoAcao : lList) {
				lancamentoAcao.setLancamento(pedido);
				lancRepository.salvarLancamentoAcao(lancamentoAcao);
			}

			pedido.setDescricao("Pedido - " + pedido.getId());
			pedido.setStatusCompra(StatusCompra.CONCLUIDO);

			repositorio.salvar(pedido);
			// repositorio.salvarControleExpedicao(pedido);

		} catch (Exception e) {
			addMessage("", "Erro ao inserir pedido entre em contato com o administrator do sistema",
					FacesMessage.SEVERITY_ERROR);
			return false;
		}

		return true;
	}

	public ControleExpedicao findControleExpedByPedido(Long id) {
		return expedicaoRepositorio.findControleExpedByPedido(id);
	}
	
	@Transactional
	public Boolean excluirItemPedido(ItemPedido item) {
		lancRepository.excluirItemPedido(item);
		return true;
	}

	public Boolean verificarAprovacao(Aprouve aprouve) {
		return aprouveRepositorio.findAprouve(aprouve);
	}

	public Boolean verificarPrivilegioENF(Aprouve aprouve) {
		return aprouveRepositorio.verificaPrivilegioNF(aprouve);
	}

	@Transactional
	public boolean salvar(Pedido pedido, ControleExpedicao controleExpedicao) {
		try {

			List<LancamentoAcao> lList = lancRepository.getLancamentosDoPedido(pedido);

			for (LancamentoAcao lancamentoAcao : lList) {
				lancamentoAcao.setLancamento(pedido);
				lancRepository.salvarLancamentoAcao(lancamentoAcao);
			}

			pedido.setDescricao("Pedido - " + pedido.getId());
			pedido.setStatusCompra(StatusCompra.CONCLUIDO);

			repositorio.salvar(pedido, controleExpedicao);
			// repositorio.salvarControleExpedicao(pedido);

		} catch (Exception e) {
			addMessage("", "Erro ao inserir pedido entre em contato com o administrator do sistema",
					FacesMessage.SEVERITY_ERROR);
			return false;
		}

		return true;
	}

	public List<Gestao> buscarGestao() {
		List<Gestao> listGestao = new ArrayList<>();
		listGestao.addAll(gestaoRepositorio.getCoordenadoria());
		listGestao.addAll(gestaoRepositorio.getRegional());
		return listGestao;
	}

	public Lancamento getLancamentoById(Long id) {
		return lancRepository.getLancamentoById(id);
	}

	public List<ArquivoLancamento> getArquivoByLancamento(Long lancamento) {
		return lancRepository.getArquivosByLancamento(lancamento);
	}

	@Transactional
	public ArquivoLancamento salvarArquivo(ArquivoLancamento arquivo) {
		return lancRepository.salvarArquivo(arquivo);
	}

	@Transactional
	public void removerArquivo(ArquivoLancamento arquivo) {
		lancRepository.removerArquivo(arquivo);
	}

	public List<Acao> acoesAutoComplete(String acao) {
		return acaoRepositorio.getAcaoAutoComplete(acao);
	}

	public List<FontePagadora> fontesAutoComplete(String fonte) {
		return fonteRespositorio.getFonteAutoComplete(fonte);
	}

	public List<ItemPedido> getItensPedido(Pedido pedido) {
		try {
			return repositorio.getItensPedido(pedido);
		} catch (Exception e) {
			return new ArrayList<>();
		}

	}

	@Transactional
	public boolean mudarStatusPedido(Pedido pedido, User usuario) {

		LogStatus log = new LogStatus();
		log.setLancamento(pedido);
		log.setData(new Date());
		log.setStatusLog(StatusCompra.CANCELADO);
		log.setUsuario(usuario);
		pedido.setStatusCompra(StatusCompra.CANCELADO);
		// lancRepository.removeTrancacoesPedido(pedido);
		repositorio.salvarPedidoSuspenso(pedido);
		lancRepository.salvarLog(log);

		return true;
	}

	@Transactional
	public boolean salvar(Pedido pedido, List<Cotacao> cotacoes) {
		try {

			for (Cotacao cotacao : cotacoes) {
				ItemPedido itemPedido = new ItemPedido();
				itemPedido.setCotacao(cotacao);
				itemPedido.setPedido(pedido);
				itemPedido.setValorUnitario(cotacao.getValor());
				itemPedido.setValorUnitarioComDesconto(cotacao.getValorDesconto());
				itemPedido.setQuantidade(cotacao.getQuantidade());
				itemPedido.setDescricaoProduto(cotacao.getItemCompra().getProduto().getDescricao());

			}

			repositorio.salvar(pedido);
		} catch (Exception e) {
			addMessage("", "Erro ao inserir pedido entre em contato com o administrator do sistema",
					FacesMessage.SEVERITY_ERROR);
			return false;
		}

		return true;
	}

	public List<ItemPedido> getItensPedidosByPedido(Pedido pedido) {
		return repositorio.getItensPedidosByPedido(pedido);
	}

	@Transactional
	public void remover(Pedido pedido) {
		try {

//			if (verificarPagamentos(pedido.getId())) {
//				addMessage("",
//						"Existem pagamentos provisionados para esse pedido de compras, ele não pode ser excluído sem antes comunicação ao financeiro para que tire de sua provisão de gastos",
//						FacesMessage.SEVERITY_ERROR);
//				return false;
//			}

			pedido = repositorio.getPedidoPorId(pedido.getId());
			List<ItemPedido> itens = repositorio.getItensPedido(pedido);

			for (ItemPedido itemPedido : itens) {
				cotacaoRepositorio.desfazerEscolhaDeCotacaoPedido(itemPedido.getCotacao().getId());
			}

			lancRepository.mudarStatus(pedido.getCompra().getId(), StatusCompra.EM_COTACAO);
			
			ControleExpedicao controleExpedicao = expedicaoRepositorio.findControleExpedByPedido(pedido.getId());
			// expedicaoRepositorio.salvar(controleExpedicao);
			expedicaoRepositorio.remover(controleExpedicao);
			repositorio.remover(pedido);

//			addMessage("", "Pedido excluído.", FacesMessage.SEVERITY_INFO);

		} catch (Exception e) {
			addMessage("", "Erro ao remover pedido entre em contato com o administrator do sistema",
					FacesMessage.SEVERITY_ERROR);
//			return false;
		}

//		return true;
	}

	@Transactional
	public boolean excluirItemPedidoTypeA(Pedido pedido, List<ItemPedido> listItem, User usuario) {

		Boolean houvePagamento = verificarPagamentos(pedido.getId());

		// Salva o log de quem fez a alteração
		LogStatus log = new LogStatus();
		log.setLancamento(pedido);
		log.setData(new Date());
		log.setStatusLog(StatusCompra.EM_COTACAO);
		log.setUsuario(usuario);
		log.setComentario("Exclusão de itens pedidos TYPE1 - RETURN. STATUS. COTAÇÃO");

		lancRepository.mudarStatus(pedido.getCompra().getId(), StatusCompra.EM_COTACAO);

		if (!houvePagamento) {
			for (ItemPedido itemPedido : listItem) {
				BigDecimal valorComDesconto = itemPedido.getValorUnitarioComDesconto();
				BigDecimal valorSemDesconto = itemPedido.getValorUnitario();
				Double quantidade = itemPedido.getQuantidade();
				BigDecimal valorTotalComDesconto = valorComDesconto.multiply(new BigDecimal(quantidade));
				BigDecimal valorTotalSemDesconto = valorSemDesconto.multiply(new BigDecimal(quantidade));
				pedido.setValorTotalComDesconto(pedido.getValorTotalComDesconto().subtract(valorTotalComDesconto));
				pedido.setValorTotalSemDesconto(pedido.getValorTotalSemDesconto().subtract(valorTotalSemDesconto));
			}
		} else {
			if (pedido.getValorDevolucao() == null) {
				pedido.setValorDevolucao(BigDecimal.ZERO);
			}
			for (ItemPedido itemPedido : listItem) {
				BigDecimal valorComDesconto = itemPedido.getValorUnitarioComDesconto();
				Double quantidade = itemPedido.getQuantidade();
				BigDecimal valorTotalComDesconto = valorComDesconto.multiply(new BigDecimal(quantidade));
				pedido.setValorDevolucao(pedido.getValorDevolucao().add(valorTotalComDesconto));
			}

		}

		// Salvo pedido com novo valor

		pedido.setItemExcluido(true);
		repositorio.salvar(pedido, "");
		// Muda o status das cotações e exclui os itens
		mudarStatusCotacao(listItem, houvePagamento);
		// Muda compra para o status de cotação

		lancRepository.salvarLog(log);

		return true;
	}

	@Transactional
	public boolean excluirItemPedidoTypeB(Pedido pedido, List<ItemPedido> listItem, User usuario) {

		Boolean houvePagamento = verificarPagamentos(pedido.getId());

		// Salva o log de quem fez a alteração
		LogStatus log = new LogStatus();
		log.setLancamento(pedido);
		log.setData(new Date());

		log.setUsuario(usuario);
		log.setComentario("Exclusão de itens pedidos TYPEB - RETURN. STATUS. PARCIAL OR CONCLUÍDO");
		// int quantideItensPedidos = pedido.getItensPedidos().size();
		// int quantidadeItensCancelado = listItem.size() +
		// lancRepository.buscarItensPedidosCancelados(pedido.getId());

		if (!houvePagamento) {
			for (ItemPedido itemPedido : listItem) {
				BigDecimal valorComDesconto = itemPedido.getValorUnitarioComDesconto();
				BigDecimal valorSemDesconto = itemPedido.getValorUnitario();
				Double quantidade = itemPedido.getQuantidade();
				BigDecimal valorTotalComDesconto = valorComDesconto.multiply(new BigDecimal(quantidade));
				BigDecimal valorTotalSemDesconto = valorSemDesconto.multiply(new BigDecimal(quantidade));
				pedido.setValorTotalComDesconto(pedido.getValorTotalComDesconto().subtract(valorTotalComDesconto));
				pedido.setValorTotalSemDesconto(pedido.getValorTotalSemDesconto().subtract(valorTotalSemDesconto));
				pedido.getItensPedidos().remove(pedido.getItensPedidos().stream()
						.filter(it -> it.getId().longValue() == itemPedido.getId().longValue()).findAny().get());
			}

		} else {
			if (pedido.getValorDevolucao() == null) {
				pedido.setValorDevolucao(BigDecimal.ZERO);
			}
			for (ItemPedido itemPedido : listItem) {
				BigDecimal valorComDesconto = itemPedido.getValorUnitarioComDesconto();
				Double quantidade = itemPedido.getQuantidade();
				BigDecimal valorTotalComDesconto = valorComDesconto.multiply(new BigDecimal(quantidade));
				pedido.setValorDevolucao(pedido.getValorDevolucao().add(valorTotalComDesconto));
				// pedido.getItensPedidos().remove(pedido.getItensPedidos().stream().filter(it
				// -> it.getId().longValue() ==
				// itemPedido.getId().longValue()).findAny().get());
			}
		}

		// if (CompraUtil.ComparaQuantidadeItensTYPEAB(pedido.getCompra(),
		// lancRepository, quantideItensPedidos - quantidadeItensCancelado,
		// pedido.getId())) {
		// lancRepository.mudarStatus(pedido.getCompra().getId(),
		// StatusCompra.CONCLUIDO);
		// log.setStatusLog(StatusCompra.CONCLUIDO);
		// } else {
		// lancRepository.mudarStatus(pedido.getCompra().getId(),
		// StatusCompra.PARCIAL_CONCLUIDO);
		// log.setStatusLog(StatusCompra.PARCIAL_CONCLUIDO);
		// }

		pedido.setItemExcluido(true);
		repositorio.salvar(pedido, "");
		mudarStatusCotacao(listItem, "TYPEB", pedido, houvePagamento);
		lancRepository.salvarLog(log);

		return true;
	}

	public StatusCompra atualizarStatus(Compra compra) {
		if (CompraUtil.ComparaQuantidadeItens(compra, lancRepository)) {
			lancRepository.mudarStatus(compra.getId(), StatusCompra.CONCLUIDO);
		} else {
			lancRepository.mudarStatus(compra.getId(), StatusCompra.PARCIAL_CONCLUIDO);
		}
		return compra.getStatusCompra();
	}

	public void mudarStatusCotacao(List<ItemPedido> itens, Boolean houvePagamento) {

		if (houvePagamento) {
			for (ItemPedido itemPedido : itens) {
				// cotacaoRepositorio.mudarStatusCotacaoEscolhaEEnvio(itemPedido.getCotacao().getId());
				// Se houver pagamento mudar somente o status do item pra
				// cancelado.
				itemPedido.setCancelado(true);
				lancRepository.salvarItemPedido(itemPedido);
			}
		} else {
			for (ItemPedido itemPedido : itens) {
				cotacaoRepositorio.mudarStatusCotacaoEscolhaEEnvio(itemPedido.getCotacao().getId());
				lancRepository.excluirItemPedido(itemPedido);
			}
		}

	}

	public void mudarStatusCotacao(List<ItemPedido> itens, String args, Pedido pedido, Boolean houvePagamento) {

		if (houvePagamento) {
			for (ItemPedido itemPedido : itens) {
				// cotacaoRepositorio.mudarStatusCotacaoEscolhaEEnvio(itemPedido.getCotacao().getId());
				// Se houver pagamento mudar somente o status do item
				itemPedido.setCancelado(true);
				lancRepository.salvarItemPedido(itemPedido);
				lancRepository.cancelarItemCompra(itemPedido.getIdItemCompra());
			}
		} else {
			for (ItemPedido itemPedido : itens) {
				cotacaoRepositorio.mudarStatusCotacaoEscolhaEEnvio(itemPedido.getCotacao().getId());
				// lancRepository.excluirItemPedido(itemPedido);
				// cotacaoRepositorio.excluirCotacaoByItem(itemPedido.getIdItemCompra());
				lancRepository.cancelarItemCompra(itemPedido.getIdItemCompra());
				// pedido.getItensPedidos().remove(pedido.getItensPedidos().stream().filter(it
				// -> it.getId().longValue() ==
				// itemPedido.getId().longValue()).findAny().get());
			}

		}
	}
	
	public void imprimirPedido(Pedido pedido, CompraService compraService) {

		List<ItemPedido> itens = getItensPedidosByPedido(pedido);

		List<LancamentoAcao> acoes = getLancamentosDoPedido(pedido);

		StringBuilder acs = new StringBuilder();

		if (pedido.getCompra().getVersionLancamento() != null
				|| pedido.getCompra().getVersionLancamento().equals("MODE01")) {
			for (LancamentoAcao lancamentoAcao : acoes) {
				acs.append(lancamentoAcao.getProjetoRubrica().getComponente().getNome());
				acs.append(" | ");
				acs.append(lancamentoAcao.getProjetoRubrica().getProjeto().getNome());
				acs.append(" | ");
				acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getTitulo());
				acs.append(" | ");
				acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getRubrica().getNome());
				acs.append(" | ");
				acs.append("R$: " + lancamentoAcao.getValor());
				acs.append("\n");
			}
		} else {
			for (LancamentoAcao lancamentoAcao : acoes) {
				acs.append(lancamentoAcao.getAcao().getCodigo() + " \n");
			}
		}

		// for (LancamentoAcao lancamentoAcao : acoes) {
		// acs.append(lancamentoAcao.getAcao().getCodigo() + ": " +
		// getFormatacao(lancamentoAcao.getValor()) + "\n");
		// }

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		String logo = path + "resources/image/logoFas.gif";
		String imgAprovacao = path + "resources/image/assinatura_aprovacao_lv.jpg";

		Map parametros = new HashMap();
		parametros.put("logo", logo);
		parametros.put("img_aprovacao", imgAprovacao);
		parametros.put("tipo_gestao", pedido.getTipoGestao().getNome());
		parametros.put("gestao", pedido.getGestao().getNome());
		parametros.put("tipo_localidade", pedido.getTipoLocalidade().getNome());
		parametros.put("localidade", pedido.getLocalidade().getNome());
		parametros.put("data_emissao", new SimpleDateFormat("dd/MM/yyyy").format(pedido.getDataEmissao()));
		parametros.put("fornecedor", pedido.getFornecedor().getNomeFantasia());
		parametros.put("contato", pedido.getFornecedor().getContato());
		parametros.put("banco", pedido.getFornecedor().getBanco());
		parametros.put("fone", pedido.getFornecedor().getTelefone());
		parametros.put("agencia", pedido.getFornecedor().getAgencia());
		parametros.put("conta", pedido.getFornecedor().getConta());
		parametros.put("data_entrega", new SimpleDateFormat("dd/MM/yyyy").format(pedido.getDataEntrega()));
		parametros.put("data_pagamento", new SimpleDateFormat("dd/MM/yyyy").format(pedido.getDataPagamento()));
		parametros.put("cnpj_fornecedor", pedido.getFornecedor().getCnpj());
		parametros.put("solicitante", pedido.getCompra().getSolicitante().getNome());
		//parametros.put("condicoes", pedido.getCondicaoPagamentoEnum().getNome());
		parametros.put("condicoes", "");
		parametros.put("subtotal", pedido.getValorTotalSemDesconto());
		parametros.put("desconto", pedido.getValorTotalSemDesconto().subtract(pedido.getValorTotalComDesconto()));
		parametros.put("total", pedido.getValorTotalComDesconto());

		parametros.put("usuario",
				pedido.getSolicitante().getNome() + " " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));

		String pedidoAux = pedido.getCompra().getId() + "/" + pedido.getId();
		parametros.put("pedido", pedido.getId());
		parametros.put("pedidoAux", pedidoAux);
		parametros.put("observ", pedido.getObservacao());

		parametros.put("acao", acs.toString());

		Util util = new Util();

		parametros.put("data_emissao", new SimpleDateFormat("dd/MM/yyyy hh:mm").format(pedido.getDataEmissao()));

		if (pedido.getTipoGestao().getNome().equals("Regional")) {
			// setarParamRegional(parametros);
			util.setarParamRegional(parametros, pedido);
		}

		if (pedido.getTipoGestao().getNome().equals("Coordenadoria")) {
			// setarParamCoordenadoria(parametros);
			util.setarParamCoordenadoria(parametros, pedido);
		}

		if (pedido.getTipoGestao().getNome().equals("Superintendencia")) {
			// setarParamSuperintendencia(parametros);
			util.setarParamSuperintendencia(parametros, pedido);
		}

		if (pedido.getTipoLocalidade().getNome().equals("Uc")) {
			// setarDestinoUC(parametros);
			util.setarDestinoUC(parametros, pedido, compraService);
		} else if (pedido.getTipoLocalidade().getNome().equals("Comunidade")) {
			// setarDestinoComunidade(parametros);
			util.setarDestinoComunidade(parametros, pedido);
		} else {
			// setarDestino(parametros);
			util.setarDestino(parametros, pedido);
		}

		JRDataSource dataSource = new JRBeanCollectionDataSource(itens, false);
		;

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/pedido_compra.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReport("Pedido", "PC" + pedido.getId().toString(), report, parametros, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Boolean verificarPagamentos(Long id) {
		return lancRepository.buscarPagamentoByLancamento(id, "");
	}

//	public Boolean verificarPagamentos(Long id) {
//		return lancRepository.buscarPagamentoByLancamento(id, "");
//	}

	public Fornecedor getFornecedorById(Long id) {
		return fornecedorRepositorio.getFornecedorPorId(id);
	}

	public Pedido getPedidoById(Long id) {
		return repositorio.getPedidoPorId(id);
	}

	public List<Cotacao> getCotacoes(Compra compra, Fornecedor fornecedor) {
		return repositorio.getCotacoes(fornecedor, compra);
	}

	public List<Fornecedor> getFornecedoresParaSelectBox(Compra compra) {
		return repositorio.getFornecedoresParaSelectBox(compra);
	}

	public List<Pedido> getPedidos(Compra compra) {
		return repositorio.getPedidos(compra);
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public List<LancamentoAcao> getLancamentosDoPedido(Pedido pedido) {
		return lancRepository.getLancamentosDoPedido(pedido);
	}
}
