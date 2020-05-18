package model;

public enum Destino {


	AM_INTERIOR("Amazonas Interior"),
	AM_CAPITAL("Amazonas Capital"),
	INTER_AMERICAS("Internacional Americas"),
	INTER("Internacional"),
	NACIONAL("Nacional");
	
	
	
	private String nome;
	
	private Destino(String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	
}
