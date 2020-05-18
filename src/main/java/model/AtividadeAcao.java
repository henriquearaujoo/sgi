package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "atividade_acao")
public class AtividadeAcao implements Serializable, Comparable<AtividadeAcao>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String descricao;
	
	@Column(precision = 10, scale = 2)
	private BigDecimal orcado;

	@Enumerated(EnumType.STRING)
	private StatusAtividade status;
	
	@Column
	private Boolean aceito;
	
	@Column(name =  "data_execucao")
	@Temporal(TemporalType.DATE)
	private Date dataExecutado;
	
	@Column(name="data_planejado")
	@Temporal(TemporalType.DATE)
	private Date dataPlanejado;
	
	@Column(name="data_inicio")
	@Temporal(TemporalType.DATE)
	private Date dataInicio;
	
	@ManyToOne
	private Projeto projeto;
	
	@Column(name="data_fim")
	@Temporal(TemporalType.DATE)
	private Date dataFim;
	
	@Column(name="data_conclusao")
	@Temporal(TemporalType.DATE)
	private Date dataConclusao;
	
	@Column
	private String observacao;
	
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Date getDataConclusao() {
		return dataConclusao;
	}

	public void setDataConclusao(Date dataConclusao) {
		this.dataConclusao = dataConclusao;
	}
	
	public AtividadeAcao(Long id,String descricao,BigDecimal orcado,StatusAtividade status,String nomeProjeto, String observacao,Date dataInicio,Date dataFim,Date dataConclusao) {
		this.id = id;
		this.descricao =  descricao;
		this.orcado = orcado;
		this.status = status;
		this.nomeProjeto = nomeProjeto;
		this.observacao = observacao;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.dataConclusao = dataConclusao;
	}
	
	@Transient
	private String nomeProjeto;
	
	@Transient
	private BigDecimal executado;
	@Transient
	private BigDecimal saldo;
	@Transient
	private String nomeAcao;
	@Transient
	private String saldoAcao; 
	@Transient
	private String tipo;
	
	
	public AtividadeAcao(){}
	
	public AtividadeAcao(Long id){
		this.id = id;
	}
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getNomeAcao() {
		return nomeAcao;
	}
	public void setNomeAcao(String nomeAcao) {
		this.nomeAcao = nomeAcao;
	}
	public String getSaldoAcao() {
		return saldoAcao;
	}
	public void setSaldoAcao(String saldoAcao) {
		this.saldoAcao = saldoAcao;
	}
	
	public int compareTo(AtividadeAcao o) {
		return this.getNomeAcao().compareTo(o.getNomeAcao());
	}
	public StatusAtividade getStatus() {
		return status;
	}
	public void setStatus(StatusAtividade status) {
		this.status = status;
	}
	public BigDecimal getExecutado() {
		return executado;
	}
	public void setExecutado(BigDecimal executado) {
		this.executado = executado;
	}
	public BigDecimal getOrcado() {
		return orcado;
	}
	public void setOrcado(BigDecimal orcado) {
		this.orcado = orcado;
	}
	public String getTipo() {
		return "atividade";
	}
	public Boolean getAceito() {
		return aceito;
	}
	public void setAceito(Boolean aceito) {
		this.aceito = aceito;
	}
	public Date getDataExecutado() {
		return dataExecutado;
	}
	public void setDataExecutado(Date dataExecutado) {
		this.dataExecutado = dataExecutado;
	}
	public Date getDataPlanejado() {
		return dataPlanejado;
	}
	public void setDataPlanejado(Date dataPlanejado) {
		this.dataPlanejado = dataPlanejado;
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
		AtividadeAcao other = (AtividadeAcao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}
	

}
