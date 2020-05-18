package repositorio;

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
import model.GrupoDeInvestimento;
import model.MetaPlano;
import model.Projeto;
import util.Filtro;

public class AcaoRepositorio {

	@Inject
	private EntityManager manager;

	public AcaoRepositorio() {
	}

	public AcaoRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public Acao getAcaoPorId(Long id) {
		return this.manager.find(Acao.class, id);
	}

	public void salvar(Acao acao) {
		this.manager.merge(acao);
	}
	
	

	public List<Acao> getAcoes(Projeto projeto) {
		StringBuilder jpql = new StringBuilder("from  Acao a  where 1 = 1 and a.projeto = :projeto ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("projeto", projeto);

		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Acao>();
	}

	public List<Acao> getAcoesIsoladas2(Projeto projeto) {
		// jpql
		StringBuilder jpql = new StringBuilder("SELECT a.id");
		jpql.append(",a.codigo");
		jpql.append(",(select  sum(af.valor) from AcaoFonte af where af.acao.id = a.id) as orcado ");
		jpql.append(" from Acao as a where a.projeto = :projeto");

		TypedQuery<Object[]> query = manager.createQuery(jpql.toString(), Object[].class);
		query.setParameter("projeto", projeto);
		List<Acao> retorno = new ArrayList<Acao>();
		List<Object[]> result = query.getResultList();
		Acao acao = new Acao();

		for (Object[] object : result) {
			acao = new Acao();
			acao.setId(new Long(object[0].toString()));
			acao.setCodigo(object[1].toString());
			acao.setOrcado(object[2] != null ? new BigDecimal(object[2].toString()) : BigDecimal.ZERO);
			retorno.add(acao);
		}

		return retorno;
	}

	
	public List<AcaoProduto> getItensByAcao(Acao acao){
		String jpql = "from AcaoProduto a where a.acao.id = :id";
		Query query = manager.createQuery(jpql);
		query.setParameter("id", acao.getId());
		return query.getResultList();
	}
	
	public List<Acao> getAcoesIsoladas3(Projeto projeto) {
		// hql
		StringBuilder jpql = new StringBuilder("select a.id, a.codigo,");
		jpql.append(" (select sum(af.valor) from acao_fonte af where af.acao_id = a.id) as orcado,");
		jpql.append(" (select sum(la.valor) from lancamento l join lancamento_acao la on");
		jpql.append(" l.id = la.lancamento_id where l.tipo != 'compra' and la.acao_id = a.id");
		jpql.append(" and l.depesareceita = 'DESPESA' and l.tipolancamento != 'pc' and l.statuscompra = 'CONCLUIDO'");
		jpql.append(" group by la.acao_id) as executado,");
		jpql.append(" (select sum(la.valor) from lancamento l join lancamento_acao la on ");
		jpql.append(" l.id = la.lancamento_id where l.tipo != 'compra' and la.acao_id = a.id");
		jpql.append("  and l.depesareceita = 'RECEITA' and l.tipolancamento != 'pc' and l.statuscompra = 'CONCLUIDO'");
		jpql.append(" group by la.acao_id) as receita  ");
		
		jpql.append(" from acao a where a.projeto_id = :projeto order by a.codigo");

		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("projeto", projeto.getId());
		List<Acao> retorno = new ArrayList<Acao>();
		List<Object[]> result = query.getResultList();

		Acao acao = new Acao();

		for (Object[] object : result) {
			acao = new Acao();
			acao.setId(new Long(object[0].toString()));
			acao.setCodigo(object[1].toString());
			acao.setOrcado(object[2] != null ? new BigDecimal(object[2].toString()) : BigDecimal.ZERO);
			acao.setExecutado(object[3] != null ? new BigDecimal(object[3].toString()) : BigDecimal.ZERO);
			acao.setReceita(object[4] != null ? new BigDecimal(object[4].toString()) : BigDecimal.ZERO);
			acao.setSaldo(acao.getOrcado().subtract(acao.getExecutado()).add(acao.getReceita()));
			retorno.add(acao);
		}

		return retorno;
	}
	
	public List<Acao> getAcoesDeProjeto(Long idProjeto){
		String jpql =  "from Acao a where a.projeto.id = :id order by a.rubrica.nome, a.ips";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", idProjeto);
		return query.getResultList();
	}
	 	
	public List<MetaPlano> getAcoesDoPlanoDeTrabalho(Projeto projeto){
		
		StringBuilder jpql = new StringBuilder("select a.id, a.codigo,");
		jpql.append(" (select l.mascara from  localidade l where l.id = a.localidade_id) as localidade,");
		jpql.append(" (select c.nome from  cadeia_produtiva c where c.id =  a.projarranjosinfra_id) as cadeia,");
		jpql.append(" a.objetivo, a.grupoinvestimento,a.ips,a.responsavel, a.repasse, a.observacao, a.quantidade,a.componente, ");
		jpql.append("(select sum(f.valor) from acao_fonte f where f.acao_id = a.id) as valor, ");
		jpql.append("(select f.nome from fonte_pagadora f where f.id =  (select fa.fonte_id from acao_fonte fa where fa.acao_id = a.id LIMIT 1)) as fonte ");
		jpql.append(" from acao as a where projeto_id = :id order by codigo ");
		
		
		
		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("id", projeto.getId());
		List<MetaPlano> retorno = new ArrayList<MetaPlano>();
		List<Object[]> result = query.getResultList();

		Acao acao = new Acao();

		for (Object[] object : result) {
			MetaPlano mt = new MetaPlano();
			mt.setCodigo(object[1] != null ?  object[1].toString() : "");
			mt.setLocalizacao(object[2] != null ?  object[2].toString() : "");
			mt.setPaeic(object[3] != null ?  object[3].toString() : "");
			mt.setObjetivo(object[4] != null ?  object[4].toString() : "");
			mt.setGrupoDeInvestimento(object[5] != null ?  GrupoDeInvestimento.valueOf(object[5].toString()).getNome()  : "");
			mt.setDescricaoDoInvestimento(object[6] != null ?  object[6].toString() : "");
			mt.setResponsavel(object[7] != null ?  object[7].toString() : "");
			mt.setEder(object[8] != null ?  object[8].toString() : "");
			mt.setObservacao(object[9] != null ?  object[9].toString() : "");
			mt.setQuantidade(object[10] != null ?  object[10].toString() : "");
			mt.setComponente(object[11] != null ?  Componente.valueOf(object[11].toString()).getNome()  : "");
			mt.setValorPrevisto(object[12] != null ?  object[12].toString() : "");
			mt.setFonteDeFinanciamento(object[13] != null ?  object[13].toString() : "");
			retorno.add(mt);
		}

		return retorno;

		
	}
	
	
	public BigDecimal getSaldoAcao(Long id) {
		// hql
		StringBuilder jpql = new StringBuilder("select a.id, a.codigo,");
		jpql.append(" (select sum(af.valor) from acao_fonte af where af.acao_id = a.id) as orcado,");
		jpql.append(" (select sum(la.valor) from lancamento l join lancamento_acao la on");
		jpql.append(" l.id = la.lancamento_id where l.tipo != 'compra' and la.acao_id = a.id");
		jpql.append(" and l.depesareceita = 'DESPESA' and l.tipolancamento != 'pc' and l.statuscompra = 'CONCLUIDO'");
		jpql.append(" group by la.acao_id) as executado,");
		jpql.append(" (select sum(la.valor) from lancamento l join lancamento_acao la on ");
		jpql.append(" l.id = la.lancamento_id where l.tipo != 'compra' and la.acao_id = a.id");
		jpql.append("  and l.depesareceita = 'RECEITA' and l.tipolancamento != 'pc' and l.statuscompra = 'CONCLUIDO'");
		jpql.append(" group by la.acao_id) as receita  ");
		
		jpql.append(" from acao a where a.id = :id");

		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("id", id);
		List<Acao> retorno = new ArrayList<Acao>();
		List<Object[]> result = query.getResultList();

		Acao acao = new Acao();

		for (Object[] object : result) {
			acao = new Acao();
			acao.setId(new Long(object[0].toString()));
			acao.setCodigo(object[1].toString());
			acao.setOrcado(object[2] != null ? new BigDecimal(object[2].toString()) : BigDecimal.ZERO);
			acao.setExecutado(object[3] != null ? new BigDecimal(object[3].toString()) : BigDecimal.ZERO);
			acao.setReceita(object[4] != null ? new BigDecimal(object[4].toString()) : BigDecimal.ZERO);
			acao.setSaldo(acao.getOrcado().subtract(acao.getExecutado()).add(acao.getReceita()));
			retorno.add(acao);
		}	

		return acao.getSaldo();
	}

	
	
	public List<Acao> getTodasAcoes(){
		String jpql = "from Acao";
		Query query =  manager.createQuery(jpql);
		return query.getResultList();
	}

	public List<Acao> verificarCodigo(Acao acao) {
		// String jpql = "from Acao a where a.codigo = :codigo and a.id != :id";
		StringBuilder jpql = new StringBuilder("from  Acao a where a.codigo = :codigo");

		if (acao.getId() != null) {
			jpql.append(" and a.id != :id");
		}

		Query query = manager.createQuery(jpql.toString());
		query.setParameter("codigo", acao.getCodigo());

		if (acao.getId() != null) {
			query.setParameter("id", acao.getId());
		}

		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Acao>();
	}

	public List<Acao> getAcaoAutoComplete(String acao) {
		StringBuilder jpql = new StringBuilder("from  Acao a where lower(a.codigo) like lower(:codigo)");
		jpql.append(" and a.desativado = :desativado ");
		jpql.append(" order by a.codigo");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("codigo", "%" + acao + "%");
		query.setParameter("desativado", false);
		query.setMaxResults(20);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Acao>();
	}
	
	public List<Acao> getAcaoAutoCompleteRECLASSIFICACAO(String acao) {
		StringBuilder jpql = new StringBuilder("from  Acao a where lower(a.codigo) like lower(:codigo)");
		//jpql.append(" and a.desativado = :desativado ");
		jpql.append(" order by a.codigo");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("codigo", "%" + acao + "%");
		//query.setParameter("desativado", false);
		query.setMaxResults(20);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Acao>();
	}

	public void remover(Acao acao) {
		this.manager.remove(this.manager.find(Acao.class, acao.getId()));
	}
	
	
	public void desativarAcoes(Long projeto) {
		
		String hql = "update acao set desativado = :desativado where projeto_id = :projeto";
		Query query = this.manager.createNativeQuery(hql);
		query.setParameter("desativado", true);
		query.setParameter("projeto", projeto);
		query.executeUpdate();
		//this.manager.remove(this.manager.find(Acao.class, acao.getId()));
	}

	// Metodosp pra usar com lazy
	public List<Projeto> filtrados(Filtro filtro) {
		Criteria criteria = criarCriteria(filtro);

		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());

		if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.asc(filtro.getPropriedadeOrdenacao()));
		} else if (filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.desc(filtro.getPropriedadeOrdenacao()));
		}

		return criteria.list();
	}

	private Criteria criarCriteria(Filtro filtro) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Projeto.class);

		if (filtro.getNome() != null) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}

		return criteria;
	}

	public int quantidadeFiltrados(Filtro filtro) {
		Criteria criteria = criarCriteria(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

}
