package repositorio;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Comunidade;
import model.UnidadeConservacao;

public class ComunidadeRepositorio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public ComunidadeRepositorio() {

	}

	public Comunidade findById(Long id) {
		return this.manager.find(Comunidade.class, id);
	}

	public void salvar(Comunidade comunidade) {
		try {
			this.manager.merge(comunidade);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}

	public void remover(Comunidade comunidade) {
		try {
			this.manager.remove(this.manager.find(Comunidade.class, comunidade.getId()));
		} catch (Exception e) {
			System.out.println(e.getCause().getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List<Comunidade> findAll() {
		String jpql = "SELECT NEW Comunidade(c.id, c.nome, c.mascara, c.unidadeConservacao.id, c.unidadeConservacao.nome, c.unidadeConservacao.mascara, c.setor, c.municipio.id, c.municipio.nome, c.nPessoa, c.nfamilia) from Comunidade c order by c.nome ASC";
		Query query = this.manager.createQuery(jpql);
		List<Comunidade> resultList = query.getResultList();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<Comunidade> getComunidadesInUC(Long id) {
		String jpql = "SELECT NEW Comunidade(c.id, c.nome) FROM Comunidade c where c.unidadeConservacao.id = :ucId order by c.nome ASC";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("ucId", id);
		List<Comunidade> resultList = query.getResultList();
		return resultList;
	}

}
