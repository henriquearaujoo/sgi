package managedbean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

import exception.NegocioException;
import model.Acao;
import model.AcaoFonte;
import model.AcaoProduto;
import model.Aprouve;
import model.AprovacaoProjeto;
import model.AtividadeProjeto;
import model.CadeiaProdutiva;
import model.CategoriaDespesaClass;
import model.CategoriaProjeto;
import model.CategoriadeDespesa;
import model.Componente;
import model.ComponenteClass;
import model.ContaBancaria;
import model.Estado;
import model.FontePagadora;
import model.Gestao;
import model.GrupoDeInvestimento;
import model.Indicador;
import model.LancamentoAuxiliar;
import model.Localidade;
import model.MetaPlano;
import model.Metas;
import model.Municipio;
import model.Objetivo;
import model.Orcamento;
import model.OrcamentoProjeto;
import model.PlanoDeTrabalho;
import model.Produto;
import model.Programa;
import model.Projeto;
import model.ProjetoRubrica;
import model.QualifProjeto;
import model.Rubrica;
import model.RubricaOrcamento;
import model.StatusAprovacaoProjeto;
import model.StatusAtividade;
import model.SubComponente;
import model.SubPrograma;
import model.TipoAdministrativoProjeto;
import model.TipoGestao;
import model.TipoLocalidade;
import model.TipoProjeto;
import model.TipoRelatorioProjeto;
import model.UnidadeConservacao;
import model.User;
import model.UserProjeto;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import repositorio.CalculatorRubricaRepositorio;
import repositorio.GestaoRepositorio;
import repositorio.LocalRepositorio;
import repositorio.RubricaRepositorio;
import service.CompraService;
import service.IndicadorService;
import service.MetasService;
import service.ObjetivoService;
import service.OrcamentoService;
import service.PlanoDeTrabalhoService;
import service.ProjetoService;
import service.UsuarioService;
import util.CDILocator;
import util.DataUtil;
import util.Filtro;
import util.ReportUtil;
import util.UsuarioSessao;
import util.Util;

@Named(value = "projetoBean")
@ViewScoped
public class ProjetoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private TreeNode root;
	private TreeNode treeNode;
	private TreeNode selectedNode;

	@Inject
	private CalculatorRubricaRepositorio calculatorRubricaRepositorio;
	private @Inject OrcamentoService orcService;

	private Object object = new Object();

	private List<Projeto> projetos = new ArrayList<Projeto>();
	private List<User> users = new ArrayList<User>();
	private List<AtividadeProjeto> atividades = new ArrayList<AtividadeProjeto>();

	private List<AtividadeProjeto> atividadesPlanejadas = new ArrayList<AtividadeProjeto>();
	private List<AtividadeProjeto> atividadesExecutadas = new ArrayList<AtividadeProjeto>();
	private List<AtividadeProjeto> atividadesExecutadasNaoPlanejadas = new ArrayList<AtividadeProjeto>();
	private List<Orcamento> orcamentos = new ArrayList<>();

	private List<Acao> acoes = new ArrayList<Acao>();
	private List<FontePagadora> fontes = new ArrayList<FontePagadora>();
	private List<CadeiaProdutiva> cadeias;
	private List<Estado> estados;
	private List<Gestao> gestoes;
	private List<AcaoProduto> itens = new ArrayList<>();
	private List<Rubrica> rubricas = new ArrayList<>();
	private List<PlanoDeTrabalho> planos = new ArrayList<>();

	private BigDecimal valorDistribuido;
	private BigDecimal diferenca;

	private float percentExecucaoFisica = 0;

	private TipoRelatorioProjeto tipoRelatorio;

	private Boolean mostrarBotao = true;

	public Boolean vefiricaPeriodo(int mes, AtividadeProjeto atividade) {
		if (atividade != null) {
			if ((mes >= (atividade.getDataInicio().getMonth() + 1))
					&& (mes <= (atividade.getDataFim().getMonth() + 1))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	public List<AcaoProduto> getItens() {
		return itens;
	}

	public void setItens(List<AcaoProduto> itens) {
		this.itens = itens;
	}

	private TabView tabview;

	private TabView tabviewProjeto;

	private String local;
	private String localId;
	private String tipoGestao;

	private Long idEstado;

	private SimpleDateFormat anoString = new SimpleDateFormat("yyyy");

	private Boolean mostraUc;
	private Boolean mostraUnidade;
	private Boolean mostraMunicipio;
	private Boolean mostraSede;
	private Boolean mostraNucleo;

	private PieChartModel pieModel = new PieChartModel();
	private BarChartModel animatedModel = new BarChartModel();
	private BarChartModel CadeiaanimatedModel = new BarChartModel();

	private BarChartModel lancamentoAnimatedModelByAcao = new BarChartModel();
	private BarChartModel lancamentoAnimatedModelByFonte = new BarChartModel();

	private AcaoFonte acaoFonte = new AcaoFonte();

	private @Inject Acao acao;
	private @Inject Projeto projeto;
	private @Inject UserProjeto userProjeto;
	private @Inject User user;
	private @Inject AcaoProduto acaoProduto;
	private @Inject OrcamentoProjeto orcamentoProjeto;
	private @Inject ProjetoRubrica projetoRubrica;
	private @Inject CategoriaProjeto categoriaProjeto;
	private List<CategoriaProjeto> categoriasDeProjeto;
	@Inject
	private ProjetoService projetoService;

	private Double porcentagem;

	private List<ProjetoRubrica> listaDeRubricasProjeto = new ArrayList<>();
	private List<RubricaOrcamento> listaDeRubricasOrcadas = new ArrayList<>();
	private List<SubComponente> subComponentes = new ArrayList<>();
	private List<ComponenteClass> componentesClass = new ArrayList<>();
	private List<OrcamentoProjeto> relacaoDeOrcamentos = new ArrayList<>();

	public AcaoProduto getAcaoProduto() {
		return acaoProduto;
	}

	public void setAcaoProduto(AcaoProduto acaoProduto) {
		this.acaoProduto = acaoProduto;
	}

	private List<Localidade> localidades = new ArrayList<Localidade>();
	private List<User> allUsuario = new ArrayList<User>();
	private List<LancamentoAuxiliar> lancamentos = new ArrayList<>();

	private List<Produto> allProdutos = new ArrayList<Produto>();

	private @Inject ProjetoService gestaoProjeto;// = new GestaoProjeto();
	private @Inject UsuarioSessao usuarioSessao;

	public UsuarioSessao getUsuarioSessao() {
		return usuarioSessao;
	}

	public void setUsuarioSessao(UsuarioSessao usuarioSessao) {
		this.usuarioSessao = usuarioSessao;
	}

	private @Inject RubricaRepositorio rubricaRepositorio;

	private @Inject ComponenteClass componente;
	private @Inject SubComponente subComponente;

	private List<SubPrograma> subsProgramas = new ArrayList<>();

	private int echoYAcao;
	private int echoYFonte;

	private List<UserProjeto> autorizacoes = new ArrayList<UserProjeto>();

	private AtividadeProjeto atividadeProjeto = new AtividadeProjeto();

	private Filtro filtro = new Filtro();
	private String nome;

	// Criado para contemplar autorizaÃ§Ã£o de projetos 25/09/2018 by christophe

	private @Inject Projeto projetoAux;

	public Projeto getProjetoAux() {
		return projetoAux;
	}

	public void setProjetoAux(Projeto projetoAux) {
		this.projetoAux = projetoAux;
	}

	private List<Projeto> listaProjetosParaAutorizar;

	public List<Projeto> getListaProjetosParaAutorizar() {
		return listaProjetosParaAutorizar;
	}

	public void setListaProjetosParaAutorizar(List<Projeto> listaProjetosParaAutorizar) {
		this.listaProjetosParaAutorizar = listaProjetosParaAutorizar;
	}

	public void openDialogByUser(Projeto projeto) {
		projetoAux = projeto;// Usado no dialog de AutorizaÃ§Ã£o de projeto para exibir o nome do projeto.
		try {
			if ((projetoService.getProjetoById(projeto.getId()).getGestao().getColaborador().getId()
					.equals(usuarioSessao.getUsuario().getColaborador().getId()))
					|| (usuarioSessao.getUsuario().getPerfil().getId().equals(new Long(1)))) {
				buscarAutorizacoes();
				openDialog("PF('dlg_liberar_projeto').show();");
			} else if (projetoService.verificaPrivilegioByProjeto(projeto, usuarioSessao.getUsuario())) {
				buscarAutorizacoes();
				openDialog("PF('dlg_liberar_projeto').show();");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
						"VocÃª nÃ£o tem permissÃ£o, Contacte o Administrador."));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "",
					"VocÃª nÃ£o tem permissÃ£o, Contacte o Administrador."));
		}

	}

	// Metodo Usado para Abrir Dialoge carregar informaÃ§Ãµes de projetos by
	// Christophe Alexander 03/10/2018
	private Double porcentagemExecutado = new Double(0);

	public Double getPorcentagemExecutado() {
		return porcentagemExecutado;
	}

	public void setPorcentagemExecutado(Double porcentagemExecutado) {
		this.porcentagemExecutado = porcentagemExecutado;
	}

	private Double execucaoFisica = 0.0;

	public Double getExecucaoFisica() {
		return execucaoFisica;
	}

	public void setExecucaoFisica(Double execucaoFisica) {
		this.execucaoFisica = execucaoFisica;
	}

	public void openDialogDetalhes(Projeto projeto) {
		projetoAux = projetoService.getDetalhesProjetoById(projeto);
		BigDecimal saldo = projetoAux.getValor().subtract(projetoAux.getValorSaldo());
		porcentagemExecutado = Double.parseDouble(saldo.divide(projetoAux.getValor(), 2, RoundingMode.HALF_UP)
				.multiply(new BigDecimal("100")).toString());
		BigDecimal auxEx = new BigDecimal(gestaoProjeto.getQuantidadeAtividade(projeto, StatusAtividade.CLOSED));
		BigDecimal auxTotal = new BigDecimal(projeto.getQuantidadeAtividade() != null ? projeto.getQuantidadeAtividade()
				: (gestaoProjeto.getQuantidadeAtividade(projeto, null)));
		execucaoFisica = (double) 0;
		if (!auxTotal.equals(new BigDecimal(0)))
			execucaoFisica = Double.parseDouble(
					(auxEx.divide(auxTotal, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100))).toString());

		openDialog("PF('dialog_detail').show();");
	}

	public void openDialog(String nomeDialog) {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript(nomeDialog);
	}

	private LineChartModel multiAxisModel = new LineChartModel();

	public LineChartModel getMultiAxisModel() {
		return multiAxisModel;
	}

	public void setMultiAxisModel(LineChartModel multiAxisModel) {
		this.multiAxisModel = multiAxisModel;
	}

	public void setPercentagem(double total, double executato) {
		percentExecucaoFisica = (float) ((executato * 100) / total);
	}

	// end

	private CartesianChartModel gExecFinance;

	private void createChartExecucao() {

		gExecFinance = new BarChartModel();
		List<String> meses = Arrays.asList("JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV",
				"DEZ");

		BarChartSeries executadoPlanejado = new BarChartSeries();
		executadoPlanejado.setLabel("Executadas");

		BarChartSeries executadoNaoPlanejado = new BarChartSeries();
		executadoNaoPlanejado.setLabel("Não Planejadas");

		LineChartSeries planejado = new LineChartSeries();
		planejado.setLabel("Planejadas");
		planejado.setYaxis(AxisType.Y2);
		planejado.setDisableStack(true);

		int qtdAtividadeMes = 0;
		int qtdAtividadeMesN = 0;
		int qtdAtividade = gestaoProjeto.getQuantidadeAtividadeTotalPorProjeto(projeto.getId());// projeto.getQuantidadeAtividade()
		// != null ?
		// projeto.getQuantidadeAtividade()
		// : 0;
		Double evolucaoPlanejada = new Double(0);
		SimpleDateFormat anoString = new SimpleDateFormat("yyyy");
		SimpleDateFormat mesString = new SimpleDateFormat("MM");
		Integer mesAtual = Integer.parseInt(mesString.format(new Date()) + "");
		mesAtual = mesAtual - 1;

		for (int a = 0; a < 12; a++) {

			// planejado.set(meses.get(a), (projeto.getQuantidadeAtividade() / 12) * (a +
			// 1));

			evolucaoPlanejada = evolucaoPlanejada + (new Double(qtdAtividade) / new Double(12));

			planejado.set(meses.get(a), evolucaoPlanejada);

			// Gamby depois mudar por filtro por ano
			// SimpleDateFormat anoString = new SimpleDateFormat("yyyy");

			Integer ano = Integer.parseInt(anoString.format(new Date()) + "");

			if (a <= mesAtual)
				if (projetoService.getQuantidadeAtividadeMes(projeto, StatusAtividade.CLOSED, a + 1, true, ano) > 0) {
					qtdAtividadeMes = qtdAtividadeMes + projetoService.getQuantidadeAtividadeMes(projeto,
							StatusAtividade.CLOSED, a + 1, true, ano);
					executadoPlanejado.set(meses.get(a), qtdAtividadeMes);
				} else {
					executadoPlanejado.set(meses.get(a), qtdAtividadeMes);
					// executadoPlanejado.set(meses.get(a), 0);
				}

			if (a <= mesAtual)
				if (projetoService.getQuantidadeAtividadeMes(projeto, StatusAtividade.CLOSED, a + 1, false, ano) > 0) {
					qtdAtividadeMesN = qtdAtividadeMesN + projetoService.getQuantidadeAtividadeMes(projeto,
							StatusAtividade.CLOSED, a + 1, false, ano);
					executadoNaoPlanejado.set(meses.get(a), qtdAtividadeMesN);
				} else {
					executadoNaoPlanejado.set(meses.get(a), qtdAtividadeMesN);
					// executadoNaoPlanejado.set(meses.get(a), 0);
				}
		}

		setPercentagem(new Double(qtdAtividade), new Double(qtdAtividadeMes));

		gExecFinance.addSeries(planejado);
		gExecFinance.addSeries(executadoPlanejado);
		gExecFinance.addSeries(executadoNaoPlanejado);

		gExecFinance.setTitle("Evolução de Excução Física");
		gExecFinance.setLegendPosition("w");
		// gExecFinance.setMouseoverHighlight(true);
		// gExecFinance.setShowDatatip(true);
		gExecFinance.setShowPointLabels(true);

		// gExecFinance.setShowDatatip(false);
		// gExecFinance.setShowPointLabels(true);

		Axis yAxis = gExecFinance.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(qtdAtividade);
		gExecFinance.getAxes().put(AxisType.Y2, yAxis);

	}

	// TODO MÃ‰TODO PRA BUSCAR QUANTIDADE POR MÃŠS
	public Integer calcularQuantidadePorMesPlanejado(int mes) {
		Integer ano = Integer.parseInt(anoString.format(new Date()) + "");
		return projetoService.getQuantidadeAtividadeMesPlanejado(projeto, mes, ano);
	}

	public Integer calcularQuantidadePorMesNaoPlanejado(int mes) {
		Integer ano = Integer.parseInt(anoString.format(new Date()) + "");
		return projetoService.getQuantidadeAtividadeMesNaoPlanejado(projeto, mes, ano);
	}

	public Integer calcularQuantidadePorMesExecutado(int mes) {
		Integer ano = Integer.parseInt(anoString.format(new Date()) + "");
		return projetoService.getQuantidadeAtividadeMesExecutado(projeto, mes, ano);
	}

	public ProjetoBean() throws NegocioException {

	}

	protected BigDecimal vlEditado;

	public void preparaEdicaoRubricaProjeto() {
		vlEditado = BigDecimal.ZERO;
		vlEditado = projetoRubrica.getValor();
		calculaValor(projetoRubrica.getValor());
	}

	public void calculaValor(BigDecimal valor) {
		if (projeto.getValor() == null)
			return;

		valorDistribuido = BigDecimal.ZERO;
		diferenca = BigDecimal.ZERO;

		Double vlDistAux;

		Double vlOrcamento = projeto.getValor().doubleValue();

		for (ProjetoRubrica rubrica : listaDeRubricasProjeto) {
			valorDistribuido = valorDistribuido.add(rubrica.getValor());
			vlDistAux = rubrica.getValor().doubleValue();
			rubrica.setPorcentagem((vlDistAux / vlOrcamento) * 100);

		}

		Double vlDistribuido = valorDistribuido.doubleValue();

		diferenca = projeto.getValor().subtract(valorDistribuido).add(valor);
		porcentagem = (vlDistribuido / vlOrcamento) * 100;

	}

	public void desativarAcoes() {
		gestaoProjeto.desativarAcoes(projeto.getId());
	}

	public void carregarProjetos() {
		projetos = new ArrayList<Projeto>();
		projetos = gestaoProjeto.getProjetosFiltroPorUsuario(filtro, usuarioSessao.getUsuario());
		projeto = new Projeto();
		projeto = projetos.size() > 0 ? projetos.get(0) : new Projeto();
		// carregarComponentes();
		popularTreeTable();
		// createChart();
	}

	public void initCadastro() {
		// createMultiAxisModel();
		carregarDetalhes();

		if (projeto != null && projeto.getId() != null)
			createChartExecucao();

	}

	public void initListagem() {
		try {
			filtro = new Filtro();
			filtro.setAtivo(true);
			filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
			filtro.setDataFinal(DataUtil.getDataFinal(new Date()));
			fetchProjects();
			fetchPlanes();
			fetchDonations();

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addList(Projeto p) {
		if (listaProjetosSelecionados == null)
			listaProjetosSelecionados = new ArrayList<>();
		boolean contain = listaProjetosSelecionados.contains(p);
		if (contain) {
			listaProjetosSelecionados.remove(p);
		} else {
			listaProjetosSelecionados.add(p);
		}
	}

	public void fetchProjects() {
		projetos = gestaoProjeto.getAllProject(filtro, usuarioSessao.getUsuario());
	}

	public void carregarProjetosMODE01() {
		// projetos = new ArrayList<Projeto>();
		// if (usuarioSessao.getUsuario().getPerfil().getId().equals(new Long(1))
		// || usuarioSessao.getUsuario().getPerfil().getId().equals(new Long(5))) {
		// projetos = gestaoProjeto.getAllProjetoMODE01();
		// } else {
		// projetos = gestaoProjeto.getProjetosFiltroPorUsuarioMODE01(filtro,
		// usuarioSessao.getUsuario());
		// }
		// projeto = new Projeto();
		// projeto = projetos.size() > 0 ? projetos.get(0) : new Projeto();

		// calculaValor();
		// popularTreeTable();
		// createChart();

	}

	public void carregarSubProgramas() {
		subsProgramas = gestaoProjeto.getSubProgramas();
	}

	public void limparProjeto() {
		projeto = new Projeto();
		relacaoDeOrcamentos = new ArrayList<>();
		subComponentes = new ArrayList<>();
	}

	public void fetchPlanes() {
		planos = gestaoProjeto.carregarPlanos(usuarioSessao.getUsuario());
	}

	public void carregarRubricas() {
		rubricas = rubricaRepositorio.getRubricas();
	}

	public void carregasRubricasOrcadas() {
		listaDeRubricasOrcadas = new ArrayList<>();

		for (OrcamentoProjeto orcamentoProjeto : relacaoDeOrcamentos) {
			// if
			// (orcamentoProjeto.getOrcamento().getFonte().getId().longValue()
			// == new Long(3).longValue()) {

			// }

			// filtro.setComponenteClass(projeto.getComponente().getId());
			// filtro.setSubComponente(projeto.getSubComponente().getId());
			listaDeRubricasOrcadas
					.addAll(gestaoProjeto.getRubricasDeOrcamento(orcamentoProjeto.getOrcamento().getId(), filtro));
			filtro = new Filtro();
		}
		// listaDeRubricasOrcadas =
		// gestaoProjeto.getRubricasDeOrcamento(idOrcamento, filtro)
	}

	public void carregarRubricasDeProjeto() {
		listaDeRubricasProjeto = gestaoProjeto.getRubricasDeProjeto(projeto.getId());
	}

	public void salvarCategoriaProjeto() {
		categoriaProjeto.setProjeto(projeto);
		gestaoProjeto.salvarCategoriaDeProjeto(categoriaProjeto);
		categoriaProjeto = new CategoriaProjeto();
		carregarListaDeCategoriasDeProjeto();
	}

	public void carregarListaDeCategoriasDeProjeto() {
		filtro.setProjetoId(projeto.getId());
		categoriasDeProjeto = gestaoProjeto.getListaDeCategoriasDeProjeto(filtro);
	}

	public void fetchDonations() {
		orcamentos = orcService.getOrcamentos(filtro);
	}

	public void salvarProjetoRubrica() {

		if (projetoRubrica.getValor().compareTo(diferenca) > 0) {
			messageSalvamento(
					"Valor ultrapassa teto do projeto, valor dentro do projeto: " + buscarValorTotal(diferenca));
			carregarRubricasDeProjeto();
			calculaValor();
			return;
		}

		if (calculaValorRubrica(projetoRubrica.getValor())) {
			messageSalvamento("Rubrica sem saldo suficiente.");
			carregarRubricasDeProjeto();
			calculaValor();
			return;
		}

		BigDecimal despesas = BigDecimal.ZERO;
		BigDecimal receitas = BigDecimal.ZERO;

		if (projetoRubrica.getId() != null) {
			despesas = calculatorRubricaRepositorio.getValorDespesaRubrica(projetoRubrica.getId());
			receitas = calculatorRubricaRepositorio.getValorReceitaRubrica(projetoRubrica.getId());

			if (projetoRubrica.getValor().compareTo(despesas.subtract(receitas)) < 0) {
				messageSalvamento("Valor inserido para rubrica Ã© inferior as despesas.");
				carregarRubricasDeProjeto();
				calculaValor();
				return;
			}
		}

		vlEditado = BigDecimal.ZERO;

		if (projetoRubrica.getRubricaOrcamento().getComponente() != null) {
			projetoRubrica.setComponente(projetoRubrica.getRubricaOrcamento().getComponente());
		}

		if (projetoRubrica.getRubricaOrcamento().getSubComponente() != null) {
			projetoRubrica.setSubComponente(projetoRubrica.getRubricaOrcamento().getSubComponente());
		}

		projetoRubrica.setProjeto(projeto);
		gestaoProjeto.salvarProjetoRubrica(projetoRubrica);
		projetoRubrica = new ProjetoRubrica();
		carregarRubricasDeProjeto();
		calculaValor();
		messageSalvamento("Salvo com Sucesso!");
	}

	public String buscarValorTotalPlanejado() {
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(totalPLanejadas);
	}

	public String buscarValorTotal(BigDecimal valor) {
		if (valor == null)
			valor = BigDecimal.ZERO;

		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(valor);
	}

	public void carregarRelacaoDeOrcamentos() {
		relacaoDeOrcamentos = gestaoProjeto.getRelacaoDeOrcamentos(projeto.getId());
	}

	public List<OrcamentoProjeto> findAllOrcamentosProjetos() {
		return gestaoProjeto.findAll();
	}

	public void carregarSubComponentes() {
		subComponentes = new ArrayList<>();
		if (projeto.getComponente() != null)
			subComponentes = gestaoProjeto.getSubComponentes(projeto.getComponente().getId());
	}

	public void carregarDetalhes() {
		if (projeto.getId() == null) {
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, c.get(Calendar.YEAR));
			c.set(Calendar.MONTH, 01);

			c.set(Calendar.DAY_OF_MONTH, 22);
			projeto.setDataInicio(c.getTime());

			Calendar c2 = Calendar.getInstance();
			c2.set(Calendar.YEAR, 2019);
			c2.set(Calendar.MONTH, 12);
			c2.set(Calendar.DAY_OF_MONTH, 31);
			projeto.setDataFinal(c2.getTime());
		}
		carregarComponentes();
		carregarSubComponentes();
		// carregarRelacaoDeOrcamentos();
		findObjetivoByIdProjeto();
		fetchPlanes();
		carregarCadeias();
		carregarSubProgramas();
		// carregarQualifs();

	}

	private List<QualifProjeto> qualificacoes = new ArrayList<>();

	public void carregarQualifs() {
		qualificacoes = new ArrayList<>();
		qualificacoes = gestaoProjeto.carregarQualificacoes();
	}

	public void carregarComponentes() {
		componentesClass = gestaoProjeto.getComponentes();
	}

	public void carregarFiltroRelatorio() {

		// switch (tipoRelatorio) {
		// case ORCADO_REALIZADO:
		// carregarDialogOrcadoRealizado();
		// break;
		// default:
		// break;
		// }
	}

	public void carregarExtratoProjeto() {
		lancamentos = new ArrayList<>();
		filtro.setProjetoId(projeto.getId());
		lancamentos = gestaoProjeto.getExtratoCtrlProjeto(filtro);
		// service.getPagamentosPE(filtro);
	}

	public void limpar() {
		filtro = new Filtro();
	}

	public String buscarValorTotalSaida() {
		BigDecimal valorTotal = BigDecimal.ZERO;
		if (lancamentos != null)
			if (!lancamentos.isEmpty()) {
				valorTotal = lancamentos.get(0).getTotalSaida();
			}
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(valorTotal);
	}

	public String buscarValorTotalEntrada() {
		BigDecimal valorTotal = BigDecimal.ZERO;
		if (lancamentos != null)
			if (!lancamentos.isEmpty()) {
				valorTotal = lancamentos.get(0).getTotalEntrada();
			}
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(valorTotal);
	}

	public String buscarValorSaldo() {
		BigDecimal valorTotal = BigDecimal.ZERO;
		if (lancamentos != null)
			if (!lancamentos.isEmpty()) {
				valorTotal = projeto.getValor().add(lancamentos.get(0).getTotalEntrada())
						.subtract(lancamentos.get(0).getTotalSaida());
			}
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(valorTotal);
	}

	// public void carregarDialogOrcadoRealizado() {
	// Map<String, Object> option = new HashMap<>();
	// option.put("modal", true);
	// option.put("resizable", false);
	// option.put("contentHeight", 470);
	// RequestContext.getCurrentInstance().openDialog("filterOrcadoRealizado",
	// option, null);
	// }

	public void salvarOrcamentoProjeto() {
		if (projeto.getId() == null) {
			messageSalvamento("Primeiro salve as informações gerais do projeto.");
			return;
		}

		projeto = gestaoProjeto.getProjetoById(projeto.getId());
		orcamentoProjeto.setStatus("Em Análise");
		orcamentoProjeto.setProjeto(projeto);
		orcamentoProjeto.setDataInsercao(new Date());
		gestaoProjeto.salvarOrcamentoProjeto(orcamentoProjeto);
		orcamentoProjeto = new OrcamentoProjeto();
		messageSalvamento("Salvo!");
		carregarRelacaoDeOrcamentos();
		carregasRubricasOrcadas();
	}

	public void removerRelacaoOrcamento() {
		relacaoDeOrcamentos.remove(orcamentoProjeto);
		gestaoProjeto.removerRelacaoOrcamento(orcamentoProjeto);
		orcamentoProjeto = new OrcamentoProjeto();
		carregarRelacaoDeOrcamentos();
	}

	public void removerProjetoRubrica() {
		gestaoProjeto.removerProjetoRubrica(projetoRubrica);
		carregarRubricasDeProjeto();
		calculaValor();
	}

	public void carregarItens() {
		acao.setItens(new ArrayList<>());
		acao.setItens(gestaoProjeto.getItensByAcao(acao));
	}

	public void getTodasAsAcoes() {
		acoes = gestaoProjeto.getTodasAcoes();
	}

	public void buscarAutorizacoes() {
		autorizacoes = gestaoProjeto.getUsuariosProjetos(projetoAux);
		userProjeto.setProjeto(projetoAux);
	}

	public void salvarUsuarioNoProjeto() {
		userProjeto.setProjeto(projetoAux);
		if (gestaoProjeto.salvarUsuarioNoProjeto(userProjeto)) {
			userProjeto = new UserProjeto();
			buscarAutorizacoes();
		}
	}

	public List<Produto> completeProduto(String query) {
		allProdutos = new ArrayList<Produto>();
		allProdutos = gestaoProjeto.getProdutos(query);
		return allProdutos;
	}

	public List<Projeto> completeProjetos(String query) {
		return gestaoProjeto.getProjetoAutoCompleteMODE01(query);
	}

	public List<ContaBancaria> completeConta(String query) {
		return gestaoProjeto.getAllConta(query);
	}

	public List<Orcamento> completeOrcamento(String query) {
		return gestaoProjeto.getOrcamentoAutoComplete(query);
	}

	public List<Projeto> completeProjeto(String query) {
		return gestaoProjeto.getProjetosAutoCompleteMode01(query);
	}

	public List<Gestao> completeGestao(String query) {
		return gestaoProjeto.getGestaoAutoComplete(query);
	}

	public List<CategoriaDespesaClass> completeCategoria(String query) {
		return gestaoProjeto.getCategoriaAutoComplete(query);
	}

	public void excluirUsuarioDoProjeto() {
		gestaoProjeto.excluirUsuarioDoProjeto(userProjeto);
		buscarAutorizacoes();
	}

	public void buscarLancamentoPorAcao() {
		filtro = new Filtro();
		filtro.setAcaoId(acao.getId());
		buscarLancamentoReport();
	}

	@Inject
	private CompraService compraService;

	public boolean verificarPrivilegioSalvamento() {
		Aprouve ap = new Aprouve();
		ap.setSigla("SAVE_PROJ");
		ap.setUsuario(usuarioSessao.getUsuario());
		return compraService.findAprouve(ap);
		// return true;
	}

	public void buscarLancamentoPorProjeto() {
		filtro = new Filtro();
		filtro.setProjetoId(projeto.getId());
		buscarLancamentoReport();
		chartLancamentos();
	}

	public void chartLancamentos() {
		createChartLancamento();
	}

	public void buscarLancamentoReport() {
		// filtro.setAcaoId(acao.getId());
		lancamentos = gestaoProjeto.getLancamentosReport(filtro);
	}

	public void setEchoYbYAcao() {
		Axis yAxis = lancamentoAnimatedModelByAcao.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(echoYAcao);
	}

	public void setEchoYbYFonte() {
		Axis yAxis = lancamentoAnimatedModelByFonte.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(echoYFonte);
	}

	public CategoriadeDespesa[] categoriasDespesas() {
		return CategoriadeDespesa.values();
	}

	public void excluirUsuarioDoPracoesojeto() {

	}

	public void editarUsuarioNoProjeto() {

	}

	public void addItem() {

		if (acao.getItens().stream()
				.filter(s -> s.getProduto().getId().longValue() == acaoProduto.getProduto().getId().longValue())
				.findAny().isPresent()) {
			acao.getItens().remove(acao.getItens().stream()
					.filter(s -> s.getProduto().getId().longValue() == acaoProduto.getProduto().getId().longValue())
					.findAny().get());
		}

		acaoProduto.setAcao(acao);
		acao.getItens().add(acaoProduto);
		acaoProduto = new AcaoProduto();
	}

	public void removerItem() {
		acao.getItens()
				.remove(acao.getItens().stream()
						.filter(s -> s.getProduto().getId().longValue() == acaoProduto.getProduto().getId().longValue())
						.findAny().get());

		acaoProduto = new AcaoProduto();

	}

	public void removerCategoriaProjeto() {
		gestaoProjeto.removerCategoriaProjeto(categoriaProjeto);
		categoriaProjeto = new CategoriaProjeto();
		carregarListaDeCategoriasDeProjeto();
	}

	public List<User> completeUsuario(String query) {
		allUsuario = new ArrayList<User>();
		allUsuario = gestaoProjeto.getUsuario(query);
		return allUsuario;
	}

	public void createChart() {

		pieModel = new PieChartModel();

		pieModel.set("Pendente", 1);
		pieModel.set("Fazendo", 1);
		pieModel.set("Concluído", 1);

		pieModel.setTitle("Atividades");
		pieModel.setLegendPosition("w");

		animatedModel = new BarChartModel();

		lancamentoAnimatedModelByAcao = new BarChartModel();
		ChartSeries investimento = new ChartSeries();
		investimento.setLabel("Investimentos");
		investimento.set("UND", 1);
		lancamentoAnimatedModelByAcao.addSeries(investimento);

		lancamentoAnimatedModelByFonte = new BarChartModel();
		ChartSeries investimentoFonte = new ChartSeries();
		investimentoFonte.setLabel("Investimentos");
		investimentoFonte.set("UND", 1);
		lancamentoAnimatedModelByFonte.addSeries(investimentoFonte);

		ChartSeries pendente = new ChartSeries();
		pendente.setLabel("Pendente");
		pendente.set("2016", 1);

		ChartSeries fazendo = new ChartSeries();
		fazendo.setLabel("Fazendo");
		fazendo.set("2016", 1);

		ChartSeries concluido = new ChartSeries();
		fazendo.setLabel("Concluído");
		fazendo.set("2016", 1);

		animatedModel.addSeries(pendente);
		animatedModel.addSeries(fazendo);
		animatedModel.addSeries(concluido);

	}

	public void novoProjeto() {
		projeto = new Projeto();
		local = "";
		tipoGestao = "";
	}

	private void createPieModels() {
		createPieModel();
		createBarChartModel();
	}

	private void createPieModel() {

		pieModel = new PieChartModel();
		if (projeto.getId() != null) {
			projeto = gestaoProjeto.getProjetoById(projeto.getId());
			pieModel.set("Pendente", gestaoProjeto.getQuantidadeAtividade(projeto, StatusAtividade.OPEN));
			pieModel.set("Fazendo", gestaoProjeto.getQuantidadeAtividade(projeto, StatusAtividade.BUILDING));
			pieModel.set("Concluído", gestaoProjeto.getQuantidadeAtividade(projeto, StatusAtividade.CLOSED));
		} else {
			pieModel.set("Pendente", 0);
			pieModel.set("Fazendo", 0);
			pieModel.set("Concluído", 0);

		}
		pieModel.setTitle("Pie");
		pieModel.setLegendPosition("w");
		pieModel.setShowDataLabels(true);

	}

	private void createChartLancamento() {
		// lancamentoAnimatedModelByAcao = initBarModelLancamento();
		initBarModelLancamento();
		configChartByAcao();
		configChartByFonte();

	}

	public void configChartByAcao() {
		lancamentoAnimatedModelByAcao.setTitle("Bar");
		// lancamentoAnimatedModelByAcao.setAnimate(true);
		lancamentoAnimatedModelByAcao.setLegendPosition("ne");
		lancamentoAnimatedModelByAcao.setShowPointLabels(true);
		Axis yAxis = lancamentoAnimatedModelByAcao.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(100000);
	}

	public void configChartByFonte() {
		lancamentoAnimatedModelByFonte.setTitle("Fontes de recurso");
		lancamentoAnimatedModelByFonte.setAnimate(true);
		lancamentoAnimatedModelByFonte.setLegendPosition("ne");
		lancamentoAnimatedModelByFonte.setShowPointLabels(true);
		Axis yAxis = lancamentoAnimatedModelByFonte.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(100000);
	}

	private void initBarModelLancamento() {

		BarChartModel model = new BarChartModel();
		ChartSeries investimento = new ChartSeries();
		BigDecimal total = BigDecimal.ZERO;
		Map<String, BigDecimal> mapaDeValores = new HashMap<>();
		if (projeto.getId() != null) {
			projeto = gestaoProjeto.getProjetoById(projeto.getId());
			investimento.setLabel("Investimento");
			for (LancamentoAuxiliar lc : lancamentos) {
				total = mapaDeValores.get(lc.getCodigo()) != null ? mapaDeValores.get(lc.getCodigo()) : BigDecimal.ZERO;
				mapaDeValores.put(lc.getCodigo(), total.add(lc.getValorPagoAcao()));
				investimento.set(lc.getCodigo(), mapaDeValores.get(lc.getCodigo()));
			}
			//
		} else {
			investimento.setLabel("");
			investimento.set("2016", 0);
		}

		model.addSeries(investimento);
		lancamentoAnimatedModelByAcao = model;

		// Modelo para chart com valores relacionados a fontes de recursos
		model = new BarChartModel();
		ChartSeries investimentoFonte = new ChartSeries();
		total = BigDecimal.ZERO;
		mapaDeValores = new HashMap<>();

		if (projeto.getId() != null) {
			projeto = gestaoProjeto.getProjetoById(projeto.getId());
			investimentoFonte.setLabel("Investimento");
			for (LancamentoAuxiliar lc : lancamentos) {
				total = mapaDeValores.get(lc.getFonte()) != null ? mapaDeValores.get(lc.getFonte()) : BigDecimal.ZERO;
				mapaDeValores.put(lc.getFonte(), total.add(lc.getValorPagoAcao()));
				investimentoFonte.set(lc.getFonte(), mapaDeValores.get(lc.getFonte()));
			}
		} else {
			investimentoFonte.setLabel("");
			investimentoFonte.set("2016", 0);
		}

		model.addSeries(investimentoFonte);
		lancamentoAnimatedModelByFonte = model;

	}

	private void createBarChartModel() {
		animatedModel = initBarModel();
		animatedModel.setTitle("Bar");
		// animatedModel.setAnimate(true);
		animatedModel.setLegendPosition("ne");
		animatedModel.setShowPointLabels(true);
		Axis yAxis = animatedModel.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(15);

	}

	private BarChartModel initBarModel() {
		BarChartModel model = new BarChartModel();
		ChartSeries pendente = new ChartSeries();
		ChartSeries concluido = new ChartSeries();
		ChartSeries fazendo = new ChartSeries();

		if (projeto.getId() != null) {
			projeto = gestaoProjeto.getProjetoById(projeto.getId());
			pendente.setLabel("Pendente");
			pendente.set("2016", gestaoProjeto.getQuantidadeAtividade(projeto, StatusAtividade.OPEN));
			fazendo.setLabel("Fazendo");
			fazendo.set("2016", gestaoProjeto.getQuantidadeAtividade(projeto, StatusAtividade.BUILDING));
			concluido.setLabel("Concluído");
			concluido.set("2016", gestaoProjeto.getQuantidadeAtividade(projeto, StatusAtividade.CLOSED));
		} else {
			pendente.setLabel("Pendente");
			pendente.set("2016", 0);
			fazendo.setLabel("Fazendo");
			fazendo.set("2016", 0);
			concluido.setLabel("Concluído");
			concluido.set("2016", 0);
		}
		model.addSeries(pendente);
		model.addSeries(fazendo);
		model.addSeries(concluido);
		return model;
	}

	public Boolean verificarPrivilegioSalvar() {
		// if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("compra")
		// || usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
		// || usuarioSessao.getUsuario().getPerfil().getDescricao().equals("pbf")
		// ||
		// usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro")) {
		// return true;
		// } else {
		// return false;
		// }

		return true;
	}

	public void messageSalvamento(String txt) {

		// StringBuilder msg = new StringBuilder("Projeto salvo com sucesso");
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", txt);

		FacesContext.getCurrentInstance().addMessage(null, message);
		;
	}

	// public void showMessageLiberarProjeto() {
	//
	// StringBuilder msg = new StringBuilder(
	// "Olï¿½ " + usuarioSessao.getNomeUsuario() + " tudo bom? Espero que
	// sim!!!\n");
	// msg.append("Esse espaÃ§o estÃ¡ em desenvolvimento e serÃ¡ lanÃ§ado em
	// breve\n");
	// msg.append("pra nï¿½o te deixar curioso vou te contar para que ele esse
	// espaï¿½o serve\n");
	// msg.append("apï¿½s criar seus projetos sï¿½ vocï¿½ terï¿½ acesso a ele e as
	// aï¿½ï¿½es correspondente ao projeto\n");
	// msg.append("esse espaï¿½o ï¿½ onde vocï¿½ vai liberar acesso ao seu projeto
	// para o usuï¿½rio que quiser\n");
	// msg.append("vamos ter 3 tipos de acesso, sï¿½o eles:\n");
	// msg.append("'Ler' onde seu projeto vai poder ser lido/visto mas nï¿½o poderar
	// ser alterado\n");
	// msg.append(
	// "'Ler e Acrescentar' onde o usuario vai poder ler/ver e acrescentar ao
	// projeto sem poder deletar ou alterar aï¿½ï¿½es ou atividades\n");
	// msg.append("'Controle total' onde o usuario vai ter todas as permissï¿½es
	// Ler/Gravar/Modificar.");
	// msg.append("\n\n");
	// msg.append(
	// "Bom, ï¿½ isso assim que o mï¿½dulo estiver pronto vocï¿½ saberï¿½, obrigado
	// pela compreensï¿½o, nï¿½s da TI somos gratos em atende-los!!!");
	// FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
	// "Autorizaï¿½ï¿½es", msg.toString());
	//
	// RequestContext.getCurrentInstance().showMessageInDialog(message);
	// ;
	// }

	public void iniciarNovaAcao() {
		acao = new Acao();
		carregarCadeias();
		carregarRubricas();
		local = "";
	}

	public void iniciarNovaAtividade() {
		atividadeProjeto = new AtividadeProjeto();
		findMetasByIdProjeto();
	}

	public void onCadeiaChange() {
		cadeias = new ArrayList<CadeiaProdutiva>();
		if (acao.getComponente() != null)
			cadeias = gestaoProjeto.getCadeias(acao.getComponente());
	}

	public void carregarCadeias() {
		cadeias = new ArrayList<CadeiaProdutiva>();
		cadeias = gestaoProjeto.getCadeias();
	}

	public void onEstadoChange() {
		localidades = gestaoProjeto.getMunicipioByEstado(idEstado);
		idEstado = new Long(0);
	}

	public void onChangeProjetoAtividade() {
		acoes = gestaoProjeto.getAcoes(projeto);
		projeto.setAcoes(new ArrayList<>());
		projeto.setAcoes(acoes);
	}

	public void onLocalChange() {
		local = "";
		if (acao.getTipoLocalidade() != null) {
			local = acao.getTipoLocalidade().getNome();
			if (local.equals("Municipio")) {
				estados = new ArrayList<>();
				estados = gestaoProjeto.getEstados(new Filtro());
				localidades = new ArrayList<>();
				idEstado = new Long(0);
			} else {
				LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
				localidades = acao.getTipoLocalidade().getLocalidade(repo);
			}
		}
	}

	public void carregarAtividades() {
		atividades = new ArrayList<>();
		atividades = gestaoProjeto.getAtividades(projeto);
	}

	private BigDecimal totalPLanejadas = BigDecimal.ZERO;
	private BigDecimal totalExecutadas = BigDecimal.ZERO;
	private BigDecimal totalNaoPlanejadas = BigDecimal.ZERO;
	private int qtdPlanejadas = 0;
	private int qtdNaoPlanejadas = 0;
	private int qtdExecutadaPlanejadas = 0;

	public void carregarCronograma() {
		atividadesPlanejadas = new ArrayList<>();
		atividadesPlanejadas = gestaoProjeto.getCronogramaPlanejado(projeto);
		atividadesExecutadas = new ArrayList<>();
		atividadesExecutadas = gestaoProjeto.getCronogramaExecutado(projeto);
		atividadesExecutadasNaoPlanejadas = new ArrayList<>();
		atividadesExecutadasNaoPlanejadas = gestaoProjeto.getCronogramaExecutadoNaoPlanejado(projeto);
		carregarDetalhesPlanejadas();
		carregarDetalhesExecutadas();
		carregarDetalhesNaoPlanejadas();

	}

	public void carregarDetalhesPlanejadas() {
		totalPLanejadas = gestaoProjeto.getTotalAtividadeByProjeto(projeto.getId(), null);
		qtdPlanejadas = gestaoProjeto.getQuantidadeAtividadeTotalPorProjeto(projeto.getId());
	}

	public boolean verificarTotalDeAtividades() {
		BigDecimal total = gestaoProjeto.getTotalAtividadeByProjeto(projeto.getId(),
				atividadeProjeto.getId() != null ? atividadeProjeto.getId() : null);
		total = total.add(atividadeProjeto.getValor());
		return total.compareTo(projeto.getValor()) > 0;
	}

	public void carregarDetalhesExecutadas() {
		totalExecutadas = gestaoProjeto.getTotalAtividadeExecutadaByProjeto(projeto.getId());
		qtdExecutadaPlanejadas = gestaoProjeto.getQuantidadeAtividadeTotalPorProjetoExecutado(projeto.getId());
	}

	public void carregarDetalhesNaoPlanejadas() {
		totalNaoPlanejadas = gestaoProjeto.getTotalAtividadeByProjetoNaoPlanejado(projeto.getId());
		qtdNaoPlanejadas = gestaoProjeto.getQuantidadeAtividadeTotalPorProjetoNaoPlanejada(projeto.getId());
	}

	public void onLocalChangeForProject() {
		local = "";
		if (projeto.getTipoLocalidade() != null) {
			local = projeto.getTipoLocalidade().getNome();

			if (local.equals("Municipio")) {
				estados = new ArrayList<>();
				estados = gestaoProjeto.getEstados(new Filtro());
				localidades = new ArrayList<>();
				idEstado = new Long(0);
			} else {
				LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
				localidades = projeto.getTipoLocalidade().getLocalidade(repo);
			}
		}
	}

	public void onRowEdit(RowEditEvent event) {
		AtividadeProjeto atv = ((AtividadeProjeto) event.getObject());
		atividadeProjeto = gestaoProjeto.getAtividadeById(atv.getId());

		atividadeProjeto.setJan(atv.getJan());
		atividadeProjeto.setFev(atv.getFev());
		atividadeProjeto.setMar(atv.getMar());
		atividadeProjeto.setAbr(atv.getAbr());
		atividadeProjeto.setMai(atv.getMai());
		atividadeProjeto.setJun(atv.getJun());
		atividadeProjeto.setJul(atv.getJul());
		atividadeProjeto.setAgo(atv.getAgo());
		atividadeProjeto.setSet(atv.getSet());
		atividadeProjeto.setOut(atv.getOut());
		atividadeProjeto.setNov(atv.getNov());
		atividadeProjeto.setDez(atv.getDez());
		gestaoProjeto.salvarAtividade(atividadeProjeto);
		carregarDetalhesPlanejadas();
		// salvarAtividadeNoGrant();
		addMessage("", "Salvo com sucesso!", FacesMessage.SEVERITY_INFO);
	}

	public void onRowCancel(RowEditEvent event) {
		atividadeProjeto = new AtividadeProjeto();
	}

	public void onRowEditExec(RowEditEvent event) {
		AtividadeProjeto atv = ((AtividadeProjeto) event.getObject());
		atividadeProjeto = gestaoProjeto.getAtividadeById(atv.getId());

		atividadeProjeto.setJanExec(atv.getJanExec());
		atividadeProjeto.setFevExec(atv.getFevExec());
		atividadeProjeto.setMarExec(atv.getMarExec());
		atividadeProjeto.setAbrExec(atv.getAbrExec());
		atividadeProjeto.setMaiExec(atv.getMaiExec());
		atividadeProjeto.setJunExec(atv.getJunExec());
		atividadeProjeto.setJulExec(atv.getJulExec());
		atividadeProjeto.setAgoExec(atv.getAgoExec());
		atividadeProjeto.setSetExec(atv.getSetExec());
		atividadeProjeto.setOutExec(atv.getOutExec());
		atividadeProjeto.setNovExec(atv.getNovExec());
		atividadeProjeto.setDezExec(atv.getDezExec());
		gestaoProjeto.salvarAtividade(atividadeProjeto);
		carregarDetalhesExecutadas();
		// TODO : EXECUCAO
		// salvarAtividadeNoGrant();
		addMessage("", "Salvo com sucesso!", FacesMessage.SEVERITY_INFO);
	}

	public void onRowCancelExec(RowEditEvent event) {
		atividadeProjeto = new AtividadeProjeto();
	}

	public void onCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if (newValue != null && !newValue.equals(oldValue)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed",
					"Old: " + oldValue + ", New:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void onTipoGestaoChange() {
		tipoGestao = "";
		if (projeto.getTipoGestao() != null) {
			tipoGestao = projeto.getTipoGestao().getNome();
			GestaoRepositorio repo = CDILocator.getBean(GestaoRepositorio.class);
			gestoes = new ArrayList<>();
			gestoes = projeto.getTipoGestao().getGestao(repo);
		}
	}

	public void inserirNovaFonte() {
		if (acao.getAcaoFontes().stream().filter(s -> s.getFonte().getNome().equals(acaoFonte.getFonte().getNome()))
				.findAny().isPresent()) {
			acao.getAcaoFontes().remove(acao.getAcaoFontes().stream()
					.filter(s -> s.getFonte().getNome().equals(acaoFonte.getFonte().getNome())).findAny().get());
		}
		acao.getAcaoFontes().add(acaoFonte);
		acaoFonte.setAcao(acao);
		acaoFonte = new AcaoFonte();
	}

	public void removerFonte() {
		acao.getAcaoFontes().remove(acao.getAcaoFontes().stream()
				.filter(s -> s.getFonte().getNome().equals(acaoFonte.getFonte().getNome())).findAny().get());
	}

	// public void aceitarAtividade() {
	//
	// atividades.stream().filter(s -> s.getId().longValue() ==
	// atividadeProjeto.getId()).findAny().get().setAceito(true);
	// atividadeAcao.setAceito(true);
	// gestaoProjeto.salvarAtividade(atividadeAcao);
	// // carregarAtividades();
	// }
	//
	// public void recusarAtividade() {
	// atividades.stream().filter(s -> s.getId().longValue() ==
	// atividadeAcao.getId()).findAny().get()
	// .setAceito(false);
	// atividadeAcao.setStatus(StatusAtividade.OPEN);
	// atividadeAcao.setAceito(false);
	// gestaoProjeto.salvarAtividade(atividadeAcao);
	// // carregarAtividades();
	// }

	public Boolean calculaValorRubrica(BigDecimal valor) {

		if (vlEditado == null) {
			vlEditado = BigDecimal.ZERO;
		}

		if (valor.compareTo(vlEditado) < 0) {
			return false;
		}

		List<ProjetoRubrica> listRubricas = gestaoProjeto
				.getProjetoRubricaByOrcamentoRubrica(projetoRubrica.getRubricaOrcamento().getId());
		BigDecimal vlExecutato = BigDecimal.ZERO;
		BigDecimal vlOrcadoRubrica = BigDecimal.ZERO;

		for (ProjetoRubrica projetoRubrica : listRubricas) {
			vlExecutato = vlExecutato.add(projetoRubrica.getValor());
		}

		if (vlEditado == null) {
			vlEditado = BigDecimal.ZERO;
		}

		if (vlExecutato == null) {
			vlExecutato = BigDecimal.ZERO;
		}

		vlExecutato = vlExecutato.add(projetoRubrica.getValor()).subtract(vlEditado);
		vlOrcadoRubrica = projetoRubrica.getRubricaOrcamento().getValor();

		// vlOrcadoRubrica = vlOrcadoRubrica.subtract(vlEditado);

		if (vlOrcadoRubrica.compareTo(vlExecutato) < 0) {
			return true;
		} else {
			return false;
		}
	}

	public void removerAcao() {
		if (acao.getId() == null) {
			addMessage("", "ação inválida", FacesMessage.SEVERITY_ERROR);
		} else {
			gestaoProjeto.removerAcao(acao);
			acao = new Acao();
		}

		carregarAcoes();
	}

	public void removerAtividade() {
		if (atividadeProjeto.getId() == null) {
			addMessage("", "Atividade inválida", FacesMessage.SEVERITY_ERROR);
		} else {
			gestaoProjeto.removerAtividade(atividadeProjeto);
			atividadeProjeto = new AtividadeProjeto();
			carregarAtividades();
			createChartExecucao();
			addMessage("", "Excluído com sucesso", FacesMessage.SEVERITY_INFO);
		}
	}

	public void salvar() {
		setarTipoGestaoInProjeto();
		setarTipoLocalidadeInProjeto();
		projeto.setValor(BigDecimal.ZERO);
		projeto.setValorPrevio(BigDecimal.ZERO);
		gestaoProjeto.salvar(projeto, usuarioSessao.getUsuario());
		carregarProjetos();
		// messageSalvamento("Projeto salvo com sucesso");
	}

	public void salvarMODE01() {

		// if (projeto.getPlanoDeTrabalho() != null) {
		// if (faltaPreencher()) {
		// return;
		// }
		// }

		setarTipoGestaoInProjeto();
		// setarTipoLocalidadeInProjeto();
		projeto.setVersionProjeto("mode01");
		projeto.setStatusAprovacao(StatusAprovacaoProjeto.EM_APROVACAO);
		projeto = gestaoProjeto.salvarMODE01(projeto, usuarioSessao.getUsuario());
		// createChartExecucao();
		System.out.println(projeto.getId());
		// messageSalvamento("Projeto salvo com sucesso");
		// carregarProjetos();
	}

	public void remover() {
		gestaoProjeto.remover(projeto);
		carregarProjetosMODE01();
	}

	public void editarAcao() {

		carregarCadeias();
		carregarRubricas();
		// TODO: aÃ§Ã£o
		local = "";

		acao = (Acao) object;
		acao = gestaoProjeto.getAcao(acao);
		acao.setAcaoFontes(new ArrayList<AcaoFonte>());
		acao.setAcaoFontes(gestaoProjeto.getAcaoFonte(acao));
		// mostrarBotao = true;

		if (acao.getLocalidade() instanceof Municipio) {
			estados = gestaoProjeto.getEstados(new Filtro());
			idEstado = ((Municipio) acao.getLocalidade()).getEstado().getId();
			localidades = gestaoProjeto.getMunicipioByEstado(idEstado);
			local = acao.getTipoLocalidade().getNome();

		} else if (acao.getLocalidade() != null) {
			LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
			localidades = acao.getTipoLocalidade().getLocalidade(repo);
			local = acao.getTipoLocalidade().getNome();
		}
	}

	public void editarStatusAtividade() {
		atividadeProjeto = (AtividadeProjeto) object;
		atividadeProjeto = gestaoProjeto.getAtividadeById(atividadeProjeto.getId());
	}

	public void editarStatusAtividadePageAtividade() {
		atividadeProjeto = gestaoProjeto.getAtividadeById(atividadeProjeto.getId());
	}

	public void editarAtividade() {

		Long id = atividadeProjeto.getId();
		atividadeProjeto = new AtividadeProjeto();
		atividadeProjeto = gestaoProjeto.getAtividadeById(id);
		findMetasByIdProjeto();

	}

	public void editar() {

		local = "";

		projeto = gestaoProjeto.getProjetoById(projeto.getId());

		if (projeto.getLocalidade() instanceof Municipio) {
			estados = gestaoProjeto.getEstados(new Filtro());
			idEstado = ((Municipio) projeto.getLocalidade()).getEstado().getId();
			localidades = gestaoProjeto.getMunicipioByEstado(idEstado);
			local = projeto.getTipoLocalidade().getNome();
		} else if (projeto.getLocalidade() != null) {
			LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
			localidades = projeto.getTipoLocalidade().getLocalidade(repo);
			local = projeto.getTipoLocalidade().getNome();
		}

		tipoGestao = projeto.getTipoGestao().getNome();
		GestaoRepositorio repo = CDILocator.getBean(GestaoRepositorio.class);
		gestoes = new ArrayList<>();
		gestoes = projeto.getTipoGestao().getGestao(repo);

	}

	public void salvarAcao() {

		setarTipoLocalidadeInAcao();
		if (gestaoProjeto.salvarAcao(acao)) {
			projeto = acao.getProjeto();
			acao = new Acao();
			acao.setProjeto(new Projeto());
			popularTreeTable();
		}
	}

	public void salvarAcaoMODE01() {
		setarTipoLocalidadeInAcao();
		acao.setProjeto(projeto);
		if (gestaoProjeto.salvarAcao(acao)) {
			acao = new Acao();
			carregarAcoes();
			messageSalvamento("ação salva com sucesso!");
		}
	}

	public void setarTipoLocalidadeInAcao() {
		String ttype = acao.getLocalidade().getType();
		switch (ttype) {
		case "mun":
			acao.setTipoLocalidade(TipoLocalidade.MUNICIPIO);
			break;
		case "uc":
			acao.setTipoLocalidade(TipoLocalidade.UC);
			break;
		case "com":
			acao.setTipoLocalidade(TipoLocalidade.COMUNIDADE);
			break;
		case "sede":
			acao.setTipoLocalidade(TipoLocalidade.SEDE);
			break;
		default:
			acao.setTipoLocalidade(TipoLocalidade.NUCLEO);
			break;
		}
	}

	public void setarTipoLocalidadeInProjeto() {
		String ttype = projeto.getLocalidade().getType();
		switch (ttype) {
		case "mun":
			projeto.setTipoLocalidade(TipoLocalidade.MUNICIPIO);
			break;
		case "uc":
			projeto.setTipoLocalidade(TipoLocalidade.UC);
			break;
		case "com":
			projeto.setTipoLocalidade(TipoLocalidade.COMUNIDADE);
			break;
		case "sede":
			projeto.setTipoLocalidade(TipoLocalidade.SEDE);
			break;
		default:
			projeto.setTipoLocalidade(TipoLocalidade.NUCLEO);
			break;
		}
	}

	public void setarTipoGestaoInProjeto() {
		String ttype = projeto.getGestao().getType();
		switch (ttype) {
		case "coord":
			projeto.setTipoGestao(TipoGestao.COORD);
			break;
		case "reg":
			projeto.setTipoGestao(TipoGestao.REGIONAL);
			break;
		default:
			projeto.setTipoGestao(TipoGestao.SUP);
			break;
		}
	}

	public void salvarAtividade() {
		if (projeto.getId() == null) {
			addMessage("", "Salve primeiro o Projeto!", FacesMessage.SEVERITY_WARN);
			return;
		}
		atividadeProjeto.setProjeto(projeto);

		if (!verificarTotalDeAtividades()) {
			if (gestaoProjeto.salvarAtividade(atividadeProjeto)) {
				atividadeProjeto = new AtividadeProjeto();
				carregarAtividades();
				// createMultiAxisModel();
				createChartExecucao();
				addMessage("", "Atividade salva com Sucesso!", FacesMessage.SEVERITY_INFO);
			} else {
				addMessage("", "Erro ao Salvar Atividade!", FacesMessage.SEVERITY_ERROR);
			}
		} else {
			addMessage("", "Total de atividades ultrapassa o valor do projeto.", FacesMessage.SEVERITY_ERROR);
		}
	}

	public void salvarAtividadeNoGrant() {
		if (projeto.getId() == null) {
			addMessage("", "Salve primeiro o Projeto!", FacesMessage.SEVERITY_WARN);
			return;
		}
		atividadeProjeto.setProjeto(projeto);
		if (gestaoProjeto.salvarAtividade(atividadeProjeto)) {
			atividadeProjeto = new AtividadeProjeto();
			// carregarAtividades();
			// createMultiAxisModel();
			// createChartExecucao();
			addMessage("", "Atividade salva com Sucesso!", FacesMessage.SEVERITY_INFO);
		} else {
			addMessage("", "Erro ao Salvar Atividade!", FacesMessage.SEVERITY_ERROR);
		}
	}

	public void iniciarNovaFonte() {
		fontes = gestaoProjeto.getFontes();
		acaoFonte = new AcaoFonte();
		acaoFonte.setFonte(new FontePagadora());
	}

	public void carregarAcoes() {
		acoes = gestaoProjeto.getAcoesMODE01(projeto);
	}

	public void popularTreeTable() {

		treeNode = new DefaultTreeNode();
		root = new DefaultTreeNode(new Acao("root"), null);
		acoes = gestaoProjeto.getAcoes(projeto);
		acao = new Acao();
		for (Acao a : acoes) {
			a.setDescricao(a.getCodigo());
			TreeNode nod = new DefaultTreeNode(a, root);
			TreeNode node = new DefaultTreeNode(acao, nod);
		}
		// if (tabview != null)
		// System.out.println(tabview.getActiveIndex());
	}

	public String buscarValorTotal() {
		BigDecimal valorTotal = BigDecimal.ZERO;
		for (ProjetoRubrica rubrica : listaDeRubricasProjeto) {
			valorTotal = valorTotal.add(rubrica.getValor());
		}
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(valorTotal);
	}

	public String buscarValorTotalCategoriaDeProjeto() {
		BigDecimal valorTotal = BigDecimal.ZERO;
		if (categoriasDeProjeto != null)
			for (CategoriaProjeto categoria : categoriasDeProjeto) {
				valorTotal = valorTotal.add(categoria.getValor());
			}
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(valorTotal);
	}

	public String buscarValorTotalAcao() {
		BigDecimal valorTotal = BigDecimal.ZERO;
		for (Acao acao : acoes) {
			valorTotal = valorTotal.add(acao.getValor());
		}
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(valorTotal);
	}

	public void definirTab() {
		int index = 0;
		if (tabview != null)
			index = tabview.getActiveIndex();

		if (index == 0) {
			popularTreeTable();
		} else if (index == 1) {
			carregarAtividades();
		} else if (index == 2) {
			buscarLancamentoPorProjeto();
		} else if (index == 3) {
			createPieModels();
		} else if (index == 4) {
			createPieModels();
		} else if (index == 6) {
			getTodasAsAcoes();
		}

	}

	public void definirTabProjeto() {
		int index = 0;
		if (tabviewProjeto != null)
			index = tabviewProjeto.getActiveIndex();

		if (tabviewProjeto != null)
			index = tabviewProjeto.getActiveIndex();

		if (index == 0) {
			System.out.println("Tab 1");

		} else if (index == 1) {

			findMetasByIdProjeto();
			findIndicadoresByProjeto();
		} else if (index == 2) {
			carregarAtividades();

		} else if (index == 3) {
			carregarRelacaoDeOrcamentos();
			carregasRubricasOrcadas();
			carregarRubricasDeProjeto();
			calculaValor();

			// createMultiAxisModel();
			// createChartExecucao();
			// carregarListaDeCategoriasDeProjeto();
		} else if (index == 4) {
			carregarCronograma();
		} else if (index == 5) {
			carregarAtividades();
			createChartExecucao();
		}

	}

	public String testaMes(Long id) {
		if (id == new Long(5)) {
			return "background-color:red";
		} else {
			return "";
		}
	}

	public void mudarStatus() {

		gestaoProjeto.mudarStatus(atividadeProjeto);
		// atividades.stream().filter(s -> s.getId().longValue() ==
		// atividadeAcao.getId()).findAny().get().setStatus(atividadeAcao.getStatus());
		atividadeProjeto = new AtividadeProjeto();
		addMessage("", "Salvo com sucesso", FacesMessage.SEVERITY_INFO);
	}

	public void mudarStatusPageAtividade() {

		gestaoProjeto.mudarStatus(atividadeProjeto);
		atividades.stream().filter(s -> s.getId().longValue() == atividadeProjeto.getId()).findAny().get()
				.setStatus(atividadeProjeto.getStatus());
		atividadeProjeto = new AtividadeProjeto();
		addMessage("", "Salvo com sucesso", FacesMessage.SEVERITY_INFO);
	}

	public void onNodeExpand(NodeExpandEvent event) {

		Object obj = event.getTreeNode().getData();
		event.getTreeNode().getChildren().clear();
		Acao ac = (Acao) event.getTreeNode().getData();

		List<AtividadeProjeto> atividades = new ArrayList<>();
		atividades = gestaoProjeto.getAtividadeByAcao(ac.getId());

		for (AtividadeProjeto atividade : atividades) {
			TreeNode node = new DefaultTreeNode(atividade, event.getTreeNode());
		}

	}

	public boolean faltaPreencher() {
		boolean retorno = false;

		if (projeto.getRepasse() == null || projeto.getRepasse().equals("")) {
			retorno = true;
			addMessage("", "Preencha o campo 'Tipo de execução'", FacesMessage.SEVERITY_ERROR);
		}

		// if (projeto.getRepasse() == null || projeto.getRepasse().equals(""))
		// {
		// retorno = true;
		// addMessage("", "Preencha o campo 'Tipo de execuÃ§Ã£o'",
		// FacesMessage.SEVERITY_ERROR);
		// }

		return retorno;
	}

	public void imprimirProjeto() {

		double total = 0;
		double totalRepasse = 0;
		double totalExecucaoDireta = 0;

		double totalFasRepasse = 0;
		double totalOutraRepasse = 0;
		double totalFasExecucao = 0;
		double totalOutraExecucao = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		projeto = gestaoProjeto.getProjetoById(projeto.getId());
		List<MetaPlano> metaPlano = gestaoProjeto.getAcoesPlanoDeTrabalho(projeto);

		for (MetaPlano mt : metaPlano) {

			double valor = Double.parseDouble(mt.getValorPrevisto());

			total += valor;

			if (mt.getEder().equalsIgnoreCase("RR")) {
				totalRepasse += valor;
			} else {
				totalExecucaoDireta += valor;
			}
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		String logo = path + "resources/image/logoFas.gif";

		Map hp = new HashMap();

		UnidadeConservacao uc = gestaoProjeto.findUcById(projeto.getLocalidade().getId());

		hp.put("NOME_INSTITUICAO", getNullValue(uc.getNomeAssociacao(), "Não cadastrado"));
		hp.put("CNPJ", getNullValue(uc.getCnpjassociacao(), "Não cadastrado"));
		hp.put("ENDERECO", getNullValue(uc.getEnderecoAssociacao(), "Não cadastrado"));
		hp.put("UF", "AM");
		hp.put("CEP", getNullValue(uc.getCepAssociacao(), "Não cadastrado"));
		hp.put("TELEFONE", getNullValue(uc.getTelefoneAssociacao(), "Não cadastrado"));
		hp.put("BANCO", getNullValue(uc.getBancoAssociacao(), "Não cadastrado"));
		hp.put("AGENCIA", getNullValue(uc.getAgenciaAssociacao(), "Não cadastrado"));
		hp.put("CONTA_CORR", getNullValue(uc.getContaAssociacao(), "Não cadastrado"));
		hp.put("CARGO", getNullValue(uc.getCargoPresidente(), "Não cadastrado"));
		hp.put("RG", getNullValue(uc.getRgPresidente(), "Não cadastrado"));
		hp.put("CPF", getNullValue(uc.getCpfPresidente(), "Não cadastrado"));
		hp.put("END_RESID", getNullValue(uc.getEnderecoPresidente(), "Não cadastrado"));
		hp.put("CIDADE_RESP", getNullValue(uc.getCidadePresidente(), "Não cadastrado"));
		hp.put("CIDADE", getNullValue(uc.getCidadeAssociacao(), "Não cadastrado"));
		hp.put("UF_RESP", "AM");
		hp.put("CEP_RESP", getNullValue(uc.getCepAssociacao(), "Não cadastrado"));
		hp.put("TELEFONE_RESP", getNullValue(uc.getTelefonePresidente(), "Não cadastrado"));
		hp.put("ID_PLANO", getNullValue(projeto.getNome(), "Não cadastrado"));
		hp.put("OBJ_GERAL", getNullValue(projeto.getObjetivo(), "Não cadastrado"));
		hp.put("OBJ_ESPECIFICO", getNullValue(projeto.getObjetivoEspecifico(), "Não cadastrado"));
		hp.put("NOME_RESPONSAVEL", getNullValue(uc.getPresidenteAssociacao(), "Não cadastrado"));
		hp.put("DATA_INICIO", getNullValue(sdf.format(projeto.getDataInicio()), "Não cadastrado"));
		hp.put("DATA_FINAL", getNullValue(sdf.format(projeto.getDataFinal()), "Não cadastrado"));
		// hp.put("CIDADE", Utils.nullValue(a.getCidade(), "Não cadastrado"));

		// hp.put("EXTRATEGY_EX", Utils.nullValue(plane.getExtrategiaExec(), "Não
		// cadastrado"));
		//
		// hp.put("COORDENADOR", Utils.nullValue(plane.getCoordenador(), "Não
		// cadastrado"));

		hp.put("TOTAL_PLANO", decimal(total));
		// hp.put("TOTAL_FAS_REPASSE", decimal(totalFasRepasse));
		// hp.put("TOTAL_OUTRA_REPASSE", decimal(totalOutraRepasse));
		// hp.put("TOTAL_FAS_EXECUCAO", decimal(totalFasExecucao));
		// hp.put("TOTAL_OUTRA_EXECUCAO", decimal(totalOutraExecucao));

		hp.put("TOTAL_REPASSE", Util.decimal(totalRepasse));
		hp.put("TOTAL_EXECUCAO_DIRETA", Util.decimal(totalExecucaoDireta));

		JRDataSource dataSource = new JRBeanCollectionDataSource(metaPlano);

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/plano_trabalho.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReport("SC", "Plano trabalho", report, hp, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void imprimirTermoProjeto() {

		projeto = gestaoProjeto.getProjetoById(projeto.getId());

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		String logo = path + "resources/image/logoFas.gif";

		Map hp = new HashMap();

		List<MetaPlano> metaPlano = new ArrayList<>();

		hp.put("EXTRATEGY_EX", getNullValue(projeto.getExtrategiaExecucao(), "Não cadastrado"));
		hp.put("COORDENADOR", getNullValue(projeto.getResponsavel(), "Não cadastrado"));
		// hp.put("CIDADE", Utils.nullValue(a.getCidade(), "Não cadastrado"));

		// hp.put("EXTRATEGY_EX", Utils.nullValue(plane.getExtrategiaExec(), "Não
		// cadastrado"));
		//
		// hp.put("COORDENADOR", Utils.nullValue(plane.getCoordenador(), "Não
		// cadastrado"));

		JRDataSource dataSource = new JRBeanCollectionDataSource(metaPlano);

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/termo_plano_trabalho.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReport("PT", "Plano trabalho", report, hp, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String decimal(double numero) {
		Locale locale = new Locale("pt", "BR");
		DecimalFormat form = new DecimalFormat("###,###,##0.00", new DecimalFormatSymbols(locale));
		return String.valueOf(form.format(numero));

	}

	public String getNullValue(String value, String valueIfNull) {
		if (value == null) {
			return valueIfNull;
		} else {
			return value;
		}
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	// caixa de seleï¿½ï¿½o de componentes
	public Componente[] getComponentes() {
		return Componente.values();
	}

	public TipoAdministrativoProjeto[] getTiposAdministrativos() {
		return TipoAdministrativoProjeto.values();
	}

	public StatusAtividade[] getStatusAtividade() {
		return StatusAtividade.values();
	}

	public Programa[] getProgramas() {
		return Programa.values();
	}

	public TipoProjeto[] TiposProjeto() {
		return TipoProjeto.values();
	}

	public TipoLocalidade[] getTipoLocais() {
		return TipoLocalidade.values();
	}

	public TipoGestao[] getTiposGestao() {
		return TipoGestao.values();
	}

	// caixa de seleï¿½ï¿½o de grupos de investimentos
	public GrupoDeInvestimento[] getGruposDeInvestimentos() {
		return GrupoDeInvestimento.values();
	}

	public TipoRelatorioProjeto[] getTipoRelatorios() {
		return TipoRelatorioProjeto.values();
	}

	public String pageCadastro() {
		return "cadastroFuncionario?faces-redirect=true";
	}

	public String cancelar() {
		return "funcionarios?faces-redirect=true";
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public UserProjeto getUserProjeto() {
		return userProjeto;
	}

	public void setUserProjeto(UserProjeto userProjeto) {
		this.userProjeto = userProjeto;
	}

	public List<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TreeNode getTreeNode() {
		return treeNode;
	}

	public void setTreeNode(TreeNode treeNode) {
		this.treeNode = treeNode;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public Acao getAcao() {
		return acao;
	}

	public void setAcao(Acao acao) {
		this.acao = acao;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public List<User> getUsers() {
		return users;
	}

	public List<AtividadeProjeto> getAtividades() {
		return atividades;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public AcaoFonte getAcaoFonte() {
		return acaoFonte;
	}

	public void setAcaoFonte(AcaoFonte acaoFonte) {
		this.acaoFonte = acaoFonte;
	}

	public List<FontePagadora> getFontes() {
		return fontes;
	}

	public void setFontes(List<FontePagadora> fontes) {
		this.fontes = fontes;
	}

	public List<CadeiaProdutiva> getCadeias() {
		return cadeias;
	}

	public void setCadeias(List<CadeiaProdutiva> cadeias) {
		this.cadeias = cadeias;
	}

	public Boolean getMostrarBotao() {
		return mostrarBotao;
	}

	public void setMostrarBotao(Boolean mostrarBotao) {
		this.mostrarBotao = mostrarBotao;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getLocalId() {
		return localId;
	}

	public void setLocalId(String localId) {
		this.localId = localId;
	}

	public List<Localidade> getLocalidades() {
		return localidades;
	}

	public void setLocalidades(List<Localidade> localidades) {
		this.localidades = localidades;
	}

	public Boolean getMostraUc() {
		return mostraUc;
	}

	public void setMostraUc(Boolean mostraUc) {
		this.mostraUc = mostraUc;
	}

	public Boolean getMostraUnidade() {
		return mostraUnidade;
	}

	public void setMostraUnidade(Boolean mostraUnidade) {
		this.mostraUnidade = mostraUnidade;
	}

	public Boolean getMostraMunicipio() {
		return mostraMunicipio;
	}

	public void setMostraMunicipio(Boolean mostraMunicipio) {
		this.mostraMunicipio = mostraMunicipio;
	}

	public Boolean getMostraSede() {
		return mostraSede;
	}

	public void setMostraSede(Boolean mostraSede) {
		this.mostraSede = mostraSede;
	}

	public Boolean getMostraNucleo() {
		return mostraNucleo;
	}

	public void setMostraNucleo(Boolean mostraNucleo) {
		this.mostraNucleo = mostraNucleo;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public Long getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Long idEstado) {
		this.idEstado = idEstado;
	}

	public List<Gestao> getGestoes() {
		return gestoes;
	}

	public void setGestoes(List<Gestao> gestoes) {
		this.gestoes = gestoes;
	}

	public String getTipoGestao() {
		return tipoGestao;
	}

	public void setTipoGestao(String tipoGestao) {
		this.tipoGestao = tipoGestao;
	}

	public AtividadeProjeto getAtividadeProjeto() {
		return atividadeProjeto;
	}

	public void setAtividadeProjeto(AtividadeProjeto atividadeProjeto) {
		this.atividadeProjeto = atividadeProjeto;
	}

	public TabView getTabview() {
		return tabview;
	}

	public void setTabview(TabView tabview) {
		this.tabview = tabview;
	}

	public PieChartModel getPieModel() {
		return pieModel;
	}

	public void setPieModel(PieChartModel pieModel) {
		this.pieModel = pieModel;
	}

	public BarChartModel getAnimatedModel() {
		return animatedModel;
	}

	public void setAnimatedModel(BarChartModel animatedModel) {
		this.animatedModel = animatedModel;
	}

	public List<UserProjeto> getAutorizacoes() {
		return autorizacoes;
	}

	public void setAutorizacoes(List<UserProjeto> autorizacoes) {
		this.autorizacoes = autorizacoes;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Acao> getAcoes() {
		return acoes;
	}

	public void setAcoes(List<Acao> acoes) {
		this.acoes = acoes;
	}

	public List<LancamentoAuxiliar> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<LancamentoAuxiliar> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public BarChartModel getLancamentoAnimatedModelByAcao() {
		return lancamentoAnimatedModelByAcao;
	}

	public void setLancamentoAnimatedModelByAcao(BarChartModel lancamentoAnimatedModelByAcao) {
		this.lancamentoAnimatedModelByAcao = lancamentoAnimatedModelByAcao;
	}

	public BarChartModel getLancamentoAnimatedModelByFonte() {
		return lancamentoAnimatedModelByFonte;
	}

	public void setLancamentoAnimatedModelByFonte(BarChartModel lancamentoAnimatedModelByFonte) {
		this.lancamentoAnimatedModelByFonte = lancamentoAnimatedModelByFonte;
	}

	public int getEchoYAcao() {
		return echoYAcao;
	}

	public void setEchoYAcao(int echoYAcao) {
		this.echoYAcao = echoYAcao;
	}

	public int getEchoYFonte() {
		return echoYFonte;
	}

	public void setEchoYFonte(int echoYFonte) {
		this.echoYFonte = echoYFonte;
	}

	public TipoRelatorioProjeto getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(TipoRelatorioProjeto tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public List<Rubrica> getRubricas() {
		return rubricas;
	}

	public void setRubricas(List<Rubrica> rubricas) {
		this.rubricas = rubricas;
	}

	public BarChartModel getCadeiaanimatedModel() {
		return CadeiaanimatedModel;
	}

	public void setCadeiaanimatedModel(BarChartModel cadeiaanimatedModel) {
		CadeiaanimatedModel = cadeiaanimatedModel;
	}

	public ComponenteClass getComponente() {
		return componente;
	}

	public void setComponente(ComponenteClass componente) {
		this.componente = componente;
	}

	public List<SubComponente> getSubComponentes() {
		return subComponentes;
	}

	public void setSubComponentes(List<SubComponente> subComponentes) {
		this.subComponentes = subComponentes;
	}

	public SubComponente getSubComponente() {
		return subComponente;
	}

	public void setSubComponente(SubComponente subComponente) {
		this.subComponente = subComponente;
	}

	public List<ComponenteClass> getComponentesClass() {
		return componentesClass;
	}

	public void setComponentesClass(List<ComponenteClass> componentesClass) {
		this.componentesClass = componentesClass;
	}

	public OrcamentoProjeto getOrcamentoProjeto() {
		return orcamentoProjeto;
	}

	public void setOrcamentoProjeto(OrcamentoProjeto orcamentoProjeto) {
		this.orcamentoProjeto = orcamentoProjeto;
	}

	public List<OrcamentoProjeto> getRelacaoDeOrcamentos() {
		return relacaoDeOrcamentos;
	}

	public void setRelacaoDeOrcamentos(List<OrcamentoProjeto> relacaoDeOrcamentos) {
		this.relacaoDeOrcamentos = relacaoDeOrcamentos;
	}

	public ProjetoRubrica getProjetoRubrica() {
		return projetoRubrica;
	}

	public void setProjetoRubrica(ProjetoRubrica projetoRubrica) {
		this.projetoRubrica = projetoRubrica;
	}

	public List<RubricaOrcamento> getListaDeRubricasOrcadas() {
		return listaDeRubricasOrcadas;
	}

	public void setListaDeRubricasOrcadas(List<RubricaOrcamento> listaDeRubricasOrcadas) {
		this.listaDeRubricasOrcadas = listaDeRubricasOrcadas;
	}

	public List<ProjetoRubrica> getListaDeRubricasProjeto() {
		return listaDeRubricasProjeto;
	}

	public void setListaDeRubricasProjeto(List<ProjetoRubrica> listaDeRubricasProjeto) {
		this.listaDeRubricasProjeto = listaDeRubricasProjeto;
	}

	public TabView getTabviewProjeto() {
		return tabviewProjeto;
	}

	public void setTabviewProjeto(TabView tabviewProjeto) {
		this.tabviewProjeto = tabviewProjeto;
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

	public void calculaValor() {
		if (projeto.getValor() == null)
			return;

		valorDistribuido = BigDecimal.ZERO;
		diferenca = BigDecimal.ZERO;

		Double vlDistAux;

		Double vlOrcamento = projeto.getValor().doubleValue();

		for (ProjetoRubrica rubrica : listaDeRubricasProjeto) {
			valorDistribuido = valorDistribuido.add(rubrica.getValor());
			vlDistAux = rubrica.getValor().doubleValue();
			rubrica.setPorcentagem((vlDistAux / vlOrcamento) * 100);

		}

		Double vlDistribuido = valorDistribuido.doubleValue();

		diferenca = projeto.getValor().subtract(valorDistribuido);
		porcentagem = (vlDistribuido / vlOrcamento) * 100;

	}

	public Double getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(Double porcentagem) {
		this.porcentagem = porcentagem;
	}

	public List<PlanoDeTrabalho> getPlanos() {
		return planos;
	}

	public void setPlanos(List<PlanoDeTrabalho> planos) {
		this.planos = planos;
	}

	public CategoriaProjeto getCategoriaProjeto() {
		return categoriaProjeto;
	}

	public void setCategoriaProjeto(CategoriaProjeto categoriaProjeto) {
		this.categoriaProjeto = categoriaProjeto;
	}

	public List<CategoriaProjeto> getCategoriasDeProjeto() {
		return categoriasDeProjeto;
	}

	public void setCategoriasDeProjeto(List<CategoriaProjeto> categoriasDeProjeto) {
		this.categoriasDeProjeto = categoriasDeProjeto;
	}

	public List<SubPrograma> getSubsProgramas() {
		return subsProgramas;
	}

	public void setSubsProgramas(List<SubPrograma> subsProgramas) {
		this.subsProgramas = subsProgramas;
	}

	public Integer getPercentExecucaoFisica() {
		return Math.round(percentExecucaoFisica);
	}

	public void setPercentExecucaoFisica(float percentExecucaoFisica) {
		this.percentExecucaoFisica = percentExecucaoFisica;
	}

	public CartesianChartModel getgExecFinance() {
		return gExecFinance;
	}

	public void setgExecFinance(CartesianChartModel gExecFinance) {
		this.gExecFinance = gExecFinance;
	}

	public List<AtividadeProjeto> getAtividadesPlanejadas() {
		return atividadesPlanejadas;
	}

	public void setAtividadesPlanejadas(List<AtividadeProjeto> atividadesPlanejadas) {
		this.atividadesPlanejadas = atividadesPlanejadas;
	}

	public List<AtividadeProjeto> getAtividadesExecutadas() {
		return atividadesExecutadas;
	}

	public void setAtividadesExecutadas(List<AtividadeProjeto> atividadesExecutadas) {
		this.atividadesExecutadas = atividadesExecutadas;
	}

	public List<QualifProjeto> getQualificacoes() {
		return qualificacoes;
	}

	public void setQualificacoes(List<QualifProjeto> qualificacoes) {
		this.qualificacoes = qualificacoes;
	}

	// As Paradas de Metas e Objetivo
	// -----------------------------------------------------------------------

	private @Inject Objetivo objetivo;// = new Objetivo();

	private @Inject Metas metas;

	private List<Objetivo> listaObjetivos;

	private List<Metas> listaMetas;

	private List<Metas> listaMetasPorObjetivo = new ArrayList<Metas>();

	@Inject
	private ObjetivoService objetivoService;

	@Inject
	private MetasService metasService;

	public List<Objetivo> getListaObjetivos() {
		return listaObjetivos;
	}

	public void setListaObjetivos(List<Objetivo> listaObjetivos) {
		this.listaObjetivos = listaObjetivos;
	}

	public List<Metas> getListaMetas() {
		return listaMetas;
	}

	public void setListaMetas(List<Metas> listaMetas) {
		this.listaMetas = listaMetas;
	}

	public Objetivo getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(Objetivo objetivo) {
		this.objetivo = objetivo;
	}

	public Metas getMetas() {
		return metas;
	}

	public void setMetas(Metas metas) {
		this.metas = metas;
	}

	public void findObjetivoByIdProjeto() {
		listaObjetivos = objetivoService.findObjetivoByIdProjeto(projeto.getId());
	}

	public List<Objetivo> objetivosByIdProjeto() {
		return objetivoService.findObjetivoByIdProjeto(projeto.getId());
	}

	public void findMetasByIdProjeto() {
		listaMetas = metasService.findMetasByIdProjeto(projeto.getId());
	}

	public List<Metas> getListaMetasPorObjetivo() {
		return listaMetasPorObjetivo;
	}

	public void setListaMetasPorObjetivo(List<Metas> listaMetasPorObjetivo) {
		this.listaMetasPorObjetivo = listaMetasPorObjetivo;
	}

	public void openDialogObjetivo() {
		objetivo = new Objetivo();
		openDialog("PF('dlg_cadastro_objetivo').show();");
	}

	public void openDialogEditObjetivo() {
		objetivo = objetivoService.getId(objetivo.getId());
		if (objetivo.getId() != null)
			metasService.findMetasByObjetivo(objetivo);
	}

	public void editarMeta() {
		metas = metasService.findById(metas.getId());
		openDialog("PF('dlg_cadastro_metas').show();");
	}

	public void editarObjetivo() {
		objetivo = objetivoService.getId(objetivo.getId());
		listaMetasPorObjetivo = metasService.findMetasByObjetivo(objetivo);
		openDialog("PF('dlg_cadastro_objetivo').show();");
	}

	public void openDialogMetas() {
		metas = new Metas();
	}

	public void removeObjetivo(Objetivo objetivo) {
		if (metasService.findMetasByObjetivo(objetivo).size() == 0) {
			objetivoService.remover(objetivo);
			findObjetivoByIdProjeto();
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Remova Primeiro as Metas do Objetivo."));
		}

	}

	public void removerMetas(Metas metas) {
		metasService.remover(metas);
		findMetasByIdProjeto();
	}

	public void salvarObjetivo() {
		try {
			if (projeto.getId() == null) {
				openDialog("PF('dlg_cadastro_objetivo').hide();");
				addMessage("", "Salve o Projeto Primeiro!", FacesMessage.SEVERITY_WARN);
				return;
			}
			objetivo.setCriador(usuarioSessao.getUsuario());
			objetivo.setDataCriacao(new Date());
			objetivo.setProjeto(projeto);
			objetivoService.salvar(objetivo);
			objetivo = new Objetivo();
			findObjetivoByIdProjeto();
			openDialog("PF('dlg_cadastro_objetivo').hide();");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void salvarMetas() {
		try {
			metas.setDataCriacao(new Date());
			metas.setUsuario(usuarioSessao.getUsuario());
			metasService.salvar(metas);
			findMetasByIdProjeto();
			openDialog("PF('dlg_cadastro_metas').hide();");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void concluirMeta(Metas metas) {
		if (metas.getConcluido()) {
			metas.setConcluido(false);
		} else {
			metas.setConcluido(true);
		}
		this.metas = metas;
		salvarMetas();
	}

	public List<AtividadeProjeto> getAtividadesExecutadasNaoPlanejadas() {
		return atividadesExecutadasNaoPlanejadas;
	}

	public void setAtividadesExecutadasNaoPlanejadas(List<AtividadeProjeto> atividadesExecutadasNaoPlanejadas) {
		this.atividadesExecutadasNaoPlanejadas = atividadesExecutadasNaoPlanejadas;
	}
	/// ----------------------Vincular Atividade a metas
	/// 04/02/2019-------------------------------------------

	public BigDecimal getTotalPLanejadas() {
		return totalPLanejadas;
	}

	public void setTotalPLanejadas(BigDecimal totalPLanejadas) {
		this.totalPLanejadas = totalPLanejadas;
	}

	public BigDecimal getTotalExecutadas() {
		return totalExecutadas;
	}

	public void setTotalExecutadas(BigDecimal totalExecutadas) {
		this.totalExecutadas = totalExecutadas;
	}

	public BigDecimal getTotalNaoPlanejadas() {
		return totalNaoPlanejadas;
	}

	public void setTotalNaoPlanejadas(BigDecimal totalNaoPlanejadas) {
		this.totalNaoPlanejadas = totalNaoPlanejadas;
	}

	public int getQtdPlanejadas() {
		return qtdPlanejadas;
	}

	public void setQtdPlanejadas(int qtdPlanejadas) {
		this.qtdPlanejadas = qtdPlanejadas;
	}

	public int getQtdExecutadaPlanejadas() {
		return qtdExecutadaPlanejadas;
	}

	public void setQtdExecutadaPlanejadas(int qtdExecutadaPlanejadas) {
		this.qtdExecutadaPlanejadas = qtdExecutadaPlanejadas;
	}

	public int getQtdNaoPlanejadas() {
		return qtdNaoPlanejadas;
	}

	public void setQtdNaoPlanejadas(int qtdNaoPlanejadas) {
		this.qtdNaoPlanejadas = qtdNaoPlanejadas;
	}

	/// fim
	/// 04/02/2019-----------------------------------------------------------------------------------------

	//// alteraÃ§Ã£o para colocar liberaÃ§Ã£o de projeto junto a tela de projeto
	//// 06/05/2019------------------------

	//// alteraÃ§Ã£o para colocar liberaÃ§Ã£o de projeto junto a tela de projeto
	//// 06/05/2019------------------------

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private PlanoDeTrabalhoService planoDeTrabalhoService;

	private List<Projeto> listafiltro;

	private List<Projeto> listaProjetos;

	private List<User> listaUsuario;

	private User usuarioSelecionado;

	private List<Projeto> listaProjetosSelecionados;

	private List<PlanoDeTrabalho> listaPlanosDeTrabalho;

	private Projeto projetoSelecionado;

	private List<User> listaUsuarios = new ArrayList<User>();

	public List<Projeto> getListaProjetosSelecionados() {
		return listaProjetosSelecionados;
	}

	public void setListaProjetosSelecionados(List<Projeto> listaProjetosSelecionados) {
		this.listaProjetosSelecionados = listaProjetosSelecionados;
	}

	public void openDialog() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('dialogUsuario').show();");
	}

	public void closeDialog() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('dialogUsuario').hide();");
	}

	public void salvaProjetoUsuario() {
		for (Projeto p : listaProjetosSelecionados) {
			UserProjeto up = new UserProjeto();
			up.setProjeto(p);
			up.setUser(usuarioSelecionado);
			projetoService.salvarUsuarioNoProjeto(up);
		}
		listaProjetosSelecionados = new ArrayList<Projeto>();

		addMessage("", "Salvo com Sucesso!", FacesMessage.SEVERITY_INFO);
	}

	public Boolean verificaPrivilegio(Projeto projeto, User usuario) {

		Aprouve ap = new Aprouve();
		// ap.setSigla("SAVE_PROJ");
		ap.setUsuario(usuarioSessao.getUsuario());

		if (!usuario.getPerfil().getId().equals(new Long(1))) {
			projeto = projetoService.getProjetoById(projeto.getId());
			ap.setSigla(projeto.getGestao().getSigla());
			if (projeto.getGestao().getColaborador().getId().equals(usuario.getColaborador().getId())
					|| compraService.findAprouve(ap)) {
				return true;
			} else
				return false;
		} else {
			return true;
		}

	}

	public void verificaProjetoSelecionado() {

		if (listaProjetosSelecionados != null && listaProjetosSelecionados.size() > 0) {
			for (Projeto projeto : listaProjetosSelecionados) {
				if (!verificaPrivilegio(projeto, usuarioSessao.getUsuario())) {
					addMessage("", "Você não é gestor do projeto - " + projeto.getNome()
							+ " e não é um usuário autorizado a liberar projetos", FacesMessage.SEVERITY_WARN);
					return;
				}
			}

			listaUsuario = usuarioService.getUsuarios();
			openDialog();

		} else {
			addMessage("", "Selecione um ou mais projetos!", FacesMessage.SEVERITY_WARN);
		}
	}

	public void findUsuariosComAcesso(Projeto projetoSelecionado) {
		this.projetoSelecionado = projetoSelecionado;
		listaUsuarios = usuarioService.findUsuarioByProjeto(projetoSelecionado);
	}

	public void removerAcesso(User usuarioSelecionado) {
		if (verificaPrivilegio(projetoSelecionado, usuarioSessao.getUsuario())) {
			UserProjeto usuario = usuarioService.findUsuarioProjeto(projetoSelecionado, usuarioSelecionado);
			usuarioService.remover(usuario);
			listaUsuarios = usuarioService.findUsuarioByProjeto(projetoSelecionado);
			addMessage("", "Excluido com Sucesso!", FacesMessage.SEVERITY_INFO);
		} else {
			addMessage("", "VocÃª nÃ£o tem permissÃ£o para esta aÃ§Ã£o!", FacesMessage.SEVERITY_WARN);
		}
	}

	public void concluirObjetivo(Objetivo objetivo) {
		if (objetivo.getConcluido()) {
			objetivo.setConcluido(false);
		} else {
			List<Metas> lista = metasService.findMetasByObjetivo(objetivo);
			for (Metas meta : lista) {
				if (meta.getConcluido().equals(false)) {
					addMessage("", "Existem metas nÃ£o concluidas do objetivo: " + objetivo.getNome()
							+ ", conclua todas e tente novamente!", FacesMessage.SEVERITY_WARN);
					return;
				}
			}
			objetivo.setConcluido(true);
		}
		this.objetivo = objetivo;
		salvarObjetivo();
	}

	public Boolean podeAprovar(Projeto projeto) {

		List<AprovacaoProjeto> aprovacoes = new ArrayList<>();

		if (projeto == null && this.projeto.getId() != null)
			projeto = gestaoProjeto.getProjetoById(this.projeto.getId());
		else if (projeto != null) {
			projeto = gestaoProjeto.getProjetoById(projeto.getId());

			aprovacoes = getAprovacoesProjeto(projeto);

			if (aprovacoes.size() < Projeto.NUMERO_APROVACOES) {

				if (projeto.getGestao() != null && projeto.getGestao().getColaborador().getId()
						.longValue() == usuarioSessao.getUsuario().getColaborador().getId().longValue())
					return podeAprovarGestao(aprovacoes);
				else if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
						|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro")) {
					return podeAprovarFinanceiro(aprovacoes);
				}
			}
		}

		return false;
	}

	public Boolean podeAprovarGestao(List<AprovacaoProjeto> aprovacoes) {

		for (AprovacaoProjeto aprovacaoProjeto : aprovacoes) {
			if (aprovacaoProjeto.getGestao() != null)
				return false;
		}

		return true;
	}

	public Boolean podeAprovarFinanceiro(List<AprovacaoProjeto> aprovacoes) {

		for (AprovacaoProjeto aprovacaoProjeto : aprovacoes) {
			if (aprovacaoProjeto.getOutro() != null && !aprovacaoProjeto.getOutro().equals(""))
				return false;
		}

		return true;
	}

	public void aprovarProjeto() {
		try {
			AprovacaoProjeto aprovacaoProjeto = new AprovacaoProjeto();
			aprovacaoProjeto.setUsuario(usuarioSessao.getUsuario());
			projeto = gestaoProjeto.getProjetoById(this.projeto.getId());
			aprovacaoProjeto.setProjeto(projeto);
			if (projeto.getGestao().getColaborador().getId().longValue() == usuarioSessao.getUsuario().getColaborador()
					.getId().longValue()) {
				aprovacaoProjeto.setGestao(projeto.getGestao());
			} else if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
					|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro")) {
				aprovacaoProjeto.setOutro(usuarioSessao.getUsuario().getPerfil().getDescricao());
			}
			aprovacaoProjeto.setDataAprovacao(new Date());
			aprovacaoProjeto = gestaoProjeto.salvarAprovacao(aprovacaoProjeto);

			List<AprovacaoProjeto> aprovacoes = getAprovacoesProjeto(projeto);
			if (aprovacoes.size() == Projeto.NUMERO_APROVACOES)
				projeto.setStatusAprovacao(StatusAprovacaoProjeto.APROVADO);
			else
				projeto.setStatusAprovacao(StatusAprovacaoProjeto.EM_APROVACAO);

			gestaoProjeto.salvar(projeto, null);
			colorirProjeto();
			addMessage("", "Projeto aprovado com sucesso.", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			addMessage("", "Não foi possível efetuar a aprovação do projeto: " + e.getMessage(),
					FacesMessage.SEVERITY_ERROR);
		}
	}

	public List<AprovacaoProjeto> getAprovacoesProjeto(Projeto projeto) {
		return gestaoProjeto.obterAprovacoesProjeto(projeto);
	}

	public User getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(User usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public ProjetoService getGestaoProjeto() {
		return gestaoProjeto;
	}

	public void setGestaoProjeto(ProjetoService gestaoProjeto) {
		this.gestaoProjeto = gestaoProjeto;
	}

	public List<Projeto> getListafiltro() {
		return listafiltro;
	}

	public void setListafiltro(List<Projeto> listafiltro) {
		this.listafiltro = listafiltro;
	}

	public List<Projeto> getListaProjetos() {
		return listaProjetos;
	}

	public void setListaProjetos(List<Projeto> listaProjetos) {
		this.listaProjetos = listaProjetos;
	}

	public List<User> getListaUsuario() {
		return listaUsuario;
	}

	public void setListaUsuario(List<User> listaUsuario) {
		this.listaUsuario = listaUsuario;
	}

	public List<PlanoDeTrabalho> getListaPlanosDeTrabalho() {
		return listaPlanosDeTrabalho;
	}

	public void setListaPlanosDeTrabalho(List<PlanoDeTrabalho> listaPlanosDeTrabalho) {
		this.listaPlanosDeTrabalho = listaPlanosDeTrabalho;
	}

	public Projeto getProjetoSelecionado() {
		return projetoSelecionado;
	}

	public void setProjetoSelecionado(Projeto projetoSelecionado) {
		this.projetoSelecionado = projetoSelecionado;
	}

	public List<User> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<User> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	//// fim
	//// ----------------------------------------------------------------------------------------------------

	///// InclusÃ£o de Indicadores na tela de objetivos e metas 11/02/2019
	///// ---------------------------------------

	@Inject
	private IndicadorService indicadorService;

	private Indicador indicador;

	private List<Indicador> listaIndicadores = new ArrayList<Indicador>();

	public void salvarIndicador() {
		indicador.setProjeto(projeto);
		indicadorService.salvarIndicador(indicador);
		addMessage("", "Salvo com Sucesso!", FacesMessage.SEVERITY_INFO);
		PrimeFaces current = PrimeFaces.current();
		findIndicadoresByProjeto();
		current.ajax().update("tab_cadastro:indicadores");
		current.executeScript("PF('dlg_cadastro_indicador').hide();");

		this.indicador = new Indicador();
	}

	public void editarIndicador() {
		openDialogIndicador();

	}

	public void concluirIndicador(Indicador indicador) {
		if (indicador.getConcluido()) {
			indicador.setConcluido(false);
		} else {
			indicador.setConcluido(true);
		}
		indicadorService.salvarIndicador(indicador);

	}

	public void excluirIndicador(Indicador indicador) {
		indicadorService.removerIndicador(indicador);
		findIndicadoresByProjeto();
		addMessage("", "Excluido com sucesso!", FacesMessage.SEVERITY_INFO);
	}

	public void openDialogIndicador() {
		if (indicador == null)
			indicador = new Indicador();
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('dlg_cadastro_indicador').show();");
	}

	private void findIndicadoresByProjeto() {
		listaIndicadores = indicadorService.findIndicadorByProjeto(projeto);
	}

	public Indicador getIndicador() {
		return indicador;
	}

	public void setIndicador(Indicador indicador) {
		this.indicador = indicador;
	}

	public List<Indicador> getListaIndicadores() {
		return listaIndicadores;
	}

	public void setListaIndicadores(List<Indicador> listaIndicadores) {
		this.listaIndicadores = listaIndicadores;
	}

	public void colorirProjeto() throws NumberFormatException, ParseException {

		// if ((usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
		// ||
		// usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro"))
		// && (filtro.getGestao() == null)
		// &&((filtro.getProjeto() == null) || (filtro.getProjeto().getId() == null))
		// ) {
		// projetos = new ArrayList<Projeto>();
		// }else {
		// projetos = gestaoProjeto.getProjetosFiltroPorUsuarioWidthColor(filtro,
		// usuarioSessao.getUsuario());
		// }

		projetos = gestaoProjeto.getProjetosFiltroPorUsuarioWidthColor(filtro, usuarioSessao.getUsuario());

		for (Projeto at : projetos) {
			if (((at.getTotalPercentualFinanceiro().isNaN() ? 0 : at.getTotalPercentualFinanceiro())
					- (at.getTotalPercentualFisico().isNaN() ? 0 : at.getTotalPercentualFisico()) < 21)
					&& ((at.getTotalPercentualFinanceiro().isNaN() ? 0 : at.getTotalPercentualFinanceiro())
							- (at.getTotalPercentualFisico().isNaN() ? 0 : at.getTotalPercentualFisico()) > 0)) {
				at.setCor("highlight_yellow");
			} else if (((at.getTotalPercentualFinanceiro().isNaN() ? 0 : at.getTotalPercentualFinanceiro())
					- (at.getTotalPercentualFisico().isNaN() ? 0 : at.getTotalPercentualFisico()) > 20)
					&& ((at.getTotalPercentualFinanceiro().isNaN() ? 0 : at.getTotalPercentualFinanceiro())
							- (at.getTotalPercentualFisico().isNaN() ? 0 : at.getTotalPercentualFisico()) < 51)) {
				at.setCor("highlight_orange");
			} else if ((at.getTotalPercentualFinanceiro().isNaN() ? 0 : at.getTotalPercentualFinanceiro())
					- (at.getTotalPercentualFisico().isNaN() ? 0 : at.getTotalPercentualFisico()) > 50) {
				at.setCor("highlight_red");
			}

			carregarComponentes();

		}
	}

	public void limpaFiltroProjeto() {
		filtro.setProjeto(new Projeto());
		filtro.setGestao(null);
		filtro.setCodigo(null);
		filtro.setTitulo(null);
		filtro.setOrcamento(null);
		filtro.setPlanoDeTrabalho(null);
		filtro.setAtivo(null);
		filtro.setDataInicio(null);
		filtro.setDataFinal(null);
	}

	public List<Gestao> findGestaoAutoComplete(String query) {
		if (query.isEmpty())
			return new ArrayList<Gestao>();
		else
			return projetoService.getGestaoAutoComplete(query);
	}

	public List<Orcamento> getOrcamentos() {
		return orcamentos;
	}

	public void setOrcamentos(List<Orcamento> orcamentos) {
		this.orcamentos = orcamentos;
	}

	/*
	 * Rascunhos
	 * 
	 */

	/*
	 * public void carregarAcoes(){ root = new DefaultTreeNode(new Acao("root"),
	 * null);
	 * 
	 * Projeto pj = new Projeto();
	 * 
	 * pj.setId(new Long(1)); pj.setNome("Projeto 01");
	 * 
	 * Acao acao = new Acao(); acao.setProjeto(pj); acao.setDescricao("CAF0114");
	 * acao.setCodigo("CAF0114"); acoes.add(acao);
	 * 
	 * acao = new Acao(); acao.setProjeto(pj); acao.setCodigo("CA0114");
	 * acao.setDescricao("CA0114"); acoes.add(acao);
	 * 
	 * acao = new Acao(); acao.setProjeto(pj); acao.setCodigo("CRI016");
	 * acao.setDescricao("CRI016"); acao.setOrcado(new BigDecimal("1000000"));
	 * acoes.add(acao);
	 * 
	 * acao = new Acao();
	 * 
	 * for (Acao a : acoes) { TreeNode nod = new DefaultTreeNode(a, root); TreeNode
	 * node = new DefaultTreeNode(acao, nod); }
	 * 
	 * this.setTreeNode(root); //this.setTreeNode(new
	 * ServiceTreeTable().getRoot(acoes)); projetos.add(pj);
	 * 
	 * }
	 */

	/*
	 * public void atualizarNome(){ this.nome = projeto.getNome(); }
	 */

	/*
	 * public List<Projeto> completeProjeto(String query) { List<Projeto>
	 * allProjetos = this.projetos; List<Projeto> filteredProjetos = new
	 * ArrayList<Projeto>();
	 * 
	 * for (int i = 0; i < allProjetos.size(); i++) { Projeto skin =
	 * allProjetos.get(i); if(skin.getNome().toLowerCase().startsWith(query)) {
	 * filteredProjetos.add(skin); } }
	 * 
	 * return filteredProjetos; }
	 */

}
