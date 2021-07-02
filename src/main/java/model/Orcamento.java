package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;


@Entity
@Table
public class Orcamento implements Serializable{

	/**
	 * 
	 * Esse orçamento é uma doação, foi dado esse nome por conta do tipo  de procedimento que o financeiro 
	 * executa
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String contrato;
	
	@Column(precision = 11, scale = 2, nullable = false)
	private BigDecimal valor;
	
	@Column
	private String titulo;
	
	@ManyToOne
	private FontePagadora fonte;
	
	@ManyToOne
	private Colaborador colaborador;
	
	@ManyToOne
	private User usuarioCadastro;
	
	@ManyToOne
	private User usuarioEdicao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltimaEdição;
	
	@Temporal(TemporalType.DATE)
	private Date dataInicio;

	@Temporal(TemporalType.DATE)
	private Date dataFinal;
		
	@ManyToOne
	private ContaBancaria contaBancaria;

	@Enumerated(EnumType.STRING)
	private TipoProjetoOrcamento tipoProjetoOrcamento;

	@Column
	private Boolean permiteEmprestimo;

	@Column
	private Boolean pagaTarifaBancaria;

	@Column
	private Boolean permiteRemanejamentoCategoria;

	@ManyToOne
	private Colaborador responsavelTecnico;

	@Column
	private Boolean permiteRemanejoRecurso;

	@ManyToOne
	private ProjetoRubrica projetoRubrica;

	@Enumerated(EnumType.STRING)
	private TipoProjetoDespesa tipoProjetoDespesa;

	@Temporal(TemporalType.DATE)
	private Date dataPrestacao;

	@Enumerated(EnumType.STRING)
	private TipoParcelamento tipoParcelamento;

	@Column
	private Integer quantidadeParcelas;

	@Column
	private Boolean preCadastro;

	@Column
	private String tipoDoacao;
	
	@Column(precision = 11, scale = 2)
	private BigDecimal valorOverhead;
	
	@Column(precision = 11, scale = 2)
	private BigDecimal valorOverheadAPagar;
	
	@Column(precision = 11, scale = 2)
	private BigDecimal valorOverheadIndireto;
	
	@Column(precision = 11, scale = 2)
	private BigDecimal valorRendimento;

	@Column(precision = 11, scale = 2)
	private BigDecimal valorCustoPessoal;
		
	
	@Transient
	private BigDecimal valorOverheadExec;
	
	@Transient
	private BigDecimal valorOverheadIndiretoExec;
	
	@Transient
	private BigDecimal valorRendimentoExec;
	
	@Transient
	private BigDecimal custoPessoalExec;
	
	@Transient
	private BigDecimal saldoReal;
	

	@OneToMany(mappedBy = "orcamento", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PrestacaoOrcamento> prestacoes = new ArrayList<>();

	@OneToMany(mappedBy = "orcamento", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<HistoricoAditivoOrcamento> historicoAditivos = new ArrayList<>();

	public Orcamento(){}


	public Orcamento(Long id,String titulo,String contrato){
		this.id = id;
		this.titulo = titulo;
		this.contrato = contrato;
	}

	public Orcamento(Long id, String titulo) {
		this.id = id;
		this.titulo = titulo;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getContrato() {
		return contrato;
	}


	public void setContrato(String contrato) {
		this.contrato = contrato;
	}


	public BigDecimal getValor() {
		return valor != null ? valor : BigDecimal.ZERO;
	}


	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}


	public String getTitulo() {
		return titulo;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}


	public FontePagadora getFonte() {
		return fonte;
	}


	public void setFonte(FontePagadora fonte) {
		this.fonte = fonte;
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
		Orcamento other = (Orcamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public Colaborador getColaborador() {
		return colaborador;
	}


	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}


	public User getUsuarioCadastro() {
		return usuarioCadastro;
	}


	public void setUsuarioCadastro(User usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}


	public User getUsuarioEdicao() {
		return usuarioEdicao;
	}


	public void setUsuarioEdicao(User usuarioEdicao) {
		this.usuarioEdicao = usuarioEdicao;
	}


	public Date getDataCriacao() {
		return dataCriacao;
	}


	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}


	public Date getDataUltimaEdição() {
		return dataUltimaEdição;
	}


	public void setDataUltimaEdição(Date dataUltimaEdição) {
		this.dataUltimaEdição = dataUltimaEdição;
	}


	public Date getDataInicio() {
		return dataInicio;
	}


	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}


	public Date getDataFinal() {
		return dataFinal;
	}


	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}


	public ContaBancaria getContaBancaria() {
		return contaBancaria;
	}


	public void setContaBancaria(ContaBancaria contaBancaria) {
		this.contaBancaria = contaBancaria;
	}

	public TipoProjetoOrcamento getTipoProjetoOrcamento() {
		return tipoProjetoOrcamento;
	}

	public void setTipoProjetoOrcamento(TipoProjetoOrcamento tipoProjetoOrcamento) {
		this.tipoProjetoOrcamento = tipoProjetoOrcamento;
	}

	public Boolean getPermiteEmprestimo() {
		return permiteEmprestimo;
	}

	public void setPermiteEmprestimo(Boolean permiteEmprestimo) {
		this.permiteEmprestimo = permiteEmprestimo;
	}

	public Boolean getPagaTarifaBancaria() {
		return pagaTarifaBancaria;
	}

	public void setPagaTarifaBancaria(Boolean pagaTarifaBancaria) {
		this.pagaTarifaBancaria = pagaTarifaBancaria;
	}

	public Boolean getPermiteRemanejamentoCategoria() {
		return permiteRemanejamentoCategoria;
	}

	public void setPermiteRemanejamentoCategoria(Boolean permiteRemanejamentoCategoria) {
		this.permiteRemanejamentoCategoria = permiteRemanejamentoCategoria;
	}

	public Colaborador getResponsavelTecnico() {
		return responsavelTecnico;
	}

	public void setResponsavelTecnico(Colaborador responsavelTecnico) {
		this.responsavelTecnico = responsavelTecnico;
	}

	public Boolean getPermiteRemanejoRecurso() {
		return permiteRemanejoRecurso;
	}

	public void setPermiteRemanejoRecurso(Boolean permiteRemanejoRecurso) {
		this.permiteRemanejoRecurso = permiteRemanejoRecurso;
	}

	public TipoProjetoDespesa getTipoProjetoDespesa() {
		return tipoProjetoDespesa;
	}

	public void setTipoProjetoDespesa(TipoProjetoDespesa tipoProjetoDespesa) {
		this.tipoProjetoDespesa = tipoProjetoDespesa;
	}

	public Date getDataPrestacao() {
		return dataPrestacao;
	}

	public void setDataPrestacao(Date dataPrestacao) {
		this.dataPrestacao = dataPrestacao;
	}

	public TipoParcelamento getTipoParcelamento() {
		return tipoParcelamento;
	}

	public void setTipoParcelamento(TipoParcelamento tipoParcelamento) {
		this.tipoParcelamento = tipoParcelamento;
	}

	public Integer getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(Integer quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public List<PrestacaoOrcamento> getPrestacoes() {
		return prestacoes;
	}

	public void setPrestacoes(List<PrestacaoOrcamento> prestacoes) {
		this.prestacoes = prestacoes;
	}

	public ProjetoRubrica getProjetoRubrica() {
		return projetoRubrica;
	}

	public void setProjetoRubrica(ProjetoRubrica projetoRubrica) {
		this.projetoRubrica = projetoRubrica;
	}

	public List<HistoricoAditivoOrcamento> getHistoricoAditivos() {
		return historicoAditivos;
	}

	public void setHistoricoAditivos(List<HistoricoAditivoOrcamento> historicoAditivos) {
		this.historicoAditivos = historicoAditivos;
	}

	public Boolean getPreCadastro() {
		return preCadastro;
	}

	public void setPreCadastro(Boolean preCadastro) {
		this.preCadastro = preCadastro;
	}

	public String getTipoDoacao() {
		return tipoDoacao;
	}

	public void setTipoDoacao(String tipoDoacao) {
		this.tipoDoacao = tipoDoacao;
	}


	public BigDecimal getValorOverhead() {
		return valorOverhead;
	}


	public void setValorOverhead(BigDecimal valorOverhead) {
		this.valorOverhead = valorOverhead;
	}


	public BigDecimal getValorOverheadIndireto() {
		return valorOverheadIndireto;
	}


	public void setValorOverheadIndireto(BigDecimal valorOverheadIndireto) {
		this.valorOverheadIndireto = valorOverheadIndireto;
	}


	public BigDecimal getValorRendimento() {
		return valorRendimento;
	}


	public void setValorRendimento(BigDecimal valorRendimento) {
		this.valorRendimento = valorRendimento;
	}





	public BigDecimal getValorOverheadExec() {
		return valorOverheadExec;
	}


	public void setValorOverheadExec(BigDecimal valorOverheadExec) {
		this.valorOverheadExec = valorOverheadExec;
	}


	public BigDecimal getValorOverheadIndiretoExec() {
		return valorOverheadIndiretoExec;
	}


	public void setValorOverheadIndiretoExec(BigDecimal valorOverheadIndiretoExec) {
		this.valorOverheadIndiretoExec = valorOverheadIndiretoExec;
	}


	public BigDecimal getValorRendimentoExec() {
		return valorRendimentoExec;
	}


	public void setValorRendimentoExec(BigDecimal valorRendimentoExec) {
		this.valorRendimentoExec = valorRendimentoExec;
	}


	public BigDecimal getCustoPessoalExec() {
		return custoPessoalExec;
	}


	public void setCustoPessoalExec(BigDecimal custoPessoalExec) {
		this.custoPessoalExec = custoPessoalExec;
	}


	public BigDecimal getValorCustoPessoal() {
		return valorCustoPessoal;
	}


	public void setValorCustoPessoal(BigDecimal valorCustoPessoal) {
		this.valorCustoPessoal = valorCustoPessoal;
	}


	public BigDecimal getValorOverheadAPagar() {
		return valorOverheadAPagar;
	}


	public void setValorOverheadAPagar(BigDecimal valorOverheadAPagar) {
		this.valorOverheadAPagar = valorOverheadAPagar;
	}


	public BigDecimal getSaldoReal() {
		return saldoReal;
	}


	public void setSaldoReal(BigDecimal saldoReal) {
		this.saldoReal = saldoReal;
	}

}
