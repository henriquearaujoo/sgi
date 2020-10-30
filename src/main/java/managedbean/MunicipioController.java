package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Municipio;

import service.MunicipioService;

@Named(value = "municipioController")
@ViewScoped
public class MunicipioController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Municipio municipio;

	public List<Municipio> listaMunicipios() {
		return municipioService.getListMunicipios();
	}
	
//	public List<Municipio> ucAutocomplete(String query){
//		return municipioService.ucAutocomplete(query);
//	}

	@Inject
	private MunicipioService municipioService;

	public void salvar() {
		try {
			municipioService.salvar(municipio);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Município", "Salvo com Sucesso!"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Município", "Não foi Possível Salvar Município"));
		}
	}

	public void delete(Municipio municipio) {
		if (municipioService.delete(municipio)) {
			FacesContext.getCurrentInstance().addMessage("msgs",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Município", "Deletado com Sucesso!"));
		} else {
			FacesContext.getCurrentInstance().addMessage("msgs", new FacesMessage(FacesMessage.SEVERITY_FATAL,
					"Município", "Não foi Possivel deletar Município!"));
		}
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

}
