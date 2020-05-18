package model;

public enum TipoOrganizacaoSocialInformalFamiliar {

	ESPORTIVA("Esportiva", false),
	RECREATIVA("Recreativa", false),
	GRUPOS_PRODUTIVOS("Grupos produtivos", false),
	GRUPO_DE_JOVENS("Grupo de jovens", false),
	OUTRO("Outro", true);
	
	private String descricao;
	private Boolean temResposta;
	
	private TipoOrganizacaoSocialInformalFamiliar(String descricao, Boolean temResposta){
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
