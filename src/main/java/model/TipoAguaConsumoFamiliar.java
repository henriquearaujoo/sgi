package model;

public enum TipoAguaConsumoFamiliar {

	SEM_TRATAMENTO("Sem tratamento", false),
	COM_TRATAMENTO("Com Tratamento", true);
	
	private String descricao;
	private Boolean temResposta;
	
	private TipoAguaConsumoFamiliar(String descricao, Boolean temResposta){
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
