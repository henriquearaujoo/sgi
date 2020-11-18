package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Comunidade;
import service.ComunidadeService;

@Named(value = "comunidade_controller")
@ViewScoped
public class ComunidadeController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ComunidadeService comunidadeService;

	@Inject
	private Comunidade comunidade;

	private List<Comunidade> comunidades;

	public void initListagem() {
		this.comunidades = this.comunidadeService.findAll();
	}

	public ComunidadeService getComunidadeService() {
		return comunidadeService;
	}

	public Comunidade getComunidade() {
		return comunidade;
	}

	public List<Comunidade> getComunidades() {
		return comunidades;
	}

	public void setComunidadeService(ComunidadeService comunidadeService) {
		this.comunidadeService = comunidadeService;
	}

	public void setComunidade(Comunidade comunidade) {
		this.comunidade = comunidade;
	}

	public void setComunidades(List<Comunidade> comunidades) {
		this.comunidades = comunidades;
	}

	public ComunidadeController() {
	}
	
	public void handleUcChange() {
		
	}

	public String salvar() {
		try {
			this.comunidadeService.salvar(this.comunidade);
			return "comunidades?faces-redirect=true&sucesso=1";
		} catch (Exception e) {
			return "comunidades?faces-redirect=true&sucesso=0";
		}
	}

	public void delete() {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			this.comunidadeService.remover(this.comunidade);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Comunidade removida com sucesso!", "");
			context.addMessage("msg", msg);
			this.initListagem();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao remover a comunidade", "");
			context.addMessage("msg", msg);
		}
	}

}
