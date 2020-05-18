package service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Versao;
import repositorio.VersaoRepositorio;
import util.UsuarioSessao;



public class VersaoService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Inject 
	private VersaoRepositorio versaoRepositorio;

	@Inject
	private UsuarioSessao usuarioSessao;

	@Transactional
	public Versao salvar(Versao versao){

		versao.setUsuario(usuarioSessao.getUsuario().getNomeUsuario());
		versao.setDataAlteracao(new Date());
		return versaoRepositorio.salvar(versao);
		 
	}

	@Transactional
	public Boolean remove(Versao versao) {
		try{
			versaoRepositorio.remove(versao);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public List<Versao> findAll(){
		return versaoRepositorio.findAll();
	}

	public Versao findByid(Long id) {
		return versaoRepositorio.findById(id);
	}


}
