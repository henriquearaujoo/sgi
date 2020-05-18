package service;

import java.io.Serializable;

import javax.inject.Inject;

import exception.NegocioException;
import model.Endereco;
import repositorio.EnderecoRepository;

public class EnderecoService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private EnderecoRepository repository;
	
	public EnderecoService(){}
	
	public EnderecoService(EnderecoRepository repository){
	 this.repository = repository;
	}
	
	public Endereco salvar(Endereco end) throws NegocioException{
		if (end != null) {
			return this.repository.adicionar(end);
		}else{
			throw new NegocioException("Erro ao cadastrar dados");
		}
	}
	

}
