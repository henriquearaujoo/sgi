package repositorio;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.CadeiaProdutivaFamiliar;
import model.Comunidade;
import model.CriacaoAnimal;
import model.Familiar;
import model.IdentificacaoFamiliar;
import model.Localidade;
import model.ProdutoAgricultura;
import model.ProdutoFlorestalMadeireiro;
import model.ProdutoFlorestalNaoMadeireiro;
import model.ProdutoPescado;
import model.SintomaComum;
import model.UnidadeConservacao;

public class FamiliarRepository {
	
	private EntityManager manager;
	
	public FamiliarRepository(){}
	
	@Inject
	public FamiliarRepository(EntityManager manager){
		this.manager = manager;
	}
	
	public List<Familiar> getFamiliarPorNumeroCadastro(Integer numero){
		String jpql = "select f from Familiar f where f.numeroCadastro = :numero";
		Query query  = manager.createQuery(jpql);
		query.setParameter("numero", numero);
		return query.getResultList();
	}
	
	public List<Familiar> getFamiliar(){
		String jpql = "SELECT NEW Familiar(f.id, f.protocolo, f.comunidade.nome) FROM Familiar f order by f.protocolo";
		Query query = this.manager.createQuery(jpql);
		return query.getResultList();
	}
	
	public List<IdentificacaoFamiliar> getMembros(Long idFamiliar){
		String jpql = "SELECT NEW IdentificacaoFamiliar(i.id, i.nome, i.tipoIdentificacao) FROM IdentificacaoFamiliar i where familiar.id = :idFamiliar order by i.nome";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("idFamiliar", idFamiliar);
		return query.getResultList();
	}
	
	public List<Familiar> getFamiliarComFiltro(String protocolo, UnidadeConservacao uc, Comunidade comunidade){
		String jpql = "SELECT NEW Familiar(f.id, f.protocolo, f.comunidade.nome) ";
		jpql += " FROM Familiar f ";
		jpql += " WHERE 1=1 ";
		
		if (protocolo != null && !protocolo.isEmpty())
			jpql += " AND f.protocolo = '" + protocolo + "'";
		
		if (uc != null)
			jpql += " AND f.unidadeConservacao.id = " + uc.getId();
		
		if (comunidade != null)
			jpql += " AND f.comunidade.id = " + comunidade.getId();
		
		jpql += " order by f.protocolo ";
		
		Query query = this.manager.createQuery(jpql);
		return query.getResultList();
	}
		
	public Familiar getFamiliarById(Long id){
		return this.manager.find(Familiar.class, id);
	}
	
	public SintomaComum findByIdSintoma(Long id) {
		return this.manager.find(SintomaComum.class, id);
	}
	
	public CadeiaProdutivaFamiliar findByIdCadeia(Long id) {
		return this.manager.find(CadeiaProdutivaFamiliar.class, id);
	}
	
	public ProdutoAgricultura findByIdProdutoAgricultura(Long id) {
		return this.manager.find(ProdutoAgricultura.class, id);
	}
	
	public ProdutoPescado findByIdProdutoPescado(Long id) {
		return this.manager.find(ProdutoPescado.class, id);
	}
	
	public CriacaoAnimal findByIdCriacaoAnimal(Long id) {
		return this.manager.find(CriacaoAnimal.class, id);
	}
	
	public ProdutoFlorestalMadeireiro findByIdProdutoMadeireiro(Long id) {
		return this.manager.find(ProdutoFlorestalMadeireiro.class, id);
	}
	
	public ProdutoFlorestalNaoMadeireiro findByIdProdutoNaoMadeireiro(Long id) {
		return this.manager.find(ProdutoFlorestalNaoMadeireiro.class, id);
	}
	
	public List<SintomaComum> buscarSintomas(String s){
		StringBuilder jpql = new StringBuilder(
				"from SintomaComum s where lower(s.descricao) like lower(:descricao)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("descricao", "%" + s + "%");
		return query.getResultList();
	}
	
	public List<CadeiaProdutivaFamiliar> buscarCadeias(String s){
		StringBuilder jpql = new StringBuilder(
				"from CadeiaProdutivaFamiliar c where lower(c.descricao) like lower(:descricao)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("descricao", "%" + s + "%");
		return query.getResultList();
	}
	
	public List<ProdutoAgricultura> buscarProdutosAgricultura(String s){
		StringBuilder jpql = new StringBuilder(
				"from ProdutoAgricultura p where lower(p.descricao) like lower(:descricao)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("descricao", "%" + s + "%");
		return query.getResultList();
	}
	
	public List<ProdutoPescado> buscarProdutosPescado(String s){
		StringBuilder jpql = new StringBuilder(
				"from ProdutoPescado p where lower(p.descricao) like lower(:descricao)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("descricao", "%" + s + "%");
		return query.getResultList();
	}
	
	public List<CriacaoAnimal> buscarCricoesAnimal(String s){
		StringBuilder jpql = new StringBuilder(
				"from CriacaoAnimal c where lower(c.descricao) like lower(:descricao)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("descricao", "%" + s + "%");
		return query.getResultList();
	}
	
	public List<ProdutoFlorestalMadeireiro> buscarProdutosMadeireiros(String s){
		StringBuilder jpql = new StringBuilder(
				"from ProdutoFlorestalMadeireiro p where lower(p.descricao) like lower(:descricao)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("descricao", "%" + s + "%");
		return query.getResultList();
	}
	
	public List<ProdutoFlorestalNaoMadeireiro> buscarProdutosNaoMadeireiros(String s){
		StringBuilder jpql = new StringBuilder(
				"from ProdutoFlorestalNaoMadeireiro p where lower(p.descricao) like lower(:descricao)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("descricao", "%" + s + "%");
		return query.getResultList();
	}
	
	public Boolean verificaCPFCadastrado(String cpf, Long id){
		String jpql = "from IdentificacaoFamiliar i where i.cpf = '" + cpf + "'";
		
		if(id != null)
			jpql += " and i.id <> :id";
		
		Query query = manager.createQuery(jpql.toString());
		
		if(id != null)
			query.setParameter("id", id);
		
		int qt = query.getResultList().size(); 
		
		return qt > 0 ? true : false;
	}
	
	public Integer getSequencialFamiliasUC(Long idUc){
		String sql = "select count(*) from familiar where unidadeconservacao_id = " + idUc;
		Query query = manager.createNativeQuery(sql);
		Object object = query.getSingleResult();
		return object != null ? Integer.parseInt(object.toString()) : 0;
	}
	
	public Boolean existeProtocolo(String protocolo){
		String jpql = "from Familiar i where i.protocolo = '" + protocolo + "'";
		
		Query query = manager.createQuery(jpql.toString());
		
		int qt = query.getResultList().size(); 
		
		return qt > 0 ? true : false;
	}
	
	public void salvar(Familiar familiar){
		this.manager.merge(familiar);
	}

	public void update(Familiar familiar) {
		manager.merge(familiar);		
	}

}
