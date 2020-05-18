package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "gestao")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public  class Gestao implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String nome;
	
	@ManyToOne
	private Colaborador colaborador;
	
	@Column
	private String usuarioAlteracao;
	
	@Column (name = "data_alteracao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAlteracao;
	
	@ManyToOne
	private Colaborador aprovadorSubistituto;
	
	@Column
	private Boolean ativo;

	
	@Column
	private Boolean gerenciaOrcamento;
	
	@ManyToOne
	private User usuario;
	
	@Column
	private String sigla;
	
	@Column	
	private Boolean ausente;
	
	@Transient
	private String type;
	
	public Gestao(){}
	
	public Gestao(Long id, String nome){
		this.id = id;
		this.nome = nome;
	}
	
	public Gestao(Long id, String nome, String type){
		this.id = id;
		this.type = type;
		
		switch (type) {
		case "coord": this.nome = setarGestao("Coordenadoria", nome);
			break;
		case "reg": this.nome = nome;		
			break;	
		default: this.nome = setarGestao("Superintendência", nome);
			break;
		}
	}
	
	public String setarGestao(String tipo, String gestao){
		StringBuilder str = new StringBuilder(tipo);
		str.append(" - ");
		str.append(gestao);
		return str.toString();
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
		Gestao other = (Gestao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Colaborador getColaborador() {
		return colaborador;
	}
	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public User getUsuario() {
		return usuario;
	}

	public Colaborador getAprovadorSubistituto() {
		return aprovadorSubistituto;
	}

	public void setAprovadorSubistituto(Colaborador aprovadorSubistituto) {
		this.aprovadorSubistituto = aprovadorSubistituto;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Boolean getAusente() {
		return ausente;
	}

	public void setAusente(Boolean ausente) {
		this.ausente = ausente;
	}

	public String getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(String usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	
	

	
	



}
