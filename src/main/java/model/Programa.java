package model;

public enum Programa {
	
	PBF("Bolsa Floresta"),
	PES("Educação e saúde"),
	PSI("Soluções Inovadoras"),
	PGT("Programa de Gestão e Transparência");
	
	
	private String nome;
	
	private Programa(String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

}
