package model;

public enum TipoDesligamentoFamiliar {

	NAO_MORADOR("Não morador"),
	FALECIMENTO("Falecimento"),
	TROCA_DE_TITULARIDADE("Troca de titularidade"),
	DESISTENCIA("Desistência"),
	IRREGULARIDADE("Irregularidade");
	
	private String descricao;
	
	private TipoDesligamentoFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
}
