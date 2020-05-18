package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Banco;
import model.Evento;
import repositorio.EventoRepositorio;
import util.Filtro;

	public class EventoService implements Serializable {
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@Inject
		private EventoRepositorio repositorio; 

		public EventoService() {
		}
				
		@Transactional
		public Evento salvar(Evento evento){
			return repositorio.salvar(evento);
		}
		
		@Transactional
		public void remover(Evento evento){
			repositorio.remover(evento);
		}
		
		public List<Evento> getAll(Filtro filtro){
			return repositorio.getAll(filtro);
		}
		
		public Evento findById(Long id){
			return repositorio.findById(id);
		}
		
		public List<Evento> getResumeList() {
			return repositorio.getResumeList();
		}
		
}
	