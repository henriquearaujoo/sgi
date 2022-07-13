package repositorio;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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

import model.ClassificacaoFornecedor;
import model.ContaBancaria;
import model.Fornecedor;
import model.LancamentoAuxiliar;
import model.Produto;
import model.Projeto;
import util.ArquivoFornecedor;
import util.DataUtil;
import util.Filtro;

public class FornecedorRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private EntityManager manager;

	public FornecedorRepositorio() {
	}

	public FornecedorRepositorio(EntityManager manager) {
		this.manager = manager;
	}
	
	public void removerFornecedor(Fornecedor fornecedor) {
		this.manager.remove(this.manager.merge(fornecedor));
	}
	
	public List<ArquivoFornecedor> getArquivosByFornecedor(Long id) {
		String jpql = "from ArquivoFornecedor af where af.fornecedor.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public LancamentoAuxiliar buscarUltimoFornecimento(Long fornecedor) {

		StringBuilder sql = new StringBuilder("select l.id, l.tipo, to_char(l.data_emissao,'dd/MM/yyyy') from lancamento l where l.fornecedor_id = :fornecedor and \n" + 
				"(tipo = 'pedido' or tipo = 'SolicitacaoPagamento') and \n" + 
				"l.data_emissao = (select max(ll.data_emissao) from lancamento ll where ll.fornecedor_id = :fornecedor) LIMIT 1;");

		Query query = manager.createNativeQuery(sql.toString());
		query.setParameter("fornecedor", fornecedor);

		List<LancamentoAuxiliar> lancamentos = new ArrayList<>();
		List<Object[]> list = query.getResultList();

		for (Object[] obj : list) {
			LancamentoAuxiliar lancamento = new LancamentoAuxiliar();
			lancamento.setId(new Long(obj[0].toString()));
			lancamento.setTipo(obj[1].toString());
			lancamento.setDataEmissaoString(obj[2].toString());
			lancamentos.add(lancamento);
		}

		return lancamentos.size() > 0 ? lancamentos.get(0) : null;
	}

	public boolean verificarExisteFornecimento(Long idFornecedor) {
		String jpql = "select count(*) from lancamento l where l.fornecedor_id = :fornecedor and \n"
				+ "(tipo = 'pedido' or tipo = 'SolicitacaoPagamento')";
		Query query = this.manager.createNativeQuery(jpql);
		query.setParameter("fornecedor", idFornecedor);
		Long i = ((BigInteger) query.getSingleResult()).longValue();
		return i > 0 ? true : false;
	}

	public Fornecedor getFornecedorPorId(Long id) {
		return this.manager.find(Fornecedor.class, id);
	}

	public List<Fornecedor> getFornecedores(String s) {
		StringBuilder jpql = new StringBuilder(
				"select new Fornecedor(f.id,f.razaoSocial,f.atividade,f.nomeFantasia,f.endereco,f.bairro,f.cep,f.cnpj,f.cpf,f.inscricaoEstadual,f.inscricaoMunicipal,f.rg,f.telefone,f.pis,f.email,f.tipo,f.observacao,f.ativo,f.taxiid,f.status,f.ativoReceita) from  Fornecedor f where lower(f.nomeFantasia) like lower(:nome) or lower(f.razaoSocial) like lower(:nome) or lower(f.cnpj) like lower(:nome)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Produto>();
	}

	public List<Fornecedor> getFornecedoresAutoComplete(String s) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Fornecedor(f.id,f.nomeFantasia)  from  Fornecedor f where lower(f.nomeFantasia) like lower(:nome) or lower(f.razaoSocial) like lower(:nome) or lower(f.cnpj) like lower(:nome)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		query.setMaxResults(20);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Produto>();
	}

	public ContaBancaria getContaBancaria(ContaBancaria conta) {

		StringBuilder jpql = new StringBuilder("from ContaBancaria where 1 = 1 ");

		if (conta.getId() != null && conta.getId() != new Long(0)) {
			jpql.append(" and id != :id ");
		}

		if (conta.getCnpj() != null && !conta.getCnpj().equals("")) {
			jpql.append(" and cnpj = :cnpj ");
		}

		if (conta.getCpf() != null && !conta.getCpf().equals("")) {
			jpql.append(" and cpf = :cpf ");
		}

		Query query = this.manager.createQuery(jpql.toString());

		if (conta.getId() != null && conta.getId() != new Long(0)) {
			query.setParameter("id", conta.getId());
		}

		if (conta.getCnpj() != null && !conta.getCnpj().equals("")) {
			query.setParameter("cnpj", conta.getCnpj());
		}

		if (conta.getCpf() != null && !conta.getCpf().equals("")) {
			query.setParameter("cpf", conta.getCpf());
		}

		query.setMaxResults(1);

		return query.getResultList().size() > 0 ? (ContaBancaria) query.getResultList().get(0) : null;
	}

	public Fornecedor salvar(Fornecedor fornecedor) {

		if (fornecedor.getId() == null) {
			ContaBancaria conta = new ContaBancaria();
			conta.setTipo("CF");
			conta.setNomeConta(fornecedor.getNomeFantasia());
			conta.setNumeroAgencia("");
			conta.setNumeroConta(fornecedor.getNomeFantasia());
			conta.setFornecedor(this.manager.merge(fornecedor));
			this.manager.merge(conta);
		} else {
			this.manager.merge(fornecedor);
		}

		return new Fornecedor();
	}

	public List<Fornecedor> getFornecedores() {
		StringBuilder jpql = new StringBuilder(
				"select new Fornecedor(f.id,f.razaoSocial,f.atividade,f.nomeFantasia,f.endereco,f.bairro,f.cep,f.cnpj,f.cpf,f.inscricaoEstadual,f.inscricaoMunicipal,f.rg,f.telefone,f.pis,f.email,f.tipo,f.observacao,f.ativo,f.taxiid,f.status,f.ativoReceita) from  Fornecedor  f ");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Fornecedor>();
	}

	public List<Fornecedor> getFornecedores(Filtro filtro) {
		StringBuilder jpql = new StringBuilder(
				"select new Fornecedor(f.id,f.razaoSocial,f.atividade,f.nomeFantasia,f.endereco,f.bairro,f.cep,f.cnpj," +
						"f.cpf,f.inscricaoEstadual,f.inscricaoMunicipal,f.rg,f.telefone,f.pis,f.email,f.tipo," +
						"f.observacao,f.ativo,f.taxiid,f.status,f.ativoReceita) from  Fornecedor f where 1 = 1 ");

		// jpql.append(" and f.classificacao = :classificacao ");

		if (filtro.getNomeFornecedor() != null && !filtro.getNomeFornecedor().equals("")) {
			jpql.append(
					" and ( lower(f.nomeFantasia) like lower(:nome) or lower(f.razaoSocial) like lower(:nome) or lower(f.cnpj) like lower(:nome)) ");
		}

		if (filtro.getCnpjcpf() != null && !filtro.getCnpjcpf().equals("")) {
			jpql.append(" and ( lower(f.cnpj) like lower(:cnpj_cpf) or lower(f.cpf) like lower(:cnpj_cpf) ) ");
		}

		if (filtro.getAtivo() != null) {
			if (filtro.getAtivo())
				jpql.append(" and f.ativo is true");
			else
				jpql.append(" and f.ativo is not true");
		}
		
		jpql.append(" order by f.nomeFantasia");

		Query query = manager.createQuery(jpql.toString());

		//query.setParameter("classificacao", ClassificacaoFornecedor.FORNECEDOR);

		if (filtro.getNomeFornecedor() != null && !filtro.getNomeFornecedor().equals("")) {
			query.setParameter("nome", "%" + filtro.getNomeFornecedor() + "%");
		}

		if (filtro.getCnpjcpf() != null && !filtro.getCnpjcpf().equals("")) {
			query.setParameter("cnpj_cpf", "%" + filtro.getCnpjcpf() + "%");
		}

		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Fornecedor>();
	}

	public List<Fornecedor> getFornecedoresRelatorio(Filtro filtro) {
		StringBuilder sql = new StringBuilder(
				"select coalesce(f.nome_fantasia, '') as nomeFantasia\n" +
						", coalesce(f.razao_social, '') as razaoSocial\n" +
						", coalesce(f.inscricao_estadual, '') as inscEstadual\n" +
						", coalesce(f.pis, '') as pis\n" +
						", case when f.tipo = 'juridica' then f.cnpj else f.cpf end as cpfCnpj\n" +
						", case when f.tipo = 'juridica' then 'Jurídica' else 'Física' end as tipoPessoa\n" +
						", coalesce(f.endereco, '') as endereco\n" +
						", coalesce(f.numero, '') as numero\n" +
						", coalesce(f.telefone, '') as telefone\n" +
						", coalesce(f.email, '') as email\n" +
						", coalesce(f.contato, '') as contato\n" +
						", coalesce(lc.nome, '') as nomeCidade\n" +
						", coalesce(le.nome, '') as estadoCidade\n" +
						", to_char((select max(data_emissao) from lancamento l where\n" +
						"   (tipo = 'SolicitacaoPagamento' or tipo = 'pedido')\n" +
						"   and (select c.fornecedor_id from conta_bancaria c where c.id = l.contarecebedor_id) = f.id), 'dd/MM/yyyy') as ultimoForn\n" +
						", case when f.ativo then 'Sim' else 'Não' end as ativo\n" +
						", case when f.tipo_pagamento = 'DEP_CONTA' then 'Depósito em conta' when f.tipo_pagamento = 'BOLETO' then 'Boleto bancário' else '' end as tipoPagamento\n" +
						", coalesce(cat.nome, '') as categoria\n" +
						", coalesce(b.nomebanco, '') as banco\n" +
						", coalesce(c.numero_agencia, '') || '-' || coalesce(c.digito_agencia, '') as agencia\n" +
						", coalesce(c.numero_conta, '') || '-' || coalesce(c.digito_conta, '') as conta\n" +
						"from fornecedor f\n" +
						"join localidade lc on f.cidade_id = lc.id \n" +
						"join localidade le on f.estado_id = le.id\n" +
						"left join conta_bancaria c on c.fornecedor_id = f.id and c.classificacao_conta = 'PRINCIPAL'\n" +
						"left join banco b on c.banco_id = b.id\n" +
						"left join categoria_despesa cat on cat.id = f.categoriafornecedor1_id and cat.tipocategoria = 'compra'\n" +
						"where 1 = 1");

		sql.append(" and f.classificacao = '" + ClassificacaoFornecedor.FORNECEDOR + "'");

		if (filtro.getNomeFornecedor() != null && !filtro.getNomeFornecedor().equals("")) {
			sql.append(" and ( lower(f.nome_fantasia) like lower('%" + filtro.getNomeFornecedor()
					+ "%') or lower(f.razao_social) like lower('%" + filtro.getNomeFornecedor()
					+ "%') or lower(f.cnpj) like lower('%" + filtro.getNomeFornecedor() + "%')) ");
		}

		if (filtro.getCnpjcpf() != null && !filtro.getCnpjcpf().equals("")) {
			sql.append(" and ( lower(f.cnpj) like lower('%" + filtro.getCnpjcpf() + "%') or lower(f.cpf) like lower('%"
					+ filtro.getCnpjcpf() + "%') ) ");
		}

		if (filtro.getAtivo() != null) {
			if (filtro.getAtivo())
				sql.append(" and f.ativo = true");
			else
				sql.append(" and f.ativo = false");
		}

		Query query = manager.createNativeQuery(sql.toString());

		List<Fornecedor> fornecedores = new ArrayList<>();
		List<Object[]> list = query.getResultList();

		for (Object[] obj : list) {
			Fornecedor f = new Fornecedor();
			f.setNomeFantasia(obj[0] != null ? obj[0].toString() : null);
			f.setRazaoSocial(obj[1] != null ? obj[1].toString() : null);
			f.setInscricaoEstadual(obj[2] != null ? obj[2].toString() : null);
			f.setPis(obj[3] != null ? obj[3].toString() : null);
			f.setCpfcnpj(obj[4] != null ? obj[4].toString() : null);
			f.setTipoPessoa(obj[5] != null ? obj[5].toString() : null);
			f.setEndereco(obj[6] != null ? obj[6].toString() : null);
			f.setNumero(obj[7] != null ? obj[7].toString() : null);
			f.setTelefone(obj[8] != null ? obj[8].toString() : null);
			f.setEmail(obj[9] != null ? obj[9].toString() : null);
			f.setContato(obj[10] != null ? obj[10].toString() : null);
			f.setNomeCidade(obj[11] != null ? obj[11].toString() : null);
			f.setEstadoCidade(obj[12] != null ? obj[12].toString() : null);
			f.setUltimoForn(obj[13] != null ? obj[13].toString() : null);
			f.setAtivoString(obj[14] != null ? obj[14].toString() : null);
			f.setTipoPagamentoString(obj[15] != null ? obj[15].toString() : null);
			f.setCategoria(obj[16] != null ? obj[16].toString() : null);
			f.setBanco(obj[17] != null ? obj[17].toString() : null);
			f.setAgencia(obj[18] != null ? obj[18].toString() : null);
			f.setConta(obj[19] != null ? obj[19].toString() : null);

			fornecedores.add(f);
		}

		return fornecedores;
	}

	public List<Fornecedor> getFornecedoresTerceiros(Filtro filtro) {
		StringBuilder jpql = new StringBuilder(
				"select new Fornecedor(f.id,f.razaoSocial,f.atividade,f.nomeFantasia,f.endereco,f.bairro,f.cep,f.cnpj,f.cpf,f.inscricaoEstadual,f.inscricaoMunicipal,f.rg,f.telefone,f.pis,f.email,f.tipo,f.observacao,f.ativo,f.taxiid,f.status,f.ativoReceita) from  Fornecedor f where 1 = 1 ");

		jpql.append(" and f.classificacao = :classificacao ");

		if (filtro.getNomeFornecedor() != null && !filtro.getNomeFornecedor().equals("")) {
			jpql.append(
					" and ( lower(f.nomeFantasia) like lower(:nome) or lower(f.razaoSocial) like lower(:nome) or lower(f.cnpj) like lower(:nome)) ");
		}

		if (filtro.getCnpjcpf() != null && !filtro.getCnpjcpf().equals("")) {
			jpql.append(" and ( lower(f.cnpj) like lower(:cnpj_cpf) or lower(f.cpf) like lower(:cnpj_cpf) ) ");
		}

		Query query = manager.createQuery(jpql.toString());

		query.setParameter("classificacao", ClassificacaoFornecedor.TERCEIRO);

		if (filtro.getNomeFornecedor() != null && !filtro.getNomeFornecedor().equals("")) {
			query.setParameter("nome", "%" + filtro.getNomeFornecedor() + "%");
		}

		if (filtro.getCnpjcpf() != null && !filtro.getCnpjcpf().equals("")) {
			query.setParameter("cnpj_cpf", "%" + filtro.getCnpjcpf() + "%");
		}

		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Fornecedor>();
	}

	public void remover(Projeto projeto) {
		this.manager.remove(this.manager.merge(projeto));
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

	public void insert(Fornecedor fornecedor) {
		manager.merge(fornecedor);

	}

	public List<Fornecedor> getFornecedoresAtivos() {
		StringBuilder jpql = new StringBuilder(
				"select new Fornecedor(f.id,f.razaoSocial,f.atividade,f.nomeFantasia,f.endereco,f.bairro,f.cep,f.cnpj,f.cpf,f.inscricaoEstadual,f.inscricaoMunicipal,f.rg,f.telefone,f.pis,f.email,f.tipo,f.observacao,f.ativo,f.taxiid,f.status,f.ativoReceita) from  Fornecedor  f where f.ativo is true ");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Fornecedor>();
	}

	public Fornecedor getFornecedor(Long long1) {
		return manager.find(Fornecedor.class, long1);
	}

}
