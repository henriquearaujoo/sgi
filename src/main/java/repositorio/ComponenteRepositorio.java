
package repositorio;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.ComponenteClass;
import model.SubComponente;

public class ComponenteRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private EntityManager manager;

	public ComponenteRepositorio() {
	}

	public ComponenteRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public void salvar(ComponenteClass componente) {
		this.manager.merge(componente);
	}
	
	public void remover(ComponenteClass componente){
		this.manager.remove(this.manager.find(ComponenteClass.class, componente.getId()));
	}

	public ComponenteClass findById(Long id) {
		return this.manager.find(ComponenteClass.class, id);
	}

	public SubComponente findByIdSubComponente(Long id) {
		return this.manager.find(SubComponente.class, id);
	}

	public List<ComponenteClass> getComponentes() {
		String jpql = "from ComponenteClass order by id asc";
		Query query = this.manager.createQuery(jpql);
		return query.getResultList();
	}

	public List<SubComponente> getSubComponentes(Long idComponente) {
		String jpql = "from SubComponente where componente.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", idComponente);
		return query.getResultList();

	}

}
