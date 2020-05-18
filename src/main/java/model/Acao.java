package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
		

@Entity
@Table
public class Acao implements Serializable{
	
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;			
	@Column
	private String codigo;
	@ManyToOne
	private CadeiaProdutiva projArranjosInfra;
	@ManyToOne
	private Projeto projeto;
	@Enumerated(EnumType.STRING)
	private Componente componente; 
	@Enumerated(EnumType.STRING)
	private GrupoDeInvestimento grupoInvestimento;
    @Column
    private String repasse;
	@Column
	private String observacao;
	@Column
	private Boolean desativado;
	
	@ManyToOne
	private ProjetoRubrica projetoRubrica;
	
	public ProjetoRubrica getProjetoRubrica() {
		return projetoRubrica;
	}

	public void setProjetoRubrica(ProjetoRubrica projetoRubrica) {
		this.projetoRubrica = projetoRubrica;
	}

	@OneToMany(mappedBy = "acao", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AcaoFonte> acaoFontes = new ArrayList<AcaoFonte>();
	
	@OneToMany(mappedBy = "acao", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AcaoProduto> itens = new ArrayList<AcaoProduto>();
	
	@ManyToOne
	private Localidade localidade;
	@Enumerated(EnumType.STRING)
	private TipoLocalidade tipoLocalidade;
	
	@ManyToOne
	private CategoriaDespesaClass categoriaDespesaClass;
	
	@Enumerated(EnumType.STRING)
	private CategoriadeDespesa categoriaDespesa;
	
	@ManyToOne
	private Rubrica rubrica;
	
	
	@Column
	private Integer quantidade;
	
	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal valor = BigDecimal.ZERO;
	
	public String getIps() {
		return ips;
	}

	public void setIps(String ips) {
		this.ips = ips;
	}

	@Column
	private String objetivo;
	@Column //Investimento,Produtos,Serviços
	private String ips;
	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	@Column
	private String responsavel;
	
	

	/**
	 * Os campos abaixo  s�o determinados para proje��o de dados
	 * em relat�rios em pdf,excel,html, etc.
	 * */
	@Transient
	private String descricao;
	@Transient
	private BigDecimal orcado;
	@Transient 
	private BigDecimal executado;
	@Transient 
	private BigDecimal saldo;
	@Transient
	private String tipo;
	
	@Transient
	private BigDecimal receita;
	
	
	@Transient
	private String localizacao;
	@Transient
	private String paeic = "";
	@Transient
	private String grupoDeInvestimento = "";
	@Transient
	private String eder = "";
	@Transient
	private String valorPrevisto = "";
	@Transient
	private String nomeProjeto = "";
	@Transient
	private String comunidade = "";
	@Transient
	private String nucleo = "";
	@Transient
	private String fonteDeFinanciamento = "";
	@Transient
	private String uc = "";
	@Transient
	private String descricaoDoInvestimento = "";
	@Transient
	private String municipio = "";
	@Transient
	private String solicitacao = "";
	@Transient
	private String superintendencia = "";
	@Transient
	private String coordenacao = "";
	@Transient
	private String regional = "";
	@Transient
	private String dataExecucao = "";

	
	
	public Acao(Long id, String codigo, 
			CadeiaProdutiva projArranjosInfra,
			Projeto projeto ,Componente componente,
			GrupoDeInvestimento grupoInvestimento, String repasse, String observacao) {
		//super();
		this.id = id;
		this.codigo = codigo;
		this.projArranjosInfra = projArranjosInfra;
		this.projeto = projeto;
		this.componente = componente;
		this.grupoInvestimento = grupoInvestimento;
		this.repasse = repasse;
		this.observacao = observacao;
		//this.orcado = orcado;
	
	}
	
	public Acao(Long id, String codigo, String localizacao, 
			//String paeic, 
			String objetivo, 
			GrupoDeInvestimento grupoInvestimento,
			String descricaoDoInvestimento, String responsavel,
			String eder, String observacao, Integer quantidade, Componente compoente
			){
		
		this.id = id;
		this.codigo = codigo;
		this.localizacao = localizacao;
		//this.paeic = paeic;
		this.objetivo = objetivo;
		this.grupoDeInvestimento = grupoInvestimento != null ?  grupoInvestimento.getNome() : "";
		this.ips = descricaoDoInvestimento;
		this.responsavel = responsavel;
		this.eder = eder;
		this.observacao = observacao;
		this.quantidade = quantidade;
		this.componente = compoente;
//		jpql.append("a.id,");
//		jpql.append("a.codigo,");
//		jpql.append("a.localidade.mascara,");
//		jpql.append("a.projArranjosInfra.nome,");// paeic
//		jpql.append("a.objetivo,");
//		jpql.append("a.grupoInvestimento,");
//		jpql.append("a.ips,"); //descricaoDoInvestimento
//		jpql.append("a.responsavel,");
//		
//		jpql.append("a.repasse,"); //eder
//		jpql.append("a.observacao,");
//		jpql.append("a.quantidade,");
//		jpql.append("a.componente");
//		jpql.append(")");
//		
		
	}
	
	public Acao(Long id, String codigo){
		this.id = id;
		this.codigo = codigo;
	}
	
	public Acao(){}
	
	public Acao(String root){
		this.codigo = root;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Acao other = (Acao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int compareTo(Acao o) {
		return this.getCodigo().compareTo(o.getCodigo());
	}

	public BigDecimal getOrcado() {
		return orcado;
	}

	public void setOrcado(BigDecimal orcado) {
		this.orcado = orcado;
	}

	public BigDecimal getExecutado() {
		return executado;
	}

	public void setExecutado(BigDecimal executado) {
		this.executado = executado;
	}

	public String getTipo() {
		return "acao";
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Componente getComponente() {
		return componente;
	}

	public void setComponente(Componente componente) {
		this.componente = componente;
	}

	public CadeiaProdutiva getProjArranjosInfra() {
		return projArranjosInfra;
	}

	public void setProjArranjosInfra(CadeiaProdutiva projArranjosInfra) {
		this.projArranjosInfra = projArranjosInfra;
	}

	public GrupoDeInvestimento getGrupoInvestimento() {
		return grupoInvestimento;
	}

	public void setGrupoInvestimento(GrupoDeInvestimento grupoInvestimento) {
		this.grupoInvestimento = grupoInvestimento;
	}

	public String getRepasse() {
		return repasse;
	}

	public void setRepasse(String repasse) {
		this.repasse = repasse;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public List<AcaoFonte> getAcaoFontes() {
		return acaoFontes;
	}

	public void setAcaoFontes(List<AcaoFonte> acaoFontes) {
		this.acaoFontes = acaoFontes;
	}

	public Localidade getLocalidade() {
		return localidade;
	}

	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
	}

	public TipoLocalidade getTipoLocalidade() {
		return tipoLocalidade;
	}

	public void setTipoLocalidade(TipoLocalidade tipoLocalidade) {
		this.tipoLocalidade = tipoLocalidade;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public BigDecimal getReceita() {
		return receita;
	}

	public void setReceita(BigDecimal receita) {
		this.receita = receita;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}

	public CategoriadeDespesa getCategoriaDespesa() {
		return categoriaDespesa;
	}

	public void setCategoriaDespesa(CategoriadeDespesa categoriaDespesa) {
		this.categoriaDespesa = categoriaDespesa;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public List<AcaoProduto> getItens() {
		return itens;
	}

	public void setItens(List<AcaoProduto> itens) {
		this.itens = itens;
	}

	public CategoriaDespesaClass getCategoriaDespesaClass() {
		return categoriaDespesaClass;
	}

	public void setCategoriaDespesaClass(CategoriaDespesaClass categoriaDespesaClass) {
		this.categoriaDespesaClass = categoriaDespesaClass;
	}

	public Rubrica getRubrica() {
		return rubrica;
	}

	public void setRubrica(Rubrica rubrica) {
		this.rubrica = rubrica;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	public String getPaeic() {
		return paeic;
	}

	public void setPaeic(String paeic) {
		this.paeic = paeic;
	}

	public String getGrupoDeInvestimento() {
		return grupoDeInvestimento;
	}

	public void setGrupoDeInvestimento(String grupoDeInvestimento) {
		this.grupoDeInvestimento = grupoDeInvestimento;
	}

	public String getEder() {
		return eder;
	}

	public void setEder(String eder) {
		this.eder = eder;
	}

	public String getValorPrevisto() {
		return valorPrevisto;
	}

	public void setValorPrevisto(String valorPrevisto) {
		this.valorPrevisto = valorPrevisto;
	}

	public String getNomeProjeto() {
		return nomeProjeto;
	}

	public void setNomeProjeto(String nomeProjeto) {
		this.nomeProjeto = nomeProjeto;
	}

	public String getComunidade() {
		return comunidade;
	}

	public void setComunidade(String comunidade) {
		this.comunidade = comunidade;
	}

	public String getNucleo() {
		return nucleo;
	}

	public void setNucleo(String nucleo) {
		this.nucleo = nucleo;
	}

	public String getFonteDeFinanciamento() {
		return fonteDeFinanciamento;
	}

	public void setFonteDeFinanciamento(String fonteDeFinanciamento) {
		this.fonteDeFinanciamento = fonteDeFinanciamento;
	}

	public String getUc() {
		return uc;
	}

	public void setUc(String uc) {
		this.uc = uc;
	}

	public String getDescricaoDoInvestimento() {
		return descricaoDoInvestimento;
	}

	public void setDescricaoDoInvestimento(String descricaoDoInvestimento) {
		this.descricaoDoInvestimento = descricaoDoInvestimento;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(String solicitacao) {
		this.solicitacao = solicitacao;
	}

	public String getSuperintendencia() {
		return superintendencia;
	}

	public void setSuperintendencia(String superintendencia) {
		this.superintendencia = superintendencia;
	}

	public String getCoordenacao() {
		return coordenacao;
	}

	public void setCoordenacao(String coordenacao) {
		this.coordenacao = coordenacao;
	}

	public String getRegional() {
		return regional;
	}

	public void setRegional(String regional) {
		this.regional = regional;
	}

	public String getDataExecucao() {
		return dataExecucao;
	}

	public void setDataExecucao(String dataExecucao) {
		this.dataExecucao = dataExecucao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Boolean getDesativado() {
		return desativado;
	}

	public void setDesativado(Boolean desativado) {
		this.desativado = desativado;
	}


}
