package model;

public enum DespesaReceita {
	
	DESPESA("Despesa"),
	RECEITA("Receita"),
	HERANCA("Herança");

	private String nome;
	
	private DespesaReceita(String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

}
