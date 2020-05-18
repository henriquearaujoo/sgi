package model;

public enum StatusPagamento {

		VALIDADO("Validado"),
		N_VALIDADO("Não validado");
	
		private String nome;
		
		private StatusPagamento(String nome){
			this.nome = nome;
		}
	
		public  String getNome(){
			return nome;
		}
}
