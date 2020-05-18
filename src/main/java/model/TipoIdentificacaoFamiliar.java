package model;

public enum TipoIdentificacaoFamiliar {

	TITULAR("Titular"),
	CONJUGE("Cônjuge"),
	OUTRO("Outro");
	
	private String descricao;
	
	private TipoIdentificacaoFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
}
