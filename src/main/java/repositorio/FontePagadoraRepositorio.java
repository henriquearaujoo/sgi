package repositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TransactionRequiredException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.postgresql.util.PSQLException;

import model.Evento;
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
	
	public List<FontePagadora> filtroFontePagadora(Filtro filtro) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("from FontePagadora as fp where fp.id = :font_id");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter(":font_id", filtro.getFontePagadora().getId());
		return query.getResultList();
	}

	public FontePagadora getFontePorId(Long id) {
		return this.manager.find(FontePagadora.class, id);
	}

	public void salvar(FontePagadora fontePagadora) {
		this.manager.merge(fontePagadora);
	}
	
	public Integer maxFontes() {
		StringBuilder jpql = new StringBuilder("SELECT MAX(fp.id) FROM FontePagadora as fp");
		Query query = manager.createQuery(jpql.toString());
		return query.getMaxResults(); 
	}
	
	public List<FontePagadora> findAll(){
		StringBuilder jpql = new StringBuilder("from FontePagadora a order by id asc");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList();
	}
	
	public List<FontePagadora> getFontes(){
		StringBuilder jpql = new StringBuilder("from  FontePagadora");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<FontePagadora>();
	}
	
	public List<FontePagadora> filtroParceiro(FontePagadora fontePagadora) {
		StringBuilder jpql = new StringBuilder(
				"SELECT New FontePagadora(fp.id, fp.nome, fp.cnpj, fp.razaoSocial) from FontePagadora fp where 1 = 1"
		);
		
		if (!fontePagadora.getNome().equals("")) {
			jpql.append(" and lower(fp.nome) = lower(:nome)");
		}
		
		if (!fontePagadora.getRazaoSocial().equals("")) {			
			jpql.append(" and lower(fp.razaoSocial) = lower(:razaoSocial)");
		}
		
		if (!fontePagadora.getCnpj().equals("")) {			
			jpql.append(" and lower(fp.cnpj) like lower(:cnpj)");
		}
		
		Query query = manager.createQuery(jpql.toString());
		
		if (!fontePagadora.getNome().equals("")) {
			query.setParameter("nome", fontePagadora.getNome());			
		}
		
		if (!fontePagadora.getRazaoSocial().equals("")) {
			query.setParameter("razaoSocial", fontePagadora.getRazaoSocial());			
		}
		
		if (!fontePagadora.getCnpj().equals("")) {			
			query.setParameter("cnpj", fontePagadora.getCnpj());
		}
		
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<FontePagadora>();
	}
	
	
	public List<FontePagadora> getFonteAutoComplete(String fonte){
		StringBuilder jpql = new StringBuilder("from  FontePagadora f where lower(f.nome) like lower(:nome)");	
		jpql.append(" order by f.nome ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%"+fonte+"%");
		query.setMaxResults(20);
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<FontePagadora>();
	}
	
	public List<FontePagadora> getCnpjAutoComplete(String cnpj){
		StringBuilder jpql = new StringBuilder("from  FontePagadora f where lower(f.cnpj) like lower(:cnpj)");	
		jpql.append(" order by f.nome ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("cnpj", "%"+cnpj+"%");
		query.setMaxResults(20);
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<FontePagadora>();
	}
	
	public List<FontePagadora> getRazaoSocialAutoComplete(String razaoSocial){
		StringBuilder jpql = new StringBuilder("from  FontePagadora f where lower(f.razaoSocial) like lower(:razaoSocial)");	
		jpql.append(" order by f.nome ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("razaoSocial", "%"+razaoSocial+"%");
		query.setMaxResults(20);
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<FontePagadora>();
	}
	
	public void remover(FontePagadora fontePagadora){
		this.manager.remove(this.manager.find(FontePagadora.class, fontePagadora.getId()));
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
