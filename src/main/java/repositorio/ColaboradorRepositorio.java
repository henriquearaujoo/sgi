package repositorio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Cargo;
import model.Colaborador;
import model.ContaBancaria;
import model.Endereco;
import model.Gestao;
import model.Localidade;
import model.StatusConta;
import util.Arquivo;
import util.Filtro;

public class ColaboradorRepositorio {
	@Inject	
	private EntityManager manager;
	
	public ColaboradorRepositorio() {
	}
	
	public ColaboradorRepositorio(EntityManager manager) {
		this.manager = manager;
	}
	
	public Arquivo load(Long id){
		StringBuilder jpql = new StringBuilder("FROM Arquivo a WHERE a.id = :Pid");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("Pid", id);
		return (Arquivo) query.getSingleResult();
	}
	
	public void salvar(Colaborador colaborador) {
		this.manager.merge(colaborador);
	}
	
	public Colaborador getColaboradorById(Long id){
		return manager.find(Colaborador.class, id);
	}
	
	public void remover(Colaborador colaborador){
		this.manager.remove(this.manager.merge(colaborador));
	}

	public List<Colaborador> findAll(){
		StringBuilder jpql = new StringBuilder("from Colaborador a");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList();
	}
	
	public List<Colaborador>findByFiltro(Filtro filtro){
		StringBuilder jpql = new StringBuilder("SELECT NEW Colaborador(c.id,c.nome,c.rg,c.pis,c.email,c.cpf) from Colaborador c where 1 = 1");
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
	
	public List<Colaborador> buscarColaborador(String s){
		StringBuilder jpql = new StringBuilder("SELECT NEW Colaborador(c.id, c.nome) from Colaborador c where 1 = 1");
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
	
	public List<Cargo> findCargo() {
		StringBuilder jpql = new StringBuilder("from Cargo k");
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

	public Colaborador findColaboradorById(Long colaborador) {
		StringBuilder jpql = new StringBuilder("FROM Colaborador c WHERE c.id = :Pcolaborador");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("Pcolaborador", colaborador);
		return (Colaborador) query.getSingleResult();
	}

	

}