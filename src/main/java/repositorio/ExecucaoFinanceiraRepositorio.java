package repositorio;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Projeto;
import model.RelatorioContasAPagar;
import util.Filtro;
import util.Util;

public class ExecucaoFinanceiraRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<RelatorioContasAPagar> getLancamentos(Filtro filtro) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		StringBuilder hql = new StringBuilder("");
		hql.append(
				"select l.id as id_lancamento_00, to_char(l.data_emissao,'DD-MM-YYYY') as data_emissao_01, to_char(pl.datapagamento,'DD-MM-YYYY') ");
		hql.append(
				"as data_pagamento_02,(select cb.nome_conta from conta_bancaria cb where  cb.id  = pl.conta_id) as conta_pagador_03, ");
		hql.append(
				"(select cb.nome_conta from  conta_bancaria cb where cb.id = pl.contarecebedor_id) as conta_recebedor_04,la.status as status_05, ");
		hql.append(
				"replace(l.descricao,';', ':')  as descricao_06,CASE la.rubricaorcamento_id when null then (select font.nome from fonte_pagadora font where id = ");
		hql.append(
				"(select orc.fonte_id from  orcamento orc where id = (select rub.orcamento_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id))) ");
		hql.append(
				"else (select font.nome from fonte_pagadora font where id = (select orc.fonte_id from  orcamento orc where id = ");
		hql.append(
				"(select rub.orcamento_id from rubrica_orcamento rub where rub.id = (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id)))) ");
		hql.append(
				"end as fonte_07,(select replace(p.nome,';', ':')  from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id))");
		hql.append(
				"as nome_projeto_08, pl.valor as valor_pago_by_acao_09, l.valor_total_com_desconto as valor_total_lancamento_10,la.id ");
		hql.append(
				"as id_lancamento_acao_11, l.id as codigo_lancamento_12, pl.numerodaparcela || '/' || pl.quantidade_parcela as parcela_13,l.tipoparcelamento as tipo_parcelamento_14, ");
		hql.append(
				"l.compra_id  as compra_id_15, CASE WHEN (l.tipo = 'SolicitacaoPagamento' and l.tipolancamento != 'ad') THEN 'SP' WHEN (l.tipo = 'SolicitacaoPagamento' ");
		hql.append(
				"and l.tipolancamento = 'ad') THEN 'SA' WHEN (l.tipo = 'Diaria') THEN 'SD' WHEN (l.tipo = 'custo_pessoal') ");
		hql.append("THEN 'CP' WHEN (l.tipo = 'pedido') THEN 'PC' WHEN (l.tipo = 'baixa_aplicacao') THEN 'BA' WHEN ");
		hql.append("(l.tipo = 'aplicacao_recurso') THEN 'AR' WHEN (l.tipo = 'doacao_efetiva') THEN 'DE' WHEN ");
		hql.append(
				"(l.tipo = 'tarifa_bancaria') THEN 'TF' WHEN (l.tipo = 'lanc_av' or l.tipo = 'lancamento_diversos') ");
		hql.append(
				"THEN 'LA'   END as tipo_16,l.reembolsado as reembolsado_17,l.idreembolso as id_reembolso_18,l.idfontereembolso ");
		hql.append(
				"as id_fonte_reembolsada_19,l.iddoacaoreembolso as id_doacao_reembolsada_20,l.depesareceita as dc_21, ");
		hql.append(
				"CASE  WHEN (pl.tipocontapagador = 'CB')  THEN '-' ELSE '+'  END as sinalizador_22, l.tipolancamento as ");
		hql.append(
				"tipo_lancamento_23,l.statusadiantamento as status_adiantamento_24,(select ll.statusadiantamento from lancamento ");
		hql.append("ll where ll.id = l.idadiantamento) status_ad_prestacao_25,l.idadiantamento as idadiantamento_26, ");
		hql.append(
				"(select rubric.nome from  rubrica rubric where rubric.id = (select rub.rubrica_id from rubrica_orcamento rub where rub.id = ");
		hql.append("(select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) ");
		hql.append(
				"as requisito_27, pl.stt as status_28, l.tipo as tipo_29,  (select comp.nome from  componente_class  comp where comp.id = ");
		hql.append(
				"(select rub.componente_id from rubrica_orcamento rub where rub.id =  (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) ");
		hql.append("as componente_30,  (select sub.nome from  sub_componente  sub where sub.id = ");
		hql.append(
				"(select rub.subcomponente_id from rubrica_orcamento rub where rub.id =  (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) ");
		hql.append(
				"as sub_componente_31, (select orc.titulo from  orcamento orc where orc.id =(select rub.orcamento_id from rubrica_orcamento rub where rub.id = ");
		hql.append(
				"(select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) as doacao_32, ");
		hql.append(
				"(select p.codigo from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) ");
		hql.append("as codigo_projeto_33, l.numerodocumento as numero_documento_34,l.nota_fiscal as nota_fiscal_35, ");
		hql.append("(select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) as tipo_conta_pagador_36, ");
		hql.append(
				"(select cb.tipo from  conta_bancaria cb where cb.id = pl.contarecebedor_id) as tipo_conta_recebedor_37, ");
		hql.append(
				"(select cad.nome from cadeia_produtiva as cad where cad.id =  (select p.cadeia_id from projeto p where p.id = ");
		hql.append("(select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id))) as cadeia_38, ");
		
		
		
		hql.append(
				"(select loc.mascara from localidade as loc where loc.id =  (select p.localidade_id from projeto p where p.id = ");
		hql.append(
				"(select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id))) as localidade_projeto_39, ");
		
		
		
		hql.append("case when l.tipo = 'Diaria' then ");
		hql.append("(case when (select loc.tipo from localidade loc where loc.id = ");
		hql.append("(select sv.localidade_id from solicitacaoviagem sv where sv.id = l.solicitacaoviagem_id)) = 'uc' ");
		hql.append("then (select loc.mascara from localidade loc where loc.id = ");
		hql.append("(select sv.localidade_id from solicitacaoviagem sv where sv.id = l.solicitacaoviagem_id)) ");
		hql.append("else (select locc.mascara  from localidade locc where locc.id =  (select loc.unidadeconservacao_id from localidade loc where loc.id = ");
		hql.append("(select sv.localidade_id from solicitacaoviagem sv where sv.id = l.solicitacaoviagem_id))) end) ");
		hql.append("else (case when (select loc.tipo from localidade as loc where loc.id =  l.localidade_id) = 'uc' ");
		hql.append("then (select loc.mascara from localidade as loc where loc.id =  l.localidade_id) ");
		hql.append("else (select loc.mascara from localidade as loc where loc.id = ");
		hql.append("(select locc.unidadeconservacao_id from localidade as locc where locc.id =  l.localidade_id)) end) end as uc_solicitacao_40, ");
		
		hql.append("case when l.tipo = 'Diaria' then ");
		hql.append("(select loc.mascara from localidade loc where loc.id = ");
		hql.append("(select sv.localidade_id from solicitacaoviagem sv where sv.id = l.solicitacaoviagem_id)) ");
		hql.append("else (select loc.mascara from localidade as loc where loc.id =  l.localidade_id) end as localidade_solicitacao_41,");
		
		hql.append(
				"(select gest.nome from gestao as gest where gest.id =  (select p.gestao_id from projeto p where p.id = ");
		hql.append("(select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id))) as gestao_42, ");
		hql.append(
				"(select pt.titulo from plano_de_trabalho as pt where pt.id =  (select p.planodetrabalho_id from projeto p where p.id = ");
		hql.append(
				"(select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id))) as plano_trabalho_43, ");
		hql.append("(select ll.mascara from localidade ll where ll.id = ");
		hql.append(
				"(select pt.id from plano_de_trabalho as pt where pt.id =  (select p.planodetrabalho_id from projeto p where p.id = ");
		hql.append("(select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)))) as uc_plano_44, ");
		
		hql.append("CASE when  l.tipo = 'pedido' then (select it.categoria from item_pedido it where it.pedido_id = l.id LIMIT 1) ");
		hql.append("when l.tipo = 'Diaria' then 'DIARIA' ");
		hql.append("else (select c.nome from categoria_financeira c where c.id = l.categoriafinanceira_id) end as categoria_45 ");
		

		hql.append("from lancamento l ");
		hql.append("join lancamento_acao la on l.id = la.lancamento_id ");
		hql.append("join pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");
		hql.append("where l.tipo != 'compra' ");
		hql.append("and l.statuscompra in ('CONCLUIDO') ");
		hql.append("and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) ");
		hql.append("and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb') ");
		hql.append(
				"and l.tipolancamento != 'reemb_conta'  and l.tipo != 'baixa_aplicacao' and l.tipo != 'doacao_efetiva' and l.tipo != 'custo_pessoal'  and l.tipo != 'aplicacao_recurso' ");
		hql.append("and (false = (pl.tipocontapagador = 'CA' and pl.tipocontarecebedor = 'CF')) ");
		hql.append("and (false = (pl.tipocontapagador = 'CF' and pl.tipocontarecebedor = 'CF')) ");
		hql.append("and (false = (pl.tipocontapagador = 'CB' and pl.tipocontarecebedor = 'CB')) ");

		hql.append(
				"and (CASE la.rubricaorcamento_id when null then (select font.id from fonte_pagadora font where id = ");
		hql.append(
				" (select orc.fonte_id from  orcamento orc where id = (select rub.orcamento_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id))) ");
		hql.append(
				"else (select font.id from fonte_pagadora font where id = (select orc.fonte_id from  orcamento orc where id = ");
		hql.append("(select rub.orcamento_id from rubrica_orcamento rub where rub.id =  ");
		hql.append(
				"(select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id)))) end) in (:fontes)");

		hql.append("and pl.datapagamento between '" + sdf.format(filtro.getDataInicio()) + "' and '"
				+ sdf.format(filtro.getDataFinal()) + "' ");

		if (filtro.getProjetos() != null && filtro.getProjetos().length > 0) {

			hql.append(
					"and  (select p.id from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) in  (:projetos) ");
		}

		hql.append("order by pl.datapagamento asc \n");

		Query query = manager.createNativeQuery(hql.toString());
		
		if (filtro.getProjetos() != null && filtro.getProjetos().length > 0) {
				
			List<Integer> list = new ArrayList<>();
			Integer[] mList = filtro.getProjetos();
			for (int i = 0; i < filtro.getProjetos().length; i++) {
				list.add(mList[i]);
			}

			query.setParameter("projetos", list);			
		}
		
		List<Integer> list = new ArrayList<>();
		Integer[] mList = filtro.getFontes();
		for (int i = 0; i < filtro.getFontes().length; i++) {
			list.add(mList[i]);
		}

		query.setParameter("fontes", list);
		

		List<Object[]> result = query.getResultList();
		List<RelatorioContasAPagar> relatorio = new ArrayList<>();

		RelatorioContasAPagar rel = new RelatorioContasAPagar();

		for (Object[] object : result) {

			rel = new RelatorioContasAPagar();
			rel.setNumeroLancamento(object[0] != null ? object[0].toString() : "");
			rel.setEmissao(object[1] != null ? object[1].toString() : "");
			rel.setPagamento(object[2] != null ? object[2].toString() : "");
			rel.setNotaFiscal(object[35] != null ? object[35].toString() : "");
			rel.setDoc(object[34] != null ? object[34].toString() : "");
			rel.setSinalizador(Util.getNullValue(object[22], ""));
			rel.setLocalidadeUC(Util.getNullValue(object[40], ""));
			rel.setLocalidade(Util.getNullValue(object[41], ""));
			rel.setCategoriaDespesa(Util.getNullValue(object[45], ""));
			rel.setPagador(object[3].toString());
			rel.setRecebedor(object[4].toString());
			rel.setDescricao(object[6] != null ? object[6].toString() : "");
			rel.setFonte(object[7] != null ? object[7].toString() : "");
			rel.setDoacao(object[32] != null ? object[32].toString() : "");
			rel.setProjeto(object[8] != null ? object[8].toString() : "");
			rel.setStatus(object[28].toString());
			rel.setParcela(object[13].toString());
			rel.setTipo(Util.getNullValue(object[16], ""));
			rel.setRubrica(Util.getNullValue(object[27], ""));
			rel.setComponente(Util.getNullValue(object[30], ""));
			rel.setSubcomponente(Util.getNullValue(object[31], ""));
			rel.setSinalizador(Util.getNullValue(object[22], ""));
			rel.setGestao(Util.getNullValue(object[42], ""));
			rel.setLocalidadeProjeto(Util.getNullValue(object[39], ""));

			if (rel.getSinalizador().equals("-")) {
				rel.setSaida(Double.valueOf(object[9].toString()));
			} else {
				rel.setEntrada(Double.valueOf(object[9].toString()));
			}

			relatorio.add(rel);

		}

		return relatorio;

	}

	public List<Projeto> getGroupProjectsByFonte(Filtro filtro) {
		StringBuilder hql = new StringBuilder("select p.id, p.nome from");
		hql.append(" projeto p join projeto_rubrica pr on p.id = pr.projeto_id");
		hql.append(" join rubrica_orcamento ro on pr.rubricaorcamento_id = pr.rubricaorcamento_id");
		hql.append(" join orcamento o on o.id = ro.orcamento_id");
		hql.append(" join fonte_pagadora fp on o.fonte_id = fp.id");
		hql.append(" where fp.id in (:fontes)");
		hql.append("  group by  p.id , p.nome");

		Query query = this.manager.createNativeQuery(hql.toString());

		List<Integer> list = new ArrayList<>();
		Integer[] mList = filtro.getFontes();
		for (int i = 0; i < filtro.getFontes().length; i++) {
			list.add(mList[i]);
		}

		query.setParameter("fontes", list);

		List<Object[]> result = query.getResultList();
		List<Projeto> projetos = new ArrayList<>();
		Projeto projeto;
		for (Object[] obj : result) {
			projeto = new Projeto();
			projeto.setId(new Long(obj[0].toString()));
			projeto.setNome(obj[1].toString());
			projetos.add(projeto);
		}

		return projetos;
	}

}
