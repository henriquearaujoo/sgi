package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import model.Configuracao;
import model.MenuLateral;
import service.ConfiguracaoService;
import util.Filtro;
import util.MakeMenu;
import util.UsuarioSessao;

@Named(value = "configuracaoController")
@ViewScoped
public class ConfiguracaoController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Configuracao> list;
	
	private MenuLateral menu = new MenuLateral();
	
	private Filtro filtro = new Filtro();

	@Inject
	private ConfiguracaoService configuracaoService;
	
	
	@Inject 
	private UsuarioSessao usuarioSessao;
	
	public Boolean verificaAdmin() {
		if(usuarioSessao.getUsuario().getPerfil().getId().equals(new Long("1"))){
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public void carregarConfiguracao() {
		list = configuracaoService.carregaConfiguracao();
	}
	
	@PostConstruct
	public void init() {
		carregarConfiguracao();
	}
	
	public void salvar(RowEditEvent event) {
		Configuracao configuracao = (Configuracao) event.getObject();
		configuracaoService.salvar(configuracao);
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
	public void salvarV2(Filtro filtro) {
		configuracaoService.salvarV2(filtro.getConfig());
    }    
	
	
	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}
	
	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuConfigurações();
	}

	public List<Configuracao> getList() {
		return list;
	}

	public void setList(List<Configuracao> list) {
		this.list = list;
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

	
	

}


