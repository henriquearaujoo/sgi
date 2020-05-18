package repositorio;

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

import model.FontePagadora;
import model.Projeto;
import model.Versao;
import util.Filtro;

public class FontePagadoraRepositorio {
	
	@Inject	
	private EntityManager manager;

	public FontePagadoraRepositorio() {
	}


	public FontePagadoraRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public FontePagadora getFontePorId(Long id) {
		return this.manager.find(FontePagadora.class, id);
	}

	
	public void salvar(FontePagadora fontePagadora) {
		this.manager.merge(fontePagadora);
	}
	
	
	public FontePagadora salvarFonte(FontePagadora fontePagadora) {
		return this.manager.merge(fontePagadora);
		
	}
	
	public List<FontePagadora> findAll(){
		StringBuilder jpql = new StringBuilder("from FontePagadora a");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList();
	}
	
	public List<FontePagadora> getFontes(){
		StringBuilder jpql = new StringBuilder("from  FontePagadora");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<FontePagadora>();
	}
	
	
	public List<FontePagadora> getFonteAutoComplete(String fonte){
		StringBuilder jpql = new StringBuilder("from  FontePagadora f where lower(f.nome) like lower(:nome)");	
		jpql.append(" order by f.nome ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%"+fonte+"%");
		query.setMaxResults(20);
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<FontePagadora>();
	}
	
	
	public void remover(FontePagadora fonte){
		this.manager.remove(this.manager.merge(fonte));
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


	public FontePagadora findById(Long id) {
		StringBuilder jpql = new StringBuilder("from FontePagadora a where a.id = "+id);
		Query query = manager.createQuery(jpql.toString());
		return (FontePagadora) query.getSingleResult();
	}

	
	

}
