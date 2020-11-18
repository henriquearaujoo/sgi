
package repositorio;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Cargo;
import model.Evento;
import model.FontePagadora;

public class CargoRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private EntityManager manager;

	public CargoRepositorio() {
	}

	public CargoRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public Cargo findById(Long id) {
		return this.manager.find(Cargo.class, id);
	}

	public List<Cargo> getCargos() {
		String jpql = "from Cargo";
		Query query = this.manager.createQuery(jpql);
		return query.getResultList();
	}

	public void salvar(Cargo cargo) {
		this.manager.merge(cargo);
	}

	public void remover(Cargo cargo) {
		this.manager.remove(this.manager.find(Cargo.class, cargo.getId()));
	}

}
