package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.MenuLateral;
import model.Post;
import model.User;
import service.PostService;
import util.Filtro;
import util.MakeMenu;

@Named(value = "post_controller")
@ViewScoped
public class PostController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Post post;
	
	private MenuLateral menu = new MenuLateral();
	
	private List<Post> posts = new ArrayList<>();
	
	private Filtro filtro = new Filtro();
	
	
	@Inject
	private PostService postService;
	
	
	public void initListagem(){
		carregarListaDePost();
	}	
	
	public PostController(){}
	
	
	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}
	
	public String salvarPost(){
		postService.salvar(post, new User());	
		return "posts?faces-redirect=true";
	}
	
	
	public void removerPost(){
		postService.remover(post);
		posts = postService.getAll(filtro);
	}

	public void carregarListaDePost(){
		posts = postService.getAll(filtro);
	}
	
	public void carregarPost(){
		post = postService.findById(post.getId());
	}
	
	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuFinanceiro();
	}
	
	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}
	
	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}


}
