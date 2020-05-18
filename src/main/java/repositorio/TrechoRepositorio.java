package repositorio;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Diaria;
import model.Trecho;

public class TrechoRepositorio {

	@Inject 
	private EntityManager manager;
	
	
	public TrechoRepositorio(){
		
	}
	
	public TrechoRepositorio(EntityManager manager){
		this.manager = manager;
	}
	
	public Trecho getTrechoById(Long id){
		return manager.find(Trecho.class, id);
	}
	
	public Trecho salvar(Trecho trecho){
		return manager.merge(trecho);
	}
	
	public Boolean remover(Trecho trecho){
		manager.remove(trecho);
		return true;
	}
	
	
	public List<Trecho> getTrechosByDiaria(Diaria diaria){
		Query query = manager.createQuery("FROM Trecho t WHERE t.diaria = :diaria order by data");
		query.setParameter("diaria", diaria);
		
		List<Trecho> trechos = query.getResultList(); 
		return trechos.isEmpty() ? new ArrayList<>() : trechos;
	}
	
}
