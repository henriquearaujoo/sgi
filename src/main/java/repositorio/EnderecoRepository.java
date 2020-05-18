package repositorio;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import anotacoes.Transactional;
import model.Endereco;

public class EnderecoRepository {

	
	private EntityManager manager;
	
	public EnderecoRepository(){}
	@Inject
	public EnderecoRepository(EntityManager manager){
		this.manager = manager;
	}
	
	@Transactional
	public Endereco adicionar(Endereco end) {
			return this.manager.merge(end);
	}

	
	
	
}
