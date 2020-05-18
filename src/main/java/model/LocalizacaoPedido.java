package model;

public enum LocalizacaoPedido {
	
	N_INICIADO("Não iniciado"),
	CONTAINER_FAS("Container FAS"),
	FORNECEDOR("Fornecedor"),
	ENVIADO("Enviado"),
	ENTREGUE("Entregue"),
	EM_TRANSITO("Em trânsito");
	
	
	private String nome;
	
	private LocalizacaoPedido(String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

}
