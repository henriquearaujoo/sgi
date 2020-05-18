package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Requisito;
import model.Versao;
import service.RequisitoService;
import service.UsuarioService;
import service.VersaoService;
import util.UsuarioSessao;

@Named(value = "versaoController")
@ViewScoped
public class VersaoController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private VersaoService versaoService;
	
	@Inject 
	private RequisitoService requisitoService;
	
	private List<Requisito> listaRequisitoSelect;
	
	@Inject
	private UsuarioSessao usuarioSessao;

	private List<Versao> listaVersao;

	private String idVersao;
	
	private Versao versao = new Versao();

	private List<Requisito> listaRequisito = new ArrayList<Requisito>();
	
	private Requisito requisito = new Requisito();
	
	public Boolean renderedBotarNovo(){
				
		if(usuarioSessao.getUsuario().getPerfil().getId() == 1) {
			return true;
		}
			return false;
	}


	public void init() {
		if(idVersao != null) 
			versao = versaoService.findByid(new Long(idVersao));
		if(versao.getId() != null)
			listaRequisito = requisitoService.findRequisito(versao.getId());
	}

	public void enviaMensagem(Severity facesMessage, String msg, String msgDetail) {
		FacesContext context = FacesContext.getCurrentInstance();
		
		FacesMessage mensagem = new FacesMessage(facesMessage,msg,msgDetail);
		context.addMessage("msg", mensagem);
		
	}
	
	public void carregaListaRequisitos(Versao versao) {
		
		listaRequisitoSelect = requisitoService.findRequisito(versao.getId());
		
	}
	
	public String salvar() {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			versao = versaoService.salvar(versao);
			for(Requisito requisito: listaRequisito) {
				requisito.setVersao(versao);
				requisitoService.salvar(requisito);
			}
		}catch(Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, " Erro Adicionar ","");
			context.addMessage("msg", msg);
		}
		
		return "versao?faces-redirect=true";

	}

	public String delete(Versao versao) {
		FacesContext context = FacesContext.getCurrentInstance();

		if(versaoService.remove(versao)){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "excluida com Sucesso!", "");
			context.addMessage("msg", msg);
		}else{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir ","");
			context.addMessage("msg", msg);
		}
		return "versao";
	}
	
	public void deleteRequisito(Requisito requisitoExcluir) {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
	
			listaRequisito.remove(requisitoExcluir);
			

			if(requisitoExcluir.getId() != null ) {
				requisitoService.remove(requisitoExcluir);
			}
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "excluida com Sucesso!", "");
			context.addMessage("msg", msg);
		}catch(Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir ","");
			context.addMessage("msg", msg);

		}		
	}


	public void addRequisito() {
		Requisito novo =  new Requisito();
		listaRequisito.add(novo);
	}

	public List<Versao> findAll(){
		return versaoService.findAll();
	}
	
	public String getIdVersao() {
		return idVersao;
	}

	public void setIdVersao(String idVersao) {
		this.idVersao = idVersao;
	}

	public List<Versao> getListaVersao() {
		return listaVersao;
	}

	public void setListaVersao(List<Versao> listaVersao) {
		this.listaVersao = listaVersao;
	}

	public Versao getVersao() {
		return versao;
	}

	public void setVersao(Versao versao) {
		this.versao = versao;
	}

	
	
	public UsuarioSessao getUsuarioSessao() {
		return usuarioSessao;
	}


	public void setUsuarioSessao(UsuarioSessao usuarioSessao) {
		this.usuarioSessao = usuarioSessao;
	}


	public List<Requisito> getListaRequisito() {
		return listaRequisito;
	}

	public void setListaRequisito(List<Requisito> listaRequisito) {
		this.listaRequisito = listaRequisito;
	}

	public List<Requisito> getListaRequisitoSelect() {
		return listaRequisitoSelect;
	}

	public void setListaRequisitoSelect(List<Requisito> listaRequisitoSelect) {
		this.listaRequisitoSelect = listaRequisitoSelect;
	}

}
