package model;

import java.io.Serializable;
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

@Entity
@Table(name = "identificacao_familiar")
public class IdentificacaoFamiliar implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private TipoIdentificacaoFamiliar tipoIdentificacao;
	
	@Column
	private String nome;
	
	@Column
	private String apelido;
	
	@Column
	private String rg;
	
	@Column
	private String cpf;
	
	@ManyToOne
	private Localidade municipio;
	
	@Column
	private Boolean sempreMorouNesteLugar;
	
	@Column
	private Integer quantidadeAnosMoradia;
	
	@Column
	private String ultimoLugarMorou;
	
	@Enumerated(EnumType.STRING)
	private TipoParentescoFamiliar tipoParentesco;
	
	@Column
	private String outroParentesco;
	
	@Enumerated(EnumType.STRING)
	private TipoGeneroFamiliar tipoGenero;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataNascimento;
	
	@Column
	private String profissaoAtividade;
	
	@Enumerated(EnumType.STRING)
	private TipoEscolaridadeFamiliar tipoEscolaridade;
	
	@Enumerated(EnumType.STRING)
	private TipoAnoEscolaridadeFamiliar tipoAnoEscolaridade;
	
	@Column
	private Boolean atualmenteEstuda;
	
	@Column
	private Boolean temRG;
	
	@Column
	private Boolean temCPF;
	
	@Column
	private Boolean temCertidaoNascimento;
	
	@Column
	private Boolean temCertidaoCasamento;
	
	@Column
	private Boolean temTituloEleitor;
	
	@Column
	private Boolean temRegMilitar;
	
	@Column
	private Boolean temCarteiraTrabalho;
	
	@ManyToOne
	private Familiar familiar;
	
	public IdentificacaoFamiliar(){}

	public IdentificacaoFamiliar(Long id, String nome, TipoIdentificacaoFamiliar tipoIdentificacao) {
		this.id = id;
		this.nome = nome;
		this.tipoIdentificacao = tipoIdentificacao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoIdentificacaoFamiliar getTipoIdentificacao() {
		return tipoIdentificacao;
	}

	public void setTipoIdentificacao(TipoIdentificacaoFamiliar tipoIdentificacao) {
		this.tipoIdentificacao = tipoIdentificacao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Boolean getSempreMorouNesteLugar() {
		return sempreMorouNesteLugar;
	}

	public void setSempreMorouNesteLugar(Boolean sempreMorouNesteLugar) {
		this.sempreMorouNesteLugar = sempreMorouNesteLugar;
	}

	public Integer getQuantidadeAnosMoradia() {
		return quantidadeAnosMoradia;
	}

	public void setQuantidadeAnosMoradia(Integer quantidadeAnosMoradia) {
		this.quantidadeAnosMoradia = quantidadeAnosMoradia;
	}

	public String getUltimoLugarMorou() {
		return ultimoLugarMorou;
	}

	public void setUltimoLugarMorou(String ultimoLugarMorou) {
		this.ultimoLugarMorou = ultimoLugarMorou;
	}

	public TipoParentescoFamiliar getTipoParentesco() {
		return tipoParentesco;
	}

	public void setTipoParentesco(TipoParentescoFamiliar tipoParentesco) {
		this.tipoParentesco = tipoParentesco;
	}

	public TipoGeneroFamiliar getTipoGenero() {
		return tipoGenero;
	}

	public void setTipoGenero(TipoGeneroFamiliar tipoGenero) {
		this.tipoGenero = tipoGenero;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getProfissaoAtividade() {
		return profissaoAtividade;
	}

	public void setProfissaoAtividade(String profissaoAtividade) {
		this.profissaoAtividade = profissaoAtividade;
	}

	public TipoEscolaridadeFamiliar getTipoEscolaridade() {
		return tipoEscolaridade;
	}

	public void setTipoEscolaridade(TipoEscolaridadeFamiliar tipoEscolaridade) {
		this.tipoEscolaridade = tipoEscolaridade;
	}

	public TipoAnoEscolaridadeFamiliar getTipoAnoEscolaridade() {
		return tipoAnoEscolaridade;
	}

	public void setTipoAnoEscolaridade(TipoAnoEscolaridadeFamiliar tipoAnoEscolaridade) {
		this.tipoAnoEscolaridade = tipoAnoEscolaridade;
	}

	public Boolean getAtualmenteEstuda() {
		return atualmenteEstuda;
	}

	public void setAtualmenteEstuda(Boolean atualmenteEstuda) {
		this.atualmenteEstuda = atualmenteEstuda;
	}

	public Boolean getTemRG() {
		return temRG;
	}

	public void setTemRG(Boolean temRG) {
		this.temRG = temRG;
	}

	public Boolean getTemCPF() {
		return temCPF;
	}

	public void setTemCPF(Boolean temCPF) {
		this.temCPF = temCPF;
	}

	public Boolean getTemCertidaoNascimento() {
		return temCertidaoNascimento;
	}

	public void setTemCertidaoNascimento(Boolean temCertidaoNascimento) {
		this.temCertidaoNascimento = temCertidaoNascimento;
	}

	public Boolean getTemCertidaoCasamento() {
		return temCertidaoCasamento;
	}

	public void setTemCertidaoCasamento(Boolean temCertidaoCasamento) {
		this.temCertidaoCasamento = temCertidaoCasamento;
	}

	public Boolean getTemTituloEleitor() {
		return temTituloEleitor;
	}

	public void setTemTituloEleitor(Boolean temTituloEleitor) {
		this.temTituloEleitor = temTituloEleitor;
	}

	public Boolean getTemRegMilitar() {
		return temRegMilitar;
	}

	public void setTemRegMilitar(Boolean temRegMilitar) {
		this.temRegMilitar = temRegMilitar;
	}

	public Boolean getTemCarteiraTrabalho() {
		return temCarteiraTrabalho;
	}

	public void setTemCarteiraTrabalho(Boolean temCarteiraTrabalho) {
		this.temCarteiraTrabalho = temCarteiraTrabalho;
	}

	public Familiar getFamiliar() {
		return familiar;
	}

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	public String getOutroParentesco() {
		return outroParentesco;
	}

	public void setOutroParentesco(String outroParentesco) {
		this.outroParentesco = outroParentesco;
	}

	public Localidade getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Localidade municipio) {
		this.municipio = municipio;
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
		IdentificacaoFamiliar other = (IdentificacaoFamiliar) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
}
