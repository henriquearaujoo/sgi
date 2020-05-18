package model;

import java.util.List;

import repositorio.GestaoRepositorio;

public enum TipoGestao {

	SUP("Superintendencia") {
		@Override
		public List<Gestao> getGestao(GestaoRepositorio repo) {
			return repo.getSuperintendencia();
		}
	},
	COORD("Coordenadoria") {
		@Override
		public List<Gestao> getGestao(GestaoRepositorio repo) {
			return repo.getCoordenadoria();
		}
	},
	REGIONAL("Regional") {
		@Override
		public List<Gestao> getGestao(GestaoRepositorio repo) {
			return repo.getRegional();
		}	},
	GERENCIA("Gerencia") {
		@Override
		public List<Gestao> getGestao(GestaoRepositorio repo) {
			return repo.getGerencia();
		}
	};

	private String nome;

	private TipoGestao(String nome) {
		this.nome = nome;
	}

	public abstract List<Gestao> getGestao(GestaoRepositorio repo);

	public String getNome() {
		return nome;
	}

}
