package repositorio;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.AtividadeRenda;
import model.CadeiaProdutivaFamiliar;
import model.Comunitario;
import model.CriacaoAnimal;
import model.Familiar;
import model.FonteAguaConsumo;
import model.FonteEnergia;
import model.IdentificacaoFamiliar;
import model.InfraestruturaComunitaria;
import model.MaterialInformativo;
import model.MeioComunicacao;
import model.ProdutoAgricultura;
import model.ProdutoFlorestalMadeireiro;
import model.ProdutoFlorestalNaoMadeireiro;
import model.ProdutoPescado;
import model.SintomaComum;

public class ComunitarioRepository {
	
	private EntityManager manager;
	
	public ComunitarioRepository(){}
	
	@Inject
	public ComunitarioRepository(EntityManager manager){
		this.manager = manager;
	}
	
	public List<Familiar> getFamiliarPorNumeroCadastro(Integer numero){
		String jpql = "select f from Familiar f where f.numeroCadastro = :numero";
		Query query  = manager.createQuery(jpql);
		query.setParameter("numero", numero);
		return query.getResultList();
	}
	
	public List<Comunitario> getComunitario(){
		String jpql = "SELECT NEW Comunitario(c.id, c.denominacao, c.nomeCompleto, c.nome) FROM Comunitario c order by c.id";
		Query query = this.manager.createQuery(jpql);
		return query.getResultList();
	}
	
	public List<IdentificacaoFamiliar> getMembros(Long idFamiliar){
		String jpql = "SELECT NEW IdentificacaoFamiliar(i.id, i.nome, i.tipoIdentificacao) FROM IdentificacaoFamiliar i where familiar.id = :idFamiliar order by i.nome";
		Query query = this.manager.createQuery(jpql);
		query.setParameter("idFamiliar", idFamiliar);
		return query.getResultList();
	}
		
	public Comunitario getComunitarioById(Long id){
		return this.manager.find(Comunitario.class, id);
	}
	
	public SintomaComum findByIdSintoma(Long id) {
		return this.manager.find(SintomaComum.class, id);
	}
	
	public FonteAguaConsumo findByIdFonteAgua(Long id) {
		return this.manager.find(FonteAguaConsumo.class, id);
	}
	
	public MeioComunicacao findByIdMeioComunicacao(Long id) {
		return this.manager.find(MeioComunicacao.class, id);
	}
	
	public MaterialInformativo findByIdMaterialInformativo(Long id) {
		return this.manager.find(MaterialInformativo.class, id);
	}
	
	public FonteEnergia findByIdFormaEnergia(Long id) {
		return this.manager.find(FonteEnergia.class, id);
	}
	
	public AtividadeRenda findByIdAtividadeRenda(Long id) {
		return this.manager.find(AtividadeRenda.class, id);
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
	
	public InfraestruturaComunitaria findByIdInfraestrutura(Long id) {
		return this.manager.find(InfraestruturaComunitaria.class, id);
	}
	
	public List<SintomaComum> buscarSintomas(String s){
		StringBuilder jpql = new StringBuilder(
				"from SintomaComum s where lower(s.descricao) like lower(:descricao)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("descricao", "%" + s + "%");
		return query.getResultList();
	}
	
	public List<FonteAguaConsumo> buscarFontesAgua(String s){
		StringBuilder jpql = new StringBuilder(
				"from FonteAguaConsumo f where lower(f.descricao) like lower(:descricao)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("descricao", "%" + s + "%");
		return query.getResultList();
	}
	
	public List<FonteEnergia> buscarFontesEnergia(String s){
		StringBuilder jpql = new StringBuilder(
				"from FonteEnergia f where lower(f.descricao) like lower(:descricao)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("descricao", "%" + s + "%");
		return query.getResultList();
	}
	
	public List<MeioComunicacao> buscarMeiosComunicacao(String s){
		StringBuilder jpql = new StringBuilder(
				"from MeioComunicacao f where lower(f.descricao) like lower(:descricao)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("descricao", "%" + s + "%");
		return query.getResultList();
	}
	
	public List<MaterialInformativo> buscarMateriaisInformativos(String s){
		StringBuilder jpql = new StringBuilder(
				"from MaterialInformativo f where lower(f.descricao) like lower(:descricao)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("descricao", "%" + s + "%");
		return query.getResultList();
	}
	
	public List<AtividadeRenda> buscarAtividadesDeRenda(String s){
		StringBuilder jpql = new StringBuilder(
				"from AtividadeRenda f where lower(f.descricao) like lower(:descricao)");
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
	
	public List<InfraestruturaComunitaria> buscarInfraestruturas(String s){
		StringBuilder jpql = new StringBuilder(
				"from InfraestruturaComunitaria i where lower(i.descricao) like lower(:descricao)");
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("descricao", "%" + s + "%");
		return query.getResultList();
	}
	
	public Boolean verificaCPFCadastrado(String cpf, Long id){
		String jpql = "from IdentificacaoFamiliar i where i.cpf = :cpf";
		
		if(id != null)
			jpql += " and i.id <> :id";
		
		Query query = manager.createQuery(jpql.toString());
		query.setParameter("cpf", "'" + cpf + "'");
		
		if(id != null)
			query.setParameter("id", id);
		
		return query.getResultList().size() > 0 ? true : false;
	}
	
	public void salvar(Comunitario comunitario){
		this.manager.merge(comunitario);
	}

	public void update(Comunitario comunitario) {
		manager.merge(comunitario);		
	}

}
