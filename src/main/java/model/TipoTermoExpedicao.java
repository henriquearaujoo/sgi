package model;

public enum TipoTermoExpedicao {

		ENTREGA("Entrega"),
		DOACAO("Doação"),
		INFRAESTRUTURA("Infraestrutura");
	
		private String nome;
		
		private TipoTermoExpedicao(String nome){
			this.nome = nome;
		}
	
		public  String getNome(){
			return nome;
		}
}
