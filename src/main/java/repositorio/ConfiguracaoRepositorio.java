package repositorio;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import anotacoes.Transactional;
import model.ConfigEmail;
import model.Configuracao;
import model.Lancamento;

public class ConfiguracaoRepositorio {

	private EntityManager manager;

	public ConfiguracaoRepositorio(){}

	@Inject
	public ConfiguracaoRepositorio(EntityManager manager){
		this.manager = manager;
	}

	public List<Configuracao> carregaConfiguracao() {
		String hql = "select c from Configuracao c ";
		Query query = manager.createQuery(hql);
		return query.getResultList();
	}
	
	public Configuracao findByid(Long id){
		return this.manager.find(Configuracao.class, id);
	}
	
	
	@Transactional
	public void update(Configuracao configuracao) {
		manager.merge(configuracao);		
	}
	
	public void salvar(Configuracao config) {
		manager.merge(config);
	}

	
	public  List<String> getToEmail(Lancamento lancamento) {
		StringBuilder stb = new StringBuilder();
		stb.append("select email from configemail WHERE tipo = 2 and (gestao = :gestao_id or gestao = 0) and documento = :doc");
		stb.append(" union ");
		stb.append("select email from aprovadordocumento WHERE gestao = :gestao_id and documento = :doc ");
		
		Query query = manager.createNativeQuery(stb.toString());
		query.setParameter("gestao_id", lancamento.getGestao().getId());
		query.setParameter("doc", lancamento.getTipov4());
		
		List<String> result = query.getResultList();

		return result;
	}
	
	public  List<ConfigEmail> getFromEmail() {
		
		String jpql = "from ConfigEmail where tipo = 1 ";
		
		Query query = manager.createQuery(jpql);
		
		return query.getResultList();
	}
	

}
