package model;

public enum CategoriaCadeia {

		PRODUTO_FLORESTAL("Produtos florestais"),
		PRODUTO_PESQUEIRO("Produtos pesqueiros"),
		PRODUTO_AGROPECUARIO("Produtos agropecuários"),
		COMERCIO_SERVICO("Comércios e serviços");
	
		private String nome;
		
		private CategoriaCadeia(String nome){
			this.nome = nome;
		}
	
		public  String getNome(){
			return nome;
		}
}
