package model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("uc")
public class UnidadeConservacao extends Localidade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Regional regional;

	@Column(name = "nome_associacao")
	private String nomeAssociacao;

	@Column(name = "presidente_associacao")
	private String presidenteAssociacao;

	@Column(name = "rg_presidente")
	private String rgPresidente;

	@Column(name = "cpf_presidente")
	private String cpfPresidente;

	@Column(name = "cnpj_associacao")
	private String cnpjassociacao;

	@Column(name = "profissao_presidente")
	private String profissaoPresidente;

	@Column(name = "endereco_presidente")
	private String enderecoPresidente;

	@Column(name = "endereco_associacao")
	private String enderecoAssociacao;

	@Column(name = "cep_associacao")
	private String cepAssociacao;

	@Column(name = "banco_associacao")
	private String bancoAssociacao;

	@Column(name = "agencia_associacao")
	private String agenciaAssociacao;

	@Column(name = "conta_associacao")
	private String contaAssociacao;

	@Column(name = "cargo_presidente")
	private String cargoPresidente;

	@Column(name = "telefone_associacao ")
	private String telefoneAssociacao;

	@Column(name = "telefone_presidente ")
	private String telefonePresidente;

	@Column(name = "cidade_presidente ")
	private String cidadePresidente;

	@Column(name = "cidade_associacao ")
	private String cidadeAssociacao;

	@ManyToOne
	private Municipio municipio;

	public UnidadeConservacao() {
	}

	public UnidadeConservacao(Long id, String nome) {
		setId(id);
		setNome(nome);
	}

	public UnidadeConservacao(Long id, String nome, String mascara) {
		setId(id);
		setNome(nome);
		setMascara(mascara);
	}

	public UnidadeConservacao(Long id, String nome, String mascara, Integer codigo) {
		setId(id);
		setNome(nome);
		setMascara(mascara);
		setCodigo(codigo);
	}

	public UnidadeConservacao(Long id, String nome, String mascara, Long regionalId, String regionalNome,
			String nomeAssociacao, String presidenteAssociacao) {
		setId(id);
		setNome(nome);
		setMascara(mascara);
		setNomeAssociacao(nomeAssociacao);
		setPresidenteAssociacao(presidenteAssociacao);
		setRegional(new Regional(regionalId, regionalNome, "reg"));
	}

	public UnidadeConservacao(String mascara) {
		setMascara(mascara);
	}

	public Regional getRegional() {
		return regional;
	}

	public void setRegional(Regional regional) {
		this.regional = regional;
	}

	public String getNomeAssociacao() {
		return nomeAssociacao;
	}

	public void setNomeAssociacao(String nomeAssociacao) {
		this.nomeAssociacao = nomeAssociacao;
	}

	public String getPresidenteAssociacao() {
		return presidenteAssociacao;
	}

	public void setPresidenteAssociacao(String presidenteAssociacao) {
		this.presidenteAssociacao = presidenteAssociacao;
	}

	public String getRgPresidente() {
		return rgPresidente;
	}

	public void setRgPresidente(String rgPresidente) {
		this.rgPresidente = rgPresidente;
	}

	public String getCpfPresidente() {
		return cpfPresidente;
	}

	public void setCpfPresidente(String cpfPresidente) {
		this.cpfPresidente = cpfPresidente;
	}

	public String getCnpjassociacao() {
		return cnpjassociacao;
	}

	public void setCnpjassociacao(String cnpjassociacao) {
		this.cnpjassociacao = cnpjassociacao;
	}

	public String getProfissaoPresidente() {
		return profissaoPresidente;
	}

	public void setProfissaoPresidente(String profissaoPresidente) {
		this.profissaoPresidente = profissaoPresidente;
	}

	public String getEnderecoAssociacao() {
		return enderecoAssociacao;
	}

	public void setEnderecoAssociacao(String enderecoAssociacao) {
		this.enderecoAssociacao = enderecoAssociacao;
	}

	public String getCepAssociacao() {
		return cepAssociacao;
	}

	public void setCepAssociacao(String cepAssociacao) {
		this.cepAssociacao = cepAssociacao;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public String getEnderecoPresidente() {
		return enderecoPresidente;
	}

	public void setEnderecoPresidente(String enderecoPresidente) {
		this.enderecoPresidente = enderecoPresidente;
	}

	public String getBancoAssociacao() {
		return bancoAssociacao;
	}

	public void setBancoAssociacao(String bancoAssociacao) {
		this.bancoAssociacao = bancoAssociacao;
	}

	public String getAgenciaAssociacao() {
		return agenciaAssociacao;
	}

	public void setAgenciaAssociacao(String agenciaAssociacao) {
		this.agenciaAssociacao = agenciaAssociacao;
	}

	public String getContaAssociacao() {
		return contaAssociacao;
	}

	public void setContaAssociacao(String contaAssociacao) {
		this.contaAssociacao = contaAssociacao;
	}

	public String getCargoPresidente() {
		return cargoPresidente;
	}

	public void setCargoPresidente(String cargoPresidente) {
		this.cargoPresidente = cargoPresidente;
	}

	public String getTelefoneAssociacao() {
		return telefoneAssociacao;
	}

	public void setTelefoneAssociacao(String telefoneAssociacao) {
		this.telefoneAssociacao = telefoneAssociacao;
	}

	public String getTelefonePresidente() {
		return telefonePresidente;
	}

	public void setTelefonePresidente(String telefonePresidente) {
		this.telefonePresidente = telefonePresidente;
	}

	public String getCidadePresidente() {
		return cidadePresidente;
	}

	public void setCidadePresidente(String cidadePresidente) {
		this.cidadePresidente = cidadePresidente;
	}

	public String getCidadeAssociacao() {
		return cidadeAssociacao;
	}

	public void setCidadeAssociacao(String cidadeAssociacao) {
		this.cidadeAssociacao = cidadeAssociacao;
	}

}
