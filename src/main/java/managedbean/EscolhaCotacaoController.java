package managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;

import exception.NegocioException;
import model.Aprouve;
import model.AtividadeProjeto;
import model.Compra;
import model.CondicaoPagamento;
import model.ContaBancaria;
import model.ControleExpedicao;
import model.Cotacao;
import model.CotacaoAuxiliar;
import model.DespesaReceita;
import model.EspelhoPedido;
import model.Fornecedor;
import model.Gestao;
import model.ItemCompra;
import model.ItemPedido;
import model.LancamentoAcao;
import model.MenuLateral;
import model.Observacao;
import model.Pedido;
import model.Projeto;
import model.ProjetoRubrica;
import model.StatusCompra;
import model.TipoParcelamento;
import model.User;
import repositorio.CalculatorRubricaRepositorio;
import service.CotacaoService;
import service.EscolhaCotacaoService;
import service.PedidoService;
import service.ProjetoService;
import service.SolicitacaoPagamentoService;
import util.Email;
import util.Filtro;
import util.MakeMenu;
import util.UsuarioSessao;

@Named(value = "escolhaCotacao")
@ViewScoped
public class EscolhaCotacaoController implements Serializable {

	private static final long serialVersionUID = 1L;

	private @Inject Compra compra;

	private @Inject UsuarioSessao usuarioSessao;

	private @Inject CotacaoService cotacaoService;

	@Inject
	private EscolhaCotacaoService escolhaService;

	private List<Cotacao> cotacoes = new ArrayList<Cotacao>();

	@Inject
	private LancamentoAcao lancamentoAcao;

	@Inject
	private LancamentoAcao lancamentoAux;

	@Inject
	private CalculatorRubricaRepositorio calculatorRubricaRepositorio;

	@Inject
	private ProjetoService projetoService;

	private @Inject ControleExpedicao controleExpedicao;

	private List<AtividadeProjeto> listaAtividadeProjeto;

	private List<LancamentoAcao> lancamentos = new ArrayList<>();

	private List<LancamentoAcao> formasDePagamento = new ArrayList<>();

	private Long fornecedor;

	private DataTable tableRank;

	private DataTable table = new DataTable();

	private Cotacao cotacao = new Cotacao();

	private MenuLateral menu = new MenuLateral();

	private String nomeFornecedor = "";
	private String observacao = "";

	private BigDecimal valorTotalPedidoSemDesconto;
	private BigDecimal valorTotalPedidoComDesconto;

	private Email email = new Email();
	private Observacao obs = new Observacao();

	private BigDecimal valorTotalAliberar = BigDecimal.ZERO;
	private BigDecimal valorTotalLiberado = BigDecimal.ZERO;
	private BigDecimal valorTotalFaltaLiberar = BigDecimal.ZERO;

	private Long idFornecedor;

	private Boolean mostraJustificativa = false;

	private List<ItemCompra> listaItensDeCompraAuxiliar = new ArrayList<>();

	private String item = "";

	private List<CotacaoAuxiliar> cotacoesAuxiliares = new ArrayList<>();
	private List<ItemCompra> itens = new ArrayList<ItemCompra>();
	private List<EspelhoPedido> espelhos = new ArrayList<>();

	private List<Pedido> pedidos = new ArrayList<Pedido>();
	private List<Pedido> pedidosSelecionados = new ArrayList<Pedido>();
	private List<Cotacao> cotacoesPedido = new ArrayList<>();
	private List<Gestao> listGestao = new ArrayList<>();

	public EscolhaCotacaoController() {
	}

	public void aprovarEspelhoComoPedido() {
		if (!this.verificaCamposPedido(this.pedido, this.controleExpedicao)) {
			if (this.verificarPrivilegio()) {
				Label_0397: {
					try {
						this.inicializaPedido(this.fornecedor, this.pedido);
						this.pedido.setUsuarioGeradorPedido(this.usuarioSessao.getUsuario());
						this.pedido.setStatusCompra(StatusCompra.CONCLUIDO);
						this.pedido.setVersionLancamento("MODE01");
						this.pedido = this.cotacaoService.salvarPedido(this.pedido, this.controleExpedicao, this.usuarioSessao.getUsuario());
						if (this.pedido == null) {
							final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Aten\u00e7\u00e3o", "Erro ao gerar o pedido, contate o administrador do sistema");
							FacesContext.getCurrentInstance().addMessage(null, message);
							return;
						}
						this.carregarMapa();
						this.cotacaoService.atualizarStatus(this.pedido.getCompra());
						this.addMessage("", "Pedido gerado com sucesso!", FacesMessage.SEVERITY_INFO);
						if (this.email.verificaInternet()) {
							this.enviarEmailAprovacaoPedido2();
							this.limparEmail();
							break Label_0397;
						}
						final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Aten\u00e7\u00e3o", "Problema de conex\u00e3o com a internet. O pedido foi gerado no entanto o e-mail de aviso n\u00e3o foi enviado, tente novamente mais tarde!");
						FacesContext.getCurrentInstance().addMessage(null, message);
					}
					catch (NoResultException e2) {
						final FacesMessage message2 = new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Rubrica sem saldo suficiente");
						FacesContext.getCurrentInstance().addMessage((String)null, message2);
					}
					catch (Exception e) {
						if (e.getMessage().equals("www.gmail.com")) {
							final FacesMessage message2 = new FacesMessage(FacesMessage.SEVERITY_WARN, "Aten\u00e7\u00e3o", "Problema de conex\u00e3o com a internet. O pedido foi gerado no entanto o e-mail de aviso n\u00e3o foi enviado, tente novamente mais tarde!");
							FacesContext.getCurrentInstance().addMessage(null, message2);
						}
						else {
							final FacesMessage message2 = new FacesMessage(FacesMessage.SEVERITY_WARN, "Aten\u00e7\u00e3o", e.getMessage());
							FacesContext.getCurrentInstance().addMessage(null, message2);
						}
					}
					finally {
						this.pedido = new Pedido();
						this.controleExpedicao = new ControleExpedicao();
					}
				}
				this.pedido = new Pedido();
				this.controleExpedicao = new ControleExpedicao();
			}
			else {
				final FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Aten\u00e7\u00e3o", "Voc\u00ea n\u00e3o tem privil\u00e9gios para esse tipo de a\u00e7\u00e3o, por favor contate o coordenador de compras!");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		}
	}

	public void escolherCotacao() {
		if (this.cotacao != null) {
			int cont = 1;
			for (final Cotacao cotacaoAux : this.cotacoes) {
				if (cotacaoAux.getId() != (long)this.cotacao.getId() && this.cotacao.getValorDesconto().doubleValue() > cotacaoAux.getValorDesconto().doubleValue()) {
					if (this.obs.getTexto() == null) {
						this.addMessage("", "Por favor explique o motivo de n\u00e3o comprar o menor pre\u00e7o!.", FacesMessage.SEVERITY_WARN);
						this.addMessage("", "Explica\u00e7\u00e3o com no minimo 5 caracteres!", FacesMessage.SEVERITY_WARN);
						return;
					}
					if (this.obs.getTexto().length() < 5) {
						this.addMessage("", "Por favor explique o motivo de n\u00e3o comprar o menor pre\u00e7o!.", FacesMessage.SEVERITY_WARN);
						this.addMessage("", "Explica\u00e7\u00e3o com no minimo 5 caracteres!", FacesMessage.SEVERITY_WARN);
						return;
					}
				}
				++cont;
			}
			this.cotacao.setHouveEscolha(Boolean.valueOf(true));
			this.cotacaoService.salvar(this.cotacao);
			this.cotacoes = (List<Cotacao>)this.cotacaoService.getCotacoes(this.cotacao.getItemCompra());
			this.espelhos = (List<EspelhoPedido>)this.cotacaoService.getEspelhoPedido(this.cotacao.getCompra());
			if (this.obs.getTexto().length() > 5) {
				this.cotacaoService.salvarObservacao(this.obs);
			}
			this.addMessage("", "Cota\u00e7\u00e3o escolhida: " + this.cotacao.getFornecedor().getNomeFantasia() + " Valor: " + this.cotacao.getValor(), FacesMessage.SEVERITY_INFO);
			this.cotacao = new Cotacao();
		}
		else {
			this.addMessage("", "Selecione uma das cota\u00e7\u00f5es", FacesMessage.SEVERITY_ERROR);
		}
	}

	public void carregarCotacoes() throws IOException {

		/*
		 * compra = (Compra)
		 * FacesContext.getCurrentInstance().getExternalContext().getFlash()
		 * .get("compra" + usuarioSessao.getNomeUsuario());
		 */
		if (compra == null || compra.getId() == null) {
			// FacesContext.getCurrentInstance().getExternalContext().getFlash().put("compra"+usuarioSessao.getNomeUsuario(),
			// cotacoes.get(0).getCompra());
			HttpServletResponse resp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			resp.sendRedirect(req.getContextPath() + "/main/cotacao.xhtml");
			return;
		}
		cotacoesAuxiliares = cotacaoService.getCotacaoAuxiliar(compra.getId());
		// espelhos = cotacaoService.getEspelhoPedido(compra);
		pedidos = getPedidosByCompra(compra);
		itens = cotacaoService.getItensByCompra(compra);
	}

	public void carregarPedidos() {
		pedidos = getPedidosByCompra(compra);
	}

	public boolean verificarSeHouvePedido(Long item) {
		return cotacaoService.verificarSeHouvePedido(item);
	}

	public boolean verificarSeHouvePedidoNoItem(Long item) {
		return cotacaoService.verificarSeHouvePedidoNoItem(item);
	}

	public void salvarRecursoNoPedido() {

		pedido = cotacaoService.salvarRecursoNoPedido(pedido, listaDeRubricasProjeto);

		try {
			enviarEmailSolicitacaoAutorizacaoPedido();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		addMessage("",
				"Congrats, um email  foi enviado informando ao aprovador, caso não tenha recebido cópia deste email, por favor, clique em 'Reenviar'",
				FacesMessage.SEVERITY_INFO);

	}

	public void testeIteracao() {
		for (Pedido pedido : pedidosSelecionados) {

		}
	}

	public void selecionarFornecedor() {

		if (pedidosSelecionados.size() == 1) {
			if (verificarPrivilegio()) {
				this.pedido = cotacaoService.getPedidoPorId(pedidosSelecionados.get(0).getId());
				listaRubricasCompra();
				popularValores();
				carregarLancamentos();
				limpar();

				PrimeFaces current = PrimeFaces.current();
				current.executeScript("PF('determinar_recursoMODE01').show();");
			}
		} else {
			addMessage("", "Você deve escolher 1 pedido (somente 1 pedido) para definir os recursos",
					FacesMessage.SEVERITY_ERROR);
		}

	}

	public void excluirPedido() {
		cotacaoService.removerPedido(pedidosSelecionados);
		carregarPedidos();
	}

	public void editarPedido() {

		if (pedidosSelecionados.size() == 1) {
			if (verificarPrivilegio()) {
				this.pedido = cotacaoService.getPedidoPorId(pedidosSelecionados.get(0).getId());
				fornecedor = pedido.getFornecedor().getId();
				contasFornecedor(pedido.getFornecedor());
				valorTotalPedidoComDesconto = pedido.getValorTotalComDesconto();
				valorTotalPedidoSemDesconto = pedido.getValorTotalSemDesconto();
				listGestao = cotacaoService.buscarGestao();

				controleExpedicao = cotacaoService.findControleExpedByPedido(pedido.getId());
				if (controleExpedicao.getId() == null) {
					controleExpedicao = new ControleExpedicao();
					controleExpedicao.setNomeResponsavel("");
					controleExpedicao.setDataAceitacaoExpedicao(new Date());
				}

				abrirDialog("PF('prepara_aprovacao').show();");

			}
		} else {
			addMessage("", "Você deve escolher 1 pedido (somente 1 pedido) para definir autorização",
					FacesMessage.SEVERITY_ERROR);
		}

	}

	public void selecionarFornecedor(Pedido pedido) {

		if (pedidosSelecionados.size() > 0) {
			if (verificarPrivilegio()) {
				this.pedido = pedido;
				listaRubricasCompra();
				this.fornecedor = pedido.getFornecedor().getId();
				this.valorTotalPedidoComDesconto = pedido.getValorTotalComDesconto();
				this.valorTotalAliberar = pedido.getValorTotalComDesconto();
				this.nomeFornecedor = pedido.getFornecedor().getNomeFantasia();
				this.valorTotalPedidoSemDesconto = pedido.getValorTotalSemDesconto();

				limpar();
				lancamentoAcao = new LancamentoAcao();
				formasDePagamento = cotacaoService.getLancamentos(compra);
				carregarLancamentos();

				PrimeFaces current = PrimeFaces.current();
				current.executeScript("PF('determinar_recursoMODE01').show();");
			}
		}

	}

	public void gerarPedido() throws Exception {

		if (idFornecedor == null) {
			addMessage("", "Escolha um fornecedor", FacesMessage.SEVERITY_ERROR);
			return;
		}

		if (listaItensDeCompraAuxiliar.size() > 0) {
			if (verificarPrivilegio()) {

				pedido = cotacaoService.gerarPedido(idFornecedor, listaItensDeCompraAuxiliar, compra.getId(), pedido,
						usuarioSessao.getUsuario());

				listaRubricasCompra();
				popularValores();
				carregarLancamentos();
				carregarCotacoes();
				limpar();

				abrirDialog("PF('determinar_recursoMODE01').show();");

			} else {
				addMessage("",
						"Você não tem privilégios para esse tipo de ação, por favor contate o coordenador de compras!",
						FacesMessage.SEVERITY_WARN);
			}

		} else {
			addMessage("", "Marque algum item.", FacesMessage.SEVERITY_WARN);
		}

	}

	public void abrirDialog(String dialog) {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript(dialog);

	}

	@Transactional
	public void openDialog() {

		try {

			if (listaItensDeCompraAuxiliar.size() > 0) {

				if (verificarPrivilegio()) {

					cotacaoService.escolherCotacaoGlobal(idFornecedor, listaItensDeCompraAuxiliar, compra.getId());
					escolherCotacaoGlobal();
					PrimeFaces current = PrimeFaces.current();
					current.executeScript("PF('determinar_recursoMODE01').show();");

				} else {
					addMessage("",
							"Você não tem privilégios para esse tipo de ação, por favor contate o coordenador de compras!",
							FacesMessage.SEVERITY_WARN);
				}

			} else {
				addMessage("", "Marque um algum item.", FacesMessage.SEVERITY_WARN);
			}

		} catch (Exception e) {
			addMessage("", e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}

	@Transactional
	public void escolherCotacaoGlobal() {

		try {

			pedido = new Pedido();
			controleExpedicao = new ControleExpedicao();
			pedido.setDataEntrega(new Date());
			pedido.setDataPagamento(new Date());
			pedido.setQuantidadeParcela(1);
			pedido.setObservacao("");
			controleExpedicao.setNomeResponsavel("");

			inicializaPedidoAntesDaAprovacao(idFornecedor, pedido);

			pedido.setUsuarioGeradorPedido(usuarioSessao.getUsuario());
			pedido.setStatusCompra(StatusCompra.N_INCIADO);
			pedido.setVersionLancamento("MODE01");

			pedido = cotacaoService.salvarPedidoAntesDaAprovacao(pedido, controleExpedicao, usuarioSessao.getUsuario());

			if (pedido == null) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
						"Erro ao gerar o pedido, contate o administrador do sistema");
				FacesContext.getCurrentInstance().addMessage(null, message);
				return;
			}

			listaRubricasCompra();
			fornecedor = idFornecedor;
			valorTotalPedidoComDesconto = pedido.getValorTotalComDesconto();
			valorTotalAliberar = pedido.getValorTotalComDesconto();
			nomeFornecedor = cotacaoService.getFornecedorById(idFornecedor).getNomeFantasia();
			valorTotalPedidoSemDesconto = pedido.getValorTotalSemDesconto();

			limpar();
			lancamentoAcao = new LancamentoAcao();
			formasDePagamento = cotacaoService.getLancamentos(compra);
			carregarLancamentos();
			carregarCotacoes();

		} catch (Exception e) {
			addMessage("", e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}

	}

	public void popularValores() {
		fornecedor = pedido.getFornecedor().getId();
		valorTotalPedidoComDesconto = pedido.getValorTotalComDesconto();
		valorTotalAliberar = pedido.getValorTotalComDesconto();
		nomeFornecedor = pedido.getFornecedor().getNomeFantasia();
		valorTotalPedidoSemDesconto = pedido.getValorTotalSemDesconto();
	}

	@Inject
	private PedidoService pedidoService;

	public List<Pedido> getPedidosByCompra(Compra compra) {
		List<Pedido> lista = pedidoService.getPedidos(compra);
		if (lista.isEmpty()) {
			return new ArrayList<Pedido>();
		} else {
			return lista;
		}

	}

	public void deletarItemEspelhoPedido(Long id) {
		try {

			if (!cotacaoService.verificarExisteItemPedidoByCotacao(id)) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
						"Exclusão suspensa, existem itens pedidos com essa escolha de cotação, para executar o procedimento é necessário exclusão do Pedido junto ao coordenador de compras.");
				FacesContext.getCurrentInstance().addMessage(null, message);
				return;
			}

			cotacaoService.deletarItemPedidoCompra(id);
			carregarMapa();
		} catch (Exception e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}

	public void deletarItemPedido(ItemPedido itemPedido) {
		try {

			if (!itemPedido.getPedido().getStatusCompra().equals(StatusCompra.N_INCIADO)) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
						"Exclusão suspensa, existem itens pedidos com essa escolha de cotação, para executar o procedimento é necessário exclusão do Pedido junto ao coordenador de compras.");
				FacesContext.getCurrentInstance().addMessage(null, message);
				return;
			}

			cotacaoService.deletarItemPedidoCompra(itemPedido);
			// pedidoService.excluirItemPedido(itemPedido);
			carregarMapa();
		} catch (Exception e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}

	public String redirecionarParaPedidos() {
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("compra" + usuarioSessao.getNomeUsuario(),
				compra);
		// redireciona = true;
		return "pedidos?faces-redirect=true";
	}

	private @Inject SolicitacaoPagamentoService pagamentoService;

	public void salvarLancamentoAcaoMODE01() {
		if (lancamentoAcao.getProjetoRubrica() == null) {
			addMessage("", "Informe uma linha orçamentária.", FacesMessage.SEVERITY_WARN);
			return;
		}

		if (!calculatorRubricaRepositorio.verificarSaldoRubrica(lancamentoAcao.getProjetoRubrica().getId(),
				lancamentoAcao.getValor())) {
			addMessage("", "Rubrica sem saldo suficiente", FacesMessage.SEVERITY_WARN);
			return;
		}

		if (lancamentoAcao.getAtividade() != null && lancamentoAcao.getAtividade().getId() != null)
			if (!projetoService.verificaSaldoAtividade(lancamentoAcao.getAtividade().getId(),
					lancamentoAcao.getValor())) {
				addMessage("", "Atividade sem saldo suficiente", FacesMessage.SEVERITY_WARN);
				return;
			}

		if (lancamentoAcao != null) {
			lancamentoAux.setProjetoRubrica(lancamentoAcao.getProjetoRubrica());
			lancamentoAux.setValor(lancamentoAcao.getValor());
			if (lancamentoAcao.getAtividade() != null)
				lancamentoAux.setAtividade(lancamentoAcao.getAtividade());
			if (lancamentoAcao.getCategoriaProjeto() != null)
				lancamentoAux.setCategoriaProjeto(lancamentoAcao.getCategoriaProjeto());
		}

		for (LancamentoAcao la : lancamentos) {
			if (la.getProjetoRubrica().equals(lancamentoAcao.getProjetoRubrica())) {
				addMessage("", "Não é Permitido Adição de Duas Linhas Orçamentárias Iguais.",
						FacesMessage.SEVERITY_WARN);
				return;
			}
		}

		if (true) {
			lancamentoAux.setFornecedor(cotacaoService.getFornecedorById(fornecedor));
			lancamentoAux.setCompra(compra);
			lancamentoAux.setDespesaReceita(DespesaReceita.DESPESA);

			cotacaoService.salvarLancamento(lancamentoAux);

			lancamentoAux = new LancamentoAcao();
			carregarLancamentos();
		}

		try {
			pedido = cotacaoService.salvarPedido(pedido, controleExpedicao, usuarioSessao.getUsuario());
		} catch (Exception e) {
			addMessage("", e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}

	}

	public void salvarLancamentoAcao() {

		if (!calculatorRubricaRepositorio.verificarSaldoRubrica(lancamentoAcao.getProjetoRubrica().getId(),
				lancamentoAcao.getValor())) {
			addMessage("", "Saldo de rubrica insuficiente", FacesMessage.SEVERITY_WARN);
			return;
		}

		if (lancamentoAcao.getAtividade() != null && lancamentoAcao.getAtividade().getId() != null)
			if (projetoService.verificaSaldoAtividade(lancamentoAcao.getAtividade().getId(),
					lancamentoAcao.getValor())) {
				addMessage("", "Saldo da atividade insuficiente", FacesMessage.SEVERITY_WARN);
				return;
			}

		for (LancamentoAcao la : lancamentos) {
			if (la.getProjetoRubrica().equals(lancamentoAcao.getProjetoRubrica())) {
				addMessage("", "Não é Permitido Adição de Duas Linhas Orçamentárias Iguais.",
						FacesMessage.SEVERITY_WARN);
				return;
			}
		}

		if (true) {
			lancamentoAcao.setFornecedor(cotacaoService.getFornecedorById(fornecedor));
			lancamentoAcao.setCompra(compra);
			lancamentoAcao.setDespesaReceita(DespesaReceita.DESPESA);
			// LancamentoAcao lAux = new LancamentoAcao();

			// lAux = verificaSeExisteNaListaDeLancamentos2();
			// if (lAux != null) {
			// lAux.setValor(lancamentoAcao.getValor());
			// cotacaoService.salvarLancamento(lAux);
			// } else {
			// cotacaoService.salvarLancamento(lancamentoAcao);
			// }

			cotacaoService.salvarLancamento(lancamentoAcao);

			lancamentoAcao = new LancamentoAcao();
			carregarLancamentos();
		}
		// } else {
		//
		// addMessage("", "Saldo insuficiente para operação - Saldo: " +
		// getNumeroFormatado(saldoAux),
		// FacesMessage.SEVERITY_ERROR);
		// return;
		// }

	}

	private List<User> allUsuario = new ArrayList<>();

	public List<User> completeUsuario(String query) {
		allUsuario = new ArrayList<User>();
		allUsuario = cotacaoService.getUsuario(query);
		return allUsuario;
	}

	public String getNumeroFormatado(BigDecimal total) {
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(total);
		// return new DecimalFormat("###,###.###").format(total);
	}

	public void limpar() {
		lancamentos = new ArrayList<>();
		lancamentoAcao = new LancamentoAcao();
		// this.valorTotalFaltaLiberar = BigDecimal.ZERO;
		// this.valorTotalFaltaLiberar = BigDecimal.ZERO;
	}

	private @Inject Pedido pedido;

	public boolean verificarPrivilegio() {
		Aprouve ap = new Aprouve();
		ap.setSigla("APC");
		ap.setUsuario(usuarioSessao.getUsuario());
		return cotacaoService.findAprouvePedido(ap);
	}

	public boolean verificarPrivilegioExclusao() {
		Aprouve ap = new Aprouve();
		ap.setSigla("EXC");
		ap.setUsuario(usuarioSessao.getUsuario());
		return cotacaoService.findAprouvePedido(ap);
	}

	// @Deprecated
	// public void aprovarEspelhoComoPedido(Long idFornecedor, BigDecimal
	// valorComDesconto, BigDecimal valorSemDesconto) {
	//
	// if (verificarPrivilegio()) {
	// try {
	//
	// valorTotalPedidoComDesconto = valorComDesconto;
	// valorTotalPedidoSemDesconto = valorSemDesconto;
	// fornecedor = idFornecedor;
	// if
	// (!calculatorRubricaRepositorio.verificarSaldoRubrica(lancamentoAcao.getProjetoRubrica().getId(),
	// lancamentoAcao.getValor())) {
	// addMessage("", "Rubrica sem saldo suficiente", FacesMessage.SEVERITY_WARN);
	// return;
	// }
	//
	//
	// if (lancamentoAcao.getAtividade() != null &&
	// lancamentoAcao.getAtividade().getId() != null)
	// if(!projetoService.verificaSaldoAtividade(lancamentoAcao.getAtividade().getId(),
	// lancamentoAcao.getValor())) {
	// addMessage("", "Atividade sem saldo suficiente", FacesMessage.SEVERITY_WARN);
	// return;
	// }
	//
	// pedido = cotacaoService.getPedidoPorCompraEFornecedor(idFornecedor, compra);
	// inicializaPedido(idFornecedor, pedido);
	// cotacaoService.salvarPedido(pedido, usuarioSessao.getUsuario());
	// carregarMapa();
	//
	// if (email.verificaInternet()) {
	// enviarEmailAprovacaoPedido2();
	// limparEmail();
	// } else {
	// FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
	// "Atenção",
	// "Problema de conexão com a internet. O pedido foi gerado no entanto o e-mail
	// de aviso não foi enviado, tente novamente mais tarde!");
	// RequestContext.getCurrentInstance().showMessageInDialog(message);
	// }
	//
	// } catch (Exception e) {
	// if (e.getMessage().equals("www.gmail.com")) {
	// FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
	// "Atenção",
	// "Problema de conexão com a internet. O pedido foi gerado no entanto o e-mail
	// de aviso não foi enviado, tente novamente mais tarde!");
	// RequestContext.getCurrentInstance().showMessageInDialog(message);
	// } else {
	// FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
	// "Atenção", e.getMessage());
	// RequestContext.getCurrentInstance().showMessageInDialog(message);
	// }
	//
	// } finally {
	// pedido = new Pedido();
	// controleExpedicao = new ControleExpedicao();
	// }
	// } else {
	// FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
	// "Atenção",
	// "Você não tem privilégios para esse tipo de ação, por favor contate o
	// coordenador de compras!");
	// RequestContext.getCurrentInstance().showMessageInDialog(message);
	//
	// }
	// }

	public void enviarEmailAutorizacaoPedido() throws IOException {
		Email email = new Email();
		if (email.verificaInternet()) {

			enviarEmailAprovacaoPedido2();

			limparEmail();
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Problema de conexão com a internet. O pedido foi gerado no entanto o e-mail de aviso não foi enviado, tente novamente mais tarde!");
			// RequestContext.getCurrentInstance().showMessageInDialog(message);
		}

	}

	public void salvarPedido() throws Exception {

		if (!verificaCamposPedido(pedido, controleExpedicao)) {
		
				pedido = cotacaoService.salvarPedido(pedido, controleExpedicao, usuarioSessao.getUsuario());

				if (pedido == null) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
							"Erro ao gerar o pedido, contate o administrador do sistema");
					FacesContext.getCurrentInstance().addMessage(null, message);
					return;
				}
				
				carregarMapa();

				pedido = new Pedido();
				controleExpedicao = new ControleExpedicao();
			
		}
	}

	public boolean verificaCamposPedido(Pedido pedido, ControleExpedicao controleExpedicao) {

		boolean faltaPreencher = false;

		if (pedido.getDataEntrega() == null) {
			faltaPreencher = true;
			addMessage("", "Por favor, informe data de entrega.", FacesMessage.SEVERITY_ERROR);
		}

		if (pedido.getDataPagamento() == null) {
			faltaPreencher = true;
			addMessage("", "Por favor, informe a data de pagamento.", FacesMessage.SEVERITY_ERROR);
		}

		if (pedido.getQuantidadeParcela() == null) {
			pedido.setQuantidadeParcela(1);
		}

		if (controleExpedicao.getNomeResponsavel() == null || controleExpedicao.getNomeResponsavel().equals("")) {
			faltaPreencher = true;
			addMessage("", "Por favor, informe o responsável pela expedição.", FacesMessage.SEVERITY_ERROR);
		}

		if (pedido.getObservacao() == null || pedido.getObservacao().equals("")) {
			faltaPreencher = true;
			addMessage("", "Por favor, informe o responsável pela expedição.", FacesMessage.SEVERITY_ERROR);
		}

		return faltaPreencher;
	}

	public void prepararAprovacaoPedido(Pedido espelho) {
		pedido = pedidoService.getPedidoById(espelho.getId());
		fornecedor = espelho.getFornecedor().getId();
		contasFornecedor(espelho.getFornecedor());
		valorTotalPedidoComDesconto = espelho.getValorTotalComDesconto();
		valorTotalPedidoSemDesconto = espelho.getValorTotalSemDesconto();
		listGestao = cotacaoService.buscarGestao();

		controleExpedicao = cotacaoService.findControleExpedByPedido(espelho.getId());
		if (controleExpedicao.getId() == null) {
			controleExpedicao = new ControleExpedicao();
			controleExpedicao.setNomeResponsavel("");
			controleExpedicao.setDataAceitacaoExpedicao(new Date());
		}

	}

	private List<ContaBancaria> contas = new ArrayList<ContaBancaria>();

	public List<ContaBancaria> getContas() {
		return contas;
	}

	public void setContas(List<ContaBancaria> contas) {
		this.contas = contas;
	}

	public List<ContaBancaria> contasFornecedor(Fornecedor fornecedor) {
		contas = new ArrayList<>();
		contas = pagamentoService.getContasFornecedor(fornecedor);
		return contas;
	}

	private boolean existePedido = false;

	public boolean existePedidoParaFornecedor(Long id) {

		return cotacaoService.verificarExistePedido(id, compra);
	}

	public void acsExistePedido(Long id) {
		existePedido = cotacaoService.verificarExistePedido(id, compra);
	}

	public Pedido inicializaPedido(Long fornecedor, Pedido pedido) throws NegocioException {

		pedido.setFornecedor(cotacaoService.getFornecedorById(fornecedor));
		pedido.setCompra(compra);
		pedido.setSolicitante(compra.getSolicitante());
		pedido.setItensPedidos(getItensPedidos(fornecedor, pedido));
		pedido.setTipoGestao(compra.getTipoGestao());
		pedido.setGestao(compra.getGestao());
		pedido.setTipoLocalidade(compra.getTipoLocalidade());
		pedido.setLocalidade(compra.getLocalidade());
		pedido.setDataEmissao(new Date());
		pedido.setVersionLancamento("MODE01");

		// pedido.setDataEntrega(new Date());
		// pedido.setDataPagamento(new Date());
		pedido.setTipoLancamento("ct");
		pedido.setDepesaReceita(DespesaReceita.DESPESA);

		ContaBancaria conta = cotacaoService.getContaByFornecedor(fornecedor);
		if (conta == null) {
			throw new NegocioException(
					"Não existe uma conta para o fornecedor, por favor entre em contato com o adm para que seja efetuado o cadastro!");
		}

		pedido.setContaRecebedor(conta);

		return pedido;
	}

	public Pedido inicializaPedidoAntesDaAprovacao(Long fornecedor, Pedido pedido) throws NegocioException {

		pedido.setFornecedor(cotacaoService.getFornecedorById(fornecedor));
		pedido.setCompra(compra);
		pedido.setSolicitante(compra.getSolicitante());
		pedido.setItensPedidos(getItensPedidosAntesDaAprovacao(fornecedor, pedido));
		pedido.setTipoGestao(compra.getTipoGestao());
		pedido.setGestao(compra.getGestao());
		pedido.setTipoLocalidade(compra.getTipoLocalidade());
		pedido.setLocalidade(compra.getLocalidade());
		pedido.setDataEmissao(new Date());
		pedido.setVersionLancamento("MODE01");

		// pedido.setDataEntrega(new Date());
		// pedido.setDataPagamento(new Date());
		pedido.setTipoLancamento("ct");
		pedido.setDepesaReceita(DespesaReceita.DESPESA);

		ContaBancaria conta = cotacaoService.getContaByFornecedor(fornecedor);
		if (conta == null) {
			throw new NegocioException(
					"Não existe uma conta para o fornecedor, por favor entre em contato com o adm para que seja efetuado o cadastro!");
		}

		pedido.setContaRecebedor(conta);

		return pedido;
	}

	protected List<ItemPedido> getItensPedidosAntesDaAprovacao(Long fornecedor, Pedido pedido) throws NegocioException {
		List<Cotacao> listAux = cotacaoService.getCotacoesHouveEscolha(fornecedor, compra);
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

	protected List<ItemPedido> getItensPedidos(Long fornecedor, Pedido pedido) throws NegocioException {
		List<ItemPedido> itens = pedidoService.getItensPedido(pedido);

		for (ItemPedido p : itens) {

			if (p.getCotacao().getHouvePedido() == null) {
				throw new NegocioException(
						"Itens na lista não foram enviados para aprovação. Por favor envie e tente novamente!");
			}

			if (!p.getCotacao().getHouvePedido()) {
				throw new NegocioException(
						"Itens na lista não foram enviados para aprovação. Por favor envie e tente novamente!");
			}

		}

		return itens;
	}

	public LancamentoAcao verificaSeExisteNaListaDeLancamentos2() {
		LancamentoAcao lAux = cotacaoService.getLancamentoAcaoPorAcaoEfonte(lancamentoAcao);

		if (lAux != null) {
			return lAux;
		}

		return null;
	}

	public void deletarLancamentoFormaDePagamento(Long id) {
		cotacaoService.deletarLancamentoFormaDePagamento(id);
		carregarLancamentos();
		listaRubricasCompra();
	}

	public void deletarLancamentoFormaDePagamento(LancamentoAcao lancamentoAcao) {
		cotacaoService.deletarLancamentoFormaDePagamento(lancamentoAcao);
		carregarLancamentos();
		listaRubricasCompra();
	}

	public void calcularValoresRecurso() {

		valorTotalLiberado = BigDecimal.ZERO;
		valorTotalFaltaLiberar = BigDecimal.ZERO;

		for (ProjetoRubrica rubrica : listaDeRubricasProjeto) {
			if (rubrica.getValorEmpenhado() != null)
				valorTotalLiberado = valorTotalLiberado.add(rubrica.getValorEmpenhado());
		}

		valorTotalFaltaLiberar = pedido.getValorTotalComDesconto().subtract(valorTotalLiberado);
	}

	public void carregarLancamentos() {
		lancamentos = new ArrayList<>();
		lancamentos = cotacaoService.getListaDeLancamentoPorFornecedorECompra(fornecedor, compra);
		valorTotalLiberado = BigDecimal.ZERO;
		if (lancamentos.size() > 0) {
			for (LancamentoAcao lanc : lancamentos) {
				valorTotalLiberado = valorTotalLiberado.add(lanc.getValor());
				valorTotalFaltaLiberar = valorTotalAliberar.subtract(valorTotalLiberado);
			}
		} else {
			this.valorTotalFaltaLiberar = valorTotalAliberar.subtract(valorTotalLiberado);
		}
	}

	public void carregarLancamentosEmail() {
		lancamentos = new ArrayList<>();
		lancamentos = cotacaoService.getListaDeLancamentoPorFornecedorECompra(fornecedor, compra);
		valorTotalLiberado = BigDecimal.ZERO;
		if (lancamentos.size() > 0) {
			for (LancamentoAcao lanc : lancamentos) {
				valorTotalLiberado = valorTotalLiberado.add(lanc.getValor());
				valorTotalFaltaLiberar = valorTotalAliberar.subtract(valorTotalLiberado);
			}
		} else {
			this.valorTotalFaltaLiberar = valorTotalAliberar.subtract(valorTotalLiberado);
		}
	}

	public void verificaSeExisteNaListaDeLancamentos() {

		if (lancamentos.stream()
				.filter(lc -> lc.getAcao().getCodigo().equals(lancamentoAcao.getAcao().getCodigo())
						&& lc.getFontePagadora().getNome().equals(lancamentoAcao.getFontePagadora().getNome()))
				.findAny().isPresent()) {

			LancamentoAcao lAux = lancamentos.stream()
					.filter(lc -> lc.getAcao().getCodigo().equals(lancamentoAcao.getAcao().getCodigo())
							&& lc.getFontePagadora().getNome().equals(lancamentoAcao.getFontePagadora().getNome()))
					.findAny().get();

			if (lAux != null) {
				lancamentos.remove(lAux);
				valorTotalLiberado = valorTotalLiberado.subtract(lAux.getValor());
				valorTotalFaltaLiberar = valorTotalAliberar.subtract(valorTotalLiberado);

			}
		}

	}

	public boolean verificaUsuario() {
		// Verifica o usuario de acordo com a restrição de dados
		return true;
	}

	public void removerLancamentoAcao() {

		lancamentos.remove(lancamentos.stream()
				.filter(ac -> ac.getAcao().getCodigo().equals(lancamentoAcao.getAcao().getCodigo())).findAny().get());
		lancamentoAcao = new LancamentoAcao();
	}

	public void setarFornecedor() {

	}

	private List<Projeto> listaProjeto = new ArrayList<>();

	private Filtro filtro = new Filtro();
	private List<Projeto> projetos = new ArrayList<>();

	private Projeto projetoAux;

	private List<ProjetoRubrica> listaDeRubricasProjeto = new ArrayList<>();

	public void listaRubricas() {

		if (projetoAux == null) {
			lancamentoAcao = new LancamentoAcao();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você deve selecionar um projeto antes de buscar rubrica! ");
			FacesContext.getCurrentInstance().addMessage(null, message);
			listaDeRubricasProjeto = new ArrayList<ProjetoRubrica>();
		} else {
			listaDeRubricasProjeto = calculatorRubricaRepositorio.getProjetoRubricaByProjeto(projetoAux.getId());
		}
	}

	public void listaRubricasCompra() {

		if (compra.getId() != null)
			listaDeRubricasProjeto = calculatorRubricaRepositorio.getProjetoRubricaByLancamento(compra.getId());
		else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Algo Deu Errado!",
					"Sua Compra está nula, atualize a tela e tente Novamente!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}

	public void listaAtividadesProjetoCompra() {

		if (compra.getId() != null)
			listaAtividadeProjeto = projetoService.getAtividadeProjetoByLancamento(compra.getId());
		else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Algo Deu Errado!",
					"Sua Compra está Nula, Atualize a Tela e Tente Novamente!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}

	public void carregarProjetos() {
		projetos = cotacaoService.getProjetosFiltroPorUsuarioMODE01(filtro, usuarioSessao.getUsuario());
		listaProjeto = cotacaoService.getProjetosByUsuario(usuarioSessao.getUsuario());

		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro")) {
			listaProjeto = cotacaoService.getProjetosbyUsuarioProjeto(null, filtro);
			return;
		}

		listaProjeto = cotacaoService.getProjetosbyUsuarioProjeto(usuarioSessao.getUsuario(), filtro);
	}

	public BigDecimal decimalFormat() {

		DecimalFormat df = new DecimalFormat("0.##");
		String d = df.format(valorTotalAliberar.doubleValue());

		return BigDecimal.ZERO;
	}

	public void enviarParaAprovacao() {

		if (valorTotalFaltaLiberar.compareTo(BigDecimal.ZERO) > 0
				|| valorTotalFaltaLiberar.compareTo(BigDecimal.ZERO) < 0) {
			addMessage("",
					getTotais(valorTotalFaltaLiberar)
							+ " falta ser liberado! Selecione a açao e o valor para liberar o pedido",
					FacesMessage.SEVERITY_WARN);
			return;
		}

		if (valorTotalFaltaLiberar.compareTo(BigDecimal.ZERO) < 0) {
			addMessage("", " Os valores listados ultrapassam o valor a ser liberado", FacesMessage.SEVERITY_WARN);
			return;
		}

		// cotacaoService.escolherPedido(lancamentos);
		// carregarMapa();

		try {
			if (email.verificaInternet()) {
				cotacaoService.escolherPedido(lancamentos);
				carregarMapa();
				enviarEmailAprovacaoPedido();
			} else {
				addMessage("",
						"Problema de conexão com a internet, por favor contate o administrador do sistema, tente mais tarde, sua solicitação de aprovação não foi enviada.",
						FacesMessage.SEVERITY_INFO);
			}
		} catch (Exception e) {
			addMessage("",
					"Problema de conexão com a internet, por favor contate o administrador do sistema, tente mais tarde, sua solicitação de aprovação não foi enviada.",
					FacesMessage.SEVERITY_INFO);
		}

		// cotacaoService.escolherPedido(fornecedor, compra);
	}

	public CondicaoPagamento[] getCondicoes() {
		return CondicaoPagamento.values();
	}

	public TipoParcelamento[] getTiposParcelas() {
		return TipoParcelamento.values();
	}

	public void limparEmail() {
		email = new Email();
	}

	public List<EspelhoPedido> getEspelhos() {
		return espelhos;
	}

	public String getColorRank() {
		if (tableRank.getRowIndex() == 0) {
			return "ranking_1";
		}
		return "";
	}

	public String getColorRankColumn() {
		if (tableRank.getRowIndex() == 0) {
			return "ranking_1_column";
		}
		return "";
	}

	public void setEspelhos(List<EspelhoPedido> espelhos) {
		this.espelhos = espelhos;
	}

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public List<MenuLateral> getMenus() {

		List<MenuLateral> lista = new ArrayList<>();
		MenuLateral menu = new MenuLateral("Compras", "compras", "fa fa-shopping-cart");
		lista.add(menu);
		menu = new MenuLateral("Produtos", "produtos", "fa fa-database");
		lista.add(menu);
		menu = new MenuLateral("Fornecedores", "fornecedores", "fa fa-user");
		lista.add(menu);

		return MakeMenu.getMenuCompra();
	}

	public List<Cotacao> getCotacoesParaEspelhoPedido(Long fornecedor) {
		return cotacaoService.getCotacoesHouveEscolha(fornecedor, compra);
	}

	public List<ItemPedido> getItensPedidoByPedido(Pedido pedido) {
		return pedidoService.getItensPedido(pedido);
	}

	public void deletarTodosEscolhidos(Long fornecedor) throws IOException {
		if (!cotacaoService.verificarExisteItensPedidoByCotacoes(fornecedor, compra.getId())) {
			cotacaoService.deletarEspelho(fornecedor, compra.getId());
			carregarCotacoes();
			addMessage("", "Excluído", FacesMessage.SEVERITY_INFO);
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Exclusão suspensa, existem itens pedidos com essa cotação, para executar o procedimento é necessário exclusão do Pedido de compras junto ao coordenador de compras.");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		}

	}

	public void deletarPedido(Pedido pedido) throws IOException {

		try {
			if (pedido.getStatusCompra().equals(StatusCompra.N_INCIADO)) {

				cotacaoService.remover(pedido);
				carregarCotacoes();
				addMessage("", "Excluído", FacesMessage.SEVERITY_INFO);
			} else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
						"Exclusão suspensa, existem itens pedidos com essa cotação, para executar o procedimento é necessário exclusão do Pedido de compras junto ao coordenador de compras.");
				FacesContext.getCurrentInstance().addMessage(null, message);
				return;
			}
		} catch (Exception e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao tentar excluir!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}

	public void carregarMapa() {
		cotacoesAuxiliares = cotacaoService.getCotacaoAuxiliar(compra.getId());
		itens = cotacaoService.getItensByCompra(compra);
		pedidos = getPedidosByCompra(compra);
	}

	public void carregarCotacoesItem(ItemCompra itemCompra) {
		item = itemCompra.getProduto().getDescricao();
		cotacoes = cotacaoService.getCotacoes(itemCompra);
		obs = new Observacao();
		obs = cotacaoService.findObservacaoByCompraAndItem(compra.getId(), itemCompra.getId());
		obs.setItemCompra(itemCompra);
		obs.setLancamento(compra);
		obs.setUsuario(usuarioSessao.getUsuario());

	}

	public void testarMetodo() {

	}

	public String getTotais(BigDecimal total) {
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(total);
		// return new DecimalFormat("###,###.###").format(total);
	}

	public BigDecimal getValorCotacao(Long idForn, Long compra, Long itemCompra) {
		BigDecimal valorDesconto = BigDecimal.ZERO;
		BigDecimal valor = BigDecimal.ZERO;
		if (!cotacaoService.getValorCotacao(idForn, compra, itemCompra).isEmpty()) {
			valorDesconto = cotacaoService.getValorCotacao(idForn, compra, itemCompra).get(0).getValorDesconto();
		}

		return valorDesconto;
	}

	public Boolean getHouveEscolhaCotacao(Long idForn, Long compra, Long itemCompra) {

		if (!cotacaoService.getValorCotacao(idForn, compra, itemCompra).isEmpty()) {
			return cotacaoService.getCotacaoEscolha(idForn, compra, itemCompra).get(0).getHouveEscolha() != null
					? cotacaoService.getCotacaoEscolha(idForn, compra, itemCompra).get(0).getHouveEscolha()
					: false;
		}

		return false;
	}

	public void enviarEmailSolicitacaoAutorizacaoPedido() throws AddressException, IOException, MessagingException {
		escolhaService.enviarEmailAutorizacaoPedido(pedido);
	}

	public void prepararEmailParaEnvioDeAprovacaoDeEspelhoPedidos() {

		// User usuarioSolicitante =
		// cotacaoService.getUsuarioByColaborador(compra.getGestao().getColaborador().getId());
		//
		// String emailSolicitante = usuarioSolicitante.getEmail();
		// String senhaEmailSolicitante = usuarioSolicitante.getSenhaem();

		email.setFromEmail("comprasgi@fas-amazonas.org");
		email.setSenhaEmail("FAS123fas");

		email.setSubject("SOLICITACAO DE APROVACAO DE PEDIDO - " + compra.getId() + " Fornecedor: " + nomeFornecedor
				+ " Data:" + new SimpleDateFormat("dd/MM/yyyy").format(compra.getDataEmissao()));
		email.setSolicitação(String.valueOf(compra.getId()));
		StringBuilder toEmailAux = new StringBuilder(
				"luiz.villares@fas-amazonas.org,admsgi@fas-amazonas.org,compra@fas-amazonas.org,compras@fas-amazonas.org");

		// StringBuilder toEmailAux = new
		// StringBuilder("italo.almeida@fas-amazonas.org");
		toEmailAux.append(",");
		toEmailAux.append(compra.getSolicitante().getEmail());
		toEmailAux.append(",");
		toEmailAux.append(compra.getGestao().getColaborador().getEmail());

		if (compra.getGestao().getId().longValue() == 10) {
			toEmailAux.append(",");
			toEmailAux.append("raquel.caldas@fas-amazonas.org");
		}

		if (compra.getTipoGestao().getNome().equals("Regional")) {
			toEmailAux.append(",");
			toEmailAux.append("valcleia.solidade@fas-amazonas.org");
		}

		email.setToEmail(toEmailAux.toString()); // Solicitante,
													// Superintendente,
													// coordenador // compras
		email.setContent(gerarConteudoHTML("Solicitação de aprovação de pedido.",
				"Solicito a aprovação para o pedido de acordo com o mapa comparativo."));

	}

	public void prepararEmailParaEnvioDeAprovacaoDeEspelhoPedidosTESTE() {

		// User usuarioSolicitante =
		// cotacaoService.getUsuarioByColaborador(compra.getGestao().getColaborador().getId());
		//
		// String emailSolicitante = usuarioSolicitante.getEmail();
		// String senhaEmailSolicitante = usuarioSolicitante.getSenhaem();

		email.setFromEmail("comprasgi@fas-amazonas.org");
		email.setSenhaEmail("FAS123fas");

		email.setSubject("SOLICITACAO DE APROVACAO DE PEDIDO - " + compra.getId() + " Fornecedor: " + nomeFornecedor
				+ " Data:" + new SimpleDateFormat("dd/MM/yyyy").format(compra.getDataEmissao()));
		email.setSolicitação(String.valueOf(compra.getId()));

		StringBuilder toEmailAux = new StringBuilder("elizabeth.rabelo@fas-amazonas.org");

		// StringBuilder toEmailAux = new StringBuilder(
		// "luiz.villares@fas-amazonas.org,admsgi@fas-amazonas.org,compra@fas-amazonas.org");
		// toEmailAux.append(",");
		// toEmailAux.append(compra.getSolicitante().getEmail());
		// toEmailAux.append(",");
		// toEmailAux.append(compra.getGestao().getColaborador().getEmail());
		//
		// if (compra.getGestao().getId().longValue() == 10) {
		// toEmailAux.append(",");
		// toEmailAux.append("raquel.caldas@fas-amazonas.org");
		// }
		//
		// if (compra.getTipoGestao().getNome().equals("Regional")) {
		// toEmailAux.append(",");
		// toEmailAux.append("valcleia.solidade@fas-amazonas.org");
		// }

		email.setToEmail(toEmailAux.toString()); // Solicitante,
													// Superintendente,
													// coordenador // compras
		email.setContent(gerarConteudoHTML("Solicitação de aprovação de pedido.",
				"Solicito a aprovação para o pedido de acordo com o mapa comparativo."));

	}

	// unificar os 2 metodos 1 e 2 assim que possível
	public void prepararEmailParaEnvioDeAprovacaoDeEspelhoPedidos2() {
		email.setFromEmail("comprasgi@fas-amazonas.org");
		email.setSenhaEmail("FAS123fas");

		email.setSubject("PEDIDO APROVADO -" + compra.getId() + " Data:"
				+ new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		email.setSolicitação(String.valueOf(compra.getId()));

		StringBuilder toEmailAux = new StringBuilder(
				"compra@fas-amazonas.org,admsgi@fas-amazonas.org,compras@fas-amazonas.org");
		toEmailAux.append(",");
		toEmailAux.append(compra.getSolicitante().getEmail());
		toEmailAux.append(",");
		toEmailAux.append(compra.getGestao().getColaborador().getEmail());

		// StringBuilder toEmailAux = new
		// StringBuilder("italo.almeida@fas-amazonas.org");
		//
		email.setToEmail(toEmailAux.toString()); // E-mail
		email.setContent(gerarConteudoHTML("PEDIDO APROVADO.", "Muito bem, seu pedido foi aprovado!"));

	}

	public void prepararEmailParaEnvioDeAprovacaoDeEspelhoPedidos2TESTE() {
		email.setFromEmail("comprasgi@fas-amazonas.org");
		email.setSenhaEmail("FAS123fas");

		email.setSubject("PEDIDO DE COMPRA AUTORIZADO -" + compra.getId() + " Data:"
				+ new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		email.setSolicitação(String.valueOf(compra.getId()));

		StringBuilder toEmailAux = new StringBuilder("elizabeth.rabelo@fas-amazonas.org");

		// StringBuilder toEmailAux = new
		// StringBuilder("compra@fas-amazonas.org,admsgi@fas-amazonas.org");
		// toEmailAux.append(",");
		// toEmailAux.append(compra.getSolicitante().getEmail());
		// toEmailAux.append(",");
		// toEmailAux.append(compra.getGestao().getColaborador().getEmail());
		email.setToEmail(toEmailAux.toString()); // E-mail
		email.setContent(gerarConteudoHTML("PEDIDO APROVADO.", "Muito bem, seu pedido foi aprovado!"));

	}

	public String gerarConteudoHTMLSC(String titulo, String texto) {
		StringBuilder html = new StringBuilder("<html> <head> <style>");
		html.append("table {width:100%;} table, th, td { border: 1px solid black; border-collapse: collapse;}");
		html.append("th, td { padding: 5px; text-align: left;} table#t01 tr:nth-child(even) {");
		html.append("background-color: #eee;} table#t01 tr:nth-child(odd) { background-color:#fff;}");
		html.append("table#t01 th { background-color: #849A39; color: white; }");
		html.append("</style>");
		html.append("</head>");

		html.append("<body>");

		html.append("<h3 align=\"center\" >" + titulo + "</h3>");

		html.append("<p>");
		html.append(texto);
		html.append("</p>");

		html.append("<b>Solicitação:</b>" + compra.getId() + "<br>"); // busca
																		// numero
																		// da
																		// solicitação

		html.append("<b>Solicitante:</b>" + compra.getSolicitante().getNome() + "<br>");

		html.append("<b>Tipo gestão:</b>" + compra.getTipoGestao().getNome() + "<br>");

		html.append("<b>Gestão:</b> " + compra.getGestao().getNome() + "<br><br>");

		// preparar o for para iterar sobre a quantidade de ações

		carregarLancamentos();

		for (LancamentoAcao l : lancamentos) {
			// html.append("<b>" + l.getFontePagadora().getNome() + "</b>" +
			// l.getAcao().getCodigo() + "<br>");

			html.append(
					"<b> Recurso: </b> " + l.getFontePagadora().getNome() + " - " + l.getAcao().getCodigo() + "<br>");
		}

		html.append("</body> </html>");
		return html.toString();
	}

	public String gerarConteudoHTML(String titulo, String texto) {
		StringBuilder html = new StringBuilder("<html> <head> <style>");
		html.append("table {width:100%;} table, th, td { border: 1px solid black; border-collapse: collapse;}");
		html.append("th, td { padding: 5px; text-align: left;} table#t01 tr:nth-child(even) {");
		html.append("background-color: #eee;} table#t01 tr:nth-child(odd) { background-color:#fff;}");
		html.append("table#t01 th { background-color: #849A39; color: white; }");
		html.append("</style>");
		html.append("</head>");

		html.append("<body>");

		html.append("<h3 align=\"center\" >" + titulo + "</h3>");

		html.append("<p>");
		html.append(texto);
		html.append("</p>");

		html.append("<b>Solicitação:</b>" + compra.getId() + "<br>"); // busca
																		// numero
																		// da
																		// solicitação

		html.append("<b>Solicitante:</b>" + compra.getSolicitante().getNome() + "<br>");

		html.append("<b>Tipo gestão:</b>" + compra.getTipoGestao().getNome() + "<br>");

		html.append("<b>Gestão:</b> " + compra.getGestao().getNome() + "<br><br>");

		// preparar o for para iterar sobre a quantidade de ações

		carregarLancamentos();

		// for (LancamentoAcao l : lancamentos) {
		// // html.append("<b>" + l.getFontePagadora().getNome() + "</b>" +
		// // l.getAcao().getCodigo() + "<br>");
		//
		// html.append(
		// "<b> Recurso: </b> " + l.getFontePagadora().getNome() + " - " +
		// l.getAcao().getCodigo() + "<br>");
		// }

		StringBuilder acs = new StringBuilder();

		if (compra.getVersionLancamento() != null && compra.getVersionLancamento().equals("MODE01")) {
			for (LancamentoAcao l : lancamentos) {

				acs = new StringBuilder();
				acs.append(l.getProjetoRubrica().getProjeto().getNome());
				acs.append("//");
				acs.append(l.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getTitulo());
				acs.append("//");
				acs.append(l.getProjetoRubrica().getRubricaOrcamento().getRubrica().getNome());
				html.append("<b> Recurso: </b> " + acs.toString() + "<br>");
			}
		} else {
			for (LancamentoAcao l : lancamentos) {
				html.append("<b> Recurso: </b> " + l.getFontePagadora().getNome() + " - " + l.getAcao().getCodigo()
						+ "<br>");
			}
		}

		html.append("<br> <b>Fornecedor:</b>" + nomeFornecedor + "<br> <br>");
		html.append("<table id=\"t01\" style=\"width:100%\"> ");
		html.append(
				"<tr>  <th>Produto</th>  <th>Qtd</th>  <th>Valor unitário</th> <th>Valor unitário c/ desconto</th> <th>Total</th> </tr>");
		// preparar o for para iterar sobre os itens do pedido

		List<Cotacao> itens = getCotacoesParaEspelhoPedido(fornecedor);
		DecimalFormat formatador = new DecimalFormat("#,##0.00");
		for (Cotacao cotacao : itens) {

			String valorDescontoS = formatador.format(cotacao.getValorDesconto()).toString();
			String valorTotalS = formatador
					.format(cotacao.getValorDesconto().multiply(new BigDecimal(cotacao.getQuantidade()))).toString();

			html.append("<tr>  <td>" + cotacao.getItemCompra().getProduto().getDescricao() + "</td> <td>"
					+ cotacao.getQuantidade() + "</td> <td>R$" + formatador.format(cotacao.getValor()).toString()
					+ "</td> <td>R$" + valorDescontoS + "</td> <td>R$" + valorTotalS + "</td> </tr>");
		}

		String valorTotalPedidoSemDescontoS = formatador.format(valorTotalPedidoSemDesconto).toString();
		String valorTotalDescontoS = formatador
				.format(valorTotalPedidoSemDesconto.subtract(valorTotalPedidoComDesconto)).toString();
		String valorTotalPedidoComDescontoS = formatador.format(valorTotalPedidoComDesconto).toString();

		html.append("<tr> <td colspan=\"5\"> <div align=\"center\"><b>Totalizadores</b></div> </td> </tr>");
		html.append("<tr> <td colspan=\"4\"> <label style=\"float:right\">Subtotal:</label></td> <td>R$"
				+ valorTotalPedidoSemDescontoS + "</td> </tr>");
		html.append("<tr> <td colspan=\"4\"> <label style=\"float:right\">Desconto:</label></td> <td>R$"
				+ valorTotalDescontoS + "</td> </tr> ");
		html.append("<tr> <td colspan=\"4\"> <label style=\"float:right\">Total:</label></td> <td>R$"
				+ valorTotalPedidoComDescontoS + "</td> </tr>");
		html.append("</table> </body> </html>");
		return html.toString();
	}

	public void enviarEmailAprovacaoPedido() {

		// Criar metodo Util.getmessagem
		prepararEmailParaEnvioDeAprovacaoDeEspelhoPedidos();
		// prepararEmailParaEnvioDeAprovacaoDeEspelhoPedidosTESTE();
		try {
			email.EnviarEmailAprovacaoHTML(compra);
			limparEmail();
			addMessage("", "Mensagem enviada com sucesso", FacesMessage.SEVERITY_INFO);

		} catch (Exception e) {
			addMessage("",
					"Problema de conexão com a internet, por favor contate o administrador do sistema, tente mais tarde.",
					FacesMessage.SEVERITY_INFO);
		}
	}

	// Nome do método deve ser mudado ou unificar os métodos através de
	// parametros
	public void enviarEmailAprovacaoPedido2() {
		prepararEmailParaEnvioDeAprovacaoDeEspelhoPedidos2();
		// prepararEmailParaEnvioDeAprovacaoDeEspelhoPedidos2TESTE();
		try {
			email.EnviarEmailAprovacaoHTML(compra);
			addMessage("", "Mensagem enviada com sucesso", FacesMessage.SEVERITY_INFO);
		} catch (AddressException e) {
			addMessage("",
					"Mensagem não enviada, entre em contato com o Administrador do sistema. Envie manualmente o email para certificação",
					FacesMessage.SEVERITY_INFO);
			e.printStackTrace();
		} catch (MessagingException e) {
			addMessage("",
					"Mensagem não enviada, entre em contato com o Administrador do sistema. Envie manualmente o email para certificação",
					FacesMessage.SEVERITY_INFO);
			e.printStackTrace();
		}

	}

	@Inject
	private Observacao observacaoMC;

	public void mostrarJustificativa() {
		mostraJustificativa = !mostraJustificativa;
		if (mostraJustificativa)
			observacaoMC = cotacaoService.getObservacoes(compra).size() > 0
					? cotacaoService.getObservacoes(compra).get(0)
					: new Observacao();

		if (observacaoMC.getId() == null) {
			observacaoMC.setLancamento(compra);
			observacaoMC.setUsuario(usuarioSessao.getUsuario());
			observacaoMC.setData(new Date());
		}
	}

	public void salvarObservacao() {
		if (observacaoMC.getTexto().length() > 5) {
			cotacaoService.salvarObservacao(observacaoMC);
			addMessage("", "Justificativa registrada.", FacesMessage.SEVERITY_INFO);
		} else {
			addMessage("", "Justificativa/Observação deve ter no mínimo 5 caracteres.", FacesMessage.SEVERITY_ERROR);
		}

	}

	public List<Cotacao> getCotacoes() {
		return cotacoes;
	}

	public void setCotacoes(List<Cotacao> cotacoes) {
		this.cotacoes = cotacoes;
	}

	public DataTable getTable() {
		return table;
	}

	public void setTable(DataTable table) {
		this.table = table;
	}

	public DataTable getTableRank() {
		return tableRank;
	}

	public void setTableRank(DataTable tableRank) {
		this.tableRank = tableRank;
	}

	public Cotacao getCotacao() {
		return cotacao;
	}

	public List<LancamentoAcao> getFormasDePagamento() {
		return formasDePagamento;
	}

	public void setFormasDePagamento(List<LancamentoAcao> formasDePagamento) {
		this.formasDePagamento = formasDePagamento;
	}

	public void setCotacao(Cotacao cotacao) {
		this.cotacao = cotacao;
	}

	public List<ItemCompra> getItens() {
		return itens;
	}

	public void setItens(List<ItemCompra> itens) {
		this.itens = itens;
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public LancamentoAcao getLancamentoAcao() {
		return lancamentoAcao;
	}

	public void setLancamentoAcao(LancamentoAcao lancamentoAcao) {
		this.lancamentoAcao = lancamentoAcao;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public UsuarioSessao getUsuarioSessao() {
		return usuarioSessao;
	}

	public void setUsuarioSessao(UsuarioSessao usuarioSessao) {
		this.usuarioSessao = usuarioSessao;
	}

	public Long getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Long fornecedor) {
		this.fornecedor = fornecedor;
	}

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}

	public List<CotacaoAuxiliar> getCotacoesAuxiliares() {
		return cotacoesAuxiliares;
	}

	public void setCotacoesAuxiliares(List<CotacaoAuxiliar> cotacoesAuxiliares) {
		this.cotacoesAuxiliares = cotacoesAuxiliares;
	}

	public List<Cotacao> getCotacoesPedido() {
		return cotacoesPedido;
	}

	public void setCotacoesPedido(List<Cotacao> cotacoesPedido) {
		this.cotacoesPedido = cotacoesPedido;
	}

	public List<LancamentoAcao> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<LancamentoAcao> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public BigDecimal getValorTotalAliberar() {
		return valorTotalAliberar;
	}

	public void setValorTotalAliberar(BigDecimal valorTotalAliberar) {
		this.valorTotalAliberar = valorTotalAliberar;
	}

	public BigDecimal getValorTotalLiberado() {
		return valorTotalLiberado;
	}

	public void setValorTotalLiberado(BigDecimal valorTotalLiberado) {
		this.valorTotalLiberado = valorTotalLiberado;
	}

	public BigDecimal getValorTotalFaltaLiberar() {
		return valorTotalFaltaLiberar;
	}

	public void setValorTotalFaltaLiberar(BigDecimal valorTotalFaltaLiberar) {
		this.valorTotalFaltaLiberar = valorTotalFaltaLiberar;
	}

	public String getItem() {
		return item;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Observacao getObs() {
		return obs;
	}

	public void setObs(Observacao obs) {
		this.obs = obs;
	}

	public ControleExpedicao getControleExpedicao() {
		return controleExpedicao;
	}

	public void setControleExpedicao(ControleExpedicao controleExpedicao) {
		this.controleExpedicao = controleExpedicao;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public List<Gestao> getListGestao() {
		return listGestao;
	}

	public void setListGestao(List<Gestao> listGestao) {
		this.listGestao = listGestao;
	}

	public Long getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	public List<ItemCompra> getListaItensDeCompraAuxiliar() {
		return listaItensDeCompraAuxiliar;
	}

	public void setListaItensDeCompraAuxiliar(List<ItemCompra> listaItensDeCompraAuxiliar) {
		this.listaItensDeCompraAuxiliar = listaItensDeCompraAuxiliar;
	}

	public Boolean getMostraJustificativa() {
		return mostraJustificativa;
	}

	public void setMostraJustificativa(Boolean mostraJustificativa) {
		this.mostraJustificativa = mostraJustificativa;
	}

	public Observacao getObservacaoMC() {
		return observacaoMC;
	}

	public void setObservacaoMC(Observacao observacaoMC) {
		this.observacaoMC = observacaoMC;
	}

	public boolean isExistePedido() {
		return existePedido;
	}

	public void setExistePedido(boolean existePedido) {
		this.existePedido = existePedido;
	}

	public List<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}

	public Projeto getProjetoAux() {
		return projetoAux;
	}

	public void setProjetoAux(Projeto projetoAux) {
		this.projetoAux = projetoAux;
	}

	public List<ProjetoRubrica> getListaDeRubricasProjeto() {
		return listaDeRubricasProjeto;
	}

	public void setListaDeRubricasProjeto(List<ProjetoRubrica> listaDeRubricasProjeto) {
		this.listaDeRubricasProjeto = listaDeRubricasProjeto;
	}

	public List<Projeto> getListaProjeto() {
		return listaProjeto;
	}

	public void setListaProjeto(List<Projeto> listaProjeto) {
		this.listaProjeto = listaProjeto;
	}

	public List<AtividadeProjeto> getListaAtividadeProjeto() {
		return listaAtividadeProjeto;
	}

	public void setListaAtividadeProjeto(List<AtividadeProjeto> listaAtividadeProjeto) {
		this.listaAtividadeProjeto = listaAtividadeProjeto;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public List<Pedido> getPedidosSelecionados() {
		return pedidosSelecionados;
	}

	public void setPedidosSelecionados(List<Pedido> pedidosSelecionados) {
		this.pedidosSelecionados = pedidosSelecionados;
	}

}
