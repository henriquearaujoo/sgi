package service;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import anotacoes.Transactional;
import model.Orcamento;
import model.OrcamentoProjeto;
import model.Projeto;
import repositorio.OrcamentoRepositorio;
import repositorio.ProjetoRepositorio;
import util.Filtro;

public class RecursosProjetoService implements Serializable {

	/**
	 * Create: 2018/04/03
	 * to: Support ProjectController Resource Class in Project  - Home office 
	 * by: Christophe Alexander
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProjetoRepositorio repositorio;
	
	@Inject
	private OrcamentoRepositorio orcamentoRepositorio;
	
	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public List<OrcamentoProjeto> findAll() {
		return repositorio.findAll();
	}
	
	@Transactional
	public OrcamentoProjeto salvarOrcamentoProjeto(OrcamentoProjeto orcamentoProjeto){
		return orcamentoRepositorio.salvarOrcamentoProjeto(orcamentoProjeto);
	}
	

	public List<OrcamentoProjeto> findByFiltros(Filtro filtro) {
		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null)
			if (filtro.getDataFinal().before(filtro.getDataInicio())) {
				addMessage("", "Data final maior que a data de inicio", FacesMessage.SEVERITY_INFO);
			}

		return repositorio.findByFiltros(filtro);
	}

	public List<Orcamento> findOrcamentoByTitulo(String query) {
		return repositorio.findOrcamentoByTitulo(query);
	}

}
