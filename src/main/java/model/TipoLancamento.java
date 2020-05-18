package model;

public enum TipoLancamento {
	
	RECEITA("Receita"),
	DESPESA("Despesa"),
	NEUTRO("Neutro");
	
	
	private String nome;
	
	private TipoLancamento(String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

}
