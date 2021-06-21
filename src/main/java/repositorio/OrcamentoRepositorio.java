package repositorio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.*;
import util.DataUtil;
import util.Filtro;
import util.Util;

public class OrcamentoRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	private TipoParcelamento tipoParcelamento;

	public OrcamentoRepositorio() {
	}

	public OrcamentoRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public RubricaOrcamento salvarRubricaOrcamento(RubricaOrcamento rubricaOrcamento) {
		return this.manager.merge(rubricaOrcamento);
	}

	public LancamentoAcao findByIdLancamentoAcao(Long id) {
		return this.manager.find(LancamentoAcao.class, id);
	}

	public void removerLinhasDeOrcamento(Long id) {
		String hql = "delete from rubrica_orcamento where orcamento_id = :id";
		Query query = this.manager.createNativeQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	public BigDecimal getTotalReceita(Long id) {
		Query q = this.manager.createNativeQuery(getSQLdespesasreceitas());
		q.setParameter("projeto", id);
		q.setParameter("tipo", "RECEITA");
		BigDecimal totalDespesa = (BigDecimal) q.getSingleResult();
		return totalDespesa;
	}

	public BigDecimal getTotalDespesa(Long id) {
		Query q = this.manager.createNativeQuery(getSQLdespesasreceitas());
		q.setParameter("projeto", id);
		q.setParameter("tipo", "DESPESA");
		BigDecimal totalDespesa = (BigDecimal) q.getSingleResult();
		return totalDespesa;
	}

	public BigDecimal getTotalReceitaPorRubrica(Long id) {
		Query q = this.manager.createNativeQuery(getSQLdespesasreceitasPorRubrica());
		q.setParameter("rubrica", id);
		q.setParameter("tipo", "RECEITA");
		BigDecimal totalDespesa = (BigDecimal) q.getSingleResult();
		return totalDespesa;
	}

	public BigDecimal getTotalDespesaPorRubrica(Long id) {
		Query q = this.manager.createNativeQuery(getSQLdespesasreceitasPorRubrica());
		q.setParameter("rubrica", id);
		q.setParameter("tipo", "DESPESA");
		BigDecimal totalDespesa = (BigDecimal) q.getSingleResult();
		return totalDespesa;
	}

	public String getSQLdespesasreceitasPorRubrica() {

		StringBuilder sql = new StringBuilder("select ");
		sql.append("sum(pl.valor) ");

		sql.append(" from lancamento l join lancamento_acao la on l.id = la.lancamento_id \n");
		sql.append(
				"join pagamento_lancamento pl on pl.lancamentoacao_id = la.id where l.tipo != 'compra' and  l.tipolancamento != 'reemb_conta'     \n");
		// sql.append(" and (l.statuscompra = 'CONCLUIDO' ");
		sql.append(" and (l.statuscompra = 'CONCLUIDO' or l.statuscompra = 'N_INCIADO') ");

		sql.append(" and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) \n");

		// sql.append(" and (select p.id from projeto p where p.id = (select
		// pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) =
		// :projeto \n");

		sql.append(" AND CASE  when l.idadiantamento is not null then \n");
		sql.append(" (((select ll.statusadiantamento from lancamento ll \n");
		sql.append(
				" where ll.id = l.idadiantamento) = 'VALIDADO') and l.tipolancamento != 'dev' and l.tipolancamento != 'reenb')   else 1 = 1 end \n");
		sql.append(" AND CASE  when l.tipolancamento  = 'ad' then \n");
		sql.append(" (l.statusadiantamento  != 'VALIDADO')  else 1 = 1 end \n");
		sql.append(" and l.depesareceita = :tipo \n");
		sql.append(" and la.projetorubrica_id = :rubrica");
		sql.append(" and l.tipolancamento != 'reemb_conta' ");

		return sql.toString();

	}

	public String getSQLdespesasreceitas() {

		StringBuilder sql = new StringBuilder("select ");
		sql.append("sum(pl.valor) ");

		sql.append(" from lancamento l join lancamento_acao la on l.id = la.lancamento_id \n");
		sql.append(
				"join pagamento_lancamento pl on pl.lancamentoacao_id = la.id where l.tipo != 'compra'   and l.statuscompra = 'CONCLUIDO' and (l.versionlancamento = 'MODE01' or \n");
		sql.append("pl.reclassificado is true)  and (select p.id from \n");
		sql.append(
				"projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id))  = :projeto \n");
		sql.append("AND CASE  when l.idadiantamento is not null then \n");
		sql.append("(((select ll.statusadiantamento from lancamento ll \n");
		sql.append(
				" where ll.id = l.idadiantamento) = 'VALIDADO') and l.tipolancamento != 'dev' and l.tipolancamento != 'reenb')   else 1 = 1 end \n");
		sql.append("AND CASE  when l.tipolancamento  = 'ad' then \n");
		sql.append("(l.statusadiantamento  != 'VALIDADO')  else 1 = 1 end \n");
		sql.append("and l.depesareceita = :tipo \n");

		return sql.toString();

	}

	public RubricaOrcamento getRubricaDespesasAdm() {
		return manager.find(RubricaOrcamento.class, new Long(64));
	}

	public Long existeProjeto(Long id) {
		String jpql = "SELECT count(o) FROM OrcamentoProjeto o where o.orcamento.id = :id";
		Query q = this.manager.createQuery(jpql);
		q.setParameter("id", id);
		Long count = (Long) q.getSingleResult();
		return count;
	}

	public DoacaoEfetiva salvarDoacaoEfetiva(DoacaoEfetiva doacao, User usuario) {

		doacao = this.manager.merge(doacao);
		doacao.setNumeroDocumento(doacao.getId().toString());
		LogStatus log = new LogStatus();
		log.setUsuario(usuario);
		log.setData(new Date());
		log.setSigla(doacao.getGestao().getSigla());
		if (doacao.getId() != null) {
			log.setSiglaPrivilegio("EDIT");
		} else {
			log.setSiglaPrivilegio("NEW");
			log.setStatusLog(StatusCompra.N_INCIADO);
		}

		log.setLancamento(doacao);
		this.manager.merge(log);

		if (doacao.getId() != null) {
			this.manager.createNativeQuery(
					"delete from pagamento_lancamento as pl where (select lla.lancamento_id from lancamento_acao as lla "
							+ "where lla.id = pl.lancamentoacao_id)  = :id")
					.setParameter("id", doacao.getId()).executeUpdate();

		}

		for (LancamentoAcao lc : doacao.getLancamentosAcoes()) {
			lc.setDespesaReceita(doacao.getDepesaReceita());
			lc.setLancamento(doacao);
			lc.setStatus(StatusPagamento.N_VALIDADO);
			lc.setTipoLancamento(doacao.getTipoLancamento());
			lc = this.manager.merge(lc);

			PagamentoLancamento pl = new PagamentoLancamento();
			pl.setConta(doacao.getContaPagador());
			pl.setContaRecebedor(doacao.getContaRecebedor());
			pl.setDataEmissao(doacao.getDataEmissao());
			pl.setDataPagamento(doacao.getDataPagamento());
			pl.setLancamentoAcao(lc);
			pl.setQuantidadeParcela(doacao.getQuantidadeParcela());
			pl.setStt(StatusPagamentoLancamento.PROVISIONADO);
			pl.setTipoParcelamento(doacao.getTipoParcelamento());
			pl.setValor(lc.getValor());
			pl.setTipoLancamento(doacao.getTipoLancamento());
			pl.setDespesaReceita(doacao.getDepesaReceita());

			if (pl.getTipoParcelamento().compareTo(TipoParcelamento.PARCELA_UNICA) == 0) {
				pl.setQuantidadeParcela(1);
			}

			Date dataBase = pl.getDataPagamento();
			tipoParcelamento = pl.getTipoParcelamento();
			BigDecimal totalParcelado = BigDecimal.ZERO;

			for (int i = 1; i <= pl.getQuantidadeParcela(); i++) {
				PagamentoLancamento pagto = new PagamentoLancamento(pl, i);

				if (i == 1) {
					pagto.setDataPagamento(pl.getDataPagamento());
				} else {
					pagto.setDataPagamento(DataUtil.setarData(dataBase, tipoParcelamento));

					if (i == pl.getQuantidadeParcela()) {
						BigDecimal total = pl.getValor();
						BigDecimal resto = total.subtract(totalParcelado);
						pagto.setValor(resto);
					}

				}

				totalParcelado = totalParcelado.add(pagto.getValor());

				this.manager.merge(pagto);
				dataBase = pagto.getDataPagamento();
			}
		}

		// return (SolicitacaoPagamento) lancamento;
		return doacao;
	}

	public BaixaAplicacao atualizarBaixa(BaixaAplicacao baixaAplicacao){
		baixaAplicacao = this.manager.merge(baixaAplicacao);
		return baixaAplicacao;
	}

	public BaixaAplicacao salvarBaixaAplicacao(BaixaAplicacao baixaAplicacao, User usuario) {

		baixaAplicacao = this.manager.merge(baixaAplicacao);
		baixaAplicacao.setNumeroDocumento(baixaAplicacao.getId().toString());
		LogStatus log = new LogStatus();
		log.setUsuario(usuario);
		log.setData(new Date());
		log.setSigla(baixaAplicacao.getGestao().getSigla());
		if (baixaAplicacao.getId() != null) {
			log.setSiglaPrivilegio("EDIT");
		} else {
			log.setSiglaPrivilegio("NEW");
			log.setStatusLog(StatusCompra.N_INCIADO);
		}

		log.setLancamento(baixaAplicacao);
		this.manager.merge(log);

		if (baixaAplicacao.getId() != null)
			this.manager.createNativeQuery(" delete \n" +
					"from pagamento_lancamento \n" +
					"where id in (select pl.id from pagamento_lancamento pl\n" +
					"join lancamento_acao la on la.id = pl.lancamentoacao_id\n" +
					"where la.lancamento_id = :id)")
					.setParameter("id", baixaAplicacao.getId()).executeUpdate();

		for (LancamentoAcao lc : baixaAplicacao.getLancamentosAcoes()) {
			lc.setDespesaReceita(baixaAplicacao.getDepesaReceita());
			lc.setLancamento(baixaAplicacao);
			lc.setStatus(StatusPagamento.N_VALIDADO);
			lc.setTipoLancamento(baixaAplicacao.getTipoLancamento());
			lc = this.manager.merge(lc);

			PagamentoLancamento pl = new PagamentoLancamento();
			pl.setConta(baixaAplicacao.getContaPagador());
			pl.setContaRecebedor(baixaAplicacao.getContaRecebedor());
			pl.setDataEmissao(baixaAplicacao.getDataEmissao());
			pl.setDataPagamento(baixaAplicacao.getDataPagamento());
			pl.setLancamentoAcao(lc);
			pl.setQuantidadeParcela(baixaAplicacao.getQuantidadeParcela());
			pl.setStt(StatusPagamentoLancamento.PROVISIONADO);
			pl.setTipoParcelamento(baixaAplicacao.getTipoParcelamento());
			pl.setValor(lc.getValor());
			pl.setTipoLancamento(baixaAplicacao.getTipoLancamento());
			pl.setDespesaReceita(baixaAplicacao.getDepesaReceita());

			if (pl.getTipoParcelamento().compareTo(TipoParcelamento.PARCELA_UNICA) == 0) {
				pl.setQuantidadeParcela(1);
			}

			Date dataBase = pl.getDataPagamento();
			tipoParcelamento = pl.getTipoParcelamento();
			BigDecimal totalParcelado = BigDecimal.ZERO;

			for (int i = 1; i <= pl.getQuantidadeParcela(); i++) {
				PagamentoLancamento pagto = new PagamentoLancamento(pl, i);

				if (i == 1) {
					pagto.setDataPagamento(pl.getDataPagamento());
				} else {
					pagto.setDataPagamento(DataUtil.setarData(dataBase, tipoParcelamento));

					if (i == pl.getQuantidadeParcela()) {
						BigDecimal total = pl.getValor();
						BigDecimal resto = total.subtract(totalParcelado);
						pagto.setValor(resto);
					}

				}

				totalParcelado = totalParcelado.add(pagto.getValor());

				this.manager.merge(pagto);
				dataBase = pagto.getDataPagamento();
			}
		}

		// return (SolicitacaoPagamento) lancamento;
		return baixaAplicacao;
	}

	public AplicacaoRecurso atualizarAplicacao(AplicacaoRecurso aplicacao){
		aplicacao = this.manager.merge(aplicacao);

		return aplicacao;
	}

	public AplicacaoRecurso salvarAplicacao(AplicacaoRecurso aplicacao, User usuario) {

		aplicacao = this.manager.merge(aplicacao);
		aplicacao.setNumeroDocumento(aplicacao.getId().toString());
		LogStatus log = new LogStatus();
		log.setUsuario(usuario);
		log.setData(new Date());
		log.setSigla(aplicacao.getGestao().getSigla());
		if (aplicacao.getId() != null) {
			log.setSiglaPrivilegio("EDIT");
		} else {
			log.setSiglaPrivilegio("NEW");
			log.setStatusLog(StatusCompra.N_INCIADO);
		}

		log.setLancamento(aplicacao);
		this.manager.merge(log);

		if (aplicacao.getId() != null)
			this.manager.createNativeQuery(" delete \n" +
					"from pagamento_lancamento \n" +
					"where id in (select pl.id from pagamento_lancamento pl\n" +
					"join lancamento_acao la on la.id = pl.lancamentoacao_id\n" +
					"where la.lancamento_id = :id)")
					.setParameter("id", aplicacao.getId()).executeUpdate();

		for (LancamentoAcao lc : aplicacao.getLancamentosAcoes()) {
			lc.setDespesaReceita(aplicacao.getDepesaReceita());
			lc.setLancamento(aplicacao);
			lc.setStatus(StatusPagamento.N_VALIDADO);
			lc.setTipoLancamento(aplicacao.getTipoLancamento());
			lc = this.manager.merge(lc);

			PagamentoLancamento pl = new PagamentoLancamento();
			pl.setConta(aplicacao.getContaPagador());
			pl.setContaRecebedor(aplicacao.getContaRecebedor());
			pl.setDataEmissao(aplicacao.getDataEmissao());
			pl.setDataPagamento(aplicacao.getDataPagamento());
			pl.setLancamentoAcao(lc);
			pl.setQuantidadeParcela(aplicacao.getQuantidadeParcela());
			pl.setStt(StatusPagamentoLancamento.PROVISIONADO);
			pl.setTipoParcelamento(aplicacao.getTipoParcelamento());
			pl.setValor(lc.getValor());
			pl.setTipoLancamento(aplicacao.getTipoLancamento());
			pl.setDespesaReceita(aplicacao.getDepesaReceita());

			if (pl.getTipoParcelamento().compareTo(TipoParcelamento.PARCELA_UNICA) == 0) {
				pl.setQuantidadeParcela(1);
			}

			Date dataBase = pl.getDataPagamento();
			tipoParcelamento = pl.getTipoParcelamento();
			BigDecimal totalParcelado = BigDecimal.ZERO;

			for (int i = 1; i <= pl.getQuantidadeParcela(); i++) {
				PagamentoLancamento pagto = new PagamentoLancamento(pl, i);

				if (i == 1) {
					pagto.setDataPagamento(pl.getDataPagamento());
				} else {
					pagto.setDataPagamento(DataUtil.setarData(dataBase, tipoParcelamento));

					if (i == pl.getQuantidadeParcela()) {
						BigDecimal total = pl.getValor();
						BigDecimal resto = total.subtract(totalParcelado);
						pagto.setValor(resto);
					}

				}

				totalParcelado = totalParcelado.add(pagto.getValor());

				this.manager.merge(pagto);
				dataBase = pagto.getDataPagamento();
			}
		}

		// return (SolicitacaoPagamento) lancamento;
		return aplicacao;
	}

	public ProjetoRubrica getProjetoRubricaById(Long id) {
		return this.manager.find(ProjetoRubrica.class, id);
	}

	public RubricaOrcamento getRubricaOrcamentoById(Long id) {
		return this.manager.find(RubricaOrcamento.class, id);
	}

	public ProjetoRubrica getRubricaBydIdDetail(Long id) {

		StringBuilder jpql = new StringBuilder(
				"SELECT new ProjetoRubrica(p.id, p.rubricaOrcamento.rubrica.nome, p.valor, p.componente.nome) from ");
		jpql.append("ProjetoRubrica p where p.id = :id ");
		Query query = this.manager.createQuery(jpql.toString());
		query.setParameter("id", id);

		return (ProjetoRubrica) query.getResultList().get(0);
	}

	public List<ProjetoRubrica> getProjetoRubricaByOrcamentoRubrica(Long id) {
		String jpql = "SELECT NEW ProjetoRubrica(pr.valor) from ProjetoRubrica pr where pr.rubricaOrcamento.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList();
	}

	public List<ProjetoRubrica> getProjetoRubricaCompleteByOrcamentoRubrica(Long id) {
		String jpql = "SELECT pr from ProjetoRubrica pr where pr.rubricaOrcamento.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList();
	}

	public void removerRubricaOrcamento(RubricaOrcamento rubricaOrcamento) {
		this.manager.remove(this.manager.merge(rubricaOrcamento));
	}

	public void removerRubricaOrcamento(Long rubricaOrcamento) {
		this.manager.remove(this.manager.find(RubricaOrcamento.class, rubricaOrcamento));
	}

	public RubricaOrcamento findByIdRubricaOrcamento(Long id) {
		return this.manager.find(RubricaOrcamento.class, id);
	}

	public Orcamento findById(Long id) {
		return this.manager.find(Orcamento.class, id);
	}

	public Orcamento salvar(Orcamento orcamento) {
		return this.manager.merge(orcamento);
	}

	public Rubrica salvarRubrica(Rubrica rubrica) {
		return this.manager.merge(rubrica);
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void remover(Orcamento orcamento) {
		this.manager.remove(this.manager.merge(orcamento));
	}

	public OrcamentoProjeto salvarOrcamentoProjeto(OrcamentoProjeto orcamentoProjeto) {
		return this.manager.merge(orcamentoProjeto);
	}

	public List<OrcamentoProjeto> getProjetosByOrcamento(Long idDoacao) {
		String jpql = "SELECT NEW OrcamentoProjeto(o.projeto.nome, o.orcamento.titulo, o.valor) FROM OrcamentoProjeto o where o.orcamento.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", idDoacao);
		return query.getResultList();
	}

	public List<OrcamentoProjeto> getProjetosByOrcamentoFiltro(Long idDoacao, Filtro filtro) {

		String sql = "SELECT p.nome, o.titulo, op.valor \n" + "FROM orcamento_projeto op\n"
				+ "JOIN orcamento o on o.id = op.orcamento_id\n" + "JOIN projeto p on p.id = op.projeto_id\n"
				+ "JOIN gestao g on g.id = p.gestao_id \n" + "WHERE orcamento_id = :id";

		if (filtro.getSuperintendencia() != null)
			sql += " AND g.superintendencia_id = :id_sup";

		if (filtro.getCoordenadoria() != null)
			sql += " AND g.coordenadoria_id = :id_coord";

		Query query = this.manager.createNativeQuery(sql);
		query.setParameter("id", idDoacao);

		if (filtro.getSuperintendencia() != null)
			query.setParameter("id_sup", filtro.getSuperintendencia().getId());

		if (filtro.getCoordenadoria() != null)
			query.setParameter("id_coord", filtro.getCoordenadoria().getId());

		List<Object[]> list = query.getResultList();

		List<OrcamentoProjeto> listOrcamento = new ArrayList<>();

		for (Object[] obj : list) {
			OrcamentoProjeto op = new OrcamentoProjeto();
			Projeto projeto = new Projeto();
			projeto.setNome(obj[0].toString());
			op.setNomeProjeto(obj[0].toString());
			op.setProjeto(projeto);
			Orcamento orcamento = new Orcamento();
			orcamento.setTitulo(obj[1].toString());
			op.setOrcamento(orcamento);
			op.setValor(new BigDecimal(obj[2].toString()));

			listOrcamento.add(op);
		}

		return listOrcamento;
	}

	public List<Orcamento> getOrcamentos(Filtro filtro) {
		String jpql = "from Orcamento";
		Query query = this.manager.createQuery(jpql);
		return query.getResultList();
	}

	public List<Projeto> getProjetosRelatorioGeral(Filtro filtro, String args)
			throws NumberFormatException, ParseException {
		// StringBuilder jpql = new StringBuilder( "SELECT NEW Orcamento(o.id, o.titulo)
		// FROM Orcamento o where 1 = 1 ");
		StringBuilder jpql = new StringBuilder();
		jpql.append(
				"select p.id, p.nome, p.valor,to_char(p.datainicio,'DD-MM-YYYY') as data_inicio,to_char(p.datafinal,'DD-MM-YYYY') as data_final,  ");

		jpql.append("p.tarifado as tarifado, ");
		jpql.append("p.codigo as codigo, ");
		jpql.append("(select cad.nome from cadeia_produtiva as cad where cad.id = p.cadeia_id) as nome_cadeia, ");
		jpql.append("p.ativo, ");
		jpql.append("(select loc.mascara from localidade loc  where loc.id = p.localidade_id) as nome_localidade,");
		jpql.append("(select pt.codigo from plano_de_trabalho pt where pt.id = p.planodetrabalho_id) as code_plano,");
		jpql.append("(select pt.titulo from plano_de_trabalho pt where pt.id = p.planodetrabalho_id) as nome_plano,");
		jpql.append("(select comp.nome from componente_class  comp where comp.id = p.componente_id) as componente,");
		jpql.append("(select sub.nome from sub_componente sub where sub.id = p.subcomponente_id) as subcomponente, ");

		jpql.append(" (select gest.nome from gestao  gest where gest.id = p.gestao_id) as gestao, ");

		// jpql.append(" (p.valor - (select sum(pr.valor) from projeto_rubrica pr where
		// pr.projeto_id = p.id)) as vl_falta_empenho ");

		jpql.append(
				" (p.valor - CASE WHEN (select sum(pr.valor) from projeto_rubrica pr where pr.projeto_id = p.id) is not null  ");
		jpql.append(
				" THEN (select sum(pr.valor) from projeto_rubrica pr where pr.projeto_id = p.id) ELSE 0 end) as vl_falta_empenho, ");

		// ENTRADAS PROVENIENTES AO PROJETO
		jpql.append("(select sum(pl.valor) ");
		jpql.append("from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id  ");
		jpql.append("	inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");
		jpql.append("	where l.tipo != 'compra' ");
		jpql.append("	and l.statuscompra in ('CONCLUIDO','N_INCIADO') ");
		jpql.append("	and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) ");

		jpql.append(
				"	and (select proj.id from projeto proj where proj.id = (select ppr.projeto_id from projeto_rubrica ppr where ppr.id = la.projetorubrica_id)) = p.id ");

		jpql.append("	and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb') ");
		jpql.append("	and l.tipolancamento != 'reemb_conta' ");
		jpql.append("	and l.tipo != 'baixa_aplicacao' ");
		jpql.append("	and l.tipo != 'doacao_efetiva' ");
		jpql.append("	and l.tipo != 'custo_pessoal'   ");
		jpql.append("	and l.tipo != 'aplicacao_recurso' ");

		jpql.append("	and case p.tarifado when true then ( 1= 1) else (l.tipo != 'tarifa_bancaria') end	 ");
		// jpql.append(" and (false = ((select cb.tipo from conta_bancaria cb where
		// cb.id = pl.conta_id) = 'CA' and (select cb.tipo from conta_bancaria cb where
		// cb.id = pl.contarecebedor_id) = 'CF'))");
		// jpql.append(" and (false = ((select cb.tipo from conta_bancaria cb where
		// cb.id = pl.conta_id) = 'CB' and (select cb.tipo from conta_bancaria cb where
		// cb.id = pl.contarecebedor_id) = 'CB'))");
		// jpql.append(" and (false = ((select cb.tipo from conta_bancaria cb where
		// cb.id = pl.conta_id) = 'CF' and (select cb.tipo from conta_bancaria cb where
		// cb.id = pl.contarecebedor_id) = 'CF'))");
		// jpql.append(" and (((select cb.tipo from conta_bancaria cb where cb.id =
		// pl.conta_id) = 'CF' or (select cb.tipo from conta_bancaria cb where cb.id =
		// pl.conta_id) = 'CA') ");
		// jpql.append(" and (select cb.tipo from conta_bancaria cb where cb.id =
		// pl.contarecebedor_id) = 'CB')) as receita,");

		jpql.append(" and (false = (pl.tipocontapagador = 'CA' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CB' and pl.tipocontarecebedor = 'CB')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CF' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(
				" and ((pl.tipocontapagador = 'CF' or pl.tipocontapagador = 'CA') and (pl.tipocontarecebedor = 'CB')) ");

		// SAÍDAS PROVENIENTES AO PROJETO
		jpql.append("(select sum(pl.valor)");
		jpql.append("	from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id ");
		jpql.append("	inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");
		jpql.append("	where l.tipo != 'compra'  \r\n");
		jpql.append("	and l.statuscompra in ('CONCLUIDO','N_INCIADO') \r\n");
		jpql.append("	and (l.versionlancamento = 'MODE01' or pl.reclassificado is true)\r\n");

		jpql.append(
				"	and (select proj.id from projeto proj where proj.id = (select ppr.projeto_id from projeto_rubrica ppr where ppr.id = la.projetorubrica_id)) = p.id ");

		jpql.append("	and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb')\r\n");
		jpql.append("	and l.tipolancamento != 'reemb_conta' \r\n");
		jpql.append("	and l.tipo != 'baixa_aplicacao'\r\n");
		jpql.append("	and l.tipo != 'doacao_efetiva' \r\n");
		jpql.append("	and l.tipo != 'custo_pessoal' \r\n");
		jpql.append("	and l.tipo != 'aplicacao_recurso'\r\n");

		jpql.append("	and case  p.tarifado  when true then ( 1 = 1 ) else (l.tipo != 'tarifa_bancaria') end	 ");

		// jpql.append(" and (false = ((select cb.tipo from conta_bancaria cb where
		// cb.id = pl.conta_id) = 'CA' and (select cb.tipo from conta_bancaria cb where
		// cb.id = pl.contarecebedor_id) = 'CF'))\r\n");
		// jpql.append(" and (false = ((select cb.tipo from conta_bancaria cb where
		// cb.id = pl.conta_id) = 'CB' and (select cb.tipo from conta_bancaria cb where
		// cb.id = pl.contarecebedor_id) = 'CB'))\r\n");
		// jpql.append(" and (false = ((select cb.tipo from conta_bancaria cb where
		// cb.id = pl.conta_id) = 'CF' and (select cb.tipo from conta_bancaria cb where
		// cb.id = pl.contarecebedor_id) = 'CF'))\r\n");
		// jpql.append(" and (((select cb.tipo from conta_bancaria cb where cb.id =
		// pl.contarecebedor_id) = 'CF' or (select cb.tipo from conta_bancaria cb where
		// cb.id = pl.contarecebedor_id) = 'CA')\r\n");
		// jpql.append(" and (select cb.tipo from conta_bancaria cb where cb.id =
		// pl.conta_id) = 'CB')) as despesa, ");

		jpql.append(" and (false = (pl.tipocontapagador = 'CA' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CB' and pl.tipocontarecebedor = 'CB')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CF' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(
				" and ((pl.tipocontarecebedor = 'CF' or pl.tipocontarecebedor = 'CA') and (pl.tipocontapagador = 'CB')) ");

		jpql.append(
				"(select sum((aproj.jan + aproj.fev + aproj.mar + aproj.abr + aproj.mai + aproj.jun + aproj.jul + aproj.ago + aproj.set + aproj.out + aproj.nov + aproj.dez)) from atividade_projeto aproj where aproj.projeto_id = p.id and planejado is true) as qtd_planejada,");

		jpql.append(
				"(select sum((aproj.janexec + aproj.fevexec + aproj.marexec + aproj.abrexec + aproj.maiexec + aproj.junexec + aproj.julexec + aproj.agoexec + aproj.setexec + aproj.outexec + aproj.novexec + aproj.dezexec)) from atividade_projeto aproj where aproj.projeto_id = p.id and planejado is true) as qtd_executada,");

		jpql.append(
				"(select sum((aproj.jan + aproj.fev + aproj.mar + aproj.abr + aproj.mai + aproj.jun + aproj.jul + aproj.ago + aproj.set + aproj.out + aproj.nov + aproj.dez)) from atividade_projeto aproj where aproj.projeto_id = p.id and (planejado is false or planejado is null) ) as qtd_nao_planejada ");

		jpql.append(" from projeto p where 1 = 1 and versionprojeto = 'mode01' ");

		if (filtro.getProjetos() != null && filtro.getProjetos().length > 0) {
			jpql.append(" and p.id in (:projetos) ");
		}

		jpql.append("order by p.codigo, p.nome ");

		Query query = this.manager.createNativeQuery(jpql.toString());

		if (filtro.getProjetos() != null && filtro.getProjetos().length > 0) {
			List<Integer> list = new ArrayList<>();
			Integer[] mList = filtro.getProjetos();

			for (int i = 0; i < filtro.getProjetos().length; i++) {
				list.add(mList[i]);
			}

			query.setParameter("projetos", list);
		}

		List<Object[]> result = query.getResultList();
		List<Projeto> projetos = new ArrayList<>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		for (Object[] object : result) {
			projetos.add(new Projeto(new Long(object[0].toString()), object[1].toString(),
					DataUtil.converteDataSql(object[3].toString()), DataUtil.converteDataSql(object[4].toString()),
					new BigDecimal(object[2] != null ? object[2].toString() : "0"),
					object[5] != null ? Boolean.valueOf(object[5].toString()) : false, Util.getNullValue(object[6], ""),
					Util.getNullValue(object[7], ""), object[8] != null ? Boolean.valueOf(object[8].toString()) : false,
					Util.getNullValue(object[9], ""), Util.getNullValue(object[10], ""),
					Util.getNullValue(object[11], ""), Util.getNullValue(object[12], ""),
					Util.getNullValue(object[13], ""), Util.getNullValue(object[14], ""),
					new BigDecimal(object[15] != null ? object[15].toString() : "0"),
					new BigDecimal(object[16] != null ? object[16].toString() : "0"),
					new BigDecimal(object[17] != null ? object[17].toString() : "0"),
					Integer.parseInt(object[18] != null ? object[18].toString() : "0"),
					Integer.parseInt(object[19] != null ? object[19].toString() : "0"),
					Integer.parseInt(object[20] != null ? object[20].toString() : "0")));

		}

		return projetos;
	}

	public List<Projeto> getProjetosFilter(Filtro filtro, String args) throws NumberFormatException, ParseException {
		// StringBuilder jpql = new StringBuilder( "SELECT NEW Orcamento(o.id, o.titulo)
		// FROM Orcamento o where 1 = 1 ");
		StringBuilder jpql = new StringBuilder();
		jpql.append(
				"select p.id, p.nome, p.valor,to_char(p.datainicio,'DD-MM-YYYY') as data_inicio,to_char(p.datafinal,'DD-MM-YYYY') as data_final,  ");

		jpql.append("p.tarifado as tarifado, ");
		jpql.append("p.codigo as codigo, ");
		jpql.append("(select cad.nome from cadeia_produtiva as cad where cad.id = p.cadeia_id) as nome_cadeia, ");
		jpql.append("p.ativo, ");
		jpql.append("(select loc.mascara from localidade loc  where loc.id = p.localidade_id) as nome_localidade,");
		jpql.append("(select pt.codigo from plano_de_trabalho pt where pt.id = p.planodetrabalho_id) as code_plano,");
		jpql.append("(select pt.titulo from plano_de_trabalho pt where pt.id = p.planodetrabalho_id) as nome_plano,");
		jpql.append("(select comp.nome from componente_class  comp where comp.id = p.componente_id) as componente,");
		jpql.append("(select sub.nome from sub_componente sub where sub.id = p.subcomponente_id) as subcomponente, ");

		jpql.append(" (select gest.nome from gestao  gest where gest.id = p.gestao_id) as gestao, ");

		// jpql.append(" (p.valor - (select sum(pr.valor) from projeto_rubrica pr where
		// pr.projeto_id = p.id)) as vl_falta_empenho ");

		jpql.append(
				" (p.valor - CASE WHEN (select sum(pr.valor) from projeto_rubrica pr where pr.projeto_id = p.id) is not null  ");
		jpql.append(
				" THEN (select sum(pr.valor) from projeto_rubrica pr where pr.projeto_id = p.id) ELSE 0 end) as vl_falta_empenho ");
		jpql.append(" from projeto p where 1 = 1 and versionprojeto = 'mode01' ");

		if (filtro.getProjetos() != null && filtro.getProjetos().length > 0) {
			jpql.append(" and p.id in (:projetos) ");
		}

		jpql.append("order by p.codigo, p.nome ");

		Query query = this.manager.createNativeQuery(jpql.toString());

		if (filtro.getProjetos() != null && filtro.getProjetos().length > 0) {
			List<Integer> list = new ArrayList<>();
			Integer[] mList = filtro.getProjetos();

			for (int i = 0; i < filtro.getProjetos().length; i++) {
				list.add(mList[i]);
			}

			query.setParameter("projetos", list);
		}

		List<Object[]> result = query.getResultList();
		List<Projeto> projetos = new ArrayList<>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		for (Object[] object : result) {
			projetos.add(new Projeto(new Long(object[0].toString()), object[1].toString(),
					DataUtil.converteDataSql(object[3].toString()), DataUtil.converteDataSql(object[4].toString()),
					new BigDecimal(object[2] != null ? object[2].toString() : "0"),
					object[5] != null ? Boolean.valueOf(object[5].toString()) : false, Util.getNullValue(object[6], ""),
					Util.getNullValue(object[7], ""), object[8] != null ? Boolean.valueOf(object[8].toString()) : false,
					Util.getNullValue(object[9], ""), Util.getNullValue(object[10], ""),
					Util.getNullValue(object[11], ""), Util.getNullValue(object[12], ""),
					Util.getNullValue(object[13], ""), Util.getNullValue(object[14], ""),
					new BigDecimal(object[15] != null ? object[15].toString() : "0")));

			/**
			 * 
			 * Nome da gestão Codigo do plano Nome do Plano Localidade do projeto Nome
			 * componente Nome subCompoente
			 * 
			 * 
			 **/

		}

		return projetos;
	}
	
	public List<Orcamento> filtrarOrcamentos(Filtro filtro) {
		StringBuilder jpql = new StringBuilder("from Orcamento o where 1 = 1 ");

		
		if(filtro.getResponsavelTecnico() != null && filtro.getResponsavelTecnico().getId() != null) {
			filtro.setResponsavelTecnico(filtro.getResponsavelTecnico());
			jpql.append("and o.responsavelTecnico.id = :responsavelTecnico ");
		}
		
		if(filtro.getColaborador() != null && filtro.getColaborador().getId() != null) {
			filtro.setColaborador(filtro.getColaborador());
			jpql.append("and o.colaborador.id = :colaborador ");
		}
		
		if(filtro.getFontePagadora() != null && filtro.getFontePagadora().getId() != null) {
			filtro.setFontePagadora(filtro.getFontePagadora());
			jpql.append("and o.fonte.id = :fonte ");
		}
		
		if(filtro.getTitulo() != null && filtro.getTitulo().length() > 0) {
			filtro.setTitulo(filtro.getTitulo());
			jpql.append("and o.titulo = :titulo ");	
		}
//		
		if (filtro.getDataInicio() != null) {
			if (filtro.getDataFinal() != null) {
				jpql.append(" and o.dataInicio >= :data_inicio and o.dataFinal <= :data_final ");
			} else {
				jpql.append(" and o.dataInicio >= :data_inicio ");
			}
		}
		
		Query query = this.manager.createQuery(jpql.toString());
		
		if(filtro.getResponsavelTecnico() != null && filtro.getResponsavelTecnico().getId() != null) {
			query.setParameter("responsavelTecnico", filtro.getResponsavelTecnico().getId());
		}
		if(filtro.getColaborador() != null && filtro.getColaborador().getId() != null) {
			query.setParameter("colaborador", filtro.getColaborador().getId());
		}
		if(filtro.getFontePagadora() != null && filtro.getFontePagadora().getId() != null) {
			query.setParameter("fonte", filtro.getFontePagadora().getId());
		}
		if(filtro.getTitulo() != null && filtro.getTitulo().length() > 0) {			
			query.setParameter("titulo", filtro.getTitulo());
		}
		if (filtro.getDataInicio() != null) {
			if (filtro.getDataFinal() != null) {
				/*
				 * jpql.append( " and c.dataEmissao between :data_inicio and :data_final");
				 */
				query.setParameter("data_inicio", filtro.getDataInicio());
				query.setParameter("data_final", filtro.getDataFinal());
			} else {
				/* jpql.append(" and c.dataEmissao > :data_inicio"); */
				query.setParameter("data_inicio", filtro.getDataInicio());
			}
		}
		
		
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Orcamento>();
		
	}

	public List<Orcamento> getOrcamentosFilter(Filtro filtro, String args) {
		// StringBuilder jpql = new StringBuilder( "SELECT NEW Orcamento(o.id, o.titulo)
		// FROM Orcamento o where 1 = 1 ");
		StringBuilder jpql = new StringBuilder();
		jpql.append("select o.id, o.titulo from orcamento o where 1 = 1");

		if (filtro.getDoacoes() != null && filtro.getDoacoes().length > 0) {
			jpql.append(" and o.id in (:acoes) ");
		}

		if (filtro.getFontes() != null && filtro.getFontes().length > 0) {
			jpql.append(" and o.fonte_id in (:fontes) ");
		}
		
		if (filtro.getTitulo() != null && filtro.getTitulo().length() > 0) {
			jpql.append(" and o.titulo in (:titulo)");
		}

		Query query = this.manager.createNativeQuery(jpql.toString());

		if (filtro.getDoacoes() != null && filtro.getDoacoes().length > 0) {
			List<Integer> list = new ArrayList<>();
			Integer[] mList = filtro.getDoacoes();

			for (int i = 0; i < filtro.getDoacoes().length; i++) {
				list.add(mList[i]);
			}

			query.setParameter("acoes", list);
		}
		
		if(filtro.getTitulo() != null && filtro.getTitulo().length() > 0) {
			List<Integer> list = new ArrayList<>();
			Integer[] mList = filtro.getFontes();

			for (int i = 0; i < filtro.getFontes().length; i++) {
				list.add(mList[i]);
			}
			query.setParameter("titulo", list);
		}

		if (filtro.getFontes() != null && filtro.getFontes().length > 0) {
			List<Integer> list = new ArrayList<>();
			Integer[] mList = filtro.getFontes();

			for (int i = 0; i < filtro.getFontes().length; i++) {
				list.add(mList[i]);
			}
			query.setParameter("fontes", list);
		}

		List<Object[]> result = query.getResultList();
		List<Orcamento> orcamentos = new ArrayList<>();

		for (Object[] object : result) {
			orcamentos.add(new Orcamento(new Long(object[0].toString()), object[1].toString()));
		}

		return orcamentos;
	}
	
	public List<Orcamento> completeTitulos(String s){
		String jpql = "from Orcamento o where lower(o.titulo) like lower(:titulo)";
		Query query = this.manager.createQuery(jpql);
		
		query.setParameter("titulo", "%" + s + "%");
		return query.getResultList();
	}

	public List<OrcamentoProjeto> getOrcamentosProjeto(Long id) {
		String jpql = "from OrcamentoProjeto where projeto.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList();
	}

	public void removerRelacaoOrcamento(OrcamentoProjeto orcamentoProjeto) {
		this.manager.remove(this.manager.find(OrcamentoProjeto.class, orcamentoProjeto.getId()));
	}

	public void removerProjetoRubrica(ProjetoRubrica projetoRubrica) {
		this.manager.remove(this.manager.find(ProjetoRubrica.class, projetoRubrica.getId()));
	}

	public List<OrcamentoProjeto> getRelacaoDeOrcamentos(Long idProjeto) {
		String jpql = "from OrcamentoProjeto where projeto.id = :id ";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", idProjeto);
		return query.getResultList();
	}

	public List<Rubrica> getRubricaAutoComplete(String txt) {
		StringBuilder jpql = new StringBuilder("from  Rubrica r where lower(r.nome) like lower(:nome)");
		jpql.append(" order by r.nome ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + txt + "%");
		query.setMaxResults(20);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Rubrica>();
	}

	public List<Orcamento> getOrcamentoAutoComplete(String txt) {
		StringBuilder jpql = new StringBuilder(
				"from  Orcamento o where lower(o.titulo) like lower(:titulo) or lower(o.contrato) like lower(:contrato) ");
		jpql.append(" order by o.titulo ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("titulo", "%" + txt + "%");
		query.setParameter("contrato", "%" + txt + "%");
		query.setMaxResults(20);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Rubrica>();
	}

	public List<RubricaOrcamento> getRubricasDeOrcamentoBNDES(Long idOrcamento, Filtro filtro) {
		StringBuilder jpql = new StringBuilder("from  RubricaOrcamento ro where ro.orcamento.id = :id");

		if (filtro.getComponenteClass() != null) {
			jpql.append(" and ro.componente.id = :componente ");
		}

		if (filtro.getSubComponente() != null) {
			jpql.append(" and ro.subComponente.id = :sub_componente ");
		}

		jpql.append(" order by ro.componente.nome, ro.subComponente.nome, ro.rubrica.nome ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", idOrcamento);

		if (filtro.getComponenteClass() != null) {
			query.setParameter("componente", filtro.getComponenteClass());
		}

		if (filtro.getSubComponente() != null) {
			query.setParameter("sub_componente", filtro.getSubComponente());
		}

		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<RubricaOrcamento>();
	}

	// public List<RubricaOrcamento> getRubricasDeOrcamento2(Long idOrcamento,
	// Filtro filtro) {
	// StringBuilder jpql = new StringBuilder("select ");
	// jpql.append("select");
	// jpql.append("ro.id as id_0,");
	// jpql.append("ro.porcentagem as porcentage_1,");
	// jpql.append("ro.valor as valor_2,");
	// jpql.append("ro.componente_id as componente_id_3,");
	// jpql.append("ro.orcamento_id as orcamento_id_4,");
	// jpql.append("ro.rubrica_id as rubrica_id_5,");
	// jpql.append("ro.subcomponente_id as subcomponente_id_6,");
	// jpql.append("ro.tipo as tipo_7,");
	// jpql.append("(select (sum(pr.valor)) from projeto_rubrica pr join
	// rubrica_orcamento ro on pr.rubricaorcamento_id = ro.id where ro.id = 3) as
	// soma_8");
	// jpql.append("from rubrica_orcamento as ro ");
	// jpql.append("where ro.orcamento_id = :id and ro.tipo is null ");
	// jpql.append(" order by ro.componente_id, ro.subcomponente_id, ro.rubrica_id
	// ");
	//
	// Query query = this.manager.createNativeQuery(jpql.toString());
	//
	//
	//
	//
	// if (filtro.getComponenteClass() != null) {
	// jpql.append(" and ro.componente_id = :componente ");
	// }
	//
	// if (filtro.getSubComponente() != null) {
	// jpql.append(" and ro.subComponente.id = :sub_componente ");
	// }
	//
	//
	// query.setParameter("id", idOrcamento);
	//
	//
	// if (filtro.getComponenteClass() != null) {
	// query.setParameter("componente", filtro.getComponenteClass());
	// }
	//
	// if (filtro.getSubComponente() != null) {
	// query.setParameter("sub_componente", filtro.getSubComponente());
	// }
	//
	//
	// List<Object[]> result = query.getResultList();
	// List<RubricaOrcamento> rubricas = new ArrayList<RubricaOrcamento>();
	//
	// for (Object[] object : result) {
	// RubricaOrcamento ad = new RubricaOrcamento(object);
	// rubricas.add(ad);
	// }
	//
	// return rubricas;
	// }

	public List<RubricaOrcamento> getNewRubricasDeOrcamento(Long idOrcamento, Filtro filtro) {
		StringBuilder jpql = new StringBuilder("select ro.id as id_00,  " + "c.nome as nome_componente_01,"
				+ "s.nome as sub_02, " + "r.nome as categoria_03, "
				+ "((ro.valor) -(select sum(valor) from projeto_rubrica where rubricaorcamento_id = ro.id)) as valor_disponivel_04 , "
				+ "(select sum(valor) from projeto_rubrica where rubricaorcamento_id = ro.id) as valor_empenhado_05, "
				+ "ro.valor as valor_06, " + "c.id as id_componente_7, " + "s.id as id_subComponente_8, "
				+ "r.id as id_categoria_9 " + "from rubrica_orcamento  ro ");
		jpql.append("inner join componente_class c on ro.componente_id = c.id ");
		jpql.append("inner join sub_componente s on ro.subcomponente_id = s.id  ");
		jpql.append("inner join rubrica r on ro.rubrica_id = r.id ");
		jpql.append("where ro.orcamento_id = :id and ro.tipo is null ");

		if (filtro.getComponenteClass() != null) {
			jpql.append(" and ro.componente_id = :componente ");
		}

		if (filtro.getSubComponente() != null) {
			jpql.append(" and ro.subcomponente_id = :sub_componente ");
		}

		Query query = this.manager.createNativeQuery(jpql.toString());
		query.setParameter("id", idOrcamento);

		if (filtro.getComponenteClass() != null) {
			query.setParameter("componente", filtro.getComponenteClass());
		}

		if (filtro.getSubComponente() != null) {
			query.setParameter("sub_componente", filtro.getSubComponente());
		}

		List<Object[]> result = query.getResultList();
		List<RubricaOrcamento> rubricas = new ArrayList<RubricaOrcamento>();

		for (Object[] object : result) {
			RubricaOrcamento ad = new RubricaOrcamento(object);
			rubricas.add(ad);
		}

		return rubricas;
	}

	public List<RubricaOrcamento> getRubricasDeOrcamento(Long idOrcamento, Filtro filtro) {
		StringBuilder jpql = new StringBuilder(
				"from  RubricaOrcamento ro where ro.orcamento.id = :id and ro.tipo is null");
		// jpql.append(" and ro.tipo != :de ");

		if (filtro.getComponenteClass() != null) {
			jpql.append(" and ro.componente.id = :componente ");
		}

		if (filtro.getSubComponente() != null) {
			jpql.append(" and ro.subComponente.id = :sub_componente ");
		}

		jpql.append(" order by ro.componente.nome, ro.subComponente.nome, ro.rubrica.nome ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", idOrcamento);

		// query.setParameter("de", "de");

		if (filtro.getComponenteClass() != null) {
			query.setParameter("componente", filtro.getComponenteClass());
		}

		if (filtro.getSubComponente() != null) {
			query.setParameter("sub_componente", filtro.getSubComponente());
		}

		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<RubricaOrcamento>();
	}

	public RubricaOrcamento buscarLinhaOrcamentoEfetiva(Long idOrcamento) {
		StringBuilder jpql = new StringBuilder(
				"from  RubricaOrcamento ro where ro.orcamento.id = :id and ro.tipo =  'de' ");
		jpql.append(" order by ro.componente.nome, ro.subComponente.nome, ro.rubrica.nome ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", idOrcamento);
		return query.getResultList().size() > 0 ? (RubricaOrcamento) query.getResultList().get(0) : null;
	}

	public RubricaOrcamento buscarLinhaOrcamentoAplicacao(Long idOrcamento) {
		StringBuilder jpql = new StringBuilder(
				"from  RubricaOrcamento ro where ro.orcamento.id = :id and ro.tipo =  'ap' ");
		jpql.append(" order by ro.componente.nome, ro.subComponente.nome, ro.rubrica.nome ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", idOrcamento);
		return query.getResultList().size() > 0 ? (RubricaOrcamento) query.getResultList().get(0) : null;
	}

	public RubricaOrcamento buscarLinhaOrcamentoAlocRendimento(Long idOrcamento) {
		StringBuilder jpql = new StringBuilder(
				"from  RubricaOrcamento ro where ro.orcamento.id = :id and ro.tipo =  'rend' ");
		jpql.append(" order by ro.componente.nome, ro.subComponente.nome, ro.rubrica.nome ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", idOrcamento);
		return query.getResultList().size() > 0 ? (RubricaOrcamento) query.getResultList().get(0) : null;
	}

	public RubricaOrcamento buscarLinhaOrcamentoBaixaAplicacao(Long idOrcamento) {
		StringBuilder jpql = new StringBuilder(
				"from  RubricaOrcamento ro where ro.orcamento.id = :id and ro.tipo =  'ba' ");
		jpql.append(" order by ro.componente.nome, ro.subComponente.nome, ro.rubrica.nome ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", idOrcamento);
		return query.getResultList().size() > 0 ? (RubricaOrcamento) query.getResultList().get(0) : null;
	}

	public ComponenteClass buscarComponenteDefault() {
		return this.manager.find(ComponenteClass.class, new Long(6));
	}

	public SubComponente buscarSubComponenteDefault() {
		return this.manager.find(SubComponente.class, new Long(11));
	}

	public Rubrica buscarRubricaDefault() {
		return this.manager.find(Rubrica.class, new Long(144));
	}

	public Rubrica buscarRubricaAplicaoDefault() {
		return this.manager.find(Rubrica.class, new Long(257));
	}

	public Rubrica buscarRubricaBaixaAplicaoDefault() {
		return this.manager.find(Rubrica.class, new Long(256));
	}

	public Rubrica buscarRubricaAlocRendimentoDefault() {

		StringBuilder jpql = new StringBuilder("from  Rubrica r where nome = 'Alocação de Rendimento'");
		Query query = manager.createQuery(jpql.toString());

		List<Rubrica> list = query.getResultList();

		return list.size() > 0 ? list.get(0) : null;
	}

	public List<RubricaOrcamento> completeRubricasDeOrcamento(String s) {
		StringBuilder jpql = new StringBuilder("from  RubricaOrcamento ro where 1 = 1");
		jpql.append(" and lower(ro.rubrica.nome) like lower(:param) ");
		jpql.append(" order by ro.componente.nome, ro.subComponente.nome, ro.rubrica.nome ");
		Query query = manager.createQuery(jpql.toString());
		// query.setParameter("id", idOrcamento);
		query.setParameter("param", "%" + s + "%");

		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<RubricaOrcamento>();
	}

	public List<ProjetoRubrica> getRubricasDeProjeto(Long idProjeto, Long idOrcamento) {
		StringBuilder jpql = new StringBuilder("from ProjetoRubrica where 1 = 1  ");

		if (idProjeto != null) {
			jpql.append(" and projeto.id = :id");
		}

		if (idOrcamento != null) {
			jpql.append(" and rubricaOrcamento.orcamento.id = :id_orcamento");
		}

		Query query = this.manager.createQuery(jpql.toString());

		if (idProjeto != null) {
			query.setParameter("id", idProjeto);
		}

		if (idOrcamento != null) {
			query.setParameter("id_orcamento", idOrcamento);
		}

		return query.getResultList();
	}

	public List<ProjetoRubrica> getRubricasDeProjetoJOIN(Long idProjeto, Long idOrcamento) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW ProjetoRubrica(pr.id,pr.rubricaOrcamento,pr.projeto.nome, pr.rubricaOrcamento.orcamento.titulo) from ProjetoRubrica as pr  ");
		jpql.append("JOIN pr.rubricaOrcamento as ro ");
		jpql.append("JOIN ro.orcamento as o ");
		jpql.append("where 1 = 1");

		// "SELECT NEW Projeto(p.id,p.nome) FROM UserProjeto as up JOIN
		// up.projeto as p ");
		if (idProjeto != null) {
			jpql.append(" and pr.projeto.id = :id");
		}

		if (idOrcamento != null) {
			jpql.append(" and o.id = :id_orcamento");
		}

		Query query = this.manager.createQuery(jpql.toString());

		if (idProjeto != null) {
			query.setParameter("id", idProjeto);
		}

		if (idOrcamento != null) {
			query.setParameter("id_orcamento", idOrcamento);
		}

		return query.getResultList();
	}

	@Deprecated
	public List<ProjetoRubrica> completeRubricasDeProjetoJOINRevert(String s) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW ProjetoRubrica(pr.id,pr.rubricaOrcamento,pr.projeto.nome, pr.rubricaOrcamento.orcamento.titulo, pr.projeto.id, pr.projeto.codigo) from ProjetoRubrica as pr  ");
		jpql.append("JOIN pr.rubricaOrcamento as ro ");
		jpql.append("JOIN ro.orcamento as o ");
		jpql.append("where 1 = 1");

		jpql.append(" and ( lower(pr.rubricaOrcamento.rubrica.nome) like lower(:nome) ");
		jpql.append(" or lower(pr.projeto.nome) like lower(:nome) ");

		jpql.append("or lower(pr.projeto.codigo) like lower(:nome) )");
		jpql.append(" and pr.projeto.ativo = :ativ ");

		Query query = this.manager.createQuery(jpql.toString());

		query.setParameter("nome", "%" + s + "%");
		query.setParameter("ativ", true);
		return query.getResultList();
	}

	// Alterado em 11/09/2018 para adição de coluna 'Saldo' ao exibir linha
	// orcamentaria TODO
	// Deixou de Ser usado 20/09/2018 para ser usado o CalcilatorRubricaRepositorio
	@Deprecated
	public List<ProjetoRubrica> completeRubricasDeProjetoJOIN(String s) {
		/*
		 * After StringBuilder jpql = new
		 * StringBuilder("SELECT NEW ProjetoRubrica(pr.id,pr.rubricaOrcamento,pr.projeto.nome, pr.rubricaOrcamento.orcamento.titulo, pr.projeto.id, pr.projeto.codigo) from ProjetoRubrica as pr  "
		 * ); jpql.append("JOIN pr.rubricaOrcamento as ro ");
		 * jpql.append("JOIN ro.orcamento as o "); jpql.append("where 1 = 1"); jpql.
		 * append(" and ( lower(pr.rubricaOrcamento.rubrica.nome) like lower(:nome) ");
		 * jpql.append(" or lower(pr.projeto.nome) like lower(:nome) ");
		 * jpql.append("or lower(pr.projeto.codigo) like lower(:nome) )");
		 * jpql.append(" and pr.projeto.ativo = :ativ ");
		 */
		// before

		StringBuilder jpql = new StringBuilder("select pr.id as id_00 ,p.codigo as codigo_01,orc.titulo as fonte_02 ,\n"
				+ " p.nome as projeto_03,\n"
				+ " (select cc.nome from componente_class cc where   cc.id = pr.componente_id ) as componente_04,\n"
				+ " (select sc.nome from sub_componente  sc where sc.id = pr.subcomponente_id) as subComponte_05,\n"
				+ " (select ru.nome from rubrica ru where ru.id = ro.rubrica_id) as linha_orcamentaria_06 ,"
				+ "((pr.valor) -(select sum(pl.valor) " + "from pagamento_lancamento pl \n"
				+ "inner join lancamento_acao la on la.id = pl.lancamentoacao_id \n"
				+ "inner join projeto_rubrica ppr on la.projetorubrica_id = ppr.id \n"
				+ "inner join lancamento l on l.id = pl.lancamento_id \n"
				+ "where l.statuscompra in ('CONCLUIDO','N_INCIADO') \n"
				+ "and pl.stt in ('EFETIVADO','PROVISIONADO') \n" + "and ppr.id = pr.id)) as valor_saldo_07  "
				+ "from projeto_rubrica pr \n" + "inner join rubrica_orcamento ro on pr.rubricaorcamento_id = ro.id \n"
				+ "inner join orcamento orc on ro.orcamento_id = orc.id \n"
				+ "inner join projeto p on p.id  = pr.projeto_id \n"
				+ "inner join rubrica r on ro.rubrica_id = r.id  \n"
				+ "where 1 = 1 and ((lower(r.nome)  like lower(:nome)) or (lower(p.nome) like lower(:nome)) or (lower(p.codigo)  like lower(:nome))) and  (p.ativo  is true )");


		Query query = this.manager.createNativeQuery(jpql.toString());

		query.setParameter("nome", "%" + s + "%");
		/*
		 * query.setParameter("ativ", true);
		 */

		List<Object[]> result = query.getResultList();
		List<ProjetoRubrica> rubricas = new ArrayList<ProjetoRubrica>();

		for (Object[] object : result) {

			ProjetoRubrica pro = new ProjetoRubrica(Util.getNullValue((object[0].toString()), new Long(0)),
					Util.getNullValue(object[1].toString(), "Valor Não Informado"),
					Util.getNullValue(object[2].toString(), "Valor Não Informado"),
					Util.getNullValue(object[3].toString(), "Valor Não Informado"),
					Util.getNullValue(object[4].toString(), "Valor Não Informado"),
					Util.getNullValue(object[5].toString(), "Valor Não Informado"),
					Util.getNullValue(object[6].toString(), "Valor Não Informado"),
					Util.getNullValue(new BigDecimal(object[7].toString())));
			rubricas.add(pro);
		}

		return rubricas;
		// end

	}

	// Desconsiderar método e usar o novo com uso do construtor
	public List<ProjetoRubrica> getRubricasDeProjeto(Long idProjeto) {
		StringBuilder jpql = new StringBuilder("from ProjetoRubrica where 1 = 1  ");

		if (idProjeto == null) {
			idProjeto = 0l;
		}

		if (idProjeto != null) {
			jpql.append(" and projeto.id = :id");
		}

		Query query = this.manager.createQuery(jpql.toString());

		if (idProjeto != null) {
			query.setParameter("id", idProjeto);
		}

		return query.getResultList();
	}

	public ProjetoRubrica findProjetoRubricaById(Long id) {
		return this.manager.find(ProjetoRubrica.class, id);
	}

	public ProjetoRubrica salvarProjetoRubrica(ProjetoRubrica projetoRubrica) {
		return this.manager.merge(projetoRubrica);
	}

	public HistoricoAditivoOrcamento salvarHistoricoOrcamento(HistoricoAditivoOrcamento historicoAditivoOrcamento) {
		historicoAditivoOrcamento = this.manager.merge(historicoAditivoOrcamento);
		return historicoAditivoOrcamento;
	}

	public List<HistoricoAditivoOrcamento> getHistoricoAditivoOrcamento(Long id) {
		String jpql = "SELECT hao from HistoricoAditivoOrcamento hao where hao.orcamento.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList();
	}

}
