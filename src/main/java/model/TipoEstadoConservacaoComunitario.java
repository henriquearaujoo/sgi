package model;

public enum TipoEstadoConservacaoComunitario {

	RUIM("Ruim"),
	BOM("Bom"),
	OTIMO("Ótimo");
	
	private String descricao;
	
	private TipoEstadoConservacaoComunitario(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
}
