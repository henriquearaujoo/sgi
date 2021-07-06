package repositorio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import model.AprovacaoProjeto;
import model.CategoriaDespesaClass;
import model.CategoriaProjeto;
import model.Configuracao;
import model.LancamentoAuxiliar;
import model.Orcamento;
import model.OrcamentoProjeto;
import model.Projeto;
import model.ProjetoRubrica;
import model.QualifProjeto;
import model.RubricaOrcamento;
import model.StatusAtividade;
import model.SubPrograma;
import model.TipoAdministrativoProjeto;
import model.User;
import model.UserProjeto;
import util.DataUtil;
import util.DateConverter;
import util.Filtro;
import util.Util;

public class ProjetoRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public ProjetoRepositorio() {
	}

	public ProjetoRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public BigDecimal getOrcamentoDeProjeto(Long id) {
		String jpql = "SELECT sum(p.valor) FROM Projeto p where p.id = :id";
		Query q = this.manager.createQuery(jpql);
		q.setParameter("id", id);
		BigDecimal vl = (BigDecimal) q.getSingleResult();
		return vl;
	}

	public BigDecimal getOrcamentoDeRubrica(Long id) {
		String jpql = "SELECT sum(p.valor) FROM ProjetoRubrica p where p.id = :id";
		Query q = this.manager.createQuery(jpql);
		q.setParameter("id", id);
		BigDecimal vl = (BigDecimal) q.getSingleResult();
		return vl;
	}

	public List<ProjetoRubrica> getRubricasDeProjeto(Long id, Filtro filtro) {

		StringBuilder jpql = new StringBuilder("select pr.id as rubrica_id_0, ");
		jpql.append(
				"(select gest.nome from gestao gest where gest.id = (select pp.gestao_id from projeto pp where pp.id = pr.projeto_id)) as gestao_1,"); // 1
		jpql.append(
				"(select pt.codigo from plano_de_trabalho   pt where pt.id = (select pp.planodetrabalho_id from projeto pp where pp.id = pr.projeto_id)) as code_pt_2,"); // 2
		jpql.append(
				"(select pt.titulo from plano_de_trabalho   pt where pt.id = (select pp.planodetrabalho_id from projeto pp where pp.id = pr.projeto_id)) as titulo_pt_3,"); // 3
		jpql.append(
				"(select loc.mascara from localidade  loc where loc.id = (select pp.localidade_id from projeto pp where pp.id = pr.projeto_id)) as localidade_projeto_4,"); // 4
		jpql.append(
				"(select cad.nome from cadeia_produtiva cad where cad.id = (select pp.cadeia_id from projeto pp where pp.id = pr.projeto_id)) as cadeia_5,"); // 5
		jpql.append(
				"(select f.nome from fonte_pagadora f where f.id = (select orc.fonte_id from orcamento orc where orc.id = (select rb.orcamento_id from rubrica_orcamento rb where rb.id = pr.rubricaorcamento_id))) as fonte_6,"); // 6
		jpql.append(
				"(select orc.titulo from orcamento orc  where orc.id = (select rb.orcamento_id from rubrica_orcamento rb where rb.id = pr.rubricaorcamento_id)) as doacao_7,"); // 7
		jpql.append("(select pp.codigo from projeto pp where pp.id = pr.projeto_id) as codigo_projeto_8,"); // 8
		jpql.append("(select pp.nome from projeto pp where pp.id = pr.projeto_id) as nome_projeto_9,"); // 9
		jpql.append("(select comp.nome from componente_class comp where comp.id = pr.componente_id) as componente_10,"); // 10
		jpql.append(
				"(select scomp.nome from sub_componente scomp where scomp.id = pr.subcomponente_id) as sub_componente_11,"); // 11
		jpql.append(
				"(select rubric.nome from rubrica  rubric  where rubric.id = (select rb.rubrica_id from rubrica_orcamento rb where rb.id = pr.rubricaorcamento_id)) as requisito_doador_12,"); // 12
		jpql.append("pr.valor as valor_13 "); // 13

		jpql.append("from projeto_rubrica pr where ");
		jpql.append(" pr.projeto_id = :id_projeto ");

		if (filtro.getFontes() != null && filtro.getFontes().length > 0) {
			jpql.append(" and (select f.id from fonte_pagadora f where f.id = \n"
					+ "(select o.fonte_id from orcamento o where o.id = \n"
					+ " (select ro.orcamento_id from rubrica_orcamento ro where ro.id = pr.rubricaorcamento_id)) \n"
					+ ") in (:fontes) ");
		}

		Query query = this.manager.createNativeQuery(jpql.toString());
		query.setParameter("id_projeto", id);

		if (filtro.getFontes() != null && filtro.getFontes().length > 0) {
			List<Integer> list = new ArrayList<>();
			Integer[] mList = filtro.getFontes();

			for (int i = 0; i < filtro.getFontes().length; i++) {
				list.add(mList[i]);
			}
			query.setParameter("fontes", list);
		}

		List<Object[]> result = query.getResultList();

		List<ProjetoRubrica> rubricas = new ArrayList<>();
		Integer idAux = 0;
		ProjetoRubrica pAux;
		for (Object[] object : result) {
			// idAux = Integer.valueOf(object[0].toString()) ;
			pAux = new ProjetoRubrica();
			pAux.setId(Long.valueOf(object[0].toString()));
			pAux.setNomeGestao(Util.getNullValue(object[1], "Não encontrado"));
			pAux.setCodigoPlano(Util.getNullValue(object[2], "Não encontrado"));
			pAux.setNomePlano(Util.getNullValue(object[3], "Não encontrado"));
			pAux.setLocalProjeto(Util.getNullValue(object[4], "Não encontrado"));
			pAux.setNomeCadeia(Util.getNullValue(object[5], "Não encontrado"));
			pAux.setNomeFonte(Util.getNullValue(object[6], "Não encontrado"));
			pAux.setNomeDoacao(Util.getNullValue(object[7], "Não encontrado"));
			pAux.setCodigoProjeto(Util.getNullValue(object[8], "Não encontrado"));
			pAux.setNomeProjeto(Util.getNullValue(object[9], "Não encontrado"));
			pAux.setNomeComponente(Util.getNullValue(object[10], "Não encontrado"));
			pAux.setNomeSubComponente(Util.getNullValue(object[11], "Não encontrado"));
			pAux.setNomeRequisitoDoador(Util.getNullValue(object[12], "Não encontrado"));
			pAux.setValor(new BigDecimal(object[13].toString()));
			rubricas.add(pAux);
		}

		// if (filtro.getFontes() != null && filtro.getFontes().length > 0) {
		// jpql.append(" and o.fonte_id in (:fontes) ");
		// }

		// if (filtro.getFontes() != null && filtro.getFontes().length > 0) {
		// List<Integer> list = new ArrayList<>();
		// Integer[] mList = filtro.getFontes();
		//
		// for (int i = 0; i < filtro.getFontes().length; i++) {
		// list.add(mList[i]);
		// }
		// query.setParameter("fontes", list);
		// }

		// List<Object[]> result = query.getResultList();

		// for (Object[] object : result) {
		// orcamentos.add(new Orcamento(new Long(object[0].toString()),
		// object[1].toString()));
		// }

		// StringBuilder jpql = new StringBuilder("SELECT new ProjetoRubrica(p.id,
		// p.rubricaOrcamento.rubrica.nome, p.valor, p.componente.nome) from ");
		// jpql.append("ProjetoRubrica p where p.projeto.id = :id ");
		// Query query = this.manager.createQuery(jpql.toString());
		// query.setParameter("id", id);

		return rubricas;
	}

	public List<ProjetoRubrica> getRubricasDeProjeto(Long id) {

		StringBuilder jpql = new StringBuilder(
				"SELECT new ProjetoRubrica(p.id, p.rubricaOrcamento.rubrica.nome, p.valor, p.componente.nome) from ");
		jpql.append("ProjetoRubrica p where p.projeto.id = :id ");
		Query query = this.manager.createQuery(jpql.toString());
		query.setParameter("id", id);

		return query.getResultList();
	}

	public ProjetoRubrica getRubricaBydIdDetail(Long id) {

		StringBuilder jpql = new StringBuilder(
				"SELECT new ProjetoRubrica(p.id, p.rubricaOrcamento.rubrica.nome, p.valor, p.componente.nome) from ");
		jpql.append("ProjetoRubrica p where p.projeto.id = :id ");
		Query query = this.manager.createQuery(jpql.toString());
		query.setParameter("id", id);

		return (ProjetoRubrica) query.getResultList().get(0);
	}

	public List<ProjetoRubrica> getProjetosRubricasByRubricaOrcamento(RubricaOrcamento rubricaOrcamento) {
		StringBuilder jpql = new StringBuilder("select p from ");
		jpql.append("ProjetoRubrica p where p.rubricaOrcamento = :rubricaOrcamento ");
		Query query = this.manager.createQuery(jpql.toString());
		query.setParameter("rubricaOrcamento", rubricaOrcamento);
		return query.getResultList();
	}

	public List<ProjetoRubrica> getNewProjetosRubricasByRubricaOrcamento(RubricaOrcamento rubricaOrcamento) {
		StringBuilder hql = new StringBuilder();
		hql.append("select ");
		hql.append("p.nome as nome_00, ");
		hql.append("p.valor as valor_projeto_01, ");
		hql.append(
				"(select valor from rubrica_orcamento where id = :rubricaOrcamento) as valor_rubrica_orcamento_02, ");
		hql.append(
				"(select sum(valor) from projeto_rubrica where rubricaorcamento_id = :rubricaOrcamento and projeto_id = p.id)  as valor_empenhado_03, ");
		hql.append(
				"CASE WHEN ((select sum(valor) from projeto_rubrica where rubricaorcamento_id = :rubricaOrcamento and projeto_id = p.id) = 0) THEN 0 ELSE ((select sum(valor) from projeto_rubrica where rubricaorcamento_id = :rubricaOrcamento and projeto_id = p.id )/(select valor from rubrica_orcamento where id = :rubricaOrcamento))*100 END as porcentagem_empenhado_rubrica_04,");
		hql.append(
				"CASE WHEN ((select sum(valor) from projeto_rubrica where rubricaorcamento_id = :rubricaOrcamento and projeto_id = p.id) = 0) THEN 0 ELSE ((select sum(valor) from projeto_rubrica where rubricaorcamento_id = :rubricaOrcamento and projeto_id = p.id )/(p.valor))*100 END as porcentagem_empenhado_projeto_05  ");
		hql.append("from projeto_rubrica  pr inner join projeto p on pr.projeto_id = p.id ");
		hql.append("where pr.rubricaorcamento_id = :rubricaOrcamento");


		Query query = this.manager.createNativeQuery(hql.toString());

		query.setParameter("rubricaOrcamento", rubricaOrcamento.getId());

		List<Object[]> result = query.getResultList();

		List<ProjetoRubrica> rubricas = new ArrayList<ProjetoRubrica>();
		ProjetoRubrica projetoRubrica;
		for (Object[] object : result) {
			projetoRubrica = new ProjetoRubrica(object);
			rubricas.add(projetoRubrica);
		}

		return rubricas;

	}

	public List<SubPrograma> getSubProgramas() {
		String jpql = "from SubPrograma";
		Query query = this.manager.createQuery(jpql);
		return query.getResultList();
	}

	// TODO : qualificações de projeto (REFORMA, AMPLIAÇÃO, CONSTRUÇÃO)
	public List<QualifProjeto> getQualificacoes() {
		String jpql = "from QualifProjeto";
		Query query = this.manager.createQuery(jpql);
		return query.getResultList();
	}

	public SubPrograma getSubPrograma(Long id) {
		return this.manager.find(SubPrograma.class, id);
	}

	public Projeto getProjetoPorId(Long id) {
		return this.manager.find(Projeto.class, id);
	}

	// TODO : qualificações de projeto (REFORMA, AMPLIAÇÃO, CONSTRUÇÃO)
	public QualifProjeto findByIdQualif(Long id) {
		return this.manager.find(QualifProjeto.class, id);
	}

	public CategoriaProjeto getCategoriaProjetoById(Long id) {
		return this.manager.find(CategoriaProjeto.class, id);
	}

	public List<Projeto> verificarNomeProjeto(Projeto projeto) {
		StringBuilder jpql = new StringBuilder("from  Projeto p where p.nome = :nome and versionProjeto = 'mode01' ");

		if (projeto.getId() != null) {
			jpql.append(" and p.id != :id");
		}

		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", projeto.getNome());

		if (projeto.getId() != null) {
			query.setParameter("id", projeto.getId());
		}

		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Projeto>();
	}

	public Boolean removerCategoriaDeProjeto(CategoriaProjeto categoria) {
		this.manager.remove(this.manager.find(CategoriaProjeto.class, categoria.getId()));

		// Colocar regra depois
		return true;

	}

	public List<CategoriaProjeto> getListaDeCategoriasDeProjeto(Filtro filtro) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW CategoriaProjeto(c.id,c.nome,c.valor)  from CategoriaProjeto c where 1 = 1");

		if (filtro.getProjetoId() != null) {
			jpql.append(" and projeto.id = :id");
		}

		Query query = this.manager.createQuery(jpql.toString());

		if (filtro.getProjetoId() != null) {
			query.setParameter("id", filtro.getProjetoId());
		}

		return query.getResultList();
	}

	public CategoriaProjeto salvarCategoriaDeProjeto(CategoriaProjeto categoriaProjeto) {
		return this.manager.merge(categoriaProjeto);
	}

	public AprovacaoProjeto salvarAprovacao(AprovacaoProjeto aprovacaoProjeto) {
		return this.manager.merge(aprovacaoProjeto);
	}

	public List<AprovacaoProjeto> obterAprovacoesProjeto(Projeto projeto) {
		StringBuilder str = new StringBuilder();
		str.append("from AprovacaoProjeto where projeto.id = " + projeto.getId());

		Query query = this.manager.createQuery(str.toString());

		return query.getResultList();
	}

	public List<Projeto> getProjetoByPlano(Long id) {
		StringBuilder hql = new StringBuilder();
		hql.append("select p.codigo as codigo_00, ");
		hql.append("(select l.mascara from localidade l where l.id = p.localidade_id) as localidade_01,");
		hql.append("(select comp.nome from componente_class comp where comp.id = p.componente_id) as componente_02,");
		hql.append("(select cad.nome from cadeia_produtiva  cad where cad.id = p.cadeia_id) as cadeia_03,");
		hql.append(
				"p.objetivo as objetivo_04, p.nome as nome_05, p.quantidade as quantidade_06, p.repasse as repasse_07, p.valor as valor_08, ");
		hql.append("(select font.nome from fonte_pagadora font where font.id = ");
		hql.append("(select orc.fonte_id from orcamento orc where orc.id = ");
		hql.append(
				"(select proj_orc.orcamento_id from orcamento_projeto proj_orc where proj_orc.projeto_id = p.id LIMIT 1))) as fonte_09, ");
		hql.append("p.id, p.observacao  ");
		hql.append(" from projeto as p where 1 = 1");

		hql.append(" and planodetrabalho_id = :plano ");

		hql.append(" order by p.codigo ");

		Query query = this.manager.createNativeQuery(hql.toString());

		query.setParameter("plano", id);

		List<Object[]> result = query.getResultList();
		List<Projeto> projetos = new ArrayList<>();
		Projeto projeto;
		for (Object[] object : result) {
			projeto = new Projeto(object);
			projetos.add(projeto);
		}

		return projetos;
	}

	public List<Projeto> getProjetoByPlanoVisaoGeral(Long id) {
		StringBuilder hql = new StringBuilder();
		hql.append("select p.codigo as codigo_00, ");
		hql.append("(select l.mascara from localidade l where l.id = p.localidade_id) as localidade_01,");
		hql.append("(select comp.nome from componente_class comp where comp.id = p.componente_id) as componente_02,");
		hql.append("(select cad.nome from cadeia_produtiva  cad where cad.id = p.cadeia_id) as cadeia_03,");
		hql.append(
				"p.objetivo as objetivo_04, p.nome as nome_05, p.quantidade as quantidade_06, p.repasse as repasse_07, p.valor as valor_08, ");
		hql.append("(select font.nome from fonte_pagadora font where font.id = ");
		hql.append("(select orc.fonte_id from orcamento orc where orc.id = ");
		hql.append(
				"(select proj_orc.orcamento_id from orcamento_projeto proj_orc where proj_orc.projeto_id = p.id LIMIT 1))) as fonte_09");
		hql.append(" from projeto as p where 1 = 1");

		hql.append(" and planodetrabalho_id = :plano ");

		Query query = this.manager.createNativeQuery(hql.toString());

		query.setParameter("plano", id);

		List<Object[]> result = query.getResultList();
		List<Projeto> projetos = new ArrayList<>();
		Projeto projeto;
		for (Object[] object : result) {
			projeto = new Projeto(object);
			projetos.add(projeto);
		}

		return projetos;
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
		jpql.append(" to_char(l.data_emissao,'DD-MM-YYYY') as data_emissao,");
		jpql.append(" to_char(l.data_pagamento,'DD-MM-YYYY') as data_pagamento");
		jpql.append(" from acao a join lancamento_acao la on a.id = la.acao_id ");
		jpql.append(" join lancamento l on la.lancamento_id = l.id ");
		jpql.append(" where l.tipo != 'compra'");

		if (filtro.getAcaoId() != null) {
			jpql.append(" and a.id = :acao");
		}

		if (filtro.getProjetoId() != null) {
			jpql.append(" and a.projeto_id = :projeto");
		}

		Query query = manager.createNativeQuery(jpql.toString());

		if (filtro.getAcaoId() != null) {
			query.setParameter("acao", filtro.getAcaoId());
		}

		if (filtro.getProjetoId() != null) {
			query.setParameter("projeto", filtro.getProjetoId());
		}

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
			lAux.setDataEmissao(DateConverter.converteDataSql(object[7].toString()));
			lAux.setDataPagamento(DateConverter.converteDataSql(object[8].toString()));
			retorno.add(lAux);
		}

		return retorno;
	}

	public void salvarUsuarioNoProjeto(UserProjeto usuarioProjeto) {
		this.manager.merge(usuarioProjeto);
	}

	public int verificarUsuarioNoProjeto(UserProjeto usuarioProjeto) {
		String jpql = "from UserProjeto where projeto = :projeto  and user = :usuario";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("projeto", usuarioProjeto.getProjeto());
		query.setParameter("usuario", usuarioProjeto.getUser());
		return query.getResultList().size();
	}

	public Integer getQuantidadeAtividade(Projeto projeto, StatusAtividade status) {
		StringBuilder jpql = new StringBuilder(
				"select count(*) from atividade_projeto a  inner join projeto p on a.projeto_id = p.id ");
		jpql.append("where p.id = :projeto");
		if (status != null) {
			jpql.append(" and a.status = :status");
		}
		Query query = manager.createNativeQuery(jpql.toString());
		if (status != null) {
			query.setParameter("status", status.name());
		}
		query.setParameter("projeto", projeto.getId());

		Object object = query.getSingleResult();

		return Integer.parseInt(object.toString() != null ? object.toString() : "0");
	}

	public void salvar(Projeto projeto, User usuario) {
		if (projeto.getId() != null) {
			this.manager.merge(projeto);
		} else {
			UserProjeto usuarioProjeto = new UserProjeto();
			usuarioProjeto.setUser(usuario);
			usuarioProjeto.setProjeto(projeto);
			usuarioProjeto.setTipo(TipoAdministrativoProjeto.CT);
			this.manager.persist(projeto);
			this.manager.persist(usuarioProjeto);

		}

	}

	public Projeto salvarMODE01(Projeto projeto, User usuario) {
		if (projeto.getId() != null) {
			return this.manager.merge(projeto);
		} else {
			UserProjeto usuarioProjeto = new UserProjeto();
			usuarioProjeto.setUser(usuario);
			usuarioProjeto.setProjeto(projeto);
			usuarioProjeto.setTipo(TipoAdministrativoProjeto.CT);
			this.manager.persist(projeto);
			this.manager.persist(usuarioProjeto);
			return projeto;
		}

	}

	public List<Projeto> getProjetosBeanContasApAgaerController() {
		StringBuilder jpql = new StringBuilder("SELECT NEW Projeto(p.id,p.nome) FROM  Projeto as p ");
		Query query = this.manager.createQuery(jpql.toString());
		return query.getResultList();
	}

	public List<Projeto> getProjetosMODE01() {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Projeto(p.id,p.nome) FROM  Projeto as p where p.versionProjeto = 'mode01' ");
		Query query = this.manager.createQuery(jpql.toString());
		return query.getResultList();
	}

	public List<Projeto> getProjetosMODE01(Filtro filtro) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Projeto(p.id,p.nome) FROM  Projeto as p where p.versionProjeto = 'mode01' ");
		Query query = this.manager.createQuery(jpql.toString());
		return query.getResultList();
	}

	public List<Projeto> getProjetosMODE01SQLNATIVE(Filtro filtro) {
		StringBuilder hql = new StringBuilder(
				"SELECT p.id, p.nome, p.codigo FROM projeto as p where p.versionprojeto = 'mode01' ");

		if (filtro.getIdDoacao() != null && filtro.getIdDoacao() != 0l) {
			hql.append(
					"and :id_doacao in (select op.orcamento_id from orcamento_projeto op where op.projeto_id = p.id)");
		}

		if (filtro.getIdPlano() != null && filtro.getIdPlano() != 0l) {
			hql.append("and p.planodetrabalho_id = :id_plano ");
		}

		if (filtro.getPlanos() != null && filtro.getPlanos().length > 0) {
			hql.append("and p.planodetrabalho_id in (:planos) ");
		}

		if (filtro.getGestaoID() != null && filtro.getGestaoID() != 0l) {
			hql.append("and p.gestao_id = :id_gestao ");
		}

		hql.append(" order by p.codigo, p.nome ");

		Query query = this.manager.createNativeQuery(hql.toString());

		if (filtro.getIdDoacao() != null && filtro.getIdDoacao() != 0l) {
			query.setParameter("id_doacao", filtro.getIdDoacao());
		}

		if (filtro.getIdPlano() != null && filtro.getIdPlano() != 0l) {
			query.setParameter("id_plano", filtro.getIdPlano());
		}

		if (filtro.getPlanos() != null && filtro.getPlanos().length > 0) {
			List<Integer> list = new ArrayList<>();
			Integer[] mList = filtro.getPlanos();

			for (int i = 0; i < filtro.getPlanos().length; i++) {
				list.add(mList[i]);
			}
			query.setParameter("planos", list);
		}

		if (filtro.getGestaoID() != null && filtro.getGestaoID() != 0l) {
			query.setParameter("id_gestao", filtro.getGestaoID());
		}

		List<Object[]> result = query.getResultList();
		List<Projeto> projetos = new ArrayList<>();
		Projeto projeto;
		for (Object[] obj : result) {
			projeto = new Projeto(new Long(obj[0].toString()), obj[1].toString(), Util.getNullValue(obj[2], ""));
			projetos.add(projeto);
		}

		return projetos;
	}

	public List<Projeto> getProjetos(List<Integer> projetos) {
		// StringBuilder jpql = new StringBuilder(
		// "SELECT NEW Projeto(p.id,p.nome) FROM Projeto as p ");
		// Query query = this.manager.createQuery(jpql.toString());

		if (!(projetos.size() > 0)) {
			return new ArrayList<>();
		}

		StringBuilder hql = new StringBuilder();
		hql.append("select p.id, p.nome, ");
		hql.append("(select sum(op.valor) from orcamento_projeto op where op.projeto_id = p.id) as valor_orcado ");
		hql.append("from projeto as p where p.id in (:projs)");
		Query query = this.manager.createNativeQuery(hql.toString());
		query.setParameter("projs", projetos);

		List<Object[]> result = query.getResultList();
		List<Projeto> projetosObj = new ArrayList<>();
		Projeto projeto;
		for (Object[] object : result) {
			projeto = new Projeto();
			projeto.setId(object[0] != null ? new Long(object[0].toString()) : new Long(0));
			projeto.setNome(object[1] != null ? object[1].toString() : "");
			projeto.setValor(object[2] != null ? new BigDecimal(object[2].toString()) : BigDecimal.ZERO);
			projetosObj.add(projeto);
		}

		return projetosObj;
	}

	public List<Projeto> getAllProjetosMODE01() {

		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Projeto(p.id,p.codigo,p.nome,p.dataInicio,p.dataFinal,p.valor,p.quantidadeAtividade) FROM Projeto  p ");

		jpql.append(" where 1 = 1 and versionProjeto = 'mode01' ");

		jpql.append("order by p.nome");

		Query query = manager.createQuery(jpql.toString());

		List<Projeto> projetos = query.getResultList();

		return projetos;
	}

	public List<Projeto> getAllProject(Filtro filtro, User user) {

		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Projeto(p.id, p.codigo, p.nome, p.dataInicio, p.dataFinal, p.valor) FROM UserProjeto as up RIGHT JOIN up.projeto as p ");
		jpql.append(" where 1 = 1 and versionProjeto = 'mode01' ");
		
		if (user != null && user.getPerfil().getId().intValue() != 1) {
			jpql.append(" and (up.user.id = :user ");
			jpql.append(" or p.gestao.colaborador.id = :coll)");
		}

		jpql.append(" group by p.id, p.nome order by p.nome");

		Query query = this.manager.createQuery(jpql.toString());

		if (user != null && user.getPerfil().getId().intValue() != 1) {
			query.setParameter("user", user.getId());
			query.setParameter("coll", user.getColaborador().getId());
		}

		return !query.getResultList().isEmpty() ? (List<Projeto>) query.getResultList() : new ArrayList<Projeto>();
	}

	// Verificar utilização em outro módulo , BI, Reports
	public List<Projeto> getProjetosFiltroPorUsuarioWidthColor(Filtro filtro, User usuario) {

		StringBuilder jpql = new StringBuilder();
		jpql.append(
				"select p.id, p.nome, p.valor,to_char(p.datainicio,'DD-MM-YYYY') as data_inicio,to_char(p.datafinal,'DD-MM-YYYY') as data_final,  ");
		jpql.append("p.codigo as codigo, ");
		jpql.append("p.ativo, ");
		// ENTRADAS PROVENIENTES AO PROJETO
		jpql.append("(select sum(pl.valor) ");
		jpql.append("from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id  ");
		jpql.append("	inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");
		jpql.append("	where l.tipo != 'compra' ");
		jpql.append("	and l.statuscompra in ('CONCLUIDO','N_INCIADO') ");
		jpql.append("	and (l.versionlancamento = 'MODE01' or pl.reclassificado is true) ");
		jpql.append(
				"	and (select proj.id from projeto proj where proj.id = (select ppr.projeto_id from projeto_rubrica ppr where ppr.id = la.projetorubrica_id)) = p.id ");
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
		jpql.append(
				" and ((pl.tipocontapagador = 'CF' or pl.tipocontapagador = 'CA') and (pl.tipocontarecebedor = 'CB'))) ");
		jpql.append(" as receita, ");

		// jpql.append(" and (false = ((select cb.tipo from conta_bancaria cb where
		// cb.id = pl.conta_id) = 'CA' and (select cb.tipo from conta_bancaria cb where
		// cb.id = pl.contarecebedor_id) = 'CF'))");
		// jpql.append(" and (false = ((select cb.tipo from conta_bancaria cb where
		// cb.id = pl.conta_id) = 'CB' and (select cb.tipo from conta_bancaria cb where
		// cb.id = pl.contarecebedor_id) = 'CB'))");
		// jpql.append(" and (false = ((select cb.tipo from conta_bancaria cb where
		// cb.id = pl.conta_id) = 'CF' and (select cb.tipo from conta_bancaria cb where
		// cb.id = pl.contarecebedor_id) = 'CF'))");
		// jpql.append(" and (((select cb.tipo from conta_bancaria cb where cb.id =
		// pl.conta_id) = 'CF' or (select cb.tipo from conta_bancaria cb where cb.id =
		// pl.conta_id) = 'CA') ");
		// jpql.append(" and (select cb.tipo from conta_bancaria cb where cb.id =
		// pl.contarecebedor_id) = 'CB')) as receita,");
		// SAÍDAS PROVENIENTES AO PROJETO
		jpql.append("(select sum(pl.valor)");
		jpql.append("	from lancamento l inner join lancamento_acao la on l.id = la.lancamento_id ");
		jpql.append("	inner join pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");
		jpql.append("	where l.tipo != 'compra'  \r\n");
		jpql.append("	and l.statuscompra in ('CONCLUIDO','N_INCIADO') \r\n");
		jpql.append("	and (l.versionlancamento = 'MODE01' or pl.reclassificado is true)\r\n");
		jpql.append(
				"	and (select proj.id from projeto proj where proj.id = (select ppr.projeto_id from projeto_rubrica ppr where ppr.id = la.projetorubrica_id)) = p.id ");
		jpql.append("	and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb')\r\n");
		jpql.append("	and l.tipolancamento != 'reemb_conta' \r\n");
		jpql.append("	and l.tipo != 'baixa_aplicacao'\r\n");
		jpql.append("	and l.tipo != 'doacao_efetiva' \r\n");
		jpql.append("	and l.tipo != 'custo_pessoal' \r\n");
		jpql.append("	and l.tipo != 'aplicacao_recurso'\r\n");
		jpql.append("	and case  p.tarifado  when true then ( 1 = 1 ) else (l.tipo != 'tarifa_bancaria') end	 ");

		jpql.append(" and (false = (pl.tipocontapagador = 'CA' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CB' and pl.tipocontarecebedor = 'CB')) ");
		jpql.append(" and (false = (pl.tipocontapagador = 'CF' and pl.tipocontarecebedor = 'CF')) ");
		jpql.append(
				" and ((pl.tipocontarecebedor = 'CF' or pl.tipocontarecebedor = 'CA') and (pl.tipocontapagador = 'CB'))) ");
		jpql.append(" as despesa, ");

		// jpql.append(" and (false = ((select cb.tipo from conta_bancaria cb where
		// cb.id = pl.conta_id) = 'CA' and (select cb.tipo from conta_bancaria cb where
		// cb.id = pl.contarecebedor_id) = 'CF'))\r\n");
		// jpql.append(" and (false = ((select cb.tipo from conta_bancaria cb where
		// cb.id = pl.conta_id) = 'CB' and (select cb.tipo from conta_bancaria cb where
		// cb.id = pl.contarecebedor_id) = 'CB'))\r\n");
		// jpql.append(" and (false = ((select cb.tipo from conta_bancaria cb where
		// cb.id = pl.conta_id) = 'CF' and (select cb.tipo from conta_bancaria cb where
		// cb.id = pl.contarecebedor_id) = 'CF'))\r\n");
		// jpql.append(" and (((select cb.tipo from conta_bancaria cb where cb.id =
		// pl.contarecebedor_id) = 'CF' or (select cb.tipo from conta_bancaria cb where
		// cb.id = pl.contarecebedor_id) = 'CA')\r\n");
		// jpql.append(" and (select cb.tipo from conta_bancaria cb where cb.id =
		// pl.conta_id) = 'CB')) as despesa, ");

		jpql.append(
				"(select sum((aproj.jan + aproj.fev + aproj.mar + aproj.abr + aproj.mai + aproj.jun + aproj.jul + aproj.ago + aproj.set + aproj.out + aproj.nov + aproj.dez)) from atividade_projeto aproj where aproj.projeto_id = p.id and planejado is true) as qtd_planejada,");
		jpql.append(
				"(select sum((aproj.janexec + aproj.fevexec + aproj.marexec + aproj.abrexec + aproj.maiexec + aproj.junexec + aproj.julexec + aproj.agoexec + aproj.setexec + aproj.outexec + aproj.novexec + aproj.dezexec)) from atividade_projeto aproj where aproj.projeto_id = p.id and planejado is true) as qtd_executada, ");
		jpql.append(" p.statusaprovacao");
		jpql.append(
				" from projeto as p  inner join gestao g on p.gestao_id = g.id  where 1 = 1 and versionprojeto = 'mode01'   ");

		if (usuario != null && !(usuario.getPerfil().getId().equals(Long.parseLong("1")))) {
			jpql.append("and ((p.id in (select up.projeto_id from usuario_projeto up where up.user_id = :usuario)) ");
			jpql.append(" or g.colaborador_id = :colaborador )");

		}
		if (filtro.getGestao() != null && filtro.getGestao().getId() != null) {
			jpql.append("and p.gestao_id = :id_gestao ");
		}

		if (filtro.getProjeto() != null && filtro.getProjeto().getId() != null) {
			jpql.append("and p.id = :id_projeto ");
		}

		if (filtro.getOrcamento() != null && filtro.getOrcamento().getId() != null) {
			jpql.append(
					" and :id_orcamento =  (select orc.id from orcamento orc where orc.id = (select rb.orcamento_id from rubrica_orcamento rb where rb.id = (select pr.rubricaorcamento_id from projeto_rubrica pr where pr.projeto_id = p.id)))");
		}

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			jpql.append(" and p.codigo = :codigo ");
		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			jpql.append(" and lower(p.nome) like '%" + filtro.getNome().toLowerCase().trim() + "%'");
		}

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			jpql.append("and p.datainicio >= '" + sdf.format(filtro.getDataInicio()) + "' and p.datafinal <= '"
					+ sdf.format(filtro.getDataFinal()) + "' ");
		}

		if (filtro.getPlanoDeTrabalho() != null) {
			jpql.append(" and p.planodetrabalho_id = :idPlano");
		}

		if (filtro.getAtivo() != null) {
			jpql.append(" and p.ativo = :ativo ");
		}

		jpql.append(" order by p.codigo, p.nome ");

		Query query = this.manager.createNativeQuery(jpql.toString());

		if (usuario != null && !(usuario.getPerfil().getId().equals(Long.parseLong("1")))) {
			query.setParameter("usuario", usuario.getId());
			query.setParameter("colaborador", usuario.getColaborador().getId());
		}

		if (filtro.getGestao() != null && filtro.getGestao().getId() != null) {
			query.setParameter("id_gestao", filtro.getGestao().getId());
		}

		if (filtro.getProjeto() != null && filtro.getProjeto().getId() != null) {
			query.setParameter("id_projeto", filtro.getProjeto().getId());
		}

		if (filtro.getOrcamento() != null && filtro.getOrcamento().getId() != null) {
			query.setParameter("id_orcamento", filtro.getOrcamento().getId());
		}

		if (filtro.getProjetos() != null && filtro.getProjetos().length > 0) {
			List<Integer> list = new ArrayList<>();
			Integer[] mList = filtro.getProjetos();

			for (int i = 0; i < filtro.getProjetos().length; i++) {
				list.add(mList[i]);
			}

			query.setParameter("projetos", list);

		}

		if (filtro.getCodigo() != null && !filtro.getCodigo().equals("")) {
			query.setParameter("codigo", filtro.getCodigo().trim());
		}

		if (filtro.getPlanoDeTrabalho() != null) {
			query.setParameter("idPlano", filtro.getPlanoDeTrabalho().getId());
		}

		if (filtro.getAtivo() != null) {
			query.setParameter("ativo", filtro.getAtivo());
		}

		List<Object[]> result = query.getResultList();
		List<Projeto> projetos = new ArrayList<>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		for (Object[] object : result) {
			projetos.add(new Projeto(new Long(object[0].toString()), object[1].toString(),
					DataUtil.converteDataSql(object[3].toString()), DataUtil.converteDataSql(object[4].toString()),
					new BigDecimal(object[2] != null ? object[2].toString() : "0"), Util.getNullValue(object[5], ""),
					object[6] != null ? Boolean.valueOf(object[6].toString()) : false,
					new BigDecimal(object[7] != null ? object[7].toString() : "0"),
					new BigDecimal(object[8] != null ? object[8].toString() : "0"),
					Integer.parseInt(object[9] != null ? object[9].toString() : "0"),
					Integer.parseInt(object[10] != null ? object[10].toString() : "0"),
					Util.getNullValue(object[11], "")));

		}

		return projetos;
	}
	
	////// filtro //////
	public List<Projeto> projectFilterByUser(Filtro filtro, User usuario) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Projeto(p.id, p.codigo, p.nome, p.dataInicio, p.dataFinal, p.valor) FROM UserProjeto as up RIGHT JOIN up.projeto as p ");
		jpql.append(" where 1 = 1 and versionProjeto = 'mode01' ");
		
		if (usuario != null && usuario.getPerfil().getId().intValue() != 1) {
			jpql.append(" and (up.user.id = :user ");
			jpql.append(" or p.gestao.colaborador.id = :coll)");
		}
		
		if(filtro.getNovoProjeto() != null) {
			jpql.append(" and p.id = :projeto_id");
		}
		
		if(filtro.getGestao() != null) {
			jpql.append(" and p.gestao.id = :gestao_id");
		}
		
		if (filtro.getDataInicio() != null || filtro.getDataFinal() != null) {
			if (filtro.getDataFinal() != null && filtro.getDataInicio() != null) {
				jpql.append(" and p.dataInicio >= :data_inicio and p.dataFinal <= :data_final ");
			} else if (filtro.getDataFinal() == null) {
				jpql.append(" and p.dataInicio >= :data_inicio ");
			} else {
				jpql.append(" and p.dataFinal <= :data_final");
			}
		}

		jpql.append(" group by p.id, p.nome order by p.nome");

		Query query = this.manager.createQuery(jpql.toString());

		if (usuario != null && usuario.getPerfil().getId().intValue() != 1) {
			query.setParameter("user", usuario.getId());
			query.setParameter("coll", usuario.getColaborador().getId());
		}
		
		if(filtro.getNovoProjeto() != null) {
			query.setParameter("projeto_id", filtro.getNovoProjeto().getId());
		}
		
		if(filtro.getGestao() != null) {
			query.setParameter("gestao_id", filtro.getGestao().getId());
		}
		
		if (filtro.getDataInicio() != null || filtro.getDataFinal() != null) {
			if (filtro.getDataFinal() != null && filtro.getDataInicio() != null) {
				query.setParameter("data_inicio", filtro.getDataInicio());
				query.setParameter("data_final", filtro.getDataFinal());
			} else if (filtro.getDataFinal() == null) {
				query.setParameter("data_inicio", filtro.getDataInicio());
			} else {
				query.setParameter("data_final", filtro.getDataFinal());
			}
		}

		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<Projeto>();
	}

	////////////////
	
	public List<Projeto> getProjetosFiltroPorUsuarioMODE01(Filtro filtro, User usuario) {
		// StringBuilder jpql = new StringBuilder("select p.id p.nome from
		// projeto p join usuario_projeto up on p.id = up.projeto_id ");

		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Projeto(p.id,p.codigo,p.nome,p.dataInicio,p.dataFinal,p.valor,p.quantidadeAtividade) FROM UserProjeto as up RIGHT JOIN up.projeto as p ");

		/*
		 * StringBuilder jpql = new StringBuilder(
		 * "SELECT NEW Projeto(p.id,p.nome) FROM Projeto  UserProjeto as up JOIN up.projeto as p "
		 * );
		 */

		jpql.append(" where 1 = 1 and versionProjeto = 'mode01' ");

		if (usuario != null) {
			jpql.append("and (up.user.id = :usuario ");
			jpql.append(" or p.gestao.colaborador.id = :colaborador)");
		}

		if (filtro.getGestao() != null && filtro.getGestao().getId() != null) {
			jpql.append("and p.gestao.id = :id_gestao ");
		}

		if (filtro.getPlanoDeTrabalho() != null && filtro.getPlanoDeTrabalho().getId() != null) {
			jpql.append("and p.planoDeTrabalho.id = :id_plano ");
		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			jpql.append("and lower(p.nome) like lower(:nome) ");
		}

		jpql.append("group by p.id, p.nome order by p.nome");

		Query query = manager.createQuery(jpql.toString());

		if (usuario != null) {
			query.setParameter("usuario", usuario.getId());
			query.setParameter("colaborador", usuario.getColaborador().getId());
		}

		if (filtro.getGestao() != null && filtro.getGestao().getId() != null) {
			query.setParameter("id_gestao", filtro.getGestao().getId());
		}

		if (filtro.getPlanoDeTrabalho() != null && filtro.getPlanoDeTrabalho().getId() != null) {
			query.setParameter("id_plano", filtro.getPlanoDeTrabalho().getId());
		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			query.setParameter("nome", "%" + filtro.getNome() + "%");
		}

		List<Projeto> projetos = query.getResultList();

		return projetos;
	}

	public List<Projeto> getProjetosFiltroPorUsuario(Filtro filtro, User usuario) {
		// StringBuilder jpql = new StringBuilder("select p.id p.nome from
		// projeto p join usuario_projeto up on p.id = up.projeto_id ");

		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Projeto(p.id,p.nome) FROM UserProjeto as up JOIN up.projeto as p ");

		/*
		 * StringBuilder jpql = new StringBuilder(
		 * "SELECT NEW Projeto(p.id,p.nome) FROM Projeto  UserProjeto as up JOIN up.projeto as p "
		 * );
		 */

		jpql.append(" where 1 = 1 ");

		if (!usuario.getNomeUsuario().equals("admin") && !usuario.getNomeUsuario().equals("paulo.sergio")
				&& !usuario.getNomeUsuario().equals("monique") && !usuario.getNomeUsuario().equals("marina")
				&& !usuario.getNomeUsuario().equals("cirlene") && !usuario.getNomeUsuario().equals("b.silva")) {
			if (usuario != null) {
				jpql.append("and up.user.id = :usuario ");
			}
		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			jpql.append("and lower(p.nome) like lower(:nome) ");
		}

		if (filtro.getGestaoID() != null && filtro.getGestaoID() != 0) {

			jpql.append(" and p.gestao.id = :gestao ");

		}

		jpql.append("group by p.id, p.nome order by p.nome");

		Query query = manager.createQuery(jpql.toString());

		if (!usuario.getNomeUsuario().equals("admin") && !usuario.getNomeUsuario().equals("paulo.sergio")
				&& !usuario.getNomeUsuario().equals("monique") && !usuario.getNomeUsuario().equals("marina")
				&& !usuario.getNomeUsuario().equals("cirlene") && !usuario.getNomeUsuario().equals("b.silva")) {
			if (usuario != null) {
				query.setParameter("usuario", usuario.getId());
			}
		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			query.setParameter("nome", "%" + filtro.getNome() + "%");
		}

		if (filtro.getGestaoID() != null && filtro.getGestaoID() != 0) {
			query.setParameter("gestao", filtro.getGestaoID());

		}

		/*
		 * List<Object[]> results = query.getResultList(); List<Projeto> projetos = new
		 * ArrayList<Projeto>(); for (Object[] objects : results) { Projeto projeto =
		 * new Projeto(); projeto.setId(Long.valueOf(objects[0].toString()));
		 * projeto.setNome(objects[1].toString()); projetos.add(projeto); }
		 */

		List<Projeto> projetos = query.getResultList();

		return projetos;
	}

	public List<Projeto> getProjetosbyUsuario(User usuario) {
		if ((usuario.getPerfil().getId() == Long.parseLong("5"))
				|| (usuario.getPerfil().getId() == Long.parseLong("1"))) {
			StringBuilder jpql = new StringBuilder(
					"SELECT NEW Projeto(p.id,p.nome,p.codigo,p.valor) FROM Projeto as p ");

			jpql.append(" where 1 = 1 and p.versionProjeto = 'mode01' ");

			jpql.append("order by p.nome");

			Query query = manager.createQuery(jpql.toString());

			List<Projeto> projetos = query.getResultList();
			return projetos;

		} else {

			StringBuilder jpql = new StringBuilder(
					"SELECT NEW Projeto(p.id,p.nome,p.codigo,p.valor) FROM UserProjeto as up JOIN up.projeto as p ");

			jpql.append(" where 1 = 1 and p.versionProjeto = 'mode01' ");
			if (usuario != null) {
				jpql.append("and up.user.id = :usuario ");
			}

			jpql.append("group by p.id, p.nome order by p.nome");

			

			Query query = manager.createQuery(jpql.toString());

			if (usuario != null) {
				query.setParameter("usuario", usuario.getId());
			}
			List<Projeto> projetos = query.getResultList();
			return projetos;

		}

	}

	public List<Projeto> getProjetosbyUsuarioProjetoLIBERACAO(User usuario, Filtro filtro) {

		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Projeto(p.id,p.nome,p.codigo) FROM UserProjeto as up RIGHT JOIN up.projeto as p ");

		jpql.append(" where 1 = 1 and p.versionProjeto = 'mode01' ");

		if (usuario != null) {
			jpql.append("and (up.user.id = :usuario ");
			jpql.append(" or p.gestao.colaborador.id = :colaborador)");

		}

		if (filtro.getGestao() != null && filtro.getGestao().getId() != null) {
			jpql.append("and p.gestao.id = :id_gestao ");
		}

		if (filtro.getPlanoDeTrabalho() != null && filtro.getPlanoDeTrabalho().getId() != null) {
			jpql.append("and p.planoDeTrabalho.id = :id_plano ");
		}

		jpql.append("group by p.id, p.nome order by p.nome");

		

		Query query = manager.createQuery(jpql.toString());

		if (usuario != null) {
			query.setParameter("usuario", usuario.getId());
			query.setParameter("colaborador", usuario.getColaborador().getId());
		}

		if (filtro.getGestao() != null && filtro.getGestao().getId() != null) {
			query.setParameter("id_gestao", filtro.getGestao().getId());
		}

		if (filtro.getPlanoDeTrabalho() != null && filtro.getPlanoDeTrabalho().getId() != null) {
			query.setParameter("id_plano", filtro.getPlanoDeTrabalho().getId());
		}

		List<Projeto> projetos = query.getResultList();

		return projetos;

	}

	public Configuracao getConfig() {
		String jpql = "from Configuracao ";
		Query query = manager.createQuery(jpql);
		return (Configuracao) query.getResultList().get(0);
	}

	// TODO: BY USUÁRIO
	public List<Projeto> getProjetosbyUsuarioProjeto(User usuario, Filtro filtro) {

		Calendar calendar = Calendar.getInstance();
		// calendar.setTime(filtro.getDataFinal());

		if (filtro.getVerificVigenciaMenosDias() != null && filtro.getVerificVigenciaMenosDias())
			calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + getConfig().getTempoSC());

		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Projeto(p.id,p.nome,p.codigo,p.valor,p.gestao) FROM UserProjeto as up RIGHT JOIN up.projeto as p ");

		jpql.append(" where 1 = 1 and p.versionProjeto = 'mode01' ");

		jpql.append(" and p.dataFinal >= :data_final ");

		if (usuario != null) {
			jpql.append("and (up.user.id = :usuario ");
			jpql.append(" or p.gestao.colaborador.id = :colaborador)");

		}

		if (filtro.getGestao() != null && filtro.getGestao().getId() != null) {
			jpql.append("and p.gestao.id = :id_gestao ");
		}

		if (filtro.getPlanoDeTrabalho() != null && filtro.getPlanoDeTrabalho().getId() != null) {
			jpql.append("and p.planoDeTrabalho.id = :id_plano ");
		}

		jpql.append("group by p.id, p.nome order by p.nome");

		

		Query query = manager.createQuery(jpql.toString());

		query.setParameter("data_final", calendar.getTime());

		if (usuario != null) {
			query.setParameter("usuario", usuario.getId());
			query.setParameter("colaborador", usuario.getColaborador().getId());
		}

		if (filtro.getGestao() != null && filtro.getGestao().getId() != null) {
			query.setParameter("id_gestao", filtro.getGestao().getId());
		}

		if (filtro.getPlanoDeTrabalho() != null && filtro.getPlanoDeTrabalho().getId() != null) {
			query.setParameter("id_plano", filtro.getPlanoDeTrabalho().getId());
		}

		List<Projeto> projetos = query.getResultList();

		return projetos;
	}

	public List<Projeto> getProjetosbyUsuarioProjeto(User usuario) {

		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Projeto(p.id,p.nome,p.codigo) FROM UserProjeto as up JOIN up.projeto as p ");

		jpql.append(" where 1 = 1 and p.versionProjeto = 'mode01' ");

		if (usuario != null) {
			jpql.append("and up.user.id = :usuario ");
		}

		jpql.append("group by p.id, p.nome order by p.nome");

		

		Query query = manager.createQuery(jpql.toString());

		if (usuario != null) {
			query.setParameter("usuario", usuario.getId());
		}

		List<Projeto> projetos = query.getResultList();

		return projetos;
	}

	public List<UserProjeto> getUsuariosProjetos(Projeto projeto) {
		Query query = manager.createQuery("from UserProjeto u where u.projeto = :projeto").setParameter("projeto",
				projeto);
		return query.getResultList();
	}

	public List<Projeto> getProjetoAutocompleteMODE01(String s) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Projeto(p.id, p.nome, p.codigo) from Projeto p where (lower(p.nome) like lower(:nome) or lower(p.codigo) like lower(:nome))  and versionProjeto = :version");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		query.setParameter("version", "mode01");
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}
	
	public List<Projeto> getProjetoAutocompleteV2(String s) {
		StringBuilder jpql = new StringBuilder(
				"select new Projeto(p.id, p.nome, p.codigo) from Projeto p where (lower(p.codigo) like lower(:nome))  and versionProjeto = :version");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		query.setParameter("version", "mode01");
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	@Deprecated
	public List<Projeto> getProjetoAutocomplete(String s) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Projeto(p.id, p.nome) from Projeto p where lower(p.nome) like lower(:nome)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public List<Projeto> buscarProjetoByGestao(Long id) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Projeto(p.id, p.nome) from Projeto p where p.gestao.id = :id ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public List<CategoriaDespesaClass> getCategoriaAutocomplete(String s) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW CategoriaDespesaClass(c.id, c.nome) from CategoriaDespesaClass c where lower(c.nome) like lower(:nome)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public List<Projeto> getProjetos(Filtro filtro, User usuario) {
		StringBuilder jpql = new StringBuilder("from Projeto o where 1 = 1 ");

		if (filtro.getUsuario() != null) {
			jpql.append("and o.usuario = :usuario ");
		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			jpql.append("and lower(o.nome) like lower(:nome) ");
		}

		jpql.append("order by o.nome");

		Query query = manager.createQuery(jpql.toString());

		if (filtro.getUsuario() != null) {
			query.setParameter("usuario", filtro.getUsuario());
		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			query.setParameter("nome", "%" + filtro.getNome() + "%");
		}

		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<Projeto>();
	}

	public void deleteProjetoRubrica(Long id) {
		String hql = "delete from projeto_rubrica where projeto_id = :id";
		Query q = this.manager.createNativeQuery(hql);
		q.setParameter("id", id);
		q.executeUpdate();
	}

	public void deleteVinculoDoacao(Long id) {
		String hql = "delete from orcamento_projeto where projeto_id = :id";
		Query q = this.manager.createNativeQuery(hql);
		q.setParameter("id", id);
		q.executeUpdate();
	}

	public void deleteVinculoUsuario(Long id) {
		String hql = "delete from usuario_projeto where projeto_id = :id";
		Query q = this.manager.createNativeQuery(hql);
		q.setParameter("id", id);
		q.executeUpdate();
	}

	public void remover(Projeto projeto) {
		projeto = getProjetoPorId(projeto.getId());
		this.manager.remove(this.manager.merge(projeto));
	}

	public void removerUsuarioProjeto(UserProjeto usuarioProjeto) {
		this.manager.remove(this.manager.merge(usuarioProjeto));
	}

	// Metodosp pra usar com lazy
	public List<Projeto> filtrados(Filtro filtro) {
		Criteria criteria = criarCriteria(filtro);

		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());

		if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.asc(filtro.getPropriedadeOrdenacao()));
		} else if (filtro.getPropriedadeOrdenacao() != null) {
			criteria.addOrder(Order.desc(filtro.getPropriedadeOrdenacao()));
		}

		return criteria.list();
	}

	private Criteria criarCriteria(Filtro filtro) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Projeto.class);

		if (filtro.getNome() != null) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}

		return criteria;
	}

	public int quantidadeFiltrados(Filtro filtro) {
		Criteria criteria = criarCriteria(filtro);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

	public List<OrcamentoProjeto> findAll() {
		StringBuilder jpql = new StringBuilder("SELECT op from OrcamentoProjeto op ");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public List<OrcamentoProjeto> findByFiltros(Filtro filtro) {

		String stStatus = "";
		if (filtro.getStatusRecurso() != null && filtro.getStatusRecurso() != "") {
			if (filtro.getStatusRecurso().equals("Em Análise"))
				stStatus = "AND lower(op.status) = lower(:pStatus) OR op.status is NULL ";
			else
				stStatus = "AND lower(op.status) = lower(:pStatus)";
		}

		String stDoacao = "";
		if (filtro.getDoacao() != null && filtro.getDoacao().getTitulo() != "") {
			stDoacao = "AND lower(op.orcamento.titulo) = lower(:pDoacao)";
		}

		String stTipo = "";
		if (filtro.getTipo() != null && filtro.getTipo() != "") {
			stTipo = "AND op.tipo = :pTipo";
		}

		String stData = "";
		if (filtro.getDataInicialRecurso() != null && filtro.getDataFinalRecurso() != null) {
			stData = "AND to_char(op.dataInsercao, 'YYYYMMdd') BETWEEN :pInicio AND :pFim";
		} else if (filtro.getDataInicialRecurso() != null) {
			stData = "AND to_char(op.dataInsercao, 'YYYYMMdd') = :pInicio ";
		} else if (filtro.getDataFinalRecurso() != null) {
			stData = "AND to_char(op.dataInsercao, 'YYYYMMdd') =  :pFim";
		}

		String jpql = "SELECT op from OrcamentoProjeto op WHERE 1 = 1 " + stData + " " + stTipo + " " + stDoacao + " "
				+ stStatus + "ORDER BY op.dataInsercao";
		Query query = manager.createQuery(jpql);

		if (filtro.getDoacao() != null && filtro.getDoacao().getTitulo() != "") {
			query.setParameter("pDoacao", filtro.getDoacao().getTitulo());
		}

		if (filtro.getTipo() != null && filtro.getTipo() != "") {
			query.setParameter("pTipo", filtro.getTipo());
		}

		if (filtro.getStatusRecurso() != null && filtro.getStatusRecurso() != "") {
			query.setParameter("pStatus", filtro.getStatusRecurso());
		}

		if (filtro.getDataInicialRecurso() != null && filtro.getDataFinalRecurso() != null) {
			query.setParameter("pInicio", format(filtro.getDataInicialRecurso(), "YYYYMMdd"));
			query.setParameter("pFim", format(filtro.getDataFinalRecurso(), "YYYYMMdd"));
		} else if (filtro.getDataInicialRecurso() != null) {
			query.setParameter("pInicio", format(filtro.getDataInicialRecurso(), "YYYYMMdd"));
		} else if (filtro.getDataFinalRecurso() != null) {
			query.setParameter("pFim", format(filtro.getDataFinalRecurso(), "YYYYMMdd"));
		}

		return query.getResultList();

	}

	public String format(Date data, String pattern) {
		SimpleDateFormat formato = new SimpleDateFormat(pattern);
		return formato.format(data);
	}

	public List<Orcamento> findOrcamentoByTitulo(String titulo) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Orcamento(o.id,o.titulo,o.contrato) from Orcamento o where lower(o.titulo) like lower(:pTitulo)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("pTitulo", "%" + titulo + "%");
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public OrcamentoProjeto findOrcamentoProjetoById(Long id) {
		StringBuilder jpql = new StringBuilder("SELECT o from OrcamentoProjeto o where o.id = :pId");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("pId", id);
		return (OrcamentoProjeto) query.getSingleResult();
	}

	public Boolean verificaPrivilegioByProjeto(Projeto projeto, User usuario) {
		StringBuilder jpql = new StringBuilder(
				"Select up from UserProjeto up where up.user =:pUsuario and up.projeto =:pProjeto");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("pProjeto", projeto);
		query.setParameter("pUsuario", usuario);
		return query.getResultList().size() > 0;
	}

	public Projeto getDetalhesProjetosById(Projeto projeto) {
		StringBuilder jpql = new StringBuilder(
				"select pl.valor as valor_pago_by_acao_09, l.valor_total_com_desconto as valor_total_lancamento_10,CASE  WHEN ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB')  THEN '-' ELSE '+'  END as sinalizador_22 ");
		jpql.append(
				"from lancamento l join lancamento_acao la on l.id = la.lancamento_id join pagamento_lancamento pl on pl.lancamentoacao_id = la.id ");
		jpql.append(
				"where l.tipo != 'compra'   and l.statuscompra in ('CONCLUIDO','N_INCIADO') and (l.versionlancamento = 'MODE01' or pl.reclassificado is true)  and (l.idadiantamento is null or l.tipolancamento = 'dev' or l.tipolancamento = 'reenb')  and l.tipolancamento != 'reemb_conta'  and l.tipo != 'baixa_aplicacao'  and l.tipo != 'doacao_efetiva'  and l.tipo != 'custo_pessoal'  and l.tipo != 'aplicacao_recurso'  and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CA'   and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))  and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CF'   and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CF'))  and (false = ((select cb.tipo from conta_bancaria cb where  cb.id  = pl.conta_id) = 'CB'  and (select cb.tipo from conta_bancaria cb where  cb.id  = pl.contarecebedor_id) = 'CB'))  ");
		if (projeto != null && projeto.getId() != null) {
			jpql.append(
					"and (select p.id from projeto p where p.id = (select pr.projeto_id from projeto_rubrica pr where id = la.projetorubrica_id))  = :pProjeto");
		}

		Query query = manager.createNativeQuery(jpql.toString());

		if (projeto != null && projeto.getId() != null) {
			query.setParameter("pProjeto", projeto.getId());
		}

		

		List<Object[]> result = query.getResultList();

		BigDecimal totalSaida = BigDecimal.ZERO;
		BigDecimal totalEntrada = BigDecimal.ZERO;

		for (Object[] object : result) {
			if (object[2].toString().equals("-")) {
				totalSaida = totalSaida.add(new BigDecimal(object[1] != null ? object[0].toString() : "0"));
			} else {
				totalEntrada = totalEntrada.add(new BigDecimal(object[1] != null ? object[0].toString() : "0"));
			}

		}

		projeto.setValorEntrada(totalEntrada);
		projeto.setValorSaida(totalSaida);
		projeto.setValorSaldo(projeto.getValor().add(totalEntrada).subtract(totalSaida));

		return projeto;
	}

	public List<Projeto> getProjetosByPlanoDeTrabalhoAndGestao(Filtro filtro) {
		StringBuilder jpql = new StringBuilder("select p.id,p.nome,p.codigo from projeto p where 1 = 1 ");
		if (filtro.getPlanoDeTrabalho() != null) {
			jpql.append("and p.planodetrabalho_id = :pPlano ");
		}

		if (filtro.getGestao() != null) {
			jpql.append("and p.gestao_id = :pGestao ");
		}

		Query query = manager.createNativeQuery(jpql.toString());

		if (filtro.getPlanoDeTrabalho() != null) {
			query.setParameter("pPlano", filtro.getPlanoDeTrabalho().getId());
		}

		if (filtro.getGestao() != null) {
			query.setParameter("pGestao", filtro.getGestao().getId());
		}

		

		List<Object[]> result = query.getResultList();

		List<Projeto> listaProjeto = new ArrayList<Projeto>();

		for (Object[] object : result) {
			Projeto p = new Projeto(object[0] != null ? new Long(object[0].toString()) : null,
					object[1] != null ? object[1].toString() : "", object[2] != null ? object[2].toString() : "");
			listaProjeto.add(p);
		}

		return listaProjeto;
	}

	// NÃO PRECISA DE INNER JOIN JÁ TEMOS O ID DO PROJETO NA ATIVIDADE_PROJETO
	public Integer getQuantidadeAtividadeMes(Projeto projeto, StatusAtividade status, int mes) {
		StringBuilder jpql = new StringBuilder(
				"select COUNT(*) from atividade_projeto a inner join projeto p on a.projeto_id = p.id ");
		jpql.append(" where DATE_PART('MONTH', a.data_conclusao) = :mes and p.id = :projeto");
		if (status != null) {
			jpql.append(" and a.status = :status");
		}
		Query query = manager.createNativeQuery(jpql.toString());
		if (status != null) {
			query.setParameter("status", status.name());
		}

		query.setParameter("mes", mes);
		query.setParameter("projeto", projeto.getId());

		Object object = query.getSingleResult();

		return Integer.parseInt(object.toString() != null ? object.toString() : "0");
	}

	public Integer getQuantidadeAtividadeMesNaoPlanejado(Projeto projeto, int mes, int ano) {
		StringBuilder jpql = new StringBuilder("select  ");

		switch (mes) {
		case 1:
			jpql.append(" sum(jan) ");
			break;
		case 2:
			jpql.append(" sum(fev) ");
			break;
		case 3:
			jpql.append(" sum(mar) ");
			break;
		case 4:
			jpql.append(" sum(abr) ");
			break;
		case 5:
			jpql.append(" sum(mai) ");
			break;
		case 6:
			jpql.append(" sum(jun) ");
			break;
		case 7:
			jpql.append(" sum(jul) ");
			break;
		case 8:
			jpql.append(" sum(ago) ");
			break;
		case 9:
			jpql.append(" sum(set) ");
			break;
		case 10:
			jpql.append(" sum(out) ");
			break;
		case 11:
			jpql.append(" sum(nov) ");
			break;

		default:
			jpql.append(" sum(dez) ");
			break;
		}

		jpql.append(" from atividade_projeto where projeto_id = :projeto ");

		jpql.append("and (planejado is false or planejado is null) ");

		Query query = manager.createNativeQuery(jpql.toString());

		query.setParameter("projeto", projeto.getId());

		Object object = query.getSingleResult();

		int result = 0;

		if (object != null) {
			if (object.toString() != null) {
				result = Integer.parseInt(object.toString());
			}
		}
		return result;
		// return Integer.parseInt(object.toString()!=null?object.toString():"0");
	}

	public Integer getQuantidadeAtividadeMesPlanejado(Projeto projeto, int mes, int ano) {
		StringBuilder jpql = new StringBuilder("select  ");

		switch (mes) {
		case 1:
			jpql.append(" sum(jan) ");
			break;
		case 2:
			jpql.append(" sum(fev) ");
			break;
		case 3:
			jpql.append(" sum(mar) ");
			break;
		case 4:
			jpql.append(" sum(abr) ");
			break;
		case 5:
			jpql.append(" sum(mai) ");
			break;
		case 6:
			jpql.append(" sum(jun) ");
			break;
		case 7:
			jpql.append(" sum(jul) ");
			break;
		case 8:
			jpql.append(" sum(ago) ");
			break;
		case 9:
			jpql.append(" sum(set) ");
			break;
		case 10:
			jpql.append(" sum(out) ");
			break;
		case 11:
			jpql.append(" sum(nov) ");
			break;

		default:
			jpql.append(" sum(dez) ");
			break;
		}

		jpql.append(" from atividade_projeto where projeto_id = :projeto ");

		jpql.append("and planejado is true");

		Query query = manager.createNativeQuery(jpql.toString());

		query.setParameter("projeto", projeto.getId());

		Object object = query.getSingleResult();

		int result = 0;

		if (object != null) {
			if (object.toString() != null) {
				result = Integer.parseInt(object.toString());
			}
		}
		return result;
		// return Integer.parseInt(object.toString()!=null?object.toString():"0");
	}

	public Integer getQuantidadeAtividadeMesExecutado(Projeto projeto, int mes, int ano) {
		StringBuilder jpql = new StringBuilder("select  ");

		switch (mes) {
		case 1:
			jpql.append(" sum(janexec) ");
			break;
		case 2:
			jpql.append(" sum(fevexec) ");
			break;
		case 3:
			jpql.append(" sum(marexec) ");
			break;
		case 4:
			jpql.append(" sum(abrexec) ");
			break;
		case 5:
			jpql.append(" sum(maiexec) ");
			break;
		case 6:
			jpql.append(" sum(junexec) ");
			break;
		case 7:
			jpql.append(" sum(julexec) ");
			break;
		case 8:
			jpql.append(" sum(agoexec) ");
			break;
		case 9:
			jpql.append(" sum(setexec) ");
			break;
		case 10:
			jpql.append(" sum(outexec) ");
			break;
		case 11:
			jpql.append(" sum(novexec) ");
			break;

		default:
			jpql.append(" sum(dezexec) ");
			break;
		}

		jpql.append(" from atividade_projeto where projeto_id = :projeto ");

		jpql.append("and planejado is true ");

		Query query = manager.createNativeQuery(jpql.toString());

		query.setParameter("projeto", projeto.getId());

		Object object = query.getSingleResult();

		int result = 0;

		if (object != null) {
			if (object.toString() != null) {
				result = Integer.parseInt(object.toString());
			}
		}
		return result;
		// return Integer.parseInt(object.toString()!=null?object.toString():"0");
	}

	public Integer getQuantidadeAtividadeMesNOVO(Projeto projeto, StatusAtividade status, int mes, Boolean planejado,
			int ano) {
		StringBuilder jpql = new StringBuilder("select  ");

		switch (mes) {
		case 1:
			jpql.append(" sum(janexec) ");
			break;
		case 2:
			jpql.append(" sum(fevexec) ");
			break;
		case 3:
			jpql.append(" sum(marexec) ");
			break;
		case 4:
			jpql.append(" sum(abrexec) ");
			break;
		case 5:
			jpql.append(" sum(maiexec) ");
			break;
		case 6:
			jpql.append(" sum(junexec) ");
			break;
		case 7:
			jpql.append(" sum(julexec) ");
			break;
		case 8:
			jpql.append(" sum(agoexec) ");
			break;
		case 9:
			jpql.append(" sum(setexec) ");
			break;
		case 10:
			jpql.append(" sum(outexec) ");
			break;
		case 11:
			jpql.append(" sum(novexec) ");
			break;

		default:
			jpql.append(" sum(dezexec) ");
			break;
		}

		jpql.append(" from atividade_projeto where projeto_id = :projeto ");

		jpql.append("and planejado = :planejado");

		Query query = manager.createNativeQuery(jpql.toString());

		query.setParameter("projeto", projeto.getId());
		query.setParameter("planejado", planejado);

		Object object = query.getSingleResult();

		int result = 0;

		if (object != null) {
			if (object.toString() != null) {
				result = Integer.parseInt(object.toString());
			}
		}
		return result;
		// return Integer.parseInt(object.toString()!=null?object.toString():"0");
	}

	public Integer getQuantidadeAtividadeMes(Projeto projeto, StatusAtividade status, int mes, Boolean planejado,
			int ano) {
		StringBuilder jpql = new StringBuilder("select COUNT(*) from atividade_projeto where projeto_id = :projeto ");
		jpql.append(" and DATE_PART('MONTH', data_conclusao) = :mes ");
		jpql.append(" and DATE_PART('YEAR', data_conclusao) = :ano ");
		jpql.append("and planejado = :planejado");

		if (status != null) {
			jpql.append(" and status = :status");
		}
		Query query = manager.createNativeQuery(jpql.toString());

		if (status != null) {
			query.setParameter("status", status.toString());
		}

		query.setParameter("ano", ano);
		query.setParameter("mes", mes);
		query.setParameter("projeto", projeto.getId());
		query.setParameter("planejado", planejado);

		Object object = query.getSingleResult();

		return Integer.parseInt(object.toString() != null ? object.toString() : "0");
	}

	public Integer getQuantidadeAtividadeTotalPorProjetoExecutado(Long id) {
		StringBuilder jpql = new StringBuilder(
				"select sum( (janexec + fevexec + marexec + abrexec + maiexec + junexec + julexec + agoexec + setexec + outexec + novexec + dezexec)) from atividade_projeto where projeto_id = :id ");
		jpql.append(" and planejado is true ");
		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("id", id);
		Object object = query.getSingleResult();
		return Integer.parseInt(object != null ? object.toString() : "0");
	}

	public Integer getQuantidadeAtividadeTotalPorProjeto(Long id) {
		StringBuilder jpql = new StringBuilder(
				"select sum( (jan + fev + mar + abr + mai + jun + jul + ago + set + out + nov + dez)) from atividade_projeto where projeto_id = :id ");
		jpql.append(" and planejado is true ");
		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("id", id);
		Object object = query.getSingleResult();
		return Integer.parseInt(object != null ? object.toString() : "0");
	}

	public Integer getQuantidadeAtividadeTotalPorProjetoNaoPlanejada(Long id) {
		StringBuilder jpql = new StringBuilder(
				"select sum( (jan + fev + mar + abr + mai + jun + jul + ago + set + out + nov + dez)) from atividade_projeto where projeto_id = :id ");
		jpql.append(" and planejado is false ");
		Query query = manager.createNativeQuery(jpql.toString());
		query.setParameter("id", id);
		Object object = query.getSingleResult();
		return Integer.parseInt(object != null ? object.toString() : "0");
	}

	public Projeto getProjetoByProjetoRubrica(Long idProjetoRubrica) {
		StringBuilder jpql = new StringBuilder("Select pr.projeto from ProjetoRubrica pr  where pr.id = :pId");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("pId", idProjetoRubrica);
		return (Projeto) query.getSingleResult();
	}
}
