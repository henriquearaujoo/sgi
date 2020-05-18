package repositorio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.AtividadeProjeto;
import model.AtividadeProjeto;
import model.Projeto;
import model.RelatorioAtividade;
import model.StatusAtividade;
import model.TipoAdministrativoProjeto;
import model.UserProjeto;
import util.Util;
import model.ProjetoRubrica;

public class AtividadeRepositorio {

	@Inject
	private EntityManager manager;

	public AtividadeRepositorio() {
	}

	public AtividadeRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public void salvar(AtividadeProjeto atividade) {
		if (atividade.getId() != null) {
			this.manager.merge(atividade);
		} else {
			this.manager.persist(atividade);
		}
	}

	public AtividadeProjeto getAtividadeById(Long id) {
		return manager.find(AtividadeProjeto.class, id);
	}

	public List<AtividadeProjeto> getAtividade(Projeto projeto) {
		StringBuilder jpql = new StringBuilder(
				"select a from  AtividadeProjeto a where a.projeto = :projeto order by a.dataConclusao desc, a.dataFim desc");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("projeto", projeto);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Projeto>();
	}

	public List<RelatorioAtividade> getAtividadesParaRelatorio(Long id) {
		StringBuilder jpql = new StringBuilder("select a.nome, a.responsavel, ");
		jpql.append("(select loc.mascara from localidade loc where loc.id = a.localizador_id) as localizador, ");
		
		jpql.append("a.valor as orcado, a.valor as executado, ");
		
		jpql.append("(jan+fev+mar+abr+mai+jun+jul+ago+set+out+nov+dez) as qtd_atividade,");
		jpql.append(
				"(janexec+fevexec+marexec+abrexec+maiexec+junexec+julexec+agoexec+setexec+outexec+novexec+dezexec) as qtd_atividade_executada, ");
		
		//ENTRADAS PROVENIENTES A ATIVIDADE
		jpql.append("(select sum(pl.valor) ");
		jpql.append("from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id  ");
		jpql.append("	inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");
		jpql.append("	where l.tipo != 'compra' ");
		jpql.append("	and l.statuscompra in ('CONCLUIDO','N_INCIADO') "); 
		jpql.append("	and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) ");
		//jpql.append("	and la.projetorubrica_id = pr.id  "); 
		jpql.append("and la.atividade_id = a.id ");
		
		jpql.append("	and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb') "); 
		jpql.append("	and l.tipolancamento != 'reemb_conta' "); 
		jpql.append("	and l.tipo != 'baixa_aplicacao' "); 
		jpql.append("	and l.tipo != 'doacao_efetiva' "); 
		jpql.append("	and l.tipo != 'custo_pessoal'   "); 
		jpql.append("	and l.tipo != 'aplicacao_recurso' ");
		//jpql.append("	and case p.tarifado when true then ( 1= 1) else (l.tipo != 'tarifa_bancaria') end	 ");
		jpql.append("	and case (select p.tarifado from projeto p where p.id = a.projeto_id) when true then ( 1 = 1 ) else (l.tipo != 'tarifa_bancaria') end	 ");
		jpql.append("	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))");
		jpql.append("	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB'))");
		jpql.append("	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))");
		jpql.append("	and (((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF' or (select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA') "); 
		jpql.append("	and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB')) as receita,") ;
		
		
		//SAÍDAS PROVENIENTES A ATIVIDADE
		jpql.append("(select sum(pl.valor)");
		jpql.append("	from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id ");
		jpql.append("	inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id "); 
		jpql.append("	where l.tipo != 'compra'  \r\n"); 
		jpql.append("	and l.statuscompra in ('CONCLUIDO','N_INCIADO') \r\n"); 
		jpql.append("	and (l.versionlancamento = 'MODE01' or pl.reclassificado is true)\r\n"); 
		//jpql.append("	and la.projetorubrica_id = pr.id  \r\n");
		jpql.append("and la.atividade_id = a.id ");
		jpql.append("	and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb')\r\n"); 
		jpql.append("	and l.tipolancamento != 'reemb_conta' \r\n"); 
		jpql.append("	and l.tipo != 'baixa_aplicacao'\r\n"); 
		jpql.append("	and l.tipo != 'doacao_efetiva' \r\n");
		jpql.append("	and l.tipo != 'custo_pessoal' \r\n");
		jpql.append("	and l.tipo != 'aplicacao_recurso'\r\n");
		//jpql.append("	and case  p.tarifado  when true then ( 1 = 1 ) else (l.tipo != 'tarifa_bancaria') end	 ");
		jpql.append("	and case (select p.tarifado from projeto p where p.id = a.projeto_id) when true then ( 1 = 1 ) else (l.tipo != 'tarifa_bancaria') end	 ");
		jpql.append("	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))\r\n" ); 
		jpql.append("	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB'))\r\n" ); 
		jpql.append("	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))\r\n" ); 
		jpql.append("	and (((select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF' or (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CA')\r\n" ); 
		jpql.append("	and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB')) as despesa ");
		
		
		jpql.append("from atividade_projeto a where a.projeto_id = :projeto ");

		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("projeto", id);

		List<Object[]> result = query.getResultList();
		List<RelatorioAtividade> atividades = new ArrayList<>();
		for (Object[] object : result) {
			atividades.add(new RelatorioAtividade(object));
		}

		return atividades;
	}

	public List<AtividadeProjeto> getCronogramaPlanejado(Projeto projeto) {
		// StringBuilder jpql = new StringBuilder("select a from AtividadeProjeto a
		// where a.projeto = :projeto order by a.dataConclusao desc, a.dataFim desc");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select ap.nome,");
		jpql.append("ap.jan,");
		jpql.append("ap.fev,");
		jpql.append("ap.mar,");
		jpql.append("ap.abr,");
		jpql.append("ap.mai,");
		jpql.append("ap.jun,");
		jpql.append("ap.jul,");
		jpql.append("ap.ago,");
		jpql.append("ap.set,");
		jpql.append("ap.out,");
		jpql.append("ap.nov,");
		jpql.append("ap.dez,");
		jpql.append("ap.responsavel,ap.quantidade,ap.valor, ap.id, ");
		jpql.append("(select loc.mascara from localidade loc where loc.id = ap.localizador_id) as localizador ");

		jpql.append(
				" from atividade_projeto ap where ap.projeto_id = :projeto and ap.planejado is true order by ap.data_inicio, ap.data_fim, ap.id ");

		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("projeto", projeto.getId());

		List<Object[]> result = query.getResultList();
		List<AtividadeProjeto> atividades = new ArrayList<>();
		AtividadeProjeto ativ = new AtividadeProjeto();

		for (Object[] object : result) {
			atividades.add(new AtividadeProjeto(object));
		}

		return atividades;
	}

	public BigDecimal getTotalAtividadeByProjeto(Long id, Long idAtividade) {
		StringBuilder jpql = new StringBuilder("SELECT sum(a.valor) FROM AtividadeProjeto a where a.projeto.id = :id and a.planejado = :plan") ;
		
		if (idAtividade != null) {
			jpql.append(" and a.id != :id_atividade");
		}
	
		Query q = this.manager.createQuery(jpql.toString());
		q.setParameter("id", id);
		q.setParameter("plan", true);
		
		if (idAtividade != null) {
			q.setParameter("id_atividade", idAtividade);
		}
		
		BigDecimal vl = (BigDecimal) q.getSingleResult();
		
		return vl != null ? vl : BigDecimal.ZERO;
	}

	public BigDecimal getTotalAtividadeExecutadaByProjeto(Long id) {
		StringBuilder jpql = new StringBuilder(
				" SELECT sum(a.valor) FROM AtividadeProjeto a where a.projeto.id = :id and a.planejado = :plan ");
		jpql.append(
				" and ((janExec + fevExec + marExec + abrExec + maiExec + junExec + julExec + agoExec + setExec + outExec + novExec + dezExec) > 0) ");
		Query q = this.manager.createQuery(jpql.toString());
		q.setParameter("id", id);
		q.setParameter("plan", true);
		BigDecimal vl = (BigDecimal) q.getSingleResult();
		return vl;
	}

	public BigDecimal getTotalAtividadeByProjetoNaoPlanejada(Long id) {
		String jpql = "SELECT sum(a.valor) FROM AtividadeProjeto a where a.projeto.id = :id and a.planejado = :plan";
		Query q = this.manager.createQuery(jpql);
		q.setParameter("id", id);
		q.setParameter("plan", false);
		BigDecimal vl = (BigDecimal) q.getSingleResult();
		return vl;
	}

	public List<AtividadeProjeto> getCronogramaPlanejadoNOVO(Projeto projeto) {
		// StringBuilder jpql = new StringBuilder("select a from AtividadeProjeto a
		// where a.projeto = :projeto order by a.dataConclusao desc, a.dataFim desc");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select ap.nome,");
		jpql.append("ap.jan,");
		jpql.append("ap.fev,");
		jpql.append("ap.mar,");
		jpql.append("ap.abr,");
		jpql.append("ap.mai,");
		jpql.append("ap.jun,");
		jpql.append("ap.jul,");
		jpql.append("ap.ago,");
		jpql.append("ap.set,");
		jpql.append("ap.out,");
		jpql.append("ap.nov,");
		jpql.append("ap.dez,");

		jpql.append("ap.responsavel,ap.quantidade,ap.valor, ap.id, ");
		jpql.append("(select loc.mascara from localidade loc where loc.id = ap.localizador_id) as localizador ");

		jpql.append(
				" from atividade_projeto ap where ap.projeto_id = :projeto order by ap.data_inicio, ap.data_fim, ap.id ");

		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("projeto", projeto.getId());

		List<Object[]> result = query.getResultList();
		List<AtividadeProjeto> atividades = new ArrayList<>();
		AtividadeProjeto ativ = new AtividadeProjeto();

		for (Object[] object : result) {
			atividades.add(new AtividadeProjeto(object));
		}

		return atividades;
	}

	public List<AtividadeProjeto> getCronogramaExecutadoNaoPlanejado(Projeto projeto) {
		// StringBuilder jpql = new StringBuilder("select a from AtividadeProjeto a
		// where a.projeto = :projeto order by a.dataConclusao desc, a.dataFim desc");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select ap.nome,");
		jpql.append("ap.jan,");
		jpql.append("ap.fev,");
		jpql.append("ap.mar,");
		jpql.append("ap.abr,");
		jpql.append("ap.mai,");
		jpql.append("ap.jun,");
		jpql.append("ap.jul,");
		jpql.append("ap.ago,");
		jpql.append("ap.set,");
		jpql.append("ap.out,");
		jpql.append("ap.nov,");
		jpql.append("ap.dez,");
		jpql.append("ap.responsavel,ap.quantidade,ap.valor, ap.id, ");
		jpql.append("(select loc.mascara from localidade loc where loc.id = ap.localizador_id) as localizador ");

		jpql.append(
				" from atividade_projeto ap where ap.projeto_id = :projeto and (ap.planejado is null or ap.planejado is false)   order by ap.data_inicio, ap.data_fim, ap.id ");

		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("projeto", projeto.getId());

		List<Object[]> result = query.getResultList();
		List<AtividadeProjeto> atividades = new ArrayList<>();
		AtividadeProjeto ativ = new AtividadeProjeto();

		for (Object[] object : result) {
			atividades.add(new AtividadeProjeto(object));
		}

		return atividades;
	}

	public List<AtividadeProjeto> getCronogramaExecutado(Projeto projeto) {
		// StringBuilder jpql = new StringBuilder("select a from AtividadeProjeto a
		// where a.projeto = :projeto order by a.dataConclusao desc, a.dataFim desc");

		StringBuilder jpql = new StringBuilder();
		jpql.append("select ap.nome,");
		jpql.append("ap.janexec,");
		jpql.append("ap.fevexec,");
		jpql.append("ap.marexec,");
		jpql.append("ap.abrexec,");
		jpql.append("ap.maiexec,");
		jpql.append("ap.junexec,");
		jpql.append("ap.julexec,");
		jpql.append("ap.agoexec,");
		jpql.append("ap.setexec,");
		jpql.append("ap.outexec,");
		jpql.append("ap.novexec,");
		jpql.append("ap.dezexec,");
		jpql.append("ap.responsavel,ap.quantidade,ap.valor, ap.id, ");
		jpql.append("(select loc.mascara from localidade loc where loc.id = ap.localizador_id) as localizador ");

		jpql.append(
				" from atividade_projeto ap where ap.projeto_id = :projeto and ap.planejado is true  order by ap.data_inicio, ap.data_fim, ap.id ");

		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("projeto", projeto.getId());

		List<Object[]> result = query.getResultList();
		List<AtividadeProjeto> atividades = new ArrayList<>();
		AtividadeProjeto ativ = new AtividadeProjeto();

		for (Object[] object : result) {
			atividades.add(new AtividadeProjeto(object, ""));
		}

		return atividades;
	}

	public List<AtividadeProjeto> getAtividadeByAcao(Long id) {
		StringBuilder jpql = new StringBuilder("from  AtividadeProjeto a where a.acao.id = :id and a.aceito = true");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Projeto>();

	}

	public void remover(AtividadeProjeto atividade) {
		this.manager.remove(this.manager.merge(atividade));
	}
	
	//Alteração para contemplar verificação de valor da atividade nas solicitações ----------------------------------------

		public List<AtividadeProjeto> getAtividadeWithSaldoByProjeto(Long idProjeto) {
			StringBuilder jpql = new StringBuilder("select ap.id as id_00 ,ap.nome as nome_01 ,ap.valor as valor_02 , ");
			jpql.append(" (select sum(pl.valor) from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id where l.tipo != 'compra' 	and l.statuscompra in ('CONCLUIDO','N_INCIADO') and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) and la.atividade_id = ap.id  and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb') ");
			jpql.append(" and l.tipolancamento != 'reemb_conta' and l.tipo != 'baixa_aplicacao' and l.tipo != 'doacao_efetiva' and l.tipo != 'custo_pessoal' and l.tipo != 'aplicacao_recurso' and case p.tarifado when true then ( 1= 1) else (l.tipo != 'tarifa_bancaria') end	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB')) and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF')) and (((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF' or (select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA') and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB')) as receita_03,"); 

			jpql.append(" (select sum(pl.valor) from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id 	inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id 	where l.tipo != 'compra' and l.statuscompra in ('CONCLUIDO','N_INCIADO') and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) and la.atividade_id = ap.id "); 
			jpql.append(" and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb') and l.tipolancamento != 'reemb_conta' and l.tipo != 'baixa_aplicacao' and l.tipo != 'doacao_efetiva' and l.tipo != 'custo_pessoal' and l.tipo != 'aplicacao_recurso' and case p.tarifado when true then ( 1 = 1 ) else (l.tipo != 'tarifa_bancaria') end	 	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF')) and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB')) ");
			jpql.append(" and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF')) and (((select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF' or (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CA') and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB')) as despesa_04 "); 

			jpql.append(" from atividade_projeto  ap inner join projeto p on p.id  = ap.projeto_id where  p.id = :idProjeto and  (p.ativo  is true ) and p.datafinal >= CURRENT_DATE ");


			Query query = this.manager.createNativeQuery(jpql.toString());

			System.out.println(jpql.toString());

			query.setParameter("idProjeto", idProjeto);

			List<Object[]> result = query.getResultList();
			List<AtividadeProjeto> atividades = new ArrayList<AtividadeProjeto>();

			for (Object[] object : result) {
				AtividadeProjeto  atividade = new AtividadeProjeto(); 

				BigDecimal total = new BigDecimal(object[2] == null? "0": object[2].toString());

				BigDecimal receitas = new BigDecimal(object[3] == null? "0": object[3].toString());
				BigDecimal despesas = new BigDecimal(object[4] == null? "0": object[4].toString()); 

				atividade.setId(Util.getNullValue((object[0].toString()), new Long(0)));
				atividade.setNome(Util.getNullValue(object[1].toString(), "Valor Não Informado"));
				atividade.setValor(total);
				atividade.setSaldo(total.subtract(despesas).add(receitas));



				atividades.add(atividade);
			}

			return atividades;


		}


		public BigDecimal getDespesasAtividadeProjeto(Long idAtividadeProjeto) {
			StringBuilder jpql = new StringBuilder("select ap.id as id_00 ,ap.nome as nome_01 ,ap.valor as valor_02 , ");
			jpql.append(" (select sum(pl.valor) from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id 	inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id 	where l.tipo != 'compra' and l.statuscompra in ('CONCLUIDO','N_INCIADO') and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) and la.atividade_id = ap.id "); 
			jpql.append(" and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb') and l.tipolancamento != 'reemb_conta' and l.tipo != 'baixa_aplicacao' and l.tipo != 'doacao_efetiva' and l.tipo != 'custo_pessoal' and l.tipo != 'aplicacao_recurso' and case p.tarifado when true then ( 1 = 1 ) else (l.tipo != 'tarifa_bancaria') end	 	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF')) and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB')) ");
			jpql.append(" and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF')) and (((select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF' or (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CA') and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB')) as despesa_04 "); 


			jpql.append(" from atividade_projeto  ap inner join projeto p on p.id  = ap.projeto_id where  ap.id = :idAtividadeProjeto and  (p.ativo  is true ) and p.datafinal >= CURRENT_DATE ");


			Query query = this.manager.createNativeQuery(jpql.toString());

			System.out.println(jpql.toString());

			query.setParameter("idAtividadeProjeto", idAtividadeProjeto);

			Object[] object = (Object[]) query.getSingleResult();


			BigDecimal receitas = new BigDecimal(object[3] == null? "0": object[3].toString());


			return receitas;



		}
		public BigDecimal getReceitasAtividadeProjeto(Long idAtividadeProjeto) {
			StringBuilder jpql = new StringBuilder("select ap.id as id_00 ,ap.nome as nome_01 ,ap.valor as valor_02 , ");
			jpql.append(" (select sum(pl.valor) from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id where l.tipo != 'compra' 	and l.statuscompra in ('CONCLUIDO','N_INCIADO') and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) and la.atividade_id = ap.id  and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb') ");
			jpql.append(" and l.tipolancamento != 'reemb_conta' and l.tipo != 'baixa_aplicacao' and l.tipo != 'doacao_efetiva' and l.tipo != 'custo_pessoal' and l.tipo != 'aplicacao_recurso' and case p.tarifado when true then ( 1= 1) else (l.tipo != 'tarifa_bancaria') end	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB')) and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF')) and (((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF' or (select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA') and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB')) as receita_03"); 



			jpql.append(" from atividade_projeto  ap inner join projeto p on p.id  = ap.projeto_id where  ap.id = :idAtividadeProjeto and  (p.ativo  is true ) and p.datafinal >= CURRENT_DATE ");


			Query query = this.manager.createNativeQuery(jpql.toString());


			System.out.println(jpql.toString());

			query.setParameter("idAtividadeProjeto", idAtividadeProjeto);

			Object[] object = (Object[]) query.getSingleResult();





			BigDecimal despesas = new BigDecimal(object[3] == null? "0": object[3].toString()); 




			return despesas;


		}



		public boolean verificarSaldoAtividade(Long idAtividadeProjeto, BigDecimal valor) {


			BigDecimal orcado = getAtividadeById(idAtividadeProjeto).getValor();
			BigDecimal saida = getDespesasAtividadeProjeto(idAtividadeProjeto);
			BigDecimal entrada = getReceitasAtividadeProjeto(idAtividadeProjeto);

			BigDecimal saldo = orcado.add(entrada).subtract(saida);

			return (saldo.subtract(valor).compareTo(BigDecimal.ZERO) >= 0 ); 
		}


		public List<AtividadeProjeto> getAtividadeWithSaldoByLancamento(Long idLancamento) {
			StringBuilder jpql = new StringBuilder("select ap.id as id_00 ,ap.nome as nome_01 ,ap.valor as valor_02 , ");
			jpql.append(" (select sum(pl.valor) from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id where l.tipo != 'compra' 	and l.statuscompra in ('CONCLUIDO','N_INCIADO') and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) and la.atividade_id = ap.id  and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb') ");
			jpql.append(" and l.tipolancamento != 'reemb_conta' and l.tipo != 'baixa_aplicacao' and l.tipo != 'doacao_efetiva' and l.tipo != 'custo_pessoal' and l.tipo != 'aplicacao_recurso' and case p.tarifado when true then ( 1= 1) else (l.tipo != 'tarifa_bancaria') end	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB')) and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF')) and (((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF' or (select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA') and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB')) as receita_03,"); 

			jpql.append(" (select sum(pl.valor) from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id 	inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id 	where l.tipo != 'compra' and l.statuscompra in ('CONCLUIDO','N_INCIADO') and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) and la.atividade_id = ap.id "); 
			jpql.append(" and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb') and l.tipolancamento != 'reemb_conta' and l.tipo != 'baixa_aplicacao' and l.tipo != 'doacao_efetiva' and l.tipo != 'custo_pessoal' and l.tipo != 'aplicacao_recurso' and case p.tarifado when true then ( 1 = 1 ) else (l.tipo != 'tarifa_bancaria') end	 	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF')) and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB')) ");
			jpql.append(" and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF')) and (((select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF' or (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CA') and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB')) as despesa_04 "); 


			jpql.append(" from atividade_projeto  ap inner join projeto p on p.id  = ap.projeto_id where  (p.ativo  is true ) and p.datafinal >= CURRENT_DATE and ap.id in (select la.atividade_id from lancamento_acao la where la.lancamento_id = (select l.id from lancamento l where l.id = :idLancamento )) ");


			Query query = this.manager.createNativeQuery(jpql.toString());

			System.out.println(jpql.toString());

			query.setParameter("idLancamento", idLancamento);

			List<Object[]> result = query.getResultList();
			List<AtividadeProjeto> atividades = new ArrayList<AtividadeProjeto>();

			for (Object[] object : result) {
				AtividadeProjeto  atividade = new AtividadeProjeto(); 

				BigDecimal total = new BigDecimal(object[2] == null? "0": object[2].toString());

				BigDecimal receitas = new BigDecimal(object[3] == null? "0": object[3].toString());
				BigDecimal despesas = new BigDecimal(object[4] == null? "0": object[4].toString()); 

				atividade.setId(Util.getNullValue((object[0].toString()), new Long(0)));
				atividade.setNome(Util.getNullValue(object[1].toString(), "Valor Não Informado"));
				atividade.setValor(total);
				atividade.setSaldo(total.subtract(despesas).add(receitas));



				atividades.add(atividade);
			}

			return atividades;


		}

}
