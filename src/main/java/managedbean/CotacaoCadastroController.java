package managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Compra;
import model.Cotacao;
import model.CotacaoAuxiliar;
import model.CotacaoParent;
import model.Fornecedor;
import model.ItemCompra;
import model.MenuLateral;
import service.CotacaoService;
import util.MakeMenu;
import util.UsuarioSessao;

@Named(value = "cotacaoCadastroController")
@ViewScoped
public class CotacaoCadastroController implements Serializable {

	private static final long serialVersionUID = 1L;

	private @Inject Compra compra;

	private @Inject CotacaoService cotacaoService;
	private @Inject Fornecedor fornecedor;
	private @Inject UsuarioSessao usuarioSessao;
	private @Inject CotacaoParent cotacaoParent;

	private MenuLateral menu = new MenuLateral();

	private List<CotacaoAuxiliar> cotacoesAuxiliares = new ArrayList<CotacaoAuxiliar>();
	private List<Cotacao> cotacoes = new ArrayList<Cotacao>();
	private List<Fornecedor> fornecedores = new ArrayList<Fornecedor>();

	private BigDecimal valorTotalDesconto = BigDecimal.ZERO;

	private BigDecimal valorTotalDescontoPorcentagem = BigDecimal.ZERO;

	public CotacaoCadastroController() {
	}

	// @PostConstruct
	public void carregarItens() throws IOException {
		iniciarNovaCotacao();
	}

	public String voltarListagem() {
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("compra" + usuarioSessao.getNomeUsuario(),
				cotacoes.get(0).getCompra());
		return "cotacao?faces-redirect=true";
	}

	private BigDecimal valorTotal = BigDecimal.ZERO;

	public void distribuirDesconto() {
		// System.out.println(valorTotalDesconto);
		// System.out.println(valorTotalDescontoPorcentagem);

		if (cotacaoParent.getValorTotalSemDesconto() != null)
			cotacaoParent.getValorTotalSemDesconto().setScale(2);
		if (cotacaoParent.getValorTotalComDesconto() != null)
			cotacaoParent.getValorTotalComDesconto().setScale(2);

		if (cotacaoParent.getValorTotalDesconto() != null) {

			if (cotacaoParent.getValorTotalDesconto().longValue() != BigDecimal.ZERO.longValue()) {
				distribuirDescontoTotalSobreUnitarios();
			} else {
				if (cotacaoParent.getValorTotalDescontoPorcentagem() == null)
					cotacaoParent.setValorTotalDescontoPorcentagem(BigDecimal.ZERO);
				distribuirPorcentagemTotalSobreUnitarios();
			}

		} else {
			if (cotacaoParent.getValorTotalDescontoPorcentagem() == null)
				cotacaoParent.setValorTotalDescontoPorcentagem(BigDecimal.ZERO);
			distribuirPorcentagemTotalSobreUnitarios();
		}

		// cotacaoParent.setValorTotalSemDesconto(valorTotalSemDesconto);
		// cotacaoParent.setValorTotalComDesconto(valorTotalComDesconto);

	}

	private BigDecimal valorTotalComDesconto = BigDecimal.ZERO;
	private BigDecimal valorTotalSemDesconto = BigDecimal.ZERO;

	public String getTotais(BigDecimal total) {
		if (total == null)
			return "";
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(total);
		// return new DecimalFormat("###,###.###").format(total);
	}

	public void distribuirPorcentagemTotalSobreUnitarios() {
		cotacaoParent.setValorTotalSemDesconto(BigDecimal.ZERO);
		cotacaoParent.setValorTotalComDesconto(BigDecimal.ZERO);
		for (Cotacao cotacao : cotacoes) {
			Double vlUnitarioComDesconto = cotacao.getValor().doubleValue()
					- ((cotacaoParent.getValorTotalDescontoPorcentagem().longValue() / 100.0)
							* cotacao.getValor().doubleValue());
			cotacao.setValorDesconto(new BigDecimal(vlUnitarioComDesconto));
			cotacaoParent.setValorTotalSemDesconto(cotacaoParent.getValorTotalSemDesconto()
					.add(cotacao.getValor().multiply(new BigDecimal(cotacao.getQuantidade())))
					.setScale(2, RoundingMode.HALF_EVEN));
			cotacaoParent.setValorTotalComDesconto(cotacaoParent.getValorTotalComDesconto()
					.add(cotacao.getValorDesconto().multiply(new BigDecimal(cotacao.getQuantidade())))
					.setScale(2, RoundingMode.HALF_EVEN));
			// System.out.println("Valor sem desconto: " + valorTotalSemDesconto);
			// System.out.println("Valor com desconto: " + valorTotalComDesconto);

		}
	}

	public void duplicarValorCotacao(Cotacao cotacao) {
		cotacao.setValorDesconto(cotacao.getValor());
	}

	public void distribuirDescontoTotalSobreUnitarios() {

		valorTotal = BigDecimal.ZERO;
		for (Cotacao cotacao : cotacoes) {
			valorTotal = valorTotal.add(cotacao.getValor().multiply(new BigDecimal(cotacao.getQuantidade())));
		}

		Double valorTotalDescontoD = cotacaoParent.getValorTotalDesconto().doubleValue();
		Double valorTotalD = valorTotal.doubleValue();
		Double porcentDesconto = (valorTotalDescontoD / valorTotalD) * 100.0;
		cotacaoParent.setValorTotalSemDesconto(BigDecimal.ZERO);
		cotacaoParent.setValorTotalComDesconto(BigDecimal.ZERO);
		for (Cotacao cotacao : cotacoes) {
			Double vlUnitarioComDesconto = cotacao.getValor().doubleValue()
					- ((porcentDesconto / 100.0) * cotacao.getValor().doubleValue());
			cotacao.setValorDesconto(new BigDecimal(vlUnitarioComDesconto).setScale(3, RoundingMode.HALF_EVEN));
			cotacaoParent.setValorTotalSemDesconto(cotacaoParent.getValorTotalSemDesconto()
					.add(cotacao.getValor().multiply(new BigDecimal(cotacao.getQuantidade()))));
			cotacaoParent.setValorTotalComDesconto(cotacaoParent.getValorTotalComDesconto()
					.add(cotacao.getValorDesconto().multiply(new BigDecimal(cotacao.getQuantidade()))));

		}

	}

	public BigDecimal pegarDecimal(Double quantidade) {
		return BigDecimal.valueOf(quantidade);
	}

	public void iniciarCotacaoParent() {
		

	}

	public void iniciarNovaCotacao() throws IOException {
		
		if (cotacaoParent.getId() == null) {
			cotacaoParent = new CotacaoParent();
		}

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

		if (fornecedor == null || fornecedor.getId() == null) {
			compra.setItens(new ArrayList<ItemCompra>());
			compra.setItens(cotacaoService.getItensByCompraCotacao(compra));
			cotacoes = new ArrayList<Cotacao>();
			for (ItemCompra item : compra.getItens()) {
				Cotacao cotacao = new Cotacao();
				cotacao.setItemCompra(item);
				cotacao.setCompra(compra);
				cotacao.setQuantidade(item.getQuantidade());
				cotacoes.add(cotacao);
			}
		} else {
			compra.setItens(new ArrayList<ItemCompra>());
			compra.setItens(cotacaoService.getItensByCompraCotacao(compra));
			cotacoes = new ArrayList<Cotacao>();
			cotacaoParent = cotacaoService.getCotacaoParent(fornecedor, compra);
			
			
			if (cotacaoParent == null) cotacaoParent =  new CotacaoParent(); 

			for (ItemCompra item : compra.getItens()) {
				Cotacao cotacao = cotacaoService.getCotacao(fornecedor, item.getCompra(), item);
				if (cotacao == null) {
					cotacao = new Cotacao();
					cotacao.setItemCompra(item);
					cotacao.setCompra(compra);
					cotacao.setQuantidade(item.getQuantidade());
				} else {
					cotacao.setItemCompra(item);
					cotacao.setCompra(compra);
					cotacao.setQuantidade(item.getQuantidade());
				}
				cotacoes.add(cotacao);
			}
		}

	}

	public String salvar() {
		cotacaoService.salvar(cotacoes, fornecedor, cotacaoParent);
		return "cotacao?faces-redirect=true&amp;includeViewParams=true";
	}
	
	public String voltar() {
		return "cotacao?faces-redirect=true&amp;includeViewParams=true";
	}

	public List<Fornecedor> completeFornecedor(String query) {
		fornecedores = new ArrayList<Fornecedor>();
		if (query.length() > 2)
			fornecedores = cotacaoService.getFornecedores(query);
		return fornecedores;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public List<Cotacao> getCotacoes() {
		return cotacoes;
	}

	public void setCotacoes(List<Cotacao> cotacoes) {
		this.cotacoes = cotacoes;
	}

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public List<MenuLateral> getMenus() {

		List<MenuLateral> lista = new ArrayList<MenuLateral>();
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

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public BigDecimal getValorTotalDesconto() {
		return valorTotalDesconto;
	}

	public void setValorTotalDesconto(BigDecimal valorTotalDesconto) {
		this.valorTotalDesconto = valorTotalDesconto;
	}

	public BigDecimal getValorTotalDescontoPorcentagem() {
		return valorTotalDescontoPorcentagem;
	}

	public void setValorTotalDescontoPorcentagem(BigDecimal valorTotalDescontoPorcentagem) {
		this.valorTotalDescontoPorcentagem = valorTotalDescontoPorcentagem;
	}

	public BigDecimal getValorTotalComDesconto() {
		return valorTotalComDesconto;
	}

	public void setValorTotalComDesconto(BigDecimal valorTotalComDesconto) {
		this.valorTotalComDesconto = valorTotalComDesconto;
	}

	public BigDecimal getValorTotalSemDesconto() {
		return valorTotalSemDesconto;
	}

	public void setValorTotalSemDesconto(BigDecimal valorTotalSemDesconto) {
		this.valorTotalSemDesconto = valorTotalSemDesconto;
	}

	public CotacaoParent getCotacaoParent() {
		return cotacaoParent;
	}

	public void setCotacaoParent(CotacaoParent cotacaoParent) {
		this.cotacaoParent = cotacaoParent;
	}

}
