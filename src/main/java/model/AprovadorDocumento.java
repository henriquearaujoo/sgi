package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class AprovadorDocumento implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column // Colaborador aprovador
	private Long colaborador;
	
	@Column // Gestão a qual diz respeito o documento
	private Long gestao;
	
	@Column // Documento correspondente ao módulo do sistema
	private String documento;
	
	@Column // email do colaborador
	private String email;
	
	@Column // Define de qual ordem é a autorizaçã
	private Long ordem;
	
	@Column // status compatível 
	private String status;
	
	@Column(name = "status_before") // status compatível 
	private String statusBefore;
	

	
	public AprovadorDocumento() {
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
		AprovadorDocumento other = (AprovadorDocumento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getColaborador() {
		return colaborador;
	}

	public void setColaborador(Long colaborador) {
		this.colaborador = colaborador;
	}

	public Long getGestao() {
		return gestao;
	}

	public void setGestao(Long gestao) {
		this.gestao = gestao;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getOrdem() {
		return ordem;
	}

	public void setOrdem(Long ordem) {
		this.ordem = ordem;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusBefore() {
		return statusBefore;
	}

	public void setStatusBefore(String statusBefore) {
		this.statusBefore = statusBefore;
	}



}
