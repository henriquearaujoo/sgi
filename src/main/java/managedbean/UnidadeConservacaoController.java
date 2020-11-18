package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.UnidadeConservacao;

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

	public List<UnidadeConservacao> listaUc() {
		return unidadeConservacaoService.getListUc();
	}
	
	public List<UnidadeConservacao> ucAutocomplete(String query){
		return unidadeConservacaoService.ucAutocomplete(query);
	}

	@Inject
	private UnidadeConservacaoService unidadeConservacaoService;

	public void salvar() {
		try {
			unidadeConservacaoService.salvar(unidadeConservacao);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Unidade de Conservação", "Salvo com Sucesso!"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Unidade de Conservação", "Não foi Possível Salvar Unidade de Conservação"));
		}
	}

	public void delete(UnidadeConservacao unidadeConservacao) {
		if (unidadeConservacaoService.delete(unidadeConservacao)) {
			FacesContext.getCurrentInstance().addMessage("msgs",
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Unidade de Conservação", "Deletado com Sucesso!"));
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
