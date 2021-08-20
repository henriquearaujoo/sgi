package repositorio.management.panel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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
import model.MetaPlano;
import model.Orcamento;
import model.Projeto;
import model.TransferenciaOverHead;
import repositorio.management.panel.models.Filtro;

public class PieChartRepositorio {

	@Inject
	private EntityManager manager;

	public PieChartRepositorio() {
	}

	public PieChartRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public BigDecimal loadTotalOfExecuted(String type, Filtro filtro) {
		Query query = this.manager.createNativeQuery(getSqlByExecutionWithParams(type, filtro));
		setQuery(query, filtro);
		BigDecimal result = (BigDecimal) query.getSingleResult();
		return result != null ? result : BigDecimal.ZERO;
	}

	public void setQuery(Query query, Filtro filtro) {
		query.setParameter("gestao_id", filtro.getGestao());
	}

	public String getSqlByExecutionWithParams(String type, Filtro filtro) {
		StringBuilder hql = new StringBuilder("select ");
		hql.append("sum(pl.valor) ");
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
		hql.append("where ");
		hql.append(" l.tipo != 'compra' ");
		hql.append("and conta_pagador.tipo = 'CB' ");
		hql.append("and conta_recebedor.tipo = 'CF' ");
		hql.append("and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) ");
		
		if (filtro.getShowUnique()) {
			hql.append("and p.gestao_id = :gestao_id ");
		}else {
			hql.append(
					"and ((p.gestao_id  = :gestao_id) or ((select g.superintendencia_id from gestao g where g.id = p.gestao_id) = :gestao_id)) ");
		}
		
		if (type == "effectived") {
			hql.append("and pl.stt = 'EFETIVADO' ");
			hql.append("and l.statuscompra = 'CONCLUIDO'");
		} else {
			hql.append("and pl.stt = 'PROVISIONADO' ");
			hql.append("and l.statuscompra in ('CONCLUIDO', 'N_INCIADO')");
		}

		if (filtro.getAno() != null) {
			hql.append(filtro.getClauseYearExecuted());
		}

		

		String result = hql.toString();

		return result;
	}

}
