package model;

public enum CategoriadeDespesa {

		PILOTEIRO("Piloteiro"),
		IMPRESSAO("Impressão"),
		PASSAGEM_FLUVIAL("Passagem fluviais"),
		COMBUSTIVEL("Combustível"),
		ALUGUEL_BARCO("Aluguel de barco"),
		DIARIA("Diárias"),
		SUPRIMENTOS("Suprimentos"),
		TABULACAO("Tabulação de dados"),
		PRESTACAO_SERVICO("Prestação de serviços"),
		PASSAGEM_AEREA("Passagem aéreas"),
		ALUGUEL_LANCHA("Aluguel de lancha"),
		DIAGRAMACAO("Diagramação"),
		REMESSA_POSTAIS("Remessas postais"),
		HOSPEDAGEM("Hospedagem"),
		TRANSPORTE("Transporte"),
		MATERIAL_PROMOCIONAL("Material promocional"),
		ENCARGOS("Encargos"),
		TREINAMENTO("Treinamento"),
		MATERIAL_EXPEDIENTE("Material de expediente"),
		TEL_SATELITE("Telefone satélite"),
		FERIAS("Férias"),
		SEGUROS("Seguros"),
		INTERNET_PROVEDOR("Internet - Provedores"),
		ALIMENTACAO("Alimentação"),
		INTERNET("Internet - Banda Larga"),
		CELULAR("Telefone Celular"),
		ENERGIA("Energia"),
		ACORDO_JUD("Acordo Judicial"),
		TARIFA_DOC("Tarifa DOC/Ted"),
		NOTA_DEBITO("Nota de débito"),
		DOACAO("Doação"),
		RENDIMENTO("Rendimentos"),
		MANUTENCAO_LANCHA("Manutenção de lancha");
	
		private String nome;
		
		private CategoriadeDespesa(String nome){
			this.nome = nome;
		}
	
		public  String getNome(){
			return nome;
		}
}
