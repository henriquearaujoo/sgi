package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import util.Util;

@Entity
@Table(name = "atividade_projeto")
public class AtividadeProjeto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String nome;

	@Column
	private String descricao;

	@Column(precision = 10, scale = 2)
	private BigDecimal orcado;

	@ManyToOne
	private Projeto projeto;

	@Enumerated(EnumType.STRING)
	private StatusAtividade status;

	@Column(name = "data_execucao")
	@Temporal(TemporalType.DATE)
	private Date dataExecutado;

	@Column(name = "data_planejado")
	@Temporal(TemporalType.DATE)
	private Date dataPlanejado;

	@Column(name = "data_inicio")
	@Temporal(TemporalType.DATE)
	private Date dataInicio;

	@Column(name = "data_fim")
	@Temporal(TemporalType.DATE)
	private Date dataFim;

	@Column(name = "data_conclusao")
	@Temporal(TemporalType.DATE)
	private Date dataConclusao;

	@Column(name = "versao")
	private String versao;

	@Column
	private String observacao;

	@Column
	private Boolean planejado;

	@Column
	private Integer execucao = 0;

	@Column
	private String responsavel;

	@Column(name = "valor", precision = 10, scale = 2)
	private BigDecimal valor;

	@Column
	private Integer quantidade;
	
	@ManyToOne
	private Metas meta;
	
	@ManyToOne
	private Localidade localizador;
	
	@Column(columnDefinition = "int default 0")
	private Integer jan;
	@Column(columnDefinition = "int default 0")
	private Integer fev;
	@Column(columnDefinition = "int default 0")
	private Integer mar;
	@Column(columnDefinition = "int default 0")
	private Integer abr;
	@Column(columnDefinition = "int default 0")
	private Integer mai;
	@Column(columnDefinition = "int default 0")
	private Integer jun;
	@Column(columnDefinition = "int default 0")
	private Integer jul;
	@Column(columnDefinition = "int default 0")
	private Integer ago;
	@Column(columnDefinition = "int default 0")
	private Integer set;
	@Column(columnDefinition = "int default 0")
	private Integer out;
	@Column(columnDefinition = "int default 0")
	private Integer nov;
	@Column(columnDefinition = "int default 0")
	private Integer dez;
	
	@Column(columnDefinition = "int default 0")
	private Integer janExec;
	@Column(columnDefinition = "int default 0")
	private Integer fevExec;
	@Column(columnDefinition = "int default 0")
	private Integer marExec;
	@Column(columnDefinition = "int default 0")
	private Integer abrExec;
	@Column(columnDefinition = "int default 0")
	private Integer maiExec;
	@Column(columnDefinition = "int default 0")
	private Integer junExec;
	@Column(columnDefinition = "int default 0")
	private Integer julExec;
	@Column(columnDefinition = "int default 0")
	private Integer agoExec;
	@Column(columnDefinition = "int default 0")
	private Integer setExec;
	@Column(columnDefinition = "int default 0")
	private Integer outExec;
	@Column(columnDefinition = "int default 0")
	private Integer novExec;
	@Column(columnDefinition = "int default 0")
	private Integer dezExec;
	
	
	
	
	@Transient
	private String nomeProjeto;

	@Transient
	private Boolean mes1;
	@Transient
	private Boolean mes2;
	@Transient
	private Boolean mes3;
	@Transient
	private Boolean mes4;
	@Transient
	private Boolean mes5;
	@Transient
	private Boolean mes6;
	@Transient
	private Boolean mes7;
	@Transient
	private Boolean mes8;
	@Transient
	private Boolean mes9;
	@Transient
	private Boolean mes10;
	@Transient
	private Boolean mes11;
	@Transient
	private Boolean mes12;
	
	@Transient
	private String nomeLocalizador;
	
	@Transient
	private BigDecimal saldo;

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public AtividadeProjeto(Long id, String nome, String descricao, BigDecimal orcado, StatusAtividade status,
			String nomeProjeto, String observacao, Date dataInicio, Date dataFim, Date dataConclusao, Boolean planejado,
			Integer execucao) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.orcado = orcado;
		this.status = status;
		this.nomeProjeto = nomeProjeto;
		this.observacao = observacao;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.dataConclusao = dataConclusao;
		this.planejado = planejado;
		this.execucao = execucao;
	}

	public AtividadeProjeto(Object[] obj) {
		this.nome = Util.nullEmptyObjectUtil(obj[0]);
//		this.mes1 = Util.nullEmptyBooleanObjectUtil(obj[1]);
//		this.mes2 = Util.nullEmptyBooleanObjectUtil(obj[2]);
//		this.mes3 = Util.nullEmptyBooleanObjectUtil(obj[3]);
//		this.mes4 = Util.nullEmptyBooleanObjectUtil(obj[4]);
//		this.mes5 = Util.nullEmptyBooleanObjectUtil(obj[5]);
//		this.mes6 = Util.nullEmptyBooleanObjectUtil(obj[6]);
//		this.mes7 = Util.nullEmptyBooleanObjectUtil(obj[7]);
//		this.mes8 = Util.nullEmptyBooleanObjectUtil(obj[8]);
//		this.mes9 = Util.nullEmptyBooleanObjectUtil(obj[9]);
//		this.mes10 = Util.nullEmptyBooleanObjectUtil(obj[10]);
//		this.mes11 = Util.nullEmptyBooleanObjectUtil(obj[11]);
//		this.mes12 = Util.nullEmptyBooleanObjectUtil(obj[12]);
		
		
		this.jan = Integer.parseInt(obj[1] != null ? obj[1].toString() : "0");
		this.fev = Integer.parseInt(obj[2] != null ? obj[2].toString() : "0");
		this.mar = Integer.parseInt(obj[3] != null ? obj[3].toString() : "0");
		this.abr = Integer.parseInt(obj[4] != null ? obj[4].toString() : "0");
		this.mai = Integer.parseInt(obj[5] != null ? obj[5].toString() : "0");
		this.jun = Integer.parseInt(obj[6] != null ? obj[6].toString() : "0");
		this.jul = Integer.parseInt(obj[7] != null ? obj[7].toString() : "0");
		this.ago = Integer.parseInt(obj[8] != null ? obj[8].toString() : "0");
		this.set = Integer.parseInt(obj[9] != null ? obj[9].toString() : "0");
		this.out = Integer.parseInt(obj[10] != null ? obj[10].toString() : "0");
		this.nov = Integer.parseInt(obj[11] != null ? obj[11].toString() : "0");
		this.dez = Integer.parseInt(obj[12] != null ? obj[12].toString() : "0");
		
		
		this.responsavel = Util.nullEmptyObjectUtil(obj[13]);
		this.quantidade =  obj[14] != null ? Integer.valueOf(obj[14].toString()) : 1;
		this.valor = obj[15] != null ? new BigDecimal(obj[15].toString()) : BigDecimal.ZERO;
		this.id = new Long(obj[16].toString());
		this.nomeLocalizador = Util.nullEmptyObjectUtil(obj[17]);
		
	}
	
	public AtividadeProjeto(Object[] obj, String args) {
		this.nome = Util.nullEmptyObjectUtil(obj[0]);
//		this.mes1 = Util.nullEmptyBooleanObjectUtil(obj[1]);
//		this.mes2 = Util.nullEmptyBooleanObjectUtil(obj[2]);
//		this.mes3 = Util.nullEmptyBooleanObjectUtil(obj[3]);
//		this.mes4 = Util.nullEmptyBooleanObjectUtil(obj[4]);
//		this.mes5 = Util.nullEmptyBooleanObjectUtil(obj[5]);
//		this.mes6 = Util.nullEmptyBooleanObjectUtil(obj[6]);
//		this.mes7 = Util.nullEmptyBooleanObjectUtil(obj[7]);
//		this.mes8 = Util.nullEmptyBooleanObjectUtil(obj[8]);
//		this.mes9 = Util.nullEmptyBooleanObjectUtil(obj[9]);
//		this.mes10 = Util.nullEmptyBooleanObjectUtil(obj[10]);
//		this.mes11 = Util.nullEmptyBooleanObjectUtil(obj[11]);
//		this.mes12 = Util.nullEmptyBooleanObjectUtil(obj[12]);
		
		
		this.janExec = Integer.parseInt(obj[1] != null ? obj[1].toString() : "0");
		this.fevExec = Integer.parseInt(obj[2] != null ? obj[2].toString() : "0");
		this.marExec = Integer.parseInt(obj[3] != null ? obj[3].toString() : "0");
		this.abrExec = Integer.parseInt(obj[4] != null ? obj[4].toString() : "0");
		this.maiExec = Integer.parseInt(obj[5] != null ? obj[5].toString() : "0");
		this.junExec = Integer.parseInt(obj[6] != null ? obj[6].toString() : "0");
		this.julExec = Integer.parseInt(obj[7] != null ? obj[7].toString() : "0");
		this.agoExec = Integer.parseInt(obj[8] != null ? obj[8].toString() : "0");
		this.setExec = Integer.parseInt(obj[9] != null ? obj[9].toString() : "0");
		this.outExec = Integer.parseInt(obj[10] != null ? obj[10].toString() : "0");
		this.novExec = Integer.parseInt(obj[11] != null ? obj[11].toString() : "0");
		this.dezExec = Integer.parseInt(obj[12] != null ? obj[12].toString() : "0");
		
		
		this.responsavel = Util.nullEmptyObjectUtil(obj[13]);
		this.quantidade =  obj[14] != null ? Integer.valueOf(obj[14].toString()) : 1;
		this.valor = obj[15] != null ? new BigDecimal(obj[15].toString()) : BigDecimal.ZERO;
		this.id = new Long(obj[16].toString());
		
		this.nomeLocalizador = Util.nullEmptyObjectUtil(obj[17]);
		
	}

	public AtividadeProjeto() {
		
		jan = 0;
		fev = 0;
		mar = 0;
		abr = 0;
		mai = 0;
		jun = 0;
		jul = 0;
		ago = 0;
		set = 0;
		out = 0;
		nov = 0;
		dez = 0;
		
	}

	public AtividadeProjeto(Long id) {
		this.id = id;
	}

	public Integer getExecucao() {
		return execucao;
	}

	public void setExecucao(Integer execucao) {
		this.execucao = execucao;
	}

	public Boolean getPlanejado() {
		return planejado;
	}

	public void setPlanejado(Boolean planejado) {
		this.planejado = planejado;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeProjeto() {
		return nomeProjeto;
	}

	public void setNomeProjeto(String nomeProjeto) {
		this.nomeProjeto = nomeProjeto;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Date getDataConclusao() {
		return dataConclusao;
	}

	public void setDataConclusao(Date dataConclusao) {
		this.dataConclusao = dataConclusao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public StatusAtividade getStatus() {
		return status;
	}

	public void setStatus(StatusAtividade status) {
		this.status = status;
	}

	public BigDecimal getOrcado() {
		return orcado;
	}

	public void setOrcado(BigDecimal orcado) {
		this.orcado = orcado;
	}

	public String getTipo() {
		return "atividade";
	}

	public Date getDataExecutado() {
		return dataExecutado;
	}

	public void setDataExecutado(Date dataExecutado) {
		this.dataExecutado = dataExecutado;
	}

	public Date getDataPlanejado() {
		return dataPlanejado;
	}

	public void setDataPlanejado(Date dataPlanejado) {
		this.dataPlanejado = dataPlanejado;
	}

	public String getVersao() {
		return versao;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public Boolean getMes1() {
		return mes1;
	}

	public void setMes1(Boolean mes1) {
		this.mes1 = mes1;
	}

	public Boolean getMes2() {
		return mes2;
	}

	public void setMes2(Boolean mes2) {
		this.mes2 = mes2;
	}

	public Boolean getMes3() {
		return mes3;
	}

	public void setMes3(Boolean mes3) {
		this.mes3 = mes3;
	}

	public Boolean getMes4() {
		return mes4;
	}

	public void setMes4(Boolean mes4) {
		this.mes4 = mes4;
	}

	public Boolean getMes5() {
		return mes5;
	}

	public void setMes5(Boolean mes5) {
		this.mes5 = mes5;
	}

	public Boolean getMes6() {
		return mes6;
	}

	public void setMes6(Boolean mes6) {
		this.mes6 = mes6;
	}

	public Boolean getMes7() {
		return mes7;
	}

	public void setMes7(Boolean mes7) {
		this.mes7 = mes7;
	}

	public Boolean getMes8() {
		return mes8;
	}

	public void setMes8(Boolean mes8) {
		this.mes8 = mes8;
	}

	public Boolean getMes9() {
		return mes9;
	}

	public void setMes9(Boolean mes9) {
		this.mes9 = mes9;
	}

	public Boolean getMes10() {
		return mes10;
	}

	public void setMes10(Boolean mes10) {
		this.mes10 = mes10;
	}

	public Boolean getMes11() {
		return mes11;
	}

	public void setMes11(Boolean mes11) {
		this.mes11 = mes11;
	}

	public Boolean getMes12() {
		return mes12;
	}

	public void setMes12(Boolean mes12) {
		this.mes12 = mes12;
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

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Integer getJan() {
		return jan;
	}

	public void setJan(Integer jan) {
		this.jan = jan;
	}

	public Integer getFev() {
		return fev;
	}

	public void setFev(Integer fev) {
		this.fev = fev;
	}

	public Integer getMar() {
		return mar;
	}

	public void setMar(Integer mar) {
		this.mar = mar;
	}

	public Integer getAbr() {
		return abr;
	}

	public void setAbr(Integer abr) {
		this.abr = abr;
	}

	public Integer getMai() {
		return mai;
	}

	public void setMai(Integer mai) {
		this.mai = mai;
	}

	public Integer getJun() {
		return jun;
	}

	public void setJun(Integer jun) {
		this.jun = jun;
	}

	public Integer getJul() {
		return jul;
	}

	public void setJul(Integer jul) {
		this.jul = jul;
	}

	public Integer getAgo() {
		return ago;
	}

	public void setAgo(Integer ago) {
		this.ago = ago;
	}

	public Integer getSet() {
		return set;
	}

	public void setSet(Integer set) {
		this.set = set;
	}

	public Integer getOut() {
		return out;
	}

	public void setOut(Integer out) {
		this.out = out;
	}

	public Integer getNov() {
		return nov;
	}

	public void setNov(Integer nov) {
		this.nov = nov;
	}

	public Integer getDez() {
		return dez;
	}

	public void setDez(Integer dez) {
		this.dez = dez;
	}

	public Integer getJanExec() {
		return janExec;
	}

	public void setJanExec(Integer janExec) {
		this.janExec = janExec;
	}

	public Integer getFevExec() {
		return fevExec;
	}

	public void setFevExec(Integer fevExec) {
		this.fevExec = fevExec;
	}

	public Integer getMarExec() {
		return marExec;
	}

	public void setMarExec(Integer marExec) {
		this.marExec = marExec;
	}

	public Integer getAbrExec() {
		return abrExec;
	}

	public void setAbrExec(Integer abrExec) {
		this.abrExec = abrExec;
	}

	public Integer getMaiExec() {
		return maiExec;
	}

	public void setMaiExec(Integer maiExec) {
		this.maiExec = maiExec;
	}

	public Integer getJunExec() {
		return junExec;
	}

	public void setJunExec(Integer junExec) {
		this.junExec = junExec;
	}

	public Integer getJulExec() {
		return julExec;
	}

	public void setJulExec(Integer julExec) {
		this.julExec = julExec;
	}

	public Integer getAgoExec() {
		return agoExec;
	}

	public void setAgoExec(Integer agoExec) {
		this.agoExec = agoExec;
	}

	public Integer getSetExec() {
		return setExec;
	}

	public void setSetExec(Integer setExec) {
		this.setExec = setExec;
	}

	public Integer getOutExec() {
		return outExec;
	}

	public void setOutExec(Integer outExec) {
		this.outExec = outExec;
	}

	public Integer getNovExec() {
		return novExec;
	}

	public void setNovExec(Integer novExec) {
		this.novExec = novExec;
	}

	public Integer getDezExec() {
		return dezExec;
	}

	public void setDezExec(Integer dezExec) {
		this.dezExec = dezExec;
	}

	public Metas getMeta() {
		return meta;
	}

	public void setMeta(Metas meta) {
		this.meta = meta;
	}

	public Localidade getLocalizador() {
		return localizador;
	}

	public void setLocalizador(Localidade localizador) {
		this.localizador = localizador;
	}

	public String getNomeLocalizador() {
		return nomeLocalizador;
	}

	public void setNomeLocalizador(String nomeLocalizador) {
		this.nomeLocalizador = nomeLocalizador;
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
		AtividadeProjeto other = (AtividadeProjeto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
	

}
