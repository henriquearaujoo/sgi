package model;

import java.util.List;

import repositorio.LocalRepositorio;
import util.CDILocator;

public enum TipoLocalidade {
	
	UC("Uc"){
		@Override
		public List<Localidade> getLocalidade(LocalRepositorio repo) {
			return repo.getUcs();
		}
	},
	COMUNIDADE("Comunidade"){
		@Override
		public List<Localidade> getLocalidade(LocalRepositorio repo) {
			return repo.getComunidades();
		}
	},
	MUNICIPIO("Municipio"){
		@Override
		public List<Localidade> getLocalidade(LocalRepositorio repo) {
			return repo.getMunicipio();
		}
	},
	SEDE("Sede"){
		@Override
		public List<Localidade> getLocalidade(LocalRepositorio repo) {
			return repo.getSede();
		}
	},NUCLEO("Nucleo"){
		@Override
		public List<Localidade> getLocalidade(LocalRepositorio repo){
			return repo.getNucleo();
		}
	};
	
	private String nome;
	
	private TipoLocalidade(String nome){
		this.nome = nome;
	}
	
	private LocalRepositorio localRepositorio = CDILocator.getBean(LocalRepositorio.class);
	
	
	
	public abstract List<Localidade> getLocalidade(LocalRepositorio repo);

	public String getNome() {
		return nome;
	}
	
}
