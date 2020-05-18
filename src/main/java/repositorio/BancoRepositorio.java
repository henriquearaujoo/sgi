package repositorio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Banco;
import model.ContaBancaria;
import util.Filtro;

public class BancoRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public BancoRepositorio() {
	}

	public Banco salvarBanco(Banco banco) {
		return this.manager.merge(banco);
	}

	public void removerBanco(Banco banco) {
		this.manager.remove(this.manager.find(Banco.class, banco.getId()));
	}

	public Banco findBancoById(Long id) {
		return this.manager.find(Banco.class, id);
	}

	public List<Banco> getTodosBancos(Filtro filtro) {
		StringBuilder jpql = new StringBuilder("from Banco as b where 1 = 1 ");

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			jpql.append("and lower(b.nomeBanco) like lower(:nome_banco) ");
		}

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			jpql.append("and lower(b.numeroBanco) like lower(:numero_banco) ");
		}

		
		jpql.append(" order by b.nomeBanco ");
		
		Query query = this.manager.createQuery(jpql.toString());

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			query.setParameter("nome_banco", "%" + filtro.getNome() + "%");
		}
		
		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			query.setParameter("numero_banco", "%" + filtro.getCodigo() + "%");
		}
		
		return query.getResultList();
	}

	public List<Banco> getBancoAutoComplete(String s) {
		StringBuilder jpql = new StringBuilder(
				"from  Banco b where ( lower(b.nomeBanco) like lower(:nome) or lower(b.numeroBanco) like lower(:nome)) ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		query.setMaxResults(30);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<ContaBancaria>();
	}

}
