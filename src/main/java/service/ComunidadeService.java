package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Comunidade;
import repositorio.ComunidadeRepositorio;

public class ComunidadeService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ComunidadeService() {
		// TODO Auto-generated constructor stub
	}

	@Inject
	public ComunidadeRepositorio comunidadeRepositorio;

	@Transactional
	public void salvar(Comunidade comunidade) throws Exception {
		try {
			// Buscar as comunidades do banco que pertencem à UC selecionada
			List<Comunidade> comunidadesInUc = this.comunidadeRepositorio
					.getComunidadesInUC(comunidade.getUnidadeConservacao().getId());
			
			// Verifica se a comunidade já foi cadastrada na UC relacionada.
			for(int i = 0; i < comunidadesInUc.size(); i++) {
				if(comunidade.getNome().equals(comunidadesInUc.get(i).getNome())) {
					throw new Exception("comunidadeJaCadastrada");
				}
			}
			
			this.comunidadeRepositorio.salvar(comunidade);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Transactional
	public void remover(Comunidade comunidade) {
		this.comunidadeRepositorio.remover(comunidade);
	}

	public List<Comunidade> findAll() {
		return this.comunidadeRepositorio.findAll();
	}

}
