package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.weld.context.RequestContext;

import anotacoes.Transactional;
import model.FormularioClimaOrgan;
import repositorio.ClimaRepositorio;

@Named(value = "clima_controller")
@ViewScoped
public class ClimaOrganController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FormularioClimaOrgan formulario;
	@Inject
	private ClimaRepositorio repositorio;
	
	
	public ClimaOrganController(){}
	
	@Transactional
	public void salvar(){
		
		try {
			repositorio.salvar(formulario);
			formulario = new FormularioClimaOrgan();
			showMessage();
			addMessage("", "Formulário salvo com sucesso!!!", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			addMessage("", "Erro ao inserir formulário!!!", FacesMessage.SEVERITY_ERROR);
		}
		
		
	}
	
	public void showMessage() {

		StringBuilder msg = new StringBuilder("Formulário salvo com sucesso!!!");

		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "(Y)", msg.toString());

		FacesContext.getCurrentInstance().addMessage(null, message);
		;
	}
	
	public List<String> getNivelSatisfacao(){
		List<String> niveis = new ArrayList<>();
		niveis.add("");
		niveis.add("Sim");
		niveis.add("Não");
		return niveis;
	}
	
	public List<String> getSimOUNao(){
		List<String> niveis = new ArrayList<>();
		niveis.add("Sim");
		niveis.add("Não");
		return niveis;
	}
	 
	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public FormularioClimaOrgan getFormulario() {
		return formulario;
	}

	public void setFormulario(FormularioClimaOrgan formulario) {
		this.formulario = formulario;
	}

}
