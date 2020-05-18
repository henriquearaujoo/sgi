package model;

public enum Componente {
	
	RENDA("Renda"),
	SOCIAL("Social"),
	ASSOCIACAO("Associacao");
	
	
	private String nome;
	
	private Componente(String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

}
