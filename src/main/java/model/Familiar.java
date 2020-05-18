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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.sf.jasperreports.components.barcode4j.CodabarComponent;

@Entity
@Table(name = "familiar")
public class Familiar implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//Identificacao
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;
	
	@ManyToOne
	private UnidadeConservacao unidadeConservacao;
	
	@ManyToOne
	private Localidade municipio;
	
	@ManyToOne
	private Localidade polo;
	
	@ManyToOne
	private Localidade setor;
	
	@Enumerated(EnumType.STRING)
	private TipoRiosFamiliar rio;
	
	@Column
	private String lago;
	
	@Column
	private Boolean moradiaIsolada;
	
	@Column
	private String localidade;
	
	@ManyToOne
	private Comunidade comunidade;
	
	@Column
	private Integer numeroPontoGPS;
	
	@Column
	private String latitude;
	
	@Column
	private String longitude;
	
	@Column
	private Integer numeroMoradoresCasa;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataModificacaoMembros;

	@OneToMany(mappedBy = "familiar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<IdentificacaoFamiliar> identificacoes = new ArrayList<>();
	
	//Dinamica Populacional
	@OneToMany(mappedBy = "familiar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DinamicaPopulacionalFamiliar> dinamicas = new ArrayList<>();
	
	//Organização social
	@Column
	private Boolean ocupaCargoDirecao;
	
	@Column
	private String cargoDirecao;
	
	@OneToMany(mappedBy = "familiar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrganizacaoSocialFormalFamiliar> oraganizacoesFormais = new ArrayList<>();
	
	@OneToMany(mappedBy = "familiar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrganizacaoSocialInformalFamiliar> oraganizacoesInformais = new ArrayList<>();
	
	//Orientacao religiosa
	@OneToMany(mappedBy = "familiar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrientacaoReligiosaFamiliar> orientacoesReligiosas = new ArrayList<>();
	
	//Habitacao
	@Enumerated(EnumType.STRING)
	private TipoAmbienteFamiliar tipoAmbiente;
	
	@Enumerated(EnumType.STRING)
	private TipoHabitacaoFamiliar tipoHabitacao;
	
	@Column
	private String outroTipoHabitacao;
	
	@Enumerated(EnumType.STRING)
	private TipoEstruturaHabitacaoFamiliar tipoEstruturaHabitacao;
	
	@Enumerated(EnumType.STRING)
	private TipoPisoHabitacaoFamiliar tipoPisoHabitacao;
	
	@Enumerated(EnumType.STRING)
	private TipoMaterialHabitacaoFamiliar tipoMaterialHabitacao;
	
	@Column
	private String outroTipoMaterial;
	
	@Enumerated(EnumType.STRING)
	private TipoCoberturaHabitacaoFamiliar tipoCoberturaHabitacao;
	
	@Column
	private String outroTipoCobertura;
	
	@Column
	private Integer numeroComodos;
	
	@Column
	private Boolean possuiTelaMosquistos;
	
	//Energia e equipamentos
	@Enumerated(EnumType.STRING)
	private TipoIluminacaoFamiliar tipoIluminacao;
	
	@Column
	private String outroTipoIluminacao;
	
	@Column
	private Integer quantidadeLampadas;
	
	@Column
	private Boolean possuiPagamentoMensal;
	
	@Column
	private BigDecimal pagamentoMensalDinheiro;
	
	@Column
	private BigDecimal pagamentoMensalDiesel;
	
	@Column
	private BigDecimal quantidadeLitroDiesel;
	
	@Column
	private BigDecimal pagamentoMensalGasolina;
	
	@Column
	private BigDecimal quantidadeLitroGasolina;
	
	//Eletrodomesticos
	@Column
	private Boolean possuiRadio;
	
	@Column
	private String radioMaisOuvida;
	
	@Column
	private String programaMaisOuvido;
	
	@Column
	private Boolean possuiTelevisao;
	
	@Column
	private Boolean possuiAntenaParabolica;
	
	@Column
	private Boolean possuiSky;
	
	@Column
	private Boolean possuiFreezer;
	
	@Column
	private Boolean possuiGeladeira;

	@Column
	private Boolean possuiOutroEletrodomestico;

	@Column
	private String outrosEletrodomesticos;
	
	//Equipamentos e infraestrutura
	@OneToMany(mappedBy = "familiar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EquipamentoEInfraestruturaFamiliar> equipamentosInfraestrutura = new ArrayList<>();
	
	//Transporte
	@OneToMany(mappedBy = "familiar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TransporteFamiliar> transportes = new ArrayList<>();
	
	@Column
	private BigDecimal tamanhoTransporte;

	@Column
	private BigDecimal capacidade;
	
	//saneamento basico
	@OneToMany(mappedBy = "familiar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AbastecimentoAguaFamiliar> abastecimentosAgua = new ArrayList<>();
	
	@Enumerated(EnumType.STRING)
	private TipoAguaConsumoFamiliar tipoConsumoAgua;
	
	@OneToMany(mappedBy = "familiar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TratamentoAguaFamiliar> tratamentosAgua = new ArrayList<>();
	
	@Column
	private Boolean possuiCriterioUtilizacaoCloro;
	
	@Column
	private BigDecimal tamanhoRecipienteAgua;
	
	@Column
	private Integer totalGotasCloro;
	
	@Column
	private Boolean deixaAguaDescansar;
	
	@Column
	private BigDecimal tempoDescansoAgua;
	
	@Column
	private Boolean temSanitario;
	
	@Enumerated(EnumType.STRING)
	private TipoLocalSanitarioFamiliar tipoLocalSanitario;
	
	@OneToMany(mappedBy = "familiar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DestinoEsgotoFamiliar> destinosEsgoto = new ArrayList<>();
	
	@Enumerated(EnumType.STRING)
	private TipoTerrenoFamiliar tipoTerreno;
	
	@OneToMany(mappedBy = "familiar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DestinoLixoFamiliar> destinosLixo = new ArrayList<>();
	
	@Enumerated(EnumType.STRING)
	private TipoLocalFossaAbertaFamiliar tipoLocalFossaAberta;
	
	@Enumerated(EnumType.STRING)
	private TipoDestinoLixoFamiliar tipoDestinoLixo;
	
	//Saúde
	@OneToMany(mappedBy = "familiar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SintomasComunsFamiliar> sintomasSaude = new ArrayList<>(); 
	
	//Renda - Atividades economicas
	@Column
	private String trabalhoAssalariado;
	
	@Column
	private Boolean temTrabalhoAssalariado;
	
	@Column
	private Boolean carteiraDeTrabalhoAssinada;
	
	@OneToMany(mappedBy = "familiar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CadeiasProdutivasFamiliar> cadeiasProdutivas = new ArrayList<>(); 
	
	@Column
	private Boolean rendaMensal;
	
	@OneToMany(mappedBy = "familiar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProdutoAgriculturaFamiliar> produtosAgricultura = new ArrayList<>(); 
	
	@Column
	private Boolean possuiAssistenciaTecnica;
	
	@Column
	private String tipoATER;
	
	@OneToMany(mappedBy = "familiar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProdutoPescadoFamiliar> produtosPescado = new ArrayList<>(); 
	
	@Column
	private Boolean possuiPeixeProcessado;
	
	@Column
	private String tipoProcessamentoPescado;
	
	@Column
	private Boolean possuiCarteiraPescador;
	
	@Column
	private Boolean possuiContratoVendaPescado;
	
	@Enumerated(EnumType.STRING)
	private TipoConservamentoPescadoFamiliar tipoConservamentoPescado;
	
	@Column
	private Boolean evitaPescarAlgumaEpocaAno;
	
	@OneToMany(mappedBy = "familiar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CriacaoAnimalFamiliar> criacoesAnimais = new ArrayList<>();
	
	@Column
	private Boolean possuiAnimalBeneficiado;
	
	@Column
	private String tipoBeneficiamento;
	
	@Column
	private Boolean possuiAssistenciaTecnicaAnimal;
	
	@Column
	private String tipoAterAnimal;
	
	@OneToMany(mappedBy = "familiar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProdutoFlorestalMadeireiroFamiliar> produtosFlorestaisMadeireiros = new ArrayList<>();
	
	@OneToMany(mappedBy = "familiar", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProdutoFlorestalNaoMadeireiroFamiliar> produtosFlorestaisNaoMadeireiros = new ArrayList<>();
	
	@Column
	private String informacoesAdicionais;

	@Column
	private Boolean vendaEmConjunto;

	@Column
	private Double valorRendamensal;

	@Column
	private Boolean possuiVendaProdutosConjunto;

	@Column
	private String produtosVendaProdutosConjunto;
	
	//Protocolo
	@Column
	private String protocolo;
	
	@Column
	private String protolocoAFEAM;
	
	@Column
	private String contaBancaria;
	
	@Column
	private String nomeCartao;
	
	@Column
	private Boolean assinouAcordoCompromisso;
	
	@Column
	private Integer numeroDoAcordo;
	
	@Column
	private Boolean participacaoOficina;
	
	@Column
	private Boolean possuiCartao;
	
	@Enumerated(EnumType.STRING)
	private TipoStatusCartaoFamiliar statusCartao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEntregaCartao;
	
	@Enumerated(EnumType.STRING)
	private TipoConvenioFamiliar tipoConvenio;
	
	@Column
	private Boolean declaracaoMoradia;
	
	@Enumerated(EnumType.STRING)
	private TipoFinanceiramenteAtivoFamiliar financeiramenteAtivo;
	
	@Column
	private Boolean solicitarNovaViaCartao;
	
	@Column
	private Boolean solicitarAtualizacaoDados;
	
	@Enumerated(EnumType.STRING)
	private TipoIdentificacaoBancariaFamiliar identificacaoBancaria;
	
	@Column
	private BigDecimal creditoAdicional;
	
	@Enumerated(EnumType.STRING)
	private TipoDesbloqueioRecursoFamiliar tipoDesbloqueioRecurso;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataValidadeCartao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataDesligamento;
	
	@Enumerated(EnumType.STRING)
	private TipoDesligamentoFamiliar tipoDesligamento;
	
	@Column(columnDefinition = "TEXT")
	private String observacoes;

	public Familiar(){
		
	}

	public Familiar(Long id, String protocolo) {
		this.id = id;
		this.protocolo = protocolo;
	}
	
	public Familiar(Long id, String protocolo, String nomeComunidade) {
		this.id = id;
		this.protocolo = protocolo;
		this.comunidade = new Comunidade();
		this.comunidade.setNome(nomeComunidade);
	}
	
	@Transient
	public String getTitular(){
		
		if (getIdentificacoes() != null){
			for (IdentificacaoFamiliar identificacaoFamiliar : getIdentificacoes()) {
				if (identificacaoFamiliar.getTipoIdentificacao().toString() == TipoIdentificacaoFamiliar.TITULAR.toString())
					return identificacaoFamiliar.getNome();
			}
		}
		
		return null;
	}

	@Transient
	public String getConjuge(){
		
		if (getIdentificacoes() != null){
			for (IdentificacaoFamiliar identificacaoFamiliar : getIdentificacoes()) {
				if (identificacaoFamiliar.getTipoIdentificacao().toString() == TipoIdentificacaoFamiliar.CONJUGE.toString())
					return identificacaoFamiliar.getNome();
			}
		}
		
		return null;
	}
	
	@Transient
	public Integer getQtdeMembros(){
		
		if (identificacoes != null)
			return identificacoes.size();
		
		return 0;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public UnidadeConservacao getUnidadeConservacao() {
		return unidadeConservacao;
	}

	public void setUnidadeConservacao(UnidadeConservacao unidadeConservacao) {
		this.unidadeConservacao = unidadeConservacao;
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

	public Integer getNumeroMoradoresCasa() {
		return numeroMoradoresCasa;
	}

	public void setNumeroMoradoresCasa(Integer numeroMoradoresCasa) {
		this.numeroMoradoresCasa = numeroMoradoresCasa;
	}

	public List<IdentificacaoFamiliar> getIdentificacoes() {
		return identificacoes;
	}

	public void setIdentificacoes(List<IdentificacaoFamiliar> identificacoes) {
		this.identificacoes = identificacoes;
	}

	public List<DinamicaPopulacionalFamiliar> getDinamicas() {
		return dinamicas;
	}

	public void setDinamicas(List<DinamicaPopulacionalFamiliar> dinamicas) {
		this.dinamicas = dinamicas;
	}

	public Localidade getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Localidade municipio) {
		this.municipio = municipio;
	}

	public Boolean getOcupaCargoDirecao() {
		return ocupaCargoDirecao;
	}

	public void setOcupaCargoDirecao(Boolean ocupaCargoDirecao) {
		this.ocupaCargoDirecao = ocupaCargoDirecao;
	}

	public String getCargoDirecao() {
		return cargoDirecao;
	}

	public void setCargoDirecao(String cargoDirecao) {
		this.cargoDirecao = cargoDirecao;
	}

	public List<OrganizacaoSocialFormalFamiliar> getOraganizacoesFormais() {
		return oraganizacoesFormais;
	}

	public void setOraganizacoesFormais(List<OrganizacaoSocialFormalFamiliar> oraganizacoesFormais) {
		this.oraganizacoesFormais = oraganizacoesFormais;
	}

	public List<OrganizacaoSocialInformalFamiliar> getOraganizacoesInformais() {
		return oraganizacoesInformais;
	}

	public void setOraganizacoesInformais(List<OrganizacaoSocialInformalFamiliar> oraganizacoesInformais) {
		this.oraganizacoesInformais = oraganizacoesInformais;
	}

	public List<OrientacaoReligiosaFamiliar> getOrientacoesReligiosas() {
		return orientacoesReligiosas;
	}

	public void setOrientacoesReligiosas(List<OrientacaoReligiosaFamiliar> orientacoesReligiosas) {
		this.orientacoesReligiosas = orientacoesReligiosas;
	}

	public TipoAmbienteFamiliar getTipoAmbiente() {
		return tipoAmbiente;
	}

	public void setTipoAmbiente(TipoAmbienteFamiliar tipoAmbiente) {
		this.tipoAmbiente = tipoAmbiente;
	}

	public TipoHabitacaoFamiliar getTipoHabitacao() {
		return tipoHabitacao;
	}

	public void setTipoHabitacao(TipoHabitacaoFamiliar tipoHabitacao) {
		this.tipoHabitacao = tipoHabitacao;
	}

	public String getOutroTipoHabitacao() {
		return outroTipoHabitacao;
	}

	public void setOutroTipoHabitacao(String outroTipoHabitacao) {
		this.outroTipoHabitacao = outroTipoHabitacao;
	}

	public TipoEstruturaHabitacaoFamiliar getTipoEstruturaHabitacao() {
		return tipoEstruturaHabitacao;
	}

	public void setTipoEstruturaHabitacao(TipoEstruturaHabitacaoFamiliar tipoEstruturaHabitacao) {
		this.tipoEstruturaHabitacao = tipoEstruturaHabitacao;
	}

	public TipoPisoHabitacaoFamiliar getTipoPisoHabitacao() {
		return tipoPisoHabitacao;
	}

	public void setTipoPisoHabitacao(TipoPisoHabitacaoFamiliar tipoPisoHabitacao) {
		this.tipoPisoHabitacao = tipoPisoHabitacao;
	}

	public TipoMaterialHabitacaoFamiliar getTipoMaterialHabitacao() {
		return tipoMaterialHabitacao;
	}

	public void setTipoMaterialHabitacao(TipoMaterialHabitacaoFamiliar tipoMaterialHabitacao) {
		this.tipoMaterialHabitacao = tipoMaterialHabitacao;
	}

	public String getOutroTipoMaterial() {
		return outroTipoMaterial;
	}

	public void setOutroTipoMaterial(String outroTipoMaterial) {
		this.outroTipoMaterial = outroTipoMaterial;
	}

	public TipoCoberturaHabitacaoFamiliar getTipoCoberturaHabitacao() {
		return tipoCoberturaHabitacao;
	}

	public void setTipoCoberturaHabitacao(TipoCoberturaHabitacaoFamiliar tipoCoberturaHabitacao) {
		this.tipoCoberturaHabitacao = tipoCoberturaHabitacao;
	}

	public String getOutroTipoCobertura() {
		return outroTipoCobertura;
	}

	public void setOutroTipoCobertura(String outroTipoCobertura) {
		this.outroTipoCobertura = outroTipoCobertura;
	}

	public Integer getNumeroComodos() {
		return numeroComodos;
	}

	public void setNumeroComodos(Integer numeroComodos) {
		this.numeroComodos = numeroComodos;
	}

	public Boolean getPossuiTelaMosquistos() {
		return possuiTelaMosquistos;
	}

	public void setPossuiTelaMosquistos(Boolean possuiTelaMosquistos) {
		this.possuiTelaMosquistos = possuiTelaMosquistos;
	}

	public TipoIluminacaoFamiliar getTipoIluminacao() {
		return tipoIluminacao;
	}

	public void setTipoIluminacao(TipoIluminacaoFamiliar tipoIluminacao) {
		this.tipoIluminacao = tipoIluminacao;
	}

	public String getOutroTipoIluminacao() {
		return outroTipoIluminacao;
	}

	public void setOutroTipoIluminacao(String outroTipoIluminacao) {
		this.outroTipoIluminacao = outroTipoIluminacao;
	}

	public Integer getQuantidadeLampadas() {
		return quantidadeLampadas;
	}

	public void setQuantidadeLampadas(Integer quantidadeLampadas) {
		this.quantidadeLampadas = quantidadeLampadas;
	}

	public Boolean getPossuiPagamentoMensal() {
		return possuiPagamentoMensal;
	}

	public void setPossuiPagamentoMensal(Boolean possuiPagamentoMensal) {
		this.possuiPagamentoMensal = possuiPagamentoMensal;
	}

	public BigDecimal getPagamentoMensalDinheiro() {
		return pagamentoMensalDinheiro;
	}

	public void setPagamentoMensalDinheiro(BigDecimal pagamentoMensalDinheiro) {
		this.pagamentoMensalDinheiro = pagamentoMensalDinheiro;
	}

	public BigDecimal getPagamentoMensalDiesel() {
		return pagamentoMensalDiesel;
	}

	public void setPagamentoMensalDiesel(BigDecimal pagamentoMensalDiesel) {
		this.pagamentoMensalDiesel = pagamentoMensalDiesel;
	}

	public BigDecimal getQuantidadeLitroDiesel() {
		return quantidadeLitroDiesel;
	}

	public void setQuantidadeLitroDiesel(BigDecimal quantidadeLitroDiesel) {
		this.quantidadeLitroDiesel = quantidadeLitroDiesel;
	}

	public BigDecimal getPagamentoMensalGasolina() {
		return pagamentoMensalGasolina;
	}

	public void setPagamentoMensalGasolina(BigDecimal pagamentoMensalGasolina) {
		this.pagamentoMensalGasolina = pagamentoMensalGasolina;
	}

	public BigDecimal getQuantidadeLitroGasolina() {
		return quantidadeLitroGasolina;
	}

	public void setQuantidadeLitroGasolina(BigDecimal quantidadeLitroGasolina) {
		this.quantidadeLitroGasolina = quantidadeLitroGasolina;
	}

	public Boolean getPossuiRadio() {
		return possuiRadio;
	}

	public void setPossuiRadio(Boolean possuiRadio) {
		this.possuiRadio = possuiRadio;
	}

	public String getRadioMaisOuvida() {
		return radioMaisOuvida;
	}

	public void setRadioMaisOuvida(String radioMaisOuvida) {
		this.radioMaisOuvida = radioMaisOuvida;
	}

	public String getProgramaMaisOuvido() {
		return programaMaisOuvido;
	}

	public void setProgramaMaisOuvido(String programaMaisOuvido) {
		this.programaMaisOuvido = programaMaisOuvido;
	}

	public Boolean getPossuiTelevisao() {
		return possuiTelevisao;
	}

	public void setPossuiTelevisao(Boolean possuiTelevisao) {
		this.possuiTelevisao = possuiTelevisao;
	}

	public Boolean getPossuiAntenaParabolica() {
		return possuiAntenaParabolica;
	}

	public void setPossuiAntenaParabolica(Boolean possuiAntenaParabolica) {
		this.possuiAntenaParabolica = possuiAntenaParabolica;
	}

	public Boolean getPossuiSky() {
		return possuiSky;
	}

	public void setPossuiSky(Boolean possuiSky) {
		this.possuiSky = possuiSky;
	}

	public Boolean getPossuiFreezer() {
		return possuiFreezer;
	}

	public void setPossuiFreezer(Boolean possuiFreezer) {
		this.possuiFreezer = possuiFreezer;
	}

	public Boolean getPossuiGeladeira() {
		return possuiGeladeira;
	}

	public void setPossuiGeladeira(Boolean possuiGeladeira) {
		this.possuiGeladeira = possuiGeladeira;
	}

	public String getOutrosEletrodomesticos() {
		return outrosEletrodomesticos;
	}

	public void setOutrosEletrodomesticos(String outrosEletrodomesticos) {
		this.outrosEletrodomesticos = outrosEletrodomesticos;
	}

	public Boolean getMoradiaIsolada() {
		return moradiaIsolada;
	}

	public void setMoradiaIsolada(Boolean moradiaIsolada) {
		this.moradiaIsolada = moradiaIsolada;
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

	public List<EquipamentoEInfraestruturaFamiliar> getEquipamentosInfraestrutura() {
		return equipamentosInfraestrutura;
	}

	public void setEquipamentosInfraestrutura(List<EquipamentoEInfraestruturaFamiliar> equipamentosInfraestrutura) {
		this.equipamentosInfraestrutura = equipamentosInfraestrutura;
	}

	public List<TransporteFamiliar> getTransportes() {
		return transportes;
	}

	public void setTransportes(List<TransporteFamiliar> transportes) {
		this.transportes = transportes;
	}

	public BigDecimal getTamanhoTransporte() {
		return tamanhoTransporte;
	}

	public void setTamanhoTransporte(BigDecimal tamanhoTransporte) {
		this.tamanhoTransporte = tamanhoTransporte;
	}

	public BigDecimal getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(BigDecimal capacidade) {
		this.capacidade = capacidade;
	}

	public List<AbastecimentoAguaFamiliar> getAbastecimentosAgua() {
		return abastecimentosAgua;
	}

	public void setAbastecimentosAgua(List<AbastecimentoAguaFamiliar> abastecimentosAgua) {
		this.abastecimentosAgua = abastecimentosAgua;
	}

	public TipoAguaConsumoFamiliar getTipoConsumoAgua() {
		return tipoConsumoAgua;
	}

	public void setTipoConsumoAgua(TipoAguaConsumoFamiliar tipoConsumoAgua) {
		this.tipoConsumoAgua = tipoConsumoAgua;
	}

	public List<TratamentoAguaFamiliar> getTratamentosAgua() {
		return tratamentosAgua;
	}

	public void setTratamentosAgua(List<TratamentoAguaFamiliar> tratamentosAgua) {
		this.tratamentosAgua = tratamentosAgua;
	}

	public Boolean getPossuiCriterioUtilizacaoCloro() {
		return possuiCriterioUtilizacaoCloro;
	}

	public void setPossuiCriterioUtilizacaoCloro(Boolean possuiCriterioUtilizacaoCloro) {
		this.possuiCriterioUtilizacaoCloro = possuiCriterioUtilizacaoCloro;
	}

	public BigDecimal getTamanhoRecipienteAgua() {
		return tamanhoRecipienteAgua;
	}

	public void setTamanhoRecipienteAgua(BigDecimal tamanhoRecipienteAgua) {
		this.tamanhoRecipienteAgua = tamanhoRecipienteAgua;
	}

	public Integer getTotalGotasCloro() {
		return totalGotasCloro;
	}

	public void setTotalGotasCloro(Integer totalGotasCloro) {
		this.totalGotasCloro = totalGotasCloro;
	}

	public Boolean getDeixaAguaDescansar() {
		return deixaAguaDescansar;
	}

	public void setDeixaAguaDescansar(Boolean deixaAguaDescansar) {
		this.deixaAguaDescansar = deixaAguaDescansar;
	}

	public BigDecimal getTempoDescansoAgua() {
		return tempoDescansoAgua;
	}

	public void setTempoDescansoAgua(BigDecimal tempoDescansoAgua) {
		this.tempoDescansoAgua = tempoDescansoAgua;
	}

	public Boolean getTemSanitario() {
		return temSanitario;
	}

	public void setTemSanitario(Boolean temSanitario) {
		this.temSanitario = temSanitario;
	}

	public TipoLocalSanitarioFamiliar getTipoLocalSanitario() {
		return tipoLocalSanitario;
	}

	public void setTipoLocalSanitario(TipoLocalSanitarioFamiliar tipoLocalSanitario) {
		this.tipoLocalSanitario = tipoLocalSanitario;
	}

	public List<DestinoEsgotoFamiliar> getDestinosEsgoto() {
		return destinosEsgoto;
	}

	public void setDestinosEsgoto(List<DestinoEsgotoFamiliar> destinosEsgoto) {
		this.destinosEsgoto = destinosEsgoto;
	}

	public TipoLocalFossaAbertaFamiliar getTipoLocalFossaAberta() {
		return tipoLocalFossaAberta;
	}

	public void setTipoLocalFossaAberta(TipoLocalFossaAbertaFamiliar tipoLocalFossaAberta) {
		this.tipoLocalFossaAberta = tipoLocalFossaAberta;
	}

	public TipoDestinoLixoFamiliar getTipoDestinoLixo() {
		return tipoDestinoLixo;
	}

	public void setTipoDestinoLixo(TipoDestinoLixoFamiliar tipoDestinoLixo) {
		this.tipoDestinoLixo = tipoDestinoLixo;
	}

	public List<SintomasComunsFamiliar> getSintomasSaude() {
		return sintomasSaude;
	}

	public void setSintomasSaude(List<SintomasComunsFamiliar> sintomasSaude) {
		this.sintomasSaude = sintomasSaude;
	}

	public String getTrabalhoAssalariado() {
		return trabalhoAssalariado;
	}

	public void setTrabalhoAssalariado(String trabalhoAssalariado) {
		this.trabalhoAssalariado = trabalhoAssalariado;
	}

	public Boolean getCarteiraDeTrabalhoAssinada() {
		return carteiraDeTrabalhoAssinada;
	}

	public void setCarteiraDeTrabalhoAssinada(Boolean carteiraDeTrabalhoAssinada) {
		this.carteiraDeTrabalhoAssinada = carteiraDeTrabalhoAssinada;
	}

	public List<CadeiasProdutivasFamiliar> getCadeiasProdutivas() {
		return cadeiasProdutivas;
	}

	public void setCadeiasProdutivas(List<CadeiasProdutivasFamiliar> cadeiasProdutivas) {
		this.cadeiasProdutivas = cadeiasProdutivas;
	}

	public Boolean getRendaMensal() {
		return rendaMensal;
	}

	public void setRendaMensal(Boolean rendaMensal) {
		this.rendaMensal = rendaMensal;
	}

	public List<ProdutoAgriculturaFamiliar> getProdutosAgricultura() {
		return produtosAgricultura;
	}

	public void setProdutosAgricultura(List<ProdutoAgriculturaFamiliar> produtosAgricultura) {
		this.produtosAgricultura = produtosAgricultura;
	}

	public Boolean getPossuiAssistenciaTecnica() {
		return possuiAssistenciaTecnica;
	}

	public void setPossuiAssistenciaTecnica(Boolean possuiAssistenciaTecnica) {
		this.possuiAssistenciaTecnica = possuiAssistenciaTecnica;
	}

	public String getTipoATER() {
		return tipoATER;
	}

	public void setTipoATER(String tipoATER) {
		this.tipoATER = tipoATER;
	}

	public List<ProdutoPescadoFamiliar> getProdutosPescado() {
		return produtosPescado;
	}

	public void setProdutosPescado(List<ProdutoPescadoFamiliar> produtosPescado) {
		this.produtosPescado = produtosPescado;
	}

	public Boolean getPossuiPeixeProcessado() {
		return possuiPeixeProcessado;
	}

	public void setPossuiPeixeProcessado(Boolean possuiPeixeProcessado) {
		this.possuiPeixeProcessado = possuiPeixeProcessado;
	}

	public String getTipoProcessamentoPescado() {
		return tipoProcessamentoPescado;
	}

	public void setTipoProcessamentoPescado(String tipoProcessamentoPescado) {
		this.tipoProcessamentoPescado = tipoProcessamentoPescado;
	}

	public Boolean getPossuiCarteiraPescador() {
		return possuiCarteiraPescador;
	}

	public void setPossuiCarteiraPescador(Boolean possuiCarteiraPescador) {
		this.possuiCarteiraPescador = possuiCarteiraPescador;
	}

	public Boolean getPossuiContratoVendaPescado() {
		return possuiContratoVendaPescado;
	}

	public void setPossuiContratoVendaPescado(Boolean possuiContratoVendaPescado) {
		this.possuiContratoVendaPescado = possuiContratoVendaPescado;
	}

	public TipoConservamentoPescadoFamiliar getTipoConservamentoPescado() {
		return tipoConservamentoPescado;
	}

	public void setTipoConservamentoPescado(TipoConservamentoPescadoFamiliar tipoConservamentoPescado) {
		this.tipoConservamentoPescado = tipoConservamentoPescado;
	}

	public List<CriacaoAnimalFamiliar> getCriacoesAnimais() {
		return criacoesAnimais;
	}

	public void setCriacoesAnimais(List<CriacaoAnimalFamiliar> criacoesAnimais) {
		this.criacoesAnimais = criacoesAnimais;
	}

	public Boolean getPossuiAnimalBeneficiado() {
		return possuiAnimalBeneficiado;
	}

	public void setPossuiAnimalBeneficiado(Boolean possuiAnimalBeneficiado) {
		this.possuiAnimalBeneficiado = possuiAnimalBeneficiado;
	}

	public String getTipoBeneficiamento() {
		return tipoBeneficiamento;
	}

	public void setTipoBeneficiamento(String tipoBeneficiamento) {
		this.tipoBeneficiamento = tipoBeneficiamento;
	}

	public Boolean getPossuiAssistenciaTecnicaAnimal() {
		return possuiAssistenciaTecnicaAnimal;
	}

	public void setPossuiAssistenciaTecnicaAnimal(Boolean possuiAssistenciaTecnicaAnimal) {
		this.possuiAssistenciaTecnicaAnimal = possuiAssistenciaTecnicaAnimal;
	}

	public String getTipoAterAnimal() {
		return tipoAterAnimal;
	}

	public void setTipoAterAnimal(String tipoAterAnimal) {
		this.tipoAterAnimal = tipoAterAnimal;
	}

	public List<ProdutoFlorestalMadeireiroFamiliar> getProdutosFlorestaisMadeireiros() {
		return produtosFlorestaisMadeireiros;
	}

	public void setProdutosFlorestaisMadeireiros(List<ProdutoFlorestalMadeireiroFamiliar> produtosFlorestaisMadeireiros) {
		this.produtosFlorestaisMadeireiros = produtosFlorestaisMadeireiros;
	}

	public List<ProdutoFlorestalNaoMadeireiroFamiliar> getProdutosFlorestaisNaoMadeireiros() {
		return produtosFlorestaisNaoMadeireiros;
	}

	public void setProdutosFlorestaisNaoMadeireiros(
			List<ProdutoFlorestalNaoMadeireiroFamiliar> produtosFlorestaisNaoMadeireiros) {
		this.produtosFlorestaisNaoMadeireiros = produtosFlorestaisNaoMadeireiros;
	}

	public String getInformacoesAdicionais() {
		return informacoesAdicionais;
	}

	public void setInformacoesAdicionais(String informacoesAdicionais) {
		this.informacoesAdicionais = informacoesAdicionais;
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

	public Boolean getEvitaPescarAlgumaEpocaAno() {
		return evitaPescarAlgumaEpocaAno;
	}

	public void setEvitaPescarAlgumaEpocaAno(Boolean evitaPescarAlgumaEpocaAno) {
		this.evitaPescarAlgumaEpocaAno = evitaPescarAlgumaEpocaAno;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public String getProtolocoAFEAM() {
		return protolocoAFEAM;
	}

	public void setProtolocoAFEAM(String protolocoAFEAM) {
		this.protolocoAFEAM = protolocoAFEAM;
	}

	public String getContaBancaria() {
		return contaBancaria;
	}

	public void setContaBancaria(String contaBancaria) {
		this.contaBancaria = contaBancaria;
	}

	public Boolean getAssinouAcordoCompromisso() {
		return assinouAcordoCompromisso;
	}

	public void setAssinouAcordoCompromisso(Boolean assinouAcordoCompromisso) {
		this.assinouAcordoCompromisso = assinouAcordoCompromisso;
	}

	public Integer getNumeroDoAcordo() {
		return numeroDoAcordo;
	}

	public void setNumeroDoAcordo(Integer numeroDoAcordo) {
		this.numeroDoAcordo = numeroDoAcordo;
	}

	public Boolean getParticipacaoOficina() {
		return participacaoOficina;
	}

	public void setParticipacaoOficina(Boolean participacaoOficina) {
		this.participacaoOficina = participacaoOficina;
	}

	public Boolean getPossuiCartao() {
		return possuiCartao;
	}

	public void setPossuiCartao(Boolean possuiCartao) {
		this.possuiCartao = possuiCartao;
	}

	public Date getDataEntregaCartao() {
		return dataEntregaCartao;
	}

	public void setDataEntregaCartao(Date dataEntregaCartao) {
		this.dataEntregaCartao = dataEntregaCartao;
	}

	public Boolean getDeclaracaoMoradia() {
		return declaracaoMoradia;
	}

	public void setDeclaracaoMoradia(Boolean declaracaoMoradia) {
		this.declaracaoMoradia = declaracaoMoradia;
	}

	public TipoFinanceiramenteAtivoFamiliar getFinanceiramenteAtivo() {
		return financeiramenteAtivo;
	}

	public void setFinanceiramenteAtivo(TipoFinanceiramenteAtivoFamiliar financeiramenteAtivo) {
		this.financeiramenteAtivo = financeiramenteAtivo;
	}

	public Boolean getSolicitarNovaViaCartao() {
		return solicitarNovaViaCartao;
	}

	public void setSolicitarNovaViaCartao(Boolean solicitarNovaViaCartao) {
		this.solicitarNovaViaCartao = solicitarNovaViaCartao;
	}

	public Boolean getSolicitarAtualizacaoDados() {
		return solicitarAtualizacaoDados;
	}

	public void setSolicitarAtualizacaoDados(Boolean solicitarAtualizacaoDados) {
		this.solicitarAtualizacaoDados = solicitarAtualizacaoDados;
	}

	public BigDecimal getCreditoAdicional() {
		return creditoAdicional;
	}

	public void setCreditoAdicional(BigDecimal creditoAdicional) {
		this.creditoAdicional = creditoAdicional;
	}

	public Date getDataValidadeCartao() {
		return dataValidadeCartao;
	}

	public void setDataValidadeCartao(Date dataValidadeCartao) {
		this.dataValidadeCartao = dataValidadeCartao;
	}

	public TipoStatusCartaoFamiliar getStatusCartao() {
		return statusCartao;
	}

	public void setStatusCartao(TipoStatusCartaoFamiliar statusCartao) {
		this.statusCartao = statusCartao;
	}

	public TipoConvenioFamiliar getTipoConvenio() {
		return tipoConvenio;
	}

	public void setTipoConvenio(TipoConvenioFamiliar tipoConvenio) {
		this.tipoConvenio = tipoConvenio;
	}

	public TipoIdentificacaoBancariaFamiliar getIdentificacaoBancaria() {
		return identificacaoBancaria;
	}

	public void setIdentificacaoBancaria(TipoIdentificacaoBancariaFamiliar identificacaoBancaria) {
		this.identificacaoBancaria = identificacaoBancaria;
	}

	public Date getDataDesligamento() {
		return dataDesligamento;
	}

	public void setDataDesligamento(Date dataDesligamento) {
		this.dataDesligamento = dataDesligamento;
	}

	public TipoDesligamentoFamiliar getTipoDesligamento() {
		return tipoDesligamento;
	}

	public void setTipoDesligamento(TipoDesligamentoFamiliar tipoDesligamento) {
		this.tipoDesligamento = tipoDesligamento;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public Boolean getTemTrabalhoAssalariado() {
		return temTrabalhoAssalariado;
	}

	public void setTemTrabalhoAssalariado(Boolean temTrabalhoAssalariado) {
		this.temTrabalhoAssalariado = temTrabalhoAssalariado;
	}

	public Date getDataModificacaoMembros() {
		return dataModificacaoMembros;
	}

	public void setDataModificacaoMembros(Date dataModificacaoMembros) {
		this.dataModificacaoMembros = dataModificacaoMembros;
	}

	public List<DestinoLixoFamiliar> getDestinosLixo() {
		return destinosLixo;
	}

	public void setDestinosLixo(List<DestinoLixoFamiliar> destinosLixo) {
		this.destinosLixo = destinosLixo;
	}

	public TipoTerrenoFamiliar getTipoTerreno() {
		return tipoTerreno;
	}

	public void setTipoTerreno(TipoTerrenoFamiliar tipoTerreno) {
		this.tipoTerreno = tipoTerreno;
	}

	public String getNomeCartao() {
		return nomeCartao;
	}

	public void setNomeCartao(String nomeCartao) {
		this.nomeCartao = nomeCartao;
	}

	public TipoDesbloqueioRecursoFamiliar getTipoDesbloqueioRecurso() {
		return tipoDesbloqueioRecurso;
	}

	public void setTipoDesbloqueioRecurso(TipoDesbloqueioRecursoFamiliar tipoDesbloqueioRecurso) {
		this.tipoDesbloqueioRecurso = tipoDesbloqueioRecurso;
	}

	public Boolean getVendaEmConjunto() {
		return vendaEmConjunto;
	}

	public void setVendaEmConjunto(Boolean vendaEmConjunto) {
		this.vendaEmConjunto = vendaEmConjunto;
	}

	public Double getValorRendamensal() {
		return valorRendamensal;
	}

	public void setValorRendamensal(Double valorRendamensal) {
		this.valorRendamensal = valorRendamensal;
	}

	public Boolean getPossuiVendaProdutosConjunto() {
		return possuiVendaProdutosConjunto;
	}

	public void setPossuiVendaProdutosConjunto(Boolean possuiVendaProdutosConjunto) {
		this.possuiVendaProdutosConjunto = possuiVendaProdutosConjunto;
	}

	public String getProdutosVendaProdutosConjunto() {
		return produtosVendaProdutosConjunto;
	}

	public void setProdutosVendaProdutosConjunto(String produtosVendaProdutosConjunto) {
		this.produtosVendaProdutosConjunto = produtosVendaProdutosConjunto;
	}

	public Boolean getPossuiOutroEletrodomestico() {
		return possuiOutroEletrodomestico;
	}

	public void setPossuiOutroEletrodomestico(Boolean possuiOutroEletrodomestico) {
		this.possuiOutroEletrodomestico = possuiOutroEletrodomestico;
	}
}
