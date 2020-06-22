package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import model.FontePagadora;
import model.Projeto;
import model.RelatorioContasAPagar;
import repositorio.ExecucaoFinanceiraRepositorio;
import repositorio.FontePagadoraRepositorio;
import util.Filtro;

	public class ExecucaoFinanceiraService implements Serializable {
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * 
		 */
		private @Inject ExecucaoFinanceiraRepositorio repositorio;
		private @Inject FontePagadoraRepositorio fonteRepositorio;
 		
		public List<Projeto> getGroupProjectsByFonte(Filtro filtro){
			return repositorio.getGroupProjectsByFonte(filtro);
		}
		
		public List<FontePagadora> buscarFontes(){
			return fonteRepositorio.getFontes();
		}
		
		public List<RelatorioContasAPagar> getLancamentos(Filtro filtro) {
			return repositorio.getLancamentos(filtro);
		}
		
}
	