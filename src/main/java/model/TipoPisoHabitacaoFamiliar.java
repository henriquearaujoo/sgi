package model;

public enum TipoPisoHabitacaoFamiliar {

	CIMENTO("Cimento"),
	BARRO_BATIDO("Barro batido"),
	MADEIRA_ASSOALHO("Madeira/assoalho");
	
	private String descricao;
	
	private TipoPisoHabitacaoFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
	
	
}
