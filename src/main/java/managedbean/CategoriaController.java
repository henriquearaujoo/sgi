package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.CategoriaDespesaClass;
import model.MenuLateral;
import repositorio.CategoriaRepositorio;
import util.MakeMenu;


@ViewScoped
@Named(value =  "categoria_controller")
public class CategoriaController implements Serializable{

	
	public CategoriaController(){}

	
	@Inject 
	private CategoriaDespesaClass  categoria;
	@Inject 
	private CategoriaRepositorio repositorio;
	
	private List<CategoriaDespesaClass> categorias;
	
	private MenuLateral menu = new MenuLateral();

	@PostConstruct
	public void init(){
		carregarCategorias();
	}
		
	
	public void carregarCategorias(){
	  categorias = repositorio.buscarGategorias();	
	}
	
	public void salvar(){
		repositorio.salvar(categoria);
		carregarCategorias();
		categoria = new CategoriaDespesaClass();
//		RequestContext context = RequestContext.getCurrentInstance();
//		context.execute("PF('cadastro_categoria').hide();");
	}
	
	public void removerCategoria(CategoriaDespesaClass categoria){
		repositorio.remover(categoria);
	}

	public void editar(){
		categoria = repositorio.findById(categoria.getId());
	}
	
	
	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}
	
	public void preparaCategoria(){
		categoria = new CategoriaDespesaClass();
	}
	
	public CategoriaDespesaClass getCategoria() {
		return categoria;
	}
	public void setCategoria(CategoriaDespesaClass categoria) {
		this.categoria = categoria;
	}
	public List<CategoriaDespesaClass> getCategorias() {
		return categorias;
	}
	public void setCategorias(List<CategoriaDespesaClass> categorias) {
		this.categorias = categorias;
	}

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}
	
	
	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuConfiguracoes();
	}
	
	
	
	
	
}
