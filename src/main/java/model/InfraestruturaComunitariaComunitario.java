package model;

import java.io.Serializable;

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
@Table(name = "infraestrutura_comunitaria_comunitario")
public class InfraestruturaComunitariaComunitario implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private InfraestruturaComunitaria infraestruturaComunitaria;
	
	@Enumerated(EnumType.STRING)
	private TipoEstadoConservacaoComunitario estadoConservacao;
	
	@Enumerated(EnumType.STRING)
	private TipoFinanciadorComunitario tipoFinanciador;
	
	@ManyToOne
	private Comunitario comunitario;
	
	@Transient
	private Boolean selecionado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public InfraestruturaComunitaria getInfraestruturaComunitaria() {
		return infraestruturaComunitaria;
	}

	public void setInfraestruturaComunitaria(InfraestruturaComunitaria infraestruturaComunitaria) {
		this.infraestruturaComunitaria = infraestruturaComunitaria;
	}

	public TipoEstadoConservacaoComunitario getEstadoConservacao() {
		return estadoConservacao;
	}

	public void setEstadoConservacao(TipoEstadoConservacaoComunitario estadoConservacao) {
		this.estadoConservacao = estadoConservacao;
	}

	public TipoFinanciadorComunitario getTipoFinanciador() {
		return tipoFinanciador;
	}

	public void setTipoFinanciador(TipoFinanciadorComunitario tipoFinanciador) {
		this.tipoFinanciador = tipoFinanciador;
	}

	public Comunitario getComunitario() {
		return comunitario;
	}

	public void setComunitario(Comunitario comunitario) {
		this.comunitario = comunitario;
	}

	public Boolean getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	
	
}
