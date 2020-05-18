package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import model.Comunidade;
import model.Municipio;
import model.UnidadeConservacao;

public class GerarComunidade {

	public static void main(String[] args){
		EntityManager manager = JpaUtil.getEntityManager();
		EntityTransaction tx = manager.getTransaction();
		tx.begin();
		
		criarComunidade(manager);
		
		tx.commit();
		manager.close();
	}
	
	public static void criarComunidade(EntityManager manager){
		
		try{
			
				FileInputStream stream = new FileInputStream("/home/odin/Documentos/deve/unidades/mamiraua.csv");
				InputStreamReader reader = new InputStreamReader(stream);
				BufferedReader br = new BufferedReader(reader);
				
				int cont = 0;
																																																																											
				while(br.ready()){																									
					
					
					String linha[] = br.readLine().split(",");
					String unidadeCon = linha[0] != null ? linha[0] : "";
					System.out.println("unidade:" + linha[0]  + "!");
					String municipio = linha[1] != null ? linha[1] : "";
					System.out.println("municipio:" + linha[1]);
					String setor = linha[2] != null ? linha[2] : "";
					String  comunidade = linha[3] != null ? linha [3] : "";  
					Integer nFamilia =	linha[4] != null ? Integer.valueOf(linha[4]) : null;
					System.out.println("nFamilia:" + linha[4]);
					Integer nPessoas = linha[5] != null ? Integer.valueOf(linha[5]) : null;
					System.out.println("nPessoas:" + linha[5]);
					
					
						UnidadeConservacao unidade = getUnidadeConservacao(manager, unidadeCon);
						Municipio nMunicipio = getMunicipio(manager, municipio);
						
						Comunidade ncomunidade = new Comunidade();
						System.out.println("unidade salva:");
						ncomunidade.setUnidadeConservacao(unidade);
						ncomunidade.setNome(comunidade);
						ncomunidade.setMunicipio(nMunicipio);
						ncomunidade.setSetor(setor);
						ncomunidade.setnPessoa(nPessoas);
						ncomunidade.setNfamilia(nFamilia);
						manager.persist(ncomunidade);
				
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
	
	
	public static UnidadeConservacao getUnidadeConservacao(EntityManager manager, String nome){

		Query query = manager.createQuery("from UnidadeConservacao where lower(nome) like lower(:nome)");
		
		query.setParameter("nome", "%"+nome+"%");
		
		UnidadeConservacao unidadeConservacao = (UnidadeConservacao) query.getResultList().get(0);
	
		return unidadeConservacao;
	
	}
	
	public static Municipio getMunicipio(EntityManager manager, String nome){
		Query query = manager.createQuery("from Municipio where lower(nome) like lower(:nome)");
		
		query.setParameter("nome", "%"+nome+"%");
		
		Municipio municipio = (Municipio) query.getResultList().get(0);
		
		return municipio;
	}
	
	
	
	
}
