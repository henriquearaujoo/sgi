package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="objetivo")
public class Objetivo implements Serializable {

	public Projeto getProjeto() {
		return projeto;
	}


	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String nome;
	
	@ManyToOne
	private Projeto projeto;
	
	@Column
	private String descricao;
	
	@Column
	private User criador;
	
	@Column
	@Temporal(TemporalType.DATE)
	private Date dataCriacao;
	
	@Column
	@Temporal(TemporalType.DATE)
	private Date dataConclusao;
	
	@Column
	private Boolean concluido = false;
	
	
	@OneToMany(mappedBy = "objetivo",cascade = CascadeType.ALL)
	private List<Metas> listaMetas = new ArrayList<Metas>();


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


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public User getCriador() {
		return criador;
	}


	public void setCriador(User criador) {
		this.criador = criador;
	}


	public Date getDataCriacao() {
		return dataCriacao;
	}


	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}


	public Date getDataConclusao() {
		return dataConclusao;
	}


	public void setDataConclusao(Date dataConclusao) {
		this.dataConclusao = dataConclusao;
	}


	public Boolean getConcluido() {
		return concluido;
	}


	public void setConcluido(Boolean concluido) {
		this.concluido = concluido;
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
		Objetivo other = (Objetivo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	
	
	
}
