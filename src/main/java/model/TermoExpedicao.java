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


@Entity
@Table(name = "termo_expedicao")
public class TermoExpedicao implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition = "TEXT")
	private String descricao;
	
	@Enumerated(EnumType.ORDINAL)
	private TipoTermoExpedicao tipo;
	
	@OneToMany(mappedBy = "termo", cascade = CascadeType.ALL, orphanRemoval = false)
	private List<Pedido>  pedidos = new ArrayList<>();
	
	@Column(name = "valor_total_sem_desconto", precision = 10, scale = 2)
	private BigDecimal valor;
	
	@Enumerated(EnumType.STRING) 
	private TipoLocalidade tipoLocalidade;
	
	@ManyToOne
	private Localidade localidade;

	@Enumerated(EnumType.STRING)
	private TipoAprovador aprovador;

	@Column(columnDefinition = "TEXT")
	private String observacao;
	
	
	//Assinado / Não Assinado
	@Column(name = "status_termo")
	private String statusTermo;
	
	@Column(name = "data_envio")
	@Temporal(TemporalType.DATE)
	private Date dataEnvio;
	
	@Column(name = "data_emissao")
	@Temporal(TemporalType.DATE)
	private Date dataEmissao;
	
	
	@Column(name = "ano_referencia")
	private String anoReferencia;
	
	
	public TermoExpedicao(){}

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

	public TipoTermoExpedicao getTipo() {
		return tipo;
	}

	public void setTipo(TipoTermoExpedicao tipo) {
		this.tipo = tipo;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
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

	public TipoAprovador getAprovador() {
		return aprovador;
	}

	public void setAprovador(TipoAprovador aprovador) {
		this.aprovador = aprovador;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public String getStatusTermo() {
		return statusTermo;
	}

	public void setStatusTermo(String statusTermo) {
		this.statusTermo = statusTermo;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public String getAnoReferencia() {
		return anoReferencia;
	}

	public void setAnoReferencia(String anoReferencia) {
		this.anoReferencia = anoReferencia;
	}
	
	
	
	
	
}
