package repositorio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.ContaBancaria;
import model.Gestao;
import model.ItemPedido;
import model.Saving;
import util.DataUtil;
import util.Filtro;
import util.Util;

public class SavingRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public SavingRepositorio() {
	}

	public List<Saving> getSaving(Filtro filtro) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		StringBuilder hql = new StringBuilder(" select ");
		hql.append("to_char( (select ped.data_pagamento from lancamento ped where ped.id = ip.pedido_id), 'DD-MM-YYYY') as data_pagamento_00,");
		hql.append("to_char( (select ped.data_emissao from lancamento ped where ped.id = ip.pedido_id), 'DD-MM-YYYY') as data_emissao_01,");
		hql.append("(select gest.nome from gestao gest where gest.id = ip.gestao_id) as gestao_02,");
		hql.append("(select loc.mascara from localidade loc where loc.id = ip.localidade_id) as localidade_03,");
		hql.append("(select f.nome_fantasia from fornecedor f where f.id = p.fornecedor_id) as fornecedor_04,");
		//hql.append("(select ped.fornecedor_id from lancamento ped where ped.id = ip.pedido_id)) as fornecedor_04,");
		hql.append("ip.descricaoproduto as desc_produto_05,");
		hql.append("ip.valor_unitario_sem_desconto as valor_com_desconto_06,");
		hql.append("ip.valor_unitario_com_desconto as valor_com_desconto_07,");
		hql.append("ip.quantidade as quantidade_08, ");
		hql.append("ip.pedido_id id_pedido_09 ");
		hql.append(" from item_pedido ip join lancamento p on p.id = ip.pedido_id where ");
		hql.append("(select ped.data_emissao from lancamento ped where ped.id = ip.pedido_id) >= '"  );
		hql.append(sdf.format(filtro.getDataInicio()));
		hql.append("' and ");
		hql.append("(select ped.data_emissao from lancamento ped where ped.id = ip.pedido_id) <= '");
		hql.append(sdf.format(filtro.getDataFinal()));
		hql.append("' ");
		
		
		if (filtro.getFornecedorID() != null) {
			hql.append(" and (select con.id from conta_bancaria con where con.fornecedor_id = p.fornecedor_id) = :paramFornecedor");
			
		}
		
		
		if (filtro.getGestaoID() != null) {
			hql.append(" and (select gest.id from gestao gest where gest.id = p.gestao_id) = :paramGestao ");
		}
		
		
		Query query = this.manager.createNativeQuery(hql.toString());
		
		
		if (filtro.getFornecedorID() != null) {
			query.setParameter("paramFornecedor", filtro.getFornecedorID());
		}
		
		
		if (filtro.getGestaoID() != null) {
			query.setParameter("paramGestao", filtro.getGestaoID());
		}
		
		List<Object[]> result = query.getResultList();
		List<Saving> lista = new ArrayList<>();
		Saving saving = new Saving();
		for (Object[] objects : result) {
			saving = new Saving();
			saving.setDataPagamento(objects[0] != null ? DataUtil.converteDataSql(objects[0].toString()) : null);
			saving.setDataEmissao(objects[1] != null ? DataUtil.converteDataSql(objects[1].toString()) : null);
			saving.setGestao(Util.getNullValue(objects[2], ""));
			saving.setLocalidade(Util.getNullValue(objects[3], ""));
			saving.setFornecedor(Util.getNullValue(objects[4], ""));
			saving.setProduto(Util.getNullValue(objects[5], ""));
			saving.setValorComDesconto(objects[6] != null ? new BigDecimal(objects[6].toString()) : BigDecimal.ZERO);
			saving.setValorSemDesconto(objects[7] != null ? new BigDecimal(objects[7].toString()) : BigDecimal.ZERO);
			saving.setQuantidade(new Double(objects[8] != null ? objects[8].toString() : ""));
			saving.setPedido(objects[9] != null ? new Long(objects[9].toString()) : 0);
			lista.add(saving);
		}

		return lista;
	}

	public List<ContaBancaria> getFornecedor() {
		String jpql = "SELECT NEW ContaBancaria(cb.id, cb.nomeConta) FROM ContaBancaria AS cb where cb.tipo = :tipo";
		Query query = manager.createQuery(jpql);
		query.setParameter("tipo", "CF");
		return query.getResultList();
	}
	
	public List<Gestao> getGestao(){
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Gestao(g.id, g.nome) from Gestao g");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList();
	}
	
	
}
