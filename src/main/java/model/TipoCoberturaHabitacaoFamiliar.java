package model;

public enum TipoCoberturaHabitacaoFamiliar {

	ALUMINIO_ZINCO("Alumínio/zinco"),
	BRASILIT("Brasilit"),
	PALHA("Palha"),
	TELHA_DE_BARRO("Telha de barro"),
	OUTRO("Outro");
	
	private String descricao;
	
	private TipoCoberturaHabitacaoFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
	
	
}
