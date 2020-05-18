package model;

public enum TipoIdentificacaoBancariaFamiliar {

	POR_CPF("Por CPF"),
	POR_PROTOCOLO("Por Protocolo");
	
	private String descricao;
	
	private TipoIdentificacaoBancariaFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
}
