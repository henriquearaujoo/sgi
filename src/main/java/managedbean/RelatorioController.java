package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import auxiliar.RelatorioItensPorUc;
import model.MenuLateral;
import model.StatusCompra;
import model.TipoGestao;
import model.TipoLocalidade;
import model.UnidadeConservacao;
import service.RelatorioService;
import util.Filtro;
import util.MakeMenu;

@Named(value = "relatorioController")
@ViewScoped
public class RelatorioController implements Serializable {

	private static final long serialVersionUID = 1L;

	private @Inject RelatorioService relatorioService;
	private MenuLateral menu = new MenuLateral();
	private List<RelatorioItensPorUc> listagem = new ArrayList<RelatorioItensPorUc>();
	private List<UnidadeConservacao> ucs = new ArrayList<UnidadeConservacao>();
	private Filtro filtro = new Filtro();
	
	
	public RelatorioController(){}
	
	public void filtrar(){
		listagem = relatorioService.getListagemRelatoriosDebens(filtro);
	} 

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public List<MenuLateral> getMenus() {

		List<MenuLateral> lista = new ArrayList<>();
		MenuLateral menu = new MenuLateral("Bens", "relatorios", "fa fa-shopping-cart");
		lista.add(menu);
		menu = new MenuLateral("Ações", "produtos", "fa fa-database");
		lista.add(menu);
		
		return MakeMenu.getMenuRelatorios();
	}
	

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}


	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
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

	public List<RelatorioItensPorUc> getListagem() {
		return listagem;
	}

	public void setListagem(List<RelatorioItensPorUc> listagem) {
		this.listagem = listagem;
	}

	public List<UnidadeConservacao> getUcs() {
		this.ucs = relatorioService.getUcs();
		return ucs;
	}

	public void setUcs(List<UnidadeConservacao> ucs) {
		this.ucs = ucs;
	}

}
