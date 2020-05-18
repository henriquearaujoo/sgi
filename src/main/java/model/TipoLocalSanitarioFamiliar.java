package model;

public enum TipoLocalSanitarioFamiliar {

	DENTRO_DE_CASA("Dentro de casa", false),
	FORA_DE_CASA("Fora de casa", false);
	
	private String descricao;
	private Boolean temResposta;
	
	private TipoLocalSanitarioFamiliar(String descricao, Boolean temResposta){
		this.descricao = descricao;
		this.temResposta = temResposta;
	}

	public  String getDescricao(){
		return descricao;
	}
	
	public Boolean getTemReposta(){
		return temResposta;
	}
}
