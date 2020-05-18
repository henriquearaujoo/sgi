package model;

public enum TipoFinanciadorComunitario {

	FAS("FAS", false),
	PREFEITURA("Prefeitura", false),
	ASSOCIACAO("Associação", false),
	OUTRO("Outro", true);
	
	private String descricao;
	private Boolean temResposta;
	
	private TipoFinanciadorComunitario(String descricao, Boolean temResposta){
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
