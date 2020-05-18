package repositorio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
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
import org.jboss.weld.resolution.TypeSafeObserverResolver;
import org.omg.CosNaming.BindingIteratorPOA;

import anotacoes.Transactional;
import model.Acao;
import model.CadeiaProdutiva;
import model.Componente;
import model.Projeto;
import util.Filtro;

public class CadeiaRepositorio {
	
	@Inject	
	private EntityManager manager;

	public CadeiaRepositorio() {
	}


	public CadeiaRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public CadeiaProdutiva getCadeiaPorId(Long id) {
		return this.manager.find(CadeiaProdutiva.class, id);
	}

	
	public void salvar(CadeiaProdutiva paic) {
		this.manager.merge(paic);
	}
	
	public List<CadeiaProdutiva> getCadeias(Componente componente){
		StringBuilder jpql = new StringBuilder("from  CadeiaProdutiva c where c.componente = :componente");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("componente", componente);
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<Projeto>();
	}
	
	public List<CadeiaProdutiva> getCadeias(){
		StringBuilder jpql = new StringBuilder("from  CadeiaProdutiva order by nome");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<Projeto>();
	}
	
	public List<Acao> getAcoesIsoladas(Projeto projeto){
		StringBuilder jpql = new StringBuilder("SELECT a.id");
					               jpql.append(",a.codigo");
					               jpql.append(",(select  sum(af.valor) from AcaoFonte af where af.acao.id = a.id) as orcado ");
					               jpql.append(" from Acao as a where a.projeto = :projeto");
				
		 TypedQuery<Object[]> query =  manager.createQuery(jpql.toString(), Object[].class);
		 query.setParameter("projeto", projeto);
		 List<Acao> retorno =  new ArrayList<Acao>();
		 List<Object[]> result = query.getResultList();
	 	 Acao acao =  new Acao();
		
		for (Object[] object : result) {
		    acao = new Acao();
			acao.setId(new Long(object[0].toString()));
		    acao.setCodigo(object[1].toString());
		    acao.setOrcado(object[2] != null ? new BigDecimal(object[2].toString()) : BigDecimal.ZERO);
		    retorno.add(acao);
		 }
		
		return retorno;
	}
	
	public List<Acao> verificarCodigo(Acao acao){
		String jpql = "from  Acao a where a.codigo = :codigo and a.id != :id";
		Query query = manager.createQuery(jpql);
		query.setParameter("codigo", acao.getCodigo());
		query.setParameter("id", acao.getId());
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<Projeto>();
	}

	
	public void remover(Projeto projeto){
		this.manager.remove(this.manager.merge(projeto));
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
