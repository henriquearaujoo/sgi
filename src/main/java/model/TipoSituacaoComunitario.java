package model;

public enum TipoSituacaoComunitario {

	DENTRO_DA_UC("Dentro da UC"),
	FORA_DA_UC("Fora da UC");
	
	private String descricao;
	
	private TipoSituacaoComunitario(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
	
}
