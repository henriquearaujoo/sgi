package geradoresTabela;

import javax.persistence.EntityManager;

import model.UnidadeConservacao;

public class GerarUnidConservacao {

	public static void maind(String[] args){
		
		
		
	}
	
	public void GerarUni(EntityManager manager){
		
		UnidadeConservacao uc12 = new UnidadeConservacao();
		uc12.setNome("Reserva extrativista Catuá-Ipixuna");
		uc12.setMascara("Resex Catuá-Ipixuna");
		
		
		
	}
	
	
	
}
