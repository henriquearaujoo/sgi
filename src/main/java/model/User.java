package model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "usuario")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String nomeUsuario;

	@Column
	private String senha;
	@Column
	private String senhav4;
	@ManyToOne
	private Perfil perfil;
	@Column
	private Boolean ativo;
	@Column
	private String email;
	@Column
	private Boolean checked;
	@ManyToOne
	private Gestao gestao;
	@ManyToOne
	private Colaborador colaborador;
	@Enumerated(EnumType.STRING)
	private TipoGestao tipoGestao;

	@Column
	private String senhaem;

	@Column
	private Boolean senhaAuto;

	public Gestao getGestao() {
		return gestao;
	}

	public void setGestao(Gestao gestao) {
		this.gestao = gestao;
	}

	@Transient
	private String nomeColaborador;

	public User(Long id, String nomeUsuario, String email, Long colaboradorId, String nomeColaborador, Boolean ativo) {
		this.setId(id);
		this.setNomeUsuario(nomeUsuario);
		this.setEmail(email);
		this.setColaborador(new Colaborador(colaboradorId, nomeColaborador));
		this.setAtivo(ativo);
	}
	
	public User(long id, Colaborador colaborador) {
		setId(id);
		setColaborador(colaborador);
	}
	
	public User(Long id, String nomeUsuario, String email) {
		setId(id);
		setNomeUsuario(nomeUsuario);
		setEmail(email);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {

		MessageDigest algorithm = null;
		try {
			algorithm = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		byte messageDigest[] = null;

		try {
			messageDigest = algorithm.digest(senha.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StringBuilder hexString = new StringBuilder();
		for (byte b : messageDigest) {
			hexString.append(String.format("%02X", 0xFF & b));
		}
		senha = hexString.toString();

		this.senha = senha;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public TipoGestao getTipoGestao() {
		return tipoGestao;
	}

	public void setTipoGestao(TipoGestao tipoGestao) {
		this.tipoGestao = tipoGestao;
	}

	public String getSenhaem() {
		return senhaem;
	}

	public void setSenhaem(String senhaem) {
		this.senhaem = senhaem;
	}

	public String getNomeColaborador() {
		return nomeColaborador;
	}

	public void setNomeColaborador(String nomeColaborador) {
		this.nomeColaborador = nomeColaborador;
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
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Boolean getSenhaAuto() {
		return senhaAuto;
	}

	public void setSenhaAuto(Boolean senhaAuto) {
		this.senhaAuto = senhaAuto;
	}

	public String getSenhav4() {
		return senhav4;
	}

	public void setSenhav4(String senhav4) {
		
		MessageDigest algorithm = null;
		try {
			algorithm = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		byte messageDigest[] = null;

		try {
			messageDigest = algorithm.digest(senhav4.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		StringBuilder hexString = new StringBuilder();

		for (byte b : messageDigest) {
			hexString.append(String.format("%02X", 0xFF & b));
		}
		
		senhav4 = hexString.toString();

		this.senhav4 = senhav4;
	}

}
