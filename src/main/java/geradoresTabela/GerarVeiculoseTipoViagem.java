package geradoresTabela;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import model.TipoViagem;
import model.Veiculo;
import util.JpaUtil;

public class GerarVeiculoseTipoViagem {

	public static void main(String[] args){
		
		EntityManager manager = JpaUtil.getEntityManager();
		EntityTransaction tx = manager.getTransaction();
		tx.begin();
		
		GerarVeiculos(manager);
		System.out.println("Carros Gerados");
		GerarTipoViagem(manager);
		System.out.println("TipoViagem Gerados ");

		tx.commit();
		manager.close();
		
	}
	
	
	public static void GerarVeiculos(EntityManager manager){
		
		Veiculo veiculo = new Veiculo();
		Veiculo veiculo2 = new Veiculo();
		Veiculo veiculo3 = new Veiculo();
		Veiculo veiculo4 = new Veiculo();
		Veiculo veiculo5 = new Veiculo();
		
		
		
		veiculo.setNome("Carro");
		veiculo2.setNome("Lancha");
		veiculo3.setNome("Barco");
		veiculo4.setNome("Moto");
		veiculo5.setNome("Avião");
		
		
		manager.merge(veiculo);
		manager.merge(veiculo2);
		manager.merge(veiculo3);
		manager.merge(veiculo4);
		manager.merge(veiculo5);
		
		
	}
	
	public static void GerarTipoViagem(EntityManager manager){
	
		TipoViagem tipoViagem1 = new TipoViagem();
		TipoViagem tipoViagem2 = new TipoViagem();
		TipoViagem tipoViagem3 = new TipoViagem();
		TipoViagem tipoViagem4 = new TipoViagem();
		TipoViagem tipoViagem5 = new TipoViagem();
		TipoViagem tipoViagem6 = new TipoViagem();
		TipoViagem tipoViagem7 = new TipoViagem();
		TipoViagem tipoViagem8 = new TipoViagem();
		TipoViagem tipoViagem9 = new TipoViagem();
		TipoViagem tipoViagem10 = new TipoViagem();
		
		
		tipoViagem1.setName("Coordenadoria de Marketing");
		tipoViagem2.setName("Coordenadoria de logística");
		tipoViagem3.setName("Coordenadoria de Comunicação");
		tipoViagem4.setName("Coordenadoria de obras");
		tipoViagem5.setName("Coordenadoria de TI");
		tipoViagem6.setName("Progama Bolsa Floresta");
		tipoViagem7.setName("Programa de Educação e Saude");
		tipoViagem8.setName("Superintendência Administrativo-financeira");
		tipoViagem9.setName("Superintendência Geral");
		tipoViagem10.setName("Superintendência Técnica-cientifica");
		
		
		manager.merge(tipoViagem1);
		manager.merge(tipoViagem2);
		manager.merge(tipoViagem3);
		manager.merge(tipoViagem4);
		manager.merge(tipoViagem5);
		manager.merge(tipoViagem6);
		manager.merge(tipoViagem7);
		manager.merge(tipoViagem8);
		manager.merge(tipoViagem9);
		manager.merge(tipoViagem10);
		
	}
	
	
}
