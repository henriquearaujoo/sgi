package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "lancamento_acao")
public class LancamentoAcao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal valor;
	@Column
	@Temporal(TemporalType.DATE)
	private Date data;
	@Transient
	private TipoLancamento tipo;
	@ManyToOne
	private Lancamento lancamento;
	@Transient
	private FontePagadora fontePagadora;
	@Transient
	private Acao acao;
	@ManyToOne
	private Fornecedor fornecedor;
	@ManyToOne
	private Compra compra;
	
	@Transient
	private AtividadeProjeto atividade;

//	@ManyToOne
//	private Orcamento orcamento;

	@Enumerated(EnumType.ORDINAL) // Status se entra ou não no contas a pagar.
	private StatusPagamento status;

	@Transient
	private String descricao;

//	@ManyToOne
//	private Projeto projeto;

	//@ManyToOne
	@Transient
	private CategoriaProjeto categoriaProjeto;
	
	@ManyToOne
	private ProjetoRubrica projetoRubrica;

	@ManyToOne
	private RubricaOrcamento rubricaOrcamento;

	@Column
	private String tipoLancamento = "";

	//@Enumerated(EnumType.STRING)
	@Transient
	private DespesaReceita despesaReceita;

	@OneToMany(mappedBy = "lancamentoAcao", cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.LAZY)
	private List<PagamentoLancamento> pagamentosLancados = new ArrayList<PagamentoLancamento>();

	@Transient
	private Long IdCategoriaProjeto;

	@Transient
	private String codigoAcao;
	@Transient
	private String fonte;
	
	@Transient
	private String nomeProjeto;
	@Transient
	private String nomeCategoria;

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public LancamentoAcao(Long id, BigDecimal valor, String codigoAcao, String fonte) {
		this.id = id;
		this.valor = valor;
		this.codigoAcao = codigoAcao;
		this.fonte = fonte;
	}
	
	
	public LancamentoAcao(BigDecimal valor, ProjetoRubrica projetoRubrica) {
		this.valor = valor;
		this.projetoRubrica = projetoRubrica;
		this.despesaReceita = DespesaReceita.DESPESA;
	}
	
	public LancamentoAcao(BigDecimal valor, RubricaOrcamento rubricaOrcamento) {
		this.valor = valor;
		this.rubricaOrcamento = rubricaOrcamento;
		this.despesaReceita = DespesaReceita.DESPESA;
	}

	public LancamentoAcao() {
	}

	public LancamentoAcao(LancamentoAcao lancamento) {
		this.lancamento = lancamento.getLancamento();
		this.valor = lancamento.getValor();
		this.status = lancamento.getStatus();
		this.projetoRubrica = lancamento.getProjetoRubrica();
		this.despesaReceita = lancamento.getDespesaReceita();
		this.rubricaOrcamento = lancamento.getRubricaOrcamento();
		this.status = StatusPagamento.VALIDADO;
	}
	
	public LancamentoAcao(LancamentoAcao lancamento, String tipo) {
		this.lancamento = lancamento.getLancamento();
		this.valor = lancamento.getValor();
		this.status = lancamento.getStatus();
		this.projetoRubrica = lancamento.getProjetoRubrica();
		//this.despesaReceita = lancamento.getDespesaReceita();
		this.rubricaOrcamento = lancamento.getRubricaOrcamento();
		this.status = StatusPagamento.VALIDADO;
		
		if (tipo.equalsIgnoreCase("devdocted")) {
			this.despesaReceita = DespesaReceita.RECEITA;
		} else {
			this.despesaReceita = DespesaReceita.DESPESA;
		}
	}
	
	
	
	
	
	public LancamentoAcao(String nomeFonte, String nomeProjeto, String nomeCategoria, BigDecimal valor) {
		this.fonte = nomeFonte;
		this.nomeProjeto = nomeProjeto;
		this.nomeCategoria = nomeCategoria;
		this.valor = valor;
	}
	
	public LancamentoAcao(PagamentoLancamento pagamento, String tipo) {
		this.lancamento =  pagamento.getLancamentoAcao().getLancamento();
		this.valor =   pagamento.getValor(); //lancamento.getValor();
		this.projetoRubrica = pagamento.getLancamentoAcao().getProjetoRubrica() != null ? pagamento.getLancamentoAcao().getProjetoRubrica() : null;
		this.rubricaOrcamento = pagamento.getLancamentoAcao().getRubricaOrcamento() != null ? pagamento.getLancamentoAcao().getRubricaOrcamento() : null;
		this.status = StatusPagamento.VALIDADO;
		this.despesaReceita = DespesaReceita.RECEITA;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public TipoLancamento getTipo() {
		return tipo;
	}

	public void setTipo(TipoLancamento tipo) {
		this.tipo = tipo;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public FontePagadora getFontePagadora() {
		return fontePagadora;
	}

	public void setFontePagadora(FontePagadora fontePagadora) {
		this.fontePagadora = fontePagadora;
	}

	public Acao getAcao() {
		return acao;
	}

	public void setAcao(Acao acao) {
		this.acao = acao;
	}

	public String getCodigoAcao() {
		return codigoAcao;
	}

	public void setCodigoAcao(String codigoAcao) {
		this.codigoAcao = codigoAcao;
	}

	public String getFonte() {
		return fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public List<PagamentoLancamento> getPagamentosLancados() {
		return pagamentosLancados;
	}

	public void setPagamentosLancados(List<PagamentoLancamento> pagamentosLancados) {
		this.pagamentosLancados = pagamentosLancados;
	}

	public DespesaReceita getDespesaReceita() {
		return despesaReceita;
	}

	public void setDespesaReceita(DespesaReceita despesaReceita) {
		this.despesaReceita = despesaReceita;
	}

	public String getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(String tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	
	//TODO: PROJETO NORMALIZADO
//	public Projeto getProjeto() {
//		return projeto;
//	}
//
//	public void setProjeto(Projeto projeto) {
//		this.projeto = projeto;
//	}

	public ProjetoRubrica getProjetoRubrica() {
		return projetoRubrica;
	}

	public void setProjetoRubrica(ProjetoRubrica projetoRubrica) {
		this.projetoRubrica = projetoRubrica;
	}

	
	//TODO: ORCAMENTO NORMALIZADO
//	public Orcamento getOrcamento() {
//		return orcamento;
//	}
//
//	public void setOrcamento(Orcamento orcamento) {
//		this.orcamento = orcamento;
//	}

	public StatusPagamento getStatus() {
		return status;
	}

	public void setStatus(StatusPagamento status) {
		this.status = status;
	}

	public RubricaOrcamento getRubricaOrcamento() {
		return rubricaOrcamento;
	}

	public void setRubricaOrcamento(RubricaOrcamento rubricaOrcamento) {
		this.rubricaOrcamento = rubricaOrcamento;
	}

	public Long getIdCategoriaProjeto() {
		return IdCategoriaProjeto;
	}

	public void setIdCategoriaProjeto(Long idCategoriaProjeto) {
		IdCategoriaProjeto = idCategoriaProjeto;
	}

	public CategoriaProjeto getCategoriaProjeto() {
		return categoriaProjeto;
	}

	public void setCategoriaProjeto(CategoriaProjeto categoriaProjeto) {
		this.categoriaProjeto = categoriaProjeto;
	}

	public AtividadeProjeto getAtividade() {
		return atividade;
	}

	public void setAtividade(AtividadeProjeto atividade) {
		this.atividade = atividade;
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
		LancamentoAcao other = (LancamentoAcao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public String getNomeProjeto() {
		return nomeProjeto;
	}

	public void setNomeProjeto(String nomeProjeto) {
		this.nomeProjeto = nomeProjeto;
	}

	public String getNomeCategoria() {
		return nomeCategoria;
	}

	public void setNomeCategoria(String nomeCategoria) {
		this.nomeCategoria = nomeCategoria;
	}

}
