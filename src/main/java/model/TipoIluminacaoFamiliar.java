package model;

public enum TipoIluminacaoFamiliar {

	LAMPARINA("Lamparina"),
	LAMPIAO("Lampião"),
	GERADOR_INDIVIDUAL("Gerador individual"),
	GERADOR_COMUNITARIO("Gerador comunitário"),
	REDE_PUBLICA("Rede pública"),
	PLACA_SOLAR("Placa solar"),
	VELA("Vela"),
	OUTRO("Outro");
	
	private String descricao;
	
	private TipoIluminacaoFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
	
	
}
