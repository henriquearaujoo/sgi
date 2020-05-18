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
import model.AtividadeProjeto;
import model.CategoriaDespesaClass;
import model.CategoriaFinanceira;
import model.CategoriaProjeto;
import model.CategoriadeDespesa;
import model.ContaBancaria;
import model.DespesaReceita;
import model.FontePagadora;
import model.Fornecedor;
import model.Lancamento;
import model.LancamentoAcao;
import model.LancamentoAvulso;
import model.LancamentoDiversos;
import model.LogStatus;
import model.MenuLateral;
import model.PagamentoLancamento;
import model.Projeto;
import model.ProjetoRubrica;
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
import service.ProjetoService;
import service.SolicitacaoPagamentoService;
import util.ArquivoLancamento;
import util.DataUtil;
import util.DiretorioUtil;
import util.DownloadUtil;
import util.Filtro;
import util.MakeMenu;
import util.ReportUtil;
import util.UsuarioSessao;

@Named(value = "diversos_coontroller")
@ViewScoped
public class LancamentoDiversosController implements Serializable {

	private static final long serialVersionUID = 1L;

	private @Inject LancamentoDiversos lancamento;

	private @Inject LancamentoAcao lancamentoAcao;

	private @Inject PagamentoService service;

	private List<LancamentoDiversos> lancamentos = new ArrayList<>();

	private List<LancamentoDiversos> selectedLancamentos = new ArrayList<>();

	private List<ContaBancaria> allContas = new ArrayList<>();

	private List<ContaBancaria> allContaFornecedor = new ArrayList<>();

	private List<FontePagadora> allFontes = new ArrayList<>();

	private List<Acao> allAcoes = new ArrayList<Acao>();

	private @Inject UsuarioSessao usuarioSessao;

	public UsuarioSessao getUsuarioSessao() {
		return usuarioSessao;
	}

	public void setUsuarioSessao(UsuarioSessao usuarioSessao) {
		this.usuarioSessao = usuarioSessao;
	}

	private @Inject Fornecedor fornecedor;

	private @Inject ContaBancaria conta;
	
	

	public List<ProjetoRubrica> getListaDeRubricasProjeto() {
		return listaDeRubricasProjeto;
	}

	public void setListaDeRubricasProjeto(List<ProjetoRubrica> listaDeRubricasProjeto) {
		this.listaDeRubricasProjeto = listaDeRubricasProjeto;
	}

	private @Inject Aprouve aprouve;
	@Inject
	private CalculatorRubricaRepositorio calculatorRubricaRepositorio;

	private List<CategoriaFinanceira> listCategoriaFinanceira = new ArrayList<>();

	private Lancamento lancamentoAux = new Lancamento();

	private @Inject ArquivoLancamento arqAuxiliar;

	private List<CategoriaProjeto> listCategoriaProjeto = new ArrayList<>();

	private List<CategoriaFinanceira> categorias = new ArrayList<>();

	private ContaBancaria selectedConta = new ContaBancaria();

	private TabView tabview;

	private List<LancamentoDiversos> lancamentosDeAdiantamento = new ArrayList<>();

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
	
	private List<ProjetoRubrica> listaDeRubricasProjeto = new ArrayList<>();

	public LancamentoDiversosController() {

	}
	
	@Inject
	private ProjetoService projetoService;
	
	private Projeto projetoAux;
	
	public Projeto getProjetoAux() {
		return projetoAux;
	}

	public void setProjetoAux(Projeto projetoAux) {
		this.projetoAux = projetoAux;
	}
	
	private List<Projeto> listaProjeto ;
	
	public List<Projeto> getListaProjeto() {
		return listaProjeto;
	}

	public void setListaProjeto(List<Projeto> listaProjeto) {
		this.listaProjeto = listaProjeto;
	}
	
	
	public void getProjetoByUsuario(){
		//listaProjeto = projetoService.getProjetosByUsuario(usuarioSessao.getUsuario());
		
		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro")) {
			listaProjeto = projetoService.getProjetosbyUsuarioProjeto(null, filtro);	
			return;
		}

		listaProjeto = projetoService.getProjetosbyUsuarioProjeto(usuarioSessao.getUsuario(), filtro);
	}
	
	
	
	public void listaRubricas() {
		
		if(projetoAux == null) {
			lancamentoAcao = new LancamentoAcao();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você deve selecionar um projeto antes de buscar rubrica! ");
			FacesContext.getCurrentInstance().addMessage(null, message);
			listaDeRubricasProjeto = new ArrayList<ProjetoRubrica>() ;
		}else {
			listaDeRubricasProjeto = calculatorRubricaRepositorio.getProjetoRubricaByProjeto(projetoAux.getId());
		}
	}
	
	public boolean poderEditar() {
		if (lancamento.getId() != null) {
			if (lancamento.getStatusCompra().getNome().equals("Não iniciado/Solicitado")||lancamento.getStatusCompra().getNome().equals("Em cotação")|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("compra")|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")) {
					return true;
				} else {
					Long idUser = usuarioSessao.getUsuario().getColaborador().getId().longValue();
					Long idSolicintante = lancamento.getSolicitante().getId().longValue();

					Long idGestor = lancamento.getGestao().getColaborador().getId();

					if ((idUser.longValue() == idSolicintante.longValue()||idUser.longValue() == idGestor.longValue())  && !lancamento.getStatusCompra().getNome().equals("Concluído")) {
						return true;
					} else {
						return false;
					}

				}

			} else {
				return true;
			}
	}
	
	public void listaRubricasAndAtividades() {

		if (projetoAux == null) {
			lancamentoAcao = new LancamentoAcao();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você deve selecionar um projeto antes de buscar rubrica! ");
			FacesContext.getCurrentInstance().addMessage(null, message);
			listaDeRubricasProjeto = new ArrayList<ProjetoRubrica>();
			setListaAtividades(new ArrayList<AtividadeProjeto>());
		} else {
			listaDeRubricasProjeto = calculatorRubricaRepositorio.getProjetoRubricaByProjeto(projetoAux.getId());
//			findAtividadesByProjetoWithSaldo();
		}
	}
	
	public void findAtividadesByProjetoWithSaldo() {
		setListaAtividades(projetoService.getAtividadeWithSaldoByProjeto(projetoAux.getId()));
	}
	
	private List<AtividadeProjeto> listaAtividades;
	
	public List<AtividadeProjeto> getListaAtividades() {
		return listaAtividades;
	}

	public void setListaAtividades(List<AtividadeProjeto> listaAtividades) {
		this.listaAtividades = listaAtividades;
	}
	
	
	public void initCadastro() {
		carregarCategoriasFin();
		getProjetoByUsuario();
	}
	
	private List<Projeto> listProject = new ArrayList<>();

	public List<Projeto> completeProjetos(String query) {
		listProject = new ArrayList<Projeto>();
		listProject = service.getProjetoAutoCompleteMODE01(query);
		return listProject;
	}

	public void init() {
		filtro = new Filtro();
		filtro.setIdConta(new Long(0));
		filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		filtro.setDataFinal(DataUtil.getDataFinal(new Date()));
		carregarContas();
		carregarLancamentos();
		carregarCategoriasFin();
		// carregarCategorias();
	}

	public void carregarCategoriasFin() {
		listCategoriaFinanceira = service.buscarCategoriasFin();
	}

	private @Inject SolicitacaoPagamentoService pagamentoService;

	public void adicionarNovaAcao() {

		Boolean verificaSaldo = true;

		if (lancamento.getCategoriaFinanceira() != null) {
			if (lancamento.getCategoriaFinanceira().getId().intValue() == 16
					|| lancamento.getCategoriaFinanceira().getId().intValue() == 60
					|| lancamento.getCategoriaFinanceira().getId().intValue() == 59
					|| lancamento.getTipoLancamento().equals("reemb_conta")) {
				verificaSaldo = false;
			}
		}

		if (verificaSaldo)
			if (!calculatorRubricaRepositorio.verificarSaldoRubrica(lancamentoAcao.getProjetoRubrica().getId(), lancamentoAcao.getValor())) {
				addMessage("", "Rubrica sem saldo suficiente", FacesMessage.SEVERITY_WARN);
				return;
			}

		if (lancamento.getValorTotalComDesconto() == null) {
			lancamento.setValorTotalComDesconto(BigDecimal.ZERO);
		}

		lancamentoAcao.setDespesaReceita(DespesaReceita.DESPESA);

		// lancamentoAcao.setProjeto(lancamentoAcao.getProjetoRubrica().getProjeto());
		// lancamentoAcao.setOrcamento(lancamentoAcao.getRubricaOrcamento().getOrcamento());

		lancamento.setValorTotalComDesconto(lancamento.getValorTotalComDesconto().add(lancamentoAcao.getValor()));
		lancamentoAcao.setLancamento(lancamento);
		lancamento.getLancamentosAcoes().add(lancamentoAcao);
		lancamentoAcao = new LancamentoAcao();
	}

	public void removerAcao(int index) {
		BigDecimal valor = lancamento.getLancamentosAcoes().get(index).getValor();
		LancamentoAcao la = lancamento.getLancamentosAcoes().get(index);

		lancamento.setValorTotalComDesconto(lancamento.getValorTotalComDesconto().subtract(valor));
		lancamento.getLancamentosAcoes().remove(index);
		lancamentoAcao = new LancamentoAcao();

	}
	
	
	public void removerAcao(LancamentoAcao lancamentoAcao) {
		BigDecimal valor = lancamentoAcao.getValor();
		lancamento.setValorTotalComDesconto(lancamento
				.getValorTotalComDesconto().subtract(valor));
		lancamento.getLancamentosAcoes().remove(lancamentoAcao);
		if(lancamento.getId()!=null && lancamentoAcao.getId() != null) {
			service.removerLancamentoAcao(lancamentoAcao);
			if(projetoAux != null && projetoAux.getId() != null) {
				listaRubricasAndAtividades();
			}
			service.salvarLancamentoDiversos(lancamento, usuarioSessao.getUsuario());
		}

	}
	

	public void carregarContas() {
		contasFiltro = service.getContasFilter();
	}

	// public void carregarCategorias() {
	// categorias = service.buscarCategorias();
	// }

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

	public List<ProjetoRubrica> completeCategoria(String s) {
		if (s.length() > 2)
			return calculatorRubricaRepositorio.getProjetoRubricaByStrNormalizado(s);
		else
			return new ArrayList<>();

		// return pagamentoService.completeRubricasDeProjetoJOIN(s);
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

	public void filtrar() {
		carregarLancamentos();
	}
	//metodo Adicionado dia 30/08/2018 by Christophe para Limpar dados dos filtros em lancamentos Diversos
	public void limpar() {
		filtro = new Filtro();
		filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		filtro.setDataFinal(DataUtil.getDataFinal(new Date()));
	}

	public void carregarLancamentos() {
		lancamentos = service.getListaDiversos(filtro);
		// closeDialogLoading();
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
		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro")) {
			lancamentos.remove(index);
			lancamento = service.getLancamentoDiversoById(lancamentoAux.getId());
			service.removerLancamento(lancamento);
			lancamento = new LancamentoDiversos();
		} else {
			showMessageLiberarExclusao();
			lancamento = new LancamentoDiversos();
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
			lancamento = service.getLancamentoDiversoById(lancamentoAux.getId());
			lancamento.setLancamentosAcoes(service.getLancamentosAcao(lancamento));
			lancamento.setArquivos(new ArrayList<>());
		} else {
			showMessageLiberarExclusao();
			lancamento = new LancamentoDiversos();
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

	public String salvarLancamento() {

		if (lancamento.getCategoriaFinanceira() != null) {
			if (lancamento.getCategoriaFinanceira().getId().intValue() == 16
					|| lancamento.getCategoriaFinanceira().getId().intValue() == 60
					|| lancamento.getCategoriaFinanceira().getId().intValue() == 59) {
				lancamento.setDepesaReceita(DespesaReceita.RECEITA);
			} else {
				lancamento.setDepesaReceita(DespesaReceita.DESPESA);
			}
		} else {
			lancamento.setDepesaReceita(DespesaReceita.DESPESA);
		}
		

		if (lancamento.getLancamentosAcoes().size() < 1) {
			addMessage("",
					"Você deve adicionar ao menos 1 classe/ação ao lançamento para que o armazenamento seja concluído.",
					FacesMessage.SEVERITY_WARN);
			closeDialogLoading();
			return "";
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
					return "";
				}
			}

		}

		lancamento.setVersionLancamento("MODE01");
		// lancamento.setDepesaReceita(DespesaReceita.DESPESA);
		lancamento.setDataPagamento(ajusteDataDePagamento(lancamento.getDataPagamento()));
		lancamento.setDataEmissao(ajusteDataDePagamento(lancamento.getDataEmissao()));
		lancamento.setStatusCompra(StatusCompra.CONCLUIDO);
		service.salvarLancamentoDiversos(lancamento, usuarioSessao.getUsuario());
		carregarLancamentos();
		lancamento = new LancamentoDiversos();
		return "lancamentos_diversos?faces-redirect=true";

	}

	public TipoDeDocumentoFiscal[] getTipoDocumentoFiscal() {
		return TipoDeDocumentoFiscal.values();
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
		service.salvarLancamentoDiversosSemRefatorar(lancamento, usuarioSessao.getUsuario());
		carregarLancamentos();
		lancamento = new LancamentoDiversos();
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

	public LancamentoDiversos getLancamento() {
		return lancamento;
	}

	public void setLancamento(LancamentoDiversos lancamento) {
		this.lancamento = lancamento;
	}

	public List<LancamentoDiversos> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<LancamentoDiversos> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public List<LancamentoDiversos> getSelectedLancamentos() {
		return selectedLancamentos;
	}

	public void setSelectedLancamentos(List<LancamentoDiversos> selectedLancamentos) {
		this.selectedLancamentos = selectedLancamentos;
	}

	public List<LancamentoDiversos> getLancamentosDeAdiantamento() {
		return lancamentosDeAdiantamento;
	}

	public void setLancamentosDeAdiantamento(List<LancamentoDiversos> lancamentosDeAdiantamento) {
		this.lancamentosDeAdiantamento = lancamentosDeAdiantamento;
	}

	public List<CategoriaFinanceira> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<CategoriaFinanceira> categorias) {
		this.categorias = categorias;
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
	
	public String mudarStatusLancamento() {
		if(usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")) {
			LogStatus log = new LogStatus(new Date());
			log.setLancamento(lancamentoAux);
			log.setStatusLog(lancamentoAux.getStatusCompra());
			log.setUsuario(usuarioSessao.getUsuario());
			
			service.mudarStatusLancamento(lancamentoAux.getId(), lancamentoAux.getStatusCompra(), log);
		}
		return "";
	}

}
