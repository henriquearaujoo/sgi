package repositorio;

import java.util.List;

import javax.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Colaborador_Relatorio;
import model.RelatorioCampo;

public class ColaboradorRelatorioRepositorio {

	
	@Inject 
	private EntityManager manager;
	
	
	public ColaboradorRelatorioRepositorio()
	{
	}
		
	public ColaboradorRelatorioRepositorio(EntityManager manager)
	{
		this.manager = manager;
	}
	
	
	public void Salvar(Colaborador_Relatorio colaboradorRelatorio)
	{
		this.manager.merge(colaboradorRelatorio);
	}
	
	
	public Colaborador_Relatorio getCRelatorioById(Long id)
	{
		return this.manager.find(Colaborador_Relatorio.class, id);
	}
	
	public List<Colaborador_Relatorio> getColaboradorRelatorio(Colaborador_Relatorio colaboradores)
	{
		StringBuilder jpql = new StringBuilder("from Colaborador_Relatorio");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList();
		
	}
	
	public List<Colaborador_Relatorio> getColaboradesByRelatorio(RelatorioCampo relatorio)
	{
		Query query = manager.createQuery("FROM Colaborador_Relatorio c WHERE  c.relatorioCampo = :relatorio ");
		query.setParameter("relatorio", relatorio);
		
		List<Colaborador_Relatorio> colaboradores = query.getResultList();
		
		return colaboradores; 
	}
	
	
	
}
