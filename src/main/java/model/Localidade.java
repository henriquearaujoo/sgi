package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "localidade")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public class Localidade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	@Column
	private String nome;
	@Column
	private String mascara;
	@Column
	private Integer codigo;

	@Transient
	private String municipioNome;

	public String getMunicipioNome() {
		return municipioNome;
	}

	public void setMunicipioNome(String municipioNome) {
		this.municipioNome = municipioNome;
	}

	@ManyToOne
	private Estado estado;

	@ManyToOne
	private Pais pais;

	@ManyToOne
	private UnidadeConservacao unidadeConservacao;

	@Transient
	private String estadoNome;
	@Transient
	private String paisNome;
	@Transient
	private String ucNome;

	public String getEstadoNome() {
		return estadoNome;
	}

	public String getPaisNome() {
		return paisNome;
	}

	public String getUcNome() {
		return ucNome;
	}

	public void setEstadoNome(String estadoNome) {
		this.estadoNome = estadoNome;
	}

	public void setPaisNome(String paisNome) {
		this.paisNome = paisNome;
	}

	public void setUcNome(String ucNome) {
		this.ucNome = ucNome;
	}

	public Localidade(Long id, String nome, String mascara, String estadoNome, String paisNome, String ucNome) {
		this.id = id;
		this.nome = nome;
		this.mascara = mascara;
		this.estadoNome = estadoNome;
		this.paisNome = paisNome;
		this.ucNome = ucNome;
	}

	public Localidade() {
	}

	public Localidade(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public Localidade(Long id, String nome, Integer codigo) {
		this.id = id;
		this.nome = nome;
		this.codigo = codigo;
	}

	@Transient
	private String type;

	public Localidade(Long id, String nome, String type) {
		this.id = id;
		this.nome = nome;
		this.type = type;
	}

	public Localidade(Long id, String nome, String type, String mascara) {
		this.id = id;
		this.nome = nome;
		this.type = type;
		this.mascara = mascara;
	}

	public Localidade(Long id, String nome, String type, String mascara, String municipioNome) {
		this.id = id;
		this.nome = nome;
		this.type = type;
		this.mascara = mascara;
		this.municipioNome = municipioNome;
	}

	public Localidade(Long id, String nome, String type, String mascara, Integer codigo) {
		this.id = id;
		this.nome = nome;
		this.type = type;
		this.mascara = mascara;
		this.codigo = codigo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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
		Localidade other = (Localidade) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getMascara() {
		return mascara;
	}

	public void setMascara(String mascara) {
		this.mascara = mascara;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public UnidadeConservacao getUnidadeConservacao() {
		return unidadeConservacao;
	}

	public void setUnidadeConservacao(UnidadeConservacao unidadeConservacao) {
		this.unidadeConservacao = unidadeConservacao;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

}
