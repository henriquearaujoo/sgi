package repositorio;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.DespesaViagem;
import model.ProgamacaoViagem;
import model.SolicitacaoViagem;
import model.TipoDespesa;
import util.Filtro;

public class DespesaRepositorio {

	@Inject 
	private EntityManager manager;
	
	
	public DespesaRepositorio(){}
	
	public DespesaRepositorio(EntityManager manager){
		this.manager = manager;
	}
	
	public DespesaViagem getDespesaById(Long id){
		
		return this.manager.find(DespesaViagem.class, id);
	}
	
	public List<DespesaViagem> getDespesaViagems(Filtro filtro) {
		StringBuilder jpql = new StringBuilder("from  DespesaViagem c where 1 = 1");

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

		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<DespesaViagem>();
	}
	
	public List<DespesaViagem> getDespesaByViagems(SolicitacaoViagem viagem){
		
		Query query = manager.createQuery("FROM DespesaViagem d WHERE d.solicitacaoViagem = :viagem ");
		query.setParameter("viagem", viagem);
		
		List<DespesaViagem> despesas = query.getResultList();
		
		return despesas;
	
	}
	
	public List<DespesaViagem> getDespesaByViagemsFromTipo(SolicitacaoViagem viagem, TipoDespesa tipo){
		
		Query query = manager.createQuery("FROM DespesaViagem d WHERE d.solicitacaoViagem = :viagem and d.tipoDespesa = :tipo ");
		query.setParameter("viagem", viagem);
		query.setParameter("tipo", tipo);
		
		List<DespesaViagem> despesas = query.getResultList();
		
		return despesas;
	
	}
	
	
	public void remove(DespesaViagem despesa){
			this.manager.remove(this.manager.merge(despesa));
	}
	
	public DespesaViagem salvar(DespesaViagem despesa){
		return manager.merge(despesa);
	}

	
	
}
