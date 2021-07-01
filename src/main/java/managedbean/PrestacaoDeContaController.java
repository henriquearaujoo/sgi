package managedbean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;

import model.Adiantamento;
import model.Aprouve;
import model.AtividadeProjeto;
import model.CategoriaDespesaClass;
import model.CategoriaFinanceira;
import model.ContaBancaria;
import model.DespesaReceita;
import model.LancamentoAcao;
import model.LancamentoAuxiliar;
import model.LancamentoAvulso;
import model.MenuLateral;
import model.PagamentoLancamento;
import model.Projeto;
import model.ProjetoRubrica;
import model.Reembolso;
import model.StatusCompra;
import model.TipoDeDocumentoFiscal;
import model.TipoParcelamento;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import repositorio.CalculatorRubricaRepositorio;
import service.PagamentoService;
import service.PrestacaoContaService;
import service.ProjetoService;
import util.DataUtil;
import util.Filtro;
import util.MakeMenu;
import util.ReportUtil;
import util.UsuarioSessao;

@Named(value = "prestacao_conta_controller")
@ViewScoped
public class PrestacaoDeContaController implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	private Filtro filtro = new Filtro();
	private MenuLateral menu = new MenuLateral();

	private @Inject LancamentoAvulso prestacaoDeContas;
	private @Inject LancamentoAcao lancamentoAcao;
	private @Inject LancamentoAcao lancamentoAcaoInsereReembolso;
	private @Inject PrestacaoContaService service;
	private @Inject UsuarioSessao usuarioSessao;

	private Boolean exigeNF;

	private List<LancamentoAvulso> listPrestacoes = new ArrayList<>();
	private List<Adiantamento> listAdiantamentos = new ArrayList<>();

	private Long idAdiantamento;
	private Long idConta;
	private BigDecimal totalSaida = BigDecimal.ZERO;
	private BigDecimal totalEntrada = BigDecimal.ZERO;

	private Projeto projetoAux;
	private List<Projeto> projetos = new ArrayList<>();

	private Integer stepIndex = 0;

	private LancamentoAuxiliar adiantamento = new LancamentoAuxiliar();

	private List<CategoriaFinanceira> listCategoriaFinanceira = new ArrayList<>();

	@Inject
	private CalculatorRubricaRepositorio calculatorRubricaRepositorio;

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}

	public String voltarListagem() {
		return "adiantamentos?faces-redirect=true";
	}

	public void steping(String modo) {

		if (modo.equalsIgnoreCase("next")) {
			if (stepIndex == 1) {
				return;
			}

			stepIndex++;
		} else {
			if (stepIndex == 0) {
				return;
			}
			stepIndex--;
		}
	}

	@PostConstruct
	public void init() {
		filtro = new Filtro();
		filtro.setIdConta(new Long(0));
		filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		filtro.setDataFinal(DataUtil.getDataFinal(new Date()));
		exigeNF = true;
		carregarCategoriasFin();
	}

	public void carregarCategoriasFin() {
		listCategoriaFinanceira = service.buscarCategoriasFin();
	}

	private List<ProjetoRubrica> listaDeRubricasProjeto = new ArrayList<>();
	private List<AtividadeProjeto> listaAtividades;

	public void listaRubricasAndAtividades() {

		if (projetoAux == null) {
			lancamentoAcao = new LancamentoAcao();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você deve selecionar um projeto antes de buscar rubrica! ");
			FacesContext.getCurrentInstance().addMessage(null, message);
			FacesContext.getCurrentInstance().addMessage(null, message);
			listaDeRubricasProjeto = new ArrayList<ProjetoRubrica>();
			setListaAtividades(new ArrayList<AtividadeProjeto>());
		} else {
			listaDeRubricasProjeto = calculatorRubricaRepositorio.getProjetoRubricaByProjeto(projetoAux.getId());
			// findAtividadesByProjetoWithSaldo();
		}
	}
	
//	public void dialogHelp() {
//		HashMap<String, Object> options = new HashMap<>();
//		options.put("width", "100%");
//		options.put("height", "100%");
//		options.put("contentWidth", "100%");
//		options.put("contentHeight", "100%");
//		options.put("resizable", false);
//		options.put("minimizable",true);
//		options.put("maximizable",true);
//		PrimeFaces.current().dialog().openDynamic("dialog/help/help_prestacao_de_contas", options, null);
//	}

	public void verificarExigencia() {

		if (prestacaoDeContas.getTipoDocumentoFiscal() == null) {
			exigeNF = true;
			return;
		}

		switch (prestacaoDeContas.getTipoDocumentoFiscal()) {
		case OUTRO:
			exigeNF = false;
			break;
		case RECIBO:
			exigeNF = false;
			break;
		default:
			exigeNF = true;
			break;
		}
	}

	public Boolean verificarExigenciaV2() {

		if (prestacaoDeContas.getTipoDocumentoFiscal() == null) {
			return true;
		}

		switch (prestacaoDeContas.getTipoDocumentoFiscal()) {
		case OUTRO:
			return false;
		case RECIBO:
			return false;
		default:
			return true;
		}
	}

	private LancamentoAvulso devolucao;
	private LancamentoAvulso reembolso;

	public void gerarRelatorioAdiantamento() {

		BigDecimal totalEntrada = BigDecimal.ZERO;
		BigDecimal totalSaida = BigDecimal.ZERO;
		String conta = "";

		// filtro.setNumeroDocumento(idAdiantamento.toString());

		List<LancamentoAvulso> mListAux = service.getPrestacoes(idAdiantamento);

		List<LancamentoAvulso> mList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		BigDecimal saldo = BigDecimal.ZERO;

		for (LancamentoAvulso lancamentoAvulso : mListAux) {
			LancamentoAvulso l = new LancamentoAvulso();



			l.setDtEmissao(sdf.format(lancamentoAvulso.getDataEmissao()));
			l.setDtPagto(sdf.format(lancamentoAvulso.getDataPagamento()));
			l.setDescricao(lancamentoAvulso.getDescricao());
			l.setNumeroDocumento(lancamentoAvulso.getNumeroDocumento());
			l.setPagador(lancamentoAvulso.getPagador());
			l.setRecebedor(lancamentoAvulso.getRecebedor());
			l.setAcao(lancamentoAcao.getProjetoRubrica().getProjeto().getNome());

			if (lancamentoAvulso.getTipoLancamento() == null || lancamentoAvulso.getTipoLancamento().equals("")) {
				l.setEntrada(BigDecimal.ZERO);
				l.setSaida(lancamentoAvulso.getValorTotalComDesconto());
			}

			if (lancamentoAvulso.getTipoLancamento().equals("ad")) {
				conta = l.getRecebedor();
				l.setEntrada(lancamentoAvulso.getValorTotalComDesconto());
				l.setSaida(BigDecimal.ZERO);
			}

			if (lancamentoAvulso.getTipoLancamento().equals("reenb")) {
				conta = l.getRecebedor();
				l.setEntrada(lancamentoAvulso.getValorTotalComDesconto());
				l.setSaida(BigDecimal.ZERO);
			}

			if (lancamentoAvulso.getTipoLancamento().equals("dev")) {
				l.setEntrada(BigDecimal.ZERO);
				l.setSaida(lancamentoAvulso.getValorTotalComDesconto());
			}

			totalEntrada = totalEntrada.add(l.getEntrada());
			totalSaida = totalSaida.add(l.getSaida());

			saldo = saldo.add(l.getEntrada()).subtract(l.getSaida());
			l.setSaldo(saldo);
			mList.add(l);
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		String logo = path + "resources/image/logoFas.gif";

		Map parametros = new HashMap();
		parametros.put("logo", logo);
		parametros.put("contabancaria", conta);
		parametros.put("TOTAL_ENTRADA", totalEntrada);
		parametros.put("TOTAL_SAIDA", totalSaida);
		parametros.put("saldoFinal", totalEntrada.subtract(totalSaida));
		parametros.put("usuario",
				usuarioSessao.getNomeUsuario() + " " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));

		JRDataSource dataSource = new JRBeanCollectionDataSource(mList, false);

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/PrestacaoContas.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReport("Adiantamento", "Adiantamento" + filtro.getNumeroDocumento(), report, parametros,
					dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public TipoDeDocumentoFiscal[] getTipoDocumentoFiscal() {
		return TipoDeDocumentoFiscal.values();
	}

	public Boolean verificarCamposDePrestacao() {

		Boolean faltaCampo = false;

		if (prestacaoDeContas.getTipoDocumentoFiscal() == null) {
			addMessage("", "Preencha o campo 'Documento fiscal'.", FacesMessage.SEVERITY_ERROR);
			// closeDialogLoading();
			faltaCampo = true;
		}

		if (verificarExigenciaV2()) {
			if (prestacaoDeContas.getNotaFiscal() == null || prestacaoDeContas.getNotaFiscal().equals("")) {
				addMessage("", "Preencha o campo 'Nota fiscal'.", FacesMessage.SEVERITY_ERROR);
				// closeDialogLoading();
				faltaCampo = true;
			}
		}

		if (prestacaoDeContas.getContaRecebedor() == null) {
			addMessage("", "Preencha o campo 'Favorecido'.", FacesMessage.SEVERITY_ERROR);
			// closeDialogLoading();
			faltaCampo = true;
		}

		if (prestacaoDeContas.getDescricao() == null || prestacaoDeContas.getDescricao().equals("")) {
			addMessage("", "Preencha o campo 'Descrição'.", FacesMessage.SEVERITY_ERROR);
			// closeDialogLoading();
			faltaCampo = true;
		}

		if (prestacaoDeContas.getDataPagamentoPrestacao() == null) {
			addMessage("", "Preencha o campo 'Data de pagamento'.", FacesMessage.SEVERITY_ERROR);
			// closeDialogLoading();
			faltaCampo = true;
		}

		if (prestacaoDeContas.getValorTotalComDesconto() == null) {
			addMessage("", "Preencha o campo 'Valor'.", FacesMessage.SEVERITY_ERROR);
			// closeDialogLoading();
			faltaCampo = true;
		}

		return faltaCampo;

	}

	public void prepararDevolucao(BigDecimal valorDevolucao) {

		if (!(valorDevolucao.compareTo(BigDecimal.ZERO) > 0)) {
			addMessage("", "Não há valores para devolver.", FacesMessage.SEVERITY_ERROR);
			return;
		}

		PagamentoLancamento pgto = service.buscarPagamentoByLancamentoAcao(lancamentoAcao.getId());

		if (pgto == null) {
			addMessage("",
					"Não existem pagamentos provisionados para esse adiantamento, por favor contate o setor financeiro.",
					FacesMessage.SEVERITY_ERROR);
			return;
		}

		devolucao = new LancamentoAvulso();
		devolucao.setDescricao("Devolução de recurso de adiantamento");
		devolucao.setContaPagador(adiantamento.getContaRecebedor());
		devolucao.setContaRecebedor(pgto.getConta());
		devolucao.setTipoLancamento("dev");
		devolucao.setValorTotalComDesconto(valorDevolucao);
		devolucao.setDepesaReceita(DespesaReceita.RECEITA);
		devolucao.setNumeroDocumento(adiantamento.getNumeroDocumento());
		devolucao.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
		devolucao.setQuantidadeParcela(1);
		devolucao.setIdAdiantamento(idAdiantamento);
		devolucao.setDataEmissao(new Date());
		devolucao.setDataPagamento(new Date());
		devolucao.setStatusCompra(StatusCompra.CONCLUIDO);

		LancamentoAcao la = new LancamentoAcao();

		// la.setProjeto(lancamentoAcao.getProjeto());
		// la.setOrcamento(lancamentoAcao.getOrcamento());
		la.setProjetoRubrica(lancamentoAcao.getProjetoRubrica());
		la.setValor(devolucao.getValorTotalComDesconto());
		la.setDespesaReceita(DespesaReceita.DESPESA);

		// la.setAcao(lancamentoAcao.getAcao());
		// la.setFontePagadora(lancamentoAcao.getFontePagadora());
		// la.setValor(devolucao.getValorTotalComDesconto());

		devolucao.setLancamentosAcoes(new ArrayList<>());
		devolucao.getLancamentosAcoes().add(la);

		abreDialog("PF('dlg_devolucao').show();");

	}

	@Inject
	private ProjetoService projetoService;

	public void carregarProjetos() {

		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro")) {
			projetos = projetoService.getProjetosbyUsuarioProjeto(null, filtro);
			return;
		}

		projetos = projetoService.getProjetosbyUsuarioProjeto(usuarioSessao.getUsuario(), filtro);

	}

	public void prepararReembolso(BigDecimal valorReembolso) {

		if (!(valorReembolso.abs().compareTo(BigDecimal.ZERO) > 0)) {
			addMessage("", "Não há valores para reembolsar.", FacesMessage.SEVERITY_ERROR);
			return;
		}

		PagamentoLancamento pgto = service.buscarPagamentoByLancamentoAcao(lancamentoAcao.getId());

		if (pgto == null) {
			addMessage("",
					"Não existem pagamentos provisionados para esse adiantamento, por favor contate o setor financeiro.",
					FacesMessage.SEVERITY_ERROR);
			return;
		}

		lancamentoAcaoInsereReembolso.setProjetoRubrica(lancamentoAcao.getProjetoRubrica());
		lancamentoAcaoInsereReembolso.setValor(valorReembolso.abs());

		reembolso = new LancamentoAvulso();
		reembolso.setDescricao("Reembolso de despesa");
		reembolso.setTipoLancamento("reenb");
		reembolso.setContaPagador(pgto.getConta());
		reembolso.setContaRecebedor(adiantamento.getContaRecebedor());
		reembolso.setValorTotalComDesconto(valorReembolso.abs());
		reembolso.setDepesaReceita(DespesaReceita.DESPESA);
		reembolso.setNumeroDocumento(adiantamento.getNumeroDocumento());
		reembolso.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
		reembolso.setQuantidadeParcela(1);
		reembolso.setIdAdiantamento(idAdiantamento);
		reembolso.setDataEmissao(new Date());
		reembolso.setDataPagamento(new Date());
		reembolso.setStatusCompra(StatusCompra.CONCLUIDO);
		reembolso.setLancamentosAcoes(new ArrayList<>());

		if (!calculatorRubricaRepositorio.verificarSaldoRubrica(
				lancamentoAcaoInsereReembolso.getProjetoRubrica().getId(), valorReembolso.abs())) {
			carregarProjetos();
			lancamentoAcaoInsereReembolso = new LancamentoAcao();
			valorTotalAReembolsar = reembolso.getValorTotalComDesconto();
			abreDialog("PF('set_budget').show();");
			addMessage("",
					"Não há saldo no recurso inserido para custeio da despesa, por favor escolha outro centro de custo",
					FacesMessage.SEVERITY_WARN);
			return;
		}

		LancamentoAcao la = new LancamentoAcao();
		la.setProjetoRubrica(lancamentoAcaoInsereReembolso.getProjetoRubrica());
		la.setValor(reembolso.getValorTotalComDesconto());
		valorTotalAReembolsar = reembolso.getValorTotalComDesconto();
		la.setDespesaReceita(DespesaReceita.DESPESA);
		reembolso.getLancamentosAcoes().add(la);
		abreDialog("PF('dlg_reembolso').show();");
	}

	public void salvarDevolucao() {
		devolucao.setVersionLancamento("MODE01");
		service.salvarLancamentoAvulso(devolucao, usuarioSessao.getUsuario());
		devolucao = new LancamentoAvulso();
		carregaPrestacoes(idAdiantamento, adiantamento.getContaRecebedor().getId());
		carregarValores();
	}

	public void salvarReembolso() {

		if (compareValorRembolso()) {
			return;
		}

		reembolso.setVersionLancamento("MODE01");
		service.salvarLancamentoAvulso(reembolso, usuarioSessao.getUsuario());
		reembolso = new LancamentoAvulso();
		carregaPrestacoes(idAdiantamento, adiantamento.getContaRecebedor().getId());
		carregarValores();
		PrimeFaces.current().executeScript("PF('set_budget').hide()");

	}

	public boolean compareValorRembolso() {

		BigDecimal totaisDaLista = BigDecimal.ZERO;

		for (LancamentoAcao la : reembolso.getLancamentosAcoes()) {
			totaisDaLista = totaisDaLista.add(la.getValor());
		}

		if (!(totaisDaLista.compareTo(reembolso.getValorTotalComDesconto()) == 0)) {
			addMessage("",
					"Hum... Parece que você adicionou menos recursos do que o reembolso necessita, por favor insira os valores corretos",
					FacesMessage.SEVERITY_ERROR);
			totaisDaLista = BigDecimal.ZERO;
			return true;
		}

		return false;
	}

	public void validarAdiantamento() {
		if (verificarPrivilegioValidacao()) {
			service.validarAdiantamentoComLog(adiantamento.getId(), usuarioSessao.getUsuario());
			carregarLancamento();
			carregaPrestacoes(idAdiantamento, adiantamento.getContaRecebedor().getId());
			service.validarLancamentosParaConcilicacao(adiantamento.getId());
			service.validarLancamentosParaConcilicacao(listPrestacoes);

			// for(LancamentoAvulso l: listPrestacoes) {
			// service.validarAdiantamentoComLog(l.getId(),usuarioSessao.getUsuario());
			// }

			carregarValores();
		} else {
			addMessage("",
					"Você não pode validar um adiantamento, por favor entre em contato com o setor financeiro, com os documentos de prestação de contas",
					FacesMessage.SEVERITY_WARN);
			return;
		}
	}

	private void abreDialog(String command) {
		PrimeFaces.current().executeScript(command);
	}

	public void analisarAdiantamento() {
		if (verificarPrivilegiOParaAnalise()) {
			service.analisarAdiantamento(adiantamento.getId());
			carregarLancamento();
			carregaPrestacoes(idAdiantamento, adiantamento.getContaRecebedor().getId());
			carregarValores();

		} else {

			addMessage("",
					"Você não pode mandar um adiantamento para análise, por favor entre em contato com o setor financeiro, com os documentos de prestação de contas",
					FacesMessage.SEVERITY_WARN);
			return;
		}
	}

	public void devolverAdiantamento() {
		service.devolverAdiantamento(adiantamento.getId());
		carregarLancamento();
		carregaPrestacoes(idAdiantamento, adiantamento.getContaRecebedor().getId());
		carregarValores();

	}

	public void carregarDetalheDoAdiantamento() {
		carregarLancamento();
		carregaPrestacoes(idAdiantamento, adiantamento.getContaRecebedor().getId());
		carregarValores();
		carregarLancamentoAcao();
	}

	public Boolean verificaCamposRecurso() {

		Boolean retorno = false;

		if (lancamentoAcaoInsereReembolso.getProjetoRubrica() == null) {
			addMessage("", "Preencha o campo 'Linha orçamentária' antes de prosseguir.", FacesMessage.SEVERITY_ERROR);
			retorno = true;
		}

		if (lancamentoAcaoInsereReembolso.getValor() == null) {
			addMessage("", "Preencha o campo 'Valor' antes de prosseguir.", FacesMessage.SEVERITY_ERROR);
			retorno = true;
		}

		return retorno;
	}

	private @Inject PagamentoService serviceP;

	public void removerAcao(LancamentoAcao lancamentoAcao) {
		reembolso.getLancamentosAcoes().remove(lancamentoAcao);
	}

	private BigDecimal valorTotalAReembolsar = BigDecimal.ZERO;

	public void adicionarNovaAcao() {

		if (verificaCamposRecurso()) {
			return;
		}

		BigDecimal totaisDaLista = lancamentoAcaoInsereReembolso.getValor();

		if (!calculatorRubricaRepositorio.verificarSaldoRubrica(
				lancamentoAcaoInsereReembolso.getProjetoRubrica().getId(), lancamentoAcaoInsereReembolso.getValor())) {
			addMessage("",
					"Não há saldo no recurso inserido para custeio da despesa, por favor escolha outro centro de custo",
					FacesMessage.SEVERITY_WARN);
			return;
		}

		for (LancamentoAcao la : reembolso.getLancamentosAcoes()) {
			if (la.getProjetoRubrica().equals(lancamentoAcaoInsereReembolso.getProjetoRubrica())) {
				addMessage("", "Não é Permitido Adição de Duas Linhas Orçamentárias Iguais.",
						FacesMessage.SEVERITY_WARN);
				totaisDaLista = BigDecimal.ZERO;
				return;
			}

			totaisDaLista = totaisDaLista.add(la.getValor());
		}

		if (totaisDaLista.compareTo(reembolso.getValorTotalComDesconto()) > 0) {
			addMessage("", "Valor inserido maior que reembolso previsto", FacesMessage.SEVERITY_ERROR);
			totaisDaLista = BigDecimal.ZERO;
			return;
		}

		lancamentoAcaoInsereReembolso.setDespesaReceita(DespesaReceita.DESPESA);
		// lancamentoAcao.setProjeto(lancamentoAcao.getProjetoRubrica().getProjeto());
		// lancamentoAcao.setOrcamento(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento());
		lancamentoAcaoInsereReembolso.setLancamento(reembolso);
		reembolso.getLancamentosAcoes().add(lancamentoAcaoInsereReembolso);

		lancamentoAcaoInsereReembolso = new LancamentoAcao();
	}

	public boolean verificarPrivilegioValidacao() {
		Aprouve ap = new Aprouve();
		ap.setSigla("VALID_ADT");
		ap.setUsuario(usuarioSessao.getUsuario());
		return service.findAprouve(ap);
	}

	public boolean verificarPrivilegioADT() {
		Aprouve ap = new Aprouve();
		ap.setSigla("PRV_ADT");
		ap.setUsuario(usuarioSessao.getUsuario());
		return service.findAprouve(ap);
	}

	public boolean verificarPrivilegiOParaAnalise() {
		Aprouve ap = new Aprouve();
		ap.setSigla("ANALISE_ADT");
		ap.setUsuario(usuarioSessao.getUsuario());
		return service.findAprouve(ap);
	}

	public void carregarValores() {
		totalEntrada = BigDecimal.ZERO;
		totalSaida = BigDecimal.ZERO;
		for (LancamentoAvulso prestacao : listPrestacoes) {

			if (prestacao.getContaPagador().getId().longValue() == adiantamento.getContaRecebedor().getId()
					.longValue()) {
				totalSaida = totalSaida.add(prestacao.getValorTotalComDesconto());
				prestacao.setContaEnvolvida(prestacao.getContaRecebedor().getNomeConta());
				prestacao.setSaidaPrestacao(true);
			} else {
				prestacao.setSaidaPrestacao(false);
				prestacao.setContaEnvolvida(prestacao.getContaPagador().getNomeConta());
				totalEntrada = totalEntrada.add(prestacao.getValorTotalComDesconto());
			}
		}
	}

	public void editarPrestacao() {

	}

	public void carregarLancamentoAcao() {
		lancamentoAcao = service.buscarLancamentoAcao(idAdiantamento);
	}

	public void carregarLancamento() {
		adiantamento = service.getLancamentoById(idAdiantamento);
	}

	public void salvarPrestacao() {

		if (prestacaoDeContas.getNotaFiscal() != null && !prestacaoDeContas.getNotaFiscal().equals("")) {
			Long id = new Long(0);

			if (prestacaoDeContas.getId() != null)
				id = prestacaoDeContas.getId();

			LancamentoAvulso l = service.getLancamentoByNF(prestacaoDeContas.getNotaFiscal(), id,
					prestacaoDeContas.getContaRecebedor().getId());
			if (l != null) {
				addMessage("", "Nota fiscal já foi paga no lançamento de número: " + l.getId(),
						FacesMessage.SEVERITY_WARN);
				l = null;

				return;
			}

		}

		if (verificarCamposDePrestacao()) {
			return;
		}

		if (prestacaoDeContas.getDataPagamentoPrestacao().before(adiantamento.getDataPagamento())) {
			addMessage("", "Não é possível incluir uma prestação  de contas com data abaixo da data do adiantamento",
					FacesMessage.SEVERITY_ERROR);
			return;
		}

		LancamentoAcao la = new LancamentoAcao();
		// la.setProjeto(lancamentoAcao.getProjeto());
		// la.setOrcamento(lancamentoAcao.getOrcamento());
		la.setProjetoRubrica(lancamentoAcao.getProjetoRubrica());
		la.setValor(prestacaoDeContas.getValorTotalComDesconto());
		la.setDespesaReceita(DespesaReceita.DESPESA);

		// la.setAcao(lancamentoAcao.getAcao());
		// la.setFontePagadora(lancamentoAcao.getFontePagadora());
		// la.setValor(prestacaoDeContas.getValorTotalComDesconto());
		prestacaoDeContas.setVersionLancamento("MODE01");
		prestacaoDeContas.setContaPagador(adiantamento.getContaRecebedor());
		prestacaoDeContas.setDepesaReceita(DespesaReceita.DESPESA);
		prestacaoDeContas.setNumeroDocumento(adiantamento.getNumeroDocumento());
		prestacaoDeContas.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
		prestacaoDeContas.setQuantidadeParcela(1);
		prestacaoDeContas.setLancamentosAcoes(new ArrayList<>());
		prestacaoDeContas.getLancamentosAcoes().add(la);
		prestacaoDeContas.setIdAdiantamento(idAdiantamento);
		prestacaoDeContas.setDataEmissao(new Date());
		prestacaoDeContas.setDataPagamento(new Date());
		prestacaoDeContas.setStatusCompra(StatusCompra.CONCLUIDO);
		service.salvarLancamentoAvulso(prestacaoDeContas, usuarioSessao.getUsuario());

		prestacaoDeContas = new LancamentoAvulso();

		carregaPrestacoes(idAdiantamento, adiantamento.getContaRecebedor().getId());
		carregarValores();

	}

	public void removerPrestacao() {
		service.removerLancamento(prestacaoDeContas);
		carregaPrestacoes(idAdiantamento, adiantamento.getContaRecebedor().getId());
		carregarValores();
		prestacaoDeContas = new LancamentoAvulso();
	}

	public void carregaPrestacoes(Long idAdiantamento, Long idConta) {
		listPrestacoes = service.buscarPrestacaoDeContas(idAdiantamento, idConta);
	}

	public List<CategoriaDespesaClass> completeCategoria(String query) {
		return service.getCategoriaAutoComplete(query);
	}

	public void carregarAdiantamentos() {

		if (filtro.getContaFiltro() == null) {
			filtro.setContaFiltro(new ContaBancaria());
			listAdiantamentos = service.buscarAdiantamentos(filtro.getContaFiltro().getId());
		}
		listAdiantamentos = service.buscarAdiantamentos(filtro.getContaFiltro().getId());

	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuFinanceiro();
	}

	public List<Adiantamento> getListAdiantamentos() {
		return listAdiantamentos;
	}

	public void setListAdiantamentos(List<Adiantamento> listAdiantamentos) {
		this.listAdiantamentos = listAdiantamentos;
	}

	public Long getIdConta() {
		return idConta;
	}

	public void setIdConta(Long idConta) {
		this.idConta = idConta;
	}

	public Long getIdAdiantamento() {
		return idAdiantamento;
	}

	public void setIdAdiantamento(Long idAdiantamento) {
		this.idAdiantamento = idAdiantamento;
	}

	public LancamentoAuxiliar getAdiantamento() {
		return adiantamento;
	}

	public void setAdiantamento(LancamentoAuxiliar adiantamento) {
		this.adiantamento = adiantamento;
	}

	public LancamentoAvulso getPrestacaoDeContas() {
		return prestacaoDeContas;
	}

	public void setPrestacaoDeContas(LancamentoAvulso prestacaoDeContas) {
		this.prestacaoDeContas = prestacaoDeContas;
	}

	public List<LancamentoAvulso> getListPrestacoes() {
		return listPrestacoes;
	}

	public void setListPrestacoes(List<LancamentoAvulso> listPrestacoes) {
		this.listPrestacoes = listPrestacoes;
	}

	public LancamentoAcao getLancamentoAcao() {
		return lancamentoAcao;
	}

	public void setLancamentoAcao(LancamentoAcao lancamentoAcao) {
		this.lancamentoAcao = lancamentoAcao;
	}

	public BigDecimal getTotalSaida() {
		return totalSaida;
	}

	public void setTotalSaida(BigDecimal totalSaida) {
		this.totalSaida = totalSaida;
	}

	public BigDecimal getTotalEntrada() {
		return totalEntrada;
	}

	public void setTotalEntrada(BigDecimal totalEntrada) {
		this.totalEntrada = totalEntrada;
	}

	public LancamentoAvulso getDevolucao() {
		return devolucao;
	}

	public void setDevolucao(LancamentoAvulso devolucao) {
		this.devolucao = devolucao;
	}

	public LancamentoAvulso getReembolso() {
		return reembolso;
	}

	public void setReembolso(LancamentoAvulso reembolso) {
		this.reembolso = reembolso;
	}

	public Boolean getExigeNF() {
		return exigeNF;
	}

	public void setExigeNF(Boolean exigeNF) {
		this.exigeNF = exigeNF;
	}

	public List<CategoriaFinanceira> getListCategoriaFinanceira() {
		return listCategoriaFinanceira;
	}

	public void setListCategoriaFinanceira(List<CategoriaFinanceira> listCategoriaFinanceira) {
		this.listCategoriaFinanceira = listCategoriaFinanceira;
	}

	public Integer getStepIndex() {
		return stepIndex;
	}

	public void setStepIndex(Integer stepIndex) {
		this.stepIndex = stepIndex;
	}

	public Projeto getProjetoAux() {
		return projetoAux;
	}

	public void setProjetoAux(Projeto projetoAux) {
		this.projetoAux = projetoAux;
	}

	public List<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}

	public List<ProjetoRubrica> getListaDeRubricasProjeto() {
		return listaDeRubricasProjeto;
	}

	public void setListaDeRubricasProjeto(List<ProjetoRubrica> listaDeRubricasProjeto) {
		this.listaDeRubricasProjeto = listaDeRubricasProjeto;
	}

	public List<AtividadeProjeto> getListaAtividades() {
		return listaAtividades;
	}

	public void setListaAtividades(List<AtividadeProjeto> listaAtividades) {
		this.listaAtividades = listaAtividades;
	}

	public BigDecimal getValorTotalAReembolsar() {
		return valorTotalAReembolsar;
	}

	public void setValorTotalAReembolsar(BigDecimal valorTotalAReembolsar) {
		this.valorTotalAReembolsar = valorTotalAReembolsar;
	}

	public LancamentoAcao getLancamentoAcaoInsereReembolso() {
		return lancamentoAcaoInsereReembolso;
	}

	public void setLancamentoAcaoInsereReembolso(LancamentoAcao lancamentoAcaoInsereReembolso) {
		this.lancamentoAcaoInsereReembolso = lancamentoAcaoInsereReembolso;
	}

}
