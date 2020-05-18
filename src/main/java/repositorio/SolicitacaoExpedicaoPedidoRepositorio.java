package repositorio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.ControleExpedicao;
import model.Pedido;
import model.SolicitacaoExpedicao;
import util.Filtro;

public class SolicitacaoExpedicaoPedidoRepositorio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

	public SolicitacaoExpedicaoPedidoRepositorio(){}
	
	public SolicitacaoExpedicaoPedidoRepositorio(EntityManager manager) { 
	   this.manager = manager;
	}
	
	public List<SolicitacaoExpedicao> getSolicitacoes(Filtro filtro){

		StringBuilder jpql = new StringBuilder("from SolicitacaoExpedicao se where se.dataEmissao between :data_inicio and :data_final \n ") ;
		
		if (filtro.getSolicitacaoExpedicaoID() != null) {
			jpql.append(" and se.id = :id \n");
		}
		
		Query query = manager.createQuery(jpql.toString());
		
		query.setParameter("data_inicio", filtro.getDataInicio());
		query.setParameter("data_final", filtro.getDataFinal());
		
		if (filtro.getSolicitacaoExpedicaoID() != null) {
			query.setParameter("id",filtro.getSolicitacaoExpedicaoID());
		}
		
	
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}
	
	public SolicitacaoExpedicao salvarSolicitacao(SolicitacaoExpedicao solicitacaoExpedicao){
		return this.manager.merge(solicitacaoExpedicao);
	}
	
	public ControleExpedicao salvarControleExpedicao(ControleExpedicao controleExpedicao){
		return this.manager.merge(controleExpedicao);
	}
	
	public Pedido getPedido(Long id){
		String jpql = "from Pedido where id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? (Pedido) query.getResultList().get(0) : null;
	}
	
	public List<Pedido> getPedidoBySolicitacao(Long id){
		String jpql = "from Pedido where solicitacaoExpedicao.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}
	
	public SolicitacaoExpedicao getSolicitacao(Long id){
		return this.manager.find(SolicitacaoExpedicao.class, id);
	}
	
	public void removerSolicitacao(SolicitacaoExpedicao solicitacao){
		this.manager.remove(this.manager.find(SolicitacaoExpedicao.class, solicitacao.getId()));
	}
	
	public void salvarPedido(Pedido pedido){
		this.manager.merge(pedido);
	}
	
}
