package model;

public enum CondicaoPagamento {
	
	BOLETO("Boleto bancário"),
	DEPOSITO("Transferência bancária"),
	CARTAO("Cartão de crédito"),
	CHEQUE("Cheque");
	
	private String nome;
	
	private CondicaoPagamento(String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

}
