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

import model.Acao;
import model.AcaoFonte;
import model.Projeto;
import util.Filtro;

public class AcaoFonteRepositorio {
	
	@Inject	
	private EntityManager manager;

	public AcaoFonteRepositorio() {
	}


	public AcaoFonteRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public void salvar(AcaoFonte acaoFonte) {
		this.manager.merge(acaoFonte);
	}
	
	public List<AcaoFonte> getAcaoFonte(Acao acao){
		StringBuilder jpql = new StringBuilder("from  AcaoFonte a  where 1 = 1 and a.acao = :acao ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("acao", acao);
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<Acao>();
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
