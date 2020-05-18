package model;

public enum StatusCompra {
	
	N_INCIADO("Não iniciado/Solicitado"),
	APROVADO("Autorizado"),
	EM_COTACAO("Em cotação"),
	
	CANCELADO("Cancelado"),
	PARCIAL_CONCLUIDO("Parcialmente Concluído"),
	CONCLUIDO("Concluído"),
	EDICAO("Editado"),
	VALIDADO("Validado"),
	DESPROV("Desprovisionado"),
	
	
	/**Status exclusivo pedido*/
	
	PENDENTE_APROVACAO("1º autorização"),
	PENDENTE_APROVACAO_TWO("2º autorização"),
	PENDENTE_APROVACAO_THREE("3º autorização"),
	PENDENTE_APROVACAO_FOUR("4º autorização"),
	PENDENTE_APROVACAO_FIVE("5º autorização"),
	PENDENTE_EMAIL("Pendente envio de Email");

	private String nome;
	
	private StatusCompra(String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

}
