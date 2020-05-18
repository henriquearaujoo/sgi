package model;

public enum TipoReunioesComunidadeComunitario {

	ESCOLA("Escola", false),
	IGREJA("Igreja", false),
	CASA_DE_COMUNITARIO("Casa de comunitário", false),
	SEM_LOCAL_DEFINIDO("Sem local definido", false),
	OUTRO("Outro", true);
	
	private String descricao;
	private Boolean temResposta;
	
	private TipoReunioesComunidadeComunitario(String descricao, Boolean temResposta){
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
