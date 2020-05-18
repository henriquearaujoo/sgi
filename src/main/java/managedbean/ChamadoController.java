package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Chamado;
import util.UsuarioSessao;


@Named(value = "chamadoController")
@ViewScoped
public class ChamadoController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private @Inject UsuarioSessao sessao;
	private @Inject Chamado chamado;
	private List<Chamado>  listaDeChamados = new ArrayList<>();
	
	
	public ChamadoController(){}


	public UsuarioSessao getSessao() {
		return sessao;
	}


	public void setSessao(UsuarioSessao sessao) {
		this.sessao = sessao;
	}


	public Chamado getChamado() {
		return chamado;
	}


	public void setChamado(Chamado chamado) {
		this.chamado = chamado;
	}


	public List<Chamado> getListaDeChamados() {
		return listaDeChamados;
	}


	public void setListaDeChamados(List<Chamado> listaDeChamados) {
		this.listaDeChamados = listaDeChamados;
	}
	
}
