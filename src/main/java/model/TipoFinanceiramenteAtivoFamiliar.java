package model;

public enum TipoFinanceiramenteAtivoFamiliar {

	SIM("Sim"),
	NAO("Não"),
	DESLIGADO("Desligado");
	
	private String descricao;
	
	private TipoFinanceiramenteAtivoFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
	
}
