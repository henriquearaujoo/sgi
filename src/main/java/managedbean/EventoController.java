package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Evento;
import model.MenuLateral;
import service.EventoService;
import util.Filtro;
import util.MakeMenu;

@Named(value = "evento_controller")
@ViewScoped
public class EventoController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Evento evento;
	
	private MenuLateral menu = new MenuLateral();
	
	private List<Evento> eventos = new ArrayList<>();
	
	private Filtro filtro = new Filtro();
	
	public void initListagem(){
		carregarLista();
	}

	@Inject
	private EventoService eventoService;
	
	
	public EventoController(){}
	
	
	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}
	
	public String salvar(){
		eventoService.salvar(evento);	
		return "eventos?faces-redirect=true";
	}
	
	
	public void remover(){
		eventoService.remover(evento);
		eventos = eventoService.getAll(filtro);
	}

	public void carregarLista(){
		eventos = eventoService.getAll(filtro);
	}
	
	public void carregarEvento(){
		evento = eventoService.findById(evento.getId());
	}
	
	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuFinanceiro();
	}
	
	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}
	
	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public List<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}


}
