package repositorio;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Compra;
import model.ControleExpedicao;
import model.Cotacao;
import model.Fornecedor;
import model.ItemCompra;
import model.ItemPedido;
import model.Pedido;

public class PedidoRepositorio{
	
	@Inject	
	private EntityManager manager;

	public PedidoRepositorio() {
	}

	public PedidoRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public Pedido getPedidoPorId(Long id) {
		return this.manager.find(Pedido.class, id);
	}
	
	public void salvar(Pedido pedido) {
		pedido.setControleExpedicao(this.manager.merge(pedido.getControleExpedicao()));
		this.manager.merge(pedido);
	}
	
	public void salvar(Pedido pedido, String args) {
		this.manager.merge(pedido);
	}
	
	public void salvarPedidoSuspenso(Pedido pedido) {
		//pedido.setControleExpedicao(this.manager.merge(pedido.getControleExpedicao()));
		this.manager.merge(pedido);
	}
	
	
	public void salvar(Pedido pedido, ControleExpedicao controleExpedicao) {
		this.manager.merge(controleExpedicao);
		this.manager.merge(pedido);
	}
	
	
	
	/*public void salvarControleExpedicao(Pedido pedido) {
		pedido.setControleExpedicao(this.manager.merge(pedido.getControleExpedicao()));
		this.manager.merge(pedido);
	}*/
	
	
	public List<ItemPedido> getItensPedido(Pedido pedido){
		String jpql = "from ItemPedido where pedido = :pedido order by descricaoProduto";
		Query query = manager.createQuery(jpql);
		query.setParameter("pedido", pedido);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}
	
	public List<Fornecedor> getFornecedoresParaSelectBox(Compra compra){
		StringBuilder jpql = new StringBuilder("select f.nome_fantasia as nomeFantasia, ");
					               jpql.append("sum(c.valor * c.quantidade) as total_valor, ");
					               jpql.append("sum(c.valor_desconto * c.quantidade) as total_desconto, ");
					               jpql.append("c.compra_id as compra, ");
					               jpql.append("c.fornecedor_id as fornecedor ");
					               jpql.append("from cotacao c left join fornecedor f on c.fornecedor_id = f.id ");
					               jpql.append("where compra_id = :compra ");
					               jpql.append("group by c.fornecedor_id ,f.nome_fantasia, c.compra_id"); 
					              
		 Query query =  manager.createNativeQuery(jpql.toString());
		 query.setParameter("compra", compra.getId());
		 List<Fornecedor> retorno =  new ArrayList<Fornecedor>();
		 List<Object[]> result = query.getResultList();
	 	 Fornecedor fornecedor =  new Fornecedor();
		
		for (Object[] object : result) {
		    fornecedor = new Fornecedor();
			fornecedor.setNomeFantasia(object[0] != null ? object[0].toString() : "");
		    fornecedor.setId(object[4] != null ? new Long(object[4].toString()) : null);
		    retorno.add(fornecedor);
		 }
		
		return retorno;
	}
	

	
	public List<ItemPedido> getItensPedidosByPedido(Pedido pedido){
		StringBuilder jpql = new StringBuilder("SELECT NEW ItemPedido(it.id,it.descricaoProduto,it.quantidade,it.valorUnitario,it.valorUnitarioComDesconto,it.unidade, it.descricaoComplementar)");
		jpql.append(" FROM  ItemPedido AS it");
		jpql.append(" where it.pedido = :pedido");
		TypedQuery<ItemPedido>  query = manager.createQuery(jpql.toString(), ItemPedido.class);
		query.setParameter("pedido", pedido);
		return query.getResultList().size() > 0  ?  query.getResultList() : (List) new ArrayList<ItemPedido>();
	}
	
	public List<Cotacao> getCotacoes(Fornecedor fornecedor,Compra compra){
		StringBuilder jpql = new StringBuilder("from  Cotacao c where c.fornecedor = :fornecedor and c.compra = :compra");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("fornecedor", fornecedor);
		query.setParameter("compra",compra);	
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<Compra>();
	}
	
	public List<Pedido> getPedidos(Compra compra){
		StringBuilder jpql = new StringBuilder("from  Pedido p where p.compra = :compra");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("compra", compra);
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<Pedido>();
	}
	
	public void remover(Pedido pedido){
		this.manager.remove(this.manager.merge(pedido));
	}	

}
