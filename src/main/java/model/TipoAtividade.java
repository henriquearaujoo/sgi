package model;

import java.util.List;

import repositorio.AtividadeCampoRepositorio;
import repositorio.GestaoRepositorio;

public enum TipoAtividade {

		OFICINA("Oficina"){
			
			@Override
			public List<Atividade> getAtividade(AtividadeCampoRepositorio repo) {
				// TODO Auto-generated method stub
				return repo.getOficina();
			}
		}, 
		VT("Visitas Tecnicas"){

			@Override
			public List<Atividade> getAtividade(AtividadeCampoRepositorio repo) {
				// TODO Auto-generated method stub
				return repo.getVisitaTecnica();
			}
		
			
			
		},
		ENTREGA("entregas"){

			@Override
			public List<Atividade> getAtividade(AtividadeCampoRepositorio repo) {
				// TODO Auto-generated method stub
				return repo.getEntregas();
			}
			
		},
		CAPACITACAO("Capacitações"){

			@Override
			public List<Atividade> getAtividade(AtividadeCampoRepositorio repo) {
				// TODO Auto-generated method stub
				return repo.getCapacitacao();
			}
			
		},
		EVENTO("Evento"){

			@Override
			public List<Atividade> getAtividade(AtividadeCampoRepositorio repo) {
				// TODO Auto-generated method stub
				return repo.getEvento();
			}

		};	
	
		
	private String nome;
	

	private TipoAtividade(String nome){
		this.nome = nome;
	}

	public abstract List<Atividade> getAtividade(AtividadeCampoRepositorio repo);

	public String getNome() {
		return nome;
	}
}
