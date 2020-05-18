package repositorio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import anotacoes.Transactional;
import model.CategoriaDespesaClass;
import model.CategoriaFinanceira;
import model.UnidadeDeCompra;

public class CategoriaRepositorio implements Serializable{
	
	@Inject	
	private EntityManager manager;

	public CategoriaRepositorio() {
	}


	public CategoriaRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	
	@Transactional
	public void salvar(CategoriaDespesaClass categoria) {
		this.manager.merge(categoria);
	}
	
	
	public UnidadeDeCompra findUndById(Long id){
		return this.manager.find(UnidadeDeCompra.class, id);	
	}
	
	public List<UnidadeDeCompra> buscarUnidades(){
		StringBuilder jpql = new StringBuilder("from  UnidadeDeCompra order by descricao");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<>();
	}
	
	public CategoriaDespesaClass findById(Long id){
		return this.manager.find(CategoriaDespesaClass.class, id);
	}
	
	public CategoriaFinanceira findByIdFinanceira(Long id){
		return this.manager.find(CategoriaFinanceira.class, id);
	}
	
	public List<CategoriaFinanceira> buscarGategoriasFin(){
		StringBuilder jpql = new StringBuilder("from  CategoriaFinanceira order by nome");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<>();
	}
	
	public List<CategoriaDespesaClass> buscarGategorias(){
		StringBuilder jpql = new StringBuilder("from  CategoriaDespesaClass order by nome");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<>();
	}
	
	public List<CategoriaDespesaClass> buscarGategoriasFinanceira(){
		StringBuilder jpql = new StringBuilder("from  CategoriaDespesaClass as c where c.tipoCategoria = 'fin'  order by nome");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<>();
	}
	
	public List<CategoriaDespesaClass> buscarGategoriasCusteioPessoal(){
		StringBuilder jpql = new StringBuilder("from  CategoriaDespesaClass as c where c.tipoCategoria = 'custeio_pessoal'  order by nome");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<>();
	}

	public List<CategoriaDespesaClass> buscarCategoriasFornecedor(){
		StringBuilder jpql = new StringBuilder("from  CategoriaDespesaClass as c where c.tipoCategoria = 'compra'  order by nome");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<>();
	}
	
	@Transactional
	public void remover(CategoriaDespesaClass categoria){
		this.manager.remove(this.manager.merge(categoria));
	}
}
