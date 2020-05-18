package model;

public enum TipoMaterialHabitacaoFamiliar {

	MADEIRA("Madeira"),
	PALHA("Palha"),
	ALVENARIA("Alvenaria"),
	TAIPA("Taipa"),
	PAXIUBA("Paxiúba"),
	OUTRO("Outro");
	
	private String descricao;
	
	private TipoMaterialHabitacaoFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
	
	
}
