package service;

import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;

import anotacoes.Transactional;

import model.ComponenteClass;
import model.SubComponente;
import repositorio.ComponenteRepositorio;

public class ComponenteService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ComponenteRepositorio componenteRepositorio;

	public ComponenteService() {

	}

	public List<ComponenteClass> findAllComponente() {
		return this.componenteRepositorio.getComponentes();
	}

	public List<SubComponente> findAllSubcomponente() {
		return this.componenteRepositorio.getSubComponentes();
	}

	@Transactional
	public void salvar(ComponenteClass componente) {
		this.componenteRepositorio.salvar(componente);
	}

	@Transactional
	public void remover(ComponenteClass componente) {
		this.componenteRepositorio.remover(componente);
	}

	@Transactional
	public void salvar(SubComponente subcomponente) {
		this.componenteRepositorio.salvar(subcomponente);
	}

	@Transactional
	public void remover(SubComponente subcomponente) {
		this.componenteRepositorio.remover(subcomponente);
	}

	public ComponenteClass findByid(Long id) {
		return this.componenteRepositorio.findById(id);
	}

	public List<ComponenteClass> completeComponente(String query) {
		return this.componenteRepositorio.buscarComponenteAutocomplete(query);
	}

}
