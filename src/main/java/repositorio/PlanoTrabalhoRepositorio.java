package repositorio;

import java.io.Serializable;
import java.math.BigDecimal;
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

import model.CategoriaDespesaClass;
import model.Gestao;
import model.PlanoDeTrabalho;
import model.Projeto;
import model.TipoAdministrativoProjeto;
import model.User;
import model.UserPlanoTrabalho;
import model.UserProjeto;
import util.Filtro;

public class PlanoTrabalhoRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public PlanoTrabalhoRepositorio() {
	}

	public PlanoTrabalhoRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public PlanoDeTrabalho getPlanoPorId(Long id) {
		return this.manager.find(PlanoDeTrabalho.class, id);
	}

	public void salvarUsuarioNoProjeto(UserPlanoTrabalho userPlano) {
		this.manager.merge(userPlano);
	}

	public int verificarUsuarioNoProjeto(UserPlanoTrabalho usuarioPlano) {
		String jpql = "from UserPlanoTrabalho where plano = :plano  and user = :usuario";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("plano", usuarioPlano.getPlano());
		query.setParameter("usuario", usuarioPlano.getUser());
		return query.getResultList().size();
	}
	
	

	public void salvar(PlanoDeTrabalho plano, User usuario) {
		if (plano.getId() != null) {
			this.manager.merge(plano);
		} else {
			UserPlanoTrabalho usuarioPlano = new UserPlanoTrabalho();
			usuarioPlano.setUser(usuario);
			usuarioPlano.setPlano(plano);
			usuarioPlano.setTipo(TipoAdministrativoProjeto.CT);
			
			
			Gestao gestao = this.manager.find(Gestao.class, plano.getGestao().getId());

			User gestor = (User) this.manager.createQuery("from User where colaborador.id = :id")
					.setParameter("id", gestao.getColaborador().getId()).setMaxResults(1).getResultList().get(0);
			
			UserPlanoTrabalho user2 = new UserPlanoTrabalho();
			user2.setPlano(plano);
			user2.setUser(gestor);
			user2.setTipo(TipoAdministrativoProjeto.CT);

			this.manager.persist(plano);
			this.manager.persist(usuarioPlano);
			this.manager.persist(user2);

		}
	}

	public List<PlanoDeTrabalho> getPlanos(){
		StringBuilder jpql = new StringBuilder("SELECT NEW PlanoDeTrabalho(p.id,p.titulo) FROM PlanoDeTrabalho as p order by p.titulo ");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList();
	}
	
	public List<PlanoDeTrabalho> getPlanosFiltroPorUsuario(Filtro filtro, User usuario) {
		// StringBuilder jpql = new StringBuilder("select p.id p.nome from
		// projeto p join usuario_projeto up on p.id = up.projeto_id ");

		StringBuilder jpql = new StringBuilder(
				"SELECT NEW PlanoDeTrabalho(p.id,p.titulo,p.uc.nome,p.dataInicio,p.dataFinal,p.valor,(select sum(pp.valor) from Projeto pp where pp.planoDeTrabalho.id = p.id),p.gestao.nome, (select count(*) from Projeto pr where pr.planoDeTrabalho.id = p.id)) FROM UserPlanoTrabalho as up JOIN up.plano as p ");

		

		/*
		 * StringBuilder jpql = new StringBuilder(
		 * "SELECT NEW Projeto(p.id,p.nome) FROM Projeto  UserProjeto as up JOIN up.projeto as p "
		 * );
		 * 
		 * this.id = id; this.titulo = titulo; this.nomeUC = nomeUC;
		 * this.dataInicio = dataInicio; this.dataFinal = dataFinal; this.valor
		 * = valor;
		 */

		jpql.append(" where 1 = 1  ");

		if (!usuario.getPerfil().getDescricao().equals("admin")
				&& !usuario.getPerfil().getDescricao().equals("financeiro")) {
			if (usuario != null) {
				jpql.append("and up.user.id = :usuario ");
			}
		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			jpql.append("and lower(p.titulo) like lower(:titulo) ");
		}

		if (filtro.getGestaoID() != null && filtro.getGestaoID() != 0) {

			jpql.append(" and p.gestao.id = :gestao ");

		}

		jpql.append("group by p.id, p.titulo, p.uc.nome,p.gestao.nome order by p.titulo");

		Query query = manager.createQuery(jpql.toString());

		if (!usuario.getPerfil().getDescricao().equals("admin")
				&& !usuario.getPerfil().getDescricao().equals("financeiro")) {
			if (usuario != null) {
				query.setParameter("usuario", usuario.getId());
			}
		}

		if (filtro.getNome() != null && !filtro.getNome().equals("")) {
			query.setParameter("titulo", "%" + filtro.getNome() + "%");
		}

		if (filtro.getGestaoID() != null && filtro.getGestaoID() != 0) {
			query.setParameter("gestao", filtro.getGestaoID());

		}

		/*
		 * List<Object[]> results = query.getResultList(); List<Projeto>
		 * projetos = new ArrayList<Projeto>(); for (Object[] objects : results)
		 * { Projeto projeto = new Projeto();
		 * projeto.setId(Long.valueOf(objects[0].toString()));
		 * projeto.setNome(objects[1].toString()); projetos.add(projeto); }
		 */

		List<PlanoDeTrabalho> planos = query.getResultList();

		return planos;
	}

	public List<UserPlanoTrabalho> getUsuariosProjetos(PlanoDeTrabalho planoDeTrabalho) {
		Query query = manager.createQuery("from UserPlanoTrabalho u where u.plano = :plano").setParameter("plano",
				planoDeTrabalho);
		return query.getResultList();
	}

	public void remover(PlanoDeTrabalho plano) {
		this.manager.remove(this.manager.merge(plano));
	}

	public void removerUsuarioPlano(UserPlanoTrabalho usuarioPlano) {
		this.manager.remove(this.manager.merge(usuarioPlano));
	}

}
