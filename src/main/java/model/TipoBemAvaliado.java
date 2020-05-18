package model;

public enum TipoBemAvaliado {

	
	AMBULANCHA("Ambulancha"),
	RADIO("Rádio");
	

	private String nome;
	
	private TipoBemAvaliado(String nome){
		this.nome = nome;
	}
	
	public String getNome(){
		return nome;
	}
	
	
}
