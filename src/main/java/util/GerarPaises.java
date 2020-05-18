package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import model.Cidade;
import model.Estado;
import model.Municipio;
import model.Pais;

public class GerarPaises {

	
	public static void main(String args[]){
		
		
		EntityManager manager = JpaUtil.getEntityManager();
		EntityTransaction tx = manager.getTransaction();
		tx.begin();
		
		//GerarPais(manager);
		
		//GerarCidade(manager);
		
		tx.commit();
		manager.close();
		
	}
	
	
	public static void GerarPais(EntityManager manager){
		
		try {
		
			FileInputStream stream = new FileInputStream("C:/OK/Paises.csv");
		    InputStreamReader reader = new InputStreamReader(stream);
			BufferedReader br = new BufferedReader(reader);
			
			int cont = 0;
			
			while(br.ready()){
					
				String linha[] = br.readLine().split(";");
				String Pais = linha[0] != null ? linha[0] : "";
				System.out.println("Pais:" + Pais);
				String municipio = linha[1] != null ? linha[1] : "";
				System.out.println("Capital:" + municipio);
				
				Pais pais = new Pais();
				
				pais.setNome(Pais);
				manager.persist(pais);
				
			}
		
			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("file error");	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("outro error");
		}
		
	}

	public static void GerarCidade(EntityManager manager){
		try {
			
			
			FileInputStream stream = new FileInputStream("C:/OK/Paises.csv");
			//FileInputStream stream = new FileInputStream("/home/developer/Documentos/doc/bd/municipios.csv");
			InputStreamReader reader = new InputStreamReader(stream);
			BufferedReader br = new BufferedReader(reader);
			while (br.ready()) {
				
				
				String linha[] = br.readLine().split(";");
				String Pais = linha[0] != null ? linha[0] : "";
				System.out.println("Pais:" + Pais);
				
				String municipio = linha[1] != null ? linha[1] : "";
				System.out.println("Capital:" + municipio);
				
				Pais pais = getPais(manager, linha[0]);
				
				
				
				Cidade cidade = new Cidade();
				cidade.setMascara(municipio);
				cidade.setNome(municipio);
				cidade.setPais(pais);
				manager.persist(cidade);
				//System.out.println("Estado: "+estado);
			}
			
			
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	public static Pais getPais(EntityManager manager, String nome){
		Query query = manager.createQuery("from Pais where lower(nome) like lower(:nome)");
		query.setParameter("nome", "%"+nome+"%");
		Pais pais = (Pais) query.getResultList().get(0);
		return pais;
		
	}
	
	
	

}
