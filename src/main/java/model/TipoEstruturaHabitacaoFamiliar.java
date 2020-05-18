package model;

public enum TipoEstruturaHabitacaoFamiliar {

	PALAFITA("Palafita"),
	FLUTUANTE("Flutuante"),
	PISO_DIRETO_NO_CHAO("Piso direto no chão");
	
	private String descricao;
	
	private TipoEstruturaHabitacaoFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
	
	
}
