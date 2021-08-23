package repositorio;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import anotacoes.Transactional;
import model.CategoriaProjeto;
import model.Colaborador;
import model.Perfil;
import model.Projeto;
import model.User;
import model.UserProjeto;
import util.Filtro;

public class UsuarioRepository {

	private EntityManager manager;

	public UsuarioRepository() {
	}

	@Inject
	public UsuarioRepository(EntityManager manager) {
		this.manager = manager;
	}

	public User getUsuario(String nome, String senha) {
		String jpql = "select u from User u where u.nomeUsuario = :nome and u.senhav4 = :senha";
		Query query = manager.createQuery(jpql);
		query.setParameter("nome", nome);
		query.setParameter("senha", senha);
		return (User) (query.getResultList().size() > 0 ? query.getResultList().get(0) : null);
	}

	public User getEmail(String email) {
		String jpql = "select u from User u where u.email = :toEmail and ativo is true";
		Query query = manager.createQuery(jpql);
		query.setParameter("toEmail", email);
		return (User) (query.getResultList().size() > 0 ? query.getResultList().get(0) : null);
	}

	public List<User> getUsuario(String nome) {
		String jpql = "select u from User u where u.nomeUsuario like :nome";
		Query query = manager.createQuery(jpql);
		query.setParameter("nome", "%" + nome + "%");
		return query.getResultList();
	}
	
	public List<User> filtrarUsuario(Filtro filtro) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW User(u.id,u.nomeUsuario, u.email, u.colaborador.id, u.colaborador.nome, u.ativo) from User u where 1 = 1"
				+ " and u.nomeUsuario <> 'teste' and u.ativo = true and u.nomeUsuario <> 'testing' and u.nomeUsuario <> 'test.financ'");
		
		if(filtro.getUserColaborador() != null && filtro.getUserColaborador().getEmail() != null && filtro.getUserColaborador().getEmail() != "") {
			jpql.append(" and u.email = :email");
		}
		
		if(filtro.getUserColaborador() != null && filtro.getUserColaborador().getNome() != "" &&
				filtro.getUserColaborador().getNome() != null) {
			jpql.append(" and u.colaborador.id = :colaborador_id");
		}
		
		if(filtro.getUsuario() != null && filtro.getUsuario().getNomeUsuario() != null && 
				filtro.getUsuario().getNomeUsuario() != "") {
			jpql.append(" and u.id = :usuario_id");
		}
		
		if(filtro.getGestaoV2() != null && filtro.getGestaoV2().getId() != null) {
			jpql.append(" and u.colaborador.gestao.id = :gestao_id");
		}
		
		Query query = manager.createQuery(jpql.toString());
		
		if(filtro.getUserColaborador() != null && filtro.getUserColaborador().getEmail() != null && filtro.getUserColaborador().getEmail() != "") {
			query.setParameter("email", filtro.getUserColaborador().getEmail());
		}
		
		if(filtro.getUserColaborador() != null && 
				filtro.getUserColaborador().getNome() != "" && filtro.getUserColaborador().getNome() != null && filtro.getUserColaborador().getId() != null) {
			query.setParameter("colaborador_id", filtro.getUserColaborador().getId());
		}
		
		if(filtro.getUsuario() != null && filtro.getUsuario().getNomeUsuario() != null && 
				filtro.getUsuario().getNomeUsuario() != "" && filtro.getUsuario().getId() != null) {
			query.setParameter("usuario_id", filtro.getUsuario().getId());
		}
		
		if(filtro.getGestaoV2() != null && filtro.getGestaoV2().getId() != null) {
			query.setParameter("gestao_id", filtro.getGestaoV2().getId());
		}
		
		return query.getResultList().size() > 0 ? query.getResultList() : getUsuarios(filtro);
	}
	
	public List<User> getUsuarioV2(String nome) {
		String jpql = "select new User(u.id, u.nomeUsuario, u.email) from User u where (u.nomeUsuario like :nome) or (u.email like :nome)";
		Query query = manager.createQuery(jpql);
		query.setParameter("nome", "%" + nome + "%");
		return query.getResultList();
	}
	
	public List<Colaborador> getColaboradorUserV2(String nome) {
		String jpql = "select new Colaborador(c.nome) from Colaborador c where lower(c.nome) like lower(:nome)";
		Query query = manager.createQuery(jpql);
		query.setParameter("nome", "%" + nome + "%");
		return query.getResultList();
	}

	public List<User> getUsuarios(Filtro filtro) {
		String jpql = "SELECT NEW User(u.id,u.nomeUsuario, u.email, u.colaborador.id, u.colaborador.nome, u.ativo) FROM User u where u.ativo is true order by u.nomeUsuario";
		Query query = this.manager.createQuery(jpql);
		return query.getResultList();
	}

	public User getUsuarioByColaborador(Long colaborador) {
		String jpql = "select u from User u where u.colaborador.id = :colaborador";
		Query query = manager.createQuery(jpql);
		query.setParameter("colaborador", colaborador);
		query.setMaxResults(1);
		return query.getResultList().size() > 0 ? (User) query.getResultList().get(0) : null;
	}

	public List<User> getUsuarios() {
		String jpql = "from User a where a.ativo is true";
		Query query = manager.createQuery(jpql);
		return query.getResultList();
	}

	public User getUsuarioById(Long id) {
		return this.manager.find(User.class, id);
	}
	
	public User getUsuario(Long id) {
		String jpql = "FROM User u where u.id = :id";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("id", id);
		return (User) query.getSingleResult();
	}

	public void salvar(User usuario) {
		this.manager.merge(usuario);
	}

	public void remover(UserProjeto usuarioProjeto) {
		this.manager.remove(this.manager.find(UserProjeto.class, usuarioProjeto.getId()));
	}

	public Perfil getPerfil(Long id) {
		return this.manager.find(Perfil.class, id);
	}

	public List<Perfil> getListPerfil() {
		String jpql = "from Perfil p";
		Query query = manager.createQuery(jpql);
		return query.getResultList();
	}

	@Transactional
	public void remover(User usuario) {
		this.manager.merge(usuario);
	}

	@Transactional
	public void updateSenha(User usuario) {
		manager.merge(usuario);
	}

	public User findUsuarioByEmail(String email) {
		String jpql = "SELECT NEW User(u.id, u.nomeUsuario, u.email, u.colaborador.id, u.colaborador.nome, u.ativo) FROM User u where u.email = :email";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("email", email);
		return  query.getResultList().size() > 0 ? (User) query.getResultList().get(0) : null;
	}
	
	public User findUsuarioByEmail(String email, Long id) {
		String jpql = "SELECT NEW User(u.id, u.nomeUsuario, u.email, u.colaborador.id, u.colaborador.nome, u.ativo) FROM User u where u.email = :email and u.id != :id ";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("email", email);
		query.setParameter("id", id);
		return  query.getResultList().size() > 0 ? (User) query.getResultList().get(0) : null;
	}

	public List<User> findUsuarioByProjeto(Projeto projetoSelecionado) {
		String jpql = "select up.user from UserProjeto up where up.projeto = :projeto";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("projeto", projetoSelecionado);
		return query.getResultList().size() > 0 ? query.getResultList() : null;
	}

	public UserProjeto findUsuarioProjeto(Projeto projetoSelecionado, User usuario) {
		String jpql = "select up from UserProjeto up where up.projeto = :projeto  and up.user = :usuario";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("projeto", projetoSelecionado);
		query.setParameter("usuario", usuario);
		return (UserProjeto) query.getSingleResult();
	}

}
