package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.UnidadeConservacao;
import repositorio.UnidadeConservacaoRepositorio;

public class UnidadeConservacaoService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Variaveis
	@Inject
	private UnidadeConservacaoRepositorio unidadeConservacaoRepositorio;

	// Metodos

	@Transactional
	public Boolean salvar(UnidadeConservacao localidade) {
		try {
			unidadeConservacaoRepositorio.salvar(localidade);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@Transactional
	public Boolean delete(UnidadeConservacao localidade) {
		return unidadeConservacaoRepositorio.delete(localidade);
	}

	public UnidadeConservacao findById(Long idUnidadeConservacao) {
		return unidadeConservacaoRepositorio.findById(idUnidadeConservacao);
	}

	public List<UnidadeConservacao> getListUc() {

		return unidadeConservacaoRepositorio.getListUc();
	}
	
	public List<UnidadeConservacao> ucAutocomplete(String query) {

		return unidadeConservacaoRepositorio.ucAutocomplete(query);
	}
	
	

}
