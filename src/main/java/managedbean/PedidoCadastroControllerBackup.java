package managedbean;

import java.io.Serializable;

/*@Named(value = "pedidoCadControllerBackup")
@ViewScoped*/
public class PedidoCadastroControllerBackup implements Serializable {

	/*private static final long serialVersionUID = 1L;
	
	private @Inject Compra compra;
	private @Inject PedidoService pedidoService;
	private @Inject Cotacao cotacao;
	private @Inject UsuarioSessao usuarioSessao;
	private @Inject Pedido pedido;
	private MenuLateral menu = new MenuLateral();
	
	private Long idFornecedor;
	private List<Pedido> pedidos = new ArrayList<Pedido>();
	private DataTable table = new DataTable();
	
	private BigDecimal totalComDesconto = BigDecimal.ZERO;
	private BigDecimal totalSemDesconto = BigDecimal.ZERO;
	private BigDecimal totalDeDesconto = BigDecimal.ZERO;
	
	
	private List<Fornecedor> fornecedores = new ArrayList<Fornecedor>(); 
	private List<Cotacao> cotacoes = new ArrayList<Cotacao>();
	private List<Cotacao> selectedCotacoes = new ArrayList<Cotacao>();
	private List<CotacaoAuxiliar> cotacoesAuxiliares = new ArrayList<>();
	private List<ItemPedido> itensPedidos = new ArrayList<ItemPedido>();
	
	
	
	@PostConstruct
	public void carregarPedidos() throws IOException{
		compra =  (Compra) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("compra"+usuarioSessao.getNomeUsuario());
		if(compra == null || compra.getId() == null){
			HttpServletResponse resp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			resp.sendRedirect(req.getContextPath() + "/main/compras.xhtml"); 
			return;
		}
		
		fornecedores = pedidoService.getFornecedoresParaSelectBox(compra);
		pedido.setCompra(compra);
		pedido.setTipoGestao(compra.getTipoGestao());
		pedido.setGestao(compra.getGestao());
		pedido.setTipoLocalidade(compra.getTipoLocalidade());
		pedido.setLocalidade(compra.getLocalidade());
	}
	
	
	
	
	public void onChangeFornecedor(){
		if (idFornecedor != null) {
		Fornecedor fornecedor = pedidoService.getFornecedorById(idFornecedor);
		cotacoes = pedidoService.getCotacoes(compra,fornecedor);
		
		pedido.setFornecedor(fornecedor);
		}else{
			cotacoes = new ArrayList<Cotacao>();
		}
	}
	
	public String redirecionarPageCadastroPedido(){
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("compra"+usuarioSessao.getNomeUsuario(), compra);
		return "pedidoCadastro?faces-redirect=true";
	}
	
	public String salvar(){
		pedido.setDataEmissao(new Date());
		pedido.setItensPedidos(itensPedidos);
		pedidoService.salvar(pedido);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("compra"+usuarioSessao.getNomeUsuario(), pedido.getCompra());
		return "pedidoCadastro?faces-redirect=true";
	}
	
	public void manterItemPedido(){
		
	   Cotacao cot = (Cotacao) table.getRowData();
	   ItemPedido itemPedido = new ItemPedido();
	   
	   if(cot.getHouvePedido() != null){
		   if(cot.getHouvePedido()){
			   cot.setHouvePedido(false);
			   itensPedidos.remove(itensPedidos.stream().filter(it -> it.getIdCotacao() == cot.getId().longValue()).findAny().get());
			   //pedido.getItensPedidos().remove(pedido.getItensPedidos().stream().filter(it -> it.getCotacao().getId().longValue() == cot.getId().longValue()).findAny().get());
			   selectedCotacoes.remove(selectedCotacoes.stream().filter(s -> s.getId().longValue() == cot.getId().longValue()).findAny().get());
			   totalSemDesconto = totalSemDesconto.subtract(cot.getValor().multiply(BigDecimal.valueOf(cot.getQuantidade())));
			   totalComDesconto = totalComDesconto.subtract(cot.getValorDesconto().multiply(BigDecimal.valueOf(cot.getQuantidade())));
			   totalDeDesconto =  totalSemDesconto.subtract(totalComDesconto);
			  	   
		   }else{
			   
			   itemPedido.setCotacao(cot);
			   itemPedido.setIdCotacao(cot.getId());
			   itemPedido.setPedido(pedido);
			   itemPedido.setValorUnitario(cot.getValor());
			   itemPedido.setValorUnitarioComDesconto(cot.getValorDesconto());
			   itemPedido.setQuantidade(cot.getQuantidade());
			   itemPedido.setDescricaoProduto(cot.getItemCompra().getProduto().getDescricao());
			   itemPedido.setTipoGestao(pedido.getTipoGestao());
			   itemPedido.setGestao(pedido.getGestao());
			   itemPedido.setTipoLocalidade(pedido.getTipoLocalidade());
			   itemPedido.setLocalidade(pedido.getLocalidade());
			   itemPedido.setTipo(cot.getItemCompra().getProduto().getTipo());
			   
			   //pedido.getItensPedidos().add(itemPedido);
			   itensPedidos.add(itemPedido);
			   selectedCotacoes.add(cot);
			   cot.setHouvePedido(true);
			   
			   totalSemDesconto = totalSemDesconto.add(cot.getValor().multiply(BigDecimal.valueOf(cot.getQuantidade())));
			   totalComDesconto = totalComDesconto.add(cot.getValorDesconto().multiply(BigDecimal.valueOf(cot.getQuantidade())));
			   totalDeDesconto =  totalSemDesconto.subtract(totalComDesconto);
			   
		   }
	   }else{
		   
		   itemPedido.setCotacao(cot);
		   itemPedido.setIdCotacao(cot.getId());
		   itemPedido.setPedido(pedido);
		   itemPedido.setValorUnitario(cot.getValor());
		   itemPedido.setValorUnitarioComDesconto(cot.getValorDesconto());
		   itemPedido.setQuantidade(cot.getQuantidade());
		   itemPedido.setDescricaoProduto(cot.getItemCompra().getProduto().getDescricao());
		   //pedido.getItensPedidos().add(itemPedido);
		   itemPedido.setTipoGestao(pedido.getTipoGestao());
		   itemPedido.setGestao(pedido.getGestao());
		   itemPedido.setTipoLocalidade(pedido.getTipoLocalidade());
		   itemPedido.setLocalidade(pedido.getLocalidade());
		   itemPedido.setTipo(cot.getItemCompra().getProduto().getTipo());
		   
		   itensPedidos.add(itemPedido);
		   selectedCotacoes.add(cot);
		   cot.setHouvePedido(true);
		   
		   totalSemDesconto = totalSemDesconto.add(cot.getValor().multiply(BigDecimal.valueOf(cot.getQuantidade())));
		   totalComDesconto = totalComDesconto.add(cot.getValorDesconto().multiply(BigDecimal.valueOf(cot.getQuantidade())));
		   totalDeDesconto =  totalSemDesconto.subtract(totalComDesconto);
		   
	   }
	   
	   pedido.setValorTotalSemDesconto(totalSemDesconto);
	   pedido.setValorTotalComDesconto(totalComDesconto);
	   pedido.setTotalDeDesonto(totalDeDesconto);
		
	}
	
	public void manterItemPedidoOld(){
		
		   Cotacao cot = (Cotacao) table.getRowData();
		   ItemPedido itemPedido = new ItemPedido();
		   
		   if(cot.getHouvePedido() != null){
			   if(cot.getHouvePedido()){
				   cot.setHouvePedido(false);
				   selectedCotacoes.remove(selectedCotacoes.stream().filter(s -> s.getId().longValue() == cot.getId().longValue()).findAny().get());
				   totalSemDesconto = totalSemDesconto.subtract(cot.getValor().multiply(BigDecimal.valueOf(cot.getQuantidade())));
				   totalComDesconto = totalComDesconto.subtract(cot.getValorDesconto().multiply(BigDecimal.valueOf(cot.getQuantidade())));
				   totalDeDesconto =  totalSemDesconto.subtract(totalComDesconto);
				  	   
			   }else{
				   selectedCotacoes.add(cot);
				   cot.setHouvePedido(true);	   
				   totalSemDesconto = totalSemDesconto.add(cot.getValor().multiply(BigDecimal.valueOf(cot.getQuantidade())));
				   totalComDesconto = totalComDesconto.add(cot.getValorDesconto().multiply(BigDecimal.valueOf(cot.getQuantidade())));
				   totalDeDesconto =  totalSemDesconto.subtract(totalComDesconto);
				   
			   }
		   }else{
			   
			   selectedCotacoes.add(cot);
			   cot.setHouvePedido(true);
			   totalSemDesconto = totalSemDesconto.add(cot.getValor().multiply(BigDecimal.valueOf(cot.getQuantidade())));
			   totalComDesconto = totalComDesconto.add(cot.getValorDesconto().multiply(BigDecimal.valueOf(cot.getQuantidade())));
			   totalDeDesconto =  totalSemDesconto.subtract(totalComDesconto);
			   
		   }
		   
		   pedido.setValorTotalSemDesconto(totalSemDesconto);
		   pedido.setValorTotalComDesconto(totalComDesconto);
		   pedido.setTotalDeDesonto(totalDeDesconto);
			
		}
	
	public String redirecionarPagePedidos(){
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("compra"+usuarioSessao.getNomeUsuario(), compra);
		return "pedidos?faces-redirect=true";
	}
	
	public String editarCotacao(){
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

	public List<Cotacao> getCotacoes() {
		return cotacoes;
	}




	public void setCotacoes(List<Cotacao> cotacoes) {
		this.cotacoes = cotacoes;
	}


	public List<Fornecedor> getFornecedores() {
		return fornecedores;
	}


	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}


	public Cotacao getCotacao() {
		return cotacao;
	}


	public void setCotacao(Cotacao cotacao) {
		this.cotacao = cotacao;
	}


	public List<Cotacao> getSelectedCotacoes() {
		return selectedCotacoes;
	}


	public void setSelectedCotacoes(List<Cotacao> selectedCotacoes) {
		this.selectedCotacoes = selectedCotacoes;
	}




	public Pedido getPedido() {
		return pedido;
	}




	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}




	public DataTable getTable() {
		return table;
	}




	public void setTable(DataTable table) {
		this.table = table;
	}
*/	
	
}
