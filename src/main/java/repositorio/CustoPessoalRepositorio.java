package repositorio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.ContaBancaria;
import model.CustoPessoal;
import model.DoacaoEfetiva;
import model.Lancamento;
import model.LancamentoAcao;
import model.LogStatus;
import model.PagamentoLancamento;
import model.Reembolso;
import model.StatusCompra;
import model.StatusPagamento;
import model.StatusPagamentoLancamento;
import model.TipoParcelamento;
import model.User;
import util.DataUtil;
import util.Filtro;

public class CustoPessoalRepositorio implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public CustoPessoalRepositorio() {
	
	}

	private TipoParcelamento tipoParcelamento;
	
	
	public CustoPessoal updateCustoPessoal(CustoPessoal custoPessoal, User usuario) {
		
		custoPessoal = this.manager.merge(custoPessoal);
		custoPessoal.setNumeroDocumento(custoPessoal.getId().toString());
		LogStatus log = new LogStatus();
		log.setUsuario(usuario);
		log.setData(new Date());
		log.setSigla(custoPessoal.getGestao().getSigla());
		if (custoPessoal.getId() != null) {
			log.setSiglaPrivilegio("EDIT");
		} else {
			log.setSiglaPrivilegio("NEW");
			log.setStatusLog(StatusCompra.N_INCIADO);
		}
		log.setLancamento(custoPessoal);
		this.manager.merge(log);
		
		
//		if (custoPessoal.getId() != null)
//			this.manager.createQuery("delete from PagamentoLancamento as pl where pl.lancamento.id = :id")
//					.setParameter("id", custoPessoal.getId()).executeUpdate();
//		
		
//		for (LancamentoAcao lc : custoPessoal.getLancamentosAcoes()) {
//			lc.setDespesaReceita(custoPessoal.getDepesaReceita());
//			lc.setLancamento(custoPessoal);
//			lc.setStatus(StatusPagamento.N_VALIDADO);
//			lc.setTipoLancamento(custoPessoal.getTipoLancamento());
//			lc = this.manager.merge(lc);
//
//			PagamentoLancamento pl = new PagamentoLancamento();
//			pl.setConta(custoPessoal.getContaPagador());
//			pl.setContaRecebedor(custoPessoal.getContaRecebedor());
//			pl.setDataEmissao(custoPessoal.getDataEmissao());
//			pl.setDataPagamento(custoPessoal.getDataPagamento());
//			pl.setLancamento(custoPessoal);
//			pl.setLancamentoAcao(lc);
//			pl.setQuantidadeParcela(custoPessoal.getQuantidadeParcela());
//			pl.setStt(StatusPagamentoLancamento.PROVISIONADO);
//			pl.setTipoParcelamento(custoPessoal.getTipoParcelamento());
//			pl.setValor(lc.getValor());
//			pl.setTipoLancamento(custoPessoal.getTipoLancamento());
//			pl.setDespesaReceita(custoPessoal.getDepesaReceita());
//
//			if (pl.getTipoParcelamento().compareTo(TipoParcelamento.PARCELA_UNICA) == 0) {
//				pl.setQuantidadeParcela(1);
//			}
//
//			Date dataBase = pl.getDataPagamento();
//			tipoParcelamento = pl.getTipoParcelamento();
//			BigDecimal totalParcelado = BigDecimal.ZERO;
//
//			for (int i = 1; i <= pl.getQuantidadeParcela(); i++) {
//				PagamentoLancamento pagto = new PagamentoLancamento(pl, i);
//
//				if (i == 1) {
//					pagto.setDataPagamento(pl.getDataPagamento());
//				} else {
//					pagto.setDataPagamento(DataUtil.setarData(dataBase, tipoParcelamento));
//
//					if (i == pl.getQuantidadeParcela()) {
//						BigDecimal total = pl.getValor();
//						BigDecimal resto = total.subtract(totalParcelado);
//						pagto.setValor(resto);
//					}
//
//				}
//
//				totalParcelado = totalParcelado.add(pagto.getValor());
//
//				this.manager.merge(pagto);
//				dataBase = pagto.getDataPagamento();
//			}
//		}
		
		// return (SolicitacaoPagamento) lancamento;
	return custoPessoal;
}
	
	public CustoPessoal salvar(CustoPessoal custoPessoal, User usuario) {
		
			custoPessoal = this.manager.merge(custoPessoal);
			custoPessoal.setNumeroDocumento(custoPessoal.getId().toString());
			LogStatus log = new LogStatus();
			log.setUsuario(usuario);
			log.setData(new Date());
			log.setSigla(custoPessoal.getGestao().getSigla());
			if (custoPessoal.getId() != null) {
				log.setSiglaPrivilegio("EDIT");
			} else {
				log.setSiglaPrivilegio("NEW");
				log.setStatusLog(StatusCompra.N_INCIADO);
			}
			log.setLancamento(custoPessoal);
			this.manager.merge(log);
			
			
			if (custoPessoal.getId() != null)
				this.manager.createQuery("delete from PagamentoLancamento as pl where pl.lancamento.id = :id")
						.setParameter("id", custoPessoal.getId()).executeUpdate();
			
			
			for (LancamentoAcao lc : custoPessoal.getLancamentosAcoes()) {
				lc.setDespesaReceita(custoPessoal.getDepesaReceita());
				lc.setLancamento(custoPessoal);
				lc.setStatus(StatusPagamento.N_VALIDADO);
				lc.setTipoLancamento(custoPessoal.getTipoLancamento());
				lc = this.manager.merge(lc);

				PagamentoLancamento pl = new PagamentoLancamento();
				pl.setConta(custoPessoal.getContaPagador());
				pl.setContaRecebedor(custoPessoal.getContaRecebedor());
				pl.setDataEmissao(custoPessoal.getDataEmissao());
				pl.setDataPagamento(custoPessoal.getDataPagamento());
				pl.setLancamentoAcao(lc);
				pl.setQuantidadeParcela(custoPessoal.getQuantidadeParcela());
				pl.setStt(StatusPagamentoLancamento.PROVISIONADO);
				pl.setTipoParcelamento(custoPessoal.getTipoParcelamento());
				pl.setValor(lc.getValor());
				pl.setTipoLancamento(custoPessoal.getTipoLancamento());
				pl.setDespesaReceita(custoPessoal.getDepesaReceita());

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
						pagto.setDataPagamento(DataUtil.setarData(dataBase, tipoParcelamento));

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
			
			// return (SolicitacaoPagamento) lancamento;
		return custoPessoal;
	}
	
	
	public Reembolso salvarReembolso(Reembolso reembolso, User usuario, ContaBancaria contaRecebedor, Long idDoacaoReembolsada, Long idFonteReembolsada, Long idRubricaOrcamento) {
		reembolso = this.manager.merge(reembolso);
		reembolso.setNumeroDocumento(reembolso.getId().toString());
		reembolso.setIdFonteReembolso(idFonteReembolsada);
		reembolso.setIdDoacaoReembolso(idDoacaoReembolsada);
		reembolso.setContaRecebedor(contaRecebedor);
		reembolso.setIdRubricaOrcamento(idRubricaOrcamento);
		
		LogStatus log = new LogStatus();
		log.setUsuario(usuario);
		log.setData(new Date());
		log.setSigla(reembolso.getGestao().getSigla());
		if (reembolso.getId() != null) {
			log.setSiglaPrivilegio("EDIT");
		} else {
			log.setSiglaPrivilegio("NEW");
			log.setStatusLog(StatusCompra.N_INCIADO);
		}
		log.setLancamento(reembolso);
		this.manager.merge(log);
		
		if (reembolso.getId() != null)
			this.manager.createQuery("delete from PagamentoLancamento as pl where pl.lancamento.id = :id")
					.setParameter("id", reembolso.getId()).executeUpdate();
		
		
		for (LancamentoAcao lc : reembolso.getLancamentosAcoes()) {
			lc.setDespesaReceita(reembolso.getDepesaReceita());
			lc.setLancamento(reembolso);
			lc.setStatus(StatusPagamento.N_VALIDADO);
			lc.setTipoLancamento(reembolso.getTipoLancamento());
			lc = this.manager.merge(lc);

			PagamentoLancamento pl = new PagamentoLancamento();
			pl.setConta(lc.getRubricaOrcamento().getOrcamento().getContaBancaria());
			pl.setContaRecebedor(contaRecebedor);
			pl.setDataEmissao(reembolso.getDataEmissao());
			pl.setDataPagamento(reembolso.getDataPagamento());
			pl.setLancamentoAcao(lc);
			pl.setQuantidadeParcela(reembolso.getQuantidadeParcela());
			pl.setStt(StatusPagamentoLancamento.PROVISIONADO);
			pl.setTipoParcelamento(reembolso.getTipoParcelamento());
			pl.setValor(lc.getValor());
			pl.setTipoLancamento(reembolso.getTipoLancamento());
			pl.setDespesaReceita(reembolso.getDepesaReceita());

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
					pagto.setDataPagamento(DataUtil.setarData(dataBase, tipoParcelamento));

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

		
		// return (SolicitacaoPagamento) lancamento;
	return reembolso;
}

	public Boolean remover(CustoPessoal custoPessoal) {
		this.manager.remove(this.manager.find(CustoPessoal.class, custoPessoal.getId()));
		return true;
	}
	
	public Boolean removerDoacaoEfetiva(DoacaoEfetiva doacaoEfetiva) {
		this.manager.remove(this.manager.find(DoacaoEfetiva.class, doacaoEfetiva.getId()));
		return true;
	}
	

	public Boolean removerReembolso(Reembolso reembolso) {
		this.manager.remove(this.manager.find(Reembolso.class, reembolso.getId()));
		return true;
	}
	
	
	public DoacaoEfetiva findDoacaoEfetivaById(Long id) {
		return this.manager.find(DoacaoEfetiva.class, id);
	}

	public CustoPessoal findById(Long id) {
		return this.manager.find(CustoPessoal.class, id);
	}

	public List<CustoPessoal> getTodos(Filtro filtro) {
		StringBuilder jpql =  new StringBuilder("from CustoPessoal c where 1 = 1 ");
		
		if (filtro.getCompetencia() != null && !filtro.getCompetencia().equals("")) 
		jpql.append("and c.competencia = :comp");
		
		if (filtro.getIdReembolso() != null) 
			jpql.append("and c.idReembolso = :id_reemb");
		
		Query query = this.manager.createQuery(jpql.toString());
		
		if (filtro.getCompetencia() != null && !filtro.getCompetencia().equals("")) 
		query.setParameter("comp", filtro.getCompetencia());
		
		if (filtro.getIdReembolso() != null) 
			query.setParameter("id_reemb", filtro.getIdReembolso());
		
		return query.getResultList();
	}
	
	
	public Boolean mudarStatusReembolsado(Long id){
		String hql = "update lancamento set reembolsado = :param, idreembolso = :id_nulo  where idreembolso = :id";
		Query query = this.manager.createNativeQuery(hql);
		query.setParameter("id", id);
		query.setParameter("param", false);
		query.setParameter("id_nulo", 0);
		query.executeUpdate();
		return true;
	}
	
	public Boolean mudarIdReembolsoEmCustoDePessoal(Long id){
		String hql = "update lancamento set idreembolso = :param where idreembolso = :id";
		Query query = this.manager.createNativeQuery(hql);
		query.setParameter("id", id);
		query.setParameter("param", false);
		query.executeUpdate();
		return true;
	}
	
	
	public List<Reembolso> getTodosReembolsos(Filtro filtro) {
		StringBuilder jpql =  new StringBuilder("from Reembolso c where 1 = 1 ");
		
		if (filtro.getCompetencia() != null && !filtro.getCompetencia().equals("")) 
		jpql.append("and c.competencia = :comp");
		
		Query query = this.manager.createQuery(jpql.toString());
		
		if (filtro.getCompetencia() != null && !filtro.getCompetencia().equals("")) 
		query.setParameter("comp", filtro.getCompetencia());
		
		
		return query.getResultList();
	}

}
