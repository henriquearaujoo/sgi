package repositorio;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.DespesaViagem;
import model.PagamentoLancamento;
import model.ProgamacaoViagem;
import model.SolicitacaoViagem;
import util.Filtro;

public class ProgramacaoViagemRepositorio {

	@Inject
	private EntityManager manager;

	public ProgramacaoViagemRepositorio() {

	}

	public ProgramacaoViagemRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public ProgamacaoViagem getProgViagem(Long id) {
		return manager.find(ProgamacaoViagem.class, id);
	}

	public List<ProgamacaoViagem> getProgamacaoViagems(Filtro filtro) {
		StringBuilder jpql = new StringBuilder("from  ProgamacaoViagem c where 1 = 1");

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			jpql.append(" and c.id = :codigo");
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
			// query.setParameter("codigo", "%"+filtro.getCodigo()+"%");
			query.setParameter("codigo", Long.valueOf(filtro.getCodigo()));
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

		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<ProgamacaoViagem>();
	}
	
	
	public List<ProgamacaoViagem> getProgViagemsByViagems(SolicitacaoViagem viagem){
		
		Query query = manager.createQuery("from ProgamacaoViagem p where p.solicitacaoViagem = :viagem ");
		query.setParameter("viagem", viagem);
		
		 List<ProgamacaoViagem> progViagems = query.getResultList();
		 	
		 return progViagems;
	}
	

	public boolean remover(ProgamacaoViagem pviagem) {
		this.manager.remove(this.manager.find(ProgamacaoViagem.class, pviagem.getId()));
		return true;
//		this.manager.remove(pviagem);
	}
	
	public boolean removerDespesa(DespesaViagem despesa) {
		this.manager.remove(this.manager.find(DespesaViagem.class, despesa.getId()));
		return true;
//		this.manager.remove(pviagem);
	}
	
	
	
	public ProgamacaoViagem salvar(ProgamacaoViagem pviagem){
		 return manager.merge(pviagem);
	}

}
