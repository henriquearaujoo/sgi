package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Requisito implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String nomeRequisito;
	
	@Column
	private String usuario;
	
	@Column
	private String descricaoRequisito;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_modificacao")
	private Date dataModificacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name= "data_alteracao")
	private Date dataAlteracao;
	
	@ManyToOne
	private Versao versao;

	public String getNomeRequisito() {
		return nomeRequisito;
	}

	public void setNomeRequisito(String nomeRequisito) {
		this.nomeRequisito = nomeRequisito;
	}

	public Versao getVersao() {
		return versao;
	}

	public void setVersao(Versao versao) {
		this.versao = versao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRequisito() {
		return nomeRequisito;
	}

	public void setRequisito(String requisito) {
		this.nomeRequisito = requisito;
	}
	
	public String getDescricaoRequisito() {
		return descricaoRequisito;
	}

	public void setDescricaoRequisito(String descricaoRequisito) {
		this.descricaoRequisito = descricaoRequisito;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Date getDataModificacao() {
		return dataModificacao;
	}

	public void setDataModificacao(Date dataModificacao) {
		this.dataModificacao = dataModificacao;
	}


	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}


	
	
	

}
