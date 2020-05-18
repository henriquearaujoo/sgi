package model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import util.DataUtil;
import util.Util;



@Entity
@Table(name = "rubrica_orcamento")
public class RubricaOrcamento implements Serializable{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Orcamento orcamento;
	
	@ManyToOne
	private Rubrica rubrica;
	
	@ManyToOne
	private ComponenteClass componente;
	
	@ManyToOne
	private SubComponente subComponente;
	
	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal valor;
	
	@Column
	private double porcentagem;
	
	@Column
	private String tipo;
	
	@Transient
	private BigDecimal saldoOrcamento = BigDecimal.ZERO;
	
	@Transient
	private BigDecimal valorEmpenhado = BigDecimal.ZERO;
	
	@Transient
	private String nomeComponente;
	
	@Transient
	private String nomeSubComponente;
	
	@Transient
	private String nomeCategoria;
	
	

	public RubricaOrcamento(){}

	public RubricaOrcamento(Object[] object) {
		this.id = object[0] !=  null ? Long.valueOf(object[0].toString()) : null;
		this.nomeComponente = object[1] !=  null ? object[1].toString() : "";	
		this.nomeSubComponente = object[2] !=  null ? object[2].toString() : "";
		this.nomeCategoria = object[3] !=  null ? object[3].toString() : "";
		this.saldoOrcamento =  object[4] !=  null ? new BigDecimal(object[4].toString()) : BigDecimal.ZERO;
		this.valorEmpenhado = object[5] !=  null ? new BigDecimal(object[5].toString()) : BigDecimal.ZERO;
		this.valor = object[6] !=  null ? new BigDecimal(object[6].toString()) : BigDecimal.ZERO;
		this.componente = new ComponenteClass(object[7] !=  null ? Long.valueOf(object[7].toString()) : null);
		this.subComponente = new SubComponente(object[8] !=  null ? Long.valueOf(object[8].toString()) : null);
		this.rubrica = new Rubrica(object[9] !=  null ? Long.valueOf(object[9].toString()) : null,nomeCategoria);
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public Rubrica getRubrica() {
		return rubrica;
	}

	public void setRubrica(Rubrica rubrica) {
		this.rubrica = rubrica;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public double getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(double porcentagem) {
		this.porcentagem = porcentagem;
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
		RubricaOrcamento other = (RubricaOrcamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public ComponenteClass getComponente() {
		return componente;
	}

	public void setComponente(ComponenteClass componente) {
		this.componente = componente;
	}

	public SubComponente getSubComponente() {
		return subComponente;
	}

	public void setSubComponente(SubComponente subComponente) {
		this.subComponente = subComponente;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
	public BigDecimal getValorEmpenhado() {
		return valorEmpenhado;
	}

	public void setValorEmpenhado(BigDecimal valorEmpenhado) {
		this.valorEmpenhado = valorEmpenhado;
	}

	public String getNomeComponente() {
		return nomeComponente;
	}

	public void setNomeComponente(String nomeComponente) {
		this.nomeComponente = nomeComponente;
	}

	public String getNomeSubComponente() {
		return nomeSubComponente;
	}

	public void setNomeSubComponente(String nomeSubComponente) {
		this.nomeSubComponente = nomeSubComponente;
	}

	public String getNomeCategoria() {
		return nomeCategoria;
	}

	public void setNomeCategoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}

	public BigDecimal getSaldoOrcamento() {
		return saldoOrcamento;
	}

	public void setSaldoOrcamento(BigDecimal saldoOrcamento) {
		this.saldoOrcamento = saldoOrcamento;
	}
	
	
	
}
