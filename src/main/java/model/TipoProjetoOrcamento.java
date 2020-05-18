package model;

public enum TipoProjetoOrcamento {

	POR_ORCAMENTO("Por orçamento"),
	POR_CAIXA("Caixa");


	private TipoProjetoOrcamento(String nome){
		this.nome = nome;
	}

	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
