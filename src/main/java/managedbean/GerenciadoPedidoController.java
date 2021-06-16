package managedbean;

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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import model.Estado;
import model.Gestao;
import model.ItemPedido;
import model.LancamentoAcao;
import model.Localidade;
import model.MenuLateral;
import model.Pedido;
import model.StatusCompra;
import model.TipoGestao;
import model.TipoLocalidade;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import repositorio.GestaoRepositorio;
import repositorio.LocalRepositorio;
import service.CompraService;
import service.GerenciadorPedidoService;
import service.PedidoService;
import util.CDILocator;
import util.Filtro;
import util.MakeMenu;
import util.ReportUtil;
import util.UsuarioSessao;
import util.Util;


@ViewScoped
@Named(value = "gerenciador_pedido_controller")
public class GerenciadoPedidoController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Filtro filtro = new Filtro();
	private @Inject Pedido pedido;
	private List<Pedido> pedidos = new ArrayList<>();
	private @Inject GerenciadorPedidoService gerenciadorService;
	private @Inject PedidoService pedidoService;
	private @Inject UsuarioSessao usuarioSessao;
	private String tipoGestao = "";
	private List<Gestao> gestoes;
	private String local = "";
	
	private Long idEstado;
	
	private List<Estado> estados;
	
	private List<Localidade> localidades = new ArrayList<Localidade>();
	private List<ItemPedido> itensPedidos = new ArrayList<ItemPedido>();
	
	private MenuLateral menu = new MenuLateral();
	
	
	@PostConstruct
	public void init(){
		carregarPedidos();
		carregarItensPedido();
	}
	
	
	public void filtrar(){
		carregarPedidos();
	}
	
	public void filtrarItens(){
		carregarItensPedido();
	}
	
	public void carregarPedidos(){
		pedidos = gerenciadorService.getPedidos(filtro);
	}
	
	
	public void carregarItensPedido(){
		itensPedidos = gerenciadorService.getItensPedidos(filtro);
	}
	
	
	public StatusCompra[] listaStatusCompras(){
		return StatusCompra.values();
	}
	
	public void limparFiltro() {
		filtro = new Filtro();
		local = "";
		tipoGestao = "";
	}
	
	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuCompra();
	}
	
	public String getFormatacao(BigDecimal total) {
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(total);
		// return new DecimalFormat("###,###.###").format(total);
	}
	
	public void editarPedido(Pedido pedido){

		gerenciadorService.editarPedido(pedido);
	}
	
	public void onEstadoChange() {
		localidades = gerenciadorService.getMunicipioByEstado(idEstado);
		idEstado = new Long(0);
	}
	
	public TipoLocalidade[] getTipoLocais() {
		return TipoLocalidade.values();
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
				estados = gerenciadorService.getEstados(new Filtro());
				localidades = new ArrayList<>();
				idEstado = new Long(0);
			} else {
				LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
				localidades = filtro.getTipoLocalidade().getLocalidade(repo);
			}
		}
	}
	
	public TipoGestao[] getTiposGestao() {
		return TipoGestao.values();
	}
	
	
	private @Inject CompraService compraService;
	public void imprimirPedido(){
		
		
		  pedido = pedidoService.getPedidoById(pedido.getId());
		 
		  List<ItemPedido> itens = pedidoService.getItensPedidosByPedido(pedido);
		  
		  List<LancamentoAcao> acoes = pedidoService.getLancamentosDoPedido(pedido);
			
		  StringBuilder acs = new StringBuilder();
			
			for (LancamentoAcao lancamentoAcao : acoes) {
				acs.append(lancamentoAcao.getAcao().getCodigo()+": "+ getFormatacao(lancamentoAcao.getValor())+"\n");
			}
			  
		  ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	      HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	      String path = request.getSession().getServletContext().getRealPath( "/" );
	      String logo = path +"resources/image/logoFas.gif";
	      String imgAprovacao = path +"resources/image/assinatura_aprovacao_lv.jpg";	
	      
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
		  parametros.put("data_entrega",new SimpleDateFormat("dd/MM/yyyy").format(pedido.getDataEntrega()));
		  parametros.put("data_pagamento",new SimpleDateFormat("dd/MM/yyyy").format(pedido.getDataPagamento()));
		  parametros.put("cnpj_fornecedor", pedido.getFornecedor().getCnpj());
		  parametros.put("solicitante",pedido.getCompra().getSolicitante().getNome());
		  parametros.put("condicoes", pedido.getCondicaoPagamentoEnum().getNome());
		  
		  parametros.put("subtotal", pedido.getValorTotalSemDesconto());
		  parametros.put("desconto", pedido.getValorTotalSemDesconto().subtract(pedido.getValorTotalComDesconto()));
		  parametros.put("total", pedido.getValorTotalComDesconto());
		  
		  parametros.put("usuario", usuarioSessao.getNomeUsuario()+" "+new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
		  
		  String pedidoAux = pedido.getCompra().getId()+"/"+pedido.getId();
		  parametros.put("pedido", pedido.getId());
		  parametros.put("pedidoAux", pedidoAux);
		  parametros.put("observ", pedido.getObservacao());
		  
		  parametros.put("acao", acs.toString());
		  
		  Util util = new Util();
			
			parametros.put("data_emissao", new SimpleDateFormat("dd/MM/yyyy hh:mm").format(pedido.getDataEmissao()));
			
			if (pedido.getTipoGestao().getNome().equals("Regional")) {
				//setarParamRegional(parametros);
				util.setarParamRegional(parametros, pedido);
			}
			
			if (pedido.getTipoGestao().getNome().equals("Coordenadoria")) {
				//setarParamCoordenadoria(parametros);
				util.setarParamCoordenadoria(parametros, pedido);
			}
			
			if (pedido.getTipoGestao().getNome().equals("Superintendencia")) {
				//setarParamSuperintendencia(parametros);
				util.setarParamSuperintendencia(parametros, pedido);
			}
			
			if (pedido.getTipoLocalidade().getNome().equals("Uc")) {
				//setarDestinoUC(parametros);
				util.setarDestinoUC(parametros, pedido, compraService);
			}else if(pedido.getTipoLocalidade().getNome().equals("Comunidade")){
				//setarDestinoComunidade(parametros);
				util.setarDestinoComunidade(parametros, pedido);
			}else{
				//setarDestino(parametros);
				util.setarDestino(parametros, pedido);
			}
		  
	      JRDataSource dataSource = new JRBeanCollectionDataSource(itens);;
			
		  try {
				JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/pedido_compra.jrxml");
				JasperReport report = JasperCompileManager.compileReport(jd);
				ReportUtil.openReport("Pedido","PC" + pedido.getId().toString(), report, parametros, dataSource);	
		  } catch (Exception e) {
				e.printStackTrace();
		 }

	}

	
	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}
	
	public GerenciadoPedidoController(){}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}


	public MenuLateral getMenu() {
		return menu;
	}


	public void setMenu(MenuLateral menu) {
		this.menu = menu;
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


	public List<Estado> getEstados() {
		return estados;
	}


	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}


	public String getLocal() {
		return local;
	}


	public void setLocal(String local) {
		this.local = local;
	}


	public Long getIdEstado() {
		return idEstado;
	}


	public void setIdEstado(Long idEstado) {
		this.idEstado = idEstado;
	}


	public List<Localidade> getLocalidades() {
		return localidades;
	}


	public void setLocalidades(List<Localidade> localidades) {
		this.localidades = localidades;
	}


	public List<ItemPedido> getItensPedidos() {
		return itensPedidos;
	}


	public void setItensPedidos(List<ItemPedido> itensPedidos) {
		this.itensPedidos = itensPedidos;
	}
	
	
	
	
	
	
}
