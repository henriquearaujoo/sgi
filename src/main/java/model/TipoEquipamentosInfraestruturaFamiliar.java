package model;

public enum TipoEquipamentosInfraestruturaFamiliar {

	CASA_DE_FARINHA_MECANIZADA("Casa de farinha mecanizada", false),
	CASA_DE_FARINHA_TRADICIONAL("Casa de farinha tradicional", false),
	BAARRACOES_DE_ARMAZENAMENTO_DE_PRODUCAO("Barracões de armazenamento de produção", false),
	MOTOSSERRA("Motosserra", false),
	ROCADEIRA("Roçadeira", false),
	OUTRO("Outro", true);
	
	private String descricao;
	private Boolean temResposta;
	
	private TipoEquipamentosInfraestruturaFamiliar(String descricao, Boolean temResposta){
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
