package service;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import auxiliar.RelatorioItensPorUc;
import model.UnidadeConservacao;
import repositorio.RelatorioRepositorio;
import util.Filtro;

/**
 * Autor: Italo Almeida 
 * Classe: Gestao de Relatórios
 * Objetivo: Gerir a regra de negocio referente a instancia do objeto
 * */

public class RelatorioService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private RelatorioRepositorio repositorio;

	
	public RelatorioService() {
	}

	public RelatorioService(RelatorioRepositorio repositorio){
		this.repositorio = repositorio;
	}
	
	public List<UnidadeConservacao> getUcs(){
		return repositorio.getUcs();
	}
	
	public List<RelatorioItensPorUc> getListagemRelatoriosDebens(Filtro filtro){
		return repositorio.getListagemParaRelatorio(filtro);
	}
	
	public void addMessage(String summary, String detail, Severity severity) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
}
