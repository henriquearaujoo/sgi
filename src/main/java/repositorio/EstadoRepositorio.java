package repositorio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import model.Estado;
import model.Projeto;
import util.Filtro;

public class EstadoRepositorio implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

	public EstadoRepositorio() {
	}

	
	public EstadoRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public Estado getEstadoPorId(Long id) {
		return this.manager.find(Estado.class, id);
	}

	
	public List<Projeto> verificarNomeProjeto(Projeto projeto){
		String jpql = "from  Projeto p where p.nome = :nome and p.id != :id";
		Query query = manager.createQuery(jpql);
		query.setParameter("nome", projeto.getNome());
		query.setParameter("id", projeto.getId());
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<Projeto>();
	}
	
	public void salvar(Projeto projeto) {
		this.manager.merge(projeto);
	}
	
	public List<Estado> getEatados(Filtro filtro){
		StringBuilder jpql = new StringBuilder("from Estado");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0  ? query.getResultList() : new ArrayList<Estado>();
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
