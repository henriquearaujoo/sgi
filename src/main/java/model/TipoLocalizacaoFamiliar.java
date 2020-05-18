package model;

public enum TipoLocalizacaoFamiliar {

	MORADIA_ISOLADA("Moradia Isolada", false),
	LOCALIDADE("Localidade", true),
	COMUNIDADE("Comunidade", true);
	
	private String descricao;
	private Boolean temResposta;
	
	private TipoLocalizacaoFamiliar(String descricao, Boolean temResposta){
		this.descricao = descricao;
		this.temResposta = temResposta;
	}

	public  String getDescricao(){
		return descricao;
	}
	
	public Boolean getTemResposta(){
		return temResposta;
	}
}
