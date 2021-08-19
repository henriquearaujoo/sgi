package repositorio.management.panel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
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

import antlr.Utils;
import model.Acao;
import model.AcaoProduto;
import model.Componente;
import model.DonationManagement;
import model.GrupoDeInvestimento;
import model.MetaPlano;
import model.Orcamento;
import model.Projeto;
import model.TransferenciaOverHead;
import service.management.panel.Models;
import util.Filtro;
import util.Util;

public class BarHorizontalChartRepositorio {

	@Inject
	private EntityManager manager;

	public BarHorizontalChartRepositorio() {
	}

	public BarHorizontalChartRepositorio(EntityManager manager) {
		this.manager = manager;
	}
	
	public List<Models> loadTotalOfEffectivedBySource() {
		Query query = this.manager.createNativeQuery(getSqlToEffectivedBySource());

		List<Object[]> result = query.getResultList();
		List<Models> effectiveds = new ArrayList<Models>();

		for (Object[] object : result) {
			Models effectived = new Models();
			effectived.setId(new Long(object[0].toString()));
			effectived.setSource(Util.getNullValue(object[1], ""));
			effectived.setEffectiveValue(Util.getNullValue(new BigDecimal(object[2].toString())));
			effectiveds.add(effectived);
		}

		return effectiveds;
	}
	
	public String getSqlToEffectivedBySource() {
		return "select\n" + "fp.id,fp.nome, sum(pl.valor) as valor_pago\n" + "from fonte_pagadora fp\n"
				+ "join orcamento o on fp.id = o.fonte_id\n" + "join rubrica_orcamento ro on ro.orcamento_id  = o.id\n"
				+ "join projeto_rubrica pr on pr.rubricaorcamento_id = ro.id\n"
				+ "join projeto p on p.id = pr.projeto_id\n"
				+ "join lancamento_acao la on la.projetorubrica_id  = pr.id\n"
				+ "join pagamento_lancamento pl on pl.lancamentoacao_id = la.id\n"
				+ "join lancamento l on l.id = la.lancamento_id\n"
				+ "join conta_bancaria conta_pagador on conta_pagador.id = pl.conta_id\n"
				+ "join conta_bancaria conta_recebedor on conta_recebedor.id = pl.contarecebedor_id\n" + "where\n"
				+ "((p.gestao_id  = 36) or ((select g.superintendencia_id from gestao g where g.id = p.gestao_id) = 36))\n"
				+ "and l.tipo != 'compra' \n" + "and conta_pagador.tipo = 'CB'\n" + "and conta_recebedor.tipo = 'CF'\n"
				+ "-- and pl.stt = 'EFETIVADO'\n" + "-- and l.statuscompra = 'CONCLUIDO'\n"
				+ "and (l.versionlancamento = 'MODE01' or pl.reclassificado is true)\n"
				+ "group by fp.id, fp.nome order by sum(pl.valor) desc ";
	}
	
	
	public List<Models> loadTotalOfPlaningBySource() {
		Query query = this.manager.createNativeQuery(getSqlToPlanningBySource());

		List<Object[]> result = query.getResultList();
		List<Models> plannings = new ArrayList<Models>();

		for (Object[] object : result) {
			Models planning = new Models();
			planning.setId(new Long(object[0].toString()));
			planning.setSource(Util.getNullValue(object[1], ""));
			planning.setPlanningValue(Util.getNullValue(new BigDecimal(object[2].toString())));
			plannings.add(planning);
		}

		return plannings;
	}


	public String getSqlToPlanningBySource() {
		return "select\n" + "fp.id, fp.nome as fonte,\n" + "sum(pr.valor) as valor_orcado\n"
				+ "from fonte_pagadora fp\n" + "join orcamento o on fp.id = o.fonte_id\n"
				+ "join rubrica_orcamento ro on ro.orcamento_id  = o.id\n"
				+ "join projeto_rubrica pr on pr.rubricaorcamento_id = ro.id\n"
				+ "join projeto p on p.id = pr.projeto_id\n" + "where \n" + "((p.gestao_id  = 36) or\n"
				+ "((select g.superintendencia_id from gestao g where g.id = p.gestao_id) = 36))\n"
				+ "group by fp.id, fp.nome order by  sum(pr.valor) desc";
	}

	

}
