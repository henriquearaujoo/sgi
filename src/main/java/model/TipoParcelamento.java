package model;

import auxiliar.ParcelaBimestral;
import auxiliar.ParcelaMensal;
import auxiliar.ParcelaQuinzenal;
import auxiliar.ParcelaSemanal;
import auxiliar.ParcelaSemestral;
import auxiliar.ParcelaTrimestral;
import auxiliar.ParcelaUnica;
import operator.Parcela;

public enum TipoParcelamento {
	
	PARCELA_UNICA("Parcela única/A vista") {
		@Override
		public Parcela obterParcela() {
			return new ParcelaUnica();
		}
	},
	SEMANAL("Semanal") {
		@Override
		public Parcela obterParcela() {
			return new ParcelaSemanal();
		}
	},
	QUINZENAL("Quinzenal") {
		@Override
		public Parcela obterParcela() {
			return new ParcelaQuinzenal();
		}
	},
	MENSAL("Mensal") {
		@Override
		public Parcela obterParcela() {
			return new ParcelaMensal();
		}
	},
	BIMESTRE("Bimestre") {
		@Override
		public Parcela obterParcela() {
			return new ParcelaBimestral();
		}
	},
	TRIMESTRE("Trimestre") {
		@Override
		public Parcela obterParcela() {
			return new ParcelaTrimestral();
		}
	},
	SEMESTRE("Semestre") {
		@Override
		public Parcela obterParcela() {
			return new ParcelaSemestral();
			
		}
	};
	
	
	private String nome;
	
	public abstract Parcela obterParcela();
	
	private TipoParcelamento(String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

}
