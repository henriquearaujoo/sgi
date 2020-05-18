package geradoresTabela;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import model.UnidadeConservacao;
import util.JpaUtil;

public class GerarComunidadeFabio{

	
	public static void main(String[] args){
		EntityManager manager = JpaUtil.getEntityManager();
		EntityTransaction tx = manager.getTransaction();
		tx.begin();
		
		
		
	}
	
	public static void Criar(EntityManager manager){
		
		try{
			
				FileInputStream stream = new FileInputStream("/home/odin/Documentos/deve/fornecedores.csv");
				InputStreamReader reader = new InputStreamReader(stream);
				BufferedReader br = new BufferedReader(reader);
				
				int cont = 0;
				
				while(br.ready()){
					
					
					String linha[] = br.readLine().split(";");
					String unidadeCon = linha[0] != null ? linha[0] : "";
					String municipio = linha[1] != null ? linha[1] : "";	
					String setor = linha[2] != null ? linha[2] : "";
					String comunidade = linha[3] != null ? linha [3] : "";  
					String nFamilia =	linha[4] != null ? linha [4] : "";	
					String nPessoas = linha[5] != null ? linha [5] : "";
					
					
					if(cont > 0){
						UnidadeConservacao unidade = getUnidadeConservacao(manager, unidadeCon);
						
						
					}
					
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

		Query query = manager.createQuery("from localidade where lower(nome) like lower(:nome)");
		
		query.setParameter("nome", "%"+nome+"%");
		
		UnidadeConservacao unidadeConservacao = (UnidadeConservacao) query.getResultList().get(0);
	
		return unidadeConservacao;
	
	}
	
	
	
}
