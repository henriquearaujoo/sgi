package repositorio;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import model.Perfil;

public class PerfilRepositorio {

	@Inject	
	private EntityManager manager;

	public PerfilRepositorio() {
	}

	public PerfilRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public Perfil getPerfil(Long id) {
		return this.manager.find(Perfil.class, id);
	}
	
}
