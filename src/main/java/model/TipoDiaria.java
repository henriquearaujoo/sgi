package model;

public enum TipoDiaria {

	DIARIA("Diária");
	
	private String nome;
	
	private TipoDiaria(String nome){
		this.nome = nome;
	}
	
	public String getNome(){
		return nome;
	}
	
	
}
