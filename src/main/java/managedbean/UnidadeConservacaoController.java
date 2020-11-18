package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Municipio;
import model.Regional;
import model.UnidadeConservacao;
import service.GestaoService;
import service.LocalidadeService;
import service.UnidadeConservacaoService;

@Named(value = "unidadeConservacaoController")
@ViewScoped
public class UnidadeConservacaoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private UnidadeConservacao unidadeConservacao;

	private List<UnidadeConservacao> unidades;

	private List<Regional> regionais;

	private List<Municipio> municipios;

	public List<Regional> getRegionais() {
		return regionais;
	}

	public List<Municipio> getMunicipios() {
		return municipios;
	}

	public void setRegionais(List<Regional> regionais) {
		this.regionais = regionais;
	}

	public void setMunicipios(List<Municipio> municipios) {
		this.municipios = municipios;
	}

	public List<UnidadeConservacao> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<UnidadeConservacao> unidades) {
		this.unidades = unidades;
	}

	public List<UnidadeConservacao> listaUc() {
		return unidadeConservacaoService.getListUc();
	}

	public List<UnidadeConservacao> ucAutocomplete(String query) {
		return unidadeConservacaoService.ucAutocomplete(query);
	}

	@Inject
	private LocalidadeService localidadeService;

	@Inject
	private GestaoService gestaoService;

	public void initListagem() {
		this.unidades = listaUc();
	}

	public void getMunEReg() {
		this.municipios = this.localidadeService.getMunicipios();
		this.regionais = this.gestaoService.getRegionais();
	}

	@Inject
	private UnidadeConservacaoService unidadeConservacaoService;

	public String salvar() {
		try {
			unidadeConservacaoService.salvar(unidadeConservacao);
			return "unidadesConservacao?faces-redirect=true&sucesso=1";
		} catch (Exception e) {
			return "unidadesConservacao?faces-redirect=true&sucesso=0";
		}
	}

	public void delete() {
		if (unidadeConservacaoService.delete(this.unidadeConservacao)) {
			FacesContext.getCurrentInstance().addMessage("msgs",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Unidade de Conservação", "Deletado com Sucesso!"));
			this.initListagem();
		} else {
			FacesContext.getCurrentInstance().addMessage("msgs", new FacesMessage(FacesMessage.SEVERITY_FATAL,
					"Unidade de Conservação", "Não foi Possivel deletar Unidade de Conservação!"));
		}
	}

	public UnidadeConservacao getUnidadeConservacao() {
		return unidadeConservacao;
	}

	public void setUnidadeConservacao(UnidadeConservacao unidadeConservacao) {
		this.unidadeConservacao = unidadeConservacao;
	}

}
