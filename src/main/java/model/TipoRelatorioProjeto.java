package model;

public enum TipoRelatorioProjeto {

		//ORCADO_REALIZADO("Orçado x Realizado");
//		CONTA_PAGAR("Contas a pagar"),
		CONFERENCIA_LANCAMENTO("Conferência de lançamentos");
	
		private String nome;
		
		private TipoRelatorioProjeto(String nome){
			this.nome = nome;
		}
	
		public  String getNome(){
			return nome;
		}
}
