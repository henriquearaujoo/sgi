package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Pais;
import model.UnidadeConservacao;
import model.Estado;
import model.Localidade;
import model.User;
import repositorio.LocalidadeRepositorio;

public class LocalidadeService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Variaveis
	@Inject
	private LocalidadeRepositorio localidadeRepositorio;
	
	//Metodos
	public List<Localidade> carregaLocalidade() {
		
		return localidadeRepositorio.carregaLocalidade();
	}
	
	public void salvar(Localidade localidade) {
		localidadeRepositorio.update(localidade);
		
	}
	
	@Transactional
	public Boolean Salvar(Localidade localidade){
		try{
			localidadeRepositorio.salvar(localidade);
			return true;
		}catch(Exception e){
			return false;
		}
		
		
	}

	public Boolean delete(Localidade localidade) {
		
		return localidadeRepositorio.delete(localidade);
	}

	public Localidade findById(Long idLocalidade) {
		return localidadeRepositorio.findByIdLocalidade(idLocalidade);
	}

	public List<Estado> getListEstado() {
		
		return localidadeRepositorio.getListEstado();
	}

	public List<Pais> getListPais() {
		
		return localidadeRepositorio.getListPais();
	}

	public List<UnidadeConservacao> getListUc() {
		
		return localidadeRepositorio.getListUc();
	}
	
	

}
