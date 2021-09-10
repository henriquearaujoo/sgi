package managedbean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import model.Colaborador;
import model.LancamentoAuxiliar;
import service.AprovacoesService;
import util.Filtro;
import util.UsuarioSessao;

@ManagedBean
@Named("AprovacoesController")
public class AprovacoesController implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioSessao usuarioSessao;
	
	private Filtro filtro =  new Filtro();
	
	@Inject
	private AprovacoesService aprovacoesService;
	
	public List<LancamentoAuxiliar> listaLancamentoPrincipal;
	
	
	public void init() {
		filtrarListaPrincipal(filtro);
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
		if(usuarioSessao.getUsuario().getPerfil().getId().equals(new Long("1"))) {
			return true;
		}else
			return false;
	}
	
	public String getParamOption() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		return params.get("type");
	}
	
	public void newApprover() {
		if (verificaPrivilegio()) {
			filtro.setParam(getParamOption());
			aprovacoesService.newApprover(filtro);
		} else {
			addMessage("", "Você não tem permissão para esta ação! ", FacesMessage.SEVERITY_ERROR);
		}
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
}
