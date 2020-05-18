package geradoresTabela;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import model.ContaBancaria;
import model.Estado;
import util.JpaUtil;

public class GerarMergeEmConta {

	public static void main(String[] args){
		
		System.out.println("hello world");
		EntityManager manager = JpaUtil.getEntityManager();
	    EntityTransaction tx = manager.getTransaction();
	    tx.begin();
		
	    
	    criarFornecedor(manager);
        
    	System.out.println("Banco populado!!!");
        
    	//criarComunidade(manager);
    
    	tx.commit();
    	manager.close();
    	
    	
	
	
	}
	
	
	
	
	public static void criarFornecedor(EntityManager manager){
		try {
			
			
			/*A classe Fornecedor precisa ser modificada pois esta faltando alguns campos relativos ao formulario enviado*/
			/* Vou deixar ao teu criterio oque deve ser adicionado, no entanto ja mapeei o csv segue a baixo o codigo*/
			
			//FileInputStream stream = new FileInputStream("C:/DEV/fornecedores.csv");
			//FileInputStream stream = new FileInputStream("");
			
			
			FileInputStream stream = new FileInputStream("/home/developer/Documentos/doc/bd/fornecedores.csv");
			InputStreamReader reader = new InputStreamReader(stream);
			BufferedReader br = new BufferedReader(reader);
			
			int cont = 0;
			
			
			List<ContaBancaria> contas = manager.createQuery("from ContaBancaria where id > 1911").getResultList();
			
			for (ContaBancaria contaBancaria : contas) {
				manager.merge(contaBancaria);
			}
			
			
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("file error");	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("outro error");
		}
	}

	
	public static Estado getEstado(EntityManager manager, String nome){
		Query query = manager.createQuery("from Estado where lower(nome) like lower(:nome)");
		query.setParameter("nome", "%"+nome+"%");
		Estado estado = (Estado) query.getResultList().get(0);
		return estado;		
	}
	
	
}