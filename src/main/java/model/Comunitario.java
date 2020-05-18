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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Comunitario implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String nome;
	
	@ManyToOne
	private UnidadeConservacao unidadeConservacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPreenchimento;
	
	@ManyToOne
	private Localidade municipio;
	
	@ManyToOne
	private User entrevistador;
	
	@ManyToOne
	private Localidade polo;
	
	@ManyToOne
	private Localidade setor;
	
	@Enumerated(EnumType.STRING)
	private TipoRiosFamiliar rio;
	
	@Column
	private String lago;
	
	@Column
	private String denominacao;
	
	@Column
	private String localidade;
	
	@ManyToOne
	private Comunidade comunidade;
	
	@Enumerated(EnumType.STRING)
	private TipoSituacaoComunitario tipoSituacao;
	
	@Enumerated(EnumType.STRING)
	private TipoAmbienteFamiliar tipoAmbiente;
	
	@Column
	private Integer numeroPontoGPS;
	
	@Column
	private String latitude;
	
	@Column
	private String longitude;
	
	@Column
	private Integer numeroCasas;
	
	@Column
	private Integer numeroInfraestruturas;
	
	@Column
	private Integer numeroFamiliasNaSede;
	
	@Column
	private Integer numeroFamiliasDistanteSede;
	
	//lider comunitario
	@Column
	private String nomeCompleto;
	
	@Column
	private String apelido;
	
	@Column
	private String funcaoNaComunidade;
	
	@Column
	private String formaDeContato;
	
	@Column
	private Integer anosMoraNaComunidade;
	
	@Column
	private Integer anosLideranca;
	
	@OneToMany(mappedBy = "comunitario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DestinoEsgotoFamiliar> destinosEsgoto = new ArrayList<>();
	
	@OneToMany(mappedBy = "comunitario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DestinoLixoFamiliar> destinosLixo = new ArrayList<>();
	
	//Infraestrutura comunitaria
	@OneToMany(mappedBy = "comunitario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InfraestruturaComunitariaComunitario> infraestruturasComunitarias = new ArrayList<>();
	
	//Detalhamento de informaqcoes
	//Centro comunitario
	private Integer qtdePessoasCentroComunitario;
	
	@OneToMany(mappedBy = "comunitario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RealizacaoReuniaoComunitario> realizacaoReunioes = new ArrayList<>();
	
	//Transporte comunitário
	@Column
	private Boolean possuiVoadeira;
	
	@Column
	private Integer qtdeVoadeiras;

	@Enumerated(EnumType.STRING)
	private TipoPotenciaMotor potenciaMotorVoadeira;
	
	//retirar
	@Enumerated(EnumType.STRING)
	private TipoFinanciadorComunitario financiadorVoadeira;
	
	@Column
	private Boolean possuiOutroTipoTransporte;
	
	@Column
	private String tipoTransporte;

	@Enumerated(EnumType.STRING)
	private TipoPotenciaMotor potenciaMotorTransporte;
	
	//retirar
	@Enumerated(EnumType.STRING)
	private TipoFinanciadorComunitario financiadorTransporte;
	
	@OneToMany(mappedBy = "comunitario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FinanciamentoVoadeiraComunitario> financiamentosVoadeira = new ArrayList<>();
	
	@OneToMany(mappedBy = "comunitario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FinanciamentoTransporteComunitario> financiamentosTransporte = new ArrayList<>();
	
	//Educacao
	@Column
	private Boolean possuiEscola;
	
	@ManyToOne
	private Comunidade comunidade1a5Ano;
	
	@ManyToOne
	private Comunidade comunidade6a9Ano;
	
	@ManyToOne
	private Comunidade comunidadeEnsinoMedio;
	
	@Column
	private Integer qtdeMoradoresFora1a5Ano;
	
	@Column
	private Integer qtdeMoradoresFora6a9Ano;
	
	@Column
	private Integer qtdeMoradoresForaEnsinoMedio;
	
	@Column
	private Integer qtdeEscolas1a5Ano;
	
	@Column
	private Integer qtdeEscolas6a9Ano;
	
	@Column
	private Integer qtdeEscolasEnsinoMedio;
	
	@Column
	private Integer qtdeProfessores1a5Ano;
	
	@Column
	private Integer qtdeProfessores6a9Ano;
	
	@Column
	private Integer qtdeProfessoresEnsinoMedio;
	
	@Column
	private Integer qtdeAlunos1a5Ano;
	
	@Column
	private Integer qtdeAlunos6a9Ano;
	
	@Column
	private Integer qtdeAlunosEnsinoMedio;
	
	//Saude
	@Column
	private Boolean atendidaPorAgenteComunitarioDeSaude;
	
	@Column
	private Boolean agenteResideNaComunidade;
	
	@ManyToOne
	private Comunidade comunidadeAgente;
	
	@OneToMany(mappedBy = "comunitario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SintomasComunsFamiliar> sintomasSaude = new ArrayList<>(); 
	
	@Column
	private Boolean atendidaPorAmbulancha;
	
	@Column
	private Boolean ambulanchaSediadaNaComunidade;
	
	@Column
	private Boolean ambulanchaFunciona;
	
	@Column
	private Boolean ambulanchaQuebrada;
	
	@Column
	private Boolean ambulanchaTemCaderneta;
	
	@Column
	private Boolean possuiPostoDeSaude;
	
	@ManyToOne
	private Comunidade comunidadePostoSaude;
	
	@ManyToOne
	private Localidade municipioPostoSaude;
	
	//Agua
	@Column
	private Boolean possuiFonteDeAguaColetivo;
	
	@OneToMany(mappedBy = "comunitario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FonteAguaConsumoComunitario> fontesAguaConsumo = new ArrayList<>(); 
	
	@OneToMany(mappedBy = "comunitario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FormaPegarAguaComunitario> formasPegarAgua = new ArrayList<>();
	
	@Column
	private Boolean existeTratamentoAgua;
	
	@OneToMany(mappedBy = "comunitario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TratamentoAguaFamiliar> tratamentosAgua = new ArrayList<>();
	
	//Energia
	@Column
	private Integer tempoMedioEnergiaDiaria;
	
	@OneToMany(mappedBy = "comunitario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FonteEnergiaComunitario> fontesEnergia = new ArrayList<>();
	
	//Comunicação
	@Column
	private Boolean possuiMeiosDeComunicacao;
	
	@OneToMany(mappedBy = "comunitario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MeioComunicacaoComunitario> meiosComunicacao = new ArrayList<>(); 
	
	@Column
	private Boolean meiosFuncionam;
	
	@Column
	private Boolean temRadioAMFM;
	
	@Column
	private String emissorasRadioMaisEscuta;
	
	@Column
	private Boolean possuiProgramaRadioPreferido;
	
	@Column
	private String programaRadioPreferido;
	
	@Column
	private Boolean casasPossuiTV;
	
	@Column
	private String programaQueMaisAssistem;
	
	@Column
	private Boolean gostariaReceberMateriasInfoPBF;
	
	@OneToMany(mappedBy = "comunitario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MaterialInformativoComunitario> materiaisInformativos = new ArrayList<>(); 
	
	//Renda
	@Column
	private BigDecimal rendaMedia;
	
	@OneToMany(mappedBy = "comunitario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AtividadeRendaComunitario> atividadesRenda = new ArrayList<>();
	
	@Column
	private String informacoesGeraisSobreAComunidade;
	
	public Comunitario(){}
	
	public Comunitario(Long id, String nomeComunidade) {
		this.id = id;
		this.comunidade = new Comunidade();
		this.comunidade.setNome(nomeComunidade);
	}

	public Comunitario(Long id, String denominacao, String nomeCompleto, String nomeComunidade) {
		this.id = id;
		this.comunidade = new Comunidade();
		this.comunidade.setNome(nomeComunidade);
		this.denominacao = denominacao;
		this.nomeCompleto = nomeCompleto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public UnidadeConservacao getUnidadeConservacao() {
		return unidadeConservacao;
	}

	public void setUnidadeConservacao(UnidadeConservacao unidadeConservacao) {
		this.unidadeConservacao = unidadeConservacao;
	}

	public Date getDataPreenchimento() {
		return dataPreenchimento;
	}

	public void setDataPreenchimento(Date dataPreenchimento) {
		this.dataPreenchimento = dataPreenchimento;
	}

	public Localidade getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Localidade municipio) {
		this.municipio = municipio;
	}

	public User getEntrevistador() {
		return entrevistador;
	}

	public void setEntrevistador(User entrevistador) {
		this.entrevistador = entrevistador;
	}

	public Localidade getPolo() {
		return polo;
	}

	public void setPolo(Localidade polo) {
		this.polo = polo;
	}

	public Localidade getSetor() {
		return setor;
	}

	public void setSetor(Localidade setor) {
		this.setor = setor;
	}

	public TipoRiosFamiliar getRio() {
		return rio;
	}

	public void setRio(TipoRiosFamiliar rio) {
		this.rio = rio;
	}

	public String getLago() {
		return lago;
	}

	public void setLago(String lago) {
		this.lago = lago;
	}

	public String getDenominacao() {
		return denominacao;
	}

	public void setDenominacao(String denominacao) {
		this.denominacao = denominacao;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public Comunidade getComunidade() {
		return comunidade;
	}

	public void setComunidade(Comunidade comunidade) {
		this.comunidade = comunidade;
	}

	public TipoSituacaoComunitario getTipoSituacao() {
		return tipoSituacao;
	}

	public void setTipoSituacao(TipoSituacaoComunitario tipoSituacao) {
		this.tipoSituacao = tipoSituacao;
	}

	public TipoAmbienteFamiliar getTipoAmbiente() {
		return tipoAmbiente;
	}

	public void setTipoAmbiente(TipoAmbienteFamiliar tipoAmbiente) {
		this.tipoAmbiente = tipoAmbiente;
	}

	public Integer getNumeroPontoGPS() {
		return numeroPontoGPS;
	}

	public void setNumeroPontoGPS(Integer numeroPontoGPS) {
		this.numeroPontoGPS = numeroPontoGPS;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Integer getNumeroCasas() {
		return numeroCasas;
	}

	public void setNumeroCasas(Integer numeroCasas) {
		this.numeroCasas = numeroCasas;
	}

	public Integer getNumeroInfraestruturas() {
		return numeroInfraestruturas;
	}

	public void setNumeroInfraestruturas(Integer numeroInfraestruturas) {
		this.numeroInfraestruturas = numeroInfraestruturas;
	}

	public Integer getNumeroFamiliasNaSede() {
		return numeroFamiliasNaSede;
	}

	public void setNumeroFamiliasNaSede(Integer numeroFamiliasNaSede) {
		this.numeroFamiliasNaSede = numeroFamiliasNaSede;
	}

	public Integer getNumeroFamiliasDistanteSede() {
		return numeroFamiliasDistanteSede;
	}

	public void setNumeroFamiliasDistanteSede(Integer numeroFamiliasDistanteSede) {
		this.numeroFamiliasDistanteSede = numeroFamiliasDistanteSede;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public String getFuncaoNaComunidade() {
		return funcaoNaComunidade;
	}

	public void setFuncaoNaComunidade(String funcaoNaComunidade) {
		this.funcaoNaComunidade = funcaoNaComunidade;
	}

	public String getFormaDeContato() {
		return formaDeContato;
	}

	public void setFormaDeContato(String formaDeContato) {
		this.formaDeContato = formaDeContato;
	}

	public Integer getAnosMoraNaComunidade() {
		return anosMoraNaComunidade;
	}

	public void setAnosMoraNaComunidade(Integer anosMoraNaComunidade) {
		this.anosMoraNaComunidade = anosMoraNaComunidade;
	}

	public Integer getAnosLideranca() {
		return anosLideranca;
	}

	public void setAnosLideranca(Integer anosLideranca) {
		this.anosLideranca = anosLideranca;
	}

	public List<DestinoEsgotoFamiliar> getDestinosEsgoto() {
		return destinosEsgoto;
	}

	public void setDestinosEsgoto(List<DestinoEsgotoFamiliar> destinosEsgoto) {
		this.destinosEsgoto = destinosEsgoto;
	}

	public List<DestinoLixoFamiliar> getDestinosLixo() {
		return destinosLixo;
	}

	public void setDestinosLixo(List<DestinoLixoFamiliar> destinosLixo) {
		this.destinosLixo = destinosLixo;
	}

	public List<InfraestruturaComunitariaComunitario> getInfraestruturasComunitarias() {
		return infraestruturasComunitarias;
	}

	public void setInfraestruturasComunitarias(List<InfraestruturaComunitariaComunitario> infraestruturasComunitarias) {
		this.infraestruturasComunitarias = infraestruturasComunitarias;
	}

	public Integer getQtdePessoasCentroComunitario() {
		return qtdePessoasCentroComunitario;
	}

	public void setQtdePessoasCentroComunitario(Integer qtdePessoasCentroComunitario) {
		this.qtdePessoasCentroComunitario = qtdePessoasCentroComunitario;
	}

	public List<RealizacaoReuniaoComunitario> getRealizacaoReunioes() {
		return realizacaoReunioes;
	}

	public void setRealizacaoReunioes(List<RealizacaoReuniaoComunitario> realizacaoReunioes) {
		this.realizacaoReunioes = realizacaoReunioes;
	}

	public Boolean getPossuiVoadeira() {
		return possuiVoadeira;
	}

	public void setPossuiVoadeira(Boolean possuiVoadeira) {
		this.possuiVoadeira = possuiVoadeira;
	}

	public Integer getQtdeVoadeiras() {
		return qtdeVoadeiras;
	}

	public void setQtdeVoadeiras(Integer qtdeVoadeiras) {
		this.qtdeVoadeiras = qtdeVoadeiras;
	}

	public TipoPotenciaMotor getPotenciaMotorVoadeira() {
		return potenciaMotorVoadeira;
	}

	public void setPotenciaMotorVoadeira(TipoPotenciaMotor potenciaMotorVoadeira) {
		this.potenciaMotorVoadeira = potenciaMotorVoadeira;
	}

	public TipoFinanciadorComunitario getFinanciadorVoadeira() {
		return financiadorVoadeira;
	}

	public void setFinanciadorVoadeira(TipoFinanciadorComunitario financiadorVoadeira) {
		this.financiadorVoadeira = financiadorVoadeira;
	}

	public Boolean getPossuiOutroTipoTransporte() {
		return possuiOutroTipoTransporte;
	}

	public void setPossuiOutroTipoTransporte(Boolean possuiOutroTipoTransporte) {
		this.possuiOutroTipoTransporte = possuiOutroTipoTransporte;
	}

	public String getTipoTransporte() {
		return tipoTransporte;
	}

	public void setTipoTransporte(String tipoTransporte) {
		this.tipoTransporte = tipoTransporte;
	}

	public TipoPotenciaMotor getPotenciaMotorTransporte() {
		return potenciaMotorTransporte;
	}

	public void setPotenciaMotorTransporte(TipoPotenciaMotor potenciaMotorTransporte) {
		this.potenciaMotorTransporte = potenciaMotorTransporte;
	}

	public TipoFinanciadorComunitario getFinanciadorTransporte() {
		return financiadorTransporte;
	}

	public void setFinanciadorTransporte(TipoFinanciadorComunitario financiadorTransporte) {
		this.financiadorTransporte = financiadorTransporte;
	}

	public Boolean getPossuiEscola() {
		return possuiEscola;
	}

	public void setPossuiEscola(Boolean possuiEscola) {
		this.possuiEscola = possuiEscola;
	}

	public Boolean getAtendidaPorAgenteComunitarioDeSaude() {
		return atendidaPorAgenteComunitarioDeSaude;
	}

	public void setAtendidaPorAgenteComunitarioDeSaude(Boolean atendidaPorAgenteComunitarioDeSaude) {
		this.atendidaPorAgenteComunitarioDeSaude = atendidaPorAgenteComunitarioDeSaude;
	}

	public Boolean getAgenteResideNaComunidade() {
		return agenteResideNaComunidade;
	}

	public void setAgenteResideNaComunidade(Boolean agenteResideNaComunidade) {
		this.agenteResideNaComunidade = agenteResideNaComunidade;
	}

	public Comunidade getComunidadeAgente() {
		return comunidadeAgente;
	}

	public void setComunidadeAgente(Comunidade comunidadeAgente) {
		this.comunidadeAgente = comunidadeAgente;
	}

	public List<SintomasComunsFamiliar> getSintomasSaude() {
		return sintomasSaude;
	}

	public void setSintomasSaude(List<SintomasComunsFamiliar> sintomasSaude) {
		this.sintomasSaude = sintomasSaude;
	}

	public Boolean getAtendidaPorAmbulancha() {
		return atendidaPorAmbulancha;
	}

	public void setAtendidaPorAmbulancha(Boolean atendidaPorAmbulancha) {
		this.atendidaPorAmbulancha = atendidaPorAmbulancha;
	}

	public Boolean getAmbulanchaSediadaNaComunidade() {
		return ambulanchaSediadaNaComunidade;
	}

	public void setAmbulanchaSediadaNaComunidade(Boolean ambulanchaSediadaNaComunidade) {
		this.ambulanchaSediadaNaComunidade = ambulanchaSediadaNaComunidade;
	}

	public Boolean getAmbulanchaFunciona() {
		return ambulanchaFunciona;
	}

	public void setAmbulanchaFunciona(Boolean ambulanchaFunciona) {
		this.ambulanchaFunciona = ambulanchaFunciona;
	}

	public Boolean getAmbulanchaQuebrada() {
		return ambulanchaQuebrada;
	}

	public void setAmbulanchaQuebrada(Boolean ambulanchaQuebrada) {
		this.ambulanchaQuebrada = ambulanchaQuebrada;
	}

	public Boolean getAmbulanchaTemCaderneta() {
		return ambulanchaTemCaderneta;
	}

	public void setAmbulanchaTemCaderneta(Boolean ambulanchaTemCaderneta) {
		this.ambulanchaTemCaderneta = ambulanchaTemCaderneta;
	}

	public Boolean getPossuiPostoDeSaude() {
		return possuiPostoDeSaude;
	}

	public void setPossuiPostoDeSaude(Boolean possuiPostoDeSaude) {
		this.possuiPostoDeSaude = possuiPostoDeSaude;
	}

	public Comunidade getComunidadePostoSaude() {
		return comunidadePostoSaude;
	}

	public void setComunidadePostoSaude(Comunidade comunidadePostoSaude) {
		this.comunidadePostoSaude = comunidadePostoSaude;
	}

	public Boolean getPossuiFonteDeAguaColetivo() {
		return possuiFonteDeAguaColetivo;
	}

	public void setPossuiFonteDeAguaColetivo(Boolean possuiFonteDeAguaColetivo) {
		this.possuiFonteDeAguaColetivo = possuiFonteDeAguaColetivo;
	}

	public List<FonteAguaConsumoComunitario> getFontesAguaConsumo() {
		return fontesAguaConsumo;
	}

	public void setFontesAguaConsumo(List<FonteAguaConsumoComunitario> fontesAguaConsumo) {
		this.fontesAguaConsumo = fontesAguaConsumo;
	}

	public List<FormaPegarAguaComunitario> getFormasPegarAgua() {
		return formasPegarAgua;
	}

	public void setFormasPegarAgua(List<FormaPegarAguaComunitario> formasPegarAgua) {
		this.formasPegarAgua = formasPegarAgua;
	}

	public Boolean getExisteTratamentoAgua() {
		return existeTratamentoAgua;
	}

	public void setExisteTratamentoAgua(Boolean existeTratamentoAgua) {
		this.existeTratamentoAgua = existeTratamentoAgua;
	}

	public List<TratamentoAguaFamiliar> getTratamentosAgua() {
		return tratamentosAgua;
	}

	public void setTratamentosAgua(List<TratamentoAguaFamiliar> tratamentosAgua) {
		this.tratamentosAgua = tratamentosAgua;
	}

	public Integer getTempoMedioEnergiaDiaria() {
		return tempoMedioEnergiaDiaria;
	}

	public void setTempoMedioEnergiaDiaria(Integer tempoMedioEnergiaDiaria) {
		this.tempoMedioEnergiaDiaria = tempoMedioEnergiaDiaria;
	}

	public List<FonteEnergiaComunitario> getFontesEnergia() {
		return fontesEnergia;
	}

	public void setFontesEnergia(List<FonteEnergiaComunitario> fontesEnergia) {
		this.fontesEnergia = fontesEnergia;
	}

	public Boolean getPossuiMeiosDeComunicacao() {
		return possuiMeiosDeComunicacao;
	}

	public void setPossuiMeiosDeComunicacao(Boolean possuiMeiosDeComunicacao) {
		this.possuiMeiosDeComunicacao = possuiMeiosDeComunicacao;
	}

	public Boolean getMeiosFuncionam() {
		return meiosFuncionam;
	}

	public void setMeiosFuncionam(Boolean meiosFuncionam) {
		this.meiosFuncionam = meiosFuncionam;
	}

	public Boolean getTemRadioAMFM() {
		return temRadioAMFM;
	}

	public void setTemRadioAMFM(Boolean temRadioAMFM) {
		this.temRadioAMFM = temRadioAMFM;
	}

	public Boolean getPossuiProgramaRadioPreferido() {
		return possuiProgramaRadioPreferido;
	}

	public void setPossuiProgramaRadioPreferido(Boolean possuiProgramaRadioPreferido) {
		this.possuiProgramaRadioPreferido = possuiProgramaRadioPreferido;
	}

	public Boolean getCasasPossuiTV() {
		return casasPossuiTV;
	}

	public void setCasasPossuiTV(Boolean casasPossuiTV) {
		this.casasPossuiTV = casasPossuiTV;
	}

	public Boolean getGostariaReceberMateriasInfoPBF() {
		return gostariaReceberMateriasInfoPBF;
	}

	public void setGostariaReceberMateriasInfoPBF(Boolean gostariaReceberMateriasInfoPBF) {
		this.gostariaReceberMateriasInfoPBF = gostariaReceberMateriasInfoPBF;
	}

	public List<MeioComunicacaoComunitario> getMeiosComunicacao() {
		return meiosComunicacao;
	}

	public void setMeiosComunicacao(List<MeioComunicacaoComunitario> meiosComunicacao) {
		this.meiosComunicacao = meiosComunicacao;
	}

	public List<MaterialInformativoComunitario> getMateriaisInformativos() {
		return materiaisInformativos;
	}

	public void setMateriaisInformativos(List<MaterialInformativoComunitario> materiaisInformativos) {
		this.materiaisInformativos = materiaisInformativos;
	}

	public String getEmissorasRadioMaisEscuta() {
		return emissorasRadioMaisEscuta;
	}

	public void setEmissorasRadioMaisEscuta(String emissorasRadioMaisEscuta) {
		this.emissorasRadioMaisEscuta = emissorasRadioMaisEscuta;
	}

	public String getProgramaRadioPreferido() {
		return programaRadioPreferido;
	}

	public void setProgramaRadioPreferido(String programaRadioPreferido) {
		this.programaRadioPreferido = programaRadioPreferido;
	}

	public String getProgramaQueMaisAssistem() {
		return programaQueMaisAssistem;
	}

	public void setProgramaQueMaisAssistem(String programaQueMaisAssistem) {
		this.programaQueMaisAssistem = programaQueMaisAssistem;
	}

	public BigDecimal getRendaMedia() {
		return rendaMedia;
	}

	public void setRendaMedia(BigDecimal rendaMedia) {
		this.rendaMedia = rendaMedia;
	}

	public List<AtividadeRendaComunitario> getAtividadesRenda() {
		return atividadesRenda;
	}

	public void setAtividadesRenda(List<AtividadeRendaComunitario> atividadesRenda) {
		this.atividadesRenda = atividadesRenda;
	}

	public String getInformacoesGeraisSobreAComunidade() {
		return informacoesGeraisSobreAComunidade;
	}

	public void setInformacoesGeraisSobreAComunidade(String informacoesGeraisSobreAComunidade) {
		this.informacoesGeraisSobreAComunidade = informacoesGeraisSobreAComunidade;
	}

	public List<FinanciamentoVoadeiraComunitario> getFinanciamentosVoadeira() {
		return financiamentosVoadeira;
	}

	public void setFinanciamentosVoadeira(List<FinanciamentoVoadeiraComunitario> financiamentosVoadeira) {
		this.financiamentosVoadeira = financiamentosVoadeira;
	}

	public List<FinanciamentoTransporteComunitario> getFinanciamentosTransporte() {
		return financiamentosTransporte;
	}

	public void setFinanciamentosTransporte(List<FinanciamentoTransporteComunitario> financiamentosTransporte) {
		this.financiamentosTransporte = financiamentosTransporte;
	}

	public Comunidade getComunidade1a5Ano() {
		return comunidade1a5Ano;
	}

	public void setComunidade1a5Ano(Comunidade comunidade1a5Ano) {
		this.comunidade1a5Ano = comunidade1a5Ano;
	}

	public Comunidade getComunidade6a9Ano() {
		return comunidade6a9Ano;
	}

	public void setComunidade6a9Ano(Comunidade comunidade6a9Ano) {
		this.comunidade6a9Ano = comunidade6a9Ano;
	}

	public Comunidade getComunidadeEnsinoMedio() {
		return comunidadeEnsinoMedio;
	}

	public void setComunidadeEnsinoMedio(Comunidade comunidadeEnsinoMedio) {
		this.comunidadeEnsinoMedio = comunidadeEnsinoMedio;
	}

	public Integer getQtdeMoradoresFora1a5Ano() {
		return qtdeMoradoresFora1a5Ano;
	}

	public void setQtdeMoradoresFora1a5Ano(Integer qtdeMoradoresFora1a5Ano) {
		this.qtdeMoradoresFora1a5Ano = qtdeMoradoresFora1a5Ano;
	}

	public Integer getQtdeMoradoresFora6a9Ano() {
		return qtdeMoradoresFora6a9Ano;
	}

	public void setQtdeMoradoresFora6a9Ano(Integer qtdeMoradoresFora6a9Ano) {
		this.qtdeMoradoresFora6a9Ano = qtdeMoradoresFora6a9Ano;
	}

	public Integer getQtdeMoradoresForaEnsinoMedio() {
		return qtdeMoradoresForaEnsinoMedio;
	}

	public void setQtdeMoradoresForaEnsinoMedio(Integer qtdeMoradoresForaEnsinoMedio) {
		this.qtdeMoradoresForaEnsinoMedio = qtdeMoradoresForaEnsinoMedio;
	}

	public Integer getQtdeEscolas1a5Ano() {
		return qtdeEscolas1a5Ano;
	}

	public void setQtdeEscolas1a5Ano(Integer qtdeEscolas1a5Ano) {
		this.qtdeEscolas1a5Ano = qtdeEscolas1a5Ano;
	}

	public Integer getQtdeEscolas6a9Ano() {
		return qtdeEscolas6a9Ano;
	}

	public void setQtdeEscolas6a9Ano(Integer qtdeEscolas6a9Ano) {
		this.qtdeEscolas6a9Ano = qtdeEscolas6a9Ano;
	}

	public Integer getQtdeEscolasEnsinoMedio() {
		return qtdeEscolasEnsinoMedio;
	}

	public void setQtdeEscolasEnsinoMedio(Integer qtdeEscolasEnsinoMedio) {
		this.qtdeEscolasEnsinoMedio = qtdeEscolasEnsinoMedio;
	}

	public Integer getQtdeProfessores1a5Ano() {
		return qtdeProfessores1a5Ano;
	}

	public void setQtdeProfessores1a5Ano(Integer qtdeProfessores1a5Ano) {
		this.qtdeProfessores1a5Ano = qtdeProfessores1a5Ano;
	}

	public Integer getQtdeProfessores6a9Ano() {
		return qtdeProfessores6a9Ano;
	}

	public void setQtdeProfessores6a9Ano(Integer qtdeProfessores6a9Ano) {
		this.qtdeProfessores6a9Ano = qtdeProfessores6a9Ano;
	}

	public Integer getQtdeProfessoresEnsinoMedio() {
		return qtdeProfessoresEnsinoMedio;
	}

	public void setQtdeProfessoresEnsinoMedio(Integer qtdeProfessoresEnsinoMedio) {
		this.qtdeProfessoresEnsinoMedio = qtdeProfessoresEnsinoMedio;
	}

	public Integer getQtdeAlunos1a5Ano() {
		return qtdeAlunos1a5Ano;
	}

	public void setQtdeAlunos1a5Ano(Integer qtdeAlunos1a5Ano) {
		this.qtdeAlunos1a5Ano = qtdeAlunos1a5Ano;
	}

	public Integer getQtdeAlunos6a9Ano() {
		return qtdeAlunos6a9Ano;
	}

	public void setQtdeAlunos6a9Ano(Integer qtdeAlunos6a9Ano) {
		this.qtdeAlunos6a9Ano = qtdeAlunos6a9Ano;
	}

	public Integer getQtdeAlunosEnsinoMedio() {
		return qtdeAlunosEnsinoMedio;
	}

	public void setQtdeAlunosEnsinoMedio(Integer qtdeAlunosEnsinoMedio) {
		this.qtdeAlunosEnsinoMedio = qtdeAlunosEnsinoMedio;
	}

	public Localidade getMunicipioPostoSaude() {
		return municipioPostoSaude;
	}

	public void setMunicipioPostoSaude(Localidade municipioPostoSaude) {
		this.municipioPostoSaude = municipioPostoSaude;
	}

	
	
}
