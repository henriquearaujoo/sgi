package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "log_status")
public class LogStatus implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Lancamento lancamento;
	@ManyToOne
	private User usuario;
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	@Enumerated
	private StatusCompra statusLog;
	
//	@Column(name="sigla") //Nome da váriavel = sigla 
//	private String siglaAprovacao;
	
	//Representa a sigla da gestão
	@Column(name="sigla")
	private String sigla;
	
	@Column(name = "sigla_privilegio")
	private String siglaPrivilegio;
	
	@Column(columnDefinition = "TEXT")
	private String comentario;
	
	@Transient
	private String nomeAprovador;
	
	public LogStatus(){}
	
	public LogStatus(Date data){
		this.data = data;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Lancamento getLancamento() {
		return lancamento;
	}


	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}


	public User getUsuario() {
		return usuario;
	}


	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}


	public Date getData() {
		return data;
	}


	public void setData(Date data) {
		this.data = data;
	}


	public StatusCompra getStatusLog() {
		return statusLog;
	}


	public void setStatusLog(StatusCompra statusLog) {
		this.statusLog = statusLog;
	}


//	public String getSiglaAprovacao() {
//		return siglaAprovacao;
//	}
//
//
//	public void setSiglaAprovacao(String siglaAprovacao) {
//		this.siglaAprovacao = siglaAprovacao;
//	}


	public String getSiglaPrivilegio() {
		return siglaPrivilegio;
	}


	public void setSiglaPrivilegio(String siglaPrivilegio) {
		this.siglaPrivilegio = siglaPrivilegio;
	}


	public String getSigla() {
		return sigla;
	}


	public void setSigla(String sigla) {
		this.sigla = sigla;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogStatus other = (LogStatus) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	
	
	
}
