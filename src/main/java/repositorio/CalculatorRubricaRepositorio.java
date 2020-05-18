package repositorio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Projeto;
import model.ProjetoRubrica;
import util.Util;

public class CalculatorRubricaRepositorio implements Serializable {

	/**
	 * Class created for calculations of the Rubricas by Christophe 20/09/2018
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private EntityManager manager;

	@Inject
	private ProjetoRepositorio projetoRepositorio;


	//Metodo para calcular as entradas na rubrica.
	public BigDecimal getValorReceitaRubrica(Long idProjetoRubrica) {
		StringBuilder jpql = new StringBuilder("select sum(pl.valor) as receita ");
		jpql.append("from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id\n"); 
		jpql.append("inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id \n");
		jpql.append("where l.tipo != 'compra'\n"); 
		jpql.append("and l.statuscompra in ('CONCLUIDO','N_INCIADO') \n");
		jpql.append("and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) \n");
		jpql.append("and la.projetorubrica_id = :pIdProjetoRubrica \n"); 
		jpql.append("and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb')\n"); 
		jpql.append("and l.tipolancamento != 'reemb_conta'\n"); 
		jpql.append("and l.tipo != 'baixa_aplicacao'\n");
		jpql.append("and l.tipo != 'doacao_efetiva'\n"); 
		jpql.append("and l.tipo != 'custo_pessoal'\n"); 
		jpql.append("and l.tipo != 'aplicacao_recurso'   \n");
		jpql.append("and case (select p.tarifado from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where pr.id = :pIdProjetoRubrica)) when true then ( 1=1 ) else (l.tipo != 'tarifa_bancaria') end	 ");
		
		jpql.append(" and (false = (pl.tipocontapagador = 'CA' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CB' and pl.tipocontarecebedor = 'CB')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CF' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and ((pl.tipocontapagador = 'CF' or pl.tipocontapagador = 'CA') and (pl.tipocontarecebedor = 'CB')) ");
//		jpql.append(" as receita ");
		
//		jpql.append("and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))\n"); 
//		jpql.append("and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB'))\n"); 
//		jpql.append("and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))\n");
//		jpql.append("and (((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF' or (select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA')\n");
//		jpql.append("and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB')\n");
		//end
		Query query = this.manager.createNativeQuery(jpql.toString());

		query.setParameter("pIdProjetoRubrica", idProjetoRubrica);

		BigDecimal result =  (BigDecimal) query.getSingleResult();

		return result == null? new BigDecimal("0"):result;
	}
	
	//Metodo para calcular as saidas de uma rubrica.
	public BigDecimal getValorDespesaRubrica(Long idProjetoRubrica) {
		StringBuilder jpql = new StringBuilder("select sum(pl.valor) as despesa ");
		jpql.append("from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id\n"); 
		jpql.append("inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id \n");
		jpql.append("where l.tipo != 'compra'\n"); 
		jpql.append("and l.statuscompra in ('CONCLUIDO','N_INCIADO') \n");
		jpql.append("and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) \n");
		jpql.append("and la.projetorubrica_id = :pIdProjetoRubrica \n"); 
		jpql.append("and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb')\n"); 
		jpql.append("and l.tipolancamento != 'reemb_conta'\n"); 
		jpql.append("and l.tipo != 'baixa_aplicacao'\n");
		jpql.append("and l.tipo != 'doacao_efetiva'\n"); 
		jpql.append("and l.tipo != 'custo_pessoal'\n"); 
		jpql.append("and l.tipo != 'aplicacao_recurso'   \n");
		jpql.append("and case (select p.tarifado from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where pr.id = :pIdProjetoRubrica)) when true then ( 1=1 ) else (l.tipo != 'tarifa_bancaria') end	 ");
		
		jpql.append(" and (false = (pl.tipocontapagador = 'CA' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CB' and pl.tipocontarecebedor = 'CB')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CF' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and ((pl.tipocontarecebedor = 'CF' or pl.tipocontarecebedor = 'CA') and (pl.tipocontapagador = 'CB')) ");
//		jpql.append(" as despesa ");
		
//		jpql.append("and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))\n"); 
//		jpql.append("and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB'))\n"); 
//		jpql.append("and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))\n");
//		jpql.append("and  (((select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF' or (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CA')\n"); 
//		jpql.append("and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB')");
		
		
		Query query = this.manager.createNativeQuery(jpql.toString());

		query.setParameter("pIdProjetoRubrica", idProjetoRubrica);

		BigDecimal result =  (BigDecimal) query.getSingleResult();

		return result == null? new BigDecimal("0"):result;
	}
	
	//Metodo para buscas rubricas por strings parciais trazendo os saldos por rubrica.
	public List<ProjetoRubrica> getProjetoRubricaByStrNormalizado(String s) {
		StringBuilder jpql = new StringBuilder("select pr.id as id_00 ,p.codigo as codigo_01,orc.titulo as fonte_02 ,");
		jpql.append("p.nome as projeto_03,"); 
		jpql.append("(select cc.nome from componente_class cc where   cc.id = pr.componente_id ) as componente_04,\n"); 
		jpql.append("(select sc.nome from sub_componente  sc where sc.id = pr.subcomponente_id) as subComponte_05,\n");
		jpql.append("(select ru.nome from rubrica ru where ru.id = ro.rubrica_id) as linha_orcamentaria_06 ,");
		jpql.append("pr.valor as valor_07, ");
		jpql.append("(select sum(pl.valor)\r\n"); 
		jpql.append("from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id  \r\n");
		jpql.append("inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id \r\n");
		jpql.append("where l.tipo != 'compra' \r\n"); 
		jpql.append("and l.statuscompra in ('CONCLUIDO','N_INCIADO') \r\n");
		jpql.append("and (l.versionlancamento = 'MODE01' or pl.reclassificado is true)\r\n");
		jpql.append("and la.projetorubrica_id = pr.id  \r\n");
		jpql.append("and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb')\r\n");
		jpql.append("and l.tipolancamento != 'reemb_conta' \r\n");
		jpql.append("and l.tipo != 'baixa_aplicacao'\r\n");
		jpql.append("and l.tipo != 'doacao_efetiva' \r\n"); 
		jpql.append("and l.tipo != 'custo_pessoal' \r\n"); 
		jpql.append("and l.tipo != 'aplicacao_recurso'   \r\n"); 
		jpql.append("	and case p.tarifado when true then ( 1 = 1 ) else (l.tipo != 'tarifa_bancaria') end	 ");
		
		jpql.append(" and (false = (pl.tipocontapagador = 'CA' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CB' and pl.tipocontarecebedor = 'CB')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CF' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and ((pl.tipocontapagador = 'CF' or pl.tipocontapagador = 'CA') and (pl.tipocontarecebedor = 'CB'))) ");
		jpql.append(" as receita, ");
		
//		jpql.append("and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))\r\n"); 
//		jpql.append("and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB'))\r\n"); 
//		jpql.append("and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))\r\n"); 
//		jpql.append("and (((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF' or (select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA')\r\n");
//		jpql.append("and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB')) as receita,") ;
		jpql.append("(select sum(pl.valor)\r\n"); 
		jpql.append("from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id  \r\n");
		jpql.append("inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id \r\n");
		jpql.append("where l.tipo != 'compra'   \r\n");
		jpql.append("and l.statuscompra in ('CONCLUIDO','N_INCIADO') \r\n"); 
		jpql.append("and (l.versionlancamento = 'MODE01' or pl.reclassificado is true)\r\n"); 
		jpql.append("and la.projetorubrica_id = pr.id  \r\n");
		jpql.append("and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb')\r\n");
		jpql.append("and l.tipolancamento != 'reemb_conta' \r\n"); 
		jpql.append("and l.tipo != 'baixa_aplicacao'\r\n");
		jpql.append("and l.tipo != 'doacao_efetiva' \r\n");
		jpql.append("and l.tipo != 'custo_pessoal' \r\n"); 
		jpql.append("and l.tipo != 'aplicacao_recurso'\r\n"); 
		jpql.append("	and case p.tarifado when true then ( 1 = 1 ) else (l.tipo != 'tarifa_bancaria') end	 ");
		
		jpql.append(" and (false = (pl.tipocontapagador = 'CA' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CB' and pl.tipocontarecebedor = 'CB')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CF' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and ((pl.tipocontarecebedor = 'CF' or pl.tipocontarecebedor = 'CA') and (pl.tipocontapagador = 'CB'))) ");
		jpql.append(" as despesa ");
		
//		jpql.append("and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))\r\n"); 
//		jpql.append("and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB'))\r\n"); 
//		jpql.append("and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))\r\n"); 
//		jpql.append("and (((select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF' or (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CA')\r\n");
//		jpql.append("and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB')) as despesa ");
		jpql.append("from projeto_rubrica pr \n"); 
		jpql.append("inner join rubrica_orcamento ro on pr.rubricaorcamento_id = ro.id \n");
		jpql.append("inner join orcamento orc on ro.orcamento_id = orc.id \n");
		jpql.append("inner join projeto p on p.id  = pr.projeto_id \n"); 
		jpql.append("inner join rubrica r on ro.rubrica_id = r.id  \n");
		jpql.append("where 1 = 1 and ((lower(r.nome)  like lower(:nome)) or (lower(p.nome) like lower(:nome)) or (lower(p.codigo)  like lower(:nome))) and  (p.ativo  is true )");
		
		Query query = this.manager.createNativeQuery(jpql.toString());

		query.setParameter("nome", "%" + s + "%");

		List<Object[]> result = query.getResultList();
		List<ProjetoRubrica> rubricas = new ArrayList<ProjetoRubrica>();

		for (Object[] object : result) {
			BigDecimal total = new BigDecimal(object[7] == null? "0": object[7].toString());
//			BigDecimal despesas = getValorDespesaRubrica(Util.getNullValue((object[0].toString()), new Long(0)));
//			BigDecimal receitas = getValorReceitaRubrica(Util.getNullValue((object[0].toString()), new Long(0)));
			
			BigDecimal receitas = new BigDecimal(object[8] == null? "0": object[8].toString());
			BigDecimal despesas = new BigDecimal(object[9] == null? "0": object[9].toString()); 
			
			ProjetoRubrica pro = new ProjetoRubrica(
					Util.getNullValue((object[0].toString()), new Long(0)),
					Util.getNullValue(object[1].toString(), "Valor Não Informado"),
					Util.getNullValue(object[2].toString(), "Valor Não Informado"),
					Util.getNullValue(object[3].toString(), "Valor Não Informado"),
					Util.getNullValue(object[4].toString(), "Valor Não Informado"),
					Util.getNullValue(object[5].toString(), "Valor Não Informado"),
					Util.getNullValue(object[6].toString(), "Valor Não Informado"),
					total.subtract(despesas).add(receitas));
			rubricas.add(pro);
		}

		return rubricas;


	}
	
	
	public List<ProjetoRubrica> getProjetoRubricaByProjeto(Long idProjeto) {
		StringBuilder jpql = new StringBuilder("select pr.id as id_00 ,p.codigo as codigo_01,orc.titulo as fonte_02 ,");
		jpql.append("p.nome as projeto_03,"); 
		jpql.append("(select cc.nome from componente_class cc where   cc.id = pr.componente_id ) as componente_04,\n"); 
		jpql.append("(select sc.nome from sub_componente  sc where sc.id = pr.subcomponente_id) as subComponte_05,\n");
		jpql.append("(select ru.nome from rubrica ru where ru.id = ro.rubrica_id) as linha_orcamentaria_06 ,");
		jpql.append("pr.valor as valor_07, ");
		
		jpql.append("(select sum(pl.valor) ");
		jpql.append("from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id  ");
		jpql.append("	inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");
		jpql.append("	where l.tipo != 'compra' ");
		jpql.append("	and l.statuscompra in ('CONCLUIDO','N_INCIADO') "); 
		jpql.append("	and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) ");
		jpql.append("	and la.projetorubrica_id = pr.id  "); 
		jpql.append("	and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb') "); 
		jpql.append("	and l.tipolancamento != 'reemb_conta' "); 
		jpql.append("	and l.tipo != 'baixa_aplicacao' "); 
		jpql.append("	and l.tipo != 'doacao_efetiva' "); 
		jpql.append("	and l.tipo != 'custo_pessoal'   "); 
		jpql.append("	and l.tipo != 'aplicacao_recurso' ");
		jpql.append("	and case p.tarifado when true then ( 1= 1) else (l.tipo != 'tarifa_bancaria') end	 ");
		
//		jpql.append("	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))");
//		jpql.append("	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB'))");
//		jpql.append("	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))");
//		jpql.append("	and (((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF' or (select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA') "); 
//		jpql.append("	and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB')) as receita,") ;
		
		jpql.append(" and (false = (pl.tipocontapagador = 'CA' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CB' and pl.tipocontarecebedor = 'CB')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CF' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and ((pl.tipocontapagador = 'CF' or pl.tipocontapagador = 'CA') and (pl.tipocontarecebedor = 'CB'))) ");
		jpql.append(" as receita, ");
		
		jpql.append("(select sum(pl.valor)");
		jpql.append("	from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id ");
		jpql.append("	inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id "); 
		jpql.append("	where l.tipo != 'compra'  \r\n"); 
		jpql.append("	and l.statuscompra in ('CONCLUIDO','N_INCIADO') \r\n"); 
		jpql.append("	and (l.versionlancamento = 'MODE01' or pl.reclassificado is true)\r\n"); 
		jpql.append("	and la.projetorubrica_id = pr.id  \r\n"); 
		jpql.append("	and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb')\r\n"); 
		jpql.append("	and l.tipolancamento != 'reemb_conta' \r\n"); 
		jpql.append("	and l.tipo != 'baixa_aplicacao'\r\n"); 
		jpql.append("	and l.tipo != 'doacao_efetiva' \r\n");
		jpql.append("	and l.tipo != 'custo_pessoal' \r\n");
		jpql.append("	and l.tipo != 'aplicacao_recurso'\r\n");
		jpql.append("	and case p.tarifado when true then ( 1 = 1 ) else (l.tipo != 'tarifa_bancaria') end	 ");
//		jpql.append("	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))\r\n" ); 
//		jpql.append("	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB'))\r\n" ); 
//		jpql.append("	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))\r\n" ); 
//		jpql.append("	and (((select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF' or (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CA')\r\n" ); 
//		jpql.append("	and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB')) as despesa ");
		
		jpql.append(" and (false = (pl.tipocontapagador = 'CA' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CB' and pl.tipocontarecebedor = 'CB')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CF' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and ((pl.tipocontarecebedor = 'CF' or pl.tipocontarecebedor = 'CA') and (pl.tipocontapagador = 'CB'))) ");
//		jpql.append(" as despesa ");
	
		
		jpql.append("from projeto_rubrica pr \n"); 
		jpql.append("inner join rubrica_orcamento ro on pr.rubricaorcamento_id = ro.id \n");
		jpql.append("inner join orcamento orc on ro.orcamento_id = orc.id \n");
		jpql.append("inner join projeto p on p.id  = pr.projeto_id \n"); 
		jpql.append("inner join rubrica r on ro.rubrica_id = r.id  \n");
		jpql.append("where  p.id = :idProjeto and  (p.ativo  is true ) ");
		jpql.append("and orc.datafinal >= CURRENT_DATE");
		Query query = this.manager.createNativeQuery(jpql.toString());
		
		System.out.println(jpql.toString());
		
		query.setParameter("idProjeto", idProjeto);

		List<Object[]> result = query.getResultList();
		List<ProjetoRubrica> rubricas = new ArrayList<ProjetoRubrica>();

		for (Object[] object : result) {
			BigDecimal total = new BigDecimal(object[7] == null? "0": object[7].toString());
			
			BigDecimal receitas = new BigDecimal(object[8] == null? "0": object[8].toString());
			BigDecimal despesas = new BigDecimal(object[9] == null? "0": object[9].toString()); 
			
			ProjetoRubrica pro = new ProjetoRubrica(
					Util.getNullValue((object[0].toString()), new Long(0)),
					Util.getNullValue(object[1].toString(), "Valor Não Informado"),
					Util.getNullValue(object[2].toString(), "Valor Não Informado"),
					Util.getNullValue(object[3].toString(), "Valor Não Informado"),
					Util.getNullValue(object[4].toString(), "Valor Não Informado"),
					Util.getNullValue(object[5].toString(), "Valor Não Informado"),
					Util.getNullValue(object[6].toString(), "Valor Não Informado"),
					total.subtract(despesas).add(receitas));
			rubricas.add(pro);
		}

		return rubricas;


	}
	
	public List<ProjetoRubrica> getProjetoRubricaByLancamento(Long idLancamento) {
		StringBuilder jpql = new StringBuilder("select pr.id as id_00 ,p.codigo as codigo_01,orc.titulo as fonte_02 ,");
		jpql.append("p.nome as projeto_03,"); 
		jpql.append("(select cc.nome from componente_class cc where   cc.id = pr.componente_id ) as componente_04,\n"); 
		jpql.append("(select sc.nome from sub_componente  sc where sc.id = pr.subcomponente_id) as subComponte_05,\n");
		jpql.append("(select ru.nome from rubrica ru where ru.id = ro.rubrica_id) as linha_orcamentaria_06 ,");
		jpql.append("pr.valor as valor_07, ");
		
		jpql.append("(select sum(pl.valor) ");
		jpql.append("from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id  ");
		jpql.append("	inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");
		jpql.append("	where l.tipo != 'compra' ");
		jpql.append("	and l.statuscompra in ('CONCLUIDO','N_INCIADO') "); 
		jpql.append("	and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) ");
		jpql.append("	and la.projetorubrica_id = pr.id  "); 
		jpql.append("	and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb') "); 
		jpql.append("	and l.tipolancamento != 'reemb_conta' "); 
		jpql.append("	and l.tipo != 'baixa_aplicacao' "); 
		jpql.append("	and l.tipo != 'doacao_efetiva' "); 
		jpql.append("	and l.tipo != 'custo_pessoal'   "); 
		jpql.append("	and l.tipo != 'aplicacao_recurso' ");
		jpql.append("	and case p.tarifado when true then ( 1= 1) else (l.tipo != 'tarifa_bancaria') end	 ");
		
		jpql.append(" and (false = (pl.tipocontapagador = 'CA' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CB' and pl.tipocontarecebedor = 'CB')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CF' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and ((pl.tipocontapagador = 'CF' or pl.tipocontapagador = 'CA') and (pl.tipocontarecebedor = 'CB'))) ");
		jpql.append(" as receita, ");
		
//		jpql.append("	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))");
//		jpql.append("	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB'))");
//		jpql.append("	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))");
//		jpql.append("	and (((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF' or (select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA') "); 
//		jpql.append("	and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB')) as receita,") ;
		
		jpql.append("(select sum(pl.valor)");
		jpql.append("	from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id ");
		jpql.append("	inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id "); 
		jpql.append("	where l.tipo != 'compra'  \r\n"); 
		jpql.append("	and l.statuscompra in ('CONCLUIDO','N_INCIADO') \r\n"); 
		jpql.append("	and (l.versionlancamento = 'MODE01' or pl.reclassificado is true)\r\n"); 
		jpql.append("	and la.projetorubrica_id = pr.id  \r\n"); 
		jpql.append("	and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb')\r\n"); 
		jpql.append("	and l.tipolancamento != 'reemb_conta' \r\n"); 
		jpql.append("	and l.tipo != 'baixa_aplicacao'\r\n"); 
		jpql.append("	and l.tipo != 'doacao_efetiva' \r\n");
		jpql.append("	and l.tipo != 'custo_pessoal' \r\n");
		jpql.append("	and l.tipo != 'aplicacao_recurso'\r\n");
		jpql.append("	and case p.tarifado when true then ( 1 = 1 ) else (l.tipo != 'tarifa_bancaria') end	 ");
		
		jpql.append(" and (false = (pl.tipocontapagador = 'CA' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CB' and pl.tipocontarecebedor = 'CB')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CF' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and ((pl.tipocontarecebedor = 'CF' or pl.tipocontarecebedor = 'CA') and (pl.tipocontapagador = 'CB'))) ");
		jpql.append(" as despesa ");
		
//		jpql.append("	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))\r\n" ); 
//		jpql.append("	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB'))\r\n" ); 
//		jpql.append("	and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))\r\n" ); 
//		jpql.append("	and (((select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF' or (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CA')\r\n" ); 
//		jpql.append("	and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB')) as despesa ");
		
		
		
		jpql.append("from projeto_rubrica pr \n"); 
		jpql.append("inner join rubrica_orcamento ro on pr.rubricaorcamento_id = ro.id \n");
		jpql.append("inner join orcamento orc on ro.orcamento_id = orc.id \n");
		jpql.append("inner join projeto p on p.id  = pr.projeto_id \n"); 
		jpql.append("inner join rubrica r on ro.rubrica_id = r.id  \n");
		jpql.append("where (p.ativo  is true ) and pr.id in (select la.projetorubrica_id from lancamento_acao la where la.lancamento_id = (select l.id from lancamento l where l.id = :idLancamento ))");
		Query query = this.manager.createNativeQuery(jpql.toString());
		
		System.out.println(jpql.toString());
		
		query.setParameter("idLancamento", idLancamento);

		List<Object[]> result = query.getResultList();
		List<ProjetoRubrica> rubricas = new ArrayList<ProjetoRubrica>();

		for (Object[] object : result) {
			BigDecimal total = new BigDecimal(object[7] == null? "0": object[7].toString());
			
			BigDecimal receitas = new BigDecimal(object[8] == null? "0": object[8].toString());
			BigDecimal despesas = new BigDecimal(object[9] == null? "0": object[9].toString()); 
			
			ProjetoRubrica pro = new ProjetoRubrica(
					Util.getNullValue((object[0].toString()), new Long(0)),
					Util.getNullValue(object[1].toString(), "Valor Não Informado"),
					Util.getNullValue(object[2].toString(), "Valor Não Informado"),
					Util.getNullValue(object[3].toString(), "Valor Não Informado"),
					Util.getNullValue(object[4].toString(), "Valor Não Informado"),
					Util.getNullValue(object[5].toString(), "Valor Não Informado"),
					Util.getNullValue(object[6].toString(), "Valor Não Informado"),
					total.subtract(despesas).add(receitas));
			rubricas.add(pro);
		}

		return rubricas;


	}
	
	

	//metodo para verificar se a rubrica possui saldo sufuciente baseda no valor passado como parametro.
	public boolean verificarSaldoRubrica(Long idProjetoRubrica, BigDecimal valor) {


		BigDecimal orcado = projetoRepositorio.getOrcamentoDeRubrica(idProjetoRubrica);
		BigDecimal saida = getValorDespesaRubrica(idProjetoRubrica);
		BigDecimal entrada = getValorReceitaRubrica(idProjetoRubrica);

		BigDecimal saldo = orcado.add(entrada).subtract(saida);

		return (saldo.subtract(valor).compareTo(BigDecimal.ZERO) >= 0 ); 
	}
	
	//metodo para verificar se o lancamento_acao e uma receita se sim retorno true!
	public Boolean verificaReceita(Long idLancamentoAcao) {
		StringBuilder jpql = new StringBuilder("select la.id ");
		jpql.append("from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id\n"); 
		jpql.append("inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id \n");
		jpql.append("where l.tipo != 'compra'\n"); 
		jpql.append("and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) \n");
		jpql.append("and la.id = :pIdLancamentoAcao \n"); 
		jpql.append("and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb')\n"); 
		jpql.append("and l.tipolancamento != 'reemb_conta'\n"); 
		jpql.append("and l.tipo = 'baixa_aplicacao'\n");
		jpql.append("and l.tipo = 'doacao_efetiva'\n"); 
		jpql.append("and l.tipo = 'aplicacao_recurso'   \n");
		
		jpql.append(" and (false = (pl.tipocontapagador = 'CA' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CB' and pl.tipocontarecebedor = 'CB')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CF' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and ((pl.tipocontapagador = 'CF' or pl.tipocontapagador = 'CA') and (pl.tipocontarecebedor = 'CB')) ");
//		jpql.append(" as receita ");
		
//		jpql.append("and (true = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB'))\n"); 
//		jpql.append("and (true = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))\n");
//		jpql.append("and (((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF' or (select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA')\n");
//		jpql.append("and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB')\n");
		//end
		Query query = this.manager.createNativeQuery(jpql.toString());

		query.setParameter("pIdLancamentoAcao", idLancamentoAcao);

		List<Object[]> result = query.getResultList();

		return result.size() > 0;
	}


}
