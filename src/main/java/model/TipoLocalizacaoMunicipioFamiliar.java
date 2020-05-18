package model;

public enum TipoLocalizacaoMunicipioFamiliar {

	SETOR("Setor", true),
	POLO("Pólo", true),
	RIO("Rio", true),
	LAGO("Lago", true),
	OUTRO("Outro", true),
	NAO_APLICAVEL("Não aplicável", false);
	
	private String descricao;
	private Boolean temResposta;
	
	private TipoLocalizacaoMunicipioFamiliar(String descricao, Boolean temResposta){
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
