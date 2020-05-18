package repositorio;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import anotacoes.Transactional;
import model.CategoriaDespesaClass;
import model.ObservacaoProduto;
import model.Produto;
import model.ProdutoHistorico;
import model.UnidadeDeCompra;
import model.User;
import util.Filtro;

public class ProdutoRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@Inject
	private CategoriaRepositorio categoriaRepositorio;

	public ProdutoRepositorio() {
	}

	public ProdutoRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public Produto getProdutoPorId(Long id) {
		return this.manager.find(Produto.class, id);
	}

	public List<UnidadeDeCompra> buscarUnidades() {
		StringBuilder jpql = new StringBuilder("from  UnidadeDeCompra where saneado = :saneado order by descricao");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("saneado", true);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<>();
	}

	public List<Produto> getProduto(String s) {
		StringBuilder jpql = new StringBuilder("from  Produto p where lower(p.descricao) like lower(:descricao)");
		jpql.append(" and p.saneado = :saneado and p.preCadastro = :pre and p.inativo = :inativo ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("descricao", "%" + s + "%");
		query.setParameter("saneado", true);
		query.setParameter("pre", false);
		query.setParameter("inativo", false);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Produto>();
	}

	public List<Produto> getProduto(Filtro filtro) {

		StringBuilder jpql = new StringBuilder("from  Produto p where 1 = 1");

		if (filtro.getDescricaoProduto() != null || !filtro.getDescricaoProduto().equals("")) {
			jpql.append(" lower(p.descricao) like lower(:descricao) ");
		}

		if (filtro.getCategoriaID() != null || filtro.getCategoriaID().longValue() != new Long(0)) {
			jpql.append(" p.categoriaDespesa.id = :categoria ");
		}

		Query query = manager.createQuery(jpql.toString());

		if (filtro.getDescricaoProduto() != null || !filtro.getDescricaoProduto().equals("")) {
			query.setParameter("descricao", filtro.getDescricaoProduto());
		}

		if (filtro.getCategoriaID() != null || filtro.getCategoriaID().longValue() != new Long(0)) {
			query.setParameter("categoria", filtro.getCategoriaID());
		}

		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Produto>();
	}

	public List<CategoriaDespesaClass> getCategoria() {
		return categoriaRepositorio.buscarGategorias();
	}

	public List<CategoriaDespesaClass> buscarGategorias() {
		StringBuilder jpql = new StringBuilder("from  CategoriaDespesaClass where tipoCategoria = :tipo order by nome");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("tipo", "compra");
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<>();
	}

	@Transactional
	public void salvar(Produto produto, User usuario) {
		if (produto.getId() != null) {
			Produto prodAux = getProdutoPorId(produto.getId());
			ProdutoHistorico produtoHistorico = new ProdutoHistorico(prodAux, usuario);
			this.manager.merge(produtoHistorico);
		} else {
			produto.setDataCriacao(new Date());
			produto.setIdUsuario(usuario.getId());
		}

		produto.setPreCadastro(false);
		produto.setCategoria(produto.getCategoriaDespesa().getNome());

		this.manager.merge(produto);
	}

	public String getEmailUsuarioById(Long id) {
		String jpql = "from User where id = :id ";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);

		if (query.getResultList().size() > 0) {
			User usuario = (User) query.getResultList().get(0);
			return usuario.getEmail();
		} else {
			return "";
		}

	}

	@Transactional
	public void salvarPrecadastro(Produto produto, User usuario) {
		if (produto.getId() != null) {
			Produto prodAux = getProdutoPorId(produto.getId());
			ProdutoHistorico produtoHistorico = new ProdutoHistorico(prodAux, usuario);
			this.manager.merge(produtoHistorico);
		} else {
			produto.setDataCriacao(new Date());
			produto.setDataPrecadasto(new Date());
			produto.setIdUsuario(usuario.getId());
		}

		produto.setPreCadastro(true);
		produto.setCategoria(produto.getCategoriaDespesa().getNome());

		this.manager.merge(produto);
	}

	@Transactional
	public void salvarObservacao(ObservacaoProduto observacao) {
		this.manager.merge(observacao);
	}

	public List<ObservacaoProduto> buscarObservacao(Produto produto) {
		String jpql = "from ObservacaoProduto where produto =  :produto order by data";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("produto", produto);
		return query.getResultList();
	}

	@Transactional
	public void salvar(Produto produto) {
		this.manager.merge(produto);
	}

	@Transactional
	public void remover(Produto produto) {
		this.manager.remove(this.manager.merge(produto));
	}

	public String getHistoricoProduto(Long idProduto) {
		String jpql = "from ProdutoHistorico where idProduto = :produto order by dataEdicao";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("produto", idProduto);
		query.setMaxResults(1);
		StringBuilder retorno = new StringBuilder();
		if (query.getResultList().size() > 0) {
			ProdutoHistorico historico = (ProdutoHistorico) query.getResultList().get(0);
			retorno.append(new SimpleDateFormat("dd/MM/yyyy").format(historico.getDataEdicao()));
			retorno.append("- por: ");
			retorno.append(historico.getUsuario().getNomeUsuario());
			return retorno.toString();
		} else {
			return "";
		}
	}

	public List<ProdutoHistorico> getHistoricosByProduto(Long idProduto) {
		String jpql = "from ProdutoHistorico where idProduto = :produto order by dataEdicao";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("produto", idProduto);
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}

	public List<Produto> listarTodos(Produto produto) {
		StringBuilder jpql = new StringBuilder("from  Produto p where 1 = 1 ");

		if (produto.getDescricao() != null && !produto.getDescricao().equals("")) {
			jpql.append(" and lower(p.descricao) like lower(:descricao) ");
		}

		jpql.append(" order by p.descricao ");

		Query query = manager.createQuery(jpql.toString());

		if (produto.getDescricao() != null && !produto.getDescricao().equals("")) {
			query.setParameter("descricao", produto.getDescricao());
		}

		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Produto>();
	}

	public List<Produto> listarTodos(Filtro filtro) {
		StringBuilder jpql = new StringBuilder("from  Produto p where 1 = 1");
		jpql.append(" and p.saneado = :saneado ");
		
		if (filtro.getIdProduto() != null) {
			jpql.append(" and p.id = :id");
		}
		
		if (filtro.getDescricao() != null && !filtro.getDescricao().equals("")) {
			jpql.append(" and lower(p.descricao) like lower(:descricao) ");
		}
		
		if (filtro.getCategoriaID() != null) {
			jpql.append(" and p.categoriaDespesa.id = :id_categoria ");
		}
		
		if (filtro.getStatusCadastro() != null && !filtro.getStatusCadastro().equals("")) {
			jpql.append(" and p.preCadastro = :pre_cadastro ");
		}

		if (filtro.getAtivoInativoProd() != null && !filtro.getAtivoInativoProd().equals("")) {
			jpql.append(" and p.inativo = :inativo ");
		}
		
		jpql.append(" order by p.id desc ");
		
		Query query = manager.createQuery(jpql.toString());
		
		if (filtro.getIdProduto() != null) {
			query.setParameter("id", filtro.getIdProduto());
		}
		
		if (filtro.getDescricao() != null && !filtro.getDescricao().equals("")) {
			query.setParameter("descricao", "%"+filtro.getDescricao()+"%");
		}
		
		if (filtro.getCategoriaID() != null) {
			query.setParameter("id_categoria", filtro.getCategoriaID());
		}
		
		if (filtro.getStatusCadastro() != null && !filtro.getStatusCadastro().equals("")) {
			query.setParameter("pre_cadastro", Boolean.valueOf(filtro.getStatusCadastro()));
		}

		if (filtro.getAtivoInativoProd() != null && !filtro.getAtivoInativoProd().equals("")) {
			query.setParameter("inativo", Boolean.valueOf(filtro.getAtivoInativoProd()));
		}
		
		
		query.setParameter("saneado", true);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Produto>();
	}
	
	
	public Boolean verificarProdutoEmPedido(Long id){
		String jpql = "from ItemCompra ic where ic.produto.id = :id";
		
		Query query = manager.createQuery(jpql); 
		query.setParameter("id", id);
		
		if (query.getResultList().size() > 0) {
			return true;
		}
		
		return false;
	}
	
	public List<Produto> listarTodosSemPrivi(Filtro filtro, User usuario) {
		StringBuilder jpql = new StringBuilder("from  Produto p where 1 = 1 and p.idUsuario = :iduser");
		jpql.append(" and p.saneado = :saneado ");
		jpql.append(" and p.preCadastro = :precad ");
		jpql.append(" order by p.id desc ");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("saneado", true);
		query.setParameter("iduser", usuario.getId());
		query.setParameter("precad", true);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Produto>();
	}

}
