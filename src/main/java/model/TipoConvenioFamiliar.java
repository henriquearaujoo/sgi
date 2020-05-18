package model;

public enum TipoConvenioFamiliar {

	FAS("FAS"),
	AFEAM("AFEAM");
	
	private String descricao;
	
	private TipoConvenioFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
}
