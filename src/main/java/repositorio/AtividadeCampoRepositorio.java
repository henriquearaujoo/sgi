package repositorio;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Atividade;
import model.Capacitacoes;
import model.Evento;
import model.Oficina;
import model.RelatorioCampo;
import model.VisitaTecnica;

public class AtividadeCampoRepositorio {

	@Inject 
	private EntityManager manager;
	
	public AtividadeCampoRepositorio(){
		
	}
	
	public AtividadeCampoRepositorio(EntityManager manager){
		this.manager = manager;
	}
	
	public Atividade getAtividadeById(Long id){
		return manager.find(Atividade.class, id);
	}
	
	public Atividade  SalvarAtividade(Atividade atividade){
		
		return this.manager.merge(atividade);
	}
	
	public List<Atividade> getOficina(){
		StringBuilder jpql = new StringBuilder("from Oficina");
		Query query =  manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<Oficina>();
	}
	
	public List<Atividade> getVisitaTecnica(){
		StringBuilder jpql = new StringBuilder("from VisitaTecnica");
		Query query =  manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<VisitaTecnica>();
	}
	
	
	public List<Atividade> getCapacitacao(){
		StringBuilder jpql = new StringBuilder("from Capacitacoes");
		Query query =  manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<Capacitacoes>();
	}
	
	
	public List<Atividade> getEvento(){
		StringBuilder jpql = new StringBuilder("from Evento");
		Query query =  manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<Evento>();
	}
	
	public List<Atividade> getEntregas(){
		StringBuilder jpql = new StringBuilder("from Entregas");
		Query query =  manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<Evento>();
	}
	
		
	public List<Atividade> getAtividadeByRelatorio(RelatorioCampo relatorio){
		
		Query query = manager.createQuery("FROM Atividade a WHERE a.relatorioCampo = :relatorio");
		query.setParameter("relatorio", relatorio);
		
		List<Atividade> atividades = query.getResultList();
		return atividades;
		
	}
	
}
