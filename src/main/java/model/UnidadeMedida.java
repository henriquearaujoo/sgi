package model;

public enum UnidadeMedida {
	
	ADESIVO("Adesivo"),
	BARRA("Barra"),
	BLOCO("Bloco"),
	CAIXA("Caixa"),
	CARTELA("Cartela"),
	CHAPA("Chapa"),
	DIARIA("Diaria"),
	DUZIA("Duzia"),
	ENVELOPE("Envelope"),
	FARDO("Fardo"),
	FOLHAS("Folhas"),
	FORMAS("Formas"),
	FRASCO("Frascos"),
	GALAO("Galao"),
	GRAMA("Grama"),
	JOGO("KIT"),
	KILOGRAMA("Kilograma"),
	LATA("Lata"),
	LATAO("Latao"),
	LITRO("Litro"),
	MACO("Maço"),
	MAO_DE_OBRA("Mão de obra"),
	METRO("Metro"),
	METRO_CUBICO("Metro Cubico"),
	METRO_LINEAR("Metro Linear"),
	METRO_QUADRADO("Metro Quadrado"),
	METRO_PROFUNDIDADE("Metro Profundidade"),
	MILHEIRO("Milheiro"),
	MILIGRAMA("Miligrama"),
	MILIMETRO("Milimetro"),
	PACOTE("Pacote"),
	PAR("Par"),
	PECA("Peça"),
	POTE("Pote"),
	RESMA("Resma"),
	ROLO("Rolo"),
	SACO("Saco"),
	SERVICO("Serviço"),
	UNIDADE("Unidade"),
	VARA("Vara"),
	VIDRO("Vidro");
	
	
	private String nome;
	
	private UnidadeMedida(String nome){
		this.nome = nome;
	}
	
	public String getNome(){
		return nome;
	}

}
