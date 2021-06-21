package repositorio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.ItemPedido;
import util.Filtro;

public class CompraDetalhadaRepository implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private @Inject EntityManager manager;
	
	public List<ItemPedido> getItensPedidosDetalhados(Filtro filtro) {

		StringBuilder jpql = new StringBuilder("select ");
		jpql.append("compra.id AS sc_00");
		jpql.append(",pedido.id as pedido_01");
		jpql.append(",to_char(compra.data_emissao,'dd/MM/yyyy')  as data_sc_02");
		jpql.append(
				", to_char((select log.data from log_status log where log.lancamento_id = compra.id and log.statuslog = 2 LIMIT 1),'dd/MM/yyyy') as data_aprovacao_03");
		jpql.append(",to_char(pedido.data_emissao,'dd/MM/yyyy')  as data_pedido_04");
		jpql.append(
				",(select colab.nome from colaborador colab where colab.id = compra.solicitante_id) as solicitante_05");
		// jpql.append(",CASE WHEN (pedido is null) ");
		// jpql.append("THEN (select cat.nome from categoria_despesa cat where cat.id =
		// (select po.categoriadespesa_id from produto po where po.id = (select
		// produto_id from item_compra tc where ip.item_compra_id = tc.id)) ");
		// jpql.append("ELSE ip.categoria END as categoria");
		
		jpql.append(", (select po.categoria from produto po where po.id = ic.produto_id) as categoria_06");
		
		jpql.append(",compra.statuscompra as status_07");
		
		jpql.append(",CASE WHEN (pedido is null) ");
		jpql.append("THEN (select gest.nome from gestao gest where gest.id = compra.gestao_id) ");
		jpql.append("ELSE (select gest.nome from gestao gest where gest.id = pedido.gestao_id) END as gestao_08 ");
		
		jpql.append(",CASE WHEN (pedido is null) ");
		jpql.append("THEN  (select loc.mascara from localidade loc where loc.id = compra.localidade_id) ");
		jpql.append(
				"ELSE (select loc.mascara from localidade loc where loc.id = pedido.localidade_id) END as destino_09 ");
		
		jpql.append(",CASE WHEN (pedido is null) ");
		jpql.append("THEN ic.quantidade ");
		jpql.append("ELSE ip.quantidade END as quantidade_10 ");
		
		jpql.append(",CASE WHEN (pedido is null) ");
		jpql.append("THEN (select po.descricao from produto po where po.id = ic.produto_id) ");
		jpql.append("ELSE ip.descricaoproduto END as descricao_11 ");
		jpql.append(
				",(select forn.nome_fantasia from fornecedor forn where forn.id = pedido.fornecedor_id) as fornecedor_12");
		
		jpql.append(",CASE WHEN (pedido is null) ");
		jpql.append("THEN ic.unidade ");
		jpql.append("ELSE ip.unidade END as unidade_medida_13 ");
		
		jpql.append(
				",(select usu.nomeusuario from usuario usu where usu.id = pedido.usuariogeradorpedido_id) as comprador_14 ");
		
		jpql.append(", compra.booturgencia as booturgencia_15,  ");
		jpql.append("to_char(compra.data_conclusao,'dd/MM/yyyy') as data_conclusao_16, ");
		
		jpql.append(
				"CASE WHEN (pedido is null) THEN 0 ELSE (ip.quantidade * ip.valor_unitario_com_desconto) END as valor_total_17,");
		
		jpql.append(
				"CASE WHEN (pedido is null) THEN 0 ELSE pedido.valor_total_com_desconto END as valor_total_pedido_18, ");
		
		jpql.append("CASE WHEN (pedido is null) THEN 0 ELSE ip.valor_unitario_com_desconto END as valor_unitario_19, ");

		jpql.append("CASE WHEN (pedido is null) THEN 'Não Informado' ELSE pedido.nota_fiscal END as notaFiscal_20, ");
		
		jpql.append("CASE WHEN (pedido is null) THEN 'Não Informado' ELSE to_char((select max(datapagamento)  ");
		jpql.append("from pagamento_lancamento where lancamento_id = pedido.id and stt = 'EFETIVADO'),'dd/MM/yyyy') END as dataPagamento_21, ");
			
		jpql.append("(select p.tipo from produto p where p.id = (select it.produto_id from item_compra as it where it.id = ic.id)) as tipo_22, ");
		
		
		jpql.append("CASE WHEN (pedido is null) ");
		jpql.append(" THEN (CASE WHEN (lac.projetorubrica_id is null) ");
		jpql.append(" THEN (select o.titulo from orcamento o where o.id = (select orcamento_id from rubrica_orcamento where id = lac.rubricaorcamento_id)) ");
		jpql.append(" ELSE (select oo.titulo from orcamento oo where oo.id = ");
		jpql.append(" (select orcamento_id from rubrica_orcamento where id = (select rubricaorcamento_id from projeto_rubrica where id = (lac.projetorubrica_id ) ))) END	) ");
		jpql.append(" ELSE (CASE WHEN (lap.projetorubrica_id is null) ");
		jpql.append(" THEN (select o.titulo from orcamento o where o.id = (select orcamento_id from rubrica_orcamento where id = lap.rubricaorcamento_id)) ");
		jpql.append("ELSE (select oo.titulo from orcamento oo where oo.id = (select orcamento_id from rubrica_orcamento where id = ");
		jpql.append(" (select rubricaorcamento_id from projeto_rubrica where id = (lap.projetorubrica_id ) ))) END	) ");
		jpql.append("END as orcamento_23 ,");
		
		jpql.append("CASE WHEN (pedido is null) ");
		jpql.append(" THEN (CASE WHEN (lac.projetorubrica_id is null) ");
		jpql.append(" THEN (select c.nome from componente_class c where c.id = (select componente_id from rubrica_orcamento where id = lac.rubricaorcamento_id)) ");
		jpql.append(" ELSE (select cc.nome from componente_class cc where cc.id =(select componente_id from projeto_rubrica where id = lac.projetorubrica_id)) END	) ");
		jpql.append("ELSE (CASE WHEN (lap.projetorubrica_id is null) ");
		jpql.append(" THEN (select c.nome from componente_class c where c.id = (select componente_id from rubrica_orcamento where id = lap.rubricaorcamento_id)) ");
		jpql.append(" ELSE (select cc.nome from componente_class cc where cc.id =(select componente_id from projeto_rubrica where id = lap.projetorubrica_id)) END	) ");
		jpql.append("END  as componente_24, ");
		
		jpql.append("CASE WHEN (pedido is null) ");
		jpql.append(" THEN (CASE WHEN (lac.projetorubrica_id is null) ");
		jpql.append(" THEN (select s.nome from sub_componente s where s.id = (select subcomponente_id from rubrica_orcamento where id = lac.rubricaorcamento_id)) ");
		jpql.append(" ELSE (select ss.nome from sub_componente ss where ss.id =(select subcomponente_id from projeto_rubrica where id = lac.projetorubrica_id)) END	) ");
		jpql.append("ELSE (CASE WHEN (lap.projetorubrica_id is null) ");
		jpql.append(" THEN (select s.nome from sub_componente s where s.id = (select subcomponente_id from rubrica_orcamento where id = lap.rubricaorcamento_id)) ");
		jpql.append(" ELSE (select ss.nome from sub_componente ss where ss.id = (select subcomponente_id from projeto_rubrica where id = lap.projetorubrica_id)) END	) ");
		jpql.append("END sub_componente_25,");
		
		jpql.append("CASE WHEN (pedido is null) ");
		jpql.append("THEN (CASE WHEN (lac.projetorubrica_id is null) ");
		jpql.append("THEN (select r.nome from rubrica r where r.id = (select rubrica_id from rubrica_orcamento where id = lac.rubricaorcamento_id)) ");
		jpql.append("ELSE (select rr.nome from rubrica rr where rr.id = (select rubrica_id from rubrica_orcamento where id =  ");
		jpql.append("(select rubricaorcamento_id from projeto_rubrica where id = lac.projetorubrica_id))) END) ");
		jpql.append("ELSE (CASE WHEN (lap.projetorubrica_id is null) ");
		jpql.append("THEN (select r.nome from rubrica r where r.id = (select rubrica_id from rubrica_orcamento where id = lap.rubricaorcamento_id)) ");
		jpql.append("ELSE (select rr.nome from rubrica rr where rr.id = (select rubrica_id from rubrica_orcamento where id =  ");
		jpql.append("(select rubricaorcamento_id from projeto_rubrica where id = lap.projetorubrica_id))) END	) END  as rubrica_26");
		
		
		jpql.append(",CASE WHEN (pedido is null) ");
		
		jpql.append("THEN '' ");
		
		jpql.append("ELSE ip.descricaocomplementar END as descricaoComplementar_27, ");
		
	
		jpql.append("CASE WHEN (pedido is null) ");
		jpql.append("THEN (CASE WHEN (lac.projetorubrica_id is null) ");
		jpql.append("THEN (select f.nome from fonte_pagadora f where f.id = (select o.fonte_id from orcamento o where o.id = ");
		jpql.append("(select orcamento_id from rubrica_orcamento where id = lac.rubricaorcamento_id))) ");
		jpql.append("ELSE (select f.nome from fonte_pagadora f where f.id =  (select oo.fonte_id from orcamento oo where oo.id = ");
		jpql.append("(select orcamento_id from rubrica_orcamento where id = (select rubricaorcamento_id from projeto_rubrica where id = (lac.projetorubrica_id ) )))) END) ");
		jpql.append("ELSE (CASE WHEN (lap.projetorubrica_id is null) ");
		jpql.append("THEN (select f.nome from fonte_pagadora f where f.id = (select o.fonte_id from orcamento o where o.id = ");
		jpql.append("(select orcamento_id from rubrica_orcamento where id = lap.rubricaorcamento_id))) ");
		jpql.append("ELSE (select f.nome from fonte_pagadora f where f.id = (select oo.fonte_id from orcamento oo where oo.id = (select orcamento_id from rubrica_orcamento where id = ");
		jpql.append("(select rubricaorcamento_id from projeto_rubrica where id = (lap.projetorubrica_id ) )))) END) ");
		jpql.append("END as fonte_28,");
		
		jpql.append("CASE WHEN (pedido is null) ");
		jpql.append("THEN  (select p.nome from projeto p where p.id = ");
		jpql.append("(select projeto_id from projeto_rubrica where id = lac.projetorubrica_id)) ");
		jpql.append("ELSE (select p.nome from projeto p where p.id = ");
		jpql.append("(select projeto_id from projeto_rubrica where id = lap.projetorubrica_id)) ");
		jpql.append("END  as projeto_29 ");
		
		
		jpql.append("from lancamento compra ");
		jpql.append("join item_compra ic on ic.compra_id = compra.id ");
		jpql.append("left join item_pedido ip on ip.item_compra_id =  ic.id ");
		jpql.append("left join lancamento pedido on ip.pedido_id = pedido.id ");
		jpql.append("left join lancamento_acao lac on lac.lancamento_id = compra.id ");
		jpql.append("left join lancamento_acao lap on lap.lancamento_id = pedido.id ");
		jpql.append("where 1 = 1 ");
		
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
			jpql.append(
					" and compra.solicitante_id in (select col.id from colaborador col where col.nome ilike :nome ) ");
		}

		if (filtro.getDataInicio() != null) {
			if (filtro.getDataFinal() != null) {
				jpql.append(" and compra.data_emissao >= :data_inicio and compra.data_emissao <= :data_final ");
			} else {
				jpql.append(" and compra.data_emissao >= :data_inicio");
			}
		}
		
		if (filtro.getDataPedidoInicial() != null) {
			if (filtro.getDataPedidoFinal() != null) {
				jpql.append(" and pedido.data_emissao >= :data_pedido_inicio and pedido.data_emissao <= :data_pedido_final ");
			} else {
				jpql.append(" and pedido.data_emissao >= :data_pedido_inicio");
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
			jpql.append(
					"and pedido.fornecedor_id in (select fornec.id from fornecedor fornec where fornec.nome_fantasia ilike :fornecedor  ");
			jpql.append("or fornec.cnpj ilike :fornecedor ");
			jpql.append("or fornec.razao_social ilike :fornecedor");
			jpql.append(")");
		}
		
		
		Query query = this.manager.createNativeQuery(jpql.toString());

		if (filtro.getUrgencia()) {
			query.setParameter("urgencia", filtro.getUrgencia());
		}

		if (filtro.getStatusCompra() != null) {
			query.setParameter("status_compra", filtro.getStatusCompra().toString());
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
		
		if (filtro.getDataPedidoInicial() != null) {
			if (filtro.getDataPedidoFinal() != null) {
				query.setParameter("data_pedido_inicio", filtro.getDataPedidoInicial());
				query.setParameter("data_pedido_final", filtro.getDataPedidoFinal());
			} else {
				query.setParameter("data_pedido_inicio", filtro.getDataPedidoInicial());
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
			if (objects[1] != null)
				item.setIdPedido(new Long(objects[1].toString()));

			item.setDataEmissaoSC(objects[2] != null ? objects[2].toString() : "");
			item.setDataAprovacao(objects[3] != null ? objects[3].toString() : "");
			item.setDataEmissaoPedido(objects[4] != null ? objects[4].toString() : "");
			item.setNomeSolicitante(objects[5] != null ? objects[5].toString() : "");
			item.setCategoria(objects[6] != null ? objects[6].toString() : "");
			item.setStatusCompra(objects[7] != null ? objects[7].toString() : "");
			item.setNomeGestao(objects[8] != null ? objects[8].toString() : "");
			item.setNomeDestino(objects[9] != null ? objects[9].toString() : "");
			item.setQuantidade(new Double(objects[10] != null ? objects[10].toString() : ""));
			item.setDescricaoProduto(objects[11] != null ? objects[11].toString() : "");
			item.setNomeFornecedor(objects[12] != null ? objects[12].toString() : "");
			item.setUnidade(objects[13] != null ? objects[13].toString() : "");
			item.setNomeComprador(objects[14] != null ? objects[14].toString() : "");
			item.setBootUrgencia(objects[15] != null ? Boolean.valueOf(objects[15].toString()) : false);
			item.setDataConclusaoSC(objects[16] != null ? objects[16].toString() : "");
			item.setValorTotal(objects[17] != null ? new BigDecimal(objects[17].toString()) : BigDecimal.ZERO);
			item.setValorTotalPedido(objects[18] != null ? new BigDecimal(objects[18].toString()) : BigDecimal.ZERO);
			item.setValorUnitarioComDesconto(
					objects[19] != null ? new BigDecimal(objects[19].toString()) : BigDecimal.ZERO);
			
			
			item.setTipo(objects[22] != null ? objects[22].toString() : "");

			item.setNotaFiscal(objects[20] != null ? objects[20].toString() : "");
			item.setDataPagamento(objects[21] != null ? objects[21].toString() : "");
			item.setOrcamento(objects[23] != null ? objects[23].toString() : "");
			item.setComponente(objects[24] != null ? objects[24].toString() : "");
			item.setSubComponente(objects[25] != null ? objects[25].toString() : "");
			item.setRubrica(objects[26] != null ? objects[26].toString() : "");
			item.setDescricaoProdutoComplementar(objects[27] != null ? objects[27].toString() : "");
			item.setFonte(objects[28] != null ? objects[28].toString() : "");
			item.setProjeto(objects[29] != null ? objects[29].toString() : "");

			itens.add(item);

			// TODO:VALOR TOTAL PEDIDO
		}

		return itens;
	}
	
}
