package model;

public enum StatusConta {

	ATIVA("Ativada"),
	INATIVA("Desativada");


	private String nome;
	
	private StatusConta(String nome){
		this.nome = nome;
	}

	public  String getNome(){
		return nome;
	}
}
