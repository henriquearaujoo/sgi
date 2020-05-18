package managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Acao;
import model.CotacaoAuxiliar;
import model.FontePagadora;
import model.Fornecedor;
import model.LancamentoAcao;
import model.MenuLateral;
import model.Pedido;
import model.TipoLancamento;
import service.PedidoService;
import util.MakeMenu;
import util.UsuarioSessao;

@Named(value = "pedidoPagamentoController")
@ViewScoped
public class PedidoPagamentoController implements Serializable {

	private static final long serialVersionUID = 1L;

	private @Inject PedidoService pedidoService;
	private @Inject UsuarioSessao usuarioSessao;
	private @Inject Pedido pedido;
	private @Inject LancamentoAcao lancamentoAcao;

	private MenuLateral menu = new MenuLateral();
	private List<CotacaoAuxiliar> cotacoesAuxiliares = new ArrayList<>();
	private Long idFornecedor;
	private List<Pedido> pedidos = new ArrayList<Pedido>();

	private List<FontePagadora> allFontes = new ArrayList<FontePagadora>();
	private List<Acao> allAcoes = new ArrayList<Acao>();


	@PostConstruct
	public void carregarPedidos() throws IOException {
		pedido = (Pedido) FacesContext.getCurrentInstance().getExternalContext().getFlash()
				.get("pedido" + usuarioSessao.getNomeUsuario());
		pedido.setLancamentosAcoes(new ArrayList<LancamentoAcao>());
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

	public List<FontePagadora> completeFonte(String query) {
		allFontes = new ArrayList<FontePagadora>();
		allFontes = pedidoService.fontesAutoComplete(query);
		return allFontes;
	}

	public List<Acao> completeAcoes(String query) {
		allAcoes = new ArrayList<Acao>();
		allAcoes = pedidoService.acoesAutoComplete(query);
		return allAcoes;
	}

	public void adicionarNovaAcao() {
		lancamentoAcao.setTipo(TipoLancamento.NEUTRO);
		lancamentoAcao.setValor(BigDecimal.ZERO);
		if (pedido.getLancamentosAcoes().stream()
				.filter(ac -> ac.getAcao().getCodigo().equals(lancamentoAcao.getAcao().getCodigo())
						&& ac.getFontePagadora().getNome().equals(lancamentoAcao.getFontePagadora().getNome()))
				.findAny().isPresent()) {

			pedido.getLancamentosAcoes().remove(pedido.getLancamentosAcoes().stream()
					.filter(ac -> ac.getAcao().getCodigo().equals(lancamentoAcao.getAcao().getCodigo())
							&& ac.getFontePagadora().getNome().equals(lancamentoAcao.getFontePagadora().getNome())).findAny().get());
		}

		lancamentoAcao.setLancamento(pedido);
		pedido.getLancamentosAcoes().add(lancamentoAcao);
		lancamentoAcao = new LancamentoAcao();

	}
	
	
	public void removerAcao() {

		pedido.getLancamentosAcoes().remove(pedido.getLancamentosAcoes().stream()
				.filter(ac -> ac.getAcao().getCodigo().equals(lancamentoAcao.getAcao().getCodigo())
						&& ac.getFontePagadora().getNome().equals(lancamentoAcao.getFontePagadora().getNome())).findAny().get());
		lancamentoAcao = new LancamentoAcao();
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

}
