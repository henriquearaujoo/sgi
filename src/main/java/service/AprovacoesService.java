package service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import model.Colaborador;
import model.Gestao;
import model.LancamentoAuxiliar;
import model.User;
import repositorio.AprovacoesRepositorio;
import repositorio.GestaoRepositorio;
import util.Filtro;

public class AprovacoesService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Inject
	private AprovacoesRepositorio aprovacoesRepositorio;
	
	@Inject
	private GestaoRepositorio gestaoRepositorio;

	public List<LancamentoAuxiliar> filtrar(Filtro filtro) {
		return aprovacoesRepositorio.filtar(filtro);
	}

	public void reprovaLancamentos(List<LancamentoAuxiliar> listLancamento) {
		// TODO Auto-generated method stub
	}

	public void aprovarLancamentos(List<LancamentoAuxiliar> listLancamento) {
		// TODO Auto-generated method stub
		
	}
	
	public List<Colaborador> getColaboradorAutoComplete(String query) {
		return aprovacoesRepositorio.getColaboradorAutoComplete(query);
	}
	
	public List<String> getDocumentList() {
		return aprovacoesRepositorio.getDocumentList();
	}
	
	public List<Gestao> getGestaoList() {
		return gestaoRepositorio.getGestao();
	}
	
	public HashMap<String, String> getStatusList(HashMap<String, String> status) {
		List<String> statusRepository = aprovacoesRepositorio.getSatusList();
		for(String stts : statusRepository) {
			switch(stts) {
			case "PENDENTE_APROVACAO_TWO":
				status.put("Segunda Aprovação", stts);
				continue;
			case "VALIDADO":
				status.put("Validado", stts);
				continue;
			case "APROVADO":
				status.put("Aprovado", stts);
				continue;
			case "CONCLUIDO":
				status.put("Concluído", stts);
				continue;
			default:
				break;
			}
		}
		return status;
	}
	
	public List<String> getStatusBeforeList() {
		return aprovacoesRepositorio.getSatusBeforeList();
	}

	public void newApprover(Filtro filtro) {
		
	}
	
	public Boolean verificaAprovacoesPendentes(User usuario) {
		return  null;
	}	
}
