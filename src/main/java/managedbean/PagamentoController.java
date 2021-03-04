
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

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.weld.context.RequestContext;
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
import model.LancamentoAuxiliar;
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

@Named(value = "pagamentoController")
@ViewScoped
public class PagamentoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private @Inject PagamentoService service;

	private @Inject SolicitacaoPagamentoService pagamentoService;
	private @Inject SolicitacaoPagamento pagamento;
	@Inject
	private UsuarioSessao usuarioSessao;
	@Inject
	private LancamentoAcao lancamentoAcao;

	@Inject
	private ProjetoService projetoService;

	@Inject
	private CalculatorRubricaRepositorio calculatorRubricaRepositorio;

	private @Inject ArquivoLancamento arqAuxiliar;

	@Inject
	private Fornecedor fornecedor;

	private Boolean exigeNF;

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

	private Projeto projetoAux;

	private Integer stepIndex = 0;

	public List<MenuLateral> getUtilMenu() {
		return utilMenu;
	}

	public void setUtilMenu(List<MenuLateral> utilMenu) {
		this.utilMenu = utilMenu;
	}

	public void steping(String modo) {
		if (modo.equalsIgnoreCase("next")) {
			if (stepIndex == 3) {
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

	public CategoriadeDespesa[] categoriasDespesas() {
		return CategoriadeDespesa.values();
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

	private UploadedFile file;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	private List<Projeto> listaProjeto;

	public List<Projeto> getListaProjeto() {
		return listaProjeto;
	}

	public void setListaProjeto(List<Projeto> listaProjeto) {
		this.listaProjeto = listaProjeto;
	}

	public List<Projeto> carregarProjetos() {
		// listaProjeto =
		// projetoService.getProjetosByUsuario(usuarioSessao.getUsuario());

		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro")) {
			projetos = projetoService.getProjetosbyUsuarioProjeto(null, filtro);
		}

		filtro.setVerificVigenciaMenosDias(true);
		projetos = projetoService.getProjetosbyUsuarioProjeto(usuarioSessao.getUsuario(), filtro);
		
		return projetos;
	}

	public void listaRubricas() {

		if (projetoAux == null) {
			lancamentoAcao = new LancamentoAcao();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você deve selecionar um projeto antes de buscar rubrica! ");
			// RequestContext.getCurrentInstance().showMessageInDialog(message);
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
			// RequestContext.getCurrentInstance().showMessageInDialog(message);
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
		filtro.setSp("sp");
		carregarPagamentos();
		carregarCategoriasFin();
		verificarExigencia();
		carregarProjetos();

		// getProjetoByUsuario();
		// carregarProjetos();
		// exigeNF = true;
		// carregarCategorias();
	}

	public boolean poderEditar() {
		if (pagamento.getId() != null) {
			if (pagamento.getStatusCompra().getNome().equals("Não iniciado/Solicitado")
					|| pagamento.getStatusCompra().getNome().equals("Em cotação")
					|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("compra")
					|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")) {
				return true;
			} else {
				Long idUser = usuarioSessao.getUsuario().getColaborador().getId().longValue();
				Long idSolicintante = pagamento.getSolicitante().getId().longValue();

				Long idGestor = pagamento.getGestao().getColaborador().getId();

				if ((idUser.longValue() == idSolicintante.longValue() || idUser.longValue() == idGestor.longValue())
						&& !pagamento.getStatusCompra().getNome().equals("Concluído")) {
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
	public List<model.CategoriaFinanceira> carregarCategoriasFin() {
		listCategoriaFinanceira = pagamentoService.buscarCategoriasFin();
		return listCategoriaFinanceira;
	}

	public List<ProjetoRubrica> completeCategoria(String s) {

		if (s.length() > 2) {
			return calculatorRubricaRepositorio.getProjetoRubricaByStrNormalizado(s);

		} else {
			return new ArrayList<>();
		}

		// return pagamentoService.completeRubricasDeProjetoJOIN(s);
	}

	// public void carregarProjetos() {
	// projetos = compraService.getProjetosFiltroPorUsuarioMODE01(filtro,
	// usuarioSessao.getUsuario());
	// }

	public void carregarPagamentos() {
		pagamentos = pagamentoService.getSolicitacaoPagamentos(filtro);

	}

	@Inject
	private ProjetoService gestaoProjeto;

	public List<Gestao> completeGestao(String query) {
		return gestaoProjeto.getGestaoAutoComplete(query);
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

	public Boolean verificarExigencia() {
		exigeNF = pagamentoService.verificarExigencia(pagamento);
		return exigeNF;
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
		String type = pagamento.getGestao().getType();
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

		String type = pagamento.getLocalidade().getType();
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

	public List<Rubrica> carregarRubricas() {
		rubricas = rubricaRepositorio.getRubricas();
		return rubricas;
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
		BigDecimal valor = pagamento.getLancamentosAcoes().get(index).getValor();
		LancamentoAcao la = pagamento.getLancamentosAcoes().get(index);

		pagamento.setValorTotalComDesconto(pagamento.getValorTotalComDesconto().subtract(valor));
		pagamento.getLancamentosAcoes().remove(index);
		lancamentoAcao = new LancamentoAcao();

	}

	public void removerAcao(LancamentoAcao lancamentoAcao) {
		BigDecimal valor = lancamentoAcao.getValor();
		pagamento.setValorTotalComDesconto(pagamento.getValorTotalComDesconto().subtract(valor));
		pagamento.getLancamentosAcoes().remove(lancamentoAcao);
		if (pagamento.getId() != null && lancamentoAcao.getId() != null) {
			if (service.removerLancamentoAcao(lancamentoAcao)) {
				if (projetoAux != null && projetoAux.getId() != null) {
					listaRubricasAndAtividades();
				}
				// pagamentoService.salvar(pagamento, usuarioSessao.getUsuario());

			} else {
				addMessage("", "Não Foi Possível Excluir!", FacesMessage.SEVERITY_WARN);
			}
		}

	}

	public String aprovar() {
		if (verificarPrivilegioAprovacao()) {
			LogStatus log = new LogStatus();
			log.setUsuario(usuarioSessao.getUsuario());
			log.setLancamento(pagamento);
			log.setData(new Date());
			log.setStatusLog(StatusCompra.CONCLUIDO);
			log.setSigla(pagamento.getGestao().getSigla());
			log.setSiglaPrivilegio("ASP");
			pagamentoService.mudarStatus(pagamento.getId(), StatusCompra.CONCLUIDO, log);
			return "pagamento?faces-redirect=true";
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você não tem privilégios para esse tipo de ação, por favor contate o administrador do sistema!");
			// RequestContext.getCurrentInstance().showMessageInDialog(message);
			return "";
		}

	}

	public String suspender() {
		if (verificarPrivilegioSuspensao()) {
			LogStatus log = new LogStatus();
			log.setUsuario(usuarioSessao.getUsuario());
			log.setLancamento(pagamento);
			log.setData(new Date());
			log.setStatusLog(StatusCompra.CANCELADO);
			log.setSiglaPrivilegio("SSP");
			log.setSigla(pagamento.getGestao().getSigla());
			pagamentoService.mudarStatus(pagamento.getId(), StatusCompra.CANCELADO, log);
			pagamento = pagamentoService.getPagamentoById(pagamento.getId());
			return "pagamento?faces-redirect=true";
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você não tem privilégios para esse tipo de ação, por favor contate o administrador do sistema!");

			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, message);
			// RequestContext.getCurrentInstance().showMessageInDialog(message);
			return "";
		}

	}

	public List<Projeto> completeProjetos(String query) {
		projetos = new ArrayList<Projeto>();
		if (query != null && !(query.isEmpty())) {
			projetos = service.getProjetoAutoCompleteMODE01(query);
		}
		return projetos;
	}

	public boolean verificarPrivilegioSuspensao() {
		Aprouve ap = new Aprouve();
		ap.setSigla("SSP");
		ap.setUsuario(usuarioSessao.getUsuario());
		return pagamentoService.findAprouve(ap);
	}

	public boolean verificarPrivilegioAprovacao() {
		Aprouve ap = new Aprouve();
		ap.setSigla("ASP");
		ap.setUsuario(usuarioSessao.getUsuario());
		return pagamentoService.findAprouve(ap);
	}

	public void adicionarNovaAcao() {

		if (verificaCamposRecurso()) {
			return;
		}
		/*
		 * Alterado para comportar o novo calculo em 20/09/2018 by Christophe before if
		 * (!pagamentoService.verificarSaldoRubrica(lancamentoAcao,
		 * lancamentoAcao.getValor())) { addMessage("", "Saldo de rubrica insuficiente",
		 * FacesMessage.SEVERITY_WARN); return; }
		 */
		// after
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

		// end
		for (LancamentoAcao la : pagamento.getLancamentosAcoes()) {
			if (la.getProjetoRubrica().equals(lancamentoAcao.getProjetoRubrica())) {
				addMessage("", "Não é Permitido Adição de Duas Linhas Orçamentárias Iguais.",
						FacesMessage.SEVERITY_WARN);
				lancamentoAcao = new LancamentoAcao();
				return;
			}
		}
		lancamentoAcao.setDespesaReceita(DespesaReceita.DESPESA);
		// lancamentoAcao.setProjeto(lancamentoAcao.getProjetoRubrica().getProjeto());
		// lancamentoAcao.setOrcamento(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento());
		pagamento.setValorTotalComDesconto(pagamento.getValorTotalComDesconto().add(lancamentoAcao.getValor()));
		lancamentoAcao.setLancamento(pagamento);
		pagamento.getLancamentosAcoes().add(lancamentoAcao);

		lancamentoAcao = new LancamentoAcao();
	}

	public Boolean verificarSaldo() {

		Boolean retorno = false;

		// Gerar código para calculo de saldo;

		// Buscar código extrato e tentar melhorar script

		return retorno;
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

		arquivo.setLancamento(pagamento);

		if (pagamento.getArquivos() == null) {
			pagamento.setArquivos(new ArrayList<>());
		}

		pagamento.getArquivos().add(arquivo);

		// service.salvarArquivo(arquivo);

		path = "";

		arquivo = new ArquivoLancamento();
		// carregarArquivos(compra.getId());
	}

	public void carregarArquivos() {
		lancamentoAux = pagamentoService.getLancamentoById(lancamentoAux.getId());
		lancamentoAux.setArquivos(new ArrayList<ArquivoLancamento>());
		lancamentoAux.setArquivos(pagamentoService.getArquivoByLancamento(lancamentoAux.getId()));
	}

	public void carregarArquivos(Long id) {
		lancamentoAux = pagamentoService.getLancamentoById(id);
		lancamentoAux.setArquivos(new ArrayList<ArquivoLancamento>());
		lancamentoAux.setArquivos(pagamentoService.getArquivoByLancamento(lancamentoAux.getId()));
	}

	public void removerArquivo(ArquivoLancamento arqAuxiliar) {
		if (verificarPrivilegioENF()) {
			pagamentoService.removerArquivo(arqAuxiliar);
			carregarArquivos(arqAuxiliar.getLancamento().getId());
			this.arqAuxiliar = new ArquivoLancamento();
		} else {
			addMessage("", "Você não tem permissão para essa ação, entre em contato com o administrador do sistema.",
					FacesMessage.SEVERITY_WARN);
		}
	}

	public void removerArquivo() {
		pagamento.getArquivos().remove(arqAuxiliar);
	}

	private @Inject Aprouve aprouve;

	public Boolean verificarPrivilegioENF() {
		aprouve.setSigla("ENF");
		aprouve.setUsuario(usuarioSessao.getUsuario());
		return pagamentoService.verificarPrivilegioENF(aprouve);
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
		pagamento = new SolicitacaoPagamento();
		pagamento.setCategoriaDespesaClass(new CategoriaDespesaClass());
	}

	public void onTipoGestaoChange() {
		tipoGestao = "";
		if (pagamento.getTipoGestao() != null) {
			tipoGestao = pagamento.getTipoGestao().getNome();
			GestaoRepositorio repo = CDILocator.getBean(GestaoRepositorio.class);
			gestoes = new ArrayList<>();
			gestoes = pagamento.getTipoGestao().getGestao(repo);
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
		allFontes = pagamentoService.fontesAutoComplete(query);
		return allFontes;
	}

	public List<Fornecedor> completeFornecedor(String query) {
		fornecedores = new ArrayList<Fornecedor>();
		fornecedores = pagamentoService.getFornecedores(query);
		return fornecedores;
	}

	private List<ContaBancaria> contas = new ArrayList<>();

	public List<ContaBancaria> completeContaAdiantamentoEFornecedor(String query) {
		contas = new ArrayList<>();
		contas = pagamentoService.getContasFornecedoresAtivos(query);
		return contas;
	}

	public List<ContaBancaria> contasFornecedor() {
		contas = new ArrayList<>();

		if (pagamento.getFornecedor() != null && pagamento.getFornecedor().getId() != null) {
			contas = pagamentoService.getContasFornecedor(pagamento.getFornecedor());
		}

		return contas;
	}

	public List<Acao> completeAcoes(String query) {
		if (query.length() > 1) {
			allAcoes = new ArrayList<Acao>();
			allAcoes = pagamentoService.acoesAutoComplete(query);
		}

		return allAcoes;
	}

	public void mudarPanelListar() {
		if (panelCadastro)
			panelCadastro = false;
		else
			panelCadastro = true;

		if (panelListagem)
			panelListagem = false;
		else
			panelListagem = true;
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
		categorias = pagamentoService.buscarCategoriasFinanceira();
	}

	public void initCadastro() {
		carregarProjetos();
		carregarRubricas();
		verificarExigencia();
		carregarCategoriasFin();
		if (pagamento.getId() != null) {
			contasFornecedor();
			PrimeFaces.current().executeScript("setarFocused('info-geral')");
			PrimeFaces.current().executeScript("setarFocused('panel-info-doc')");
		}
	}

	public void editarPagamento() {

		pagamentosFiltered = new ArrayList<>();

		panelCadastro = true;
		panelListagem = false;

		carregarProjetos();

		pagamento = pagamentoService.getPagamentoById(pagamento.getId());
		pagamento.setLancamentosAcoes(pagamentoService.getLancamentosAcoes(pagamento));
		tipoGestao = pagamento.getTipoGestao().getNome();

		local = pagamento.getTipoLocalidade().getNome();
		if (pagamento.getLocalidade() instanceof Municipio) {
			estados = pagamentoService.getEstados(new Filtro());
			idEstado = ((Municipio) pagamento.getLocalidade()).getEstado().getId();
			localidades = pagamentoService.getMunicipioByEstado(idEstado);
		} else {
			LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
			localidades = pagamento.getTipoLocalidade().getLocalidade(repo);

		}

		GestaoRepositorio repo = CDILocator.getBean(GestaoRepositorio.class);
		gestoes = new ArrayList<>();
		gestoes = pagamento.getTipoGestao().getGestao(repo);
		carregarRubricas();
		carregarCategorias();
		verificarExigencia();
		contasFornecedor();
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
		panelCadastro = false;
		panelListagem = true;
		pagamentos = pagamentoService.getSolicitacaoPagamentos(filtro);
		// limpar();
		return "pagamento?faces-redirect=true";

	}

	public void salvar(Integer type) throws NegocioException {
		pagamento.setTipov4("SP");
		pagamento = pagamentoService.salvar(pagamento, usuarioSessao.getUsuario(), type);

		executeScript("PF('dlg_salvar').hide();");

		if (type == 1) {
			executeScript("PF('dlg_email').show();");
		}
	}

	public void enviarEmailAutorizacaoSP() {
		pagamentoService.enviarEmailAutorizacaoSP(pagamento, 0);
		closeDialogLoading();
	}

	private void executeScript(String script) {
		PrimeFaces.current().executeScript(script);
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
		if (pagamento.getTipoLocalidade() != null) {
			local = pagamento.getTipoLocalidade().getNome();
			if (local.equals("Municipio")) {
				estados = new ArrayList<>();
				estados = pagamentoService.getEstados(new Filtro());
				localidades = new ArrayList<>();
				idEstado = new Long(0);
			} else {
				LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
				localidades = pagamento.getTipoLocalidade().getLocalidade(repo);
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
				estados = pagamentoService.getEstados(new Filtro());
				localidades = new ArrayList<>();
				idEstado = new Long(0);
			} else {
				LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
				localidades = filtro.getTipoLocalidade().getLocalidade(repo);
			}
		}
	}

	public void filtrar() {
		filtro.setSp("sp");

		if (filtro.getGestao() != null && filtro.getGestao().getId() != null) {
			filtro.setGestaoID(filtro.getGestao().getId());
		}

		if (filtro.getLocalidade() != null && filtro.getLocalidade().getId() != null) {
			filtro.setLocalidadeID(filtro.getLocalidade().getId());
		}
		pagamentos = pagamentoService.getSolicitacaoPagamentos(filtro);
		// filtro = new Filtro();
		// closeDialogLoading();

	}

	private void closeDialogLoading() {
		// RequestContext context = RequestContext.getCurrentInstance();
		// context.execute("PF('statusialog').hide();");
		// context.execute("PF('dlg_filtro').hide();");
	}

	public List<Localidade> completeLocalidade(String s) {
		if (s.length() > 2)
			return pagamentoService.buscaLocalidade(s);

		return new ArrayList<Localidade>();
	}

	private @Inject CompraService compraService;

	public void imprimir(Long id) {

		// List<ItemCompra> itens =
		// compraService.getItensTransientByCompra(compra);

		NumberFormat z = NumberFormat.getCurrencyInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		pagamento = pagamentoService.getPagamentoById(id);

		List<LancamentoAcao> acoes = pagamentoService.getLancamentosAcoes(pagamento);

		StringBuilder acs = new StringBuilder();
		if (pagamento.getVersionLancamento() != null && pagamento.getVersionLancamento().equals("MODE01")) {
			for (LancamentoAcao lancamentoAcao : acoes) {
				acs.append(lancamentoAcao.getProjetoRubrica().getComponente().getNome());
				acs.append("| ");
				acs.append(lancamentoAcao.getProjetoRubrica().getProjeto().getNome());
				acs.append("| ");
				acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getTitulo());
				acs.append("| ");
				acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getRubrica().getNome());
				acs.append("| ");
				// Alteração para Adicionar valor do rateio no relatorio 03/09/2018 by
				// christophe
				acs.append("R$: " + lancamentoAcao.getValor());
				// end
				acs.append(" \n ");
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

		ContaBancaria contaBanc = pagamentoService.getContaById(pagamento.getContaRecebedor().getId());

		Fornecedor fornecedor = new Fornecedor();
		if (contaBanc.getFornecedor() != null) {
			fornecedor = pagamentoService.getFornecedorById(contaBanc.getFornecedor().getId());
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		String logo = path + "resources/image/logoFas.gif";

		Map parametros = new HashMap();
		parametros.put("logo", logo);
		parametros.put("tipo_gestao", pagamento.getTipoGestao().getNome());
		parametros.put("gestao", pagamento.getGestao().getNome());
		parametros.put("tipo_localidade", pagamento.getTipoLocalidade().getNome());
		parametros.put("localidade", pagamento.getLocalidade().getNome());
		parametros.put("usuario",
				usuarioSessao.getNomeUsuario() + " " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
		parametros.put("solicitacao", pagamento.getId().toString());
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

		parametros.put("data_pagamento", sdf.format(pagamento.getDataPagamento()));
		parametros.put("pis", contaBanc.getPIS());

		parametros.put("sp", pagamento.getId().toString());
		parametros.put("data_emissao", sdf.format(pagamento.getDataEmissao()));
		parametros.put("descricao", pagamento.getDescricao());

		parametros.put("data_solic", sdf.format(pagamento.getDataEmissao()));
		// parametros.put("data_aprovacao",
		// sdf.format(pagamento.getDataAprovacao()));
		parametros.put("data_sup", "");
		parametros.put("solicitante", pagamento.getSolicitante().getNome());

		parametros.put("usuario", pagamento.getSolicitante().getNome());
		parametros.put("nf", pagamento.getNotaFiscal());
		parametros.put("valor_total_c_desconto", z.format(pagamento.getValorTotalComDesconto()));
		parametros.put("data_vencto", sdf.format(pagamento.getDataPagamento()));
		parametros.put("modo_pagamento", pagamento.getCondicaoPagamentoEnum().getNome());

		Util util = new Util();

		parametros.put("data_emissao", new SimpleDateFormat("dd/MM/yyyy hh:mm").format(pagamento.getDataEmissao()));

		if (pagamento.getTipoGestao().getNome().equals("Regional")) {
			// setarParamRegional(parametros);
			util.setarParamRegional(parametros, pagamento);
		}

		if (pagamento.getTipoGestao().getNome().equals("Coordenadoria")) {
			// setarParamCoordenadoria(parametros);
			util.setarParamCoordenadoria(parametros, pagamento);
		}

		if (pagamento.getTipoGestao().getNome().equals("Superintendencia")) {
			// setarParamSuperintendencia(parametros);
			util.setarParamSuperintendencia(parametros, pagamento);
		}

		if (pagamento.getTipoLocalidade().getNome().equals("Uc")) {
			// setarDestinoUC(parametros);
			util.setarDestinoUC(parametros, pagamento, compraService);
		} else if (pagamento.getTipoLocalidade().getNome().equals("Comunidade")) {
			// setarDestinoComunidade(parametros);
			util.setarDestinoComunidade(parametros, pagamento);
		} else {
			// setarDestino(parametros);
			util.setarDestino(parametros, pagamento);
		}

		JRDataSource dataSource = new JRBeanCollectionDataSource(new ArrayList<>());
		;

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/sp.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReport("SC", "" + pagamento.getId().toString(), report, parametros, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Deprecated
	public void imprimir() {

		// List<ItemCompra> itens =
		// compraService.getItensTransientByCompra(compra);

		NumberFormat z = NumberFormat.getCurrencyInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		pagamento = pagamentoService.getPagamentoById(pagamento.getId());

		List<LancamentoAcao> acoes = pagamentoService.getLancamentosAcoes(pagamento);

		StringBuilder acs = new StringBuilder();
		if (pagamento.getVersionLancamento() != null && pagamento.getVersionLancamento().equals("MODE01")) {
			for (LancamentoAcao lancamentoAcao : acoes) {
				acs.append(lancamentoAcao.getProjetoRubrica().getComponente().getNome());
				acs.append("| ");
				acs.append(lancamentoAcao.getProjetoRubrica().getProjeto().getNome());
				acs.append("| ");
				acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getTitulo());
				acs.append("| ");
				acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getRubrica().getNome());
				acs.append("| ");
				// Alteração para Adicionar valor do rateio no relatorio 03/09/2018 by
				// christophe
				acs.append("R$: " + lancamentoAcao.getValor());
				// end
				acs.append(" \n ");
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

		ContaBancaria contaBanc = pagamentoService.getContaById(pagamento.getContaRecebedor().getId());

		Fornecedor fornecedor = new Fornecedor();
		if (contaBanc.getFornecedor() != null) {
			fornecedor = pagamentoService.getFornecedorById(contaBanc.getFornecedor().getId());
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		String logo = path + "resources/image/logoFas.gif";

		Map parametros = new HashMap();
		parametros.put("logo", logo);
		parametros.put("tipo_gestao", pagamento.getTipoGestao().getNome());
		parametros.put("gestao", pagamento.getGestao().getNome());
		parametros.put("tipo_localidade", pagamento.getTipoLocalidade().getNome());
		parametros.put("localidade", pagamento.getLocalidade().getNome());
		parametros.put("usuario",
				usuarioSessao.getNomeUsuario() + " " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
		parametros.put("solicitacao", pagamento.getId().toString());
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

		parametros.put("data_pagamento", sdf.format(pagamento.getDataPagamento()));
		parametros.put("pis", contaBanc.getPIS());

		parametros.put("sp", pagamento.getId().toString());
		parametros.put("data_emissao", sdf.format(pagamento.getDataEmissao()));
		parametros.put("descricao", pagamento.getDescricao());

		parametros.put("data_solic", sdf.format(pagamento.getDataEmissao()));
		// parametros.put("data_aprovacao",
		// sdf.format(pagamento.getDataAprovacao()));
		parametros.put("data_sup", "");
		parametros.put("solicitante", pagamento.getSolicitante().getNome());

		parametros.put("usuario", pagamento.getSolicitante().getNome());
		parametros.put("nf", pagamento.getNotaFiscal());
		parametros.put("valor_total_c_desconto", z.format(pagamento.getValorTotalComDesconto()));
		parametros.put("data_vencto", sdf.format(pagamento.getDataPagamento()));
		parametros.put("modo_pagamento", pagamento.getCondicaoPagamentoEnum().getNome());

		Util util = new Util();

		parametros.put("data_emissao", new SimpleDateFormat("dd/MM/yyyy hh:mm").format(pagamento.getDataEmissao()));

		if (pagamento.getTipoGestao().getNome().equals("Regional")) {
			// setarParamRegional(parametros);
			util.setarParamRegional(parametros, pagamento);
		}

		if (pagamento.getTipoGestao().getNome().equals("Coordenadoria")) {
			// setarParamCoordenadoria(parametros);
			util.setarParamCoordenadoria(parametros, pagamento);
		}

		if (pagamento.getTipoGestao().getNome().equals("Superintendencia")) {
			// setarParamSuperintendencia(parametros);
			util.setarParamSuperintendencia(parametros, pagamento);
		}

		if (pagamento.getTipoLocalidade().getNome().equals("Uc")) {
			// setarDestinoUC(parametros);
			util.setarDestinoUC(parametros, pagamento, compraService);
		} else if (pagamento.getTipoLocalidade().getNome().equals("Comunidade")) {
			// setarDestinoComunidade(parametros);
			util.setarDestinoComunidade(parametros, pagamento);
		} else {
			// setarDestino(parametros);
			util.setarDestino(parametros, pagamento);
		}

		JRDataSource dataSource = new JRBeanCollectionDataSource(new ArrayList<>());
		;

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/sp.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReport("SC", "" + pagamento.getId().toString(), report, parametros, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onEstadoChange() {
		localidades = pagamentoService.getMunicipioByEstado(idEstado);
		idEstado = new Long(0);

	}

	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuFinanceiro();
	}

	public SolicitacaoPagamentoService getPagamentoService() {
		return pagamentoService;
	}

	public void setPagamentoService(SolicitacaoPagamentoService pagamentoService) {
		this.pagamentoService = pagamentoService;
	}

	public SolicitacaoPagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(SolicitacaoPagamento pagamento) {
		this.pagamento = pagamento;
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

	public Projeto getProjetoAux() {
		return projetoAux;
	}

	public void setProjetoAux(Projeto projetoAux) {
		this.projetoAux = projetoAux;
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

	public Boolean getExigeNF() {
		return exigeNF;
	}

	public void setExigeNF(Boolean exigeNF) {
		this.exigeNF = exigeNF;
	}

	public StatusCompra[] getStatus() {
		return StatusCompra.values();
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
