package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "conta_bancaria")
public class ContaBancaria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "numero_conta")
	private String numeroConta;

	@Column(name = "digito_conta")
	private String digitoConta;

	@Column(name = "numero_agencia")
	private String numeroAgencia;

	@Column(name = "digito_agencia")
	private String digitoAgencia;

	@Column(name = "nome_conta")
	private String nomeConta;

	@Column(name = "nome_banco")
	private String nomeBanco;

	@Column(name = "saldo_inicial", precision = 10, scale = 2)
	private BigDecimal saldoInicial;

	@Transient
	private BigDecimal saldoAtual;

	@Column
	private String tipo;

	@Column
	private String PIS;

	//PRINCIPAL, SECUNDARIA E FLEG
	@Column(name = "classificacao_conta")
	private String classificacaoConta;

	@Enumerated(EnumType.STRING)
	private StatusConta status;

	@ManyToOne
	private Fornecedor fornecedor;

	@ManyToOne
	private Colaborador colaborador;

	
	@Column
	private String cnpj;
	@Column
	private String cpf;
	@Column
	private String taxiid;
	@Column(name = "razao_social")
	private String razaoSocial;
	@Column
	private String tipoJuridico;
	@Column
	private String email;

	@Column
	private String cargo;
	// Gambiarra porque os stakeholder não podem mudar a forma de pensar.

	@ManyToOne
	private Banco banco;

	@Transient
	private List<RendimentoConta> rendimentos = new ArrayList<>();

	@Transient
	private List<FonteDoacaoConta> doacoes = new ArrayList<>();

	@Transient
	private List<AlocacaoRendimento> alocacoesRendimentos = new ArrayList<>();

	@Transient
	private BigDecimal totalEntrada = BigDecimal.ZERO;

	@Transient
	private BigDecimal totalSaida = BigDecimal.ZERO;

	public ContaBancaria(Long id, String nomeConta) {
		this.id = id;
		this.nomeConta = nomeConta;
	}

	public ContaBancaria(Long id) {
		this.id = id;
	}

	public ContaBancaria(Long id, String numeroConta, String numeroAgencia, String nomeConta, String nomeBanco,
			BigDecimal saldoInicial, String tipo, StatusConta status) {
		this.id = id;
		this.numeroConta = numeroConta;
		this.numeroAgencia = numeroAgencia;
		this.nomeConta = nomeConta;
		this.nomeBanco = nomeBanco;
		this.saldoInicial = saldoInicial;
		this.tipo = tipo;
		this.status = status;
	}

	public ContaBancaria() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumeroConta() {
		return numeroConta;
	}

	public void setNumeroConta(String numeroConta) {
		this.numeroConta = numeroConta;
	}

	public String getNumeroAgencia() {
		return numeroAgencia;
	}

	public void setNumeroAgencia(String numeroAgencia) {
		this.numeroAgencia = numeroAgencia;
	}

	public String getNomeConta() {
		return nomeConta;
	}

	public void setNomeConta(String nomeConta) {
		this.nomeConta = nomeConta;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
		ContaBancaria other = (ContaBancaria) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(BigDecimal saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	public BigDecimal getSaldoAtual() {
		return saldoAtual;
	}

	public void setSaldoAtual(BigDecimal saldoAtual) {
		this.saldoAtual = saldoAtual;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
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

	public String getTipoJuridico() {
		return tipoJuridico;
	}

	public void setTipoJuridico(String tipoJuridico) {
		this.tipoJuridico = tipoJuridico;
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

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getTaxiid() {
		return taxiid;
	}

	public void setTaxiid(String taxiid) {
		this.taxiid = taxiid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}


	public StatusConta getStatus() {
		return status;
	}

	public void setStatus(StatusConta status) {
		this.status = status;
	}

	public String getPIS() {
		return PIS;
	}

	public void setPIS(String pIS) {
		PIS = pIS;
	}

	public String getClassificacaoConta() {
		return classificacaoConta;
	}

	public void setClassificacaoConta(String classificacaoConta) {
		this.classificacaoConta = classificacaoConta;
	}

	public String getContaFornecedor(){
		if (getBanco() != null)
			return getBanco().getNomeBanco() + " " + getAgencia() + "/" + getConta();
		else
			return getAgencia() + "/" + getConta();
	}

	public String getDigitoConta() {
		return digitoConta;
	}

	public void setDigitoConta(String digitoConta) {
		this.digitoConta = digitoConta;
	}

	public String getDigitoAgencia() {
		return digitoAgencia;
	}

	public void setDigitoAgencia(String digitoAgencia) {
		this.digitoAgencia = digitoAgencia;
	}

	@Transient
	public String getAgencia(){
		if (numeroAgencia != null && digitoAgencia != null)
			return numeroAgencia + "-" + digitoAgencia;
		else
			return numeroAgencia;
	}

	@Transient
	public String getConta(){
		if (numeroConta != null && digitoConta != null )
			return numeroConta + "-" + digitoConta;
		else
			return numeroConta;
	}

	public List<RendimentoConta> getRendimentos() {
		return rendimentos;
	}

	public void setRendimentos(List<RendimentoConta> rendimentos) {
		this.rendimentos = rendimentos;
	}

	public List<AlocacaoRendimento> getAlocacoesRendimentos() {
		return alocacoesRendimentos;
	}

	public void setAlocacoesRendimentos(List<AlocacaoRendimento> alocacoesRendimentos) {
		this.alocacoesRendimentos = alocacoesRendimentos;
	}

	@Transient
	public String getTipoConta(){

		if (tipo != null){
			switch (tipo){
				case "CB":
					return "Corrente";
				case "CAP":
					return "Aplicação";
				case "CA":
					return "Adiantamento";
				case "CF":
					return "Conta fornecedor";
				case "RDT":
					return "Baixa de rendimento";
			}
		}

		return "";
	}

	public List<FonteDoacaoConta> getDoacoes() {
		return doacoes;
	}

	public void setDoacoes(List<FonteDoacaoConta> doacoes) {
		this.doacoes = doacoes;
	}
}
