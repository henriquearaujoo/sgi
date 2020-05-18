package repositorio;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Versao;

public class VersaoRepositorio implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Inject
	private EntityManager manager;
	
	public VersaoRepositorio() {
		
	}
	
	public VersaoRepositorio(EntityManager manager) {
		this.manager = manager;
	}
		
	public Versao salvar(Versao versao) {
		return this.manager.merge(versao);
		
	}
	
	public void remove(Versao versao) {
		manager.remove(manager.find(Versao.class, versao.getId()));
	}
	
	public List<Versao> findAll(){
		StringBuilder jpql = new StringBuilder("from Versao a");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList();
	}
	
	public Versao findById(Long id) {
		StringBuilder jpql = new StringBuilder("from Versao a where a.id = "+id);
		Query query = manager.createQuery(jpql.toString());
		return (Versao) query.getSingleResult();
	}
}
