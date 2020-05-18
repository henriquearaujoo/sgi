package model;

public enum StatusPagamentoLancamento {
	
	PROVISIONADO("Provisionado"),
	EFETIVADO("Efetivado");
	
	private String nome;
	
	private StatusPagamentoLancamento(String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

}
