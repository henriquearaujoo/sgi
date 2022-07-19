package repositorio;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.*;
import util.Filtro;

public class DiariaRepositorio {

	@Inject
	private EntityManager manager;

	public DiariaRepositorio() {

	}

	public DiariaRepositorio(EntityManager manager) {
		this.manager = manager;

	}

	public Diaria diariaById(Long id) {
		return manager.find(Diaria.class, id);
	}

	public Diaria salvar(Diaria diaria) {
		return manager.merge(diaria);
	}

	public Boolean remover(Diaria diaria) {
		try {
			this.manager.remove(this.manager.merge(diaria));
			return true;
		}catch (Exception e) {
			return false;
		}
		
	}

	public List<SolicitacaoPagamento> getDiarias(Filtro filtro) {
		StringBuilder jpql = new StringBuilder("from  Diaria c where 1 = 1");

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			jpql.append(" and lower(c.codigo) like lower(:codigo)");

		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			jpql.append(" and lower(c.solicitante.nome) like lower(:nome)");

		}

		if (filtro.getDataInicio() != null) {
			if (filtro.getDataFinal() != null) {
				jpql.append(" and c.dataEmissao between :data_inicio and :data_final");
			} else {
				jpql.append(" and c.dataEmissao > :data_inicio");
			}
		}

		if (filtro.getGestaoID() != null) {
			jpql.append(" and c.gestao.id = :gestaoID ");
		}

		if (filtro.getLocalidadeID() != null) {
			jpql.append(" and c.localidade.id = :localidadeID ");
		}

		jpql.append(" order by c.dataEmissao");

		Query query = manager.createQuery(jpql.toString());

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			/* jpql.append(" and lower(c.codigo) like lower(:codigo)"); */
			query.setParameter("codigo", "%" + filtro.getCodigo() + "%");
		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			/*
			 * jpql.append(" and lower(c.solicitante.nome) like lower(:nome)");
			 */
			query.setParameter("nome", "%" + filtro.getNome() + "%");
		}

		if (filtro.getDataInicio() != null) {
			if (filtro.getDataFinal() != null) {
				/*
				 * jpql.append(
				 * " and c.dataEmissao between :data_inicio and :data_final");
				 */
				query.setParameter("data_inicio", filtro.getDataInicio());
				query.setParameter("data_final", filtro.getDataFinal());
			} else {
				/* jpql.append(" and c.dataEmissao > :data_inicio"); */
				query.setParameter("data_inicio", filtro.getDataInicio());
			}
		}

		if (filtro.getGestaoID() != null) {
			/* jpql.append(" and c.gestao.id = :gestaoID "); */
			query.setParameter("gestaoID", filtro.getGestaoID());
		}

		if (filtro.getLocalidadeID() != null) {
			/* jpql.append(" and c.localidade.id = :localidadeID "); */
			query.setParameter("localidadeID", filtro.getLocalidadeID());
		}

		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Diaria>();
	}

	
	public List<Diaria> getDiariaByViagems(SolicitacaoViagem viagem) {
		String jpql = "from Diaria where solicitacaoViagem.id = :id";
		
		Query query = manager.createQuery(jpql);
		query.setParameter("id", viagem.getId());
		List<Diaria> diarias = query.getResultList();
		return diarias;

	}
	
	public List<Diaria> getDiariaByViagems(Long viagem) {
		String jpql = "from Diaria where solicitacaoViagem.id = :id";
		Query query = manager.createQuery(jpql);
		query.setParameter("id", viagem);
		List<Diaria> diarias = query.getResultList();
		return diarias;

	}
	
	/*--
	public List<Trecho> setTrechos(Diaria diaria){
		
		Query query = manager.createQuery("FROM Trecho t WHERE t.diaria = :diaria");
		query.setParameter("diaria", diaria);
		List<Trecho> trechos = query.getResultList();
		
		return trechos;
		
	}	--*/
	
	
	public List<Trecho> setTrechos(Long id){
		Query q = this.manager.createQuery("FROM Trecho t WHERE t.diaria.id = :id");
		q.setParameter("id", id);
		return q.getResultList();
	} 
	
	

}
