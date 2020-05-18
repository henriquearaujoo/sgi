package service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.PrimeFaces;

import anotacoes.Transactional;
import exception.NegocioException;
import model.Aprouve;
import model.Compra;
import model.ContaBancaria;
import model.ControleExpedicao;
import model.Cotacao;
import model.CotacaoAuxiliar;
import model.CotacaoParent;
import model.DespesaReceita;
import model.EspelhoPedido;
import model.Fornecedor;
import model.Gestao;
import model.ItemCompra;
import model.ItemPedido;
import model.Lancamento;
import model.LancamentoAcao;
import model.LogStatus;
import model.Observacao;
import model.Pedido;
import model.Projeto;
import model.ProjetoRubrica;
import model.StatusCompra;
import model.TipoParcelamento;
import model.User;
import operator.Parcela;
import repositorio.AcaoRepositorio;
import repositorio.AprouveRepositorio;
import repositorio.AtividadeRepositorio;
import repositorio.CalculatorRubricaRepositorio;
import repositorio.ContaRepository;
import repositorio.ControleExpedicaoRepositorio;
import repositorio.CotacaoRepository;
import repositorio.FornecedorRepositorio;
import repositorio.GestaoRepositorio;
import repositorio.LancamentoRepository;
import repositorio.ProjetoRepositorio;
import repositorio.UsuarioRepository;
import util.CompraUtil;
import util.Filtro;

/**
 * @author Italo Almeida Classe: Gestao de Projeto Objetivo: Gerir a regra de
 *         negocio referente a instancia do objeto
 */

public class CotacaoService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private LancamentoRepository lancamentoRepositorio;

	@Inject
	private AcaoRepositorio acaoRepositorio;

	@Inject
	private CotacaoRepository repositorio;

	@Inject
	private FornecedorRepositorio fornecedorRepositorio;

	@Inject
	private ContaRepository contaRepositorio;

	@Inject
	private UsuarioRepository usuarioRepositorio;

	@Inject
	private ControleExpedicaoRepositorio expedicaoRepositorio;

	@Inject
	private GestaoRepositorio gestaoRepositorio;

	private @Inject AprouveRepositorio aprouveRepositorio;

	private @Inject ProjetoRepositorio projetoRepositorio;

	@Transactional
	public StatusCompra atualizarStatus(Compra compra) {
		if (CompraUtil.ComparaQuantidadeItens(compra, lancamentoRepositorio)) {
			lancamentoRepositorio.mudarStatus(compra.getId(), StatusCompra.CONCLUIDO);
		} else {
			lancamentoRepositorio.mudarStatus(compra.getId(), StatusCompra.PARCIAL_CONCLUIDO);
		}
		return compra.getStatusCompra();
	}

	public Compra getCompraById(Long id) {
		return lancamentoRepositorio.getCompraPorId(id);
	}

	public List<Projeto> getProjetosFiltroPorUsuarioMODE01(Filtro filtro, User usuario) {
		return projetoRepositorio.getProjetosFiltroPorUsuarioMODE01(filtro, usuario);
	}

	public List<Projeto> getProjetosByUsuario(User usuario) {
		return projetoRepositorio.getProjetosbyUsuario(usuario);
	}

	public List<Projeto> getProjetosbyUsuarioProjeto(User usuario, Filtro filtro) {
		return projetoRepositorio.getProjetosbyUsuarioProjeto(usuario, filtro);
	}

	public Boolean gerarPedido() {

		return null;
	}
	

	@Transactional
	public boolean salvar(List<Cotacao> cotacoes, Fornecedor fornecedor, CotacaoParent cotacaoParent) {
		try {

			Cotacao cotacaoAuxiliar = new Cotacao();
			setarStatusCompra(cotacoes.get(0).getCompra());
			cotacaoParent.setCompra(cotacoes.get(0).getCompra());
			cotacaoParent.setFornecedor(fornecedor);

			for (Cotacao cotacao : cotacoes) {
				if (cotacao.getId() != null) {
					repositorio.salvar(cotacao);
				} else {
					cotacao.setFornecedor(new Fornecedor());
					cotacao.setFornecedor(fornecedor);
					cotacaoAuxiliar = getCotacao(fornecedor, cotacao.getCompra(), cotacao.getItemCompra());
					if (cotacaoAuxiliar != null) {
						cotacaoAuxiliar.setValor(cotacao.getValor());
						cotacaoAuxiliar.setValorDesconto(cotacao.getValorDesconto());
						repositorio.salvar(cotacaoAuxiliar);
					} else {
						repositorio.salvar(cotacao);
					}
				}
			}

			if (cotacaoParent.getId() == null) cotacaoParent.setDataEmisao(Calendar.getInstance().getTime()); 
		 
			repositorio.salvarCotacaoParent(cotacaoParent);
			

		} catch (Exception e) {
			addMessage("", "Erro ao inserir cotação entre em contato com o administrator do sistema",
					FacesMessage.SEVERITY_ERROR);
			return false;
		}

		return true;
	}

	@Transactional
	public boolean salvar(List<Cotacao> cotacoes, Fornecedor fornecedor) {
		try {

			Cotacao cotacaoAuxiliar = new Cotacao();
			setarStatusCompra(cotacoes.get(0).getCompra());
			for (Cotacao cotacao : cotacoes) {
				if (cotacao.getId() != null) {
					repositorio.salvar(cotacao);
				} else {
					cotacao.setFornecedor(new Fornecedor());
					cotacao.setFornecedor(fornecedor);
					cotacaoAuxiliar = getCotacao(fornecedor, cotacao.getCompra(), cotacao.getItemCompra());
					if (cotacaoAuxiliar != null) {
						cotacaoAuxiliar.setValor(cotacao.getValor());
						cotacaoAuxiliar.setValorDesconto(cotacao.getValorDesconto());
						repositorio.salvar(cotacaoAuxiliar);
					} else {
						repositorio.salvar(cotacao);
					}
				}
			}
		} catch (Exception e) {
			addMessage("", "Erro ao inserir cotação entre em contato com o administrator do sistema",
					FacesMessage.SEVERITY_ERROR);
			return false;
		}

		return true;
	}

	@Transactional
	public void escolherCotacaoGlobal(Long idFornecedor, List<ItemCompra> itens, Long idCompra)
			throws NegocioException {
		for (ItemCompra it : itens) {
			Cotacao cot = repositorio.getCotacoesByFornecedorItemECompra(idFornecedor, it.getId(), idCompra);
			if (cot != null) {
				if (cot.getHouveEscolha() == null || !cot.getHouveEscolha()) {
					cot.setHouveEscolha(true);
					repositorio.salvar(cot);
				} else {
					throw new NegocioException(
							"Você escolheu um ou mais itens que já tiveram fornecedores selecionados!");
				}
			} else {
				throw new NegocioException("Você escolheu um item para fornecedor sem ter cotações para ele");
			}
		}
	}

	public void escolherCotacao(Long idFornecedor, List<ItemCompra> itens, Long idCompra) throws NegocioException {
		for (ItemCompra it : itens) {
			Cotacao cot = repositorio.getCotacoesByFornecedorItemECompra(idFornecedor, it.getId(), idCompra);
			if (cot != null) {
				if (cot.getHouveEscolha() == null || !cot.getHouveEscolha()) {
					cot.setHouveEscolha(true);
					repositorio.salvar(cot);
				} else {
					throw new NegocioException(
							"Você escolheu um ou mais itens que já tiveram fornecedores selecionados!");
				}
			} else {
				throw new NegocioException("Você escolheu um item para fornecedor sem ter cotações para ele");
			}
		}
	}

	@Transactional
	public Pedido gerarPedido(Long idFornecedor, List<ItemCompra> itens, Long idCompra, Pedido pedido, User usuario)
			throws Exception {

		escolherCotacao(idFornecedor, itens, idCompra);
		pedido = inicializaPedidoAntesDaAprovacao(idFornecedor, pedido, idCompra, usuario);
		pedido = lancamentoRepositorio.salvarPedido(pedido);
		LogStatus log = lancamentoRepositorio.gerarESalvarLogSolicitacao(pedido, "GPC", usuario);
		expedicaoRepositorio.gerarEsalvarExpedicao(pedido);

		if (pedido == null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Erro ao gerar o pedido, contate o administrador do sistema");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return null;
		}

		return pedido;
	}

	@Inject
	private CalculatorRubricaRepositorio calculatorRubricaRepositorio;

	@Inject
	private AtividadeRepositorio atividadeRepositorio;

	public Boolean verificaSaldoAtividade(Long idAtividadeProjeto, BigDecimal valor) {
		return atividadeRepositorio.verificarSaldoAtividade(idAtividadeProjeto, valor);
	}

	@Transactional
	public Pedido salvarRecursoNoPedido(Pedido pedido, List<ProjetoRubrica> recursos) {
		
		List<LancamentoAcao> list = lancamentoRepositorio.getLancamentoAcaoPorLancamento(pedido.getId(), "");

		for (LancamentoAcao lancamentoAcao : list) {
			repositorio.removeAllRecursoPagamento(lancamentoAcao.getId());
		}
		
		repositorio.removeAllRecurso(pedido.getId());
		
		List<LancamentoAcao> listRecursosVinculados = gerarLancamentoAcao(pedido, recursos);

		if (listRecursosVinculados != null) {
			pedido.setStatusCompra(StatusCompra.PENDENTE_APROVACAO);
			pedido = lancamentoRepositorio.salvarPedido(pedido, listRecursosVinculados);
		}
		
		return pedido;

	}

	public List<LancamentoAcao> gerarLancamentoAcao(Pedido pedido, List<ProjetoRubrica> recursos) {
		LancamentoAcao lancamentoAcao;
		List<LancamentoAcao> retorno = new ArrayList<>();

		for (ProjetoRubrica recurso : recursos) {
			if (!calculatorRubricaRepositorio.verificarSaldoRubrica(recurso.getId(), recurso.getValorEmpenhado())) {
				addMessage("",
						"Rubrica sem saldo suficiente: " + recurso.getNomeProjeto() + "/" + recurso.getNomeCategoria(),
						FacesMessage.SEVERITY_WARN);
				return null;
			}

			lancamentoAcao = new LancamentoAcao();
			lancamentoAcao.setProjetoRubrica(recurso);
			lancamentoAcao.setValor(recurso.getValorEmpenhado());
			lancamentoAcao.setFornecedor(pedido.getFornecedor());
			lancamentoAcao.setCompra(pedido.getCompra());
			lancamentoAcao.setDespesaReceita(DespesaReceita.DESPESA);
			lancamentoAcao.setLancamento(pedido);
			retorno.add(lancamentoAcao);

		}

		return retorno;
	}

	public Pedido inicializaPedidoAntesDaAprovacao(Long fornecedor, Pedido pedido, Long idCompra, User usuario)
			throws NegocioException {

		Compra compra = getCompraById(idCompra);

		pedido = repositorio.getPedidoByCompraAndForneced(fornecedor, idCompra);

		if (pedido == null) {
			pedido = new Pedido();
			pedido.setDataEntrega(new Date());
			pedido.setDataPagamento(new Date());
			pedido.setQuantidadeParcela(1);
			pedido.setObservacao("");

			pedido.setFornecedor(getFornecedorById(fornecedor));
			pedido.setCompra(compra);
			pedido.setSolicitante(compra.getSolicitante());
			pedido.setItensPedidos(getItensPedidosAntesDaAprovacao(fornecedor, pedido, compra));
			pedido.setTipoGestao(compra.getTipoGestao());
			pedido.setGestao(compra.getGestao());
			pedido.setTipoLocalidade(compra.getTipoLocalidade());
			pedido.setLocalidade(compra.getLocalidade());
			pedido.setDataEmissao(new Date());
			pedido.setVersionLancamento("MODE01");
			pedido.setTipoLancamento("ct");
			pedido.setDepesaReceita(DespesaReceita.DESPESA);

			pedido.setUsuarioGeradorPedido(usuario);
			pedido.setStatusCompra(StatusCompra.N_INCIADO);
			pedido.setVersionLancamento("MODE01");

		} else {
			pedido.getItensPedidos().clear();
			pedido.getItensPedidos().addAll(getItensPedidosAntesDaAprovacao(fornecedor, pedido, compra));

		}

		ContaBancaria conta = getContaByFornecedor(fornecedor);
		if (conta == null) {
			throw new NegocioException(
					"Não existe uma conta para o fornecedor, por favor entre em contato com o adm para que seja efetuado o cadastro!");
		}

		pedido.setContaRecebedor(conta);
		pedido.setTipov4("PC");

		return pedido;
	}

	protected List<ItemPedido> getItensPedidosAntesDaAprovacao(Long fornecedor, Pedido pedido, Compra compra)
			throws NegocioException {

		List<Cotacao> listAux = getCotacoesHouveEscolha(fornecedor, compra);
		List<ItemPedido> itens = new ArrayList<>();
		BigDecimal totalValor = BigDecimal.ZERO;
		BigDecimal totalValorDesconto = BigDecimal.ZERO;

		for (Cotacao cot : listAux) {

			ItemPedido it = new ItemPedido();
			it.setPedido(pedido);
			it.setUnidade(cot.getItemCompra().getUnidade());
			it.setCotacao(cot);
			it.setQuantidade(cot.getItemCompra().getQuantidade());
			it.setIdItemCompra(cot.getItemCompra().getId());
			it.setTipoGestao(compra.getTipoGestao());
			it.setTipoLocalidade(compra.getTipoLocalidade());
			it.setGestao(compra.getGestao());
			it.setLocalidade(compra.getLocalidade());
			it.setCategoria(cot.getItemCompra().getProduto().getCategoria());
			it.setDescricaoProduto(cot.getItemCompra().getProduto().getDescricao());
			it.setTipo(cot.getItemCompra().getProduto().getTipo());
			it.setValorUnitario(cot.getValor());
			it.setValorUnitarioComDesconto(cot.getValorDesconto());
			it.setValorTotal(cot.getValorDesconto().multiply(BigDecimal.valueOf(cot.getQuantidade())));
			it.setUnidade(cot.getItemCompra().getUnidade() != null ? cot.getItemCompra().getUnidade() : "");
			it.setDescricaoComplementar(cot.getItemCompra().getDescricaoComplementar() != null
					? cot.getItemCompra().getDescricaoComplementar()
					: "");
			itens.add(it);
			totalValor = totalValor.add(cot.getValor().multiply(BigDecimal.valueOf(cot.getQuantidade())));
			totalValorDesconto = totalValorDesconto
					.add(cot.getValorDesconto().multiply(BigDecimal.valueOf(cot.getQuantidade())));

		}

		pedido.setValorTotalSemDesconto(totalValor);
		pedido.setValorTotalComDesconto(totalValorDesconto);
		pedido.setTotalDeDesonto(pedido.getValorTotalSemDesconto().subtract(pedido.getValorTotalComDesconto()));
		return itens;
	}

	public boolean findAprouvePedido(Aprouve aprouve) {
		return aprouveRepositorio.findAprouvePedido(aprouve);
	}

	public List<User> getUsuario(String nome) {
		return usuarioRepositorio.getUsuario(nome);
	}

	public Observacao findObservacaoByCompraAndItem(Long idCompra, Long idItem) {
		return lancamentoRepositorio.findObservacaoByCompra(idCompra, idItem);
	}

	public User getUsuarioByColaborador(Long colaborador) {
		return usuarioRepositorio.getUsuarioByColaborador(colaborador);
	}

	@Transactional
	public Boolean excluirCotacao(Long idCompra, Long idFornecedor) {
		Boolean retorno = repositorio.excluirCotacao(idCompra, idFornecedor);
		retorno = repositorio.excluirCotacaoParent(idCompra, idFornecedor);
		return retorno;
	}

	public List<Observacao> getObservacoes(Compra compra) {
		return lancamentoRepositorio.getObservacoes(compra);
	}

	public Boolean verificarVinculoCotacao(Long fornecedor, Long compra) {
		List<Cotacao> list = repositorio.getCotacoesByCompraByFornecedor(fornecedor, compra);

		for (Cotacao cotacao : list) {

		}

		return true;
	}

	public BigDecimal getSaldoAcao(Long id) {
		return acaoRepositorio.getSaldoAcao(id);
	}

	public ContaBancaria getContaByFornecedor(Long fornecedor) {
		return contaRepositorio.getContaByFornecedor(fornecedor);
	}

	public void setarStatusCompra(Compra compra) {
		if (!repositorio.verificarSeHaCotacao(compra)) {
			compra.setStatusCompra(StatusCompra.EM_COTACAO);
			lancamentoRepositorio.salvar(compra);
		} else if (compra.getStatusCompra().getNome().equals("Autorizado")) {
			compra.setStatusCompra(StatusCompra.EM_COTACAO);
			lancamentoRepositorio.salvar(compra);
		}
	}

	public List<LancamentoAcao> getLancamentosAcoes(Lancamento lancamento) {
		return lancamentoRepositorio.getLancamentosAcoes(lancamento);
	}

	@Transactional
	public boolean salvarPedido(Pedido pedido, User usuario) {
		try {

			LogStatus log = new LogStatus();
			log.setUsuario(usuario);
			log.setLancamento(pedido.getCompra());
			log.setData(new Date());
			log.setStatusLog(StatusCompra.APROVADO);
			log.setSiglaPrivilegio("APC");
			log.setSigla(pedido.getCompra().getGestao().getSigla());
			lancamentoRepositorio.salvarLog(log);

			Compra compra = pedido.getCompra();

			if (CompraUtil.ComparaQuantidadeItens(pedido.getCompra(), lancamentoRepositorio,
					pedido.getItensPedidos().size())) {
				compra.setStatusCompra(StatusCompra.CONCLUIDO);
			} else {
				compra.setStatusCompra(StatusCompra.PARCIAL_CONCLUIDO);
			}

			lancamentoRepositorio.salvar(compra);
			lancamentoRepositorio.salvar(pedido);

			/*
			 * List<LancamentoAcao> lList =
			 * compraRepositorio.getLancamentosDoPedido(pedido);
			 * 
			 * for (LancamentoAcao lancamentoAcao : lList) {
			 * lancamentoAcao.setLancamento(pedido);
			 * compraRepositorio.salvarLancamentoAcao(lancamentoAcao); }
			 */

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Transactional
	public Pedido salvarPedidoAntesDaAprovacao(Pedido pedido, ControleExpedicao controle, User usuario)
			throws Exception {
		pedido = lancamentoRepositorio.salvarPedidoAntesDaAprovacao(pedido, controle, usuario);
		return pedido;
	}

	public Pedido salvarPedidoAntesDaAprovacao(Pedido pedido, User usuario) throws Exception {

		ControleExpedicao controle = new ControleExpedicao();
		controle.setNomeResponsavel("");

		pedido = lancamentoRepositorio.salvarPedidoAntesDaAprovacao(pedido, controle, usuario);
		return pedido;
	}

	@Transactional
	public Pedido salvarPedido(Pedido pedido, ControleExpedicao controle, User usuario){

		return lancamentoRepositorio.salvarPedido(pedido, controle, usuario);

	}

	@Transactional
	public Pedido salvarPedidoSemMudarStatus(Pedido pedido, ControleExpedicao controle, User usuario) throws Exception {
		pedido = lancamentoRepositorio.salvarPedidoSemMudarStatusCompra(pedido, controle, usuario);
		return pedido;
	}

	// @Transactional
	// public boolean salvarPedido(Pedido pedido, ControleExpedicao
	// controleExpedicao, User usuario) {
	// try {
	//
	// if (controleExpedicao.getId() == null) {
	// controleExpedicao.setDataAceitacaoExpedicao(new Date());
	// }
	//
	// controleExpedicao.setPedido(pedido);
	// // pedido.setDescricao("Pedido - "+pedido.getId());
	// pedido = compraRepositorio.salvarPedidoExpedicao(pedido, controleExpedicao);
	//
	// } catch (Exception e) {
	// return false;
	// }
	// return true;
	// }

	private TipoParcelamento tipoParcelamento;

	public Date setarData(Date dataBase) {
		Parcela parcela = tipoParcelamento.obterParcela();
		Date data = parcela.calculaData(dataBase);
		return data;
	}

	public void getLancamentoAcao(Pedido pedido) {

		List<LancamentoAcao> alList = lancamentoRepositorio
				.getLancamentoPorFornecedorECompra(pedido.getFornecedor().getId(), pedido.getCompra());

		for (LancamentoAcao lancamentoAcao : alList) {
			lancamentoAcao.setLancamento(pedido);
			lancamentoRepositorio.salvarLancamentoAcao(lancamentoAcao);

		}
	}

	public ControleExpedicao findControleExpedByPedido(Long id) {
		return expedicaoRepositorio.findControleExpedByPedido(id);
	}

	public Pedido getPedidoPorCompraEFornecedor(Long fornecedor, Compra compra) {
		return lancamentoRepositorio.getPedidoPorCompraEFornecedor(fornecedor, compra);
	}

	public boolean escolherPedido(Long fornecedor, Compra compra) {

		List<Cotacao> cotacoes = new ArrayList<Cotacao>();
		cotacoes = repositorio.getCotacoes(fornecedor, compra);

		try {

			for (Cotacao cotacao : cotacoes) {
				// seta o valor indicando que foi enviado para aprovação
				cotacao.setHouvePedido(true);
				repositorio.salvar(cotacao);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Transactional
	public Boolean deletarLancamentoFormaDePagamento(Long id) {
		try {
			lancamentoRepositorio.deletarTodosPagamentos(id);
			repositorio.deletarLancamentoFormaDePagamento(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Transactional
	public Boolean deletarLancamentoFormaDePagamento(LancamentoAcao lancamentoAcao) {
		try {
			repositorio.deletarLancamentoFormaDePagamento(lancamentoAcao.getId());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean deletarRecurso(LancamentoAcao lancamentoAcao) {
		try {
			repositorio.deletarLancamentoFormaDePagamento(lancamentoAcao.getId());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Inject
	private PedidoService pedidoService;

	@Transactional
	public void removerPedido(List<Pedido> pedidos) {

		if (pedidos.size() > 0) {

			for (Pedido pedido : pedidos) {
				try {

					pedido = lancamentoRepositorio.getPedidoPorId(pedido.getId());

					List<LancamentoAcao> list = lancamentoRepositorio.getLancamentoAcaoPorLancamento(pedido.getId(),
							"");

					for (LancamentoAcao lancamentoAcao : list) {
						repositorio.removeAllRecursoPagamento(lancamentoAcao.getId());
					}

					repositorio.removeAllLog(pedido.getId());
					repositorio.removeAllRecurso(pedido.getId());
					repositorio.removeAllExpedicao(pedido.getId());

					pedido = pedidoService.getPedidoById(pedido.getId());
					List<ItemPedido> itens = pedidoService.getItensPedido(pedido);

					for (ItemPedido itemPedido : itens) {
						repositorio.desfazerEscolhaDeCotacaoPedido(itemPedido.getCotacao().getId());
					}

					repositorio.mudarStatus(pedido.getCompra().getId(), StatusCompra.EM_COTACAO);
					ControleExpedicao controleExpedicao = expedicaoRepositorio
							.findControleExpedByPedido(pedido.getId());

					if (controleExpedicao.getId() != null)
						expedicaoRepositorio.remover(controleExpedicao);

					repositorio.remover(pedido);

				} catch (Exception e) {
					addMessage("", "Erro ao remover pedido: " + pedido.getId()
							+ ", entre em contato com o administrator do sistema", FacesMessage.SEVERITY_ERROR);

				}
			}

		} else {
			addMessage("", "Você deve escolher 1 pedido ou mais para exclusão!", FacesMessage.SEVERITY_ERROR);
			return;
		}
	
		addMessage("", "("+pedidos.size() + ") foram excluídos!", FacesMessage.SEVERITY_INFO);
	
	}

	@Transactional
	public void remover(Pedido pedido) {
		try {

			pedido = lancamentoRepositorio.getPedidoPorId(pedido.getId());

			List<LancamentoAcao> list = lancamentoRepositorio.getLancamentoAcaoPorLancamento(pedido.getId(), "");

			for (LancamentoAcao lancamentoAcao : list) {
				repositorio.removeAllRecursoPagamento(lancamentoAcao.getId());
			}

			repositorio.removeAllLog(pedido.getId());
			repositorio.removeAllRecurso(pedido.getId());
			repositorio.removeAllExpedicao(pedido.getId());

			pedido = pedidoService.getPedidoById(pedido.getId());
			List<ItemPedido> itens = pedidoService.getItensPedido(pedido);

			for (ItemPedido itemPedido : itens) {
				repositorio.desfazerEscolhaDeCotacaoPedido(itemPedido.getCotacao().getId());
			}

			repositorio.mudarStatus(pedido.getCompra().getId(), StatusCompra.EM_COTACAO);
			ControleExpedicao controleExpedicao = expedicaoRepositorio.findControleExpedByPedido(pedido.getId());

			if (controleExpedicao.getId() != null)
				expedicaoRepositorio.remover(controleExpedicao);

			repositorio.remover(pedido);

		} catch (Exception e) {
			addMessage("", "Erro ao remover pedido entre em contato com o administrator do sistema",
					FacesMessage.SEVERITY_ERROR);

		}

	}

	@Transactional
	public boolean escolherPedido(List<LancamentoAcao> lancamentos) {

		if (lancamentos.size() > 0) {

			LancamentoAcao lanc = lancamentos.get(0);
			escolherPedido(lanc.getFornecedor().getId(), lanc.getCompra());

			for (LancamentoAcao lancamentoAcao : lancamentos) {
				lancamentoRepositorio.salvarLancamentoAcao(lancamentoAcao);
			}

		} else {
			addMessage("", "Não foi possível salvar os lançamentos", FacesMessage.SEVERITY_ERROR);
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

	@Transactional
	public void salvarLancamento(LancamentoAcao lancamento) {
		lancamentoRepositorio.salvarLancamentoAcao(lancamento);

	}

	public LancamentoAcao salvarLancamento(LancamentoAcao lancamento, String args) {
		return lancamentoRepositorio.salvarLancamentoAcao(lancamento);

	}

	public List<LancamentoAcao> getListaDeLancamentoPorFornecedorECompra(Long fornecedor, Compra compra) {
		return lancamentoRepositorio.getLancamentoPorFornecedorECompra(fornecedor, compra);
	}

	@Transactional
	public void removerLancamentoAcao(LancamentoAcao lancamento) {
		lancamentoRepositorio.removerLancamentoAcao(lancamento);
	}

	public LancamentoAcao getLancamentoAcaoPorAcaoEfonte(LancamentoAcao lancamento) {
		return lancamentoRepositorio.getLancamentoAcaoPorAcaoEfonte(lancamento);
	}

	public void salvarCotacaoComoPedido(Compra compra, Fornecedor fornecedor) {

	}

	public List<Cotacao> getCotacoesHouveEscolha(Long fornecedor, Compra compra) {
		return repositorio.getCotacoesHouveEscolha(fornecedor, compra);
	}

	@Transactional
	public boolean salvar(Cotacao cotacao) {

		Cotacao cot = repositorio.verificarCotacaoExiste(cotacao);
		if (cot != null) {
			cot.setHouveEscolha(false);
			repositorio.salvar(cot);
		}

		repositorio.salvar(cotacao);
		return true;
	}

	@Transactional
	public boolean salvarObservacao(Observacao obs) {
		lancamentoRepositorio.salvarObservacao(obs);
		return true;
	}

	// A cotação deve voltar ao estado de não pedido (houvePedido = false)
	@Transactional
	public boolean deletarItemPedidoCompra(ItemPedido item) throws NegocioException {
		repositorio.deletarItemPedidoCompra(item.getCotacao().getId());
		repositorio.excluirItemPedido(item);
		return true;
	}

	@Transactional
	public boolean deletarItemPedidoCompra(Long id) throws NegocioException {
		repositorio.deletarItemPedidoCompra(id);
		return true;
	}

	public Pedido getPedidoPorId(Long id) {
		return this.lancamentoRepositorio.getPedidoPorId(id);
	}

	public boolean verificarExisteItemPedidoByCotacao(Long id) {
		return repositorio.verificarExisteItemPedidoByCotacao(id);
	}

	public boolean verificarExisteItensPedidoByCotacoes(Long idFornecedor, Long idCompra) {
		return repositorio.verificarExisteItensPedidoByCotacoes(idFornecedor, idCompra);
	}

	@Transactional
	public void deletarEspelho(Long idFornecedor, Long idCompra) {
		repositorio.deletarItensEspelhoPedidoCompra(idFornecedor, idCompra);
	}

	public Boolean verificarSeHouvePedido(Long item) {
		return repositorio.verificarSeHouvePedido(item);
	}

	public Boolean verificarSeHouvePedidoNoItem(Long item) {
		return repositorio.verificarSeHouvePedidoNoItem(item);
	}

	public List<LancamentoAcao> getLancamentos(Compra compra) {
		return lancamentoRepositorio.getLancamentosAcao(compra);
	}

	public List<Cotacao> getValorCotacao(Long idForn, Long compra, Long itemCompra) {
		return repositorio.getValorCotacao(idForn, compra, itemCompra);
	}

	public List<Cotacao> getCotacaoEscolha(Long idForn, Long compra, Long itemCompra) {
		return repositorio.getCotacaoEscolha(idForn, compra, itemCompra);
	}

	public ItemCompra getValoresDeItem(Long fornecedorId, Long itemCompraId) {
		return repositorio.getValoresDeItem(fornecedorId, itemCompraId);
	}

	public List<ItemCompra> getItensTransientByCompra(Compra compra) {
		return lancamentoRepositorio.getItensTransientByCompra(compra);
	}

	public Fornecedor getFornecedorById(Long id) {
		return fornecedorRepositorio.getFornecedorPorId(id);
	}

	public List<Fornecedor> getFornecedores(String s) {
		return repositorio.getFornecedores(s);
	}

	public List<ItemCompra> getItensByCompra(Compra compra) {
		return lancamentoRepositorio.getItensByCompra(compra);
	}

	public List<ItemCompra> getItensByCompraCotacao(Compra compra) {
		return lancamentoRepositorio.getItensByCompra(compra);
	}

	public Cotacao getCotacao(Fornecedor fornecedor, Compra compra, ItemCompra item) {
		return repositorio.getCotacao(fornecedor, compra, item);
	}

	public CotacaoParent getCotacaoParent(Fornecedor fornecedor, Compra compra) {
		return repositorio.getCotacaoParent(fornecedor, compra);
	}

	public List<CotacaoAuxiliar> getCotacaoAuxiliar(Long id) {
		return repositorio.getCotacaoAuxiliar(id);
	}

	public List<EspelhoPedido> getEspelhoPedido(Compra compra) {
		return repositorio.getEspelhoPedido(compra);
	}

	public List<Cotacao> getCotacoes(Compra compra, Fornecedor fornecedor) {
		return repositorio.getCotacoes(fornecedor, compra);
	}

	public List<Cotacao> getCotacoes(ItemCompra itemCompra) {
		return repositorio.getCotacoes(itemCompra);
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public boolean verificarExistePedido(Long fornecedor, Compra compra) {
		return lancamentoRepositorio.verificarExistePedido(fornecedor, compra);
	}
	
	

}
