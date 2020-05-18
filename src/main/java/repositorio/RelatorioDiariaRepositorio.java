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
import model.RelatorioDiaria;
import util.DataUtil;
import util.Filtro;
import util.Util;

public class RelatorioDiariaRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public RelatorioDiariaRepositorio() {
	}

	public List<RelatorioDiaria> getDiaria(Filtro filtro) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		StringBuilder hql = new StringBuilder(" select ");

		hql.append("(select g.nome from gestao as g where g.id=l.gestao_id) as gestao_00,");
		hql.append("(select c.nome_conta from conta_bancaria as c where c.id=l.contarecebedor_id) as profissional_01,");
		hql.append("(select d.mascara from localidade as d where d.id=sv.localidade_id) as localidade_02,");
		hql.append("to_char(sv.data_ida, 'DD-MM-YYYY') as data_ida_03,");
		hql.append("to_char(sv.data_volta, 'DD-MM-YYYY') as data_volta_04,");
		hql.append("l.diasnocampo as diasnocampo_05,");
		hql.append("l.valor_total_com_desconto as valor_total_com_desconto_06,");
		hql.append("l.id as id_diaria_07,");
		hql.append("sv.missao as id_viagem_08,");
		hql.append("to_char(sv.data_emissao, 'DD-MM-YYYY') as data_emissao_09,");
		hql.append("l.descricao descricao_10 ");
		hql.append("FROM lancamento as l  join  solicitacaoviagem as sv on sv.id = l.solicitacaoviagem_id where");

		hql.append(" l.data_emissao >= '");
		hql.append(sdf.format(filtro.getDataInicio()));
		hql.append("' and ");
		hql.append("l.data_emissao <= '");
		hql.append(sdf.format(filtro.getDataFinal()));
		hql.append("' ");

		if (filtro.getFornecedorID() != null) {
			hql.append(" and l.contarecebedor_id = :paramFornecedor");

		}

		if (filtro.getGestaoID() != null) {
			hql.append(" and l.gestao_id = :paramGestao ");
		}

		Query query = this.manager.createNativeQuery(hql.toString());

		if (filtro.getFornecedorID() != null) {
			query.setParameter("paramFornecedor", filtro.getFornecedorID());
		}

		if (filtro.getGestaoID() != null) {
			query.setParameter("paramGestao", filtro.getGestaoID());
		}

		List<Object[]> result = query.getResultList();
		List<RelatorioDiaria> lista = new ArrayList<>();
		RelatorioDiaria relatorio = new RelatorioDiaria();
		for (Object[] objects : result) {
			relatorio = new RelatorioDiaria();

			relatorio.setGestao(Util.getNullValue(objects[0], ""));
			relatorio.setNomeProfissional(Util.getNullValue(objects[1], ""));
			relatorio.setLocalidade(Util.getNullValue(objects[2], ""));
			relatorio.setDataIda(objects[3] != null ? DataUtil.converteDataSql(objects[3].toString()) : null);
			relatorio.setDataVolta(objects[4] != null ? DataUtil.converteDataSql(objects[4].toString()) : null);
			relatorio.setDiasEmCampo(objects[5] != null ? Integer.valueOf(objects[5].toString()) : 0);
			relatorio.setValorTotal(objects[6] != null ? new BigDecimal(objects[6].toString()) : BigDecimal.ZERO);
			relatorio.setIdDiaria(objects[7] != null ? new Long(objects[7].toString()) : 0);
			relatorio.setMissao(Util.getNullValue(objects[8], ""));
			relatorio.setDataEmissao(objects[9] != null ? DataUtil.converteDataSql(objects[9].toString()) : null);
			relatorio.setDescricao(Util.getNullValue(objects[10], ""));
			lista.add(relatorio);
		}

		return lista;
	}

	public List<ContaBancaria> getFornecedor() {
		String jpql = "SELECT NEW ContaBancaria(cb.id, cb.nomeConta) FROM ContaBancaria AS cb where cb.tipo = :tipo";
		Query query = manager.createQuery(jpql);
		query.setParameter("tipo", "CF");
		return query.getResultList();
	}

	public List<Gestao> getGestao() {
		StringBuilder jpql = new StringBuilder("SELECT NEW Gestao(g.id, g.nome) from Gestao g");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList();
	}

}
