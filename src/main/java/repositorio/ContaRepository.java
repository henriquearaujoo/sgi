package repositorio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.*;
import util.Filtro;
import util.Util;

public class ContaRepository implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private EntityManager manager;

	public ContaRepository() {
	}

	public ContaBancaria getDefaultRecebedorTarifa() {
		return manager.find(ContaBancaria.class, new Long(1238));
	}

	public ContaBancaria buscarContaFleg(Long fornecedor) {
		String jpql = "FROM ContaBancaria AS cb where cb.classificacaoConta = :classificacao and cb.fornecedor.id = :id_fornecedor";
		Query query = manager.createQuery(jpql);
		query.setParameter("classificacao", "FLEG");
		query.setParameter("id_fornecedor", fornecedor);
		return query.getResultList().size() > 0 ? (ContaBancaria) query.getResultList().get(0) : null;
	}
	
	public List<ContaBancaria> buscarContaDiferenteFleg(Long fornecedor) {
		String jpql = "FROM ContaBancaria AS cb where cb.classificacaoConta != :classificacao and cb.fornecedor.id = :id_fornecedor";
		Query query = manager.createQuery(jpql);
		query.setParameter("classificacao", "FLEG");
		query.setParameter("id_fornecedor", fornecedor);
		return query.getResultList().size() > 0 ?  query.getResultList() : new ArrayList<>();
	}

	public ContaBancaria findById(Long id) {
		return manager.find(ContaBancaria.class, id);
	}

	public ContaRepository(EntityManager manager) {
		this.manager = manager;
	}

	public PagamentoLancamento salvar(PagamentoLancamento pagamento) {
		return manager.merge(pagamento);
	}

	public void removerConta(ContaBancaria contaBancaria) {
		this.manager.remove(this.manager.find(ContaBancaria.class, contaBancaria.getId()));
	}

	public ContaBancaria getContaById(Long id) {
		return manager.find(ContaBancaria.class, id);
	}

	public ContaBancaria salvarConta(ContaBancaria conta) {
		return manager.merge(conta);
	}

	public ContaBancaria salvarContaFornecedor(ContaBancaria contaBancaria) {

		Fornecedor fornecedor = new Fornecedor();

		if (contaBancaria.getId() != null && contaBancaria.getFornecedor() != null) {
			fornecedor = this.manager.find(Fornecedor.class, contaBancaria.getFornecedor().getId());
		}

		fornecedor.setNomeFantasia(contaBancaria.getNomeConta());
		fornecedor.setRazaoSocial(contaBancaria.getRazaoSocial());
		fornecedor.setCnpj(contaBancaria.getCnpj() != null ? contaBancaria.getCnpj() : "");
		fornecedor.setCpf(contaBancaria.getCpf() != null ? contaBancaria.getCpf() : "");
		fornecedor.setTaxiid(contaBancaria.getTaxiid() != null ? contaBancaria.getTaxiid() : "");
		fornecedor.setTipo(contaBancaria.getTipoJuridico() != null ? contaBancaria.getTipoJuridico() : "");
		fornecedor.setEmail(contaBancaria.getEmail() != null ? contaBancaria.getEmail() : "");
		fornecedor.setPis(contaBancaria.getPIS() != null ? contaBancaria.getPIS() : "");
		contaBancaria.setFornecedor(manager.merge(fornecedor));
		return this.manager.merge(contaBancaria);
	}

	public ContaBancaria salvarContaBancariaFornecedor(ContaBancaria contaBancaria) {

		Fornecedor fornecedor = contaBancaria.getFornecedor();

		// if (contaBancaria.getId() != null && contaBancaria.getFornecedor() !=
		// null) {
		// fornecedor = this.manager.find(Fornecedor.class,
		// contaBancaria.getFornecedor().getId());
		// }

		// fornecedor.setNomeFantasia(contaBancaria.getNomeConta());
		// fornecedor.setRazaoSocial(contaBancaria.getRazaoSocial());
		// fornecedor.setCnpj(contaBancaria.getCnpj() != null ?
		// contaBancaria.getCnpj() : "");
		// fornecedor.setCpf(contaBancaria.getCpf() != null ?
		// contaBancaria.getCpf() : "");
		// fornecedor.setTaxiid(contaBancaria.getTaxiid() != null ?
		// contaBancaria.getTaxiid() : "");
		// fornecedor.setTipo(contaBancaria.getTipoJuridico() != null ?
		// contaBancaria.getTipoJuridico() : "");
		// fornecedor.setEmail(contaBancaria.getEmail() != null ?
		// contaBancaria.getEmail() : "");
		// fornecedor.setPis(contaBancaria.getPIS() != null ?
		// contaBancaria.getPIS() : "");
		contaBancaria.setFornecedor(manager.merge(fornecedor));
		return this.manager.merge(contaBancaria);

		//
		// contaBancaria.setCnpj(fornecedor.getCnpj() != null ?
		// fornecedor.getCnpj() : "");
		// contaBancaria.setCpf(fornecedor.getCpf() != null ?
		// fornecedor.getCpf() : "");
		// contaBancaria.setEmail(fornecedor.getEmail() != null ?
		// fornecedor.getEmail() : "");
		// contaBancaria.setPIS(fornecedor.getPis() != null ?
		// fornecedor.getPis() : "");
		// contaBancaria.setRazaoSocial(fornecedor.getRazaoSocial() != null ?
		// fornecedor.getRazaoSocial() : "");
		// contaBancaria.setNomeConta(fornecedor.getNomeFantasia() != null ?
		// fornecedor.getNomeFantasia() : "");
		// contaBancaria.setSaldoInicial(new BigDecimal("0"));
		// contaBancaria.setSaldoAtual(new BigDecimal("0"));
		// contaBancaria.setTipo("CF");
		// fornecedor.setAgencia(contaBancaria.getNumeroAgencia());
		// fornecedor.setBanco(contaBancaria.getNomeBanco());
		// fornecedor.setConta(contaBancaria.getNumeroConta());
		// contaBancaria.setFornecedor(fornecedor);
	}

	public ContaBancaria updateContaBancariaFornecedor(ContaBancaria contaBancaria) {
		return this.manager.merge(contaBancaria);
	}

	public List<ContaBancaria> getContasBancarias(Filtro filtro) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW ContaBancaria(c.id,c.numeroConta,c.numeroAgencia,c.nomeConta,b.nomeBanco,c.saldoInicial,c.tipo,c.status) ");
		jpql.append(" from  ContaBancaria as c LEFT JOIN c.banco as b  where 1 = 1 and c.tipo  != 'CF' ");

		if (filtro.getConta() != null && !filtro.getConta().equals("")) {
			jpql.append("and (lower(c.numeroConta) like lower(:conta) or lower(c.nomeConta) like lower(:conta)) ");
		}

		if (filtro.getAgencia() != null && !filtro.getAgencia().equals("")) {
			jpql.append("and lower(c.numeroAgencia) like lower(:agencia) ");
		}

		if (filtro.getTipoConta() != null && !filtro.getTipoConta().equals("")) {
			jpql.append("and c.tipo = :tipo ");
		}
		
		if (filtro.getStatusConta() != null && !filtro.getStatusConta().equals("")) {
			jpql.append("and c.status = :status ");
		}
		
		jpql.append(" order by c.id");

		Query query = this.manager.createQuery(jpql.toString());

		if (filtro.getConta() != null && !filtro.getConta().equals("")) {
			query.setParameter("conta", "%" + filtro.getConta() + "%");
		}

		if (filtro.getAgencia() != null && !filtro.getAgencia().equals("")) {
			query.setParameter("agencia", "%" + filtro.getAgencia() + "%");
		}

		if (filtro.getTipoConta() != null && !filtro.getTipoConta().equals("")) {
			query.setParameter("tipo", filtro.getTipoConta());
		}
		
		if (filtro.getStatusConta() != null && !filtro.getStatusConta().equals("")) {
			query.setParameter("status", StatusConta.valueOf(filtro.getStatusConta()));
		}

		return query.getResultList();
	}

	public List<ContaBancaria> getContas(Filtro filtro) {
		String jpql = "SELECT NEW ContaBancaria(cb.id, cb.nomeConta) FROM ContaBancaria AS cb where cb.tipo != :tipo";
		Query query = manager.createQuery(jpql);
		query.setParameter("tipo", "CF");
		return query.getResultList();
	}

	public List<ContaBancaria> getContasCorrente(Filtro filtro) {
		String jpql = "SELECT NEW ContaBancaria(cb.id, cb.nomeConta) FROM ContaBancaria AS cb where cb.tipo = :tipo";
		Query query = manager.createQuery(jpql);
		query.setParameter("tipo", "CB");
		return query.getResultList();
	}

	public List<ContaBancaria> getContasFornecedoresEAdiantamentos(String s) {
		StringBuilder jpql = new StringBuilder(
				"from  ContaBancaria c where ( (lower(c.nomeConta) like lower(:nome) or lower(c.razaoSocial) like lower(:nome)) and c.tipo = :tipo ) or ");
		jpql.append(
				" ( (lower(c.nomeConta) like lower(:nome) or lower(c.razaoSocial) like lower(:nome)) and c.fornecedor.id is not null and c.tipo != 'CB') ");

		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		query.setParameter("tipo", "CA");
		query.setMaxResults(30);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<ContaBancaria>();
	}

	// Modificado para filtrar apenas contas com status 'ATIVO' 04/09/2018 by
	// christophe
	public List<ContaBancaria> getContasFornecedoresAtivos(String s) {
		StringBuilder jpql = new StringBuilder("from  ContaBancaria c where ");
		jpql.append(
				"  (lower(c.nomeConta) like lower(:nome) or lower(c.razaoSocial) like lower(:nome) or lower(c.cnpj) like lower(:nome) ) and c.tipo = 'CF' and (c.status = :status or c.status is null )  ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		query.setParameter("status", StatusConta.ATIVA);

		query.setMaxResults(30);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<ContaBancaria>();
	}

	public List<ContaBancaria> getContaFornecedorPrincipal(Fornecedor fornecedor) {
		StringBuilder jpql = new StringBuilder("from  ContaBancaria c where classificacaoConta = 'PRINCIPAL'");
		jpql.append("  and c.fornecedor.id = :fornecedor and c.tipo = 'CF' and (c.status = :status or c.status is null )  order by c.classificacaoConta");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("fornecedor", fornecedor.getId());
		query.setParameter("status", StatusConta.ATIVA);

		query.setMaxResults(30);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<ContaBancaria>();
	}
	
	public List<ContaBancaria> getContasFornecedor(Fornecedor fornecedor) {
		StringBuilder jpql = new StringBuilder("from  ContaBancaria c where  c.fornecedor.id = :fornecedor");
		jpql.append(" and c.tipo = 'CF' and (c.status = :status or c.status is null )  order by c.classificacaoConta");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("fornecedor", fornecedor.getId());
		query.setParameter("status", StatusConta.ATIVA);

		query.setMaxResults(30);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<ContaBancaria>();
	}

	public List<ContaBancaria> getContasColaboradores(String s) {
		StringBuilder jpql = new StringBuilder(
				"from  ContaBancaria c where ((lower(c.nomeConta) like lower(:nome) or lower(c.razaoSocial) like lower(:nome)) and c.tipo = :tipo ) ");
		// jpql.append(" and c.fornecedor.status is true ");
		// jpql.append(
		// " ( lower(c.nomeConta) like lower(:nome) or lower(c.razaoSocial) like
		// lower(:nome) and c.fornecedor.id is not null and c.tipo != 'CB') ");

		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		query.setParameter("tipo", "CF");
		query.setMaxResults(30);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<ContaBancaria>();
	}

	public List<ContaBancaria> getContasFilter() {
		String jpql = "SELECT NEW ContaBancaria(cb.id, cb.nomeConta) FROM ContaBancaria AS cb";
		Query query = manager.createQuery(jpql);
		return query.getResultList();
	}

	public List<Acao> getListAcoes(Filtro filtro) {
		String jpql = "SELECT NEW Acao(a.id, a.codigo) FROM Acao AS a ";
		Query query = manager.createQuery(jpql);
		return query.getResultList();
	}

	public List<Acao> getListAcoesFiltroProjeto(Filtro filtro) {
		StringBuilder jpql = new StringBuilder("SELECT NEW Acao(a.id, a.codigo) FROM Acao AS a ");

		jpql.append(" where 1 = 1 ");

		if (filtro.getProjetoId() != null && filtro.getProjetoId() != new Long(0))
			jpql.append(" and a.projeto.id = :id1");

		if (filtro.getComponentes() != null)
			if (filtro.getComponentes().length > 0) {
				jpql.append(" and a.componente in (:componente)");
			}

		if (filtro.getGestoes() != null)
			if (filtro.getGestoes().length > 0) {
				jpql.append(" and a.projeto.gestao.id in (:gestao)");
			}

		// jpql.append(" or a.projeto.id = :id2");
		// jpql.append(" or a.projeto.id = :id3");
		// jpql.append(" or a.projeto.id = :id4");
		// jpql.append(" or a.projeto.id = :id5");
		// jpql.append(" or a.projeto.id = :id6");
		// jpql.append(" or a.projeto.id = :id7");

		jpql.append(" order by a.projeto.id, a.codigo ");
		Query query = manager.createQuery(jpql.toString());

		if (filtro.getProjetoId() != null && filtro.getProjetoId() != new Long(0))
			query.setParameter("id1", filtro.getProjetoId()); // fflorest maués

		if (filtro.getComponentes() != null)
			if (filtro.getComponentes().length > 0) {
				List<Componente> list = new ArrayList<>();
				Componente[] mList = filtro.getComponentes();
				for (int i = 0; i < filtro.getComponentes().length; i++) {
					list.add(mList[i]);
				}

				query.setParameter("componente", list);
			}

		if (filtro.getGestoes() != null)
			if (filtro.getGestoes().length > 0) {
				List<Long> list = new ArrayList<>();
				Integer[] mList = filtro.getGestoes();
				for (int i = 0; i < filtro.getGestoes().length; i++) {
					list.add(mList[i].longValue());
				}

				query.setParameter("gestao", list);
			}

		// query.setParameter("id2", new Long(63)); //canumã renda
		// query.setParameter("id3", new Long(64)); //canumã social
		// query.setParameter("id4", new Long(75));//uatumã
		// query.setParameter("id5", new Long(57));// rio negro social
		// query.setParameter("id6", new Long(77)); // Rio negro
		// query.setParameter("id7", new Long(62)); // piagaçu purus
		return query.getResultList();
	}

	public ContaBancaria getContaByFornecedor(Long fornecedor) {
		String jpql = "from ContaBancaria cb where cb.fornecedor.id = :id";
		Query query = manager.createQuery(jpql);
		query.setParameter("id", fornecedor);
		return query.getResultList().size() > 0 ? (ContaBancaria) query.getResultList().get(0) : null;
	}

	public List<RelatorioContasAPagar> getPagamentosByConta(Filtro filtro) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder hql = new StringBuilder("select pl.id as id_pagamento, ");
		hql.append("pl.lancamento_id as codigo_lancamento, \n");
		// hql.append("pl.dataemissao as data_emissao, \n");
		// hql.append("pl.datapagamento as data_pagamento, \n");
		hql.append("to_char(pl.dataemissao,'dd/MM/yyyy') as data_emissao, \n");
		hql.append("to_char(pl.datapagamento,'dd/MM/yyyy') as data_pagamento, \n");

		hql.append("fp.nome, \n");
		hql.append("p.nome as projeto, \n");
		hql.append("a.codigo as acao, \n");
		hql.append("pl.numerodaparcela as numero_parcela, \n");
		hql.append("pl.quantidade_parcela as qtd_parcela,\n ");
		hql.append("pl.valor as valor,\n ");
		hql.append("(select cb.nome_conta from conta_bancaria cb where cb.id = pl.conta_id) as conta_pagador, \n");
		hql.append(
				"(select cb.nome_conta from conta_bancaria cb where cb.id = pl.contarecebedor_id) as recebedor_conta,\n ");
		hql.append("pl.stt as status,\n ");
		hql.append("pl.tipolancamento as ad_pc,\n ");
		hql.append("l.tipo  as tipo_lancamento, \n ");

		hql.append(" pl.conta_id as idcontapagador, \n");
		hql.append(" pl.contarecebedor_id as idcontarecebedor, \n");
		hql.append(" l.numerodocumento as numero_lancamento, ");

		// hql.append(" la.descricao ");
		hql.append(" case when la.descricao = '' or la.descricao is null then l.descricao  ");
		hql.append(" else la.descricao");
		hql.append(" end as descricao, ");
		hql.append(" l.categoriadespesa, ");
		hql.append(" l.nota_fiscal ");

		hql.append("from pagamento_lancamento pl join lancamento_acao la \n");
		hql.append("on pl.lancamentoacao_id = la.id \n");
		hql.append("join lancamento l on l.id = la.lancamento_id \n");
		hql.append("join acao a on la.acao_id = a.id \n");

		hql.append("left join localidade loc on l.localidade_id = loc.id \n");
		hql.append("join projeto p on a.projeto_id = p.id ");
		hql.append("join fonte_pagadora fp on fp.id = la.fontepagadora_id  \n");
		hql.append("where (pl.conta_id = :conta or pl.contarecebedor_id = :conta) \n");

		// hql.append(" and (a.componente = 'RENDA' or a.componente =
		// 'ASSOCIACAO') ");
		// hql.append(" and a.componente = 'SOCIAL' ");
		// hql.append(" and a.componente = 'ASSOCIACAO' ");
		// hql.append(" and a.componente = 'RENDA' ");

		hql.append("and pl.datapagamento between '" + sdf.format(filtro.getDataInicio()) + "' and '"
				+ sdf.format(filtro.getDataFinal()) + "' ");

		// hql.append("and pl.stt = 'PROVISIONADO' ");
		// hql.append("and pl.stt = 'EFETIVADO' ");

		hql.append("order by pl.datapagamento asc");
		
		Query query = manager.createNativeQuery(hql.toString());
		query.setParameter("conta", Integer.valueOf(filtro.getIdConta().toString()));

		List<Object[]> result = query.getResultList();
		List<RelatorioContasAPagar> relatorio = new ArrayList<>();

		RelatorioContasAPagar rel = new RelatorioContasAPagar();

		for (Object[] object : result) {

			rel = new RelatorioContasAPagar();
			// rel.setEmissao(object[0] != null ? object[0].toString() : "");
			rel.setDoc(object[17] != null ? object[17].toString() : "");
			rel.setEmissao(object[2] != null ? object[2].toString() : "");
			rel.setPagamento(object[3] != null ? object[3].toString() : "");
			rel.setFonte(object[4] != null ? object[4].toString() : "");
			rel.setProjeto(object[5] != null ? object[5].toString() : "");
			rel.setAcao(object[6] != null ? object[6].toString() : "");
			rel.setParcela(object[7].toString() + "/" + object[8].toString());
			rel.setDescricao(object[18] != null ? object[18].toString() : "");
			rel.setCategoriaDespesa(
					object[19] != null ? CategoriadeDespesa.valueOf(object[19].toString()).getNome() : "");
			rel.setNotaFiscal(object[20] != null ? object[20].toString() : "");

			// String tipo = object[13] != null ? object[13].toString();

			if (!object[13].toString().equals("ad") && !object[13].toString().equals("pc")) {
				if (Long.valueOf(object[15].toString()).intValue() == filtro.getIdConta().intValue()) {
					rel.setSaida(Double.valueOf(object[9].toString()));
				}

				if (Long.valueOf(object[16].toString()).intValue() == filtro.getIdConta().intValue()) {
					rel.setEntrada(Double.valueOf(object[9].toString()));
				}
			} else {
				if (object[13].toString().equals("ad")) {
					rel.setSaida(Double.valueOf(object[9].toString()));
				}
				if (object[13].toString().equals("pc")) {
					rel.setPrestacaoConta(Double.valueOf(object[9].toString()));
				}
			}

			rel.setPagador(object[10].toString());
			rel.setRecebedor(object[11].toString());
			rel.setStatus(object[12].toString());
			rel.setNumeroLancamento(object[1] != null ? object[1].toString() : "");

			relatorio.add(rel);

		}

		return relatorio;
	}

	public List<RelatorioContasAPagar> getPagamentosPorConta(Filtro filtro) {

		StringBuilder hql = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		hql.append("select ");
		hql.append("pl.id as id_pagamento_00,");
		hql.append("pl.lancamento_id as codigo_lancamento_01, ");
		hql.append("to_char(pl.dataemissao,'dd/MM/yyyy') as data_emissao_02, ");
		hql.append("to_char(pl.datapagamento,'dd/MM/yyyy') as data_pagamento_03, ");
		// hql.append("(select font.nome from fonte_pagadora font where id =
		// (select orc.fonte_id from orcamento orc where id = la.orcamento_id))
		// as fonte_04,");
		hql.append("CASE when la.rubricaorcamento_id is not null then ");

		hql.append("(select font.nome from fonte_pagadora font where id =");
		hql.append("(select orc.fonte_id from  orcamento orc where id = (select rub.orcamento_id from rubrica_orcamento rub where rub.id = la.rubricaorcamento_id))) else ");

		hql.append("(select font.nome from fonte_pagadora font where id = (select orc.fonte_id from  orcamento orc where id = ");
		hql.append("(select rub.orcamento_id from rubrica_orcamento rub where rub.id = ");
		hql.append("(select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id = la.projetorubrica_id)))) end as fonte_04,");

		// CASE la.rubricaorcamento_id when null then (select font.nome from
		// fonte_pagadora font where id =
		// (select orc.fonte_id from orcamento orc where id = (select
		// rub.orcamento_id from rubrica_orcamento rub where rub.id =
		// la.rubricaorcamento_id))) else
		// (select font.nome from fonte_pagadora font where id = (select
		// orc.fonte_id from orcamento orc where id =
		// (select rub.orcamento_id from rubrica_orcamento rub where rub.id =
		// (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.id =
		// la.projetorubrica_id))))end as fonte_04

		// hql.append("(select proj.nome from projeto proj where proj.id =
		// la.projeto_id) as projeto_05 ");
		hql.append("(select p.nome from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id)) as nome_projeto_05, ");

		hql.append("pl.numerodaparcela as numero_parcela_06, ");
		hql.append("pl.quantidade_parcela as qtd_parcela_07,");
		hql.append("pl.valor as valor_08,");
		hql.append("(select cb.nome_conta from conta_bancaria cb where cb.id = pl.conta_id) as conta_pagador_09, ");
		hql.append("(select cb.nome_conta from conta_bancaria cb where cb.id = pl.contarecebedor_id) as recebedor_conta_10,");
		hql.append("pl.stt as status_11,");
		hql.append("pl.tipolancamento as ad_pc_12,");
		hql.append("l.tipo  as tipo_lancamento_13, ");
		hql.append("pl.conta_id as idcontapagador_14, ");
		hql.append("pl.contarecebedor_id as idcontarecebedor_15, ");
		hql.append("l.numerodocumento as numero_lancamento_16, ");
		hql.append("case when la.descricao = '' or la.descricao is null then l.descricao else la.descricao end as descricao_17,  ");
		hql.append("l.categoriadespesa as categoria_18,  ");
		hql.append("l.nota_fiscal as nf_19 ");
		hql.append("from pagamento_lancamento pl join lancamento_acao la on pl.lancamentoacao_id = la.id ");
		hql.append("join lancamento l on l.id = la.lancamento_id ");
		hql.append("left join localidade loc on l.localidade_id = loc.id ");
		hql.append("where (pl.conta_id = :conta or pl.contarecebedor_id = :conta) and l.versionlancamento = 'MODE01' ");

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null){
			hql.append("and pl.datapagamento between '" + sdf.format(filtro.getDataInicio()) + "' and '"
					+ sdf.format(filtro.getDataFinal()) + "' ");
		}else if (filtro.getDataInicio() != null && filtro.getDataFinal() == null){
			hql.append("and pl.datapagamento = '" + sdf.format(filtro.getDataInicio()) + "'");
		}else if (filtro.getDataInicio() == null && filtro.getDataFinal() != null){
			hql.append("and pl.datapagamento = '" + sdf.format(filtro.getDataFinal()) + "'");
		}

		if (filtro.getOrcamento() != null) {
			hql.append(" and la.rubricaorcamento_id in (select rub.id from rubrica_orcamento rub where rub.orcamento_id = " + filtro.getOrcamento().getId() + ") ");
			hql.append(" or ");
			hql.append(" la.projetorubrica_id in (select pr.id from projeto_rubrica pr where pr.rubricaorcamento_id in (select rub.id from rubrica_orcamento rub where rub.orcamento_id = " + filtro.getOrcamento().getId() + ")) ");
		}

		if (filtro.getTipoConta() != null && !filtro.getTipoConta().equals("")){
			hql.append(" and l.tipo = '" + filtro.getTipoConta() + "'");
		}

		if (filtro.getTipoDespesa() != null){
			if (filtro.getTipoDespesa().intValue() == 0){
				hql.append(" and pl.conta_id = " + filtro.getIdConta());
			}else{
				hql.append(" and pl.conta_id <> " + filtro.getIdConta());
			}
		}

		Query query = manager.createNativeQuery(hql.toString());
		query.setParameter("conta", Integer.valueOf(filtro.getIdConta().toString()));

		List<Object[]> result = query.getResultList();
		List<RelatorioContasAPagar> relatorio = new ArrayList<>();

		RelatorioContasAPagar rel = new RelatorioContasAPagar();

		for (Object[] object : result) {

			rel = new RelatorioContasAPagar(object, filtro.getIdConta().intValue());
			relatorio.add(rel);

		}

		return relatorio;
	}

	public List<RelatorioContasAPagar> getOutrosPagamentosPorConta(Long idConta, Integer tipo) {

		StringBuilder hql = new StringBuilder();
		hql.append("select ");
		hql.append("pl.valor as valor_08,");
		hql.append("l.tipo  as tipo_lancamento_13, ");
		hql.append("pl.conta_id as idcontapagador_14 ");
		hql.append("from pagamento_lancamento pl join lancamento_acao la on pl.lancamentoacao_id = la.id ");
		hql.append("join lancamento l on l.id = la.lancamento_id ");
		hql.append("left join localidade loc on l.localidade_id = loc.id ");
		hql.append("where l.versionlancamento = 'MODE01' ");
		hql.append("and l.tipo <> 'rendimento' and l.tipo <> 'aplicacao_recurso' and l.tipo <> 'baixa_aplicacao'");

		if (tipo == 0){
			hql.append(" and pl.conta_id = :conta ");
		}else{
			hql.append(" and pl.contarecebedor_id = :conta ");
		}

		Query query = manager.createNativeQuery(hql.toString());
		query.setParameter("conta", idConta);

		List<Object[]> result = query.getResultList();
		List<RelatorioContasAPagar> relatorio = new ArrayList<>();

		RelatorioContasAPagar rel = new RelatorioContasAPagar();

		for (Object[] object : result) {

			rel = new RelatorioContasAPagar(object, idConta.intValue());
			relatorio.add(rel);

		}

		return relatorio;
	}

	public List<RelatorioContasAPagar> getPagamentosByContaMODE01(Filtro filtro) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder hql = new StringBuilder("select ");
		hql.append("to_char(pl.datapagamento,'dd/MM/yyyy') as data_pagamento_00,   \n");
		hql.append("to_char(pl.dataemissao,'dd/MM/yyyy') as data_emissao_01,  \n");
		hql.append("l.nota_fiscal as nota_fiscal_02, \n");
		hql.append("l.numerodocumento as numero_documento_03, \n");
		// hql.append("(select proj.nome from projeto proj where proj.id =
		// la.projeto_id) as projeto,");
		hql.append("l.categoriadespesa as categoria_04, \n");
		hql.append("l.id as numero_lancamento_05, \n");
		hql.append("(select cb.nome_conta from conta_bancaria cb where cb.id = pl.conta_id) as conta_pagador_06, \n ");
		hql.append(
				"(select cb.nome_conta from conta_bancaria cb where cb.id = pl.contarecebedor_id) as recebedor_conta_07, \n");
		hql.append("case when la.descricao = '' or la.descricao is null then l.descricao \n ");
		hql.append(" else la.descricao end as descricao_08, \n");

		// hql.append("CASE when la.rubricaorcamento_id is null then (select f.nome from
		// fonte_pagadora f where ");
		// hql.append("f.id = (select orc.fonte_id from orcamento orc where ");
		// hql.append("orc.id = (select rubric.orcamento_id from rubrica_orcamento
		// rubric where ");
		// hql.append("rubric.id = (select proj_r.rubricaorcamento_id from
		// projeto_rubrica proj_r where ");
		// hql.append(
		// "proj_r.id = (select lla.projetorubrica_id from lancamento_acao lla where
		// lla.id = pl.lancamentoacao_id))))) ");
		// hql.append(" else ");
		// hql.append("(select f.nome from fonte_pagadora f where f.id = (select
		// orc.fonte_id from orcamento orc where ");
		// hql.append(" orc.id = (select rubric.orcamento_id from rubrica_orcamento
		// rubric where ");
		// hql.append("rubric.id = (select lla.rubricaorcamento_id from ");
		// hql.append("lancamento_acao lla where lla.id = pl.lancamentoacao_id)))) end
		// as fonte_09, ");

		hql.append("CASE  when la.projetorubrica_id is not null then \n");
		hql.append("(select f.nome from fonte_pagadora f where f.id = \n");
		hql.append(
				"(select orc.fonte_id from orcamento orc where orc.id = (select rubric.orcamento_id from rubrica_orcamento rubric where rubric.id = \n");
		hql.append(
				"(select proj_r.rubricaorcamento_id from projeto_rubrica proj_r  where proj_r.id = (select lla.projetorubrica_id from lancamento_acao lla where  \n");
		hql.append(" lla.id = pl.lancamentoacao_id)))))  \n");
		hql.append(" when la.rubricaorcamento_id is not null then \n");
		hql.append(
				" (select f.nome from fonte_pagadora f where f.id = (select orc.fonte_id from orcamento orc where  orc.id = \n");
		hql.append(
				" (select rubric.orcamento_id from rubrica_orcamento rubric where rubric.id = (select lla.rubricaorcamento_id from lancamento_acao lla where \n");
		hql.append(" lla.id = pl.lancamentoacao_id)))) \n");
		hql.append(
				" else (select fp.nome from fonte_pagadora fp where fp.id = la.fontepagadora_id) end as fonte_09, \n");

		hql.append(" CASE when la.projetorubrica_id is not null \n");
		hql.append(" then \n ");
		hql.append(
				" (select proj.nome from projeto proj where proj.id = (select projr.projeto_id from projeto_rubrica projr where projr.id = la.projetorubrica_id)) \n");
		hql.append(" else \n ");
		hql.append(
				" (select pp.nome from projeto pp where pp.id = (select ac.projeto_id from acao ac where ac.id = la.acao_id)) \n");
		hql.append(" end as projeto_10, \n");

		// hql.append(
		// "(select proj.nome from projeto proj where proj.id = (select projr.projeto_id
		// from projeto_rubrica projr where projr.id = la.projetorubrica_id)) as
		// projeto_10,");

		hql.append(
				" pl.stt as status_11, pl.valor as valor_12, pl.id as id_pagamento_13,  pl.lancamento_id as codigo_lancamento_14, \n");
		hql.append("pl.numerodaparcela || '/' || pl.quantidade_parcela as parcela_15, \n");
		hql.append(
				" l.tipo  as tipo_lancamento_16, pl.conta_id as idcontapagador_17,  pl.contarecebedor_id as idcontarecebedor_18, \n");
		hql.append(" (select ac.codigo from acao ac where ac.id = la.acao_id) as acao_19, \n");
		hql.append("l.tipo as tipo_20, \n");

		hql.append("CASE  when la.projetorubrica_id is not null then \n");
		hql.append(
				"(select rub.nome from rubrica rub where rub.id = (select rubric.rubrica_id from rubrica_orcamento rubric where rubric.id = \n");
		hql.append(
				"(select proj_r.rubricaorcamento_id from projeto_rubrica proj_r  where proj_r.id = (select lla.projetorubrica_id from lancamento_acao lla where  \n ");
		hql.append("lla.id = pl.lancamentoacao_id))))  else \n ");
		hql.append(
				"(select rub.nome from rubrica rub where  rub.id =  (select rubric.rubrica_id from rubrica_orcamento rubric where rubric.id = \n");
		hql.append(
				"(select lla.rubricaorcamento_id from lancamento_acao lla where  lla.id = pl.lancamentoacao_id)))  end as linha_orcamentaria_21, \n");

		hql.append("CASE  when la.projetorubrica_id is not null then \n");
		hql.append(
				"(select comp.nome from componente_class comp where comp.id = (select rubric.componente_id from rubrica_orcamento rubric where rubric.id = \n");
		hql.append(
				"(select proj_r.rubricaorcamento_id from projeto_rubrica proj_r  where proj_r.id = (select lla.projetorubrica_id from lancamento_acao lla where  \n ");
		hql.append("lla.id = pl.lancamentoacao_id))))  else \n ");
		hql.append(
				"(select comp.nome from componente_class comp where comp.id =  (select rubric.componente_id from rubrica_orcamento rubric where rubric.id = \n");
		hql.append(
				"(select lla.rubricaorcamento_id from lancamento_acao lla where  lla.id = pl.lancamentoacao_id)))  end as componente_22, \n");

		hql.append(" CASE  when la.projetorubrica_id is not null then \n");
		hql.append(
				"(select sub.nome from sub_componente sub where sub.id = (select rubric.subcomponente_id from rubrica_orcamento rubric where rubric.id = \n");
		hql.append(
				"(select proj_r.rubricaorcamento_id from projeto_rubrica proj_r  where proj_r.id = (select lla.projetorubrica_id from lancamento_acao lla where \n");
		hql.append(" lla.id = pl.lancamentoacao_id))))  else  \n");
		hql.append(
				"(select sub.nome from sub_componente  sub where sub.id =  (select rubric.subcomponente_id from rubrica_orcamento rubric where rubric.id = \n");
		hql.append(
				"(select lla.rubricaorcamento_id from lancamento_acao lla where  lla.id = pl.lancamentoacao_id)))  end as sub_componente_23, \n");
		hql.append("l.tipo_documento_fiscal as tipo_documento_24,  \n");

		hql.append("CASE ");
		hql.append("WHEN (l.tipo = 'SolicitacaoPagamento' and l.tipolancamento != 'ad') THEN 'SP' ");
		hql.append("WHEN (l.tipo = 'SolicitacaoPagamento' and l.tipolancamento = 'ad') THEN 'SA' ");
		hql.append("WHEN (l.tipo = 'Diaria') THEN 'SD' ");
		hql.append("WHEN (l.tipo = 'custo_pessoal') THEN 'CP' ");
		hql.append("WHEN (l.tipo = 'pedido') THEN 'PC' ");
		hql.append("WHEN (l.tipo = 'baixa_aplicacao') THEN 'BA' ");
		hql.append("WHEN (l.tipo = 'aplicacao_recurso') THEN 'AR' ");
		hql.append("WHEN (l.tipo = 'doacao_efetiva') THEN 'DE' ");
		hql.append("WHEN (l.tipo = 'tarifa_bancaria') THEN 'TF' ");
		hql.append("WHEN (l.tipo = 'lanc_av' or l.tipo = 'lancamento_diversos') THEN 'LA'  ");
		hql.append(" END as tipo_25, ");

		hql.append("CASE  when la.projetorubrica_id is not null then \n"
				+ "(select orc.titulo from orcamento orc where orc.id = (select rubric.orcamento_id from rubrica_orcamento rubric where rubric.id = \n"
				+ "(select proj_r.rubricaorcamento_id from projeto_rubrica proj_r  where proj_r.id = (select lla.projetorubrica_id from lancamento_acao lla where \n"
				+ " lla.id = pl.lancamentoacao_id))))  else  \n"
				+ "(select orc.titulo from orcamento orc  where orc.id =  (select rubric.orcamento_id from rubrica_orcamento rubric where rubric.id = \n"
				+ "(select lla.rubricaorcamento_id from lancamento_acao lla where  lla.id = pl.lancamentoacao_id)))  end as orcamento_26, ");
		hql.append("cf.nome as categoria_27 ");
		hql.append("from pagamento_lancamento pl join lancamento_acao la \n");
		hql.append("on pl.lancamentoacao_id = la.id \n");
		hql.append("join lancamento l on l.id = la.lancamento_id \n");
		hql.append("join categoria_financeira cf on cf.id = l.categoriafinanceira_id \n");
		hql.append("left join localidade loc on l.localidade_id = loc.id \n");
		hql.append("where (pl.conta_id = :conta or pl.contarecebedor_id = :conta) and \n");
		hql.append(" CASE  when l.versionlancamento = 'MODE01' then \n");
		hql.append("la.status = 0   else 1 = 1 end \n");
		hql.append(" and l.statuscompra = 'CONCLUIDO' \n");
		// hql.append(" and l.versionlancamento = 'MODE01' ");

		hql.append("and pl.datapagamento between '" + sdf.format(filtro.getDataInicio()) + "' and '"
				+ sdf.format(filtro.getDataFinal()) + "' ");
		hql.append("order by pl.datapagamento asc \n");
		
		Query query = manager.createNativeQuery(hql.toString());
		query.setParameter("conta", Integer.valueOf(filtro.getIdConta().toString()));

		List<Object[]> result = query.getResultList();
		List<RelatorioContasAPagar> relatorio = new ArrayList<>();

		RelatorioContasAPagar rel = new RelatorioContasAPagar();

		for (Object[] object : result) {

			rel = new RelatorioContasAPagar();
			rel.setPagamento(object[0] != null ? object[0].toString() : "");
			rel.setEmissao(object[1] != null ? object[1].toString() : "");
			rel.setNotaFiscal(object[2] != null ? object[2].toString() : "");
			rel.setDoc(object[3] != null ? object[3].toString() : "");
			//rel.setCategoriaDespesa(object[18] != null ?
			//CategoriadeDespesa.valueOf(object[18].toString()).getNome() : "");
			rel.setCategoriaDespesa(object[27].toString());
			rel.setNumeroLancamento(object[5] != null ? object[5].toString() : "");
			rel.setPagador(object[6].toString());
			rel.setRecebedor(object[7].toString());
			rel.setDescricao(object[8] != null ? object[8].toString() : "");
			rel.setFonte(object[9] != null ? object[9].toString() : "");
			rel.setProjeto(object[10] != null ? object[10].toString() : "");
			rel.setDoacao(object[26] != null ? object[26].toString() : "");
			rel.setStatus(object[11].toString());
			rel.setParcela(object[15].toString());
			rel.setAcao(object[19] != null ? object[19].toString() : "");
			rel.setTipo(Util.getNullValue(object[25], ""));
			rel.setRubrica(Util.getNullValue(object[21], ""));
			rel.setComponente(Util.getNullValue(object[22], ""));
			rel.setSubcomponente(Util.getNullValue(object[23], ""));
			rel.setTipoDocumento(Util.getNullValue(object[24], ""));

			if (Long.valueOf(object[17].toString()).intValue() == filtro.getIdConta().intValue()) {
				rel.setSaida(Double.valueOf(object[12].toString()));
			} else {
				rel.setEntrada(Double.valueOf(object[12].toString()));
			}

			relatorio.add(rel);

		}

		return relatorio;
	}

	public List<RelatorioContasAPagar> getPagamentosByContaMODE01Relatorio(Filtro filtro) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder hql = new StringBuilder("select ");
		hql.append("to_char(pl.datapagamento,'dd/MM/yyyy') as data_pagamento_00,   \n");
		hql.append("to_char(pl.dataemissao,'dd/MM/yyyy') as data_emissao_01,  \n");
		hql.append("l.nota_fiscal as nota_fiscal_02, \n");
		hql.append("l.numerodocumento as numero_documento_03, \n");
		hql.append("l.categoriadespesa as categoria_04, \n");
		hql.append("l.id as numero_lancamento_05, \n");
		hql.append("(select cb.nome_conta from conta_bancaria cb where cb.id = pl.conta_id) as conta_pagador_06, \n ");
		hql.append("(select cb.nome_conta from conta_bancaria cb where cb.id = pl.contarecebedor_id) as recebedor_conta_07, \n");
		hql.append("case when la.descricao = '' or la.descricao is null then l.descricao \n ");
		hql.append(" else la.descricao end as descricao_08, \n");
		hql.append("CASE  when la.projetorubrica_id is not null then \n");
		hql.append("(select f.nome from fonte_pagadora f where f.id = \n");
		hql.append("(select orc.fonte_id from orcamento orc where orc.id = (select rubric.orcamento_id from rubrica_orcamento rubric where rubric.id = \n");
		hql.append("(select proj_r.rubricaorcamento_id from projeto_rubrica proj_r  where proj_r.id = (select lla.projetorubrica_id from lancamento_acao lla where  \n");
		hql.append(" lla.id = pl.lancamentoacao_id)))))  \n");
		hql.append(" when la.rubricaorcamento_id is not null then \n");
		hql.append(" (select f.nome from fonte_pagadora f where f.id = (select orc.fonte_id from orcamento orc where  orc.id = \n");
		hql.append(" (select rubric.orcamento_id from rubrica_orcamento rubric where rubric.id = (select lla.rubricaorcamento_id from lancamento_acao lla where \n");
		hql.append(" lla.id = pl.lancamentoacao_id)))) \n");
		hql.append(" else (select fp.nome from fonte_pagadora fp where fp.id = la.fontepagadora_id) end as fonte_09, \n");
		hql.append(" CASE when la.projetorubrica_id is not null \n");
		hql.append(" then \n ");
		hql.append(" (select proj.nome from projeto proj where proj.id = (select projr.projeto_id from projeto_rubrica projr where projr.id = la.projetorubrica_id)) \n");
		hql.append(" else \n ");
		hql.append(" (select pp.nome from projeto pp where pp.id = (select ac.projeto_id from acao ac where ac.id = la.acao_id)) \n");
		hql.append(" end as projeto_10, \n");
		hql.append(" pl.stt as status_11, pl.valor as valor_12, pl.id as id_pagamento_13,  pl.lancamento_id as codigo_lancamento_14, \n");
		hql.append("pl.numerodaparcela || '/' || pl.quantidade_parcela as parcela_15, \n");
		hql.append(" l.tipo  as tipo_lancamento_16, pl.conta_id as idcontapagador_17,  pl.contarecebedor_id as idcontarecebedor_18, \n");
		hql.append(" (select ac.codigo from acao ac where ac.id = la.acao_id) as acao_19, \n");
		hql.append("l.tipo as tipo_20, \n");
		hql.append("CASE  when la.projetorubrica_id is not null then \n");
		hql.append("(select rub.nome from rubrica rub where rub.id = (select rubric.rubrica_id from rubrica_orcamento rubric where rubric.id = \n");
		hql.append("(select proj_r.rubricaorcamento_id from projeto_rubrica proj_r  where proj_r.id = (select lla.projetorubrica_id from lancamento_acao lla where  \n ");
		hql.append("lla.id = pl.lancamentoacao_id))))  else \n ");
		hql.append("(select rub.nome from rubrica rub where  rub.id =  (select rubric.rubrica_id from rubrica_orcamento rubric where rubric.id = \n");
		hql.append("(select lla.rubricaorcamento_id from lancamento_acao lla where  lla.id = pl.lancamentoacao_id)))  end as linha_orcamentaria_21, \n");
		hql.append("CASE  when la.projetorubrica_id is not null then \n");
		hql.append("(select comp.nome from componente_class comp where comp.id = (select rubric.componente_id from rubrica_orcamento rubric where rubric.id = \n");
		hql.append("(select proj_r.rubricaorcamento_id from projeto_rubrica proj_r  where proj_r.id = (select lla.projetorubrica_id from lancamento_acao lla where  \n ");
		hql.append("lla.id = pl.lancamentoacao_id))))  else \n ");
		hql.append("(select comp.nome from componente_class comp where comp.id =  (select rubric.componente_id from rubrica_orcamento rubric where rubric.id = \n");
		hql.append("(select lla.rubricaorcamento_id from lancamento_acao lla where  lla.id = pl.lancamentoacao_id)))  end as componente_22, \n");
		hql.append(" CASE  when la.projetorubrica_id is not null then \n");
		hql.append("(select sub.nome from sub_componente sub where sub.id = (select rubric.subcomponente_id from rubrica_orcamento rubric where rubric.id = \n");
		hql.append("(select proj_r.rubricaorcamento_id from projeto_rubrica proj_r  where proj_r.id = (select lla.projetorubrica_id from lancamento_acao lla where \n");
		hql.append(" lla.id = pl.lancamentoacao_id))))  else  \n");
		hql.append("(select sub.nome from sub_componente  sub where sub.id =  (select rubric.subcomponente_id from rubrica_orcamento rubric where rubric.id = \n");
		hql.append("(select lla.rubricaorcamento_id from lancamento_acao lla where  lla.id = pl.lancamentoacao_id)))  end as sub_componente_23, \n");
		hql.append("l.tipo_documento_fiscal as tipo_documento_24,  \n");
		hql.append("CASE ");
		hql.append("WHEN (l.tipo = 'SolicitacaoPagamento' and l.tipolancamento != 'ad') THEN 'SP' ");
		hql.append("WHEN (l.tipo = 'SolicitacaoPagamento' and l.tipolancamento = 'ad') THEN 'SA' ");
		hql.append("WHEN (l.tipo = 'Diaria') THEN 'SD' ");
		hql.append("WHEN (l.tipo = 'custo_pessoal') THEN 'CP' ");
		hql.append("WHEN (l.tipo = 'pedido') THEN 'PC' ");
		hql.append("WHEN (l.tipo = 'baixa_aplicacao') THEN 'BA' ");
		hql.append("WHEN (l.tipo = 'aplicacao_recurso') THEN 'AR' ");
		hql.append("WHEN (l.tipo = 'doacao_efetiva') THEN 'DE' ");
		hql.append("WHEN (l.tipo = 'tarifa_bancaria') THEN 'TF' ");
		hql.append("WHEN (l.tipo = 'lanc_av' or l.tipo = 'lancamento_diversos') THEN 'LA'  ");
		hql.append(" END as tipo_25, ");
		hql.append("CASE  when la.projetorubrica_id is not null then \n"
				+ "(select orc.titulo from orcamento orc where orc.id = (select rubric.orcamento_id from rubrica_orcamento rubric where rubric.id = \n"
				+ "(select proj_r.rubricaorcamento_id from projeto_rubrica proj_r  where proj_r.id = (select lla.projetorubrica_id from lancamento_acao lla where \n"
				+ " lla.id = pl.lancamentoacao_id))))  else  \n"
				+ "(select orc.titulo from orcamento orc  where orc.id =  (select rubric.orcamento_id from rubrica_orcamento rubric where rubric.id = \n"
				+ "(select lla.rubricaorcamento_id from lancamento_acao lla where  lla.id = pl.lancamentoacao_id)))  end as orcamento_26, ");
		hql.append("case pl.tipoparcelamento when 'PARCELA_UNICA' then 'Parcela única/A vista'\n");
		hql.append("when 'SEMANAL' then 'Semanal'\n");
		hql.append("when 'QUINZENAL' then 'Quinzenal'\n");
		hql.append("when 'MENSAL' then 'Mensal'\n");
		hql.append("when 'BIMESTRE' then 'Bimestre'\n");
		hql.append("when 'TRIMESTRE' then 'Trimestre'\n");
		hql.append("when 'SEMESTRE' then 'Semestre'\n");
		hql.append("end as condicao_pagamento_27, \n");
		hql.append("(select nome from gestao where id = l.gestao_id) as gestao_28,\n");
		hql.append("CASE when la.projetorubrica_id is not null\n");
		hql.append("then\n");
		hql.append("(select proj.programa from projeto proj where proj.id = (select projr.projeto_id from projeto_rubrica projr where projr.id = la.projetorubrica_id)) \n");
		hql.append("else\n");
		hql.append("(select pp.programa from projeto pp where pp.id = (select ac.projeto_id from acao ac where ac.id = la.acao_id))\n");
		hql.append("end as programa_29,\n");
		hql.append("case when pl.conta_id = :conta then 'Despesa' else 'Receita' end as tipotransferencia_30,\n");
		hql.append("l.compra_id as sc_31,\n");
		hql.append("l.sv_id as sv_32,\n");
		hql.append("(select case trim(tipo) when 'fisica' then cpf when 'juridica' then cnpj end as cpfcnpj from fornecedor\n");
		hql.append("where id = (select fornecedor_id from conta_bancaria where id = :conta)) as cpfcnpj_33, \n");
		hql.append("(select case trim(fd.tipo) when 'fisica' then fd.cpf else fd.cnpj end as cpf_cnpj "
				+ "from conta_bancaria cb join fornecedor fd on fd.id = cb.fornecedor_id"
				+ " where cb.id = l.contarecebedor_id) as cpf_cnpj_34, \n");
		hql.append("( select cf.nome from categoria_financeira cf where cf.id = l.categoriafinanceira_id ) as categoria_financeira_35, \n");
		hql.append("( select fd.razao_social from conta_bancaria cb "
				+ "join fornecedor fd on fd.id = cb.fornecedor_id where cb.id = l.contarecebedor_id ) as razao_social_36 \n");
		hql.append("from pagamento_lancamento pl join lancamento_acao la \n");
		hql.append("on pl.lancamentoacao_id = la.id \n");
		hql.append("join lancamento l on l.id = la.lancamento_id \n");
		hql.append("left join localidade loc on l.localidade_id = loc.id \n");
		hql.append("where (pl.conta_id = :conta or pl.contarecebedor_id = :conta)");
		hql.append("and pl.datapagamento between '" + sdf.format(filtro.getDataInicio()) + "' and '" + sdf.format(filtro.getDataFinal()) + "' ");
		hql.append("order by pl.datapagamento asc \n");
	
		Query query = manager.createNativeQuery(hql.toString());
		query.setParameter("conta", Integer.valueOf(filtro.getIdConta().toString()));

		List<Object[]> result = query.getResultList();
		List<RelatorioContasAPagar> relatorio = new ArrayList<>();

		RelatorioContasAPagar rel = new RelatorioContasAPagar();

		System.out.println(System.getProperty("os.name").equals("Linux"));
		
		
		for (Object[] object : result) {

			rel = new RelatorioContasAPagar();
			rel.setPagamento(object[0] != null ? object[0].toString() : "");
			rel.setEmissao(object[1] != null ? object[1].toString() : "");
			rel.setNotaFiscal(object[2] != null ? object[2].toString() : "");
			rel.setDoc(object[3] != null ? object[3].toString() : "");
			rel.setCategoriaDespesa(object[35] != null ? 
					object[35].toString() : "");
			rel.setNumeroLancamento(object[5] != null ? object[5].toString() : "");
			rel.setPagador(object[6].toString());
			rel.setRecebedor(object[7].toString());
			rel.setDescricao(object[8] != null ? object[8].toString() : "");
			rel.setFonte(object[9] != null ? object[9].toString() : "");
			rel.setProjeto(object[10] != null ? object[10].toString() : "");
			rel.setDoacao(object[26] != null ? object[26].toString() : "");
			rel.setStatus(object[11].toString());
			rel.setParcela(object[15].toString());
			rel.setAcao(object[19] != null ? object[19].toString() : "");
			rel.setTipo(Util.getNullValue(object[25], ""));
			rel.setRubrica(Util.getNullValue(object[21], ""));
			rel.setComponente(Util.getNullValue(object[22], ""));
			rel.setSubcomponente(Util.getNullValue(object[23], ""));
			rel.setTipoDocumento(Util.getNullValue(object[24], ""));

			if (Long.valueOf(object[17].toString()).intValue() == filtro.getIdConta().intValue()) {
				rel.setSaida(Double.valueOf(object[12].toString()));
			} else {
				rel.setEntrada(Double.valueOf(object[12].toString()));
			}
			rel.setCondicaoPagamento(Util.getNullValue(object[27], ""));
			rel.setGestao(Util.getNullValue(object[28], ""));
			rel.setPrograma(Util.getNullValue(object[29], ""));
			rel.setDespesaReceita(Util.getNullValue(object[30], ""));
			rel.setSc(Util.getNullValue(object[31], ""));
			rel.setSv(Util.getNullValue(object[32], ""));
			rel.setCpfcnpj(Util.getNullValue(object[34], ""));
			rel.setRazaoSocial(object[36] != null ? 
					object[36].toString() : "");
			relatorio.add(rel);

		}

		return relatorio;
	}

	// TODO: novo método para alcance da nova formatação de dados executados.
	public List<RelatorioFinanceiro> buscarPagamentos(Filtro filtro) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder hql = new StringBuilder("select pl.id as id_pagamento, ");
		hql.append("pl.lancamento_id as codigo_lancamento, \n");
		// hql.append("pl.dataemissao as data_emissao, \n");
		// hql.append("pl.datapagamento as data_pagamento, \n");
		hql.append("to_char(pl.dataemissao,'dd/MM/yyyy') as data_emissao, \n");
		hql.append("to_char(pl.datapagamento,'dd/MM/yyyy') as data_pagamento, \n");

		hql.append("fp.nome, \n");
		hql.append("p.nome as projeto, \n");
		hql.append("a.codigo as acao, \n");
		hql.append("pl.numerodaparcela as numero_parcela, \n");
		hql.append("pl.quantidade_parcela as qtd_parcela,\n ");
		hql.append("pl.valor as valor,\n ");
		hql.append("(select cb.nome_conta from conta_bancaria cb where cb.id = pl.conta_id) as conta_pagador, \n");
		hql.append(
				"(select cb.nome_conta from conta_bancaria cb where cb.id = pl.contarecebedor_id) as recebedor_conta,\n ");
		hql.append("pl.stt as status,\n ");
		hql.append("pl.tipolancamento as ad_pc,\n ");
		hql.append("l.tipo  as tipo_lancamento, \n ");

		hql.append(" pl.conta_id as idcontapagador, \n");
		hql.append(" pl.contarecebedor_id as idcontarecebedor, \n");
		hql.append(" l.numerodocumento as numero_lancamento, ");

		// hql.append(" la.descricao ");
		hql.append(" case when la.descricao = '' or la.descricao is null then l.descricao  ");
		hql.append(" else la.descricao");
		hql.append(" end as descricao, ");
		hql.append(" l.categoriadespesa, ");
		hql.append(" l.nota_fiscal, ");
		hql.append(" (select cb.tipo from conta_bancaria cb where cb.id = pl.conta_id) as tipo_conta_pagador, ");
		hql.append(
				" (select cb.tipo from conta_bancaria cb where cb.id = pl.contarecebedor_id) as tipo_conta_recebedor, ");
		hql.append(" l.statusadiantamento");
		hql.append(",l.idadiantamento");
		hql.append(",(select ll.statusadiantamento from lancamento ll where ll.id = l.idadiantamento) status_ad ");
		hql.append(",(select r.nome from rubrica r where r.id = l.rubrica_id) as rubrica ");

		hql.append(" from pagamento_lancamento pl join lancamento_acao la \n");
		hql.append("on pl.lancamentoacao_id = la.id \n");
		hql.append("join lancamento l on l.id = la.lancamento_id \n");
		hql.append("join acao a on la.acao_id = a.id \n");

		hql.append("left join localidade loc on l.localidade_id = loc.id \n");
		hql.append("join projeto p on a.projeto_id = p.id ");
		hql.append("join fonte_pagadora fp on fp.id = la.fontepagadora_id  \n");
		hql.append("where 1 = 1 and l.statuscompra = 'CONCLUIDO' ");

		if (filtro.getContas().length > 0) {
			hql.append(" and (pl.conta_id in (:contas) or pl.contarecebedor_id in (:contas)) \n");
		}

		if (filtro.getFontes().length > 0) {
			hql.append(" and (la.fontepagadora_id in (:fontes)) \n");
		}

		if (filtro.getProjetoId() != null && filtro.getProjetoId().longValue() != 0l) {
			hql.append(" and (p.id = :id_projeto) \n");
		}

		if (filtro.getAcoes().length > 0) {
			hql.append(" and (la.acao_id in (:acoes)) \n");
		}

		hql.append("and pl.datapagamento between '" + sdf.format(filtro.getDataInicio()) + "' and '"
				+ sdf.format(filtro.getDataFinal()) + "' ");

		hql.append("order by pl.datapagamento asc");

		Query query = manager.createNativeQuery(hql.toString());

		if (filtro.getContas().length > 0) {
			List<Integer> list = new ArrayList<>();
			Integer[] mList = filtro.getContas();

			for (int i = 0; i < filtro.getContas().length; i++) {
				list.add(mList[i]);
			}

			query.setParameter("contas", list);
		}

		if (filtro.getFontes().length > 0) {
			List<Integer> list = new ArrayList<>();
			Integer[] mList = filtro.getFontes();

			for (int i = 0; i < filtro.getFontes().length; i++) {
				list.add(mList[i]);
			}

			query.setParameter("fontes", list);

		}

		if (filtro.getProjetoId() != null && filtro.getProjetoId().longValue() != 0l) {
			query.setParameter("id_projeto", filtro.getProjetoId());
		}

		if (filtro.getAcoes().length > 0) {
			List<Integer> list = new ArrayList<>();
			Integer[] mList = filtro.getAcoes();

			for (int i = 0; i < filtro.getAcoes().length; i++) {
				list.add(mList[i]);
			}

			query.setParameter("acoes", list);

		}

		List<Object[]> result = query.getResultList();
		List<RelatorioFinanceiro> relatorio = new ArrayList<>();
		RelatorioFinanceiro rel = new RelatorioFinanceiro();

		for (Object[] object : result) {

			String tipoPagador = object[21] != null ? object[21].toString() : "";
			String tipoRecebedor = object[22] != null ? object[22].toString() : "";

			String tipoLancamento = object[13] != null ? object[13].toString() : "";
			String statusAdiantamento = object[23] != null ? object[23].toString() : "";
			Long idAdiantamento = object[24] != null ? new Long(object[24].toString()) : null;
			String statusAdiantamentoDePrestacao = object[25] != null ? object[25].toString() : "";

			if (tipoLancamento.equals("ad")) {
				if (statusAdiantamento.equals("VALIDADO")) {
					rel = new RelatorioFinanceiro(object, "", "");
					relatorio.add(rel);
					continue;
				} else {
					rel = new RelatorioFinanceiro(object);
					relatorio.add(rel);
					continue;
				}
			}

			if (idAdiantamento != null) {
				if (statusAdiantamentoDePrestacao.equals("VALIDADO")) {
					if (tipoLancamento.equals("dev") || tipoLancamento.equals("reenb")) {
						rel = new RelatorioFinanceiro(object, "", "");
						relatorio.add(rel);
						continue;
					} else {
						rel = new RelatorioFinanceiro(object);
						relatorio.add(rel);
						continue;
					}

				} else {
					continue;
				}

			}

			if ((tipoPagador.equals("CB") || tipoPagador.equals("CA")) && !tipoRecebedor.equals("CB")) {
				rel = new RelatorioFinanceiro(object);
				relatorio.add(rel);
			} else if (!tipoPagador.equals("CB") && tipoRecebedor.equals("CB")) {
				rel = new RelatorioFinanceiro(object, "");
				relatorio.add(rel);
			} else if (tipoPagador.equals("CB") && tipoRecebedor.equals("CB")) {
				rel = new RelatorioFinanceiro(object);
				relatorio.add(rel);
				rel = new RelatorioFinanceiro(object, "");
				relatorio.add(rel);
			}

		}

		return relatorio;
	}

	// Usado para outros relatórios que ainda não tem interface com o usuário
	public List<RelatorioContasAPagar> getPagamentosByContaParaOutrosFiltros(Filtro filtro) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder hql = new StringBuilder("select pl.id as id_pagamento, ");
		hql.append("pl.lancamento_id as codigo_lancamento, \n");
		// hql.append("pl.dataemissao as data_emissao, \n");
		// hql.append("pl.datapagamento as data_pagamento, \n");
		hql.append("to_char(pl.dataemissao,'dd/MM/yyyy') as data_emissao, \n");
		hql.append("to_char(pl.datapagamento,'dd/MM/yyyy') as data_pagamento, \n");

		hql.append("fp.nome, \n");
		hql.append("p.nome as projeto, \n");
		hql.append("a.codigo as acao, \n");
		hql.append("pl.numerodaparcela as numero_parcela, \n");
		hql.append("pl.quantidade_parcela as qtd_parcela,\n ");
		hql.append("pl.valor as valor,\n ");
		hql.append("(select cb.nome_conta from conta_bancaria cb where cb.id = pl.conta_id) as conta_pagador, \n");
		hql.append(
				"(select cb.nome_conta from conta_bancaria cb where cb.id = pl.contarecebedor_id) as recebedor_conta,\n ");
		hql.append("pl.stt as status,\n ");
		hql.append("pl.tipolancamento as ad_pc,\n ");
		hql.append("l.tipo  as tipo_lancamento, \n ");

		hql.append(" pl.conta_id as idcontapagador, \n");
		hql.append(" pl.contarecebedor_id as idcontarecebedor, \n");
		hql.append(" l.numerodocumento as numero_lancamento, ");

		// hql.append(" la.descricao ");
		hql.append(" case when la.descricao = '' or la.descricao is null then l.descricao  ");
		hql.append(" else la.descricao");
		hql.append(" end as descricao, ");
		hql.append(" l.categoriadespesa, ");
		hql.append(" l.nota_fiscal ");

		hql.append("from pagamento_lancamento pl join lancamento_acao la \n");
		hql.append("on pl.lancamentoacao_id = la.id \n");
		hql.append("join lancamento l on l.id = la.lancamento_id \n");
		hql.append("join acao a on la.acao_id = a.id \n");

		hql.append("left join localidade loc on l.localidade_id = loc.id \n");
		hql.append("join projeto p on a.projeto_id = p.id ");
		hql.append("join fonte_pagadora fp on fp.id = la.fontepagadora_id  \n");
		hql.append("where (pl.conta_id = :conta or pl.contarecebedor_id = :conta) \n");

		// hql.append(" and (a.componente = 'RENDA' or a.componente =
		// 'ASSOCIACAO') ");
		// hql.append(" and a.componente = 'SOCIAL' ");
		// hql.append(" and a.componente = 'ASSOCIACAO' ");
		// hql.append(" and a.componente = 'RENDA' ");

		hql.append("and pl.datapagamento between '" + sdf.format(filtro.getDataInicio()) + "' and '"
				+ sdf.format(filtro.getDataFinal()) + "' ");

		// hql.append(" and la.despesareceita = 'RECEITA' ");

		// hql.append("and l.categoriadespesa = 'DOACAO' ");

		// hql.append("and pl.stt = 'PROVISIONADO' ");
		// hql.append("and pl.stt = 'EFETIVADO' ");

		hql.append("order by pl.datapagamento asc");

		Query query = manager.createNativeQuery(hql.toString());
		query.setParameter("conta", Integer.valueOf(filtro.getIdConta().toString()));

		List<Object[]> result = query.getResultList();
		List<RelatorioContasAPagar> relatorio = new ArrayList<>();

		RelatorioContasAPagar rel = new RelatorioContasAPagar();

		for (Object[] object : result) {

			rel = new RelatorioContasAPagar();
			// rel.setEmissao(object[0] != null ? object[0].toString() : "");
			rel.setDoc(object[17] != null ? object[17].toString() : "");
			rel.setEmissao(object[2] != null ? object[2].toString() : "");
			rel.setPagamento(object[3] != null ? object[3].toString() : "");
			rel.setFonte(object[4] != null ? object[4].toString() : "");
			rel.setProjeto(object[5] != null ? object[5].toString() : "");
			rel.setAcao(object[6] != null ? object[6].toString() : "");
			rel.setParcela(object[7].toString() + "/" + object[8].toString());
			rel.setDescricao(object[18] != null ? object[18].toString() : "");
			rel.setCategoriaDespesa(
					object[19] != null ? CategoriadeDespesa.valueOf(object[19].toString()).getNome() : "");
			rel.setNotaFiscal(object[20] != null ? object[20].toString() : "");

			// String tipo = object[13] != null ? object[13].toString();

			if (!object[13].toString().equals("ad") && !object[13].toString().equals("pc")) {
				if (Long.valueOf(object[15].toString()).intValue() == filtro.getIdConta().intValue()) {
					rel.setSaida(Double.valueOf(object[9].toString()));
				}

				if (Long.valueOf(object[16].toString()).intValue() == filtro.getIdConta().intValue()) {
					rel.setEntrada(Double.valueOf(object[9].toString()));
				}
			} else {
				if (object[13].toString().equals("ad")) {
					rel.setSaida(Double.valueOf(object[9].toString()));
				}
				if (object[13].toString().equals("pc")) {
					rel.setPrestacaoConta(Double.valueOf(object[9].toString()));
				}
			}

			rel.setPagador(object[10].toString());
			rel.setRecebedor(object[11].toString());
			rel.setStatus(object[12].toString());
			rel.setNumeroLancamento(object[1] != null ? object[1].toString() : "");

			relatorio.add(rel);

		}

		return relatorio;
	}

	public List<Acao> getAcoesIn(Filtro filtro) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		StringBuilder hql = new StringBuilder("select a.id, a.codigo,");

		hql.append(" (select sum(af.valor) from acao_fonte af where af.acao_id = a.id) as orcado,");

		hql.append(" (select sum(la.valor) from lancamento l join lancamento_acao la on");

		hql.append(" l.id = la.lancamento_id join pagamento_lancamento pl on pl.lancamentoacao_id = la.id  ");

		hql.append(" where 1 = 1");

		if (filtro.getDataInicio() != null) {
			hql.append(" and pl.datapagamento < '" + sdf.format(filtro.getDataInicio()) + "' ");
		}

		if (filtro.getDataInicioEmissao() != null) {
			hql.append(" and l.data_emissao < '" + sdf.format(filtro.getDataInicioEmissao()) + "'");
		}

		hql.append(" and l.tipo != 'compra' and la.acao_id = a.id ");

		hql.append(" and l.depesareceita = 'DESPESA' and l.tipolancamento != 'pc'");

		hql.append(" group by la.acao_id) as executado,");

		hql.append(" (select sum(la.valor) from lancamento l join lancamento_acao la on ");

		hql.append(" l.id = la.lancamento_id join pagamento_lancamento pll on pll.lancamentoacao_id = la.id ");

		hql.append(" where 1 = 1 ");

		if (filtro.getDataInicio() != null) {
			hql.append(" and pll.datapagamento < '" + sdf.format(filtro.getDataInicio()) + "'");
		}

		if (filtro.getDataInicioEmissao() != null) {
			hql.append(" and l.data_emissao < '" + sdf.format(filtro.getDataInicioEmissao()) + "'");
		}

		hql.append(" and l.tipo != 'compra' and la.acao_id = a.id ");

		hql.append("  and l.depesareceita = 'RECEITA' and l.tipolancamento != 'pc'");

		hql.append(" group by la.acao_id) as receita  ");

		hql.append(" from acao a where 1 = 1 ");

		if (filtro.getAcoes().length > 0) {
			hql.append(" and a.id in (:acoes)");
		}

		if (filtro.getProjetoId() != null && filtro.getProjetoId() != Long.valueOf(0)) {
			hql.append(" and a.projeto_id = :projeto");
		}

		hql.append(" order by a.codigo, a.projeto_id ");

		Query query = manager.createNativeQuery(hql.toString());
		// query.setParameter("data", "'"+sdf.format(filtro.getDataFinal())
		// +"'");

		if (filtro.getAcoes().length > 0) {
			List<Integer> list = new ArrayList<>();
			Integer[] mList = filtro.getAcoes();

			for (int i = 0; i < filtro.getAcoes().length; i++) {
				list.add(mList[i]);
			}

			query.setParameter("acoes", list);
		}

		if (filtro.getProjetoId() != null && filtro.getProjetoId() != Long.valueOf(0)) {
			query.setParameter("projeto", filtro.getProjetoId());
		}

		List<Acao> retorno = new ArrayList<Acao>();
		List<Object[]> result = query.getResultList();

		Acao acao = new Acao();

		for (Object[] object : result) {
			acao = new Acao();
			acao.setId(new Long(object[0].toString()));
			acao.setCodigo(object[1].toString());
			acao.setOrcado(object[2] != null ? new BigDecimal(object[2].toString()) : BigDecimal.ZERO);
			acao.setExecutado(object[3] != null ? new BigDecimal(object[3].toString()) : BigDecimal.ZERO);
			acao.setReceita(object[4] != null ? new BigDecimal(object[4].toString()) : BigDecimal.ZERO);
			acao.setSaldo(acao.getOrcado().subtract(acao.getExecutado()).add(acao.getReceita()));
			retorno.add(acao);
		}

		return retorno;

	}

	public List<RelatorioContasAPagar> getPagamentosByDocumento(Filtro filtro) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder hql = new StringBuilder("select pl.id as id_pagamento, pl.lancamento_id as codigo_lancamento,  ");
		hql.append("to_char(pl.dataemissao,'dd/MM/yyyy') as data_emissao, \n");
		hql.append("to_char(pl.datapagamento,'dd/MM/yyyy') as data_pagamento,  \n");
		hql.append("fp.nome, p.nome as projeto, a.codigo as acao, pl.numerodaparcela as numero_parcela,  \n");
		hql.append("pl.quantidade_parcela as qtd_parcela, pl.valor as valor, \n");
		hql.append("(select cb.nome_conta from conta_bancaria cb where cb.id = pl.conta_id) as conta_pagador,  \n");
		hql.append(
				"(select cb.nome_conta from conta_bancaria cb where cb.id = pl.contarecebedor_id) as recebedor_conta, \n");
		hql.append(
				"pl.stt as status, pl.tipolancamento as ad_pc, l.tipo  as tipo_lancamento, pl.conta_id as idcontapagador,  \n");
		hql.append(
				"pl.contarecebedor_id as idcontarecebedor, l.numerodocumento as numero_lancamento, la.despesareceita, \n ");
		hql.append(" l.descricao, l.tipolancamento ");
		hql.append("from pagamento_lancamento pl join lancamento_acao la on pl.lancamentoacao_id = la.id  \n ");
		hql.append("join lancamento l on l.id = la.lancamento_id  join acao a on la.acao_id = a.id  \n");
		hql.append("left join localidade loc on l.localidade_id = loc.id  \n ");
		hql.append(" join projeto p on a.projeto_id = p.id join fonte_pagadora fp on fp.id = la.fontepagadora_id  \n ");
		hql.append(" where 1 = 1 and ");

		// hql.append(" (l.categoriadespesa != 'DIARIA' OR l.categoriadespesa is
		// null) AND l.numerodocumento = '"+filtro.getNumeroDocumento()+"' and
		// l.id != "+filtro.getLancamentoID());
		hql.append(
				"  (l.categoriadespesaclass_id != 30 OR l.categoriadespesaclass_id is null) AND l.numerodocumento =  '"
						+ filtro.getNumeroDocumento() + "' and l.id != " + filtro.getLancamentoID());
		// hql.append("where (pl.conta_id = :conta or pl.contarecebedor_id =
		// :conta) \n");

		// hql.append("and pl.datapagamento between
		// '"+sdf.format(filtro.getDataInicio())+"' and
		// '"+sdf.format(filtro.getDataFinal())+"' ");

		// hql.append("and pl.stt = 'PROVISIONADO' ");
		// hql.append("and pl.stt = 'EFETIVADO' ");

		hql.append(" order by pl.datapagamento asc");

		Query query = manager.createNativeQuery(hql.toString());
		// query.setParameter("acao", filtro.getAcaoId());

		List<Object[]> result = query.getResultList();
		List<RelatorioContasAPagar> relatorio = new ArrayList<>();

		RelatorioContasAPagar rel = new RelatorioContasAPagar();

		for (Object[] object : result) {

			rel = new RelatorioContasAPagar();
			// rel.setEmissao(object[0] != null ? object[0].toString() : "");
			rel.setDoc(object[17] != null ? object[17].toString() : "");
			rel.setEmissao(object[2] != null ? object[2].toString() : "");
			rel.setPagamento(object[3] != null ? object[3].toString() : "");
			rel.setFonte(object[4] != null ? object[4].toString() : "");
			rel.setProjeto(object[5] != null ? object[5].toString() : "");
			rel.setAcao(object[6] != null ? object[6].toString() : "");
			rel.setParcela(object[7].toString() + "/" + object[8].toString());
			rel.setDespesaReceita(object[18] != null ? object[18].toString() : "");
			rel.setDescricao(object[19] != null ? object[19].toString() : "");
			rel.setTipoLancamento(object[20] != null ? object[20].toString() : "");

			// String tipo = object[13] != null ? object[13].toString();

			if (!object[20].toString().equals("ad") && !object[20].toString().equals("pc")) {
				if (!object[18].toString().equals("HERANCA")) {
					if (object[18].toString().equals("DESPESA") && !object[20].toString().equals("dev")) {
						rel.setSaida(Double.valueOf(object[9].toString()));
					} else if (object[18].toString().equals("RECEITA") || object[20].toString().equals("dev")) {
						rel.setEntrada(Double.valueOf(object[9].toString()));
					}
				} else {
					rel.setPrestacaoConta(Double.valueOf(object[9].toString()));
				}
			} else {
				if (object[20].toString().equals("ad")) {
					rel.setAdiantamento(Double.valueOf(object[9].toString()));
				}
				if (object[20].toString().equals("pc")) {
					rel.setPrestacaoConta(Double.valueOf(object[9].toString()));
				}
			}

			// rel.setSaida(Double.valueOf(object[9].toString()));

			/*
			 * if(!object[18].toString().equals("HERANCA")){
			 * if(object[18].toString().equals("DESPESA")){
			 * rel.setSaida(Double.valueOf(object[9].toString())); }else
			 * if(object[18].toString().equals("RECEITA")){
			 * rel.setEntrada(Double.valueOf(object[9].toString())); } }else{
			 * rel.setReembolso(Double.valueOf(object[9].toString())); }
			 */

			rel.setPagador(object[10].toString());
			rel.setRecebedor(object[11].toString());
			rel.setStatus(object[12].toString());
			rel.setNumeroLancamento(object[1] != null ? object[1].toString() : "");

			relatorio.add(rel);

		}

		return relatorio;

	}

	public List<RelatorioExecucao> getPagamentosByProjeto(Long idProjeto, Filtro filtro) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder hql = new StringBuilder("select ");
		hql.append("pl.id as id_pagamento_00, ");

		hql.append("pl.lancamento_id as codigo_lancamento_01, ");
		hql.append("to_char(pl.dataemissao,'dd/MM/yyyy') as data_emissao_02, ");
		hql.append("to_char(pl.datapagamento,'dd/MM/yyyy') as data_pagamento_03, ");
		hql.append("l.data_doc_fiscal as tipo_documento_fiscal_04,");
		hql.append("l.tipo_documento_fiscal as doc_fiscal_05,");
		hql.append("(select comp.nome from componente_class  comp where id = ");
		hql.append("(select ro.componente_id from rubrica_orcamento ro ");
		hql.append("where ro.id = (select pr.rubricaorcamento_id from projeto_rubrica pr ");
		hql.append("where pr.id = la.projetorubrica_id))) as componente_06,");
		hql.append("(select subcomp.nome from sub_componente subcomp where id = ");
		hql.append("(select ro.subcomponente_id from rubrica_orcamento ro ");
		hql.append("where ro.id = (select pr.rubricaorcamento_id from projeto_rubrica pr  ");
		hql.append("where pr.id = la.projetorubrica_id))) as sub_componente_07,");
		hql.append("(select fp.nome from fonte_pagadora fp where fp.id = ");
		hql.append("(select oc.fonte_id from orcamento oc where id = la.orcamento_id)) as fonte_08,");
		hql.append("p.nome as projeto_09, ");
		hql.append("(select rub.nome from rubrica rub where id = ");
		hql.append("(select ro.rubrica_id from rubrica_orcamento ro ");
		hql.append("where ro.id = (select pr.rubricaorcamento_id from projeto_rubrica pr ");
		hql.append("where pr.id = la.projetorubrica_id))) as categoria_10,  ");
		hql.append("pl.numerodaparcela as numero_parcela_11, ");
		hql.append("pl.quantidade_parcela as qtd_parcela_12,");

		hql.append("pl.valor as valor_13,");
		hql.append("(select cb.nome_conta from conta_bancaria cb where cb.id = pl.conta_id) as conta_pagador_14, ");
		hql.append(
				"(select cb.nome_conta from conta_bancaria cb where cb.id = pl.contarecebedor_id) as recebedor_conta_15,");
		hql.append("pl.stt as status_16,");
		hql.append("pl.tipolancamento as ad_pc_17,");
		hql.append("l.tipo  as tipo_lancamento_18,");
		hql.append("pl.conta_id as idcontapagador_19, ");
		hql.append("pl.contarecebedor_id as idcontarecebedor_20, ");
		hql.append("l.numerodocumento as numero_lancamento_21,  ");
		hql.append("case when la.descricao = '' or la.descricao is null ");
		hql.append("then l.descricao  else la.descricao end as descricao_22,");
		hql.append("l.categoriadespesa as categoriadespesa_23,  ");
		hql.append("l.nota_fiscal as nota_fiscal_24, ");
		hql.append("(select cb.tipo from conta_bancaria cb where cb.id = pl.conta_id) as tipo_conta_pagador_25,  ");
		hql.append(
				"(select cb.tipo from conta_bancaria cb where cb.id = pl.contarecebedor_id) as tipo_conta_recebedor_26,  ");
		hql.append("l.statusadiantamento as statusadiantamento_27,");
		hql.append("l.idadiantamento as idadiantamento_28,");
		hql.append("(select ll.statusadiantamento from lancamento ll where ll.id = l.idadiantamento) status_ad_29 ");

		hql.append("from pagamento_lancamento pl join lancamento_acao la ");
		hql.append("on pl.lancamentoacao_id = la.id ");
		hql.append("join lancamento l on l.id = la.lancamento_id ");
		hql.append("join projeto p on la.projeto_id = p.id ");
		hql.append("where 1 = 1 and l.statuscompra = 'CONCLUIDO'  ");
		hql.append(" and p.id = " + idProjeto);

		// hql.append("where (pl.conta_id = :conta or pl.contarecebedor_id =
		// :conta) \n");

		if (filtro.getDataInicio() != null) {
			hql.append(" and pl.datapagamento between '" + sdf.format(filtro.getDataInicio()) + "' and '"
					+ sdf.format(filtro.getDataFinal()) + "' ");
		}

		if (filtro.getDataInicioEmissao() != null) {
			hql.append(" and l.data_emissao between '" + sdf.format(filtro.getDataInicioEmissao()) + "' and '"
					+ sdf.format(filtro.getDataFinalEmissao()) + "' ");
		}

		hql.append(" order by pl.datapagamento asc");

		Query query = manager.createNativeQuery(hql.toString());
		// query.setParameter("acao", filtro.getAcaoId());

		List<Object[]> result = query.getResultList();
		List<RelatorioExecucao> relatorio = new ArrayList<>();

		RelatorioExecucao rel = new RelatorioExecucao();

		for (Object[] object : result) {

			String tipoPagador = object[25] != null ? object[25].toString() : "";
			String tipoRecebedor = object[26] != null ? object[26].toString() : "";
			String tipoLancamento = object[17] != null ? object[17].toString() : "";
			String statusAdiantamento = object[27] != null ? object[27].toString() : "";
			Long idAdiantamento = object[28] != null ? new Long(object[28].toString()) : null;
			String statusAdiantamentoDePrestacao = object[29] != null ? object[29].toString() : "";

			if (tipoLancamento.equals("ad")) {
				if (statusAdiantamento.equals("VALIDADO")) {
					rel = new RelatorioExecucao(object, "", "");
					relatorio.add(rel);

					continue;
				} else {
					rel = new RelatorioExecucao(object);
					relatorio.add(rel);
					continue;
				}
			}

			if (idAdiantamento != null) {
				if (statusAdiantamentoDePrestacao.equals("VALIDADO")) {
					if (tipoLancamento.equals("dev") || tipoLancamento.equals("reenb")) {
						rel = new RelatorioExecucao(object, "", "");
						relatorio.add(rel);
						continue;
					} else {
						rel = new RelatorioExecucao(object);
						relatorio.add(rel);
						continue;
					}

				} else {
					continue;
				}

			}

			if ((tipoPagador.equals("CB") || tipoPagador.equals("CA")) && !tipoRecebedor.equals("CB")) {
				rel = new RelatorioExecucao(object);
				relatorio.add(rel);
			} else if (!tipoPagador.equals("CB") && tipoRecebedor.equals("CB")) {
				rel = new RelatorioExecucao(object, "");
				relatorio.add(rel);
			} else if (tipoPagador.equals("CB") && tipoRecebedor.equals("CB")) {
				rel = new RelatorioExecucao(object);
				relatorio.add(rel);
				rel = new RelatorioExecucao(object, "");
				relatorio.add(rel);
			}

		}

		return relatorio;
	}

	public List<RelatorioContasAPagar> getPagamentosByAcao(Filtro filtro) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder hql = new StringBuilder("select pl.id as id_pagamento, pl.lancamento_id as codigo_lancamento,  ");
		hql.append("to_char(pl.dataemissao,'dd/MM/yyyy') as data_emissao, \n");
		hql.append("to_char(pl.datapagamento,'dd/MM/yyyy') as data_pagamento,  \n");
		hql.append("fp.nome, p.nome as projeto, a.codigo as acao, pl.numerodaparcela as numero_parcela,  \n");
		hql.append("pl.quantidade_parcela as qtd_parcela, pl.valor as valor, \n");
		hql.append("(select cb.nome_conta from conta_bancaria cb where cb.id = pl.conta_id) as conta_pagador,  \n");
		hql.append(
				"(select cb.nome_conta from conta_bancaria cb where cb.id = pl.contarecebedor_id) as recebedor_conta, \n");
		hql.append(
				"pl.stt as status, pl.tipolancamento as ad_pc, l.tipo  as tipo_lancamento, pl.conta_id as idcontapagador,  \n");
		hql.append(
				"pl.contarecebedor_id as idcontarecebedor, l.numerodocumento as numero_lancamento, la.despesareceita, \n ");
		hql.append(" l.descricao, l.tipolancamento, ");
		hql.append(
				" (select cat.nome from categoria_despesa cat  where cat.id = l.categoriadespesaclass_id) as categoria, ");
		hql.append(" (select r.nome from rubrica r where r.id = l.rubrica_id) as rubrica ");
		hql.append(" from pagamento_lancamento pl join lancamento_acao la on pl.lancamentoacao_id = la.id  \n ");
		hql.append(" join lancamento l on l.id = la.lancamento_id  join acao a on la.acao_id = a.id  \n");
		hql.append(" left join localidade loc on l.localidade_id = loc.id  \n ");
		hql.append(" join projeto p on a.projeto_id = p.id join fonte_pagadora fp on fp.id = la.fontepagadora_id  \n ");
		hql.append(" where 1 = 1  and ");
		hql.append(" l.tipolancamento != 'pc' and la.acao_id =  " + filtro.getAcaoId());

		// hql.append("where (pl.conta_id = :conta or pl.contarecebedor_id =
		// :conta) \n");

		if (filtro.getDataInicio() != null) {
			hql.append(" and pl.datapagamento between '" + sdf.format(filtro.getDataInicio()) + "' and '"
					+ sdf.format(filtro.getDataFinal()) + "' ");
		}

		if (filtro.getDataInicioEmissao() != null) {
			hql.append(" and l.data_emissao between '" + sdf.format(filtro.getDataInicioEmissao()) + "' and '"
					+ sdf.format(filtro.getDataFinalEmissao()) + "' ");
		}
		// hql.append("and l.categoriadespesaclass_id = 30 ");

		// hql.append("and pl.stt = 'PROVISIONADO' ");
		// hql.append("and pl.stt = 'EFETIVADO' ");

		// hql.append("and l.numerodocumento = '9256' ");

		hql.append(" order by pl.datapagamento asc");

		Query query = manager.createNativeQuery(hql.toString());
		// query.setParameter("acao", filtro.getAcaoId());

		List<Object[]> result = query.getResultList();
		List<RelatorioContasAPagar> relatorio = new ArrayList<>();

		RelatorioContasAPagar rel = new RelatorioContasAPagar();

		for (Object[] object : result) {

			rel = new RelatorioContasAPagar();
			// rel.setEmissao(object[0] != null ? object[0].toString() : "");
			rel.setDoc(object[17] != null ? object[17].toString() : "");
			rel.setEmissao(object[2] != null ? object[2].toString() : "");
			rel.setPagamento(object[3] != null ? object[3].toString() : "");
			rel.setFonte(object[4] != null ? object[4].toString() : "");
			rel.setProjeto(object[5] != null ? object[5].toString() : "");
			rel.setAcao(object[6] != null ? object[6].toString() : "");
			rel.setParcela(object[7].toString() + "/" + object[8].toString());
			rel.setDespesaReceita(object[18] != null ? object[18].toString() : "");
			rel.setDescricao(object[19] != null ? object[19].toString() : "");
			rel.setTipoLancamento(object[20] != null ? object[20].toString() : "");
			rel.setCategoriaDespesa(object[21] != null ? object[21].toString() : "");
			rel.setRubrica(object[22] != null ? object[22].toString() : "");

			// String tipo = object[13] != null ? object[13].toString();

			if (!object[20].toString().equals("ad") && !object[20].toString().equals("pc")) {

				if (!object[18].toString().equals("HERANCA") && !rel.getTipoLancamento().equals("reemb_conta")) {

					if (object[18].toString().equals("DESPESA")) {
						rel.setSaida(Double.valueOf(object[9].toString()));
					} else if (object[18].toString().equals("RECEITA")) {
						rel.setEntrada(Double.valueOf(object[9].toString()));
					}

				} else {
					rel.setPrestacaoConta(Double.valueOf(object[9].toString()));
				}
			} else {
				if (object[20].toString().equals("ad")) {
					rel.setAdiantamento(Double.valueOf(object[9].toString()));
				}
				if (object[20].toString().equals("pc")) {
					rel.setPrestacaoConta(Double.valueOf(object[9].toString()));
				}
			}

			/*
			 * if(!object[18].toString().equals("HERANCA")){
			 * if(object[18].toString().equals("DESPESA")){
			 * rel.setSaida(Double.valueOf(object[9].toString())); }else
			 * if(object[18].toString().equals("RECEITA")){
			 * rel.setEntrada(Double.valueOf(object[9].toString())); } }else{
			 * rel.setReembolso(Double.valueOf(object[9].toString())); }
			 */

			rel.setPagador(object[10].toString());
			rel.setRecebedor(object[11].toString());
			rel.setStatus(object[12].toString());
			rel.setNumeroLancamento(object[1] != null ? object[1].toString() : "");

			relatorio.add(rel);

		}

		return relatorio;
	}

	public List<RelatorioContasAPagar> getPagamentosByContaCA(Filtro filtro) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder hql = new StringBuilder("select pl.id as id_pagamento, ");
		hql.append("pl.lancamento_id as codigo_lancamento, \n");
		hql.append("to_char(pl.dataemissao,'dd/MM/yyyy') as data_emissao, \n");
		hql.append("to_char(pl.datapagamento,'dd/MM/yyyy') as data_pagamento, \n");
		hql.append("fp.nome, \n");
		hql.append("p.nome as projeto, \n");
		hql.append("a.codigo as acao, \n");
		hql.append("pl.numerodaparcela as numero_parcela, \n");
		hql.append("pl.quantidade_parcela as qtd_parcela,\n ");
		hql.append("pl.valor as valor,\n ");
		hql.append("(select cb.nome_conta from conta_bancaria cb where cb.id = pl.conta_id) as conta_pagador, \n");
		hql.append(
				"(select cb.nome_conta from conta_bancaria cb where cb.id = pl.contarecebedor_id) as recebedor_conta,\n ");
		hql.append("pl.stt as status,\n ");
		hql.append("pl.tipolancamento as ad_pc,\n ");
		hql.append("l.tipo  as tipo_lancamento, \n ");

		hql.append(" pl.conta_id as idcontapagador, \n");
		hql.append(" pl.contarecebedor_id as idcontarecebedor, \n");
		hql.append(" l.numerodocumento as numero_lancamento, ");

		hql.append(" case when la.descricao = '' or la.descricao is null then l.descricao  ");
		hql.append(" else la.descricao");
		hql.append(" end as descricao, ");
		hql.append(" l.categoriadespesa, ");
		hql.append(" l.nota_fiscal ");

		hql.append("from pagamento_lancamento pl join lancamento_acao la \n");
		hql.append("on pl.lancamentoacao_id = la.id \n");
		hql.append("join lancamento l on l.id = la.lancamento_id \n");
		hql.append("join acao a on la.acao_id = a.id \n");

		hql.append("left join localidade loc on l.localidade_id = loc.id \n");
		hql.append("join projeto p on a.projeto_id = p.id ");
		hql.append("join fonte_pagadora fp on fp.id = la.fontepagadora_id  \n");

		// Versao anterior dia 31
		// hql.append("where (pl.conta_id = :conta or pl.contarecebedor_id =
		// :conta) \n");
		// Versao posterior
		hql.append(
				"where (pl.conta_id = :conta or (pl.contarecebedor_id = :conta and (pl.tipolancamento = 'ad' or pl.tipolancamento = 'reenb')) ) \n");

		// hql.append(" and (a.componente = 'RENDA' or a.componente =
		// 'ASSOCIACAO') ");

		// hql.append(" and a.componente = 'SOCIAL' ");
		// hql.append(" and a.componente = 'ASSOCIACAO' ");
		// hql.append(" and a.componente = 'RENDA' ");

		hql.append("and pl.datapagamento between '" + sdf.format(filtro.getDataInicio()) + "' and '"
				+ sdf.format(filtro.getDataFinal()) + "' ");

		// hql.append("and pl.stt = 'PROVISIONADO' ");
		// hql.append("and pl.stt = 'EFETIVADO' ");

		hql.append("order by pl.datapagamento asc");

		Query query = manager.createNativeQuery(hql.toString());
		query.setParameter("conta", Integer.valueOf(filtro.getIdConta().toString()));

		List<Object[]> result = query.getResultList();
		List<RelatorioContasAPagar> relatorio = new ArrayList<>();

		RelatorioContasAPagar rel = new RelatorioContasAPagar();

		for (Object[] object : result) {

			rel = new RelatorioContasAPagar();
			// rel.setEmissao(object[0] != null ? object[0].toString() : "");
			rel.setDoc(object[17] != null ? object[17].toString() : "");
			rel.setEmissao(object[2] != null ? object[2].toString() : "");
			rel.setPagamento(object[3] != null ? object[3].toString() : "");
			rel.setFonte(object[4] != null ? object[4].toString() : "");
			rel.setProjeto(object[5] != null ? object[5].toString() : "");
			rel.setAcao(object[6] != null ? object[6].toString() : "");
			rel.setParcela(object[7].toString() + "/" + object[8].toString());
			rel.setDescricao(object[18] != null ? object[18].toString() : "");

			rel.setCategoriaDespesa(
					object[19] != null ? CategoriadeDespesa.valueOf(object[19].toString()).getNome() : "");
			rel.setNotaFiscal(object[20] != null ? object[20].toString() : "");

			// String tipo = object[13] != null ? object[13].toString();

			if (!object[13].toString().equals("ad") && !object[13].toString().equals("pc")) {
				if (Long.valueOf(object[15].toString()).intValue() == filtro.getIdConta().intValue()) {
					rel.setSaida(Double.valueOf(object[9].toString()));
				}

				if (Long.valueOf(object[16].toString()).intValue() == filtro.getIdConta().intValue()) {
					rel.setEntrada(Double.valueOf(object[9].toString()));
				}
			} else {
				if (object[13].toString().equals("ad")) {
					rel.setEntrada(Double.valueOf(object[9].toString()));
				}
				if (object[13].toString().equals("pc")) {
					rel.setSaida(Double.valueOf(object[9].toString()));
				}
			}

			rel.setPagador(object[10].toString());
			rel.setRecebedor(object[11].toString());
			rel.setStatus(object[12].toString());
			rel.setNumeroLancamento(object[1] != null ? object[1].toString() : "");

			relatorio.add(rel);

		}

		return relatorio;
	}

	public List<RelatorioContasAPagar> getPagamentosByContaCAMODE01(Filtro filtro) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder hql = new StringBuilder("select pl.id as id_pagamento, ");
		hql.append("pl.lancamento_id as codigo_lancamento, \n");
		hql.append("to_char(pl.dataemissao,'dd/MM/yyyy') as data_emissao, \n");
		hql.append("to_char(pl.datapagamento,'dd/MM/yyyy') as data_pagamento, \n");
		hql.append("fp.nome, \n");
		hql.append("p.nome as projeto, \n");
		hql.append("a.codigo as acao, \n");
		hql.append("pl.numerodaparcela as numero_parcela, \n");
		hql.append("pl.quantidade_parcela as qtd_parcela,\n ");
		hql.append("pl.valor as valor,\n ");
		hql.append("(select cb.nome_conta from conta_bancaria cb where cb.id = pl.conta_id) as conta_pagador, \n");
		hql.append(
				"(select cb.nome_conta from conta_bancaria cb where cb.id = pl.contarecebedor_id) as recebedor_conta,\n ");
		hql.append("pl.stt as status,\n ");
		hql.append("pl.tipolancamento as ad_pc,\n ");
		hql.append("l.tipo  as tipo_lancamento, \n ");

		hql.append(" pl.conta_id as idcontapagador, \n");
		hql.append(" pl.contarecebedor_id as idcontarecebedor, \n");
		hql.append(" l.numerodocumento as numero_lancamento, ");

		hql.append(" case when la.descricao = '' or la.descricao is null then l.descricao  ");
		hql.append(" else la.descricao");
		hql.append(" end as descricao, ");
		hql.append(" l.categoriadespesa, ");
		hql.append(" l.nota_fiscal ");

		hql.append("from pagamento_lancamento pl join lancamento_acao la \n");
		hql.append("on pl.lancamentoacao_id = la.id \n");
		hql.append("join lancamento l on l.id = la.lancamento_id \n");
		hql.append("join acao a on la.acao_id = a.id \n");

		hql.append("left join localidade loc on l.localidade_id = loc.id \n");
		hql.append("join projeto p on a.projeto_id = p.id ");
		hql.append("join fonte_pagadora fp on fp.id = la.fontepagadora_id  \n");

		// Versao anterior dia 31
		// hql.append("where (pl.conta_id = :conta or pl.contarecebedor_id =
		// :conta) \n");
		// Versao posterior
		hql.append(
				"where (pl.conta_id = :conta or (pl.contarecebedor_id = :conta and (pl.tipolancamento = 'ad' or pl.tipolancamento = 'reenb')) ) \n");

		// hql.append(" and (a.componente = 'RENDA' or a.componente =
		// 'ASSOCIACAO') ");

		// hql.append(" and a.componente = 'SOCIAL' ");
		// hql.append(" and a.componente = 'ASSOCIACAO' ");
		// hql.append(" and a.componente = 'RENDA' ");

		hql.append("and pl.datapagamento between '" + sdf.format(filtro.getDataInicio()) + "' and '"
				+ sdf.format(filtro.getDataFinal()) + "' ");

		// hql.append("and pl.stt = 'PROVISIONADO' ");
		// hql.append("and pl.stt = 'EFETIVADO' ");

		hql.append("order by pl.datapagamento asc");

		Query query = manager.createNativeQuery(hql.toString());
		query.setParameter("conta", Integer.valueOf(filtro.getIdConta().toString()));

		List<Object[]> result = query.getResultList();
		List<RelatorioContasAPagar> relatorio = new ArrayList<>();

		RelatorioContasAPagar rel = new RelatorioContasAPagar();

		for (Object[] object : result) {

			rel = new RelatorioContasAPagar();
			// rel.setEmissao(object[0] != null ? object[0].toString() : "");
			rel.setDoc(object[17] != null ? object[17].toString() : "");
			rel.setEmissao(object[2] != null ? object[2].toString() : "");
			rel.setPagamento(object[3] != null ? object[3].toString() : "");
			rel.setFonte(object[4] != null ? object[4].toString() : "");
			rel.setProjeto(object[5] != null ? object[5].toString() : "");
			rel.setAcao(object[6] != null ? object[6].toString() : "");
			rel.setParcela(object[7].toString() + "/" + object[8].toString());
			rel.setDescricao(object[18] != null ? object[18].toString() : "");

			rel.setCategoriaDespesa(
					object[19] != null ? CategoriadeDespesa.valueOf(object[19].toString()).getNome() : "");
			rel.setNotaFiscal(object[20] != null ? object[20].toString() : "");

			// String tipo = object[13] != null ? object[13].toString();

			if (!object[13].toString().equals("ad") && !object[13].toString().equals("pc")) {
				if (Long.valueOf(object[15].toString()).intValue() == filtro.getIdConta().intValue()) {
					rel.setSaida(Double.valueOf(object[9].toString()));
				}

				if (Long.valueOf(object[16].toString()).intValue() == filtro.getIdConta().intValue()) {
					rel.setEntrada(Double.valueOf(object[9].toString()));
				}
			} else {
				if (object[13].toString().equals("ad")) {
					rel.setEntrada(Double.valueOf(object[9].toString()));
				}
				if (object[13].toString().equals("pc")) {
					rel.setSaida(Double.valueOf(object[9].toString()));
				}
			}

			rel.setPagador(object[10].toString());
			rel.setRecebedor(object[11].toString());
			rel.setStatus(object[12].toString());
			rel.setNumeroLancamento(object[1] != null ? object[1].toString() : "");

			relatorio.add(rel);

		}

		return relatorio;
	}

	public List<RelatorioContasAPagar> getPagamentosByContaCAParaOutrosFiltros(Filtro filtro) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuilder hql = new StringBuilder("select pl.id as id_pagamento, ");
		hql.append("pl.lancamento_id as codigo_lancamento, \n");
		hql.append("to_char(pl.dataemissao,'dd/MM/yyyy') as data_emissao, \n");
		hql.append("to_char(pl.datapagamento,'dd/MM/yyyy') as data_pagamento, \n");
		hql.append("fp.nome, \n");
		hql.append("p.nome as projeto, \n");
		hql.append("a.codigo as acao, \n");
		hql.append("pl.numerodaparcela as numero_parcela, \n");
		hql.append("pl.quantidade_parcela as qtd_parcela,\n ");
		hql.append("pl.valor as valor,\n ");
		hql.append("(select cb.nome_conta from conta_bancaria cb where cb.id = pl.conta_id) as conta_pagador, \n");
		hql.append(
				"(select cb.nome_conta from conta_bancaria cb where cb.id = pl.contarecebedor_id) as recebedor_conta,\n ");
		hql.append("pl.stt as status,\n ");
		hql.append("pl.tipolancamento as ad_pc,\n ");
		hql.append("l.tipo  as tipo_lancamento, \n ");

		hql.append(" pl.conta_id as idcontapagador, \n");
		hql.append(" pl.contarecebedor_id as idcontarecebedor, \n");
		hql.append(" l.numerodocumento as numero_lancamento, ");

		hql.append(" case when la.descricao = '' or la.descricao is null then l.descricao  ");
		hql.append(" else la.descricao");
		hql.append(" end as descricao, ");
		hql.append(" l.categoriadespesa, ");
		hql.append(" l.nota_fiscal ");

		hql.append("from pagamento_lancamento pl join lancamento_acao la \n");
		hql.append("on pl.lancamentoacao_id = la.id \n");
		hql.append("join lancamento l on l.id = la.lancamento_id \n");
		hql.append("join acao a on la.acao_id = a.id \n");

		hql.append("left join localidade loc on l.localidade_id = loc.id \n");
		hql.append("join projeto p on a.projeto_id = p.id ");
		hql.append("join fonte_pagadora fp on fp.id = la.fontepagadora_id  \n");

		// Versao anterior dia 31
		// hql.append("where (pl.conta_id = :conta or pl.contarecebedor_id =
		// :conta) \n");
		// Versao posterior
		hql.append(
				"where (pl.conta_id = :conta or (pl.contarecebedor_id = :conta and (pl.tipolancamento = 'ad' or pl.tipolancamento = 'reenb')) ) \n");

		// hql.append(" and (a.componente = 'RENDA' or a.componente =
		// 'ASSOCIACAO') ");

		// hql.append(" and a.componente = 'SOCIAL' ");
		// hql.append(" and a.componente = 'ASSOCIACAO' ");
		// hql.append(" and a.componente = 'RENDA' ");

		hql.append("and pl.datapagamento between '" + sdf.format(filtro.getDataInicio()) + "' and '"
				+ sdf.format(filtro.getDataFinal()) + "' ");

		// hql.append("and l.categoriadespesa = 'DOACAO' ");
		// hql.append(" and la.despesareceita = 'RECEITA' ");

		// hql.append("and pl.stt = 'PROVISIONADO' ");
		// hql.append("and pl.stt = 'EFETIVADO' ");

		hql.append("order by pl.datapagamento asc");

		Query query = manager.createNativeQuery(hql.toString());
		query.setParameter("conta", Integer.valueOf(filtro.getIdConta().toString()));

		List<Object[]> result = query.getResultList();
		List<RelatorioContasAPagar> relatorio = new ArrayList<>();

		RelatorioContasAPagar rel = new RelatorioContasAPagar();

		for (Object[] object : result) {

			rel = new RelatorioContasAPagar();
			// rel.setEmissao(object[0] != null ? object[0].toString() : "");
			rel.setDoc(object[17] != null ? object[17].toString() : "");
			rel.setEmissao(object[2] != null ? object[2].toString() : "");
			rel.setPagamento(object[3] != null ? object[3].toString() : "");
			rel.setFonte(object[4] != null ? object[4].toString() : "");
			rel.setProjeto(object[5] != null ? object[5].toString() : "");
			rel.setAcao(object[6] != null ? object[6].toString() : "");
			rel.setParcela(object[7].toString() + "/" + object[8].toString());
			rel.setDescricao(object[18] != null ? object[18].toString() : "");

			rel.setCategoriaDespesa(
					object[19] != null ? CategoriadeDespesa.valueOf(object[19].toString()).getNome() : "");
			rel.setNotaFiscal(object[20] != null ? object[20].toString() : "");

			// String tipo = object[13] != null ? object[13].toString();

			if (!object[13].toString().equals("ad") && !object[13].toString().equals("pc")) {
				if (Long.valueOf(object[15].toString()).intValue() == filtro.getIdConta().intValue()) {
					rel.setSaida(Double.valueOf(object[9].toString()));
				}

				if (Long.valueOf(object[16].toString()).intValue() == filtro.getIdConta().intValue()) {
					rel.setEntrada(Double.valueOf(object[9].toString()));
				}
			} else {
				if (object[13].toString().equals("ad")) {
					rel.setEntrada(Double.valueOf(object[9].toString()));
				}
				if (object[13].toString().equals("pc")) {
					rel.setSaida(Double.valueOf(object[9].toString()));
				}
			}

			rel.setPagador(object[10].toString());
			rel.setRecebedor(object[11].toString());
			rel.setStatus(object[12].toString());
			rel.setNumeroLancamento(object[1] != null ? object[1].toString() : "");

			relatorio.add(rel);

		}

		return relatorio;
	}

	public List<ContaBancaria> getContaSaldoMODE01(Filtro filtro) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		StringBuilder hql = new StringBuilder("select ");
		hql.append("cb.nome_banco as banco, \n");
		hql.append("cb.nome_conta as conta, \n");
		hql.append("cb.numero_agencia as agencia, \n");

		// hql.append("(select sum(valor) \n");
		// hql.append("from pagamento_lancamento pl1 where contarecebedor_id = cb.id and
		// pl1.tipolancamento != 'pc' \n");
		// hql.append("and pl1.datapagamento < '" + sdf.format(filtro.getDataInicio()) +
		// "') as total_entrada, \n");

		// hql.append("(select sum(valor) \n");
		// hql.append("from pagamento_lancamento pl2 where conta_id = cb.id and
		// pl2.tipolancamento != 'pc' \n");
		// hql.append("and pl2.datapagamento < '" + sdf.format(filtro.getDataInicio()) +
		// "') as total_saida, \n");

		// hql.append("(select sum(pl.valor) from pagamento_lancamento pl join
		// lancamento_acao la ");
		// hql.append(" on pl.lancamentoacao_id = la.id join lancamento l on l.id =
		// la.lancamento_id ");
		// hql.append(" left join localidade loc on l.localidade_id = loc.id ");
		// hql.append(" where pl.contarecebedor_id = cb.id and CASE when
		// l.versionlancamento = 'MODE01' then ");
		// hql.append(" la.status = 0 else 1 = 1 end ");
		// hql.append(" and (l.statuscompra = 'CONCLUIDO' l.statuscompra = 'N_INCIADO')
		// ");
		// hql.append(" and pl.datapagamento < '"+ sdf.format(filtro.getDataInicio())
		// +"') as total_entrada,");

		hql.append("(");
		hql.append("select sum(pl.valor) from pagamento_lancamento pl join lancamento_acao la \n");
		hql.append("on pl.lancamentoacao_id = la.id \n");
		hql.append("join lancamento l on l.id = la.lancamento_id \n");
		hql.append("left join localidade loc on l.localidade_id = loc.id \n");
		hql.append("where (pl.contarecebedor_id = cb.id) and \n");
		hql.append(" CASE  when l.versionlancamento = 'MODE01' then \n");
		hql.append("la.status = 0   else 1 = 1 end \n");
		hql.append(" and (l.statuscompra = 'CONCLUIDO') \n");
		hql.append(" and pl.datapagamento < '" + sdf.format(filtro.getDataInicio()) + "') as total_entrada,");

		hql.append("(");
		hql.append("select sum(pl.valor) from pagamento_lancamento pl join lancamento_acao la \n");
		hql.append("on pl.lancamentoacao_id = la.id \n");
		hql.append("join lancamento l on l.id = la.lancamento_id \n");
		hql.append("left join localidade loc on l.localidade_id = loc.id \n");
		hql.append("where (pl.conta_id = cb.id) and \n");
		hql.append(" CASE  when l.versionlancamento = 'MODE01' then \n");
		hql.append("la.status = 0   else 1 = 1 end \n");
		hql.append(" and (l.statuscompra = 'CONCLUIDO') \n");
		hql.append(" and pl.datapagamento < '" + sdf.format(filtro.getDataInicio()) + "') as total_saida,");

		// hql.append("(select sum(pl.valor) from pagamento_lancamento pl join
		// lancamento_acao la ");
		// hql.append("on pl.lancamentoacao_id = la.id join lancamento l on l.id =
		// la.lancamento_id ");
		// hql.append("left join localidade loc on l.localidade_id = loc.id ");
		// hql.append("where pl.conta_id = cb.id and CASE when l.versionlancamento =
		// 'MODE01' then ");
		// hql.append("la.status = 0 else 1 = 1 end ");
		// hql.append(" and l.statuscompra = 'CONCLUIDO' ");
		// hql.append("and pl.datapagamento < '" +sdf.format(filtro.getDataInicio()) +
		// "') as total_saida,");

		hql.append("cb.saldo_inicial as saldo_inicial, \n ");
		hql.append("cb.id, \n ");
		hql.append("cb.tipo, ");
		hql.append("cb.numero_conta, \n");
		hql.append("cb.digito_conta, \n");
		hql.append("cb.digito_agencia \n");
		hql.append("from conta_bancaria cb \n ");
		hql.append(" where 1 = 1 ");

		if (filtro.getContas().length > 0) {
			hql.append(" and cb.id in (:contas)");
		}

		Query query = manager.createNativeQuery(hql.toString());
		// query.setParameter("data", "'"+sdf.format(filtro.getDataFinal())
		// +"'");

		if (filtro.getContas().length > 0) {
			List<Integer> list = new ArrayList<>();
			Integer[] mList = filtro.getContas();

			for (int i = 0; i < filtro.getContas().length; i++) {
				list.add(mList[i]);
			}

			query.setParameter("contas", list);
		}

		List<Object[]> result = query.getResultList();
		List<ContaBancaria> contas = new ArrayList<>();

		for (Object[] object : result) {
			ContaBancaria conta = new ContaBancaria();
			conta.setNomeBanco(object[0] != null ? object[0].toString() : "");
			conta.setNomeConta(object[1] != null ? object[1].toString() : "");
			conta.setNumeroAgencia(object[2] != null ? object[2].toString() : "");
			conta.setTotalEntrada(object[3] != null ? new BigDecimal(object[3].toString()) : BigDecimal.ZERO);
			conta.setTotalSaida(object[4] != null ? new BigDecimal(object[4].toString()) : BigDecimal.ZERO);
			conta.setSaldoInicial(object[5] != null ? new BigDecimal(object[5].toString()) : BigDecimal.ZERO);
			conta.setId(new Long(object[6].toString()));
			conta.setTipo(object[7] != null ? object[7].toString() : "");
			conta.setNumeroConta(object[8] != null ? object[8].toString() : "");
			conta.setDigitoConta(object[9] != null ? object[9].toString() : "");
			conta.setDigitoAgencia(object[10] != null ? object[10].toString() : "");
			contas.add(conta);
		}

		return contas;
	}

	public List<ContaBancaria> getContaSaldo(Filtro filtro) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		StringBuilder hql = new StringBuilder("select ");
		hql.append("cb.nome_banco as banco, \n");
		hql.append("cb.nome_conta as conta, \n");
		hql.append("cb.numero_agencia as agencia, \n");
		hql.append("(select sum(valor) \n");
		hql.append("from pagamento_lancamento pl1 where contarecebedor_id  = cb.id and pl1.tipolancamento != 'pc' \n");
		hql.append("and pl1.datapagamento < '" + sdf.format(filtro.getDataInicio()) + "') as total_entrada, \n");
		hql.append("(select sum(valor) \n");
		hql.append("from pagamento_lancamento pl2 where conta_id = cb.id and pl2.tipolancamento != 'pc' \n");
		hql.append("and pl2.datapagamento < '" + sdf.format(filtro.getDataInicio()) + "') as total_saida, \n");
		hql.append("cb.saldo_inicial as saldo_inicial, \n ");
		hql.append("cb.id, \n ");
		hql.append("cb.tipo ");
		hql.append("from conta_bancaria cb \n ");
		hql.append(" where 1 = 1 ");

		if (filtro.getContas().length > 0) {
			hql.append(" and cb.id in (:contas)");
		}

		Query query = manager.createNativeQuery(hql.toString());
		// query.setParameter("data", "'"+sdf.format(filtro.getDataFinal())
		// +"'");

		if (filtro.getContas().length > 0) {
			List<Integer> list = new ArrayList<>();
			Integer[] mList = filtro.getContas();

			for (int i = 0; i < filtro.getContas().length; i++) {
				list.add(mList[i]);
			}

			query.setParameter("contas", list);
		}

		List<Object[]> result = query.getResultList();
		List<ContaBancaria> contas = new ArrayList<>();

		for (Object[] object : result) {
			ContaBancaria conta = new ContaBancaria();
			conta.setNomeBanco(object[0] != null ? object[0].toString() : "");
			conta.setNomeConta(object[1] != null ? object[1].toString() : "");
			conta.setNumeroAgencia(object[2] != null ? object[2].toString() : "");
			conta.setTotalEntrada(object[3] != null ? new BigDecimal(object[3].toString()) : BigDecimal.ZERO);
			conta.setTotalSaida(object[4] != null ? new BigDecimal(object[4].toString()) : BigDecimal.ZERO);
			conta.setSaldoInicial(object[5] != null ? new BigDecimal(object[5].toString()) : BigDecimal.ZERO);
			conta.setId(new Long(object[6].toString()));
			conta.setTipo(object[7] != null ? object[7].toString() : "");
			contas.add(conta);
		}

		return contas;
	}

	public ContaBancaria getContaSaldoCA(Filtro filtro) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		StringBuilder hql = new StringBuilder("select ");
		hql.append("cb.nome_banco as banco, \n");
		hql.append("cb.nome_conta as conta, \n");
		hql.append("cb.numero_agencia as agencia, \n");
		hql.append("(select sum(valor) \n");
		hql.append(
				"from pagamento_lancamento pl1 where contarecebedor_id  = cb.id and (pl1.tipolancamento = 'ad' or pl1.tipolancamento = 'reenb')  \n");
		hql.append("and pl1.datapagamento < '" + sdf.format(filtro.getDataInicio()) + "') as total_entrada, \n");
		hql.append("(select sum(valor) \n");
		hql.append("from pagamento_lancamento pl2 where conta_id = cb.id  \n");
		hql.append("and pl2.datapagamento < '" + sdf.format(filtro.getDataInicio()) + "') as total_saida, \n");
		hql.append("cb.saldo_inicial as saldo_inicial, \n ");
		hql.append("cb.id, \n ");
		hql.append("cb.tipo ");
		hql.append("from conta_bancaria cb \n ");
		hql.append(" where 1 = 1 ");

		if (filtro.getIdConta() != null) {
			hql.append(" and cb.id = :conta");
		}

		Query query = manager.createNativeQuery(hql.toString());
		// query.setParameter("data", "'"+sdf.format(filtro.getDataFinal())
		// +"'");

		if (filtro.getIdConta() != null) {
			query.setParameter("conta", filtro.getIdConta());
		}

		List<Object[]> result = query.getResultList();
		List<ContaBancaria> contas = new ArrayList<>();

		for (Object[] object : result) {
			ContaBancaria conta = new ContaBancaria();
			conta.setNomeBanco(object[0] != null ? object[0].toString() : "");
			conta.setNomeConta(object[1] != null ? object[1].toString() : "");
			conta.setNumeroAgencia(object[2] != null ? object[2].toString() : "");
			conta.setTotalEntrada(object[3] != null ? new BigDecimal(object[3].toString()) : BigDecimal.ZERO);
			conta.setTotalSaida(object[4] != null ? new BigDecimal(object[4].toString()) : BigDecimal.ZERO);
			conta.setSaldoInicial(object[5] != null ? new BigDecimal(object[5].toString()) : BigDecimal.ZERO);
			conta.setId(new Long(object[6].toString()));
			conta.setTipo(object[7] != null ? object[7].toString() : "");
			contas.add(conta);
		}

		return contas.size() > 0 ? contas.get(0) : new ContaBancaria();
	}

	public List<ContaBancaria> getContaSaldoCB(Filtro filtro) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		StringBuilder hql = new StringBuilder("select ");
		hql.append("cb.nome_banco as banco, \n");
		hql.append("cb.nome_conta as conta, \n");
		hql.append("cb.numero_agencia as agencia, \n");
		hql.append("(select sum(valor) \n");
		hql.append("from pagamento_lancamento pl1 where contarecebedor_id  = cb.id and pl1.tipolancamento != 'pc' \n");
		hql.append("and pl1.datapagamento < '" + sdf.format(filtro.getDataInicio()) + "') as total_entrada, \n");
		hql.append("(select sum(valor) \n");
		hql.append("from pagamento_lancamento pl2 where conta_id = cb.id and pl2.tipolancamento != 'pc' \n");
		hql.append("and pl2.datapagamento < '" + sdf.format(filtro.getDataInicio()) + "') as total_saida, \n");
		hql.append("cb.saldo_inicial as saldo_inicial, \n ");
		hql.append("cb.id, \n ");
		hql.append("cb.tipo ");
		hql.append("from conta_bancaria cb \n ");
		hql.append(" where 1 = 1 ");

		if (filtro.getContas().length > 0) {
			hql.append(" and cb.id in (:contas)");
		}

		Query query = manager.createNativeQuery(hql.toString());
		// query.setParameter("data", "'"+sdf.format(filtro.getDataFinal())
		// +"'");

		if (filtro.getContas().length > 0) {
			List<Integer> list = new ArrayList<>();
			Integer[] mList = filtro.getContas();

			for (int i = 0; i < filtro.getContas().length; i++) {
				list.add(mList[i]);
			}

			query.setParameter("contas", list);
		}

		List<Object[]> result = query.getResultList();
		List<ContaBancaria> contas = new ArrayList<>();

		for (Object[] object : result) {
			ContaBancaria conta = new ContaBancaria();
			conta.setNomeBanco(object[0] != null ? object[0].toString() : "");
			conta.setNomeConta(object[1] != null ? object[1].toString() : "");
			conta.setNumeroAgencia(object[2] != null ? object[2].toString() : "");
			conta.setTotalEntrada(object[3] != null ? new BigDecimal(object[3].toString()) : BigDecimal.ZERO);
			conta.setTotalSaida(object[4] != null ? new BigDecimal(object[4].toString()) : BigDecimal.ZERO);
			conta.setSaldoInicial(object[5] != null ? new BigDecimal(object[5].toString()) : BigDecimal.ZERO);
			conta.setId(new Long(object[6].toString()));
			conta.setTipo(object[7] != null ? object[7].toString() : "");
			contas.add(conta);
		}

		return contas;
	}

	public List<RendimentoConta> obterRendimentosRelatorio(ContaBancaria conta){
		String sql = "select to_char(rc.datapagamento, 'dd/MM/yyyy') as datapagamento\n" +
				"\t, competencia\n" +
				"\t, descricao\n" +
				"\t, rc.valor\n" +
				"\t, coalesce(b.nomebanco, '') || ' ag.: ' || cb.numero_agencia || (case when cb.digito_agencia is null then '' else '-' || cb.digito_agencia end) || ' conta: ' || cb.numero_conta || (case when cb.digito_conta is null then '' else '-' || cb.digito_conta end) as contarecebedora\n" +
				"\t, coalesce(bp.nomebanco, '') || ' ag.: ' || cbp.numero_agencia || (case when cbp.digito_agencia is null then '' else '-' || cbp.digito_agencia end) || ' conta: ' || cbp.numero_conta || (case when cbp.digito_conta is null then '' else '-' || cbp.digito_conta end) as contapagadora\n" +
				"\t, o.titulo as fontedoacao\n" +
				"\t, u.nomeusuario\n" +
				"from rendimento_conta rc\n" +
				"join conta_bancaria cb on cb.id = rc.conta_id\n" +
				"left join banco b on b.id = cb.banco_id\n" +
				"join conta_bancaria cbp on cbp.id = rc.contapagadora_id\n" +
				"left join banco bp on b.id = cbp.banco_id\n" +
				"join usuario u on u.id = rc.usuario_id\n" +
				"left join orcamento o on o.id = rc.orcamento_id\n" +
				"where rc.conta_id = :p_conta\n" +
				"order by rc.datapagamento desc";
		Query query = manager.createNativeQuery(sql);
		query.setParameter("p_conta", conta.getId());

		List<Object[]> list = query.getResultList();
		List<RendimentoConta> rendimentos = new ArrayList<>();

		for (Object[] obj :
				list) {
			RendimentoConta rendimentoConta = new RendimentoConta();
			rendimentoConta.setDatapagamento(obj[0].toString());
			rendimentoConta.setCompetencia(obj[1].toString());
			//rendimentoConta.setDescricao(obj[2].toString());
			rendimentoConta.setValor(new BigDecimal(obj[3].toString()));
			rendimentoConta.setContarecebedora(obj[4].toString());
			rendimentoConta.setContapagadora(obj[5].toString());
			rendimentoConta.setFontedoacao(obj[6].toString());
			rendimentoConta.setNomeusuario(obj[7].toString());

			rendimentos.add(rendimentoConta);
		}

		return rendimentos;
	}

	public List<ContaBancaria> obterContasRendimento(){
		String hql = "from ContaBancaria where tipo = 'RDT' order by id desc";

		Query query = manager.createQuery(hql);

		return query.getResultList();
	}

    public List<BaixaAplicacao> obterBaixas(Long id){
		String hql = "from BaixaAplicacao where idContaAplicacao = :p_id order by id desc";

		Query query = manager.createQuery(hql);
		query.setParameter("p_id", id);

		return query.getResultList();
	}

	public List<AplicacaoRecurso> obterAplicacoes(Long id){
		String hql = "from AplicacaoRecurso where idContaAplicacao = :p_id order by id desc";

		Query query = manager.createQuery(hql);
		query.setParameter("p_id", id);

		return query.getResultList();
	}

	public RendimentoConta salvarRendimento(RendimentoConta rendimentoConta){
		return this.manager.merge(rendimentoConta);
	}

	public FonteDoacaoConta salvarDoacao(FonteDoacaoConta fonteDoacaoConta){
		return this.manager.merge(fonteDoacaoConta);
	}

	public void removerRendimento(RendimentoConta rendimentoConta) {
		this.manager.remove(this.manager.find(RendimentoConta.class, rendimentoConta.getId()));
	}

	public void removerDoacao(FonteDoacaoConta fonteDoacaoConta) {
		this.manager.remove(this.manager.find(FonteDoacaoConta.class, fonteDoacaoConta.getId()));
	}

	public AlocacaoRendimento salvarAlocacaoRendimento(AlocacaoRendimento alocacaoRendimento){
		return this.manager.merge(alocacaoRendimento);
	}

	public void removerAlocacaoRendimento(AlocacaoRendimento alocacaoRendimento) {
		this.manager.remove(this.manager.find(AlocacaoRendimento.class, alocacaoRendimento.getId()));
	}

	public List<RendimentoConta> obterRendimentos(ContaBancaria contaBancaria){
		String hql = "from RendimentoConta where conta.id = :p_id";

		Query query = manager.createQuery(hql);
		query.setParameter("p_id", contaBancaria.getId());

		return query.getResultList();
	}

	public List<FonteDoacaoConta> obterDoacoes(ContaBancaria contaBancaria){
		String hql = "from FonteDoacaoConta where conta.id = :p_id";

		Query query = manager.createQuery(hql);
		query.setParameter("p_id", contaBancaria.getId());

		return query.getResultList();
	}

	public List<AlocacaoRendimento> obterAlocacoesRendimentos(ContaBancaria contaBancaria){
		String hql = "from AlocacaoRendimento where conta.id = :p_id";

		Query query = manager.createQuery(hql);
		query.setParameter("p_id", contaBancaria.getId());

		return query.getResultList();
	}
}
