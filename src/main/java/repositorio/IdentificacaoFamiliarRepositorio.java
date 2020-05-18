package repositorio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import anotacoes.Transactional;
import model.CategoriaDespesaClass;
import model.IdentificacaoFamiliar;
import model.UnidadeDeCompra;

public class IdentificacaoFamiliarRepositorio implements Serializable{
	
	@Inject	
	private EntityManager manager;

	public IdentificacaoFamiliarRepositorio() {
	}


	public IdentificacaoFamiliarRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	
	@Transactional
	public void salvar(IdentificacaoFamiliar identificacao) {
		this.manager.merge(identificacao);
	}
	
	public IdentificacaoFamiliar findById(Long id){
		return this.manager.find(IdentificacaoFamiliar.class, id);
	}
	
	
	@Transactional
	public void remover(IdentificacaoFamiliar identificacao){
		this.manager.remove(this.manager.merge(identificacao));
	}
}
