package repositorio;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import model.FormularioClimaOrgan;

public class ClimaRepositorio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Inject
	private EntityManager manager;

	public ClimaRepositorio() {
	}

	
	public FormularioClimaOrgan salvar(FormularioClimaOrgan formulario){
		return this.manager.merge(formulario);
	}
	
}
