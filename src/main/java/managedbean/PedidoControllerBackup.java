package managedbean;

import java.io.Serializable;

/*@Named(value = "pedidoControllerBackup")
@ViewScoped*/
public class PedidoControllerBackup implements Serializable {

	/*private static final long serialVersionUID = 1L;
	
	private @Inject Compra compra;
	private @Inject PedidoService pedidoService;
	private @Inject UsuarioSessao usuarioSessao;
	private @Inject Pedido pedido;
	private MenuLateral menu = new MenuLateral();
	private List<CotacaoAuxiliar> cotacoesAuxiliares = new ArrayList<>();
	private Long idFornecedor;
	private List<Pedido> pedidos = new ArrayList<Pedido>();

	
	@PostConstruct
	public void carregarPedidos() throws IOException{
	
		compra =  (Compra) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("compra"+usuarioSessao.getNomeUsuario());
		
		if(compra == null || compra.getId() == null){
			//FacesContext.getCurrentInstance().getExternalContext().getFlash().put("compra"+usuarioSessao.getNomeUsuario(), cotacoes.get(0).getCompra());
			HttpServletResponse resp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			resp.sendRedirect(req.getContextPath() + "/main/compras.xhtml"); 
			return;
		}
		pedidos = pedidoService.getPedidos(compra);
	}
	
	
	public void imprimirPedido(){
		 
		  List<ItemPedido> itens = pedidoService.getItensPedidosByPedido(pedido);
			  
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
		  parametros.put("condicoes", pedido.getCondicaoPagamento());
		  
		  parametros.put("subtotal", pedido.getValorTotalSemDesconto());
		  parametros.put("desconto", pedido.getValorTotalSemDesconto().subtract(pedido.getValorTotalComDesconto()));
		  parametros.put("total", pedido.getValorTotalComDesconto());
		  
		  parametros.put("usuario", usuarioSessao.getNomeUsuario()+" "+new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
		  parametros.put("pedido", pedido.getId());
		  parametros.put("observ", "");
	      JRDataSource dataSource = new JRBeanCollectionDataSource(itens);;
			
		  try {
				JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/pedido_compra.jrxml");
				JasperReport report = JasperCompileManager.compileReport(jd);
				ReportUtil.openReport("Pedido","PC" + compra.getCodigo(), report, parametros, dataSource);	
		  } catch (Exception e) {
				e.printStackTrace();
		 }

	}
	
	
	
	public String prepararPagamentoPedido() {
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("pedido"+usuarioSessao.getNomeUsuario(), pedido);
		return "pedidoPagamento?faces-redirect=true";
	}

	public String redirecionarPageCadastroPedido(){
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("compra"+usuarioSessao.getNomeUsuario(), compra);
		return "pedidoCadastro?faces-redirect=true";
	}
	
	public String redirecionarPageCompras(){
		compra = null;
		return "compras?faces-redirect=true";
	}
	
	public String editarCotacao(){
		Fornecedor fornecedor = cotacaoService.getFornecedorById(idFornecedor);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("compra"+usuarioSessao.getNomeUsuario(), compra);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("fornecedor"+usuarioSessao.getNomeUsuario(), fornecedor);
		
		return "cotacaoCadastro?faces-redirect=true";
	}
	
	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public List<Fornecedor> getFornecedor(){
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

		return lista;
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
	
*/	
}
