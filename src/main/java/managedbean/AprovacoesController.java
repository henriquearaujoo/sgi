package managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.AprovadorDocumento;
import model.Colaborador;
import model.Gestao;
import model.LancamentoAuxiliar;
import service.AprovacoesService;
import util.Filtro;
import util.UsuarioSessao;


@Named("aprovadorController")
@ViewScoped
public class AprovacoesController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioSessao usuarioSessao;

	private Filtro filtro = new Filtro();

	@Inject
	private AprovadorDocumento aprovador;
	
	@Inject
	private Colaborador colaborador;

	private HashMap<String, String> status = new HashMap<String, String>();
	private List<AprovadorDocumento> approvers;
	private List<String> statusBefore = new ArrayList<String>();
	private List<Gestao> managements = new ArrayList<Gestao>();
	private List<String> documents = new ArrayList<String>();
	
	private Long idApprover;
	private Long idColaborador;

	@Inject
	private AprovacoesService aprovacoesService;

	private List<LancamentoAuxiliar> listaLancamentoPrincipal;

	public void init() { 	
		filtrarListaPrincipal(filtro);
	}
	
	public void initCadastro() {
		if (idApprover != null && idColaborador != null) {
			this.colaborador = aprovacoesService.getColaboradorById(idColaborador);
			this.aprovador = aprovacoesService.getApproverById(idApprover);
		}
		
		initAllListLabels();
	}

	public void initApprover() {
		initAllApprover();
	}

	public void reprovaLancamento(List<LancamentoAuxiliar> listLancamento) {
		aprovacoesService.reprovaLancamentos(listLancamento);
	}

	public void aprovarLancamento(List<LancamentoAuxiliar> listLancamento) {
		aprovacoesService.aprovarLancamentos(listLancamento);
	}

	public void filtrarListaPrincipal(Filtro filtro) {
		try {
			listaLancamentoPrincipal = aprovacoesService.filtrar(filtro);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Boolean verificaPrivilegio() {
		if (usuarioSessao.getUsuario().getPerfil().getId().equals(new Long("1"))) {
			return true;
		} else
			return false;
	}

	public String getParamOption() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		return params.get("type");
	}

	public void newApprover() {
		if (verificaPrivilegio()) {
			aprovador.setColaborador(getColaborador().getId());
			aprovacoesService.newApprover(aprovador);
			if (idApprover != null && idColaborador != null) {
				redirectWithMessageSuccess("APROVADOR ATUALIZADO!!! 🥳");
			} else {
				redirectWithMessageSuccess("NOVO APROVADOR SALVO!!! 🥳");				
			}
		} else {
			addMessage("", "Você não tem permissão para esta ação! 😓", FacesMessage.SEVERITY_ERROR);
		}
	}
	
	public void removeApprover(AprovadorDocumento approver) {
		if (verificaPrivilegio()) {
			boolean approverRemove = aprovacoesService.removeApprover(approver); 
			if (approverRemove) {
				initAllApprover();
				addMessage("REMOVIDO", "O aprovador foi removido! Sentiremos saudades! 🙁", FacesMessage.SEVERITY_INFO);
			} else {
				addMessage("ERRO AO REMOVER", "Não foi possível remover este aprovador! 🙁", FacesMessage.SEVERITY_INFO);
			}
		} else {
			addMessage("IMPOSSÍVEL", "Você não tem permissão para esta ação! 😓", FacesMessage.SEVERITY_ERROR);
		}
	}
	
	public void redirectWithMessageSuccess(String message) {
		FacesContext context = FacesContext.getCurrentInstance();
		addMessage("SUCESSO", message, FacesMessage.SEVERITY_INFO);
		context.getExternalContext().getFlash().setKeepMessages(true);
		try {			
			context.getExternalContext().redirect("/sgi_0.0.1/main/aprovadores.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initObjectApprover() {
		aprovador = new AprovadorDocumento();
	}
	
	public void initAllListLabels() {
		status = aprovacoesService.getStatusList();
		
		statusBefore = aprovacoesService.getStatusBeforeList();
		
		managements = aprovacoesService.getGestaoList();
		
		documents = aprovacoesService.getDocumentList();
		
	}

	public void initAllApprover() {
		approvers = aprovacoesService.getAllApproverDataConverter();
	}

	public List<Colaborador> colaboradorAutoComplete(String query) {
		return aprovacoesService.getColaboradorAutoComplete(query);
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage("msg-aprovadores", message);
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public UsuarioSessao getUsuarioSessao() {
		return usuarioSessao;
	}

	public void setUsuarioSessao(UsuarioSessao usuarioSessao) {
		this.usuarioSessao = usuarioSessao;
	}

	public HashMap<String, String> getStatus() {
		return status;
	}

	public AprovadorDocumento getAprovador() {
		return aprovador;
	}

	public void setAprovador(AprovadorDocumento aprovador) {
		this.aprovador = aprovador;
	}

	public List<AprovadorDocumento> getApprovers() {
		return approvers;
	}

	public void setApprovers(List<AprovadorDocumento> approvers) {
		this.approvers = approvers;
	}

	public List<String> getStatusBefore() {
		return statusBefore;
	}

	public void setStatusBefore(List<String> statusBefore) {
		this.statusBefore = statusBefore;
	}

	public List<Gestao> getManagements() {
		return managements;
	}

	public void setManagements(List<Gestao> managements) {
		this.managements = managements;
	}

	public List<String> getDocuments() {
		return documents;
	}

	public void setDocuments(List<String> documents) {
		this.documents = documents;
	}

	public void setStatus(HashMap<String, String> status) {
		this.status = status;
	}

	public List<LancamentoAuxiliar> getListaLancamentoPrincipal() {
		return listaLancamentoPrincipal;
	}

	public void setListaLancamentoPrincipal(List<LancamentoAuxiliar> listaLancamentoPrincipal) {
		this.listaLancamentoPrincipal = listaLancamentoPrincipal;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public Long getIdApprover() {
		return idApprover;
	}

	public void setIdApprover(Long idApprover) {
		this.idApprover = idApprover;
	}

	public Long getIdColaborador() {
		return idColaborador;
	}

	public void setIdColaborador(Long idColaborador) {
		this.idColaborador = idColaborador;
	}
}
