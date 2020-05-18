package model;

public enum StatusFornecedor {
	
	PRE_CADASTRADO("Pré cadastrado"),
	CADASTRADO("Cadastrado"),
	BLOQUEADO("Bloqueado");
	
	
	private String nome;
	
	private StatusFornecedor(String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

}
