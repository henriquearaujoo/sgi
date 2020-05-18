package repositorio;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Utilidade;
import util.Filtro;

public class UtilidadeRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public UtilidadeRepositorio() {
	}

	public Utilidade salvar(Utilidade utilidade) {
		return this.manager.merge(utilidade);
	}

	public void remover(Utilidade utilidade) {
		this.manager.remove(this.manager.find(Utilidade.class, utilidade.getId()));
	}

	public Utilidade findById(Long id) {
		return this.manager.find(Utilidade.class, id);
	}

	public List<Utilidade> getAll(Filtro filtro) {
		StringBuilder jpql = new StringBuilder("from Utilidade as u where 1 = 1 ");
		
		
		if (filtro.getDescricao() != null && !filtro.getDescricao().equals("")) {
			jpql.append(" and lower(u.descricao) like lower(:desc)");
		}
		
		if (filtro.getCategoriaID() != null) {
			jpql.append(" and u.categoria.id = :id_categoria ");
		}
		
		jpql.append(" order by u.id desc ");
		
		Query query = this.manager.createQuery(jpql.toString());
		
		if (filtro.getDescricao() != null && !filtro.getDescricao().equals("")) {
			query.setParameter("desc", "%" + filtro.getDescricao() + "%" );
		}
		
		if (filtro.getCategoriaID() != null) {
			query.setParameter("id_categoria", filtro.getCategoriaID());
		}
	
		return query.getResultList();
	}
	
	
	public List<Utilidade> getResumeList() {
		StringBuilder jpql = new StringBuilder("SELECT NEW Utilidade(e.id,e.titulo, e.descricao, e.dataInicio, e.dataFim, e.linkReuniao) FROM Utilidade as e ");
			
		jpql.append(" order by e.id desc ");
		
		Query query = this.manager.createQuery(jpql.toString());

		return query.getResultList();
	}

}
