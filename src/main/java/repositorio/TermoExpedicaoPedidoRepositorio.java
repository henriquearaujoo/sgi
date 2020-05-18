package repositorio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Estado;
import model.ItemPedido;
import model.Localidade;
import model.Pedido;
import model.Projeto;
import model.TermoExpedicao;
import util.Filtro;

public class TermoExpedicaoPedidoRepositorio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

	public TermoExpedicaoPedidoRepositorio(){}
	
	public TermoExpedicaoPedidoRepositorio(EntityManager manager) { 
	   this.manager = manager;
	}
	
	
	public List<TermoExpedicao> getTermos(Filtro filtro){
		StringBuilder jpql = new StringBuilder("from TermoExpedicao t where 1 = 1 ");
		//jpql.append("and t.dataEmissao between :data_inicio and :data_final \n");		
		Query query = this.manager.createQuery(jpql.toString());
//		query.setParameter("data_inicio", filtro.getDataInicio());
//		query.setParameter("data_final", filtro.getDataFinal());
		return query.getResultList();
	}
	
	public TermoExpedicao salvarTermo(TermoExpedicao termo){
		return this.manager.merge(termo);
	}
	
	public TermoExpedicao getTermoById(Long id){
		return this.manager.find(TermoExpedicao.class, id);
	}
	
	public List<Pedido> getPedidosByTermo(Long id){
		String jpql = "from Pedido where  termo.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}
	
	public List<Estado> getEatados(Filtro filtro){
		StringBuilder jpql = new StringBuilder("from Estado");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0  ? query.getResultList() : new ArrayList<Estado>();
	}
	
	public List<Localidade> getMunicipioByEstado(Long id){
		StringBuilder jpql = new StringBuilder("from  Municipio where estado.id = :id");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", id);
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<Projeto>();
	}
	
	
	public Pedido getPedido(Long id){
		String jpql = "from Pedido where id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? (Pedido) query.getResultList().get(0) : null;
	}
	
	public void salvarPedido(Pedido pedido){
		this.manager.merge(pedido);
	}

	public List<ItemPedido> getItensPedidoByTermo(TermoExpedicao expedicao) {
		StringBuilder jpql = new StringBuilder("from  ItemPedido ip where ip.pedido.termo = :termo");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("termo", expedicao);
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<ItemPedido>();
	}

	public List<ItemPedido> getItensPedidoById(Long id) {
		StringBuilder jpql = new StringBuilder("from  ItemPedido where id = :id");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", id);
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<ItemPedido>();
	}
}
