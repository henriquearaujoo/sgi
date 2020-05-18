package model;

public enum TipoHabitacaoFamiliar {

	CASA_PROPRIA("Casa própria"),
	CEDIDA_EMPRESTADA("Cedida/emprestada"),
	CASA_DE_FAMILIARES("Casa de familiares"),
	OUTRO("Outro");
	
	private String descricao;
	
	private TipoHabitacaoFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
	
	
}
