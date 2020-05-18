package repositorio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.TransferenciaOverHead;

public class TransferenciaOverHeadRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public TransferenciaOverHeadRepositorio() {
	}

	public TransferenciaOverHead salvarTransferenciaOverHead(TransferenciaOverHead transf) {
		return this.manager.merge(transf);
	}

	public void removerTransferenciaOverHead(TransferenciaOverHead transf) {
		this.manager.remove(this.manager.merge(transf));
	}

	public List<TransferenciaOverHead> getListaTransferencia(TransferenciaOverHead transf) {
		String jpql = "from TransferenciaOverHead where doacaoPagadora = :doacao";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("doacao", transf.getDoacaoPagadora());
		return query.getResultList();
	}
	
	
	public BigDecimal getTotalTransferenciaOverheadPagos(TransferenciaOverHead transf) {
		StringBuilder jpql = new StringBuilder("select sum(valor) from TransferenciaOverHead where doacaoPagadora = :doacao and tipo = 0");
		
		if (transf.getId() != null) {
			jpql.append(" and id != :id_transf");
		}
		
		Query query = this.manager.createQuery(jpql.toString());
		
		if (transf.getId() != null) {
			query.setParameter("id_transf", transf.getId());
		}
		
		query.setParameter("doacao", transf.getDoacaoPagadora());
		return  query.getSingleResult() != null ? new BigDecimal(query.getSingleResult().toString()) : BigDecimal.ZERO;
	}
	
	public BigDecimal getTotalTransferenciaOverheadIndiretoPagos(TransferenciaOverHead transf) {
		StringBuilder jpql = new StringBuilder("select sum(valor) from TransferenciaOverHead where doacaoPagadora = :doacao and tipo = 1");
		
		if (transf.getId() != null) {
			jpql.append(" and id != :id_transf");
		}
		
		Query query = this.manager.createQuery(jpql.toString());
		
		if (transf.getId() != null) {
			query.setParameter("id_transf", transf.getId());
		}
		
		query.setParameter("doacao", transf.getDoacaoPagadora());
		return  query.getSingleResult() != null ? new BigDecimal(query.getSingleResult().toString()) : BigDecimal.ZERO;
	}

	public BigDecimal getValorByOrcamento(Long orcamento) {
		
		
		
		
		return null;
	}

}
