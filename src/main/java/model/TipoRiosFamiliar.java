package model;

public enum TipoRiosFamiliar {

	ABACATE("Abacate"),
	APOQUITAUA("Apoquitaua"),
	ARAUA("Arauá"),
	ARIPUANA("Aripuanã"),
	CAIOE("Caié"),
	CANUMA("Canumã"),
	CARIBE("Caribé"),
	CUIERAS("Cuieras"),
	GREGORIO("Gregorio"),
	JAPURA("Japurá"),
	JATAPU("Jatapú"),
	JUMA("Juma"),
	JURUA("Juruá"),
	JUTAI("Jutaí"),
	LAGO_DO_AYAPUA("Lago do Ayapuá"),
	LAGO_DO_CAUA("Lago do Cáua"),
	LAGO_DO_JARI("Lago do Jari"),
	LAGO_DO_PARICATUBA("Lago do Paricatuba"),
	MADEIRA("Madeira"),
	MAREPAUA("Marepaua"),
	MUJUI("Mujuí"),
	NEGRO("Negro"),
	PARANA_DO_ACARIQUARA("Paraná do Acariquara"),
	PARANA_DO_SAO_JOAO("Paraná do São João"),
	PARAUARI("Parauari"),
	PURUS("Purus"),
	RAMAL_KM_59("Ramal KM 59"),
	SOLIMOES("Solimões"),
	UATUMA("Uatumã"),
	URARIA("Uraiá");
	
	
	private String descricao;
	
	private TipoRiosFamiliar(String descricao){
		this.descricao = descricao;
	}

	public  String getDescricao(){
		return descricao;
	}
	
}
