package model;

public enum TipoDeCategoria {

		DESPESA_VIAGEM("Categorias de Despesa de viagem"),
		DESPESA_PRODUTO("Categorias de Despesa de produto"),
		FINANCEIRO("Categorias financeiras"),
		ACOES("Categoria de ações");
	
		private String nome;
		
		private TipoDeCategoria(String nome){
			this.nome = nome;
		}
	
		public  String getNome(){
			return nome;
		}
}
