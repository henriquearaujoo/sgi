package model;

public enum TipoFontesEnergiaComunitario {

	LAMPARINA("Lamparina", false),
	LUZ_PARA_TODOS("Luz para todos", false),
	PLACA_SOLAR("Placa Solar", false),
	GERADOR_INDIVIDUAL("Gerador individual", false),
	GERADOR_COMUNITARIO("Gerador comunitario", false),
	RODA_DAGUA("Roda d'água", false),
	LANTERNA("Lanterna", false),
	OUTRO("Outro", true);
	
	private String descricao;
	private Boolean temResposta;
	
	private TipoFontesEnergiaComunitario(String descricao, Boolean temResposta){
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
