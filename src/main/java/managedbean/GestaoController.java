package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Aprouve;
import model.CategoriaDespesaClass;
import model.Colaborador;
import model.Coordenadoria;
import model.Gestao;
import model.MenuLateral;
import model.Regional;
import model.Superintendencia;
import model.User;
import repositorio.AprouveRepositorio;
import service.ColaboradorService;
import service.GestaoService;
import util.MakeMenu;
import util.UsuarioSessao;

@Named(value = "gestaoController")
@ViewScoped
public class GestaoController implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	@Inject
	private GestaoService gestaoService;

	@Inject
	private ColaboradorService colaboradorService;

	private List<Colaborador> colaborador = new ArrayList<>();

	private Gestao gestao;

	private Aprouve aprouve = new Aprouve();
	
	@Inject
	private UsuarioSessao sessao;

	@Inject
	private AprouveRepositorio aprouveRepositorio;

	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuConfigurações();
	}

	public String salvar() {
		gestao = gestaoService.salvar(gestao);
		return "gestao?faces-redirect=true";
	}
	public String salvarHome() {
		gestao = gestaoService.salvar(gestao);
		return "home_new?faces-redirect=true";
	}

	public void editar(Gestao gestao) {

		this.gestao = gestaoService.findById(gestao.getId());

		if (gestao instanceof Superintendencia) {
			gestao.setType("sup");
		} else if (gestao instanceof Coordenadoria) {
			gestao.setType("coord");
		} else if (gestao instanceof Regional) {
			gestao.setType("reg");
		}

	}
	
public void editarGestao(User usuario) {
		
		
		carregarColaborador();
		this.gestao = gestaoService.findByColaborador(usuario.getColaborador().getId());

		if (gestao instanceof Superintendencia) {
			gestao.setType("sup");
		} else if (gestao instanceof Coordenadoria) {
			gestao.setType("coord");
		} else if (gestao instanceof Regional) {
			gestao.setType("reg");
		}

	}

	public void openDialogNovo() {
		gestao = new Gestao();
	}

	public List<Gestao> findAll() {
		return gestaoService.findAll();
	}

	public List<Colaborador> carregarColaborador() {
		return colaborador = colaboradorService.findAll();
	}

	public Gestao getGestao() {
		return gestao;
	}

	public void setGestao(Gestao gestao) {
		this.gestao = gestao;
	}

	public List<Colaborador> getColaborador() {
		return colaborador;
	}

	public void setColaborador(List<Colaborador> colaborador) {
		this.colaborador = colaborador;
	}

	public UsuarioSessao getSessao() {
		return sessao;
	}

	public void setSessao(UsuarioSessao sessao) {
		this.sessao = sessao;
	}
	
	public List<Gestao> getRegionais(){
		return this.gestaoService.getRegionais();
	}

}