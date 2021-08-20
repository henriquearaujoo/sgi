package repositorio.management.panel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import repositorio.management.panel.models.Filtro;
import service.management.panel.ModelsSource;
import util.Util;

public class BarHorizontalChartRepositorio {

	@Inject
	private EntityManager manager;

	public BarHorizontalChartRepositorio() {
	}

	public BarHorizontalChartRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public List<ModelsSource> loadTotalOfCExecutionBySource(String type, Filtro filtro) {

		Query query = this.manager.createNativeQuery(getSqlToExecutionBySource(type, filtro));
		setQueryExecuted(query, filtro);
		List<Object[]> result = query.getResultList();
		List<ModelsSource> effectiveds = new ArrayList<ModelsSource>();

		for (Object[] object : result) {
			ModelsSource effectived = new ModelsSource();
			effectived.setId(new Long(object[0].toString()));
			effectived.setSource(Util.getNullValue(object[1], ""));
			effectived.setEffectiveValue(Util.getNullValue(new BigDecimal(object[2].toString())));
			effectiveds.add(effectived);
		}

		return effectiveds;
	}
	
	public void setQueryExecuted(Query query, Filtro filtro) {
		query.setParameter("gestao_id", filtro.getGestao());
	}

	public String getSqlToExecutionBySource(String type, Filtro filtro) {

		StringBuilder hql = new StringBuilder("select ");
		hql.append("fp.id, fp.nome, ");
		hql.append("sum(pl.valor) as valor_pago ");
		hql.append("from fonte_pagadora fp join orcamento o on fp.id = o.fonte_id ");
		hql.append("join rubrica_orcamento ro on ro.orcamento_id  = o.id ");
		hql.append("join projeto_rubrica pr on pr.rubricaorcamento_id = ro.id ");
		hql.append("join projeto p on p.id = pr.projeto_id ");
		hql.append("join lancamento_acao la on la.projetorubrica_id  = pr.id ");
		hql.append("join pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");
		hql.append("join lancamento l on l.id = la.lancamento_id ");
		hql.append("join conta_bancaria conta_pagador on conta_pagador.id = pl.conta_id ");
		hql.append("join conta_bancaria conta_recebedor on conta_recebedor.id = pl.contarecebedor_id ");
		hql.append("where 1 = 1 ");
		
		if (filtro.getShowUnique()) {
			hql.append("and p.gestao_id = :gestao_id ");
		} else {
			hql.append(
					"and ((p.gestao_id  = :gestao_id) or ((select g.superintendencia_id from gestao g where g.id = p.gestao_id) = :gestao_id)) ");
		}
		
		hql.append("and l.tipo != 'compra' ");
		hql.append("and conta_pagador.tipo = 'CB' ");
		hql.append("and conta_recebedor.tipo = 'CF' ");
		
		hql.append(filtro.getClauseYearExecuted());

		if (type == "effectived") {
			hql.append("and pl.stt = 'EFETIVADO' ");
			hql.append("and l.statuscompra = 'CONCLUIDO' ");
		} else {
			hql.append("and l.statuscompra in ('CONCLUIDO','N_INCIADO') ");
		}

		hql.append("and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) ");
		hql.append("group by  fp.id, fp.nome ");
		hql.append("order by sum(pl.valor) desc  ");

		return hql.toString();
	}

	public List<ModelsSource> loadTotalOfPlaningBySource(Filtro filtro) {
		return buildPlanningListByDateProject(filtro);
	}

	public List<ModelsSource> buildPlanningListByDateProject(Filtro filtro) {
		List<ModelsSource> plannings = new ArrayList<ModelsSource>();

		Query query = this.manager.createNativeQuery(getSqlToPlanningWithFilterDate(filtro));
		setQueryPlanning(query, filtro);

		List<Object[]> result = query.getResultList();

		for (Object[] object : result) {
			ModelsSource planning = new ModelsSource();
			planning.setId(new Long(object[0].toString()));
			planning.setSource(Util.getNullValue(object[1], ""));
			planning.setPlanningValue(Util.getNullValue(new BigDecimal(object[7].toString())));
			accumulatePlanning(planning, plannings);
		}

		return plannings;

	}

	public void setQueryPlanning(Query query, Filtro filtro) {
		query.setParameter("gestao_id", filtro.getGestao());
	}

	public void accumulatePlanning(ModelsSource model, List<ModelsSource> plannings) {

		for (ModelsSource planning : plannings) {
			if (planning.getId().longValue() == model.getId().longValue()) {
				planning.setPlanningValue(planning.getPlanningValue().add(model.getPlanningValue()));
				return;
			}
		}

		plannings.add(model); // Vai fazer automáticamente uma só vez

	}

	public String getSqlToPlanningWithFilterDate(Filtro filtro) {

		StringBuilder hql = new StringBuilder("select ");
		hql.append("fp.id as id_fonte, ");
		hql.append("fp.nome as fonte, ");
		hql.append("sum(pr.valor) as valor_orcado, ");
		hql.append("p.id as id_project, ");
		hql.append("p.nome as projeto, ");
		hql.append("p.datainicio ,");
		hql.append("p.datafinal ,");
		hql.append("case when ");
		hql.append(filtro.getClauseYearPlanningCase());
		hql.append("then ");
		hql.append(filtro.getClauseInitCalcMonth());
		hql.append("else ");
		hql.append(filtro.getClauseEndCalcMonth());
		hql.append(" end  as VALOR_PROJETO_ANO ");

		hql.append("from fonte_pagadora fp ");
		hql.append("join orcamento o on fp.id = o.fonte_id ");
		hql.append("join rubrica_orcamento ro on ro.orcamento_id  = o.id ");
		hql.append("join projeto_rubrica pr on pr.rubricaorcamento_id = ro.id ");
		hql.append("join projeto p on p.id = pr.projeto_id ");
		hql.append("where 1 = 1 ");

		if (filtro.getShowUnique()) {
			hql.append("and p.gestao_id = :gestao_id ");
		} else {
			hql.append(
					"and ((p.gestao_id  = :gestao_id) or ((select g.superintendencia_id from gestao g where g.id = p.gestao_id) = :gestao_id)) ");
		}

		hql.append(filtro.getClauseYearPlanningDateEnd());

		// hql.append("and (p.datainicio >= '2021-01-01' or p.datafinal >= '2021-01-01')
		// ");

		hql.append("group by fp.id, fp.nome , p.id, p.nome ");
		hql.append("order by  sum(pr.valor) desc ");

		String result = hql.toString();

		return result;
	}

}
