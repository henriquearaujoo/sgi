package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Colaborador;
import model.Gestao;
import model.MenuLateral;
import model.TipoGestao;
import model.User;
import repositorio.GestaoRepositorio;
import service.UsuarioService;
import util.CDILocator;
import util.Filtro;
import util.MakeMenu;
import util.UsuarioSessao;


@Named(value = "configController")
@ViewScoped
public class ConfigController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private 
	@Inject
	UsuarioService service;
	private User usuario = new User();
	private List<User> usuarios = new ArrayList<User>();
	private List<Colaborador> colaboradores = new ArrayList<Colaborador>();
	private @Inject UsuarioSessao usuarioSessao;
	private Filtro filtro = new Filtro();
	private Boolean panelListagem = true;
	private Boolean panelCadastro = false;
	private Boolean panelBlock = true;
	private String tipoGestao = "";
	private List<Gestao> gestoes;
	
	
	
	@PostConstruct
	public void init(){
		colaboradores = service.getColaborador();
		usuarios = service.getUsuarios();
		
		
		
		}
	
	
	
	
	public void novoUsuario(){
		
		panelCadastro = true;
		panelListagem = false;
		
		
	}
	
	public void editarUsuario(){
		
		
		
	}
	
	public void salvarUsuario(){
		
		if(service.Salvar(usuario)){
			panelCadastro = false;
			panelListagem = true;
			usuario = new User();
			usuarios = service.getUsuarios();
		}
		
	}
	
	public void voltarListagem(){
		
		panelCadastro = false;
		panelListagem = true;
		
		
	}
	
	
	
	public void validar(){
		
		
		
		if(usuarioSessao.getNomeUsuario().equals("admin")){
		
			panelBlock = false;
			panelListagem = true;
			System.out.println("Permitido");
			
		}else{
			
			panelBlock = true;
			panelListagem = false;
			System.out.println("Nao permitido");
				
		}
		
			
		
	}

	
	public List<MenuLateral> getMenus() {

		List<MenuLateral> lista = new ArrayList<>();
		MenuLateral menu = new MenuLateral("Bens", "relatorios", "fa fa-shopping-cart");
		lista.add(menu);
		menu = new MenuLateral("Ações", "produtos", "fa fa-database");
		lista.add(menu);
		
		return MakeMenu.getMenuConfigurações();
	}
	
	
	public void onTipoGestaoChange() {
		tipoGestao = "";
		if (usuario.getTipoGestao() != null) {
			tipoGestao = usuario.getTipoGestao().getNome();
			GestaoRepositorio repo = CDILocator.getBean(GestaoRepositorio.class);
			gestoes = new ArrayList<>();
			gestoes = usuario.getTipoGestao().getGestao(repo);
		}
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

	

	public TipoGestao[] getTiposGestao() {
		return TipoGestao.values();
	}
	
	
	public UsuarioService getService() {
		return service;
	}


	public void setService(UsuarioService service) {
		this.service = service;
	}


	public User getUsuario() {
		return usuario;
	}


	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}


	public List<Colaborador> getColaboradores() {
		return colaboradores;
	}


	public void setColaboradores(List<Colaborador> colaboradores) {
		this.colaboradores = colaboradores;
	}


	public Filtro getFiltro() {
		return filtro;
	}


	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}


	public Boolean getPanelListagem() {
		return panelListagem;
	}


	public void setPanelListagem(Boolean panelListagem) {
		this.panelListagem = panelListagem;
	}


	public Boolean getPanelCadastro() {
		return panelCadastro;
	}


	public void setPanelCadastro(Boolean panelCadastro) {
		this.panelCadastro = panelCadastro;
	}


	public Boolean getPanelBlock() {
		return panelBlock;
	}


	public void setPanelBlock(Boolean panelBlock) {
		this.panelBlock = panelBlock;
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

	public List<User> getUsuarios() {
		return usuarios;
	}


	public void setUsuarios(List<User> usuaruios) {
		this.usuarios = usuaruios;
	}
	
}
