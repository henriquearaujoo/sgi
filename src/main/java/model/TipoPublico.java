package model;

public enum TipoPublico {

	Homen("Homen"),
	Mulher("Mulher");
	
	private String nome;
	
	private TipoPublico(String nome){
		this.nome = nome;
	}
	
	public String getNome(){
		return nome;
	}
	
}
