package model;

public enum TipoDeDocumentoFiscal {

		NF("Nota fiscal"),
		NFE("Nota fiscal eletrônica"),
		RECIBO("Recibo"),
		FATURA("Fatura"),
		OUTRO("Outros");
	

		private String nome;
		
		private TipoDeDocumentoFiscal(String nome){
			this.nome = nome;
		}
	
		public  String getNome(){
			return nome;
		}
}
