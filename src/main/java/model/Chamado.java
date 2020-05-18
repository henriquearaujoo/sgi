package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Chamado implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//Quem cadastra a solicitação, variável populada automáticamente pelo sistema identificando usuário logado
	@ManyToOne
	private User emissor;
	
	//Nível de prioridade
	@Column
	private Integer prioridade;
	
	//Surpresa
	@Column
	private String linkRetornoEmail;

	//Solicitante ou sugestor 
	@ManyToOne
	private  Colaborador solicitante;

	//Codigo gerado para o sistema, posteriormente enviado como protocólo para o usuário
	@Column
	private String codigo;
	
	//Responsável pela análise da demanda, classificação e prioridade (Deve ser discutida com equipe)
	@Column
	private String analista;
	
	//Responsável pela codificação da demanda
	@Column
	private String desenvolvedor;

	//Nova demanda, Suporte ou BUG
	@Enumerated(EnumType.STRING)
	private TipoAtendimento tipoAtendimento;
	
	@Column
	private String descricao;
	
	//Data e hora de emissão (Momento em que a demanda é registrada no sistema)
	@Column(name = "data_emissao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEmissao;
	
	//Data e hora do atendimento  
	@Column(name = "data_atendimento")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAtendimento;
	
	//Data e hora da conclusão, usado para calculo de horas em uma demanda, registrado pelo analista após os procedimentos de  
	//testes
	@Column(name = "data_conlusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataConclusao;
	

	public Chamado(){
		
		
		
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
		Chamado other = (Chamado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public User getEmissor() {
		return emissor;
	}



	public void setEmissor(User emissor) {
		this.emissor = emissor;
	}



	public Colaborador getSolicitante() {
		return solicitante;
	}



	public void setSolicitante(Colaborador solicitante) {
		this.solicitante = solicitante;
	}



	public String getCodigo() {
		return codigo;
	}



	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}



	public String getAnalista() {
		return analista;
	}



	public void setAnalista(String analista) {
		this.analista = analista;
	}



	public String getDesenvolvedor() {
		return desenvolvedor;
	}



	public void setDesenvolvedor(String desenvolvedor) {
		this.desenvolvedor = desenvolvedor;
	}



	public TipoAtendimento getTipoAtendimento() {
		return tipoAtendimento;
	}



	public void setTipoAtendimento(TipoAtendimento tipoAtendimento) {
		this.tipoAtendimento = tipoAtendimento;
	}



	public String getDescricao() {
		return descricao;
	}



	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}



	public Date getDataAtendimento() {
		return dataAtendimento;
	}



	public void setDataAtendimento(Date dataAtendimento) {
		this.dataAtendimento = dataAtendimento;
	}



	public Date getDataConclusao() {
		return dataConclusao;
	}



	public void setDataConclusao(Date dataConclusao) {
		this.dataConclusao = dataConclusao;
	}

}
