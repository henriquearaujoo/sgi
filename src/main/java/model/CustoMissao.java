package model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class CustoMissao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Id 
	@GeneratedValue
	private long id;
	
	@Enumerated(EnumType.STRING)
	private TipoCustoMissao tipoCusto;
	
	@ManyToOne
	private RelatorioCampo relatorioCampo;
	
	private double previsto;
	
	private double realizado;
	
	private double economizado;
	
	private double total;

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public TipoCustoMissao getTipoCusto() {
		return tipoCusto;
	}

	public void setTipoCusto(TipoCustoMissao tipoCusto) {
		this.tipoCusto = tipoCusto;
	}

	public RelatorioCampo getRelatorioCampo() {
		return relatorioCampo;
	}

	public void setRelatorioCampo(RelatorioCampo relatorioCampo) {
		this.relatorioCampo = relatorioCampo;
	}

	public double getPrevisto() {
		return previsto;
	}

	public void setPrevisto(double previsto) {
		this.previsto = previsto;
	}

	public double getRealizado() {
		return realizado;
	}

	public void setRealizado(double realizado) {
		this.realizado = realizado;
	}

	public double getEconomizado() {
		return economizado;
	}

	public void setEconomizado(double economizado) {
		this.economizado = economizado;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
	
	
	
	
	
	
}
