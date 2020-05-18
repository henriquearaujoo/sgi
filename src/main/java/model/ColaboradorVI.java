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
@Table(name = "colaborador_vi")
public class ColaboradorVI extends Person implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name = "data_admissao")
	@Temporal(TemporalType.DATE)
	private Date dataDeAdmissao;
	
	@Column
	private String cargo;
	
	@Column(name = "nome_social")
	private String nomeSocial;
	
	@ManyToOne
	private Gestao gestao;
	
	@Column
	private String sexo;
	
	@ManyToOne
	private Localidade lotacao;
	
	@Column(name =  "controla_ponto")
	private Boolean controlaPonto;
	
	@Column(name = "ctps_serie_uf")
	private String ctpsSerieUf;
	
	@Column
	private String pis;
	
	@Column
	private String titulo;
	
	@Column(name =  "ensino_fund_medio")
	private String ensinoFundamentalMedio;
	
	@Column
	private String graduacao;
	
	@Column(name="pos_graduacao")
	private String posGraduacao;
	
	@Column(name = "quitacao_militar")
	private String quitacaoMilitar;
	
	@Column
	private String matricula;
	
	@Column
	private String skype;

	
	
	public ColaboradorVI(){}
	
	public ColaboradorVI(Long id, String nome){
		setId(id);
		setNome(nome);
	}
	
	public ColaboradorVI(String nome){
		setNome(nome);
	}
	
	public ColaboradorVI(Long id,String nome,String rg,String pis,String email,String cpf) {
		setId(id);
		setNome(nome);
		setRg(rg);
		setPis(pis);
		setEmail(email);
		setCpf(cpf);
	}
	

	public Date getDataDeAdmissao() {
		return dataDeAdmissao;
	}

	public void setDataDeAdmissao(Date dataDeAdmissao) {
		this.dataDeAdmissao = dataDeAdmissao;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
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

	public String getSkype() {
		return skype;
	}

	public void setSkype(String skype) {
		this.skype = skype;
	}

	public String getNomeSocial() {
		return nomeSocial;
	}

	public void setNomeSocial(String nomeSocial) {
		this.nomeSocial = nomeSocial;
	}


}