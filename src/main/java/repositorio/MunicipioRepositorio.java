package repositorio;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import anotacoes.Transactional;
import model.Municipio;

public class MunicipioRepositorio {

	private EntityManager manager;

	public MunicipioRepositorio() {
	}

	@Inject
	public MunicipioRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public Municipio findById(Long id) {
		return this.manager.find(Municipio.class, id);
	}

	public Municipio findByIdMunicipio(Long idMunicipio) {
		return this.manager.find(Municipio.class, idMunicipio);
	}

	@Transactional
	public void update(Municipio municipio) {
		this.manager.merge(municipio);
	}

	@Transactional
	public Boolean delete(Municipio municipio) {
		try {
			this.manager.remove(this.manager.find(Municipio.class, municipio.getId()));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void salvar(Municipio municipio) {
		this.manager.merge(municipio);
	}

	@SuppressWarnings("unchecked")
	public List<Municipio> getListMunicipios() {
		String jpql = "from Municipio m order by m.nome ASC";
		Query query = manager.createQuery(jpql);
		return query.getResultList();
	}
	
//	@SuppressWarnings("unchecked")
//	public List<Municipio> ucAutocomplete(String queryStr) {
//		String jpql = "from Municipio uc where lower(uc.nome) like :nome";
//		Query query = manager.createQuery(jpql);
//		query.setParameter("nome", "%" + queryStr.toLowerCase() + "%");
//		return query.getResultList();
//	}
}
