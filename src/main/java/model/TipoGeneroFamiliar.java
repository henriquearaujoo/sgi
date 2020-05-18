package model;

public enum TipoGeneroFamiliar {

	MASCULINO("Masculino"),
	FEMININO("Feminino");
	
	private String descricao;
	
	private TipoGeneroFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
}
