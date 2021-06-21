package repositorio;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;


import model.ColaboradorVI;
import model.Endereco;
import model.Gestao;
import model.Localidade;
import util.Arquivo;
import util.Filtro;

public class ColaboradorVIRepositorio {
	@Inject	
	private EntityManager manager;
	
	public ColaboradorVIRepositorio() {
	}
	
	public ColaboradorVIRepositorio(EntityManager manager) {
		this.manager = manager;
	}
	
	public Arquivo load(Long id){
		StringBuilder jpql = new StringBuilder("FROM Arquivo a WHERE a.id = :Pid");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("Pid", id);
		return (Arquivo) query.getSingleResult();
	}
	
	public void salvar(ColaboradorVI colaborador) {
		this.manager.merge(colaborador);
	}
	
	public ColaboradorVI getColaboradorById(Long id){
		return manager.find(ColaboradorVI.class, id);
	}
	
	public void remover(ColaboradorVI colaborador){
		this.manager.remove(this.manager.merge(colaborador));
	}

	public List<ColaboradorVI> findAll(Filtro filtro){
		StringBuilder jpql = new StringBuilder("from ColaboradorVI c where 1 = 1");
		
		
		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			jpql.append(" and lower(c.nome) like lower(:nome) ");
		}
		
		
		Query query = manager.createQuery(jpql.toString());
		
		
		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			query.setParameter("nome", "%"+filtro.getNome()+"%");
		}
		
		return query.getResultList();
	}
	
	
	public List<ColaboradorVI>findByFiltro(Filtro filtro){
		StringBuilder jpql = new StringBuilder("SELECT NEW ColaboradorVI(c.id,c.nome,c.rg,c.pis,c.email,c.cpf) from ColaboradorVI c where 1 = 1");
		if(filtro.getNomeColaborador() != null && filtro.getNomeColaborador() !=  "") {
			jpql.append(" and lower(c.nome) like lower(:pNome)");
		}
		if(filtro.getCPF() != null && filtro.getCPF() !=  "") {
			jpql.append(" and c.cpf like :pCpf");
		}
		if(filtro.getDataNascimento()  != null) {
			jpql.append(" and to_char(c.dataDeNascimento,'dd/MM/yyyy') = :pDataNascimento");
		}
		if(filtro.getGestaoColaborador() != null ) {
			jpql.append(" and c.gestao = :pGestao");
		}
		
		
		Query query = manager.createQuery(jpql.toString());
		
		if(filtro.getNomeColaborador() != null && filtro.getNomeColaborador() !=  "") {
			query.setParameter("pNome", "%"+filtro.getNomeColaborador()+"%");
		}
		if(filtro.getCPF() != null && filtro.getCPF() !=  "") {
			query.setParameter("pCpf", "%"+filtro.getCPF()+"%");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(filtro.getDataNascimento()  != null) {
			query.setParameter("pDataNascimento", sdf.format(filtro.getDataNascimento()));
		}
		if(filtro.getGestaoColaborador() != null ) {
			query.setParameter("pGestao", filtro.getGestaoColaborador());
		}
		
		
		
		return query.getResultList();
	}
	
	public List<ColaboradorVI> buscarColaborador(String s){
		StringBuilder jpql = new StringBuilder("SELECT NEW ColaboradorVI(c.id, c.nome) from ColaboradorVI c where 1 = 1");
		jpql.append(" and lower(c.nome) like lower(:nome)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%"+s+"%");
		return query.getResultList();
	}

	public List<Gestao> findGestao() {
		StringBuilder jpql = new StringBuilder("from Gestao g");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList();
	}

	public List<Localidade> findLocalidade() {
		StringBuilder jpql = new StringBuilder("select NEW Localidade(l.id,l.nome) from Localidade l where l.nome <> 'nulo' and l.nome <> 'null'");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList();
	}

	public Endereco findEnderecoByCep(String cep) {
		StringBuilder jpql = new StringBuilder("FROM Endereco e WHERE e.cep = :Pcep");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("Pcep", cep);
		return (Endereco) query.getSingleResult();
	}

	public ColaboradorVI findColaboradorById(Long colaborador) {
		StringBuilder jpql = new StringBuilder("FROM ColaboradorVI c WHERE c.id = :Pcolaborador");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("Pcolaborador", colaborador);
		return (ColaboradorVI) query.getSingleResult();
	}

	

}