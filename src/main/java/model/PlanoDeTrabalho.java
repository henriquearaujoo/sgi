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

@Entity
@Table(name = "plano_de_trabalho")
public class PlanoDeTrabalho implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String titulo; //identificação do plano
	
	@Column
	private String codigo;
	
	@Column(name = "objetivo_especifico")
	@Lob
	private String objetivoEspecifico;
	
	@Column
	@Lob
	private String objetivo;
	
	@Lob
	private String contextualizacao;
	
	@Column(name = "extrategia_execucao")
	@Lob
	private String extrategiaExecucao;
	
	@Temporal(TemporalType.DATE)
	private Date dataInicio;
	
	@Temporal(TemporalType.DATE)
	private Date dataFinal;
	
	@ManyToOne
	private User usuario;
	
	@Enumerated(EnumType.STRING)
	private Programa programa;
	
	@ManyToOne
	private UnidadeConservacao uc;
	
	@Enumerated(EnumType.STRING) 
	private TipoLocalidade tipoLocalidade;
	
	@Enumerated(EnumType.STRING)
	private TipoGestao tipoGestao;
	
	@ManyToOne
	private Gestao gestao;

	@Column
	private String versionProjeto;
	
	@Column
	private Boolean validado;
	
	@Column(precision = 10, scale = 2, nullable = false)
	private BigDecimal valor = BigDecimal.ZERO;
	
	@OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrcamentoProjeto> orcamentos = new ArrayList<>();
	
	@Column
	private String responsavel;
	
	@Transient
	private List<Acao> acoes = new ArrayList<>();
	
	@Transient
	private String nomeUC;
	
	@Transient
	private String nomeGestao;
	
	@Transient
	private Long qtdProjetos;
	
	@Transient
	private BigDecimal valorPrevisto;
	
	
	
	
	public BigDecimal getValorPrevisto() {
		return valorPrevisto;
	}

	public void setValorPrevisto(BigDecimal valorPrevisto) {
		this.valorPrevisto = valorPrevisto;
	}

	public PlanoDeTrabalho(){}
	
	public String getNomeGestao() {
		return nomeGestao;
	}

	public void setNomeGestao(String nomeGestao) {
		this.nomeGestao = nomeGestao;
	}

	public Long getQtdProjetos() {
		return qtdProjetos;
	}

	public void setQtdProjetos(Long qtdProjetos) {
		this.qtdProjetos = qtdProjetos;
	}

	public PlanoDeTrabalho(Long id, String titulo){
		this.id = id;
		this.titulo = titulo;
	}
	
	public PlanoDeTrabalho(Long id, String titulo, String nomeUC, Date dataInicio, Date dataFinal,BigDecimal valorPrevisto, BigDecimal valor,String nomeGestao,Long qtdProjetos){
		this.id = id;
		this.titulo = titulo;
		this.nomeUC = nomeUC;
		this.dataInicio = dataInicio;
		this.dataFinal = dataFinal;
		this.valor = valor;
		this.valorPrevisto = valorPrevisto;
		this.nomeGestao = nomeGestao;
		this.qtdProjetos = qtdProjetos;
	}
	
	public PlanoDeTrabalho(Long id, String titulo, String nomeUC, Date dataInicio, Date dataFinal, BigDecimal valor){
		this.id = id;
		this.titulo = titulo;
		this.nomeUC = nomeUC;
		this.dataInicio = dataInicio;
		this.dataFinal = dataFinal;
		this.valor = valor;
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
		PlanoDeTrabalho other = (PlanoDeTrabalho) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
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

	public UnidadeConservacao getUc() {
		return uc;
	}

	public void setUc(UnidadeConservacao uc) {
		this.uc = uc;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getObjetivoEspecifico() {
		return objetivoEspecifico;
	}

	public void setObjetivoEspecifico(String objetivoEspecifico) {
		this.objetivoEspecifico = objetivoEspecifico;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNomeUC() {
		return nomeUC;
	}

	public void setNomeUC(String nomeUC) {
		this.nomeUC = nomeUC;
	}

	public String getExtrategiaExecucao() {
		return extrategiaExecucao;
	}

	public void setExtrategiaExecucao(String extrategiaExecucao) {
		this.extrategiaExecucao = extrategiaExecucao;
	}

//	public BigDecimal getValorPrevio() {
//		return valorPrevio;
//	}
//
//	public void setValorPrevio(BigDecimal valorPrevio) {
//		this.valorPrevio = valorPrevio;
//	}

	
}
