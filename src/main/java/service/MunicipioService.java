package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Municipio;
import repositorio.MunicipioRepositorio;

public class MunicipioService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Variaveis
	@Inject
	private MunicipioRepositorio unidadeConservacaoRepositorio;

	// Metodos

//	public void salvar(Municipio localidade) {
//		unidadeConservacaoRepositorio.update(localidade);
//
//	}

	@Transactional
	public Boolean salvar(Municipio localidade) {
		try {
			unidadeConservacaoRepositorio.salvar(localidade);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public Boolean delete(Municipio localidade) {

		return unidadeConservacaoRepositorio.delete(localidade);
	}

	public Municipio findById(Long idMunicipio) {
		return unidadeConservacaoRepositorio.findById(idMunicipio);
	}

	public List<Municipio> getListMunicipios() {

		return unidadeConservacaoRepositorio.getListMunicipios();
	}
	
//	public List<Municipio> ucAutocomplete(String query) {
//
//		return unidadeConservacaoRepositorio.ucAutocomplete(query);
//	}

}
