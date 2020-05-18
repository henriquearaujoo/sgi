package model;

import operator.LancamentoIterface;

public enum TipoLancamentoV4 {
	
	SC("Compra") {
		@Override
		public LancamentoIterface obterInstancia() {
			return new Compra();
		}
	},
	SP("Pagamento") {
		@Override
		public LancamentoIterface obterInstancia() {
			return new SolicitacaoPagamento();
		}
	},
	SA("Adiantamento") {
		@Override
		public LancamentoIterface obterInstancia() {
			return new SolicitacaoPagamento();
		}
	},
	SD("Diária") {
		@Override
		public LancamentoIterface obterInstancia() {
			return new Diaria();
		}
	},
	PC("Pedido") {
		@Override
		public LancamentoIterface obterInstancia() {
			return new Pedido();
		}
	};
	
	
	private String nome;
	
	public abstract LancamentoIterface obterInstancia();
	
	private TipoLancamentoV4(String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

}
