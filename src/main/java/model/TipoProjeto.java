package model;

public enum TipoProjeto {
	
	PROJETO_FIM("Projeto Fim"),
	PROJETO_MEIO("Projeto meio");
	
	private String nome;
	
	private TipoProjeto(String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

}
