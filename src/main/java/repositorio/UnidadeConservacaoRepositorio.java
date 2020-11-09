package repositorio;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import anotacoes.Transactional;
import model.Localidade;
import model.UnidadeConservacao;

public class UnidadeConservacaoRepositorio {

	@Inject
	private EntityManager manager;

	public UnidadeConservacaoRepositorio() {
	}

	public UnidadeConservacao findById(Long id) {
		return this.manager.find(UnidadeConservacao.class, id);
	}

	public Localidade findByIdLocalidade(Long idLocalidade) {
		return manager.find(Localidade.class, idLocalidade);
	}

	@Transactional
	public void update(Localidade localidade) {
		manager.merge(localidade);

	}

	@Transactional
	public Boolean delete(UnidadeConservacao localidade) {
		try {
			this.manager.remove(this.manager.find(Localidade.class, localidade.getId()));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Transactional
	public void salvar(UnidadeConservacao localidade) {
		try {
			this.manager.merge(localidade);
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public List<UnidadeConservacao> getListUc() {
		String jpql = "SELECT NEW UnidadeConservacao(uc.id,uc.nome,uc.mascara,uc.regional.id,uc.regional.nome,uc.nomeAssociacao,uc.presidenteAssociacao) from UnidadeConservacao uc";
		Query query = manager.createQuery(jpql);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<UnidadeConservacao> ucAutocomplete(String queryStr) {
		String jpql = "from UnidadeConservacao uc where lower(uc.nome) like :nome";
		Query query = manager.createQuery(jpql);
		query.setParameter("nome", "%" + queryStr.toLowerCase() + "%");
		return query.getResultList();
	}
}
