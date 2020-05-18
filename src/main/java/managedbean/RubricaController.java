package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import anotacoes.Transactional;
import model.MenuLateral;
import model.Rubrica;
import repositorio.RubricaRepositorio;
import util.MakeMenu;

@Named(value = "rubricaController")
@ViewScoped
public class RubricaController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private @Inject RubricaRepositorio repositorio;
	private @Inject Rubrica rubrica;
	private List<Rubrica> rubricas = new ArrayList<>();
	private MenuLateral menu = new MenuLateral();

	@PostConstruct
	public void init() {
		carregarRubricas();
	}

	@Transactional
	public void salvar() {
		repositorio.salvar(rubrica);
		carregarRubricas();
	}

	@Transactional
	public void remover() {
		repositorio.remover(rubrica);
		carregarRubricas();
	}

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public void carregarRubricas() {
		rubricas = repositorio.getRubricas();
	}

	public Rubrica getRubrica() {
		return rubrica;
	}

	public void setRubrica(Rubrica rubrica) {
		this.rubrica = rubrica;
	}

	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuConfigurações();
	}

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}

	public List<Rubrica> getRubricas() {
		return rubricas;
	}

	public void setRubricas(List<Rubrica> rubricas) {
		this.rubricas = rubricas;
	}

}
