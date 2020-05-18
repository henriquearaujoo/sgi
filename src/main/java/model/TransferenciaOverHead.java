package model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transferencia_overhead")
public class TransferenciaOverHead implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String descricao;

    @Temporal(TemporalType.DATE)
    private Date dataEmprestimo;
    
    @Temporal(TemporalType.DATE)
    private Date dataEmissao;
    
    @Temporal(TemporalType.DATE)
    private Date dataPrevisaoPagamento;

    @Column(precision = 11, scale = 2, nullable = false)
    private BigDecimal valor;

    @ManyToOne
    private ContaBancaria contaRecebedora;

    @ManyToOne
    private ContaBancaria contaPagadora;
    
    @ManyToOne
    private Orcamento doacaoPagadora;
    
    
    @ManyToOne
    private Orcamento doacaoRecebedora;
    
    
    @Column // 0 = OVERHEAD 1 OVERHEAD INDIRETO 
	private Integer tipo;

  

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

	public Date getDataEmprestimo() {
		return dataEmprestimo;
	}

	public void setDataEmprestimo(Date dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Date getDataPrevisaoPagamento() {
		return dataPrevisaoPagamento;
	}

	public void setDataPrevisaoPagamento(Date dataPrevisaoPagamento) {
		this.dataPrevisaoPagamento = dataPrevisaoPagamento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public ContaBancaria getContaRecebedora() {
		return contaRecebedora;
	}

	public void setContaRecebedora(ContaBancaria contaRecebedora) {
		this.contaRecebedora = contaRecebedora;
	}

	public ContaBancaria getContaPagadora() {
		return contaPagadora;
	}

	public void setContaPagadora(ContaBancaria contaPagadora) {
		this.contaPagadora = contaPagadora;
	}

	public Orcamento getDoacaoPagadora() {
		return doacaoPagadora;
	}

	public void setDoacaoPagadora(Orcamento doacaoPagadora) {
		this.doacaoPagadora = doacaoPagadora;
	}

	public Orcamento getDoacaoRecebedora() {
		return doacaoRecebedora;
	}

	public void setDoacaoRecebedora(Orcamento doacaoRecebedora) {
		this.doacaoRecebedora = doacaoRecebedora;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

}
