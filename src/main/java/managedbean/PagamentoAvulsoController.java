package managedbean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import org.jboss.weld.context.RequestContext;
import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.FileUploadEvent;

import model.Acao;
import model.Aprouve;
import model.CategoriaDespesaClass;
import model.CategoriadeDespesa;
import model.ContaBancaria;
import model.DespesaReceita;
import model.FontePagadora;
import model.Fornecedor;
import model.Lancamento;
import model.LancamentoAcao;
import model.LancamentoAvulso;
import model.MenuLateral;
import model.PagamentoLancamento;
import model.StatusCompra;
import model.TipoParcelamento;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import service.PagamentoService;
import util.ArquivoLancamento;
import util.DataUtil;
import util.DiretorioUtil;
import util.DownloadUtil;
import util.Filtro;
import util.MakeMenu;
import util.ReportUtil;
import util.UsuarioSessao;

@Named(value = "pagtoAvulsoController")
@ViewScoped
public class PagamentoAvulsoController implements Serializable {

	private static final long serialVersionUID = 1L;

	private @Inject LancamentoAvulso lancamento;

	private @Inject LancamentoAcao lancamentoAcao;

	private @Inject PagamentoService service;

	private List<LancamentoAvulso> lancamentos = new ArrayList<>();

	private List<LancamentoAvulso> selectedLancamentos = new ArrayList<>();

	private List<ContaBancaria> allContas = new ArrayList<>();

	private List<ContaBancaria> allContaFornecedor = new ArrayList<>();

	private List<FontePagadora> allFontes = new ArrayList<>();

	private List<Acao> allAcoes = new ArrayList<Acao>();

	private @Inject UsuarioSessao usuarioSessao;

	private @Inject Fornecedor fornecedor;

	private @Inject ContaBancaria conta;

	private @Inject Aprouve aprouve;

	private Lancamento lancamentoAux = new Lancamento();

	private @Inject ArquivoLancamento arqAuxiliar;

	private List<CategoriaDespesaClass> categorias = new ArrayList<>();

	private ContaBancaria selectedConta = new ContaBancaria();

	private TabView tabview;

	private List<LancamentoAvulso> lancamentosDeAdiantamento = new ArrayList<>();

	private List<PagamentoLancamento> pagamentos = new ArrayList<>();

	private List<ContaBancaria> contasFiltro = new ArrayList<>();

	private MenuLateral menu = new MenuLateral();

	private Filtro filtro = new Filtro();

	public List<ContaBancaria> getContasFiltro() {
		return contasFiltro;
	}

	public void setContasFiltro(List<ContaBancaria> contasFiltro) {
		this.contasFiltro = contasFiltro;
	}

	private String nomeArquivo = "";

	private String path = "";

	private ArquivoLancamento arquivo = new ArquivoLancamento();

	public ArquivoLancamento getArquivo() {
		return arquivo;
	}

	public void setArquivo(ArquivoLancamento arquivo) {
		this.arquivo = arquivo;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public PagamentoAvulsoController() {
	}

	@PostConstruct
	public void init() {
		filtro = new Filtro();
		filtro.setIdConta(new Long(0));
		filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		filtro.setDataFinal(DataUtil.getDataFinal(new Date()));
		carregarContas();
		carregarLancamentos();
		carregarCategorias();
	}

	public void carregarContas() {
		contasFiltro = service.getContasFilter();
	}

	public void carregarCategorias() {
		categorias = service.buscarCategorias();
	}

	public void carregarPagamentos() {
		pagamentos = service.buscarPagamentoByLancamento(lancamento.getId());
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		// String pathFinal = DiretorioUtil.DIRECTORY_UPLOAD_LINUX;
		String pathFinal = DiretorioUtil.DIRECTORY_UPLOAD;
		arquivo.setConteudo(event.getFile().getContents());
		arquivo.setPath(pathFinal + lancamentoAux.getId() + "." + event.getFile().getFileName());
		path = event.getFile().getFileName();
		arquivo.setData(new Date());

	}

	/*
	 * public void prepararEdicaoAcao(){ Acao ac = new Acao(); ac.setId(new
	 * Long(33)); List<Acao> acoesCompildas = service.getAcoes(); }
	 */

	public void removerArquivo() {
		if (verificarPrivilegioENF()) {
			service.removerArquivo(arqAuxiliar);
			arqAuxiliar = new ArquivoLancamento();
			carregarArquivos();
		} else {
			addMessage("", "Você não tem permissão para essa ação, entre em contato com o administrador do sistema.",
					FacesMessage.SEVERITY_WARN);
		}

	}

	public void carregarArquivos() {
		lancamentoAux = service.getLancamentoById(lancamentoAux.getId());
		lancamentoAux.setArquivos(new ArrayList<ArquivoLancamento>());
		lancamentoAux.setArquivos(service.getArquivoByLancamento(lancamentoAux.getId()));
	}

	public Boolean verificarPrivilegioENF() {
		aprouve.setSigla("ENF");
		aprouve.setUsuario(usuarioSessao.getUsuario());
		// aprouve.setSigla(compra.getGestao().getSigla());
		return service.verificarPrivilegioENF(aprouve);
	}

	public void limparLista() {
		lancamentos = new ArrayList<>();
		closeDialogLoading();
	}

	public void limparArquivo() {
		arquivo = new ArquivoLancamento();
	}

	public void adicionarArquivo() {

		arquivo.setUsuario(usuarioSessao.getUsuario());

		File file = new File(arquivo.getPath());
		FileOutputStream fot;
		try {
			fot = new FileOutputStream(file);
			fot.write(arquivo.getConteudo());
			fot.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		arquivo.setLancamento(lancamentoAux);
		// lancamentoAux.getArquivos().add(arquivo);
		service.salvarArquivo(arquivo);

		path = "";

		arquivo = new ArquivoLancamento();

		carregarArquivos();
	}

	public void visualizarArquivo() throws IOException {
		DownloadUtil.downloadFile(arqAuxiliar.getNome(), arqAuxiliar.getPath(), "application/pdf",
				FacesContext.getCurrentInstance());
		arqAuxiliar = new ArquivoLancamento();
	}

	public void carregarLancamentos() {
		lancamentos = service.getListaLancamentosAvulso(filtro);
		closeDialogLoading();

	}

	public List<ContaBancaria> completeConta(String query) {
		allContas = new ArrayList<>();
		allContas = service.getAllConta(query);
		return allContas;
	}

	public CategoriaDespesaClass getCategoria() {
		CategoriaDespesaClass c = new CategoriaDespesaClass();
		c.setId(new Long(34));
		c.setNome("Categoria de despesa");
		return c;
	}

	public void prepararFornecedor() {
		conta = new ContaBancaria();
		allContaFornecedor = service.getAllContaListaModal("");
	}

	public void excluirLancamentoAvulso(int index) {
		if (usuarioSessao.getNomeUsuario().equals("admin") || usuarioSessao.getNomeUsuario().equals("b.silva")
				|| usuarioSessao.getNomeUsuario().equals("marina") || usuarioSessao.getNomeUsuario().equals("cirlene")
				|| usuarioSessao.getNomeUsuario().equals("Janderson")
				|| usuarioSessao.getNomeUsuario().equals("monique")
				|| usuarioSessao.getNomeUsuario().equals("adriane.goncalves")) {
			lancamentos.remove(index);
			lancamento = service.getLancamentoAvulsoById(lancamentoAux.getId());
			service.removerLancamento(lancamento);
			lancamento = new LancamentoAvulso();
		} else {
			showMessageLiberarExclusao();
			lancamento = new LancamentoAvulso();
		}

		lancamentoAux = new Lancamento();
		closeDialogLoading();
	}

	public List<Acao> completeAcoes(String query) {
		allAcoes = new ArrayList<Acao>();
		allAcoes = service.acoesAutoComplete(query);
		return allAcoes;
	}

	private void closeDialogLoading() {
		PrimeFaces.current().executeScript("PF('statusDialog').hide();");
	}

	public void showMessageLiberarExclusao() {

		StringBuilder msg = new StringBuilder("Olá " + usuarioSessao.getNomeUsuario()
				+ " você não tem permissão para excluir ou editar um lançamento após entrada no sistema, por favor entre em contato com o setor administrativo para tal ação.\n");

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso!", msg.toString());

		FacesContext.getCurrentInstance().addMessage(null, message);

	}

	public void salvarFornecedor() {

		if (!conta.getTipoJuridico().equals("int")) {
			ContaBancaria contaAuxiliar = service.getContaBancaria(conta);

			if (contaAuxiliar != null) {
				addMessage("", "CNPJ/CPF já cadastrado. Fornecedor: " + contaAuxiliar.getRazaoSocial(),
						FacesMessage.SEVERITY_ERROR);
				return;
			}
		}

		conta.setTipo("CF");
		service.salvarContaFornecedor(conta);
		conta = new ContaBancaria();
	}

	public void salvarConta() {
		service.salvarConta(conta);
		conta = new ContaBancaria();
	}

	public void editarFornecedor() {
		conta = service.getContaBancariaById(selectedConta.getId());
	}

	public List<FontePagadora> completeFonte(String query) {
		allFontes = new ArrayList<FontePagadora>();
		allFontes = service.fontesAutoComplete(query);
		return allFontes;
	}

	public void preparaDialogAdiantamento() {
		filtro = new Filtro();
	}

	public void gerarRelatorioAdiantamento() {

		BigDecimal totalEntrada = BigDecimal.ZERO;
		BigDecimal totalSaida = BigDecimal.ZERO;
		String conta = "";

		List<LancamentoAvulso> mListAux = service.getListaLancamentosAvulsoParaRelatorio(filtro);

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
			l.setAcao(lancamentoAvulso.getAcao());

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

	public TipoParcelamento[] tiposParcelas() {
		return TipoParcelamento.values();
	}

	public CategoriadeDespesa[] categoriasDespesas() {
		return CategoriadeDespesa.values();
	}

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public void prepararEdicao() {

		if (usuarioSessao.getNomeUsuario().equals("admin") || usuarioSessao.getNomeUsuario().equals("b.silva")
				|| usuarioSessao.getNomeUsuario().equals("marina") || usuarioSessao.getNomeUsuario().equals("cirlene")
				|| usuarioSessao.getNomeUsuario().equals("Janderson")
				|| usuarioSessao.getNomeUsuario().equals("monique")
				|| usuarioSessao.getNomeUsuario().equals("adriane.goncalves")) {
			lancamento = service.getLancamentoAvulsoById(lancamentoAux.getId());
			lancamento.setLancamentosAcoes(service.getLancamentosAcao(lancamento));
			lancamento.setArquivos(new ArrayList<>());
		} else {
			showMessageLiberarExclusao();
			lancamento = new LancamentoAvulso();

		}

		lancamentoAux = new Lancamento();
		closeDialogLoading();
	}

	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuFinanceiro();
	}

	public void limparConta() {
		conta = new ContaBancaria();
	}

	public void salvarLancamento() {

		if (lancamento.getLancamentosAcoes().size() < 1) {
			addMessage("",
					"Você deve adicionar ao menos 1 classe/ação ao lançamento para que o armazenamento seja concluído.",
					FacesMessage.SEVERITY_WARN);
			closeDialogLoading();
			return;
		}

		if (lancamento.getTipoLancamento() == null) {
			lancamento.setTipoLancamento("");
		}

		if (!lancamento.getTipoLancamento().equals("reenb")) {
			if (lancamento.getNotaFiscal() != null && !lancamento.getNotaFiscal().equals("")) {
				Long id = new Long(0);

				if (lancamento.getId() != null)
					id = lancamento.getId();

				LancamentoAvulso l = service.getLancamentoByNF(lancamento.getNotaFiscal(), id,lancamento.getContaRecebedor().getId());
				if (l != null) {
					addMessage("", "Nota fiscal já foi paga no lançamento de número: " + l.getId(),
							FacesMessage.SEVERITY_WARN);
					l = null;
					closeDialogLoading();
					return;
				}
			}

		}

		lancamento.setDataPagamento(ajusteDataDePagamento(lancamento.getDataPagamento()));
		lancamento.setDataEmissao(ajusteDataDePagamento(lancamento.getDataEmissao()));
		lancamento.setStatusCompra(StatusCompra.CONCLUIDO);
		service.salvarLancamentoAvulso(lancamento, usuarioSessao.getUsuario());
		carregarLancamentos();
		lancamento = new LancamentoAvulso();

	}

	public void salvarLancamentoSemRefatorar() {

		if (lancamento.getLancamentosAcoes().size() < 1) {
			addMessage("",
					"Você deve adicionar ao menos 1 classe/ação ao lançamento para que o armazenamento seja concluído.",
					FacesMessage.SEVERITY_WARN);
			closeDialogLoading();
			return;
		}

		if (lancamento.getTipoLancamento() == null) {
			lancamento.setTipoLancamento("");
		}

		if (!lancamento.getTipoLancamento().equals("reenb")) {
			if (lancamento.getNotaFiscal() != null && !lancamento.getNotaFiscal().equals("")) {
				Long id = new Long(0);

				if (lancamento.getId() != null)
					id = lancamento.getId();

				LancamentoAvulso l = service.getLancamentoByNF(lancamento.getNotaFiscal(), id,
						lancamento.getContaRecebedor().getId());
				if (l != null) {
					addMessage("", "Nota fiscal já foi paga no lançamento de número: " + l.getId(),
							FacesMessage.SEVERITY_WARN);
					l = null;
					closeDialogLoading();
					return;
				}

			}

		}

		lancamento.setDataPagamento(ajusteDataDePagamento(lancamento.getDataPagamento()));
		lancamento.setDataEmissao(ajusteDataDePagamento(lancamento.getDataEmissao()));
		lancamento.setStatusCompra(StatusCompra.CONCLUIDO);
		service.salvarLancamentoAvulsoSemRefatorar(lancamento, usuarioSessao.getUsuario());
		carregarLancamentos();
		lancamento = new LancamentoAvulso();
		addMessage("", "Lançamento salvo!", FacesMessage.SEVERITY_INFO);

	}

	public Date ajusteDataDePagamento(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.set(Calendar.HOUR_OF_DAY, 3);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();

	}

	public void addLancamentoAcao() {
		lancamentoAcao.setLancamento(lancamento);
		BigDecimal saldo = service.getSaldoAcao(lancamentoAcao.getAcao().getId());
		BigDecimal saldoAux = saldo;
		saldo = saldo.subtract(lancamentoAcao.getValor());
		lancamento.getLancamentosAcoes().add(lancamentoAcao);
		lancamento.setValorTotalComDesconto(lancamento.getValorTotalComDesconto().add(lancamentoAcao.getValor()));
		lancamentoAcao = new LancamentoAcao();
	}

	public String getNumeroFormatado(BigDecimal total) {
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(total);
		// return new DecimalFormat("###,###.###").format(total);
	}

	public void removeLancamentoAcao(int index) {
		BigDecimal valor = lancamento.getLancamentosAcoes().get(index).getValor();
		LancamentoAcao la = lancamento.getLancamentosAcoes().get(index);

		if (la.getId() != null) {
			service.removerLancamentoAcao(la);
		}

		lancamento.setValorTotalComDesconto(lancamento.getValorTotalComDesconto().subtract(valor));
		lancamento.getLancamentosAcoes().remove(index);
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public LancamentoAvulso getLancamento() {
		return lancamento;
	}

	public void setLancamento(LancamentoAvulso lancamento) {
		this.lancamento = lancamento;
	}

	public List<LancamentoAvulso> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<LancamentoAvulso> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public List<ContaBancaria> getAllContas() {
		return allContas;
	}

	public void setAllContas(List<ContaBancaria> allContas) {
		this.allContas = allContas;
	}

	public DespesaReceita[] getDespesaReceita() {
		return DespesaReceita.values();
	}

	public LancamentoAcao getLancamentoAcao() {
		return lancamentoAcao;
	}

	public void setLancamentoAcao(LancamentoAcao lancamentoAcao) {
		this.lancamentoAcao = lancamentoAcao;
	}

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}

	public List<LancamentoAvulso> getSelectedLancamentos() {
		return selectedLancamentos;
	}

	public void setSelectedLancamentos(List<LancamentoAvulso> selectedLancamentos) {
		this.selectedLancamentos = selectedLancamentos;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public TabView getTabview() {
		return tabview;
	}

	public void setTabview(TabView tabview) {
		this.tabview = tabview;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public List<ContaBancaria> getAllContaFornecedor() {
		return allContaFornecedor;
	}

	public void setAllContaFornecedor(List<ContaBancaria> allContaFornecedor) {
		this.allContaFornecedor = allContaFornecedor;
	}

	public ContaBancaria getSelectedConta() {
		return selectedConta;
	}

	public void setSelectedConta(ContaBancaria selectedConta) {
		this.selectedConta = selectedConta;
	}

	public ContaBancaria getConta() {
		return conta;
	}

	public void setConta(ContaBancaria conta) {
		this.conta = conta;
	}

	public ArquivoLancamento getArqAuxiliar() {
		return arqAuxiliar;
	}

	public void setArqAuxiliar(ArquivoLancamento arqAuxiliar) {
		this.arqAuxiliar = arqAuxiliar;
	}

	public Lancamento getLancamentoAux() {
		return lancamentoAux;
	}

	public void setLancamentoAux(Lancamento lancamentoAux) {
		this.lancamentoAux = lancamentoAux;
	}

	public List<CategoriaDespesaClass> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<CategoriaDespesaClass> categorias) {
		this.categorias = categorias;
	}

	public List<PagamentoLancamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<PagamentoLancamento> pagamentos) {
		this.pagamentos = pagamentos;
	}

	public Aprouve getAprouve() {
		return aprouve;
	}

	public void setAprouve(Aprouve aprouve) {
		this.aprouve = aprouve;
	}

}
