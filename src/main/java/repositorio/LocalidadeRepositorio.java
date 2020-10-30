package repositorio;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import anotacoes.Transactional;
import model.Configuracao;
import model.Estado;
import model.Localidade;
import model.Municipio;
import model.Perfil;
import model.UnidadeConservacao;
import model.User;
import model.UserProjeto;
import model.Pais;

public class LocalidadeRepositorio {
	
	private EntityManager manager;
	
	public LocalidadeRepositorio() {}
	
	@Inject
	public LocalidadeRepositorio(EntityManager manager) {
		this.manager = manager;
	}
	//Localidade (id,nome,mascara,codigo,estado,pais,unidadeConservacao);
	public List<Localidade> carregaLocalidade() {
//		String hql = "select new Localidade(l.id,l.nome,l.mascara,e.nome,p.nome,uc.nome) from localidade l left join localidade uc on l.unidadeconversavacao_id = uc.id left join localidade e on l.estado_id = e.id left join localidade p on l.pais_id = p.id";
		StringBuilder hql = new StringBuilder("SELECT NEW Localidade(l.id,l.nome,l.mascara,e.nome,p.nome,uc.nome) from Localidade l left join l.estado e left join l.pais p left join l.unidadeConservacao uc ");
		
		Query query = manager.createQuery(hql.toString());
		return query.getResultList();
	}
	
	public Configuracao findByid(Long id){
		return this.manager.find(Configuracao.class, id);
	}
	
	public Localidade findByIdLocalidade(Long idLocalidade) {
		return manager.find(Localidade.class, idLocalidade);
	}
	
	@Transactional
	public void update(Localidade localidade) {
		manager.merge(localidade);
		
	}
	@Transactional
	public Boolean delete(Localidade localidade) {
		try {
			manager.remove(this.manager.find(Localidade.class, localidade.getId()));
			return true;
		}catch(Exception e) {
			return false;
		}
	}

	public void salvar(Localidade localidade) {
		this.manager.merge(localidade);
		
	}

	public List<Estado> getListEstado() {
		String jpql = "from Estado e";
		Query query = manager.createQuery(jpql);
		return query.getResultList();
		
	}

	public List<Pais> getListPais() {
		String jpql = "from Pais p";
		Query query = manager.createQuery(jpql);
		return query.getResultList();
	}

	public List<UnidadeConservacao> getListUc() {
		String jpql = "from UnidadeConservacao uc";
		Query query = manager.createQuery(jpql);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Municipio> getMunicipios() {
		String jpql = "from Municipio m order by m.nome asc";
		Query query = manager.createQuery(jpql);
		return query.getResultList();
	}
}
