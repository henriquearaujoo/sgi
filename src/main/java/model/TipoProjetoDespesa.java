package model;

public enum TipoProjetoDespesa {

	MESMO_PROJETO("Mesmo projeto"),
	OUTRO("Outro");


	private TipoProjetoDespesa(String nome){
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
