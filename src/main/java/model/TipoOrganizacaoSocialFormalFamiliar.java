package model;

public enum TipoOrganizacaoSocialFormalFamiliar {

	CONSELHO("Conselho", false),
	ASSOCIACAO_MAE("Associação-mãe", false),
	ASSOCIACAO_COMUNITARIA("Associação comunitária", false),
	ASSOCIACAO_SETORIAL("Associação setorial", false),
	COOPERATIVA("Cooperativa", false),
	EMPRESA_BASE_COMUNITARIA("Empresa de base comunitária", false),
	SINDICATO_TRABALHADORES_RURAIS("Sindicato dos trabalhadores rurais", false),
	COLONIA_DE_PESCADORES("Colônia de pescadores", false),
	OUTRO("Outro", true);
	
	private String descricao;
	private Boolean temResposta;
	
	private TipoOrganizacaoSocialFormalFamiliar(String descricao, Boolean temResposta){
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
