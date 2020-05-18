package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import util.Util;


@Entity
@Table(name = "projeto_rubrica")
public class ProjetoRubrica implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private Projeto projeto;
	@ManyToOne
	private RubricaOrcamento rubricaOrcamento;
	@ManyToOne
	private ComponenteClass componente;
	@ManyToOne
	private SubComponente subComponente;
	@ManyToOne
	private Rubrica rubrica;
	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal valor = BigDecimal.ZERO;
	
	@Column
	private Double porcentagem;
	
	@Transient
	private BigDecimal valorEmpenhado;
	
	@Transient
	private BigDecimal valorProjeto;
	
	@Transient
	private BigDecimal valorSaldo;
	
	@Transient
	private double porcentagemEmpenhadoRubrica;
	
	@Transient
	private double porcentagemEmpenhadoProjeto;

	@Transient
	private String nomeProjeto;

	@Transient
	private String nomeFonte;
	
	@Transient 
	private String nomeComponente;
	
	@Transient 
	private String nomeSubComponente;

	@Transient 
	private String nomeCategoria;
	
	@Transient 
	private Long idProjeto;
	
	@Transient 
	private String codigo;
	
	@Transient 
	private String codigoProjeto;
	
	
	@Transient 
	private String codigoPlano;
	
	
	@Transient 
	private String nomePlano;
	
	@Transient 
	private String nomeGestao;
	
	@Transient 
	private String localProjeto;
	
	@Transient 
	private String nomeCadeia;
	
	@Transient 
	private String nomeDoacao;
	
	@Transient 
	private String nomeRequisitoDoador;
	
		
	
	
	public ProjetoRubrica(){}
	
	public ProjetoRubrica(Long id){
		this.id = id;
	}
		
	public ProjetoRubrica(Long id, RubricaOrcamento rubricaOrcamento){
		this.id = id;
		this.rubricaOrcamento = rubricaOrcamento; 
	}
	
	
	
	
	public ProjetoRubrica(Long id, BigDecimal valor, String nomeProjeto, String nomeFonte, String nomeComponente,
			String nomeSubComponente, String nomeCategoria, String codigo, String codigoProjeto, String codigoPlano,
			String nomePlano, String nomeGestao, String localProjeto, String nomeCadeia, String nomeDoacao,
			String nomeRequisitoDoador) {
		super();
		this.id = id;
		this.valor = valor;
		this.nomeProjeto = nomeProjeto;
		this.nomeFonte = nomeFonte;
		this.nomeComponente = nomeComponente;
		this.nomeSubComponente = nomeSubComponente;
		this.nomeCategoria = nomeCategoria;
		this.codigo = codigo;
		this.codigoProjeto = codigoProjeto;
		this.codigoPlano = codigoPlano;
		this.nomePlano = nomePlano;
		this.nomeGestao = nomeGestao;
		this.localProjeto = localProjeto;
		this.nomeCadeia = nomeCadeia;
		this.nomeDoacao = nomeDoacao;
		this.nomeRequisitoDoador = nomeRequisitoDoador;
	}

	public ProjetoRubrica(Long id, String categoria, BigDecimal valor, String componente){
		this.id = id;
		this.nomeCategoria = categoria; 
		this.valor = valor;
		this.nomeComponente = componente;
	}
	
	public ProjetoRubrica(Long id, RubricaOrcamento rubricaOrcamento, String nomeProjeto, String nomeFonte, Long idProjeto, String codigo){
		this.id = id;
		this.rubricaOrcamento = rubricaOrcamento; 
		this.nomeProjeto = nomeProjeto;
		this.nomeFonte = nomeFonte;
		this.idProjeto = idProjeto;
		this.codigo = codigo;
	}
	
	public ProjetoRubrica(Long id,String codigo,String nomeFonte,String nomeProjeto, String nomeComponente, String nomeSub,String linha,BigDecimal valor){
		
		this.id = id;
		this.codigo = codigo;
		this.nomeFonte = nomeFonte;
		this.nomeProjeto = nomeProjeto;
		this.nomeComponente = nomeComponente;
		this.nomeSubComponente = nomeSub;
		this.nomeCategoria = linha;
		this.valorSaldo = valor;
		
		
	}
	
	public ProjetoRubrica(Long id,String nomeProjeto,BigDecimal valor,BigDecimal valorEmpenhado,BigDecimal valorProjeto,Double porcentagemEmpenhadoRubrica,Double porcentagemEmpenhadoProjeto) {
		
	}
	public ProjetoRubrica(Object[] object) {
		this.nomeProjeto = Util.nullEmptyObjectUtil(object[0]);
		this.valor = new BigDecimal(Util.nullEmptyObjectUtil(object[2]));
		this.valorProjeto = new BigDecimal(Util.nullEmptyObjectUtil(object[1]));
		this.valorEmpenhado = new BigDecimal(Util.nullEmptyObjectUtil(object[3]));
		this.porcentagemEmpenhadoRubrica = Double.parseDouble(Util.nullEmptyObjectUtil(object[4]));
		this.porcentagemEmpenhadoProjeto = Double.parseDouble(Util.nullEmptyObjectUtil(object[5]));
	}
	
	public ProjetoRubrica(Long id,String nomeComponente, String nomeSubComponente, String nomeCategoria, String nomeProjeto, String nomeFonte){
		this.id = id;
		this.nomeComponente = nomeComponente;
		this.nomeSubComponente = nomeSubComponente;
		this.nomeCategoria = nomeCategoria;
		this.nomeProjeto = nomeProjeto;
		this.nomeFonte = nomeFonte;
	}

	public Long getId() {
		return id;
	}
	
	public ProjetoRubrica(BigDecimal valor){
		this.valor = valor;
	}

	

	public void setId(Long id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
		ProjetoRubrica other = (ProjetoRubrica) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Projeto getProjeto() {
		return projeto;
	}
	

	public BigDecimal getValorSaldo() {
		return valorSaldo;
	}

	public void setValorSaldo(BigDecimal valorSaldo) {
		this.valorSaldo = valorSaldo;
	}

	public BigDecimal getValorProjeto() {
		return valorProjeto;
	}

	public void setValorProjeto(BigDecimal valorProjeto) {
		this.valorProjeto = valorProjeto;
	}


	public double getPorcentagemEmpenhadoRubrica() {
		return porcentagemEmpenhadoRubrica;
	}

	public void setPorcentagemEmpenhadoRubrica(double porcentagemEmpenhadoRubrica) {
		this.porcentagemEmpenhadoRubrica = porcentagemEmpenhadoRubrica;
	}

	public double getPorcentagemEmpenhadoProjeto() {
		return porcentagemEmpenhadoProjeto;
	}

	public void setPorcentagemEmpenhadoProjeto(double porcentagemEmpenhadoProjeto) {
		this.porcentagemEmpenhadoProjeto = porcentagemEmpenhadoProjeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public BigDecimal getValorEmpenhado() {
		return valorEmpenhado;
	}

	public void setValorEmpenhado(BigDecimal valorEmpenhado) {
		this.valorEmpenhado = valorEmpenhado;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public ComponenteClass getComponente() {
		return componente;
	}

	public void setComponente(ComponenteClass componente) {
		this.componente = componente;
	}

	public SubComponente getSubComponente() {
		return subComponente;
	}

	public void setSubComponente(SubComponente subComponente) {
		this.subComponente = subComponente;
	}

	public RubricaOrcamento getRubricaOrcamento() {
		return rubricaOrcamento;
	}

	public void setRubricaOrcamento(RubricaOrcamento rubricaOrcamento) {
		this.rubricaOrcamento = rubricaOrcamento;
	}

	public Rubrica getRubrica() {
		return rubrica;
	}

	public void setRubrica(Rubrica rubrica) {
		this.rubrica = rubrica;
	}

	public Double getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(Double porcentagem) {
		this.porcentagem = porcentagem;
	}

	public String getNomeProjeto() {
		return nomeProjeto;
	}

	public void setNomeProjeto(String nomeProjeto) {
		this.nomeProjeto = nomeProjeto;
	}

	public String getNomeFonte() {
		return nomeFonte;
	}

	public void setNomeFonte(String nomeFonte) {
		this.nomeFonte = nomeFonte;
	}

	public Long getIdProjeto() {
		return idProjeto;
	}

	public void setIdProjeto(Long idProjeto) {
		this.idProjeto = idProjeto;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNomeComponente() {
		return nomeComponente;
	}

	public void setNomeComponente(String nomeComponente) {
		this.nomeComponente = nomeComponente;
	}

	public String getNomeSubComponente() {
		return nomeSubComponente;
	}

	public void setNomeSubComponente(String nomeSubComponente) {
		this.nomeSubComponente = nomeSubComponente;
	}

	public String getNomeCategoria() {
		return nomeCategoria;
	}

	public void setNomeCategoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}

	public String getCodigoProjeto() {
		return codigoProjeto;
	}

	public void setCodigoProjeto(String codigoProjeto) {
		this.codigoProjeto = codigoProjeto;
	}

	public String getCodigoPlano() {
		return codigoPlano;
	}

	public void setCodigoPlano(String codigoPlano) {
		this.codigoPlano = codigoPlano;
	}

	public String getNomePlano() {
		return nomePlano;
	}

	public void setNomePlano(String nomePlano) {
		this.nomePlano = nomePlano;
	}

	public String getNomeGestao() {
		return nomeGestao;
	}

	public void setNomeGestao(String nomeGestao) {
		this.nomeGestao = nomeGestao;
	}

	public String getLocalProjeto() {
		return localProjeto;
	}

	public void setLocalProjeto(String localProjeto) {
		this.localProjeto = localProjeto;
	}

	public String getNomeCadeia() {
		return nomeCadeia;
	}

	public void setNomeCadeia(String nomeCadeia) {
		this.nomeCadeia = nomeCadeia;
	}

	public String getNomeDoacao() {
		return nomeDoacao;
	}

	public void setNomeDoacao(String nomeDoacao) {
		this.nomeDoacao = nomeDoacao;
	}

	public String getNomeRequisitoDoador() {
		return nomeRequisitoDoador;
	}

	public void setNomeRequisitoDoador(String nomeRequisitoDoador) {
		this.nomeRequisitoDoador = nomeRequisitoDoador;
	}




}
