package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import model.Tutorial;

@Named(value = "tutorial_controller")
@ViewScoped
public class TutorialController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private Tutorial tutorial;
	private List<Tutorial> listTutorial = new ArrayList<>(); 
	private Boolean showTutorial;
	
	@PostConstruct
	public void init() {
	   carregarLista();
	   showTutorial = false;
	   
	}

	public TutorialController() {
		
	}
	
	public void redirecionarPDF() {
		showTutorial = true;
	}
	
	
	public void carregarLista() {
		listTutorial = new ArrayList<>();
		
		
		tutorial = new  Tutorial();
		tutorial.setLabel("Fluxo de atendimento SGI");
		tutorial.setId("0");
		tutorial.setPath("../resources/tutorial/atendimento_sgi.pdf");
		listTutorial.add(tutorial);
		
		tutorial = new  Tutorial();
		tutorial.setLabel("Seleção de linha orçamentária - Projeção de saldos");
		tutorial.setId("0");
		tutorial.setPath("../resources/tutorial/projecao_saldos.pdf");
		listTutorial.add(tutorial);
		
		
		tutorial = new  Tutorial();
		tutorial.setLabel("Relatório Orçado x realizado de projetos");
		tutorial.setId("1");
		tutorial.setPath("../resources/tutorial/relatorio_orcado_realizado.pdf");
		listTutorial.add(tutorial);
		
		tutorial = new  Tutorial();
		tutorial.setLabel("Relatório Saving");
		tutorial.setId("2");
		tutorial.setPath("../resources/tutorial/saving.pdf");
		listTutorial.add(tutorial);
		
		tutorial = new  Tutorial();
		tutorial.setLabel("Relatório de adiantamentos");
		tutorial.setPath("../resources/tutorial/adiantamentos.pdf");
		tutorial.setId("3");
		listTutorial.add(tutorial);
		
		
		tutorial = new  Tutorial();
		tutorial.setLabel("Melhoria no controle de diárias");
		tutorial.setPath("../resources/tutorial/controle_diarias.pdf");
		tutorial.setId("4");
		listTutorial.add(tutorial);
		
		tutorial = new  Tutorial();
		tutorial.setLabel("Melhoria no controle de projetos");
		tutorial.setPath("../resources/tutorial/controle_projetos.pdf");
		tutorial.setId("5");
		listTutorial.add(tutorial);
		
	}
	

	public Tutorial getTutorial() {
		return tutorial;
	}

	public void setTutorial(Tutorial tutorial) {
		this.tutorial = tutorial;
	}

	public List<Tutorial> getListTutorial() {
		return listTutorial;
	}

	public void setListTutorial(List<Tutorial> listTutorial) {
		this.listTutorial = listTutorial;
	}

	public Boolean getShowTutorial() {
		return showTutorial;
	}

	public void setShowTutorial(Boolean showTutorial) {
		this.showTutorial = showTutorial;
	}
		
	
}
