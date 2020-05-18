package model;

public enum TipoSaidaComunidadeFamiliar {

	ESTUDO("Estudo"),
	TRABALHO("Trabalho"),
	DOENCA("Doença"),
	OUTROS("Outros");
	
	private String descricao;
	
	private TipoSaidaComunidadeFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
}
