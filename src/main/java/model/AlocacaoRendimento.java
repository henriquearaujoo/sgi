package model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "alocacao_rendimento")
public class AlocacaoRendimento implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 11, scale = 2, nullable = false)
    private BigDecimal valor;

    @ManyToOne
    private ContaBancaria conta;

    @ManyToOne
    private ContaBancaria contaAlocacao;

    @ManyToOne
    private Orcamento orcamento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public ContaBancaria getConta() {
        return conta;
    }

    public void setConta(ContaBancaria conta) {
        this.conta = conta;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }

    public ContaBancaria getContaAlocacao() {
        return contaAlocacao;
    }

    public void setContaAlocacao(ContaBancaria contaAlocacao) {
        this.contaAlocacao = contaAlocacao;
    }
}
