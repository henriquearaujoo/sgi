package model;

public enum TipoAbastecimentoAguaFamiliar {

	DIRETO_DO_RIO("Direto do rio", false),
	DIRETO_DO_IGARAPE("Direto do igarapé", false),
	COLETA_DE_AGUA_DA_CHUVA("Coleta de água da chuva", false),
	POCO_COMUNITARIO("Poço comunitário", false),
	POCO_ARTESIANO_PROPRIO("Poço artesiano próprio", false),
	CISTERNA("Cisterna", false),
	CACIMBA_COMUNITARIA("Cacimba comunitária", false),
	COLETIVO_E_REDE_DE_DISTRIBUICAO("Coletivo e rede de distribuição", false),
	OUTRO("Outro", true);
	
	private String descricao;
	private Boolean temResposta;
	
	private TipoAbastecimentoAguaFamiliar(String descricao, Boolean temResposta){
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
