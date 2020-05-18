package geradoresTabela;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import model.Capacitacoes;
import model.Entregas;
import model.Evento;
import model.Oficina;
import model.VisitaTecnica;
import util.JpaUtil;

public class GerarAtividades {

	
	public static void main(String[] args){
		
		
		
		EntityManager manager = JpaUtil.getEntityManager();
			
		EntityTransaction tx = manager.getTransaction();
		
		tx.begin();
		
		GerarAtividades(manager);
		System.out.println("Atividades Carregadas");
		tx.commit();
		manager.close();
		
		
		
	}
	
	public static void GerarAtividades(EntityManager manager){

		Oficina oficina1 = new Oficina();
		Oficina oficina2 = new Oficina();
		Oficina oficina3 = new Oficina();
		Oficina oficina4 = new Oficina();
		
		oficina1.setNome("Familiar");
		oficina2.setNome("Definicao de investimento");
		oficina3.setNome("gestão de bens");
		oficina4.setNome("Assembleias");
		
		manager.merge(oficina1);
		manager.merge(oficina2);
		manager.merge(oficina3);
		manager.merge(oficina4);
		
		VisitaTecnica visitaTecnica1 = new VisitaTecnica();
		VisitaTecnica visitaTecnica2 = new VisitaTecnica();
		VisitaTecnica visitaTecnica3 = new VisitaTecnica();
		
		visitaTecnica1.setNome("Acompanhamento Tecnico");
		visitaTecnica2.setNome("Monitoramento/Pesquisa");
		visitaTecnica3.setNome("Mobilizização");
	
		manager.merge(visitaTecnica1);
		manager.merge(visitaTecnica2);
		manager.merge(visitaTecnica3);
		
		
		
		Entregas entregas = new Entregas();
		Entregas entregas2 = new Entregas();
		Entregas entregas3 = new Entregas();
		
		entregas.setNome("Cartões");
		entregas2.setNome("bens");
		entregas3.setNome("Infraesturtura");
		
		
		manager.merge(entregas);
		manager.merge(entregas2);
		manager.merge(entregas3);
		
		
		Capacitacoes capacitacoes = new Capacitacoes();
		Capacitacoes capacitacoes2 = new Capacitacoes();
		Capacitacoes capacitacoes3 = new Capacitacoes();
		
		capacitacoes.setNome("Apoio a geração de renda");
		capacitacoes2.setNome("Emporderamento Comunitario");
		capacitacoes3.setNome("Infraestrutura Comunitaria");
		
		
		manager.merge(capacitacoes);
		manager.merge(capacitacoes2);
		manager.merge(capacitacoes3);
		
		
		Evento evento = new Evento();
		Evento evento2 = new Evento();
		Evento evento3 = new Evento();
		Evento evento4 = new Evento();
		
		
//		evento.setNome("Reunião de Conselho");
//		evento2.setNome("Oficina/ Reunião de Terceiros");
//		evento3.setNome("Treinamento / capacitações");
//		evento4.setNome("Outros");
		
		manager.merge(evento);
		manager.merge(evento2);
		manager.merge(evento3);
		manager.merge(evento4);
		
	
	
	}
		
	
}
