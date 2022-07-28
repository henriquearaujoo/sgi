package managedbean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.weld.context.RequestContext;
import org.primefaces.PrimeFaces;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import anotacoes.Transactional;
import model.Acao;
import model.Aprouve;
import model.AprouveSigla;
import model.CategoriaFinanceira;
import model.ContaBancaria;
import model.DespesaReceita;
import model.Devolucao;
import model.FontePagadora;
import model.Fornecedor;
import model.Imposto;
import model.ItemPedido;
import model.Lancamento;
import model.LancamentoAcao;
import model.LancamentoAuxiliar;
import model.LancamentoAvulso;
import model.LancamentoDevDocTed;
import model.LancamentoReembolso;
import model.LogStatus;
import model.MenuLateral;
import model.Orcamento;
import model.OrcamentoProjeto;
import model.PagamentoLancamento;
import model.PagamentoPE;
import model.Pedido;
import model.Projeto;
import model.ProjetoRubrica;
import model.StatusCompra;
import model.StatusPagamentoLancamento;
import model.TarifaBancaria;
import model.TermoExpedicao;
import model.TipoArquivo;
import model.TipoParcelamento;
import model.UnidadeConservacao;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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
import util.ValidarAcesso;

@Named(value = "lancamentos_v2")
@ViewScoped
public class LancamentoV2Controller implements Serializable {

	public Aprouve getAprouve() {
		return aprouve;
	}

	public void setAprouve(Aprouve aprouve) {
		this.aprouve = aprouve;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/***
	 * Classe controladora dos pagamentos
	 * 
	 */

	// Variáveis de classe
	private @Inject PagamentoLancamento pagamento;
	private @Inject TarifaBancaria tarifa;
	private PagamentoLancamento pagamentoLancAuxiliar = new PagamentoLancamento();
	private @Inject ContaBancaria conta;
	private @Inject PagamentoService service;
	private MenuLateral menu = new MenuLateral();
	private LancamentoAuxiliar lancAuxiliar;
	private Lancamento lancamentoMudarStatus = new Lancamento();

	private Boolean editarLinha = false;

	public UsuarioSessao getUsuarioSessao() {
		return usuarioSessao;
	}

	public void setUsuarioSessao(UsuarioSessao usuarioSessao) {
		this.usuarioSessao = usuarioSessao;
	}

	private Filtro filtro = new Filtro();

	private @Inject UsuarioSessao usuarioSessao;

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
	private List<CategoriaFinanceira> listCategoriaFinanceira = new ArrayList<>();

	private List<PagamentoPE> selectedpagamentosPE;

	private InputText inputNomeArquivo;

	private LancamentoAvulso lancamentoAvulso;

	private List<ContaBancaria> allContas = new ArrayList<>();

	private List<LancamentoAvulso> avulsos = new ArrayList<>();

	private Boolean prepararDialog = false;

	private Date dataAuxiliarAlteracaoEmMassa = new Date();

	private List<ProjetoRubrica> listaDeRubricasProjeto = new ArrayList<ProjetoRubrica>();

	@Inject
	private ProjetoService projetoService;

	// Variaveis reembolso(Devolucao DOC/TED)
	private Lancamento lancamentoReembolsoDocTed = new Lancamento();
	private PagamentoLancamento pagamentoLancamentoReembolsoDocTed = new PagamentoLancamento();
	private Date dataDevolucao;

	// Variaveis reembolso
	private Lancamento lancamentoReembolso = new Lancamento();
	private Date dataReembolso;
	private Boolean gerarReembolso = false;

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

	
//	public void dialogHelp() {
//		HashMap<String, Object> options = new HashMap<>();
//		options.put("width", "100%");
//		options.put("height", "100%");
//		options.put("contentWidth", "100%");
//		options.put("contentHeight", "100%");
//		options.put("resizable", false);
//		options.put("minimizable",true);
//		options.put("maximizable",true);
//		PrimeFaces.current().dialog().openDynamic("dialog/help/help_lancamentoV2", options, null);
//	}


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

	public String direcionarTelaImpostos() {
		return "impostos_geracao?faces-redirect=true";
	}

	public List<ProjetoRubrica> getListaDeRubricasProjeto() {
		return listaDeRubricasProjeto;
	}

	public void setListaDeRubricasProjeto(List<ProjetoRubrica> listaDeRubricasProjeto) {
		this.listaDeRubricasProjeto = listaDeRubricasProjeto;
	}

	@Inject
	private CalculatorRubricaRepositorio calculatorRubricaRepositorio;

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

	public void changeMode() {
		editarLinha = !editarLinha;

		if (editarLinha) {
			getProjetoByUsuario();
			listaRubricas();
		}

	}

	public Date getDataAuxiliarAlteracaoEmMassa() {
		return dataAuxiliarAlteracaoEmMassa;
	}

	public void setDataAuxiliarAlteracaoEmMassa(Date dataAuxiliarAlteracaoEmMassa) {
		this.dataAuxiliarAlteracaoEmMassa = dataAuxiliarAlteracaoEmMassa;
	}

	private List<LancamentoAvulso> lancamentosAvulsos = new ArrayList<>();

	public LancamentoV2Controller() {

	}

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public void salvarTarifa() {
		service.salvarTarifa(tarifa, usuarioSessao.getUsuario());
		carregarLancamentos();
	}

	public List<ContaBancaria> completeConta(String query) {
		allContas = new ArrayList<>();
		allContas = service.getAllConta(query);
		return allContas;
	}

	public List<Acao> completeAcoes(String query) {
		allAcoes = new ArrayList<Acao>();
		allAcoes = service.acoesAutoComplete(query);
		return allAcoes;
	}

	private List<Projeto> listProject = new ArrayList<>();

	public List<Projeto> completeProjetos(String query) {
		listProject = new ArrayList<Projeto>();
		if (query != null && !(query.isEmpty())) {
			listProject = service.getProjetoAutoCompleteMODE01(query);
		}
		return listProject;
	}

	private Lancamento lancamento = new Lancamento();
	private UploadedFile file;
	private ArquivoLancamento arquivo = new ArquivoLancamento();
	private String path = "";
	private @Inject ArquivoLancamento arqAuxiliar;
	private @Inject Aprouve aprouve;

	public TipoArquivo[] getTipos() {
		return TipoArquivo.values();
	}

	public void visualizarArquivo() throws IOException {
		DownloadUtil.downloadFile(arqAuxiliar.getNome(), arqAuxiliar.getPath(), "application/pdf",
				FacesContext.getCurrentInstance());
		arqAuxiliar = new ArquivoLancamento();
	}

	public void removerArquivo() {
		if (verificarPrivilegioENF()) {
			service.removerArquivo(arqAuxiliar);
			arqAuxiliar = new ArquivoLancamento();
			carregarArquivos(lancamento.getId());
		} else {
			addMessage("",
					"Você não tem permissão para esse tipo de ação, por favor entre em contato com administrador do sistema.",
					FacesMessage.SEVERITY_WARN);
		}
	}

	public Boolean verificarPrivilegioENF() {
		aprouve.setSigla("ENF");
		aprouve.setUsuario(usuarioSessao.getUsuario());
		// aprouve.setSigla(compra.getGestao().getSigla());
		return service.verificarPrivilegioENF(aprouve);
	}

	public void preparaArquivo() {
		String pathFinal = DiretorioUtil.DIRECTORY_UPLOAD;
		arquivo.setConteudo(file.getContents());
		arquivo.setPath(pathFinal + lancamento.getId() + "." + file.getFileName());
		path = file.getFileName();
		arquivo.setData(new Date());
		arquivo.setUsuario(usuarioSessao.getUsuario());
	}

	private byte[] conteudo;

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

	public boolean verificarArquivo() {
		boolean retorno = false;

		if (arquivo.getNome() == null || arquivo.getNome().equals("")) {
			retorno = true;
			addMessage("", "Preencha o campo 'Descrição'", FacesMessage.SEVERITY_ERROR);
		}

		if (arquivo.getTipo() == null || arquivo.getTipo().getNome().equals("")) {
			retorno = true;
			addMessage("", "Selecione um tipo ('Nota fiscal','Processo','Comprovante')", FacesMessage.SEVERITY_ERROR);
		}

		return retorno;
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void carregarArquivos(Long id) {
		lancamento = service.getLancamentoById(id);
		lancamento.setArquivos(new ArrayList<ArquivoLancamento>());

		if (lancamento instanceof Pedido) {
			lancamento.setArquivos(service.getArquivoByLancamento(id, ((Pedido) lancamento).getCompra().getId()));
		} else {
			lancamento.setArquivos(service.getArquivoByLancamento(id));
		}

	}

	public void imprimirTermo(Long id) {

		if (id.longValue() == new Long(0).longValue()) {
			return;
		}

		TermoExpedicao termo = service.getTermoById(id);

		HashMap<String, Object> params = new HashMap<String, Object>();

		UnidadeConservacao uc = (UnidadeConservacao) termo.getLocalidade();

		String aprovador = "";
		String aprovadorAss = "";
		String sup = "";
		String associacao = uc.getNomeAssociacao();
		String cnpjAssociacao = uc.getCnpjassociacao();
		String presidente = uc.getPresidenteAssociacao();
		String rgPresidente = uc.getRgPresidente();
		String cpfPresidente = uc.getCpfPresidente();

		ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

		String caminho = context.getRealPath("resources");

		params.put("ano", termo.getAnoReferencia());
		params.put("associacao", associacao);
		params.put("cnpj_associacao", cnpjAssociacao);
		params.put("presidente", presidente);
		params.put("cpf_presidente", cpfPresidente);
		params.put("rg_presidente", rgPresidente);

		if (termo.getAprovador().getNome().equals("Luiz Villarez")) {
			aprovador = "Superintendente Administrativo-Financeiro, Sr. LUIZ CRUZ VILLARES, brasileiro, casado, administrador de empresas, portador da cédula de identidade nº 8.882.839-SSP/SP, inscrito no CPF/MF sob nº 066.535.688-90, residente e domiciliado em São Paulo-SP, na rua Almeida Garret";
			aprovadorAss = "LUIZ CRUZ VILLARES";
			sup = "Superintendente Administrativo-Financeiro";
		} else {
			aprovador = "Superintendente Técnico-Científico, EDUARDO COSTA TAVEIRA , brasileiro, casado, mestre em ciências do ambiente, portador da cédula de identidade nº 1299947-4-SSP/RJ CPF 601.314.622-53, residente e domiciliado em Manaus, na Rua 15, QD12, Nº 08, condominio Villa Verde I, Santo Agostinho";
			aprovadorAss = "EDUARDO COSTA TAVEIRA";
			sup = "Superintendente Técnico-Científico";
		}
		params.put("aprovador", aprovador);
		params.put("aprovador_ass", aprovadorAss);
		params.put("sup", sup);
		params.put("logo", caminho + File.separatorChar + "image" + File.separatorChar + "logofas.png");
		String relatorio = "";
		if (termo.getTipo().getNome().equals("Entrega")) {
			relatorio = "relatorio" + File.separatorChar + "TermoEntregaLivre.jrxml";

			try {

				List<ItemPedido> listaExpedicao = service.itensPedido(termo);
				JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaExpedicao);

				JasperReport jasperReport = JasperCompileManager
						.compileReport(caminho + File.separatorChar + relatorio);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds);

				byte[] b = JasperExportManager.exportReportToPdf(jasperPrint);

				FacesContext fc = FacesContext.getCurrentInstance();
				HttpServletResponse res = (HttpServletResponse) fc.getExternalContext().getResponse();
				res.setContentType("application/pdf");
				res.setHeader("Content-disposition", "inline;filename=TermodeEntrega.pdf");
				res.getOutputStream().write(b);
				res.getOutputStream().flush();
				res.getOutputStream().close();
				fc.responseComplete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (termo.getTipo().getNome().equals("Doação")) {
			relatorio = "relatorio" + File.separatorChar + "TermoDoacao.jrxml";

			try {

				List<ItemPedido> listaExpedicao = service.itensPedido(termo);
				JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaExpedicao);

				JasperReport jasperReport = JasperCompileManager
						.compileReport(caminho + File.separatorChar + relatorio);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds);

				byte[] b = JasperExportManager.exportReportToPdf(jasperPrint);

				FacesContext fc = FacesContext.getCurrentInstance();
				HttpServletResponse res = (HttpServletResponse) fc.getExternalContext().getResponse();
				res.setContentType("application/pdf");
				res.setHeader("Content-disposition", "inline;filename=TermodeDoacao.pdf");
				res.getOutputStream().write(b);
				res.getOutputStream().flush();
				res.getOutputStream().close();
				fc.responseComplete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

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
	private HomeService homeService;

	public void init() throws IOException {
		
		ValidarAcesso.checkEnterPage(usuarioSessao.getUsuario());

		if (homeService.verificarSolicitacoes(usuarioSessao.getIdColadorador())) {
			HttpServletResponse resp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			resp.sendRedirect(req.getContextPath() + "/main/autorizacoes.xhtml");
		}

		limpar();
		carregarLancamentos();
		carregarCategoriasFin();
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

	@Inject
	private AutorizacaoService autService;

	public void imprimirMapa(String idCompra) {
		autService.imprimirMapaComparativo(new Long(idCompra));
	}

	public void imprimir(Lancamento lancamento) {
		service.imprimir(lancamento);
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

	public void editarPagamentoTab2() {
		Boolean LinhaOrcamentariaDiferente = false;
		Boolean RubricaDiferenteReembolsavel = false;
		Boolean projetoRubricaNull = false;
		final PagamentoLancamento old = this.service.findPagamentoById(this.pagamentoLancAuxiliar.getId());
		final Boolean efetivado = this.pagamentoLancAuxiliar.getStt().equals((Object)StatusPagamentoLancamento.EFETIVADO);
		final Boolean valorDiferente = !old.getValor().equals(this.pagamentoLancAuxiliar.getValor());
		final Boolean dataDiferente = !old.getDataPagamento().equals(this.pagamentoLancAuxiliar.getDataPagamento());
		final Boolean valorMaior = this.pagamentoLancAuxiliar.getValor().compareTo(old.getValor()) > 0;
		if (this.pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica() != null) {
			LinhaOrcamentariaDiferente = !this.pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica().equals((Object)old.getLancamentoAcao().getProjetoRubrica());
			RubricaDiferenteReembolsavel = !this.pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica().getRubricaOrcamento().getRubrica().getNome().contains("Reembols\u00e1vel");
			projetoRubricaNull = true;
		}
		final Boolean diferenteDeEntrada = !this.verificaConta(this.pagamentoLancAuxiliar);
		if (efetivado && (valorDiferente || dataDiferente)) {
			this.addMessage("", "Este Lancamento se encontra 'EFETIVADO' n\u00e3o \u00e9 possivel alterar (data) ou (valor)!", FacesMessage.SEVERITY_WARN);
			return;
		}
		if (projetoRubricaNull && (valorMaior || LinhaOrcamentariaDiferente) && RubricaDiferenteReembolsavel && diferenteDeEntrada) {
			if (LinhaOrcamentariaDiferente) {
				if (!this.calculatorRubricaRepositorio.verificarSaldoRubrica(this.pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica().getId(), this.pagamentoLancAuxiliar.getValor())) {
					this.addMessage("", "Saldo de rubrica insuficiente", FacesMessage.SEVERITY_WARN);
					return;
				}
			}
			else if (!this.calculatorRubricaRepositorio.verificarSaldoRubrica(this.pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica().getId(), this.pagamentoLancAuxiliar.getValor().subtract(old.getValor()))) {
				this.addMessage("", "Saldo de rubrica insuficiente", FacesMessage.SEVERITY_WARN);
				return;
			}
		}
		this.service.editarPagamento(this.pagamentoLancAuxiliar);
		this.pagamentoLancAuxiliar = new PagamentoLancamento();
		this.carregarLancamentos();
		final PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('dlg_edt_pagamento_tab2').hide();");
		this.addMessage("", "Salvo com Sucesso!", FacesMessage.SEVERITY_INFO);
	}

//	public void editarPagamentoTab2() {
//		// Boolean verificarSaldo = true;
//		// if(pagamentoLancAuxiliar.getContaRecebedor().getTipo().equals("CB")) {
//		// verificarSaldo = false;
//		// }
//
//		// if(verificarSaldo) {
//		// if
//		// (!calculatorRubricaRepositorio.verificarSaldoRubrica(pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica().getId(),
//		// pagamentoLancAuxiliar.getLancamentoAcao().getValor())) {
//		// addMessage("", "Saldo de rubrica insuficiente", FacesMessage.SEVERITY_WARN);
//		// return;
//		// }
//		//
//		// }
//
//		try {
//
//			if (gerarReembolso) {
//
//				if (dataReembolso == null)
//					throw new Exception("A data do reembolso é obrigatória");
//			}
//
//			Boolean LinhaOrcamentariaDiferente = false;
//			Boolean RubricaDiferenteReembolsavel = false;
//			Boolean projetoRubricaNull = false;
//
//			PagamentoLancamento old = service.findPagamentoById(pagamentoLancAuxiliar.getId());
//			Boolean efetivado = pagamentoLancAuxiliar.getStt().equals(StatusPagamentoLancamento.EFETIVADO);
//			Boolean valorDiferente = (!old.getValor().equals(pagamentoLancAuxiliar.getValor()));
//			Boolean dataDiferente = (!old.getDataPagamento().equals(pagamentoLancAuxiliar.getDataPagamento()));
//			Boolean valorMaior = ((pagamentoLancAuxiliar.getValor().compareTo(old.getValor()) > 0));
//
//			if (pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica() != null) {
//				LinhaOrcamentariaDiferente = (!pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica()
//						.equals(old.getLancamentoAcao().getProjetoRubrica()));
//				RubricaDiferenteReembolsavel = (!pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica()
//						.getRubricaOrcamento().getRubrica().getNome().contains("Reembolsável"));
//				projetoRubricaNull = true;
//			}
//
//			Boolean diferenteDeEntrada = (!verificaConta(pagamentoLancAuxiliar));
//
//			if (efetivado) {
//
//				if (valorDiferente || dataDiferente) {
//					addMessage("", "Este Lancamento se encontra 'EFETIVADO' não é possivel alterar (data) ou (valor)!",
//							FacesMessage.SEVERITY_WARN);
//					return;
//				}
//			}
//
//			if (projetoRubricaNull) {
//				if (valorMaior || LinhaOrcamentariaDiferente) {
//					if (RubricaDiferenteReembolsavel && diferenteDeEntrada) {
//						if (LinhaOrcamentariaDiferente) {
//							// Quando se troca a linha orcamentaria verifica-se o valor esta disponivel
//							if (!calculatorRubricaRepositorio.verificarSaldoRubrica(
//									pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica().getId(),
//									(pagamentoLancAuxiliar.getValor()))) {
//								addMessage("", "Saldo de rubrica insuficiente", FacesMessage.SEVERITY_WARN);
//								return;
//							}
//						} else {
//							// Quando não se troca a linha orcametaria verifica se tem disponilvel apenas a
//							// diferença
//							if (!calculatorRubricaRepositorio.verificarSaldoRubrica(
//									pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica().getId(),
//									(pagamentoLancAuxiliar.getValor().subtract(old.getValor())))) {
//								addMessage("", "Saldo de rubrica insuficiente", FacesMessage.SEVERITY_WARN);
//								return;
//							}
//						}
//					}
//				}
//			}
//
//			service.editarPagamento(pagamentoLancAuxiliar);
//
//			if (gerarReembolso) {
//
//				inserirReembolso("reembolso", "REEMBOLSO - " + lancamentoReembolso.getDescricao(),
//						lancamentoReembolso.getCodigo(), lancamentoReembolso.getDepesaReceita(), pagamentoLancAuxiliar,
//						lancamentoReembolso, dataReembolso, conta, pagamentoLancAuxiliar.getConta());
//			}
//
//			pagamentoLancAuxiliar = new PagamentoLancamento();
//			// carregarPagamentosPE();
//			carregarLancamentos();
//
//			PrimeFaces current = PrimeFaces.current();
//			current.executeScript("PF('dlg_edt_pagamento_tab2').hide();");
//
//			addMessage("", "Salvo com Sucesso!", FacesMessage.SEVERITY_INFO);
//		} catch (Exception e) {
//			e.printStackTrace();
//			addMessage("", e.getMessage(), FacesMessage.SEVERITY_ERROR);
//		}
//	}

	public Boolean verificaConta(PagamentoLancamento pagamentoLancamento) {
		return (pagamentoLancamento.getContaRecebedor().getTipo().equals("CB"));
	}

	public void salvaAlteracaoData() {
		if (lancamentosSelected.isEmpty()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Selecione um Lançamento.");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		} else {

			for (LancamentoAuxiliar pg : lancamentosSelected) {
				PagamentoLancamento pagamentoLanc = service.findPagamentoById(pg.getIdPagamento());
				pagamentoLanc.setDataPagamento(dataAuxiliarAlteracaoEmMassa);
				service.editarPagamento(pagamentoLanc);
			}
			carregarLancamentos();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Salvo",
					"Data de Pagamento Alterada com Sucesso!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}


	public StatusPagamentoLancamento[] getStatusPagamento() {
		return StatusPagamentoLancamento.values();
	}

	private List<OrcamentoProjeto> orcamentosProjetos = new ArrayList<>();
	private List<Orcamento> orcamentos = new ArrayList<>();

	@Deprecated
	public List<ProjetoRubrica> completeCategoria(String s) {
		if (s.length() > 2)
			return service.completeRubricasDeProjetoJOIN(s);
		else
			return new ArrayList<>();

		// return pagamentoService.completeRubricasDeProjetoJOIN(s);
	}

	public void prepararEdicao(Long id) {
		try {

			editarLinha = editarLinha == null ? false : false;

			pagamentoLancAuxiliar = new PagamentoLancamento();
			pagamentoLancAuxiliar = service.findPagamentoById(id);
			// TODO aqui deve ser pego o id do lancamento do lancamento acao
			lancamentoReembolso = service
					.getLancamentoById(pagamentoLancAuxiliar.getLancamentoAcao().getLancamento().getId());

			conta = pagamentoLancAuxiliar.getConta();

			projetoAux = null;

			if (pagamentoLancAuxiliar.getLancamentoAcao() != null
					&& pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica() != null) {
				projetoAux = new Projeto();
				projetoAux = pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica().getProjeto();
				listaRubricas();

			}

			dataReembolso = null;
			gerarReembolso = false;

			// orcamentosProjetos = service.getOrcamentoByProjeto(
			// pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica().getProjeto().getId());

			// for (OrcamentoProjeto orcProjeto : orcamentosProjetos) {
			// orcamentos.add(orcProjeto.getOrcamento());
			// }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

//	public void prepararEdicao(final Long id) {
//		try {
//			this.editarLinha = (this.editarLinha == null && false);
//			this.pagamentoLancAuxiliar = new PagamentoLancamento();
//			this.pagamentoLancAuxiliar = this.service.findPagamentoById(id);
//			this.projetoAux = null;
//			if (this.pagamentoLancAuxiliar.getLancamentoAcao() != null && this.pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica() != null) {
//				this.projetoAux = new Projeto();
//				this.projetoAux = this.pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica().getProjeto();
//				this.listaRubricas();
//			}
//		}
//		catch (Exception ex) {}
//	}

	public Boolean getContaAlterada() {

		if (pagamentoLancAuxiliar != null && conta != null && conta.getId() != null
				&& pagamentoLancAuxiliar.getConta() != null
				&& (pagamentoLancAuxiliar.getConta().getId().longValue() != conta.getId().longValue())) {
			return true;
		} else {
			dataReembolso = null;
			gerarReembolso = false;
			return false;
		}
	}

	public void limparDataReembolso() {
		if (!gerarReembolso)
			dataReembolso = null;
	}

	public void prepararEdicao(Lancamento l) {

		lancamentoMudarStatus = l;
	}

	private String tipoTarifa;

	public void carregarCategoriasFin() {
		listCategoriaFinanceira = service.buscarCategoriasFin();
	}

//	public void prepararTarifa(Long id) {
//		tarifa = new TarifaBancaria();
//		pagamentoLancAuxiliar = new PagamentoLancamento();
//		pagamentoLancAuxiliar = service.findPagamentoById(id);
//
//		tarifa.setContaPagador(pagamentoLancAuxiliar.getConta());
//		tarifa.setContaRecebedor(service.getDefaultRecebedorTarifa());
//		tarifa.setDataEmissao(pagamentoLancAuxiliar.getDataPagamento());
//		tarifa.setDataPagamento(pagamentoLancAuxiliar.getDataPagamento());
//		tarifa.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
//		tarifa.setQuantidadeParcela(1);
//		tarifa.setDataDocumentoFiscal(pagamentoLancAuxiliar.getDataPagamento());
//		// TODO pegar o lancamento do lancamento acao
//		tarifa.setDescricao(
//				"Tarifa referente ao lançamento: " + pagamentoLancAuxiliar.getLancamentoAcao().getLancamento().getId());
//		tarifa.setLancamentosAcoes(new ArrayList<>());
//		tarifa.setVersionLancamento("MODE01");
//		tarifa.setValorTotalComDesconto(service.getTarifa());
//		tarifa.setStatusCompra(StatusCompra.CONCLUIDO);
//		tarifa.setIdTarifado(pagamentoLancAuxiliar.getLancamentoAcao().getLancamento().getId());
//		tarifa.setDepesaReceita(DespesaReceita.DESPESA);
//
//		if (!pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica().getRubricaOrcamento().getOrcamento()
//				.getFonte().getNome().equals("Fundo Amazônia")) {
//			tarifa.getLancamentosAcoes().add(new LancamentoAcao(service.getTarifa(),
//					pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica()));
//
//			tipoTarifa = "projeto";
//		} else {
//			tarifa.getLancamentosAcoes().add(new LancamentoAcao(service.getTarifa(), service.getRubricaDespesasAdm()));
//			tipoTarifa = "doacao";
//		}
//
//		// Rubrica Orcamento ID = 64
//	}

	public void prepararTarifa(final Long id) {
		this.tarifa = new TarifaBancaria();
		this.pagamentoLancAuxiliar = new PagamentoLancamento();
		this.pagamentoLancAuxiliar = this.service.findPagamentoById(id);
		this.tarifa.setContaPagador(this.pagamentoLancAuxiliar.getConta());
		this.tarifa.setContaRecebedor(this.service.getDefaultRecebedorTarifa());
		this.tarifa.setDataEmissao(this.pagamentoLancAuxiliar.getDataPagamento());
		this.tarifa.setDataPagamento(this.pagamentoLancAuxiliar.getDataPagamento());
		this.tarifa.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
		this.tarifa.setQuantidadeParcela(Integer.valueOf(1));
		this.tarifa.setDataDocumentoFiscal(this.pagamentoLancAuxiliar.getDataPagamento());
		this.tarifa.setDescricao("Tarifa referente ao lan\u00e7amento: " + this.pagamentoLancAuxiliar.getLancamentoAcao().getLancamento().getId());
		this.tarifa.setLancamentosAcoes((List)new ArrayList());
		this.tarifa.setVersionLancamento("MODE01");
		this.tarifa.setValorTotalComDesconto(this.service.getTarifa());
		this.tarifa.setStatusCompra(StatusCompra.CONCLUIDO);
		this.tarifa.setIdTarifado(this.pagamentoLancAuxiliar.getLancamentoAcao().getLancamento().getId());
		this.tarifa.setDepesaReceita(DespesaReceita.DESPESA);
		if (!this.pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica().getRubricaOrcamento().getOrcamento().getFonte().getNome().equals("Fundo Amaz\u00f4nia")) {
			this.tarifa.getLancamentosAcoes().add(new LancamentoAcao(this.service.getTarifa(), this.pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica()));
			this.tipoTarifa = "projeto";
		}
		else {
			this.tarifa.getLancamentosAcoes().add(new LancamentoAcao(this.service.getTarifa(), this.service.getRubricaDespesasAdm()));
			this.tipoTarifa = "doacao";
		}
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
	
	public void validar() {
		if(ValidarAcesso.checkPasswordAprouve(usuarioSessao.getUsuario(), 
				  AprouveSigla.CONCILIACAO, 
				  aprouve, service)) {
			service.efetivarMultiplos(lancamentosSelected);
			carregarLancamentos();
			lancamentosSelected = new ArrayList<>();
		} else {
			addMessage("Erro de validação:", "Senha incorreta!", 
					FacesMessage.SEVERITY_WARN);
		}
	}

//	public void efetivarMultiplos() {
//
//		if (lancamentosSelected.isEmpty()) {
//			FacesContext.getCurrentInstance().addMessage("msg_lancamento", new FacesMessage(FacesMessage.SEVERITY_WARN,
//					"", "Selecione pelo menos um lançamento para efetivar!"));
//			return;
//		}
//
//		/*
//		 * for (LancamentoAuxiliar la : lancamentosSelected) { if
//		 * (la.getStatus().equals("EFETIVADO")) { continue; } }
//		 */
//
//		// TODO
//		if(ValidarAcesso.checkIfUserCanApprove(usuarioSessao.getUsuario(),
//											   AprouveSigla.CONCILIACAO,
//											   service)) {
//			executeScript("PF('dialogValid').show();");
//		} else {
//			addMessage("Sem autorização:", "Você não tem autorização para efetivar esse lançamento.", FacesMessage.SEVERITY_ERROR);
//		}
//	}

	public void efetivarMultiplos() {
		for (final LancamentoAuxiliar la : this.lancamentosSelected) {
			if (la.getStatus().equals("EFETIVADO")) {}
		}
		this.service.efetivarMultiplos((List)this.lancamentosSelected);
		this.carregarLancamentos();
		this.lancamentosSelected = new ArrayList<LancamentoAuxiliar>();
	}

	public void desprovisionar() {
		for (final LancamentoAuxiliar lancamento : this.lancamentosSelected) {
			if (lancamento.getStatus().equals("EFETIVADO")) {
				FacesContext.getCurrentInstance().addMessage("msg_lancamento", new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Existem um ou mais Lan\u00e7amentos Efetivados desmarque-os e tente novamente!"));
				return;
			}
		}
		this.service.desprovisionar((List)this.lancamentosSelected);
		this.carregarLancamentos();
		this.lancamentosSelected = new ArrayList<LancamentoAuxiliar>();
	}

	// TODO: Put it on the util package
	public void executeScript(String script) {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript(script);
	}

	public void openDialoEstorno() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('dialogEstorno').show()");
	}

	public void closeDialoEstorno() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('dialogEstorno').hide()");
	}

	public void reverseTransaction() {
		try {
//			aprouve.setSigla("ADM-II");
			if (ValidarAcesso.checkPasswordAprouve(usuarioSessao.getUsuario(), 
					AprouveSigla.CONCILIACAO, aprouve, service)) {
				service.reverseTransaction(lancamentosSelected);
				carregarLancamentos();
				closeDialoEstorno();
				FacesContext.getCurrentInstance().addMessage("msg_lancamento",
						new FacesMessage(FacesMessage.SEVERITY_INFO, "", "A(s) parcela(s) selecionada(s) voltaram ao status de pagamento 'PROVISIONADO'"));
			} else {
				FacesContext.getCurrentInstance().addMessage("msg_lancamento",
						new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Senha Incorreta!"));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("msg_lancamento",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro! ", e.getCause().getMessage()));
		}
	}

	public void verifyTransactionToReverse() {
		if (lancamentosSelected.isEmpty()) {
			FacesContext.getCurrentInstance().addMessage("msg_lancamento",
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Selecione pelo menos um lançamento!"));
			return;
		}
		for (LancamentoAuxiliar lancamento : lancamentosSelected) {
			if (!lancamento.getStatus().equals("EFETIVADO")) {
				FacesContext.getCurrentInstance().addMessage("msg_lancamento", new FacesMessage(
						FacesMessage.SEVERITY_WARN, "",
						"Existem um ou mais lançamentos que já estão como 'PROVISIONADOS', desmarque-os e tente novamente!"));
				return;
			}
		}
		executeScript("PF('dialogEstorno').show()");
	}

	public void verifyTransactionToCancel() {
		if (lancamentosSelected.isEmpty()) {
			FacesContext.getCurrentInstance().addMessage("msg_lancamento",
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Selecione pelo menos um lançamento!"));
			return;
		}
		executeScript("PF('cancel-transaction').show()");
	}
	
	public void verifyTransactionToOpen() {
		if (lancamentosSelected.isEmpty()) {
			FacesContext.getCurrentInstance().addMessage("msg_lancamento",
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Selecione pelo menos um lançamento!"));
			return;
		}
		executeScript("PF('open-transaction').show()");
	}

	public void cancelTransaction() {
		try {
			aprouve.setSigla("ADM-II");
			if (service.verifyAprouve(aprouve)) {
				service.cancelTransaction(lancamentosSelected);
				carregarLancamentos();
				lancamentosSelected = new ArrayList<>();
				executeScript("PF('cancel-transaction').hide()");
				FacesContext.getCurrentInstance().addMessage("msg_lancamento",
						new FacesMessage(FacesMessage.SEVERITY_INFO, "", "O(s) lançamento(s) foram cancelado(s) e excluído(s) da execução financeira."));
			} else {
				FacesContext.getCurrentInstance().addMessage("msg_lancamento",
						new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Senha Incorreta!"));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("msg_lancamento",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro! ", e.getCause().getMessage()));
		}
	}
	
	
	public void openTransaction() {
		try {
			aprouve.setSigla("ADM-II");
			if (service.verifyAprouve(aprouve)) {
				service.openTransaction(lancamentosSelected);
				carregarLancamentos();
				lancamentosSelected = new ArrayList<>();
				executeScript("PF('open-transaction').hide()");
				FacesContext.getCurrentInstance().addMessage("msg_lancamento",
						new FacesMessage(FacesMessage.SEVERITY_INFO, "", "O(s) lançamento(s) voltaram ao status de não iniciado."));
			} else {
				FacesContext.getCurrentInstance().addMessage("msg_lancamento",
						new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Senha Incorreta!"));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("msg_lancamento",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro! ", e.getCause().getMessage()));
		}
	}

	public void removerLancamento() {
		for (LancamentoAuxiliar la : lancamentosSelected) {
			if (la.getStatus().equals("EFETIVADO")) {
				FacesContext.getCurrentInstance().addMessage("msg_lancamento",
						new FacesMessage(FacesMessage.SEVERITY_WARN, "",
								"Existem um ou mais Lançamentos Efetivados desmarque-os e tente novamente!"));
				return;
			}
		}
		service.removerLancamento(lancamentosSelected);
		carregarLancamentos();
		lancamentosSelected = new ArrayList<>();
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

	public void carregarLancamentos() {
		lancamentos = new ArrayList<>();
		lancamentos = service.getLancamentosCP(filtro);
		// System.out.println("Lançamentos");
		// service.getPagamentosPE(filtro);
	}

	public void validarLancamentos() {
		service.validarLancamentos(lancamentosSelected);
		carregarLancamentos();
	}

	public void invalidarLancamentos() {
		service.invalidarLancamentos(lancamentosSelected);
		carregarLancamentos();
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

	// TODO: PADRÃO PREPARA DEVOLUÇÃO/LANÇAMENTO
	public void prepararReembolso(LancamentoAuxiliar l) {
		pagamentoLancamentoReembolsoDocTed = service.findPagamentoById(l.getIdPagamento());
		lancamentoReembolsoDocTed = service.getLancamentoById(l.getId());

		dataDevolucao = null;
	}

	public void prepararDevolucao(LancamentoAuxiliar l) {
		pagamento = service.findPagamentoById(l.getIdPagamento());
		lancamento = service.getLancamentoById(l.getId());
		dataDevolucao = null;
	}

	private List<Imposto> impostos = new ArrayList<>();

	public void prepararDetalhamento(Long id) {
		impostos = new ArrayList<>();
		impostos = service.buscarImpostoPorLancamento(id);
		pagamentos = new ArrayList<>();
		pagamentos = service.buscarParcelasPorLancamento(id);
		lancamento = new Lancamento();
		lancamento = service.getLancamentoById(id);
	}

	@Transactional
	public void inserirReembolso(String tipo, String descricao, String codigo, DespesaReceita despesaReceita,
			PagamentoLancamento pagamento, Lancamento lancamento, Date dataPagamento, ContaBancaria contaPagador,
			ContaBancaria contaRecebedor) {

		if (tipo.equals("reembolso")) {
			LancamentoReembolso reembolso = new LancamentoReembolso(lancamento);
			reembolso.setDataPagamento(dataPagamento);
			reembolso.setValorReembolso(pagamento.getValor());
			reembolso.setContaPagador(contaPagador);
			reembolso.setContaRecebedor(contaRecebedor);
			reembolso.setIdLancamento(lancamento.getId());
			reembolso.setQuantidadeParcela(1);
			reembolso.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
			reembolso.setDepesaReceita(despesaReceita);
			reembolso.setCodigo(codigo);
			reembolso.setDescricao(descricao);
			reembolso.setDataEmissao(new Date());

			reembolso = service.salvarReembolso(reembolso);

			LancamentoAcao lancamentoAcao = new LancamentoAcao(pagamento.getLancamentoAcao());
			lancamentoAcao.setLancamento(reembolso);
			lancamentoAcao = service.salvarLancamentoAcao(lancamentoAcao);

			pagamento.setId(null);
			pagamento.setLancamentoAcao(lancamentoAcao);
			pagamento.setContaRecebedor(contaRecebedor);
			pagamento.setConta(contaPagador);
			pagamento.setQuantidadeParcela(1);
			pagamento.setDataPagamento(dataPagamento);

			service.salvar(pagamento);
		} else {
			LancamentoDevDocTed devDocTed = new LancamentoDevDocTed(lancamento);
			devDocTed.setDataPagamento(dataPagamento);
			devDocTed.setValorReembolso(pagamento.getValor());
			devDocTed.setContaPagador(contaPagador);
			devDocTed.setContaRecebedor(contaRecebedor);
			devDocTed.setIdLancamento(lancamento.getId());
			devDocTed.setQuantidadeParcela(1);
			devDocTed.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
			devDocTed.setDepesaReceita(despesaReceita);
			devDocTed.setCodigo(codigo);
			devDocTed.setDescricao(descricao);
			devDocTed.setDataEmissao(new Date());

			devDocTed = service.salvarDevDocTed(devDocTed);

			LancamentoAcao lancamentoAcao = new LancamentoAcao(pagamento.getLancamentoAcao(), tipo);
			lancamentoAcao.setLancamento(devDocTed);
			lancamentoAcao = service.salvarLancamentoAcao(lancamentoAcao);

			PagamentoLancamento pgto = new PagamentoLancamento();

			// pagamento.setId(null);
			// pagamento.setLancamentoAcao(lancamentoAcao);
			// pagamento.setLancamento(devDocTed);
			// pagamento.setContaRecebedor(contaRecebedor);
			// pagamento.setConta(contaPagador);
			// pagamento.setQuantidadeParcela(1);
			// pagamento.setDataPagamento(dataPagamento);

			pgto.setId(null);
			pgto.setLancamentoAcao(lancamentoAcao);
			pgto.setContaRecebedor(contaRecebedor);
			pgto.setConta(contaPagador);
			pgto.setQuantidadeParcela(1);
			pgto.setDataPagamento(dataPagamento);
			pgto.setDataEmissao(new Date());
			pgto.setTipoParcelamento(lancamento.getTipoParcelamento());
			pgto.setValor(pagamento.getValor());
			pgto.setDespesaReceita(lancamentoAcao.getDespesaReceita());
			pgto.setStt(StatusPagamentoLancamento.PROVISIONADO);// Verificar se já deve vir EFETIVADO;

			service.salvar(pgto);
		}

	}

	public void inserirDevolucao(String descricao, String codigo, PagamentoLancamento pagamento, Lancamento lancamento,
			Date dataPagamento, ContaBancaria contaPagador, ContaBancaria contaRecebedor) {

		Devolucao devolucao = new Devolucao(lancamento);

		devolucao.setDataPagamento(dataPagamento);
		devolucao.setValorTotalComDesconto(pagamento.getValor());
		devolucao.setContaPagador(contaPagador);
		devolucao.setContaRecebedor(contaRecebedor);
		devolucao.setIdLancamento(lancamento.getId());
		devolucao.setQuantidadeParcela(1);
		devolucao.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
		devolucao.setDepesaReceita(DespesaReceita.RECEITA);
		devolucao.setCodigo(codigo);
		devolucao.setDescricao(descricao);
		devolucao.setDataEmissao(new Date());

		devolucao = service.salvarDevolucao(devolucao);

		LancamentoAcao lancamentoAcao = new LancamentoAcao(pagamento, "");
		lancamentoAcao.setLancamento(devolucao);
		lancamentoAcao = service.salvarLancamentoAcao(lancamentoAcao);

		PagamentoLancamento pgto = new PagamentoLancamento();

		pgto.setId(null);
		pgto.setLancamentoAcao(lancamentoAcao);
		pgto.setConta(contaPagador);
		pgto.setContaRecebedor(contaRecebedor);
		pgto.setQuantidadeParcela(1);
		pgto.setDataPagamento(dataPagamento);
		pgto.setDataEmissao(new Date());
		pgto.setTipoParcelamento(lancamento.getTipoParcelamento());
		pgto.setValor(pagamento.getValor());
		pgto.setDespesaReceita(lancamentoAcao.getDespesaReceita());
		pgto.setStt(StatusPagamentoLancamento.PROVISIONADO);// Verificar se já deve vir EFETIVADO;

		service.salvar(pgto);
	}

	// TODO: PADRÃO DEVOLUÇÃO
	public void salvarDevolucao() {

		try {
			if (dataDevolucao.before(pagamento.getDataPagamento()))
				throw new Exception("A data informada não deve ser menor que a data do pagamento ("
						+ new SimpleDateFormat("dd/MM/yyyy").format(pagamento.getDataPagamento()) + ")");

			String codigo = lancamento.getCodigo() != null ? lancamento.getCodigo() : null;

			inserirDevolucao("Devolução pelo fornecedor lancamento: " + lancamento.getId(), codigo, pagamento,
					lancamento, dataDevolucao, pagamento.getContaRecebedor(), pagamento.getConta());

			dataDevolucao = null;
			addMessage("", "Devolução registrada", FacesMessage.SEVERITY_INFO);
			carregarLancamentos();

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("msg_devdocted",
					new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), ""));
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('dlg_reembolso').show();");
		}
	}

	public void salvarReembolsoDocTed() {

		try {
			if (dataDevolucao.before(pagamentoLancamentoReembolsoDocTed.getDataPagamento()))
				throw new Exception("A data informada não deve ser menor que a data do pagamento ("
						+ new SimpleDateFormat("dd/MM/yyyy")
								.format(pagamentoLancamentoReembolsoDocTed.getDataPagamento())
						+ ")");

			String codigo = lancamentoReembolsoDocTed.getCodigo() != null
					? lancamentoReembolsoDocTed.getCodigo().replace("SP", "DT")
					: null;
			inserirReembolso("devdocted",
					"DEVDOCTED - Lançamento " + lancamentoReembolsoDocTed.getId() + " Parcela "
							+ pagamentoLancamentoReembolsoDocTed.getNumeroDaParcela(),
					codigo, DespesaReceita.RECEITA, pagamentoLancamentoReembolsoDocTed, lancamentoReembolsoDocTed,
					dataDevolucao, lancamentoReembolsoDocTed.getContaRecebedor(),
					pagamentoLancamentoReembolsoDocTed.getConta());
			inserirReembolso("posdevdocted", "PÓS DEVDOCTED - " + lancamentoReembolsoDocTed.getDescricao(),
					lancamentoReembolsoDocTed.getCodigo(), lancamentoReembolsoDocTed.getDepesaReceita(),
					pagamentoLancamentoReembolsoDocTed, lancamentoReembolsoDocTed, dataDevolucao,
					pagamentoLancamentoReembolsoDocTed.getConta(), lancamentoReembolsoDocTed.getContaRecebedor()); // INVERTE
																													// OS
																													// PAPEIS
																													// DE
																													// PAGADOR
																													// E
																													// RECEBEDOR

			dataDevolucao = null;
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("msg_devdocted",
					new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), ""));
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('dlg_reembolso').show();");
		}
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

	public TarifaBancaria getTarifa() {
		return tarifa;
	}

	public void setTarifa(TarifaBancaria tarifa) {
		this.tarifa = tarifa;
	}

	public String getTipoTarifa() {
		return tipoTarifa;
	}

	public void setTipoTarifa(String tipoTarifa) {
		this.tipoTarifa = tipoTarifa;
	}

	public ArquivoLancamento getArquivo() {
		return arquivo;
	}

	public void setArquivo(ArquivoLancamento arquivo) {
		this.arquivo = arquivo;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public ArquivoLancamento getArqAuxiliar() {
		return arqAuxiliar;
	}

	public void setArqAuxiliar(ArquivoLancamento arqAuxiliar) {
		this.arqAuxiliar = arqAuxiliar;
	}

	public InputText getInputNomeArquivo() {
		return inputNomeArquivo;
	}

	public void setInputNomeArquivo(InputText inputNomeArquivo) {
		this.inputNomeArquivo = inputNomeArquivo;
	}

	public List<CategoriaFinanceira> getListCategoriaFinanceira() {
		return listCategoriaFinanceira;
	}

	public void setListCategoriaFinanceira(List<CategoriaFinanceira> listCategoriaFinanceira) {
		this.listCategoriaFinanceira = listCategoriaFinanceira;
	}

	public StatusCompra[] getStatus() {
		return StatusCompra.values();
	}

	public String mudarStatusLancamento() {

		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")) {
			LogStatus log = new LogStatus(new Date());
			log.setLancamento(service.getLancamentoById(lancamentoMudarStatus.getId()));
			log.setStatusLog(lancamentoMudarStatus.getStatusCompra());
			log.setUsuario(usuarioSessao.getUsuario());

			service.mudarStatusLancamento(lancamentoMudarStatus.getId(), lancamentoMudarStatus.getStatusCompra(), log);
		}
		return "";
	}

	public Lancamento getLancamentoMudarStatus() {
		return lancamentoMudarStatus;
	}

	public void setLancamentoMudarStatus(Lancamento lancamentoMudarStatus) {
		this.lancamentoMudarStatus = lancamentoMudarStatus;
	}

	public Boolean getEditarLinha() {
		return editarLinha;
	}

	public void setEditarLinha(Boolean editarLinha) {
		this.editarLinha = editarLinha;
	}

	// Getters e Setters reembolso docted
	public Lancamento getLancamentoReembolsoDocTed() {
		return lancamentoReembolsoDocTed;
	}

	public void setLancamentoReembolsoDocTed(Lancamento lancamentoReembolsoDocTed) {
		this.lancamentoReembolsoDocTed = lancamentoReembolsoDocTed;
	}

	public PagamentoLancamento getPagamentoLancamentoReembolsoDocTed() {
		return pagamentoLancamentoReembolsoDocTed;
	}

	public void setPagamentoLancamentoReembolsoDocTed(PagamentoLancamento pagamentoLancamentoReembolsoDocTed) {
		this.pagamentoLancamentoReembolsoDocTed = pagamentoLancamentoReembolsoDocTed;
	}

	public Date getDataDevolucao() {
		return dataDevolucao;
	}

	public void setDataDevolucao(Date dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}

	public Lancamento getLancamentoReembolso() {
		return lancamentoReembolso;
	}

	public void setLancamentoReembolso(Lancamento lancamentoReembolso) {
		this.lancamentoReembolso = lancamentoReembolso;
	}

	public Date getDataReembolso() {
		return dataReembolso;
	}

	public void setDataReembolso(Date dataReembolso) {
		this.dataReembolso = dataReembolso;
	}

	public Boolean getGerarReembolso() {
		return gerarReembolso;
	}

	public void setGerarReembolso(Boolean gerarReembolso) {
		this.gerarReembolso = gerarReembolso;
	}

	public List<Imposto> getImpostos() {
		return impostos;
	}

	public void setImpostos(List<Imposto> impostos) {
		this.impostos = impostos;
	}

	public byte[] getConteudo() {
		return conteudo;
	}

	public void setConteudo(byte[] conteudo) {
		this.conteudo = conteudo;
	}
}
