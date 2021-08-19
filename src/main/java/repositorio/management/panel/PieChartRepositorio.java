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
import util.Filtro;

public class PieChartRepositorio {

	@Inject
	private EntityManager manager;

	public PieChartRepositorio() {
	}

	public PieChartRepositorio(EntityManager manager) {
		this.manager = manager;
	}
	
	public BigDecimal loadTotalOfPlanning() {
		Query query = this.manager.createNativeQuery(getSqlByPlanning());
		// query.setParameter("orcamento_id", orcamento.getId());
		BigDecimal result = (BigDecimal) query.getSingleResult();
		return result;
	}

	public BigDecimal loadTotalOfProvisioned() {
		Query query = this.manager.createNativeQuery(getSqlByProvisioned());
		// query.setParameter("orcamento_id", orcamento.getId());
		BigDecimal result = (BigDecimal) query.getSingleResult();
		return result;
	}

	public BigDecimal loadTotalOfEffectived() {

		Query query = this.manager.createNativeQuery(getSqlByEffectived());
		// query.setParameter("orcamento_id", orcamento.getId());
		BigDecimal result = (BigDecimal) query.getSingleResult();
		return result;
	}

	public String getSqlByPlanning() {
		return "select\n" + "sum(pr.valor)\n" + "from fonte_pagadora fp\n" + "join orcamento o on fp.id = o.fonte_id\n"
				+ "join rubrica_orcamento ro on ro.orcamento_id  = o.id\n"
				+ "join projeto_rubrica pr on pr.rubricaorcamento_id = ro.id\n"
				+ "join projeto p on p.id = pr.projeto_id\n" + "where \n"
				// + "-- o.id = 231 and \n"
				+ "((p.gestao_id  = 36) or\n"
				+ "((select g.superintendencia_id from gestao g where g.id = p.gestao_id) = 36))\n";
		// + "-- and\n"
		// + "-- l.tipo != 'compra'\n"
		// + "-- and l.id = 98900\n"
		// + "-- and l.statuscompra = 'CONCLUIDO'\n"
		// + "-- and (l.versionlancamento = 'MODE01' or pl.reclassificado is true)";
	}

	public String getSqlByProvisioned() {
		return "select\n" + "sum(pl.valor)\n" + "from fonte_pagadora fp\n" + "join orcamento o on fp.id = o.fonte_id\n"
				+ "join rubrica_orcamento ro on ro.orcamento_id  = o.id\n"
				+ "join projeto_rubrica pr on pr.rubricaorcamento_id = ro.id\n"
				+ "join projeto p on p.id = pr.projeto_id\n"
				+ "join lancamento_acao la on la.projetorubrica_id  = pr.id\n"
				+ "join pagamento_lancamento pl on pl.lancamentoacao_id = la.id\n"
				+ "join lancamento l on l.id = la.lancamento_id\n"
				+ "join conta_bancaria conta_pagador on conta_pagador.id = pl.conta_id\n"
				+ "join conta_bancaria conta_recebedor on conta_recebedor.id = pl.contarecebedor_id\n" + "where\n"
				+ "((p.gestao_id  = 36) or\n"
				+ "((select g.superintendencia_id from gestao g where g.id = p.gestao_id) = 36))\n" + "-- o.id = 231\n"
				+ "and l.tipo != 'compra'\n" + "and conta_pagador.tipo = 'CB'\n" + "and conta_recebedor.tipo = 'CF'\n"
				+ "and pl.stt = 'PROVISIONADO'\n" + "-- and l.id = 98900\n" + "-- and l.statuscompra = 'CONCLUIDO'\n"
				+ "and (l.versionlancamento = 'MODE01' or pl.reclassificado is true)";
	}

	public String getSqlByEffectived() {
		return "select\n" + "sum(pl.valor)\n" + "from fonte_pagadora fp\n" + "join orcamento o on fp.id = o.fonte_id\n"
				+ "join rubrica_orcamento ro on ro.orcamento_id  = o.id\n"
				+ "join projeto_rubrica pr on pr.rubricaorcamento_id = ro.id\n"
				+ "join projeto p on p.id = pr.projeto_id\n"
				+ "join lancamento_acao la on la.projetorubrica_id  = pr.id\n"
				+ "join pagamento_lancamento pl on pl.lancamentoacao_id = la.id\n"
				+ "join lancamento l on l.id = la.lancamento_id\n"
				+ "join conta_bancaria conta_pagador on conta_pagador.id = pl.conta_id\n"
				+ "join conta_bancaria conta_recebedor on conta_recebedor.id = pl.contarecebedor_id\n" + "where\n"
				+ "((p.gestao_id  = 36) or\n"
				+ "((select g.superintendencia_id from gestao g where g.id = p.gestao_id) = 36))\n" + "-- o.id = 231\n"
				+ "and l.tipo != 'compra'\n" + "and conta_pagador.tipo = 'CB'\n" + "and conta_recebedor.tipo = 'CF'\n"
				+ "and pl.stt = 'EFETIVADO'\n" + "-- and l.id = 98900\n" + "-- and l.statuscompra = 'CONCLUIDO'\n"
				+ "and (l.versionlancamento = 'MODE01' or pl.reclassificado is true)";

	}

}
