package model;

public enum TipoAdministrativoProjeto {
	
	CT("Controle total"),
	LEITURA("Leitura"),
	LEITURA_GRAVACAO("Leitura e gravação");
	
	
	private TipoAdministrativoProjeto(String nome){
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
