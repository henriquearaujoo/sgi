package model;

public enum TipoFormasPegarAguaComunitario {

	BALDES_OU_LATAS("Baldes ou latas", false),
	MOTORBOMBA("Motorbomba", false),
	CACIMBA("Caçimba", false),
	POCO_ARTESIANO("Poço artesiano", false),
	PROCHUVA("Prochuva", false),
	OUTRO("Outro", true);
	
	private String descricao;
	private Boolean temResposta;
	
	private TipoFormasPegarAguaComunitario(String descricao, Boolean temResposta){
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
