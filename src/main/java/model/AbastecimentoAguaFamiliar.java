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
@Table(name = "abastecimento_agua_familiar")
public class AbastecimentoAguaFamiliar implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private TipoAbastecimentoAguaFamiliar tipo;
	
	@Column
	private String resposta;
	
	@ManyToOne
	private Familiar familiar;
	
	@Column
	private Boolean temResposta;
	
	@Transient
	private Boolean selecionado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResposta() {
		return resposta;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
	}

	public Familiar getFamiliar() {
		return familiar;
	}

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	public Boolean getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public Boolean getTemResposta() {
		return temResposta;
	}

	public void setTemResposta(Boolean temResposta) {
		this.temResposta = temResposta;
	}

	public TipoAbastecimentoAguaFamiliar getTipo() {
		return tipo;
	}

	public void setTipo(TipoAbastecimentoAguaFamiliar tipo) {
		this.tipo = tipo;
	}

}
