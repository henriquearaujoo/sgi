package repositorio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.ItemPedido;
import model.Pedido;
import model.SolicitacaoExpedicao;
import util.Filtro;

public class GerenciadorPedidoRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public GerenciadorPedidoRepositorio() {
	}

	public GerenciadorPedidoRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public List<Pedido> getPedidos(Filtro filtro) {
		String jpql = "SELECT NEW Pedido(p.id,p.fornecedor.nomeFantasia,p.localidade.mascara,p.notaFiscal, p.valorTotalComDesconto, p.descricao) from Pedido p order by p.dataEmissao desc ";
		Query query = manager.createQuery(jpql);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public List<ItemPedido> getItensPedidosDetalhados(Filtro filtro) {

		StringBuilder jpql = new StringBuilder("select ");
		jpql.append("compra.id AS sc");
		jpql.append(",pedido.id as pedido");
		jpql.append(",to_char(compra.data_emissao,'dd/MM/yyyy')  as data_sc");
		jpql.append(
				", to_char((select log.data from log_status log where log.lancamento_id = compra.id and log.statuslog = 2 LIMIT 1),'dd/MM/yyyy') as data_aprovacao");
		jpql.append(",to_char(pedido.data_emissao,'dd/MM/yyyy')  as data_pedido");
		jpql.append(",(select colab.nome from colaborador colab where colab.id = compra.solicitante_id) as solicitante");
		
		jpql.append(",CASE WHEN (pedido is null) ");
		jpql.append("THEN (select cat.nome from categoria_despesa cat where cat.id = (select po.categoriadespesa_id from produto po where po.id = ic.produto_id)) ");
		jpql.append("ELSE ip.categoria END as categoria");
		jpql.append(",compra.statuscompra");
		
		jpql.append(",CASE WHEN (pedido is null) ");
		jpql.append("THEN (select gest.nome from gestao gest where gest.id = compra.gestao_id) ");
		jpql.append("ELSE (select gest.nome from gestao gest where gest.id = pedido.gestao_id) END as gestao ");
		
		jpql.append(",CASE WHEN (pedido is null) ");
		jpql.append("THEN  (select loc.mascara from localidade loc where loc.id = compra.localidade_id) ");
		jpql.append("ELSE (select loc.mascara from localidade loc where loc.id = pedido.localidade_id) END as destino ");
		
		jpql.append(",CASE WHEN (pedido is null) ");
		jpql.append("THEN ic.quantidade ");
		jpql.append("ELSE ip.quantidade END as quantidade ");
		
		jpql.append(",CASE WHEN (pedido is null) ");
		jpql.append("THEN (select po.descricao from produto po where po.id = ic.produto_id) ");
		jpql.append("ELSE ip.descricaoproduto END as descricao ");
	
		jpql.append(",(select forn.nome_fantasia from fornecedor forn where forn.id = pedido.fornecedor_id) as fornecedor");
		
		jpql.append(",CASE WHEN (pedido is null) ");
		jpql.append("THEN ic.unidade ");
		jpql.append("ELSE ip.unidade END as unidade_medida ");
		
		jpql.append(",(select usu.nomeusuario from usuario usu where usu.id = pedido.usuariogeradorpedido_id) as comprador ");
		jpql.append(", compra.booturgencia ");
//		jpql.append(" from ");
//		jpql.append("lancamento compra join lancamento pedido on compra.id = pedido.compra_id ");
//		jpql.append("join item_pedido ip on ip.pedido_id = pedido.id ");
//		jpql.append("join item_compra ic on ic.id = ip.item_compra_id ");
//		jpql.append("join produto po on po.id = ic.produto_id where 1 = 1 ");
		
		jpql.append("from lancamento compra ");
		jpql.append("join item_compra ic on ic.compra_id = compra.id ");
		jpql.append("left join item_pedido ip on ip.item_compra_id =  ic.id ");
		jpql.append("left join lancamento pedido on ip.pedido_id = pedido.id where 1 = 1 ");
		
		

		if (filtro.getUrgencia()) {
			jpql.append(" and compra.booturgencia = :urgencia ");
		}
		
		if (filtro.getStatusCompra() != null) {
			jpql.append(" and compra.statuscompra = :status_compra");
		}
		
		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			jpql.append(" and compra.id = :codigo");
		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			jpql.append(" and compra.solicitante_id in (select col.id from colaborador col where col.nome ilike :nome ) ");
			//jpql.append(" and lower(p.solicitante.nome) like lower(:nome)");
		}

		if (filtro.getDataInicio() != null) {
			if (filtro.getDataFinal() != null) {
				jpql.append(" and pedido.data_emissao >= :data_inicio and pedido.data_emissao <= :data_final");
			} else {
				jpql.append(" and pedido.data_emissao >= :data_inicio");
			}
		}

		if (filtro.getGestaoID() != null) {
			jpql.append(" and pedido.gestao_id = :gestaoID ");
		}

		if (filtro.getLocalidadeID() != null) {
			jpql.append(" and pedido.localidade_id = :localidadeID ");
		}

		if (filtro.getDescricaoProduto() != null && !filtro.getDescricaoProduto().equals("")) {
			jpql.append(" and ip.descricaoproduto ilike :descricao_produto   ");
		}

		if (filtro.getNotaFiscal() != null && !filtro.getNotaFiscal().equals("")) {
			jpql.append(" and pedido.nota_fiscal ilike :nota_fiscal ");
		}

		if (filtro.getNomeFornecedor() != null && !filtro.getNomeFornecedor().equals("")) {
			jpql.append("and pedido.fornecedor_id in (select fornec.id from fornecedor fornec where fornec.nome_fantasia ilike :fornecedor  ");
			jpql.append("or fornec.cnpj ilike :fornecedor ");
			jpql.append("or fornec.razao_social ilike :fornecedor");
			jpql.append(")");
		}

		Query query = this.manager.createNativeQuery(jpql.toString());
		
		
		if (filtro.getUrgencia()) {
			query.setParameter("urgencia", filtro.getUrgencia());
		}
		
		if (filtro.getStatusCompra() != null) {
			query.setParameter("status_compra", filtro.getStatusCompra());
		}
		
		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			query.setParameter("codigo", Long.valueOf(filtro.getCodigo()));
		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			query.setParameter("nome", "%" + filtro.getNome() + "%");
		}

		if (filtro.getDataInicio() != null) {
			if (filtro.getDataFinal() != null) {
				query.setParameter("data_inicio", filtro.getDataInicio());
				query.setParameter("data_final", filtro.getDataFinal());
			} else {
				query.setParameter("data_inicio", filtro.getDataInicio());
			}
		}

		if (filtro.getGestaoID() != null) {
			query.setParameter("gestaoID", filtro.getGestaoID());
		}

		if (filtro.getLocalidadeID() != null) {
			query.setParameter("localidadeID", filtro.getLocalidadeID());
		}

		if (filtro.getDescricaoProduto() != null && !filtro.getDescricaoProduto().equals("")) {
			query.setParameter("descricao_produto", "%" + filtro.getDescricaoProduto() + "%");
		}

		if (filtro.getNotaFiscal() != null && !filtro.getNotaFiscal().equals("")) {
			query.setParameter("nota_fiscal", "%" + filtro.getNotaFiscal() + "%");
		}

		if (filtro.getNomeFornecedor() != null && !filtro.getNomeFornecedor().equals("")) {
			query.setParameter("fornecedor", "%" + filtro.getNomeFornecedor() + "%");
		}

		
		
		List<Object[]> result = query.getResultList();
		List<ItemPedido> itens = new ArrayList<>();
		for (Object[] objects : result) {
			
			ItemPedido item = new ItemPedido();
			item.setIdSC(new Long(objects[0].toString()));
			if (objects[1] !=  null) 
			item.setIdPedido(new Long(objects[1].toString()));
			item.setDataEmissaoSC(objects[2] !=  null ? objects[2].toString() : "");
			item.setDataAprovacao(objects[3] !=  null ? objects[3].toString() : "");
			item.setDataEmissaoPedido(objects[4] !=  null ? objects[4].toString() : "");
			item.setNomeSolicitante(objects[5] !=  null ? objects[5].toString() : "");
			item.setCategoria(objects[6] !=  null ? objects[6].toString() : "");
			item.setStatusCompra(objects[7] !=  null ? objects[7].toString() : "");
			item.setNomeGestao(objects[8] !=  null ? objects[8].toString() : "");
			item.setNomeDestino(objects[9] !=  null ? objects[9].toString() : "");
			item.setQuantidade(new Double(objects[10] !=  null ? objects[10].toString() : ""));
			item.setDescricaoProduto(objects[11] !=  null ? objects[11].toString() : "");
			item.setNomeFornecedor(objects[12] !=  null ? objects[12].toString() : "");
			item.setUnidade(objects[13] !=  null ? objects[13].toString() : "");
			item.setNomeComprador(objects[14] !=  null ? objects[14].toString() : "" );
			item.setBootUrgencia(objects[15] != null ? Boolean.valueOf(objects[15].toString())  : false);
			itens.add(item);
		}
		

		return itens;
	}
	

	public List<Pedido> getPedidosFilter(Filtro filtro) {

		StringBuilder jpql = new StringBuilder("SELECT   ");
		jpql.append(
				"NEW Pedido(p.id,p.fornecedor.nomeFantasia,p.localidade.mascara,p.notaFiscal, p.valorTotalComDesconto, p.descricao)  ");
		jpql.append("from ItemPedido as ip JOIN  ip.pedido as  p  where 1 = 1");

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			jpql.append(" and p.id = :codigo");
		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			jpql.append(" and lower(p.solicitante.nome) like lower(:nome)");
		}

		if (filtro.getDataInicio() != null) {
			if (filtro.getDataFinal() != null) {
				jpql.append(" and p.dataEmissao >= :data_inicio and p.dataEmissao <= :data_final");
			} else {
				jpql.append(" and p.dataEmissao >= :data_inicio");
			}
		}

		if (filtro.getGestaoID() != null) {
			jpql.append(" and p.gestao.id = :gestaoID ");
		}

		if (filtro.getLocalidadeID() != null) {
			jpql.append(" and p.localidade.id = :localidadeID ");
		}

		if (filtro.getDescricaoProduto() != null && !filtro.getDescricaoProduto().equals("")) {
			jpql.append(" and lower(ip.descricaoProduto) like lower(:descricao_produto)  ");
		}

		if (filtro.getNotaFiscal() != null && !filtro.getNotaFiscal().equals("")) {
			jpql.append(" and lower(p.notaFiscal) like lower(:nota_fiscal)  ");
		}

		if (filtro.getNomeFornecedor() != null && !filtro.getNomeFornecedor().equals("")) {
			jpql.append(
					" and (lower(p.fornecedor.nomeFantasia) like lower(:fornecedor) or lower(p.fornecedor.razaoSocial) like lower(:fornecedor) or lower(p.fornecedor.cnpj) like lower(:fornecedor) or lower(p.fornecedor.cpf) like lower(:fornecedor)) ");
		}

		jpql.append(" group by p.id, p.fornecedor.nomeFantasia, p.localidade.mascara ");
		jpql.append(" order by p.dataEmissao desc");

		Query query = manager.createQuery(jpql.toString());

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			query.setParameter("codigo", Long.valueOf(filtro.getCodigo()));
		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			query.setParameter("nome", "%" + filtro.getNome() + "%");
		}

		if (filtro.getDataInicio() != null) {
			if (filtro.getDataFinal() != null) {
				query.setParameter("data_inicio", filtro.getDataInicio());
				query.setParameter("data_final", filtro.getDataFinal());
			} else {
				query.setParameter("data_inicio", filtro.getDataInicio());
			}
		}

		if (filtro.getGestaoID() != null) {
			query.setParameter("gestaoID", filtro.getGestaoID());
		}

		if (filtro.getLocalidadeID() != null) {
			query.setParameter("localidadeID", filtro.getLocalidadeID());
		}

		if (filtro.getDescricaoProduto() != null && !filtro.getDescricaoProduto().equals("")) {
			query.setParameter("descricao_produto", "%" + filtro.getDescricaoProduto() + "%");
		}

		if (filtro.getNotaFiscal() != null && !filtro.getNotaFiscal().equals("")) {
			query.setParameter("nota_fiscal", "%" + filtro.getNotaFiscal() + "%");
		}

		if (filtro.getNomeFornecedor() != null && !filtro.getNomeFornecedor().equals("")) {
			query.setParameter("fornecedor", "%" + filtro.getNomeFornecedor() + "%");
		}

		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public void updatePedido(Pedido pedido) {
		StringBuilder jpql = new StringBuilder("UPDATE Pedido ");

		if (pedido.getDescricao() != null) {
			jpql.append("SET  descricao = :descricao ");
		} else {
			pedido.setDescricao("");
			jpql.append("SET  descricao = :descricao ");
		}

		if (pedido.getNotaFiscal() != null) {
			jpql.append(", notaFiscal = :nf ");
		}

		jpql.append(" where id = :id ");

		Query query = manager.createQuery(jpql.toString());
		query.setParameter("descricao", pedido.getDescricao());

		if (pedido.getNotaFiscal() != null) {
			query.setParameter("nf", pedido.getNotaFiscal());
		}

		query.setParameter("id", pedido.getId());
		query.executeUpdate();
	}

	public SolicitacaoExpedicao salvarSolicitacao(SolicitacaoExpedicao solicitacaoExpedicao) {
		return this.manager.merge(solicitacaoExpedicao);
	}

}
