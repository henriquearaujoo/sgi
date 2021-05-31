package managedbean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import anotacoes.Transactional;
import model.*;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import service.ContaService;
import service.CustoPessoalService;
import service.OrcamentoService;
import service.PagamentoService;
import util.*;

@Named(value = "conta_controller")
@ViewScoped
public class ContaBancariaController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ContaBancaria contaBancaria;

	private List<ContaBancaria> contas = new ArrayList<>();

	private List<Banco> bancos = new ArrayList<>();

	@Inject
	private ContaService service;
	private @Inject OrcamentoService orcService;
	private @Inject PagamentoService pagService;
	@Inject
	private CustoPessoalService custoService;

	@Inject
	private LancamentoAcao lancamentoAcao;

	private BaixaAplicacao baixaAplicacao;

	private AplicacaoRecurso aplicacao;

	private @Inject Orcamento orcamento;

	private MenuLateral menu = new MenuLateral();

	private Filtro filtro = new Filtro();

	private List<RelatorioContasAPagar> listaDePagamentosRelatados = new ArrayList<>();

	private RendimentoConta rendimentoConta = new RendimentoConta();

	private FonteDoacaoConta fonteDoacaoConta = new FonteDoacaoConta();

	private AlocacaoRendimento alocacaoRendimento = new AlocacaoRendimento();

	private List<ContaBancaria> contasPagadoras = new ArrayList<>();

	private List<ContaBancaria> contasAlocacao = new ArrayList<>();

	private List<Orcamento> orcamentos = new ArrayList<>();

	private List<BaixaAplicacao> baixas = new ArrayList<>();

	private List<AplicacaoRecurso> aplicacoes = new ArrayList<>();

	private Integer tipoAplicacao = 0;

	private BigDecimal valorAplicacao;

	private Date dataAplicacao = new Date();

	private @Inject UsuarioSessao usuarioSessao;

	private Boolean editarAplicacao = false;

	private Boolean editarBaixa = false;

	public ContaBancariaController() {
	}

	public void initListagem() {
		carregarContas();
	}

	public void initCadastro() {
		filtro = new Filtro();
		filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		filtro.setDataFinal(DataUtil.getDataFinal(new Date()));

		/*
		 * carregarContasPagadoras(); carregarContasAlocacao(); carregarOrcamentos();
		 * carregarAplicacoes(); carregarBaixas(); carregarRendimentos();
		 * carregarAlocacoes(); carregarDoacoes();
		 */

		
	}

	public void carregarTransacoes() {
		if (contaBancaria.getId() != null) {
			filtro.setIdConta(contaBancaria.getId());
			listaDePagamentosRelatados = service.getPagamentosPorConta(filtro);
		} else {
			listaDePagamentosRelatados = new ArrayList<>();
		}
	}

	public void limparFiltro() {
		filtro = new Filtro();
	}

	public void carregarContas() {
		contas = service.getContasBancarias(filtro);
	}

	public void carregarOrcamentos() {
		orcamentos = orcService.getOrcamentos(filtro);
	}

	public void carregarBaixas() {
		if (contaBancaria.getId() != null)
			baixas = service.obterBaixas(contaBancaria.getId());
		else
			baixas = new ArrayList<>();
	}

	public void carregarAplicacoes() {
		if (contaBancaria.getId() != null)
			aplicacoes = service.obterAplicacoes(contaBancaria.getId());
		else
			aplicacoes = new ArrayList<>();
	}

	public void carregarRendimentos() {
		if (contaBancaria.getId() != null)
			contaBancaria.setRendimentos(service.obterRendimentos(contaBancaria));
		else
			contaBancaria.setRendimentos(new ArrayList<>());
	}

	public void carregarDoacoes() {
		if (contaBancaria.getId() != null)
			contaBancaria.setDoacoes(service.obterDoacoes(contaBancaria));
		else
			contaBancaria.setDoacoes(new ArrayList<>());
	}

	public void carregarAlocacoes() {
		if (contaBancaria.getId() != null)
			contaBancaria.setAlocacoesRendimentos(service.obterAlocacoesRendimentos(contaBancaria));
		else
			contaBancaria.setAlocacoesRendimentos(new ArrayList<>());
	}

	public String gerarCodigo(String sigla) {

		StringBuilder s = new StringBuilder(sigla);

		Calendar calendar = Calendar.getInstance();
		int ano = calendar.get(Calendar.YEAR);
		int mes = calendar.get(Calendar.MONTH) + 1;
		int dia = calendar.get(Calendar.DAY_OF_MONTH);
		int minuto = calendar.get(Calendar.MINUTE);
		int segundo = calendar.get(Calendar.SECOND);
		int milisegundo = calendar.get(Calendar.MILLISECOND);
		/*
		 * System.out.println("Ano: "+ano); System.out.println("M�s: "+mes);
		 * System.out.println("Dia: "+dia); System.out.println("Minuto: " +minuto);
		 * System.out.println("Segundo: "+segundo);
		 * System.out.println("Milisegundo: "+milisegundo);
		 */
		s.append(String.valueOf(ano).substring(2));
		s.append(String.valueOf(mes));
		s.append(String.valueOf(dia));
		s.append(String.valueOf(minuto));
		s.append(String.valueOf(segundo));
		// s.append(String.valueOf(milisegundo).substring(0, 2));
		// System.out.println(s.toString());
		return s.toString();
	}

	public String salvarConta() {

		service.salvarConta(contaBancaria);

		contaBancaria = new ContaBancaria();
		return "contas?faces-redirect=true";
	}

	public List<Banco> completeBanco(String query) {
		bancos = new ArrayList<>();
		bancos = service.getBancoAutoComplete(query);
		return bancos;
	}

	public void removerConta() {
		service.removerConta(contaBancaria);
		carregarContas();
	}

	public void carregarContasPagadoras() {
		contasPagadoras = service.obterContasRendimento();
	}

	public void carregarContasAlocacao() {
		contasAlocacao = orcService.getContas();
	}

	@Transactional
	public void salvarRendimento() {
		try {

			rendimentoConta.setConta(contaBancaria);
			rendimentoConta.setUsuario(usuarioSessao.getUsuario());

			rendimentoConta = service.salvarRendimento(rendimentoConta);

			if (contaBancaria.getRendimentos() == null)
				contaBancaria.setRendimentos(new ArrayList<>());

			contaBancaria.getRendimentos().add(rendimentoConta);

			rendimentoConta = new RendimentoConta();

			addMessage("", "Rendimento salvo com sucesso.", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			addMessage("", e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}

	@Transactional
	public void salvarDoacao() {
		try {

			fonteDoacaoConta.setConta(contaBancaria);
			fonteDoacaoConta.setUsuario(usuarioSessao.getUsuario());

			fonteDoacaoConta = service.salvarDoacao(fonteDoacaoConta);

			if (contaBancaria.getDoacoes() == null)
				contaBancaria.setDoacoes(new ArrayList<>());

			contaBancaria.getDoacoes().add(fonteDoacaoConta);

			fonteDoacaoConta = new FonteDoacaoConta();

			addMessage("", "Doação adicionada com sucesso.", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			addMessage("", e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}

	@Transactional
	public void salvarAlocacaoRendimento() {
		try {

			BigDecimal disponivel = getValorDisponivel();
			if (alocacaoRendimento.getValor().compareTo(disponivel) == 1) {
				NumberFormat format = NumberFormat.getCurrencyInstance();
				throw new Exception("O valor da alocação é maior que o valor disponível: " + format.format(disponivel));
			}

			if (contaBancaria.getAlocacoesRendimentos() == null)
				contaBancaria.setAlocacoesRendimentos(new ArrayList<>());

			alocacaoRendimento.setConta(contaBancaria);
			alocacaoRendimento.setContaAlocacao(alocacaoRendimento.getOrcamento().getContaBancaria());

			alocacaoRendimento = service.salvarAlocacaoRendimento(alocacaoRendimento);

			contaBancaria.setAlocacoesRendimentos(service.obterAlocacoesRendimentos(contaBancaria));

			for (AlocacaoRendimento ar : contaBancaria.getAlocacoesRendimentos()) {

				pagService.deletarRendimento(ar.getId());

				LancamentoAlocacaoRendimento lar = new LancamentoAlocacaoRendimento();
				lar.setIdAlocacaoRendimento(ar.getId());
				lar.setDataPagamento(new Date());
				lar.setContaPagador(contaBancaria);
				lar.setContaRecebedor(ar.getContaAlocacao());
				lar.setQuantidadeParcela(1);
				lar.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
				lar.setDepesaReceita(DespesaReceita.RECEITA);
				lar.setCodigo(gerarCodigo("RDT"));
				lar.setDescricao("Rendimento da conta: " + contaBancaria.getContaFornecedor());
				lar.setDataEmissao(new Date());
				lar.setValorTotalComDesconto(ar.getValor());
				lar.setValorTotalSemDesconto(ar.getValor());
				lar.setSolicitante(usuarioSessao.getUsuario().getColaborador());
				lar.setStatusCompra(StatusCompra.CONCLUIDO);
				lar.setVersionLancamento("MODE01");

				lar = pagService.salvarRendimento(lar);

				LancamentoAcao lancamentoAcao = new LancamentoAcao();
				lancamentoAcao.setLancamento(lar);
				lancamentoAcao.setData(new Date());
				lancamentoAcao.setValor(ar.getValor());
				lancamentoAcao.setDespesaReceita(DespesaReceita.RECEITA);

				RubricaOrcamento linhaOrcamento = orcService
						.buscarLinhaOrcamentoAlocRendimento(ar.getOrcamento().getId());

				if (linhaOrcamento == null) {
					linhaOrcamento = new RubricaOrcamento();
					linhaOrcamento.setOrcamento(ar.getOrcamento());
					linhaOrcamento.setTipo("rend");
					linhaOrcamento.setValor(BigDecimal.ZERO);
					linhaOrcamento.setComponente(orcService.buscarComponenteDefault());
					linhaOrcamento.setSubComponente(orcService.buscarSubComponenteDefault());
					linhaOrcamento.setRubrica(orcService.buscarRubricaAlocRendimentoDefault());
					linhaOrcamento = orcService.salvarRubricaOrcamento(linhaOrcamento);
				}

				lancamentoAcao.setRubricaOrcamento(linhaOrcamento);

				lancamentoAcao.setStatus(StatusPagamento.VALIDADO);

				lancamentoAcao = pagService.salvarLancamentoAcao(lancamentoAcao);

				PagamentoLancamento pagamentoLancamento = new PagamentoLancamento();
				pagamentoLancamento.setLancamentoAcao(lancamentoAcao);
				pagamentoLancamento.setContaRecebedor(ar.getContaAlocacao());
				pagamentoLancamento.setConta(contaBancaria);
				pagamentoLancamento.setQuantidadeParcela(1);
				pagamentoLancamento.setDataPagamento(new Date());
				pagamentoLancamento.setDataEmissao(new Date());
				pagamentoLancamento.setValor(ar.getValor());
				pagamentoLancamento.setStt(StatusPagamentoLancamento.PROVISIONADO);
				pagamentoLancamento.setQuantidadeParcela(1);
				pagamentoLancamento.setNumeroDaParcela(1);
				pagamentoLancamento.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
				pagamentoLancamento.setDespesaReceita(DespesaReceita.RECEITA);
				pagamentoLancamento.setTipoContaRecebedor(ar.getContaAlocacao().getTipo());
				pagamentoLancamento.setTipoContaPagador(contaBancaria.getTipo());

				pagService.salvarPagamentoUnico(pagamentoLancamento);

			}

			alocacaoRendimento = new AlocacaoRendimento();

			addMessage("", "Alocação efetuada com sucesso.", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			addMessage("", e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}

	@Transactional
	public void removerRendimento() {

		try {
			if (contaBancaria.getRendimentos() != null) {

				if (rendimentoConta.getId() != null) {
					service.removerRendimento(rendimentoConta);
					pagService.deletarRendimento(rendimentoConta.getId());
				}

				contaBancaria.getRendimentos().remove(rendimentoConta);
			}

			rendimentoConta = new RendimentoConta();

			addMessage("", "Rendimento removido com sucesso.", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			addMessage("", e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}

	@Transactional
	public void removerDoacao() {

		try {
			if (contaBancaria.getDoacoes() != null) {

				if (fonteDoacaoConta.getId() != null) {
					service.removerDoacao(fonteDoacaoConta);
				}

				contaBancaria.getDoacoes().remove(fonteDoacaoConta);
			}

			fonteDoacaoConta = new FonteDoacaoConta();

			addMessage("", "Doação removida com sucesso.", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			addMessage("", e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}

	@Transactional
	public void removerAlocacaoRendimento() {

		try {
			if (contaBancaria.getAlocacoesRendimentos() != null) {

				if (alocacaoRendimento.getId() != null) {
					service.removerAlocacaoRendimento(alocacaoRendimento);
					pagService.deletarRendimento(alocacaoRendimento.getId());
				}

				contaBancaria.getAlocacoesRendimentos().remove(alocacaoRendimento);
			}

			alocacaoRendimento = new AlocacaoRendimento();

			addMessage("", "Alocação removida com sucesso.", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			addMessage("", e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}

	private BigDecimal getValorTotalRendimentos() {

		BigDecimal total = BigDecimal.ZERO;


		/*
		 * if (contaBancaria.getRendimentos() == null) contaBancaria.setRendimentos(new
		 * ArrayList<>());
		 * 
		 * for (RendimentoConta r : contaBancaria.getRendimentos()) { total =
		 * total.add(r.getValor()); }
		 */

		return total;
	}

	private BigDecimal getValorTotalDoacoes() {

		BigDecimal total = BigDecimal.ZERO;

		/*
		 * if (contaBancaria.getDoacoes() == null) contaBancaria.setDoacoes(new
		 * ArrayList<>());
		 * 
		 * for (FonteDoacaoConta r : contaBancaria.getDoacoes()) { total =
		 * total.add(r.getValor()); }
		 */


		return total;
	}

	public String getTotalRendimentos() {

		Locale myLocale = new Locale("pt", "BR");
		NumberFormat format = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(myLocale));

		// BigDecimal rendimentos = getValorTotalRendimentos();

		return format.format(BigDecimal.ZERO);
	}

	public String getTotalDoacoes() {

		Locale myLocale = new Locale("pt", "BR");
		NumberFormat format = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(myLocale));

		// BigDecimal doacoes = getValorTotalDoacoes();

		return format.format(BigDecimal.ZERO);
	}

	public BigDecimal getValotTotalAlocacoes() {
		BigDecimal total = BigDecimal.ZERO;

		/*
		 * if (contaBancaria.getAlocacoesRendimentos() == null)
		 * contaBancaria.setAlocacoesRendimentos(new ArrayList<>());
		 * 
		 * for (AlocacaoRendimento a : contaBancaria.getAlocacoesRendimentos()) { total
		 * = total.add(a.getValor()); }
		 */

		//Locale myLocale = new Locale("pt", "BR");
		//NumberFormat format = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(myLocale));

		//BigDecimal doacoes = getValorTotalDoacoes();

		return total;
	}


	public String getTotalAlocacoes() {

		Locale myLocale = new Locale("pt", "BR");
		NumberFormat format = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(myLocale));

		// BigDecimal total = getValotTotalAlocacoes();

		return format.format(BigDecimal.ZERO);
	}

	public BigDecimal getValorTotalAplicacoes() {
		BigDecimal total = BigDecimal.ZERO;

		/*
		 * if (aplicacoes == null) aplicacoes = new ArrayList<>();
		 * 
		 * for (AplicacaoRecurso a : aplicacoes) { total =
		 * total.add(a.getValorTotalComDesconto()); }
		 * 
		 * if (total.compareTo(BigDecimal.ZERO) == 1) total = total.divide(new
		 * BigDecimal(2));
		 */

		return total;
	}

	public String getTotalAplicacoes() {

		Locale myLocale = new Locale("pt", "BR");
		NumberFormat format = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(myLocale));
		// BigDecimal total = getValorTotalAplicacoes();

		return format.format(BigDecimal.ZERO);
	}

	public BigDecimal getValorTotalBaixas() {
		BigDecimal total = BigDecimal.ZERO;

		/*
		 * if (baixas == null) baixas = new ArrayList<>();
		 * 
		 * for (BaixaAplicacao a : baixas) { total =
		 * total.add(a.getValorTotalComDesconto()); }
		 * 
		 * if (total.compareTo(BigDecimal.ZERO) == 1) total = total.divide(new
		 * BigDecimal(2));
		 */

		return total;
	}

	public String getTotalBaixas() {

		// BigDecimal total = getValorTotalBaixas();
		Locale myLocale = new Locale("pt", "BR");
		NumberFormat format = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(myLocale));

		return format.format(BigDecimal.ZERO);
	}

	public BigDecimal getValorDisponivel() {
		/*BigDecimal totalRendimentos = BigDecimal.ZERO;
		BigDecimal totalAplicados = BigDecimal.ZERO;

		if (contaBancaria.getRendimentos() == null)
			contaBancaria.setRendimentos(new ArrayList<>());

		for (RendimentoConta r : contaBancaria.getRendimentos()) {
			totalRendimentos = totalRendimentos.add(r.getValor());
		}

		for (AlocacaoRendimento a : contaBancaria.getAlocacoesRendimentos()) {
			totalAplicados = totalAplicados.add(a.getValor());
		}*/

		return BigDecimal.ZERO;//totalRendimentos.subtract(totalAplicados);
	}

	public String getTotalDisponivel() {

		Locale myLocale = new Locale("pt", "BR");
		NumberFormat format = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(myLocale));

		//BigDecimal disponivel = getValorDisponivel();

		return format.format(BigDecimal.ZERO);
	}

	public String getSaldoInicial() {
		/*
		 * if (contaBancaria.getSaldoInicial() != null) { return
		 * format.format(contaBancaria.getSaldoInicial()); }
		 */

		Locale myLocale = new Locale("pt", "BR");
		NumberFormat format = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(myLocale));

		return format.format(BigDecimal.ZERO);
	}

	public BigDecimal getValorOutrasReceitasConta() {
		BigDecimal total = BigDecimal.ZERO;
		/*
		 * if(contaBancaria.getId() == null) return total;
		 * 
		 * List<RelatorioContasAPagar> receitas =
		 * service.getOutrosPagamentosPorConta(contaBancaria.getId(), 1);
		 * 
		 * for (RelatorioContasAPagar r : receitas) { total = total.add(r.getValor()); }
		 */

		return total;
	}

	public BigDecimal getValorOutrasDespesasConta() {
		BigDecimal total = BigDecimal.ZERO;

		/*
		 * if (contaBancaria.getId() == null) return BigDecimal.ZERO;
		 * 
		 * List<RelatorioContasAPagar> despesas =
		 * service.getOutrosPagamentosPorConta(contaBancaria.getId(), 0);
		 * 
		 * for (RelatorioContasAPagar r : despesas) { total = total.add(r.getValor()); }
		 */

		return total;
	}

	public String getTotalOutrasDespesas() {

		Locale myLocale = new Locale("pt", "BR");
		NumberFormat format = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(myLocale));

		/*
		 * if (contaBancaria.getId() == null) return format.format(BigDecimal.ZERO);
		 * BigDecimal total = getValorOutrasDespesasConta();
		 */
		return format.format(BigDecimal.ZERO);
	}

	public String getTotalOutrasReceitas() {

		Locale myLocale = new Locale("pt", "BR");
		NumberFormat format = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(myLocale));

		/*
		 * if (contaBancaria.getId() == null) return format.format(BigDecimal.ZERO);
		 * 
		 * BigDecimal total = getValorOutrasReceitasConta();
		 */
		return format.format(BigDecimal.ZERO);
	}

	public String getSaldoTotal() {

		/*
		 * BigDecimal total = BigDecimal.ZERO; BigDecimal totalBaixas =
		 * getValorTotalBaixas(); BigDecimal totalAplicacoes =
		 * getValorTotalAplicacoes(); BigDecimal totalRendimentos =
		 * getValorTotalRendimentos(); BigDecimal totalDoacoes = getValorTotalDoacoes();
		 * BigDecimal saldoInicial = contaBancaria.getSaldoInicial() != null ?
		 * contaBancaria.getSaldoInicial() : BigDecimal.ZERO; BigDecimal totalAlocacoes
		 * = getValotTotalAlocacoes(); BigDecimal totalOutrasDespesas =
		 * getValorOutrasDespesasConta(); BigDecimal totalOutrasReceitas =
		 * getValorOutrasReceitasConta();
		 * 
		 * total =
		 * saldoInicial.add(totalDoacoes).subtract(totalBaixas).add(totalAplicacoes).add
		 * (totalRendimentos)
		 * .subtract(totalAlocacoes).add(totalOutrasReceitas).subtract(
		 * totalOutrasDespesas);
		 */

		Locale myLocale = new Locale("pt", "BR");
		NumberFormat format = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(myLocale));
		return format.format(BigDecimal.ZERO);
	}

	public String exportarExcel() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");

		List<RendimentoConta> rendimentos = new ArrayList<>();

		for (RendimentoConta r : contaBancaria.getRendimentos()) {
			if (r.getId() == null) {
				r.setDatapagamento(new SimpleDateFormat("dd/MM/yyyy").format(r.getDataPagamento()));
				r.setNomeusuario(r.getUsuario().getNomeUsuario());
				r.setFontedoacao(r.getOrcamento() != null ? r.getOrcamento().getTitulo() : "");
				r.setContarecebedora(r.getConta().getContaFornecedor());
				r.setContapagadora(r.getContaPagadora().getContaFornecedor());
				rendimentos.add(r);
			}

			rendimentos.addAll(service.obterRendimentosRelatorio(contaBancaria));
		}

		try {
			if (rendimentos.size() > 0) {
				JRDataSource dataSource = new JRBeanCollectionDataSource(rendimentos);
				JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/rendimentos_conta_xls.jrxml");
				JasperReport report = JasperCompileManager.compileReport(jd);
				ReportUtil.openReportXls("Rendimentos",
						"rendimentos_" + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()), report, null,
						dataSource);
			} else
				throw new Exception("Não há dados para exportar.");
		} catch (Exception e) {
			e.printStackTrace();
			addMessage("", e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}

		return "";
	}

	public String exportarPagamentosExcel() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");

		carregarTransacoes();

		try {
			if (listaDePagamentosRelatados.size() > 0) {
				JRDataSource dataSource = new JRBeanCollectionDataSource(listaDePagamentosRelatados);
				JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/pagamentos_conta_xls.jrxml");
				JasperReport report = JasperCompileManager.compileReport(jd);
				ReportUtil.openReportXls("Transações",
						"transacoes_" + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()), report, null,
						dataSource);
			} else
				throw new Exception("Não há dados para exportar.");
		} catch (Exception e) {
			e.printStackTrace();
			addMessage("", e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}

		return "";
	}

	@Transactional
	public void mudarStatus(ContaBancaria contaBancaria) {
		contaBancaria = service.getContaBancaria(contaBancaria.getId());
		if (contaBancaria.getStatus() == null || contaBancaria.getStatus().equals(StatusConta.ATIVA)) {
			contaBancaria.setStatus(StatusConta.INATIVA);
		} else {
			contaBancaria.setStatus(StatusConta.ATIVA);
		}
		service.salvarContaBancaria(contaBancaria);
		carregarContas();

	}

	public void inserirBaixaAplicacao(ContaBancaria conta1, ContaBancaria conta2) {
		baixaAplicacao = new BaixaAplicacao();
		baixaAplicacao.setContaPagador(conta1);
		baixaAplicacao.setContaRecebedor(conta2);
		baixaAplicacao.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
		baixaAplicacao.setQuantidadeParcela(1);
		baixaAplicacao.setValorTotalComDesconto(valorAplicacao);
		baixaAplicacao.setDescricao("Baixa de aplicação");
		baixaAplicacao.setDataPagamento(dataAplicacao);
		baixaAplicacao.setIdContaAplicacao(contaBancaria.getId());
		baixaAplicacao.setOrcamento(orcamento);

		if (baixaAplicacao.getId() == null) {
			baixaAplicacao.setCodigo(gerarCodigo("SP"));
			baixaAplicacao.setSolicitante(usuarioSessao.getUsuario().getColaborador());
			baixaAplicacao.setDepesaReceita(DespesaReceita.RECEITA);
			baixaAplicacao.setLocalidade(custoService.findLocalidadeByIdDefault());
			baixaAplicacao.setGestao(custoService.findGestaoByIdDefault());
		}

		baixaAplicacao.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
		baixaAplicacao.setTipoGestao(TipoGestao.COORD);
		baixaAplicacao.setTipoLocalidade(TipoLocalidade.SEDE);
		baixaAplicacao.setVersionLancamento("MODE01");

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
		lancamentoAcao.setValor(baixaAplicacao.getValorTotalComDesconto());
		lancamentoAcao.setLancamento(baixaAplicacao);
		baixaAplicacao.getLancamentosAcoes().add(lancamentoAcao);
		lancamentoAcao = new LancamentoAcao();

		orcService.salvarBaixaAplicacao(baixaAplicacao, usuarioSessao.getUsuario());
	}

	public void inserirAplicacao(ContaBancaria conta1, ContaBancaria conta2) {
		aplicacao = new AplicacaoRecurso();
		aplicacao.setContaPagador(conta1);
		aplicacao.setContaRecebedor(conta2);
		aplicacao.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
		aplicacao.setQuantidadeParcela(1);
		aplicacao.setValorTotalComDesconto(valorAplicacao);
		aplicacao.setDescricao("Aplicação");
		aplicacao.setDataPagamento(dataAplicacao);
		aplicacao.setIdContaAplicacao(contaBancaria.getId());
		aplicacao.setOrcamento(orcamento);

		if (aplicacao.getId() == null) {
			aplicacao.setCodigo(gerarCodigo("SP"));
			aplicacao.setSolicitante(usuarioSessao.getUsuario().getColaborador());
			aplicacao.setDepesaReceita(DespesaReceita.RECEITA);
			aplicacao.setLocalidade(custoService.findLocalidadeByIdDefault());
			aplicacao.setGestao(custoService.findGestaoByIdDefault());
		}

		aplicacao.setTipoGestao(TipoGestao.COORD);
		aplicacao.setTipoLocalidade(TipoLocalidade.SEDE);
		aplicacao.setVersionLancamento("MODE01");

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
		lancamentoAcao.setValor(aplicacao.getValorTotalComDesconto());
		lancamentoAcao.setLancamento(aplicacao);
		aplicacao.getLancamentosAcoes().add(lancamentoAcao);
		lancamentoAcao = new LancamentoAcao();

		orcService.salvarAplicacao(aplicacao, usuarioSessao.getUsuario());

	}

	public void atualizarAplicacao() {
		ContaBancaria contaPagador = orcService.findContaTransitoria(new Long(5));
		if (aplicacao.getContaRecebedor().getId().longValue() == 5) {
			aplicacao.setContaPagador(contaBancaria);
			aplicacao.setContaRecebedor(contaPagador);
		} else if (aplicacao.getContaPagador().getId().longValue() == 5) {
			aplicacao.setContaPagador(contaPagador);
			aplicacao.setContaRecebedor(orcamento.getContaBancaria());
		}
		aplicacao.setValorTotalComDesconto(valorAplicacao);
		aplicacao.setDataPagamento(dataAplicacao);
		aplicacao.setIdContaAplicacao(contaBancaria.getId());
		aplicacao.setOrcamento(orcamento);

		orcService.atualizarAplicacao(aplicacao);
	}

	public void atualizarBaixa() {
		ContaBancaria contaPagador = orcService.findContaTransitoria(new Long(5));
		if (baixaAplicacao.getContaRecebedor().getId().longValue() == 5) {
			baixaAplicacao.setContaPagador(contaBancaria);
			baixaAplicacao.setContaRecebedor(contaPagador);
		} else if (baixaAplicacao.getContaPagador().getId().longValue() == 5) {
			baixaAplicacao.setContaPagador(contaPagador);
			baixaAplicacao.setContaRecebedor(orcamento.getContaBancaria());
		}
		baixaAplicacao.setValorTotalComDesconto(valorAplicacao);
		baixaAplicacao.setDataPagamento(dataAplicacao);
		baixaAplicacao.setIdContaAplicacao(contaBancaria.getId());
		baixaAplicacao.setOrcamento(orcamento);

		orcService.atualizarBaixa(baixaAplicacao);
	}

	public void salvarBaixaAplicacao() {
		try {

			BigDecimal totalBaixas = getValorTotalBaixas();
			BigDecimal totalAplicacoes = getValorTotalAplicacoes();

			totalBaixas = totalBaixas.add(valorAplicacao);

			if (totalBaixas.compareTo(totalAplicacoes) == 1)
				throw new Exception("O valor total de baixas é superior ao valor de aplicações.");

			if (!editarBaixa) {
				ContaBancaria contaPagador = orcService.findContaTransitoria(new Long(5));
				inserirBaixaAplicacao(contaBancaria, contaPagador);
				inserirBaixaAplicacao(contaPagador, orcamento.getContaBancaria());
			} else
				atualizarBaixa();
			carregarBaixas();
			orcamento = new Orcamento();
			baixaAplicacao = new BaixaAplicacao();
			lancamentoAcao = new LancamentoAcao();
			valorAplicacao = null;
			editarBaixa = false;

			addMessage("", "Baixa salva com sucesso.", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			addMessage("", "Não foi possível salvar a baixa: " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}

	public void salvarAplicacao() {

		try {

			if (!editarAplicacao) {
				ContaBancaria contaRecebedor = orcService.findContaTransitoria(new Long(5));
				inserirAplicacao(orcamento.getContaBancaria(), contaRecebedor);
				inserirAplicacao(contaRecebedor, contaBancaria);
			} else
				atualizarAplicacao();
			carregarAplicacoes();
			orcamento = new Orcamento();
			aplicacao = new AplicacaoRecurso();
			lancamentoAcao = new LancamentoAcao();
			valorAplicacao = null;
			editarAplicacao = false;

			addMessage("", "Aplicação salva com sucesso.", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			addMessage("", "Não foi possível salvar a aplicação: " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}

	public void editarAplicacao() {
		editarAplicacao = true;
		tipoAplicacao = 0;
		valorAplicacao = aplicacao.getValorTotalComDesconto();
		dataAplicacao = aplicacao.getDataPagamento();
		orcamento = aplicacao.getOrcamento();
	}

	public void editarBaixa() {
		editarBaixa = true;
		tipoAplicacao = 1;
		valorAplicacao = baixaAplicacao.getValorTotalComDesconto();
		dataAplicacao = baixaAplicacao.getDataPagamento();
		orcamento = baixaAplicacao.getOrcamento();
	}

	public void cancelarBaixaAplicacao() {
		orcamento = new Orcamento();
		aplicacao = new AplicacaoRecurso();
		lancamentoAcao = new LancamentoAcao();
		valorAplicacao = null;
		editarAplicacao = false;
		editarBaixa = false;
		dataAplicacao = new Date();
	}

	public void addMessage(String summary, String detail, FacesMessage.Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuFinanceiro();
	}

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public ContaBancaria getContaBancaria() {
		return contaBancaria;
	}

	public void setContaBancaria(ContaBancaria contaBancaria) {
		this.contaBancaria = contaBancaria;
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

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}

	public List<RelatorioContasAPagar> getListaDePagamentosRelatados() {
		return listaDePagamentosRelatados;
	}

	public void setListaDePagamentosRelatados(List<RelatorioContasAPagar> listaDePagamentosRelatados) {
		this.listaDePagamentosRelatados = listaDePagamentosRelatados;
	}

	public RendimentoConta getRendimentoConta() {
		return rendimentoConta;
	}

	public void setRendimentoConta(RendimentoConta rendimentoConta) {
		this.rendimentoConta = rendimentoConta;
	}

	public List<ContaBancaria> getContasPagadoras() {
		return contasPagadoras;
	}

	public void setContasPagadoras(List<ContaBancaria> contasPagadoras) {
		this.contasPagadoras = contasPagadoras;
	}

	public List<Orcamento> getOrcamentos() {
		return orcamentos;
	}

	public void setOrcamentos(List<Orcamento> orcamentos) {
		this.orcamentos = orcamentos;
	}

	public AlocacaoRendimento getAlocacaoRendimento() {
		return alocacaoRendimento;
	}

	public void setAlocacaoRendimento(AlocacaoRendimento alocacaoRendimento) {
		this.alocacaoRendimento = alocacaoRendimento;
	}

	public List<ContaBancaria> getContasAlocacao() {
		return contasAlocacao;
	}

	public void setContasAlocacao(List<ContaBancaria> contasAlocacao) {
		this.contasAlocacao = contasAlocacao;
	}

	public Integer getTipoAplicacao() {
		return tipoAplicacao;
	}

	public void setTipoAplicacao(Integer tipoAplicacao) {
		this.tipoAplicacao = tipoAplicacao;
	}

	public Date getDataAplicacao() {
		return dataAplicacao;
	}

	public void setDataAplicacao(Date dataAplicacao) {
		this.dataAplicacao = dataAplicacao;
	}

	public BigDecimal getValorAplicacao() {
		return valorAplicacao;
	}

	public void setValorAplicacao(BigDecimal valorAplicacao) {
		this.valorAplicacao = valorAplicacao;
	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public List<BaixaAplicacao> getBaixas() {
		return baixas;
	}

	public void setBaixas(List<BaixaAplicacao> baixas) {
		this.baixas = baixas;
	}

	public List<AplicacaoRecurso> getAplicacoes() {
		return aplicacoes;
	}

	public void setAplicacoes(List<AplicacaoRecurso> aplicacoes) {
		this.aplicacoes = aplicacoes;
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

	public FonteDoacaoConta getFonteDoacaoConta() {
		return fonteDoacaoConta;
	}

	public void setFonteDoacaoConta(FonteDoacaoConta fonteDoacaoConta) {
		this.fonteDoacaoConta = fonteDoacaoConta;
	}
}
