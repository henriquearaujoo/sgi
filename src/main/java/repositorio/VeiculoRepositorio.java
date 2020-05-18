package repositorio;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Projeto;
import model.Veiculo;

public class VeiculoRepositorio {
	
	@Inject
	private EntityManager manager;
	
	public VeiculoRepositorio(){
		
	}
	
	public VeiculoRepositorio(EntityManager manager){
		this.manager = manager;
	}
	
	public Veiculo getVeiculoPorId(Long id){
		return this.manager.find(Veiculo.class, id);
	}
	
	public void salvar(Veiculo veiculo){
		this.manager.merge(veiculo);
	}
	
	public List<Veiculo> getVeiculos(){
		StringBuilder jpql = new StringBuilder("from Veiculo");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<Veiculo>();
	}
}
