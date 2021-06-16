package repositorio;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import model.DespesaViagem;
import model.Diaria;
import model.ItemPedido;
import model.LogStatus;
import model.LogViagem;
import model.ProgamacaoViagem;
import model.SolicitacaoViagem;
import model.StatusCompra;
import util.Filtro;

public class ViagemRepositorio {

	@Inject
	private EntityManager manager;


	public ViagemRepositorio(){

	}


	public ViagemRepositorio(EntityManager manager){
		this.manager = manager;
	}


	public void salvarDiaria(Diaria diaria){
		this.manager.merge(diaria);
	}

	public List<Diaria> findDiariasbyViagem(SolicitacaoViagem viagem){
		String jpql = "from Diaria d where d.solicitacaoViagem.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id",viagem.getId());

		return query.getResultList();
	}

	public List<Diaria> findDiariasbyViagem(Long viagem){
		String jpql = "from Diaria d where d.solicitacaoViagem.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id",viagem);
		return query.getResultList();
	}

	public SolicitacaoViagem getViagemPorId(Long id){
		return this.manager.find(SolicitacaoViagem.class, id);
	}


	public List<SolicitacaoViagem> getViagems(Filtro filtro){

		//		public SolicitacaoViagem(Long id, String missao, String descricao, String solicitante,  Date data){
		//			this.id = id;
		//			this.missao = missao;
		//			this.motivo = descricao;
		//			this.solicitante = new Colaborador(solicitante);
		//			this.dataEmissao = data;
		//		}
		//		

		StringBuilder jpql = new StringBuilder(" SELECT NEW SolicitacaoViagem(c.id,c.missao,c.motivo,c.solicitante.nome, c.dataEmissao,c.statusViagem) FROM SolicitacaoViagem c where 1 = 1");

		jpql.append("and c.versionLancamento = 'MODE01' ");

		if(filtro.getCodigo() != null && !filtro.getCodigo().equals("")){
			jpql.append(" and c.id = :codigo");	
		}

		if(filtro.getNome() != null && !filtro.getNome().equals("")){
			jpql.append(" and lower(c.solicitante.nome) like lower(:nome)");

		}

		if(filtro.getDataInicio() != null){
			if(filtro.getDataFinal() != null){
				jpql.append(" and c.dataEmissao between :data_inicio and :data_final");	
			}else{
				jpql.append(" and c.dataEmissao > :data_inicio");	
			}
		}

		if(filtro.getGestaoID() != null){
			jpql.append(" and c.gestao.id = :gestaoID ");
		}

		if(filtro.getLocalidadeID() != null){
			jpql.append(" and c.localidade.id = :localidadeID ");
		}
		
		
		//--------------------------------------------
		if(filtro.getCodigoDaria() != null && !filtro.getCodigoDaria().equals("")){
			jpql.append(" and :pDiaria in (select d.id from Diaria d where d.solicitacaoViagem = c)");
		}
		
		if(filtro.getDescricao() != null && !filtro.getDescricao().equals("")){
			jpql.append(" and lower(c.motivo)  like lower(:pDescricao) ");
		}
		
		if(filtro.getDestino() != null ){
			jpql.append(" and c.localidade.id = (select l.id from Localidade l where l.nome like :pDestino )");
		}
		
		if(filtro.getMissao() != null && !filtro.getMissao().equals("")){
			jpql.append(" and lower(c.missao)  like lower(:pMissao) ");
		}
		
		//---------------------------------------------------------
		jpql.append(" order by c.dataEmissao desc");

		Query query = manager.createQuery(jpql.toString());

		if(filtro.getCodigo() != null && !filtro.getCodigo().equals("")){
			/*jpql.append(" and lower(c.codigo) like lower(:codigo)");*/
			//query.setParameter("codigo", "%"+filtro.getCodigo()+"%");
			query.setParameter("codigo", Long.valueOf(filtro.getCodigo()));
		}

		if(filtro.getNome() != null && !filtro.getNome().equals("")){
			/*jpql.append(" and lower(c.solicitante.nome) like lower(:nome)");*/
			query.setParameter("nome", "%"+filtro.getNome()+"%");
		}

		if(filtro.getDataInicio() != null){
			if(filtro.getDataFinal() != null){
				/*jpql.append(" and c.dataEmissao between :data_inicio and :data_final");*/	
				query.setParameter("data_inicio", filtro.getDataInicio());
				query.setParameter("data_final", filtro.getDataFinal());
			}else{
				/*jpql.append(" and c.dataEmissao > :data_inicio");*/	
				query.setParameter("data_inicio", filtro.getDataInicio());
			}
		}

		if(filtro.getGestaoID() != null){
			/*jpql.append(" and c.gestao.id = :gestaoID ");*/
			query.setParameter("gestaoID", filtro.getGestaoID());
		}

		if(filtro.getLocalidadeID() != null){
			/*jpql.append(" and c.localidade.id = :localidadeID ");*/
			query.setParameter("localidadeID", filtro.getLocalidadeID());
		}
		
		if(filtro.getCodigoDaria() != null && !filtro.getCodigoDaria().equals("")){
			query.setParameter("pDiaria", Long.parseLong(filtro.getCodigoDaria()));
		}
		if(filtro.getDescricao() != null && !filtro.getDescricao().equals("")){
			query.setParameter("pDescricao", "%"+filtro.getDescricao()+"%");
		}
		if(filtro.getDestino() != null ){
			query.setParameter("pDestino", filtro.getDestino().getNome());
		}
		if(filtro.getMissao() != null && !filtro.getMissao().equals("")){
			query.setParameter("pMissao", filtro.getMissao());
		}
		
		
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<SolicitacaoViagem>();
	}

	public void remover(SolicitacaoViagem viagem){

		this.manager.remove(this.manager.merge(viagem));
	}



	public void salvar(SolicitacaoViagem viagem){
		this.manager.merge(viagem);
	}

	public SolicitacaoViagem salvar(SolicitacaoViagem viagem, String args){
		return this.manager.merge(viagem);
	}

	public List<ProgamacaoViagem> setProgamacaoViagem(Long id){
		Query  q = this.manager.createQuery("FROM ProgamacaoViagem p WHERE p.solicitacaoViagem.id = :id ");
		q.setParameter("id", id);
		return q.getResultList();

	}

	public List<DespesaViagem> setDespesaViagem(Long id){
		Query q = this.manager.createQuery("FROM DespesaViagem d WHERE d.solicitacaoViagem.id = :id");
		q.setParameter("id", id);
		return q.getResultList();
	}

	public List<Diaria> setDiarias(Long id){
		Query q = this.manager.createQuery("FROM Diaria d WHERE d.solicitacaoViagem.id = :id");
		q.setParameter("id", id);
		return q.getResultList();
	} 


	public Boolean mudarStatus(Long id, StatusCompra statusViagem){

		try{
			String jpql = "update SolicitacaoViagem c set c.statusViagem = :statusViagem where c.id = :id";
			Query query =  manager.createQuery(jpql);
			query.setParameter("statusViagem", statusViagem);
			query.setParameter("id", id);
			query.executeUpdate();
		}catch(Exception e){
			return false;
		}

		return true;
	}

	public void salvarLog(LogViagem log){
		this.manager.merge(log);
	}

	public void salvarLog(LogStatus log){
		this.manager.merge(log);
	}

	public void deleteLogViagem(LogViagem log) {
		manager.remove(this.manager.merge(log));
	}


	public List<LogViagem> findLogByViagem(Long id) {
		String jpql="Select l From LogViagem l where viagem.id = :pId";		
		Query query = manager.createQuery(jpql);
		query.setParameter("pId", id);
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<LogViagem>();
	}

	public void deleteLogByViagem(Long id) {
		String jpql="delete From LogViagem l where l.id = :pId";		
		Query query = manager.createQuery(jpql);
		query.setParameter("pId", id);
	}


	public void deleteViagem(Long id) {
		String jpql="delete From SolicitacaoViagem l where l.id = :pId";		
		Query query = manager.createQuery(jpql);
		query.setParameter("pId", id);
	}
	
	
	public String verificaConflitoPeriodo(Long idColaboradorConta, String dataInicio, String dataFim, Long idDiaria) {
		
		StringBuilder jpql = new StringBuilder("select solicitacaoviagem_id ||'/'|| id, data_vencimento from lancamento  where tipo = 'Diaria' and ");
		jpql.append("contarecebedor_id = :id_colaborador_conta and ");
		//jpql.append("((to_date(to_char(data_vencimento, 'YYYY-MM-DD'), 'YYYY-MM-DD'), (diasnocampo || ' days')::interval)  ");
		
		jpql.append("((to_date(to_char(data_ida_v2, 'YYYY-MM-DD'), 'YYYY-MM-DD'), to_date(to_char(data_volta_v2, 'YYYY-MM-DD'), 'YYYY-MM-DD')) ");
		jpql.append("OVERLAPS ");
		jpql.append("(date '"+dataInicio+"', date '"+dataFim+"' )) is true ");
		
		if (idDiaria != null && idDiaria != 0l) {
			jpql.append(" and id != :id_diaria");
		}
		
		jpql.append(" LIMIT 1 ");
		
		
		Query query = this.manager.createNativeQuery(jpql.toString());
		query.setParameter("id_colaborador_conta", idColaboradorConta);
		
		if (idDiaria != null && idDiaria != 0l) {
			query.setParameter("id_diaria", idDiaria);
		}
		
				
		List<Object[]> result = query.getResultList();
		String objeto = "";
		
		
		for (Object[] objects : result) {
			objeto =  objects[0] != null ? objects[0].toString() : "";
		}

		
		return objeto;
	}
}
