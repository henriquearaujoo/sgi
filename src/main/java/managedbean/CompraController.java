package managedbean;

/**

* @author Ítalo Almeida
* @version 1.0
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
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
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.mail.EmailException;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import model.Acao;
import model.Aprouve;
import model.AtividadeProjeto;
import model.CategoriaDespesaClass;
import model.CategoriaProjeto;
import model.Compra;
import model.Estado;
import model.FontePagadora;
import model.Gestao;
import model.ItemCompra;
import model.ItemPedido;
import model.Lancamento;
import model.LancamentoAcao;
import model.Localidade;
import model.LogStatus;
import model.MenuLateral;
import model.Municipio;
import model.OrcamentoProjeto;
import model.Pedido;
import model.Produto;
import model.Projeto;
import model.ProjetoRubrica;
import model.Rubrica;
import model.StatusCompra;
import model.TipoGestao;
import model.TipoLancamento;
import model.TipoLocalidade;
import model.UnidadeDeCompra;
import model.UnidadeMedida;
import model.User;
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
import service.FornecedorService;
import service.HomeService;
import service.PedidoService;
import service.ProjetoService;
import util.ArquivoLancamento;
import util.CDILocator;
import util.DataUtil;
import util.DiretorioUtil;
import util.DownloadUtil;
import util.Email;
import util.Filtro;
import util.MakeMenu;
import util.ReportUtil;
import util.UsuarioSessao;
import util.Util;

@Named(value = "compraController")
@ViewScoped
public class CompraController implements Serializable {

	private static final long serialVersionUID = 1L;

	private @Inject CompraService compraService;
	private @Inject Compra compra;
	private @Inject UsuarioSessao usuarioSessao;
	private @Inject LancamentoAcao lancamentoAcao;

	@Inject
	private CalculatorRubricaRepositorio calculatorRubricaRepositorio;
	private Filtro filtro = new Filtro();
	private Produto produto = new Produto();

	private Long idEstado;
	private Long idOrcamento;
	private String local;
	private String tipoGestao = "";
	private @Inject Aprouve aprouve;

	private StringBuilder localDeCompra = new StringBuilder();

	private Boolean panelListagem = true;
	private Boolean panelCadastro = false;

	private List<Gestao> gestoes;
	private List<Produto> allProdutos = new ArrayList<Produto>();

	private List<FontePagadora> allFontes = new ArrayList<FontePagadora>();
	private List<CategoriaDespesaClass> categorias = new ArrayList<>();
	private List<Acao> allAcoes = new ArrayList<Acao>();

	private List<Localidade> localidades = new ArrayList<Localidade>();
	private List<Municipio> municipiosDeCompra = new ArrayList<Municipio>();
	private List<Municipio> municipiosDeCompraSelected = new ArrayList<Municipio>();

	private List<Compra> comprasFiltered = new ArrayList<Compra>();
	private List<Estado> estados;
	private List<Compra> compras = new ArrayList<Compra>();
	private List<OrcamentoProjeto> orcamentosProjetos = new ArrayList<>();
	private List<ProjetoRubrica> projetoRubricas = new ArrayList<>();

	private List<ItemCompra> itensDetalhados = new ArrayList<>();

	private List<ItemPedido> itensPedidos = new ArrayList<ItemPedido>();

	private ItemCompra itemCompra = new ItemCompra();

	private List<MenuLateral> menus = new ArrayList<>();

	private List<Projeto> projetos = new ArrayList<>();

	private Email email = new Email();

	private @Inject Projeto projeto;

	private @Inject PedidoService pedidoService;

	@Inject
	private ProjetoService projetoService;

	@Inject
	private FornecedorService fornecedorService;

	private Integer stepIndex = 0;

	private List<CategoriaProjeto> listCategoriaProjeto = new ArrayList<>();

	private List<ProjetoRubrica> listaDeRubricasProjeto = new ArrayList<>();

	private List<Pedido> pedidos = new ArrayList<Pedido>();

	private List<Projeto> listaProjeto;

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

	public Boolean verificaAcessoComprasDetalhadas() {
		if (usuarioSessao.getUsuario().getPerfil().getId().equals(new Long("1"))
				|| usuarioSessao.getUsuario().getPerfil().getId().equals(new Long("5"))) {
			return true;
		} else {
			return false;
		}
	}

	// TODO: MÉTODO QUE DEVE SER CARREGADO EM PROJETOS
	public void carregarProjetos() {

		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro")) {
			projetos = projetoService.getProjetosbyUsuarioProjeto(null, filtro);
			return;
		}

		filtro.setVerificVigenciaMenosDias(true);
		projetos = projetoService.getProjetosbyUsuarioProjeto(usuarioSessao.getUsuario(), filtro);

	}

	private @Inject Pedido pedido;

	public void imprimirPedido() {

		List<ItemPedido> itens = pedidoService.getItensPedidosByPedido(pedido);

		List<LancamentoAcao> acoes = pedidoService.getLancamentosDoPedido(pedido);

		StringBuilder acs = new StringBuilder();

		if (pedido.getCompra().getVersionLancamento() != null
				|| pedido.getCompra().getVersionLancamento().equals("MODE01")) {
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
		// getFormatacao(lancamentoAcao.getValor()) + "\n");
		// }

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		String logo = path + "resources/image/logoFas.gif";
		String imgAprovacao = path + "resources/image/assinatura_aprovacao_lv.jpg";

		Map parametros = new HashMap();
		parametros.put("logo", logo);
		parametros.put("img_aprovacao", imgAprovacao);
		parametros.put("tipo_gestao", pedido.getTipoGestao().getNome());
		parametros.put("gestao", pedido.getGestao().getNome());
		parametros.put("tipo_localidade", pedido.getTipoLocalidade().getNome());
		parametros.put("localidade", pedido.getLocalidade().getNome());
		parametros.put("data_emissao", new SimpleDateFormat("dd/MM/yyyy").format(pedido.getDataEmissao()));
		parametros.put("fornecedor", pedido.getFornecedor().getNomeFantasia());
		parametros.put("contato", pedido.getFornecedor().getContato());
		parametros.put("banco", pedido.getFornecedor().getBanco());
		parametros.put("fone", pedido.getFornecedor().getTelefone());
		parametros.put("agencia", pedido.getFornecedor().getAgencia());
		parametros.put("conta", pedido.getFornecedor().getConta());
		parametros.put("data_entrega", new SimpleDateFormat("dd/MM/yyyy").format(pedido.getDataEntrega()));
		parametros.put("data_pagamento", new SimpleDateFormat("dd/MM/yyyy").format(pedido.getDataPagamento()));
		parametros.put("cnpj_fornecedor", pedido.getFornecedor().getCnpj());
		parametros.put("solicitante", pedido.getCompra().getSolicitante().getNome());
		parametros.put("condicoes", pedido.getCondicaoPagamentoEnum().getNome());

		parametros.put("subtotal", pedido.getValorTotalSemDesconto());
		parametros.put("desconto", pedido.getValorTotalSemDesconto().subtract(pedido.getValorTotalComDesconto()));
		parametros.put("total", pedido.getValorTotalComDesconto());

		parametros.put("usuario",
				usuarioSessao.getNomeUsuario() + " " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));

		String pedidoAux = pedido.getCompra().getId() + "/" + pedido.getId();
		parametros.put("pedido", pedido.getId());
		parametros.put("pedidoAux", pedidoAux);
		parametros.put("observ", pedido.getObservacao());

		parametros.put("acao", acs.toString());

		Util util = new Util();

		parametros.put("data_emissao", new SimpleDateFormat("dd/MM/yyyy hh:mm").format(pedido.getDataEmissao()));

		if (pedido.getTipoGestao().getNome().equals("Regional")) {
			// setarParamRegional(parametros);
			util.setarParamRegional(parametros, pedido);
		}

		if (pedido.getTipoGestao().getNome().equals("Coordenadoria")) {
			// setarParamCoordenadoria(parametros);
			util.setarParamCoordenadoria(parametros, pedido);
		}

		if (pedido.getTipoGestao().getNome().equals("Superintendencia")) {
			// setarParamSuperintendencia(parametros);
			util.setarParamSuperintendencia(parametros, pedido);
		}

		if (pedido.getTipoLocalidade().getNome().equals("Uc")) {
			// setarDestinoUC(parametros);
			util.setarDestinoUC(parametros, pedido, compraService);
		} else if (pedido.getTipoLocalidade().getNome().equals("Comunidade")) {
			// setarDestinoComunidade(parametros);
			util.setarDestinoComunidade(parametros, pedido);
		} else {
			// setarDestino(parametros);
			util.setarDestino(parametros, pedido);
		}

		JRDataSource dataSource = new JRBeanCollectionDataSource(itens, false);
		;

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/pedido_compra.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReport("Pedido", "PC" + pedido.getId().toString(), report, parametros, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
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
//		PrimeFaces.current().dialog().openDynamic("dialog/help/help_compra_new", options, null);
//	}
	private Lancamento lancamentoAux = new Lancamento();

	public void carregarArquivos() {
		lancamentoAux = pedidoService.getLancamentoById(lancamentoAux.getId());
		lancamentoAux.setArquivos(new ArrayList<ArquivoLancamento>());
		lancamentoAux.setArquivos(pedidoService.getArquivoByLancamento(lancamentoAux.getId()));
	}

	public void removerPedido() {

		// pedidoService.mudarStatusPedido(pedido, usuarioSessao.getUsuario());
		pedidoService.remover(pedido);
		pedidos = pedidoService.getPedidos(pedido.getCompra());
	}

	private ArquivoLancamento arquivo = new ArquivoLancamento();

	public ArquivoLancamento getArquivo() {
		return arquivo;
	}

	public void setArquivo(ArquivoLancamento arquivo) {
		this.arquivo = arquivo;
	}

	public Boolean verificarPrivilegioENF() {
		aprouve.setSigla("ENF");
		aprouve.setUsuario(usuarioSessao.getUsuario());
		// aprouve.setSigla(compra.getGestao().getSigla());
		return pedidoService.verificarPrivilegioENF(aprouve);
	}

	public void removerArquivo() {
		compra.getArquivos().remove(arqAuxiliar);
	}

	public List<Projeto> completeProjetos(String query) {
		projetos = new ArrayList<Projeto>();
		if (query != null && !(query.isEmpty())) {
			projetos = compraService.getProjetoAutoCompleteMODE01(query);
		}
		return projetos;
	}

	private @Inject ArquivoLancamento arqAuxiliar;

	public ArquivoLancamento getArqAuxiliar() {
		return arqAuxiliar;
	}

	public void setArqAuxiliar(ArquivoLancamento arqAuxiliar) {
		this.arqAuxiliar = arqAuxiliar;
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

	private String path = "";
	private UploadedFile file;

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		String pathFinal = DiretorioUtil.DIRECTORY_UPLOAD;		
		conteudo = event.getFile().getContents();
		arquivo.setNome(event.getFile().getFileName());
		arquivo.setPath(pathFinal + gerarCodigoArquivo() + "." + event.getFile().getFileName());
		arquivo.setData(new Date());
		arquivo.setUsuario(usuarioSessao.getUsuario());
		// adicionarArquivo();
	}

	public void preparaArquivo() {
		String pathFinal = DiretorioUtil.DIRECTORY_UPLOAD;
		arquivo.setConteudo(file.getContents());
		arquivo.setPath(pathFinal + lancamentoAux.getId() + "." + file.getFileName());
		path = file.getFileName();
		arquivo.setData(new Date());
		arquivo.setUsuario(usuarioSessao.getUsuario());
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

		arquivo.setLancamento(compra);
		
		if(compra.getArquivos() == null) { 
			compra.setArquivos(new ArrayList<>()); 
		}  
		
		compra.getArquivos().add(arquivo);
		
		//service.salvarArquivo(arquivo);

		path = "";

		arquivo = new ArquivoLancamento();
		//carregarArquivos(compra.getId());
	}

	public PedidoService getPedidoService() {
		return pedidoService;
	}

	public void setPedidoService(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	public ProjetoService getProjetoService() {
		return projetoService;
	}

	public void setProjetoService(ProjetoService projetoService) {
		this.projetoService = projetoService;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Lancamento getLancamentoAux() {
		return lancamentoAux;
	}

	public void setLancamentoAux(Lancamento lancamentoAux) {
		this.lancamentoAux = lancamentoAux;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public Map<String, String> getMapCategorias() {
		return mapCategorias;
	}

	public void setMapCategorias(Map<String, String> mapCategorias) {
		this.mapCategorias = mapCategorias;
	}

	public RubricaRepositorio getRubricaRepositorio() {
		return rubricaRepositorio;
	}

	public void setRubricaRepositorio(RubricaRepositorio rubricaRepositorio) {
		this.rubricaRepositorio = rubricaRepositorio;
	}

	public void setMenus(List<MenuLateral> menus) {
		this.menus = menus;
	}

	public void listaRubricas() {

		if (projetoAux == null) {
			lancamentoAcao = new LancamentoAcao();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você deve selecionar um projeto antes de buscar rubrica! ");
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
			listaDeRubricasProjeto = new ArrayList<ProjetoRubrica>();
			setListaAtividades(new ArrayList<AtividadeProjeto>());
		} else {
			listaDeRubricasProjeto = calculatorRubricaRepositorio.getProjetoRubricaByProjeto(projetoAux.getId());
			// findAtividadesByProjetoWithSaldo();
		}
	}

	public List<Gestao> completeGestao(String query) {
		return projetoService.getGestaoAutoComplete(query);
	}

	public List<Localidade> completeLocalidade(String s) {
		if (s.length() > 2)
			return fornecedorService.buscaLocalidade(s);

		return new ArrayList<Localidade>();
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
		
		filtro = new Filtro();
		filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		filtro.setDataFinal(DataUtil.getDataFinal(new Date()));
		carregarCompras();
		// carregarMunicipiosDeCompra();
		carregarProjetos();
		carregarCategorias();
		// getProjetoByUsuario();
		// carregarItensDetalhados();
		//filtro = new Filtro();

	}

	public void carregarMunicipiosDeCompra() {
		municipiosDeCompra = compraService.getMunicipiosDeCompra();
	}

	public void carregarMenus() {
		menus = MakeMenu.getMenuCompra();
	}

	public void carregarCategorias() {
		categorias = compraService.buscarCategoriasDeCompras();
	}

	// public void carregarProjetos() {
	// projetos = compraService.getProjetosFiltroPorUsuarioMODE01(filtro,
	// usuarioSessao.getUsuario());
	// }

	public List<ProjetoRubrica> completeCategoria(String s) {
		if (s.length() > 2)
			return calculatorRubricaRepositorio.getProjetoRubricaByStrNormalizado(s);
		else
			return new ArrayList<>();
	}

	public void carregarRubricasDeProjetoAndOrcamentoSelect() {
		listaDeRubricasProjeto = new ArrayList<>();
		if (lancamentoAcao.getProjetoRubrica().getProjeto().getId() != null)
			listaDeRubricasProjeto = compraService.getRubricasDeProjetoJOIN(
					lancamentoAcao.getProjetoRubrica().getProjeto().getId(),
					lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getId());
	}

	public void carregarRubricasDeProjetoAndOrcamento() {
		if (lancamentoAcao.getProjetoRubrica().getProjeto() == null)
			return;
		OrcamentoProjeto op = new OrcamentoProjeto();
		if (orcamentosProjetos.size() > 0) {
			op = orcamentosProjetos.get(0);
			listaDeRubricasProjeto = new ArrayList<>();
			if (lancamentoAcao.getProjetoRubrica().getProjeto().getId() != null) {
				listaDeRubricasProjeto = compraService.getRubricasDeProjetoJOIN(
						lancamentoAcao.getProjetoRubrica().getProjeto().getId(), op.getOrcamento().getId());
			}

		}
	}

	public void carregarRubricasDeProjeto() {
		listaDeRubricasProjeto = new ArrayList<>();
		if (lancamentoAcao.getProjetoRubrica().getProjeto().getId() != null)
			listaDeRubricasProjeto = compraService
					.getRubricasDeProjeto(lancamentoAcao.getProjetoRubrica().getProjeto().getId(), null);

		// TODO: VERIFICAR AO MUDAR ORCAMENTO DE PROJETO
	}

	public void carregarDetalhesDeRecurso() {

		if (lancamentoAcao.getProjetoRubrica().getProjeto() == null) {
			orcamentosProjetos = new ArrayList<>();
			listaDeRubricasProjeto = new ArrayList<>();
			return;
		}

		carregarOrcamentosProjeto();
		carregarRubricasDeProjetoAndOrcamento();
	}

	public boolean poderEditar() {
		if (compra.getId() != null) {
			if (compra.getStatusCompra() == null || compra.getStatusCompra().getNome().equals("Não iniciado/Solicitado")
					|| compra.getStatusCompra().getNome().equals("Em cotação")
					|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("compra")
					|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")) {

				return true;
			} else {
				Long idUser = usuarioSessao.getUsuario().getColaborador().getId().longValue();
				Long idSolicintante = compra.getSolicitante().getId().longValue();
				Long idGestor = compra.getGestao().getColaborador().getId();

				if ((idUser.longValue() == idSolicintante.longValue() || idUser.longValue() == idGestor.longValue())
						&& compra.getStatusCompra().getNome().equals("Não iniciado/Solicitado")) {
					return true;
				} else {
					return false;
				}

			}

		} else {
			return true;
		}
	}

	public User carregarUsuarioSessao() {
		User user = usuarioSessao.getUsuario(); // setar o valor da gestão de acordo com o perfil do usuário
		return user;
	}
	
	public void carregarCompras() {
		filtro.setGestaoID(carregarUsuarioSessao().getColaborador().getGestao().getId());
		compras = compraService.getCompras(filtro);
		itemCompra = new ItemCompra();
		itemCompra.setCompra(new Compra());
		itemCompra.setProduto(new Produto());
	}

	public void filtrarDetalhadas() {
		carregarItensDetalhados();
	}

	public void carregarItensDetalhados() {
		itensDetalhados = compraService.getItensCompra(filtro);
	}

	public StatusCompra[] listaStatusCompras() {
		return StatusCompra.values();
	}

	public CompraController() {
	}

	public void iniciarFiltro() {
		filtro = new Filtro();
		local = "";
		tipoGestao = "";
	}

	public void filtrarItens() {
		carregarItensPedido();
	}

	public void carregarItensPedido() {
		itensPedidos = compraService.getItensPedidos(filtro);
	}

	public String listarPedidos() {
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("compra" + usuarioSessao.getNomeUsuario(),
				compra);
		return "pedidos?faces-redirect=true";
	}

	public void buscarMunicipiosDeCompra() {
		localDeCompra = compraService.buscarMunicipiosDeCompra(municipiosDeCompraSelected, compra);
	}

	public void removerAcao(int index) {
		// compra.getLancamentosAcoes().remove(compra.getLancamentosAcoes().stream()
		// .filter(ac ->
		// ac.getAcao().getCodigo().equals(lancamentoAcao.getAcao().getCodigo())).findAny().get());

		compra.getLancamentosAcoes().remove(index);
		lancamentoAcao = new LancamentoAcao();
	}

	public void carregarCategoriasDeProjeto() {
		if (!(lancamentoAcao == null || lancamentoAcao.getProjetoRubrica() == null)) {
			filtro.setProjetoId(lancamentoAcao.getProjetoRubrica().getProjeto().getId());
			listCategoriaProjeto = compraService.getCategoriasDeProjeto(filtro);
		}
	}

	public void adicionarNovaAcao() {
		if (verificaCamposRecurso()) {
			return;
		}

		lancamentoAcao.setTipo(TipoLancamento.NEUTRO);
		lancamentoAcao.setLancamento(compra);
		compra.getLancamentosAcoes().add(lancamentoAcao);
		lancamentoAcao = new LancamentoAcao();

	}

	public Boolean verificaCamposRecurso() {

		Boolean retorno = false;

		if (lancamentoAcao.getProjetoRubrica() == null) {
			addMessage("", "Preencha o campo 'Linha orçamentária' antes de prosseguir.", FacesMessage.SEVERITY_ERROR);
			retorno = true;
		}

		return retorno;
	}


	private Map<String, String> mapCategorias = new HashMap<>();

	private List<UnidadeDeCompra> unidades = new ArrayList<>();

	public void addItem() {

		itemCompra.setUnidade(itemCompra.getUnidadeM().getDescricao());
		itemCompra.setCompra(compra);
		compra.getItens().add(itemCompra);

		if (!itemCompra.getProduto().getDescricao().equals("FRETE SEDEX")
				&& !itemCompra.getProduto().getDescricao().equals("FRETE PAC")) {
			if (verificaCategoria()) {
				addMessage("", "Você não pode inserir dois itens de segmentos diferentes.",
						FacesMessage.SEVERITY_ERROR);
				compra.getItens().remove(compra.getItens().size() - 1);
				return;
			}
		}

		itemCompra = new ItemCompra();
	}

	public void carregarUnidades() {
		unidades = compraService.buscarUnidades();
	}

	public boolean verificaCategoria() {
		List<String> categorias = new ArrayList<>();
		for (ItemCompra item : compra.getItens()) {
			String categoria = item.getProduto().getCategoriaDespesa().getNome();
			if (categorias.size() > 0) {
				if (categorias.stream().filter(cat -> cat.equals(categoria)).findAny().isPresent()) {
					continue;
				} else {
					return true;
				}
			} else {
				categorias.add(categoria);
			}

		}

		return false;
	}

	public UnidadeMedida[] getUnidades() {
		return UnidadeMedida.values();
	}

	public void editarItemCompra(int index) {
		compra.getItens().remove(index);
	}

	public void atualizarStatusSC() {
		compra = compraService.verificaStatusSC(compra);
	}

	public void removerItem(int index) {

		if (compra.getId() != null) {
			if (!compra.getStatusCompra().getNome().equals("Não iniciado/Solicitado")) {
				User usuarioAuxiliar = usuarioSessao.getUsuario();
				if (usuarioAuxiliar.getPerfil().getDescricao().equals("compra")
						|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")) {
					ItemCompra item = compra.getItens().get(index);
					if (item.getId() != null) {
						if (compraService.verificaItemPedido(item.getId())) {
							FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
									"Existem pedidos para o item que deseja excluir, por favor entre no pedido de compra e selecione a opção de exclusão que deseja.");
							FacesContext.getCurrentInstance().addMessage(null, message);
							return;
						}

						compra.getItens().remove(index);
						// compraService.excluirItemPedidoByItemCompra(item);
						compraService.excluirCotacaoByItem(item);
						compraService.verificaStatusExclusaoItemCompra(compra);
					} else {
						compra.getItens().remove(index);
					}

					return;
				}

				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
						"Os itens ja foram autorizados e não podem ser excluídos, senão pelo pelo gestor de compras, por favor entre em contato com o setor.");
				FacesContext.getCurrentInstance().addMessage(null, message);

				return;
			} else {
				ItemCompra item = compra.getItens().get(index);
				if (item.getId() != null) {
					if (compraService.verificaItemPedido(item.getId())) {
						FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
								"Existem pedidos para o item que deseja excluir, por favor entre no pedido de compra e selecione a opção de exclusão que deseja.");
						FacesContext.getCurrentInstance().addMessage(null, message);
						return;
					}

					compra.getItens().remove(index);
					// compraService.excluirItemPedidoByItemCompra(item);
					compraService.excluirCotacaoByItem(item);
					compraService.verificaStatusExclusaoItemCompra(compra);
				} else {
					compra.getItens().remove(index);
				}
			}

		} else {
			compra.getItens().remove(index);
		}

	}

	public void carregarPedidos() {
		pedidos = pedidoService.getPedidos(compra);
	}

	public void carregarPedidos(Long id) {
		compra = compraService.getCompraById(id);
		pedidos = pedidoService.getPedidos(compra);
	}

	/**
	 * Método de reabertura de compra, volta ao status de inicializado, permitindo
	 * ao usuário final fazer alterações
	 * 
	 * @return void
	 */

	public void reabrir() {
		if (verificarPrivilegioReabertura()) {
			LogStatus log = new LogStatus();
			log.setUsuario(usuarioSessao.getUsuario());
			log.setLancamento(compra);
			log.setData(new Date());
			log.setStatusLog(StatusCompra.N_INCIADO);
			log.setSigla(compra.getGestao().getSigla());
			log.setSiglaPrivilegio("RSC"); // reabertura de compra
			compra = compraService.mudarStatus(compra.getId(), StatusCompra.N_INCIADO, log);
			compra.setItens(new ArrayList<>());
			compra.setItens(compraService.getItensByCompra(compra));
			addMessage("", "Compra reaberta, você pode editar agora", FacesMessage.SEVERITY_INFO);
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você não tem privilégios para esse tipo de ação, por favor contate o administrador do sistema!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}

	

	public void autorizar() throws AddressException, MessagingException {

		if (verificarAprovacao()) {
			LogStatus log = new LogStatus();
			log.setUsuario(usuarioSessao.getUsuario());
			log.setLancamento(compra);
			log.setData(new Date());
			log.setStatusLog(StatusCompra.APROVADO);
			log.setSigla(aprouve.getSigla());
			log.setSiglaPrivilegio("ASC");
			compra = compraService.mudarStatus(compra.getId(), StatusCompra.APROVADO, log);
			addMessage("", "Solicitação aprovada!", FacesMessage.SEVERITY_INFO);
			try {

				if (email.verificaInternet()) {
					 prepararEmailDeAvisoDeAprovacao();
					//prepararEmailDeAvisoDeAprovacaoTESTE();
					email.EnviarEmailSimples();
					email = new Email();

				} else {
					addMessage("",
							"Problema de conexão com a internet, por favor contate o administrador do sistema, a solicitação foi aprovada mas não foi enviada para os e-mails designados",
							FacesMessage.SEVERITY_INFO);
					limparEmail();

				}

			} catch (Exception e) {
				closeDialogLoading();
				limparEmail();
				PrimeFaces.current().executeScript("PF('dlg_aprovacao_coordenador').hide();");
				addMessage("",
						"Problema de conexão com a internet, por favor contate o administrador do sistema, a solicitação foi "
								+ "aprovada mas não foi enviada para os e-mails designados",
						FacesMessage.SEVERITY_INFO);
				return;
			} finally {
				PrimeFaces.current().executeScript("PF('dlg_aprovacao_coordenador').hide();");
			}

			closeDialogLoading();
			return;

		} else {
			closeDialogLoading();
			limparEmail();
			addMessage("", "Senha incorreta para aprovação", FacesMessage.SEVERITY_ERROR);
			return;
		}

	}

	public Boolean verificarAprovacao() {
		aprouve.setSigla(compra.getGestao().getSigla());
		Boolean retorno = compraService.verificarAprovacao(aprouve);

		if (!retorno) {
			aprouve.setSigla("ADM_PIV");
			retorno = compraService.verificarAprovacao(aprouve);
		}

		return retorno;
	}


	public void prepararEmailDeAvisoDeAprovacao() {

		StringBuilder toEmailAux = new StringBuilder("compra@fas-amazonas.org,admsgi@fas-amazonas.org");
		toEmailAux.append(",");
		toEmailAux.append(compra.getSolicitante().getEmail());
		toEmailAux.append(",");
		toEmailAux.append(compra.getGestao().getColaborador().getEmail());

		email.setToEmail(toEmailAux.toString());
		email.setFromEmail("comprasgi@fas-amazonas.org");
		email.setSubject("SC - " + compra.getId() + " Autorizada\n Data: "
				+ new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		email.setSolicitação(String.valueOf(compra.getId()));
		email.setSenhaEmail("FAS123fas");
		email.setContent(gerarTextoDeAprovacao());
	}

	

	public String gerarCodigo() {

		StringBuilder s = new StringBuilder("SC");

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

	public void limpar() {
		tipoGestao = "";
		local = "";
		compra = new Compra();
	}

	public void onTipoGestaoChange() {
		tipoGestao = "";
		if (compra.getTipoGestao() != null) {
			tipoGestao = compra.getTipoGestao().getNome();
			GestaoRepositorio repo = CDILocator.getBean(GestaoRepositorio.class);
			gestoes = new ArrayList<>();
			gestoes = compra.getTipoGestao().getGestao(repo);
		}

		closeDialogLoading();
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

	public List<Produto> completeProduto(String query) {
		allProdutos = new ArrayList<Produto>();
		allProdutos = compraService.getProdutos(query);
		return allProdutos;
	}

	public List<FontePagadora> completeFonte(String query) {
		return compraService.fontesAutoComplete(query);
	}

	public List<Acao> completeAcoes(String query) {
		allAcoes = new ArrayList<Acao>();
		allAcoes = compraService.acoesAutoComplete(query);
		return allAcoes;
	}

	public void initCadastro() {
		editarCompraMODE01();
		PrimeFaces.current().executeScript("setarFocused('info-geral')");
	}

	public void iniciarNovaCompra() {
		panelCadastro = true;
		panelListagem = false;
		carregarRubricas();
		carregarProjetos();
		carregarMunicipiosDeCompra();
		carregarUnidades();

		// if (panelCadastro) {
		// compra.setItens(new ArrayList<>());
		// compra.setItens(compraService.getItensByCompra(compra));
		// }

		limpar();
	}

	public void editarCompraMODE01() {

		carregarProjetos();
		carregarMunicipiosDeCompra();
		// carregarUnidades();

		if (compra.getId() == null) {
			return;
		}

		comprasFiltered = new ArrayList<>();

		panelCadastro = true;
		panelListagem = false;

		compra = compraService.getCompraById(compra.getId());
		buscarMunicipiosDeCompra();
		// compra.setLocalidade(compraService.getLocalidadeById(compra.getId()));
		compra.setItens(new ArrayList<ItemCompra>());
		compra.setItens(compraService.getItensByCompraAllProducts(compra));
		compra.setLancamentosAcoes(compraService.getLancamentosAcoes(compra));

		tipoGestao = compra.getTipoGestao().getNome();
		local = compra.getTipoLocalidade().getNome();

		if (compra.getLocalidade() instanceof Municipio) {
			estados = compraService.getEstados(new Filtro());
			idEstado = ((Municipio) compra.getLocalidade()).getEstado().getId();
			localidades = compraService.getMunicipioByEstado(idEstado);
		} else {
			LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
			localidades = compra.getTipoLocalidade().getLocalidade(repo);
		}

		GestaoRepositorio repo = CDILocator.getBean(GestaoRepositorio.class);
		gestoes = new ArrayList<>();
		gestoes = compra.getTipoGestao().getGestao(repo);

		carregarRubricas();

		closeDialogLoading();

	}

	public void editarCompra() {
		comprasFiltered = new ArrayList<>();

		panelCadastro = true;
		panelListagem = false;

		compra = compraService.getCompraById(compra.getId());
		buscarMunicipiosDeCompra();
		// compra.setLocalidade(compraService.getLocalidadeById(compra.getId()));
		compra.setItens(new ArrayList<ItemCompra>());
		compra.setItens(compraService.getItensByCompraAllProducts(compra));
		compra.setLancamentosAcoes(compraService.getLancamentosAcoes(compra));

		tipoGestao = compra.getTipoGestao().getNome();
		local = compra.getTipoLocalidade().getNome();

		if (compra.getLocalidade() instanceof Municipio) {
			estados = compraService.getEstados(new Filtro());
			idEstado = ((Municipio) compra.getLocalidade()).getEstado().getId();
			localidades = compraService.getMunicipioByEstado(idEstado);
		} else {
			LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
			localidades = compra.getTipoLocalidade().getLocalidade(repo);
		}

		GestaoRepositorio repo = CDILocator.getBean(GestaoRepositorio.class);
		gestoes = new ArrayList<>();
		gestoes = compra.getTipoGestao().getGestao(repo);

		carregarRubricas();

		closeDialogLoading();

	}

	
	private void executeScript(String script) {
		PrimeFaces.current().executeScript(script);
	}
	
	private void openDialog() {
		PrimeFaces.current().executeScript("PF('statusDialog').show();");
	}

	private void closeDialogLoading() {
		PrimeFaces.current().executeScript("PF('statusDialog').hide();");
	}

	public void iniciarNovoLancamentoAcao() {
		lancamentoAcao = new LancamentoAcao();
	}

	/**
	 * Método de retorno pra tela de listagem de compras
	 * 
	 * @return void
	 */
	public String voltarListagem() {
		return "compra_new?faces-redirect=true";
	}

	public TipoGestao buscarTipoGestao() {
		String type = compra.getGestao().getType();
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

		String type = compra.getLocalidade().getType();
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

	private Integer tt = 0;

	public String salvarCompra() {

		// for (Municipio municipio : municipiosDeCompraSelected) {

		// }

		compra.setTipoGestao(buscarTipoGestao());
		compra.setTipoLocalidade(buscarTipoLocalidade());

		// if (municipiosDeCompraSelected.isEmpty()) {
		// addMessage("", "Por favor adicione no mínimo um municipio para compra.",
		// FacesMessage.SEVERITY_WARN);
		// closeDialogLoading();
		// return "";
		// }
		//
		// if (compra.getBootUrgencia()) {
		// if (compra.getJustificativa().length() < 3) {
		// addMessage("", "Por favor justifique a urgência.",
		// FacesMessage.SEVERITY_WARN);
		// closeDialogLoading();
		// return "";
		// }
		// }
		//
		// if (compra.getItens().isEmpty()) {
		// addMessage("", "Por favor adicione no mínimo um item de compra.",
		// FacesMessage.SEVERITY_WARN);
		// closeDialogLoading();
		// return "";
		// }
		//
		// if (compra.getLancamentosAcoes().isEmpty()) {
		// addMessage("", "Por favor adicione ações que irão custear essas despesas.",
		// FacesMessage.SEVERITY_WARN);
		// closeDialogLoading();
		// return "";
		// } else if (verificarFundoAmazonia()) {
		//
		// }
		//
		// if (compra.getId() == null) {
		// compra.setCodigo(gerarCodigo());
		// compra.setSolicitante(usuarioSessao.getUsuario().getColaborador());
		// }
		//
		// if (compraService.salvar(compra, usuarioSessao.getUsuario(),
		// municipiosDeCompraSelected)) {
		// limpar();
		// closeDialogLoading();
		// return "compra_new?faces-redirect=true";
		// }
		closeDialogLoading();
		return "";
	}

	public void salvar(Integer type) throws EmailException {

		compra.setTipoGestao(buscarTipoGestao());
		compra.setTipoLocalidade(buscarTipoLocalidade());
		compra.setVersionLancamento("MODE01");
		compra.setTipov4("SC");

		if (compra.getItens().isEmpty()) {
			addMessage("", "Por favor adicione no mínimo um item de compra.", FacesMessage.SEVERITY_WARN);
			closeDialogLoading();
			return;
		}

		if (compra.getLancamentosAcoes().isEmpty()) {
			addMessage("", "Por favor adicione ações que irão custear essas despesas.", FacesMessage.SEVERITY_WARN);
			closeDialogLoading();
			return;
		}

		if (compra.getId() == null) {
			compra.setCodigo(gerarCodigo());
			compra.setSolicitante(usuarioSessao.getUsuario().getColaborador());
		}

		compraService.salvar(compra, usuarioSessao.getUsuario(), municipiosDeCompraSelected, type);
		executeScript("PF('dlg_salvar').hide();");
		
		if (type == 1) {
			executeScript("PF('dlg_email').show();");
		}
		
		
		closeDialogLoading();
		
		

		

	}

	public String salvarCompraMODE01() {

		// compra.setTipoGestao(buscarTipoGestao());
		// compra.setTipoLocalidade(buscarTipoLocalidade());
		// compra.setVersionLancamento("MODE01");
		//
		// if (compra.getItens().isEmpty()) {
		// addMessage("", "Por favor adicione no mínimo um item de compra.",
		// FacesMessage.SEVERITY_WARN);
		// closeDialogLoading();
		// return "";
		// }
		//
		// if (compra.getLancamentosAcoes().isEmpty()) {
		// addMessage("", "Por favor adicione ações que irão custear essas despesas.",
		// FacesMessage.SEVERITY_WARN);
		// closeDialogLoading();
		// return "";
		// }
		//
		// if (compra.getId() == null) {
		// compra.setCodigo(gerarCodigo());
		// compra.setSolicitante(usuarioSessao.getUsuario().getColaborador());
		// }
		//
		// if (compraService.salvar(compra, usuarioSessao.getUsuario(),
		// municipiosDeCompraSelected)) {
		// limpar();
		// closeDialogLoading();
		// return "compra_new?faces-redirect=true";
		// }

		closeDialogLoading();

		return "";
	}

	public boolean verificarFundoAmazonia() {

		// for (LancamentoAcao lancAcao : compra.getLancamentosAcoes()) {
		// if (lancAcao.getFontePagadora().getId().longValue() == 3) {
		// return true;
		// }
		//
		// }

		return false;
	}

	public void suspender() {
		if (verificarPrivilegioSuspensao()) {
			LogStatus log = new LogStatus();
			log.setUsuario(usuarioSessao.getUsuario());
			log.setLancamento(compra);
			log.setData(new Date());
			log.setStatusLog(StatusCompra.CANCELADO);
			log.setSigla(compra.getGestao().getSigla());
			log.setSiglaPrivilegio("SSC");
			compra = compraService.mudarStatus(compra.getId(), StatusCompra.CANCELADO, log);
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você não tem privilégios para esse tipo de ação, por favor contate o administrador do sistema!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}

	public boolean verificarPrivilegioSuspensao() {
		Aprouve ap = new Aprouve();
		ap.setSigla("SSC");
		ap.setUsuario(usuarioSessao.getUsuario());
		return compraService.findAprouve(ap);
	}

	public boolean verificarPrivilegioReabertura() {
		Aprouve ap = new Aprouve();
		ap.setSigla("RSC");
		ap.setUsuario(usuarioSessao.getUsuario());
		return compraService.findAprouve(ap);
	}

	public void cancelar() {
		if (verificarPrivilegioSuspensao()) {
			LogStatus log = new LogStatus();
			log.setUsuario(usuarioSessao.getUsuario());
			log.setLancamento(compra);
			log.setData(new Date());
			log.setStatusLog(StatusCompra.CANCELADO);
			log.setSigla(compra.getGestao().getSigla());
			log.setSiglaPrivilegio("SSC");
			compra = compraService.mudarStatus(compra.getId(), StatusCompra.CANCELADO, log);
			addMessage("", "Compra cancelada.", FacesMessage.SEVERITY_INFO);
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você não tem privilégios para esse tipo de ação, por favor contate o administrador do sistema!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

	}

//	public String redirecionar() {
//		return menu.getEndereco() + "?faces-redirect=true";
//	}

	public List<MenuLateral> getMenus() {
		if (menus.isEmpty())
			menus = MakeMenu.getMenuCompra();
		return menus;
	}

	@Deprecated
	public void onLocalChangeForProject() {
		local = "";
		if (compra.getTipoLocalidade() != null) {
			local = compra.getTipoLocalidade().getNome();
			if (local.equals("Municipio")) {
				estados = new ArrayList<>();
				estados = compraService.getEstados(new Filtro());
				localidades = new ArrayList<>();
				idEstado = new Long(0);
			} else {
				LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
				localidades = compra.getTipoLocalidade().getLocalidade(repo);
			}
		}

		closeDialogLoading();
	}

	@Deprecated
	public void onLocalChangeFilter() {
		local = "";
		if (filtro.getTipoLocalidade() != null) {
			local = filtro.getTipoLocalidade().getNome();
			if (local.equals("Municipio")) {
				estados = new ArrayList<>();
				estados = compraService.getEstados(new Filtro());
				localidades = new ArrayList<>();
				idEstado = new Long(0);
			} else {
				LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
				localidades = filtro.getTipoLocalidade().getLocalidade(repo);
			}
		}
	}

	private @Inject RubricaRepositorio rubricaRepositorio;
	private List<Rubrica> rubricas = new ArrayList<>();

	public void carregarRubricas() {
		rubricas = rubricaRepositorio.getRubricas();
	}

	public void filtrar() {

		if (filtro.getGestao() != null && filtro.getGestao().getId() != null) {
			filtro.setGestaoID(filtro.getGestao().getId());
		} else {
			filtro.setGestao(usuarioSessao.getUsuario().getGestao()); // setar o valor da gestão de acordo com o perfil do usuário
		}

		if (filtro.getLocalidade() != null && filtro.getLocalidade().getId() != null) {
			filtro.setLocalidadeID(filtro.getLocalidade().getId());
		}
		compras = compraService.getCompras(filtro);
		// filtro = new Filtro();
	}

	public void limparFiltro() {
		filtro = new Filtro();
		local = "";
		tipoGestao = "";
	}

	public void imprimir(Compra comp) {
		compraService.imprimirCompra(comp);

	}

	public void imprimir() {
		compraService.imprimirCompra(compra);
	}

	public void carregarOrcamentosProjeto() {
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

	public void enviarEmailAutorizacaoSC() {
		compra = compraService.getCompraById(compra.getId());
		compraService.enviarEmailAutorizacaoSC(compra, 0);
		closeDialogLoading();
	}
	

	public void imprimirFormulario(Long id) {

		compra = compraService.getCompraById(id);
		List<ItemCompra> itens = compraService.getItensTransientByCompra(compra);

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		String logo = path + "resources/image/logoFas.gif";

		Map parametros = new HashMap();
		parametros.put("logo", logo);
		parametros.put("tipo_gestao", compra.getTipoGestao().getNome());
		parametros.put("gestao", compra.getGestao().getNome());
		parametros.put("tipo_localidade", compra.getTipoLocalidade().getNome());
		parametros.put("localidade", compra.getLocalidade().getNome());
		parametros.put("usuario",
				usuarioSessao.getNomeUsuario() + " " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
		parametros.put("solicitacao", "" + compra.getId());
		parametros.put("observ", "");
		JRDataSource dataSource = new JRBeanCollectionDataSource(itens);
		;

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/coleta_preco.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReport("Formulário", "Formulário" + compra.getId(), report, parametros, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void limparEmail() {
		email = new Email();
	}

	private String gerarConteudo() {
		StringBuilder str = new StringBuilder();

		str.append("Solicitação de aprovação \n\n");

		str.append("Solicitação: " + compra.getId() + "\n");
		str.append("Data:        " + new SimpleDateFormat("dd/MM/yyyy").format(compra.getDataEmissao()) + "\n");
		str.append("Solicitante: " + compra.getSolicitante().getNome() + "\n");
		str.append("Tipo gestão: " + compra.getTipoGestao().getNome() + "\n");
		str.append("Gestão: " + compra.getGestao().getNome() + "\n\n");

		str.append("Solicito aprovação para abertura do processo de compras, para a solicitação em anexo.");

		return str.toString();
	}

	private String gerarTextoDeAprovacao() {
		StringBuilder str = new StringBuilder();

		str.append("SC aprovada!!! \n\n");

		str.append("Solicitação:" + compra.getId() + "\n");
		str.append("Data de aprov:" + new SimpleDateFormat("dd/MM/yyyy").format(compra.getDataEmissao()) + "\n");
		str.append("Solicitante:" + compra.getSolicitante().getNome() + "\n");
		str.append("Tipo gestão:" + compra.getTipoGestao().getNome() + "\n");
		str.append("Gestão: " + compra.getGestao().getNome() + "\n");
		// str.append("Aprovado p/: " +apro+"\n");
		str.append("\n\n");
		str.append("Muito bem, sua solicitação de compra foi aprovada e já está pronta para o processo de cotação\n");
		str.append("Esse email também está sendo copiado o setor de compras.");

		return str.toString();
	}

	public void onEstadoChange() {
		localidades = compraService.getMunicipioByEstado(idEstado);
		idEstado = new Long(0);
	}

	

	public List<Compra> getCompras() {
		return compras;
	}

	public void setCompras(List<Compra> compras) {
		this.compras = compras;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
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

	public String getTipoGestao() {
		return tipoGestao;
	}

	public void setTipoGestao(String tipoGestao) {
		this.tipoGestao = tipoGestao;
	}

	public List<Gestao> getGestoes() {
		return gestoes;
	}

	public void setGestoes(List<Gestao> gestoes) {
		this.gestoes = gestoes;
	}

	public TipoGestao[] getTiposGestao() {
		return TipoGestao.values();
	}

	public StatusCompra[] getStatusCompras() {
		return StatusCompra.values();
	}

	public TipoLocalidade[] getTipoLocais() {
		return TipoLocalidade.values();
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

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public List<Localidade> getLocalidades() {
		return localidades;
	}

	public void setLocalidades(List<Localidade> localidades) {
		this.localidades = localidades;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public ItemCompra getItemCompra() {
		return itemCompra;
	}

	public void setItemCompra(ItemCompra itemCompra) {
		this.itemCompra = itemCompra;
	}

	public List<Produto> getAllProdutos() {
		return allProdutos;
	}

	public void setAllProdutos(List<Produto> allProdutos) {
		this.allProdutos = allProdutos;
	}

	public List<Compra> getComprasFiltered() {
		return comprasFiltered;
	}

	public void setComprasFiltered(List<Compra> comprasFiltered) {
		this.comprasFiltered = comprasFiltered;
	}

	public LancamentoAcao getLancamentoAcao() {
		return lancamentoAcao;
	}

	public void setLancamentoAcao(LancamentoAcao lancamentoAcao) {
		this.lancamentoAcao = lancamentoAcao;
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

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public Aprouve getAprouve() {
		return aprouve;
	}

	public void setAprouve(Aprouve aprouve) {
		this.aprouve = aprouve;
	}

	public List<ItemCompra> getItensDetalhados() {
		return itensDetalhados;
	}

	public void setItensDetalhados(List<ItemCompra> itensDetalhados) {
		this.itensDetalhados = itensDetalhados;
	}

	public List<ItemPedido> getItensPedidos() {
		return itensPedidos;
	}

	public void setItensPedidos(List<ItemPedido> itensPedidos) {
		this.itensPedidos = itensPedidos;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public List<Municipio> getMunicipiosDeCompra() {
		return municipiosDeCompra;
	}

	public void setMunicipiosDeCompra(List<Municipio> municipiosDeCompra) {
		this.municipiosDeCompra = municipiosDeCompra;
	}

	public List<Municipio> getMunicipiosDeCompraSelected() {
		return municipiosDeCompraSelected;
	}

	public void setMunicipiosDeCompraSelected(List<Municipio> municipiosDeCompraSelected) {
		this.municipiosDeCompraSelected = municipiosDeCompraSelected;
	}

	public Integer getTt() {
		return tt;
	}

	public void setTt(Integer tt) {
		this.tt = tt;
	}

	public void setLocalDeCompra(StringBuilder localDeCompra) {
		this.localDeCompra = localDeCompra;
	}

	public StringBuilder getLocalDeCompra() {
		return localDeCompra;
	}

	public List<Rubrica> getRubricas() {
		return rubricas;
	}

	public void setRubricas(List<Rubrica> rubricas) {
		this.rubricas = rubricas;
	}

	public List<ProjetoRubrica> getListaDeRubricasProjeto() {
		return listaDeRubricasProjeto;
	}

	public void setListaDeRubricasProjeto(List<ProjetoRubrica> listaDeRubricasProjeto) {
		this.listaDeRubricasProjeto = listaDeRubricasProjeto;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
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

	public List<ProjetoRubrica> getProjetoRubricas() {
		return projetoRubricas;
	}

	public void setProjetoRubricas(List<ProjetoRubrica> projetoRubricas) {
		this.projetoRubricas = projetoRubricas;
	}

	public Long getIdOrcamento() {
		return idOrcamento;
	}

	public void setIdOrcamento(Long idOrcamento) {
		this.idOrcamento = idOrcamento;
	}

	public List<CategoriaProjeto> getListCategoriaProjeto() {
		return listCategoriaProjeto;
	}

	public void setListCategoriaProjeto(List<CategoriaProjeto> listCategoriaProjeto) {
		this.listCategoriaProjeto = listCategoriaProjeto;
	}

	public List<CategoriaDespesaClass> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<CategoriaDespesaClass> categorias) {
		this.categorias = categorias;
	}

	public void setUnidades(List<UnidadeDeCompra> unidades) {
		this.unidades = unidades;
	}

	// Alterações para inserir atividades nas solicitaçoes de compras 13/02/2019
	// 22:18 -------------------------------------------

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

	public Integer getStepIndex() {
		return stepIndex;
	}

	public void setStepIndex(Integer stepIndex) {
		this.stepIndex = stepIndex;
	}

	public UsuarioSessao getUsuarioSessao() {
		return usuarioSessao;
	}

	public void setUsuarioSessao(UsuarioSessao usuarioSessao) {
		this.usuarioSessao = usuarioSessao;
	}

	private Projeto projetoAux;

	public Projeto getProjetoAux() {
		return projetoAux;
	}

	public void setProjetoAux(Projeto projetoAux) {
		this.projetoAux = projetoAux;
	}

	public List<Projeto> getListaProjeto() {
		return listaProjeto;
	}

	public void setListaProjeto(List<Projeto> listaProjeto) {
		this.listaProjeto = listaProjeto;
	}

	// ----------------------------------------------------------------------------------------------------------

}
