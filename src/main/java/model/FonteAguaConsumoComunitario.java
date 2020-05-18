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
@Table(name = "fonte_agua_consumo_comunitario")
public class FonteAguaConsumoComunitario implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private FonteAguaConsumo fonte;
	
	@Column
	private Integer nivel;
	
	@Column
	private String resposta;
	
	@ManyToOne
	private Comunitario comunitario;
	
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

	public Integer getNivel() {
		return nivel;
	}

	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}

	public Comunitario getComunitario() {
		return comunitario;
	}

	public void setComunitario(Comunitario comunitario) {
		this.comunitario = comunitario;
	}

	public FonteAguaConsumo getFonte() {
		return fonte;
	}

	public void setFonte(FonteAguaConsumo fonte) {
		this.fonte = fonte;
	}

	
}
