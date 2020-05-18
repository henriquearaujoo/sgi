package repositorio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Banco;
import model.ContaBancaria;
import model.GuiaImposto;
import model.Imposto;
import model.LancamentoAcao;
import model.LogStatus;
import model.PagamentoLancamento;
import model.StatusCompra;
import model.StatusPagamento;
import model.StatusPagamentoLancamento;
import model.TipoParcelamento;
import model.User;
import operator.Parcela;
import util.FilterImposto;
import util.Filtro;

public class ImpostoRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public ImpostoRepositorio() {
	}

	public Banco salvarBanco(Banco banco) {
		return this.manager.merge(banco);
	}

	public void removerImposto(Imposto imposto) {
		this.manager.remove(this.manager.find(Imposto.class, imposto.getId()));
	}

	public Banco findImpostoById(Long id) {
		return this.manager.find(Banco.class, id);
	}

	public List<GuiaImposto> buscarGuias() {
		StringBuilder jpql = new StringBuilder("from GuiaImposto as g where 1 = 1 ");
		jpql.append(" and g.statusCompra = :statusCompra");
		jpql.append(" order by g.id ");
		Query query = this.manager.createQuery(jpql.toString());
		query.setParameter("statusCompra", StatusCompra.CONCLUIDO);
		return query.getResultList();
	}

	public GuiaImposto buscarGuiaPorId(Long id) {
		return this.manager.find(GuiaImposto.class, id);
	}

	public List<Imposto> buscarImpostos(Filtro filtro) {
		StringBuilder jpql = new StringBuilder("from Imposto as i where 1 = 1 ");

		jpql.append(" and i.pagamentoLancamento.id = :id ");

		jpql.append(" order by i.id ");

		Query query = this.manager.createQuery(jpql.toString());

		// if (filtro.getNome() != null && !filtro.getNome().equals("")) {
		// query.setParameter("nome_banco", "%" + filtro.getNome() + "%");
		// }

		query.setParameter("id", filtro.getLancamentoID());

		return query.getResultList();
	}

	public List<ContaBancaria> getContasFas() {
		String jpql = "SELECT NEW ContaBancaria(cb.id, cb.nomeConta) FROM ContaBancaria AS cb where cb.tipo = :tipo";
		Query query = manager.createQuery(jpql);
		query.setParameter("tipo", "CB");
		return query.getResultList();
	}

	public List<ContaBancaria> getContasForaFas() {
		String jpql = "SELECT NEW ContaBancaria(cb.id, cb.nomeConta) FROM ContaBancaria AS cb where cb.tipo = :tipo";
		Query query = manager.createQuery(jpql);
		query.setParameter("tipo", "CF");
		return query.getResultList();
	}

	public List<Imposto> buscarImpostos(FilterImposto filter) {

		StringBuilder jpql = new StringBuilder("from Imposto as i where 1 = 1 ");

		if (filter.getIdGuia() != null) {
			jpql.append(" and i.guia.id = :id_guia");
		} else {
			jpql.append(" and i.guia is null");
		}

		if (filter.getIdConta() != null) {
			jpql.append(" and i.pagamentoLancamento.contaRecebedor.id = :id_conta");
		}

		if (filter.getTipo() != null) {
			jpql.append(" and i.tipo = :tipo");
		}

		jpql.append(" order by i.id ");

		Query query = this.manager.createQuery(jpql.toString());

		if (filter.getIdGuia() != null) {
			query.setParameter("id_guia", filter.getIdGuia());
		}

		if (filter.getIdConta() != null) {
			query.setParameter("id_conta", filter.getIdConta());
		}

		if (filter.getTipo() != null) {
			query.setParameter("tipo", filter.getTipo());
		}

		return query.getResultList();
	}

	private TipoParcelamento tipoParcelamento;

	public Date setarData(Date dataBase) {
		Parcela parcela = tipoParcelamento.obterParcela();
		Date data = parcela.calculaData(dataBase);
		return data;
	}

	public GuiaImposto salvarGuia(GuiaImposto guia, User usuario) {

		// if (guia.getId() != null)
		// this.manager.createQuery("delete from PagamentoLancamento as pl where
		// pl.lancamentoAcao.lancamento.id = :id")
		// .setParameter("id", guia.getId()).executeUpdate();

		if (guia.getId() != null) {
			this.manager.createNativeQuery("delete from pagamento_lancamento as pl where "
					+ "(select la.lancamento_id from lancamento_acao as la where la.id = pl.lancamentoacao_id) = :id")
					.setParameter("id", guia.getId()).executeUpdate();
			
			this.manager.createNativeQuery("update imposto set guia_id = null where guia_id = :id")
					.setParameter("id", guia.getId()).executeUpdate();
			
		}	

		LogStatus log = new LogStatus();
		log.setUsuario(usuario);
		log.setData(new Date());
		if (guia.getId() != null) {
			log.setStatusLog(StatusCompra.EDICAO);
			log.setSiglaPrivilegio("EDIT");
		} else {
			log.setSiglaPrivilegio("NEW");
			log.setStatusLog(StatusCompra.N_INCIADO);
		}

		guia = this.manager.merge(guia);
		log.setLancamento(guia);
		this.manager.merge(log);

		for (LancamentoAcao lc : guia.getLancamentosAcoes()) {
			lc.setDespesaReceita(guia.getDepesaReceita());
			lc.setLancamento(guia);
			lc.setStatus(StatusPagamento.VALIDADO);
			lc.setTipoLancamento("Tarifa");

			lc = this.manager.merge(lc);

			PagamentoLancamento pl = new PagamentoLancamento();
			pl.setConta(guia.getContaPagador());
			pl.setContaRecebedor(guia.getContaRecebedor());
			pl.setDataEmissao(guia.getDataEmissao());
			pl.setDataPagamento(guia.getDataPagamento());
			pl.setLancamentoAcao(lc);
			pl.setQuantidadeParcela(guia.getQuantidadeParcela());
			pl.setStt(StatusPagamentoLancamento.PROVISIONADO);
			pl.setTipoParcelamento(guia.getTipoParcelamento());
			pl.setValor(lc.getValor());
			pl.setTipoLancamento(guia.getTipoLancamento());
			pl.setDespesaReceita(guia.getDepesaReceita());

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

		return guia;

	}

	public Imposto salvarImposto(Imposto imposto) {
		return this.manager.merge(imposto);
	}

	public Imposto buscarImpostoPorId(Long id) {
		return this.manager.find(Imposto.class, id);
	}

	public GuiaImposto salvarGuia(GuiaImposto guia) {
		return this.manager.merge(guia);
	}

}
