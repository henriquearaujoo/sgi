package repositorio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import model.Acao;
import model.AcaoProduto;
import model.Componente;
import model.DonationManagement;
import model.GrupoDeInvestimento;
import model.MetaPlano;
import model.Orcamento;
import model.Projeto;
import model.TransferenciaOverHead;
import util.Filtro;

public class DonationManagementRepositorio {

	@Inject
	private EntityManager manager;

	public DonationManagementRepositorio() {
	}

	public DonationManagementRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public void save(DonationManagement donationManagement) {
		this.manager.merge(donationManagement);
	}
	
	public List<BigDecimal> selectValueCurrent(Orcamento orcamento) {
		String jpql = "select dm.valor from DonationManagement dm where 1 = 1 and dm.donation.id = :orcamento_id";
		
		Query query = this.manager.createQuery(jpql);
		query.setParameter("orcamento_id", orcamento.getId());
		 
		List<BigDecimal> values = query.getResultList();
		
		return values;
		
	}
	
	public List<DonationManagement> selectDonationManagement(Long id) {
		String jpql = "from DonationManagement dm where dm.donation.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList();
	}
	
	public String getSumSqlByDonation() {
		return "(select\n"
				+ "sum(pr.valor)\n"
				+ "from fonte_pagadora fp\n"
				+ "join orcamento o on fp.id = o.fonte_id\n"
				+ "join rubrica_orcamento ro on ro.orcamento_id  = o.id\n"
				+ "join projeto_rubrica pr on pr.rubricaorcamento_id = ro.id\n"
				+ "join projeto p on p.id = pr.projeto_id\n"
				+ "where o.id = :id) as total_project ";
	}

}
