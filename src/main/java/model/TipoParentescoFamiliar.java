package model;

public enum TipoParentescoFamiliar {

	FILHO("Filho"),
	NETO("Neto"),
	ENTEADO("Enteado"),
	PAI_MAE_ESPOSA("Pai/Mãe da Esposa"),
	PAI_MAE_ESPOSO("Pai/Mãe do Esposo"),
	SOBRINHO("Sobrinho"),
	OUTROS("Outros");
	
	private String descricao;
	
	private TipoParentescoFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
}
