package model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "configuracao")
public class Configuracao implements Serializable{

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		@Column
		private Integer tempoSC;
		@Column
		private Integer tempoSP;
		@Column
		private Integer tempoSD;
		
		@Column
		private BigDecimal valorTarifaBancaria;

		public BigDecimal getValorTarifaBancaria() {
			return valorTarifaBancaria;
		}

		public void setValorTarifaBancaria(BigDecimal valorTarifaBancaria) {
			this.valorTarifaBancaria = valorTarifaBancaria;
		}

		public Configuracao() {		
	
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Integer getTempoSC() {
			return tempoSC;
		}

		public void setTempoSC(Integer tempoSC) {
			this.tempoSC = tempoSC;
		}

		public Integer getTempoSP() {
			return tempoSP;
		}

		public void setTempoSP(Integer tempoSP) {
			this.tempoSP = tempoSP;
		}

		public Integer getTempoSD() {
			return tempoSD;
		}

		public void setTempoSD(Integer tempoSD) {
			this.tempoSD = tempoSD;
		}
		
		
	
	
}


