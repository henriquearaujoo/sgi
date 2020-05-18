package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Transient;

import service.ControleExpedicaoService;
import util.CDILocator;

@Entity
@Table(name = "controle_expedicao")
public class ControleExpedicao implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//Data da retirada no forncedor
	@Temporal(TemporalType.DATE)
	@Column(name = "entrada_mercadoria")
	private Date entradaMercadoria;
	//Data em que a logistica entrega ao solicitante
	@Temporal(TemporalType.DATE)
	@Column(name = "saida_mercadoria")
	private Date saidaMercadoria;
	//Data que o pedido foi entregue no destino pode coincidir com a data da saida.
	@Temporal(TemporalType.DATE)
	@Column(name = "data_recebimento")
	private Date dataRecebimento;
	@Column
	private String transporte;
	@Column(name = "valor_transporte", precision = 10, scale = 2)
	private BigDecimal valorTransporte;
	@Column(name = "protocolo_nf")
	private String protocoloNf;
	@Column(name = "nota_fiscal")
	private String notaFiscal;
	@ManyToOne
	private Pedido pedido;
	
	@Column
	private String nomeResponsavel;
	
	@ManyToOne
	private Gestao gestaoResponsavel;
	
	//Data da aceitacao da solicitação
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_aceitacao_expedicao")
	private Date dataAceitacaoExpedicao;
	
	@Enumerated(EnumType.ORDINAL)
	private LocalizacaoPedido localizacaoPedido;
	@Transient
	private Long idPedido;
	@Transient
	private Long idCompra;
	@Transient
	private TipoLocalidade tipoLocalidade;
	@Transient
	private String mascara;
	@Transient
	private String unidadeDeConservacao;
	@Transient
	private String nomeSolicitante;
	@Transient
	private BigDecimal valorTotalComDesconto;
	@Transient
	private String nomeFantasia;
	@Transient
	private Date dataEntrega;
	@Transient
	private String nomeGestaoResponsavel;
	@Transient
	private Long idLocalidade;
	@Transient
	private Long idTermo;
	
	
	
	
	public ControleExpedicao(){}
	
	
	@Transient
	private ControleExpedicaoService service = CDILocator.getBean(ControleExpedicaoService.class);
	
	@Transient
	private List<ItemPedido> itens = new ArrayList<>();
	
	public ControleExpedicao(Long id, Long idPedido, Long idCompra, TipoLocalidade tipoLocalidade,String mascara,Long idLocalidade ,String nomeSolicitante
			,BigDecimal valorTotalComDesconto,String nomeFantasia,Date dataEntrega,Date dataAceitacaoExpedicao, String gestaoResponsavel,String nomeResponsavel
			,LocalizacaoPedido localizacaoPedido,Date entradaMercadoria,Date saidaMercadoria,String protocoloNf,Date dataRecebimento){
		
		this.id = id;
		this.idPedido = idPedido;
		this.idCompra = idCompra;
		this.tipoLocalidade = tipoLocalidade;
		this.mascara = mascara;
		this.nomeSolicitante = nomeSolicitante;
		this.valorTotalComDesconto = valorTotalComDesconto;
		this.nomeFantasia = nomeFantasia;
		this.dataEntrega = dataEntrega;
		this.dataAceitacaoExpedicao = dataAceitacaoExpedicao;
		this.nomeGestaoResponsavel = gestaoResponsavel;
		this.nomeResponsavel = nomeResponsavel;
		this.localizacaoPedido = localizacaoPedido;
		this.entradaMercadoria = entradaMercadoria;
		this.saidaMercadoria = saidaMercadoria;
		this.protocoloNf = protocoloNf;
		this.dataRecebimento = dataRecebimento;
		this.idLocalidade = idLocalidade;
		if (tipoLocalidade.getNome().equals("Comunidade")) 
		this.unidadeDeConservacao = service.buscarUC(idLocalidade);
		itens = service.buscarItens(idPedido);
		this.idTermo = service.getNumeroTermo(idPedido);
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		ControleExpedicao other = (ControleExpedicao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Date getEntradaMercadoria() {
		return entradaMercadoria;
	}

	public void setEntradaMercadoria(Date entradaMercadoria) {
		this.entradaMercadoria = entradaMercadoria;
	}

	public Date getSaidaMercadoria() {
		return saidaMercadoria;
	}

	public void setSaidaMercadoria(Date saidaMercadoria) {
		this.saidaMercadoria = saidaMercadoria;
	}

	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public String getTransporte() {
		return transporte;
	}

	public void setTransporte(String transporte) {
		this.transporte = transporte;
	}

	public BigDecimal getValorTransporte() {
		return valorTransporte;
	}

	public void setValorTransporte(BigDecimal valorTransporte) {
		this.valorTransporte = valorTransporte;
	}

	public String getProtocoloNf() {
		return protocoloNf;
	}

	public void setProtocoloNf(String protocoloNf) {
		this.protocoloNf = protocoloNf;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Date getDataAceitacaoExpedicao() {
		return dataAceitacaoExpedicao;
	}

	public void setDataAceitacaoExpedicao(Date dataAceitacaoExpedicao) {
		this.dataAceitacaoExpedicao = dataAceitacaoExpedicao;
	}

	public LocalizacaoPedido getLocalizacaoPedido() {
		return localizacaoPedido;
	}

	public void setLocalizacaoPedido(LocalizacaoPedido localizacaoPedido) {
		this.localizacaoPedido = localizacaoPedido;
	}

	public String getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(String notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public Gestao getGestaoResponsavel() {
		return gestaoResponsavel;
	}

	public void setGestaoResponsavel(Gestao gestaoResponsavel) {
		this.gestaoResponsavel = gestaoResponsavel;
	}

	public Long getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

	public Long getIdCompra() {
		return idCompra;
	}

	public void setIdCompra(Long idCompra) {
		this.idCompra = idCompra;
	}

	public TipoLocalidade getTipoLocalidade() {
		return tipoLocalidade;
	}

	public void setTipoLocalidade(TipoLocalidade tipoLocalidade) {
		this.tipoLocalidade = tipoLocalidade;
	}

	public String getMascara() {
		return mascara;
	}

	public void setMascara(String mascara) {
		this.mascara = mascara;
	}

	public String getUnidadeDeConservacao() {
		return unidadeDeConservacao;
	}

	public void setUnidadeDeConservacao(String unidadeDeConservacao) {
		this.unidadeDeConservacao = unidadeDeConservacao;
	}

	public String getNomeSolicitante() {
		return nomeSolicitante;
	}

	public void setNomeSolicitante(String nomeSolicitante) {
		this.nomeSolicitante = nomeSolicitante;
	}

	

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public Date getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public String getNomeGestaoResponsavel() {
		return nomeGestaoResponsavel;
	}

	public void setNomeGestaoResponsavel(String nomeGestaoResponsavel) {
		this.nomeGestaoResponsavel = nomeGestaoResponsavel;
	}

	public BigDecimal getValorTotalComDesconto() {
		return valorTotalComDesconto;
	}

	public void setValorTotalComDesconto(BigDecimal valorTotalComDesconto) {
		this.valorTotalComDesconto = valorTotalComDesconto;
	}

	public List<ItemPedido> getItens() {
		return itens;
	}

	public void setItens(List<ItemPedido> itens) {
		this.itens = itens;
	}

	public Long getIdTermo() {
		return idTermo;
	}

	public void setIdTermo(Long idTermo) {
		this.idTermo = idTermo;
	}


	
	
	
}
