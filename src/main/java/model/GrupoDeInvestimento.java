package model;

public enum GrupoDeInvestimento {
	
	AQUISICAO("Aquisição"),
	REFORMA("Reforma"),
	PRESTACAO_SERVICOS("Prestação de serviços"),
	CONSTRUCAO("Construção"),
	APOIO_FINANCEIRO("Apoio financeiro"),
	ASSIST_TECNICA("Assistencia técnica"),
	ASSESS_TECNICA("Assessoria técnica"),
	AMPLIACAO("Ampliação"),
	INSTALACAO("Instalação"),
	AQUISICAO_INSTALCAO("Aquisição e Ampliação"),
	CONSTRUCAO_AMPLIACAO("Construção e Ampliação"),
	REFORMA_AMPLIACAO("Reforma e Apliação"),
	REVITALIZACAO("Revitalização"),
	REVITALIZACAO_AMPLIACAO("Revitalização e Ampliação"),
	CAPITAL_GIRO("Capital de giro"),
	DOACAO("Doação"),
	AJUDA_CUSTO("Ajuda de custo"),
	CAPACITACAO("Capacitação"),
	EVENTOS("Eventos");
	
	private String nome;
	
	private GrupoDeInvestimento(String nome){
		this.nome = nome;
	}

	public String getNome(){
		return nome;
	}
	
}
