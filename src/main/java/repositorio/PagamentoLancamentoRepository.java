package repositorio;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.google.common.util.concurrent.ExecutionError;

import model.Acao;
import model.Configuracao;
import model.ContaBancaria;
import model.FontePagadora;
import model.Fornecedor;
import model.Lancamento;
import model.LancamentoAcao;
import model.LancamentoAuxiliar;
import model.LancamentoAvulso;
import model.Log;
import model.PagamentoLancamento;
import model.PagamentoPE;
import model.Projeto;
import model.ProjetoRubrica;
import model.SolicitacaoPagamento;
import model.StatusCompra;
import model.StatusConta;
import model.TipoParcelamento;
import model.User;
import util.DateConverter;
import util.Filtro;
import util.Util;

public class PagamentoLancamentoRepository {

	@Inject
	private EntityManager manager;

	public PagamentoLancamentoRepository() {
	}

	public PagamentoLancamentoRepository(EntityManager manager) {
		this.manager = manager;
	}

	public PagamentoLancamento findById(Long id) {
		String jpql = "from PagamentoLancamento where id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		PagamentoLancamento pag = query.getResultList().size() > 0 ? (PagamentoLancamento) query.getResultList().get(0)
				: new PagamentoLancamento();
		return pag;

		// return manager.find(PagamentoLancamento.class, id);
	}

	public void efetivarPagamentos(Long id, String data, Long conta) {
		StringBuilder hql = new StringBuilder("update pagamento_lancamento  set stt = 'EFETIVADO'  ");
		hql.append("WHERE (select la.lancamento_id from lancamento_acao la where la.id = lancamentoacao_id) = :id ");
		hql.append("and to_char(datapagamento,'dd/MM/yyyy') =  :data ");
		hql.append("AND conta_id = :conta");

		Query query = this.manager.createNativeQuery(hql.toString());

		query.setParameter("id", id);
		query.setParameter("data", data);
		query.setParameter("conta", conta);
		query.executeUpdate();
	}

	public void provisionarPagamentos(Long id, String data, Long conta) {
		StringBuilder hql = new StringBuilder("update pagamento_lancamento  set stt = 'PROVISIONADO'  ");
		hql.append("WHERE (select la.lancamento_id from lancamento_acao la where la.id = lancamentoacao_id) = :id ");
		hql.append("and to_char(datapagamento,'dd/MM/yyyy') =  :data ");
		hql.append("AND conta_id = :conta");

		Query query = this.manager.createNativeQuery(hql.toString());

		query.setParameter("id", id);
		query.setParameter("data", data);
		query.setParameter("conta", conta);
		query.executeUpdate();
	}

	public PagamentoLancamento salvar(PagamentoLancamento pagamento) {
		pagamento.setTipoContaPagador(pagamento.getConta().getTipo());
		pagamento.setTipoContaRecebedor(pagamento.getContaRecebedor().getTipo());
		return manager.merge(pagamento);
	}

	public Lancamento salvar(Lancamento lancamento) {
		return manager.merge(lancamento);
	}

	public LancamentoAcao salvar(LancamentoAcao lancamentoAcao) {
		return manager.merge(lancamentoAcao);
	}

	public boolean remover(PagamentoLancamento pagamento) {
		String hql = "delete from pagamento_lancamento where id = :id";
		Query query = this.manager.createNativeQuery(hql);
		query.setParameter("id", pagamento.getId());
		query.executeUpdate();

		// this.manager.remove(this.manager.find(PagamentoLancamento.class,
		// pagamento.getId()));
		return true;
	}

	public List<FontePagadora> getFontes() {
		String jpql = "from FontePagadora";
		Query query = manager.createQuery(jpql);
		return query.getResultList();
	}

	public List<Fornecedor> getFornecedores() {
		String jpql = "SELECT NEW Fornecedor(f.id, f.nomeFantasia) from Fornecedor as f";
		Query query = manager.createQuery(jpql);
		return query.getResultList();
	}

	public List<Projeto> getProjetos() {
		String jpql = "from Projeto";
		Query query = manager.createQuery(jpql);
		return query.getResultList();
	}

	public List<Acao> getAcoes() {
		String jpql = "from Acao";
		Query query = manager.createQuery(jpql);
		return query.getResultList();
	}

	public List<ContaBancaria> getContas() {
		String jpql = "SELECT NEW ContaBancaria(cb.id,cb.nomeConta) from ContaBancaria cb where cb.tipo = 'CB' ";
		Query query = manager.createQuery(jpql);
		return query.getResultList();
	}

	public List<ContaBancaria> getContasFiltro() {
		String jpql = "SELECT NEW ContaBancaria(cb.id,cb.nomeConta) from ContaBancaria cb ";
		Query query = manager.createQuery(jpql);
		return query.getResultList();
	}

	public LancamentoAcao getLancamentoAcao(Long id) {
		String jpql = "from LancamentoAcao where id = :id";
		Query query = manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? (LancamentoAcao) query.getResultList().get(0) : null;
	}

	public List<PagamentoLancamento> getPagamentos(Long id) {
		String jpql = "from PagamentoLancamento where lancamentoAcao.id = :id";
		Query query = manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public List<LancamentoAuxiliar> getLancamentosReport(Filtro filtro) {

		// hql
		StringBuilder hql = new StringBuilder("");
		hql.append("select a.id as id_acao,");
		hql.append(" l.id as id_lancamento,");
		hql.append(" l.descricao,");
		hql.append(" (select f.nome from  fonte_pagadora f where f.id = la.fontepagadora_id) as fonte,");
		hql.append(" a.codigo as codigo,");
		hql.append(" la.valor as valor_pago_by_acao,");
		hql.append(" l.valor_total_com_desconto as valor_total_lancamento, ");
		hql.append(" to_char(l.data_emissao,'DD-MM-YYYY') as data_emissao,");
		hql.append(" to_char(l.data_pagamento,'DD-MM-YYYY') as data_pagamento,");
		hql.append(" la.id as id_lancamento_acao,");
		hql.append(" l.id as codigo_lancamento,");
		hql.append(" l.quantidade_parcela as qtd_parcela, ");
		hql.append("(select f.nome_fantasia from  fornecedor f where f.id = l.fornecedor_id) as fornecedor,");
		hql.append(" l.tipoparcelamento,");
		hql.append(
				" (select count.nome_conta from conta_bancaria count where count.id = l.contapagador_id) as conta_pagador,");
		hql.append(
				" (select count.nome_conta from conta_bancaria count where count.id = l.contarecebedor_id) as conta_recebedor,");
		hql.append(" l.compra_id");
		hql.append(" from acao a join lancamento_acao la on a.id = la.acao_id ");
		hql.append(" join lancamento l on la.lancamento_id = l.id ");
		hql.append(" where l.tipo != 'compra' and l.tipo != 'lanc_av' ");
		hql.append(" and l.statuscompra = 'CONCLUIDO' ");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			hql.append(" and l.data_pagamento between '" + sdf.format(filtro.getDataInicio()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinal()) + "' ");

		} else if (filtro.getDataInicio() != null) {
			hql.append(" and l.data_pagamento >= '" + sdf.format(filtro.getDataInicio()) + "' ");

		} else if (filtro.getDataFinal() != null) {
			hql.append(" and l.data_pagamento >= '" + sdf.format(filtro.getDataFinal()) + "' ");
		}

		if (filtro.getDataInicioEmissao() != null && filtro.getDataFinalEmissao() != null) {
			hql.append(" and l.data_emissao between '" + sdf.format(filtro.getDataInicioEmissao()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");

		} else if (filtro.getDataInicioEmissao() != null) {
			hql.append(" and l.data_emissao >= '" + sdf.format(filtro.getDataInicioEmissao()) + "' ");

		} else if (filtro.getDataFinalEmissao() != null) {
			hql.append(" and l.data_emissao >= '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");
		}

		if (filtro.getAcaoId() != null) {
			hql.append(" and a.id = :acao");
		}

		if (filtro.getProjetoId() != null) {
			hql.append(" and a.projeto_id = :projeto");
		}

		if (filtro.getIdConta() != null) {
			hql.append(" and l.contarecebedor_id = :contaa");
		}

		if (filtro.getLancamentoID() != null) {
			hql.append(" and l.id = :lancamento");
		}

		hql.append(" order by l.data_pagamento desc, l.data_emissao desc");

		Query query = manager.createNativeQuery(hql.toString());

		if (filtro.getAcaoId() != null) {
			query.setParameter("acao", filtro.getAcaoId());
		}

		if (filtro.getProjetoId() != null) {
			query.setParameter("projeto", filtro.getProjetoId());
		}

		if (filtro.getIdConta() != null) {
			query.setParameter("contaa", filtro.getIdConta());
		}

		if (filtro.getLancamentoID() != null) {
			query.setParameter("lancamento", filtro.getLancamentoID());
		}

		List<LancamentoAuxiliar> retorno = new ArrayList<LancamentoAuxiliar>();
		List<Object[]> result = query.getResultList();

		LancamentoAuxiliar lAux = new LancamentoAuxiliar();

		for (Object[] object : result) {

			lAux = new LancamentoAuxiliar();
			lAux.setAcaoId(new Long(object[0].toString()));
			lAux.setId(new Long(object[1].toString()));
			lAux.setDescricao(object[2].toString());
			lAux.setFonte(object[3].toString());
			lAux.setCodigo(object[4].toString());
			lAux.setValorPagoAcao(object[5] != null ? new BigDecimal(object[5].toString()) : BigDecimal.ZERO);
			lAux.setValorLancamento(object[6] != null ? new BigDecimal(object[6].toString()) : BigDecimal.ZERO);
			lAux.setDataEmissao(DateConverter.converteDataSql(object[7].toString()));
			lAux.setDataPagamento(DateConverter.converteDataSql(object[8].toString()));
			lAux.setIdAcaoLancamentoId(new Long(object[9].toString()));
			lAux.setCodigoLancamento(object[10] != null ? object[10].toString() : "");
			lAux.setQuantidadeParcela(object[11] != null ? Integer.valueOf(object[11].toString()) : 1);
			lAux.setLabelFornecedor(object[12] != null ? object[12].toString() : "");
			lAux.setTipoParcelamento(TipoParcelamento.valueOf(object[13].toString()));
			lAux.setContaPagadorLbl(object[14] != null ? object[14].toString() : "");
			lAux.setContaRecebedorLbl(object[15] != null ? object[15].toString() : "");
			lAux.setCompraID(object[16] != null ? object[16].toString() : "");

			retorno.add(lAux);
		}

		return retorno;
	}

	public List<LancamentoAuxiliar> getRelacaoDespesasLinhaOrcamentaria(Filtro filtro) {

		StringBuilder hql = new StringBuilder("");
		hql.append("select l.id as id_lancamento_00, ");
		hql.append(
				"to_char(l.data_emissao,'DD-MM-YYYY') as data_emissao_01, to_char(pl.datapagamento,'DD-MM-YYYY') as data_pagamento_02,");
		hql.append("(select cb.nome_conta from conta_bancaria cb where  cb.id  = pl.conta_id) as conta_pagador_03,");
		hql.append(
				"(select cb.nome_conta from  conta_bancaria cb where cb.id = pl.contarecebedor_id) as conta_recebedor_04,");
		hql.append("la.status as status_05,");
		hql.append("l.descricao as descricao_06,");

		// hql.append("(select font.nome from fonte_pagadora font where id =
		// (select orc.fonte_id from orcamento orc where id = la.orcamento_id))
		// as fonte_07,");
		hql.append("CASE la.rubricaorcamento_id when null then (select font.nome from fonte_pagadora font where id = ");
		hql.append(
				"(select orc.fonte_id from  orcamento orc where id = (select rub.orcamento_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id))) else ");
		hql.append(
				"(select font.nome from fonte_pagadora font where id = (select orc.fonte_id from  orcamento orc where id = ");
		hql.append("(select rub.orcamento_id from rubrica_orcamento rub where rub.id = ");
		hql.append(
				"(select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))))end as fonte_07,");
		// hql.append("(select p.nome from projeto p where p.id = la.projeto_id)
		// as nome_projeto_08,");
		hql.append(
				"(select p.nome from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) as nome_projeto_08,");
		hql.append("pl.valor as valor_pago_by_acao_09, l.valor_total_com_desconto as valor_total_lancamento_10,");
		hql.append(
				"la.id as id_lancamento_acao_11, l.id as codigo_lancamento_12, l.quantidade_parcela as qtd_parcela_13,");
		hql.append("l.tipoparcelamento as tipo_parcelamento_14, l.compra_id  as compra_id_15, ");

		hql.append("CASE ");
		hql.append("WHEN (l.tipo = 'SolicitacaoPagamento' and l.tipolancamento != 'ad') THEN 'SP' ");
		hql.append("WHEN (l.tipo = 'SolicitacaoPagamento' and l.tipolancamento = 'ad') THEN 'SA' ");
		hql.append("WHEN (l.tipo = 'Diaria') THEN 'SD' ");
		hql.append("WHEN (l.tipo = 'custo_pessoal') THEN 'CP' ");
		hql.append("WHEN (l.tipo = 'pedido') THEN 'PC' ");
		hql.append("WHEN (l.tipo = 'baixa_aplicacao') THEN 'BA' ");
		hql.append("WHEN (l.tipo = 'aplicacao_recurso') THEN 'AR' ");
		hql.append("WHEN (l.tipo = 'doacao_efetiva') THEN 'DE' ");
		hql.append("WHEN (l.tipo = 'tarifa_bancaria') THEN 'TF' ");
		hql.append("WHEN (l.tipo = 'lanc_av' or l.tipo = 'lancamento_diversos') THEN 'LA'  ");
		hql.append(" END as tipo_16,");

		hql.append("l.reembolsado as reembolsado_17,");
		hql.append("l.idreembolso as id_reembolso_18,");
		hql.append("l.idfontereembolso as id_fonte_reembolsada_19,");
		hql.append("l.iddoacaoreembolso as id_doacao_reembolsada_20,");
		hql.append("l.depesareceita as dc_21,");
		// hql.append("CASE ");
		// hql.append("WHEN (l.depesareceita = 'DESPESA') THEN '-'");
		// hql.append("WHEN (l.depesareceita = 'RECEITA') THEN '+'");
		// hql.append("END as sinalizador_22,");

		// TODO TROCA DO SINALIZADOR
		hql.append("CASE  WHEN (pl.tipocontapagador = 'CB') ");
		
		
		
		hql.append(" THEN '-' ELSE '+'  END as sinalizador_22, ");

		hql.append("l.tipolancamento as tipo_lancamento_23,");
		hql.append("l.statusadiantamento as status_adiantamento_24,");
		hql.append(
				"(select ll.statusadiantamento from lancamento ll where ll.id = l.idadiantamento) status_ad_prestacao_25,");
		hql.append("l.idadiantamento as idadiantamento_26,");

		hql.append(
				" (select rubric.nome from  rubrica rubric where rubric.id = (select rub.rubrica_id from rubrica_orcamento rub where rub.id = ");
		hql.append(
				" (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) as requisito_27,");
		hql.append(" pl.stt as status_28,");
		hql.append(" l.tipo as tipo_29, ");
		hql.append(" (select comp.nome from  componente_class  comp where comp.id =  ");
		hql.append(
				" (select rub.componente_id from rubrica_orcamento rub where rub.id =  (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) ");
		hql.append(" as componente_30, ");
		hql.append(" (select sub.nome from  sub_componente  sub where sub.id =  ");
		hql.append(
				" (select rub.subcomponente_id from rubrica_orcamento rub where rub.id =  (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) ");
		hql.append(" as sub_componente_31, ");

		hql.append("(select orc.titulo from  orcamento orc where orc.id =");
		hql.append(
				"(select rub.orcamento_id from rubrica_orcamento rub where rub.id =  (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) ");
		hql.append("as doacao_32, ");

		hql.append("(select p.codigo from projeto p where p.id = ");
		hql.append(
				"(select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) as codigo_projeto_33, ");

		hql.append("l.numerodocumento as numero_documento_34,");

		hql.append("l.nota_fiscal as nota_fiscal_35, ");

		hql.append("(select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) as tipo_conta_pagador_36, ");
		hql.append(
				"(select cb.tipo from  conta_bancaria cb where cb.id = pl.contarecebedor_id) as tipo_conta_recebedor_37 ");

		hql.append(
				" from lancamento l join lancamento_acao la on l.id = la.lancamento_id join pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");
		hql.append(
				"where l.tipo != 'compra'   and l.statuscompra in ('CONCLUIDO','N_INCIADO') and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) ");

		hql.append(" and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb') ");
		hql.append(" and l.tipolancamento != 'reemb_conta' ");
		hql.append(" and l.tipo != 'baixa_aplicacao' ");
		hql.append(" and l.tipo != 'doacao_efetiva' ");
		hql.append(" and l.tipo != 'custo_pessoal' ");
		hql.append(" and l.tipo != 'aplicacao_recurso' ");
		
//		hql.append(" and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA'  ");
//		hql.append(" and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF')) ");
		
		hql.append(" and (false = (pl.tipocontapagador = 'CA' and pl.tipocontarecebedor = 'CF')) ");
		
//		hql.append(" and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF'  ");
//		hql.append(" and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF')) ");
		
		hql.append(" and (false = (pl.tipocontapagador = 'CF' and pl.tipocontarecebedor = 'CF')) ");
		
//		hql.append(" and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB' ");
//		hql.append(" and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB')) ");
		
		hql.append(" and (false = (pl.tipocontapagador = 'CB' and pl.tipocontarecebedor = 'CB')) ");
		
		
		
		
		if (!filtro.getTarifado()) {
			hql.append("and l.tipo != 'tarifa_bancaria' ");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (filtro.getDataInicio() != null) {
			hql.append(" and pl.datapagamento >= '" + sdf.format(filtro.getDataInicio()) + "' ");
		}
		
		
		if (filtro.getDataFinal() != null) {
			hql.append(" and pl.datapagamento <= '" + sdf.format(filtro.getDataFinal()) + "' ");

//			 hql.append("and (l.data_pagamento >= '" +
//			 sdf.format(filtro.getDataInicio()) + "' and l.data_pagamento <='" + sdf.format(filtro.getDataFinal()) + "') " );

		}

		// if (filtro.getProjetoId() != null) {
		// hql.append(
		// " and (select p.id from projeto p where p.id = (select pr.projeto_id from
		// projeto_rubrica pr where id = la.projetorubrica_id)) = :projeto");
		// }

		if (filtro.getRubricaID() != null) {
			hql.append("and la.projetorubrica_id = :rubrica");
		}

		if (filtro.getProjetoId() != null) {
			hql.append(
					" and (select p.id from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id))  = :projeto");
		}

		// if (filtro.getProjetoId() != null) {
		// hql.append(" and a.projeto_id = :projeto");
		// }

		if (filtro.getIdConta() != null) {
			hql.append(" and l.contarecebedor_id = :contaa");
		}

		if (filtro.getLancamentoID() != null) {
			hql.append(" and l.id = :lancamento");
		}

		if (filtro.getRubricas() != null && filtro.getRubricas().size() > 0) {
			hql.append(" and la.projetorubrica_id in (:rubrica_projeto) ");
		}

		hql.append(" order by pl.datapagamento asc, l.data_emissao desc");
		
		

		Query query = manager.createNativeQuery(hql.toString());

		if (filtro.getRubricaID() != null) {
			query.setParameter("rubrica", filtro.getRubricaID());
		}

		if (filtro.getProjetoId() != null) {
			query.setParameter("projeto", filtro.getProjetoId());
		}

		if (filtro.getIdConta() != null) {
			query.setParameter("contaa", filtro.getIdConta());
		}

		if (filtro.getLancamentoID() != null) {
			query.setParameter("lancamento", filtro.getLancamentoID());
		}

		if (filtro.getRubricas() != null && filtro.getRubricas().size() > 0) {

			List<Integer> mList = new ArrayList<>();
			for (ProjetoRubrica rubrica : filtro.getRubricas()) {
				mList.add(rubrica.getId().intValue());
			}

			query.setParameter("rubrica_projeto", mList);
		}

		List<LancamentoAuxiliar> retorno = new ArrayList<LancamentoAuxiliar>();
		List<Object[]> result = query.getResultList();

		LancamentoAuxiliar lAux = new LancamentoAuxiliar();

		BigDecimal totalSaida = BigDecimal.ZERO;
		BigDecimal totalEntrada = BigDecimal.ZERO;

		for (Object[] object : result) {

			lAux = new LancamentoAuxiliar();
			lAux.setIdAdiantamentoDePrestacao(object[26] != null ? new Long(object[26].toString()) : null);
			lAux.setStatusAdDePrestacao(object[25] != null ? object[25].toString() : "");
			lAux.setTipoLancamento(object[23] != null ? object[23].toString() : "");
			lAux.setStatusAd(object[24] != null ? object[24].toString() : "");
			lAux.setId(new Long(object[0].toString()));

			lAux.setDescricao(object[6].toString());
			lAux.setFonte(object[7].toString());
			lAux.setNomeProjeto(object[8] != null ? object[8].toString() : "");
			lAux.setValorLancamento(object[10] != null ? new BigDecimal(object[10].toString()) : BigDecimal.ZERO);
			lAux.setDataEmissao(DateConverter.converteDataSql(object[1].toString()));
			lAux.setDataPagamento(DateConverter.converteDataSql(object[2].toString()));
			lAux.setIdAcaoLancamentoId(new Long(object[11].toString()));
			lAux.setCodigoLancamento(object[0] != null ? object[0].toString() : "");
			lAux.setQuantidadeParcela(object[13] != null ? Integer.valueOf(object[13].toString()) : 1);
			lAux.setLabelFornecedor(object[4] != null ? object[4].toString() : "");
			lAux.setTipoParcelamento(TipoParcelamento.valueOf(object[14].toString()));
			lAux.setContaPagadorLbl(object[3] != null ? object[3].toString() : "");
			lAux.setContaRecebedorLbl(object[4] != null ? object[4].toString() : "");
			lAux.setCompraID(object[15] != null ? object[15].toString() : "");
			lAux.setStatusPagamento(object[5] != null ? Integer.valueOf(object[5].toString()) : 1);
			lAux.setSinalizador(object[22].toString());

			lAux.setRequisitoDeDoador(object[27] != null ? object[27].toString() : "");
			lAux.setStatus(object[28].toString());
			lAux.setTipo(object[16].toString());
			lAux.setComponente(object[30].toString());
			lAux.setSubComponente(object[31].toString());
			lAux.setDoacao(object[32] != null ? object[32].toString() : "");
			lAux.setCodigo(object[33] != null ? object[33].toString() : "");
			lAux.setNumeroDocumento(object[34] != null ? object[34].toString() : "");

			lAux.setTipoContaPagador(object[36] != null ? object[36].toString() : "");
			lAux.setTipoContaRecebedor(object[37] != null ? object[37].toString() : "");

			lAux.setValorPagoAcao(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
			if (lAux.getSinalizador().equals("-")) {
				totalSaida = totalSaida.add(lAux.getValorPagoAcao());
				lAux.setSaida(lAux.getValorPagoAcao());
				lAux.setEntrada(BigDecimal.ZERO);
			} else {
				totalEntrada = totalEntrada.add(lAux.getValorPagoAcao());
				lAux.setSaida(BigDecimal.ZERO);
				lAux.setEntrada(lAux.getValorPagoAcao());
			}
			retorno.add(lAux);

		}

		if (!retorno.isEmpty()) {
			retorno.get(0).setTotalSaida(totalSaida);
			retorno.get(0).setTotalEntrada(totalEntrada);
		}

		return retorno;

	}

	public List<LancamentoAuxiliar> getExtratoByLinhaOrcamentaria(Filtro filtro) {

		StringBuilder hql = new StringBuilder("");
		hql.append("select l.id as id_lancamento_00, ");
		hql.append(
				"to_char(l.data_emissao,'DD-MM-YYYY') as data_emissao_01, to_char(pl.datapagamento,'DD-MM-YYYY') as data_pagamento_02,");
		hql.append("(select cb.nome_conta from conta_bancaria cb where  cb.id  = pl.conta_id) as conta_pagador_03,");
		hql.append(
				"(select cb.nome_conta from  conta_bancaria cb where cb.id = pl.contarecebedor_id) as conta_recebedor_04,");
		hql.append("la.status as status_05,");
		hql.append("l.descricao as descricao_06,");

		// hql.append("(select font.nome from fonte_pagadora font where id =
		// (select orc.fonte_id from orcamento orc where id = la.orcamento_id))
		// as fonte_07,");
		hql.append("CASE la.rubricaorcamento_id when null then (select font.nome from fonte_pagadora font where id = ");
		hql.append(
				"(select orc.fonte_id from  orcamento orc where id = (select rub.orcamento_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id))) else ");
		hql.append(
				"(select font.nome from fonte_pagadora font where id = (select orc.fonte_id from  orcamento orc where id = ");
		hql.append("(select rub.orcamento_id from rubrica_orcamento rub where rub.id = ");
		hql.append(
				"(select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))))end as fonte_07,");
		// hql.append("(select p.nome from projeto p where p.id = la.projeto_id)
		// as nome_projeto_08,");
		hql.append(
				"(select p.nome from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) as nome_projeto_08,");
		hql.append("pl.valor as valor_pago_by_acao_09, l.valor_total_com_desconto as valor_total_lancamento_10,");
		hql.append(
				"la.id as id_lancamento_acao_11, l.id as codigo_lancamento_12, l.quantidade_parcela as qtd_parcela_13,");
		hql.append("l.tipoparcelamento as tipo_parcelamento_14, l.compra_id  as compra_id_15, l.tipo as tipo_16, ");
		hql.append("l.reembolsado as reembolsado_17,");
		hql.append("l.idreembolso as id_reembolso_18,");
		hql.append("l.idfontereembolso as id_fonte_reembolsada_19,");
		hql.append("l.iddoacaoreembolso as id_doacao_reembolsada_20,");
		hql.append("l.depesareceita as dc_21,");
		hql.append("CASE  ");
		hql.append("WHEN (l.depesareceita = 'DESPESA') THEN '-'");
		hql.append("WHEN (l.depesareceita = 'RECEITA') THEN '+'");
		hql.append("END as sinalizador_22,");
		hql.append("l.tipolancamento as tipo_lancamento_23,");
		hql.append("l.statusadiantamento as status_adiantamento_24,");
		hql.append(
				"(select ll.statusadiantamento from lancamento ll where ll.id = l.idadiantamento) status_ad_prestacao_25,");
		hql.append("l.idadiantamento as idadiantamento_26,");

		hql.append(
				" (select rubric.nome from  rubrica rubric where rubric.id = (select rub.rubrica_id from rubrica_orcamento rub where rub.id = ");
		hql.append(
				" (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) as requisito_27,");
		hql.append(" pl.stt as status_28,");
		hql.append(" l.tipo as tipo_29, ");
		hql.append(" (select comp.nome from  componente_class  comp where comp.id =  ");
		hql.append(
				" (select rub.componente_id from rubrica_orcamento rub where rub.id =  (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) ");
		hql.append(" as componente_30, ");
		hql.append(" (select sub.nome from  sub_componente  sub where sub.id =  ");
		hql.append(
				" (select rub.subcomponente_id from rubrica_orcamento rub where rub.id =  (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) ");
		hql.append(" as sub_componente_31, ");

		hql.append("(select orc.titulo from  orcamento orc where orc.id =");
		hql.append(
				"(select rub.orcamento_id from rubrica_orcamento rub where rub.id =  (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) ");
		hql.append("as doacao_32, ");

		hql.append("(select p.codigo from projeto p where p.id = ");
		hql.append(
				"(select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) as codigo_projeto_33, ");

		hql.append("l.numerodocumento as numero_documento_34,");

		hql.append("l.nota_fiscal as nota_fiscal_35, ");

		hql.append("(select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) as tipo_conta_pagador_36, ");
		hql.append(
				"(select cb.tipo from  conta_bancaria cb where cb.id = pl.contarecebedor_id) as tipo_conta_recebedor_37 ");

		hql.append(
				" from lancamento l join lancamento_acao la on l.id = la.lancamento_id join pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");
		hql.append(
				"where l.tipo != 'compra'   and l.statuscompra in ('CONCLUIDO','N_INCIADO') and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) ");
		// hql.append("and l.tipo != 'lanc_av' ");
		// hql.append(" and (la.orcamento_id = :id_doacao or l.iddoacaoreembolso
		// = :id_doacao) ");
		// hql.append("and la.projeto_id = "+filtro.getProjetoId()+ " ");

		// hql.append(" and l.id = 44746");

		// hql.append(" and pl.stt != 'PROVISIONADO' ");

		if (!filtro.getTarifado()) {
			hql.append("and l.tipo != 'tarifa_bancaria' ");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (filtro.getDataFinal() != null) {
			hql.append(" and to_char(pl.datapagamento, 'yyyy-mm-dd') <= '" + sdf.format(filtro.getDataFinal()) + "' ");

			//
			// hql.append("and (l.data_pagamento >= '" +
			// sdf.format(filtro.getDataInicio()) + "' and l.data_pagamento <=
			// '" + sdf.format(filtro.getDataFinal()) + "') " );

		}
		// else if (filtro.getDataInicio() != null) {
		// hql.append(" and to_char(l.data_pagamento,'yyyy-mm-dd') >= '" +
		// sdf.format(filtro.getDataInicio()) + "' ");
		//
		// } else if (filtro.getDataFinal() != null) {
		// hql.append(
		// " and and to_char(l.data_pagamento,'yyyy-mm-dd') <= '" +
		// sdf.format(filtro.getDataFinal()) + "' ");
		// }

		// if (filtro.getDataInicioEmissao() != null && filtro.getDataFinalEmissao() !=
		// null) {
		// hql.append(" and and to_char(l.data_emissao,'yyyy-mm-dd') between '"
		// + sdf.format(filtro.getDataInicioEmissao()) + "' and ");
		// hql.append(" '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");
		//
		// } else if (filtro.getDataInicioEmissao() != null) {
		// hql.append(" and to_char(l.data_emissao,'yyyy-mm-dd') >= '" +
		// sdf.format(filtro.getDataInicioEmissao())
		// + "' ");
		//
		// } else if (filtro.getDataFinalEmissao() != null) {
		// hql.append(" and to_char(l.data_emissao,'yyyy-mm-dd') >= '" +
		// sdf.format(filtro.getDataFinalEmissao())
		// + "' ");
		// }

		// if (filtro.getProjetoId() != null) {
		// hql.append(
		// " and (select p.id from projeto p where p.id = (select pr.projeto_id from
		// projeto_rubrica pr where id = la.projetorubrica_id)) = :projeto");
		// }

		if (filtro.getRubricaID() != null) {
			hql.append("and la.projetorubrica_id = :rubrica");
		}

		// if (filtro.getProjetoId() != null) {
		// hql.append(" and a.projeto_id = :projeto");
		// }

		if (filtro.getIdConta() != null) {
			hql.append(" and l.contarecebedor_id = :contaa");
		}

		if (filtro.getLancamentoID() != null) {
			hql.append(" and l.id = :lancamento");
		}

		hql.append(" order by pl.datapagamento asc, l.data_emissao desc");

		Query query = manager.createNativeQuery(hql.toString());

		// query.setParameter("id_doacao", filtro.getIdDoacao());
		//
		// if (filtro.getProjetoId() != null) {
		// query.setParameter("projeto", filtro.getProjetoId());
		// }

		if (filtro.getRubricaID() != null) {
			query.setParameter("rubrica", filtro.getRubricaID());
		}

		if (filtro.getIdConta() != null) {
			query.setParameter("contaa", filtro.getIdConta());
		}

		if (filtro.getLancamentoID() != null) {
			query.setParameter("lancamento", filtro.getLancamentoID());
		}

		List<LancamentoAuxiliar> retorno = new ArrayList<LancamentoAuxiliar>();
		List<Object[]> result = query.getResultList();

		LancamentoAuxiliar lAux = new LancamentoAuxiliar();

		BigDecimal totalSaida = BigDecimal.ZERO;
		BigDecimal totalEntrada = BigDecimal.ZERO;

		for (Object[] object : result) {

			
			lAux = new LancamentoAuxiliar();
			lAux.setIdAdiantamentoDePrestacao(object[26] != null ? new Long(object[26].toString()) : null);
			lAux.setStatusAdDePrestacao(object[25] != null ? object[25].toString() : "");
			lAux.setTipoLancamento(object[23] != null ? object[23].toString() : "");
			lAux.setStatusAd(object[24] != null ? object[24].toString() : "");
			lAux.setId(new Long(object[0].toString()));
			lAux.setDescricao(object[6].toString());
			lAux.setFonte(object[7].toString());
			lAux.setNomeProjeto(object[8] != null ? object[8].toString() : "");
			lAux.setValorLancamento(object[10] != null ? new BigDecimal(object[10].toString()) : BigDecimal.ZERO);
			lAux.setDataEmissao(DateConverter.converteDataSql(object[1].toString()));
			lAux.setDataPagamento(DateConverter.converteDataSql(object[2].toString()));
			lAux.setIdAcaoLancamentoId(new Long(object[11].toString()));
			lAux.setCodigoLancamento(object[0] != null ? object[0].toString() : "");
			lAux.setQuantidadeParcela(object[13] != null ? Integer.valueOf(object[13].toString()) : 1);
			lAux.setLabelFornecedor(object[4] != null ? object[4].toString() : "");
			lAux.setTipoParcelamento(TipoParcelamento.valueOf(object[14].toString()));
			lAux.setContaPagadorLbl(object[3] != null ? object[3].toString() : "");
			lAux.setContaRecebedorLbl(object[4] != null ? object[4].toString() : "");
			lAux.setCompraID(object[14] != null ? object[14].toString() : "");
			lAux.setStatusPagamento(object[5] != null ? Integer.valueOf(object[5].toString()) : 1);
			lAux.setSinalizador(object[22].toString());

			lAux.setRequisitoDeDoador(object[27] != null ? object[27].toString() : "");
			lAux.setStatus(object[28].toString());
			lAux.setTipo(object[29].toString());
			lAux.setComponente(object[30].toString());
			lAux.setSubComponente(object[31].toString());
			lAux.setDoacao(object[32] != null ? object[32].toString() : "");
			lAux.setCodigo(object[33] != null ? object[33].toString() : "");
			lAux.setNumeroDocumento(object[34] != null ? object[34].toString() : "");

			lAux.setTipoContaPagador(object[36] != null ? object[36].toString() : "");
			lAux.setTipoContaRecebedor(object[37] != null ? object[37].toString() : "");
			// hql.append(" (select pr.rubricaorcamento_id from projeto_rubrica pr where
			// pr.id = la.projetorubrica_id))) as requisito_27,");
			// hql.append(" pl.stt as status_28,");
			// hql.append(" l.tipo as tipo_29 ");

			// Verifica reembolso
			if (lAux.getTipoLancamento().equals("reemb_conta")) {
				lAux.setValorNulo(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
				lAux.setSaida(BigDecimal.ZERO);
				lAux.setEntrada(BigDecimal.ZERO);
				// retorno.add(lAux);
				continue;
			} else {
				if (lAux.getTipoContaPagador().equals("CF") && lAux.getTipoContaRecebedor().equals("CB")) {
					lAux.setSinalizador("+");
				}
			}

			// Verifica se é adiantamento e se foi validado
			if (lAux.getTipoLancamento().equals("ad")) {
				if (lAux.getStatusAd().equals("VALIDADO")) {
					lAux.setValorNulo(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
					lAux.setSaida(BigDecimal.ZERO);
					lAux.setEntrada(BigDecimal.ZERO);
					// retorno.add(lAux);
					continue;
				} else {
					lAux.setValorPagoAcao(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
					if (lAux.getSinalizador().equals("-")) {
						totalSaida = totalSaida.add(lAux.getValorPagoAcao());
						lAux.setSaida(lAux.getValorPagoAcao());
						lAux.setEntrada(BigDecimal.ZERO);
					} else {
						totalEntrada = totalEntrada.add(lAux.getValorPagoAcao());
						lAux.setSaida(BigDecimal.ZERO);
						lAux.setEntrada(lAux.getValorPagoAcao());
					}
					retorno.add(lAux);
					continue;
				}
			}

			// Verifica se é um prestação de contas e se o adiantamento da prestação foi
			// validado
			if (lAux.getIdAdiantamentoDePrestacao() != null) {
				if (lAux.getStatusAdDePrestacao().equals("VALIDADO")) {
					if (lAux.getTipoLancamento().equals("dev") || lAux.getTipoLancamento().equals("reenb")
							|| lAux.getTipoLancamento().equals("reemb_conta")) {
						lAux.setValorNulo(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
						lAux.setSaida(BigDecimal.ZERO);
						lAux.setEntrada(BigDecimal.ZERO);
						// retorno.add(lAux);
						continue;
					} else {
						lAux.setValorPagoAcao(
								object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);

						if (lAux.getSinalizador().equals("-")) {
							totalSaida = totalSaida.add(lAux.getValorPagoAcao());
							lAux.setSaida(lAux.getValorPagoAcao());
							lAux.setEntrada(BigDecimal.ZERO);
						} else {
							totalEntrada = totalEntrada.add(lAux.getValorPagoAcao());
							lAux.setSaida(BigDecimal.ZERO);
							lAux.setEntrada(lAux.getValorPagoAcao());
						}

						retorno.add(lAux);
						continue;
					}

				} else {
					lAux.setValorNulo(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
					lAux.setSaida(BigDecimal.ZERO);
					lAux.setEntrada(BigDecimal.ZERO);
					// retorno.add(lAux);
					continue;
				}

			} else {
				lAux.setValorPagoAcao(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
				if (lAux.getSinalizador().equals("-")) {
					totalSaida = totalSaida.add(lAux.getValorPagoAcao());
					lAux.setSaida(lAux.getValorPagoAcao());
					lAux.setEntrada(BigDecimal.ZERO);
				} else {
					totalEntrada = totalEntrada.add(lAux.getValorPagoAcao());
					lAux.setSaida(BigDecimal.ZERO);
					lAux.setEntrada(lAux.getValorPagoAcao());
				}
				retorno.add(lAux);
			}

		}

		if (!retorno.isEmpty()) {
			retorno.get(0).setTotalSaida(totalSaida);
			retorno.get(0).setTotalEntrada(totalEntrada);
		}

		return retorno;

	}

	public List<LancamentoAuxiliar> getExtratoCtrlProjetoOrcamentoExecutado(Filtro filtro) {

		// hql
		StringBuilder hql = new StringBuilder("");
		hql.append("select l.id as id_lancamento_00, ");
		hql.append(
				"to_char(l.data_emissao,'DD-MM-YYYY') as data_emissao_01, to_char(pl.datapagamento,'DD-MM-YYYY') as data_pagamento_02,");
		hql.append("(select cb.nome_conta from conta_bancaria cb where  cb.id  = pl.conta_id) as conta_pagador_03,");
		hql.append(
				"(select cb.nome_conta from  conta_bancaria cb where cb.id = pl.contarecebedor_id) as conta_recebedor_04,");
		hql.append("la.status as status_05,");
		hql.append("l.descricao as descricao_06,");

		// hql.append("(select font.nome from fonte_pagadora font where id =
		// (select orc.fonte_id from orcamento orc where id = la.orcamento_id))
		// as fonte_07,");
		hql.append("CASE la.rubricaorcamento_id when null then (select font.nome from fonte_pagadora font where id = ");
		hql.append(
				"(select orc.fonte_id from  orcamento orc where id = (select rub.orcamento_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id))) else ");
		hql.append(
				"(select font.nome from fonte_pagadora font where id = (select orc.fonte_id from  orcamento orc where id = ");
		hql.append("(select rub.orcamento_id from rubrica_orcamento rub where rub.id = ");
		hql.append(
				"(select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))))end as fonte_07,");
		// hql.append("(select p.nome from projeto p where p.id = la.projeto_id)
		// as nome_projeto_08,");
		hql.append(
				"(select p.nome from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) as nome_projeto_08,");
		hql.append("pl.valor as valor_pago_by_acao_09, l.valor_total_com_desconto as valor_total_lancamento_10,");
		hql.append(
				"la.id as id_lancamento_acao_11, l.id as codigo_lancamento_12, l.quantidade_parcela as qtd_parcela_13,");
		hql.append("l.tipoparcelamento as tipo_parcelamento_14, l.compra_id  as compra_id_15,  ");
		hql.append("CASE ");
		hql.append("WHEN (l.tipo = 'SolicitacaoPagamento' and l.tipolancamento != 'ad') THEN 'SP' ");
		hql.append("WHEN (l.tipo = 'SolicitacaoPagamento' and l.tipolancamento = 'ad') THEN 'SA' ");
		hql.append("WHEN (l.tipo = 'Diaria') THEN 'SD' ");
		hql.append("WHEN (l.tipo = 'custo_pessoal') THEN 'CP' ");
		hql.append("WHEN (l.tipo = 'pedido') THEN 'PC' ");
		hql.append("WHEN (l.tipo = 'baixa_aplicacao') THEN 'BA' ");
		hql.append("WHEN (l.tipo = 'aplicacao_recurso') THEN 'AR' ");
		hql.append("WHEN (l.tipo = 'doacao_efetiva') THEN 'DE' ");
		hql.append("WHEN (l.tipo = 'tarifa_bancaria') THEN 'TF' ");
		hql.append("WHEN (l.tipo = 'lanc_av' or l.tipo = 'lancamento_diversos') THEN 'LA'  ");
		hql.append(" END as tipo_16,");

		hql.append("l.reembolsado as reembolsado_17,");
		hql.append("l.idreembolso as id_reembolso_18,");
		hql.append("l.idfontereembolso as id_fonte_reembolsada_19,");
		hql.append("l.iddoacaoreembolso as id_doacao_reembolsada_20,");
		hql.append("l.depesareceita as dc_21,");
		hql.append("CASE  ");
		hql.append("WHEN (l.depesareceita = 'DESPESA') THEN '-'");
		hql.append("WHEN (l.depesareceita = 'RECEITA') THEN '+'");
		hql.append("END as sinalizador_22,");
		hql.append("l.tipolancamento as tipo_lancamento_23,");
		hql.append("l.statusadiantamento as status_adiantamento_24,");
		hql.append(
				"(select ll.statusadiantamento from lancamento ll where ll.id = l.idadiantamento) status_ad_prestacao_25,");
		hql.append("l.idadiantamento as idadiantamento_26,");

		hql.append(
				" (select rubric.nome from  rubrica rubric where rubric.id = (select rub.rubrica_id from rubrica_orcamento rub where rub.id = ");
		hql.append(
				" (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) as requisito_27,");
		hql.append(" pl.stt as status_28,");
		hql.append(" l.tipo as tipo_29, ");
		hql.append(" (select comp.nome from  componente_class  comp where comp.id =  ");
		hql.append(
				" (select rub.componente_id from rubrica_orcamento rub where rub.id =  (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) ");
		hql.append(" as componente_30, ");
		hql.append(" (select sub.nome from  sub_componente  sub where sub.id =  ");
		hql.append(
				" (select rub.subcomponente_id from rubrica_orcamento rub where rub.id =  (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) ");
		hql.append(" as sub_componente_31, ");

		hql.append("(select orc.titulo from  orcamento orc where orc.id =");
		hql.append(
				"(select rub.orcamento_id from rubrica_orcamento rub where rub.id =  (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) ");
		hql.append("as doacao_32, ");

		hql.append("(select p.codigo from projeto p where p.id = ");
		hql.append(
				"(select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) as codigo_projeto_33, ");

		hql.append("l.numerodocumento as numero_documento_34, ");
		hql.append("l.nota_fiscal as nota_fiscal_35, ");
		hql.append("(select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) as tipo_conta_pagador_36, ");
		hql.append(
				"(select cb.tipo from  conta_bancaria cb where cb.id = pl.contarecebedor_id) as tipo_conta_recebedor_37 ");

		hql.append(
				" from lancamento l join lancamento_acao la on l.id = la.lancamento_id join pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");
		hql.append(
				"where l.tipo != 'compra'   and l.statuscompra in ('CONCLUIDO','N_INCIADO') and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) ");
		// hql.append("and l.tipo != 'lanc_av' ");
		// hql.append(" and (la.orcamento_id = :id_doacao or l.iddoacaoreembolso
		// = :id_doacao) ");
		// hql.append("and la.projeto_id = "+filtro.getProjetoId()+ " ");

		// hql.append(" and l.id = 42344 ");

		// hql.append(" and pl.stt != 'PROVISIONADO' ");

		// *****
		if (filtro.getRubricas() != null && filtro.getRubricas().size() > 0) {
			hql.append(" and la.projetorubrica_id in (:rubrica_projeto) ");
		}

		// ****

		if (!filtro.getTarifado()) {
			hql.append(" and l.tipo != 'tarifa_bancaria' ");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		//
		if (filtro.getDataFinal() != null) {
			hql.append(" and to_char(pl.datapagamento, 'yyyy-mm-dd') <= '" + sdf.format(filtro.getDataFinal()) + "' ");

			//
			// hql.append("and (l.data_pagamento >= '" +
			// sdf.format(filtro.getDataInicio()) + "' and l.data_pagamento <=
			// '" + sdf.format(filtro.getDataFinal()) + "') " );

		}
		// else if (filtro.getDataInicio() != null) {
		// hql.append(" and to_char(l.data_pagamento,'yyyy-mm-dd') >= '" +
		// sdf.format(filtro.getDataInicio()) + "' ");
		//
		// } else if (filtro.getDataFinal() != null) {
		// hql.append(
		// " and and to_char(l.data_pagamento,'yyyy-mm-dd') <= '" +
		// sdf.format(filtro.getDataFinal()) + "' ");
		// }
		//
		// if (filtro.getDataInicioEmissao() != null && filtro.getDataFinalEmissao() !=
		// null) {
		// hql.append(" and and to_char(l.data_emissao,'yyyy-mm-dd') between '"
		// + sdf.format(filtro.getDataInicioEmissao()) + "' and ");
		// hql.append(" '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");
		//
		// } else if (filtro.getDataInicioEmissao() != null) {
		// hql.append(" and to_char(l.data_emissao,'yyyy-mm-dd') >= '" +
		// sdf.format(filtro.getDataInicioEmissao())
		// + "' ");
		//
		// } else if (filtro.getDataFinalEmissao() != null) {
		// hql.append(" and to_char(l.data_emissao,'yyyy-mm-dd') >= '" +
		// sdf.format(filtro.getDataFinalEmissao())
		// + "' ");
		// }

		if (filtro.getProjetoId() != null) {
			hql.append(
					" and (select p.id from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id))  = :projeto");
		}

		// if (filtro.getProjetoId() != null) {
		// hql.append(" and a.projeto_id = :projeto");
		// }

		if (filtro.getIdConta() != null) {
			hql.append(" and l.contarecebedor_id = :contaa");
		}

		if (filtro.getLancamentoID() != null) {
			hql.append(" and l.id = :lancamento");
		}

		hql.append(" order by pl.datapagamento asc, l.data_emissao desc");

		Query query = manager.createNativeQuery(hql.toString());

		if (filtro.getRubricas() != null && filtro.getRubricas().size() > 0) {

			List<Integer> mList = new ArrayList<>();
			for (ProjetoRubrica rubrica : filtro.getRubricas()) {
				mList.add(rubrica.getId().intValue());
			}

			query.setParameter("rubrica_projeto", mList);
		}

		// query.setParameter("id_doacao", filtro.getIdDoacao());
		//
		if (filtro.getProjetoId() != null) {
			query.setParameter("projeto", filtro.getProjetoId());
		}

		if (filtro.getIdConta() != null) {
			query.setParameter("contaa", filtro.getIdConta());
		}

		if (filtro.getLancamentoID() != null) {
			query.setParameter("lancamento", filtro.getLancamentoID());
		}

		List<LancamentoAuxiliar> retorno = new ArrayList<LancamentoAuxiliar>();
		List<Object[]> result = query.getResultList();

		LancamentoAuxiliar lAux = new LancamentoAuxiliar();

		BigDecimal totalSaida = BigDecimal.ZERO;
		BigDecimal totalEntrada = BigDecimal.ZERO;

		for (Object[] object : result) {


			lAux = new LancamentoAuxiliar();
			lAux.setIdAdiantamentoDePrestacao(object[26] != null ? new Long(object[26].toString()) : null);
			lAux.setStatusAdDePrestacao(object[25] != null ? object[25].toString() : "");
			lAux.setTipoLancamento(object[23] != null ? object[23].toString() : "");
			lAux.setStatusAd(object[24] != null ? object[24].toString() : "");

			// lAux.setAcaoId(new Long(object[0].toString()));
			lAux.setId(new Long(object[0].toString()));

			lAux.setDescricao(object[6].toString());
			lAux.setFonte(object[7].toString());
			lAux.setNomeProjeto(object[8] != null ? object[8].toString() : "");
			lAux.setValorLancamento(object[10] != null ? new BigDecimal(object[10].toString()) : BigDecimal.ZERO);
			lAux.setDataEmissao(DateConverter.converteDataSql(object[1].toString()));
			lAux.setDataPagamento(DateConverter.converteDataSql(object[2].toString()));
			lAux.setIdAcaoLancamentoId(new Long(object[11].toString()));
			lAux.setCodigoLancamento(object[0] != null ? object[0].toString() : "");
			lAux.setQuantidadeParcela(object[13] != null ? Integer.valueOf(object[13].toString()) : 1);
			lAux.setLabelFornecedor(object[4] != null ? object[4].toString() : "");
			lAux.setTipoParcelamento(TipoParcelamento.valueOf(object[14].toString()));
			lAux.setContaPagadorLbl(object[3] != null ? object[3].toString() : "");
			lAux.setContaRecebedorLbl(object[4] != null ? object[4].toString() : "");
			lAux.setCompraID(object[14] != null ? object[14].toString() : "");
			lAux.setStatusPagamento(object[5] != null ? Integer.valueOf(object[5].toString()) : 1);
			lAux.setSinalizador(object[22].toString());

			lAux.setRequisitoDeDoador(object[27] != null ? object[27].toString() : "");
			lAux.setStatus(object[28].toString());
			lAux.setTipo(object[16] != null ? object[16].toString() : "");
			lAux.setComponente(object[30].toString());
			lAux.setSubComponente(object[31].toString());
			lAux.setDoacao(object[32] != null ? object[32].toString() : "");
			lAux.setCodigo(object[33] != null ? object[33].toString() : "");
			lAux.setNumeroDocumento(object[34] != null ? object[34].toString() : "");
			lAux.setNotaFiscal(object[35] != null ? object[35].toString() : "");
			lAux.setTipoContaPagador(object[36] != null ? object[36].toString() : "");
			lAux.setTipoContaRecebedor(object[37] != null ? object[37].toString() : "");
			// hql.append(" (select pr.rubricaorcamento_id from projeto_rubrica pr where
			// pr.id = la.projetorubrica_id))) as requisito_27,");
			// hql.append(" pl.stt as status_28,");
			// hql.append(" l.tipo as tipo_29 ");

			if (lAux.getTipoLancamento().equals("reemb_conta")) {
				lAux.setValorNulo(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
				lAux.setSaida(BigDecimal.ZERO);
				lAux.setEntrada(BigDecimal.ZERO);
				// retorno.add(lAux);
				continue;
			} else {
				if (lAux.getTipoContaPagador().equals("CF") && lAux.getTipoContaRecebedor().equals("CB")) {
					lAux.setSinalizador("+");
				}
			}

			if (lAux.getTipoLancamento().equals("ad")) {
				if (lAux.getStatusAd().equals("VALIDADO")) {
					lAux.setValorNulo(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
					lAux.setSaida(BigDecimal.ZERO);
					lAux.setEntrada(BigDecimal.ZERO);
					// retorno.add(lAux);
					continue;
				} else {
					lAux.setValorPagoAcao(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
					if (lAux.getSinalizador().equals("-")) {
						totalSaida = totalSaida.add(lAux.getValorPagoAcao());
						lAux.setSaida(lAux.getValorPagoAcao());
						lAux.setEntrada(BigDecimal.ZERO);
					} else {
						totalEntrada = totalEntrada.add(lAux.getValorPagoAcao());
						lAux.setSaida(BigDecimal.ZERO);
						lAux.setEntrada(lAux.getValorPagoAcao());
					}
					retorno.add(lAux);
					continue;
				}
			}

			if (lAux.getIdAdiantamentoDePrestacao() != null) {
				if (lAux.getStatusAdDePrestacao().equals("VALIDADO")) {
					if (lAux.getTipoLancamento().equals("dev") || lAux.getTipoLancamento().equals("reenb")
							|| lAux.getTipoLancamento().equals("reemb_conta")) {
						lAux.setValorNulo(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
						lAux.setSaida(BigDecimal.ZERO);
						lAux.setEntrada(BigDecimal.ZERO);
						// retorno.add(lAux);
						continue;
					} else {
						lAux.setValorPagoAcao(
								object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);

						if (lAux.getSinalizador().equals("-")) {
							totalSaida = totalSaida.add(lAux.getValorPagoAcao());
							lAux.setSaida(lAux.getValorPagoAcao());
							lAux.setEntrada(BigDecimal.ZERO);
						} else {
							totalEntrada = totalEntrada.add(lAux.getValorPagoAcao());
							lAux.setSaida(BigDecimal.ZERO);
							lAux.setEntrada(lAux.getValorPagoAcao());
						}

						retorno.add(lAux);
						continue;
					}

				} else {
					lAux.setValorNulo(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
					lAux.setSaida(BigDecimal.ZERO);
					lAux.setEntrada(BigDecimal.ZERO);
					// retorno.add(lAux);
					continue;
				}

			} else {
				lAux.setValorPagoAcao(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
				if (lAux.getSinalizador().equals("-")) {
					totalSaida = totalSaida.add(lAux.getValorPagoAcao());
					lAux.setSaida(lAux.getValorPagoAcao());
					lAux.setEntrada(BigDecimal.ZERO);
				} else {
					totalEntrada = totalEntrada.add(lAux.getValorPagoAcao());
					lAux.setSaida(BigDecimal.ZERO);
					lAux.setEntrada(lAux.getValorPagoAcao());
				}
				retorno.add(lAux);
			}

		}

		if (!retorno.isEmpty()) {
			retorno.get(0).setTotalSaida(totalSaida);
			retorno.get(0).setTotalEntrada(totalEntrada);
		}

		return retorno;

	}

	public List<LancamentoAuxiliar> getExtratoCtrlProjeto(Filtro filtro) {

		// hql
		StringBuilder hql = new StringBuilder("");
		hql.append("select l.id as id_lancamento_00, ");
		hql.append(
				"to_char(l.data_emissao,'DD-MM-YYYY') as data_emissao_01, to_char(pl.datapagamento,'DD-MM-YYYY') as data_pagamento_02,");
		hql.append("(select cb.nome_conta from conta_bancaria cb where  cb.id  = pl.conta_id) as conta_pagador_03,");
		hql.append(
				"(select cb.nome_conta from  conta_bancaria cb where cb.id = pl.contarecebedor_id) as conta_recebedor_04,");
		hql.append("la.status as status_05,");
		hql.append("l.descricao as descricao_06,");

		// hql.append("(select font.nome from fonte_pagadora font where id =
		// (select orc.fonte_id from orcamento orc where id = la.orcamento_id))
		// as fonte_07,");
		hql.append("CASE la.rubricaorcamento_id when null then (select font.nome from fonte_pagadora font where id = ");
		hql.append(
				"(select orc.fonte_id from  orcamento orc where id = (select rub.orcamento_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id))) else ");
		hql.append(
				"(select font.nome from fonte_pagadora font where id = (select orc.fonte_id from  orcamento orc where id = ");
		hql.append("(select rub.orcamento_id from rubrica_orcamento rub where rub.id = ");
		hql.append(
				"(select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))))end as fonte_07,");
		// hql.append("(select p.nome from projeto p where p.id = la.projeto_id)
		// as nome_projeto_08,");
		hql.append(
				"(select p.nome from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) as nome_projeto_08,");
		hql.append("pl.valor as valor_pago_by_acao_09, l.valor_total_com_desconto as valor_total_lancamento_10,");
		hql.append(
				"la.id as id_lancamento_acao_11, l.id as codigo_lancamento_12, l.quantidade_parcela as qtd_parcela_13,");
		hql.append("l.tipoparcelamento as tipo_parcelamento_14, l.compra_id  as compra_id_15, l.tipo as tipo_16, ");
		hql.append("l.reembolsado as reembolsado_17,");
		hql.append("l.idreembolso as id_reembolso_18,");
		hql.append("l.idfontereembolso as id_fonte_reembolsada_19,");
		hql.append("l.iddoacaoreembolso as id_doacao_reembolsada_20,");
		hql.append("l.depesareceita as dc_21,");
		hql.append("CASE  ");
		hql.append("WHEN (l.depesareceita = 'DESPESA') THEN '-'");
		hql.append("WHEN (l.depesareceita = 'RECEITA') THEN '+'");
		hql.append("END as sinalizador_22,");
		hql.append("l.tipolancamento as tipo_lancamento_23,");
		hql.append("l.statusadiantamento as status_adiantamento_24,");
		hql.append(
				"(select ll.statusadiantamento from lancamento ll where ll.id = l.idadiantamento) status_ad_prestacao_25,");
		hql.append("l.idadiantamento as idadiantamento_26");

		hql.append(
				" from lancamento l join lancamento_acao la on l.id = la.lancamento_id join pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");
		hql.append(
				"where l.tipo != 'compra'   and l.statuscompra = 'CONCLUIDO' and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) ");
		// hql.append("and l.tipo != 'lanc_av' ");
		// hql.append(" and (la.orcamento_id = :id_doacao or l.iddoacaoreembolso
		// = :id_doacao) ");
		// hql.append("and la.projeto_id = "+filtro.getProjetoId()+ " ");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			hql.append(" and to_char(l.data_pagamento, 'yyyy-mm-dd') between '" + sdf.format(filtro.getDataInicio())
					+ "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinal()) + "' ");
			//
			// hql.append("and (l.data_pagamento >= '" +
			// sdf.format(filtro.getDataInicio()) + "' and l.data_pagamento <=
			// '" + sdf.format(filtro.getDataFinal()) + "') " );

		} else if (filtro.getDataInicio() != null) {
			hql.append(" and to_char(l.data_pagamento,'yyyy-mm-dd') >= '" + sdf.format(filtro.getDataInicio()) + "' ");

		} else if (filtro.getDataFinal() != null) {
			hql.append(
					" and and to_char(l.data_pagamento,'yyyy-mm-dd') <= '" + sdf.format(filtro.getDataFinal()) + "' ");
		}

		if (filtro.getDataInicioEmissao() != null && filtro.getDataFinalEmissao() != null) {
			hql.append(" and and to_char(l.data_emissao,'yyyy-mm-dd')  between '"
					+ sdf.format(filtro.getDataInicioEmissao()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");

		} else if (filtro.getDataInicioEmissao() != null) {
			hql.append(" and to_char(l.data_emissao,'yyyy-mm-dd') >= '" + sdf.format(filtro.getDataInicioEmissao())
					+ "' ");

		} else if (filtro.getDataFinalEmissao() != null) {
			hql.append(" and to_char(l.data_emissao,'yyyy-mm-dd')  >= '" + sdf.format(filtro.getDataFinalEmissao())
					+ "' ");
		}

		if (filtro.getProjetoId() != null) {
			hql.append(
					" and (select p.id from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id))  = :projeto");
		}

		// if (filtro.getProjetoId() != null) {
		// hql.append(" and a.projeto_id = :projeto");
		// }

		if (filtro.getIdConta() != null) {
			hql.append(" and l.contarecebedor_id = :contaa");
		}

		if (filtro.getLancamentoID() != null) {
			hql.append(" and l.id = :lancamento");
		}

		hql.append(" order by l.data_pagamento desc, l.data_emissao desc");

		Query query = manager.createNativeQuery(hql.toString());

		// query.setParameter("id_doacao", filtro.getIdDoacao());
		//
		if (filtro.getProjetoId() != null) {
			query.setParameter("projeto", filtro.getProjetoId());
		}

		if (filtro.getIdConta() != null) {
			query.setParameter("contaa", filtro.getIdConta());
		}

		if (filtro.getLancamentoID() != null) {
			query.setParameter("lancamento", filtro.getLancamentoID());
		}

		List<LancamentoAuxiliar> retorno = new ArrayList<LancamentoAuxiliar>();
		List<Object[]> result = query.getResultList();

		LancamentoAuxiliar lAux = new LancamentoAuxiliar();

		BigDecimal totalSaida = BigDecimal.ZERO;
		BigDecimal totalEntrada = BigDecimal.ZERO;

		for (Object[] object : result) {

			lAux = new LancamentoAuxiliar();
			lAux.setIdAdiantamentoDePrestacao(object[26] != null ? new Long(object[26].toString()) : null);
			lAux.setStatusAdDePrestacao(object[25] != null ? object[25].toString() : "");
			lAux.setTipoLancamento(object[23] != null ? object[23].toString() : "");
			lAux.setStatusAd(object[24] != null ? object[24].toString() : "");

			// lAux.setAcaoId(new Long(object[0].toString()));
			lAux.setId(new Long(object[0].toString()));
			lAux.setDescricao(object[6].toString());
			lAux.setFonte(object[7].toString());
			lAux.setNomeProjeto(object[8] != null ? object[8].toString() : "");
			lAux.setValorLancamento(object[10] != null ? new BigDecimal(object[10].toString()) : BigDecimal.ZERO);
			lAux.setDataEmissao(DateConverter.converteDataSql(object[1].toString()));
			lAux.setDataPagamento(DateConverter.converteDataSql(object[2].toString()));
			lAux.setIdAcaoLancamentoId(new Long(object[11].toString()));
			lAux.setCodigoLancamento(object[0] != null ? object[0].toString() : "");
			lAux.setQuantidadeParcela(object[13] != null ? Integer.valueOf(object[13].toString()) : 1);
			lAux.setLabelFornecedor(object[4] != null ? object[4].toString() : "");
			lAux.setTipoParcelamento(TipoParcelamento.valueOf(object[14].toString()));
			lAux.setContaPagadorLbl(object[3] != null ? object[3].toString() : "");
			lAux.setContaRecebedorLbl(object[4] != null ? object[4].toString() : "");
			lAux.setCompraID(object[14] != null ? object[14].toString() : "");
			lAux.setStatusPagamento(object[5] != null ? Integer.valueOf(object[5].toString()) : 1);
			lAux.setSinalizador(object[22].toString());

			if (lAux.getTipoLancamento().equals("ad")) {
				if (lAux.getStatusAd().equals("VALIDADO")) {
					lAux.setValorNulo(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
					retorno.add(lAux);
					continue;
				} else {
					lAux.setValorPagoAcao(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
					if (lAux.getSinalizador().equals("-")) {
						totalSaida = totalSaida.add(lAux.getValorPagoAcao());
					} else {
						totalEntrada = totalEntrada.add(lAux.getValorPagoAcao());
					}
					retorno.add(lAux);
					continue;
				}
			}

			if (lAux.getIdAdiantamentoDePrestacao() != null) {
				if (lAux.getStatusAdDePrestacao().equals("VALIDADO")) {
					if (lAux.getTipoLancamento().equals("dev") || lAux.getTipoLancamento().equals("reenb")) {
						lAux.setValorNulo(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
						retorno.add(lAux);
						continue;
					} else {
						lAux.setValorPagoAcao(
								object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);

						if (lAux.getSinalizador().equals("-")) {
							totalSaida = totalSaida.add(lAux.getValorPagoAcao());
						} else {
							totalEntrada = totalEntrada.add(lAux.getValorPagoAcao());
						}

						retorno.add(lAux);
						continue;
					}

				} else {
					lAux.setValorNulo(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
					retorno.add(lAux);
					continue;
				}

			} else {
				lAux.setValorPagoAcao(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
				if (lAux.getSinalizador().equals("-")) {
					totalSaida = totalSaida.add(lAux.getValorPagoAcao());
				} else {
					totalEntrada = totalEntrada.add(lAux.getValorPagoAcao());
				}
				retorno.add(lAux);
			}

		}

		if (!retorno.isEmpty()) {
			retorno.get(0).setTotalSaida(totalSaida);
			retorno.get(0).setTotalEntrada(totalEntrada);
		}

		return retorno;

	}

	public List<LancamentoAuxiliar> getExtratoCtrlDoacaoParaConferencia(Filtro filtro) {

		// hql
		StringBuilder hql = new StringBuilder("");
		hql.append("select l.id as id_lancamento_00, ");
		hql.append(
				"to_char(l.data_emissao,'DD-MM-YYYY') as data_emissao_01, to_char(pl.datapagamento,'DD-MM-YYYY') as data_pagamento_02,");
		hql.append("(select cb.nome_conta from conta_bancaria cb where  cb.id  = pl.conta_id) as conta_pagador_03,");
		hql.append(
				"(select cb.nome_conta from  conta_bancaria cb where cb.id = pl.contarecebedor_id) as conta_recebedor_04,");
		hql.append("la.status as status_05,");
		hql.append("l.descricao as descricao_06,");

		// hql.append("(select font.nome from fonte_pagadora font where id =
		// (select orc.fonte_id from orcamento orc where id = la.orcamento_id))
		// as fonte_07,");
		hql.append(
				" CASE  when la.rubricaorcamento_id is not null then (select font.nome from fonte_pagadora font where id = ");
		hql.append(
				" (select orc.fonte_id from  orcamento orc where id = (select rub.orcamento_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id))) else ");
		hql.append(
				" (select font.nome from fonte_pagadora font where id = (select orc.fonte_id from  orcamento orc where id = ");
		hql.append(" (select rub.orcamento_id from rubrica_orcamento rub where rub.id = ");
		hql.append(
				" (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))))end as fonte_07,");
		// hql.append("(select p.nome from projeto p where p.id = la.projeto_id)
		// as nome_projeto_08,");
		hql.append(
				"(select p.nome from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) as nome_projeto_08,");

		hql.append("pl.valor as valor_pago_by_acao_09, l.valor_total_com_desconto as valor_total_lancamento_10,");
		hql.append(
				"la.id as id_lancamento_acao_11, l.id as codigo_lancamento_12, l.quantidade_parcela as qtd_parcela_13,");
		hql.append("l.tipoparcelamento as tipo_parcelamento_14, l.compra_id  as compra_id_15, l.tipo as tipo_16, ");
		hql.append("l.reembolsado as reembolsado_17,");
		hql.append("l.idreembolso as id_reembolso_18,");
		hql.append("l.idfontereembolso as id_fonte_reembolsada_19,");
		hql.append("l.iddoacaoreembolso as id_doacao_reembolsada_20,");
		hql.append("l.depesareceita as dc_21,");
		hql.append("CASE  ");
		// hql.append("WHEN (l.depesareceita = 'DESPESA' and la.orcamento_id ");
		hql.append("WHEN (l.depesareceita = 'DESPESA' and ");

		hql.append(
				"(CASE  when la.rubricaorcamento_id is not null then (select orc.id from  orcamento orc where id = ");
		hql.append("(select rub.orcamento_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id)) else ");
		hql.append(
				"(select orc.id from  orcamento orc where id = (select rub.orcamento_id from rubrica_orcamento rub where rub.id = ");
		hql.append(
				"(select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) END) ");

		hql.append(" = :id_doacao) THEN '-' ");

		// (CASE la.rubricaorcamento_id when null then (select orc.id from
		// orcamento orc where id =
		// (select rub.orcamento_id from rubrica_orcamento rub where rub.id =
		// la.rubricaorcamento_id)) else
		// (select orc.id from orcamento orc where id = (select rub.orcamento_id
		// from rubrica_orcamento rub where rub.id =
		// (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id =
		// la.projetorubrica_id))) END)

		hql.append("WHEN (l.depesareceita = 'DESPESA' and l.iddoacaoreembolso = :id_doacao) THEN '+'");
		hql.append("WHEN (l.depesareceita = 'RECEITA') THEN '+'");
		hql.append("END as sinalizador_22,");
		hql.append("l.tipolancamento as tipo_lancamento_23,");
		hql.append("l.statusadiantamento as status_adiantamento_24,");
		hql.append(
				"(select ll.statusadiantamento from lancamento ll where ll.id = l.idadiantamento) status_ad_prestacao_25,");
		hql.append("l.idadiantamento as idadiantamento_26,");
		hql.append("l.numerodocumento as numero_documento_27,");
		hql.append("l.tipo as tipo_28, ");
		hql.append("pl.stt as status_29, ");
		hql.append(
				"(select p.codigo from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) as codigo_projeto_30,");
		hql.append("l.nota_fiscal as nota_fiscal_31, ");

		hql.append("CASE  when la.rubricaorcamento_id is not null then ");
		hql.append(
				"(select orc.titulo from  orcamento orc where id = (select rub.orcamento_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id)) else  ");
		hql.append("(select orc.titulo from  orcamento orc where id =  ");
		hql.append(
				"(select rub.orcamento_id from rubrica_orcamento rub where rub.id =  (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) ");
		hql.append("end as doacao_32,");

		hql.append("CASE  when la.rubricaorcamento_id is not null then ");
		hql.append(
				"(select comp.nome from  componente_class comp where id = (select rub.componente_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id)) else  ");
		hql.append("(select comp.nome from  componente_class comp where id =  ");
		hql.append(
				"(select rub.componente_id from rubrica_orcamento rub where rub.id =  (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) ");
		hql.append("end as componente_33,");

		hql.append("CASE  when la.rubricaorcamento_id is not null then ");
		hql.append(
				"(select sub.nome from  sub_componente  sub where id = (select rub.subcomponente_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id)) else  ");
		hql.append("(select sub.nome from  sub_componente  sub where id =  ");
		hql.append(
				"(select rub.subcomponente_id from rubrica_orcamento rub where rub.id =  (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id)))");
		hql.append("end as sub_componente_34, ");

		hql.append("CASE  when la.rubricaorcamento_id is not null then ");
		hql.append(
				"(select rub.nome from  rubrica rub where id = (select rub.rubrica_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id)) else  ");
		hql.append("(select rub.nome from  rubrica rub where id =  ");
		hql.append(
				"(select rub.rubrica_id from rubrica_orcamento rub where rub.id =  (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id)))");
		hql.append("end as requisito_35, ");
		hql.append("l.tipo as tipo_36, ");

		hql.append(
				"(select p.codigo from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) as codigo_projeto_37, ");

		hql.append(
				" (select g.nome from gestao g where g.id = (select p.gestao_id from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id))) as gestao_38");

		hql.append(
				" from lancamento l join lancamento_acao la on l.id = la.lancamento_id join pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");
		hql.append(
				"where l.tipo != 'compra'   and l.statuscompra = 'CONCLUIDO' and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) ");
		hql.append(" and  l.tipo != 'doacao_efetiva' ");
		// hql.append("and l.tipo != 'lanc_av' ");
		hql.append(
				" and ((CASE  when la.rubricaorcamento_id is not null then (select orc.id from  orcamento orc where id = ");
		hql.append("(select rub.orcamento_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id)) else ");
		hql.append(
				"(select orc.id from  orcamento orc where id = (select rub.orcamento_id from rubrica_orcamento rub where rub.id = ");
		hql.append("(select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) END)");
		hql.append(" = :id_doacao or l.iddoacaoreembolso = :id_doacao)");

		hql.append(" and  la.status =  0 ");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			hql.append(" and to_char(l.data_pagamento, 'yyyy-mm-dd') between '" + sdf.format(filtro.getDataInicio())
					+ "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinal()) + "' ");
			//
			// hql.append("and (l.data_pagamento >= '" +
			// sdf.format(filtro.getDataInicio()) + "' and l.data_pagamento <=
			// '" + sdf.format(filtro.getDataFinal()) + "') " );

		} else if (filtro.getDataInicio() != null) {
			hql.append(" and to_char(pl.datapagamento,'yyyy-mm-dd') >= '" + sdf.format(filtro.getDataInicio()) + "' ");

		} else if (filtro.getDataFinal() != null) {
			hql.append(
					" and and to_char(pl.datapagamento,'yyyy-mm-dd') <= '" + sdf.format(filtro.getDataFinal()) + "' ");
		}

		if (filtro.getDataInicioEmissao() != null && filtro.getDataFinalEmissao() != null) {
			hql.append(" and and to_char(l.data_emissao,'yyyy-mm-dd')  between '"
					+ sdf.format(filtro.getDataInicioEmissao()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");

		} else if (filtro.getDataInicioEmissao() != null) {
			hql.append(" and to_char(l.data_emissao,'yyyy-mm-dd') >= '" + sdf.format(filtro.getDataInicioEmissao())
					+ "' ");

		} else if (filtro.getDataFinalEmissao() != null) {
			hql.append(" and to_char(l.data_emissao,'yyyy-mm-dd')  >= '" + sdf.format(filtro.getDataFinalEmissao())
					+ "' ");
		}

		// if (filtro.getProjetoId() != null) {
		// hql.append(" and la.projeto_id = :projeto");
		// }

		// if (filtro.getProjetoId() != null) {
		// hql.append(" and a.projeto_id = :projeto");
		// }

		if (filtro.getContas() != null && filtro.getContas().length > 0) {
			hql.append(" and (pl.conta_id in (:contas) OR pl.contarecebedor_id in (:contas))  ");
		}

		if (filtro.getProjetos() != null && filtro.getProjetos().length > 0) {
			hql.append(
					" and (select p.id from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) in (:projetos) ");
		}

		if (filtro.getIdConta() != null) {
			hql.append(" and l.contarecebedor_id = :contaa");
		}

		if (filtro.getLancamentoID() != null) {
			hql.append(" and l.id = :lancamento");
		}

		hql.append(" order by pl.datapagamento desc, l.data_emissao desc");

		Query query = manager.createNativeQuery(hql.toString());

		query.setParameter("id_doacao", filtro.getIdDoacao());
		//
		// if (filtro.getProjetoId() != null) {
		// query.setParameter("projeto", filtro.getProjetoId());
		// }

		if (filtro.getContas() != null && filtro.getContas().length > 0) {
			List<Integer> list = new ArrayList<>();
			Integer[] mList = filtro.getContas();
			for (int i = 0; i < filtro.getContas().length; i++) {
				list.add(mList[i]);
			}

			query.setParameter("contas", list);
		}

		if (filtro.getProjetos() != null && filtro.getProjetos().length > 0) {
			List<Integer> list = new ArrayList<>();
			Integer[] mList = filtro.getProjetos();
			for (int i = 0; i < filtro.getProjetos().length; i++) {
				list.add(mList[i]);
			}

			query.setParameter("projetos", list);
		}

		if (filtro.getIdConta() != null) {
			query.setParameter("contaa", filtro.getIdConta());
		}

		if (filtro.getLancamentoID() != null) {
			query.setParameter("lancamento", filtro.getLancamentoID());
		}

		List<LancamentoAuxiliar> retorno = new ArrayList<LancamentoAuxiliar>();
		List<Object[]> result = query.getResultList();

		LancamentoAuxiliar lAux = new LancamentoAuxiliar();

		BigDecimal totalSaida = BigDecimal.ZERO;
		BigDecimal totalEntrada = BigDecimal.ZERO;

		for (Object[] object : result) {

			lAux = new LancamentoAuxiliar();
			lAux.setIdAdiantamentoDePrestacao(object[26] != null ? new Long(object[26].toString()) : null);
			lAux.setStatusAdDePrestacao(object[25] != null ? object[25].toString() : "");
			lAux.setTipoLancamento(object[23] != null ? object[23].toString() : "");
			lAux.setStatusAd(object[24] != null ? object[24].toString() : "");

			// lAux.setAcaoId(new Long(object[0].toString()));
			lAux.setId(new Long(object[0].toString()));
			lAux.setDescricao(object[6].toString());
			lAux.setFonte(object[7].toString());
			lAux.setNomeProjeto(object[8] != null ? object[8].toString() : "");
			lAux.setValorLancamento(object[10] != null ? new BigDecimal(object[10].toString()) : BigDecimal.ZERO);
			lAux.setDataEmissao(DateConverter.converteDataSql(object[1].toString()));
			lAux.setDataPagamento(DateConverter.converteDataSql(object[2].toString()));
			lAux.setIdAcaoLancamentoId(new Long(object[11].toString()));
			lAux.setCodigoLancamento(object[0] != null ? object[0].toString() : "");
			lAux.setQuantidadeParcela(object[13] != null ? Integer.valueOf(object[13].toString()) : 1);
			lAux.setLabelFornecedor(object[4] != null ? object[4].toString() : "");
			lAux.setTipoParcelamento(TipoParcelamento.valueOf(object[14].toString()));
			lAux.setContaPagadorLbl(object[3] != null ? object[3].toString() : "");
			lAux.setContaRecebedorLbl(object[4] != null ? object[4].toString() : "");
			lAux.setCompraID(object[14] != null ? object[14].toString() : "");
			lAux.setStatusPagamento(object[5] != null ? Integer.valueOf(object[5].toString()) : 1);
			lAux.setSinalizador(object[22].toString());
			lAux.setNumeroDocumento(object[27] != null ? object[27].toString() : "");
			lAux.setStatus(object[29] != null ? object[29].toString() : "");
			lAux.setCodigo(object[30] != null ? object[30].toString() : "");
			lAux.setNotaFiscal(object[31] != null ? object[31].toString() : "");
			lAux.setDoacao(object[32] != null ? object[32].toString() : "");
			lAux.setComponente(object[33] != null ? object[33].toString() : "");
			lAux.setSubComponente(object[34] != null ? object[34].toString() : "");
			lAux.setRequisitoDeDoador(object[35] != null ? object[35].toString() : "");
			lAux.setTipo(object[36] != null ? object[36].toString() : "");

			lAux.setCodigo(object[37] != null ? object[37].toString() : "");
			lAux.setNomeGestao(object[38] != null ? object[38].toString() : "");

			hql.append(
					"(select rub.rubrica_id from rubrica_orcamento rub where rub.id =  (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id)))");
			hql.append("end as requisito_35, ");
			hql.append("l.tipo as tipo_36, ");

			hql.append(
					"(select p.codigo from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) as codigo_projeto_37");

			if (lAux.getTipoLancamento().equals("ad")) {
				if (lAux.getStatusAd().equals("VALIDADO")) {
					lAux.setValorNulo(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
					lAux.setSaida(BigDecimal.ZERO);
					lAux.setEntrada(BigDecimal.ZERO);
					retorno.add(lAux);
					continue;
				} else {
					lAux.setValorPagoAcao(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
					if (lAux.getSinalizador().equals("-")) {
						totalSaida = totalSaida.add(lAux.getValorPagoAcao());
						lAux.setSaida(lAux.getValorPagoAcao());
						lAux.setEntrada(BigDecimal.ZERO);
					} else {
						totalEntrada = totalEntrada.add(lAux.getValorPagoAcao());
						lAux.setSaida(BigDecimal.ZERO);
						lAux.setEntrada(lAux.getValorPagoAcao());
					}
					retorno.add(lAux);
					continue;
				}
			}

			if (lAux.getIdAdiantamentoDePrestacao() != null) {
				if (lAux.getStatusAdDePrestacao().equals("VALIDADO")) {
					if (lAux.getTipoLancamento().equals("dev") || lAux.getTipoLancamento().equals("reenb")
							|| lAux.getTipoLancamento().equals("reemb_conta")) {
						lAux.setValorNulo(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
						lAux.setSaida(BigDecimal.ZERO);
						lAux.setEntrada(BigDecimal.ZERO);
						retorno.add(lAux);
						continue;
					} else {
						lAux.setValorPagoAcao(
								object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
						if (lAux.getSinalizador().equals("-")) {
							totalSaida = totalSaida.add(lAux.getValorPagoAcao());
							lAux.setSaida(lAux.getValorPagoAcao());
							lAux.setEntrada(BigDecimal.ZERO);
						} else {
							totalEntrada = totalEntrada.add(lAux.getValorPagoAcao());
							lAux.setSaida(BigDecimal.ZERO);
							lAux.setEntrada(lAux.getValorPagoAcao());
						}

						retorno.add(lAux);
						continue;
					}

				} else {
					lAux.setValorNulo(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
					lAux.setSaida(BigDecimal.ZERO);
					lAux.setEntrada(BigDecimal.ZERO);
					retorno.add(lAux);
					continue;
				}

			} else {
				lAux.setValorPagoAcao(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
				if (lAux.getSinalizador().equals("-")) {
					totalSaida = totalSaida.add(lAux.getValorPagoAcao());
					lAux.setSaida(lAux.getValorPagoAcao());
					lAux.setEntrada(BigDecimal.ZERO);
				} else {
					totalEntrada = totalEntrada.add(lAux.getValorPagoAcao());
					lAux.setSaida(BigDecimal.ZERO);
					lAux.setEntrada(lAux.getValorPagoAcao());
				}
				retorno.add(lAux);
			}

		}

		if (!retorno.isEmpty()) {
			retorno.get(0).setTotalSaida(totalSaida);
			retorno.get(0).setTotalEntrada(totalEntrada);
		}

		return retorno;

	}

	public List<LancamentoAuxiliar> getExtratoCtrlDoacao(Filtro filtro) {

		// hql
		StringBuilder hql = new StringBuilder("");
		hql.append("select l.id as id_lancamento_00, ");
		hql.append(
				"to_char(l.data_emissao,'DD-MM-YYYY') as data_emissao_01, to_char(pl.datapagamento,'DD-MM-YYYY') as data_pagamento_02,");
		hql.append("(select cb.nome_conta from conta_bancaria cb where  cb.id  = pl.conta_id) as conta_pagador_03,");
		hql.append(
				"(select cb.nome_conta from  conta_bancaria cb where cb.id = pl.contarecebedor_id) as conta_recebedor_04,");
		hql.append("la.status as status_05,");
		hql.append("l.descricao as descricao_06,");

		// hql.append("(select font.nome from fonte_pagadora font where id =
		// (select orc.fonte_id from orcamento orc where id = la.orcamento_id))
		// as fonte_07,");
		hql.append(
				" CASE  when la.rubricaorcamento_id is not null then (select font.nome from fonte_pagadora font where id = ");
		hql.append(
				" (select orc.fonte_id from  orcamento orc where id = (select rub.orcamento_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id))) else ");
		hql.append(
				" (select font.nome from fonte_pagadora font where id = (select orc.fonte_id from  orcamento orc where id = ");
		hql.append(" (select rub.orcamento_id from rubrica_orcamento rub where rub.id = ");
		hql.append(
				" (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))))end as fonte_07,");
		// hql.append("(select p.nome from projeto p where p.id = la.projeto_id)
		// as nome_projeto_08,");
		hql.append(
				"(select p.nome from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) as nome_projeto_08,");

		hql.append("pl.valor as valor_pago_by_acao_09, l.valor_total_com_desconto as valor_total_lancamento_10,");
		hql.append(
				"la.id as id_lancamento_acao_11, l.id as codigo_lancamento_12, l.quantidade_parcela as qtd_parcela_13,");
		hql.append("l.tipoparcelamento as tipo_parcelamento_14, l.compra_id  as compra_id_15, l.tipo as tipo_16, ");
		hql.append("l.reembolsado as reembolsado_17,");
		hql.append("l.idreembolso as id_reembolso_18,");
		hql.append("l.idfontereembolso as id_fonte_reembolsada_19,");
		hql.append("l.iddoacaoreembolso as id_doacao_reembolsada_20,");
		hql.append("l.depesareceita as dc_21,");
		hql.append("CASE  ");
		// hql.append("WHEN (l.depesareceita = 'DESPESA' and la.orcamento_id ");
		hql.append("WHEN (l.depesareceita = 'DESPESA' and ");

		hql.append(
				"(CASE  when la.rubricaorcamento_id is not null then (select orc.id from  orcamento orc where id = ");
		hql.append("(select rub.orcamento_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id)) else ");
		hql.append(
				"(select orc.id from  orcamento orc where id = (select rub.orcamento_id from rubrica_orcamento rub where rub.id = ");
		hql.append(
				"(select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) END) ");

		hql.append(" = :id_doacao) THEN '-' ");

		// (CASE la.rubricaorcamento_id when null then (select orc.id from
		// orcamento orc where id =
		// (select rub.orcamento_id from rubrica_orcamento rub where rub.id =
		// la.rubricaorcamento_id)) else
		// (select orc.id from orcamento orc where id = (select rub.orcamento_id
		// from rubrica_orcamento rub where rub.id =
		// (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id =
		// la.projetorubrica_id))) END)

		hql.append("WHEN (l.depesareceita = 'DESPESA' and l.iddoacaoreembolso = :id_doacao) THEN '+'");
		hql.append("WHEN (l.depesareceita = 'RECEITA') THEN '+'");
		hql.append("END as sinalizador_22,");
		hql.append("l.tipolancamento as tipo_lancamento_23,");
		hql.append("l.statusadiantamento as status_adiantamento_24,");
		hql.append(
				"(select ll.statusadiantamento from lancamento ll where ll.id = l.idadiantamento) status_ad_prestacao_25,");
		hql.append("l.idadiantamento as idadiantamento_26");

		hql.append(
				" from lancamento l join lancamento_acao la on l.id = la.lancamento_id join pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");
		hql.append(
				"where l.tipo != 'compra'   and l.statuscompra = 'CONCLUIDO' and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) ");
		hql.append(" and  l.tipo != 'doacao_efetiva' ");
		// hql.append("and l.tipo != 'lanc_av' ");
		hql.append(
				" and ((CASE  when la.rubricaorcamento_id is not null then (select orc.id from  orcamento orc where id = ");
		hql.append("(select rub.orcamento_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id)) else ");
		hql.append(
				"(select orc.id from  orcamento orc where id = (select rub.orcamento_id from rubrica_orcamento rub where rub.id = ");
		hql.append("(select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id))) END)");
		hql.append(" = :id_doacao or l.iddoacaoreembolso = :id_doacao)");

		hql.append(" and  la.status =  0 ");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			hql.append(" and to_char(l.data_pagamento, 'yyyy-mm-dd') between '" + sdf.format(filtro.getDataInicio())
					+ "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinal()) + "' ");
			//
			// hql.append("and (l.data_pagamento >= '" +
			// sdf.format(filtro.getDataInicio()) + "' and l.data_pagamento <=
			// '" + sdf.format(filtro.getDataFinal()) + "') " );

		} else if (filtro.getDataInicio() != null) {
			hql.append(" and to_char(pl.datapagamento,'yyyy-mm-dd') >= '" + sdf.format(filtro.getDataInicio()) + "' ");

		} else if (filtro.getDataFinal() != null) {
			hql.append(
					" and and to_char(pl.datapagamento,'yyyy-mm-dd') <= '" + sdf.format(filtro.getDataFinal()) + "' ");
		}

		if (filtro.getDataInicioEmissao() != null && filtro.getDataFinalEmissao() != null) {
			hql.append(" and and to_char(l.data_emissao,'yyyy-mm-dd')  between '"
					+ sdf.format(filtro.getDataInicioEmissao()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");

		} else if (filtro.getDataInicioEmissao() != null) {
			hql.append(" and to_char(l.data_emissao,'yyyy-mm-dd') >= '" + sdf.format(filtro.getDataInicioEmissao())
					+ "' ");

		} else if (filtro.getDataFinalEmissao() != null) {
			hql.append(" and to_char(l.data_emissao,'yyyy-mm-dd')  >= '" + sdf.format(filtro.getDataFinalEmissao())
					+ "' ");
		}

		if (filtro.getProjetoId() != null) {
			hql.append(" and la.projeto_id = :projeto");
		}

		// if (filtro.getProjetoId() != null) {
		// hql.append(" and a.projeto_id = :projeto");
		// }

		if (filtro.getIdConta() != null) {
			hql.append(" and l.contarecebedor_id = :contaa");
		}

		if (filtro.getLancamentoID() != null) {
			hql.append(" and l.id = :lancamento");
		}

		hql.append(" order by pl.datapagamento desc, l.data_emissao desc");

		Query query = manager.createNativeQuery(hql.toString());

		query.setParameter("id_doacao", filtro.getIdDoacao());
		//
		if (filtro.getProjetoId() != null) {
			query.setParameter("projeto", filtro.getProjetoId());
		}

		if (filtro.getIdConta() != null) {
			query.setParameter("contaa", filtro.getIdConta());
		}

		if (filtro.getLancamentoID() != null) {
			query.setParameter("lancamento", filtro.getLancamentoID());
		}

		List<LancamentoAuxiliar> retorno = new ArrayList<LancamentoAuxiliar>();
		List<Object[]> result = query.getResultList();

		LancamentoAuxiliar lAux = new LancamentoAuxiliar();

		BigDecimal totalSaida = BigDecimal.ZERO;
		BigDecimal totalEntrada = BigDecimal.ZERO;

		for (Object[] object : result) {

			lAux = new LancamentoAuxiliar();
			lAux.setIdAdiantamentoDePrestacao(object[26] != null ? new Long(object[26].toString()) : null);
			lAux.setStatusAdDePrestacao(object[25] != null ? object[25].toString() : "");
			lAux.setTipoLancamento(object[23] != null ? object[23].toString() : "");
			lAux.setStatusAd(object[24] != null ? object[24].toString() : "");

			// lAux.setAcaoId(new Long(object[0].toString()));
			lAux.setId(new Long(object[0].toString()));
			lAux.setDescricao(object[6].toString());
			lAux.setFonte(object[7].toString());
			lAux.setNomeProjeto(object[8] != null ? object[8].toString() : "");
			lAux.setValorLancamento(object[10] != null ? new BigDecimal(object[10].toString()) : BigDecimal.ZERO);
			lAux.setDataEmissao(DateConverter.converteDataSql(object[1].toString()));
			lAux.setDataPagamento(DateConverter.converteDataSql(object[2].toString()));
			lAux.setIdAcaoLancamentoId(new Long(object[11].toString()));
			lAux.setCodigoLancamento(object[0] != null ? object[0].toString() : "");
			lAux.setQuantidadeParcela(object[13] != null ? Integer.valueOf(object[13].toString()) : 1);
			lAux.setLabelFornecedor(object[4] != null ? object[4].toString() : "");
			lAux.setTipoParcelamento(TipoParcelamento.valueOf(object[14].toString()));
			lAux.setContaPagadorLbl(object[3] != null ? object[3].toString() : "");
			lAux.setContaRecebedorLbl(object[4] != null ? object[4].toString() : "");
			lAux.setCompraID(object[14] != null ? object[14].toString() : "");
			lAux.setStatusPagamento(object[5] != null ? Integer.valueOf(object[5].toString()) : 1);
			lAux.setSinalizador(object[22].toString());

			if (lAux.getTipoLancamento().equals("ad")) {
				if (lAux.getStatusAd().equals("VALIDADO")) {
					lAux.setValorNulo(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
					retorno.add(lAux);
					continue;
				} else {
					lAux.setValorPagoAcao(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
					if (lAux.getSinalizador().equals("-")) {
						totalSaida = totalSaida.add(lAux.getValorPagoAcao());
					} else {
						totalEntrada = totalEntrada.add(lAux.getValorPagoAcao());
					}
					retorno.add(lAux);
					continue;
				}
			}

			if (lAux.getIdAdiantamentoDePrestacao() != null) {
				if (lAux.getStatusAdDePrestacao().equals("VALIDADO")) {
					if (lAux.getTipoLancamento().equals("dev") || lAux.getTipoLancamento().equals("reenb")
							|| lAux.getTipoLancamento().equals("reemb_conta")) {
						lAux.setValorNulo(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
						retorno.add(lAux);
						continue;
					} else {
						lAux.setValorPagoAcao(
								object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
						if (lAux.getSinalizador().equals("-")) {
							totalSaida = totalSaida.add(lAux.getValorPagoAcao());
						} else {
							totalEntrada = totalEntrada.add(lAux.getValorPagoAcao());
						}

						retorno.add(lAux);
						continue;
					}

				} else {
					lAux.setValorNulo(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
					retorno.add(lAux);
					continue;
				}

			} else {
				lAux.setValorPagoAcao(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
				if (lAux.getSinalizador().equals("-")) {
					totalSaida = totalSaida.add(lAux.getValorPagoAcao());
				} else {
					totalEntrada = totalEntrada.add(lAux.getValorPagoAcao());
				}
				retorno.add(lAux);
			}

		}

		if (!retorno.isEmpty()) {
			retorno.get(0).setTotalSaida(totalSaida);
			retorno.get(0).setTotalEntrada(totalEntrada);
		}

		return retorno;
	}

	public List<LancamentoAuxiliar> getDoacoesEfetivadas(Filtro filtro) {

		// hql
		StringBuilder hql = new StringBuilder("");
		hql.append("select l.id as id_lancamento_00, ");
		hql.append("la.id as id_lancamento_acao_01,");
		hql.append("pl.id as id_pagamento_02,");
		hql.append("to_char(pl.datapagamento,'DD-MM-YYYY') as data_pagamento_03,");
		hql.append("to_char(l.data_emissao,'DD-MM-YYYY') as data_emissao_04,");
		hql.append("l.descricao as descricao_05,");
		hql.append("(select cont.nome_conta from conta_bancaria cont where cont.id = pl.conta_id) as doador_06,");
		hql.append("pl.stt as status_07,");
		hql.append("pl.valor as valor_08 ");
		hql.append(
				"from lancamento l join lancamento_acao  la on la.lancamento_id = l.id join pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");
		hql.append(" where l.tipo = 'doacao_efetiva' ");
		hql.append(
				"and (select ro.orcamento_id from rubrica_orcamento  ro where id = la.rubricaorcamento_id) = :id_doacao ");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		
		if (filtro.getStatusPagamento() != null) {
			hql.append(" and pl.stt = :stt_pgto ");
		}

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			hql.append(" and to_char(pl.datapagamento, 'yyyy-mm-dd') between '" + sdf.format(filtro.getDataInicio())
					+ "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinal()) + "' ");
			//
			// hql.append("and (l.data_pagamento >= '" +
			// sdf.format(filtro.getDataInicio()) + "' and l.data_pagamento <=
			// '" + sdf.format(filtro.getDataFinal()) + "') " );

		} else if (filtro.getDataInicio() != null) {
			hql.append(" and to_char(pl.datapagamento,'yyyy-mm-dd') >= '" + sdf.format(filtro.getDataInicio()) + "' ");

		} else if (filtro.getDataFinal() != null) {
			hql.append(
					" and and to_char(pl.datapagamento,'yyyy-mm-dd') <= '" + sdf.format(filtro.getDataFinal()) + "' ");
		}

		if (filtro.getDataInicioEmissao() != null && filtro.getDataFinalEmissao() != null) {
			hql.append(" and and to_char(l.data_emissao,'yyyy-mm-dd')  between '"
					+ sdf.format(filtro.getDataInicioEmissao()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");

		} else if (filtro.getDataInicioEmissao() != null) {
			hql.append(" and to_char(l.data_emissao,'yyyy-mm-dd') >= '" + sdf.format(filtro.getDataInicioEmissao())
					+ "' ");

		} else if (filtro.getDataFinalEmissao() != null) {
			hql.append(" and to_char(l.data_emissao,'yyyy-mm-dd')  >= '" + sdf.format(filtro.getDataFinalEmissao())
					+ "' ");
		}

		if (filtro.getIdConta() != null) {
			hql.append(" and l.contarecebedor_id = :contaa");
		}

		if (filtro.getLancamentoID() != null) {
			hql.append(" and l.id = :lancamento");
		}

		hql.append(" order by pl.datapagamento asc, l.data_emissao asc");

		Query query = manager.createNativeQuery(hql.toString());

		query.setParameter("id_doacao", filtro.getIdDoacao());
		
		if (filtro.getStatusPagamento() != null) {
			query.setParameter("stt_pgto", filtro.getStatusPagamento().name());
		}

		if (filtro.getLancamentoID() != null) {
			query.setParameter("lancamento", filtro.getLancamentoID());
		}

		List<LancamentoAuxiliar> retorno = new ArrayList<LancamentoAuxiliar>();
		List<Object[]> result = query.getResultList();

		LancamentoAuxiliar lAux = new LancamentoAuxiliar();

		for (Object[] object : result) {

			lAux = new LancamentoAuxiliar();
			lAux.setId(new Long(object[0].toString()));
			lAux.setIdAcaoLancamentoId(new Long(object[1].toString()));
			lAux.setIdPagamento(new Long(object[2].toString()));
			lAux.setDataPagamento(DateConverter.converteDataSql(object[3].toString()));
			lAux.setDataEmissao(DateConverter.converteDataSql(object[4].toString()));
			lAux.setDescricao(object[5].toString());
			lAux.setContaPagadorLbl(object[6].toString());
			lAux.setStatus(object[7].toString());
			lAux.setValorPagoAcao(new BigDecimal(object[8].toString()));
			retorno.add(lAux);
		}

		return retorno;
	}

	public BigDecimal getAreceber(Filtro filtro) {

		// hql
		StringBuilder hql = new StringBuilder("");
		hql.append("select sum(pl.valor) as valor ");
		hql.append(
				"from lancamento l join lancamento_acao  la on la.lancamento_id = l.id join pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");
		hql.append(" where l.tipo = 'doacao_efetiva' ");
		hql.append(
				"and (select ro.orcamento_id from rubrica_orcamento  ro where id = la.rubricaorcamento_id) = :id_doacao ");
		// hql.append("and pl.stt = 'EFETIVADO' ");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			hql.append(" and to_char(pl.datapagamento, 'yyyy-mm-dd') between '" + sdf.format(filtro.getDataInicio())
					+ "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinal()) + "' ");
			//
			// hql.append("and (l.data_pagamento >= '" +
			// sdf.format(filtro.getDataInicio()) + "' and l.data_pagamento <=
			// '" + sdf.format(filtro.getDataFinal()) + "') " );

		} else if (filtro.getDataInicio() != null) {
			hql.append(" and to_char(pl.datapagamento,'yyyy-mm-dd') >= '" + sdf.format(filtro.getDataInicio()) + "' ");

		} else if (filtro.getDataFinal() != null) {
			hql.append(
					" and and to_char(pl.datapagamento,'yyyy-mm-dd') <= '" + sdf.format(filtro.getDataFinal()) + "' ");
		}

		if (filtro.getDataInicioEmissao() != null && filtro.getDataFinalEmissao() != null) {
			hql.append(" and and to_char(l.data_emissao,'yyyy-mm-dd')  between '"
					+ sdf.format(filtro.getDataInicioEmissao()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");

		} else if (filtro.getDataInicioEmissao() != null) {
			hql.append(" and to_char(l.data_emissao,'yyyy-mm-dd') >= '" + sdf.format(filtro.getDataInicioEmissao())
					+ "' ");

		} else if (filtro.getDataFinalEmissao() != null) {
			hql.append(" and to_char(l.data_emissao,'yyyy-mm-dd')  >= '" + sdf.format(filtro.getDataFinalEmissao())
					+ "' ");
		}

		if (filtro.getIdConta() != null) {
			hql.append(" and l.contarecebedor_id = :contaa");
		}

		if (filtro.getLancamentoID() != null) {
			hql.append(" and l.id = :lancamento");
		}

		// hql.append(" order by pl.datapagamento desc, l.data_emissao desc");

		Query query = manager.createNativeQuery(hql.toString());

		query.setParameter("id_doacao", filtro.getIdDoacao());

		if (filtro.getLancamentoID() != null) {
			query.setParameter("lancamento", filtro.getLancamentoID());
		}

		if (query.getSingleResult() == null) {
			return BigDecimal.ZERO;
		} else {
			return (BigDecimal) query.getSingleResult();
		}
	}

	public BigDecimal getSaldoDoacaoEfetiva(Filtro filtro) {

		// hql
		StringBuilder hql = new StringBuilder("");
		hql.append("select sum(pl.valor) as valor ");
		hql.append(
				"from lancamento l join lancamento_acao  la on la.lancamento_id = l.id join pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");
		hql.append(" where l.tipo = 'doacao_efetiva' ");
		hql.append(
				"and (select ro.orcamento_id from rubrica_orcamento  ro where id = la.rubricaorcamento_id) = :id_doacao ");
		hql.append("and pl.stt = 'EFETIVADO' ");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			hql.append(" and to_char(pl.datapagamento, 'yyyy-mm-dd') between '" + sdf.format(filtro.getDataInicio())
					+ "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinal()) + "' ");
			//
			// hql.append("and (l.data_pagamento >= '" +
			// sdf.format(filtro.getDataInicio()) + "' and l.data_pagamento <=
			// '" + sdf.format(filtro.getDataFinal()) + "') " );

		} else if (filtro.getDataInicio() != null) {
			hql.append(" and to_char(pl.datapagamento,'yyyy-mm-dd') >= '" + sdf.format(filtro.getDataInicio()) + "' ");

		} else if (filtro.getDataFinal() != null) {
			hql.append(
					" and and to_char(pl.datapagamento,'yyyy-mm-dd') <= '" + sdf.format(filtro.getDataFinal()) + "' ");
		}

		if (filtro.getDataInicioEmissao() != null && filtro.getDataFinalEmissao() != null) {
			hql.append(" and and to_char(l.data_emissao,'yyyy-mm-dd')  between '"
					+ sdf.format(filtro.getDataInicioEmissao()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");

		} else if (filtro.getDataInicioEmissao() != null) {
			hql.append(" and to_char(l.data_emissao,'yyyy-mm-dd') >= '" + sdf.format(filtro.getDataInicioEmissao())
					+ "' ");

		} else if (filtro.getDataFinalEmissao() != null) {
			hql.append(" and to_char(l.data_emissao,'yyyy-mm-dd')  >= '" + sdf.format(filtro.getDataFinalEmissao())
					+ "' ");
		}

		if (filtro.getIdConta() != null) {
			hql.append(" and l.contarecebedor_id = :contaa");
		}

		if (filtro.getLancamentoID() != null) {
			hql.append(" and l.id = :lancamento");
		}

		// hql.append(" order by pl.datapagamento desc, l.data_emissao desc");

		Query query = manager.createNativeQuery(hql.toString());

		query.setParameter("id_doacao", filtro.getIdDoacao());

		if (filtro.getLancamentoID() != null) {
			query.setParameter("lancamento", filtro.getLancamentoID());
		}

		if (query.getSingleResult() == null) {
			return BigDecimal.ZERO;
		} else {
			return (BigDecimal) query.getSingleResult();
		}

	}

	public List<LancamentoAuxiliar> getLancamentosReportRECLASSIFICACAO(Filtro filtro) {

		// hql
		StringBuilder hql = new StringBuilder("");
		hql.append("select l.id as id_lancamento_00, \n");
		hql.append(
				"to_char(l.data_emissao,'DD-MM-YYYY') as data_emissao_01, to_char(pl.datapagamento,'DD-MM-YYYY') as data_pagamento_02, \n");
		hql.append("(select cb.nome_conta from conta_bancaria cb where  cb.id  = pl.conta_id) as conta_pagador_03, \n");
		hql.append(
				"(select cb.nome_conta from  conta_bancaria cb where cb.id = pl.contarecebedor_id) as conta_recebedor_04, \n");
		hql.append("la.status as status_05, \n");
		hql.append("l.descricao as descricao_06, \n");

		// hql.append("(select font.nome from fonte_pagadora font where id =
		// (select orc.fonte_id from orcamento orc where id = la.orcamento_id))
		// as fonte_07,");
		hql.append("CASE  when la.rubricaorcamento_id is null then \n");
		hql.append(
				"(select font.nome from fonte_pagadora font where id = (select orc.fonte_id from  orcamento orc where id = \n");
		hql.append(
				"(select rub.orcamento_id from rubrica_orcamento rub where rub.id = (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id)))) \n");
		hql.append("else \n");
		hql.append(
				"(select font.nome from fonte_pagadora font where id = (select orc.fonte_id from  orcamento orc where id = \n");
		hql.append("(select rub.orcamento_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id))) \n");
		hql.append("end as fonte_07, \n");
		// hql.append("(select p.nome from projeto p where p.id = la.projeto_id)
		// as nome_projeto_08,");
		hql.append(
				"(select p.nome from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) as nome_projeto_08, \n");
		hql.append("pl.valor as valor_pago_by_acao_09, l.valor_total_com_desconto as valor_total_lancamento_10, \n");
		hql.append(
				"la.id as id_lancamento_acao_11, l.id as codigo_lancamento_12, l.quantidade_parcela as qtd_parcela_13, \n");
		hql.append(
				"l.tipoparcelamento as tipo_parcelamento_14, l.compra_id  as compra_id_15, la.id as id_lancamento_acao_16, \n");
		hql.append("pl.id as id_pagamento_17,");
		hql.append("pl.stt as status_provisao_18,");
		hql.append("(select f.nome from fonte_pagadora f where la.fontepagadora_id = f.id) as fonte_v1_19,");
		hql.append("(select ac.codigo from acao ac where la.acao_id = ac.id) as acao_v1_20 ");
		// hql.append("(select f.nome from fonte_pagadora f where la.fontepagadora_id =
		// f.id) as fonte_v1_21 ");

		hql.append(
				" from lancamento l join lancamento_acao la on l.id = la.lancamento_id join pagamento_lancamento pl on pl.lancamentoacao_id = la.id \n");
		hql.append(
				"where l.tipo != 'compra'   and l.statuscompra = 'CONCLUIDO' and ((l.versionlancamento != 'MODE01') or (l.versionlancamento is null)) \n");

		if (filtro.getAcao() != null && filtro.getAcao().getId() != null) {
			hql.append("and (select ac.id from acao ac where la.acao_id = ac.id) =  " + filtro.getAcao().getId());
		}

		// hql.append("and l.tipo != 'lanc_av' ");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			hql.append(" and l.data_pagamento between '" + sdf.format(filtro.getDataInicio()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinal()) + "' ");

		} else if (filtro.getDataInicio() != null) {
			hql.append(" and l.data_pagamento >= '" + sdf.format(filtro.getDataInicio()) + "' ");

		} else if (filtro.getDataFinal() != null) {
			hql.append(" and l.data_pagamento >= '" + sdf.format(filtro.getDataFinal()) + "' ");
		}

		if (filtro.getDataInicioEmissao() != null && filtro.getDataFinalEmissao() != null) {
			hql.append(" and l.data_emissao between '" + sdf.format(filtro.getDataInicioEmissao()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");

		} else if (filtro.getDataInicioEmissao() != null) {
			hql.append(" and l.data_emissao >= '" + sdf.format(filtro.getDataInicioEmissao()) + "' ");

		} else if (filtro.getDataFinalEmissao() != null) {
			hql.append(" and l.data_emissao >= '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");
		}

		// if (filtro.getProjetoId() != null) {
		// hql.append(" and p.id = :projeto");
		// }

		// if (filtro.getProjetoId() != null) {
		// hql.append(" and a.projeto_id = :projeto");
		// }

		if (filtro.getIdConta() != null) {
			hql.append(" and (pl.contarecebedor_id = :contaa or pl.conta_id = :contaa) ");
		}

		if (filtro.getLancamentoID() != null) {
			hql.append(" and l.id = :lancamento ");
		}

		if (filtro.getNumeroDocumento() != null && !filtro.getNumeroDocumento().equals("")) {
			hql.append(" and l.numerodocumento = :doc ");
		}

		hql.append(" order by l.data_pagamento desc, l.data_emissao desc");

		Query query = manager.createNativeQuery(hql.toString());
		
		//
		// if (filtro.getProjetoId() != null) {
		// query.setParameter("projeto", filtro.getProjetoId());
		// }

		if (filtro.getIdConta() != null) {
			query.setParameter("contaa", filtro.getIdConta());
		}

		if (filtro.getLancamentoID() != null) {
			query.setParameter("lancamento", filtro.getLancamentoID());
		}

		if (filtro.getNumeroDocumento() != null && !filtro.getNumeroDocumento().equals("")) {
			query.setParameter("doc", filtro.getNumeroDocumento());
		}

		List<LancamentoAuxiliar> retorno = new ArrayList<LancamentoAuxiliar>();
		List<Object[]> result = query.getResultList();

		LancamentoAuxiliar lAux = new LancamentoAuxiliar();

		for (Object[] object : result) {

			lAux = new LancamentoAuxiliar();
			// lAux.setAcaoId(new Long(object[0].toString()));
			lAux.setId(new Long(object[0].toString()));
			lAux.setDescricao(object[6].toString());
			lAux.setFonte(object[7] != null ? object[7].toString() : "");
			lAux.setNomeProjeto(object[8] != null ? object[8].toString() : "");
			lAux.setValorPagoAcao(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
			lAux.setValorLancamento(object[10] != null ? new BigDecimal(object[10].toString()) : BigDecimal.ZERO);
			lAux.setDataEmissao(DateConverter.converteDataSql(object[1].toString()));
			lAux.setDataPagamento(DateConverter.converteDataSql(object[2].toString()));
			lAux.setIdAcaoLancamentoId(new Long(object[16].toString()));
			lAux.setCodigoLancamento(object[0] != null ? object[0].toString() : "");
			lAux.setQuantidadeParcela(object[13] != null ? Integer.valueOf(object[13].toString()) : 1);
			lAux.setLabelFornecedor(object[4] != null ? object[4].toString() : "");
			lAux.setTipoParcelamento(TipoParcelamento.valueOf(object[14].toString()));
			lAux.setContaPagadorLbl(object[3] != null ? object[3].toString() : "");
			lAux.setContaRecebedorLbl(object[4] != null ? object[4].toString() : "");
			lAux.setCompraID(object[14] != null ? object[14].toString() : "");
			lAux.setStatusPagamento(object[5] != null ? Integer.valueOf(object[5].toString()) : 1);
			lAux.setIdPagamento(new Long(object[17].toString()));
			lAux.setStatus(object[18].toString());
			lAux.setFonteV1(object[19] != null ? object[19].toString() : "");
			lAux.setAcaoV1(object[20] != null ? object[20].toString() : "");

			retorno.add(lAux);
		}

		return retorno;

	}

	public void validarReclassificacao(Long id) {
		String hql = "update pagamento_lancamento set reclassificado = :param where id = :id ";
		Query query = manager.createNativeQuery(hql);
		query.setParameter("param", true);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	public void desprovisionar(Long id) {
		String hql = "update lancamento set statuscompra = 'N_INCIADO' where id = :id ";
		Query query = manager.createNativeQuery(hql);
		// query.setParameter("param", true);
		query.setParameter("id", id);
		query.executeUpdate();
	}
	
	public void estornarLancamento(Long id) throws Exception {
		String sql = "update pagamento_lancamento pl set pl.stt = 'PROVISIONADO' where (select la.lancamento_id from lancamento_acao la where la.id = pl.lancamentoacao_id) = :pId";
		Query query = manager.createNativeQuery(sql);
		query.setParameter("pId", id);
		query.executeUpdate();
	}
	
	public void removerLancamento(Long id) {
		manager.remove(manager.find(Lancamento.class, id));
	}

	public List<LancamentoAuxiliar> getLancamentosParaValidacao(Filtro filtro) {

		// hql
		StringBuilder hql = new StringBuilder("");
		hql.append("select l.id as id_lancamento_00, \n");
		hql.append(
				"to_char(l.data_emissao,'DD-MM-YYYY') as data_emissao_01, to_char(pl.datapagamento,'DD-MM-YYYY') as data_pagamento_02, \n");
		hql.append("(select cb.nome_conta from conta_bancaria cb where  cb.id  = pl.conta_id) as conta_pagador_03, \n");
		hql.append(
				"(select cb.nome_conta from  conta_bancaria cb where cb.id = pl.contarecebedor_id) as conta_recebedor_04, \n");
		hql.append("la.status as status_05, \n");
		hql.append("l.descricao as descricao_06, \n");

		// hql.append("(select font.nome from fonte_pagadora font where id =
		// (select orc.fonte_id from orcamento orc where id = la.orcamento_id))
		// as fonte_07,");
		hql.append("CASE  when la.rubricaorcamento_id is null then \n");
		hql.append(
				"(select font.nome from fonte_pagadora font where id = (select orc.fonte_id from  orcamento orc where id = \n");
		hql.append(
				"(select rub.orcamento_id from rubrica_orcamento rub where rub.id = (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id)))) \n");
		hql.append("else \n");
		hql.append(
				"(select font.nome from fonte_pagadora font where id = (select orc.fonte_id from  orcamento orc where id = \n");
		hql.append("(select rub.orcamento_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id))) \n");
		hql.append("end as fonte_07, \n");
		// hql.append("(select p.nome from projeto p where p.id = la.projeto_id)
		// as nome_projeto_08,");
		hql.append(
				"(select p.nome from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) as nome_projeto_08, \n");
		hql.append("pl.valor as valor_pago_by_acao_09, l.valor_total_com_desconto as valor_total_lancamento_10, \n");
		hql.append(
				"la.id as id_lancamento_acao_11, l.id as codigo_lancamento_12, l.quantidade_parcela as qtd_parcela_13, \n");
		hql.append(
				"l.tipoparcelamento as tipo_parcelamento_14, l.compra_id  as compra_id_15, la.id as id_lancamento_acao_16, \n");
		hql.append("pl.id as id_pagamento_17,");
		hql.append("pl.stt as status_provisao_18,");
		hql.append("l.tipo_v4 as tipo_v4_19");
		
		hql.append(
				" from lancamento l join lancamento_acao la on l.id = la.lancamento_id join pagamento_lancamento pl on pl.lancamentoacao_id = la.id \n");
		hql.append(
				"where l.tipo != 'compra'   and l.statuscompra = 'CONCLUIDO' and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) \n");

		// hql.append("and l.tipo != 'lanc_av' ");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			hql.append(" and pl.datapagamento between '" + sdf.format(filtro.getDataInicio()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinal()) + "' ");

		} else if (filtro.getDataInicio() != null) {
			hql.append(" and pl.datapagamento >= '" + sdf.format(filtro.getDataInicio()) + "' ");

		} else if (filtro.getDataFinal() != null) {
			hql.append(" and pl.datapagamento >= '" + sdf.format(filtro.getDataFinal()) + "' ");
		}

		if (filtro.getDataInicioEmissao() != null && filtro.getDataFinalEmissao() != null) {
			hql.append(" and l.data_emissao between '" + sdf.format(filtro.getDataInicioEmissao()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");

		} else if (filtro.getDataInicioEmissao() != null) {
			hql.append(" and l.data_emissao >= '" + sdf.format(filtro.getDataInicioEmissao()) + "' ");

		} else if (filtro.getDataFinalEmissao() != null) {
			hql.append(" and l.data_emissao >= '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");
		}

		if (filtro.getProjetoId() != null) {
			hql.append(
					" and (select p.id from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) = :projeto");
		}

		if (filtro.getPagador() != null && filtro.getPagador().getId() != null) {
			hql.append(" and pl.conta_id = :pagador");
		}
		
		if (filtro.getFornecedor() != null && filtro.getFornecedor().getId() != null) {
			hql.append(" and pl.contarecebedor_id = :recebedor");
		}

		if (filtro.getStatusPagamentoInt() != null) {
			if (filtro.getStatusPagamentoInt() == 0) {
				hql.append(" and (la.status = 0) ");
			} else {
				hql.append(" and (la.status <> 0 or la.status is null) ");
			}

		}

		if (filtro.getLancamentoID() != null) {
			hql.append(" and (l.id = :lancamento or l.idadiantamento = :lancamento)");
		}

		if (filtro.getStatusPagamento() != null) {
			hql.append(" and pl.stt = :stt ");
		}

		hql.append(" order by pl.datapagamento desc, l.data_emissao desc");

		Query query = manager.createNativeQuery(hql.toString());


		if (filtro.getProjetoId() != null) {
			query.setParameter("projeto", filtro.getProjetoId());
		}

		if (filtro.getPagador() != null && filtro.getPagador().getId() != null) {
			query.setParameter("pagador", filtro.getPagador().getId());
		}
		
		if (filtro.getFornecedor() != null && filtro.getFornecedor().getId() != null) {
			query.setParameter("recebedor", filtro.getFornecedor().getId());
		}

		if (filtro.getLancamentoID() != null) {
			query.setParameter("lancamento", filtro.getLancamentoID());
		}

		if (filtro.getStatusPagamento() != null) {
			query.setParameter("stt", filtro.getStatusPagamento().toString());
		}

		List<LancamentoAuxiliar> retorno = new ArrayList<LancamentoAuxiliar>();
		List<Object[]> result = query.getResultList();

		LancamentoAuxiliar lAux = new LancamentoAuxiliar();
		
		

		for (Object[] object : result) {

			lAux = new LancamentoAuxiliar();
			// lAux.setAcaoId(new Long(object[0].toString()));

		
			lAux.setId(new Long(object[0].toString()));
			
			lAux.setDescricao(object[6].toString());
			lAux.setFonte(object[7].toString());
			lAux.setNomeProjeto(object[8] != null ? object[8].toString() : "");
			lAux.setValorPagoAcao(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
			lAux.setValorLancamento(object[10] != null ? new BigDecimal(object[10].toString()) : BigDecimal.ZERO);
			lAux.setDataEmissao(DateConverter.converteDataSql(object[1].toString()));
			lAux.setDataPagamento(DateConverter.converteDataSql(object[2].toString()));
			lAux.setIdAcaoLancamentoId(new Long(object[16].toString()));
			lAux.setCodigoLancamento(object[0] != null ? object[0].toString() : "");
			lAux.setQuantidadeParcela(object[13] != null ? Integer.valueOf(object[13].toString()) : 1);
			lAux.setLabelFornecedor(object[4] != null ? object[4].toString() : "");
			lAux.setTipoParcelamento(TipoParcelamento.valueOf(object[14].toString()));
			lAux.setContaPagadorLbl(object[3] != null ? object[3].toString() : "");
			lAux.setContaRecebedorLbl(object[4] != null ? object[4].toString() : "");
			lAux.setCompraID(object[15] != null ? object[15].toString() : "");
			lAux.setStatusPagamento(object[5] != null ? Integer.valueOf(object[5].toString()) : 1);
			lAux.setIdPagamento(new Long(object[17].toString()));
			lAux.setStatus(object[18].toString());
			lAux.setTipov4(object[19] != null ? object[19].toString() : "");
			
			retorno.add(lAux);
		}
		
		return retorno;

	}

	public List<LancamentoAuxiliar> getLancamentosReportMODE01(Filtro filtro) {

		// hql
		StringBuilder hql = new StringBuilder("");
		hql.append("select l.id as id_lancamento_00, \n");
		hql.append(
				"to_char(l.data_emissao,'DD-MM-YYYY') as data_emissao_01, to_char(pl.datapagamento,'DD-MM-YYYY') as data_pagamento_02, \n");
		hql.append("(select cb.nome_conta from conta_bancaria cb where  cb.id  = pl.conta_id) as conta_pagador_03, \n");
		hql.append(
				"(select cb.nome_conta from  conta_bancaria cb where cb.id = pl.contarecebedor_id) as conta_recebedor_04, \n");
		hql.append("la.status as status_05, \n");
		hql.append("l.descricao as descricao_06, \n");

		// hql.append("(select font.nome from fonte_pagadora font where id =
		// (select orc.fonte_id from orcamento orc where id = la.orcamento_id))
		// as fonte_07,");
		hql.append("CASE  when la.rubricaorcamento_id is null then \n");
		hql.append(
				"(select font.nome from fonte_pagadora font where id = (select orc.fonte_id from  orcamento orc where id = \n");
		hql.append(
				"(select rub.orcamento_id from rubrica_orcamento rub where rub.id = (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id)))) \n");
		hql.append("else \n");
		hql.append(
				"(select font.nome from fonte_pagadora font where id = (select orc.fonte_id from  orcamento orc where id = \n");
		hql.append("(select rub.orcamento_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id))) \n");
		hql.append("end as fonte_07, \n");
		// hql.append("(select p.nome from projeto p where p.id = la.projeto_id)
		// as nome_projeto_08,");
		hql.append(
				"(select p.nome from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) as nome_projeto_08, \n");
		hql.append("pl.valor as valor_pago_by_acao_09, l.valor_total_com_desconto as valor_total_lancamento_10, \n");
		hql.append(
				"la.id as id_lancamento_acao_11, l.id as codigo_lancamento_12, l.quantidade_parcela as qtd_parcela_13, \n");
		hql.append(
				"l.tipoparcelamento as tipo_parcelamento_14, l.compra_id  as compra_id_15, la.id as id_lancamento_acao_16, \n");
		hql.append("pl.id as id_pagamento_17,");
		hql.append("pl.stt as status_provisao");
		hql.append(
				" from lancamento l join lancamento_acao la on l.id = la.lancamento_id join pagamento_lancamento pl on pl.lancamentoacao_id = la.id \n");
		hql.append("where l.tipo != 'compra'   and l.statuscompra = 'CONCLUIDO' and l.versionlancamento = 'MODE01' \n");

		// hql.append("and l.tipo != 'lanc_av' ");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			hql.append(" and pl.datapagamento between '" + sdf.format(filtro.getDataInicio()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinal()) + "' ");

		} else if (filtro.getDataInicio() != null) {
			hql.append(" and pl.datapagamento >= '" + sdf.format(filtro.getDataInicio()) + "' ");

		} else if (filtro.getDataFinal() != null) {
			hql.append(" and pl.datapagamento >= '" + sdf.format(filtro.getDataFinal()) + "' ");
		}

		if (filtro.getDataInicioEmissao() != null && filtro.getDataFinalEmissao() != null) {
			hql.append(" and l.data_emissao between '" + sdf.format(filtro.getDataInicioEmissao()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");

		} else if (filtro.getDataInicioEmissao() != null) {
			hql.append(" and l.data_emissao >= '" + sdf.format(filtro.getDataInicioEmissao()) + "' ");

		} else if (filtro.getDataFinalEmissao() != null) {
			hql.append(" and l.data_emissao >= '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");
		}

		if (filtro.getProjetoId() != null) {
			hql.append(
					" and (select p.id from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) = :projeto");
		}

		if (filtro.getIdConta() != null) {
			hql.append(" and (pl.contarecebedor_id = :contaa or pl.conta_id = :contaa) ");
		}

		if (filtro.getLancamentoID() != null) {
			hql.append(" and l.id = :lancamento");
		}

		if (filtro.getStatusPagamento() != null) {
			hql.append(" and pl.stt = :stt ");
		}

		hql.append(" order by pl.datapagamento desc, l.data_emissao desc");

		Query query = manager.createNativeQuery(hql.toString());
		
		//

		if (filtro.getProjetoId() != null) {
			query.setParameter("projeto", filtro.getProjetoId());
		}

		if (filtro.getIdConta() != null) {
			query.setParameter("contaa", filtro.getIdConta());
		}

		if (filtro.getLancamentoID() != null) {
			query.setParameter("lancamento", filtro.getLancamentoID());
		}

		if (filtro.getStatusPagamento() != null) {
			query.setParameter("stt", filtro.getStatusPagamento().toString());
		}

		List<LancamentoAuxiliar> retorno = new ArrayList<LancamentoAuxiliar>();
		List<Object[]> result = query.getResultList();

		LancamentoAuxiliar lAux = new LancamentoAuxiliar();

		for (Object[] object : result) {

			lAux = new LancamentoAuxiliar();
			// lAux.setAcaoId(new Long(object[0].toString()));
			lAux.setId(new Long(object[0].toString()));
			lAux.setDescricao(object[6].toString());
			lAux.setFonte(object[7].toString());
			lAux.setNomeProjeto(object[8] != null ? object[8].toString() : "");
			lAux.setValorPagoAcao(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
			lAux.setValorLancamento(object[10] != null ? new BigDecimal(object[10].toString()) : BigDecimal.ZERO);
			lAux.setDataEmissao(DateConverter.converteDataSql(object[1].toString()));
			lAux.setDataPagamento(DateConverter.converteDataSql(object[2].toString()));
			lAux.setIdAcaoLancamentoId(new Long(object[16].toString()));
			lAux.setCodigoLancamento(object[0] != null ? object[0].toString() : "");
			lAux.setQuantidadeParcela(object[13] != null ? Integer.valueOf(object[13].toString()) : 1);
			lAux.setLabelFornecedor(object[4] != null ? object[4].toString() : "");
			lAux.setTipoParcelamento(TipoParcelamento.valueOf(object[14].toString()));
			lAux.setContaPagadorLbl(object[3] != null ? object[3].toString() : "");
			lAux.setContaRecebedorLbl(object[4] != null ? object[4].toString() : "");
			lAux.setCompraID(object[14] != null ? object[14].toString() : "");
			lAux.setStatusPagamento(object[5] != null ? Integer.valueOf(object[5].toString()) : 1);
			lAux.setIdPagamento(new Long(object[17].toString()));
			lAux.setStatus(object[18].toString());
			retorno.add(lAux);
		}

		return retorno;
	}

	public List<LancamentoAuxiliar> getLancamentosCP(Filtro filtro) {

		// hql
		StringBuilder hql = new StringBuilder("");
		hql.append("select l.id as id_lancamento_00, \n");
		hql.append(
				"to_char(l.data_emissao,'DD-MM-YYYY') as data_emissao_01, to_char(pl.datapagamento,'DD-MM-YYYY') as data_pagamento_02, \n");
		hql.append("(select cb.nome_conta from conta_bancaria cb where  cb.id  = pl.conta_id) as conta_pagador_03, \n");
		hql.append(
				"(select cb.nome_conta from  conta_bancaria cb where cb.id = pl.contarecebedor_id) as conta_recebedor_04, \n");
		hql.append("la.status as status_05, \n");
		hql.append("l.descricao as descricao_06, \n");
		hql.append("CASE  when la.rubricaorcamento_id is null then \n");
		hql.append(
				"(select font.nome from fonte_pagadora font where id = (select orc.fonte_id from  orcamento orc where id = \n");
		hql.append(
				"(select rub.orcamento_id from rubrica_orcamento rub where rub.id = (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id)))) \n");
		hql.append("else \n");
		hql.append(
				"(select font.nome from fonte_pagadora font where id = (select orc.fonte_id from  orcamento orc where id = \n");
		hql.append("(select rub.orcamento_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id))) \n");
		hql.append("end as fonte_07, \n");
		hql.append(
				"(select p.nome from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) as nome_projeto_08, \n");
		hql.append("pl.valor as valor_pago_by_acao_09, l.valor_total_com_desconto as valor_total_lancamento_10, \n");
		hql.append(
				"la.id as id_lancamento_acao_11, l.id as codigo_lancamento_12, l.quantidade_parcela as qtd_parcela_13, \n");
		hql.append(
				"l.tipoparcelamento as tipo_parcelamento_14, l.compra_id  as compra_id_15, la.id as id_lancamento_acao_16, \n");
		hql.append("pl.id as id_pagamento_17,");
		hql.append("pl.stt as status_provisao_18, ");
		hql.append("l.termo_id as termo_19, ");
		hql.append(" l.nota_fiscal as numero_fiscal_20, ");
		hql.append("l.statuscompra as numero_21, ");
		hql.append("l.idadiantamento as numero_adiantamento_22, ");
		hql.append("l.tipo_v4 as tipo_v4_23 ");
		
		hql.append(
				" from lancamento l join lancamento_acao la on l.id = la.lancamento_id join pagamento_lancamento pl on pl.lancamentoacao_id = la.id \n");
		hql.append(
				"where l.tipo != 'compra'   and l.statuscompra = 'CONCLUIDO' and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) \n");
		hql.append("and la.status = 0 ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			hql.append(" and pl.datapagamento between '" + sdf.format(filtro.getDataInicio()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinal()) + "' ");

		} else if (filtro.getDataInicio() != null) {
			hql.append(" and pl.datapagamento >= '" + sdf.format(filtro.getDataInicio()) + "' ");

		} else if (filtro.getDataFinal() != null) {
			hql.append(" and pl.datapagamento >= '" + sdf.format(filtro.getDataFinal()) + "' ");
		}

		if (filtro.getDataInicioEmissao() != null && filtro.getDataFinalEmissao() != null) {
			hql.append(" and l.data_emissao between '" + sdf.format(filtro.getDataInicioEmissao()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");

		} else if (filtro.getDataInicioEmissao() != null) {
			hql.append(" and l.data_emissao >= '" + sdf.format(filtro.getDataInicioEmissao()) + "' ");

		} else if (filtro.getDataFinalEmissao() != null) {
			hql.append(" and l.data_emissao >= '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");
		}

		if (filtro.getProjetoId() != null) {
			hql.append(
					" and (select p.id from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) = :projeto");
		}
		// Codigo alterado para separar busca por contas de pagador e fornecedor
		// 27/08/2018 by Christophe
		// after
		// if (filtro.getIdConta() != null) {
		// hql.append(" and (pl.contarecebedor_id = :contaa or pl.conta_id = :contaa)
		// ");
		// }
		// before
		if (filtro.getFornecedor() != null && filtro.getFornecedor().getId() != null) {
			hql.append(" and  pl.contarecebedor_id = :recebedor ");
		}
		if (filtro.getPagador() != null && filtro.getPagador().getId() != null) {
			hql.append(" and pl.conta_id = :pagador ");
		}
		// end

		if (filtro.getLancamentoID() != null) {
			hql.append(" and (l.id = :lancamento or l.idadiantamento = :lancamento)");
		}

		if (filtro.getStatusPagamento() != null) {
			hql.append(" and pl.stt = :stt ");
		}

		if (filtro.getNumeroDocumento() != "" && filtro.getNumeroDocumento() != null) {
			hql.append(" and l.nota_fiscal = :pNumeroDocumento ");
		}

		// Codigo Alterado para adicionar filtro de numero do adiantamento 03/09/2018 by
		// christophe
		if (filtro.getNumeroDocumentoAdiantemento() != "" && filtro.getNumeroDocumentoAdiantemento() != null) {
			hql.append(
					" and (l.id = :pNumeroDocumentoAdiantamento or l.idadiantamento = :pNumeroDocumentoAdiantamento)");
		}
		// end

		hql.append(" order by pl.datapagamento desc, l.data_emissao desc");

		Query query = manager.createNativeQuery(hql.toString());
		

		if (filtro.getProjetoId() != null) {
			query.setParameter("projeto", filtro.getProjetoId());
		}

		// Codigo alterado para separar busca por contas de pagador e fornecedor
		// 27/08/2018 by Christophe
		// after
		// if (filtro.getIdConta() != null) {
		// query.setParameter("contaa", filtro.getIdConta());
		// }
		// before
		if (filtro.getFornecedor() != null && filtro.getFornecedor().getId() != null) {
			query.setParameter("recebedor", filtro.getFornecedor().getId());
		}

		if (filtro.getPagador() != null && filtro.getPagador().getId() != null) {
			query.setParameter("pagador", filtro.getPagador().getId());
		}

		// END
		if (filtro.getLancamentoID() != null) {
			query.setParameter("lancamento", filtro.getLancamentoID());
		}

		if (filtro.getStatusPagamento() != null) {
			query.setParameter("stt", filtro.getStatusPagamento().toString());
		}

		if (filtro.getNumeroDocumento() != null && filtro.getNumeroDocumento() != "") {
			query.setParameter("pNumeroDocumento", filtro.getNumeroDocumento());
		}

		// Codigo Alterado para adicionar filtro de numero do adiantamento 03/09/2018 by
		// christophe
		if (filtro.getNumeroDocumentoAdiantemento() != "" && filtro.getNumeroDocumentoAdiantemento() != null) {
			query.setParameter("pNumeroDocumentoAdiantamento", Long.parseLong(filtro.getNumeroDocumentoAdiantemento()));
		}

		List<LancamentoAuxiliar> retorno = new ArrayList<LancamentoAuxiliar>();
		List<Object[]> result = query.getResultList();

		LancamentoAuxiliar lAux = new LancamentoAuxiliar();

		for (Object[] object : result) {

			lAux = new LancamentoAuxiliar();
			// lAux.setAcaoId(new Long(object[0].toString()));
			lAux.setId(new Long(object[0].toString()));
			
			lAux.setDescricao(object[6].toString());
			lAux.setFonte(object[7].toString());
			lAux.setNomeProjeto(object[8] != null ? object[8].toString() : "");
			lAux.setValorPagoAcao(object[9] != null ? new BigDecimal(object[9].toString()) : BigDecimal.ZERO);
			lAux.setValorLancamento(object[10] != null ? new BigDecimal(object[10].toString()) : BigDecimal.ZERO);
			lAux.setDataEmissao(DateConverter.converteDataSql(object[1].toString()));
			lAux.setDataPagamento(DateConverter.converteDataSql(object[2].toString()));
			lAux.setIdAcaoLancamentoId(new Long(object[16].toString()));
			lAux.setCodigoLancamento(object[0] != null ? object[0].toString() : "");
			lAux.setQuantidadeParcela(object[13] != null ? Integer.valueOf(object[13].toString()) : 1);
			lAux.setLabelFornecedor(object[4] != null ? object[4].toString() : "");
			lAux.setTipoParcelamento(TipoParcelamento.valueOf(object[14].toString()));
			lAux.setContaPagadorLbl(object[3] != null ? object[3].toString() : "");
			lAux.setContaRecebedorLbl(object[4] != null ? object[4].toString() : "");
			lAux.setCompraID(object[15] != null ? object[15].toString() : "");
			lAux.setStatusPagamento(object[5] != null ? Integer.valueOf(object[5].toString()) : 1);
			lAux.setIdPagamento(new Long(object[17].toString()));
			lAux.setStatus(object[18].toString());
			lAux.setIdTermo(Util.getNullValue(object[19], new Long(0)));
			lAux.setNumeroDocumento(Util.getNullValue(object[20], ""));
			lAux.setStatusCompra(object[21] != null ? StatusCompra.valueOf(object[21].toString()) : null);
			lAux.setIdAdiantamentoDePrestacao(object[22] != null ? Long.valueOf(object[22].toString()) : null);
			lAux.setTipov4(object[23] != null ? object[23].toString() : "");
			
			retorno.add(lAux);
		}

		return retorno;
	}

	public List<PagamentoPE> getPagamentosPEMODE01(Filtro filtro) {
		StringBuilder hql = new StringBuilder(" select ");
		preparaHqlPagamentosPEMODE01(filtro, hql);
		Query query = manager.createNativeQuery(hql.toString());
		
		prepararQueryPagamentoPEMODE01(query, filtro);
		List<PagamentoPE> pagamentos = new ArrayList<>();
		List<Object[]> result = query.getResultList();

		for (Object[] objects : result) {
			PagamentoPE p = new PagamentoPE(objects, "");
			pagamentos.add(p);
		}

		return pagamentos;
	}

	public List<PagamentoPE> getPagamentosPE(Filtro filtro) {
		StringBuilder hql = new StringBuilder(" select ");
		preparaHqlPagamentosPE(filtro, hql);
		Query query = manager.createNativeQuery(hql.toString());
		prepararQueryPagamentoPE(query, filtro);
		List<PagamentoPE> pagamentos = new ArrayList<>();
		List<Object[]> result = query.getResultList();
		for (Object[] objects : result) {
			PagamentoPE p = new PagamentoPE(objects);
			pagamentos.add(p);
		}

		return pagamentos;
	}

	public List<ContaBancaria> getAllConta(String s) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW ContaBancaria(c.id,c.nomeConta) FROM  ContaBancaria AS c where (lower(c.nomeConta) like lower(:nome)   ");
		jpql.append(
				" or lower(c.cnpj) like lower(:nome) or lower(c.cpf) like lower(:nome)) and (c.status = :status or c.status is null ) ");
		jpql.append(" order by c.nomeConta ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		query.setParameter("status", StatusConta.ATIVA);
		query.setMaxResults(20);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<ContaBancaria>();
	}

	public List<ContaBancaria> getAllContaListaModal(String s) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW ContaBancaria(c.id,c.nomeConta) FROM  ContaBancaria AS c where lower(c.nomeConta) like lower(:nome) ");
		jpql.append(" order by c.nomeConta ");

		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "" + s + "%");
		// query.setMaxResults(20);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<ContaBancaria>();
	}

	public LancamentoAvulso getLancamentoAvulsoByNotaFiscal(String nf, Long id, Long idConta) {
		String jpql = "from LancamentoAvulso av where lower(av.notaFiscal) = lower(:nota) and av.contaRecebedor.id = :idConta and av.id != :id";
		Query query = manager.createQuery(jpql);
		query.setParameter("nota", nf);
		query.setParameter("id", id);
		query.setParameter("idConta", idConta);
		query.setMaxResults(1);
		return query.getResultList().size() > 0 ? (LancamentoAvulso) query.getResultList().get(0) : null;

	}

	public SolicitacaoPagamento getSolicitacaoPagamentoByNotaFiscal(String nf, Long id, Long idConta) {
		String jpql = "from SolicitacaoPagamento av where lower(av.notaFiscal) = lower(:nota) and av.contaRecebedor.id = :idConta and av.id != :id";
		Query query = manager.createQuery(jpql);
		query.setParameter("nota", nf);
		query.setParameter("id", id);
		query.setParameter("idConta", idConta);
		query.setMaxResults(1);
		return query.getResultList().size() > 0 ? (SolicitacaoPagamento) query.getResultList().get(0) : null;

	}
	
	
	public SolicitacaoPagamento verificarNotaFiscal(String nf, Long id, Long idConta) {
		String jpql = "from SolicitacaoPagamento av where lower(av.notaFiscal) = lower(:nota) and av.contaRecebedor.id = :idConta and av.id != :id";
		Query query = manager.createQuery(jpql);
		query.setParameter("nota", nf);
		query.setParameter("id", id);
		query.setParameter("idConta", idConta);
		query.setMaxResults(1);
		return query.getResultList().size() > 0 ? (SolicitacaoPagamento) query.getResultList().get(0) : null;

	}

	public void preparaHqlPagamentosPEMODE01(Filtro filtro, StringBuilder hql) {

		hql.append(
				" DISTINCT((select cb.nome_conta from conta_bancaria cb where  cb.id  = pl.conta_id)) as pagador_00,  ");
		hql.append(
				" (select cont.nome_conta from conta_bancaria cont where cont.id = pl.contarecebedor_id LIMIT 1) as recebedor_01, ");
		hql.append(" pl.datapagamento as data_pagto_02, ");
		hql.append(" to_char(pl.datapagamento,'DD-MM-YYYY') as data_pagamento_03, ");
		hql.append(" to_char(l.data_emissao,'DD-MM-YYYY') as data_emissao_04,  ");
		hql.append(" l.id as id_lancamento_05, ");
		hql.append(" l.descricao as descricao_06,");
		hql.append(" pl.numerodaparcela || '/' || pl.quantidade_parcela as parcela_07,");
		hql.append(
				" (select sum(pp.valor) from pagamento_lancamento pp where (select lla.lancamento_id from lancamento_acao lla where pp.lancamentoacao_id = lla.id ) = l.id and pp.conta_id = pl.conta_id and pp.datapagamento = pl.datapagamento) as valor_08");
		hql.append(",pl.stt as status_9 ");
		hql.append(",l.numerodocumento as documento_10 ");
		hql.append(",pl.conta_id as conta_id_11");
		hql.append(
				",(select ppl.id from pagamento_lancamento ppl where ppl.datapagamento = pl.datapagamento and (select la.lancamento_id from lancamento_acao la where la.id = ppl.lancamentoacao_id) = l.id and ppl.conta_id = pl.conta_id LIMIT 1) ");
		hql.append("as id_pagamento_12 ");
		hql.append(" from lancamento l join lancamento_acao la on l.id = la.lancamento_id ");
		hql.append(
				" join pagamento_lancamento pl on pl.lancamentoacao_id = la.id where l.tipo != 'compra' and l.tipo != 'lanc_av'  ");
		hql.append(" and l.statuscompra = 'CONCLUIDO' and l.versionlancamento = 'MODE01'  ");

		hql.append(" and (select laa.status from lancamento_acao laa where laa.lancamento_id = l.id LIMIT 1)  =  0   ");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			hql.append(" and pl.datapagamento between '" + sdf.format(filtro.getDataInicio()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinal()) + "' ");

		} else if (filtro.getDataInicio() != null) {
			hql.append(" and pl.datapagamento >= '" + sdf.format(filtro.getDataInicio()) + "' ");

		} else if (filtro.getDataFinal() != null) {
			hql.append(" and pl.datapagamento >= '" + sdf.format(filtro.getDataFinal()) + "' ");
		}

		if (filtro.getDataInicioEmissao() != null && filtro.getDataFinalEmissao() != null) {
			hql.append(" and pl.dataemissao between '" + sdf.format(filtro.getDataInicioEmissao()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");

		} else if (filtro.getDataInicioEmissao() != null) {
			hql.append(" and pl.dataemissao >= '" + sdf.format(filtro.getDataInicioEmissao()) + "' ");

		} else if (filtro.getDataFinalEmissao() != null) {
			hql.append(" and pl.dataemissao >= '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");
		}

		// if (filtro.getAcaoId() != null) {
		// hql.append(" and acc.id = :acao");
		// }

		if (filtro.getProjetoId() != null) {
			hql.append(" and proj.id = :projeto");
		}

		if (filtro.getFornecedorID() != null) {
			hql.append(" and l.fornecedor_id = :fornecedor");
		}

		if (filtro.getLancamentoID() != null) {
			hql.append(" and l.id = :lancamento");
		}

		if (filtro.getNumeroDocumento() != null && !filtro.getNumeroDocumento().equals("")) {
			hql.append(" and l.numerodocumento = :documento ");
		}

		if (filtro.getStatusPagamento() != null) {
			hql.append(" and pl.stt = :stt");
		}

		if (filtro.getIdConta() != null) {
			hql.append(" and (pl.conta_id = :ccb or pl.contarecebedor_id = :ccb) ");
		}

		hql.append(" order by pl.datapagamento");
	}

	public void preparaHqlPagamentosPE(Filtro filtro, StringBuilder hql) {

		hql.append(" to_char(pl.dataemissao,'DD-MM-YYYY')  as data_emissao, ");
		hql.append(" to_char(pl.datapagamento,'DD-MM-YYYY') as data_pagamento,");
		hql.append(" acc.codigo as acao,");

		hql.append(" (select font.nome from fonte_pagadora font where font.id = la.fontepagadora_id) as fonte,");
		// hql.append(" (select font.nome from fonte_pagadora font where id =
		// (select orc.fonte_id from orcamento orc where id = la.orcamento_id))
		// as fonte,");

		hql.append(" pl.stt,");
		hql.append(" pl.valor as valor, ");
		hql.append(" cb.nome_conta as conta,");
		hql.append(" l.id as lancamento,");
		hql.append(" pl.numerodaparcela || '/' || pl.quantidade_parcela as parcela,");
		hql.append(" (select f.nome_fantasia from  fornecedor f where f.id = l.fornecedor_id) as fornecedor,");
		hql.append(" (select loc.mascara from  localidade  loc where loc.id = l.localidade_id) as localidade,");
		hql.append(" pl.id as id_pagamento,");
		hql.append(
				"  (select count.nome_conta from conta_bancaria count where count.id = l.contarecebedor_id) as conta_recebedor,");

		hql.append(" l.compra_id as compra_id,");
		hql.append(" l.numerodocumento as numero_documento,");
		hql.append(" l.descricao as descricao");

		hql.append(" from conta_bancaria cb join ");
		hql.append(" pagamento_lancamento pl on pl.conta_id = cb.id");
		hql.append(" join lancamento_acao la on la.id = pl.lancamentoacao_id ");
		hql.append(" join lancamento l on la.lancamento_id = l.id ");
		hql.append(" join acao acc on la.acao_id = acc.id");
		// hql.append(" join projeto proj on la.projeto_id = proj.id ");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			hql.append(" and pl.datapagamento between '" + sdf.format(filtro.getDataInicio()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinal()) + "' ");

		} else if (filtro.getDataInicio() != null) {
			hql.append(" and pl.datapagamento >= '" + sdf.format(filtro.getDataInicio()) + "' ");

		} else if (filtro.getDataFinal() != null) {
			hql.append(" and pl.datapagamento >= '" + sdf.format(filtro.getDataFinal()) + "' ");
		}

		if (filtro.getDataInicioEmissao() != null && filtro.getDataFinalEmissao() != null) {
			hql.append(" and pl.dataemissao between '" + sdf.format(filtro.getDataInicioEmissao()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");

		} else if (filtro.getDataInicioEmissao() != null) {
			hql.append(" and pl.dataemissao >= '" + sdf.format(filtro.getDataInicioEmissao()) + "' ");

		} else if (filtro.getDataFinalEmissao() != null) {
			hql.append(" and pl.dataemissao >= '" + sdf.format(filtro.getDataFinalEmissao()) + "' ");
		}

		if (filtro.getAcaoId() != null) {
			hql.append(" and acc.id = :acao");
		}

		if (filtro.getProjetoId() != null) {
			hql.append(" and acc.projeto_id = :projeto");
		}

		if (filtro.getFornecedorID() != null) {
			hql.append(" and l.fornecedor_id = :fornecedor");
		}

		if (filtro.getLancamentoID() != null) {
			hql.append(" and l.id = :lancamento");
		}

		if (filtro.getNumeroDocumento() != null && !filtro.getNumeroDocumento().equals("")) {
			hql.append(" and l.numerodocumento = :documento ");
		}

		if (filtro.getStatusPagamento() != null) {
			hql.append(" and pl.stt = :stt");
		}

		if (filtro.getIdConta() != null) {
			hql.append(" and (cb.id = :ccb or l.contarecebedor_id = :ccb) ");
		}

		hql.append(" order by pl.datapagamento");
	}

	public void prepararQueryPagamentoPE(Query query, Filtro filtro) {

		if (filtro.getAcaoId() != null) {
			query.setParameter("acao", filtro.getAcaoId());
		}

		if (filtro.getProjetoId() != null) {
			query.setParameter("projeto", filtro.getProjetoId());
		}

		if (filtro.getFornecedorID() != null) {
			query.setParameter("fornecedor", filtro.getFornecedorID());
		}

		if (filtro.getLancamentoID() != null) {
			query.setParameter("lancamento", filtro.getLancamentoID());
		}

		if (filtro.getStatusPagamento() != null) {
			query.setParameter("stt", filtro.getStatusPagamento().name());
		}

		if (filtro.getIdConta() != null) {
			query.setParameter("ccb", filtro.getIdConta());
		}

		if (filtro.getNumeroDocumento() != null && !filtro.getNumeroDocumento().equals("")) {
			query.setParameter("documento", filtro.getNumeroDocumento());
		}
	}

	public void prepararQueryPagamentoPEMODE01(Query query, Filtro filtro) {

		// if (filtro.getAcaoId() != null) {
		// query.setParameter("acao", filtro.getAcaoId());
		// }

		// if (filtro.getProjetoId() != null) {
		// query.setParameter("projeto", filtro.getProjetoId());
		// }

		if (filtro.getFornecedorID() != null) {
			query.setParameter("fornecedor", filtro.getFornecedorID());
		}

		if (filtro.getLancamentoID() != null) {
			query.setParameter("lancamento", filtro.getLancamentoID());
		}

		if (filtro.getStatusPagamento() != null) {
			query.setParameter("stt", filtro.getStatusPagamento().name());
		}

		if (filtro.getIdConta() != null) {
			query.setParameter("ccb", filtro.getIdConta());
		}

		if (filtro.getNumeroDocumento() != null && !filtro.getNumeroDocumento().equals("")) {
			query.setParameter("documento", filtro.getNumeroDocumento());
		}
	}

	public BigDecimal getTarifa() {
		String jpql = "select c from Configuracao c ";
		Query query = manager.createQuery(jpql);
		Configuracao config =  (Configuracao) query.getSingleResult();
		if(config.getValorTarifaBancaria() == null)
			return new BigDecimal("0");
		else
			return config.getValorTarifaBancaria();
	}

	public void estornar(Long id) {
			String hql = "update pagamento_lancamento set stt = 'PROVISIONADO' where id = :id ";
			Query query = manager.createNativeQuery(hql);
			query.setParameter("id", id);
			query.executeUpdate();
		
	}

	
	public Boolean salvarLog(Log log) {
		try {
			manager.merge(log);
			return true;
		}catch (Exception e) {
			return false;
		}
		
	}
	
//	public verificaEntrada(LancamentoAcao lancamentoAcao) {
//		
//	}

}
