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

@Entity
@Table(name = "dinamica_populacional_familiar")
public class DinamicaPopulacionalFamiliar implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private IdentificacaoFamiliar membro;
	
	@Column
	private Integer ano;
	
	@Enumerated(EnumType.STRING)
	private TipoSaidaComunidadeFamiliar tipoSaida;
	
	@ManyToOne
	private Comunidade comunidade;
		
	@ManyToOne
	private UnidadeConservacao uc;
	
	@ManyToOne
	private Localidade cidade;
	
	@Column
	private String localTrabalho;
	
	@ManyToOne
	private Familiar familiar;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public TipoSaidaComunidadeFamiliar getTipoSaida() {
		return tipoSaida;
	}

	public void setTipoSaida(TipoSaidaComunidadeFamiliar tipoSaida) {
		this.tipoSaida = tipoSaida;
	}

	public Comunidade getComunidade() {
		return comunidade;
	}

	public void setComunidade(Comunidade comunidade) {
		this.comunidade = comunidade;
	}

	public UnidadeConservacao getUc() {
		return uc;
	}

	public void setUc(UnidadeConservacao uc) {
		this.uc = uc;
	}

	public String getLocalTrabalho() {
		return localTrabalho;
	}

	public void setLocalTrabalho(String localTrabalho) {
		this.localTrabalho = localTrabalho;
	}

	public Familiar getFamiliar() {
		return familiar;
	}

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	public IdentificacaoFamiliar getMembro() {
		return membro;
	}

	public void setMembro(IdentificacaoFamiliar membro) {
		this.membro = membro;
	}

	public Localidade getCidade() {
		return cidade;
	}

	public void setCidade(Localidade cidade) {
		this.cidade = cidade;
	}
	
	
}
