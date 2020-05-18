package model;

public enum TipoAmbienteFamiliar {

	VARZEA("Várzea"),
	MISTO("Misto"),
	TERRA_FIRME("Terra firme");
	
	
	private String descricao;
	
	private TipoAmbienteFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
	
}
