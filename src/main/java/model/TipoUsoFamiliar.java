package model;

public enum TipoUsoFamiliar {

	CONSUMO("Consumo"),
	VENDA("Venda");
	
	
	private String descricao;
	
	private TipoUsoFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
	
}
