package model;

public enum StatusAdiantamento {

		EM_ABERTO("Em aberto"),
		EM_ANALISE("Em análise"),
		VALIDADO("Validado"),
		DEVOLUCAO("Devolução de documentos");
	
	
		private String nome;
		
		private StatusAdiantamento(String nome){
			this.nome = nome;
		}
	
		public  String getNome(){
			return nome;
		}
}
