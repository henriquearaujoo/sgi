package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OneToMany;

import model.Cargo;
import model.Colaborador;
import service.CargoService;

@Named(value = "cargo_controller")
@ViewScoped
public class CargoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private CargoService cargoService;

	@Inject
	private Cargo cargo;

	private List<Cargo> cargos;
	
	@OneToMany(mappedBy = "cargo")
	private List<Colaborador> colaboradores;

	public CargoController() {
	}
	
	private String id;

	public void init() {
		if (id != null)
			this.cargo = cargoService.findByid(Long.parseLong(id));
	}
	
	public void initListagem() {
		this.cargos = this.findAll();
	}

	public List<Cargo> findAll() {
		return cargoService.findAll();
	}

	public String salvar() {
		try {
			cargoService.salvar(this.cargo);
			return "cargos?faces-redirect=true&sucesso=1";
		} catch (Exception e) {
			return "cargos?faces-redirect=true&sucesso=0";
		}
	}

	public void remover() {
		try {
			cargoService.remover(this.cargo);
			FacesContext.getCurrentInstance().addMessage("messages",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Cargo removido com sucesso!"));
			this.initListagem();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro!", "Não foi possível remover o cargo desejado. Provavelmente, ele está associado a algum colaborador."));
		}
	}

	public List<Cargo> getCargos() {
		return cargos;
	}

	public void setCargos(List<Cargo> cargos) {
		this.cargos = cargos;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public List<Colaborador> getColaboradores() {
		return colaboradores;
	}

	public void setColaboradores(List<Colaborador> colaboradores) {
		this.colaboradores = colaboradores;
	}

}
