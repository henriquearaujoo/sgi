package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Banco;
import repositorio.BancoRepositorio;
import util.Filtro;

	public class BancoService implements Serializable {
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@Inject
		private BancoRepositorio repositorio; 

		public BancoService() {
		}
				
		@Transactional
		public Banco salvarBanco(Banco banco){
			return repositorio.salvarBanco(banco);
		}
		
		@Transactional
		public void removerBanco(Banco banco){
			repositorio.removerBanco(banco);
		}
		
		public List<Banco> getTodosBancos(Filtro filtro){
			return repositorio.getTodosBancos(filtro);
		}

		public List<Banco> getTodosBancos(){
			return repositorio.getTodosBancos();
		}

		
		public Banco findBancoById(Long id){
			return repositorio.findBancoById(id);
		}
		
		
}
	