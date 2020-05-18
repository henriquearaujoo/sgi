package model;

public enum TipoSeriesEscolaComunitario {

	PRIMEIRO_A_QUINTO("1 a 5 ano", true),
	SEXTO_A_NONO("6 a 9 ano", true),
	ENSINO_MEDIO("Ensino médio", true);
	
	private String descricao;
	private Boolean temResposta;
	
	private TipoSeriesEscolaComunitario(String descricao, Boolean temResposta){
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
