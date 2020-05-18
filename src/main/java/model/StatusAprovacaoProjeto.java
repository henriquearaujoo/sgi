package model;

public enum StatusAprovacaoProjeto {

	EM_APROVACAO("Em aprovação"),
	APROVADO("Aprovado");

	private String descricao;

	private StatusAprovacaoProjeto(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
}
