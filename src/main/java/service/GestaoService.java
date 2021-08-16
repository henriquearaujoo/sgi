package service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Coordenadoria;
import model.Gestao;
import model.Regional;
import model.Superintendencia;
import repositorio.GestaoRepositorio;
import util.UsuarioSessao;

public class GestaoService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	GestaoRepositorio gestaoRepositorio;
	
	@Inject
	private UsuarioSessao usuarioSessao;

	public List<Gestao> findAll() {
		return gestaoRepositorio.findAll();
	}

	@Transactional
	public Gestao salvar(Gestao gestao) {

		Gestao aux = new Gestao();

		if (gestao.getId() != null) {
			aux = gestaoRepositorio.findById(gestao.getId());
			gestao = gestaoRepositorio.salvarGestao(getInstance(gestao, aux));
		} else {
			gestao = gestaoRepositorio.salvarGestao(getInstance(gestao));
		}

		return gestao;
	}
	
	@Transactional
	public void deleteGestaoById(Gestao gestao) {
		gestaoRepositorio.deleteGestao(gestao);
	}

	public Gestao getInstance(Gestao gestao, Gestao aux) {

		aux.setNome(gestao.getNome());
		aux.setSigla(gestao.getSigla());
		aux.setUsuarioAlteracao(usuarioSessao.getUsuario().getNomeUsuario());
		aux.setDataAlteracao(new Date());
		aux.setAprovadorSubistituto(gestao.getAprovadorSubistituto());
		aux.setColaborador(gestao.getColaborador());
		aux.setAusente(gestao.getAusente());

		return aux;
	}

	// mudar nome da variável
	public Gestao getInstance(Gestao gestao) {

		if (gestao.getType().equals("coord")) {

			// Coordenadoria coord = new
			// Coordenadoria(gestao.getId(),gestao.getNome(),gestao.getType());

			Coordenadoria coord = new Coordenadoria();
			coord.setId(gestao.getId() != null ? gestao.getId() : null);
			coord.setNome(gestao.getNome());
			coord.setSigla(gestao.getSigla());
			coord.setAprovadorSubistituto(gestao.getAprovadorSubistituto());
			coord.setColaborador(gestao.getColaborador());
			coord.setAusente(gestao.getAusente());
			return coord;

		} else if (gestao.getType().equals("reg")) {

			Regional reg = new Regional();
			reg.setId(gestao.getId() != null ? gestao.getId() : null);
			reg.setNome(gestao.getNome());
			reg.setSigla(gestao.getSigla());
			reg.setAprovadorSubistituto(gestao.getAprovadorSubistituto());
			reg.setColaborador(gestao.getColaborador());
			reg.setAusente(gestao.getAusente());
			return reg;

		} else if (gestao.getType().equals("sup")) {

			Superintendencia sup = new Superintendencia();
			sup.setId(gestao.getId() != null ? gestao.getId() : null);
			sup.setNome(gestao.getNome());
			sup.setSigla(gestao.getSigla());
			sup.setAprovadorSubistituto(gestao.getAprovadorSubistituto());
			sup.setColaborador(gestao.getColaborador());
			sup.setAusente(gestao.getAusente());
			return sup;

		}
		return gestao;
	}

	public Gestao findById(Long id) {
		return gestaoRepositorio.findById(id);
	}
	
	public Gestao findByColaborador(Long id) {
		return gestaoRepositorio.findByColaborador(id);
	}
	
	public Boolean verificaGestao(Long id) {
		return gestaoRepositorio.verificaGestao(id);
	}
	
	public List<Regional> getRegionais(){
		return gestaoRepositorio.getRegionais();
	}
	

}
