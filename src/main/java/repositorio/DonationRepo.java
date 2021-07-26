package repositorio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.LancamentoAuxiliar;
import model.TipoParcelamento;
import util.DateConverter;
import util.Filtro;
import util.Util;

public class DonationRepo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	protected EntityManager manager;

	public DonationRepo(EntityManager manager) {
		this.manager = manager;
	}

	public DonationRepo() {
	}

	public List<LancamentoAuxiliar> loadDisbursementInProject(Filtro filtro) {

		StringBuilder hql = new StringBuilder("");
		hql.append("select ");
		hql.append("(select g.nome from gestao g where g.id = p.gestao_id) as gestao_00, ");
		hql.append("fp.nome as fonte_pagadora_01 ,");
		hql.append("p.nome  as projeto_02, ");
		hql.append("l.tipo_v4 as tipo_03,");
		hql.append(
				"(l.tipo_v4  || ' - ' || l.id || (case when l.tipo_v4 = 'PC' then '/'||l.compra_id else '' end)) as identificao_04,");
		hql.append("replace(l.descricao ,';',':') as descricao_05,");
		hql.append("l.id as lancamento_06, ");
		hql.append("conta_pagador.tipo as tipo_pagador_07,");
		hql.append("conta_pagador.nome_conta as conta_pagador_08,");
		hql.append("conta_recebedor.tipo as tipo_recebedor_09,");
		hql.append("conta_recebedor.nome_conta as conta_recebedor_10,");
		hql.append("to_char(pl.datapagamento ,'DD-MM-YYYY') as data_pagamento_11,");
		hql.append("l.valor_total_com_desconto as valor_total_com_desconto_12,");
		hql.append("pl.valor as valor_pago_13, ");
		hql.append("pl.stt as status_pagamento_14 ");
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
		hql.append("where o.id = :id_donation ");
		hql.append("and l.tipo != 'compra' ");
		hql.append("and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) ");
		hql.append("and conta_pagador.tipo = 'CB' ");
		hql.append("and conta_recebedor.tipo = 'CF' ");
		hql.append("order by pl.datapagamento desc");

		Query query = manager.createNativeQuery(hql.toString());
		query.setParameter("id_donation", filtro.getIdDoacao());
		
		List<LancamentoAuxiliar> retorno = new ArrayList<LancamentoAuxiliar>();
		List<Object[]> result = query.getResultList();
		LancamentoAuxiliar lancamento = new LancamentoAuxiliar();

		for (Object[] object : result) {

			lancamento = new LancamentoAuxiliar();
			lancamento.setId(new Long(object[6].toString()));
			lancamento.setNomeGestao(Util.getNullValue(object[0], ""));
			lancamento.setFonte(Util.getNullValue(object[1], ""));
			lancamento.setNomeProjeto(Util.getNullValue(object[2], ""));
			lancamento.setTipov4(Util.getNullValue(object[3], ""));
			lancamento.setContaPagadorLbl(Util.getNullValue(object[8], ""));
			lancamento.setContaRecebedorLbl(Util.getNullValue(object[10], ""));
			lancamento.setDescricao(Util.getNullValue(object[5], ""));
			lancamento.setDataPagamento(DateConverter.converteDataSql(object[11].toString()));
			lancamento.setValorPagoAcao(object[13] != null ? new BigDecimal(object[13].toString()) : BigDecimal.ZERO);
			lancamento.setStatus(Util.getNullValue(object[14], ""));
			retorno.add(lancamento);

		}

		return retorno;
	}

}
