package service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.AprovadorDocumento;
import model.Colaborador;
import model.Gestao;
import model.LancamentoAuxiliar;
import model.User;
import repositorio.AprovacoesRepositorio;
import repositorio.GestaoRepositorio;
import util.Filtro;

public class AprovacoesService implements Serializable {

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

	@Transactional
	public List<Colaborador> getColaboradorAutoComplete(String query) {
		return aprovacoesRepositorio.getColaboradorAutoComplete(query);
	}

	@Transactional
	public List<String> getDocumentList() {
		return aprovacoesRepositorio.getDocumentList();
	}

	@Transactional
	public List<Gestao> getGestaoList() {
		return gestaoRepositorio.getGestao();
	}

	@Transactional
	public Colaborador getColaboradorById(Long id) {
		return aprovacoesRepositorio.getColaboradorById(id);
	}

	@Transactional
	public AprovadorDocumento getApproverById(Long id) {
		return aprovacoesRepositorio.getApproverById(id);
	}

	@Transactional
	public HashMap<String, String> getStatusList() {
		List<String> statusRepository = aprovacoesRepositorio.getSatusList();
		HashMap<String, String> status = new HashMap<String, String>();
		for (String stts : statusRepository) {
			switch (stts) {
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

	@Transactional
	public List<String> getStatusBeforeList() {
		return aprovacoesRepositorio.getSatusBeforeList();
	}

	@Transactional
	public void newApprover(AprovadorDocumento aprovador) {
		aprovacoesRepositorio.saveApprover(aprovador);
	}

	@Transactional
	public List<AprovadorDocumento> getAllApprover() {
		List<AprovadorDocumento> ap = aprovacoesRepositorio.getAllApprover();
		return ap;
	}

	@Transactional
	public List<AprovadorDocumento> getAllApproverDataConverter() {
		List<Gestao> management = getGestaoList();
		List<AprovadorDocumento> approver = getAllApprover();
		List<Colaborador> collaborator = aprovacoesRepositorio.getAllColaborador();
		HashMap<String, String> status = getStatusList();
		List<AprovadorDocumento> newApproverList = new ArrayList<AprovadorDocumento>();

		convert(approver, newApproverList, management, status, collaborator);

		return newApproverList;
	}

	@Transactional
	public void convert(List<AprovadorDocumento> approver, List<AprovadorDocumento> newApproverList,
			List<Gestao> management, HashMap<String, String> status, List<Colaborador> collaborator) {
		for (AprovadorDocumento ad : approver) {
			AprovadorDocumento apd = new AprovadorDocumento(ad);

			for (Gestao g : management) {
				if (apd.getGestao().longValue() == g.getId().longValue()) {
					apd.setManagement(g);
					break;
				}
			}

			for (Map.Entry<String, String> stts : status.entrySet()) {
				if (apd.getStatus().equals(stts.getValue())) {
					apd.setStatus(stts.getKey());
					break;
				}
			}

			for (Colaborador c : collaborator) {
				if (c.getId().longValue() == apd.getColaborador().longValue()) {
					apd.setCollaborator(c);
					newApproverList.add(apd);
					break;
				}
			}

		}
	}
	
	@Transactional
	public boolean removeApprover(AprovadorDocumento approver) {
		try {
			aprovacoesRepositorio.removeApprover(approver);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Boolean verificaAprovacoesPendentes(User usuario) {
		return null;
	}
}
