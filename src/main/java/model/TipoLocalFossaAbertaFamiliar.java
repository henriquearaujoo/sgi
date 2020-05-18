package model;

public enum TipoLocalFossaAbertaFamiliar {

	ABAIXO("Abaixo", false),
	ACIMA("Acima", false),
	MENOS_DE_20M("Menos de 20 m", false),
	MAIS_DE_20M("Mais de 20 m", false);
	
	private String descricao;
	private Boolean temResposta;
	
	private TipoLocalFossaAbertaFamiliar(String descricao, Boolean temResposta){
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
