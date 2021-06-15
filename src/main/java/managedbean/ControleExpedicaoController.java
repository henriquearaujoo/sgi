package managedbean;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
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
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import model.Colaborador;
import model.ControleExpedicao;
import model.Estado;
import model.Fornecedor;
import model.Gestao;
import model.ItemPedido;
import model.LancamentoAcao;
import model.Localidade;
import model.LocalizacaoPedido;
import model.MenuLateral;
import model.Pedido;
import model.TermoExpedicao;
import model.TipoAprovador;
import model.TipoGestao;
import model.TipoLocalidade;
import model.TipoTermoExpedicao;
import model.UnidadeConservacao;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import repositorio.GestaoRepositorio;
import repositorio.LocalRepositorio;
import service.CompraService;
import service.ControleExpedicaoService;
import service.SolicitacaoPagamentoService;
import util.CDILocator;
import util.DataUtil;
import util.Filtro;
import util.MakeMenu;
import util.ReportUtil;
import util.UsuarioSessao;
import util.Util;

@ViewScoped
@Named(value = "controle_expedicao_controller")
public class ControleExpedicaoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ControleExpedicao> expedicoes = new ArrayList<>();
	private List<ControleExpedicao> selectExpedicoes = new ArrayList<>();
	private List<Pedido> pedidos = new ArrayList<>();
	private @Inject ControleExpedicao controleExpedicao;
	private Filtro filtro = new Filtro();
	private @Inject ControleExpedicaoService service;
	private @Inject SolicitacaoPagamentoService pagamentoService;
	private Long idPedido;
	private String tipoGestao = "";
	private List<Gestao> gestoes;
	private String local;
	private List<Estado> estados;
	private List<Localidade> localidades = new ArrayList<Localidade>();
	private Long idEstado;
	private List<Fornecedor> fornecedores = new ArrayList<>();
	private List<Colaborador> solicitantes = new ArrayList<>();
	private Fornecedor fornecedor = new Fornecedor();
	private Colaborador solicitante = new Colaborador();
	private @Inject TermoExpedicao termo;

	private MenuLateral menu = new MenuLateral();

	public ControleExpedicaoController() {
	}

	@PostConstruct
	public void init() {
		filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		filtro.setDataFinal(DataUtil.getDataFinal(new Date()));
		carregarExpedicoes();
	}

	public void salvarControleExpedicao() {
		service.salvar(controleExpedicao);
		init();
	}

	public boolean verificaPedidoTermo() {
		boolean existePedido = false;

		// for (ControleExpedicao c : selectExpedicoes) {
		for (Pedido c : termo.getPedidos()) {
			Pedido pedidoAuxiliar = service.getPedido(c.getId());// service.getPedido(c.getPedido().getId());
			if (pedidoAuxiliar.getTermo() != null) {
				// FacesMessage message = new
				// FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Pedido
				// "+pedidoAuxiliar.getId()+" já consta em outro termo de
				// número: "+pedidoAuxiliar.getTermo().getId());
				// RequestContext.getCurrentInstance().showMessageInDialog(message);
				addMessage("", "Pedido " + pedidoAuxiliar.getId() + " já consta em outro termo de número: "
						+ pedidoAuxiliar.getTermo().getId(), FacesMessage.SEVERITY_INFO);
				pedidoAuxiliar = new Pedido();
				existePedido = true;
			}
		}

		return existePedido;
	}
	
	public void dialogHelp() {
		HashMap<String, Object> options = new HashMap<>();
		options.put("width", "100%");
		options.put("height", "100%");
		options.put("contentWidth", "100%");
		options.put("contentHeight", "100%");
		options.put("resizable", false);
		PrimeFaces.current().dialog().openDynamic("dialog/help/help_controle_expedicao", options, null);
	}
	
	public List<Gestao> completeGestao(String query) {
		return service.getGestaoAutoComplete(query);
	}
	
	public List<Localidade> completeLocalidade(String s) {
		if (s.length() > 2)
			return pagamentoService.buscaLocalidade(s);

		return new ArrayList<Localidade>();
	}

	public void removerPedidoDoTermo(int index) {
		Pedido pedido = termo.getPedidos().get(index);
		termo.getPedidos().remove(index);
		selectExpedicoes.remove(index);
	}

	public void salvarTermo() {
		if (!verificaPedidoTermo()) {
			termo.setDataEmissao(new Date());
			service.salvarTermo(termo);
			termo = new TermoExpedicao();
			closeDialogLoading();
			closeDialogTermo();
			addMessage("", "Termo salvo com sucesso!", FacesMessage.SEVERITY_INFO);
		} else {
			closeDialogLoading();
			// closeDialogTermo();
			return;
		}
		init();

		// addMessage("", "Em desenvolvimento, Contate o Administrador do
		// sistema.", FacesMessage.SEVERITY_INFO);
	}

	public String listaItens(List<ItemPedido> itens) {
		StringBuilder str = new StringBuilder();

		int cont = 0;

		for (ItemPedido itemPedido : itens) {
			if (cont == 0) {
				str.append(itemPedido.getDescricaoProduto());
			} else {
				str.append(", ");
				str.append(itemPedido.getDescricaoProduto());
			}

			cont++;
		}

		return str.toString();

	}

	public void prepararTermo() {

		termo = new TermoExpedicao();
		termo.setPedidos(new ArrayList<>());

		for (ControleExpedicao c : selectExpedicoes) {

			Pedido p = service.getPedido(c.getIdPedido());
			p.setTermo(termo);
			// c.getPedido().setTermo(termo);
			termo.getPedidos().add(p);
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

	public void onLocalChangeFilter() {
		local = "";
		if (filtro.getTipoLocalidade() != null) {
			local = filtro.getTipoLocalidade().getNome();
			if (local.equals("Municipio")) {
				estados = new ArrayList<>();
				estados = service.getEstados(new Filtro());
				localidades = new ArrayList<>();
				idEstado = new Long(0);
			} else {
				LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
				localidades = filtro.getTipoLocalidade().getLocalidade(repo);
			}
		}
	}

	public LocalizacaoPedido[] getLocalizacoesPedidos() {
		return LocalizacaoPedido.values();
	}

	public List<Fornecedor> completeFornecedor(String query) {
		fornecedores = new ArrayList<Fornecedor>();
		fornecedores = service.getFornecedores(query);

		return fornecedores;
	}

	public List<Colaborador> completeSolicitante(String query) {
		solicitantes = new ArrayList<Colaborador>();
		solicitantes = service.getSolicitantes(query);
		return solicitantes;
	}

	public TipoLocalidade[] getTipoLocais() {
		return TipoLocalidade.values();
	}

	public void onEstadoChange() {
		localidades = service.getMunicipioByEstado(idEstado);
		idEstado = new Long(0);
	}

	public void filtrar() {
		if (fornecedor != null && fornecedor.getId() != null) {
			filtro.setFornecedorID(fornecedor.getId());
		} else {
			filtro.setFornecedorID(null);
		}

		if (solicitante != null && solicitante.getId() != null) {
			filtro.setSolicitanteID(solicitante.getId());
		} else {
			filtro.setSolicitanteID(null);
		}

		carregarExpedicoes();
		closeDialogLoading();
	}

	public void limparFiltro() {
		local = "";
		tipoGestao = "";
		filtro = new Filtro();
		filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		filtro.setDataFinal(DataUtil.getDataFinal(new Date()));
		solicitante = new Colaborador();
		fornecedor = new Fornecedor();
	}

	public void onLocalChangeForProject() {
		local = "";
		if (termo.getTipoLocalidade() != null) {
			local = termo.getTipoLocalidade().getNome();
			if (local.equals("Municipio")) {
				estados = new ArrayList<>();
				estados = service.getEstados(new Filtro());
				localidades = new ArrayList<>();
				idEstado = new Long(0);
			} else {
				LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
				localidades = termo.getTipoLocalidade().getLocalidade(repo);
			}
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

		if(termo.getAprovador().getNome().equals("Luiz Villarez")){
			aprovador = "Superintendente Administrativo-Financeiro, Sr. LUIZ CRUZ VILLARES, brasileiro, casado, administrador de empresas, portador da cédula de identidade nº 8.882.839-SSP/SP, inscrito no CPF/MF sob nº 066.535.688-90, residente e domiciliado em São Paulo-SP, na rua Almeida Garret";
			aprovadorAss = "LUIZ CRUZ VILLARES";
			sup = "Superintendente Administrativo-Financeiro";
		}else{
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

	public void prepararControleExpedicao() {
		controleExpedicao = service.findControleById(controleExpedicao.getId());
	}

	private void closeDialogLoading() {
		PrimeFaces.current().executeScript("PF('statusDialog').hide();");
	}

	private void closeDialogTermo() {
		PrimeFaces.current().executeScript("PF('dlg_novo_termo').hide();");
	}

	public TipoTermoExpedicao[] getTiposTermos() {
		return TipoTermoExpedicao.values();
	}

	public TipoAprovador[] getAprovadores() {
		return TipoAprovador.values();
	}

	public void carregarExpedicoes() {
		expedicoes = service.getExpedicoes(filtro);
	}

	public LocalizacaoPedido[] getLocalizacoes() {
		return LocalizacaoPedido.values();
	}

	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuLogistica();
	}

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public List<ControleExpedicao> getExpedicoes() {
		return expedicoes;
	}

	public void setExpedicoes(List<ControleExpedicao> expedicoes) {
		this.expedicoes = expedicoes;
	}

	public ControleExpedicao getControleExpedicao() {
		return controleExpedicao;
	}

	public void setControleExpedicao(ControleExpedicao controleExpedicao) {
		this.controleExpedicao = controleExpedicao;
	}

	public TipoGestao[] getTiposGestao() {
		return TipoGestao.values();
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

	public Long getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

	public void onRowSelect(SelectEvent event) {
		ControleExpedicao controle = ((ControleExpedicao) event.getObject());
		Pedido pedido = controle.getPedido();
		if (pedido.getTermo() != null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Pedido já foi consta em um termo de número: " + pedido.getTermo().getId());
			FacesContext.getCurrentInstance().addMessage(null, message);

		}

	}

	public void onRowUnselect(UnselectEvent event) {
		ControleExpedicao controle = ((ControleExpedicao) event.getObject());
	}

	private @Inject UsuarioSessao usuarioSessao;
	private @Inject CompraService compraService;

	public void imprimirPedido() {

		Pedido pedido = service.getPedido(idPedido);

		List<ItemPedido> itens = service.getItensPedidosByPedido(pedido);

		List<LancamentoAcao> acoes = service.getLancamentosDoPedido(pedido);

		StringBuilder acs = new StringBuilder();

		// for (LancamentoAcao lancamentoAcao : acoes) {
		// acs.append(lancamentoAcao.getAcao().getCodigo()+": "+
		// getFormatacao(lancamentoAcao.getValor())+"\n");
		// }

		if (pedido.getCompra().getVersionLancamento() != null) {
			if (pedido.getCompra().getVersionLancamento() != null
					|| pedido.getCompra().getVersionLancamento().equals("MODE01")) {
				for (LancamentoAcao lancamentoAcao : acoes) {
					acs.append(lancamentoAcao.getProjetoRubrica().getProjeto().getNome());
					acs.append("//");
					acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getTitulo());
					acs.append("//");
					acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getRubrica().getNome());
					acs.append("\n");
				}
			} else {
				for (LancamentoAcao lancamentoAcao : acoes) {
					acs.append(lancamentoAcao.getAcao().getCodigo() + " \n");
				}
			}
		} else {
			for (LancamentoAcao lancamentoAcao : acoes) {
				acs.append(lancamentoAcao.getAcao().getCodigo() + " \n");
			}
		}

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
		parametros.put("condicoes", pedido.getCondicaoPagamento());

		parametros.put("subtotal", pedido.getValorTotalSemDesconto());
		parametros.put("desconto", pedido.getValorTotalSemDesconto().subtract(pedido.getValorTotalComDesconto()));
		parametros.put("total", pedido.getValorTotalComDesconto());

		parametros.put("usuario",
				usuarioSessao.getNomeUsuario() + " " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
		parametros.put("pedidoAux", pedido.getCompra().getId() + "/" + pedido.getId());
		parametros.put("pedido", pedido.getId());
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

		JRDataSource dataSource = new JRBeanCollectionDataSource(itens);
		;

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/pedido_compra.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReport("Pedido", "PC" + pedido.getId().toString(), report, parametros, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getFormatacao(BigDecimal total) {
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(total);
		// return new DecimalFormat("###,###.###").format(total);
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

	public Long getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Long idEstado) {
		this.idEstado = idEstado;
	}

	public List<Fornecedor> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Colaborador getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Colaborador solicitante) {
		this.solicitante = solicitante;
	}

	public List<ControleExpedicao> getSelectExpedicoes() {
		return selectExpedicoes;
	}

	public void setSelectExpedicoes(List<ControleExpedicao> selectExpedicoes) {
		this.selectExpedicoes = selectExpedicoes;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public TermoExpedicao getTermo() {
		return termo;
	}

	public void setTermo(TermoExpedicao termo) {
		this.termo = termo;
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
