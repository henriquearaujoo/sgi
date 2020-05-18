package model;

public enum TipoEntrega {

	N_BENS("Número de bens"),
	INFRA("Infraestruturas");
	
private String nome;
	
	private TipoEntrega(String nome){
		this.nome = nome;
	
	}
	
	public String getNome(){
		return nome;
	}
	
}
