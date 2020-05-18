package model;

public enum StatusSolicitacaoExpedicao {
	
	ACOLHIDO("Acolhido - aguardando controle expedição"),
	EXPEDICAO_FINALIZADA("Expedição finalizada"),
	N_INICIADO("Não iniciado");
	
	private String nome;
	
	private StatusSolicitacaoExpedicao(String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

}
