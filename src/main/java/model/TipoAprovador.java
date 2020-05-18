package model;

public enum TipoAprovador {

	SUP_ADM("Luiz Villarez"),
	SUP_SSA("Valcléia Solidade"),
	SUP_TEC("Eduardo Taveira");
	
		private String nome;
		
		private TipoAprovador(String nome){
			this.nome = nome;
		}
	
		public  String getNome(){
			return nome;
		}
}
