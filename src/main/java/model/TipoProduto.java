package model;

public enum TipoProduto {

	BEM("Bem"),
	SERVIÇO("Serviços"),
	MATERIAL("Insumos");
	
	
private String nome;
	
	private TipoProduto(String nome){
		this.nome = nome;
	
	}
	
	public String getNome(){
		return nome;
	}
	
}
