package managedbean;

import java.io.Serializable;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.ComponenteClass;
import model.SubComponente;
import service.ComponenteService;

@Named(value = "componente_controller")
@ViewScoped
public class ComponenteController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ComponenteService componenteService;

	@Inject
	private ComponenteClass componente;

	@Inject
	private SubComponente subcomponente;

	private List<ComponenteClass> componentes;

	private List<SubComponente> subcomponentes;

	public void initListagemComponente() {
		this.componentes = this.findAllComponentes();
	}

	public void initListagemSubcomponente() {
		this.subcomponentes = this.findAllSubcomponentes();
	}

	public List<ComponenteClass> findAllComponentes() {
		return this.componenteService.findAllComponente();
	}

	public List<SubComponente> findAllSubcomponentes() {
		return this.componenteService.findAllSubcomponente();
	}

	public String salvarComponente() {
		try {
			this.componenteService.salvar(this.componente);
			return "componentes?faces-redirect=true&sucesso=1";
		} catch (Exception e) {
			return "componentes?faces-redirect=true&sucesso=0";
		}
	}

	public void removerComponente() {
		try {
			this.componenteService.remover(this.componente);
			FacesContext.getCurrentInstance().addMessage("messages",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Componente removido com sucesso!"));
			this.initListagemComponente();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro!", "Não foi possível remover o componente desejado."));
		}
	}

	public String salvarSubcomponente() {
		try {
			this.componenteService.salvar(this.subcomponente);
			return "subcomponentes?faces-redirect=true&sucesso=1";
		} catch (Exception e) {
			return "subcomponentes?faces-redirect=true&sucesso=0";
		}
	}

	public void removerSubcomponente() {
		try {
			this.componenteService.remover(this.subcomponente);
			FacesContext.getCurrentInstance().addMessage("messages",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Componente removido com sucesso!"));
			this.initListagemSubcomponente();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("messages", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro!", "Não foi possível remover o componente desejado."));
		}
	}

	public List<ComponenteClass> completeComponente(String query) {
		return this.componenteService.completeComponente(query);
	}

	public ComponenteClass getComponente() {
		return this.componente;
	}

	public void setComponente(ComponenteClass componente) {
		this.componente = componente;
	}

	public SubComponente getSubcomponente() {
		return subcomponente;
	}

	public void setSubcomponente(SubComponente subcomponente) {
		this.subcomponente = subcomponente;
	}

	public List<ComponenteClass> getComponentes() {
		return this.componentes;
	}

	public void setComponentes(List<ComponenteClass> componentes) {
		this.componentes = componentes;
	}

	public List<SubComponente> getSubcomponentes() {
		return subcomponentes;
	}

	public void setSubcomponentes(List<SubComponente> subcomponentes) {
		this.subcomponentes = subcomponentes;
	}

}
