package managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.weld.context.RequestContext;
import org.primefaces.component.datatable.DataTable;

import exception.NegocioException;
import model.Compra;
import model.CondicaoPagamento;
import model.ContaBancaria;
import model.ControleExpedicao;
import model.Cotacao;
import model.CotacaoAuxiliar;
import model.Fornecedor;
import model.Gestao;
import model.ItemPedido;
import model.MenuLateral;
import model.Pedido;
import model.TipoParcelamento;
import model.User;
import service.CotacaoService;
import service.PedidoService;
import util.MakeMenu;
import util.UsuarioSessao;

@Named(value = "pedidoCadController")
@ViewScoped
public class PedidoCadastroController implements Serializable {

	private static final long serialVersionUID = 1L;

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

	private ItemPedido itemPedido = new ItemPedido();
	private List<Fornecedor> fornecedores = new ArrayList<Fornecedor>();
	private List<Cotacao> cotacoes = new ArrayList<Cotacao>();
	private List<Cotacao> selectedCotacoes = new ArrayList<Cotacao>();
	private List<CotacaoAuxiliar> cotacoesAuxiliares = new ArrayList<>();
	private List<ItemPedido> itensPedidos = new ArrayList<ItemPedido>();
	private List<ItemPedido> itensPedidosSelected = new ArrayList<ItemPedido>();
	private List<Gestao> listGestao = new ArrayList<>();

	private @Inject ControleExpedicao controleExpedicao;

	public PedidoCadastroController() {
	}

	public void excluirItemTypeA() {
		removerItemTypeA();
	}

	public void excluirItemTypeB() {
		//desistência parcial pelo solicitante (Cancelar itens do pedido e da SC), quantidade para cancelamento.
		for (ItemPedido it : itensPedidosSelected) {

		}

	}

//	public void carregarPedidos() throws IOException {
//		// pedido = (Pedido)
//		// FacesContext.getCurrentInstance().getExternalContext().getFlash().get("pedido"+usuarioSessao.getNomeUsuario());
//
//		if (pedido == null || pedido.getId() == null) {
//			HttpServletResponse resp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
//					.getResponse();
//			HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
//					.getRequest();
//			resp.sendRedirect(req.getContextPath() + "/main/compras.xhtml");
//			return;
//		}
//
//		/*
//		 * fornecedores = pedidoService.getFornecedoresParaSelectBox(compra);
//		 * pedido.setCompra(compra);
//		 * pedido.setTipoGestao(compra.getTipoGestao());
//		 * pedido.setGestao(compra.getGestao());
//		 * pedido.setTipoLocalidade(compra.getTipoLocalidade());
//		 * pedido.setLocalidade(compra.getLocalidade());
//		 */
//
//		listGestao = pedidoService.buscarGestao();
//
//		fornecedores.add(pedido.getFornecedor());
//		pedido.setItensPedidos(new ArrayList<>());
//		pedido.setItensPedidos(pedidoService.getItensPedido(pedido));
//		pedido.setTotalDeDesonto(pedido.getValorTotalSemDesconto().subtract(pedido.getValorTotalComDesconto()));
//
//		controleExpedicao = pedidoService.findControleExpedByPedido(pedido.getId());
//
//		if (controleExpedicao.getId() == null) {
//			controleExpedicao = new ControleExpedicao();
//			controleExpedicao.setNomeResponsavel("");
//		}
//
//	}

	public void carregarPedidos() throws IOException {
		if (this.pedido == null || this.pedido.getId() == null) {
			final HttpServletResponse resp = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
			final HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			resp.sendRedirect(String.valueOf(req.getContextPath()) + "/main/compras.xhtml");
			return;
		}
		this.listGestao = (List<Gestao>)this.pedidoService.buscarGestao();
		this.fornecedores.add(this.pedido.getFornecedor());
		this.pedido.setItensPedidos((List)new ArrayList());
		this.pedido.setItensPedidos(this.pedidoService.getItensPedido(this.pedido));
		this.pedido.setTotalDeDesonto(this.pedido.getValorTotalSemDesconto().subtract(this.pedido.getValorTotalComDesconto()));
		this.controleExpedicao = this.pedidoService.findControleExpedByPedido(this.pedido.getId());
		if (this.controleExpedicao.getId() == null) {
			(this.controleExpedicao = new ControleExpedicao()).setNomeResponsavel("");
		}
	}

	private @Inject CotacaoService cotacaoService;

	public List<Fornecedor> completeFornecedor(String query) {
		fornecedores = new ArrayList<Fornecedor>();
		if (query.length() > 2)
			fornecedores = cotacaoService.getFornecedores(query);
		return fornecedores;
	}

	public void removerPedido() {
		// pedidoService.mudarStatusPedido(pedido, usuarioSessao.getUsuario());
		pedidoService.remover(pedido);
	}
	
	

	public boolean verificarPagamentos() {
		return pedidoService.verificarPagamentos(pedido.getId());
	}

	public void removerItem(int index) {

		User usuarioAuxiliar = usuarioSessao.getUsuario();

		if (usuarioAuxiliar.getPerfil().getDescricao().equals("compra")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")) {
			
			BigDecimal valorComDesconto = itemPedido.getValorUnitarioComDesconto();
			BigDecimal valorSemDesconto = itemPedido.getValorUnitario();
			Double quantidade = itemPedido.getQuantidade();

			BigDecimal valorTotalComDesconto = valorComDesconto.multiply(new BigDecimal(quantidade));
			BigDecimal valorTotalSemDesconto = valorSemDesconto.multiply(new BigDecimal(quantidade));

			pedido.setValorTotalComDesconto(pedido.getValorTotalComDesconto().subtract(valorTotalComDesconto));
			pedido.setValorTotalSemDesconto(pedido.getValorTotalSemDesconto().subtract(valorTotalSemDesconto));

			if (!verificarPagamentos()) {
				pedido.getItensPedidos().remove(index);
			}
			// pedidoService.salvar(pedido);

			return;
		}

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
				"Você não tem autorização pra esse tipo de ação, por favor contate o gestor de compras.");
		FacesContext.getCurrentInstance().addMessage(null, message);

		return;
	}
	
	public void removerItemTypeA() {

		User usuarioAuxiliar = usuarioSessao.getUsuario();

		if (usuarioAuxiliar.getPerfil().getDescricao().equals("compra")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")) {
			
			pedido = pedidoService.getPedidoById(pedido.getId());
			pedidoService.excluirItemPedidoTypeA(pedido, itensPedidosSelected, usuarioSessao.getUsuario());
			pedido.setItensPedidos(new ArrayList<>());
			pedido.setItensPedidos(pedidoService.getItensPedido(pedido));
			return;
		}

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
				"Você não tem autorização pra esse tipo de ação, por favor contate o gestor de compras.");
		FacesContext.getCurrentInstance().addMessage(null, message);

		return;
	}
	
	
	public void removerItemTypeB() {

		User usuarioAuxiliar = usuarioSessao.getUsuario();

		if (usuarioAuxiliar.getPerfil().getDescricao().equals("compra")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")) {
			
			pedido = pedidoService.getPedidoById(pedido.getId());
			pedido.setItensPedidos(new ArrayList<>());
			pedido.setItensPedidos(pedidoService.getItensPedido(pedido));
			pedidoService.excluirItemPedidoTypeB(pedido, itensPedidosSelected, usuarioSessao.getUsuario());
			pedido.setItensPedidos(new ArrayList<>());
			pedido.setItensPedidos(pedidoService.getItensPedido(pedido));
			pedidoService.atualizarStatus(pedido.getCompra());
			
			return;
		}

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
				"Você não tem autorização pra esse tipo de ação, por favor contate o gestor de compras.");
		FacesContext.getCurrentInstance().addMessage(null, message);

		return;
	}

	public CondicaoPagamento[] getCondicoes() {
		return CondicaoPagamento.values();
	}

	public void onChangeFornecedor() {
		if (idFornecedor != null) {
			Fornecedor fornecedor = pedidoService.getFornecedorById(idFornecedor);
			cotacoes = pedidoService.getCotacoes(compra, fornecedor);

			pedido.setFornecedor(fornecedor);
		} else {
			cotacoes = new ArrayList<Cotacao>();
		}
	}

	/*
	 * public String redirecionarPageCadastroPedido(){
	 * FacesContext.getCurrentInstance().getExternalContext().getFlash().put(
	 * "compra"+usuarioSessao.getNomeUsuario(), compra); return
	 * "pedidoCadastro?faces-redirect=true"; }
	 */

	public String salvar() throws NegocioException {
		//pedido.setDataEmissao(new Date());

		ContaBancaria conta = cotacaoService.getContaByFornecedor(pedido.getFornecedor().getId());

		if (conta == null) {
			throw new NegocioException(
					"Não existe uma conta para o fornecedor, por favor entre em contato com o adm para que seja efetuado o cadastro!");
		}

		pedido.setContaRecebedor(conta);

		if (controleExpedicao.getId() == null) {
			controleExpedicao.setDataAceitacaoExpedicao(new Date());
		}

		controleExpedicao.setPedido(pedido);

		pedido.setSolicitante(pedido.getCompra().getSolicitante());
		// pedido.setItensPedidos(itensPedidos);
		//pedido.setUsuarioGeradorPedido(usuarioSessao.getUsuario());
		pedidoService.salvar(pedido, controleExpedicao);
		// FacesContext.getCurrentInstance().getExternalContext().getFlash().put("compra"+usuarioSessao.getNomeUsuario(),
		// pedido.getCompra());
		return "pedidos?faces-redirect=true&amp;includeViewParams=true";
	}

	public TipoParcelamento[] getTiposParcelas() {
		return TipoParcelamento.values();
	}

	// salvar2 não está ativo
	/*
	 * public void salvar2(){ pedido.setDataEmissao(new Date());
	 * //pedido.setItensPedidos(itensPedidos); pedidoService.salvar(pedido);
	 * //FacesContext.getCurrentInstance().getExternalContext().getFlash().put(
	 * "compra"+usuarioSessao.getNomeUsuario(), pedido.getCompra()); //return
	 * "pedidos?faces-redirect=true&amp;includeViewParams=true"; }
	 */

	public void manterItemPedido() {

		Cotacao cot = (Cotacao) table.getRowData();
		ItemPedido itemPedido = new ItemPedido();

		if (cot.getHouvePedido() != null) {
			if (cot.getHouvePedido()) {
				cot.setHouvePedido(false);
				itensPedidos.remove(itensPedidos.stream().filter(it -> it.getIdCotacao() == cot.getId().longValue())
						.findAny().get());
				// pedido.getItensPedidos().remove(pedido.getItensPedidos().stream().filter(it
				// -> it.getCotacao().getId().longValue() ==
				// cot.getId().longValue()).findAny().get());
				selectedCotacoes.remove(selectedCotacoes.stream()
						.filter(s -> s.getId().longValue() == cot.getId().longValue()).findAny().get());
				totalSemDesconto = totalSemDesconto
						.subtract(cot.getValor().multiply(BigDecimal.valueOf(cot.getQuantidade())));
				totalComDesconto = totalComDesconto
						.subtract(cot.getValorDesconto().multiply(BigDecimal.valueOf(cot.getQuantidade())));
				totalDeDesconto = totalSemDesconto.subtract(totalComDesconto);

			} else {

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

				// pedido.getItensPedidos().add(itemPedido);
				itensPedidos.add(itemPedido);
				selectedCotacoes.add(cot);
				cot.setHouvePedido(true);

				totalSemDesconto = totalSemDesconto
						.add(cot.getValor().multiply(BigDecimal.valueOf(cot.getQuantidade())));
				totalComDesconto = totalComDesconto
						.add(cot.getValorDesconto().multiply(BigDecimal.valueOf(cot.getQuantidade())));
				totalDeDesconto = totalSemDesconto.subtract(totalComDesconto);

			}
		} else {

			itemPedido.setCotacao(cot);
			itemPedido.setIdCotacao(cot.getId());
			itemPedido.setPedido(pedido);
			itemPedido.setValorUnitario(cot.getValor());
			itemPedido.setValorUnitarioComDesconto(cot.getValorDesconto());
			itemPedido.setQuantidade(cot.getQuantidade());
			itemPedido.setDescricaoProduto(cot.getItemCompra().getProduto().getDescricao());
			// pedido.getItensPedidos().add(itemPedido);
			itemPedido.setTipoGestao(pedido.getTipoGestao());
			itemPedido.setGestao(pedido.getGestao());
			itemPedido.setTipoLocalidade(pedido.getTipoLocalidade());
			itemPedido.setLocalidade(pedido.getLocalidade());
			itemPedido.setTipo(cot.getItemCompra().getProduto().getTipo());

			itensPedidos.add(itemPedido);
			selectedCotacoes.add(cot);
			cot.setHouvePedido(true);

			totalSemDesconto = totalSemDesconto.add(cot.getValor().multiply(BigDecimal.valueOf(cot.getQuantidade())));
			totalComDesconto = totalComDesconto
					.add(cot.getValorDesconto().multiply(BigDecimal.valueOf(cot.getQuantidade())));
			totalDeDesconto = totalSemDesconto.subtract(totalComDesconto);

		}

		pedido.setValorTotalSemDesconto(totalSemDesconto);
		pedido.setValorTotalComDesconto(totalComDesconto);
		pedido.setTotalDeDesonto(totalDeDesconto);

	}

	public void manterItemPedidoOld() {

		Cotacao cot = (Cotacao) table.getRowData();
		ItemPedido itemPedido = new ItemPedido();

		if (cot.getHouvePedido() != null) {
			if (cot.getHouvePedido()) {
				cot.setHouvePedido(false);
				selectedCotacoes.remove(selectedCotacoes.stream()
						.filter(s -> s.getId().longValue() == cot.getId().longValue()).findAny().get());
				totalSemDesconto = totalSemDesconto
						.subtract(cot.getValor().multiply(BigDecimal.valueOf(cot.getQuantidade())));
				totalComDesconto = totalComDesconto
						.subtract(cot.getValorDesconto().multiply(BigDecimal.valueOf(cot.getQuantidade())));
				totalDeDesconto = totalSemDesconto.subtract(totalComDesconto);

			} else {
				selectedCotacoes.add(cot);
				cot.setHouvePedido(true);
				totalSemDesconto = totalSemDesconto
						.add(cot.getValor().multiply(BigDecimal.valueOf(cot.getQuantidade())));
				totalComDesconto = totalComDesconto
						.add(cot.getValorDesconto().multiply(BigDecimal.valueOf(cot.getQuantidade())));
				totalDeDesconto = totalSemDesconto.subtract(totalComDesconto);

			}
		} else {

			selectedCotacoes.add(cot);
			cot.setHouvePedido(true);
			totalSemDesconto = totalSemDesconto.add(cot.getValor().multiply(BigDecimal.valueOf(cot.getQuantidade())));
			totalComDesconto = totalComDesconto
					.add(cot.getValorDesconto().multiply(BigDecimal.valueOf(cot.getQuantidade())));
			totalDeDesconto = totalSemDesconto.subtract(totalComDesconto);

		}

		pedido.setValorTotalSemDesconto(totalSemDesconto);
		pedido.setValorTotalComDesconto(totalComDesconto);
		pedido.setTotalDeDesonto(totalDeDesconto);

	}

	public String redirecionarPagePedidos() {
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("compra" + usuarioSessao.getNomeUsuario(),
				compra);
		return "pedidos?faces-redirect=true";
	}

	public String editarCotacao() {
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

	public List<Gestao> getListGestao() {
		return listGestao;
	}

	public void setListGestao(List<Gestao> listGestao) {
		this.listGestao = listGestao;
	}

	public ControleExpedicao getControleExpedicao() {
		return controleExpedicao;
	}

	public void setControleExpedicao(ControleExpedicao controleExpedicao) {
		this.controleExpedicao = controleExpedicao;
	}

	public ItemPedido getItemPedido() {
		return itemPedido;
	}

	public void setItemPedido(ItemPedido itemPedido) {
		this.itemPedido = itemPedido;
	}

	public List<ItemPedido> getItensPedidosSelected() {
		return itensPedidosSelected;
	}

	public void setItensPedidosSelected(List<ItemPedido> itensPedidosSelected) {
		this.itensPedidosSelected = itensPedidosSelected;
	}

}
