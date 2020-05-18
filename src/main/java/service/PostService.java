package service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Post;
import model.User;
import repositorio.PostRepositorio;
import util.Filtro;

	public class PostService implements Serializable {
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@Inject
		private PostRepositorio repositorio; 

		public PostService() {
		}
		
				
		@Transactional
		public Post salvar(Post post, User usuario){
			
			post.setDataPublicacao(new Date());
			post.setDataInicio(new Date());
			post.setDataFim(new Date());
			post.setGestao(usuario.getColaborador().getGestao());
			post.setUsuario(usuario);
			
			
			return repositorio.salvar(post);
		}
		
		@Transactional
		public void remover(Post post){
			repositorio.remover(post);
		}
		
		public List<Post> getAll(Filtro filtro){
			return repositorio.getAll(filtro);
		}
		
		public Post findById(Long id){
			return repositorio.findById(id);
		}
		
}
	