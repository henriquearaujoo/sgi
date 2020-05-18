package model;

public enum TipoArquivo {
	
	NF("Nota Fiscal"),
	PROCESSO("Processo"),
	COMPROVANTE("Comprovante");
	
	
	private TipoArquivo(String nome){
		this.nome = nome;
	}

	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
