package model;

public enum TipoImposto {
	
	PF_INSS("PF-INSS"),
	PJ_INSS("PJ-INSS"),
	PF_IRRF("PF-IRRF"),
	PJ_IRRF("PJ-IRRF"),
	PCC("PCC"),
	PIS("PIS"),
	FGTS("FGTS");
	

	private String nome;
	
	private TipoImposto(String nome){
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

}
