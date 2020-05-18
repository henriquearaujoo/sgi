package repositorio;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import anotacoes.Transactional;
import model.CategoriaDespesaClass;
import model.ContaBancaria;
import model.Metas;
import model.Objetivo;
import model.Projeto;

public class MetasRepositorio {

	@Inject	
	private EntityManager manager;
	
	public MetasRepositorio () {}

	public void salvar(Metas metas) {
		manager.merge(metas);
	}
	
	public void update(Metas metas) {
		manager.merge(metas);
	}

	public List<Metas> findMetasByObjetivo(Objetivo objetivo) {
		String jpql = "from  Metas m where m.objetivo.id = :idObjetivo order by m.nome ";
		Query query = manager.createQuery(jpql);
		query.setParameter("idObjetivo", objetivo.getId());
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<Metas>();
		
	}

	public List<Metas> findMetasByIdProjeto(Long id) {
		String jpql = "from  Metas m where m.objetivo.projeto.id = :idProjeto order by m.nome ";
		Query query = manager.createQuery(jpql);
		query.setParameter("idProjeto", id);
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<Metas>();
	}

	public Metas findById(Long id) {
		return this.manager.find(Metas.class, id);
	}
	
	@Transactional
	public void remover(Metas metas){
		this.manager.remove(this.manager.find(Metas.class, metas.getId()));
	}
	
}
