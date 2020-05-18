package service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Requisito;
import model.Versao;
import repositorio.RequisitoRepositorio;
import util.UsuarioSessao;

public class RequisitoService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private RequisitoRepositorio  requisitoRepositorio;
	
	@Inject
	private UsuarioSessao usuarioSessao;
	
	

	@Transactional
	public Boolean salvar(Requisito requisito){

		requisito.setUsuario(usuarioSessao.getUsuario().getNomeUsuario());
		requisito.setDataAlteracao(new Date());
		requisitoRepositorio.salvar(requisito);
		return true;
	}
	
	@Transactional
	public Boolean remove(Requisito requisito) {
		try{
			requisitoRepositorio.remove(requisito);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public List<Requisito> findAll(){
		return requisitoRepositorio.findAll();
	}


	public List<Requisito> findRequisito(Long id) {
	return requisitoRepositorio.findRequisito(id);
	}
	
}
