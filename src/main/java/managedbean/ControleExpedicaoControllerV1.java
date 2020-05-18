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
import service.ControleExpedicaoService;
import util.CDILocator;
import util.DataUtil;
import util.Filtro;
import util.MakeMenu;
import util.ReportUtil;
import util.UsuarioSessao;


@ViewScoped
@Named(value = "controle_expedicao_controller_new")
public class ControleExpedicaoControllerV1 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private List<ControleExpedicao> expedicoes = new ArrayList<>();
	
	private Filtro filtro = new Filtro();

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
	private Colaborador solicitante  = new Colaborador();
	
	private List<Pedido> listaDePedidos = new ArrayList<>();
	
	private @Inject ControleExpedicaoService service;
	private @Inject ControleExpedicao controleExpedicao;
	

	private MenuLateral menu = new MenuLateral();

	public ControleExpedicaoControllerV1(){}
	
	@PostConstruct
	public void init(){
		filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		filtro.setDataFinal(DataUtil.getDataFinal(new Date()));
		carregarExpedicoes();
	}
	
	public void carregarPedidos(){
		listaDePedidos = service.buscarPedidos(filtro);
	}
	
	public void salvarControleExpedicao(){
		service.salvar(controleExpedicao);
		init();
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
	
	public LocalizacaoPedido[] getLocalizacoesPedidos(){
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
	
	public void filtrar(){
		if (fornecedor != null && fornecedor.getId() != null) {
			filtro.setFornecedorID(fornecedor.getId());
		}else{
			filtro.setFornecedorID(null);
		}
		
		if(solicitante != null && solicitante.getId() != null){
			filtro.setSolicitanteID(solicitante.getId());
		}else{
			filtro.setSolicitanteID(null);
		}
		
		carregarExpedicoes();
	}
	
	
	public void limparFiltro(){
		local = "";
		tipoGestao = "";
		filtro = new Filtro();
		filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		filtro.setDataFinal(DataUtil.getDataFinal(new Date()));
		solicitante = new Colaborador();
		fornecedor = new Fornecedor();
	}
	
	
	public void carregarExpedicoes(){
		expedicoes = service.getExpedicoes(filtro);
	}
	
	public LocalizacaoPedido[] getLocalizacoes(){
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
	
	
	private @Inject UsuarioSessao usuarioSessao;
	
	
	public void imprimirPedido(){
		 
		  Pedido pedido = service.getPedido(idPedido);
		
		  List<ItemPedido> itens = service.getItensPedidosByPedido(pedido);
		  
		  List<LancamentoAcao> acoes = service.getLancamentosDoPedido(pedido);
			
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
		  parametros.put("banco", pedido.getFornecedor().getBanco());
		  parametros.put("fone", pedido.getFornecedor().getTelefone());
		  parametros.put("agencia", pedido.getFornecedor().getAgencia());
		  parametros.put("conta", pedido.getFornecedor().getConta());
		  parametros.put("data_entrega",new SimpleDateFormat("dd/MM/yyyy").format(pedido.getDataEntrega()));
		  parametros.put("data_pagamento",new SimpleDateFormat("dd/MM/yyyy").format(pedido.getDataPagamento()));
		  parametros.put("cnpj_fornecedor", pedido.getFornecedor().getCnpj());
		  parametros.put("solicitante",pedido.getCompra().getSolicitante().getNome());
		  parametros.put("condicoes", pedido.getCondicaoPagamento());
		  
		  parametros.put("subtotal", pedido.getValorTotalSemDesconto());
		  parametros.put("desconto", pedido.getValorTotalSemDesconto().subtract(pedido.getValorTotalComDesconto()));
		  parametros.put("total", pedido.getValorTotalComDesconto());
		  
		  parametros.put("usuario", usuarioSessao.getNomeUsuario()+" "+new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
		  parametros.put("pedido", pedido.getId());
		  parametros.put("observ", pedido.getObservacao());
		  
		  parametros.put("acao", acs.toString());
		  
	      JRDataSource dataSource = new JRBeanCollectionDataSource(itens);;
			
		  try {
				JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/pedido_compra.jrxml");
				JasperReport report = JasperCompileManager.compileReport(jd);
				ReportUtil.openReport("Pedido","PC" +pedido.getId().toString(), report, parametros, dataSource);	
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
	
}

