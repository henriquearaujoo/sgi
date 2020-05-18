package model;

public enum TipoColeta {

	caderneta_ambulancha("Caderneta de ambulanchas"),
	questionario_comun("Questionário comunitário"),
	questionario_radio("Questionário de rádios"),
	formulario_indicadores("Formularios de Indicadores"),
	ficha_cadeia_produtiva("Ficha de cadeias produtivas");
	
	
	
	private String nome;
	
	private TipoColeta(String nome){
		this.nome = nome;
	}
	
	public String getNome(){
		return nome;
	}
	
	
	
}
