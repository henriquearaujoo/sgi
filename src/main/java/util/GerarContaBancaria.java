package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import model.ContaBancaria;
import model.Estado;
import model.Fornecedor;

public class GerarContaBancaria {
	
	
	
	public static void main(String[] args){
	
		EntityManager manager = JpaUtil.getEntityManager();
	    EntityTransaction tx = manager.getTransaction();
	    tx.begin();
		criarConta(manager);
		tx.commit();
		manager.close();
	}
	
	public static void criarConta(EntityManager manager){
		try {
			
		
			FileInputStream stream = new FileInputStream("/home/developer/Documentos/doc/bd/contas.csv");
			InputStreamReader reader = new InputStreamReader(stream);
			BufferedReader br = new BufferedReader(reader);
			
			int cont = 0;
			
			
			while (br.ready()) {
				
				String linha[] = br.readLine().split(";");
				String tipo = linha[0] != null ? linha[0] : "";
				String banco = linha[1] != null ? linha[1] : "";
				String agencia = linha[2] != null ? linha[2] : "";
				String nomeConta = linha[3] != null ? linha[3] : "";
				
				ContaBancaria conta = new ContaBancaria();
				conta.setTipo(tipo);
				conta.setNomeBanco(banco);
				conta.setNumeroAgencia(agencia);
				conta.setNomeConta(nomeConta);
				conta.setSaldoAtual(BigDecimal.ZERO);
				conta.setSaldoAtual(BigDecimal.ZERO);
		
				manager.persist(conta);
			}
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
	
	
	public static Estado getEstado(EntityManager manager, String nome){
		Query query = manager.createQuery("from Estado where lower(nome) like lower(:nome)");
		query.setParameter("nome", "%"+nome+"%");
		Estado estado = (Estado) query.getResultList().get(0);
		return estado;		
	}


}
