package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Cargo;
import model.ComponenteClass;
import model.Gestao;
import repositorio.CargoRepositorio;
import repositorio.ComponenteRepositorio;

public class CargoService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Inject
	private CargoRepositorio cargoRespositorio;
	
	public CargoService() {
		
	}
	
	public List<Cargo> findAll(){
		return cargoRespositorio.getCargos();
	}
	
//	public List<ComponenteClass> getComponenteAutoComplete(String query) {
//		return cargoRespositorio.buscarComponenteAutocomplete(query);
//	}
	
	@Transactional
	public void salvar(Cargo cargo){
		cargoRespositorio.salvar(cargo);	 
	}
	
	@Transactional
	public void remover(Cargo cargo) {
		cargoRespositorio.remover(cargo);
	}

	public Cargo findByid(Long id) {
		return cargoRespositorio.findById(id);
	}

}
