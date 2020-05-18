package model;

public enum TipoAnoEscolaridadeFamiliar {

	NAO_SE_APLICA("Não se aplica"),
	PRIMEIRO("1º"),
	SEGUNDO("2º"),
	TERCEIRO("3º"),
	QUARTO("4º"),
	QUINTO("5º"),
	SEXTO("6º"),
	SETIMO("7º"),
	OITAVO("8º"),
	NONO("9º");
	
	
	private String descricao;
	
	private TipoAnoEscolaridadeFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
}
