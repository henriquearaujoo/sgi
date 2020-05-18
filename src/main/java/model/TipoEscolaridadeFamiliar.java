package model;

public enum TipoEscolaridadeFamiliar {

	ALFABETIZACAO("Alfabetização"),
	NAO_ESTUDOU("Não Estudou"),
	EDUCACAO_DE_JOVENS_E_ADULTOS("Educação de Jovens e Adultos (EJA)"),
	REESCREVENDO_O_FUTURO("Reescrevendo o Futuro"),
	ENSINO_FUNDAMENTAL("Ensino Fundamental"),
	ENSINO_MEDIO("Ensino Médio"),
	ENSINO_SUPERIOR_COMPLETO("Ensino Superior Completo"),
	ENSINO_SUPERIOR_INCOMPLETO("Ensino Superior Incompleto");
	
	private String descricao;
	
	private TipoEscolaridadeFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
}
