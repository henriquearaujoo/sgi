
package managedbean;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import exception.NegocioException;
import model.Acao;
import model.Aprouve;
import model.AtividadeProjeto;
import model.CategoriaDespesaClass;
import model.CategoriaFinanceira;
import model.CategoriaProjeto;
import model.CategoriadeDespesa;
import model.CondicaoPagamento;
import model.ContaBancaria;
import model.DespesaReceita;
import model.Estado;
import model.FontePagadora;
import model.Fornecedor;
import model.Gestao;
import model.Lancamento;
import model.LancamentoAcao;
import model.Localidade;
import model.LogStatus;
import model.MenuLateral;
import model.Municipio;
import model.OrcamentoProjeto;
import model.Projeto;
import model.ProjetoRubrica;
import model.Rubrica;
import model.SolicitacaoPagamento;
import model.StatusAdiantamento;
import model.StatusCompra;
import model.TipoDeDocumentoFiscal;
import model.TipoGestao;
import model.TipoLocalidade;
import model.TipoParcelamento;
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
import service.AdiantamentoService;
import service.CompraService;
import service.HomeService;
import service.PagamentoService;
import service.ProjetoService;
import service.SolicitacaoPagamentoService;
import util.ArquivoLancamento;
import util.CDILocator;
import util.DataUtil;
import util.DiretorioUtil;
import util.DownloadUtil;
import util.Filtro;
import util.MakeMenu;
import util.ReportUtil;
import util.UsuarioSessao;
import util.Util;

@Named(value = "adiantamentoController")
@ViewScoped
public class AdiantamentoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private @Inject PagamentoService service;
	private @Inject AdiantamentoService adiantamentoService;
	private @Inject SolicitacaoPagamento adiantamento;

	@Inject
	private CalculatorRubricaRepositorio calculatorRubricaRepositorio;

	@Inject
	private ProjetoService projetoService;

	@Inject
	private UsuarioSessao usuarioSessao;
	@Inject
	private LancamentoAcao lancamentoAcao;

	private @Inject ArquivoLancamento arqAuxiliar;

	@Inject
	private Fornecedor fornecedor;

	private String path = "";

	private Lancamento lancamentoAux = new Lancamento();

	private Boolean inserirDescricao = false;

	private ArquivoLancamento arquivo = new ArquivoLancamento();

	private MakeMenu hybrid = new MakeMenu();

	private Long idOrcamento;

	private List<MenuLateral> utilMenu = hybrid.getMenuFinanceiro();

	private List<Projeto> projetos = new ArrayList<>();

	private List<CategoriaDespesaClass> categorias = new ArrayList<>();

	private List<CategoriaProjeto> listCategoriaProjeto = new ArrayList<>();

	private List<CategoriaFinanceira> listCategoriaFinanceira = new ArrayList<>();

	private List<Projeto> listaProjeto;

	public List<Projeto> getListaProjeto() {
		return listaProjeto;
	}

	public void setListaProjeto(List<Projeto> listaProjeto) {
		this.listaProjeto = listaProjeto;
	}

	public List<MenuLateral> getUtilMenu() {
		return utilMenu;
	}

	public void setUtilMenu(List<MenuLateral> utilMenu) {
		this.utilMenu = utilMenu;
	}

	public CategoriadeDespesa[] categoriasDespesas() {
		return CategoriadeDespesa.values();
	}

	public void carregarProjetos() {

		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro")) {
			projetos = projetoService.getProjetosbyUsuarioProjeto(null, filtro);
			return;
		}

		projetos = projetoService.getProjetosbyUsuarioProjeto(usuarioSessao.getUsuario(), filtro);

	}

	private List<Fornecedor> fornecedores = new ArrayList<Fornecedor>();

	private MenuLateral menu = new MenuLateral();
	private Filtro filtro = new Filtro();

	private Long idEstado;
	private String local;
	private String tipoGestao = "";
	private Boolean panelListagem = true;
	private Boolean panelCadastro = false;

	private List<Gestao> gestoes;

	private List<FontePagadora> allFontes = new ArrayList<FontePagadora>();
	private List<Acao> allAcoes = new ArrayList<Acao>();

	private List<Localidade> localidades = new ArrayList<Localidade>();
	private List<SolicitacaoPagamento> pagamentosFiltered = new ArrayList<SolicitacaoPagamento>();
	private List<Estado> estados;
	private List<SolicitacaoPagamento> pagamentos = new ArrayList<SolicitacaoPagamento>();

	private List<OrcamentoProjeto> orcamentosProjetos = new ArrayList<>();

	private List<ProjetoRubrica> listaDeRubricasProjeto = new ArrayList<>();

	private String titulo = "Solicitação de pagamento";

	private Projeto projetoAux;

	private UploadedFile file;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	@Inject
	private ProjetoService gestaoProjeto;

	public List<Gestao> completeGestao(String query) {
		return gestaoProjeto.getGestaoAutoComplete(query);
	}

	public List<Localidade> completeLocalidade(String s) {
		if (s.length() > 2)
			return adiantamentoService.buscaLocalidade(s);

		return new ArrayList<Localidade>();
	}

	private Integer stepIndex = 0;

	public void initCadastro() {
		carregarProjetos();
		carregarRubricas();
		carregarCategoriasFin();

		if (adiantamento.getId() != null) {
			PrimeFaces.current().executeScript("setarFocused('info-geral')");
		}

	}
	
	public void dialogHelp() {
		HashMap<String, Object> options = new HashMap<>();
		options.put("width", "100%");
		options.put("height", "100%");
		options.put("contentWidth", "100%");
		options.put("contentHeight", "100%");
		options.put("resizable", false);
		options.put("minimizable",true);
		options.put("maximizable",true);
		PrimeFaces.current().dialog().openDynamic("dialog/help/help_adiantamentos", options, null);
	}

	public void steping(String modo) {
		if (modo.equalsIgnoreCase("next")) {
			if (stepIndex == 2) {
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

	@Inject
	private HomeService homeService;

	// @PostConstruct
	public void init() throws IOException {

		if (homeService.verificarSolicitacoes(usuarioSessao.getIdColadorador())) {
			HttpServletResponse resp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			resp.sendRedirect(req.getContextPath() + "/main/autorizacoes.xhtml");
		}

		filtro = new Filtro();
		filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		filtro.setDataFinal(DataUtil.getDataFinal(new Date()));
		filtro.setTipoLancamento("ad");
		fetchOrders();
		carregarProjetos();
		carregarCategoriasFin();
		PrimeFaces.current().executeScript("setarFocused('filter-list')");
		// getProjetoByUsuario();
		// carregarCategorias();
	}

	public boolean poderEditar() {
		if (adiantamento.getId() != null) {
			if (adiantamento.getStatusCompra() == null
					|| adiantamento.getStatusCompra().getNome().equals("Não iniciado/Solicitado")
					|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("compra")
					|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")) {

				return true;
			} else {
				Long idUser = usuarioSessao.getUsuario().getColaborador().getId().longValue();
				Long idSolicintante = adiantamento.getSolicitante().getId().longValue();

				Long idGestor = adiantamento.getGestao().getColaborador().getId();

				// if (idUser.longValue() == idSolicintante.longValue() || idUser.longValue() ==
				// idGestor.longValue()) {
				if ((idUser.longValue() == idSolicintante.longValue() || idUser.longValue() == idGestor.longValue())
						&& !adiantamento.getStatusCompra().getNome().equals("Concluído")) {
					return true;
				} else {
					return false;
				}

			}

		} else {
			return true;
		}
	}

	// novo módulo
	public void carregarCategoriasFin() {
		listCategoriaFinanceira = adiantamentoService.buscarCategoriasFin();
	}

	public List<ProjetoRubrica> completeCategoria(String s) {
		if (s.length() > 2) {
			if (projetoAux == null) {
				lancamentoAcao = new LancamentoAcao();

				addMessage("msg", "Você deve selecionar um projeto antes de buscar rubrica! ",
						FacesMessage.SEVERITY_WARN);

				return new ArrayList<ProjetoRubrica>();
			} else {
				return calculatorRubricaRepositorio.getProjetoRubricaByStrNormalizado(s);
			}

		} else {
			return new ArrayList<>();
		}
		// return pagamentoService.completeRubricasDeProjetoJOIN(s);
	}

	public void listaRubricas() {

		if (projetoAux == null) {
			lancamentoAcao = new LancamentoAcao();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você deve selecionar um projeto antes de buscar rubrica! ");
			FacesContext.getCurrentInstance().addMessage(null, message);

			FacesContext.getCurrentInstance().addMessage(null, message);
			listaDeRubricasProjeto = new ArrayList<ProjetoRubrica>();
		} else {
			listaDeRubricasProjeto = calculatorRubricaRepositorio.getProjetoRubricaByProjeto(projetoAux.getId());
		}

	}

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

	private List<AtividadeProjeto> listaAtividades;

	public void findAtividadesByProjeto() {
		setListaAtividades(projetoService.getAtividades(projetoAux));

	}

	public void findAtividadesByProjetoWithSaldo() {
		setListaAtividades(projetoService.getAtividadeWithSaldoByProjeto(projetoAux.getId()));
	}

	public List<AtividadeProjeto> getListaAtividades() {
		return listaAtividades;
	}

	public void setListaAtividades(List<AtividadeProjeto> listaAtividades) {
		this.listaAtividades = listaAtividades;
	}

	// public void carregarProjetos() {
	// projetos = compraService.getProjetosFiltroPorUsuarioMODE01(filtro,
	// usuarioSessao.getUsuario());
	// }
	//
	public List<Projeto> completeProjetos(String query) {
		projetos = new ArrayList<Projeto>();
		if (query != null && !(query.isEmpty())) {
			projetos = service.getProjetoAutoCompleteMODE01(query);
		}
		return projetos;
	}

	public void fetchOrders() {
		pagamentos = adiantamentoService.fetchOrdersAdvancedPayment(filtro);

	}

	public void carregarDetalhesDeRecurso() {
		if (lancamentoAcao.getProjetoRubrica().getProjeto() == null) {
			orcamentosProjetos = new ArrayList<>();
			listaDeRubricasProjeto = new ArrayList<>();
			return;
		}

		carregarOrcamentosProjeto();
		carregarRubricasDeProjetoAndOrcamento();
		// carregarRubricasDeProjeto();
	}

	public void carregarRubricasDeProjetoAndOrcamentoSelect() {
		listaDeRubricasProjeto = new ArrayList<>();
		if (lancamentoAcao.getProjetoRubrica().getProjeto().getId() != null)
			listaDeRubricasProjeto = compraService.getRubricasDeProjetoJOIN(
					lancamentoAcao.getProjetoRubrica().getProjeto().getId(),
					lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getId());
	}

	public void carregarRubricasDeProjetoAndOrcamento() {
		if (lancamentoAcao.getProjetoRubrica() == null)
			return;
		OrcamentoProjeto op = new OrcamentoProjeto();
		if (orcamentosProjetos.size() > 0) {
			op = orcamentosProjetos.get(0);
			listaDeRubricasProjeto = new ArrayList<>();
			if (lancamentoAcao.getProjetoRubrica().getProjeto().getId() != null)
				listaDeRubricasProjeto = compraService.getRubricasDeProjetoJOIN(
						lancamentoAcao.getProjetoRubrica().getProjeto().getId(), op.getOrcamento().getId());

		}
	}

	public void carregarRubricasDeProjeto() {
		listaDeRubricasProjeto = new ArrayList<>();
		if (lancamentoAcao.getProjetoRubrica().getProjeto().getId() != null)
			listaDeRubricasProjeto = compraService
					.getRubricasDeProjeto(lancamentoAcao.getProjetoRubrica().getProjeto().getId(), null);
	}

	public void carregarOrcamentosProjeto() {
		if (lancamentoAcao.getProjetoRubrica().getProjeto() == null)
			return;

		orcamentosProjetos = new ArrayList<>();
		if (lancamentoAcao.getProjetoRubrica().getProjeto() != null) {
			orcamentosProjetos = compraService
					.getOrcamentoByProjeto(lancamentoAcao.getProjetoRubrica().getProjeto().getId());
			if (orcamentosProjetos.size() > 0) {
				idOrcamento = orcamentosProjetos.get(0).getId();
			} else {
				idOrcamento = null;
			}
		}
	}

	public TipoGestao buscarTipoGestao() {
		String type = adiantamento.getGestao().getType();
		switch (type) {
		case "coord":
			return TipoGestao.COORD;
		case "reg":
			return TipoGestao.REGIONAL;
		default:
			return TipoGestao.SUP;
		}
	}

	public TipoLocalidade buscarTipoLocalidade() {

		String type = adiantamento.getLocalidade().getType();
		switch (type) {
		case "mun":
			return TipoLocalidade.MUNICIPIO;
		case "uc":
			return TipoLocalidade.UC;
		case "com":
			return TipoLocalidade.COMUNIDADE;
		case "sede":
			return TipoLocalidade.SEDE;
		default:
			return TipoLocalidade.NUCLEO;
		}
	}

	private @Inject RubricaRepositorio rubricaRepositorio;
	private List<Rubrica> rubricas = new ArrayList<>();

	public void carregarRubricas() {
		rubricas = rubricaRepositorio.getRubricas();
	}

	public CategoriaDespesaClass getCategoria() {
		CategoriaDespesaClass c = new CategoriaDespesaClass();
		c.setId(new Long(34));
		c.setNome("Categoria de despesa");
		return c;
	}

	public void iniciarFiltro() {
		filtro = new Filtro();
		local = "";
		tipoGestao = "";
	}

	@Deprecated
	public void removerAcao(int index) {
		BigDecimal valor = adiantamento.getLancamentosAcoes().get(index).getValor();
		LancamentoAcao la = adiantamento.getLancamentosAcoes().get(index);

		adiantamento.setValorTotalComDesconto(adiantamento.getValorTotalComDesconto().subtract(valor));
		adiantamento.getLancamentosAcoes().remove(index);
		lancamentoAcao = new LancamentoAcao();

	}

	public void removerAcao(LancamentoAcao lancamentoAcao) {
		
		//BigDecimal valor = lancamentoAcao.getValor();
		adiantamento.setValorTotalComDesconto(BigDecimal.ZERO);
		adiantamento.getLancamentosAcoes().remove(lancamentoAcao);
		
		if (adiantamento.getId() != null && lancamentoAcao.getId() != null) {
			service.removerLancamentoAcao(lancamentoAcao);
			if (projetoAux != null && projetoAux.getId() != null) {
				listaRubricasAndAtividades();
			}
			// adiantamentoService.salvar(pagamento, usuarioSessao.getUsuario());
		}

	}

	public String aprovar() {
		if (verificarPrivilegioAprovacao()) {
			LogStatus log = new LogStatus();
			log.setUsuario(usuarioSessao.getUsuario());
			log.setLancamento(adiantamento);
			log.setData(new Date());
			log.setStatusLog(StatusCompra.CONCLUIDO);
			log.setSigla(adiantamento.getGestao().getSigla());
			log.setSiglaPrivilegio("ASP");
			adiantamentoService.mudarStatus(adiantamento.getId(), StatusCompra.CONCLUIDO, log);
			return "adiantamentos?faces-redirect=true";
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você não tem privilégios para esse tipo de ação, por favor contate o administrador do sistema!");
			FacesContext.getCurrentInstance().addMessage(null, message);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return "";
		}

	}

	public String suspender() {
		if (verificarPrivilegioSuspensao()) {
			LogStatus log = new LogStatus();
			log.setUsuario(usuarioSessao.getUsuario());
			log.setLancamento(adiantamento);
			log.setData(new Date());
			log.setStatusLog(StatusCompra.CANCELADO);
			log.setSiglaPrivilegio("SSP");
			log.setSigla(adiantamento.getGestao().getSigla());
			adiantamentoService.mudarStatus(adiantamento.getId(), StatusCompra.CANCELADO, log);
			adiantamento = adiantamentoService.getPagamentoById(adiantamento.getId());
			return "adiantamento?faces-redirect=true";
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você não tem privilégios para esse tipo de ação, por favor contate o administrador do sistema!");
			FacesContext.getCurrentInstance().addMessage(null, message);
			FacesContext.getCurrentInstance().addMessage(null, message);
			return "";
		}

	}

	public boolean verificarPrivilegioSuspensao() {
		Aprouve ap = new Aprouve();
		ap.setSigla("SSP");
		ap.setUsuario(usuarioSessao.getUsuario());
		return adiantamentoService.findAprouve(ap);
	}

	public boolean verificarPrivilegioAprovacao() {
		Aprouve ap = new Aprouve();
		ap.setSigla("ASP");
		ap.setUsuario(usuarioSessao.getUsuario());
		return adiantamentoService.findAprouve(ap);
	}

	public void adicionarNovaAcao() {

		if (verificaCamposRecurso()) {
			return;
		}

		if (!calculatorRubricaRepositorio.verificarSaldoRubrica(lancamentoAcao.getProjetoRubrica().getId(),
				lancamentoAcao.getValor())) {
			addMessage("", "Saldo de rubrica insuficiente", FacesMessage.SEVERITY_WARN);
			return;
		}

		if (lancamentoAcao.getAtividade() != null && lancamentoAcao.getAtividade().getId() != null) {
			if (!projetoService.verificaSaldoAtividade(lancamentoAcao.getAtividade().getId(),
					lancamentoAcao.getValor())) {
				addMessage("", "Atividade sem saldo suficiente", FacesMessage.SEVERITY_WARN);
				return;
			}
		}

		for (LancamentoAcao la : adiantamento.getLancamentosAcoes()) {
			if (la.getProjetoRubrica().equals(lancamentoAcao.getProjetoRubrica())) {
				addMessage("", "Não é Permitido Adição de Duas Linhas Orçamentárias Iguais.",
						FacesMessage.SEVERITY_WARN);
				return;
			}
		}

		lancamentoAcao.setDespesaReceita(DespesaReceita.DESPESA);
		// lancamentoAcao.setProjeto(lancamentoAcao.getProjetoRubrica().getProjeto());
		// lancamentoAcao.setOrcamento(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento());
		adiantamento.setValorTotalComDesconto(lancamentoAcao.getValor());
		lancamentoAcao.setLancamento(adiantamento);
		adiantamento.getLancamentosAcoes().add(lancamentoAcao);

		lancamentoAcao = new LancamentoAcao();
	}

	public Boolean verificaCamposRecurso() {

		Boolean retorno = false;

		if (lancamentoAcao.getProjetoRubrica() == null) {
			addMessage("", "Preencha o campo 'Linha orçamentária' antes de prosseguir.", FacesMessage.SEVERITY_ERROR);
			retorno = true;
		}

		if (lancamentoAcao.getValor() == null) {
			addMessage("", "Preencha o campo 'Valor' antes de prosseguir.", FacesMessage.SEVERITY_ERROR);
			retorno = true;
		}

		return retorno;
	}

	private byte[] conteudo;

	public void adicionarArquivo() throws IOException {

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

		arquivo.setLancamento(adiantamento);

		if (adiantamento.getArquivos() == null) {
			adiantamento.setArquivos(new ArrayList<>());
		}

		adiantamento.getArquivos().add(arquivo);

		path = "";

		arquivo = new ArquivoLancamento();
		// carregarArquivos(compra.getId());
	}

	public void carregarArquivos() {
		lancamentoAux = adiantamentoService.getLancamentoById(lancamentoAux.getId());
		lancamentoAux.setArquivos(new ArrayList<ArquivoLancamento>());
		lancamentoAux.setArquivos(adiantamentoService.getArquivoByLancamento(lancamentoAux.getId()));
	}

	public void removerArquivo() {
		adiantamento.getArquivos().remove(arqAuxiliar);
	}

	private @Inject Aprouve aprouve;

	public Boolean verificarPrivilegioENF() {
		aprouve.setSigla("ENF");
		aprouve.setUsuario(usuarioSessao.getUsuario());
		// aprouve.setSigla(compra.getGestao().getSigla());
		return adiantamentoService.verificarPrivilegioENF(aprouve);
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		String pathFinal = DiretorioUtil.DIRECTORY_UPLOAD;
		conteudo = event.getFile().getContents();
		arquivo.setNome(event.getFile().getFileName());
		arquivo.setPath(pathFinal + gerarCodigoArquivo() + "." + event.getFile().getFileName());
		arquivo.setData(new Date());
		arquivo.setUsuario(usuarioSessao.getUsuario());
		// adicionarArquivo();
	}

	public String gerarCodigoArquivo() {

		StringBuilder s = new StringBuilder();

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

		return s.toString();
	}

	public CondicaoPagamento[] getCondicoes() {
		return CondicaoPagamento.values();
	}

	public void carregarCategoriasDeProjeto() {
		if (!(lancamentoAcao == null || lancamentoAcao.getProjetoRubrica() == null)) {
			filtro.setProjetoId(lancamentoAcao.getProjetoRubrica().getProjeto().getId());
			listCategoriaProjeto = compraService.getCategoriasDeProjeto(filtro);
		}
	}

	public void supender() {

	}

	public void cancelar() {

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

	public void visualizarArquivo() throws IOException {
		DownloadUtil.visualizarArquivo(arqAuxiliar.getNome(), arqAuxiliar.getPath(), "application/pdf",
				FacesContext.getCurrentInstance());
		arqAuxiliar = new ArquivoLancamento();
	}

	public void baixarArquivo() throws IOException {
		DownloadUtil.baixarArquivo(arqAuxiliar.getNome(), arqAuxiliar.getPath(), "application/pdf",
				FacesContext.getCurrentInstance());
		arqAuxiliar = new ArquivoLancamento();
	}

	public void limpar() {
		tipoGestao = "";
		local = "";
		adiantamento = new SolicitacaoPagamento();
		adiantamento.setCategoriaDespesaClass(new CategoriaDespesaClass());
	}

	public void onTipoGestaoChange() {
		tipoGestao = "";
		if (adiantamento.getTipoGestao() != null) {
			tipoGestao = adiantamento.getTipoGestao().getNome();
			GestaoRepositorio repo = CDILocator.getBean(GestaoRepositorio.class);
			gestoes = new ArrayList<>();
			gestoes = adiantamento.getTipoGestao().getGestao(repo);
		}
	}

	public void onTipoGestaoChangeFiltro() {
		tipoGestao = "";
		if (filtro.getTipoGestao() != null) {
			tipoGestao = filtro.getTipoGestao().getNome();
			GestaoRepositorio repo = CDILocator.getBean(GestaoRepositorio.class);
			gestoes = new ArrayList<>();
			gestoes = filtro.getTipoGestao().getGestao(repo);
		}
	}

	public List<FontePagadora> completeFonte(String query) {
		allFontes = new ArrayList<FontePagadora>();
		allFontes = adiantamentoService.fontesAutoComplete(query);
		return allFontes;
	}

	public List<Fornecedor> completeFornecedor(String query) {
		fornecedores = new ArrayList<Fornecedor>();
		fornecedores = adiantamentoService.getFornecedores(query);
		return fornecedores;
	}

	private List<ContaBancaria> contas = new ArrayList<>();

	public List<ContaBancaria> completeContaFornecedorAtivos(String query) {
		contas = new ArrayList<>();
		contas = adiantamentoService.getContasFornecedoresAtivos(query);
		return contas;
	}

	public List<Acao> completeAcoes(String query) {
		allAcoes = new ArrayList<Acao>();
		allAcoes = adiantamentoService.acoesAutoComplete(query);
		return allAcoes;
	}

	public void iniciarNovoPagamento() {
		panelCadastro = true;
		panelListagem = false;
		carregarCategorias();
		carregarRubricas();
		limpar();
		carregarProjetos();
	}

	public void carregarCategorias() {
		categorias = adiantamentoService.buscarCategoriasFinanceira();
	}

	public void editarPagamento() {

		pagamentosFiltered = new ArrayList<>();

		adiantamento = adiantamentoService.getPagamentoById(adiantamento.getId());
		adiantamento.setLancamentosAcoes(adiantamentoService.getLancamentosAcoes(adiantamento));
		tipoGestao = adiantamento.getTipoGestao().getNome();
		carregarProjetos();
		local = adiantamento.getTipoLocalidade().getNome();
		if (adiantamento.getLocalidade() instanceof Municipio) {

			estados = adiantamentoService.getEstados(new Filtro());
			idEstado = ((Municipio) adiantamento.getLocalidade()).getEstado().getId();
			localidades = adiantamentoService.getMunicipioByEstado(idEstado);
		} else {
			LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
			localidades = adiantamento.getTipoLocalidade().getLocalidade(repo);

		}

		GestaoRepositorio repo = CDILocator.getBean(GestaoRepositorio.class);
		gestoes = new ArrayList<>();
		gestoes = adiantamento.getTipoGestao().getGestao(repo);
		carregarRubricas();
		carregarCategorias();
	}

	public void uploadImagen(FileUploadEvent event) {
		try {
			UploadedFile uf = event.getFile();
			InputStream in = new BufferedInputStream(uf.getInputstream());
			File file = new File("C://fotosJSF//" + uf.getFileName());
			String caminho = file.getAbsolutePath();
			FileOutputStream fos = new FileOutputStream(file);
			while (in.available() != 0) {
				fos.write(in.read());
			}
			fos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void upload() {
		if (file != null) {
			try {
				InputStream in = new BufferedInputStream(file.getInputstream());
				File nFile = new File("C://fotosJSF//" + file.getFileName());
				String caminho = nFile.getAbsolutePath();
				FileOutputStream fos = new FileOutputStream(nFile);
				while (in.available() != 0) {
					fos.write(in.read());
				}
				fos.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void iniciarNovoLancamentoAcao() {
		lancamentoAcao = new LancamentoAcao();
	}

	public String voltarListagem() {
		pagamentos = adiantamentoService.getSolicitacaoPagamentos(filtro);
		limpar();
		return "/main/adiantamentos?faces-redirect=true";
	}

	public void salvar(Integer type) throws NegocioException {

		adiantamento.setTipov4("SA");
		adiantamentoService.salvar(adiantamento, usuarioSessao.getUsuario(), type);

		executeScript("PF('dlg_salvar').hide();");

		if (type == 1) {
			executeScript("PF('dlg_email').show();");
		}

	}

	public void enviarEmailAutorizacaoSA() {
		adiantamentoService.enviarEmailAutorizacaoSA(adiantamento, 0);
	}

	private void executeScript(String script) {
		PrimeFaces.current().executeScript(script);
	}

	public String mudarStatusLancamento(Lancamento l) {
		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")) {
			LogStatus log = new LogStatus(new Date());
			log.setLancamento(l);
			log.setStatusLog(l.getStatusCompra());
			log.setUsuario(usuarioSessao.getUsuario());

			service.mudarStatusLancamento(l.getId(), l.getStatusCompra(), log);
		}
		return "";
	}

	public DespesaReceita[] getDespesaReceita() {
		return DespesaReceita.values();
	}

	public TipoParcelamento[] tiposParcelas() {
		return TipoParcelamento.values();
	}

	public void deletarCompra() {
	}

	public void adiicionarNovoItem() {
	}

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public void onLocalChangeForProject() {
		local = "";
		if (adiantamento.getTipoLocalidade() != null) {
			local = adiantamento.getTipoLocalidade().getNome();
			if (local.equals("Municipio")) {
				estados = new ArrayList<>();
				estados = adiantamentoService.getEstados(new Filtro());
				localidades = new ArrayList<>();
				idEstado = new Long(0);
			} else {
				LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
				localidades = adiantamento.getTipoLocalidade().getLocalidade(repo);
			}
		}
	}

	public void limparFiltro() {
		filtro = new Filtro();
		tipoGestao = "";
		local = "";
	}

	public void onLocalChangeFilter() {
		local = "";
		if (filtro.getTipoLocalidade() != null) {
			local = filtro.getTipoLocalidade().getNome();
			if (local.equals("Municipio")) {
				estados = new ArrayList<>();
				estados = adiantamentoService.getEstados(new Filtro());
				localidades = new ArrayList<>();
				idEstado = new Long(0);
			} else {
				LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
				localidades = filtro.getTipoLocalidade().getLocalidade(repo);
			}
		}
	}

	public void filtrar() {
		filtro.setTipoLancamento("ad");
		pagamentos = adiantamentoService.getSolicitacaoPagamentos(filtro);

	}

	private @Inject CompraService compraService;

	public void imprimir() {

		// List<ItemCompra> itens =
		// compraService.getItensTransientByCompra(compra);

		NumberFormat z = NumberFormat.getCurrencyInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		adiantamento = adiantamentoService.getPagamentoById(adiantamento.getId());

		List<LancamentoAcao> acoes = adiantamentoService.getLancamentosAcoes(adiantamento);

		StringBuilder acs = new StringBuilder();
		if (adiantamento.getVersionLancamento() != null && adiantamento.getVersionLancamento().equals("MODE01")) {
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
		// z.format(lancamentoAcao.getValor()) + " \n");
		// }

		// Fornecedor fornecedor =
		// pagamentoService.getFornecedorById(pagamento.getFornecedor().getId());

		ContaBancaria contaBanc = adiantamentoService.getContaById(adiantamento.getContaRecebedor().getId());

		Fornecedor fornecedor = new Fornecedor();
		if (contaBanc.getFornecedor() != null) {
			fornecedor = adiantamentoService.getFornecedorById(contaBanc.getFornecedor().getId());
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		String logo = path + "resources/image/logoFas.gif";

		Map parametros = new HashMap();
		parametros.put("logo", logo);
		parametros.put("tipo_gestao", adiantamento.getTipoGestao().getNome());
		parametros.put("gestao", adiantamento.getGestao().getNome());
		parametros.put("tipo_localidade", adiantamento.getTipoLocalidade().getNome());
		parametros.put("localidade", adiantamento.getLocalidade().getNome());
		parametros.put("usuario",
				usuarioSessao.getNomeUsuario() + " " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
		parametros.put("solicitacao", adiantamento.getId().toString());
		parametros.put("observ", "");
		parametros.put("acao", acs.toString());

		parametros.put("fornecedor", contaBanc.getNomeConta() != null ? contaBanc.getNomeConta() : "");
		parametros.put("banco", contaBanc.getNomeBanco() != null ? contaBanc.getNomeBanco() : "");
		parametros.put("agencia", contaBanc.getNumeroAgencia() != null ? contaBanc.getNumeroAgencia() : "");
		parametros.put("conta", contaBanc.getNumeroConta() != null ? contaBanc.getNumeroConta() : "");
		parametros.put("cnpj_fornecedor", contaBanc.getCnpj() != null ? contaBanc.getCnpj() : "");
		parametros.put("cpf", contaBanc.getCpf() != null ? contaBanc.getCpf() : "");

		if (contaBanc.getFornecedor() != null) {
			parametros.put("contato", fornecedor.getContato() != null ? fornecedor.getContato() : "");
			parametros.put("fone", fornecedor.getTelefone() != null ? fornecedor.getTelefone() : "");
		} else {
			parametros.put("contato", "");
			parametros.put("fone", "");
		}

		parametros.put("data_pagamento", sdf.format(adiantamento.getDataPagamento()));
		parametros.put("pis", contaBanc.getPIS());

		parametros.put("sp", adiantamento.getId().toString());
		parametros.put("data_emissao", sdf.format(adiantamento.getDataEmissao()));
		parametros.put("descricao", adiantamento.getDescricao());

		parametros.put("data_solic", sdf.format(adiantamento.getDataEmissao()));
		// parametros.put("data_aprovacao",
		// sdf.format(pagamento.getDataAprovacao()));
		parametros.put("data_sup", "");
		parametros.put("solicitante", adiantamento.getSolicitante().getNome());

		parametros.put("usuario", adiantamento.getSolicitante().getNome());
		parametros.put("nf", adiantamento.getNotaFiscal());
		parametros.put("valor_total_c_desconto", z.format(adiantamento.getValorTotalComDesconto()));
		parametros.put("data_vencto", sdf.format(adiantamento.getDataPagamento()));
		parametros.put("modo_pagamento", adiantamento.getCondicaoPagamentoEnum().getNome());

		Util util = new Util();

		parametros.put("data_emissao", new SimpleDateFormat("dd/MM/yyyy hh:mm").format(adiantamento.getDataEmissao()));

		if (adiantamento.getTipoGestao().getNome().equals("Regional")) {
			// setarParamRegional(parametros);
			util.setarParamRegional(parametros, adiantamento);
		}

		if (adiantamento.getTipoGestao().getNome().equals("Coordenadoria")) {
			// setarParamCoordenadoria(parametros);
			util.setarParamCoordenadoria(parametros, adiantamento);
		}

		if (adiantamento.getTipoGestao().getNome().equals("Superintendencia")) {
			// setarParamSuperintendencia(parametros);
			util.setarParamSuperintendencia(parametros, adiantamento);
		}

		if (adiantamento.getTipoLocalidade().getNome().equals("Uc")) {
			// setarDestinoUC(parametros);
			util.setarDestinoUC(parametros, adiantamento, compraService);
		} else if (adiantamento.getTipoLocalidade().getNome().equals("Comunidade")) {
			// setarDestinoComunidade(parametros);
			util.setarDestinoComunidade(parametros, adiantamento);
		} else {
			// setarDestino(parametros);
			util.setarDestino(parametros, adiantamento);
		}

		JRDataSource dataSource = new JRBeanCollectionDataSource(new ArrayList<>());
		;

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/sa.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReport("AD", "" + adiantamento.getId().toString(), report, parametros, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onEstadoChange() {
		localidades = adiantamentoService.getMunicipioByEstado(idEstado);
		idEstado = new Long(0);

	}

	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuFinanceiro();
	}

	public SolicitacaoPagamento getPagamento() {
		return adiantamento;
	}

	public void setPagamento(SolicitacaoPagamento pagamento) {
		this.adiantamento = pagamento;
	}

	public UsuarioSessao getUsuarioSessao() {
		return usuarioSessao;
	}

	public void setUsuarioSessao(UsuarioSessao usuarioSessao) {
		this.usuarioSessao = usuarioSessao;
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

	public TipoGestao[] getTiposGestao() {
		return TipoGestao.values();
	}

	public TipoDeDocumentoFiscal[] getTipoDocumentoFiscal() {
		return TipoDeDocumentoFiscal.values();
	}

	public TipoLocalidade[] getTipoLocais() {
		return TipoLocalidade.values();
	}

	public Projeto getProjetoAux() {
		return projetoAux;
	}

	public void setProjetoAux(Projeto projetoAux) {
		this.projetoAux = projetoAux;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public Long getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Long idEstado) {
		this.idEstado = idEstado;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getTipoGestao() {
		return tipoGestao;
	}

	public void setTipoGestao(String tipoGestao) {
		this.tipoGestao = tipoGestao;
	}

	public Boolean getPanelListagem() {
		return panelListagem;
	}

	public void setPanelListagem(Boolean panelListagem) {
		this.panelListagem = panelListagem;
	}

	public Boolean getPanelCadastro() {
		return panelCadastro;
	}

	public void setPanelCadastro(Boolean panelCadastro) {
		this.panelCadastro = panelCadastro;
	}

	public List<Gestao> getGestoes() {
		return gestoes;
	}

	public void setGestoes(List<Gestao> gestoes) {
		this.gestoes = gestoes;
	}

	public List<FontePagadora> getAllFontes() {
		return allFontes;
	}

	public void setAllFontes(List<FontePagadora> allFontes) {
		this.allFontes = allFontes;
	}

	public List<Acao> getAllAcoes() {
		return allAcoes;
	}

	public void setAllAcoes(List<Acao> allAcoes) {
		this.allAcoes = allAcoes;
	}

	public List<Localidade> getLocalidades() {
		return localidades;
	}

	public void setLocalidades(List<Localidade> localidades) {
		this.localidades = localidades;
	}

	public List<SolicitacaoPagamento> getPagamentosFiltered() {
		return pagamentosFiltered;
	}

	public void setPagamentosFiltered(List<SolicitacaoPagamento> pagamentosFiltered) {
		this.pagamentosFiltered = pagamentosFiltered;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public List<SolicitacaoPagamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<SolicitacaoPagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Boolean getInserirDescricao() {
		return inserirDescricao;
	}

	public void setInserirDescricao(Boolean inserirDescricao) {
		this.inserirDescricao = inserirDescricao;
	}

	public ArquivoLancamento getArqAuxiliar() {
		return arqAuxiliar;
	}

	public void setArqAuxiliar(ArquivoLancamento arqAuxiliar) {
		this.arqAuxiliar = arqAuxiliar;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Lancamento getLancamentoAux() {
		return lancamentoAux;
	}

	public void setLancamentoAux(Lancamento lancamentoAux) {
		this.lancamentoAux = lancamentoAux;
	}

	public ArquivoLancamento getArquivo() {
		return arquivo;
	}

	public void setArquivo(ArquivoLancamento arquivo) {
		this.arquivo = arquivo;
	}

	public List<CategoriaDespesaClass> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<CategoriaDespesaClass> categorias) {
		this.categorias = categorias;
	}

	public List<Rubrica> getRubricas() {
		return rubricas;
	}

	public void setRubricas(List<Rubrica> rubricas) {
		this.rubricas = rubricas;
	}

	public List<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}

	public List<OrcamentoProjeto> getOrcamentosProjetos() {
		return orcamentosProjetos;
	}

	public void setOrcamentosProjetos(List<OrcamentoProjeto> orcamentosProjetos) {
		this.orcamentosProjetos = orcamentosProjetos;
	}

	public Long getIdOrcamento() {
		return idOrcamento;
	}

	public void setIdOrcamento(Long idOrcamento) {
		this.idOrcamento = idOrcamento;
	}

	public List<ProjetoRubrica> getListaDeRubricasProjeto() {
		return listaDeRubricasProjeto;
	}

	public void setListaDeRubricasProjeto(List<ProjetoRubrica> listaDeRubricasProjeto) {
		this.listaDeRubricasProjeto = listaDeRubricasProjeto;
	}

	public List<CategoriaProjeto> getListCategoriaProjeto() {
		return listCategoriaProjeto;
	}

	public void setListCategoriaProjeto(List<CategoriaProjeto> listCategoriaProjeto) {
		this.listCategoriaProjeto = listCategoriaProjeto;
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

	public List<ContaBancaria> getContas() {
		return contas;
	}

	public void setContas(List<ContaBancaria> contas) {
		this.contas = contas;
	}

	public Integer getStepIndex() {
		return stepIndex;
	}

	public void setStepIndex(Integer stepIndex) {
		this.stepIndex = stepIndex;
	}
}
