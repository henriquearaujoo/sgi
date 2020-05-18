package repositorio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Acao;
import model.Comunidade;
import model.Estado;
import model.Localidade;
import model.Municipio;
import model.Projeto;
import model.UnidadeConservacao;

public class LocalRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public LocalRepositorio() {
	}

	public LocalRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public Localidade getLocalPorId(Long id) {
		return this.manager.find(Localidade.class, id);
	}

	public Localidade findByIdLocal(Long id) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Localidade(l.id, l.nome, l.class, l.mascara) from Localidade l where l.id = :id");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? (Localidade) query.getResultList().get(0) : new Localidade();
	}
	
	
	public Estado findEstadoBydId(Long id) {
		return this.manager.find(Estado.class, id);
	}
	
	
	public List<Localidade> findCidadeByEstado(Long estado) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Localidade(l.id, l.nome, l.class, l.mascara) from Localidade l where l.estado.id = :estado");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("estado", estado);
		return query.getResultList().size() > 0 ?  query.getResultList() : new ArrayList<>();
	}
	
	public List<Estado> getEstados() {
		StringBuilder jpql = new StringBuilder(
				"from Estado");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0 ? query.getResultList() : new ArrayList<>();
	}
	
	

	public void salvar(Localidade local) {
		this.manager.merge(local);
	}
	
	public UnidadeConservacao findUcByCodigo(Long id){
		return this.manager.find(UnidadeConservacao.class, id);
	}

	public List<Comunidade> getComunidade(Comunidade comunidade) {
		StringBuilder jpql = new StringBuilder("from  Comunidade");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Projeto>();
	}

	public List<Localidade> getComunidades() {
		StringBuilder jpql = new StringBuilder("from  Comunidade");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Projeto>();
	}

	public List<Localidade> getUcs() {
		StringBuilder jpql = new StringBuilder("from  UnidadeConservacao");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Projeto>();
	}

	public List<Localidade> getSede() {
		StringBuilder jpql = new StringBuilder("from  Sede");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Projeto>();
	}

	public List<Localidade> getMunicipio() {
		StringBuilder jpql = new StringBuilder("from  Municipio");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Projeto>();
	}

	public List<Municipio> getMunicipiosDeCompra() {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW  Municipio(m.id,m.nome) FROM Municipio m where m.estado.id = :estado and m.municipioDeCompra = :param");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("estado", new Long(4));
		query.setParameter("param", true);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Projeto>();
	}

	public List<Localidade> getMunicipioByEstado(Long id) {
		StringBuilder jpql = new StringBuilder("from  Municipio where estado.id = :id");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("id", id);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Projeto>();
	}

	public List<Localidade> getNucleo() {
		StringBuilder jpql = new StringBuilder("from  Nucleo");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Projeto>();
	}

	public List<Localidade> getLocalidades(Class<Localidade> localClass) {
		StringBuilder jpql = new StringBuilder("from  Localidade");
		Query query = manager.createQuery(jpql.toString(), localClass);
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Projeto>();
	}

	public List<UnidadeConservacao> getUnidadeConservacao(UnidadeConservacao unidade) {
		StringBuilder jpql = new StringBuilder("from  UnidadeConservacao");
		Query query = manager.createQuery(jpql.toString());
		return query.getResultList().size() > 0 ? query.getResultList() : (List) new ArrayList<Projeto>();
	}

	public UnidadeConservacao findByIdUnidadeDeConservacao(Long id) {
		return this.manager.find(UnidadeConservacao.class, id);
	}
	
	public Comunidade findByIdComunidade(Long id) {
		return this.manager.find(Comunidade.class, id);
	}
	
	public Municipio findByIdMunicipio(Long id) {
		return this.manager.find(Municipio.class, id);
	}

	public List<Localidade> buscarLocalidade(String s) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Localidade(l.id, l.nome, l.class, l.mascara,m.nome) from Localidade l left join  l.municipio m where lower(l.nome) like lower(:nome)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		return query.getResultList();
	}
	
	public List<UnidadeConservacao> buscarUC(String s) {
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW UnidadeConservacao(uc.id,uc.nome,uc.mascara) from UnidadeConservacao uc where (lower(uc.nome) like lower(:nome)  or lower(uc.mascara) like lower(:nome))");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		return query.getResultList();
	}
	
	public List<Comunidade> buscarComunidades(String s){
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Comunidade(c.id, c.nome, c.municipio) from Comunidade c where lower(c.nome) like lower(:nome)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		return query.getResultList();
	}
	
	public List<Municipio> buscarMunicipios(String s){
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Municipio(m.id, m.nome, m.codigo) from Municipio m where lower(m.nome) like lower(:nome)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		return query.getResultList();
	}
	
	public List<UnidadeConservacao> buscarUnidadesConservacao(String s){
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW UnidadeConservacao(u.id, u.nome, u.mascara, u.codigo) from UnidadeConservacao u where lower(u.nome) like lower(:nome)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		return query.getResultList();
	}
	
	public List<Localidade> buscarLocalidades(String s){
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Localidade(l.id, l.nome, l.codigo) from Localidade l where lower(l.nome) like lower(:nome)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		return query.getResultList();
	}
	
	public List<Localidade> buscarPolos(String s){
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Localidade(l.id, l.nome, l.codigo) from Localidade l where lower(l.nome) like lower(:nome) and tipo = 'polo'");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		return query.getResultList();
	}
	
	public List<Localidade> buscarLocalidadesPorUc(String s, Long idUc){
		StringBuilder jpql = new StringBuilder(
				"SELECT NEW Localidade(l.id, l.nome, l.codigo) from Localidade l where lower(l.nome) like lower(:nome) and tipo = 'com' and l.unidadeConservacao.id = :idUc");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("nome", "%" + s + "%");
		query.setParameter("idUc", idUc);
		return query.getResultList();
	}

	public void remover(Acao acao) {
		this.manager.remove(this.manager.merge(acao));
	}

}
