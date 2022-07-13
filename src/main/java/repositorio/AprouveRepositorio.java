
package repositorio;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import anotacoes.Transactional;
import model.Aprouve;
import model.User;

public class AprouveRepositorio implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private EntityManager manager;

	public AprouveRepositorio() {
	}

	public AprouveRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	@Transactional
	public void salvar(Aprouve aprouve) {
		this.manager.merge(aprouve);
	}

	@Transactional
	public void remover(Aprouve aprouve) {
		this.manager.remove(this.manager.find(Aprouve.class, aprouve.getId()));
	}

	public List<Aprouve> findAprouve(User user) {
		String jpql = "from Aprouve a where a.usuario = :user";

		TypedQuery<Aprouve> query = this.manager.createQuery(jpql, Aprouve.class);
		query.setParameter("user", user);
		return query.getResultList().size() > 0 ? query.getResultList() : null;
	}

	public Boolean findAprouve(Aprouve aprouve) {

		String jpql = "from Aprouve a where a.sigla = :sigla and a.senha = :senha";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("sigla", aprouve.getSigla());
		query.setParameter("senha", aprouve.getSenha());
		query.setMaxResults(1);
		return !query.getResultList().isEmpty();
	}

	public Boolean verificaAprouveEstorno(Aprouve aprouve) {

		String jpql = "from Aprouve a where a.sigla = :sigla and a.senha = :senha";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("sigla", aprouve.getSigla());
		query.setParameter("senha", aprouve.getSenha());
		query.setMaxResults(1);
		return !query.getResultList().isEmpty();
	}

	public Boolean verifyAprouve(Aprouve aprouve) {
		String jpql = "from Aprouve a where a.sigla = :sigla and a.senha = :senha";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("sigla", aprouve.getSigla());
		query.setParameter("senha", aprouve.getSenha());
		query.setMaxResults(1);
		return !query.getResultList().isEmpty();
	}

	public Boolean findAprouvePedido(Aprouve aprouve) {
		String jpql = "from Aprouve a where a.sigla = :sigla and a.usuario = :usuario";
		Query query = this.manager.createQuery(jpql);
//		query.setParameter("sigla", aprouve.getSiglaGestao());
		query.setParameter("sigla", aprouve.getSigla());
		query.setParameter("usuario", aprouve.getUsuario());
		query.setMaxResults(1);
		return !query.getResultList().isEmpty();
	}

	public Boolean verificaPrivilegioNF(Aprouve aprouve) {
		String jpql = "from Aprouve a where a.sigla = :sigla and a.usuario = :usuario";
		Query query = this.manager.createQuery(jpql);
//		query.setParameter("sigla", aprouve.getSiglaGestao());
		query.setParameter("sigla", aprouve.getSigla());
		query.setParameter("usuario", aprouve.getUsuario());
		query.setMaxResults(1);
		return !query.getResultList().isEmpty();
	}

	public List<Aprouve> getTodos() {
		String jpql = "from Aprouve";
		Query query = this.manager.createQuery(jpql);
		return query.getResultList();
	}

	public List<User> getUsuario(String nome) {
		String jpql = "select u from User u where u.nomeUsuario like :nome";
		Query query = manager.createQuery(jpql);
		query.setParameter("nome", "%" + nome + "%");
		return query.getResultList();
	}

	public String getCriptografada(String senha) {

		MessageDigest algorithm = null;

		try {
			algorithm = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		byte messageDigest[] = null;

		try {
			messageDigest = algorithm.digest(senha.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StringBuilder hexString = new StringBuilder();

		for (byte b : messageDigest) {
			hexString.append(String.format("%02X", 0xFF & b));
		}

		senha = hexString.toString();

		return senha;
	}

	public Aprouve salvarAprovadores(Aprouve aprouve) {
		return this.manager.merge(aprouve);
	}

}
