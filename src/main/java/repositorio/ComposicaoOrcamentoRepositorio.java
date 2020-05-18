package repositorio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.AlocacaoRendimento;
import model.TransferenciaOverHead;

public class ComposicaoOrcamentoRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public ComposicaoOrcamentoRepositorio() {
	}



	public BigDecimal getValorOverheadByOrcamento(Long orcamento) {
		String jpql = "select sum(to.valor) from TransferenciaOverHead to where to.doacaoRecebedora.id = :id and to.tipo = 0";
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", orcamento);
		return  query.getSingleResult() != null ? new BigDecimal(query.getSingleResult().toString()) : BigDecimal.ZERO;
	}
	
	
	public BigDecimal getValorOverheadPagoByOrcamento(Long orcamento) {
		String jpql = "select sum(to.valor) from TransferenciaOverHead to where to.doacaoPagadora.id = :id and to.tipo = 0";
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", orcamento);
		return  query.getSingleResult() != null ? new BigDecimal(query.getSingleResult().toString()) : BigDecimal.ZERO;
	}
	
	
	public BigDecimal getValorIndiretoByOrcamento(Long orcamento) {
		String jpql = "select sum(to.valor) from TransferenciaOverHead to where to.doacaoRecebedora.id = :id and to.tipo = 1";
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", orcamento);
		return  query.getSingleResult() != null ? new BigDecimal(query.getSingleResult().toString()) : BigDecimal.ZERO;
	}
	
	
	public BigDecimal getValorRendimentoByOrcamento(Long orcamento) {
		String jpql = "select sum(ar.valor) from AlocacaoRendimento ar where ar.orcamento.id = :id";
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", orcamento);
		return  query.getSingleResult() != null ? new BigDecimal(query.getSingleResult().toString()) : BigDecimal.ZERO;
	}
	
	
	
	public List<TransferenciaOverHead> getListOverheadByOrcamento(Long orcamento) {
		String jpql = "from TransferenciaOverHead to where to.doacaoRecebedora.id = :id and to.tipo = 0";
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", orcamento);
		return  query.getResultList();
	}
	
	
	public List<TransferenciaOverHead> getListOverheadIndiretoByOrcamento(Long orcamento) {
		String jpql = "from TransferenciaOverHead to where to.doacaoRecebedora.id = :id and to.tipo = 1";
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", orcamento);
		return  query.getResultList();
	}
	
	
	public List<AlocacaoRendimento> getListRendimentoByOrcamento(Long orcamento) {
		String jpql = "from AlocacaoRendimento ar where ar.orcamento.id = :id";
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", orcamento);
		return  query.getResultList();
	}
	
}
