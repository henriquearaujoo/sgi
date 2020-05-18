package repositorio;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Requisito;

public class RequisitoRepositorio implements Serializable	{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Inject
	private EntityManager manager;
	
	public RequisitoRepositorio() {
		
	}
	
	public RequisitoRepositorio(EntityManager manager) {
		this.manager = manager;
	}
		
	public void salvar(Requisito requisito) {
		this.manager.merge(requisito);
	}
	
	public void remove(Requisito requisito) {
		manager.remove(manager.find(Requisito.class, requisito.getId()));
	}
	
	public List<Requisito> findAll(){
		StringBuilder jpql = new StringBuilder("from Requisito r");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList();
	}

	

	public List<Requisito> findRequisito(Long id) {
		String hql = "SELECT r FROM Requisito r WHERE r.versao.id = :pVersaoId";
		TypedQuery<Requisito> query = manager.createQuery(hql, Requisito.class);
		query.setParameter("pVersaoId", id);
		return  query.getResultList();
	}
	
	

}
