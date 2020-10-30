package repositorio;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import anotacoes.Transactional;
import model.Localidade;
import model.UnidadeConservacao;

public class UnidadeConservacaoRepositorio {

	private EntityManager manager;

	public UnidadeConservacaoRepositorio() {
	}

	@Inject
	public UnidadeConservacaoRepositorio(EntityManager manager) {
		this.manager = manager;
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
	public Boolean delete(Localidade localidade) {
		try {
			manager.remove(this.manager.find(Localidade.class, localidade.getId()));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void salvar(Localidade localidade) {
		this.manager.merge(localidade);
	}

	@SuppressWarnings("unchecked")
	public List<UnidadeConservacao> getListUc() {
		String jpql = "from UnidadeConservacao uc";
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
