package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Orcamento;
import model.OrcamentoProjeto;
import service.RecursosProjetoService;
import util.Filtro;

@Named(value = "recursosProjetoBean")
@ViewScoped
public class RecursosProjetoController implements Serializable{

	
	/**
	 * Create: 2018/04/03
	 * to: Manage screen of "recursos_projetos.html" in Project  - Home office 
	 * by: Christophe Alexander
	 */
	private static final long serialVersionUID = 1L;

	
	private @Inject RecursosProjetoService recursosProjetoService;
	
	private Filtro filtro = new Filtro();
	
	
	private List<OrcamentoProjeto> listaInit;
	
	private List<Orcamento> listaOrcamento;

	public List<OrcamentoProjeto> findAllOrcamentosProjetos(){
		return recursosProjetoService.findAll();
	}
	
	//Metodo de lipeza de filtros
	public void limpar() {
		filtro = new Filtro();
	}
	
	//Metodo de Filtragem
	public void filtraRecurosProjeto(){
		listaInit = recursosProjetoService.findByFiltros(filtro);

	}
	
	public List<Orcamento> completeOrcamentos(String query) {
		listaOrcamento = new ArrayList<Orcamento>();
		listaOrcamento = recursosProjetoService.findOrcamentoByTitulo(query);
		return listaOrcamento;
	}
	
	//Metodo de Aprovação 
	public void aprovaStatusRecursos(OrcamentoProjeto  orcamentoProjeto){
		try{
			orcamentoProjeto.setStatus("Aprovado");
			recursosProjetoService.salvarOrcamentoProjeto(orcamentoProjeto);
			filtraRecurosProjeto();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Status Atualizado com Sucesso!");

			FacesContext.getCurrentInstance().addMessage(null, message);
		}catch (Exception e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Aviso", "Não Foi Possível Atualizar o status!");

			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	//Metodo Reprovacao
	public void reprovaStatusRecursos(OrcamentoProjeto  orcamentoProjeto){
		try{
			orcamentoProjeto.setStatus("Reprovado");
			recursosProjetoService.salvarOrcamentoProjeto(orcamentoProjeto);
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Status Atualizado com Sucesso!");
			filtraRecurosProjeto();
			FacesContext.getCurrentInstance().addMessage(null, message);
		}catch (Exception e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Aviso", "Não Foi Possível Atualizar o status!");

			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	//Getter Setter------------------------------------------------------------
	
	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public List<OrcamentoProjeto> getListaInit() {
		return listaInit;
	}

	public void setListaInit(List<OrcamentoProjeto> listaInit) {
		this.listaInit = listaInit;
	}

}
