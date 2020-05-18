package model;

public enum TipoStatusCartaoFamiliar {

	BLOQUEADO("Bloqueado"),
	NOVO("Novo"),
	SUSPENSO("Suspenso"),
	VALIDO("Válido");
	
	private String descricao;
	
	private TipoStatusCartaoFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
}
