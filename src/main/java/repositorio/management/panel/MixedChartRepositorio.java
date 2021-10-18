package repositorio.management.panel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.collections.ArrayStack;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import model.Acao;
import model.AcaoProduto;
import model.Componente;
import model.DonationManagement;
import model.GrupoDeInvestimento;
import model.LancamentoAuxiliar;
import model.MetaPlano;
import model.Orcamento;
import model.Projeto;
import model.TransferenciaOverHead;
import repositorio.management.panel.models.Filtro;
import service.management.panel.ModelsMonth;
import service.management.panel.ModelsSource;
import util.Util;

public class MixedChartRepositorio {

	@Inject
	private EntityManager manager;

	public MixedChartRepositorio() {
	}

	public MixedChartRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public List<ModelsMonth> loadTotalOfEffectivedByMonth(Filtro filtro) {
		Query query = this.manager.createNativeQuery(getSqlToEffectivedByMonth(filtro));
		setQueryExecuted(query, filtro);
		List<Object[]> result = query.getResultList();
		List<ModelsMonth> months = new ArrayList<ModelsMonth>();

		int lastIndex = Calendar.getInstance().get(Calendar.MONTH) + 1;

		for (int a = 1; a <= lastIndex; a++) { // values.add(Math.round()); ModelsMonth
			ModelsMonth month = new ModelsMonth(a, BigDecimal.ZERO);
			months.add(month);
		}

		for (Object[] object : result) {
			ModelsMonth month = new ModelsMonth();
			month.setMonth(Integer.valueOf(object[1].toString()));
			month.setValue(Util.getNullValue(new BigDecimal(object[2].toString())));

			if (month.getMonth() > lastIndex) {
				return months;
			}

			months.set(month.getMonth() - 1, month);
		}

		return months;
	}
	
	public void setQueryExecuted(Query query, Filtro filtro) {
		query.setParameter("gestao_id", filtro.getGestao());
	}

	public BigDecimal getValuOfMonth(List<ModelsMonth> months, int index) {

		return BigDecimal.ZERO;
	}

	public String getSqlToEffectivedByMonth(Filtro filtro) {

		StringBuilder hql = new StringBuilder("select ");
		hql.append("to_char(pl.datapagamento, 'YYYY') as ano,");
		hql.append("to_char(pl.datapagamento, 'MM') as mes,");
		hql.append("sum(pl.valor)  as valor_pago_na_fonte ");
		hql.append("from fonte_pagadora fp ");
		hql.append("join orcamento o on fp.id = o.fonte_id ");
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
		hql.append("and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) ");
		// if (type == "effectived") {
		hql.append("and pl.stt = 'EFETIVADO' ");
		hql.append("and l.statuscompra = 'CONCLUIDO' ");
		// } else {
		// hql.append("and l.statuscompra in ('CONCLUIDO','N_INCIADO') ");
		// }

		hql.append("group by ano, mes ");
		hql.append("order by ano, mes ");
		
		String result = hql.toString();

		return hql.toString();
	}
	
	public List<LancamentoAuxiliar> getStatementExecution(Filtro filtro) {
		StringBuilder jpql = new StringBuilder(getSqlToStatementExecution(filtro));
		
		Query query = manager.createNativeQuery(jpql.toString());
		if (filtro.getIdFonte() != null) {
			query.setParameter("fonte_id", filtro.getIdFonte());
		}
		
		if (filtro.getIdProjeto() != null) {
			query.setParameter("projeto_id", filtro.getIdProjeto());
		}
		setQueryExecuted(query, filtro);
		
		List<LancamentoAuxiliar> retorno = new ArrayList<>();
		LancamentoAuxiliar lancamento = new LancamentoAuxiliar();
		
		int allResults = query.getResultList().size();
		
		query.setFirstResult((filtro.getPageCurrent() - 1) * filtro.getItemForPage());
		query.setMaxResults(filtro.getItemForPage());
		
		filtro.setPageSize(allResults / filtro.getItemForPage());
		filtro.setLastPage(filtro.getPageSize()+1);
		
		List<Object[]> result = query.getResultList();
		
		for(Object[] object : result ) {
			lancamento = new LancamentoAuxiliar();
			lancamento.setId(new Long(object[1].toString()));
			lancamento.setNomeProjeto(Util.getNullValue(object[2], ""));
			lancamento.setContaRecebedorLbl(Util.getNullValue(object[3], ""));
			lancamento.setValorPagoAcao(new BigDecimal(object[4].toString()));
			lancamento.setFonte(Util.getNullValue(object[5], ""));
			lancamento.setTipov4(Util.getNullValue(object[6], ""));
			retorno.add(lancamento);
		}
		
		return retorno;
	}
	
	
	public String getSqlToStatementExecution(Filtro filtro) {

		StringBuilder hql = new StringBuilder("select ");
		hql.append("(select g.nome from gestao g where g.id = p.gestao_id) as gestao_00, l.id, ");
		hql.append("p.nome as projeto, conta_recebedor.nome_conta, l.valor_total_com_desconto, fp.nome as fonte, l.tipo_v4 ");
		hql.append("from fonte_pagadora fp ");
		hql.append("join orcamento o on fp.id = o.fonte_id ");
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
		
		if (filtro.getIdFonte() != null) {
			hql.append(" and fp.id = :fonte_id ");
		}
		
		if (filtro.getIdProjeto() != null) {
			hql.append(" and p.id = :projeto_id ");
		}

		hql.append("and l.tipo != 'compra' ");
		hql.append("and conta_pagador.tipo = 'CB' ");
		hql.append("and conta_recebedor.tipo = 'CF' ");
		hql.append(filtro.getClauseYearExecuted());
		hql.append("and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) ");
		// if (type == "effectived") {
		hql.append("and pl.stt = 'EFETIVADO' ");
		hql.append("and l.statuscompra = 'CONCLUIDO' ");
		hql.append(" order by fonte");
		// } else {
		// hql.append("and l.statuscompra in ('CONCLUIDO','N_INCIADO') ");
		// }

		return hql.toString();
	}

}
