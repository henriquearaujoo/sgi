package model;

public enum TipoDespesa {
		
	DESPESAS("despesas"),
	PROVIMENTOS("provimentos"),
	PASSAGENS("passagens"),
	CUSTOS_ECONOMIZADOS("custos economizados");
	
	
	private String nome;
	
	private TipoDespesa(String nome){
		this.nome = nome;
	
	}
	
	public String getNome(){
		return nome;
	}
	
}
