package model;

public enum TipoDestinoEsgotoFamiliar {

	CEU_ABERTO("Céu aberto", false),
	PAU_DA_GATA("Pau da gata", false),
	FOSSA_ABERTA("Fossa aberta", false),
	RIO_OU_IGARAPE("Rio ou igarapé", false),
	PEDRA_SANITARIA("Pedra sanitária", false),
	FOSSA_SEPTICA("Fossa séptica", false),
	BIODIGESTOR("Biodigestor", false),
	OUTRO("Outro", true);
	
	private String descricao;
	private Boolean temResposta;
	
	private TipoDestinoEsgotoFamiliar(String descricao, Boolean temResposta){
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
