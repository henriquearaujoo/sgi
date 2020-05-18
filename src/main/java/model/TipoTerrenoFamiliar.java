package model;

public enum TipoTerrenoFamiliar {

	ABAIXO("Abaixo"),
	ACIMA("Acima"),
	MENOS_DE_20M("Menos de 20 m"),
	MAIS_DE_20M("Mais de 20 m");
	
	
	private String descricao;
	
	private TipoTerrenoFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
	
}
