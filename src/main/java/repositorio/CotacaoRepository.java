package repositorio;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Compra;
import model.Cotacao;
import model.CotacaoAuxiliar;
import model.CotacaoParent;
import model.EspelhoPedido;
import model.Fornecedor;
import model.ItemCompra;
import model.ItemPedido;
import model.Lancamento;
import model.LancamentoAcao;
import model.Pedido;
import model.Produto;
import model.StatusCompra;

public class CotacaoRepository {

	@Inject
	private EntityManager manager;

	public CotacaoRepository() {

	}

	public CotacaoRepository(EntityManager manager) {
		this.manager = manager;
	}

	public Cotacao getCotacaoPorId(Long id) {
		return this.manager.find(Cotacao.class, id);
	}

	public void salvar(Cotacao cotacao) {
		this.manager.merge(cotacao);
	}
	
	
	public void salvarCotacaoParent(CotacaoParent cotacaoParent) {
		this.manager.merge(cotacaoParent);
	}
	
	
	public Pedido getPedidoByCompraAndForneced(Long fornecedor, Long compra) {
		
		String jpql = "from Pedido where compra.id = :id_compra and fornecedor.id = :id_fornecedor ";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id_compra", compra);
		query.setParameter("id_fornecedor", fornecedor);
		
		return query.getResultList().size() > 0 ? (Pedido) query.getResultList().get(0) : null;
		
	}

	public Boolean excluirCotacao(Long idCompra, Long idFornecedor) {

		String hql = "delete Cotacao c where c.fornecedor.id = :id_fornecedor and c.compra.id = :id_compra ";
		Query query = this.manager.createQuery(hql);
		query.setParameter("id_fornecedor", idFornecedor);
		query.setParameter("id_compra", idCompra);

		try {
			int reuslt = query.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Boolean excluirCotacaoParent(Long idCompra, Long idFornecedor) {

		String hql = "delete CotacaoParent c where c.fornecedor.id = :id_fornecedor and c.compra.id = :id_compra ";
		Query query = this.manager.createQuery(hql);
		query.setParameter("id_fornecedor", idFornecedor);
		query.setParameter("id_compra", idCompra);

		try {
			int reuslt = query.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	
	public void excluirCotacao(Cotacao cotacao){
		this.manager.remove(cotacao);
	}

	public Boolean buscarItemPedidoByCotacao(Long cotacao) {

		String jpql = "from ItemPedido";

		return true;
	}

	public List<Cotacao> getCotacoesByCompraByFornecedor(Long fornecedor, Long compra) {
		String jpql = "from Cotacao c where c.fornecedor.id = :fornecedor and c.compra = :compra ";
		Query query = manager.createQuery(jpql);
		query.setParameter("fornecedor", fornecedor);
		query.setParameter("compra", compra);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();

	}

	public List<Cotacao> getCotacoesHouveEscolha(Long fornecedor, Compra compra) {

		String jpql = "from Cotacao c where c.fornecedor.id = :fornecedor and c.compra = :compra and c.houveEscolha = :houve";
		Query query = manager.createQuery(jpql);
		query.setParameter("fornecedor", fornecedor);
		query.setParameter("compra", compra);
		query.setParameter("houve", true);

		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();

	}

	public Cotacao verificarCotacaoExiste(Cotacao cotacao) {
		StringBuilder jpql = new StringBuilder();

		jpql.append("from Cotacao c where c.id != :id and c.houveEscolha = :param ");
		jpql.append("and c.compra = :compra ");
		jpql.append("and c.itemCompra = :item_compra ");

		Query query = manager.createQuery(jpql.toString());

		query.setParameter("id", cotacao.getId());
		query.setParameter("param", true);
		query.setParameter("compra", cotacao.getCompra());
		query.setParameter("item_compra", cotacao.getItemCompra());

		return query.getResultList().size() > 0 ? (Cotacao) query.getResultList().get(0) : null;

	}
	
	
	
	public Boolean excluirCotacaoByItem(ItemCompra item) {

			String hql = "delete Cotacao c where c.itemCompra.id = :id_item  ";
	
			Query query = this.manager.createQuery(hql);
		
			query.setParameter("id_item", item.getId());
		
			try {
				int reuslt = query.executeUpdate();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		
	}
	
	public Boolean excluirCotacaoByItem(Long item) {

		String hql = "delete Cotacao c where c.itemCompra.id = :id_item  ";

		Query query = this.manager.createQuery(hql);
	
		query.setParameter("id_item", item);
	
		try {
			int reuslt = query.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	
}


	public List<CotacaoAuxiliar> getCotacaoAuxiliar(Long id) {
		StringBuilder jpql = new StringBuilder("select f.nome_fantasia as nomeFantasia, ");
		jpql.append("sum(c.valor * c.quantidade) as total_valor, ");
		jpql.append("sum(c.valor_desconto * c.quantidade) as total_desconto, ");
		jpql.append("c.compra_id as compra, ");
		jpql.append("c.fornecedor_id as fornecedor ");
		jpql.append("from cotacao c left join fornecedor f on c.fornecedor_id = f.id ");
		jpql.append("where compra_id = :compra ");
		jpql.append("group by c.fornecedor_id ,f.nome_fantasia, c.compra_id ");
		jpql.append("order by total_desconto LIMIT 5");
		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("compra", id);
		List<CotacaoAuxiliar> retorno = new ArrayList<CotacaoAuxiliar>();
		List<Object[]> result = query.getResultList();
		CotacaoAuxiliar cotacao = new CotacaoAuxiliar();

		for (Object[] object : result) {
			cotacao = new CotacaoAuxiliar();
			cotacao.setFornecedor(object[0] != null ? object[0].toString() : "");
			cotacao.setTotalValor(object[1] != null ? new BigDecimal(object[1].toString()) : BigDecimal.ZERO);
			cotacao.setTotalDesconto(object[2] != null ? new BigDecimal(object[2].toString()) : BigDecimal.ZERO);
			cotacao.setCompraId(object[3] != null ? new Long(object[3].toString()) : null);
			cotacao.setFornecedorId(object[4] != null ? new Long(object[4].toString()) : null);
			retorno.add(cotacao);
		}

		return retorno;
	}

	public List<EspelhoPedido> getEspelhoPedido(Compra compra) {
		StringBuilder jpql = new StringBuilder("select forn.nome_fantasia as fornecedor,   ");
		jpql.append("cot.compra_id as compra, sum(cot.quantidade * cot.valor) as valor, ");
		jpql.append("sum(cot.quantidade * cot.valor_desconto) as valor_desconto, ");
		jpql.append("forn.id as fornecedorID ");
		jpql.append("from cotacao cot left join fornecedor forn on cot.fornecedor_id = forn.id ");
		jpql.append("where cot.compra_id = :compra and  cot.houveescolha = true ");

		jpql.append("group by forn.id, forn.nome_fantasia, cot.compra_id  order by forn.nome_fantasia ");

		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("compra", compra.getId());
		List<EspelhoPedido> retorno = new ArrayList<EspelhoPedido>();
		List<Object[]> result = query.getResultList();
		EspelhoPedido espelhoPedido = new EspelhoPedido();

		for (Object[] object : result) {
			espelhoPedido = new EspelhoPedido();
			espelhoPedido.setFornecedor(object[0] != null ? object[0].toString() : "");
			espelhoPedido.setCompraId(object[1] != null ? new Long(object[1].toString()) : 0l);
			espelhoPedido.setTotalValor(object[2] != null
					? new BigDecimal(object[2].toString()).setScale(2, RoundingMode.HALF_EVEN) : BigDecimal.ZERO);
			espelhoPedido.setTotalDesconto(object[3] != null
					? new BigDecimal(object[3].toString()).setScale(2, RoundingMode.HALF_EVEN) : BigDecimal.ZERO);
			espelhoPedido.setFornecedorId(object[4] != null ? new Long(object[4].toString()) : 0l);
			retorno.add(espelhoPedido);
		}

		return retorno;
	}

	public ItemCompra getValoresDeItem(Long fornecedorId, Long itemCompraId) {
		StringBuilder jpql = new StringBuilder(
				"select c.quantidade as quantidade, c.valor, c.valor_desconto, c.houveescolha ");
		jpql.append("from cotacao c join item_compra it on c.itemcompra_id = it.id  ");
		jpql.append("where c.fornecedor_id = :idfornecedor and c.itemcompra_id = :iditem ");

		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("idfornecedor", fornecedorId);
		query.setParameter("iditem", itemCompraId);

		if (query.getResultList().isEmpty()) {
			ItemCompra it = new ItemCompra();
			it.setQuantidade(Double.valueOf(0));
			it.setValor(new BigDecimal(0));
			it.setValorDesconto(new BigDecimal(0));
			return it;
		}
		Object[] result = (Object[]) query.getSingleResult();

		ItemCompra it = new ItemCompra();
		it.setQuantidade(Double.valueOf(result[0].toString()));
		it.setValor(new BigDecimal(result[1].toString()));
		it.setValorDesconto(new BigDecimal(result[2].toString()));

		return it;

	}

	public List<Cotacao> getCotacoes(Fornecedor fornecedor, Compra compra) {
		StringBuilder jpql = new StringBuilder(
				"from  Cotacao c where c.fornecedor.id = :fornecedor and c.compra.id = :compra");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("fornecedor", fornecedor.getId());
		query.setParameter("compra", compra.getId());
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Compra>();
	}

	public Boolean verificarSeHouvePedido(Long item) {
		StringBuilder jpql = new StringBuilder("select ic.id from ");
		jpql.append(" item_compra  ic left join cotacao c on ic.id = c.itemcompra_id ");
		jpql.append(" where ic.id = :param and c.houvepedido is true");
		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("param", item);
		return query.getResultList().size() > 0 ? true : false;
	}
	
	public Boolean verificarSeHouvePedidoNoItem(Long item) {
		StringBuilder jpql = new StringBuilder("select ic.id from item_pedido ic where ic.item_compra_id = "+ item);
		Query query = manager.createNativeQuery(jpql.toString());
		//query.setParameter("param", item);
		return query.getResultList().size() > 0 ? true : false;
	}

	public List<Cotacao> getCotacoes(Long fornecedor, Compra compra) {
		StringBuilder jpql = new StringBuilder(
				"from  Cotacao c where c.fornecedor.id = :fornecedor and c.compra = :compra \n");
		jpql.append(" and c.houveEscolha = :houve");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("fornecedor", fornecedor);
		query.setParameter("compra", compra);
		query.setParameter("houve", true);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Compra>();
	}

	public Cotacao getCotacao(Fornecedor fornecedor, Compra compra, ItemCompra item) {
		StringBuilder jpql = new StringBuilder(
				"from  Cotacao c where c.fornecedor = :fornecedor and c.compra = :compra and c.itemCompra = :item_compra");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("fornecedor", fornecedor);
		query.setParameter("compra", compra);
		query.setParameter("item_compra", item);
		query.setMaxResults(1);

		return query.getResultList().size() > 0 ? (Cotacao) query.getResultList().get(0) : null;
	}
	
	
	public CotacaoParent getCotacaoParent(Fornecedor fornecedor, Compra compra) {
		
		StringBuilder jpql = new StringBuilder(
				"from  CotacaoParent c where c.fornecedor = :fornecedor and c.compra = :compra");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("fornecedor", fornecedor);
		query.setParameter("compra", compra);
		query.setMaxResults(1);

		return query.getResultList().size() > 0 ? (CotacaoParent) query.getResultList().get(0) : null;
	}

	public List<Cotacao> getCotacoes(ItemCompra itemCompra) {
		StringBuilder jpql = new StringBuilder(
				"from  Cotacao c where c.itemCompra = :itemCompra  and c.valorDesconto > :zero order by c.valorDesconto");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("itemCompra", itemCompra);
		query.setParameter("zero", new BigDecimal(0));
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Compra>();
	}

	public Cotacao getCotacoesByFornecedorItemECompra(Long idFornecedor, Long itemCompra, Long compra) {
		StringBuilder jpql = new StringBuilder(
				"from  Cotacao c where c.itemCompra.id = :itemCompra  and c.valorDesconto > :zero  ");
		jpql.append(" and c.fornecedor.id = :id_fornecedor ");
		jpql.append(" and c.compra.id = :id_compra ");
		jpql.append("order by c.valorDesconto ");

		Query query = manager.createQuery(jpql.toString());
		query.setParameter("itemCompra", itemCompra);
		query.setParameter("zero", new BigDecimal(0));
		query.setParameter("id_fornecedor", idFornecedor);
		query.setParameter("id_compra", compra);
		return query.getResultList().size() > 0 ? (Cotacao) query.getResultList().get(0) : null;
	}

	public List<Fornecedor> getFornecedores(String s) {
		StringBuilder jpql = new StringBuilder(
				"from  Fornecedor f where    ");
		jpql.append(" (lower(f.nomeFantasia) like lower(:nome)) ");
		jpql.append(" or ");
		jpql.append(" (lower(f.razaoSocial) like lower(:nome)) ");
		jpql.append(" or ");
		jpql.append(" (f.cnpj like :nome)) ");
		
		jpql.append(" and f.ativo is true ");
					
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		query.setMaxResults(20);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Fornecedor>();
	}

	public void remover(Lancamento lancamento) {
		this.manager.remove(this.manager.merge(lancamento));
	}

	public void removerTodosByCompraEFornecedor(Fornecedor fornecedor, Compra compra) {
		StringBuilder jpql = new StringBuilder(
				"delete from  cotacao where compra_id = :compra and fornecedor_id = :fornecedor ");
		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("compra", compra.getId());
		query.setParameter("fornecedor", fornecedor.getId());
		query.executeUpdate();
	}

	public void deletarItemPedidoCompra(Long id) {
		StringBuilder jpql = new StringBuilder(
				"update cotacao set houvepedido = :houvepedido, houveescolha = :houveescolha  where id = :id");
		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("houvepedido", false);
		query.setParameter("houveescolha", false);
		query.setParameter("id", id);
		query.executeUpdate();
	}
	
	public void desfazerEscolhaDeCotacaoPedido(Long id) {
		StringBuilder jpql = new StringBuilder(
				"update cotacao set houvepedido = :houvepedido, houveescolha = :houveescolha  where id = :id");
		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("houvepedido", false);
		query.setParameter("houveescolha", false);
		query.setParameter("id", id);
		query.executeUpdate();
	}
	
	public Boolean excluirItemPedido(ItemPedido item) {
		this.manager.remove(this.manager.find(ItemPedido.class, item.getId()));
		return true;
	}

	public void deletarItensEspelhoPedidoCompra(Long idFornecedor, Long idCompra) {
		StringBuilder jpql = new StringBuilder(
				"update cotacao set houvepedido = :houvepedido, houveescolha = :houveescolha  where compra_id = :compra and fornecedor_id = :fornecedor");
		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("houvepedido", false);
		query.setParameter("houveescolha", false);
		query.setParameter("compra", idCompra);
		query.setParameter("fornecedor", idFornecedor);
		query.executeUpdate();
	}

	// Muda o status da cotação, para especificar que não houve pedido.
	public void mudarStatusCotacao(Long id) {
		StringBuilder jpql = new StringBuilder("update cotacao set houvepedido = :houvepedido  where id = :id");
		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("houvepedido", false);
		// query.setParameter("houveescolha", false);
		query.setParameter("id", id);
		query.executeUpdate();
	}
	
	public void mudarStatusCotacaoEscolhaEEnvio(Long id) {
		StringBuilder jpql = new StringBuilder("update cotacao set houvepedido = :houvepedido, houveescolha = :houveescolha  where id = :id");
		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("houvepedido", false);
		query.setParameter("houveescolha", false);
		query.setParameter("id", id);
		query.executeUpdate();
	}
	
	public boolean mudarStatus(Long id, StatusCompra statusCompra) {

		try {

			Compra compra = manager.find(Compra.class, id);
			compra.setStatusCompra(statusCompra);
			manager.merge(compra);
			/*
			 * String jpql =
			 * "update Compra c set c.statusCompra = :statusCompra where c.id = :id" ; Query
			 * query = manager.createQuery(jpql); query.setParameter("statusCompra",
			 * statusCompra); query.setParameter("id", id); query.executeUpdate();
			 */
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public boolean verificarExisteItemPedidoByCotacao(Long id) {
		String jpql = "from ItemPedido where cotacao.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList().isEmpty();
	}
	
	public boolean verificarExisteItensPedidoByCotacoes(Long idFornecedor, Long idCompra) {
		String jpql = "select count(*) from item_pedido   where cotacao_id in (select c.id from cotacao c where fornecedor_id = :fornecedor and compra_id = :compra);";
		Query query = this.manager.createNativeQuery(jpql);
		query.setParameter("compra", idCompra);
		query.setParameter("fornecedor", idFornecedor);
		Long i = ((BigInteger) query.getSingleResult()).longValue();
		return i > 0 ? true : false;
	}
	
	

	
	public void removeAllLog(Long lancamento) {
		String jpql = "delete from LogStatus l where l.lancamento.id = :id";
		Query query =  this.manager.createQuery(jpql);
		query.setParameter("id", lancamento);
		query.executeUpdate();
	}
	
	public void removeAllRecurso(Long lancamento) {
		String jpql = "delete from LancamentoAcao la where la.lancamento.id = :id";
		Query query =  this.manager.createQuery(jpql);
		query.setParameter("id", lancamento);
		query.executeUpdate();
	}
	
	public void removeAllRecursoPagamento(Long lancamento) {
		String jpql = "delete from PagamentoLancamento pa where pa.lancamentoAcao.id = :id";
		Query query =  this.manager.createQuery(jpql);
		query.setParameter("id", lancamento);
		query.executeUpdate();
	}
	
	public void removeAllExpedicao(Long lancamento) {
		String jpql = "delete from ControleExpedicao ce where ce.pedido.id = :id";
		Query query =  this.manager.createQuery(jpql);
		query.setParameter("id", lancamento);
		query.executeUpdate();
	}
	
	
	public List<Cotacao> getValorCotacao(Long idForn, Long compra, Long itemCompra) {
		StringBuilder jpql = new StringBuilder("SELECT NEW Cotacao(c.id,c.valorDesconto,c.valor)");
		jpql.append(" FROM  Cotacao AS c");
		jpql.append(" where c.fornecedor.id = :fornecedor and c.compra.id = :compra and c.itemCompra.id = :itemCompra");

		TypedQuery<Cotacao> query = manager.createQuery(jpql.toString(), Cotacao.class);
		query.setParameter("fornecedor", idForn);
		query.setParameter("compra", compra);
		query.setParameter("itemCompra", itemCompra);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Cotacao>();
	}

	public List<Cotacao> getCotacaoEscolha(Long idForn, Long compra, Long itemCompra) {
		StringBuilder jpql = new StringBuilder("SELECT NEW Cotacao(c.id,c.houveEscolha)");
		jpql.append(" FROM  Cotacao AS c");
		jpql.append(" where c.fornecedor.id = :fornecedor and c.compra.id = :compra and c.itemCompra.id = :itemCompra");

		TypedQuery<Cotacao> query = manager.createQuery(jpql.toString(), Cotacao.class);
		query.setParameter("fornecedor", idForn);
		query.setParameter("compra", compra);
		query.setParameter("itemCompra", itemCompra);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Cotacao>();
	}

	public void deletarLancamentoFormaDePagamento(Long id) {
		manager.remove(manager.find(LancamentoAcao.class, id));
	}

	public Boolean verificarSeHaCotacao(Compra compra) {
		String jpql = "from Cotacao where compra = :compra";
		Query query = manager.createQuery(jpql);
		query.setParameter("compra", compra);
		query.setMaxResults(1);
		return query.getResultList().size() > 0 ? true : false;
	}

}
