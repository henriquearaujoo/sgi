package managedbean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.weld.context.RequestContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;

import model.Acao;
import model.ContaBancaria;
import model.FontePagadora;
import model.Fornecedor;
import model.Imposto;
import model.Lancamento;
import model.LancamentoAuxiliar;
import model.LancamentoAvulso;
import model.MenuLateral;
import model.Orcamento;
import model.OrcamentoProjeto;
import model.PagamentoLancamento;
import model.PagamentoPE;
import model.Pedido;
import model.Projeto;
import model.ProjetoRubrica;
import model.StatusPagamentoLancamento;
import model.TipoImposto;
import model.TipoParcelamento;
import repositorio.CalculatorRubricaRepositorio;
import service.AutorizacaoService;
import service.HomeService;
import service.PagamentoService;
import service.ProjetoService;
import util.ArquivoLancamento;
import util.DataUtil;
import util.DiretorioUtil;
import util.DownloadUtil;
import util.Filtro;
import util.MakeMenu;
import util.UsuarioSessao;

@Named(value = "liberacao_lancamento_controller")
@ViewScoped
public class LiberacaoLancamentoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/***
	 * Classe controladora dos pagamentos
	 */

	// Variáveis de classe
	private @Inject PagamentoLancamento pagamento;
	private PagamentoLancamento pagamentoLancAuxiliar = new PagamentoLancamento();
	private @Inject ContaBancaria conta;
	private @Inject PagamentoService service;
	private MenuLateral menu = new MenuLateral();
	private LancamentoAuxiliar lancAuxiliar;
	private Filtro filtro = new Filtro();

	// Variáveis primitivas
	private BigDecimal totalApagar = BigDecimal.ZERO;
	private BigDecimal totalFaltaPagar = BigDecimal.ZERO;
	private BigDecimal totalPago = BigDecimal.ZERO;
	// Listas

	private List<PagamentoLancamento> pagamentos;
	private List<ContaBancaria> contas;
	private List<LancamentoAuxiliar> lancamentos;
	private List<LancamentoAuxiliar> lancamentosSelected;
	private List<FontePagadora> fontes;
	private List<Projeto> projetos;
	private List<Acao> acoes;
	private List<Fornecedor> fornecedores;
	private List<PagamentoPE> pagamentosPE;
	private List<Acao> allAcoes = new ArrayList<Acao>();
	private List<PagamentoPE> selectedpagamentosPE;

	private Boolean editarLinha = false;

	private LancamentoAvulso lancamentoAvulso;

	private List<ContaBancaria> allContas = new ArrayList<>();

	private List<LancamentoAvulso> avulsos = new ArrayList<>();

	private Boolean prepararDialog = false;

	private List<LancamentoAvulso> lancamentosAvulsos = new ArrayList<>();

	private @Inject ArquivoLancamento arqAuxiliar;

	public LiberacaoLancamentoController() {

	}

	public void changeMode() {
		editarLinha = !editarLinha;

		if (editarLinha) {
			getProjetoByUsuario();
			listaRubricas();
		}
	}

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public List<ContaBancaria> completeConta(String query) {
		allContas = new ArrayList<>();
		;
		allContas = service.getAllConta(query);
		return allContas;
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
//		PrimeFaces.current().dialog().openDynamic("dialog/help/help_liberacao_lancamento", options, null);
//	}

	public List<Acao> completeAcoes(String query) {
		allAcoes = new ArrayList<Acao>();
		allAcoes = service.acoesAutoComplete(query);
		return allAcoes;
	}

	private List<Projeto> listProject = new ArrayList<>();

	public List<Projeto> completeProjetos(String query) {
		listProject = new ArrayList<Projeto>();
		listProject = service.getProjetoAutoCompleteMODE01(query);
		return listProject;
	}

	private List<Acao> allAcoesInPagto = new ArrayList<>();

	public List<Acao> completeAcoesInPagto(String query) {
		allAcoesInPagto = new ArrayList<Acao>();
		allAcoesInPagto = service.acoesAutoComplete(query);
		return allAcoesInPagto;
	}

	public void visualizarArquivo(String id) throws IOException {

		ArquivoLancamento arqAuxiliar = new ArquivoLancamento();

		DownloadUtil.downloadFile(arqAuxiliar.getNome(), arqAuxiliar.getPath(), "application/pdf",
				FacesContext.getCurrentInstance());
		arqAuxiliar = new ArquivoLancamento();
	}

	public void visualizarArquivo() throws IOException {
		DownloadUtil.downloadFile(arqAuxiliar.getNome(), arqAuxiliar.getPath(), "application/pdf",
				FacesContext.getCurrentInstance());
		arqAuxiliar = new ArquivoLancamento();
	}

	public List<MenuLateral> getMenus() {

		List<MenuLateral> lista = new ArrayList<>();
		MenuLateral menu = new MenuLateral("Solicitaçao de Pagamento", "pagamento", "fa fa-shopping-cart");
		lista.add(menu);
		menu = new MenuLateral("Solicitacao de Viagem", "SolicitacaoViagem", "fa fa-fighter-jet");
		lista.add(menu);
		menu = new MenuLateral("Solicitação de Diaria", "SolicitacaoDiaria", "fa fa-user");
		lista.add(menu);
		menu = new MenuLateral("Lançamentos", "lancamentos", "fa fa-money");
		lista.add(menu);
		menu = new MenuLateral("Lançamentos avulsos", "lancamento_avulso", "fa fa-money");
		lista.add(menu);

		return MakeMenu.getMenuFinanceiro();
	}

	@Inject
	private UsuarioSessao usuarioSessao;

	@Inject
	private ProjetoService projetoService;

	@Inject
	private CalculatorRubricaRepositorio calculatorRubricaRepositorio;

	private Projeto projetoAux;

	public Projeto getProjetoAux() {
		return projetoAux;
	}

	public void setProjetoAux(Projeto projetoAux) {
		this.projetoAux = projetoAux;
	}

	private List<Projeto> listaProjeto;

	public List<Projeto> getListaProjeto() {
		return listaProjeto;
	}

	public void setListaProjeto(List<Projeto> listaProjeto) {
		this.listaProjeto = listaProjeto;
	}

	public void getProjetoByUsuario() {
		// listaProjeto =
		// projetoService.getProjetosByUsuario(usuarioSessao.getUsuario());

		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro")) {
			listaProjeto = projetoService.getProjetosbyUsuarioProjeto(null, filtro);
			return;
		}

		listaProjeto = projetoService.getProjetosbyUsuarioProjeto(usuarioSessao.getUsuario(), filtro);
	}

	private List<ProjetoRubrica> listaDeRubricasProjeto = new ArrayList<ProjetoRubrica>();

	public List<ProjetoRubrica> getListaDeRubricasProjeto() {
		return listaDeRubricasProjeto;
	}

	public void setListaDeRubricasProjeto(List<ProjetoRubrica> listaDeRubricasProjeto) {
		this.listaDeRubricasProjeto = listaDeRubricasProjeto;
	}

	// public void listaRubricas() {
	//
	// if(projetoAux == null) {
	// FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
	// "Atenção",
	// "Você deve selecionar um projeto antes de buscar rubrica! ");
	// RequestContext.getCurrentInstance().showMessageInDialog(message);
	// listaDeRubricasProjeto = new ArrayList<ProjetoRubrica>() ;
	// }else {
	// listaDeRubricasProjeto =
	// calculatorRubricaRepositorio.getProjetoRubricaByProjeto(projetoAux.getId());
	// }
	// }
	//
	public void listaRubricas() {

		if (projetoAux == null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você deve selecionar um projeto antes de buscar rubrica! ");
			FacesContext.getCurrentInstance().addMessage(null, message);
			listaDeRubricasProjeto = new ArrayList<ProjetoRubrica>();
		} else {
			listaDeRubricasProjeto = calculatorRubricaRepositorio.getProjetoRubricaByProjeto(projetoAux.getId());
		}
	}

	@Inject
	private HomeService homeService;

	public void init() throws IOException {

		if (homeService.verificarSolicitacoes(usuarioSessao.getIdColadorador())) {
			HttpServletResponse resp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			resp.sendRedirect(req.getContextPath() + "/main/autorizacoes.xhtml");
		}

		limpar();
		carregarLancamentos();
		getProjetoByUsuario();
		// carregarPagamentosPE();
		// carregarProjetos();
		// carregarAcoes();
		// carregarContasFiltro();
		// carregarFornecedores();
	}

	public void prepararLancamentoAvulso() {
		lancamentoAvulso = new LancamentoAvulso();
	}

	public void salvarPagamento() {
		pagamento.setStt(StatusPagamentoLancamento.PROVISIONADO);
		pagamento.setDataEmissao(new Date());
		service.salvar(pagamento);
		pagamento = new PagamentoLancamento();
		prepararDialogPagamento();
		carregarPagamentos();
		carregarPagamentosPE();
	}

	public void prepararLancamentoAvulsso() {
	}

	public void editarPagamento() {
		service.editarPagamento(pagamentoLancAuxiliar);
		pagamentoLancAuxiliar = new PagamentoLancamento();
		prepararDialogPagamento();
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void editarPagamentoTab2() {
		Boolean LinhaOrcamentariaDiferente = false;
		Boolean RubricaDiferenteReembolsavel = false;
		PagamentoLancamento old = service.findPagamentoById(pagamentoLancAuxiliar.getId());
		Boolean efetivado = pagamentoLancAuxiliar.getStt().equals(StatusPagamentoLancamento.EFETIVADO);
		Boolean valorDiferente = (!old.getValor().equals(pagamentoLancAuxiliar.getValor()));
		Boolean dataDiferente = (!old.getDataPagamento().equals(pagamentoLancAuxiliar.getDataPagamento()));
		Boolean valorMaior = ((pagamentoLancAuxiliar.getValor().compareTo(old.getValor()) > 0));
		Boolean projetoRubricaNull = pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica() != null;
		if (projetoRubricaNull)
			LinhaOrcamentariaDiferente = (!pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica()
					.equals(old.getLancamentoAcao().getProjetoRubrica()));

		Boolean diferenteDeEntrada = (!verificaConta(pagamentoLancAuxiliar));
		if (!projetoRubricaNull)
			RubricaDiferenteReembolsavel = (!pagamentoLancAuxiliar.getLancamentoAcao().getRubricaOrcamento()
					.getRubrica().getNome().contains("Reembolsável"));
		else
			RubricaDiferenteReembolsavel = (!pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica()
					.getRubricaOrcamento().getRubrica().getNome().contains("Reembolsável"));

		if (efetivado) {

			if (valorDiferente || dataDiferente) {
				addMessage("", "Este Lancamento se encontra 'EFETIVADO' não é possivel alterar (data) ou (valor)!",
						FacesMessage.SEVERITY_WARN);
				return;
			}
		}

		if (projetoRubricaNull) {
			if (valorMaior || LinhaOrcamentariaDiferente) {
				if (RubricaDiferenteReembolsavel && diferenteDeEntrada) {
					if (LinhaOrcamentariaDiferente) {
						// Quando se troca a linha orcamentaria verifica-se o valor esta disponivel
						if (!calculatorRubricaRepositorio.verificarSaldoRubrica(
								pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica().getId(),
								(pagamentoLancAuxiliar.getValor()))) {
							addMessage("", "Saldo de rubrica insuficiente", FacesMessage.SEVERITY_WARN);
							return;
						}
					} else {
						// Quando não se troca a linha orcametaria verifica se tem disponilvel apenas a
						// diferença
						if (!calculatorRubricaRepositorio.verificarSaldoRubrica(
								pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica().getId(),
								(pagamentoLancAuxiliar.getValor().subtract(old.getValor())))) {
							addMessage("", "Saldo de rubrica insuficiente", FacesMessage.SEVERITY_WARN);
							return;
						}
					}
				}
			}
		}
		service.editarPagamento(pagamentoLancAuxiliar);
		pagamentoLancAuxiliar = new PagamentoLancamento();
		// carregarPagamentosPE();
		carregarLancamentos();
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('dlg_edt_pagamento_tab2').hide();");
		addMessage("", "Salvo com Sucesso!", FacesMessage.SEVERITY_INFO);
	}

	private Lancamento lancamento = new Lancamento();
	private ArquivoLancamento arquivo = new ArquivoLancamento();
	private byte[] conteudo;
	private String path = "";

	public void adicionarArquivo() {

		File file = new File(arquivo.getPath());
		FileOutputStream fot;
		try {
			fot = new FileOutputStream(file);
			fot.write(conteudo);
			fot.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		arquivo.setLancamento(lancamento);
		// lancamentoAux.getArquivos().add(arquivo);
		service.salvarArquivo(arquivo);

		path = "";

		arquivo = new ArquivoLancamento();

		carregarArquivos(lancamento.getId());
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		// String pathFinal = DiretorioUtil.DIRECTORY_UPLOAD_LINUX;
		String pathFinal = DiretorioUtil.DIRECTORY_UPLOAD;
		// arquivo.setConteudo(event.getFile().getContents());

		conteudo = event.getFile().getContents();
		arquivo.setNome(event.getFile().getFileName());
		arquivo.setPath(pathFinal + lancamento.getId() + "." + event.getFile().getFileName());
		arquivo.setData(new Date());
		arquivo.setUsuario(usuarioSessao.getUsuario());
		// adicionarArquivo();
	}

	public void carregarArquivos(Long id) {
		lancamento = service.getLancamentoById(id);
		lancamento.setArquivos(new ArrayList<ArquivoLancamento>());
		
		if (lancamento instanceof Pedido) {
			lancamento.setArquivos(service.getArquivoByLancamento(id, ((Pedido) lancamento).getCompra().getId()));
		}else {
			lancamento.setArquivos(service.getArquivoByLancamento(id));
		}
	
	}

	public void removerArquivo() {

		service.removerArquivo(arqAuxiliar);
		arqAuxiliar = new ArquivoLancamento();
		carregarArquivos(lancamento.getId());

	}

	public StatusPagamentoLancamento[] getStatusPagamento() {
		return StatusPagamentoLancamento.values();
	}

	public Boolean verificaConta(PagamentoLancamento pagamentoLancamento) {
		return (pagamentoLancamento.getContaRecebedor().getTipo().equals("CB"));
	}

	private List<OrcamentoProjeto> orcamentosProjetos = new ArrayList<>();
	private List<Orcamento> orcamentos = new ArrayList<>();

	public void prepararEdicao(Long id) {

		// getProjetoByUsuario();
		// listaRubricas();

		editarLinha = editarLinha == null ? false : false;
		pagamentoLancAuxiliar = new PagamentoLancamento();
		pagamentoLancAuxiliar = service.findPagamentoById(id);

		projetoAux = null;

		if (pagamentoLancAuxiliar.getLancamentoAcao() != null
				&& pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica() != null) {
			projetoAux = new Projeto();
			projetoAux = pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica().getProjeto();
			listaRubricas();

		}

		// orcamentosProjetos = service.getOrcamentoByProjeto(
		// pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica().getProjeto().getId());

		// for (OrcamentoProjeto orcProjeto : orcamentosProjetos) {
		// orcamentos.add(orcProjeto.getOrcamento());
		// }
	}

	public void removerPagamento() {
		service.removerPagamento(pagamento);
		pagamento = new PagamentoLancamento();
		prepararDialogPagamento();

	}

	private List<FontePagadora> allFontes = new ArrayList<>();

	public List<FontePagadora> completeFonte(String query) {
		allFontes = new ArrayList<FontePagadora>();
		allFontes = service.fontesAutoComplete(query);
		return allFontes;
	}

	public void efetivar(Long id) {
		service.efetivar(id);
		carregarPagamentosPE();
	}

	public void efetivarMultiplos() {
		service.efetivar(selectedpagamentosPE);
		carregarPagamentosPE();
		selectedpagamentosPE = new ArrayList<>();
	}

	public void prepararDialogPagamento() {
		carregarContas();
		carregarPagamentos();
		pagamento = new PagamentoLancamento();
		pagamento.setLancamentoAcao(service.buscarLancamentoAcao(lancAuxiliar.getIdAcaoLancamentoId()));
		pagamento.setQuantidadeParcela(lancAuxiliar.getQuantidadeParcela());
		pagamento.setTipoParcelamento(lancAuxiliar.getTipoParcelamento());
	}

	public void postProcessXLS(Object document) {
		// HSSFWorkbook wb = (HSSFWorkbook) document;
		// HSSFSheet sheet = wb.getSheetAt(0);
		// HSSFRow header = sheet.getRow(0);
		//
		// HSSFCellStyle cellStyle = wb.createCellStyle();
		// cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		// cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// short d = 1;
		// cellStyle.setBorderBottom(d);
		// cellStyle.setBorderLeft(d);
		// cellStyle.setBorderRight(d);
		// cellStyle.setBorderTop(d);
		//
		// for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
		// HSSFCell cell = header.getCell(i);
		// cell.setCellStyle(cellStyle);
		// }

	}

	public void carregarContas() {
		contas = new ArrayList<>();
		contas = service.getContas();
		totalApagar = lancAuxiliar.getValorPagoAcao();
	}

	public void carregarContasFiltro() {
		contas = new ArrayList<>();
		contas = service.getContasFiltro();
	}

	public void carregarFornecedores() {
		fornecedores = new ArrayList<>();
		fornecedores = service.getFornecedores();
	}

	public void carregarPagamentos() {
		pagamentos = service.getPagamentos(lancAuxiliar.getIdAcaoLancamentoId());
		totalFaltaPagar = totalApagar;
		totalPago = BigDecimal.ZERO;

		for (PagamentoLancamento pagto : pagamentos) {
			totalPago = totalPago.add(pagto.getValor());
			totalFaltaPagar = totalApagar.subtract(totalPago);
		}
	}
	
	@Inject
	private AutorizacaoService autService;
	
	public void imprimirMapa(String idCompra) {
		autService.imprimirMapaComparativo( new Long(idCompra));
	}

	public void carregarLancamentos() {
		lancamentos = new ArrayList<>();
		lancamentos = service.getLancamentosParaValidacao(filtro);
		// service.getPagamentosPE(filtro);
	}
	
	
	public void imprimir(Lancamento lancamento) {
		service.imprimir(lancamento);
	}

	public void validarLancamentos() {
		service.validarLancamentos(lancamentosSelected);
		carregarLancamentos();
	}

	public void invalidarLancamentos() {
		for (LancamentoAuxiliar lancamento : lancamentosSelected) {
			if (lancamento.getStatus().equals("EFETIVADO")) {
				FacesContext.getCurrentInstance().addMessage("msg_lancamento",
						new FacesMessage(FacesMessage.SEVERITY_WARN, "",
								"Existem um ou mais Lançamentos Efetivados desmarque-os e tente novamente!"));
				return;
			}
		}
		service.invalidarLancamentos(lancamentosSelected);
		carregarLancamentos();
	}

	@Inject
	private Imposto imposto;
	private List<Imposto> impostos = new ArrayList<>();

	public void salvarImposto() {
		imposto.setPagamentoLancamento(pagamentoLancAuxiliar);
		imposto = service.salvarImposto(imposto);
		if (imposto.getId() != null)
			imposto = new Imposto();

		listarImpostos();
	}

	public void listarImpostos() {
		Filtro f = new Filtro();
		f.setLancamentoID(pagamentoLancAuxiliar.getId());
		impostos = service.buscarImpostos(f);
		f = null;
	}

	public void removerImposto(Imposto imp) {
		service.removerImposto(imp);
		imposto = new Imposto();
		listarImpostos();
	}

	public void listarImpostos(Long idLancamento, Long idPagamento) {

		Filtro f = new Filtro();
		f.setLancamentoID(idPagamento);
		impostos = service.buscarImpostos(f);
		f = null;

		pagamentoLancAuxiliar = new PagamentoLancamento();
		pagamentoLancAuxiliar = service.findPagamentoById(idPagamento);

	}

	public TipoImposto[] getTipoImposto() {
		return TipoImposto.values();
	}

	public void carregarPagamentosPE() {
		pagamentosPE = new ArrayList<>();
		pagamentosPE = service.getPagamentosPEMODE01(filtro);
	}

	public void limpar() {
		filtro = new Filtro();
		filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		filtro.setDataFinal(DataUtil.getDataFinal(new Date()));
	}

	public void carregarProjetos() {
		projetos = new ArrayList<>();
		projetos = service.getProjetos();
	}

	public void carregarAcoes() {
		acoes = new ArrayList<>();
		acoes = service.getAcoes();
	}

	public void carregarFontes() {
		fontes = new ArrayList<>();
		fontes = service.getFontePagadora();
	}

	public TipoParcelamento[] getParcelamentos() {
		return TipoParcelamento.values();
	}

	public PagamentoLancamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(PagamentoLancamento pagamento) {
		this.pagamento = pagamento;
	}

	public List<PagamentoLancamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<PagamentoLancamento> pagamentos) {
		this.pagamentos = pagamentos;
	}

	public ContaBancaria getConta() {
		return conta;
	}

	public void setConta(ContaBancaria conta) {
		this.conta = conta;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public List<ContaBancaria> getContas() {
		return contas;
	}

	public void setContas(List<ContaBancaria> contas) {
		this.contas = contas;
	}

	public List<LancamentoAuxiliar> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<LancamentoAuxiliar> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}

	public List<FontePagadora> getFontes() {
		return fontes;
	}

	public void setFontes(List<FontePagadora> fontes) {
		this.fontes = fontes;
	}

	public List<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}

	public List<Acao> getAcoes() {
		return acoes;
	}

	public void setAcoes(List<Acao> acoes) {
		this.acoes = acoes;
	}

	public BigDecimal getTotalApagar() {
		return totalApagar;
	}

	public void setTotalApagar(BigDecimal totalApagar) {
		this.totalApagar = totalApagar;
	}

	public BigDecimal getTotalFaltaPagar() {
		return totalFaltaPagar;
	}

	public void setTotalFaltaPagar(BigDecimal totalFaltaPagar) {
		this.totalFaltaPagar = totalFaltaPagar;
	}

	public BigDecimal getTotalPago() {
		return totalPago;
	}

	public void setTotalPago(BigDecimal totalPago) {
		this.totalPago = totalPago;
	}

	public LancamentoAuxiliar getLancAuxiliar() {
		return lancAuxiliar;
	}

	public void setLancAuxiliar(LancamentoAuxiliar lancAuxiliar) {
		this.lancAuxiliar = lancAuxiliar;
	}

	public List<Fornecedor> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public List<PagamentoPE> getPagamentosPE() {
		return pagamentosPE;
	}

	public void setPagamentosPE(List<PagamentoPE> pagamentosPE) {
		this.pagamentosPE = pagamentosPE;
	}

	public PagamentoLancamento getPagamentoLancAuxiliar() {
		return pagamentoLancAuxiliar;
	}

	public void setPagamentoLancAuxiliar(PagamentoLancamento pagamentoLancAuxiliar) {
		this.pagamentoLancAuxiliar = pagamentoLancAuxiliar;
	}

	public List<LancamentoAvulso> getAvulsos() {
		avulsos = new ArrayList<>();
		avulsos.add(new LancamentoAvulso());
		avulsos.add(new LancamentoAvulso());
		return avulsos;
	}

	public void setAvulsos(List<LancamentoAvulso> avulsos) {
		this.avulsos = avulsos;
	}

	public LancamentoAvulso getLancamentoAvulso() {
		return lancamentoAvulso;
	}

	public void setLancamentoAvulso(LancamentoAvulso lancamentoAvulso) {
		this.lancamentoAvulso = lancamentoAvulso;
	}

	public List<ContaBancaria> getAllContas() {
		return allContas;
	}

	public void setAllContas(List<ContaBancaria> allContas) {
		this.allContas = allContas;
	}

	public List<PagamentoPE> getSelectedpagamentosPE() {
		return selectedpagamentosPE;
	}

	public void setSelectedpagamentosPE(List<PagamentoPE> selectedpagamentosPE) {
		this.selectedpagamentosPE = selectedpagamentosPE;
	}

	public List<Acao> getAllAcoesInPagto() {
		return allAcoesInPagto;
	}

	public void setAllAcoesInPagto(List<Acao> allAcoesInPagto) {
		this.allAcoesInPagto = allAcoesInPagto;
	}

	public List<OrcamentoProjeto> getOrcamentosProjetos() {
		return orcamentosProjetos;
	}

	public void setOrcamentosProjetos(List<OrcamentoProjeto> orcamentosProjetos) {
		this.orcamentosProjetos = orcamentosProjetos;
	}

	public List<LancamentoAuxiliar> getLancamentosSelected() {
		return lancamentosSelected;
	}

	public void setLancamentosSelected(List<LancamentoAuxiliar> lancamentosSelected) {
		this.lancamentosSelected = lancamentosSelected;
	}

	public Boolean getEditarLinha() {
		return editarLinha;
	}

	public void setEditarLinha(Boolean editarLinha) {
		this.editarLinha = editarLinha;
	}

	public Imposto getImposto() {
		return imposto;
	}

	public void setImposto(Imposto imposto) {
		this.imposto = imposto;
	}

	public List<Imposto> getImpostos() {
		return impostos;
	}

	public void setImpostos(List<Imposto> impostos) {
		this.impostos = impostos;
	}

	public ArquivoLancamento getArqAuxiliar() {
		return arqAuxiliar;
	}

	public void setArqAuxiliar(ArquivoLancamento arqAuxiliar) {
		this.arqAuxiliar = arqAuxiliar;
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

}
