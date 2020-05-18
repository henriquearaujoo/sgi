package repositorio;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import model.Estado;
import model.Metas;
import model.Objetivo;

public class ObjetivoRepositorio {

	@Inject	
	private EntityManager manager;
	
	public ObjetivoRepositorio() {}
	
	@Transactional
	public void salvar(Objetivo objetivo) {
		this.manager.merge(objetivo);
	}
	
	@Transactional
	public void update(Objetivo objetivo) {
		this.manager.merge(objetivo);
	}

	public Objetivo getId(Long id) {
		return this.manager.find(Objetivo.class, id);
	}

	public List<Objetivo> findObjetivoByIdProjeto(Long id) {
		String jpql = "from Objetivo o where o.projeto.id = :id order by o.nome ";
		Query query = manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}
	
	
	
	public void remover(Objetivo objetivo){
		this.manager.remove(this.manager.find(Objetivo.class, objetivo.getId()));
	}
}
