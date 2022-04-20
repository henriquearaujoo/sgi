package model;

public enum TipoPagamento {
	
	DEP_CONTA("Depósito em conta"),
	BOLETO("Boleto bancário"),
	CHEQUE("Cheque");
	
	
	private TipoPagamento(String nome){
		this.nome = nome;
	}
	
	private String nome;
	
	public String getNome(){
		return nome;
	}
	

}
