package repositorio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Banco;
import model.ContaBancaria;
import model.Post;
import util.Filtro;

public class PostRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public PostRepositorio() {
	}

	public Post salvar(Post post) {
		return this.manager.merge(post);
	}

	public void remover(Post post) {
		this.manager.remove(this.manager.find(Post.class, post.getId()));
	}

	public Post findById(Long id) {
		return this.manager.find(Post.class, id);
	}

	public List<Post> getAll(Filtro filtro) {
		StringBuilder jpql = new StringBuilder("from Post as e where 1 = 1 ");

		if (filtro.getTitulo() != null && !filtro.getTitulo().equals("")) {
			jpql.append("and lower(e.titulo) like lower(:titulo) ");
		}
		
		jpql.append(" order by e.dataPublicacao desc ");
		
		Query query = this.manager.createQuery(jpql.toString());

		if (filtro.getTitulo() != null && !filtro.getTitulo().equals("")) {
			query.setParameter("titulo", "%" + filtro.getTitulo() + "%");
		}
	
		return query.getResultList();
	}

}
