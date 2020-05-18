package model;

public enum LogStatusEnum {
	
	RECEITA("Receita"),
	DESPESA("Despesa"),
	HERANCA("Herança");

	private String nome;
	
	private LogStatusEnum(String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

}
