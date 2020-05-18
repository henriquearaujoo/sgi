package repositorio;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Imposto;
import model.PagamentoLancamento;

public class DetalhamentoRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public DetalhamentoRepositorio() {
	}


	public List<Imposto> buscarImpostoPorLancamento(Long id) {
		StringBuilder jpql = new StringBuilder("from Imposto as i where 1 = 1 ");

		jpql.append(" and i.pagamentoLancamento.lancamentoAcao.lancamento.id = :id ");

		jpql.append(" order by i.id ");

		Query query = this.manager.createQuery(jpql.toString());

		query.setParameter("id", id);

		return query.getResultList();
	}
	
	
	public List<PagamentoLancamento> buscarParcelasPorLancamento(Long id) {
		StringBuilder jpql = new StringBuilder("from PagamentoLancamento as p where 1 = 1 ");

		jpql.append(" and p.lancamentoAcao.lancamento.id = :id ");

		jpql.append(" order by p.dataPagamento ");

		Query query = this.manager.createQuery(jpql.toString());

		query.setParameter("id", id);

		return query.getResultList();
	}
	


}
