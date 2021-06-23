package managedbean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.model.chart.PieChartModel;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;

import model.AlocacaoRendimento;
import model.AplicacaoRecurso;
import model.BaixaAplicacao;
import model.Colaborador;
import model.ComponenteClass;
import model.ContaBancaria;
import model.DespesaReceita;
import model.DoacaoEfetiva;
import model.FontePagadora;
import model.Gestao;
import model.HistoricoAditivoOrcamento;
import model.LancamentoAcao;
import model.LancamentoAuxiliar;
import model.Orcamento;
import model.OrcamentoProjeto;
import model.PrestacaoOrcamento;
import model.Projeto;
import model.ProjetoRubrica;
import model.Rubrica;
import model.RubricaOrcamento;
import model.StatusPagamentoLancamento;
import model.SubComponente;
import model.TipoGestao;
import model.TipoLocalidade;
import model.TipoParcelamento;
import model.TipoProjetoDespesa;
import model.TipoProjetoOrcamento;
import model.TransferenciaOverHead;
import repositorio.CalculatorRubricaRepositorio;
import repositorio.ComponenteRepositorio;
import repositorio.GestaoRepositorio;
import repositorio.OrcamentoRepositorio;
import service.CompraService;
import service.CustoPessoalService;
import service.OrcamentoService;
import service.ProjetoService;
import service.SolicitacaoViagemService;
import util.ChartJsView;
import util.DataUtil;
import util.Filtro;
import util.UsuarioSessao;

@Named(value = "orcamento_controller")
@ViewScoped
public class OrcamentoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private @Inject Orcamento orcamento;
	private @Inject OrcamentoRepositorio repositorio;
	private @Inject ComponenteRepositorio componenteRepositorio;
	private @Inject CompraService compraService;
	private @Inject SolicitacaoViagemService viagemService;
	private @Inject UsuarioSessao usuarioSessao;
	private @Inject RubricaOrcamento rubricaOrcamento;
	private @Inject Rubrica rubrica;
	private @Inject ProjetoService gestaoProjeto;
	private @Inject GestaoRepositorio gestaoRepositorio;
	private @Inject CalculatorRubricaRepositorio calculatorRubricaRepositorio;
	private @Inject OrcamentoService orcamentoService;

	private Projeto projetoDespesa;

	private List<SubComponente> subComponentes = new ArrayList<>();
	private List<ComponenteClass> componentes = new ArrayList<>();
	private List<Orcamento> orcamentos = new ArrayList<>();
	private List<RubricaOrcamento> rubricas = new ArrayList<>();
	private List<OrcamentoProjeto> projetosDoOrcamento = new ArrayList<>();
	private List<ProjetoRubrica> listaProjetoRubricas = new ArrayList<ProjetoRubrica>();
	private List<PrestacaoOrcamento> listaPrestacoesOrcamento = new ArrayList<>();
	private List<ProjetoRubrica> listaDeRubricasProjeto = new ArrayList<>();

	private List<TransferenciaOverHead> listaOverHead = new ArrayList<>();
	private List<TransferenciaOverHead> listaOverHeadIndireto = new ArrayList<>();
	private List<AlocacaoRendimento> rendimentos = new ArrayList<>();

	private String saldoDistribuido;

	private @Inject ComponenteClass componente;
	private @Inject SubComponente subComponente;
	private List<Projeto> listProject = new ArrayList<>();

	private List<ContaBancaria> allContas = new ArrayList<>();

	private List<LancamentoAuxiliar> lancamentos;
	private List<LancamentoAuxiliar> doacoesEfetivadas;

	private BigDecimal saldoRealOrcamento;
	private BigDecimal aReceber;
	private Filtro filtro = new Filtro();

	private List<ContaBancaria> contas = new ArrayList<>();

	private List<ContaBancaria> contasOverhead = new ArrayList<>();

	private List<TransferenciaOverHead> transferencias = new ArrayList<>();

	private Double porcentagem;

	private Date dataFinalOrcamento;

	@Inject
	private TransferenciaOverHead overhead;

	private Boolean preCadastro;

	private Boolean continueCadastro = false;

	public void init() {
		carregarOrcamentos();

		// carregarComponentes();
		// carregarRubrica();
		// carregarProjetosPorDoacao();
		// carregarSaldoNaoDistribuido();
		// carregarContas();

	}

	public void initCadastro() {
		carregarComponentes();
		carregarRubrica();
		carregarProjetosPorDoacao();
		carregarSaldoNaoDistribuido();
		carregarContas();
		carregarSaldoEmCaixa();
		// createPieModel();
		// createHorizontalBarModel();
		// createBarChartS();
		// createStackedBarModel();
		createStackedGroupBarModel();
		// carregarRubricasProjetoDespesa();

		if (orcamento.getProjetoRubrica() != null && orcamento.getProjetoRubrica().getProjeto() != null) {
			setProjetoDespesa(orcamento.getProjetoRubrica().getProjeto());
			carregarRubricasProjetoDespesa();
		}

		if (orcamento != null && orcamento.getDataFinal() != null)
			dataFinalOrcamento = orcamento.getDataFinal();

		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("administracao")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin"))
			preCadastro = false;
		else
			preCadastro = true;

		PrimeFaces.current().executeScript("setarFocused('info-geral')");
	}

	@Inject
	private LancamentoAcao lancamentoAcao;

	@Inject
	private DoacaoEfetiva doacaoEfetiva;

	@Inject
	private BaixaAplicacao baixaAplicacao;

	@Inject
	private AplicacaoRecurso aplicacao;

	@Inject
	private CustoPessoalService service;

	private PrestacaoOrcamento prestacaoOrcamento = new PrestacaoOrcamento();

	private LancamentoAuxiliar lancamentoAuxiliar = new LancamentoAuxiliar();

	public LancamentoAuxiliar getLancamentoAuxiliar() {
		return lancamentoAuxiliar;
	}

	public void setLancamentoAuxiliar(LancamentoAuxiliar lancamentoAuxiliar) {
		this.lancamentoAuxiliar = lancamentoAuxiliar;
	}

	public List<ContaBancaria> completeContaAdiantamentoEFornecedor(String query) {
		contas = new ArrayList<>();
		contas = orcService.getContasFornecedoresEAdiantamentos(query);
		return contas;
	}

	public TipoParcelamento[] tiposParcelas() {
		return TipoParcelamento.values();
	}

	public void dialogHelp() {
		HashMap<String, Object> options = new HashMap<>();
		options.put("width", "100%");
		options.put("height", "100%");
		options.put("contentWidth", "100%");
		options.put("contentHeight", "100%");
		options.put("resizable", false);
		options.put("minimizable", true);
		options.put("maximizable", true);
		PrimeFaces.current().dialog().openDynamic("dialog/help/help_orcamento", options, null);
	}

	public void editarLancamentoEfetivo() {
		doacaoEfetiva = service.findDoacaoEfetivaById(lancamentoAuxiliar.getId());
	}

	public void excluirLancamentoEfetivo() {
		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro")) {
			doacaoEfetiva = service.findDoacaoEfetivaById(lancamentoAuxiliar.getId());
			service.removerDoacaoEfetiva(doacaoEfetiva);
			carregarDoacoesEfetivadas();
		} else {
			mostrarMessageLiberarExclusao();
		}

		lancamentoAuxiliar = new LancamentoAuxiliar();
		// closeDialogLoading();
	}

	public void mostrarMessageLiberarExclusao() {

		StringBuilder msg = new StringBuilder("Olá " + usuarioSessao.getNomeUsuario()
				+ " você não tem permissão para excluir ou editar um lançamento após entrada no sistema, por favor entre em contato com o setor administrativo para tal ação.\n");

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso!", msg.toString());

		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	public void prepararAplicacao() {
		ContaBancaria contaRecebedor = orcService.findContaTransitoria(new Long(5));
		aplicacao.setContaPagador(orcamento.getContaBancaria());
		aplicacao.setContaRecebedor(contaRecebedor);
		aplicacao.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
		aplicacao.setQuantidadeParcela(1);
	}

	public void preparaBaixaAplicacao() {
		ContaBancaria contaPagador = orcService.findContaTransitoria(new Long(5));
		baixaAplicacao.setContaPagador(contaPagador);
		baixaAplicacao.setContaRecebedor(orcamento.getContaBancaria());
		baixaAplicacao.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
		baixaAplicacao.setQuantidadeParcela(1);
	}

	public void salvarBaixaAplicacao() {
		if (baixaAplicacao.getId() == null) {
			baixaAplicacao.setCodigo(gerarCodigo());
			baixaAplicacao.setSolicitante(usuarioSessao.getUsuario().getColaborador());
			baixaAplicacao.setDepesaReceita(DespesaReceita.RECEITA);
			baixaAplicacao.setLocalidade(service.findLocalidadeByIdDefault());
			baixaAplicacao.setGestao(service.findGestaoByIdDefault());
		}

		baixaAplicacao.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
		baixaAplicacao.setTipoGestao(TipoGestao.COORD);
		baixaAplicacao.setTipoLocalidade(TipoLocalidade.SEDE);
		baixaAplicacao.setVersionLancamento("MODE01");
		// baixaAplicacao.setContaRecebedor(orcamento.getContaBancaria());

		lancamentoAcao.setDespesaReceita(DespesaReceita.RECEITA);

		RubricaOrcamento linhaOrcamento = orcService.buscarLinhaOrcamentoBaixaAplicacao(orcamento.getId());

		if (linhaOrcamento == null) {
			linhaOrcamento = new RubricaOrcamento();
			linhaOrcamento.setOrcamento(orcamento);
			linhaOrcamento.setTipo("ba");
			linhaOrcamento.setValor(BigDecimal.ZERO);
			linhaOrcamento.setComponente(orcService.buscarComponenteDefault());
			linhaOrcamento.setSubComponente(orcService.buscarSubComponenteDefault());
			linhaOrcamento.setRubrica(orcService.buscarRubricaBaixaAplicacaoDefault());
			linhaOrcamento = orcService.salvarRubricaOrcamento(linhaOrcamento);
		}

		lancamentoAcao.setRubricaOrcamento(linhaOrcamento);
		// lancamentoAcao.setProjeto(lancamentoAcao.getProjetoRubrica().getProjeto());
		// lancamentoAcao.setOrcamento(lancamentoAcao.getRubricaOrcamento().getOrcamento());
		lancamentoAcao.setValor(baixaAplicacao.getValorTotalComDesconto());
		// custoPessoal.setValorTotalComDesconto(custoPessoal.getValorTotalComDesconto().add(lancamentoAcao.getValor()));
		lancamentoAcao.setLancamento(baixaAplicacao);
		baixaAplicacao.getLancamentosAcoes().add(lancamentoAcao);
		lancamentoAcao = new LancamentoAcao();

		orcService.salvarBaixaAplicacao(baixaAplicacao, usuarioSessao.getUsuario());

		baixaAplicacao = new BaixaAplicacao();
		lancamentoAcao = new LancamentoAcao();
		limpar();
	}

	public void salvarAplicacao() {
		if (aplicacao.getId() == null) {
			aplicacao.setCodigo(gerarCodigo());
			aplicacao.setSolicitante(usuarioSessao.getUsuario().getColaborador());
			aplicacao.setDepesaReceita(DespesaReceita.RECEITA);
			aplicacao.setLocalidade(service.findLocalidadeByIdDefault());
			aplicacao.setGestao(service.findGestaoByIdDefault());
		}

		// doacaoEfetiva.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
		aplicacao.setTipoGestao(TipoGestao.COORD);
		aplicacao.setTipoLocalidade(TipoLocalidade.SEDE);
		aplicacao.setVersionLancamento("MODE01");
		// aplicacao.setContaRecebedor(orcamento.getContaBancaria());

		lancamentoAcao.setDespesaReceita(DespesaReceita.RECEITA);

		RubricaOrcamento linhaOrcamento = orcService.buscarLinhaOrcamentoAplicacao(orcamento.getId());

		if (linhaOrcamento == null) {
			linhaOrcamento = new RubricaOrcamento();
			linhaOrcamento.setOrcamento(orcamento);
			linhaOrcamento.setTipo("ap");
			linhaOrcamento.setValor(BigDecimal.ZERO);
			linhaOrcamento.setComponente(orcService.buscarComponenteDefault());
			linhaOrcamento.setSubComponente(orcService.buscarSubComponenteDefault());
			linhaOrcamento.setRubrica(orcService.buscarRubricaAplicacoDefault());
			linhaOrcamento = orcService.salvarRubricaOrcamento(linhaOrcamento);
		}

		lancamentoAcao.setRubricaOrcamento(linhaOrcamento);
		// lancamentoAcao.setProjeto(lancamentoAcao.getProjetoRubrica().getProjeto());
		// lancamentoAcao.setOrcamento(lancamentoAcao.getRubricaOrcamento().getOrcamento());
		lancamentoAcao.setValor(aplicacao.getValorTotalComDesconto());
		// custoPessoal.setValorTotalComDesconto(custoPessoal.getValorTotalComDesconto().add(lancamentoAcao.getValor()));
		lancamentoAcao.setLancamento(aplicacao);
		aplicacao.getLancamentosAcoes().add(lancamentoAcao);
		lancamentoAcao = new LancamentoAcao();

		orcService.salvarAplicacao(aplicacao, usuarioSessao.getUsuario());

		aplicacao = new AplicacaoRecurso();
		lancamentoAcao = new LancamentoAcao();
		limpar();
	}

	public void salvarDoacaoEfetiva() {
		if (doacaoEfetiva.getId() == null) {
			doacaoEfetiva.setCodigo(gerarCodigo());
			doacaoEfetiva.setSolicitante(usuarioSessao.getUsuario().getColaborador());
			doacaoEfetiva.setDepesaReceita(DespesaReceita.RECEITA);
			doacaoEfetiva.setLocalidade(service.findLocalidadeByIdDefault());
			doacaoEfetiva.setGestao(service.findGestaoByIdDefault());
		}

		// doacaoEfetiva.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
		doacaoEfetiva.setTipoGestao(TipoGestao.COORD);
		doacaoEfetiva.setTipoLocalidade(TipoLocalidade.SEDE);
		doacaoEfetiva.setVersionLancamento("MODE01");
		doacaoEfetiva.setContaRecebedor(orcamento.getContaBancaria());

		lancamentoAcao.setDespesaReceita(DespesaReceita.RECEITA);

		RubricaOrcamento linhaOrcamento = orcService.buscarLinhaOrcamentoEfetiva(orcamento.getId());

		if (linhaOrcamento == null) {
			linhaOrcamento = new RubricaOrcamento();
			linhaOrcamento.setOrcamento(orcamento);
			linhaOrcamento.setTipo("de");
			linhaOrcamento.setValor(BigDecimal.ZERO);
			linhaOrcamento.setComponente(orcService.buscarComponenteDefault());
			linhaOrcamento.setSubComponente(orcService.buscarSubComponenteDefault());
			linhaOrcamento.setRubrica(orcService.buscarRubricaDefault());
			linhaOrcamento = orcService.salvarRubricaOrcamento(linhaOrcamento);
		}

		lancamentoAcao.setRubricaOrcamento(linhaOrcamento);
		// lancamentoAcao.setProjeto(lancamentoAcao.getProjetoRubrica().getProjeto());
		// lancamentoAcao.setOrcamento(lancamentoAcao.getRubricaOrcamento().getOrcamento());
		lancamentoAcao.setValor(doacaoEfetiva.getValorTotalComDesconto());
		// custoPessoal.setValorTotalComDesconto(custoPessoal.getValorTotalComDesconto().add(lancamentoAcao.getValor()));
		lancamentoAcao.setLancamento(doacaoEfetiva);
		doacaoEfetiva.setLancamentosAcoes(new ArrayList<>());
		doacaoEfetiva.getLancamentosAcoes().add(lancamentoAcao);

		lancamentoAcao = new LancamentoAcao();

		orcService.salvarDoacaoEfetiva(doacaoEfetiva, usuarioSessao.getUsuario());
		carregarDoacoesEfetivadas();

		doacaoEfetiva = new DoacaoEfetiva();
		lancamentoAcao = new LancamentoAcao();
		limpar();

	}

	public String gerarCodigo() {

		StringBuilder s = new StringBuilder("SP");

		Calendar calendar = Calendar.getInstance();
		int ano = calendar.get(Calendar.YEAR);
		int mes = calendar.get(Calendar.MONTH) + 1;
		int dia = calendar.get(Calendar.DAY_OF_MONTH);
		int minuto = calendar.get(Calendar.MINUTE);
		int segundo = calendar.get(Calendar.SECOND);
		int milisegundo = calendar.get(Calendar.MILLISECOND);

		s.append(String.valueOf(ano).substring(2));
		s.append(String.valueOf(mes));
		s.append(String.valueOf(dia));
		s.append(String.valueOf(minuto));
		s.append(String.valueOf(segundo));
		// s.append(String.valueOf(milisegundo).substring(0, 2));
		return s.toString();
	}

	private PieChartModel pieModel;

	private void createPieModel() {
		pieModel = new PieChartModel();

		pieModel.set("OverHead", 540);
		pieModel.set("Indiretos", 325);
		pieModel.set("Rendimentos", 702);
		pieModel.set("Custo de pessoal", 421);

		pieModel.setTitle("Simple Pie");
		pieModel.setLegendPosition("w");
		pieModel.setShadow(false);
	}

	private HorizontalBarChartModel hbarModel;
	private BarChartModel stackedBarModel;

	public void createBarChartS() {
		hbarModel = new HorizontalBarChartModel();
		ChartJsView cjs = new ChartJsView();
		hbarModel = cjs.initBar();

	}

	public void createStackedBarModel() {
		stackedBarModel = new BarChartModel();
		ChartJsView cjs = new ChartJsView();
		stackedBarModel = cjs.createStackedBarModel();
	}

	private BarChartModel stackedGroupBarModel;

	public void createStackedGroupBarModel() {
		stackedGroupBarModel = new BarChartModel();
		ChartJsView cjs = new ChartJsView();
		stackedGroupBarModel = cjs.createStackedGroupBarModelValuesBudget(orcamento);
	}

//	private void createHorizontalBarModel() {
//		horizontalBarModel = new HorizontalBarChartModel();
//
//		ChartSeries planejado = new ChartSeries();
//		planejado.setLabel("Planejado");
//
//		planejado.set("Custo pessoal",
//				orcamento.getValorCustoPessoal() != null ? orcamento.getValorCustoPessoal().intValue() : 0);
//		planejado.set("Rendimentos",
//				orcamento.getValorRendimento() != null ? orcamento.getValorRendimento().intValue() : 0);
//		planejado.set("Indireto",
//				orcamento.getValorOverheadIndireto() != null ? orcamento.getValorOverheadIndireto().intValue() : 0);
//		planejado.set("Overhead", orcamento.getValorOverhead() != null ? orcamento.getValorOverhead().intValue() : 0);
//		planejado.set("Doação", orcamento.getValor() != null ? orcamento.getValor().intValue() : 0);
//
//		// planejado.set("Custo pessoal", 1000);
//		// planejado.set("Rendimentos", 500);
//		// planejado.set("Indireto", BigDecimal.ZERO.intValue());
//		// planejado.set("Overhead",BigDecimal.ZERO.intValue());
//		// planejado.set("Doação", BigDecimal.ZERO.intValue());
//
//		ChartSeries executado = new ChartSeries();
//		executado.setLabel("Recebido");
//		executado.set("Custo pessoal",
//				orcamento.getCustoPessoalExec() != null ? orcamento.getCustoPessoalExec().intValue() : 0);
//		executado.set("Rendimentos",
//				orcamento.getValorRendimentoExec() != null ? orcamento.getValorRendimentoExec().intValue() : 0);
//		executado.set("Indireto",
//				orcamento.getValorOverheadIndiretoExec() != null ? orcamento.getValorOverheadIndiretoExec().intValue()
//						: 0);
//		executado.set("Overhead",
//				orcamento.getValorOverheadExec() != null ? orcamento.getValorOverheadExec().intValue() : 0);
//		executado.set("Doação", saldoRealOrcamento != null ? saldoRealOrcamento.intValue() : 0);
//
//		// executado.set("Custo pessoal", 1000);
//		// executado.set("Rendimentos", 500);
//		// executado.set("Indireto", BigDecimal.ZERO.intValue());
//		// executado.set("Overhead",BigDecimal.ZERO.intValue());
//		// executado.set("Doação", BigDecimal.ZERO.intValue());
//
//		horizontalBarModel.addSeries(executado);
//		horizontalBarModel.addSeries(planejado);
//		horizontalBarModel.setMouseoverHighlight(true);
//
//		horizontalBarModel.setTitle("Planejado x Recebido");
//		horizontalBarModel.setLegendPosition("e");
//		horizontalBarModel.setStacked(false);
//
//		Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
//		// xAxis.setLabel("Births");
//		xAxis.setMin(0);
//		xAxis.setMax(
//				orcamento.getValor() != null ? orcamento.getValor().intValue() + (orcamento.getValor().intValue() / 3)
//						: 500);
//
//		Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
//		// yAxis.setLabel("Gender");
//	}

	public void carregarOrcamentos() {
		orcamentos = repositorio.getOrcamentos(filtro);
	}

	public void carregarContas() {
		contas = orcService.getContas();
	}

	public void carregarContasOverHead() {
		contasOverhead = orcService.getContasCorrente();
	}

	public void carregarComponentes() {
		componentes = componenteRepositorio.getComponentes();
	}

	public void carregarSubComponentes() {
		subComponentes = componenteRepositorio.getSubComponentes(componente.getId());
	}

	public List<Orcamento> getOrcamentos() {
		return orcamentos;
	}

	public void carregarProjetosPorDoacao() {
		if (orcamento.getId() != null)
			projetosDoOrcamento = orcService.getProjetosByOrcamentoFiltro(orcamento.getId(), filtro);
	}

	public void salvarRubrica() {
		// repositorio.salvarRubrica(rubrica);
		orcService.salvarRubrica(rubrica);
		rubrica = new Rubrica();
		messageSalvamento("Nova Rubrica salva, pode incluir no seu orçamento!");
	}

	public void salvarRubricaService() {
		salvarRubrica();
		rubrica = new Rubrica();
	}

	public void messageSalvamento(String txt) {

		// StringBuilder msg = new StringBuilder("Projeto salvo com sucesso");
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", txt);

		FacesContext.getCurrentInstance().addMessage(null, message);
		;
	}

	private @Inject OrcamentoService orcService;
	private BigDecimal valorDistribuido;
	private BigDecimal diferenca;

	public BigDecimal valorExcecutado(RubricaOrcamento rubricaOrcamento) {
		BigDecimal total = BigDecimal.ZERO;
		for (ProjetoRubrica p : repositorio.getProjetoRubricaCompleteByOrcamentoRubrica(rubricaOrcamento.getId())) {
			if (repositorio.getTotalDespesaPorRubrica(p.getId()) != null)
				total = total.add(repositorio.getTotalDespesaPorRubrica(p.getId()));
			if (repositorio.getTotalReceitaPorRubrica(p.getId()) != null)
				total = total.subtract(repositorio.getTotalReceitaPorRubrica(p.getId()));
		}

		return total;
	}

	// @Transactional
	public void salvarRubricaOrcamento() {

		if (rubricaOrcamento.getValor() != null) {
			if (rubricaOrcamento.getValor().compareTo(diferenca) > 0) {
				messageSalvamento(
						"Valor ultrapassa o teto do projeto, valor dentro do projeto: " + buscarValorTotal(diferenca));
				carregarRubrica();
				calculaValor();
				return;
			}

			rubricaOrcamento.setOrcamento(orcamento);
			rubricaOrcamento.setComponente(componente);
			rubricaOrcamento.setSubComponente(subComponente);

			if (rubricaOrcamento.getValor() == null) {
				rubricaOrcamento.setValor(BigDecimal.ZERO);
			}

			orcService.salvarRubricaOrcamento(rubricaOrcamento);
			// rubricaOrcamento = new RubricaOrcamento();
			limparVar();
			carregarRubrica();
			calculaValor();

			if (!continueCadastro) {
				executeScript("PF('dlg-nova-linha').hide()", "");
			}

			addMessage("", "Linha orçamentária salva com sucesso!", FacesMessage.SEVERITY_INFO);

			// carregarOrcamentos();
		} else {
			messageSalvamento("Campo Valor é Obrigatorio!");
		}
	}

	private List<Rubrica> categorias;

	public void preparEdicaoRubrica() {
		componente = rubricaOrcamento.getComponente();
		carregarSubComponentes();
		subComponente = rubricaOrcamento.getSubComponente();
		calculaValor(rubricaOrcamento.getValor());
	}

	public void remover() {
		orcService.removerDoacao(orcamento);
		carregarOrcamentos();
	}

	public void carregarCategorias() {
		categorias = new ArrayList<>();
		categorias = orcService.getCategorias();
	}

	public List<Projeto> completeProjetos(String query) {
		listProject = new ArrayList<Projeto>();
		listProject = orcService.getProjetoAutoCompleteMODE01(query);
		return listProject;
	}

	public List<ContaBancaria> completeConta(String query) {
		allContas = new ArrayList<>();
		;
		allContas = orcService.getAllConta(query);
		return allContas;
	}

	public void carregarExtatoDoacao() {
		lancamentos = new ArrayList<>();
		filtro.setIdDoacao(orcamento.getId());
		lancamentos = orcService.getExtratoCtrlDoacao(filtro);
		// service.getPagamentosPE(filtro);
	}

	private List<LancamentoAuxiliar> lancs = new ArrayList<>();

	public void gerarExecucaoFinanceira() {
		List<Orcamento> doacoes = repositorio.getOrcamentos(filtro);
		lancamentos = new ArrayList<>();
		for (Orcamento doac : doacoes) {
			filtro.setIdDoacao(doac.getId());
			lancamentos.addAll(orcService.getExtratoCtrlDoacaoParaConferencia(filtro));
		}
	}

	public void carregarDoacoesEfetivadas() {
		doacoesEfetivadas = new ArrayList<>();
		filtro = new Filtro();
		filtro.setIdDoacao(orcamento.getId());
		doacoesEfetivadas = orcService.getDoacoesEfetivadas(filtro);
		saldoRealOrcamento = orcService.getSaldoDoacoesEfetivadas(filtro);
		// service.getPagamentosPE(filtro);
	}

	public void carregarDoacoesEfetivadas(Filtro filtro) {
		doacoesEfetivadas = new ArrayList<>();
		filtro.setStatusPagamento(StatusPagamentoLancamento.EFETIVADO);
		filtro.setIdDoacao(orcamento.getId());
		doacoesEfetivadas = orcService.getDoacoesEfetivadas(filtro);
		saldoRealOrcamento = orcService.getSaldoDoacoesEfetivadas(filtro);
		// service.getPagamentosPE(filtro);
	}

	public void carregarSaldoEmCaixa() {
		saldoRealOrcamento = BigDecimal.ZERO;
		aReceber = BigDecimal.ZERO;
		if (orcamento.getId() != null) {
			filtro = new Filtro();
			filtro.setIdDoacao(orcamento.getId());
			saldoRealOrcamento = orcService.getSaldoDoacoesEfetivadas(filtro);
			orcamento.setSaldoReal(saldoRealOrcamento);
		}

		if (orcamento.getId() != null)
			aReceber = orcamento.getValor().subtract(saldoRealOrcamento);
	}

	public void carregarAreceber() {
		aReceber = BigDecimal.ZERO;
		if (orcamento.getId() != null) {
			filtro = new Filtro();
			filtro.setIdDoacao(orcamento.getId());
			aReceber = orcService.getAreceber(filtro);
		}
	}

	public void limpar() {
		filtro = new Filtro();
		filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		filtro.setDataFinal(DataUtil.getDataFinal(new Date()));
	}

	public void limparVar() {
		rubricaOrcamento = new RubricaOrcamento();
		componente = new ComponenteClass();
		subComponente = new SubComponente();
	}

	// Edição de linha orçamentária na nova versão

	public void editLine(Long id) {
		limparVar();
		rubricaOrcamento = orcService.findByIdRubricaOrcamento(id);
		componente = rubricaOrcamento.getComponente();
		subComponente = rubricaOrcamento.getSubComponente();
	}

	// public void salvarRubricaOrcamentoService(){
	// salvarRubricaOrcamento();
	// carregarRubrica();
	// }

	public String buscarValorTotal(BigDecimal valor) {
		NumberFormat format = NumberFormat.getCurrencyInstance();
		if (valor == null) {
			return "";
		} else {
			return format.format(valor);
		}
	}

	public String buscarValorTotalSaida() {
		BigDecimal valorTotal = BigDecimal.ZERO;
		if (lancamentos != null)
			if (!lancamentos.isEmpty()) {
				valorTotal = lancamentos.get(0).getTotalSaida();
			}
		NumberFormat format = NumberFormat.getCurrencyInstance();
		if (valorTotal == null) {
			valorTotal = BigDecimal.ZERO;
		}
		return format.format(valorTotal);
	}

	public String buscarValorTotalEntrada() {
		BigDecimal valorTotal = BigDecimal.ZERO;
		if (lancamentos != null)
			if (!lancamentos.isEmpty()) {
				valorTotal = lancamentos.get(0).getTotalEntrada();
			}
		NumberFormat format = NumberFormat.getCurrencyInstance();
		if (valorTotal == null) {
			valorTotal = BigDecimal.ZERO;
		}
		return format.format(valorTotal);
	}

	public String buscarValorSaldo() {
		BigDecimal valorTotal = BigDecimal.ZERO;
		if (lancamentos != null)
			if (!lancamentos.isEmpty()) {
				valorTotal = orcamento.getValor().add(lancamentos.get(0).getTotalEntrada())
						.subtract(lancamentos.get(0).getTotalSaida());
			}
		NumberFormat format = NumberFormat.getCurrencyInstance();
		if (valorTotal == null) {
			valorTotal = BigDecimal.ZERO;
		}
		return format.format(valorTotal);
	}

	public String buscarValorTotal() {
		BigDecimal valorTotal = BigDecimal.ZERO;
		for (RubricaOrcamento rubricaOrcamento : rubricas) {
			valorTotal = valorTotal.add(rubricaOrcamento.getValor());
		}
		NumberFormat format = NumberFormat.getCurrencyInstance();
		if (valorTotal == null) {
			valorTotal = BigDecimal.ZERO;
		}
		return format.format(valorTotal);
	}

	private String saldoNaoDistribuido;

	public void carregarSaldoNaoDistribuido() {
		if (orcamento.getId() == null) {
			return;
		}

		BigDecimal valorTotal = BigDecimal.ZERO;

		for (OrcamentoProjeto orc : projetosDoOrcamento) {
			valorTotal = valorTotal.add(orc.getValor());
		}

		NumberFormat format = NumberFormat.getCurrencyInstance();
		saldoDistribuido = format.format(valorTotal);
		saldoNaoDistribuido = format.format(orcamento.getValor().subtract(valorTotal));
	}

	public void removerRubrica(int index) {
		// orcamento.getRubricas().remove(index);
		// repositorio.salvar(orcamento);
		orcService.removerRubricaOrcamento(rubricaOrcamento.getId());
		rubricaOrcamento = new RubricaOrcamento();
		carregarRubrica();
		calculaValor();
	}

	private RubricaOrcamento rubricaOrcamentoTemp = new RubricaOrcamento();

	public RubricaOrcamento getRubricaOrcamentoTemp() {
		return rubricaOrcamentoTemp;
	}

	public void setRubricaOrcamentoTemp(RubricaOrcamento rubricaOrcamentoTemp) {
		this.rubricaOrcamentoTemp = rubricaOrcamentoTemp;
	}

	public void listarProjetosEmpenhados(RubricaOrcamento rubricaOrcamento) {
		rubricaOrcamentoTemp = rubricaOrcamento;
		listaProjetoRubricas = gestaoProjeto.getNewProjetoRubricaByRubricaOrcamento(rubricaOrcamento);
	}

	public void abrirMovimentacao(RubricaOrcamento rubricaOrcamento) {
		rubricaOrcamentoTemp = rubricaOrcamento;
		listaProjetoRubricas = gestaoProjeto.getNewProjetoRubricaByRubricaOrcamento(rubricaOrcamento);
	}

	public List<ProjetoRubrica> getListaProjetoRubricas() {
		return listaProjetoRubricas;
	}

	public void setListaProjetoRubricas(List<ProjetoRubrica> listaProjetoRubricas) {
		this.listaProjetoRubricas = listaProjetoRubricas;
	}

	public void carregaDialog() {

	}

	public void carregarRubrica() {

		if (orcamento.getId() != null) {
			rubricas = orcService.getNewRubricasDeOrcamento(orcamento.getId(), filtro);
		}
	}

	public void calculaValor(BigDecimal valor) {

		valorDistribuido = BigDecimal.ZERO;
		diferenca = BigDecimal.ZERO;

		Double vlDistAux;

		Double vlOrcamento = orcamento.getValor().doubleValue();

		for (RubricaOrcamento rubrica : rubricas) {
			valorDistribuido = valorDistribuido.add(rubrica.getValor());
			vlDistAux = rubrica.getValor().doubleValue();
			rubrica.setPorcentagem((vlDistAux / vlOrcamento) * 100);

		}

		Double vlDistribuido = valorDistribuido.doubleValue();

		diferenca = orcamento.getValor().subtract(valorDistribuido).add(valor);
		porcentagem = (vlDistribuido / vlOrcamento) * 100;

	}

	private BigDecimal valorOrcadoOverhead = BigDecimal.ZERO;//
	private BigDecimal valorPagoOverhead = BigDecimal.ZERO;//
	private BigDecimal valorDisponivelOverhead = BigDecimal.ZERO;

	private BigDecimal valorOrcadoOverheadIndireto = BigDecimal.ZERO;// Valor vem do total de alocação de rendimento
																		// para doação
	private BigDecimal valorPagoOverheadIndireto = BigDecimal.ZERO;//
	private BigDecimal valorDisponivelOverheadIndireto = BigDecimal.ZERO;

	public void carregarValoresOverhead() {
		TransferenciaOverHead transf = new TransferenciaOverHead();
		transf.setDoacaoPagadora(orcamento);
		valorPagoOverhead = orcService.getTotalTransferenciaOverheadPagos(transf);
		valorOrcadoOverhead = orcamento.getValorOverheadAPagar() != null
				? transf.getDoacaoPagadora().getValorOverheadAPagar()
				: BigDecimal.ZERO;

		valorDisponivelOverhead = valorOrcadoOverhead.subtract(valorPagoOverhead);
	}

	public void carregarValoresOverheadIndireto() {
		TransferenciaOverHead transf = new TransferenciaOverHead();
		transf.setDoacaoPagadora(orcamento);

		valorOrcadoOverheadIndireto = orcService.getValorRendimento(orcamento.getId());
		valorPagoOverheadIndireto = orcService.getTotalTransferenciaOverheadIndiretoPagos(transf);

		valorDisponivelOverheadIndireto = valorOrcadoOverheadIndireto.subtract(valorPagoOverheadIndireto);

	}

	public void calculaValor() {

		valorDistribuido = BigDecimal.ZERO;
		diferenca = BigDecimal.ZERO;

		Double vlDistAux;

		Double vlOrcamento = orcamento.getValor().doubleValue();

		for (RubricaOrcamento rubrica : rubricas) {
			valorDistribuido = valorDistribuido.add(rubrica.getValor());
			vlDistAux = rubrica.getValor().doubleValue();
			rubrica.setPorcentagem((vlDistAux / vlOrcamento) * 100);

		}

		Double vlDistribuido = valorDistribuido.doubleValue();

		diferenca = orcamento.getValor().subtract(valorDistribuido);
		porcentagem = (vlDistribuido / vlOrcamento) * 100;

	}

	// TODO: Refatorar para uma classe util
	public void executeScript(String script, String modo) {

		PrimeFaces.current().executeScript(script);

	}

	private TabView tabviewOrcamento;

	public void definirTabOrcamento() {
		int index = 0;
		if (tabviewOrcamento != null) {
			index = tabviewOrcamento.getActiveIndex();

		}

		if (index == 0) {
			executeScript("setarFocused('info-geral')", "focus");
		} else if (index == 1) {

			executeScript("setarFocused('info-permissoes-input-text')", "focus");
		} else if (index == 2) {
			carregarRubrica();
			calculaValor();
			carregarPainelOrcamentoRequisito();
			createStackedGroupBarModel();
		} else if (index == 3) {
			carregarExtatoDoacao();
		} else if (index == 4) {
			carregarDoacoesEfetivadas();
		} else if (index == 5) {
			// carregarOrcamentos();
			// carregarContasOverHead();
			// carregarListaDeTransferencias();
		} else if (index == 6) {
			carregarOrcamentos();
			carregarContasOverHead();
			carregarListaDeTransferencias();
			carregarInformacoesOverhead();
		} else if (index == 7) {
			carregarPainelOrcamento();
			// createPieModel();
			// createHorizontalBarModel();
		}
	}

	public void carregarInformacoesOverhead() {
		carregarValorRendimentoAlocado();
		carregarValoresOverhead();
		carregarValoresOverheadIndireto();
	}

	public void filtrarRubricas() {
		if (componente.getId() != null) {
			filtro.setComponenteClass(componente.getId());
		}

		if (subComponente.getId() != null) {
			filtro.setSubComponente(subComponente.getId());
		}

		carregarRubrica();
	}

	public void limparFiltro() {
		filtro = new Filtro();
	}

	public void filtroOrcamento() {
		orcamentos = orcamentoService.filtrarOrcamentos(filtro);

		limparFiltro();
	}

	public void verificarTabs() {
	}

	public void salvarTransferenciaOverHead() {

		overhead.setContaPagadora(orcamento.getContaBancaria());
		overhead.setDoacaoPagadora(orcamento);

		if (orcService.verificarSaldoOverHead(overhead)) {
			orcService.salvarTransferenciaOverHead(overhead);
			overhead = new TransferenciaOverHead();
			carregarListaDeTransferencias();
			carregarInformacoesOverhead();
		} else {
			addMessage("", "Saldo indisponível para transferência de Overhead", FacesMessage.SEVERITY_WARN);
		}

	}

	public void removerTransferenciaOverHead() {
		orcService.removerTransferenciaOverHead(overhead);
		overhead = new TransferenciaOverHead();
		carregarListaDeTransferencias();
		carregarInformacoesOverhead();
	}

	public void carregarListaDeTransferencias() {
		transferencias = new ArrayList<TransferenciaOverHead>();
		TransferenciaOverHead t = new TransferenciaOverHead();
		t.setDoacaoPagadora(orcamento);
		transferencias = orcService.getListaTransferencia(t);
	}

	public void setarContaRecebedoraOverHead() {
		overhead.setContaRecebedora(overhead.getDoacaoRecebedora().getContaBancaria());
	}

	public void carregarValorRendimentoAlocado() {
		orcamento.setValorRendimentoExec(orcService.getValorRendimento(orcamento.getId()));
	}

	public void carregarPainelOrcamentoRequisito() {
		orcamento.setValorOverheadExec(orcService.getValorOverheadPago(orcamento.getId()));
		carregarSaldoEmCaixa();
	}

	public void carregarPainelOrcamento() {
		orcamento.setValorOverheadExec(orcService.getValorOverhead(orcamento.getId()));
		orcamento.setValorOverheadIndiretoExec(orcService.getValorOverheadIndireto(orcamento.getId()));
		orcamento.setValorRendimentoExec(orcService.getValorRendimento(orcamento.getId()));

		listaOverHead = orcService.getListOverHeadByOrcamento(orcamento.getId());
		listaOverHeadIndireto = orcService.getListOverHeadIndiretoByOrcamento(orcamento.getId());
		rendimentos = orcService.getListRendimentoByOrcamento(orcamento.getId());

		carregarSaldoEmCaixa();
		filtro = new Filtro();
		filtro.setStatusPagamento(StatusPagamentoLancamento.EFETIVADO);
		carregarDoacoesEfetivadas(filtro);

	}

	public void salvar() {

		try {
			if (orcamento.getId() == null) {
				orcamento.setDataCriacao(new Date());
				orcamento.setUsuarioCadastro(usuarioSessao.getUsuario());

				dataFinalOrcamento = orcamento.getDataFinal();
			} else {
				if (dataFinalOrcamento != null && orcamento.getDataFinal().after(dataFinalOrcamento)) {
					HistoricoAditivoOrcamento historicoAditivoOrcamento = new HistoricoAditivoOrcamento();
					historicoAditivoOrcamento.setDataAlteracao(new Date());
					historicoAditivoOrcamento.setDataAnterior(dataFinalOrcamento);
					historicoAditivoOrcamento.setDataAditivo(orcamento.getDataFinal());
					historicoAditivoOrcamento.setOrcamento(orcamento);
					historicoAditivoOrcamento.setUsuario(usuarioSessao.getUsuario());
					historicoAditivoOrcamento = orcService.salvarHistoricoOrcamento(historicoAditivoOrcamento);
					dataFinalOrcamento = orcamento.getDataFinal();
				} else
					dataFinalOrcamento = orcamento.getDataFinal();
			}

			orcamento.setDataUltimaEdição(new Date());
			orcamento.setUsuarioEdicao(usuarioSessao.getUsuario());
			orcamento.setPreCadastro(preCadastro);
			orcamento = orcService.salvar(orcamento);
			messageSalvamento("Orçamento salvo");
		} catch (Exception e) {
			e.printStackTrace();
			addMessage("", e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}

	public void salvarOrcamentoPainel() {

		try {
			if (orcamento.getId() == null) {
				orcamento.setDataCriacao(new Date());
				orcamento.setUsuarioCadastro(usuarioSessao.getUsuario());

				dataFinalOrcamento = orcamento.getDataFinal();
			} else {
				if (dataFinalOrcamento != null && orcamento.getDataFinal().after(dataFinalOrcamento)) {
					HistoricoAditivoOrcamento historicoAditivoOrcamento = new HistoricoAditivoOrcamento();
					historicoAditivoOrcamento.setDataAlteracao(new Date());
					historicoAditivoOrcamento.setDataAnterior(dataFinalOrcamento);
					historicoAditivoOrcamento.setDataAditivo(orcamento.getDataFinal());
					historicoAditivoOrcamento.setOrcamento(orcamento);
					historicoAditivoOrcamento.setUsuario(usuarioSessao.getUsuario());
					historicoAditivoOrcamento = orcService.salvarHistoricoOrcamento(historicoAditivoOrcamento);
					dataFinalOrcamento = orcamento.getDataFinal();
				} else
					dataFinalOrcamento = orcamento.getDataFinal();
			}

			orcamento.setDataUltimaEdição(new Date());
			orcamento.setUsuarioEdicao(usuarioSessao.getUsuario());
			orcamento.setPreCadastro(preCadastro);
			orcamento = orcService.salvar(orcamento);
			messageSalvamento("Orçamento salvo");
			carregarPainelOrcamento();
			// createHorizontalBarModel();

		} catch (Exception e) {
			e.printStackTrace();
			addMessage("", e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}

	public void gerarPrestacoes() {

		try {

			if (orcamento.getQuantidadeParcelas() == null)
				throw new Exception("Defina a quantidade de parcelas.");

			if (orcamento.getDataPrestacao() == null)
				throw new Exception("Defina a data de inicio da prestação.");

			if (orcamento.getTipoParcelamento() == null)
				throw new Exception("Defina o tipo de parcelamento.");

			orcamento.setPrestacoes(new ArrayList<>());
			Date data = orcamento.getDataPrestacao();
			BigDecimal valorParcela = orcamento.getValor().divide(new BigDecimal(orcamento.getQuantidadeParcelas()), 2,
					RoundingMode.HALF_UP);
			for (int i = 1; i <= orcamento.getQuantidadeParcelas().intValue(); i++) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(orcamento.getDataPrestacao());
				PrestacaoOrcamento prestacaoOrcamento = new PrestacaoOrcamento();
				if (i == 1)
					prestacaoOrcamento.setData(orcamento.getDataPrestacao());
				else {
					data = orcamento.getTipoParcelamento().obterParcela().calculaData(data);
					prestacaoOrcamento.setData(data);
				}
				prestacaoOrcamento.setValor(valorParcela);
				prestacaoOrcamento.setNumero(i);
				prestacaoOrcamento.setOrcamento(orcamento);

				orcamento.getPrestacoes().add(prestacaoOrcamento);
			}
		} catch (Exception e) {
			e.printStackTrace();
			addMessage("", e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}

	public void adicionarPrestacao() {
		try {

			if (prestacaoOrcamento.getData() == null)
				throw new Exception("Defina a data da prestação.");

			if (prestacaoOrcamento.getValor() == null)
				throw new Exception("Defina o valor da prestação.");

			if (orcamento.getPrestacoes() == null)
				orcamento.setPrestacoes(new ArrayList<>());

			int numero = 0;

			BigDecimal total = BigDecimal.ZERO;
			total = total.add(prestacaoOrcamento.getValor());
			for (PrestacaoOrcamento prestacaoOrcamento : orcamento.getPrestacoes()) {
				numero = prestacaoOrcamento.getNumero();
				total = total.add(prestacaoOrcamento.getValor());
			}

			if (total.compareTo(orcamento.getValor()) == 1)
				throw new Exception("O valor total das parcelas é maior que o valor orçado.");

			prestacaoOrcamento.setOrcamento(orcamento);
			prestacaoOrcamento.setNumero(numero + 1);

			orcamento.getPrestacoes().add(prestacaoOrcamento);

			prestacaoOrcamento = new PrestacaoOrcamento();

		} catch (Exception e) {
			e.printStackTrace();
			addMessage("", e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}

	public void removerPrestacao() {
		recalcularAoRemoverPrestacao(prestacaoOrcamento);
		orcamento.getPrestacoes().remove(prestacaoOrcamento);

		prestacaoOrcamento = new PrestacaoOrcamento();
	}

	public void recalcularAoRemoverPrestacao(PrestacaoOrcamento prestacaoOrcamento) {

		BigDecimal orcado = orcamento != null && orcamento.getValor() != null ? orcamento.getValor() : BigDecimal.ZERO;

		BigDecimal totalAnteriores = BigDecimal.ZERO;
		if (orcamento.getPrestacoes() == null)
			orcamento.setPrestacoes(new ArrayList<>());

		int count = 0;
		for (PrestacaoOrcamento po : orcamento.getPrestacoes()) {
			if (po.getNumero().intValue() < prestacaoOrcamento.getNumero().intValue())
				totalAnteriores = totalAnteriores.add(po.getValor());
			else if (po.getNumero().intValue() > prestacaoOrcamento.getNumero().intValue())
				count++;
		}

		if (count > 0) {
			BigDecimal total = orcado.subtract(totalAnteriores);
			BigDecimal rateio = total.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP);

			int num = prestacaoOrcamento.getNumero();
			for (PrestacaoOrcamento po : orcamento.getPrestacoes()) {
				if (po.getNumero().intValue() > prestacaoOrcamento.getNumero().intValue()) {
					po.setValor(rateio);
					po.setNumero(num);
					num++;
				}

			}
		}
	}

	public void recalcularPrestacoes(PrestacaoOrcamento prestacaoOrcamento) {

		BigDecimal orcado = orcamento != null && orcamento.getValor() != null ? orcamento.getValor() : BigDecimal.ZERO;

		BigDecimal totalAnteriores = BigDecimal.ZERO;
		if (orcamento.getPrestacoes() == null)
			orcamento.setPrestacoes(new ArrayList<>());

		int count = 0;
		for (PrestacaoOrcamento po : orcamento.getPrestacoes()) {
			if (po.getNumero().intValue() <= prestacaoOrcamento.getNumero().intValue())
				totalAnteriores = totalAnteriores.add(po.getValor());
			else
				count++;
		}

		BigDecimal total = orcado.subtract(totalAnteriores);
		BigDecimal rateio = total.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP);

		for (PrestacaoOrcamento po : orcamento.getPrestacoes()) {
			if (po.getNumero().intValue() > prestacaoOrcamento.getNumero().intValue())
				po.setValor(rateio);
		}
	}

	public String getTotalPrestacoes() {

		BigDecimal total = BigDecimal.ZERO;
		NumberFormat format = NumberFormat.getCurrencyInstance();

		if (orcamento.getPrestacoes() == null)
			orcamento.setPrestacoes(new ArrayList<>());

		for (PrestacaoOrcamento po : orcamento.getPrestacoes()) {
			total = total.add(po.getValor());
		}

		return format.format(total);
	}

	public void carregarRubricasProjetoDespesa() {

		if (getProjetoDespesa() != null) {
			listaDeRubricasProjeto = calculatorRubricaRepositorio
					.getProjetoRubricaByProjeto(getProjetoDespesa().getId());
		} else {
			orcamento.setProjetoRubrica(null);
			listaDeRubricasProjeto = null;
		}
	}

	public void carregarHistoricoAditivos() {
		orcamento.setHistoricoAditivos(orcService.getHistoricoAditivoOrcamento(orcamento.getId()));
	}

	public void setOrcamentos(List<Orcamento> orcamentos) {
		this.orcamentos = orcamentos;
	}

	public List<FontePagadora> completeFonte(String query) {
		List<FontePagadora> fontesPagadora = compraService.fontesAutoComplete(query);
		return fontesPagadora;
	}

	public List<String> completeTitulo(String query) {
		List<String> orcamentos = orcamentoService.getOrcamentoTitulosAutoComplete(query);
		return orcamentos;
	}

	public List<Colaborador> completeColaborador(String query) {
		return viagemService.buscarColaboradores(query);
	}

	public List<Rubrica> completeRubrica(String query) {
		List<Rubrica> allRubrica = new ArrayList<Rubrica>();
		allRubrica = repositorio.getRubricaAutoComplete(query);
		return allRubrica;
	}

	public List<Gestao> completeSuperintendencia(String query) {
		List<Gestao> itens = new ArrayList<>();
		itens = TipoGestao.SUP.getGestao(gestaoRepositorio);
		return itens;
	}

	public List<Gestao> completeCoordenadoria(String query) {
		List<Gestao> itens = new ArrayList<>();
		itens = TipoGestao.COORD.getGestao(gestaoRepositorio);
		return itens;
	}

	public List<Gestao> completeGerencia(String query) {
		List<Gestao> itens = new ArrayList<>();
		itens = TipoGestao.GERENCIA.getGestao(gestaoRepositorio);
		return itens;
	}

	public TipoProjetoOrcamento[] getTiposProjetosOrcamento() {
		return TipoProjetoOrcamento.values();
	}

	public TipoProjetoDespesa[] getTiposProjetoDespesa() {
		return TipoProjetoDespesa.values();
	}

	public TipoParcelamento[] getTiposParcelamento() {
		return TipoParcelamento.values();
	}

	public void limparProjetoDespesa() {
		if (orcamento != null && ((orcamento.getPagaTarifaBancaria() != null && !orcamento.getPagaTarifaBancaria())
				|| (orcamento.getTipoProjetoDespesa() != null
						&& orcamento.getTipoProjetoDespesa().toString().equals("MESMO_PROJETO")))) {
			orcamento.setTipoProjetoDespesa(null);
			projetoDespesa = null;
			orcamento.setProjetoRubrica(null);
		}
	}

	public void limparProjetoDespesaTipo() {
		if (orcamento != null && ((orcamento.getPagaTarifaBancaria() != null && !orcamento.getPagaTarifaBancaria())
				|| (orcamento.getTipoProjetoDespesa() != null
						&& orcamento.getTipoProjetoDespesa().toString().equals("MESMO_PROJETO")))) {
			projetoDespesa = null;
			orcamento.setProjetoRubrica(null);
		}
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public RubricaOrcamento getRubricaOrcamento() {
		return rubricaOrcamento;
	}

	public void setRubricaOrcamento(RubricaOrcamento rubricaOrcamento) {
		this.rubricaOrcamento = rubricaOrcamento;
	}

	public List<RubricaOrcamento> getRubricas() {
		return rubricas;
	}

	public void setRubricas(List<RubricaOrcamento> rubricas) {
		this.rubricas = rubricas;
	}

	public Rubrica getRubrica() {
		return rubrica;
	}

	public void setRubrica(Rubrica rubrica) {
		this.rubrica = rubrica;
	}

	public List<SubComponente> getSubComponentes() {
		return subComponentes;
	}

	public void setSubComponentes(List<SubComponente> subComponentes) {
		this.subComponentes = subComponentes;
	}

	public List<ComponenteClass> getComponentes() {
		return componentes;
	}

	public void setComponentes(List<ComponenteClass> componentes) {
		this.componentes = componentes;
	}

	public ComponenteClass getComponente() {
		return componente;
	}

	public void setComponente(ComponenteClass componente) {
		this.componente = componente;
	}

	public SubComponente getSubComponente() {
		return subComponente;
	}

	public void setSubComponente(SubComponente subComponente) {
		this.subComponente = subComponente;
	}

	public TabView getTabviewOrcamento() {
		return tabviewOrcamento;
	}

	public void setTabviewOrcamento(TabView tabviewOrcamento) {
		this.tabviewOrcamento = tabviewOrcamento;
	}

	public BigDecimal getValorDistribuido() {
		return valorDistribuido;
	}

	public void setValorDistribuido(BigDecimal valorDistribuido) {
		this.valorDistribuido = valorDistribuido;
	}

	public BigDecimal getDiferenca() {
		return diferenca;
	}

	public void setDiferenca(BigDecimal diferenca) {
		this.diferenca = diferenca;
	}

	public Double getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(Double porcentagem) {
		this.porcentagem = porcentagem;
	}

	public List<OrcamentoProjeto> getProjetosDoOrcamento() {
		return projetosDoOrcamento;
	}

	public void setProjetosDoOrcamento(List<OrcamentoProjeto> projetosDoOrcamento) {
		this.projetosDoOrcamento = projetosDoOrcamento;
	}

	public String getSaldoDistribuido() {
		return saldoDistribuido;
	}

	public void setSaldoDistribuido(String saldoDistribuido) {
		this.saldoDistribuido = saldoDistribuido;
	}

	public String getSaldoNaoDistribuido() {
		return saldoNaoDistribuido;
	}

	public void setSaldoNaoDistribuido(String saldoNaoDistribuido) {
		this.saldoNaoDistribuido = saldoNaoDistribuido;
	}

	public List<ContaBancaria> getContas() {
		return contas;
	}

	public void setContas(List<ContaBancaria> contas) {
		this.contas = contas;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public List<Projeto> getListProject() {
		return listProject;
	}

	public void setListProject(List<Projeto> listProject) {
		this.listProject = listProject;
	}

	public List<ContaBancaria> getAllContas() {
		return allContas;
	}

	public void setAllContas(List<ContaBancaria> allContas) {
		this.allContas = allContas;
	}

	public List<LancamentoAuxiliar> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<LancamentoAuxiliar> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public List<Rubrica> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Rubrica> categorias) {
		this.categorias = categorias;
	}

	public DoacaoEfetiva getDoacaoEfetiva() {
		return doacaoEfetiva;
	}

	public void setDoacaoEfetiva(DoacaoEfetiva doacaoEfetiva) {
		this.doacaoEfetiva = doacaoEfetiva;
	}

	public List<LancamentoAuxiliar> getDoacoesEfetivadas() {
		return doacoesEfetivadas;
	}

	public void setDoacoesEfetivadas(List<LancamentoAuxiliar> doacoesEfetivadas) {
		this.doacoesEfetivadas = doacoesEfetivadas;
	}

	public BigDecimal getSaldoRealOrcamento() {
		return saldoRealOrcamento;
	}

	public void setSaldoRealOrcamento(BigDecimal saldoRealOrcamento) {
		this.saldoRealOrcamento = saldoRealOrcamento;
	}

	public BigDecimal getaReceber() {
		return aReceber;
	}

	public void setaReceber(BigDecimal aReceber) {
		this.aReceber = aReceber;
	}

	public BaixaAplicacao getBaixaAplicacao() {
		return baixaAplicacao;
	}

	public void setBaixaAplicacao(BaixaAplicacao baixaAplicacao) {
		this.baixaAplicacao = baixaAplicacao;
	}

	public AplicacaoRecurso getAplicacao() {
		return aplicacao;
	}

	public void setAplicacao(AplicacaoRecurso aplicacao) {
		this.aplicacao = aplicacao;
	}

	public List<PrestacaoOrcamento> getListaPrestacoesOrcamento() {
		return listaPrestacoesOrcamento;
	}

	public void setListaPrestacoesOrcamento(List<PrestacaoOrcamento> listaPrestacoesOrcamento) {
		this.listaPrestacoesOrcamento = listaPrestacoesOrcamento;
	}

	public List<ProjetoRubrica> getListaDeRubricasProjeto() {
		return listaDeRubricasProjeto;
	}

	public void setListaDeRubricasProjeto(List<ProjetoRubrica> listaDeRubricasProjeto) {
		this.listaDeRubricasProjeto = listaDeRubricasProjeto;
	}

	public PrestacaoOrcamento getPrestacaoOrcamento() {
		return prestacaoOrcamento;
	}

	public void setPrestacaoOrcamento(PrestacaoOrcamento prestacaoOrcamento) {
		this.prestacaoOrcamento = prestacaoOrcamento;
	}

	public Projeto getProjetoDespesa() {
		return projetoDespesa;
	}

	public void setProjetoDespesa(Projeto projetoDespesa) {
		this.projetoDespesa = projetoDespesa;
	}

	public Boolean getPreCadastro() {
		return preCadastro;
	}

	public void setPreCadastro(Boolean preCadastro) {
		this.preCadastro = preCadastro;
	}

	public List<ContaBancaria> getContasOverhead() {
		return contasOverhead;
	}

	public void setContasOverhead(List<ContaBancaria> contasOverhead) {
		this.contasOverhead = contasOverhead;
	}

	public TransferenciaOverHead getOverhead() {
		return overhead;
	}

	public void setOverhead(TransferenciaOverHead overhead) {
		this.overhead = overhead;
	}

	public List<TransferenciaOverHead> getTransferencias() {
		return transferencias;
	}

	public void setTransferencias(List<TransferenciaOverHead> transferencias) {
		this.transferencias = transferencias;
	}

	public List<TransferenciaOverHead> getListaOverHead() {
		return listaOverHead;
	}

	public void setListaOverHead(List<TransferenciaOverHead> listaOverHead) {
		this.listaOverHead = listaOverHead;
	}

	public List<TransferenciaOverHead> getListaOverHeadIndireto() {
		return listaOverHeadIndireto;
	}

	public void setListaOverHeadIndireto(List<TransferenciaOverHead> listaOverHeadIndireto) {
		this.listaOverHeadIndireto = listaOverHeadIndireto;
	}

	public List<AlocacaoRendimento> getRendimentos() {
		return rendimentos;
	}

	public void setRendimentos(List<AlocacaoRendimento> rendimentos) {
		this.rendimentos = rendimentos;
	}

	public PieChartModel getPieModel() {
		return pieModel;
	}

	public void setPieModel(PieChartModel pieModel) {
		this.pieModel = pieModel;
	}

//	public HorizontalBarChartModel getHorizontalBarModel() {
//		return horizontalBarModel;
//	}
//
//	public void setHorizontalBarModel(HorizontalBarChartModel horizontalBarModel) {
//		this.horizontalBarModel = horizontalBarModel;
//	}

	public BigDecimal getValorPagoOverhead() {
		return valorPagoOverhead;
	}

	public void setValorPagoOverhead(BigDecimal valorPagoOverhead) {
		this.valorPagoOverhead = valorPagoOverhead;
	}

	public BigDecimal getValorOrcadoOverhead() {
		return valorOrcadoOverhead;
	}

	public void setValorOrcadoOverhead(BigDecimal valorOrcadoOverhead) {
		this.valorOrcadoOverhead = valorOrcadoOverhead;
	}

	public BigDecimal getValorDisponivelOverhead() {
		return valorDisponivelOverhead;
	}

	public void setValorDisponivelOverhead(BigDecimal valorDisponivelOverhead) {
		this.valorDisponivelOverhead = valorDisponivelOverhead;
	}

	public BigDecimal getValorPagoOverheadIndireto() {
		return valorPagoOverheadIndireto;
	}

	public void setValorPagoOverheadIndireto(BigDecimal valorPagoOverheadIndireto) {
		this.valorPagoOverheadIndireto = valorPagoOverheadIndireto;
	}

	public BigDecimal getValorOrcadoOverheadIndireto() {
		return valorOrcadoOverheadIndireto;
	}

	public void setValorOrcadoOverheadIndireto(BigDecimal valorOrcadoOverheadIndireto) {
		this.valorOrcadoOverheadIndireto = valorOrcadoOverheadIndireto;
	}

	public BigDecimal getValorDisponivelOverheadIndireto() {
		return valorDisponivelOverheadIndireto;
	}

	public void setValorDisponivelOverheadIndireto(BigDecimal valorDisponivelOverheadIndireto) {
		this.valorDisponivelOverheadIndireto = valorDisponivelOverheadIndireto;
	}

	public Boolean getContinueCadastro() {
		return continueCadastro;
	}

	public void setContinueCadastro(Boolean continueCadastro) {
		this.continueCadastro = continueCadastro;
	}

	public HorizontalBarChartModel getHbarModel() {
		return hbarModel;
	}

	public void setHbarModel(HorizontalBarChartModel hbarModel) {
		this.hbarModel = hbarModel;
	}

	public BarChartModel getStackedBarModel() {
		return stackedBarModel;
	}

	public void setStackedBarModel(BarChartModel stackedBarModel) {
		this.stackedBarModel = stackedBarModel;
	}

	public BarChartModel getStackedGroupBarModel() {
		return stackedGroupBarModel;
	}

	public void setStackedGroupBarModel(BarChartModel stackedGroupBarModel) {
		this.stackedGroupBarModel = stackedGroupBarModel;
	}

}
