package repositorio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.*;
import operator.Parcela;
import util.ArquivoFornecedor;
import util.ArquivoLancamento;
import util.DataUtil;
import util.DateConverter;
import util.Filtro;
import util.Util;

public class LancamentoRepository implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private @Inject EntityManager manager;

	@Inject
	private CalculatorRubricaRepositorio calculatorRubricaRepositorio;

	private TipoParcelamento tipoParcelamento;
	
	@Inject
	private LogRepositorio logRepositorio;

	public LancamentoRepository() {
	}

	public LancamentoRepository(EntityManager manager) {
		this.manager = manager;
	}

	public List<PrestacaoDeConta> getPrestacaoByIdAdiantamento(Long id) {
		return new ArrayList<>();
	}

	public void salvarDiaria(Lancamento diaria) {
		this.manager.merge(diaria);
	}

	public void deletarTodosPagamentos(Long id) {
		this.manager
				.createNativeQuery(" delete \n" + "from pagamento_lancamento \n"
						+ "where id in (select pl.id from pagamento_lancamento pl\n"
						+ "join lancamento_acao la on la.id = pl.lancamentoacao_id\n" + "where la.lancamento_id = :id)")
				.setParameter("id", id).executeUpdate();
	}

	public PagamentoLancamento buscarPagamentoByLancamentoAcao(Long id) {
		String jpql = "from PagamentoLancamento where lancamentoAcao.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		query.setMaxResults(1);
		return query.getResultList().size() > 0 ? (PagamentoLancamento) query.getResultList().get(0) : null;
	}

	public Boolean validarAdiantamento(Long id) {
		try {

			Lancamento lanc = this.manager.find(Lancamento.class, id);
			lanc.setStatusAdiantamento(StatusAdiantamento.VALIDADO);
			this.manager.merge(lanc);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public Boolean validarAdiantementoComLog(Long id, User usuario) {

		try {
			Lancamento lancamento = this.manager.find(Lancamento.class, id);
			lancamento.setStatusAdiantamento(StatusAdiantamento.VALIDADO);
			lancamento.setDataValidacao(new Date());
			this.manager.merge(lancamento);

			LogStatus log = new LogStatus();
			log.setUsuario(usuario);
			log.setData(new Date());
			log.setStatusLog(StatusCompra.VALIDADO);
			log.setSiglaPrivilegio("EDIT");

			log.setLancamento(lancamento);
			this.manager.merge(log);
			return true;

		} catch (Exception e) {
			return false;
		}

	}

	public Boolean validarLancamentos(Long id) {
		try {

			StringBuilder hql = new StringBuilder("update lancamento_acao  set status = 0  ");
			hql.append("WHERE lancamento_id = :id ");
			// hql.append("and to_char(datapagamento,'dd/MM/yyyy') = :data ");
			// hql.append("AND conta_id = :conta");

			Query query = this.manager.createNativeQuery(hql.toString());

			query.setParameter("id", id);
			// query.setParameter("data", data);
			// query.setParameter("conta", conta);
			query.executeUpdate();

			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public Boolean invalidarLancamentos(Long id) {
		try {

			StringBuilder hql = new StringBuilder("update lancamento_acao  set status = 1  ");
			hql.append("WHERE lancamento_id = :id ");
			// hql.append("and to_char(datapagamento,'dd/MM/yyyy') = :data ");
			// hql.append("AND conta_id = :conta");

			Query query = this.manager.createNativeQuery(hql.toString());

			query.setParameter("id", id);
			// query.setParameter("data", data);
			// query.setParameter("conta", conta);
			query.executeUpdate();

			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public Boolean devolverAdiantamento(Long id) {
		try {

			Lancamento lanc = this.manager.find(Lancamento.class, id);
			lanc.setStatusAdiantamento(StatusAdiantamento.DEVOLUCAO);
			this.manager.merge(lanc);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public Boolean AnalisarAdiantamento(Long id) {
		try {

			Lancamento lanc = this.manager.find(Lancamento.class, id);
			lanc.setStatusAdiantamento(StatusAdiantamento.EM_ANALISE);
			this.manager.merge(lanc);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public List<Adiantamento> buscarAdiantamentos(Long idConta) {
		// Alterado para contemplar o relatorio de adiantamento 13/09/2018
		// StringBuilder jpql = new StringBuilder("select ");
		// jpql.append(" (select g.nome from gestao g where g.id = l.gestao_id) as
		// gestao, ");
		// jpql.append(" to_char(l.data_emissao,'dd-MM-yyyy') as data_emissao,");
		// jpql.append(" l.numerodocumento,");
		// jpql.append(" (select c.nome_conta from conta_bancaria c where c.id =
		// l.contarecebedor_id) as favorecido,");
		// jpql.append("l.descricao,");
		// jpql.append("l.valor_total_com_desconto,");
		// jpql.append(" CASE WHEN (l.data_validacao is null) THEN");
		// jpql.append(" to_date(to_char(CURRENT_DATE, 'dd-MM-yyyy'),'dd-MM-yyyy') -
		// to_date(to_char((select max(data) from log_status where lancamento_id = l.id
		// and sigla_privilegio = 'ASP'), 'dd-MM-yyyy'),'dd-MM-yyyy')");
		// jpql.append(" ELSE to_date(to_char(l.data_validacao,
		// 'dd-MM-yyyy'),'dd-MM-yyyy') - to_date(to_char((select max(data) from
		// log_status where lancamento_id = l.id and sigla_privilegio = 'ASP'),
		// 'dd-MM-yyyy'),'dd-MM-yyyy')");
		// jpql.append(" END AS TOTAL_DIAS, l.id ,");
		// jpql.append(" to_char(l.data_validacao,'dd/MM/yyyy') as data_validacao , ");
		// jpql.append(" (select sum(la.valor_total_com_desconto) from lancamento la
		// where la.idAdiantamento = l.id) as valorPrestado , ");
		// jpql.append("l.statusadiantamento");
		// //Alterado 03/09/2018 para correção de bug D08 by Christophe
		// jpql.append(" from lancamento l where l.tipolancamento = 'ad' and
		// l.versionlancamento = 'MODE01' and l.contarecebedor_id = :idConta and
		// l.statuscompra = 'CONCLUIDO' order by l.data_emissao desc");

		StringBuilder jpql = new StringBuilder(" select ");
		jpql.append("(select g.nome from gestao g where g.id = l.gestao_id) as gestao0, ");
		jpql.append("to_char(l.data_emissao,'dd-MM-yyyy') as data_emissao1, ");
		jpql.append(
				"l.numerodocumento,(select c.nome_conta from conta_bancaria c where c.id = l.contarecebedor_id) as favorecido3, ");
		jpql.append("l.descricao, l.valor_total_com_desconto, ");
		jpql.append("CASE WHEN (l.data_validacao is null) THEN ");
		jpql.append(
				"to_date(to_char(CURRENT_DATE, 'dd-MM-yyyy'),'dd-MM-yyyy') - to_date(to_char((select max(data) from log_status  where lancamento_id = l.id and sigla_privilegio = 'ASP'), 'dd-MM-yyyy'),'dd-MM-yyyy') ");
		jpql.append(
				"ELSE to_date(to_char(l.data_validacao, 'dd-MM-yyyy'),'dd-MM-yyyy') - to_date(to_char((select max(data) from log_status  where lancamento_id = l.id and sigla_privilegio = 'ASP'), 'dd-MM-yyyy'),'dd-MM-yyyy') ");
		jpql.append("END AS TOTAL_DIAS6, l.id ,");
		jpql.append("to_char(l.data_validacao,'dd/MM/yyyy') as data_validacao8 , ");
		jpql.append(
				"(select sum(la.valor_total_com_desconto) from lancamento la where la.idAdiantamento =  l.id and la.tipolancamento != 'reenb') as valorPrestado9 , ");
		jpql.append(
				"l.statusadiantamento, p.codigo as codigo_11,orc.titulo as orcamento_titulo_12 ,p.nome as projeto_nome_13,(select cc.nome from componente_class cc where   cc.id = pr.componente_id ) as componente_nome_14,  (select sc.nome from sub_componente  sc where sc.id = pr.subcomponente_id) as subComponte_nome_15, (select ru.nome from rubrica ru where ru.id = ro.rubrica_id) as rubrica_nome_16, ");
		jpql.append(
				"(select con.nome_conta from conta_bancaria con where con.id = pl.conta_id) as pagador_conta_17, (select conn.nome_conta from conta_bancaria conn where conn.id = pl.contarecebedor_id) as recebedor_conta_18, ");
		jpql.append(
				"(select sum(la.valor_total_com_desconto) from lancamento la where la.idAdiantamento =  l.id and la.tipolancamento = 'reenb') as valorEntrada19 ");
		jpql.append("from lancamento l ");
		jpql.append("inner join lancamento_acao la on la.lancamento_id = l.id ");
		jpql.append("inner join pagamento_lancamento pl on pl.lancamento_id = l.id ");
		jpql.append("inner join projeto_rubrica pr on pr.id = la.projetorubrica_id ");
		jpql.append("inner join rubrica_orcamento ro on pr.rubricaorcamento_id = ro.id ");
		jpql.append("inner join orcamento orc on ro.orcamento_id = orc.id ");
		jpql.append("inner join projeto p on p.id  = pr.projeto_id ");
		jpql.append("inner join rubrica r on ro.rubrica_id = r.id ");

		if (idConta != null) {
			jpql.append(
					"where l.tipolancamento  = 'ad' and l.versionlancamento = 'MODE01'  and l.contarecebedor_id = :idConta and l.statuscompra = 'CONCLUIDO' order by l.data_emissao desc ");
		} else {
			jpql.append(
					"where l.tipolancamento  = 'ad' and l.versionlancamento = 'MODE01'  and l.statuscompra = 'CONCLUIDO' order by l.data_emissao desc ");

		}

		Query query = this.manager.createNativeQuery(jpql.toString());

		if (idConta != null) {
			query.setParameter("idConta", idConta);
		}
		List<Object[]> result = query.getResultList();
		List<Adiantamento> adiantamentos = new ArrayList<>();
		try {
			for (Object[] object : result) {
				Adiantamento ad = new Adiantamento(object);
				adiantamentos.add(ad);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return adiantamentos;
	}

	public void removerItemPedido(ItemPedido itemPedido) {
		this.manager.remove(this.manager.find(ItemPedido.class, itemPedido.getId()));
	}

	public boolean removerItemCompra(Long item) {
		try {
			this.manager.remove(this.manager.find(ItemCompra.class, item));
		} catch (Exception e) {
			e.getMessage();
			return false;
		}

		return true;
	}

	public boolean cancelarItemCompra(Long item) {
		try {
			ItemCompra itemCompra = this.manager.find(ItemCompra.class, item);
			itemCompra.setCancelado(true);
			this.manager.merge(itemCompra);
		} catch (Exception e) {
			e.getMessage();
			return false;
		}

		return true;
	}

	public Lancamento getLancamentoPorId(Long id) {
		return this.manager.find(Lancamento.class, id);
	}

	public LancamentoAvulso getLancamentoAvulsoPorId(Long id) {
		return this.manager.find(LancamentoAvulso.class, id);
	}

	public LancamentoDiversos getLancamentoDiversosPorId(Long id) {
		return this.manager.find(LancamentoDiversos.class, id);
	}

	public LancamentoAcao getLancamentoAcaoPorId(Long id) {
		return this.manager.find(LancamentoAcao.class, id);
	}

	public LancamentoAcao getLancamentoAcaoPorLancamento(Long id) {
		String jpql = "from LancamentoAcao where lancamento.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return (LancamentoAcao) query.getResultList().get(0);
	}

	public List<Observacao> getObservacoes(Compra compra) {
		String jpql = "from Observacao where lancamento.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", compra.getId());
		return query.getResultList();
	}

	public Boolean excluirItemPedidoByItemCompra(ItemCompra item) {

		String hql = "delete ItemPedido i where i.idItemCompra = :id_item  ";
		Query query = this.manager.createQuery(hql);
		query.setParameter("id_item", item.getId());
		try {
			int reuslt = query.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public Boolean excluirItemPedido(ItemPedido item) {
		this.manager.remove(this.manager.find(ItemPedido.class, item.getId()));
		return true;
	}

	public Date getDataDeAprovacao(Long compra) {

		String jpql = "SELECT NEW LogStatus(l.data) from LogStatus l where l.lancamento.id = :id and l.statusLog = :stt ";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", compra);
		query.setParameter("stt", StatusCompra.APROVADO);
		query.setMaxResults(1);
		if (query.getResultList().isEmpty()) {
			return null;
		} else {
			return ((LogStatus) query.getResultList().get(0)).getData();
		}
	}

	public List<LancamentoAcao> getLancamentosEmAcoes(Long id) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW LancamentoAcao(la.id, la.valor ,la.acao.codigo, la.fontePagadora.nome) FROM LancamentoAcao la where la.lancamento.id = :id ");
		Query query = this.manager.createQuery(jpql.toString());
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public List<ArquivoLancamento> getDocumentosByLancmento(Long id) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW ArquivoLancamento(aq.id,aq.nome,aq.data, aq.path) FROM ArquivoLancamento aq where aq.lancamento.id =  :id order by aq.data  ");
		Query query = this.manager.createQuery(jpql.toString());
		query.setParameter("id", id);
		return query.getResultList();
	}

	public List<PagamentoLancamento> getPagamentosINLancamento(Long id) {
		StringBuilder jpql = new StringBuilder("SELECT NEW     ");
		jpql.append(" PagamentoLancamento(pa.id, pa.valor, pa.dataPagamento,");
		jpql.append("pa.numeroDaParcela, pa.quantidadeParcela,");
		jpql.append("pa.conta.nomeConta, pa.contaRecebedor.nomeConta ,pa.lancamentoAcao.acao.codigo,");
		jpql.append(
				"pa.lancamentoAcao.fontePagadora.nome) FROM PagamentoLancamento pa where pa.lancamentoAcao.lancamento.id = :id order by pa.dataPagamento");
		Query query = this.manager.createQuery(jpql.toString());
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public List<ItemPedido> getItensPedidosDetalhados(Filtro filtro) {

		StringBuilder jpql = new StringBuilder("select ");
		jpql.append("compra.id AS sc");
		jpql.append(",pedido.id as pedido");
		jpql.append(",to_char(compra.data_emissao,'dd/MM/yyyy')  as data_sc");
		jpql.append(
				", to_char((select log.data from log_status log where log.lancamento_id = compra.id and log.statuslog = 2 LIMIT 1),'dd/MM/yyyy') as data_aprovacao");
		jpql.append(",to_char(pedido.data_emissao,'dd/MM/yyyy')  as data_pedido");
		jpql.append(
				",(select colab.nome from colaborador colab where colab.id = compra.solicitante_id) as solicitante");
		// jpql.append(",CASE WHEN (pedido is null) ");
		// jpql.append("THEN (select cat.nome from categoria_despesa cat where cat.id =
		// (select po.categoriadespesa_id from produto po where po.id = (select
		// produto_id from item_compra tc where ip.item_compra_id = tc.id)) ");
		// jpql.append("ELSE ip.categoria END as categoria");
		jpql.append(", (select po.categoria from produto po where po.id = ic.produto_id) as categoria");
		jpql.append(",compra.statuscompra");
		jpql.append(",CASE WHEN (pedido is null) ");
		jpql.append("THEN (select gest.nome from gestao gest where gest.id = compra.gestao_id) ");
		jpql.append("ELSE (select gest.nome from gestao gest where gest.id = pedido.gestao_id) END as gestao ");
		jpql.append(",CASE WHEN (pedido is null) ");
		jpql.append("THEN  (select loc.mascara from localidade loc where loc.id = compra.localidade_id) ");
		jpql.append(
				"ELSE (select loc.mascara from localidade loc where loc.id = pedido.localidade_id) END as destino ");
		jpql.append(",CASE WHEN (pedido is null) ");
		jpql.append("THEN ic.quantidade ");
		jpql.append("ELSE ip.quantidade END as quantidade ");
		jpql.append(",CASE WHEN (pedido is null) ");
		jpql.append("THEN (select po.descricao from produto po where po.id = ic.produto_id) ");
		jpql.append("ELSE ip.descricaoproduto END as descricao ");
		jpql.append(
				",(select forn.nome_fantasia from fornecedor forn where forn.id = pedido.fornecedor_id) as fornecedor");
		jpql.append(",CASE WHEN (pedido is null) ");
		jpql.append("THEN ic.unidade ");
		jpql.append("ELSE ip.unidade END as unidade_medida ");
		jpql.append(
				",(select usu.nomeusuario from usuario usu where usu.id = pedido.usuariogeradorpedido_id) as comprador ");
		jpql.append(", compra.booturgencia, to_char(compra.data_conclusao,'dd/MM/yyyy') as data_conclusao, ");
		jpql.append(
				"CASE WHEN (pedido is null) THEN 0 ELSE (ip.quantidade * ip.valor_unitario_com_desconto) END as valor_total,");
		jpql.append(
				"CASE WHEN (pedido is null) THEN 0 ELSE pedido.valor_total_sem_desconto END as valor_total_pedido, ");
		jpql.append("CASE WHEN (pedido is null) THEN 0 ELSE ip.valor_unitario_com_desconto END as valor_unitario, ");

		jpql.append("CASE WHEN (pedido is null) THEN 'Não Informado' ELSE pedido.nota_fiscal END as notaFiscal, ");
		jpql.append(
				"CASE WHEN (pedido is null) THEN 'Não Informado' ELSE to_char((select max(datapagamento) from pagamento_lancamento where (select lancamento_id from lancamento_acao where id =  lancamentoacao_id) = pedido.id and stt = 'EFETIVADO'),'dd/MM/yyyy') END as dataPagamento, ");

		jpql.append(
				"(select p.tipo from produto p where p.id = (select it.produto_id from item_compra as it where it.id = ic.id)) as tipo ");
		jpql.append("from lancamento compra ");
		jpql.append("join item_compra ic on ic.compra_id = compra.id ");
		jpql.append("left join item_pedido ip on ip.item_compra_id =  ic.id ");
		jpql.append("left join lancamento pedido on ip.pedido_id = pedido.id where 1 = 1 ");

		if (filtro.getUrgencia()) {
			jpql.append(" and compra.booturgencia = :urgencia ");
		}

		if (filtro.getStatusCompra() != null) {
			jpql.append(" and compra.statuscompra = :status_compra");
		}

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			jpql.append(" and compra.id = :codigo");
		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			jpql.append(
					" and compra.solicitante_id in (select col.id from colaborador col where col.nome ilike :nome ) ");
		}

		if (filtro.getDataInicio() != null) {
			if (filtro.getDataFinal() != null) {
				jpql.append(" and compra.data_emissao >= :data_inicio and compra.data_emissao <= :data_final ");
			} else {
				jpql.append(" and compra.data_emissao >= :data_inicio");
			}
		}

		if (filtro.getGestaoID() != null) {
			jpql.append(" and pedido.gestao_id = :gestaoID ");
		}

		if (filtro.getLocalidadeID() != null) {
			jpql.append(" and pedido.localidade_id = :localidadeID ");
		}

		if (filtro.getDescricaoProduto() != null && !filtro.getDescricaoProduto().equals("")) {
			jpql.append(" and ip.descricaoproduto ilike :descricao_produto   ");
		}

		if (filtro.getNotaFiscal() != null && !filtro.getNotaFiscal().equals("")) {
			jpql.append(" and pedido.nota_fiscal ilike :nota_fiscal ");
		}

		if (filtro.getNomeFornecedor() != null && !filtro.getNomeFornecedor().equals("")) {
			jpql.append(
					"and pedido.fornecedor_id in (select fornec.id from fornecedor fornec where fornec.nome_fantasia ilike :fornecedor  ");
			jpql.append("or fornec.cnpj ilike :fornecedor ");
			jpql.append("or fornec.razao_social ilike :fornecedor");
			jpql.append(")");
		}

		Query query = this.manager.createNativeQuery(jpql.toString());

		if (filtro.getUrgencia()) {
			query.setParameter("urgencia", filtro.getUrgencia());
		}

		if (filtro.getStatusCompra() != null) {
			query.setParameter("status_compra", filtro.getStatusCompra().toString());
		}

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			query.setParameter("codigo", Long.valueOf(filtro.getCodigo()));
		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			query.setParameter("nome", "%" + filtro.getNome() + "%");
		}

		if (filtro.getDataInicio() != null) {
			if (filtro.getDataFinal() != null) {
				query.setParameter("data_inicio", filtro.getDataInicio());
				query.setParameter("data_final", filtro.getDataFinal());
			} else {
				query.setParameter("data_inicio", filtro.getDataInicio());
			}
		}

		if (filtro.getGestaoID() != null) {
			query.setParameter("gestaoID", filtro.getGestaoID());
		}

		if (filtro.getLocalidadeID() != null) {
			query.setParameter("localidadeID", filtro.getLocalidadeID());
		}

		if (filtro.getDescricaoProduto() != null && !filtro.getDescricaoProduto().equals("")) {
			query.setParameter("descricao_produto", "%" + filtro.getDescricaoProduto() + "%");
		}

		if (filtro.getNotaFiscal() != null && !filtro.getNotaFiscal().equals("")) {
			query.setParameter("nota_fiscal", "%" + filtro.getNotaFiscal() + "%");
		}

		if (filtro.getNomeFornecedor() != null && !filtro.getNomeFornecedor().equals("")) {
			query.setParameter("fornecedor", "%" + filtro.getNomeFornecedor() + "%");
		}

		List<Object[]> result = query.getResultList();
		List<ItemPedido> itens = new ArrayList<>();
		for (Object[] objects : result) {

			ItemPedido item = new ItemPedido();
			item.setIdSC(new Long(objects[0].toString()));
			if (objects[1] != null)
				item.setIdPedido(new Long(objects[1].toString()));

			item.setDataEmissaoSC(objects[2] != null ? objects[2].toString() : "");
			item.setDataAprovacao(objects[3] != null ? objects[3].toString() : "");
			item.setDataEmissaoPedido(objects[4] != null ? objects[4].toString() : "");
			item.setNomeSolicitante(objects[5] != null ? objects[5].toString() : "");
			item.setCategoria(objects[6] != null ? objects[6].toString() : "");
			item.setStatusCompra(objects[7] != null ? objects[7].toString() : "");
			item.setNomeGestao(objects[8] != null ? objects[8].toString() : "");
			item.setNomeDestino(objects[9] != null ? objects[9].toString() : "");
			item.setQuantidade(new Double(objects[10] != null ? objects[10].toString() : ""));
			item.setDescricaoProduto(objects[11] != null ? objects[11].toString() : "");
			item.setNomeFornecedor(objects[12] != null ? objects[12].toString() : "");
			item.setUnidade(objects[13] != null ? objects[13].toString() : "");
			item.setNomeComprador(objects[14] != null ? objects[14].toString() : "");
			item.setBootUrgencia(objects[15] != null ? Boolean.valueOf(objects[15].toString()) : false);
			item.setDataConclusaoSC(objects[16] != null ? objects[16].toString() : "");
			item.setValorTotal(objects[17] != null ? new BigDecimal(objects[17].toString()) : BigDecimal.ZERO);
			item.setValorTotalPedido(objects[18] != null ? new BigDecimal(objects[18].toString()) : BigDecimal.ZERO);
			item.setValorUnitarioComDesconto(
					objects[19] != null ? new BigDecimal(objects[19].toString()) : BigDecimal.ZERO);
			item.setTipo(objects[22] != null ? objects[22].toString() : "");

			item.setNotaFiscal(objects[20] != null ? objects[20].toString() : "");
			item.setDataPagamento(objects[21] != null ? objects[21].toString() : "");

			itens.add(item);

			// TODO:VALOR TOTAL PEDIDO
		}

		return itens;
	}

	public List<LancamentoAuxiliar> getlancamentosParaGerenciar(Filtro filtro) {
		StringBuilder hql = new StringBuilder("select ");
		hql.append("to_char(l.data_emissao,'yyyy-mm-dd') as data_emissao,");// 1
		hql.append("to_char(l.data_pagamento,'yyyy-mm-dd') as data_pagamento,");// 2
		hql.append("l.numerodocumento as documento,");// 3
		hql.append("l.id as lancamento,");// 4
		hql.append("(select count(la.id)  from lancamento_acao la where la.lancamento_id = l.id) as acoes,");// 5
		hql.append(
				"(select count(pl.id)  from pagamento_lancamento  pl where (select lancamento_id from lancamento_acao where id =  pl.lancamentoacao_id)  = l.id) as pagamentos,");// 6
		hql.append("(select count(a.id) from arquivo_lancamento a where a.lancamento_id = l.id), ");// 7
		hql.append(
				"(select sum(pl.valor)  from pagamento_lancamento  pl where pl.lancamento_id = l.id) as valor_lancamento,"); // 8
		hql.append(
				"(select cb.nome_conta from conta_bancaria cb where cb.id = l.contarecebedor_id) as recebedor_conta, ");// 9
		hql.append("l.descricao ");
		hql.append(
				"from lancamento l where 1 = 1 and (tipo = 'lanc_av' or tipo = 'SolicitacaoPagamento' or tipo = 'pedido') ");// 10
		hql.append(" and (select count(pl.id)  from pagamento_lancamento  pl where pl.lancamento_id = l.id) > 0  ");

		if (filtro.getLancamentoID() != null) {
			hql.append(" and l.id = :id ");
		}

		if (filtro.getNumeroDocumento() != null && !filtro.getNumeroDocumento().equals("")) {
			hql.append(" and l.numerodocumento = :documento ");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			hql.append(" and l.data_pagamento between '" + sdf.format(filtro.getDataInicio()) + "' and ");
			hql.append(" '" + sdf.format(filtro.getDataFinal()) + "' ");

		} else if (filtro.getDataInicio() != null) {
			hql.append(" and l.data_emissao >= '" + sdf.format(filtro.getDataInicio()) + "' ");

		} else if (filtro.getDataFinal() != null) {
			hql.append(" and l.data_pagamento >= '" + sdf.format(filtro.getDataFinal()) + "' ");
		}

		Query query = this.manager.createNativeQuery(hql.toString());

		if (filtro.getLancamentoID() != null) {
			query.setParameter("id", filtro.getLancamentoID());
		}

		if (filtro.getNumeroDocumento() != null && !filtro.getNumeroDocumento().equals("")) {
			query.setParameter("documento", filtro.getNumeroDocumento());
		}

		List<Object[]> result = query.getResultList();
		List<LancamentoAuxiliar> lancamentos = new ArrayList<>();

		for (Object[] objects : result) {

			LancamentoAuxiliar lancamento = new LancamentoAuxiliar();

			lancamento.setDataEmissao(DataUtil.converteDataSql(objects[0].toString()));
			lancamento.setDataPagamento(DataUtil.converteDataSql(objects[1].toString()));
			lancamento.setNumeroDocumento(objects[2] != null ? objects[2].toString() : "");
			lancamento.setId(new Long(objects[3].toString()));

			lancamento.setQuantidadeAcoes(Integer.valueOf(objects[4].toString()));
			lancamento.setQuantidadePagamentos(Integer.valueOf(objects[5].toString()));
			lancamento.setQuantidadeArquivos(Integer.valueOf(objects[6].toString()));
			lancamento.setSaida(new BigDecimal(objects[7].toString()));
			lancamento.setRecebedor(objects[8] != null ? objects[8].toString() : "");
			lancamento.setDescricao(objects[9] != null ? objects[9].toString() : "");
			lancamentos.add(lancamento);
		}
		return lancamentos;
	}

	public List<Diaria> findDiariasbyViagem(SolicitacaoViagem viagem) {
		String jpql = "from Diaria";
		Query query = this.manager.createQuery(jpql);
		// query.setParameter("id",viagem.getId());

		return query.getResultList();
	}

	public Integer buscarItensPedidosCancelados(Long id) {
		String jpql = "from ItemPedido where pedido.id = :id and cancelado = :cancel";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		query.setParameter("cancel", true);
		return query.getResultList().size();
	}

	public Pedido getPedidoPorCompraEFornecedor(Long fornecedor, Compra compra) {
		String jpql = "from Pedido where fornecedor.id = :fornecedor and compra = :compra";
		Query query = manager.createQuery(jpql);
		query.setParameter("fornecedor", fornecedor);
		query.setParameter("compra", compra);
		query.setMaxResults(1);
		return query.getResultList().size() > 0 ? (Pedido) query.getResultList().get(0) : new Pedido();
	}

	public Observacao findObservacaoByCompra(Long idCompra, Long idItem) {
		String jpql = "from Observacao o where o.lancamento.id = :id_compra and o.itemCompra.id = :id_item";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id_compra", idCompra);
		query.setParameter("id_item", idItem);
		query.setMaxResults(1);
		return query.getResultList().size() > 0 ? (Observacao) query.getResultList().get(0) : new Observacao();
	}

	public ArquivoLancamento salvarArquivo(ArquivoLancamento arquivo) {
		// arquivo.setLancamento(this.manager.find(Lancamento.class,
		// arquivo.getLancamento().getId()));
		return this.manager.merge(arquivo);
	}

	public void salvarObservacao(Observacao obs) {
		this.manager.merge(obs);
	}

	public List<ArquivoLancamento> getArquivosByLancamento(Long id) {
		StringBuilder jpql = new StringBuilder("from ArquivoLancamento al where al.lancamento.id = :id ");
		Query query = this.manager.createQuery(jpql.toString());
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public List<ArquivoLancamento> getArquivosByLancamento(Long id, Long idCompra) {
		StringBuilder jpql = new StringBuilder("from ArquivoLancamento al where al.lancamento.id = :id ");
		jpql.append("or ");
		jpql.append("al.lancamento.id = :id_compra");

		Query query = this.manager.createQuery(jpql.toString());
		query.setParameter("id", id);
		query.setParameter("id_compra", idCompra);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public List<ArquivoLancamento> getNewArquivosByLancamento(Long id) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW ArquivoLancamento(aq.nome, aq.conteudo, aq.path) FROM ArquivoLancamento aq where aq.lancamento.id =  :id  ");
		Query query = this.manager.createQuery(jpql.toString());
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public List<ArquivoFornecedor> getArquivosByFornecedor(Long id) {
		String jpql = "from ArquivoFornecedor af where af.fornecedor.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public void removerArquivo(ArquivoLancamento arquivo) {
		manager.remove(this.manager.find(ArquivoLancamento.class, arquivo.getId()));
	}

	public Lancamento getLancamentoById(Long id) {
		return this.manager.find(Lancamento.class, id);
	}

	public Lancamento getLancamento(Long id) {
		String jpql = "from Lancamento l where l.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return (Lancamento) query.getSingleResult();
	}

	public LancamentoAuxiliar getLancamentoAuxiliarById(Long id) {
		LancamentoAuxiliar adiantamento = new LancamentoAuxiliar();
		Lancamento lanc = this.manager.find(Lancamento.class, id);
		adiantamento.setNumeroDocumento(lanc.getNumeroDocumento());
		adiantamento.setValorTotalComDesconto(lanc.getValorTotalComDesconto());
		adiantamento.setDataPagamento(lanc.getDataPagamento());
		adiantamento.setRecebedor(lanc.getContaRecebedor().getNomeConta());
		adiantamento.setContaRecebedor(lanc.getContaRecebedor());
		adiantamento.setId(lanc.getId());

		if (lanc.getStatusAdiantamento() != null) {

			adiantamento.setStatusAdiantamento(lanc.getStatusAdiantamento());

			if (lanc.getStatusAdiantamento().compareTo(StatusAdiantamento.EM_ABERTO) == 0) {
				adiantamento.setPodeEditarAdiantamento(true);
			} else {
				adiantamento.setPodeEditarAdiantamento(false);
			}

		}

		return adiantamento;
	}

	public List<LancamentoAvulso> buscarPrestacaoDeContas(Long idAdiantamento, Long idConta) {
		String jpql = "from LancamentoAvulso where idAdiantamento = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", idAdiantamento);
		return query.getResultList();

	}

	public List<LancamentoAvulso> buscarPrestacaoDeContasEntrada(Long idAdiantamento, Long idConta) {
		String jpql = "from LancamentoAvulso where idAdiantamento = :id and contaRecebedor.id = :id_recebedor";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", idAdiantamento);
		query.setParameter("id_recebedor", idConta);
		return query.getResultList();

	}

	public boolean deleteFromPagamentoByPedido(Pedido pedido) {

		try {
			this.manager.createQuery("delete from PagamentoLancamento as pl where pl.lancamento.id = :id")
					.setParameter("id", pedido.getId()).executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public boolean removeTrancacoesPedido(Pedido pedido) {
		this.manager.createQuery("delete from LancamentoAcao as la where la.lancamento.id = :id")
				.setParameter("id", pedido.getId()).executeUpdate();
		this.manager.createQuery("delete from PagamentoLancamento as pl where pl.lancamento.id = :id")
				.setParameter("id", pedido.getId()).executeUpdate();
		return true;
	}

	public Pedido salvarPedidoExpedicao(Pedido pedido, ControleExpedicao controleExpedicao) {
		this.manager.merge(controleExpedicao);
		return this.manager.merge(pedido);
	}

	public List<LancamentoDiversos> getListaDiversos2(Filtro filtro) {

		StringBuilder hql = new StringBuilder("");
		hql.append("select l.id as id_lancamento_00,");
		hql.append("to_char(l.data_emissao,'DD-MM-YYYY') as data_emissao_01,");
		hql.append("to_char(l.data_pagamento,'DD-MM-YYYY') as data_pagamento_02,");
		hql.append(
				"(select cb.nome_conta from conta_bancaria cb where  cb.id  = l.contapagador_id) as conta_pagador_03,");
		hql.append(
				"(select cb.nome_conta from  conta_bancaria cb where cb.id = l.contarecebedor_id) as conta_recebedor_04,");
		hql.append(" l.valor_total_com_desconto as valor_total_lancamento_5,");
		hql.append(" l.id as codigo_lancamento_6,");
		hql.append("l.nota_fiscal as numero_7,");
		hql.append("l.descricao as descricao_8,");
		hql.append("l.tipo_documento_fiscal as tipo_documento_fiscal_9,");
		hql.append("l.nota_fiscal as nota_fiscal_10,");
		hql.append("l.statuscompra as status_11");
		hql.append(" from lancamento l " + "inner join pagamento_lancamento pl on pl.lancamento_id = l.id \n");
		hql.append("where  1 = 1    and l.tipo = 'lancamento_diversos'  \n");

		// hql.append(" and l.statusCompra = 'CONCLUIDO' ");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (filtro.getLancamentoID() != null) {
			hql.append(" and l.id = :lancamento");
		}

		if (filtro.getNumeroDocumento() != "" && filtro.getNumeroDocumento() != null) {
			hql.append(" and l.nota_fiscal = :pNumeroDocumento ");
		}

		if (filtro.getProjetoId() != null) {
			hql.append(
					" and :projeto in (select pr.projeto_id from projeto_rubrica pr where pr.id in (select la.projetorubrica_id from lancamento_acao la where la.lancamento_id = l.id))");
		}

		if (filtro.getDescricao() != "" && filtro.getDescricao() != null) {
			hql.append(" and l.descricao like :descricao ");
		}

		// Codigo alterado para separar busca por contas de pagador e fornecedor
		// 03/09/2018 by Christophe
		// after
		// if (filtro.getIdConta() != null) {
		// hql.append(" and (pl.contarecebedor_id = :contaa or pl.conta_id = :contaa)
		// ");
		// }
		// before

		if (filtro.getFornecedor() != null && filtro.getFornecedor().getId() != null) {
			hql.append(" and  pl.contarecebedor_id = :fornecedor ");
		}
		if (filtro.getPagador() != null && filtro.getPagador().getId() != null) {
			hql.append(" and pl.conta_id = :pagador ");
		}
		// end
		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			hql.append(" and to_char(data_pagamento, 'YYYY-MM-DD') >= '" + sdf.format(filtro.getDataInicio())
					+ "' and to_char(data_pagamento, 'YYYY-MM-DD') <=" + "'" + sdf.format(filtro.getDataFinal())
					+ "' ");

		} else if (filtro.getDataInicio() != null) {
			hql.append(" and to_char(data_pagamento, 'YYYY-MM-DD') >= '" + sdf.format(filtro.getDataInicio()) + "' ");

		} else if (filtro.getDataFinal() != null) {
			hql.append(" and to_char(data_pagamento, 'YYYY-MM-DD') <= '" + sdf.format(filtro.getDataFinal()) + "' ");
		}
		// Alterado para Ordenar 22/09/2018 by Christophe
		hql.append(" order by l.id desc ");
		Query query = manager.createNativeQuery(hql.toString());

		if (filtro.getLancamentoID() != null) {
			query.setParameter("lancamento", filtro.getLancamentoID());
		}

		if (filtro.getDescricao() != "" && filtro.getDescricao() != null) {
			query.setParameter("descricao", "%" + filtro.getDescricao() + "%");
		}

		if (filtro.getProjetoId() != null) {
			query.setParameter("projeto", filtro.getProjetoId());
		}

		// Codigo alterado para separar busca por contas de pagador e fornecedor
		// 03/09/2018 by Christophe
		// after
		// if (filtro.getIdConta() != null) {
		// query.setParameter("contaa", filtro.getIdConta());
		// }
		// before
		if (filtro.getFornecedor() != null && filtro.getFornecedor().getId() != null) {
			query.setParameter("fornecedor", filtro.getFornecedor().getId());
		}

		if (filtro.getPagador() != null && filtro.getPagador().getId() != null) {
			query.setParameter("pagador", filtro.getPagador().getId());
		}

		// END

		if (filtro.getNumeroDocumento() != null && filtro.getNumeroDocumento() != "") {
			query.setParameter("pNumeroDocumento", filtro.getNumeroDocumento());
		}

		List<LancamentoDiversos> retorno = new ArrayList<LancamentoDiversos>();
		List<Object[]> result = query.getResultList();

		LancamentoDiversos lAux = new LancamentoDiversos();

		for (Object[] object : result) {

			lAux = new LancamentoDiversos();
			lAux.setId(new Long(object[0].toString()));
			lAux.setCodigo(object[0].toString());
			lAux.setNumeroDocumento(Util.getNullValue(object[7], " "));
			lAux.setDataPagamento(DateConverter.converteDataSql(object[2].toString()));
			lAux.setContaPagadorStr(object[3] != null ? object[3].toString() : "");
			lAux.setContaRecebedorStr(object[4] != null ? object[4].toString() : "");
			lAux.setValorTotalComDesconto(object[5] != null ? new BigDecimal(object[5].toString()) : BigDecimal.ZERO);
			lAux.setDescricao(object[8] != null ? object[8].toString() : "");
			lAux.setTipoDocumentoFiscal(object[9] != null ? TipoDeDocumentoFiscal.valueOf(object[9].toString())
					: TipoDeDocumentoFiscal.OUTRO);
			lAux.setNotaFiscal(object[10] != null ? object[10].toString() : "");
			lAux.setStatusCompra(object[11] != null ? StatusCompra.valueOf(object[11].toString()) : null);
			retorno.add(lAux);
		}

		return retorno;
	}

	public List<LancamentoDiversos> getListaDiversos(Filtro filtro) {

		// public Lancamento(Long id, String documento, String descricao, Date
		// dataPagamento, String nomePagador, String nomeRecebedor, BigDecimal
		// valorTotal){
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Lancamento(l.id,l.notaFiscal,l.descricao,l.dataPagamento,l.contaPagador.nomeConta,l.contaRecebedor.nomeConta,l.valorTotalComDesconto");
		jpql.append(") FROM Lancamento l where 1 = 1  and l.class = :classe1  and l.statusCompra = :statusCompra");

		if (filtro.getNumeroDocumento() != null && !filtro.getNumeroDocumento().equals("")) {
			jpql.append(" and l.numeroDocumento = :documento ");
		}

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			jpql.append(" and l.id = :codigo ");
		}

		if (filtro.getIdConta() != null && !(filtro.getIdConta().longValue() == 0l)) {
			jpql.append(" and ( l.contaPagador.id = :id_conta  or l.contaRecebedor.id = :id_conta ) ");
		}

		if (filtro.getDescricao() != null && !filtro.getDescricao().equals("")) {
			jpql.append(" and lower(l.descricao) like lower(:descricao) ");
		}

		if (filtro.getDataInicio() != null) {
			if (filtro.getDataFinal() != null) {
				jpql.append(" and l.dataEmissao between :data_inicio and :data_final");
			} else {
				jpql.append(" and l.dataEmissao > :data_inicio");
			}
		}

		jpql.append(" order by  l.dataEmissao ");

		Query query = manager.createQuery(jpql.toString());
		// mudanÃ§a rÃ¡pida, eliminar parametros apÃ³s atualizaÃ§Ã£o
		query.setParameter("classe1", "lancamento_diversos");

		query.setParameter("statusCompra", StatusCompra.CONCLUIDO);

		// query.setParameter("categoria", CategoriadeDespesa.DIARIA);

		if (filtro.getDataInicio() != null) {
			if (filtro.getDataFinal() != null) {
				query.setParameter("data_inicio", filtro.getDataInicio());
				query.setParameter("data_final", filtro.getDataFinal());
			} else {
				query.setParameter("data_inicio", filtro.getDataInicio());
			}
		}

		if (filtro.getNumeroDocumento() != null && !filtro.getNumeroDocumento().equals("")) {
			query.setParameter("documento", filtro.getNumeroDocumento());
		}

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			query.setParameter("codigo", Long.valueOf(filtro.getCodigo()));
		}

		if (filtro.getIdConta() != null && !(filtro.getIdConta().longValue() == 0l)) {
			/*
			 * jpql.append(" and lower(c.solicitante.nome) like lower(:nome)");
			 */
			query.setParameter("id_conta", filtro.getIdConta());
		}

		if (filtro.getDescricao() != null && !filtro.getDescricao().equals("")) {
			/*
			 * jpql.append(" and lower(c.solicitante.nome) like lower(:nome)");
			 */
			query.setParameter("descricao", "%" + filtro.getDescricao() + "%");
		}

		return query.getResultList();
	}

	public List<LancamentoAvulso> getListaLancamentosAvulso(Filtro filtro) {

		// public Lancamento(Long id, String documento, String descricao, Date
		// dataPagamento, String nomePagador, String nomeRecebedor, BigDecimal
		// valorTotal){
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Lancamento(l.id,l.numeroDocumento,l.descricao,l.dataPagamento,l.contaPagador.nomeConta,l.contaRecebedor.nomeConta,l.valorTotalComDesconto) FROM Lancamento l where 1 = 1  and (l.class = :classe1 or l.class = :classe2) and l.statusCompra = :statusCompra");

		if (filtro.getNumeroDocumento() != null && !filtro.getNumeroDocumento().equals("")) {
			jpql.append(" and l.numeroDocumento = :documento ");
		}

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			jpql.append(" and l.id = :codigo ");
		}

		if (filtro.getIdConta() != null) {
			jpql.append(" and ( l.contaPagador.id = :id_conta  or l.contaRecebedor.id = :id_conta ) ");
		}

		if (filtro.getDescricao() != null && !filtro.getDescricao().equals("")) {
			jpql.append(" and lower(l.descricao) like lower(:descricao) ");
		}

		if (filtro.getDataInicio() != null) {
			if (filtro.getDataFinal() != null) {
				jpql.append(" and l.dataEmissao between :data_inicio and :data_final");
			} else {
				jpql.append(" and l.dataEmissao > :data_inicio");
			}
		}

		jpql.append(" order by  l.dataEmissao ");

		Query query = manager.createQuery(jpql.toString());

		query.setParameter("classe1", "lanc_av");
		query.setParameter("classe2", "SolicitacaoPagamento");
		query.setParameter("statusCompra", StatusCompra.CONCLUIDO);

		// query.setParameter("categoria", CategoriadeDespesa.DIARIA);

		if (filtro.getDataInicio() != null) {
			if (filtro.getDataFinal() != null) {
				query.setParameter("data_inicio", filtro.getDataInicio());
				query.setParameter("data_final", filtro.getDataFinal());
			} else {
				query.setParameter("data_inicio", filtro.getDataInicio());
			}
		}

		if (filtro.getNumeroDocumento() != null && !filtro.getNumeroDocumento().equals("")) {
			query.setParameter("documento", filtro.getNumeroDocumento());
		}

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			query.setParameter("codigo", Long.valueOf(filtro.getCodigo()));
		}

		if (filtro.getIdConta() != null) {
			/*
			 * jpql.append(" and lower(c.solicitante.nome) like lower(:nome)");
			 */
			query.setParameter("id_conta", filtro.getIdConta());
		}

		if (filtro.getDescricao() != null && !filtro.getDescricao().equals("")) {
			/*
			 * jpql.append(" and lower(c.solicitante.nome) like lower(:nome)");
			 */
			query.setParameter("descricao", "%" + filtro.getDescricao() + "%");
		}

		return query.getResultList();
	}

	public List<LancamentoAvulso> getPrestacoes(Long id) {

		StringBuilder hql = new StringBuilder(" select ");

		hql.append(" to_char(l.data_emissao,'DD-MM-YYYY') as data_emissao, "); // 0
		hql.append(" to_char(l.data_pagamento,'DD-MM-YYYY') as data_pagamento, "); // 1
		hql.append(" l.numerodocumento as documento, ");// 2
		hql.append(" (select ac.codigo from acao ac where ac.id = la.acao_id) as acao,  ");// 3
		hql.append(" (select c.nome_conta  from conta_bancaria as c where c.id = pl.conta_id) as pagador, ");// 4
		hql.append("(select c.nome_conta  from conta_bancaria as c where c.id = pl.contarecebedor_id) as recebedor,");// 5
		hql.append(" l.descricao, ");// 6
		hql.append(" l.tipolancamento, ");// 7
		hql.append(" la.valor ");// 8

		hql.append(
				" from lancamento l join lancamento_acao la on l.id = la.lancamento_id join  pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");

		// hql.append(" acao a on a.id = la.acao_id ");

		hql.append(" where l.numerodocumento = '" + id + "'");

		// hql.append(" and (l.categoriadespesa != 'DIARIA' OR
		// l.categoriadespesa IS NULL) ");

		hql.append(" and (l.categoriadespesaclass_id != 30 OR l.categoriadespesaclass_id IS NULL) ");
		Query query = this.manager.createNativeQuery(hql.toString());

		List<LancamentoAvulso> lancamentos = new ArrayList<>();
		List<Object[]> result = query.getResultList();

		for (Object[] objects : result) {

			LancamentoAvulso l = new LancamentoAvulso();

			l.setDataEmissao(DataUtil.converteDataSql(objects[0].toString()));
			l.setDataPagamento(DataUtil.converteDataSql(objects[1].toString()));
			l.setNumeroDocumento(objects[2].toString());
			// l.setAcao(objects[3].toString());
			l.setPagador(objects[4].toString());
			l.setRecebedor(objects[5].toString());
			l.setDescricao(objects[6].toString());
			l.setTipoLancamento(objects[7].toString());
			l.setValorTotalComDesconto(new BigDecimal(objects[8].toString()));
			lancamentos.add(l);

		}

		return lancamentos; // query.getResultList();
	}

	public List<LancamentoAvulso> getListaLancamentosAvulsoParaRelatorio(Filtro filtro) {

		StringBuilder hql = new StringBuilder(" select ");

		hql.append(" to_char(l.data_emissao,'DD-MM-YYYY') as data_emissao, "); // 0
		hql.append(" to_char(l.data_pagamento,'DD-MM-YYYY') as data_pagamento, "); // 1
		hql.append(" l.numerodocumento as documento, ");// 2
		hql.append(" (select ac.codigo from acao ac where ac.id = la.acao_id) as acao,  ");// 3
		hql.append(" (select c.nome_conta  from conta_bancaria as c where c.id = pl.conta_id) as pagador, ");// 4
		hql.append("(select c.nome_conta  from conta_bancaria as c where c.id = pl.contarecebedor_id) as recebedor,");// 5
		hql.append(" l.descricao, ");// 6
		hql.append(" l.tipolancamento, ");// 7
		hql.append(" la.valor ");// 8

		hql.append(
				" from lancamento l join lancamento_acao la on l.id = la.lancamento_id join  pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");

		// hql.append(" acao a on a.id = la.acao_id ");

		hql.append(" where l.numerodocumento = '" + filtro.getNumeroDocumento() + "'");

		// hql.append(" and (l.categoriadespesa != 'DIARIA' OR
		// l.categoriadespesa IS NULL) ");

		hql.append(" and (l.categoriadespesaclass_id != 30 OR l.categoriadespesaclass_id IS NULL) ");
		Query query = this.manager.createNativeQuery(hql.toString());

		List<LancamentoAvulso> lancamentos = new ArrayList<>();
		List<Object[]> result = query.getResultList();

		for (Object[] objects : result) {

			LancamentoAvulso l = new LancamentoAvulso();

			l.setDataEmissao(DataUtil.converteDataSql(objects[0].toString()));
			l.setDataPagamento(DataUtil.converteDataSql(objects[1].toString()));
			l.setNumeroDocumento(objects[2].toString());
			l.setAcao(objects[3].toString());
			l.setPagador(objects[4].toString());
			l.setRecebedor(objects[5].toString());
			l.setDescricao(objects[6].toString());
			l.setTipoLancamento(objects[7].toString());
			l.setValorTotalComDesconto(new BigDecimal(objects[8].toString()));
			lancamentos.add(l);

		}

		return lancamentos; // query.getResultList();
	}

	public Compra getCompraPorId(Long id) {
		return this.manager.find(Compra.class, id);
	}

	public Pedido getPedidoPorId(Long id) {
		return this.manager.find(Pedido.class, id);
	}

	public List<LancamentoAcao> getLancamentoAcaoPorLancamento(Long id, String args) {
		String jpql = "from LancamentoAcao where lancamento.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public SolicitacaoPagamento getPagamentoPorId(Long id) {
		return this.manager.find(SolicitacaoPagamento.class, id);
	}

	public void salvar(Lancamento lancamento) {
		lancamento = this.manager.merge(lancamento);
		lancamento.setNumeroDocumento(lancamento.getId().toString());
	}

	public LancamentoReembolso salvarReembolso(LancamentoReembolso lancamentoReembolso) {
		return this.manager.merge(lancamentoReembolso);
	}

	public LancamentoDevDocTed salvarDevDocTed(LancamentoDevDocTed lancamentoDevDocTed) {
		return this.manager.merge(lancamentoDevDocTed);
	}

	public Devolucao salvarDevolucao(Devolucao devolucao) {
		return this.manager.merge(devolucao);
	}

	public LancamentoAlocacaoRendimento salvarRendimento(LancamentoAlocacaoRendimento lancamentoAlocacaoRendimento) {
		return this.manager.merge(lancamentoAlocacaoRendimento);
	}

	public void deletarRendimento(Long id) {
		String sql = "select id from lancamento where tipo = 'rendimento' and id_alocacao_rendimento = :p_id";

		Query query = this.manager.createNativeQuery(sql);
		query.setParameter("p_id", id);

		Object objIdLancamento = null;

		try {
			objIdLancamento = query.getSingleResult();
		} catch (Exception e) {

		}

		if (objIdLancamento != null) {
			Long idLancamento = Long.parseLong(objIdLancamento.toString());

			sql = "select id from lancamento_acao where lancamento_id = " + idLancamento;

			query = this.manager.createNativeQuery(sql);

			Object objIdLancamentoAcao = null;

			try {
				objIdLancamentoAcao = query.getSingleResult();
			} catch (Exception e) {

			}

			if (objIdLancamentoAcao != null) {

				Long idLancamentoAcao = Long.parseLong(objIdLancamentoAcao.toString());

				sql = "delete from pagamento_lancamento where lancamentoacao_id = " + idLancamentoAcao;

				query = this.manager.createNativeQuery(sql);
				query.executeUpdate();

				sql = "delete from lancamento_acao where id = " + idLancamentoAcao;

				query = this.manager.createNativeQuery(sql);
				query.executeUpdate();

				sql = "delete from lancamento where id = " + idLancamento;

				query = this.manager.createNativeQuery(sql);
				query.executeUpdate();
			}
		}
	}

	public void salvarItemPedido(ItemPedido itemPedido) {
		this.manager.merge(itemPedido);
	}

	public SolicitacaoPagamento salvarSolicitacaoAdiantamento(Lancamento lancamento, User usuario) {

		LogStatus log = new LogStatus();
		log.setUsuario(usuario);
		log.setData(new Date());
		log.setSigla(lancamento.getGestao().getSigla());

		//lancamento = this.manager.merge(lancamento);
		lancamento.setNumeroDocumento(lancamento.getId().toString());
		log.setLancamento(lancamento);

		this.manager.merge(log);

		if (lancamento.getId() != null) {
			log.setSiglaPrivilegio("EDIT");

			List<LancamentoAcao> lancamentos = new ArrayList<>();
			lancamentos = getLancamentosAcoes(lancamento.getId());
			deletarTodosPagamentos(lancamento.getId());

			// SolicitacaoPagamento pagamento = getPagamentoPorId(lancamento.getId());

			for (LancamentoAcao lc : lancamentos) {

				lc.setDespesaReceita(lancamento.getDepesaReceita());
				lc.setTipoLancamento(lancamento.getTipoLancamento());
				lc.setStatus(StatusPagamento.N_VALIDADO);
				lc = salvarLancamentoAcaoReturn(lc);

				PagamentoLancamento pl = new PagamentoLancamento();
				pl.setConta(lc.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getContaBancaria());
				pl.setContaRecebedor(lancamento.getContaRecebedor());
				pl.setTipoContaPagador(
						lc.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getContaBancaria().getTipo());
				if (lancamento.getContaRecebedor() != null)
					pl.setTipoContaRecebedor(lancamento.getContaRecebedor().getTipo());
				pl.setDataEmissao(lancamento.getDataEmissao());
				pl.setDataPagamento(lancamento.getDataPagamento());
				pl.setLancamentoAcao(lc);
				pl.setQuantidadeParcela(lancamento.getQuantidadeParcela());
				pl.setStt(StatusPagamentoLancamento.PROVISIONADO);
				pl.setTipoParcelamento(lancamento.getTipoParcelamento());
				pl.setValor(lc.getValor());
				pl.setTipoLancamento(lancamento.getTipoLancamento());
				pl.setDespesaReceita(lancamento.getDepesaReceita());

				if (pl.getTipoParcelamento().compareTo(TipoParcelamento.PARCELA_UNICA) == 0) {
					pl.setQuantidadeParcela(1);
				}

				Date dataBase = pl.getDataPagamento();
				tipoParcelamento = pl.getTipoParcelamento();
				BigDecimal totalParcelado = BigDecimal.ZERO;

				for (int i = 1; i <= pl.getQuantidadeParcela(); i++) {
					PagamentoLancamento pagto = new PagamentoLancamento(pl, i);

					if (i == 1) {
						pagto.setDataPagamento(pl.getDataPagamento());
					} else {
						pagto.setDataPagamento(setarData(dataBase));

						if (i == pl.getQuantidadeParcela()) {
							BigDecimal total = pl.getValor();
							BigDecimal resto = total.subtract(totalParcelado);
							pagto.setValor(resto);
						}

					}

					totalParcelado = totalParcelado.add(pagto.getValor());

					salvarPagamento(pagto);
					dataBase = pagto.getDataPagamento();
				}
			}

		} else {
			log.setSiglaPrivilegio("NEW");
			log.setStatusLog(StatusCompra.N_INCIADO);

			lancamento = this.manager.merge(lancamento);
			lancamento.setNumeroDocumento(lancamento.getId().toString());

			for (LancamentoAcao lc : lancamento.getLancamentosAcoes()) {
				lc.setDespesaReceita(lancamento.getDepesaReceita());
				lc.setLancamento(lancamento);
				lc.setStatus(StatusPagamento.N_VALIDADO);
				lc.setTipoLancamento(lancamento.getTipoLancamento());
				lc = this.manager.merge(lc);

				PagamentoLancamento pl = new PagamentoLancamento();
				pl.setConta(lc.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getContaBancaria());
				pl.setContaRecebedor(lancamento.getContaRecebedor());
				pl.setDataEmissao(lancamento.getDataEmissao());
				pl.setDataPagamento(lancamento.getDataPagamento());
				pl.setLancamentoAcao(lc);
				pl.setQuantidadeParcela(lancamento.getQuantidadeParcela());
				pl.setStt(StatusPagamentoLancamento.PROVISIONADO);
				pl.setTipoParcelamento(lancamento.getTipoParcelamento());
				pl.setValor(lc.getValor());
				pl.setTipoLancamento(lancamento.getTipoLancamento());
				pl.setDespesaReceita(lancamento.getDepesaReceita());

				if (pl.getTipoParcelamento().compareTo(TipoParcelamento.PARCELA_UNICA) == 0) {
					pl.setQuantidadeParcela(1);
				}

				Date dataBase = pl.getDataPagamento();
				tipoParcelamento = pl.getTipoParcelamento();
				BigDecimal totalParcelado = BigDecimal.ZERO;

				for (int i = 1; i <= pl.getQuantidadeParcela(); i++) {
					PagamentoLancamento pagto = new PagamentoLancamento(pl, i);

					if (i == 1) {
						pagto.setDataPagamento(pl.getDataPagamento());
					} else {
						pagto.setDataPagamento(setarData(dataBase));

						if (i == pl.getQuantidadeParcela()) {
							BigDecimal total = pl.getValor();
							BigDecimal resto = total.subtract(totalParcelado);
							pagto.setValor(resto);
						}

					}

					totalParcelado = totalParcelado.add(pagto.getValor());

					this.manager.merge(pagto);
					dataBase = pagto.getDataPagamento();
				}
			}

		}
		log.setLancamento(lancamento);
		this.manager.merge(log);

		return (SolicitacaoPagamento) lancamento;

	}

	public SolicitacaoPagamento salvarSolicitacaoPagamento(Lancamento lancamento, User usuario) {

		LogStatus log = new LogStatus();
		log.setUsuario(usuario);
		log.setData(new Date());
		log.setSigla(lancamento.getGestao().getSigla());

		lancamento = this.manager.merge(lancamento);
		lancamento.setNumeroDocumento(lancamento.getId().toString());
		log.setLancamento(lancamento);

		this.manager.merge(log);

		if (lancamento.getId() != null) {
			log.setSiglaPrivilegio("EDIT");

			List<LancamentoAcao> lancamentos = new ArrayList<>();
			lancamentos = getLancamentosAcoes(lancamento.getId());
			deletarTodosPagamentos(lancamento.getId());

			// SolicitacaoPagamento pagamento = getPagamentoPorId(lancamento.getId());

			for (LancamentoAcao lc : lancamentos) {

				lc.setDespesaReceita(lancamento.getDepesaReceita());
				lc.setTipoLancamento(lancamento.getTipoLancamento());
				lc.setStatus(StatusPagamento.N_VALIDADO);
				lc = salvarLancamentoAcaoReturn(lc);

				PagamentoLancamento pl = new PagamentoLancamento();
				pl.setConta(lc.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getContaBancaria());
				pl.setContaRecebedor(lancamento.getContaRecebedor());
				pl.setTipoContaPagador(
						lc.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getContaBancaria().getTipo());
				if (lancamento.getContaRecebedor() != null)
					pl.setTipoContaRecebedor(lancamento.getContaRecebedor().getTipo());
				pl.setDataEmissao(lancamento.getDataEmissao());
				pl.setDataPagamento(lancamento.getDataPagamento());
				pl.setLancamentoAcao(lc);
				pl.setQuantidadeParcela(lancamento.getQuantidadeParcela());
				pl.setStt(StatusPagamentoLancamento.PROVISIONADO);
				pl.setTipoParcelamento(lancamento.getTipoParcelamento());
				pl.setValor(lc.getValor());
				pl.setTipoLancamento(lancamento.getTipoLancamento());
				pl.setDespesaReceita(lancamento.getDepesaReceita());

				if (pl.getTipoParcelamento().compareTo(TipoParcelamento.PARCELA_UNICA) == 0) {
					pl.setQuantidadeParcela(1);
				}

				Date dataBase = pl.getDataPagamento();
				tipoParcelamento = pl.getTipoParcelamento();
				BigDecimal totalParcelado = BigDecimal.ZERO;

				for (int i = 1; i <= pl.getQuantidadeParcela(); i++) {
					PagamentoLancamento pagto = new PagamentoLancamento(pl, i);

					if (i == 1) {
						pagto.setDataPagamento(pl.getDataPagamento());
					} else {
						pagto.setDataPagamento(setarData(dataBase));

						if (i == pl.getQuantidadeParcela()) {
							BigDecimal total = pl.getValor();
							BigDecimal resto = total.subtract(totalParcelado);
							pagto.setValor(resto);
						}

					}

					totalParcelado = totalParcelado.add(pagto.getValor());

					salvarPagamento(pagto);
					dataBase = pagto.getDataPagamento();
				}
			}

		} else {
			log.setSiglaPrivilegio("NEW");
			log.setStatusLog(StatusCompra.N_INCIADO);

			lancamento = this.manager.merge(lancamento);
			lancamento.setNumeroDocumento(lancamento.getId().toString());

			for (LancamentoAcao lc : lancamento.getLancamentosAcoes()) {
				lc.setDespesaReceita(lancamento.getDepesaReceita());
				lc.setLancamento(lancamento);
				lc.setStatus(StatusPagamento.N_VALIDADO);
				lc.setTipoLancamento(lancamento.getTipoLancamento());
				lc = this.manager.merge(lc);

				PagamentoLancamento pl = new PagamentoLancamento();
				pl.setConta(lc.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getContaBancaria());
				pl.setContaRecebedor(lancamento.getContaRecebedor());
				pl.setDataEmissao(lancamento.getDataEmissao());
				pl.setDataPagamento(lancamento.getDataPagamento());
				pl.setLancamentoAcao(lc);
				pl.setQuantidadeParcela(lancamento.getQuantidadeParcela());
				pl.setStt(StatusPagamentoLancamento.PROVISIONADO);
				pl.setTipoParcelamento(lancamento.getTipoParcelamento());
				pl.setValor(lc.getValor());
				pl.setTipoLancamento(lancamento.getTipoLancamento());
				pl.setDespesaReceita(lancamento.getDepesaReceita());

				if (pl.getTipoParcelamento().compareTo(TipoParcelamento.PARCELA_UNICA) == 0) {
					pl.setQuantidadeParcela(1);
				}

				Date dataBase = pl.getDataPagamento();
				tipoParcelamento = pl.getTipoParcelamento();
				BigDecimal totalParcelado = BigDecimal.ZERO;

				for (int i = 1; i <= pl.getQuantidadeParcela(); i++) {
					PagamentoLancamento pagto = new PagamentoLancamento(pl, i);

					if (i == 1) {
						pagto.setDataPagamento(pl.getDataPagamento());
					} else {
						pagto.setDataPagamento(setarData(dataBase));

						if (i == pl.getQuantidadeParcela()) {
							BigDecimal total = pl.getValor();
							BigDecimal resto = total.subtract(totalParcelado);
							pagto.setValor(resto);
						}

					}

					totalParcelado = totalParcelado.add(pagto.getValor());

					this.manager.merge(pagto);
					dataBase = pagto.getDataPagamento();
				}
			}

		}
		log.setLancamento(lancamento);
		this.manager.merge(log);

		return (SolicitacaoPagamento) lancamento;
	}

	public List<CompraMunicipio> getLocaisDeCompra(Long id) {
		String jpql = "from CompraMunicipio where compra.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList();
	}

	public Compra salvarCompra(Lancamento lancamento, User usuario) {

		String siglaPrivilegio = "NEW";
		if (lancamento.getId() != null) {
			siglaPrivilegio = "EDIT";
		}
		
		lancamento = this.manager.merge(lancamento);
		lancamento.setNumeroDocumento(lancamento.getId().toString());
		
		logRepositorio.saveLogLancamento(lancamento, usuario, siglaPrivilegio);

		return (Compra) lancamento;
	}

	public Pedido salvarPedido(Pedido lancamento) {
		lancamento = this.manager.merge(lancamento);
		lancamento.setNumeroDocumento(lancamento.getId().toString());
		lancamento.setTipov4("PC");
		if (lancamento.getDescricao() == null) {
			lancamento.setDescricao(
					lancamento.getId().toString() + " - " + lancamento.getItensPedidos().get(0).getCategoria());
		}

		return lancamento;
	}

	@Inject
	private AtividadeRepositorio atvRepositorio;

	public Pedido salvarPedidoAntesDaAprovacao(Pedido lancamento, ControleExpedicao controle, User usuario)
			throws Exception {
		lancamento = this.manager.merge(lancamento);
		lancamento.setNumeroDocumento(lancamento.getId().toString());

		lancamento.setDescricao("Pedido - " + lancamento.getId());
		controle.setPedido(lancamento);

		LogStatus log = new LogStatus();
		log.setUsuario(usuario);
		log.setLancamento(lancamento);
		log.setData(new Date());
		log.setStatusLog(StatusCompra.N_INCIADO);
		log.setSigla(lancamento.getCompra().getGestao().getSigla());
		log.setSiglaPrivilegio("APC");

		salvarLog(log);

		return lancamento;
	}

	public LogStatus gerarESalvarLogSolicitacao(Lancamento lancamento, String tipoLog, User usuario) {

		LogStatus log = new LogStatus();
		log.setUsuario(usuario);
		log.setLancamento(lancamento);
		log.setData(new Date());
		log.setStatusLog(StatusCompra.N_INCIADO);
		log.setSigla(lancamento.getGestao().getSigla());
		log.setSiglaPrivilegio(tipoLog);

		return this.manager.merge(log);
	}

	public Pedido salvarPedidoSemMudarStatusCompra(Pedido lancamento, ControleExpedicao controle, User usuario)
			throws Exception {

		controle.setPedido(lancamento);

		List<LancamentoAcao> lList = getLancamentosDoPedido(lancamento);
		for (LancamentoAcao lancamentoAcao : lList) {
			lancamentoAcao.setLancamento(lancamento);

			if (lancamentoAcao.getAtividade() != null && lancamentoAcao.getAtividade().getId() != null)
				if (!atvRepositorio.verificarSaldoAtividade(lancamentoAcao.getAtividade().getId(),
						lancamentoAcao.getValor())) {

					StringBuilder str = new StringBuilder();

					str.append("Fonte: ");
					str.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getTitulo());
					str.append("\n");

					str.append("Projeto: ");
					str.append(lancamentoAcao.getProjetoRubrica().getProjeto().getNome());
					str.append("\n");

					str.append("Componente: ");
					str.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getComponente().getNome());
					str.append("\n");

					str.append("Subcomponente: ");
					str.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getSubComponente().getNome());
					str.append("\n");

					str.append("Linha: ");
					str.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getRubrica().getNome());
					str.append("\n");

					str.append("Atividade: ");
					str.append(lancamentoAcao.getAtividade().getNome());
					str.append("\n");

					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", str.toString());
					FacesContext.getCurrentInstance().addMessage(null, message);
					throw new NoResultException();

				}

			if (!calculatorRubricaRepositorio.verificarSaldoRubrica(lancamentoAcao.getProjetoRubrica().getId(),
					lancamentoAcao.getValor())) {

				StringBuilder str = new StringBuilder();

				str.append("Fonte: ");
				str.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getTitulo());
				str.append("\n");

				str.append("Projeto: ");
				str.append(lancamentoAcao.getProjetoRubrica().getProjeto().getNome());
				str.append("\n");

				str.append("Componente: ");
				str.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getComponente().getNome());
				str.append("\n");

				str.append("Subcomponente: ");
				str.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getSubComponente().getNome());
				str.append("\n");

				str.append("Linha: ");
				str.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getRubrica().getNome());
				str.append("\n");

				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "", str.toString());
				FacesContext.getCurrentInstance().addMessage(null, message);
				throw new NoResultException();
			}

			salvarLancamentoAcao(lancamentoAcao);
		}

		if (lancamento.getId() != null)
			deletarTodosPagamentos(lancamento.getId());

		for (LancamentoAcao lc : lList) {
			lc.setDespesaReceita(lancamento.getDepesaReceita());
			lc.setTipoLancamento(lancamento.getTipoLancamento());
			lc.setStatus(StatusPagamento.N_VALIDADO);
			lc = salvarLancamentoAcaoReturn(lc);

			PagamentoLancamento pl = new PagamentoLancamento();
			pl.setConta(lc.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getContaBancaria());
			pl.setContaRecebedor(lancamento.getContaRecebedor());
			pl.setTipoContaPagador(
					lc.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getContaBancaria().getTipo());
			pl.setTipoContaRecebedor(lancamento.getContaRecebedor().getTipo());
			pl.setDataEmissao(lancamento.getDataEmissao());
			pl.setDataPagamento(lancamento.getDataPagamento());
			pl.setLancamentoAcao(lc);
			pl.setQuantidadeParcela(lancamento.getQuantidadeParcela());
			pl.setStt(StatusPagamentoLancamento.PROVISIONADO);
			pl.setTipoParcelamento(lancamento.getTipoParcelamento());
			pl.setValor(lc.getValor());
			pl.setTipoLancamento(lancamento.getTipoLancamento());
			pl.setDespesaReceita(lancamento.getDepesaReceita());

			if (pl.getTipoParcelamento() == null) {
				pl.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
			}

			if (pl.getTipoParcelamento().compareTo(TipoParcelamento.PARCELA_UNICA) == 0) {
				pl.setQuantidadeParcela(1);
			}

			Date dataBase = pl.getDataPagamento();
			tipoParcelamento = pl.getTipoParcelamento();
			BigDecimal totalParcelado = BigDecimal.ZERO;

			for (int i = 1; i <= pl.getQuantidadeParcela(); i++) {
				PagamentoLancamento pagto = new PagamentoLancamento(pl, i);

				if (i == 1) {
					pagto.setDataPagamento(pl.getDataPagamento());
				} else {
					pagto.setDataPagamento(setarData(dataBase));

					if (i == pl.getQuantidadeParcela()) {
						BigDecimal total = pl.getValor();
						BigDecimal resto = total.subtract(totalParcelado);
						pagto.setValor(resto);
					}

				}

				totalParcelado = totalParcelado.add(pagto.getValor());

				salvarPagamento(pagto);
				dataBase = pagto.getDataPagamento();
			}
		}

		manager.merge(controle);
		manager.merge(lancamento);
		return lancamento;
	}

	public Pedido salvarPedido(Pedido lancamento, List<LancamentoAcao> recursos) {

		if (lancamento.getId() != null)
			deletarTodosPagamentos(lancamento.getId());

		for (LancamentoAcao lc : recursos) {
			lc.setDespesaReceita(lancamento.getDepesaReceita());
			lc.setTipoLancamento(lancamento.getTipoLancamento());
			lc.setStatus(StatusPagamento.N_VALIDADO);
			lc = salvarLancamentoAcaoReturn(lc);

			PagamentoLancamento pl = new PagamentoLancamento();
			pl.setConta(lc.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getContaBancaria());
			pl.setContaRecebedor(lancamento.getContaRecebedor());
			pl.setTipoContaPagador(
					lc.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getContaBancaria().getTipo());
			pl.setTipoContaRecebedor(lancamento.getContaRecebedor().getTipo());
			pl.setDataEmissao(lancamento.getDataEmissao());
			pl.setDataPagamento(lancamento.getDataPagamento());
			pl.setLancamentoAcao(lc);
			pl.setQuantidadeParcela(lancamento.getQuantidadeParcela());
			pl.setStt(StatusPagamentoLancamento.PROVISIONADO);
			pl.setTipoParcelamento(lancamento.getTipoParcelamento());
			pl.setValor(lc.getValor());
			pl.setTipoLancamento(lancamento.getTipoLancamento());
			pl.setDespesaReceita(lancamento.getDepesaReceita());

			if (pl.getTipoParcelamento() == null) {
				pl.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
			}

			if (pl.getTipoParcelamento().compareTo(TipoParcelamento.PARCELA_UNICA) == 0) {
				pl.setQuantidadeParcela(1);
			}

			Date dataBase = pl.getDataPagamento();
			tipoParcelamento = pl.getTipoParcelamento();
			BigDecimal totalParcelado = BigDecimal.ZERO;

			for (int i = 1; i <= pl.getQuantidadeParcela(); i++) {
				PagamentoLancamento pagto = new PagamentoLancamento(pl, i);

				if (i == 1) {
					pagto.setDataPagamento(pl.getDataPagamento());
				} else {
					pagto.setDataPagamento(setarData(dataBase));

					if (i == pl.getQuantidadeParcela()) {
						BigDecimal total = pl.getValor();
						BigDecimal resto = total.subtract(totalParcelado);
						pagto.setValor(resto);
					}

				}

				totalParcelado = totalParcelado.add(pagto.getValor());

				this.manager.merge(pagto);
				dataBase = pagto.getDataPagamento();
			}
		}

		lancamento.setTipov4("PC");
		this.manager.merge(lancamento);

		return lancamento;

	}

	public Pedido salvarPedido(Pedido lancamento, ControleExpedicao controle, User usuario) {
		// rtr
		controle.setPedido(lancamento);

		List<LancamentoAcao> lList = getLancamentosDoPedido(lancamento);
		for (LancamentoAcao lancamentoAcao : lList) {
			lancamentoAcao.setLancamento(lancamento);
			salvarLancamentoAcao(lancamentoAcao);
		}

		LogStatus log = new LogStatus();
		log.setUsuario(usuario);
		log.setLancamento(lancamento);
		log.setData(new Date());
		log.setStatusLog(lancamento.getStatusCompra());
		log.setSigla(lancamento.getCompra().getGestao().getSigla());
		log.setSiglaPrivilegio("APC");

		salvarLog(log);

		if (lancamento.getId() != null)
			deletarTodosPagamentos(lancamento.getId());

		for (LancamentoAcao lc : lList) {
			lc.setDespesaReceita(lancamento.getDepesaReceita());
			lc.setTipoLancamento(lancamento.getTipoLancamento());
			lc.setStatus(StatusPagamento.N_VALIDADO);
			lc = salvarLancamentoAcaoReturn(lc);

			PagamentoLancamento pl = new PagamentoLancamento();
			pl.setConta(lc.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getContaBancaria());
			pl.setContaRecebedor(lancamento.getContaRecebedor());
			pl.setTipoContaPagador(
					lc.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getContaBancaria().getTipo());
			pl.setTipoContaRecebedor(lancamento.getContaRecebedor().getTipo());
			pl.setDataEmissao(lancamento.getDataEmissao());
			pl.setDataPagamento(lancamento.getDataPagamento());
			pl.setLancamentoAcao(lc);
			pl.setQuantidadeParcela(lancamento.getQuantidadeParcela());
			pl.setStt(StatusPagamentoLancamento.PROVISIONADO);
			pl.setTipoParcelamento(lancamento.getTipoParcelamento());
			pl.setValor(lc.getValor());
			pl.setTipoLancamento(lancamento.getTipoLancamento());
			pl.setDespesaReceita(lancamento.getDepesaReceita());

			if (pl.getTipoParcelamento() == null) {
				pl.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
			}

			if (pl.getTipoParcelamento().compareTo(TipoParcelamento.PARCELA_UNICA) == 0) {
				pl.setQuantidadeParcela(1);
			}

			Date dataBase = pl.getDataPagamento();
			tipoParcelamento = pl.getTipoParcelamento();
			BigDecimal totalParcelado = BigDecimal.ZERO;

			for (int i = 1; i <= pl.getQuantidadeParcela(); i++) {
				PagamentoLancamento pagto = new PagamentoLancamento(pl, i);

				if (i == 1) {
					pagto.setDataPagamento(pl.getDataPagamento());
				} else {
					pagto.setDataPagamento(setarData(dataBase));

					if (i == pl.getQuantidadeParcela()) {
						BigDecimal total = pl.getValor();
						BigDecimal resto = total.subtract(totalParcelado);
						pagto.setValor(resto);
					}

				}

				totalParcelado = totalParcelado.add(pagto.getValor());

				salvarPagamento(pagto);
				dataBase = pagto.getDataPagamento();
			}
		}

		manager.merge(controle);
		manager.merge(lancamento);
		return lancamento;
	}

	public List<PagamentoLancamento> buscarPagamentoDoLancamento() {
		return new ArrayList<>();
	}

	public LancamentoDiversos salvarLancamentoDiversosSemRefatorar(LancamentoDiversos lancamento, User usuario) {

		LogStatus log = new LogStatus();
		log.setUsuario(usuario);
		log.setData(new Date());
		if (lancamento.getId() != null) {
			log.setStatusLog(StatusCompra.EDICAO);
		} else {
			log.setStatusLog(StatusCompra.N_INCIADO);
		}
		lancamento = this.manager.merge(lancamento);
		log.setLancamento(lancamento);

		this.manager.merge(log);
		return this.manager.merge(lancamento);
	}

	public LancamentoAvulso salvarLancamentoSemRefatorar(LancamentoAvulso lancamento, User usuario) {

		LogStatus log = new LogStatus();
		log.setUsuario(usuario);
		log.setData(new Date());
		if (lancamento.getId() != null) {
			log.setStatusLog(StatusCompra.EDICAO);
		} else {
			log.setStatusLog(StatusCompra.N_INCIADO);
		}
		lancamento = this.manager.merge(lancamento);
		log.setLancamento(lancamento);

		this.manager.merge(log);
		return this.manager.merge(lancamento);
	}

	public List<PagamentoLancamento> buscarPagamentoByLancamento(Long id) {
		String jpql = "from PagamentoLancamento as pl where pl.lancamento.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList();
	}

	public Boolean buscarPagamentoByLancamento(Long id, String args) {
		String jpql = "from PagamentoLancamento as pl where pl.lancamento.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? true : false;
	}

	public LancamentoDiversos salvarLancamentoDiversos(LancamentoDiversos lancamento, User usuario) {

		if (lancamento.getId() != null)
			this.manager.createQuery("delete from PagamentoLancamento as pl where pl.lancamento.id = :id")
					.setParameter("id", lancamento.getId()).executeUpdate();

		LogStatus log = new LogStatus();
		log.setUsuario(usuario);
		log.setData(new Date());
		if (lancamento.getId() != null) {
			log.setStatusLog(StatusCompra.EDICAO);
			log.setSiglaPrivilegio("EDIT");
		} else {
			log.setSiglaPrivilegio("NEW");
			log.setStatusLog(StatusCompra.N_INCIADO);
		}

		lancamento = this.manager.merge(lancamento);
		log.setLancamento(lancamento);
		this.manager.merge(log);

		for (LancamentoAcao lc : lancamento.getLancamentosAcoes()) {
			lc.setDespesaReceita(lancamento.getDepesaReceita());
			lc.setLancamento(lancamento);
			lc.setTipoLancamento(lancamento.getTipoLancamento());
			lc = this.manager.merge(lc);

			PagamentoLancamento pl = new PagamentoLancamento();
			pl.setConta(lancamento.getContaPagador());
			pl.setContaRecebedor(lancamento.getContaRecebedor());
			pl.setTipoContaPagador(lancamento.getContaPagador().getTipo());
			pl.setTipoContaRecebedor(lancamento.getContaRecebedor().getTipo());
			pl.setDataEmissao(lancamento.getDataEmissao());
			pl.setDataPagamento(lancamento.getDataPagamento());
			pl.setLancamentoAcao(lc);
			pl.setQuantidadeParcela(lancamento.getQuantidadeParcela());
			pl.setStt(StatusPagamentoLancamento.PROVISIONADO);
			pl.setTipoParcelamento(lancamento.getTipoParcelamento());
			pl.setValor(lc.getValor());
			pl.setTipoLancamento(lancamento.getTipoLancamento());
			pl.setDespesaReceita(lancamento.getDepesaReceita());

			if (pl.getTipoParcelamento().compareTo(TipoParcelamento.PARCELA_UNICA) == 0) {
				pl.setQuantidadeParcela(1);
			}

			Date dataBase = pl.getDataPagamento();
			tipoParcelamento = pl.getTipoParcelamento();
			BigDecimal totalParcelado = BigDecimal.ZERO;

			for (int i = 1; i <= pl.getQuantidadeParcela(); i++) {
				PagamentoLancamento pagto = new PagamentoLancamento(pl, i);

				if (i == 1) {
					pagto.setDataPagamento(pl.getDataPagamento());
				} else {
					pagto.setDataPagamento(setarData(dataBase));

					if (i == pl.getQuantidadeParcela()) {
						BigDecimal total = pl.getValor();
						BigDecimal resto = total.subtract(totalParcelado);
						pagto.setValor(resto);
					}

				}

				totalParcelado = totalParcelado.add(pagto.getValor());

				this.manager.merge(pagto);
				dataBase = pagto.getDataPagamento();
			}
		}

		return lancamento;

	}

	public TarifaBancaria salvarTarifa(TarifaBancaria tarifa, User usuario) {

		if (tarifa.getId() != null)
			this.manager.createQuery("delete from PagamentoLancamento as pl where pl.lancamento.id = :id")
					.setParameter("id", tarifa.getId()).executeUpdate();

		LogStatus log = new LogStatus();
		log.setUsuario(usuario);
		log.setData(new Date());
		if (tarifa.getId() != null) {
			log.setStatusLog(StatusCompra.EDICAO);
			log.setSiglaPrivilegio("EDIT");
		} else {
			log.setSiglaPrivilegio("NEW");
			log.setStatusLog(StatusCompra.N_INCIADO);
		}

		tarifa = this.manager.merge(tarifa);
		log.setLancamento(tarifa);
		this.manager.merge(log);

		for (LancamentoAcao lc : tarifa.getLancamentosAcoes()) {
			lc.setDespesaReceita(tarifa.getDepesaReceita());
			lc.setLancamento(tarifa);
			lc.setStatus(StatusPagamento.VALIDADO);
			lc.setTipoLancamento("Tarifa");

			lc = this.manager.merge(lc);

			PagamentoLancamento pl = new PagamentoLancamento();
			pl.setConta(tarifa.getContaPagador());
			pl.setContaRecebedor(tarifa.getContaRecebedor());
			pl.setDataEmissao(tarifa.getDataEmissao());
			pl.setDataPagamento(tarifa.getDataPagamento());
			pl.setLancamentoAcao(lc);
			pl.setQuantidadeParcela(tarifa.getQuantidadeParcela());
			pl.setStt(StatusPagamentoLancamento.PROVISIONADO);
			pl.setTipoParcelamento(tarifa.getTipoParcelamento());
			pl.setValor(tarifa.getValorTotalComDesconto());
			pl.setTipoLancamento(tarifa.getTipoLancamento());
			pl.setDespesaReceita(tarifa.getDepesaReceita());

			if (pl.getTipoParcelamento().compareTo(TipoParcelamento.PARCELA_UNICA) == 0) {
				pl.setQuantidadeParcela(1);
			}

			Date dataBase = pl.getDataPagamento();
			tipoParcelamento = pl.getTipoParcelamento();
			BigDecimal totalParcelado = BigDecimal.ZERO;

			for (int i = 1; i <= pl.getQuantidadeParcela(); i++) {
				PagamentoLancamento pagto = new PagamentoLancamento(pl, i);

				if (i == 1) {
					pagto.setDataPagamento(pl.getDataPagamento());
				} else {
					pagto.setDataPagamento(setarData(dataBase));

					if (i == pl.getQuantidadeParcela()) {
						BigDecimal total = pl.getValor();
						BigDecimal resto = total.subtract(totalParcelado);
						pagto.setValor(resto);
					}

				}

				totalParcelado = totalParcelado.add(pagto.getValor());

				this.manager.merge(pagto);
				dataBase = pagto.getDataPagamento();
			}
		}

		return tarifa;

	}

	public LancamentoAvulso salvarLancamentoAvulso(LancamentoAvulso lancamento, User usuario) {

		if (lancamento.getId() != null) {
			this.manager.createNativeQuery(
					"delete from pagamento_lancamento pl where lancamentoacao_id in (select la.id from lancamento_acao la where la.lancamento_id = :id)")
					.setParameter("id", lancamento.getId()).executeUpdate();
		}

		LogStatus log = new LogStatus();
		log.setUsuario(usuario);
		log.setData(new Date());
		if (lancamento.getId() != null) {
			log.setStatusLog(StatusCompra.EDICAO);
			log.setSiglaPrivilegio("EDIT");
		} else {
			log.setSiglaPrivilegio("NEW");
			log.setStatusLog(StatusCompra.N_INCIADO);
		}

		lancamento = this.manager.merge(lancamento);
		log.setLancamento(lancamento);
		this.manager.merge(log);

		for (LancamentoAcao lc : lancamento.getLancamentosAcoes()) {
			lc.setDespesaReceita(lancamento.getDepesaReceita());
			lc.setLancamento(lancamento);
			lc.setTipoLancamento(lancamento.getTipoLancamento());
			lc = this.manager.merge(lc);

			PagamentoLancamento pl = new PagamentoLancamento();
			pl.setConta(lancamento.getContaPagador());
			pl.setContaRecebedor(lancamento.getContaRecebedor());
			pl.setDataEmissao(lancamento.getDataEmissao());
			pl.setTipoContaPagador(lancamento.getContaPagador().getTipo());
			pl.setTipoContaRecebedor(lancamento.getContaRecebedor().getTipo());
			pl.setDataPagamento(lancamento.getDataPagamento());
			pl.setLancamentoAcao(lc);
			pl.setQuantidadeParcela(lancamento.getQuantidadeParcela());
			pl.setStt(StatusPagamentoLancamento.PROVISIONADO);
			pl.setTipoParcelamento(lancamento.getTipoParcelamento());
			pl.setValor(lancamento.getValorTotalComDesconto());
			pl.setTipoLancamento(lancamento.getTipoLancamento());
			pl.setDespesaReceita(lancamento.getDepesaReceita());

			if (pl.getTipoParcelamento().compareTo(TipoParcelamento.PARCELA_UNICA) == 0) {
				pl.setQuantidadeParcela(1);
			}

			Date dataBase = pl.getDataPagamento();
			tipoParcelamento = pl.getTipoParcelamento();
			BigDecimal totalParcelado = BigDecimal.ZERO;

			for (int i = 1; i <= pl.getQuantidadeParcela(); i++) {
				PagamentoLancamento pagto = new PagamentoLancamento(pl, i);

				if (i == 1) {
					pagto.setDataPagamento(pl.getDataPagamento());
				} else {
					pagto.setDataPagamento(setarData(dataBase));

					if (i == pl.getQuantidadeParcela()) {
						BigDecimal total = pl.getValor();
						BigDecimal resto = total.subtract(totalParcelado);
						pagto.setValor(resto);
					}

				}

				totalParcelado = totalParcelado.add(pagto.getValor());

				this.manager.merge(pagto);
				dataBase = pagto.getDataPagamento();
			}
		}

		return lancamento;

	}

	public ArquivoLancamento getArquivoById(Long id) {
		String jpql = "from ArquivoLancamento where id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? (ArquivoLancamento) query.getResultList().get(0) : null;
	}

	public Date setarData(Date dataBase) {
		Parcela parcela = tipoParcelamento.obterParcela();
		Date data = parcela.calculaData(dataBase);
		return data;
	}

	public LancamentoAvulso salvarLancamentoAvulso(Lancamento lancamento) {
		return (LancamentoAvulso) this.manager.merge(lancamento);
	}

	public LancamentoAcao salvarLancamentoAcaoReturn(LancamentoAcao lc) {
		return (LancamentoAcao) this.manager.merge(lc);
	}

	public LancamentoAcao salvarLancamentoAcao(LancamentoAcao lancAcao) {
		return this.manager.merge(lancAcao);
	}

	public void salvarPagamento(PagamentoLancamento pagamento) {
		this.manager.merge(pagamento);
	}

	public List<LancamentoAcao> getLancamentosAcoes(Lancamento lancamento) {
		String jpql = "from LancamentoAcao where lancamento =  :lancamento";
		Query query = this.manager.createQuery(jpql.toString());
		query.setParameter("lancamento", lancamento);
		return query.getResultList();
	}

	public List<LancamentoAcao> getLancamentosAcoes(Long id) {
		String jpql = "from LancamentoAcao where lancamento.id =  :lancamento";
		Query query = this.manager.createQuery(jpql.toString());
		query.setParameter("lancamento", id);
		return query.getResultList();
	}

	public LancamentoAcao getLancamentoAcaoPorAcaoEfonte(LancamentoAcao lancamentoAcao) {
		StringBuilder jpql = new StringBuilder("from LancamentoAcao la where ");
		jpql.append(" la.compra = :compra");
		jpql.append(" and la.fornecedor = :fornecedor");
		jpql.append(" and la.fontePagadora = :fonte");
		jpql.append(" and la.acao = :acao");

		Query query = manager.createQuery(jpql.toString());
		query.setParameter("compra", lancamentoAcao.getCompra());
		query.setParameter("fornecedor", lancamentoAcao.getFornecedor());
		query.setParameter("fonte", lancamentoAcao.getFontePagadora());
		query.setParameter("acao", lancamentoAcao.getAcao());
		query.setMaxResults(1);

		return query.getResultList().size() > 0 ? (LancamentoAcao) query.getResultList().get(0) : null;
	}

	public List<LancamentoAcao> getLancamentoPorFornecedorECompra(Long fornecedor, Compra compra) {
		StringBuilder jpql = new StringBuilder("from LancamentoAcao la where ");
		jpql.append(" la.compra = :compra");
		jpql.append(" and la.fornecedor.id = :fornecedor");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("compra", compra);
		query.setParameter("fornecedor", fornecedor);

		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public Boolean removerLancamentoAcao(LancamentoAcao lancamento) {
		try {
			manager.remove(manager.find(LancamentoAcao.class, lancamento.getId()));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<Compra> getComprasOLD(Filtro filtro) {
		StringBuilder jpql = new StringBuilder("from  Compra c where 1 = 1");

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

		jpql.append(" order by c.dataEmissao desc");

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
				 * jpql.append( " and c.dataEmissao between :data_inicio and :data_final");
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

		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Compra>();
	}

	public Boolean verificaItemPedido(Long id) {
		String jpql = "from ItemPedido where idItemCompra = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? true : false;
	}

	public String getSQLPurchase(Filtro filtro) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Compra(c.id, c.gestao.nome, c.solicitante.nome, c.localidade.mascara, c.dataEmissao,c.statusCompra, c.bootUrgencia, c.CategoriaDespesaClass.nome) ");

		// jpql.append(" FROM Compra c join c.lancamentosAcoes la join la.projetoRubrica
		// pr join pr.projeto p where 1 = 1 ");
		jpql.append(" FROM  Compra c  where 1 = 1 ");
		jpql.append(" and c.versionLancamento = 'MODE01' ");
		if (filtro.getCategoriaID() != null) {
			jpql.append(" and c.CategoriaDespesaClass.id = :catego ");
		}

		if (filtro.getUrgencia()) {
			jpql.append("and c.bootUrgencia = :urgencia ");
		}

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			jpql.append(" and c.id = :codigo ");
		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			jpql.append(" and lower(c.solicitante.nome) like lower(:nome) ");

		}

		if (filtro.getDataInicio() != null) {
			if (filtro.getDataFinal() != null) {
				jpql.append(" and c.dataEmissao >= :data_inicio and c.dataEmissao <= :data_final ");
			} else {
				jpql.append(" and c.dataEmissao >= :data_inicio ");
			}
		}

		if (filtro.getGestaoID() != null) {
			jpql.append("and (c.gestao.id = :gestaoID or c.gestao.superintendencia.id = :gestaoID) ");
		}

		if (filtro.getLocalidadeID() != null) {
			jpql.append(" and c.localidade.id = :localidadeID ");
		}

		if (filtro.getDescricaoProduto() != null && !filtro.getDescricaoProduto().equals("")) {
			jpql.append(
					" and ((select count(it) from ItemCompra it where it.compra.id = c.id and  lower(it.produto.descricao) like lower(:descricao_produto) )   >  0) ");
		}

		if (filtro.getCategoriaID() != null) {
			jpql.append(" and compra.CategoriaDespesaClass.id = :catego ");
		}

		if (filtro.getProjetoId() != null && filtro.getProjetoId() != null) {
			jpql.append(
					" and ((select count(la) from LancamentoAcao la where la.lancamento.id = c.id and la.projetoRubrica.projeto.id = :pProjetoID)   >  0) ");

		}

		jpql.append(" order by c.dataEmissao desc");

		return jpql.toString();
	}

	public List<Compra> setQueryPurchase(Query query, Filtro filtro) {

		if (filtro.getCategoriaID() != null) {
			// jpql.append(" and compra.CategoriaDespesaClass.id = :catego ");
			query.setParameter("catego", filtro.getCategoriaID());
		}

		if (filtro.getUrgencia()) {
			query.setParameter("urgencia", filtro.getUrgencia());
		}

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
				 * jpql.append( " and c.dataEmissao between :data_inicio and :data_final");
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

		if (filtro.getDescricaoProduto() != null && !filtro.getDescricaoProduto().equals("")) {
			query.setParameter("descricao_produto", "%" + filtro.getDescricaoProduto() + "%");
		}

		if (filtro.getProjetoId() != null && filtro.getProjetoId() != null) {
			query.setParameter("pProjetoID", filtro.getProjetoId());
		}

		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Compra>();
	}

	// public Compra(Long id,String nomeGestao, String nomeSolicitante, String
	// nomeDestino, Date data, StatusCompra status)
	public List<Compra> getCompras(Filtro filtro) {
		Query query = manager.createQuery(getSQLPurchase(filtro));
		return setQueryPurchase(query, filtro);
	}

	public List<ItemCompra> getItensCompraDetalhados(Filtro filtro) {
		StringBuilder jpql = new StringBuilder("SELECT NEW ItemCompra(");

		jpql.append("i.compra.id, i.compra.dataEmissao, i.produto.categoriaDespesa.nome, i.compra.observacao, ");
		jpql.append(
				" i.compra.statusCompra, i.compra.gestao.nome, i.compra.localidade.mascara, i.quantidade, i.unidade, i.produto.descricao, i.compra.solicitante.nome ");
		jpql.append(" , i.compra.gestao.nome, i.compra.bootUrgencia, i.produto.tipo ");
		// jpql.append(" , (SELECT l.data from log_status l where
		// l.lancamento_id = i.compra.id and l.statusLog = 2) ");
		jpql.append(" ) FROM  ItemCompra i where 1 = 1 ");

		if (filtro.getUrgencia()) {
			jpql.append("and i.compra.bootUrgencia = :urgencia ");
		}

		if (filtro.getStatusCompra() != null) {
			jpql.append(" and i.compra.statusCompra = :status_compra");
		}

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			jpql.append(" and i.compra.id = :codigo");
		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			jpql.append(" and lower(i.compra.solicitante.nome) like lower(:nome)");

		}

		if (filtro.getDataInicio() != null) {
			if (filtro.getDataFinal() != null) {
				jpql.append(" and i.compra.dataEmissao >= :data_inicio and i.compra.dataEmissao <= :data_final");
			} else {
				jpql.append(" and i.compra.dataEmissao >= :data_inicio");
			}
		}

		if (filtro.getLocalidadeID() != null) {
			jpql.append(" and i.compra.localidade.id = :localidadeID ");
		}

		if (filtro.getDescricaoProduto() != null && !filtro.getDescricaoProduto().equals("")) {
			jpql.append(" and  lower(i.produto.descricao) like lower(:descricao_produto)  ");
			// jpql.append(" and ((select count(it) from ItemCompra it where
			// it.compra.id = c.id and lower(it.produto.descricao) like
			// lower(:descricao_produto) ) > 0) ");
		}

		jpql.append(" order by  i.compra.dataEmissao desc");

		Query query = manager.createQuery(jpql.toString());

		if (filtro.getUrgencia()) {
			query.setParameter("urgencia", filtro.getUrgencia());
		}

		if (filtro.getStatusCompra() != null) {
			query.setParameter("status_compra", filtro.getStatusCompra());
		}

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
				 * jpql.append( " and c.dataEmissao between :data_inicio and :data_final");
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

		if (filtro.getDescricaoProduto() != null && !filtro.getDescricaoProduto().equals("")) {
			query.setParameter("descricao_produto", "%" + filtro.getDescricaoProduto() + "%");
		}

		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<ItemCompra>();
	}

	public List<SolicitacaoPagamento> fetchOrdersByTypeAdPaySA(Filtro filtro) {

		StringBuilder jpql = new StringBuilder(
				"SELECT NEW SolicitacaoPagamento(p.id, p.gestao.nome, p.solicitante.nome, p.localidade.mascara,  ");
		jpql.append("p.dataEmissao,p.statusCompra,  ");
		jpql.append("forn.nomeFantasia, p.descricao, p.valorTotalComDesconto, p.statusAdiantamento) ");
		jpql.append(" from SolicitacaoPagamento p ");
		jpql.append(" left join p.contaRecebedor recebedor ");
		jpql.append(" left join recebedor.fornecedor forn ");

		// jpql.append(" left join c.lancamentosAcoes la left join la.projetoRubrica pr
		// join pr.projeto p ");

		jpql.append(" where 1  = 1 ");

		jpql.append(" and p.versionLancamento = 'MODE01' ");

		if (filtro.getSp() != null && !filtro.getSp().equals("")) {
			jpql.append(" and p.tipoLancamento != 'ad' ");
		}

		if (filtro.getTipoLancamento() != null && !filtro.getTipoLancamento().equals("")) {
			jpql.append(" and p.tipoLancamento = 'ad' ");

		}

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			jpql.append(" and p.id = :codigo ");

		}

		if (filtro.getDataInicio() != null) {
			if (filtro.getDataFinal() != null) {
				jpql.append(" and p.dataEmissao between :data_inicio and :data_final");
			} else {
				jpql.append(" and p.dataEmissao > :data_inicio");
			}
		}

		if (filtro.getGestaoID() != null) {
			jpql.append(" and p.gestao.id = :gestaoID ");
		}

		if (filtro.getGestao() != null) {
			jpql.append(" and p.gestao = :gestao");
		}

		if (filtro.getNomeFornecedor() != null && !filtro.getNomeFornecedor().equals("")) {
			jpql.append(
					" and (lower(p.contaRecebedor.nomeConta) like lower(:fornecedor) or lower(p.contaRecebedor.razaoSocial)  ");
			jpql.append("like lower(:fornecedor) or lower(p.contaRecebedor.cnpj) like lower(:fornecedor) ");
			jpql.append("or lower(p.contaRecebedor.cpf) like lower(:fornecedor)) ");

		}

		if (filtro.getDescricao() != null && !filtro.getDescricao().equals("")) {
			jpql.append(" and lower(p.descricao) like lower(:descricao)");
		}

		if (filtro.getProjeto() != null && filtro.getProjeto().getId() != null) {
			jpql.append(" and :pProjetoID = p.id");
		}

		jpql.append(" order by p.dataEmissao desc");

		Query query = manager.createQuery(jpql.toString());

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			/* jpql.append(" and lower(c.codigo) like lower(:codigo)"); */
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
				 * jpql.append( " and c.dataEmissao between :data_inicio and :data_final");
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

		if (filtro.getGestao() != null) {
			query.setParameter("gestao", filtro.getGestao());
		}

		if (filtro.getLocalidade() != null) {
			query.setParameter("localidade", filtro.getLocalidade());
		}

		if (filtro.getNomeFornecedor() != null && !filtro.getNomeFornecedor().equals("")) {
			query.setParameter("fornecedor", "%" + filtro.getNomeFornecedor() + "%");
		}

		if (filtro.getDescricao() != null && !filtro.getDescricao().equals("")) {
			query.setParameter("descricao", "%" + filtro.getDescricao() + "%");
		}

		if (filtro.getProjeto() != null && filtro.getProjeto().getId() != null) {
			query.setParameter("pProjetoID", filtro.getProjeto().getId());
		}

		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<SolicitacaoPagamento>();
	}

	public List<SolicitacaoPagamento> getPagamentos(Filtro filtro) {

		StringBuilder jpql = new StringBuilder(
				"SELECT NEW SolicitacaoPagamento(p.id, p.gestao.nome, p.solicitante.nome, p.localidade.mascara,  ");
		jpql.append(
				"p.dataEmissao,p.statusCompra, forn.nomeFantasia, p.descricao, p.valorTotalComDesconto) from SolicitacaoPagamento p");

		jpql.append(" left join p.contaRecebedor recebedor ");
		jpql.append(" left join recebedor.fornecedor forn ");

		// jpql.append(" left join c.lancamentosAcoes la left join la.projetoRubrica pr
		// join pr.projeto p ");

		jpql.append(" where 1  = 1 ");

		jpql.append(" and p.versionLancamento = 'MODE01' ");

		if (filtro.getSp() != null && !filtro.getSp().equals("")) {
			jpql.append(" and p.tipoLancamento != 'ad' ");
		}

		if (filtro.getTipoLancamento() != null && !filtro.getTipoLancamento().equals("")) {
			jpql.append(" and p.tipoLancamento = 'ad' ");

		}

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			jpql.append(" and p.id = :codigo ");

		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			jpql.append(" and lower(p.solicitante.nome) like lower(:nome)");

		}

		if (filtro.getDataInicio() != null) {
			if (filtro.getDataFinal() != null) {
				jpql.append(" and p.dataEmissao between :data_inicio and :data_final");
			} else {
				jpql.append(" and p.dataEmissao > :data_inicio");
			}
		}

		if (filtro.getGestaoID() != null) {
			jpql.append(" and p.gestao.id = :gestaoID ");
		}

		if (filtro.getGestao() != null) {
			jpql.append(" and p.gestao = :gestao");
		}

		if (filtro.getLocalidadeID() != null) {
			jpql.append(" and p.localidade.id = :localidadeID ");
		}

		if (filtro.getLocalidade() != null) {
			jpql.append(" and p.localidade = :localidade ");
		}

		if (filtro.getNomeFornecedor() != null && !filtro.getNomeFornecedor().equals("")) {
			jpql.append(
					" and (lower(p.contaRecebedor.nomeConta) like lower(:fornecedor) or lower(p.contaRecebedor.razaoSocial)  ");
			jpql.append("like lower(:fornecedor) or lower(p.contaRecebedor.cnpj) like lower(:fornecedor) ");
			jpql.append("or lower(p.contaRecebedor.cpf) like lower(:fornecedor)) ");

		}

		if (filtro.getDescricao() != null && !filtro.getDescricao().equals("")) {
			jpql.append(" and lower(p.descricao) like lower(:descricao)");
		}

		if (filtro.getProjeto() != null && filtro.getProjeto().getId() != null) {
			jpql.append(" and :pProjetoID = p.id");
		}

		jpql.append(" order by p.dataEmissao desc");

		Query query = manager.createQuery(jpql.toString());

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			/* jpql.append(" and lower(c.codigo) like lower(:codigo)"); */
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
				 * jpql.append( " and c.dataEmissao between :data_inicio and :data_final");
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

		if (filtro.getGestao() != null) {
			query.setParameter("gestao", filtro.getGestao());
		}

		if (filtro.getLocalidade() != null) {
			query.setParameter("localidade", filtro.getLocalidade());
		}

		if (filtro.getNomeFornecedor() != null && !filtro.getNomeFornecedor().equals("")) {
			query.setParameter("fornecedor", "%" + filtro.getNomeFornecedor() + "%");
		}

		if (filtro.getDescricao() != null && !filtro.getDescricao().equals("")) {
			query.setParameter("descricao", "%" + filtro.getDescricao() + "%");
		}

		if (filtro.getProjeto() != null && filtro.getProjeto().getId() != null) {
			query.setParameter("pProjetoID", filtro.getProjeto().getId());
		}

		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<SolicitacaoPagamento>();
	}

	public List<ItemCompra> getItensByCompra(Compra compra) {
		StringBuilder jpql = new StringBuilder(
				"from  ItemCompra it where it.compra = :compra and (it.cancelado is false or it.cancelado is null)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("compra", compra);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<ItemCompra>();
	}

	public List<ItemCompra> getItensByCompraAllProduct(Compra compra) {
		StringBuilder jpql = new StringBuilder("from  ItemCompra it where it.compra = :compra ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("compra", compra);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<ItemCompra>();
	}

	public List<ItemCompra> getItensByCompraCotacao(Compra compra) {
		StringBuilder jpql = new StringBuilder(
				"from  ItemCompra it where it.compra = :compra and (it.cancelado is false or it.cancelado is null)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("compra", compra);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<ItemCompra>();
	}

	public List<ItemCompra> getItensTransientByCompra(Compra compra) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW ItemCompra(it.id,p.descricao,it.quantidade,p.categoria,p.tipo,it.unidade,it.descricaoComplementar)");
		jpql.append(" FROM  ItemCompra AS it");
		jpql.append(" JOIN it.produto AS p ");
		jpql.append(" where it.compra = :compra and (it.cancelado is false or it.cancelado is null)");

		TypedQuery<ItemCompra> query = manager.createQuery(jpql.toString(), ItemCompra.class);
		query.setParameter("compra", compra);
		// query.setParameter("cancelado", true);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<ItemCompra>();
	}

	public List<LancamentoAcao> getLancamentosAcao(Lancamento lancamento) {
		StringBuilder jpql = new StringBuilder("from LancamentoAcao where lancamento = :lancamento");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("lancamento", lancamento);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public List<LancamentoAcao> getLancamentosAcao(Lancamento lancamento, String args) {
		StringBuilder jpql = new StringBuilder("from LancamentoAcao where lancamento = :lancamento");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("lancamento", lancamento);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public List<LancamentoAcao> getLancamentosAcao(Long lancamento) {
		StringBuilder jpql = new StringBuilder("from LancamentoAcao where lancamento.id = :lancamento");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("lancamento", lancamento);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public void remover(Lancamento lancamento) {
		this.manager.remove(this.manager.find(Lancamento.class, lancamento.getId()));
	}

	public boolean mudarStatus(Long id, StatusCompra statusCompra) {

		try {

			Compra compra = manager.find(Compra.class, id);
			compra.setStatusCompra(statusCompra);
			manager.merge(compra);
			/*
			 * String jpql =
			 * "update Compra c set c.statusCompra = :statusCompra where c.id = :id" ; Query
			 * query = manager.createQuery(jpql); query.setParameter("statusCompra",
			 * statusCompra); query.setParameter("id", id); query.executeUpdate();
			 */
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public boolean mudarStatusPagamento(Long id, StatusCompra statusCompra) {

		try {

			SolicitacaoPagamento pagamento = manager.find(SolicitacaoPagamento.class, id);
			pagamento.setStatusCompra(statusCompra);
			manager.merge(pagamento);

		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public boolean mudarStatusAdiantamento(Long id, StatusCompra statusCompra) {

		try {

			SolicitacaoPagamento pagamento = manager.find(SolicitacaoPagamento.class, id);
			pagamento.setStatusCompra(statusCompra);
			manager.merge(pagamento);

		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public boolean mudarStatusLancamento(Long id, StatusCompra statusCompra) {

		try {

			Lancamento lancamento = manager.find(Lancamento.class, id);
			lancamento.setStatusCompra(statusCompra);
			manager.merge(lancamento);
			/*
			 * String jpql =
			 * "update Compra c set c.statusCompra = :statusCompra where c.id = :id" ; Query
			 * query = manager.createQuery(jpql); query.setParameter("statusCompra",
			 * statusCompra); query.setParameter("id", id); query.executeUpdate();
			 */
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public Compra mudarStatusCompra(Long id, StatusCompra statusCompra) {
		try {
			Compra compra = manager.find(Compra.class, id);
			compra.setStatusCompra(statusCompra);
			return manager.merge(compra);
		} catch (Exception e) {
			return new Compra();
		}
	}

	public Compra mudarStatusCompraAutorizada(Long id, StatusCompra statusCompra) {
		try {
			Compra compra = manager.find(Compra.class, id);
			compra.setStatusCompra(statusCompra);
			return manager.merge(compra);
		} catch (Exception e) {
			return new Compra();
		}
	}

	public boolean salvarLog(LogStatus logStatus) {
		manager.merge(logStatus);
		return true;
	}

	public void removeAllLancamento(Pedido pedido) {
		StringBuilder jpql = new StringBuilder(
				"delete from  lancamento_acao where fornecedor_id = :fornecedor and compra_id = :compra");
		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("fornecedor", pedido.getFornecedor().getId());
		query.setParameter("compra", pedido.getCompra().getId());
		query.executeUpdate();

	}

	public List<LancamentoAuxiliar> getLancamentosReport(Filtro filtro) {

		// hql
		StringBuilder jpql = new StringBuilder("");
		jpql.append("select a.id as id_acao,");
		jpql.append(" l.id as codigo_lancamento,");
		jpql.append(" l.descricao,");
		jpql.append(" (select f.nome from  fonte_pagadora f where f.id = la.fontepagadora_id) as fonte,");
		jpql.append(" a.codigo as codigo,");
		jpql.append(" la.valor as valor_pago_by_acao,");
		jpql.append(" l.valor_total_com_desconto as valor_total_lancamento, ");
		jpql.append(" l.data_emissao,");
		jpql.append(" l.data_pagamento");
		jpql.append(" from acao a join lancamento_acao la on a.id = la.acao_id ");
		jpql.append(" join lancamento l on la.lancamento_id = l.id ");
		jpql.append(" where l.tipo != 'compra';");

		Query query = manager.createNativeQuery(jpql.toString());
		List<LancamentoAuxiliar> retorno = new ArrayList<LancamentoAuxiliar>();
		List<Object[]> result = query.getResultList();

		LancamentoAuxiliar lAux = new LancamentoAuxiliar();

		for (Object[] object : result) {

			lAux = new LancamentoAuxiliar();
			lAux.setAcaoId(new Long(object[0].toString()));
			lAux.setId(new Long(object[1].toString()));
			lAux.setDescricao(object[2].toString());
			lAux.setFonte(object[3].toString());
			lAux.setCodigo(object[4].toString());
			lAux.setValorPagoAcao(object[5] != null ? new BigDecimal(object[5].toString()) : BigDecimal.ZERO);
			lAux.setValorLancamento(object[6] != null ? new BigDecimal(object[6].toString()) : BigDecimal.ZERO);
			lAux.setDataEmissao(new Date(object[7].toString()));
			lAux.setDataPagamento(new Date(object[8].toString()));
			retorno.add(lAux);
		}

		return retorno;
	}

	public List<LancamentoAcao> getLancamentosDoPedido(Pedido pedido) {
		String jpql = "from LancamentoAcao where fornecedor = :fornecedor and compra = :compra";
		Query query = manager.createQuery(jpql);
		query.setParameter("fornecedor", pedido.getFornecedor());
		query.setParameter("compra", pedido.getCompra());
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public int getQtdItemPedido(Compra compra) {
		String jpql = "from ItemPedido ip where ip.pedido.compra = :compra and (ip.cancelado is false or ip.cancelado is null)";
		Query query = manager.createQuery(jpql);
		query.setParameter("compra", compra);
		return query.getResultList().size();
	}

	public int getQtdItemPedido(Compra compra, Long idPedido) {
		String jpql = "from ItemPedido where pedido.compra = :compra and pedido.id != :pedido";
		Query query = manager.createQuery(jpql);
		query.setParameter("compra", compra);
		query.setParameter("pedido", idPedido);
		return query.getResultList().size();
	}

	public int getQuantidadeItemCompra(Compra compra) {
		String jpql = "from ItemCompra it where it.compra = :compra and (it.cancelado is false or it.cancelado is null)";
		Query query = manager.createQuery(jpql);
		query.setParameter("compra", compra);
		return query.getResultList().size();
	}

	public boolean verificarExistePedido(Long fornecedor, Compra compra) {
		String jpql = "from Pedido where fornecedor.id = :fornecedor and compra = :compra";
		Query query = manager.createQuery(jpql);
		query.setParameter("fornecedor", fornecedor);
		query.setParameter("compra", compra);
		return query.getResultList().size() > 0 ? true : false;
	}

	public ContaBancaria getContaById(Long id) {
		return manager.find(ContaBancaria.class, id);
	}

	public Fornecedor getFornecedorById(Long id) {
		return this.manager.find(Fornecedor.class, id);
	}

	public List<ItemPedido> getItensPedidosByPedido(Lancamento pedido) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW ItemPedido(it.id,it.descricaoProduto,it.quantidade,it.valorUnitario,it.valorUnitarioComDesconto,it.unidade, it.descricaoComplementar)");
		jpql.append(" FROM  ItemPedido AS it");
		jpql.append(" where it.pedido = :pedido");
		TypedQuery<ItemPedido> query = manager.createQuery(jpql.toString(), ItemPedido.class);
		query.setParameter("pedido", pedido);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<ItemPedido>();
	}

}