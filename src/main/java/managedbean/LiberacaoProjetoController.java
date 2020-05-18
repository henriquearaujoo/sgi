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

import org.primefaces.PrimeFaces;

import model.Gestao;
import model.PlanoDeTrabalho;
import model.Projeto;
import model.User;
import model.UserProjeto;
import service.PlanoDeTrabalhoService;
import service.ProjetoService;
import service.UsuarioService;
import util.Filtro;
import util.UsuarioSessao;

@Named(value = "liberacaoProjetoController")
@ViewScoped
public class LiberacaoProjetoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ProjetoService projetoService;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private UsuarioSessao usuarioSessao;

	@Inject
	private PlanoDeTrabalhoService planoDeTrabalhoService;

	private List<Projeto> listafiltro;

	private Filtro filtro = new Filtro();

	private List<Projeto> listaProjetos;

	private List<User> listaUsuario;

	private User usuarioSelecionado;

	private List<Projeto> listaProjetosSelecionados;

	private List<PlanoDeTrabalho> listaPlanosDeTrabalho;
	
	private Projeto projetoSelecionado;

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage("msg", message);
	}

	public void init() {
		//listaProjetos = projetoService.getProjetosbyUsuarioProjeto(usuarioSessao.getUsuario());
		listaUsuario = usuarioService.getUsuarios();
		listaPlanosDeTrabalho = planoDeTrabalhoService.getPLanos(filtro, usuarioSessao.getUsuario());
		findProjetosByPlanosDeTrabalhoAndGestao();
	}

	public void openDialog() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('dialogUsuario').show();");
	}

	public void closeDialog() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('dialogUsuario').hide();");
	}

	public void salvaProjetoUsuario() {
		for (Projeto p : listaProjetosSelecionados) {
			UserProjeto up = new UserProjeto();
			up.setProjeto(p);
			up.setUser(usuarioSelecionado);
			projetoService.salvarUsuarioNoProjeto(up);
		}
		listaProjetosSelecionados = new ArrayList<Projeto>();

		addMessage("", "Salvo com Sucesso!", FacesMessage.SEVERITY_INFO);
	}

	public void verificaProjetoSelecionado() {
		if (listaProjetosSelecionados != null && listaProjetosSelecionados.size() > 0) {
			openDialog();
		} else {
			addMessage("", "Selecione um ou mais projetos!", FacesMessage.SEVERITY_WARN);
		}
	}

	// TODO: AO APERTAR FILTRAR
	public void findProjetosByPlanosDeTrabalhoAndGestao() {
		// listaProjetos = projetoService.getProjetosByPlanoDeTrabalhoAndGestao(filtro);

		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro")) {
			listaProjetos = projetoService.getProjetosbyUsuarioProjetoLIBERACAO(null, filtro);	
			return;
		}

		listaProjetos = projetoService.getProjetosbyUsuarioProjetoLIBERACAO(usuarioSessao.getUsuario(), filtro);
	}
	
	//Alteração Realizada para contemplar a exclusão de acesso by christophe.
	
	private List<User> listaUsuarios = new ArrayList<User>();
	
	public void findUsuariosComAcesso(Projeto projetoSelecionado) {
		this.projetoSelecionado = projetoSelecionado;
		listaUsuarios = usuarioService.findUsuarioByProjeto(projetoSelecionado);
	}
	
	public void removerAcesso(User usuarioSelecionado) {
		UserProjeto usuario  = usuarioService.findUsuarioProjeto(projetoSelecionado, usuarioSelecionado);
		usuarioService.remover(usuario);
		listaUsuarios = usuarioService.findUsuarioByProjeto(projetoSelecionado);
		addMessage("", "Excluido com Sucesso!", FacesMessage.SEVERITY_INFO);
		
	}
	
	
	//-------------------------------------------------------
	
	
	

	public Projeto getProjetoSelecionado() {
		return projetoSelecionado;
	}

	public void setProjetoSelecionado(Projeto projetoSelecionado) {
		this.projetoSelecionado = projetoSelecionado;
	}
	
	public List<User> getListaUsuarios() {
		return listaUsuarios;
	}
	
	public void setListaUsuarios(List<User> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public List<Gestao> completeGestao(String query) {
		return projetoService.getGestaoAutoComplete(query);
	}

	public List<Projeto> getListaProjetos() {
		return listaProjetos;
	}

	public void setListaProjetos(List<Projeto> listaProjetos) {
		this.listaProjetos = listaProjetos;
	}

	public List<User> getListaUsuario() {
		return listaUsuario;
	}

	public void setListaUsuario(List<User> listaUsuario) {
		this.listaUsuario = listaUsuario;
	}

	

	public List<PlanoDeTrabalho> getListaPlanosDeTrabalho() {
		return listaPlanosDeTrabalho;
	}

	public List<Projeto> getListafiltro() {
		return listafiltro;
	}

	public void setListafiltro(List<Projeto> listafiltro) {
		this.listafiltro = listafiltro;
	}

	public void setListaPlanosDeTrabalho(List<PlanoDeTrabalho> listaPlanosDeTrabalho) {
		this.listaPlanosDeTrabalho = listaPlanosDeTrabalho;
	}
	
	public User getUsuarioSelecionado() {
		return usuarioSelecionado;
	}
	
	public void setUsuarioSelecionado(User usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public List<Projeto> getListaProjetosSelecionados() {
		return listaProjetosSelecionados;
	}

	public void setListaProjetosSelecionados(List<Projeto> listaProjetosSelecionados) {
		this.listaProjetosSelecionados = listaProjetosSelecionados;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

}
