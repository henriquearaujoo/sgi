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

	public void salvar(SubComponente subcomponente) {
		this.manager.merge(subcomponente);
	}

	public void remover(ComponenteClass componente) {
		this.manager.remove(this.manager.find(ComponenteClass.class, componente.getId()));
	}

	public void remover(SubComponente subcomponente) {
		this.manager.remove(this.manager.find(SubComponente.class, subcomponente.getId()));
	}

	public ComponenteClass findById(Long id) {
		return this.manager.find(ComponenteClass.class, id);
	}

	public SubComponente findByIdSubComponente(Long id) {
		return this.manager.find(SubComponente.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<ComponenteClass> findAllComponente() {
		String jpql = "SELECT NEW ComponenteClass(c.id, c.nome) from ComponenteClass c order by c.id asc";
		Query query = this.manager.createQuery(jpql);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<SubComponente> findAllSubcomponente() {
		String jpql = "SELECT NEW SubComponente(s.id, s.nome, s.componente.id, s.componente.nome) from SubComponente s order by s.id asc";
		Query query = this.manager.createQuery(jpql);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ComponenteClass> buscarComponenteAutocomplete(String s) {
		String jpql = "SELECT NEW ComponenteClass(g.id, g.nome) from ComponenteClass g where lower(g.nome) like lower(:nome)";
		Query query = manager.createQuery(jpql);
		query.setParameter("nome", "%" + s + "%");
		return query.getResultList();
	}

}
