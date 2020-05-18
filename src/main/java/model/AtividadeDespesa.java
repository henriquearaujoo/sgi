package model;

public enum AtividadeDespesa {

		ALUGUEL_BARCO("Aluguel de barco Regional"),
		ALUGUEL_VOADEIRA("Aluguel de Voadeira"),
		ALUGUEL_VEICULO("Aluguel de Veiculo (Caminhão, pick-up, moto, ônibus, etc) "),
		GAS_COZINHA("gás de Cozinha"),
		GASOLINA_COMUNITARIO("gasolina para comunitario"),
		GASOLINA_DESL_EQP("Gasolina para deslocamento da equipe"),
		GASOLINA_EQUIPE("Gasolina para equipe"),
		MOTORISTA("Motorisa"),
		OLEO_DSL_COMUNITARIO("Óle diesel para comunitário "),
		OLEO_DSL_EQP("Óleo diesel para equipe"),
		OLEO_LUBRI("Óleo lubrificante (2T, 20w40, 20w50, outros)"),
		OUTROS("Outros (xerox, moto taxi, translado, etc.)"),
		SRVICE_CARREGADOR("Servico de Carregado"),
		SRVICE_COZINHEIRA("Servico de Cozinheira"),
		SRVICE_PILOTEIRO("Servico de Piloteiro"),
		SRVICE_MATEIRO("Servico Mateiro"),
		PASS_FLUV("Passagem fluvial"),
		PASS_AEREA("Passagem aérea"),
		ALIMENTACAO("Alimentação"),
		CENTRO_SOCIAL("Centro Social"),
		TAXI_LOTACAO("Táxi lotação"),
		CARGA("Cargas");
	
	
		private String nome;
		
		private AtividadeDespesa(String nome){
			this.nome = nome;
		}
	
		public  String getNome(){
			return nome;
		}
}
