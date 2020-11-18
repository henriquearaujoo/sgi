package repositorio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Acao;
import model.Coordenadoria;
import model.Gestao;
import model.Projeto;
import model.Regional;
import model.Requisito;
import model.Superintendencia;
import model.Versao;

public class GestaoRepositorio implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private EntityManager manager;

	public GestaoRepositorio() {
	}

	public GestaoRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public Gestao getGestaoPorId(Long id) {
		return this.manager.find(Gestao.class, id);
	}

	public Gestao findByIdGestao(Long id) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Gestao(g.id, g.nome, g.class) from Gestao g where g.id = :id");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? (Gestao) query.getResultList().get(0) : new Gestao();
	}

	public List<Gestao> getGestao() {
		StringBuilder jpql = new StringBuilder("SELECT NEW Gestao(g.id, g.nome, g.class) from Gestao g");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList();
	}

	public List<Superintendencia> buscarSuperintendencia() {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Superintendencia(s.id, s.nome, s.class) from Superintendencia s");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList();
	}

	public List<Coordenadoria> buscarCoordenadoriaBySup(Long id) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Coordenadoria(c.id, c.nome, c.class) from Coordenadoria c where c.superintendencia.id = :id order by c.nome");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", id);
		return query.getResultList();
	}
	
	
	public List<Regional> buscarSubCoodenadoriaByCoord(Long id) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Regional(r.id, r.nome, r.class) from Regional r where r.coordenadoria.id = :id order by r.nome");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", id);
		return query.getResultList();
	}
	
	
	

	public List<Gestao> buscarGestaoAutocomplete(String s) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Gestao(g.id, g.nome, g.class) from Gestao g where lower(g.nome) like lower(:nome)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		return query.getResultList();
	}

	public void salvar(Gestao gestao) {
		this.manager.merge(gestao);
	}
	
	

	public List<Gestao> getSuperintendencia() {
		StringBuilder jpql = new StringBuilder("from  Superintendencia");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Gestao>();
	}

	public List<Gestao> getCoordenadoria() {
		StringBuilder jpql = new StringBuilder("from  Coordenadoria");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Gestao>();
	}

	public List<Regional> getRegionais() {
		StringBuilder jpql = new StringBuilder("from Regional");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Gestao>();
	}
	
	public List<Gestao> getRegional() {
		StringBuilder jpql = new StringBuilder("from Regional");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Gestao>();
	}
	
	public List<Gestao> getGerencia() {
		StringBuilder jpql = new StringBuilder("from  Gerencia");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Gestao>();
	}

	/*
	 * public List<Acao> getAcoes(Projeto projeto){ StringBuilder jpql = new
	 * StringBuilder("from  Acao a  where 1 = 1 and a.projeto = :projeto ");
	 * Query query = manager.createQuery(jpql.toString());
	 * query.setParameter("projeto", projeto);
	 * 
	 * return query.getResultList().size() > 0 ? query.getResultList() : (List)
	 * new ArrayList<Projeto>(); }
	 */

	/*
	 * public List<Acao> getAcoesIsoladas(Projeto projeto){ StringBuilder jpql =
	 * new StringBuilder("SELECT a.id"); jpql.append(",a.codigo"); jpql.
	 * append(",(select  sum(af.valor) from AcaoFonte af where af.acao.id = a.id) as orcado "
	 * ); jpql.append(" from Acao as a where a.projeto = :projeto");
	 * 
	 * TypedQuery<Object[]> query = manager.createQuery(jpql.toString(),
	 * Object[].class); query.setParameter("projeto", projeto); List<Acao>
	 * retorno = new ArrayList<Acao>(); List<Object[]> result =
	 * query.getResultList(); Acao acao = new Acao();
	 * 
	 * for (Object[] object : result) { acao = new Acao(); acao.setId(new
	 * Long(object[0].toString())); acao.setCodigo(object[1].toString());
	 * acao.setOrcado(object[2] != null ? new BigDecimal(object[2].toString()) :
	 * BigDecimal.ZERO); retorno.add(acao); }
	 * 
	 * return retorno; }
	 */

	/*
	 * public List<Acao> verificarCodigo(Acao acao){ String jpql =
	 * "from  Acao a where a.codigo = :codigo and a.id != :id"; Query query =
	 * manager.createQuery(jpql); query.setParameter("codigo",
	 * acao.getCodigo()); query.setParameter("id", acao.getId()); return
	 * query.getResultList().size() > 0 ? query.getResultList() : (List) new
	 * ArrayList<Projeto>(); }
	 */

	public void remover(Acao acao) {
		this.manager.remove(this.manager.merge(acao));
	}

	public List<Gestao> findAll() {
		StringBuilder jpql = new StringBuilder("from Gestao g");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList();
	}

	public Gestao salvarGestao(Gestao gestao) {
		return this.manager.merge(gestao);
//		if(gestao.getId() == null) 
//			
//		else
//			return this.manager.merge(findById(gestao.getId()));
	}

	public Gestao findById(Long id) {
		String hql = "SELECT r FROM Gestao r WHERE r.id = :pGestaoId";
		TypedQuery<Gestao> query = manager.createQuery(hql, Gestao.class);
		query.setParameter("pGestaoId", id);
		return  query.getSingleResult();
	}
	
	public Regional findByIdRegional(Long id) {
		String hql = "SELECT r FROM Regional r WHERE r.id = :pRegionalId";
		TypedQuery<Regional> query = manager.createQuery(hql, Regional.class);
		query.setParameter("pRegionalId", id);
		return  query.getSingleResult();
	}
	
	
	public Gestao findByColaborador(Long id) {
		String hql = "SELECT g FROM Gestao g WHERE g.colaborador.id = :id_col";
		TypedQuery<Gestao> query = manager.createQuery(hql, Gestao.class);
		query.setParameter("id_col", id);
		
		return  query.getResultList().size() > 0 ? query.getResultList().get(0) : null;
	}

	public Boolean verificaGestao(Long id) {
		String hql = "SELECT g FROM Gestao g WHERE g.colaborador.id = :id_col";
		TypedQuery<Gestao> query = manager.createQuery(hql, Gestao.class);
		query.setParameter("id_col", id);
		
		return  query.getResultList().size() > 0 ? true : false;
	}

}
