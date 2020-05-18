package repositorio;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Colaborador;
import model.Comunidade;
import model.ControleExpedicao;
import model.Estado;
import model.Fornecedor;
import model.ItemPedido;
import model.LancamentoAcao;
import model.Localidade;
import model.Pedido;
import model.Produto;
import model.Projeto;
import util.Filtro;



public class ControleExpedicaoRepositorio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Inject	
	private EntityManager manager;
	
	
	public ControleExpedicaoRepositorio(){}
	
	public ControleExpedicaoRepositorio(EntityManager manager){
		this.manager = manager;
	}
	
	public Long getNumeroTermo(Long id) {
		String hql = "select p.termo_id from lancamento as p where p.id = :id";
		Query query = this.manager.createNativeQuery(hql).setParameter("id", id);
		if (query.getSingleResult() != null) {
			Long value = ((BigInteger) query.getSingleResult()).longValue();
			return value;
		}
		return null;
	}
	
	
	public ControleExpedicao findByid(Long id){
		return this.manager.find(ControleExpedicao.class, id);
	}
	
	public List<ItemPedido> buscarItens(Long id){
		String jpql = "SELECT NEW ItemPedido(i.descricaoProduto) FROM ItemPedido i where i.pedido.id = :id";
		Query query = manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList().isEmpty() ? new ArrayList<>() : query.getResultList();
	}
	
	public Boolean remover(ControleExpedicao controleExpedicao){
		try {
			this.manager.remove(controleExpedicao);
			return true;
		} catch (Exception e) {
			e.getMessage();
			return false;
		}		
	}
	
	public ControleExpedicao findControleExpedByPedido(Long id){
		String jpql = "from ControleExpedicao c where c.pedido.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? (ControleExpedicao) query.getResultList().get(0) : new ControleExpedicao();
	}
	

	
	public List<Pedido> buscarPedidos(Filtro filtro){
		StringBuilder jpql = new StringBuilder("from Pedido p where c.dataAceitacaoExpedicao between :data_inicio  and :data_final \n");
		
		if (filtro.getLancamentoID() != null) {
			jpql.append(" and c.pedido.id = :id_pedido");
		}
		
		if(filtro.getLocalidadeID() != null){
			jpql.append(" and c.pedido.localidade.id = :id_localidade");
		}
		
		
		if(filtro.getGestaoID() != null){
			jpql.append(" and c.pedido.gestao.id = :id_gestao");
		}
		
		if(filtro.getLocalizacaoPedido() != null){
			jpql.append(" and c.localizacaoPedido = :rastreamento");
		}
		
		if(filtro.getSolicitanteID() != null){
			jpql.append(" and c.pedido.solicitante.id = :solicitante");			
		}
		
		if(filtro.getFornecedorID() != null){
			jpql.append(" and c.pedido.fornecedor.id = :fornecedor");
		}
		
		Query query = this.manager.createQuery(jpql.toString());
		
		
		query.setParameter("data_inicio", filtro.getDataInicio());
		query.setParameter("data_final", filtro.getDataFinal());
		
		if (filtro.getLancamentoID() != null) {
		  query.setParameter("id_pedido", filtro.getLancamentoID());
		}
		
		if(filtro.getLocalidadeID() != null){
			query.setParameter("id_localidade", filtro.getLocalidadeID());
		}
		
		
		if(filtro.getGestaoID() != null){
			query.setParameter("id_gestao", filtro.getGestaoID());
		}
		
		
		if(filtro.getLocalizacaoPedido() != null){
			query.setParameter("rastreamento", filtro.getLocalizacaoPedido());
		}
		
		if(filtro.getSolicitanteID() != null){
			query.setParameter("solicitante", filtro.getSolicitanteID());			
		}
		
		if(filtro.getFornecedorID() != null){
			query.setParameter("fornecedor", filtro.getFornecedorID());
		}
		
		return query.getResultList();

	}

	
	public String buscarUC(Long id){
		String jpql = "SELECT NEW Comunidade(c.unidadeConservacao.mascara) FROM Comunidade c where c.id = :id";
		Query query = manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList().isEmpty() ? "" : ((Comunidade) query.getResultList().get(0)).getMascara();
	}
	
	
	public List<ControleExpedicao> getExpedicoes(Filtro filtro){
		//StringBuilder jpql = new StringBuilder("from ControleExpedicao c where c.dataAceitacaoExpedicao between :data_inicio  and :data_final \n");
		
		
		
		StringBuilder jpql = new StringBuilder("SELECT NEW ControleExpedicao(c.id");
		jpql.append(",c.pedido.id");// idPedido Long
		jpql.append(",c.pedido.compra.id");// idCompra long
		jpql.append(",c.pedido.tipoLocalidade");// tipoLocalidade TipoLocalidade
		jpql.append(",c.pedido.localidade.mascara");// mascara String
		jpql.append(",c.pedido.localidade.id"); //unidadeDeConservacao String
		jpql.append(",c.pedido.solicitante.nome");// nomeSolicitante string
		jpql.append(",c.pedido.valorTotalComDesconto");// valorTotalDesconto BigDecimal
		jpql.append(",c.pedido.fornecedor.nomeFantasia");// nomeFantasia
		jpql.append(",c.pedido.dataEntrega");// dataEntrega Date
		jpql.append(",c.dataAceitacaoExpedicao"); // dataAceitacaoExpedicao Date
		jpql.append(",c.pedido.gestao.nome");// gestaoResponsavel String
		jpql.append(",c.nomeResponsavel"); // nomeResponsavel String
		jpql.append(",c.localizacaoPedido");// localizacaoPedido LocalizacaoPedido
		jpql.append(",c.entradaMercadoria");// entradaMercadoria Date
		jpql.append(",c.saidaMercadoria");// saidaMercadoria Date
		jpql.append(",c.protocoloNf");// protocoloNf String
		jpql.append(",c.dataRecebimento");// dataRecebimento Date
		jpql.append(")");
		
		jpql.append(" FROM ControleExpedicao c where c.dataAceitacaoExpedicao between :data_inicio  and :data_final \n");
		
		if (filtro.getLancamentoID() != null) {
			jpql.append(" and (c.pedido.id = :id_pedido or c.pedido.compra.id = :id_pedido) ");
		}
		
		if(filtro.getLocalidadeID() != null){
			jpql.append(" and (c.pedido.localidade.id = :id_localidade or c.pedido.localidade.unidadeConservacao.id = :id_localidade)");
		}
		
		
		if(filtro.getGestaoID() != null){
			jpql.append(" and c.pedido.gestao.id = :id_gestao");
		}
		
		if(filtro.getLocalizacaoPedido() != null){
			jpql.append(" and c.localizacaoPedido = :rastreamento");
		}
		
		if(filtro.getSolicitanteID() != null){
			jpql.append(" and c.pedido.solicitante.id = :solicitante");			
		}
		
		if(filtro.getFornecedorID() != null){
			jpql.append(" and c.pedido.fornecedor.id = :fornecedor");
		}
		
		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			jpql.append(" and  (lower(c.nomeResponsavel) like  lower(:nomeResponsavel) or lower(c.gestaoResponsavel.nome) like  lower(:nomeResponsavel))");
		}
		
		
		jpql.append(" order by c.dataAceitacaoExpedicao desc");
		
		Query query = this.manager.createQuery(jpql.toString());
		
		
		query.setParameter("data_inicio", filtro.getDataInicio());
		query.setParameter("data_final", filtro.getDataFinal());
		
		if (filtro.getLancamentoID() != null) {
		  query.setParameter("id_pedido", filtro.getLancamentoID());
		}
		
		if(filtro.getLocalidadeID() != null){
			query.setParameter("id_localidade", filtro.getLocalidadeID());
		}
		
		
		if(filtro.getGestaoID() != null){
			query.setParameter("id_gestao", filtro.getGestaoID());
		}
		
		
		if(filtro.getLocalizacaoPedido() != null){
			query.setParameter("rastreamento", filtro.getLocalizacaoPedido());
		}
		
		if(filtro.getSolicitanteID() != null){
			query.setParameter("solicitante", filtro.getSolicitanteID());			
		}
		
		if(filtro.getFornecedorID() != null){
			query.setParameter("fornecedor", filtro.getFornecedorID());
		}
		
		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			query.setParameter("nomeResponsavel", "%" + filtro.getNome() + "%");
		
		}

		return query.getResultList();

	}
	
	
	public Pedido getPedido(Long id){
		String jpql = "from Pedido where id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return (Pedido) query.getResultList().get(0);
	}
	
	public Pedido getPedidoControleExpedicao(Long id){
		String jpql = "from Pedido where id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return (Pedido) query.getResultList().get(0);
	}
	
	public ControleExpedicao salvar(ControleExpedicao controleExpedicao){
		return this.manager.merge(controleExpedicao);
	}
	
	public ControleExpedicao gerarEsalvarExpedicao(Pedido pedido){
		ControleExpedicao controle = new ControleExpedicao();
		controle.setNomeResponsavel("");
		controle.setDataAceitacaoExpedicao(new Date());
		controle.setPedido(pedido);
		return this.manager.merge(controle);
	}
	
	
	//TODO: Impressão do pedido de compras SGI.
	public List<ItemPedido> getItensPedidosByPedido(Pedido pedido){
		StringBuilder jpql = new StringBuilder("SELECT NEW ItemPedido(it.id,it.descricaoProduto,it.quantidade,it.valorUnitario,it.valorUnitarioComDesconto,it.unidade, it.descricaoComplementar)");
		jpql.append(" FROM  ItemPedido AS it");
		jpql.append(" where it.pedido = :pedido");
		TypedQuery<ItemPedido>  query = manager.createQuery(jpql.toString(), ItemPedido.class);
		query.setParameter("pedido", pedido);
		return query.getResultList().size() > 0  ?  query.getResultList() : (List) new ArrayList<ItemPedido>();
	}
	
	public List<LancamentoAcao> getLancamentosDoPedido(Pedido pedido) {
		String jpql = "from LancamentoAcao where fornecedor = :fornecedor and compra = :compra";
		Query query = manager.createQuery(jpql);
		query.setParameter("fornecedor", pedido.getFornecedor());
		query.setParameter("compra", pedido.getCompra());
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
	
	public List<Fornecedor> getFornecedores(String s){
		StringBuilder jpql = new StringBuilder("from  Fornecedor f where lower(f.nomeFantasia) like lower(:nome) or lower(f.razaoSocial) like lower(:nome) ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%"+s+"%");
		query.setMaxResults(20);
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<Produto>();
	}
	
	public List<Colaborador> getSolicitantes(String s){
		StringBuilder jpql = new StringBuilder("from  Colaborador c where lower(c.nome) like lower(:nome) ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%"+s+"%");
		query.setMaxResults(20);
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<Produto>();
	}
	

}
