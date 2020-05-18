package repositorio;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Publico;
import model.RelatorioCampo;

public class PublicoRepositorio {

	@Inject
	private EntityManager manager;
	
	
	public PublicoRepositorio(){
		
	}
	
	public PublicoRepositorio(EntityManager manager){
		this.manager = manager;
	}
	
	public Publico getPublicoById(long id){
		return manager.find(Publico.class, id);
	}
	
	public Publico salvarPublico(Publico publico){
		
		return manager.merge(publico);
	}
	
	public List<Publico> publicoById(RelatorioCampo relatorio){
		

		Query query = manager.createQuery("FROM Publico a WHERE a.relatorioCampo = :relatorio");
		query.setParameter("relatorio", relatorio);
		
		List<Publico> publico = query.getResultList();
		return publico;
		
	}


}
