package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="prestacao_conta")
public class PrestacaoDeConta implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private ContaBancaria contaRecebedor;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEmissao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPagamento;
	
	
	@Column
	private Long idAdiantamento;
	
	@Column
	private String numeroDocumento;
	
	@Column
	private String descricao;
	
	@Column
	private String notaFiscal;
	
	@ManyToOne
	private Acao acaoPagadora;
	
	@ManyToOne
	private FontePagadora fontePagadora;
	
	@ManyToOne
	private CategoriaDespesaClass categoria;
	
	@Column(name = "valor_total_sem_desconto", precision = 10, scale = 2)
	private BigDecimal valor;
	
	
	
	public PrestacaoDeConta(){}
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ContaBancaria getContaRecebedor() {
		return contaRecebedor;
	}

	public void setContaRecebedor(ContaBancaria contaRecebedor) {
		this.contaRecebedor = contaRecebedor;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}



	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}


	public Acao getAcaoPagadora() {
		return acaoPagadora;
	}

	public void setAcaoPagadora(Acao acaoPagadora) {
		this.acaoPagadora = acaoPagadora;
	}

	public FontePagadora getFontePagadora() {
		return fontePagadora;
	}

	public void setFontePagadora(FontePagadora fontePagadora) {
		this.fontePagadora = fontePagadora;
	}

	public CategoriaDespesaClass getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaDespesaClass categoria) {
		this.categoria = categoria;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}



	public String getNotaFiscal() {
		return notaFiscal;
	}



	public void setNotaFiscal(String notaFiscal) {
		this.notaFiscal = notaFiscal;
	}



	public String getDescricao() {
		return descricao;
	}



	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	

}
