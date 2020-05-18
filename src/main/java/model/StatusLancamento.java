package model;

public enum StatusLancamento {
	
	
	PREVISTO("Agendado para pagamento"),
	PAGO("Lançamento pago");
	
	private String nome;
	
	private StatusLancamento(String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	

}
