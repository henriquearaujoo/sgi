package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import util.DataUtil;
import util.Util;

/**
 * @author developer
 *
 */
@Entity
@Table
public class Projeto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int NUMERO_APROVACOES = 2;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String nome;
	@Column
	private String codigo;
	@Column
	private String objetivo;
	@Column(name = "objetivo_especifico")
	@Lob
	private String objetivoEspecifico;

	@Column(name = "observacao")
	private String observacao;

	@Column
	private Boolean estrutura;

	@Column
	private Boolean tarifado;

	@Column(name = "contextualizacao")
	@Lob
	private String contextualizacao;
	@Temporal(TemporalType.DATE)
	private Date dataInicio;
	@Temporal(TemporalType.DATE)
	private Date dataFinal;
	@ManyToOne
	private User usuario;
	@Enumerated(EnumType.STRING)
	private Programa programa;
	@Enumerated(EnumType.STRING)
	private TipoProjeto tipoProjeto;
	@ManyToOne
	private Localidade localidade;
	@ManyToOne
	private QualifProjeto qualificacaoProjeto;

	@Enumerated(EnumType.STRING)
	private TipoLocalidade tipoLocalidade;
	@Enumerated(EnumType.STRING)
	private TipoGestao tipoGestao;
	@ManyToOne
	private Gestao gestao;

	@Column
	private Boolean ativo;

	@ManyToOne
	private SubPrograma subPrograma;

	@ManyToOne
	private CadeiaProdutiva cadeia;

	@Column(name = "extrategia_execucao")
	private String extrategiaExecucao;

	@Column
	private Integer quantidade;

	@ManyToOne(fetch = FetchType.LAZY)
	private PlanoDeTrabalho planoDeTrabalho;

	@Column
	private Boolean fundoAmazonia;

	@Column
	private String versionProjeto;

	@Column
	private Boolean validado;

	@ManyToOne
	private ComponenteClass componente;
	@ManyToOne
	private SubComponente subComponente;

	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal valor = BigDecimal.ZERO;

	@Column(precision = 10, scale = 2, nullable = false, name = "valor_previsto")
	private BigDecimal valorPrevio = BigDecimal.ZERO;

	@OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrcamentoProjeto> orcamentos = new ArrayList<>();

	// @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval =
	// true)
	// private List<AtividadeProjeto> atividadesProjeto = new ArrayList<>();

	@OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AtividadeAcao> atividades = new ArrayList<>();

	@Column
	private String responsavel;

	@Enumerated(EnumType.STRING)
	private GrupoDeInvestimento grupoInvestimento;

	@Column
	private String repasse;

	@Column
	private Integer quantidadeAtividade;

	@Enumerated(EnumType.STRING)
	private StatusAprovacaoProjeto statusAprovacao;

	@Transient
	private List<Acao> acoes = new ArrayList<>();

	@Transient
	private String nomeLocalidade;

	@Transient
	private String nomeCadeia;

	@Transient
	private String nomeComponente;

	@Transient
	private String nomeSubComponente;

	@Transient
	private String nomeFonte;

	@Transient
	private String nomeGestao;

	@Transient
	private String plano;

	@Transient
	private String codePlano;

	@Transient
	private BigDecimal valorSaida;

	@Transient
	private BigDecimal valorEntrada;

	@Transient
	private BigDecimal valorSaldo;

	@Transient
	private BigDecimal totalVLOrcado;
	@Transient
	private BigDecimal totalVLlExecutado;
	@Transient
	private BigDecimal totalSaldo;
	@Transient
	private Double totalPercentualFinanceiro;
	@Transient
	private Integer totalQtdPlanejada;
	@Transient
	private Integer totalQtdNaoPlanejada;
	@Transient
	private Integer totalQtdExecutada;
	@Transient
	private Integer totalPendentes;
	@Transient
	private Double totalPercentualFisico;
	@Transient
	private BigDecimal totalEntrada;
	@Transient
	private BigDecimal totalSaida;

	@Transient
	private String cor;

	public Projeto() {
	}

	public Projeto(Object[] object) {
		setCodigo(Util.nullEmptyObjectUtil(object[0]));
		setNomeLocalidade(Util.nullEmptyObjectUtil(object[1]));
		setNomeComponente(Util.nullEmptyObjectUtil(object[2]));
		setNomeCadeia(Util.nullEmptyObjectUtil(object[3]));
		setObjetivo(Util.nullEmptyObjectUtil(object[4]));
		setNome(Util.nullEmptyObjectUtil(object[5]));
		setQuantidade(object[6] != null ? Integer.valueOf(object[6].toString()) : 0);
		setRepasse(Util.nullEmptyObjectUtil(object[7]));
		setValor(object[8] != null ? new BigDecimal(object[8].toString()) : BigDecimal.ZERO);
		setNomeFonte(Util.nullEmptyObjectUtil(object[9]));
		setId(object[10] == null ? new Long(0) : new Long(object[10].toString()));
		setObservacao(Util.nullEmptyObjectUtil(object[11]));
	}

	public Projeto(Long id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public Projeto(Long id, String nome, BigDecimal valor) {
		this.id = id;
		this.nome = nome;
		this.valor = valor;
	}

	public Projeto(Long id, String nome, String codigo) {
		this.id = id;
		this.nome = nome;
		this.codigo = codigo;
	}

	public Projeto(Long id, String nome, String codigo, BigDecimal valor) {
		this.id = id;
		this.nome = codigo + " - " + nome;
		// this.codigo = codigo;
	}

	public Projeto(Long id, String nome, String codigo, BigDecimal valor, Gestao gestao) {
		this.id = id;
		this.nome = codigo + " - " + nome + " / " + gestao.getNome();
	}

	public Projeto(Long id, String nome, Date dataInicio, Date dataFim, BigDecimal valor) {
		this.id = id;
		this.nome = nome;
		this.dataInicio = dataInicio;
		this.dataFinal = dataFim;
		this.valor = valor;
	}

	public Projeto(Long id, String nome, Date dataInicio, Date dataFim, BigDecimal valor, Boolean tarifado) {
		this.id = id;
		this.nome = nome;
		this.dataInicio = dataInicio;
		this.dataFinal = dataFim;
		this.valor = valor;
		this.tarifado = tarifado;
	}

	public Projeto(Long id, String codigo, String nome, BigDecimal valor, Integer quantidadeAtividade) {
		this.id = id;
		this.codigo = codigo;
		this.nome = nome;
		this.valor = valor;
		this.quantidadeAtividade = quantidadeAtividade;
	}

	public Projeto(Long id, String nome, Date dataInicio, Date dataFim, BigDecimal valor, Boolean tarifado,
			String codigo) {
		this.id = id;
		this.nome = nome;
		this.dataInicio = dataInicio;
		this.dataFinal = dataFim;
		this.valor = valor;
		this.tarifado = tarifado;
		this.codigo = codigo;
	}

	public Projeto(Long id, String codigo, String nome, Date dataInicio, Date dataFim, BigDecimal valor,
			Integer quantidaDeAtividades) {
		this.id = id;
		this.nome = nome;
		this.dataInicio = dataInicio;
		this.dataFinal = dataFim;
		this.valor = valor;
		this.quantidadeAtividade = quantidaDeAtividades;
		this.codigo = codigo;
	}

	public Projeto(Long id, String nome, Date dataInicio, Date dataFim, BigDecimal valor, Boolean tarifado,
			String codigo, String cadeia) {
		this.id = id;
		this.nome = nome;
		this.dataInicio = dataInicio;
		this.dataFinal = dataFim;
		this.valor = valor;
		this.tarifado = tarifado;
		this.codigo = codigo;
		this.nomeCadeia = cadeia;
	}

	public Projeto(Long id, String nome, Date dataInicio, Date dataFim, BigDecimal valor, Boolean tarifado,
			String codigo, String cadeia, Boolean ativo) {
		this.id = id;
		this.nome = nome;
		this.dataInicio = dataInicio;
		this.dataFinal = dataFim;
		this.valor = valor;
		this.tarifado = tarifado;
		this.codigo = codigo;
		this.nomeCadeia = cadeia;
		this.ativo = ativo;

	}

	@Transient
	private BigDecimal faltaEmpenhar = BigDecimal.ZERO;

	public Projeto(Long id, String nome, Date dataInicio, Date dataFim, BigDecimal valor, String codigo, Boolean ativo,
			BigDecimal totalEntrada, BigDecimal totalSaida, Integer qtdAtividadePlan, Integer qtdAtividadeExec) {

		this.id = id;
		this.nome = nome;
		this.dataInicio = dataInicio;
		this.dataFinal = dataFim;
		this.valor = valor;
		this.codigo = codigo;
		this.ativo = ativo;

		this.totalVLlExecutado = totalSaida.subtract(totalEntrada);
		this.totalSaldo = this.valor.subtract(this.totalVLlExecutado);

		this.totalQtdPlanejada = qtdAtividadePlan;

		this.totalQtdExecutada = qtdAtividadeExec;

		this.totalPendentes = qtdAtividadePlan - qtdAtividadeExec;

		this.totalPercentualFisico = (totalQtdExecutada.doubleValue() / totalQtdPlanejada) * 100;

		this.totalPercentualFinanceiro = (totalVLlExecutado.doubleValue() / this.valor.doubleValue()) * 100;

	}

	public Projeto(Long id, String nome, Date dataInicio, Date dataFim, BigDecimal valor, String codigo, Boolean ativo,
			BigDecimal totalEntrada, BigDecimal totalSaida, Integer qtdAtividadePlan, Integer qtdAtividadeExec,
			String statusAprovacao) {

		this.id = id;
		this.nome = nome;
		this.dataInicio = dataInicio;
		this.dataFinal = dataFim;
		this.valor = valor;
		this.codigo = codigo;
		this.ativo = ativo;

		this.totalVLlExecutado = totalSaida.subtract(totalEntrada);
		this.totalSaldo = this.valor.subtract(this.totalVLlExecutado);

		this.totalQtdPlanejada = qtdAtividadePlan;

		this.totalQtdExecutada = qtdAtividadeExec;

		this.totalPendentes = qtdAtividadePlan - qtdAtividadeExec;

		this.totalPercentualFisico = (totalQtdExecutada.doubleValue() / totalQtdPlanejada) * 100;

		this.totalPercentualFinanceiro = (totalVLlExecutado.doubleValue() / this.valor.doubleValue()) * 100;

		if (!statusAprovacao.equals(""))
			this.statusAprovacao = StatusAprovacaoProjeto.valueOf(statusAprovacao);
	}

	public Projeto(Long id, String nome, Date dataInicio, Date dataFim, BigDecimal valor, Boolean tarifado,
			String codigo, String cadeia, Boolean ativo, String nomeLocalidade, String codePlano, String plano,
			String componente, String subComponente, String nomeGestao, BigDecimal faltaEmpenhar) {
		this.id = id;
		this.nome = nome;
		this.dataInicio = dataInicio;
		this.dataFinal = dataFim;
		this.valor = valor;
		this.tarifado = tarifado;
		this.codigo = codigo;
		this.nomeCadeia = cadeia;
		this.ativo = ativo;
		this.nomeLocalidade = nomeLocalidade;
		this.codePlano = codePlano;
		this.plano = plano;
		this.nomeComponente = componente;
		this.nomeSubComponente = subComponente;
		this.nomeGestao = nomeGestao;
		this.faltaEmpenhar = faltaEmpenhar;

	}

	public Projeto(Long id, String nome, Date dataInicio, Date dataFim, BigDecimal valor, Boolean tarifado,
			String codigo, String cadeia, Boolean ativo, String nomeLocalidade, String codePlano, String plano,
			String componente, String subComponente, String nomeGestao, BigDecimal faltaEmpenhar,
			BigDecimal totalEntrada, BigDecimal totalSaida, Integer qtdAtividadePlan, Integer qtdAtividadeExec,
			Integer qtdAtividadeNpLan) {
		this.id = id;
		this.nome = nome;
		this.dataInicio = dataInicio;
		this.dataFinal = dataFim;
		this.valor = valor;
		this.tarifado = tarifado;
		this.codigo = codigo;
		this.nomeCadeia = cadeia;
		this.ativo = ativo;
		this.nomeLocalidade = nomeLocalidade;
		this.codePlano = codePlano;
		this.plano = plano;
		this.nomeComponente = componente;
		this.nomeSubComponente = subComponente;
		this.nomeGestao = nomeGestao;
		this.faltaEmpenhar = faltaEmpenhar;

		this.totalVLlExecutado = totalSaida.subtract(totalEntrada);
		this.totalSaldo = this.valor.subtract(this.totalVLlExecutado);

		this.totalQtdPlanejada = qtdAtividadePlan;

		this.totalQtdNaoPlanejada = qtdAtividadeNpLan;

		this.totalQtdExecutada = qtdAtividadeExec;

		this.totalPendentes = qtdAtividadePlan - qtdAtividadeExec;

		this.totalPercentualFisico = (totalQtdExecutada.doubleValue() / totalQtdPlanejada) * 100;

		this.totalPercentualFinanceiro = (totalVLlExecutado.doubleValue() / this.valor.doubleValue()) * 100;

	}

	public Projeto(Long id, String codigo, String nome, Date dataInicio, Date dataFim, BigDecimal valor) {
		this.id = id;
		this.codigo = codigo;
		this.nome = nome;
		this.dataInicio = dataInicio;
		this.dataFinal = dataFim;
		this.valor = valor;
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
		Projeto other = (Projeto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public BigDecimal getValorSaida() {
		return valorSaida;
	}

	public void setValorSaida(BigDecimal valorSaida) {
		this.valorSaida = valorSaida;
	}

	public BigDecimal getValorEntrada() {
		return valorEntrada;
	}

	public void setValorEntrada(BigDecimal valorEntrada) {
		this.valorEntrada = valorEntrada;
	}

	public BigDecimal getValorSaldo() {
		return valorSaldo;
	}

	public void setValorSaldo(BigDecimal valorSaldo) {
		this.valorSaldo = valorSaldo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
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

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
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

	public TipoGestao getTipoGestao() {
		return tipoGestao;
	}

	public void setTipoGestao(TipoGestao tipoGestao) {
		this.tipoGestao = tipoGestao;
	}

	public Gestao getGestao() {
		return gestao;
	}

	public void setGestao(Gestao gestao) {
		this.gestao = gestao;
	}

	public List<Acao> getAcoes() {
		return acoes;
	}

	public void setAcoes(List<Acao> acoes) {
		this.acoes = acoes;
	}

	public String getObjetivoEspecifico() {
		return objetivoEspecifico;
	}

	public void setObjetivoEspecifico(String objetivoEspecifico) {
		this.objetivoEspecifico = objetivoEspecifico;
	}

	public String getContextualizacao() {
		return contextualizacao;
	}

	public void setContextualizacao(String contextualizacao) {
		this.contextualizacao = contextualizacao;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public List<OrcamentoProjeto> getOrcamentos() {
		return orcamentos;
	}

	public void setOrcamentos(List<OrcamentoProjeto> orcamentos) {
		this.orcamentos = orcamentos;
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

	public Boolean getFundoAmazonia() {
		return fundoAmazonia;
	}

	public void setFundoAmazonia(Boolean fundoAmazonia) {
		this.fundoAmazonia = fundoAmazonia;
	}

	public String getVersionProjeto() {
		return versionProjeto;
	}

	public void setVersionProjeto(String versionProjeto) {
		this.versionProjeto = versionProjeto;
	}

	public Boolean getValidado() {
		return validado;
	}

	public void setValidado(Boolean validado) {
		this.validado = validado;
	}

	public BigDecimal getValorPrevio() {
		return valorPrevio;
	}

	public void setValorPrevio(BigDecimal valorPrevio) {
		this.valorPrevio = valorPrevio;
	}

	public PlanoDeTrabalho getPlanoDeTrabalho() {
		return planoDeTrabalho;
	}

	public void setPlanoDeTrabalho(PlanoDeTrabalho planoDeTrabalho) {
		this.planoDeTrabalho = planoDeTrabalho;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
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

	public CadeiaProdutiva getCadeia() {
		return cadeia;
	}

	public void setCadeia(CadeiaProdutiva cadeia) {
		this.cadeia = cadeia;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Boolean getEstrutura() {
		return estrutura;
	}

	public void setEstrutura(Boolean estrutura) {
		this.estrutura = estrutura;
	}

	public String getNomeLocalidade() {
		return nomeLocalidade;
	}

	public void setNomeLocalidade(String nomeLocalidade) {
		this.nomeLocalidade = nomeLocalidade;
	}

	public String getNomeCadeia() {
		return nomeCadeia;
	}

	public void setNomeCadeia(String nomeCadeia) {
		this.nomeCadeia = nomeCadeia;
	}

	public String getNomeComponente() {
		return nomeComponente;
	}

	public void setNomeComponente(String nomeComponente) {
		this.nomeComponente = nomeComponente;
	}

	public String getNomeFonte() {
		return nomeFonte;
	}

	public void setNomeFonte(String nomeFonte) {
		this.nomeFonte = nomeFonte;
	}

	public String getExtrategiaExecucao() {
		return extrategiaExecucao;
	}

	public void setExtrategiaExecucao(String extrategiaExecucao) {
		this.extrategiaExecucao = extrategiaExecucao;
	}

	public SubPrograma getSubPrograma() {
		return subPrograma;
	}

	public void setSubPrograma(SubPrograma subPrograma) {
		this.subPrograma = subPrograma;
	}

	public TipoProjeto getTipoProjeto() {
		return tipoProjeto;
	}

	public void setTipoProjeto(TipoProjeto tipoProjeto) {
		this.tipoProjeto = tipoProjeto;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Boolean getTarifado() {
		return tarifado;
	}

	public void setTarifado(Boolean tarifado) {
		this.tarifado = tarifado;
	}

	public String getNomeGestao() {
		return nomeGestao;
	}

	public void setNomeGestao(String nomeGestao) {
		this.nomeGestao = nomeGestao;
	}

	public String getPlano() {
		return plano;
	}

	public void setPlano(String plano) {
		this.plano = plano;
	}

	public String getNomeSubComponente() {
		return nomeSubComponente;
	}

	public void setNomeSubComponente(String nomeSubComponente) {
		this.nomeSubComponente = nomeSubComponente;
	}

	public String getCodePlano() {
		return codePlano;
	}

	public void setCodePlano(String codePlano) {
		this.codePlano = codePlano;
	}

	public BigDecimal getFaltaEmpenhar() {
		return faltaEmpenhar;
	}

	public void setFaltaEmpenhar(BigDecimal faltaEmpenhar) {
		this.faltaEmpenhar = faltaEmpenhar;
	}

	public Integer getQuantidadeAtividade() {
		return quantidadeAtividade;
	}

	public void setQuantidadeAtividade(Integer quantidadeAtividade) {
		this.quantidadeAtividade = quantidadeAtividade;
	}

	public QualifProjeto getQualificacaoProjeto() {
		return qualificacaoProjeto;
	}

	public void setQualificacaoProjeto(QualifProjeto qualificacaoProjeto) {
		this.qualificacaoProjeto = qualificacaoProjeto;
	}

	public List<AtividadeAcao> getAtividades() {
		return atividades;
	}

	public void setAtividades(List<AtividadeAcao> atividades) {
		this.atividades = atividades;
	}

	public BigDecimal getTotalVLOrcado() {
		return totalVLOrcado;
	}

	public void setTotalVLOrcado(BigDecimal totalVLOrcado) {
		this.totalVLOrcado = totalVLOrcado;
	}

	public BigDecimal getTotalVLlExecutado() {
		return totalVLlExecutado;
	}

	public void setTotalVLlExecutado(BigDecimal totalVLlExecutado) {
		this.totalVLlExecutado = totalVLlExecutado;
	}

	public Double getTotalPercentualFinanceiro() {
		return totalPercentualFinanceiro;
	}

	public void setTotalPercentualFinanceiro(Double totalPercentualFinanceiro) {
		this.totalPercentualFinanceiro = totalPercentualFinanceiro;
	}

	public Integer getTotalQtdPlanejada() {
		return totalQtdPlanejada;
	}

	public void setTotalQtdPlanejada(Integer totalQtdPlanejada) {
		this.totalQtdPlanejada = totalQtdPlanejada;
	}

	public Integer getTotalQtdExecutada() {
		return totalQtdExecutada;
	}

	public void setTotalQtdExecutada(Integer totalQtdExecutada) {
		this.totalQtdExecutada = totalQtdExecutada;
	}

	public Integer getTotalPendentes() {
		return totalPendentes;
	}

	public void setTotalPendentes(Integer totalPendentes) {
		this.totalPendentes = totalPendentes;
	}

	public Double getTotalPercentualFisico() {
		return totalPercentualFisico;
	}

	public void setTotalPercentualFisico(Double totalPercentualFisico) {
		this.totalPercentualFisico = totalPercentualFisico;
	}

	public BigDecimal getTotalEntrada() {
		return totalEntrada;
	}

	public void setTotalEntrada(BigDecimal totalEntrada) {
		this.totalEntrada = totalEntrada;
	}

	public BigDecimal getTotalSaida() {
		return totalSaida;
	}

	public void setTotalSaida(BigDecimal totalSaida) {
		this.totalSaida = totalSaida;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getTotalSaldo() {
		return totalSaldo;
	}

	public void setTotalSaldo(BigDecimal totalSaldo) {
		this.totalSaldo = totalSaldo;
	}

	public Integer getTotalQtdNaoPlanejada() {
		return totalQtdNaoPlanejada;
	}

	public void setTotalQtdNaoPlanejada(Integer totalQtdNaoPlanejada) {
		this.totalQtdNaoPlanejada = totalQtdNaoPlanejada;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public StatusAprovacaoProjeto getStatusAprovacao() {
		return statusAprovacao;
	}

	public void setStatusAprovacao(StatusAprovacaoProjeto statusAprovacao) {
		this.statusAprovacao = statusAprovacao;
	}
}
