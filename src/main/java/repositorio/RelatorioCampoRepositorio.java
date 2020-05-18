package repositorio;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.RelatorioCampo;
import model.SolicitacaoViagem;
import model.Veiculo;

public class RelatorioCampoRepositorio {

	@Inject
	EntityManager manager;

	public RelatorioCampoRepositorio() {

	}

	public RelatorioCampoRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public RelatorioCampo relatorioPorId(Long id) {

		return this.manager.find(RelatorioCampo.class, id);
	}

	public List<RelatorioCampo> getRelatorios() {
		StringBuilder jpql = new StringBuilder("from RelatorioCampo");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<RelatorioCampo>();
	}

	public void remover(RelatorioCampo relatorio) {

		this.manager.remove(relatorio);
	}

	public void salvar(RelatorioCampo relatorio) {
		this.manager.merge(relatorio);
	}
	
	
	
	
	
}
