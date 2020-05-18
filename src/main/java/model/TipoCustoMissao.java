package model;

public enum TipoCustoMissao {

	CUSTO_FAS("Custo Fas"),
	MOBILIZACAO_FAS("Mobilização Fas"),
	APOIO_TERCEIROS("Apoio de Terceiros");
	
private String nome;
	
	private TipoCustoMissao(String nome){
		this.nome = nome;
	}
	
	public String getNome(){
		return nome;
	}
	
	
}
