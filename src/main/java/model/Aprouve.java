package model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Aprouve implements Serializable {

	/**
	 * 
	 */

	// Classe para aprovação de etapas no sistema (Ex: aprovação de compras,
	// aprovação de solicitações de pagamento, ETC)

	
	/**
	 * Tipos de aprovação 
	 * 
	 * 1. SC
	 * 	1.1 - Aprovação do coordenador relacionado a sc.
	 *  1.2 - Aprovação superintendente
	 *  
	 * 
	 *  */
	
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String senha;
	@Column
	private String tipo;
//	@Column(name = "sigla") 
//	private String siglaGestao;
	@Column(name = "sigla")
	private String sigla;
	@ManyToOne
	private User usuario;
	
	@Column 
	private String descricao;
	
	
	

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Aprouve() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		Aprouve other = (Aprouve) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		
		
//		MessageDigest algorithm = null;
//		try {
//			algorithm = MessageDigest.getInstance("SHA-256");
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//		
//		byte messageDigest[] = null;
//		
//		try {
//			messageDigest = algorithm.digest(senha.getBytes("UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 
//		StringBuilder hexString = new StringBuilder();
//		for (byte b : messageDigest) {
//		  hexString.append(String.format("%02X", 0xFF & b));
//		}
//		senha = hexString.toString();
		
		this.senha = senha;	
		
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

//	public String getSiglaGestao() {
//		return siglaGestao;
//	}
//
//	public void setSiglaGestao(String siglaGestao) {
//		this.siglaGestao = siglaGestao;
//	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	 
	
	
}
