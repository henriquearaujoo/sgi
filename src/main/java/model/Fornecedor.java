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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import util.ArquivoFornecedor;

@Entity
@Table(name = "fornecedor")
public class Fornecedor implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "razao_social")
	private String razaoSocial;

	@Column
	private String atividade;

	@Column
	private Boolean status;

	@Column
	private Boolean ativoReceita;

	@Transient
	private String logradouro;

	@Column(name="nome_fantasia")
	private String nomeFantasia;
	@Column
	private String endereco;
	@Column
	private String bairro;
	@ManyToOne
	private Localidade cidade;
	@ManyToOne
	private Estado estado;
	@Column
	private String cep;
	@Column
	private String cnpj;
	@Column
	private String cpf;
	@Column(name =  "inscricao_estadual")
	private String inscricaoEstadual;
	
	@Column(name = "inscricao_municipal")
	private String inscricaoMunicipal;

	@Column
	private String rg;
	@Column
	private String telefone;
	
	@Column
	private String telefoneSecundario;
	
	@Column
	private String numero;

	@Column
	private String pis;
	@Column
	private String email;
	
	@Column
	private String emailSecundario;
	
	@Column
	private String tipo;
	@Column
	private String banco;
	@Column
	private String agencia;
	@Column
	private String conta;
	@Column
	private String observacao;	
	@Column
	private String complemento;
	
	@Column
	private Boolean ativo;
	
	@OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ArquivoFornecedor> arquivos = new ArrayList<ArquivoFornecedor>();
	
	@Enumerated(EnumType.STRING)
	private StatusFornecedor statusFornecedor;
	
	@Column(name = "data_nascimento")
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	
	@Column(name = "tipo_pagamento")
	@Enumerated(EnumType.STRING)
	private TipoPagamento tipoPagamento;
	
	@Column
	private String contato;
	
	@Column
	private String txModify;
	
	@Column
	private Date dtModify;
	
	@Column
	private String cargo;
	
	@Enumerated(EnumType.STRING)
	private ClassificacaoFornecedor classificacao;
	
	public String getTxModify() {
		return txModify;
	}

	public void setTxModify(String txModify) {
		this.txModify = txModify;
	}

	public Date getDtModify() {
		return dtModify;
	}

	public void setDtModify(Date dtModify) {
		this.dtModify = dtModify;
	}

	@Column
	private String taxiid;

//	@ManyToOne(fetch = FetchType.LAZY)
//	private ContaBancaria contaBancaria;

	@OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ContaBancaria> contasBancarias = new ArrayList<>();


	@Column
	private  String categoria1;
	
	@ManyToOne
	private CategoriaDespesaClass categoriaFornecedor1;

//	@Column
//	private  String categoria2;
//
//	@ManyToOne
//	private CategoriaDespesaClass categoriaFornecedor2;
//
//	@Column
//	private  String categoria3;
//
//	@ManyToOne
//	private CategoriaDespesaClass categoriaFornecedor3;

	@Transient
	private BigDecimal totalCotacao = BigDecimal.ZERO;
	@Transient
	private Long idCompra;

	@Transient
	private List<Cotacao> cotacoes = new ArrayList<Cotacao>();
	@Transient
	private String cpfcnpj;
	@Transient
	private String tipoPessoa;
	@Transient
	private String nomeCidade;
	@Transient
	private String ultimoForn;
	@Transient
	private String estadoCidade;
	@Transient
	private String ativoString;
	@Transient
	private String tipoPagamentoString;
	@Transient
	private String categoria;
	
	
	public Fornecedor() {
		super();
	}

	public Fornecedor(Long id,String razaoSocial,String atividade,String nomeFantasia,String endereco,String bairro,String cep,String cnpj,String cpf,String inscricaoEstadual,String inscricaoMunicipal,String rg,String telefone,String pis,String email,String tipo,String observacao,Boolean ativo,String taxiid,Boolean status,Boolean ativoReceita){
		this.id = id;
		this.razaoSocial = razaoSocial;
		this.atividade = atividade;
		this.nomeFantasia = nomeFantasia;
		this.endereco = endereco;
		this.bairro = bairro;
		this.cep = cep;
		this.cnpj =cnpj;
		this.cpf = cpf;
		this.inscricaoEstadual = inscricaoEstadual;
		this.inscricaoMunicipal = inscricaoMunicipal;
		this.rg = rg;
		this.telefone = telefone;
		this.pis = pis;
		this.email = email;
		this.observacao = observacao;
		this.ativo = ativo;
		this.taxiid = taxiid;
		this.status = status;
		this.ativoReceita = ativoReceita;
		this.tipo = tipo;
		
	}
	
	
	public Fornecedor(Long id, String nomeFantasia){
		this.id = id;
		this.nomeFantasia = nomeFantasia;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}
	
	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}
	
	public String getAtividade() {
		return atividade;
	}

	public void setAtividade(String atividade) {
		this.atividade = atividade;
	}
	
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getAtivoReceita() {
		return ativoReceita;
	}

	public void setAtivoReceita(Boolean ativoReceita) {
		this.ativoReceita = ativoReceita;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public Localidade getCidade() {
		return cidade;
	}

	public void setCidade(Localidade cidade) {
		this.cidade = cidade;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}


	public String getPis() {
		return pis;
	}

	public void setPis(String pis) {
		this.pis = pis;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public StatusFornecedor getStatusFornecedor() {
		return statusFornecedor;
	}

	public void setStatusFornecedor(StatusFornecedor statusFornecedor) {
		this.statusFornecedor = statusFornecedor;
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
		Fornecedor other = (Fornecedor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public List<Cotacao> getCotacoes() {
		return cotacoes;
	}

	public void setCotacoes(List<Cotacao> cotacoes) {
		this.cotacoes = cotacoes;
	}

	public String getTaxiid() {
		return taxiid;
	}

	public void setTaxiid(String taxiid) {
		this.taxiid = taxiid;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public List<ContaBancaria> getContasBancarias() {
		return contasBancarias;
	}

	public void setContasBancarias(List<ContaBancaria> contasBancarias) {
		this.contasBancarias = contasBancarias;
	}

	public ClassificacaoFornecedor getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(ClassificacaoFornecedor classificacao) {
		this.classificacao = classificacao;
	}
	
	public String getTelefoneSecundario() {
		return telefoneSecundario;
	}

	public void setTelefoneSecundario(String telefoneSecundario) {
		this.telefoneSecundario = telefoneSecundario;
	}
	
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getEmailSecundario() {
		return emailSecundario;
	}

	public void setEmailSecundario(String emailSecundario) {
		this.emailSecundario = emailSecundario;
	}

	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public String getCpfcnpj() {
		return cpfcnpj;
	}

	public void setCpfcnpj(String cpfcnpj) {
		this.cpfcnpj = cpfcnpj;
	}

	public String getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getNomeCidade() {
		return nomeCidade;
	}

	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
	}

	public String getUltimoForn() {
		return ultimoForn;
	}

	public void setUltimoForn(String ultimoForn) {
		this.ultimoForn = ultimoForn;
	}

	public String getCategoria1() {
		return categoria1;
	}

	public void setCategoria1(String categoria1) {
		this.categoria1 = categoria1;
	}

	public CategoriaDespesaClass getCategoriaFornecedor1() {
		return categoriaFornecedor1;
	}

	public void setCategoriaFornecedor1(CategoriaDespesaClass categoriaFornecedor1) {
		this.categoriaFornecedor1 = categoriaFornecedor1;
	}
//
//	public String getCategoria2() {
//		return categoria2;
//	}
//
//	public void setCategoria2(String categoria2) {
//		this.categoria2 = categoria2;
//	}
//
//	public CategoriaDespesaClass getCategoriaFornecedor2() {
//		return categoriaFornecedor2;
//	}
//
//	public void setCategoriaFornecedor2(CategoriaDespesaClass categoriaFornecedor2) {
//		this.categoriaFornecedor2 = categoriaFornecedor2;
//	}
//
//	public String getCategoria3() {
//		return categoria3;
//	}
//
//	public void setCategoria3(String categoria3) {
//		this.categoria3 = categoria3;
//	}
//
//	public CategoriaDespesaClass getCategoriaFornecedor3() {
//		return categoriaFornecedor3;
//	}
//
//	public void setCategoriaFornecedor3(CategoriaDespesaClass categoriaFornecedor3) {
//		this.categoriaFornecedor3 = categoriaFornecedor3;
//	}

	public List<ArquivoFornecedor> getArquivos() {
		return arquivos;
	}

	public void setArquivos(List<ArquivoFornecedor> arquivos) {
		this.arquivos = arquivos;
	}

	public String getEstadoCidade() {
		return estadoCidade;
	}

	public void setEstadoCidade(String estadoCidade) {
		this.estadoCidade = estadoCidade;
	}

	public String getAtivoString() {
		return ativoString;
	}

	public void setAtivoString(String ativoString) {
		this.ativoString = ativoString;
	}

	public String getTipoPagamentoString() {
		return tipoPagamentoString;
	}

	public void setTipoPagamentoString(String tipoPagamentoString) {
		this.tipoPagamentoString = tipoPagamentoString;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
}
