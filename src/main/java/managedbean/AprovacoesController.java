package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import model.AprovadorDocumento;
import model.Colaborador;
import model.Gestao;
import model.LancamentoAuxiliar;
import service.AprovacoesService;
import util.Filtro;
import util.UsuarioSessao;

@ManagedBean
@Named("aprovadorController")
public class AprovacoesController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioSessao usuarioSessao;

	private Filtro filtro = new Filtro();
	private String teste;

	private AprovadorDocumento aprovadorDocumento = new AprovadorDocumento();

	private HashMap<String, String> status = new HashMap<String, String>();
	private List<AprovadorDocumento> approvers = new ArrayList<>();

	@Inject
	private AprovacoesService aprovacoesService;

	public List<LancamentoAuxiliar> listaLancamentoPrincipal;

	public void init() {
		filtrarListaPrincipal(filtro);
	}

	public void initApprover() {
//		filtro = new Filtro();
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
		
		System.out.println(teste);
		
//		if (verificaPrivilegio()) {
//			filtro.setParam(getParamOption());
//			aprovacoesService.newApprover(aprovadorDocumento);
//		} else {
//			addMessage("", "Você não tem permissão para esta ação! ", FacesMessage.SEVERITY_ERROR);
//		}
	}

	public void initObjectApprover() {
		aprovadorDocumento = new AprovadorDocumento();
	}

	public void searchApprover() {

	}

	public void initAllListsApprover() {

	}

	public String getTeste() {
		return teste;
	}

	public void setTeste(String teste) {
		this.teste = teste;
	}

	public List<AprovadorDocumento> getAllApprover() {
		return aprovacoesService.getAllApprover();
	}

	public HashMap<String, String> getStatusList() {
		return aprovacoesService.getStatusList(status);
	}

	public List<String> getStatusBeforeList() {
		return aprovacoesService.getStatusBeforeList();
	}

	public List<Gestao> getGestaoList() {
		return aprovacoesService.getGestaoList();
	}

	public List<String> getDocumentList() {
		return aprovacoesService.getDocumentList();
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

	public AprovadorDocumento getAprovadorDocumento() {
		return aprovadorDocumento;
	}

	public void setAprovadorDocumento(AprovadorDocumento aprovadorDocumento) {
		this.aprovadorDocumento = aprovadorDocumento;
	}

	public List<AprovadorDocumento> getApprovers() {
		return approvers;
	}

	public void setApprovers(List<AprovadorDocumento> approvers) {
		this.approvers = approvers;
	}
}
