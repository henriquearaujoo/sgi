package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "SolicitacaoViagem")
public class SolicitacaoViagem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String codigo;
	
	@Column 
	private String missao;
	
	@Column(name = "data_ida")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataIda;
	
	@Column(name = "data_volta")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataVolta;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAprovacao;
	
	@Column(name = "data_emissao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEmissao;
	
	@Column(columnDefinition = "TEXT")
	private String observacao;

	@Column(columnDefinition = "TEXT")
	private String motivo;

	//Variavel temporaria
	@Column
	private String fonteRecuroTemp;
	
	//Relacionamento de classes
	@Enumerated(EnumType.STRING)
	private TipoGestao tipoGestao;
	
	@ManyToOne
	private Gestao gestao;

	@Enumerated(EnumType.STRING) 
	private TipoLocalidade tipoLocalidade;
	
	@ManyToOne
	private Localidade localidade;
	
	
	@ManyToOne
	private Colaborador solicitante;
	
	@ManyToOne
	private Colaborador aprovador;

	@Enumerated(EnumType.STRING)
	private Componente componente;
	
	@Enumerated(EnumType.STRING)
	private Destino destino;
	
	@ManyToOne
	private TipoViagem tipoViagem;

	@ManyToOne
	private Veiculo veiculoIda;
	
	@ManyToOne
	private Veiculo veiculoVolta;
	
	@Enumerated(EnumType.STRING)
	private StatusCompra statusViagem;
	
	@Column
	private String versionLancamento;

	//Relacionamentos de classe
	//Entidades fracas & listas
	@OneToMany(mappedBy="solicitacaoViagem", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProgamacaoViagem> progamacaoViagem = new ArrayList<ProgamacaoViagem>();
	
	
	@OneToMany(mappedBy="solicitacaoViagem", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DespesaViagem> despesaViagem = new ArrayList<DespesaViagem>();
	
	
	@OneToMany(mappedBy="solicitacaoViagem", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Diaria> diaria =  new ArrayList<Diaria>();
	
	
	public SolicitacaoViagem(Long id, String missao, String descricao, String solicitante,  Date data){
		this.id = id;
		this.missao = missao;
		this.motivo = descricao;
		this.solicitante = new Colaborador(solicitante);
		this.dataEmissao = data;
	}
	
	public SolicitacaoViagem(Long id, String missao, String descricao, String solicitante,  Date data,StatusCompra status){
		this.id = id;
		this.missao = missao;
		this.motivo = descricao;
		this.solicitante = new Colaborador(solicitante);
		this.dataEmissao = data;
		this.statusViagem = status;
	}
	
	
	public SolicitacaoViagem(){
		
	}
	
	public Date getDataAprovacao() {
		return dataAprovacao;
	}


	public void setDataAprovacao(Date dataAprovacao) {
		this.dataAprovacao = dataAprovacao;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoGestao getTipoGestao() {
		return tipoGestao;
	}

	public void setTipoGestao(TipoGestao tipoGestao) {
		this.tipoGestao = tipoGestao;
	}

	public Colaborador getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Colaborador solicitante) {
		this.solicitante = solicitante;
	}

	public Gestao getGestao() {
		return gestao;
	}

	public void setGestao(Gestao gestao) {
		this.gestao = gestao;
	}
	
	public List<Diaria> getDiaria() {
		return diaria;
	}


	public void setDiaria(List<Diaria> diaria) {
		this.diaria = diaria;
	}


	public List<DespesaViagem> getDespesaViagem() {
		return despesaViagem;
	}


	public void setDespesaViagem(List<DespesaViagem> despesaViagem) {
		this.despesaViagem = despesaViagem;
	}
	
	public TipoViagem getTipoViagem() {
		return tipoViagem;
	}


	public void setTipoViagem(TipoViagem tipoViagem) {
		this.tipoViagem = tipoViagem;
	}


	
	public Veiculo getVeiculoIda() {
		return veiculoIda;
	}


	public void setVeiculoIda(Veiculo veiculoIda) {
		this.veiculoIda = veiculoIda;
	}


	public Veiculo getVeiculoVolta() {
		return veiculoVolta;
	}


	public void setVeiculoVolta(Veiculo veiculoVolta) {
		this.veiculoVolta = veiculoVolta;
	}


	public String getMissao() {
		return missao;
	}


	public void setMissao(String missao) {
		this.missao = missao;
	}


	public StatusCompra getStatusViagem() {
		return statusViagem;
	}


	public void setStatusViagem(StatusCompra statusViagem) {
		this.statusViagem = statusViagem;
	}

	public TipoLocalidade getTipoLocalidade() {
		return tipoLocalidade;
	}

	public void setTipoLocalidade(TipoLocalidade tipoLocalidade) {
		this.tipoLocalidade = tipoLocalidade;
	}

	public Localidade getLocalidade() {
		return localidade;
	}

	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Date getDataIda() {
		return dataIda;
	}

	public void setDataIda(Date dataIda) {
		this.dataIda = dataIda;
	}

	public Date getDataVolta() {
		return dataVolta;
	}

	public void setDataVolta(Date dataVolta) {
		this.dataVolta = dataVolta;
	}
	
	public List<ProgamacaoViagem> getProgamacaoViagem() {
		return progamacaoViagem;
	}

	public void setProgamacaoViagem(List<ProgamacaoViagem> progamacaoViagem) {
		this.progamacaoViagem = progamacaoViagem;
	}
	
	public Componente getComponente() {
		return componente;
	}

	public void setComponente(Componente componente) {
		this.componente = componente;
	}

	public Destino getDestino() {
		return destino;
	}

	public void setDestino(Destino destino) {
		this.destino = destino;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
	public Colaborador getAprovador() {
		return aprovador;
	}

	public void setAprovador(Colaborador aprovador) {
		this.aprovador = aprovador;
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
		SolicitacaoViagem other = (SolicitacaoViagem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public String getFonteRecuroTemp() {
		return fonteRecuroTemp;
	}


	public void setFonteRecuroTemp(String fonteRecuroTemp) {
		this.fonteRecuroTemp = fonteRecuroTemp;
	}


	public String getVersionLancamento() {
		return versionLancamento;
	}


	public void setVersionLancamento(String versionLancamento) {
		this.versionLancamento = versionLancamento;
	}
	
	
	
	
}
