package model;

public enum TipoTratamentoAguaFamiliar {

	CLORO("Cloro", false),
	AGUA_FERVIDA("Água fervida", false),
	POTE("Pote", false),
	FILTRO("Filtro", false),
	SACHES_PEG("Sachês P&G", false),
	OUTRO("Outro tratamento", true);
	
	private String descricao;
	private Boolean temResposta;
	
	private TipoTratamentoAguaFamiliar(String descricao, Boolean temResposta){
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
