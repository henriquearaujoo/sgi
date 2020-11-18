package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import util.Arquivo;

@Entity
@Table(name = "colaborador")
public class Colaborador extends Person implements Serializable {

	private static final long serialVersionUID = 1L;
	// TIPO DE PESSOA

	// **

	@Column(name = "data_admissao")
	@Temporal(TemporalType.DATE)
	private Date dataDeAdmissao;
	@Column(name = "cargo")
	private String cargo_string;
	@ManyToOne
	private Gestao gestao;
	@Column
	private String sexo;

	@ManyToOne
	private Cargo cargo;

	public Cargo getCargo() {
		return this.cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	@Column
	private Long aprovadorSub;

	@Column
	private Boolean ausente;

	@ManyToOne
	private Localidade lotacao;
	@Column(name = "controla_ponto")
	private Boolean controlaPonto;
	@Column(name = "ctps_serie_uf")
	private String ctpsSerieUf;
	@Column
	private String pis;
	@Column
	private String titulo;
	@Column(name = "ensino_fund_medio")
	private String ensinoFundamentalMedio;
	@Column
	private String graduacao;
	@Column(name = "pos_graduacao")
	private String posGraduacao;
	@Column(name = "quitacao_militar")
	private String quitacaoMilitar;
	@Column
	private String matricula;
	@ManyToOne
	private ContaBancaria conta;
	@ManyToOne
	private Arquivo arquivo;

	@ManyToOne
	private ProgamacaoViagem progamacaoViagem;

	public Colaborador() {
	}

	public Colaborador(Long id, String nome) {
		setId(id);
		setNome(nome);
	}

	public Colaborador(String nome) {
		setNome(nome);
	}

	public Colaborador(Long id, String nome, String rg, String pis, String email, String cpf) {
		setId(id);
		setNome(nome);
		setRg(rg);
		setPis(pis);
		setEmail(email);
		setCpf(cpf);
	}

	public RelatorioCampo getRelatorioCampo() {
		return relatorioCampo;
	}

	public void setRelatorioCampo(RelatorioCampo relatorioCampo) {
		this.relatorioCampo = relatorioCampo;
	}

	@ManyToOne
	private RelatorioCampo relatorioCampo;

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	/** 
	 **/

	public Date getDataDeAdmissao() {
		return dataDeAdmissao;
	}

	public void setDataDeAdmissao(Date dataDeAdmissao) {
		this.dataDeAdmissao = dataDeAdmissao;
	}

	public String getCargoString() {
		return cargo_string;
	}

	public void setCargo(String cargoString) {
		this.cargo_string = cargoString;
	}

	public Gestao getGestao() {
		return gestao;
	}

	public void setGestao(Gestao gestao) {
		this.gestao = gestao;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Localidade getLotacao() {
		return lotacao;
	}

	public void setLotacao(Localidade lotacao) {
		this.lotacao = lotacao;
	}

	public Boolean getControlaPonto() {
		return controlaPonto;
	}

	public void setControlaPonto(Boolean controlaPonto) {
		this.controlaPonto = controlaPonto;
	}

	public String getCtpsSerieUf() {
		return ctpsSerieUf;
	}

	public void setCtpsSerieUf(String ctpsSerieUf) {
		this.ctpsSerieUf = ctpsSerieUf;
	}

	public String getPis() {
		return pis;
	}

	public void setPis(String pis) {
		this.pis = pis;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getEnsinoFundamentalMedio() {
		return ensinoFundamentalMedio;
	}

	public void setEnsinoFundamentalMedio(String ensinoFundamentalMedio) {
		this.ensinoFundamentalMedio = ensinoFundamentalMedio;
	}

	public String getGraduacao() {
		return graduacao;
	}

	public void setGraduacao(String graduacao) {
		this.graduacao = graduacao;
	}

	public String getPosGraduacao() {
		return posGraduacao;
	}

	public void setPosGraduacao(String posGraduacao) {
		this.posGraduacao = posGraduacao;
	}

	public String getQuitacaoMilitar() {
		return quitacaoMilitar;
	}

	public void setQuitacaoMilitar(String quitacaoMilitar) {
		this.quitacaoMilitar = quitacaoMilitar;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public ContaBancaria getConta() {
		return conta;
	}

	public void setConta(ContaBancaria conta) {
		this.conta = conta;
	}

	public Long getAprovadorSub() {
		return aprovadorSub;
	}

	public void setAprovadorSub(Long aprovadorSub) {
		this.aprovadorSub = aprovadorSub;
	}

	public Boolean getAusente() {
		return ausente;
	}

	public void setAusente(Boolean ausente) {
		this.ausente = ausente;
	}

}