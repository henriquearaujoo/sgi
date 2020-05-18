package util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import model.Colaborador;
import model.Perfil;
import model.User;

public class GerarUsuarios {
		
	
	public static void main(String[] args){
		
			EntityManager manager = JpaUtil.getEntityManager();
			
			EntityTransaction tx = manager.getTransaction();
		    tx.begin();
			
		    GerarUsuarios(manager);
		    System.out.println("Banco populado!!!");
			
			tx.commit();
			manager.close();
		
		}
	
	
	public static void GerarUsuarios(EntityManager manager){
		
		List<Colaborador> colaboradores = new ArrayList<>();
		
		
		StringBuilder jpql = new StringBuilder("from  Colaborador");
		Query query = manager.createQuery(jpql.toString());
		
		colaboradores = query.getResultList();
		
		
		
		
		for (Colaborador c: colaboradores){
			
		   	
           String nome	= c.getNome();
           Date data = c.getDataDeNascimento();	
           
           
       
           String[] nomeCompleto = nome.split(" ");
           
           String firstName = nomeCompleto[0];	
           
           String senha = ( firstName + data.getYear());
           
          /* String lastName = split[last] ;
           String login = (lastName);
           
			*/
         
           
           String lastName = nomeCompleto[nomeCompleto.length -1];
           
           String UserName = (firstName +"."+ lastName);
           
           System.out.println(UserName);
           System.out.println(senha);
           
           
           Perfil perfil = new Perfil();
           perfil.setId((long) 1);
           
           User user = new User();
           
           user.setNomeUsuario(UserName);
           user.setSenha(senha);
           user.setAtivo(true);
           user.setPerfil(perfil);
           user.setColaborador(c);
           
           manager.persist(user);
		}
		
	}
	
	
	
	
	
}
