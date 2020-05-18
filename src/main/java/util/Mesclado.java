package util;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import model.ContaBancaria;
import model.Fornecedor;
import repositorio.FornecedorRepositorio;

public class Mesclado {

	
	
	
	 
	private Fornecedor fornecedor;
	
	private static FornecedorRepositorio fornecedorRepositorio;	
	
	
	
	
	public static void main(String[] args){
		
		EntityManager manager = JpaUtil.getEntityManager();
		
		EntityTransaction tx = manager.getTransaction();
	    tx.begin();
		
	    GeraContasBancarias(manager);
	    System.out.println("Banco populado!!!");
		
		tx.commit();
		manager.close();
		
	}
	
	
	private static void GeraContasBancarias(EntityManager manager){
		
		List<Fornecedor> fornecedores = new ArrayList<Fornecedor>();
		
		
		StringBuilder jpql = new StringBuilder("from  Fornecedor");
		Query query = manager.createQuery(jpql.toString());
		
		fornecedores = query.getResultList(); 
	
		
		for(Fornecedor f : fornecedores){
			
			System.out.println(f.getNomeFantasia());
			
			String banco = f.getBanco();
			String nomeFantasia = f.getNomeFantasia();
			String conta = f.getConta();
			String nomeConta = nomeFantasia +" - "+ conta;
			String agencia = f.getAgencia();
			
			ContaBancaria contaBancaria = new ContaBancaria();
			
			contaBancaria.setNomeBanco(banco);
			contaBancaria.setNomeConta(nomeConta);
			contaBancaria.setNumeroAgencia(agencia);
			contaBancaria.setNumeroConta(conta);
			contaBancaria.setFornecedor(new Fornecedor());
			contaBancaria.setFornecedor(f);
			
			manager.persist(contaBancaria);
			
		}
		
		
		
	}
	
	
	
	
}
