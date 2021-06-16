package repositorio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Adiantamento;
import model.Banco;
import model.ContaBancaria;
import model.Lancamento;
import model.LancamentoAcao;
import util.ArquivoLancamento;
import util.Filtro;

public class AutorizacaoRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public AutorizacaoRepositorio() {
	}

	public Lancamento getLancamentoById(Long id) {
		return manager.find(Lancamento.class, id);
	}

	public Boolean desautorizar(List<Long> identificacoes, Long idCol) {

		try {

			StringBuilder stb = new StringBuilder();
			stb.append("update lancamento l set statuscompra = (case when statuscompra = 'CONCLUIDO' then 'CONCLUIDO' ");
			stb.append("when statuscompra = 'PENDENTE_APROVACAO' then (select ad.status_before from aprovadordocumento ad ");
			stb.append("where ad.documento = l.tipo_v4 and ad.colaborador = :id_colaborador and ad.gestao = l.gestao_id and ad.ordem = 1) ");
			stb.append("ELSE ");
			stb.append("(select ad.status_before from aprovadordocumento ad where ad.documento = l.tipo_v4 and ad.colaborador = :id_colaborador ");
			stb.append("and ad.gestao = l.gestao_id and ad.ordem = 2) END), statusadiantamento = (case when statuscompra = 'CONCLUIDO' THEN ");
			stb.append("(select ad.status_before from aprovadordocumento ad where ad.documento = l.tipo_v4 and ad.colaborador = :id_colaborador and ad.gestao = l.gestao_id and ad.ordem = 2) ");
			stb.append("ELSE l.statusadiantamento END) where id in (:identifys); ");
			
			Query query = manager.createNativeQuery(stb.toString());
			query.setParameter("id_colaborador", idCol);
			query.setParameter("identifys", identificacoes);
			int updateCount = query.executeUpdate();
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;

	}

	public Boolean autorizar(List<Long> identificacoes, Long idCol) {

		try {

		
			StringBuilder stb = new StringBuilder();
			stb.append("update lancamento l set statuscompra = (case when statuscompra = 'CONCLUIDO' then 'CONCLUIDO' ");
			stb.append("when statuscompra = 'PENDENTE_APROVACAO' then (select ad.status from aprovadordocumento ad ");
			stb.append("where ad.documento = l.tipo_v4 and ad.colaborador = :id_colaborador and ad.gestao = l.gestao_id and ad.ordem = 1) ");
			stb.append("ELSE ");
			stb.append("(select ad.status from aprovadordocumento ad where ad.documento = l.tipo_v4 and ad.colaborador = :id_colaborador ");
			stb.append("and ad.gestao = l.gestao_id and ad.ordem = 2) END), ");
			
			stb.append("statusadiantamento = (case when statuscompra = 'CONCLUIDO' THEN ");
			stb.append("(select ad.status from aprovadordocumento ad where ad.documento = l.tipo_v4 and ad.colaborador = :id_colaborador and ad.gestao = l.gestao_id and ad.ordem = 2) ");
			stb.append("ELSE l.statusadiantamento END) where id in (:identifys); ");
			
			
			Query query = manager.createNativeQuery(stb.toString());
			query.setParameter("id_colaborador", idCol);
			query.setParameter("identifys", identificacoes);
			int updateCount = query.executeUpdate();
		

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}
	
	
	public Boolean autorizar(Long identificacao, Long idCol) {

		try {

			StringBuilder stb = new StringBuilder(
					" update lancamento l set statuscompra = (select ad.status from aprovadordocumento ad ");
			stb.append(
					"where ad.documento = l.tipo_v4 and ad.colaborador = :id_colaborador and ad.gestao = l.gestao_id) where id = :identify");
			Query query = manager.createNativeQuery(stb.toString());
			query.setParameter("id_colaborador", idCol);
			query.setParameter("identify", identificacao);
			int updateCount = query.executeUpdate();
			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	public boolean verificaSolicitacoes(Long id) {

		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT count(l.id) ");

		jpql.append("from lancamento l where ");

		jpql.append(" l.versionlancamento = 'MODE01' ");

		jpql.append(" and ");
		
		jpql.append("(");

		jpql.append(
				"((:id in (select ad.colaborador from aprovadordocumento ad where ad.gestao = l.gestao_id and ad.ordem = 1 ");
		
		jpql.append(" and ad.documento = l.tipo_v4");
		jpql.append(" ))");
		jpql.append(" and l.statuscompra = 'PENDENTE_APROVACAO') ");
					
		jpql.append(" or ");

		jpql.append(
				"((:id in (select ad.colaborador from aprovadordocumento ad where ad.gestao = l.gestao_id and ad.ordem = 2 ");
		
		jpql.append(" and ad.documento = l.tipo_v4");
		jpql.append(" ))");
		jpql.append(" and (l.statuscompra = 'PENDENTE_APROVACAO_TWO' or l.statusadiantamento = 'EM_ANALISE')) ");

		jpql.append(")");


		Query query = this.manager.createNativeQuery(jpql.toString());
		query.setParameter("id", id);

		Integer result = Integer.valueOf(query.getSingleResult().toString());

		return result > 0;
	}

	public List<Long> getAusentes(Long id) {

		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT l.id as tipo, l.id,");
		jpql.append(" order by l.data_emissao desc");

		Query query = this.manager.createNativeQuery(jpql.toString());
		query.setParameter("id", id);

		List<Object[]> result = query.getResultList();

		List<Long> ausentes = new ArrayList<>();

		try {
			for (Object[] object : result) {
				Lancamento lancamento = new Lancamento();
				lancamento.setTipov4(object[0].toString());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ausentes;
	}

	public List<Lancamento> getSolicitacoes(Long id) {

		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT ");
		jpql.append("CASE WHEN (l.tipo_v4 = 'SA' and l.statuscompra = 'CONCLUIDO') ");
		jpql.append("then 'PCA' ");
		jpql.append("ELSE l.tipo_v4 END as tipo, ");
		jpql.append("l.id, ");
		jpql.append("to_char(l.data_emissao, 'dd/MM/yyyy'), ");
		jpql.append("CASE ");
		jpql.append(
				"WHEN (l.tipo_v4 = 'SC') THEN (select c.nome from categoria_despesa c where c.id = l.categoriadespesaclass_id) ");
		jpql.append(
				"WHEN (l.tipo_v4 = 'SP') THEN (select c.nome from categoria_financeira c where c.id = l.categoriafinanceira_id) ");
		jpql.append("WHEN (l.tipo_v4 = 'SA') THEN '' ");
		jpql.append("WHEN (l.tipo_v4 = 'SD') THEN 'Diária' ");
		jpql.append("WHEN (l.tipo_v4 = 'SV') THEN 'Viagem' ");
		jpql.append(
				"WHEN (l.tipo_v4 = 'PC') THEN (select ip.categoria from item_pedido ip where ip.pedido_id = l.id LIMIT 1) ");
		jpql.append("END as categoria, ");
		jpql.append("CASE ");
		jpql.append("WHEN (l.tipo_v4 = 'SC') THEN '' ");
		jpql.append(
				"WHEN (l.tipo_v4 = 'SP') THEN (select f.nome_fantasia from fornecedor f where f.id = l.fornecedor_id) ");
		jpql.append(
				"WHEN (l.tipo_v4 = 'SA' or l.tipo_v4 = 'SD') THEN (select c.nome_conta from conta_bancaria c where c.id = l.contarecebedor_id) ");
		jpql.append("WHEN (l.tipo_v4 = 'SV') THEN '' ");
		jpql.append(
				"WHEN (l.tipo_v4 = 'PC') THEN (select f.nome_fantasia from fornecedor f where f.id = l.fornecedor_id) ");
		jpql.append("END as fornecedor, ");
		jpql.append("l.descricao as descricao,");
		jpql.append("l.valor_total_com_desconto as valor, l.compra_id ");
//		jpql.append("l.compra_id ");

		jpql.append("from lancamento l where ");

		jpql.append(" l.versionlancamento = 'MODE01' ");

		jpql.append(" and ");
		
		jpql.append("(");

		jpql.append(
				"((:id in (select ad.colaborador from aprovadordocumento ad where ad.gestao = l.gestao_id and ad.ordem = 1 ");
		
		jpql.append(" and ad.documento = l.tipo_v4");
		jpql.append(" ))");
		jpql.append(" and l.statuscompra = 'PENDENTE_APROVACAO') ");
		
		jpql.append(" or ");

		jpql.append(
				"((:id in (select ad.colaborador from aprovadordocumento ad where ad.gestao = l.gestao_id and ad.ordem = 2 ");
		
		jpql.append(" and ad.documento = l.tipo_v4");
		jpql.append(" ))");
		jpql.append(" and (l.statuscompra = 'PENDENTE_APROVACAO_TWO' or l.statusadiantamento = 'EM_ANALISE')) ");

		jpql.append(")");

		jpql.append(" order by l.data_emissao desc");

		Query query = this.manager.createNativeQuery(jpql.toString());
		query.setParameter("id", id);

		List<Object[]> result = query.getResultList();

		List<Lancamento> lancamentos = new ArrayList<>();

		try {
			for (Object[] object : result) {
				Lancamento lancamento = new Lancamento();
				lancamento.setTipov4(object[0].toString());
				lancamento.setId(new Long(object[1].toString()));
				lancamento.setDtEmissao(object[2].toString());
				lancamento.setNomeCategoria(object[3] != null ? object[3].toString() : "");
				lancamento.setNomeFornecedor(object[4] != null ? object[4].toString() : "");
				lancamento.setDescricao(object[5] != null ? object[5].toString() : "");
				lancamento.setValorTotalComDesconto(
						object[6] != null ? new BigDecimal(object[6].toString()) : BigDecimal.ZERO);

				if (object[7] != null) {
					lancamento.setIdCompra(new Long(object[7].toString()));
				}

				lancamentos.add(lancamento);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lancamentos;
	}

	// RESUMO PARA O DETALHAMENTO
	public Lancamento getLancamentoByIdResumo(Long id) {
		StringBuilder jpql = new StringBuilder("SELECT NEW Lancamento(l.id, l.gestao.nome, l.solicitante.nome, ");

		jpql.append(
				"l.dataEmissao, l.statusCompra, forn.nomeFantasia, l.descricao, l.observacao, loc.mascara, l.valorTotalComDesconto, l.tipov4, comp.id) from Lancamento l ");
		jpql.append("left join l.fornecedor forn ");
		jpql.append("left join l.localidade loc ");
		jpql.append("left join l.compra comp ");
		jpql.append(" where l.id = :id");

		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", id);

		return (Lancamento) query.getResultList().get(0);
	}

	// ARQUIVOS DE LANÇAMENTO - ANEXOS
	public List<ArquivoLancamento> getArquivosByLancamento(Long id) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW ArquivoLancamento(aq.nome, aq.conteudo, aq.path) FROM ArquivoLancamento aq where aq.lancamento.id =  :id  ");
		Query query = this.manager.createQuery(jpql.toString());
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}
	
	public List<ArquivoLancamento> getArquivosByLancamento(Long id, Long idCompra) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW ArquivoLancamento(aq.nome, aq.conteudo, aq.path) FROM ArquivoLancamento aq where aq.lancamento.id =  :id  ");
		jpql.append("or ");
		jpql.append("aq.lancamento.id =  :id_compra ");
		Query query = this.manager.createQuery(jpql.toString());
		query.setParameter("id", id);
		query.setParameter("id_compra", idCompra);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	// RECURSOS QUE CUSTEIAM O LANÇAMENTO
	public List<LancamentoAcao> getRecursos(Long id) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW LancamentoAcao(la.projetoRubrica.rubricaOrcamento.orcamento.titulo, ");
		jpql.append("la.projetoRubrica.projeto.nome,");
		jpql.append("la.projetoRubrica.rubricaOrcamento.rubrica.nome, ");
		jpql.append("la.valor ");
		jpql.append(") ");
		jpql.append("FROM LancamentoAcao la where la.lancamento.id = :id ");
		Query query = this.manager.createQuery(jpql.toString());
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

}
