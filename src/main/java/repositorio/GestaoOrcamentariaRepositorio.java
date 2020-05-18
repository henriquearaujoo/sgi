package repositorio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Coordenadoria;
import model.Gestao;
import model.GestaoOrcamento;
import model.GestaoRecurso;
import model.Orcamento;
import model.RubricaOrcamento;
import util.Filtro;

public class GestaoOrcamentariaRepositorio implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public GestaoOrcamentariaRepositorio() {
	}
	
	public GestaoRecurso salvar(GestaoRecurso gestaoRecurso) {
		return this.manager.merge(gestaoRecurso);
	}
	
	public List<GestaoRecurso> getRecursosAportados(Long id){
		StringBuilder jpql = new StringBuilder("from GestaoRecurso gr where gr.gestao.id = :id_gestao ");
		Query query = this.manager.createQuery(jpql.toString());
		query.setParameter("id_gestao", id);
		return query.getResultList();
	}
	
	public void removerRecursoAportado(GestaoRecurso gestaoRecurso) {
		this.manager.remove(this.manager.find(GestaoRecurso.class, gestaoRecurso.getId()));
	}

	public GestaoRecurso findRecursoAportadoById(Long id) {
		return this.manager.find(GestaoRecurso.class, id);
	}
		
	public BigDecimal getValorLiberado(Long gestaoId) {
		String jpql = "select sum(gr.valor) from GestaoRecurso gr where gr.gestao.id = :id ";
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", gestaoId);
		return  query.getSingleResult() != null ? new BigDecimal(query.getSingleResult().toString()) : BigDecimal.ZERO;
	}
	
	// ORCAMENTO LIBERADO DURANTE O ANO
	public GestaoOrcamento salvarOrcamentoDeGestao(GestaoOrcamento gestaoOrcamento) {
		return this.manager.merge(gestaoOrcamento);
	}
	
	// ORCAMENTO LIBERADO DURANTE O ANO	
	public GestaoOrcamento findOrcamentoGestaoById(Long id) {
		return this.manager.find(GestaoOrcamento.class, id);
	}
	
	public GestaoOrcamento findOrcamentoGestaoByGestaoId(Long id) {
		String jpql = "from GestaoOrcamento go where go.gestao.id = :id_gestao";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id_gestao", id);
		return query.getResultList().size() > 0 ? (GestaoOrcamento) query.getResultList().get(0) : null;
	}
	
	
	//GESTAO PARTICIPATIVAS NO ORÇAMENTO GERAL
	
	public Coordenadoria findGestaoById(Long id) {
		return this.manager.find(Coordenadoria.class, id);
	}
	
	public List<Gestao> getGestoesParticipativas() {
		String jpql = "from Gestao g where g.sigla in ('PDI','PGT','PBF','ADM', 'COM','EFP') order by g.nome";
		Query query = this.manager.createQuery(jpql);
		//query.setParameter("class1", "coord");
		return query.getResultList();
	}
	

	public List<Orcamento> getOrcamentos() {
		String jpql = "from Orcamento";
		Query query = this.manager.createQuery(jpql);
		return query.getResultList();
	}
	
	
	public List<RubricaOrcamento> getRubricasDeOrcamento(Long idOrcamento) {
		StringBuilder jpql = new StringBuilder(
				"from  RubricaOrcamento ro where ro.orcamento.id = :id and ro.tipo is null");
	
		jpql.append(" order by ro.componente.nome, ro.subComponente.nome, ro.rubrica.nome ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", idOrcamento);

		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<RubricaOrcamento>();
	}

}
