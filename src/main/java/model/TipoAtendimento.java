package model;

public enum TipoAtendimento {

	
	NOVA("Nova demanda"),
	SUPORTE("Suporte"),
	BUG("Bug");
	

	private String nome;
	
	private TipoAtendimento(String nome){
		this.nome = nome;
	}
	
	public String getNome(){
		return nome;
	}
	
	
}
