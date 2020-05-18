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

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import model.Aprouve;
import model.Compra;
import model.ControleExpedicao;
import model.CotacaoAuxiliar;
import model.Fornecedor;
import model.ItemPedido;
import model.Lancamento;
import model.LancamentoAcao;
import model.MenuLateral;
import model.Pedido;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import service.CompraService;
import service.PedidoService;
import util.ArquivoLancamento;
import util.DiretorioUtil;
import util.DownloadUtil;
import util.MakeMenu;
import util.ReportUtil;
import util.UsuarioSessao;
import util.Util;

@Named(value = "pedidoController")
@ViewScoped
public class PedidoController implements Serializable {

	private static final long serialVersionUID = 1L;

	private MenuLateral menu = new MenuLateral();
	private List<CotacaoAuxiliar> cotacoesAuxiliares = new ArrayList<>();
	private Long idFornecedor;
	private List<Pedido> pedidos = new ArrayList<Pedido>();
	private Lancamento lancamentoAux = new Lancamento();
	private ArquivoLancamento arquivo = new ArquivoLancamento();
	private String path = "";

	private @Inject Compra compra;
	private @Inject PedidoService pedidoService;
	private @Inject UsuarioSessao usuarioSessao;
	private @Inject Pedido pedido;
	private @Inject ArquivoLancamento arqAuxiliar;
	private @Inject ControleExpedicao controleExpedicao;

	public PedidoController() {
	}

	public void carregarPedidos() throws IOException {

		// compra = (Compra)
		// FacesContext.getCurrentInstance().getExternalContext().getFlash().get("compra"+usuarioSessao.getNomeUsuario());

		if (compra == null || compra.getId() == null) {
			// FacesContext.getCurrentInstance().getExternalContext().getFlash().put("compra"+usuarioSessao.getNomeUsuario(),
			// cotacoes.get(0).getCompra());
			HttpServletResponse resp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			resp.sendRedirect(req.getContextPath() + "/main/compras.xhtml");
			return;
		}
		pedidos = pedidoService.getPedidos(compra);
	}

	public void carregarArquivos() {
		lancamentoAux = pedidoService.getLancamentoById(lancamentoAux.getId());
		lancamentoAux.setArquivos(new ArrayList<ArquivoLancamento>());
		lancamentoAux.setArquivos(pedidoService.getArquivoByLancamento(lancamentoAux.getId()));
	}

	public boolean verificaPermissao() {
		if (usuarioSessao.getNomeUsuario().equals("admin")) {
			return true;
		}
		return false;
	}

	private UploadedFile file;

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		// String pathFinal = DiretorioUtil.DIRECTORY_UPLOAD_LINUX;
		file = event.getFile();

		String pathFinal = DiretorioUtil.DIRECTORY_UPLOAD;
		arquivo.setConteudo(event.getFile().getContents());
		arquivo.setPath(pathFinal + lancamentoAux.getId() + "." + event.getFile().getFileName());
		path = event.getFile().getFileName();
		arquivo.setData(new Date());

	}

	public void preparaArquivo() {
		String pathFinal = DiretorioUtil.DIRECTORY_UPLOAD;
		arquivo.setConteudo(file.getContents());
		arquivo.setPath(pathFinal + lancamentoAux.getId() + "." + file.getFileName());
		path = file.getFileName();
		arquivo.setData(new Date());
		arquivo.setUsuario(usuarioSessao.getUsuario());
	}

	public void adicionarArquivo() {
		preparaArquivo();
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
		pedidoService.salvarArquivo(arquivo);

		path = "";

		arquivo = new ArquivoLancamento();

		carregarArquivos();
	}

	public void removerArquivo() {
		if (verificarPrivilegioENF()) {
			pedidoService.removerArquivo(arqAuxiliar);
			arqAuxiliar = new ArquivoLancamento();
			carregarArquivos();
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
		return pedidoService.verificarPrivilegioENF(aprouve);
	}

	public void visualizarArquivo() throws IOException {
		DownloadUtil.downloadFile(arqAuxiliar.getNome(), arqAuxiliar.getPath(), "application/pdf",
				FacesContext.getCurrentInstance());
		arqAuxiliar = new ArquivoLancamento();
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	private @Inject CompraService compraService;

	public void imprimirPedido() {

		List<ItemPedido> itens = pedidoService.getItensPedidosByPedido(pedido);

		List<LancamentoAcao> acoes = pedidoService.getLancamentosDoPedido(pedido);

		StringBuilder acs = new StringBuilder();

		if (pedido.getCompra().getVersionLancamento() != null || pedido.getCompra().getVersionLancamento().equals("MODE01")) {
			for (LancamentoAcao lancamentoAcao : acoes) {
				acs.append(lancamentoAcao.getProjetoRubrica().getComponente().getNome());
				acs.append(" | ");
				acs.append(lancamentoAcao.getProjetoRubrica().getProjeto().getNome());
				acs.append(" | ");
				acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getTitulo());
				acs.append(" | ");
				acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getRubrica().getNome());
				acs.append(" | ");
				acs.append("R$: "+lancamentoAcao.getValor());
				acs.append("\n");
			}
		} else {
			for (LancamentoAcao lancamentoAcao : acoes) {
				acs.append(lancamentoAcao.getAcao().getCodigo() + " \n");
			}
		}
		
		
//		for (LancamentoAcao lancamentoAcao : acoes) {
//			acs.append(lancamentoAcao.getAcao().getCodigo() + ": " + getFormatacao(lancamentoAcao.getValor()) + "\n");
//		}

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

		parametros.put("usuario", usuarioSessao.getNomeUsuario() + " "
				+ new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));

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

	private @Inject Aprouve aprouve;

	public Boolean verificarAprovacao() {
		// aprouve.setSiglaGestao(compra.getGestao().getSigla());
		aprouve.setSigla("EPC");
		return pedidoService.verificarAprovacao(aprouve);
	}

	public void removerPedido() {

		 //pedidoService.mudarStatusPedido(pedido, usuarioSessao.getUsuario());
		pedidoService.remover(pedido);
		pedidos = pedidoService.getPedidos(pedido.getCompra());
	}

	public String prepararPagamentoPedido() {
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("pedido" + usuarioSessao.getNomeUsuario(),
				pedido);
		return "pedidoPagamento?faces-redirect=true";
	}

	public String redirecionarPageCadastroPedido() {
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("pedido" + usuarioSessao.getNomeUsuario(),
				pedido);
		return "pedidoCadastro?faces-redirect=true";
	}

	public String redirecionarPageCompras() {
		compra = null;
		return "compras?faces-redirect=true";
	}

	public String editarCotacao() {
		/*
		 * Fornecedor fornecedor =
		 * cotacaoService.getFornecedorById(idFornecedor);
		 * FacesContext.getCurrentInstance().getExternalContext().getFlash().put
		 * ("compra"+usuarioSessao.getNomeUsuario(), compra);
		 * FacesContext.getCurrentInstance().getExternalContext().getFlash().put
		 * ("fornecedor"+usuarioSessao.getNomeUsuario(), fornecedor);
		 */
		return "cotacaoCadastro?faces-redirect=true";
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public List<Fornecedor> getFornecedor() {
		return null;
	}

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public List<MenuLateral> getMenus() {
		List<MenuLateral> lista = new ArrayList<>();
		MenuLateral menu = new MenuLateral("Compras", "compras", "fa fa-shopping-cart");
		lista.add(menu);
		menu = new MenuLateral("Produtos", "produtos", "fa fa-database");
		lista.add(menu);
		menu = new MenuLateral("Fornecedores", "fornecedores", "fa fa-user");
		lista.add(menu);
		return MakeMenu.getMenuCompra();
	}

	public String getFormatacao(BigDecimal total) {
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(total);
		// return new DecimalFormat("###,###.###").format(total);
	}

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}

	public List<CotacaoAuxiliar> getCotacoesAuxiliares() {
		return cotacoesAuxiliares;
	}

	public void setCotacoesAuxiliares(List<CotacaoAuxiliar> cotacoesAuxiliares) {
		this.cotacoesAuxiliares = cotacoesAuxiliares;
	}

	public Long getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
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

	public ArquivoLancamento getArqAuxiliar() {
		return arqAuxiliar;
	}

	public void setArqAuxiliar(ArquivoLancamento arqAuxiliar) {
		this.arqAuxiliar = arqAuxiliar;
	}

	public ControleExpedicao getControleExpedicao() {
		return controleExpedicao;
	}

	public void setControleExpedicao(ControleExpedicao controleExpedicao) {
		this.controleExpedicao = controleExpedicao;
	}

	public Aprouve getAprouve() {
		return aprouve;
	}

	public void setAprouve(Aprouve aprouve) {
		this.aprouve = aprouve;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

}
