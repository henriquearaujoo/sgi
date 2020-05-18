package model;

public enum StatusAtividade {
	
	OPEN("Não Iniciada"),
	BUILDING("Em Excução"),
	CLOSED("Concluído");
	
	
	private StatusAtividade(String nome){
		this.nome = nome;
	}
	
	private String nome;
	
	public String getNome(){
		return nome;
	}
	

}
