package repositorio;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Projeto;
import model.TipoViagem;

public class TipoViagemRepositorio {

	@Inject 
	private EntityManager manager;
	
	public TipoViagemRepositorio(){
		
	}
	
	public TipoViagemRepositorio(EntityManager manager){
		this.manager = manager;
	}
	
	
	public TipoViagem TipoViagemPorId(Long id){
		return this.manager.find(TipoViagem.class, id);
	} 
	
	public List<TipoViagem> getTipoViagem(){
		StringBuilder jpql = new StringBuilder("from TipoViagem");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<TipoViagem>();
	}
	
	public void Salvar(TipoViagem tipoViagem){
		this.manager.merge(tipoViagem);
	}
	
	
}
