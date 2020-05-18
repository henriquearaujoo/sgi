package model;

public enum TipoDesbloqueioRecursoFamiliar {

	SALDO_ACUMULADO("Saldo acumulado"),
	CARTAO_NOVO("Cartão novo");
	
	private String descricao;
	
	private TipoDesbloqueioRecursoFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
}
