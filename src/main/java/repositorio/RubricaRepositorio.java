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

import model.Acao;
import model.AcaoFonte;
import model.Projeto;
import model.ProjetoRubrica;
import model.Rubrica;
import util.Filtro;

public class RubricaRepositorio implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject	
	private EntityManager manager;

	public RubricaRepositorio() {
	}


	public RubricaRepositorio(EntityManager manager) {
		this.manager = manager;
	}
	
	public ProjetoRubrica getRubricaDeProjeto(Long id) {
		return manager.find(ProjetoRubrica.class, id);
	}

	
	
	public Rubrica salvar(Rubrica rubrica){
		return this.manager.merge(rubrica);
	}
	
	public void remover(Rubrica rubrica){
		this.manager.remove(this.manager.merge(rubrica));
	}
	
	public Rubrica findById(Long id){
		return this.manager.find(Rubrica.class, id);
	}
	
	public List<Rubrica> getRubricas(){
		String jpql = "from Rubrica";
		Query query = this.manager.createQuery(jpql);
		return query.getResultList();
	}
	

}
