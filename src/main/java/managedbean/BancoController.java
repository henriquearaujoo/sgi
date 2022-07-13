package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Banco;
import model.MenuLateral;
import service.BancoService;
import util.Filtro;
import util.MakeMenu;

@Named(value = "banco_controller")
@ViewScoped
public class BancoController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Banco banco = new Banco();
	
	private MenuLateral menu = new MenuLateral();
	
	private List<Banco> bancos = new ArrayList<>();
	
	private Filtro filtro = new Filtro();
	
	public void initListagem(){
		carregarListaDeBanco();
	}

	@Inject
	private BancoService bancoService;
	
	
	public BancoController(){}
	
	
	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}
	
	public String salvarBanco(){
		bancoService.salvarBanco(banco);	
		return "bancos?faces-redirect=true";
	}

	public void createNewBank() {
		if (banco.getNomeBanco().isEmpty() || banco.getNomeBanco() == null
				|| banco.getNumeroBanco().isEmpty() || banco.getNumeroBanco() == null) {
			FacesContext.getCurrentInstance()
					.addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção!","Preencha todos os campos"));
		}
		bancoService.salvarBanco(banco);
		getAllBanks();
		banco = new Banco();
	}
	
	
	public void removerBanco(){
		bancoService.removerBanco(banco);
		bancos = bancoService.getTodosBancos(filtro);
	}

	public void getAllBanks() {
		bancos = bancoService.getTodosBancos();
	}

	public void carregarListaDeBanco(){
		bancos = bancoService.getTodosBancos(filtro);
	}
	
	public void carregarBanco(){
		banco = bancoService.findBancoById(banco.getId());
	}
	
	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuFinanceiro();
	}
	
	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}
	
	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public List<Banco> getBancos() {
		return bancos;
	}

	public void setBancos(List<Banco> bancos) {
		this.bancos = bancos;
	}

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}


}
