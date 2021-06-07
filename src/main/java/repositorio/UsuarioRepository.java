package repositorio;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import anotacoes.Transactional;
import model.CategoriaProjeto;
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
		String jpql = "select u from User u where u.nomeUsuario = :nome and u.senha = :senha";
		Query query = manager.createQuery(jpql);
		query.setParameter("nome", nome);
		query.setParameter("senha", senha);
		return (User) (query.getResultList().size() > 0 ? query.getResultList().get(0) : null);
	}

	public User getEmail(String email) {
		String jpql = "select u from User u where u.email = :toEmail";
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
