package model;

public enum TipoTransporteFamiliar {

	MOTOR_DE_CENTRO("Motor de centro (em hp)", true),
	MOTOR_DE_POPA("Motor de popa (em hp)", true),
	MOTOR_RABETA("Motor rabeta (em hp)", true),
	CANOA("Canoa", false),
	BOTE_DE_ALUMINIO("Bote de alumínio", false),
	MOTOCICLETA("Motocicleta", false),
	CARROCA_DE_BOI("Carroça de boi", false),
	OUTRO("Outros", true);
	
	private String descricao;
	private Boolean temResposta;
	
	private TipoTransporteFamiliar(String descricao, Boolean temResposta){
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
