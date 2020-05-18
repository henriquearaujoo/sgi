package model;

public enum TipoDestinoLixoFamiliar {

	QUEIMA("Queima", false),
	ENTERRA("Enterra", false),
	JOGA_NO_RIO("Joga no rio", false),
	JOGA_NO_MATO("Joga no mato", false),
	COLETA_PUBLIC("Coleta pública", false),
	REAPROVEIRTA("Reaproveita", false),
	SEPARA_LIXO_ORGANICO("Separa o lixo orgânico do resto", false),
	OUTRO("Outro", true),;
	
	private String descricao;
	private Boolean temResposta;
	
	private TipoDestinoLixoFamiliar(String descricao, Boolean temResposta){
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
