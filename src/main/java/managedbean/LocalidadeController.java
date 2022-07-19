package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Estado;
import model.Localidade;
import model.MenuLateral;
import model.Municipio;
import model.Pais;
import model.UnidadeConservacao;
import service.LocalidadeService;
import util.MakeMenu;
import util.UsuarioSessao;

@Named(value = "localidadeController")
@ViewScoped
public class LocalidadeController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Localidade> list;

	private MenuLateral menu = new MenuLateral();

	private Long idLocalidade;

	public Long getIdLocalidade() {
		return idLocalidade;
	}

	public void setIdLocalidade(Long idLocalidade) {
		this.idLocalidade = idLocalidade;
	}

	private Localidade localidade = new Localidade();

	public Localidade getLocalidade() {
		return localidade;
	}

	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
	}

	public boolean poderEditar() {
		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin"))
			return true;

		return false;
	}

	public List<Estado> listaEstado() {
		return localidadeService.getListEstado();
	}

	public List<Pais> listaPais() {
		return localidadeService.getListPais();
	}

	public List<UnidadeConservacao> listaUc() {
		return localidadeService.getListUc();
	}

	public List<Municipio> getMunicipios() {
		return localidadeService.getMunicipios();
	}

	public String editar(Localidade localidade) {
		this.localidade = localidade;
		return "localidade_edit?faces-redirect=true";
	}

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public String listagemLocalidades() {
		return "localidade?faces-redirect=true";
	}

	@Inject
	private LocalidadeService localidadeService;

	@Inject
	private UsuarioSessao usuarioSessao;

	public void carregaLocalidade() {
		list = localidadeService.carregaLocalidade();
	}

	@PostConstruct
	public void init() {

		carregaLocalidade();
	}

	public String salvar() {
		try {
			localidadeService.salvar(localidade);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Localidade", "Salvo com Sucesso!"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Localidade", "Não foi Possível Salvar Localidade"));
		}

		return redirect();
	}

	public void delete(Localidade localidade) {
		if (localidadeService.delete(localidade)) {
			FacesContext.getCurrentInstance().addMessage("msgs",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Localidade", "Deletado com Sucesso!"));
		} else {
			FacesContext.getCurrentInstance().addMessage("msgs", new FacesMessage(FacesMessage.SEVERITY_FATAL,
					"Localidade", "Não foi Possivel deletar Localidade!"));
		}
		carregaLocalidade();
	}

	// Metodos de Direct
	public String novo() {
		return "localidade_edit?faces-redirect=true";
	}

	public String redirect() {
		return "localidade";
	}

	public String redirecionar(MenuLateral menu) {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuConfiguracoes();
	}

	public List<Localidade> getList() {
		return list;
	}

	public void setList(List<Localidade> list) {
		this.list = list;
	}

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}

}
