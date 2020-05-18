package util;

import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.User;
import repositorio.UsuarioRepository;

@Named(value =  "sessaoBean")
@SessionScoped
public class UsuarioSessao implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String nomeUsuario;
	private Date dataLogin;
	private  User usuario;
	private  Boolean gestor;
	
	private boolean logado;
	
	
	public UsuarioSessao(){}


	public Long getIdColadorador() {
		return  this.usuario.getColaborador().getId();
	}
	
	public String getNomeUsuario() {
		return nomeUsuario;
	}


	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}


	public Date getDataLogin() {
		return dataLogin;
	}


	public void setDataLogin(Date dataLogin) {
		this.dataLogin = dataLogin;
	}


	public boolean isLogado() {
		return logado;
	}


	public void setLogado(boolean logado) {
		this.logado = logado;
	}
	
	public boolean verificaLogado(){
		return nomeUsuario != null;
	}
	
	
	
	public  User getUsuario() {
		//UsuarioRepository repository = ;
		return usuario;
	}



	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}


	public Boolean getGestor() {
		return gestor;
	}


	public void setGestor(Boolean gestor) {
		this.gestor = gestor;
	}
	
}
