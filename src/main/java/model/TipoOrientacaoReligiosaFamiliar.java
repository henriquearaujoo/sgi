package model;

public enum TipoOrientacaoReligiosaFamiliar {

	CATOLICA("Católica", false),
	EVANGELICA("Evangélica", false),
	OUTRAS("Outras", true);
	
	private String descricao;
	private Boolean temResposta;
	
	private TipoOrientacaoReligiosaFamiliar(String descricao, Boolean temResposta){
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
