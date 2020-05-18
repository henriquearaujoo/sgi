package model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "rendimento_conta")
public class RendimentoConta implements Serializable {

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
    private Date dataPagamento;

    @Column(precision = 11, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column
    private String competencia;

    @ManyToOne
    private ContaBancaria conta;

    @ManyToOne
    private ContaBancaria contaPagadora;

    @ManyToOne
    private User usuario;

    @ManyToOne
    private Orcamento orcamento;

    @Transient
    private String contarecebedora;

    @Transient
    private String contapagadora;

    @Transient
    private String fontedoacao;

    @Transient
    private String nomeusuario;

    @Transient
    private String datapagamento;

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

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

    public ContaBancaria getConta() {
        return conta;
    }

    public void setConta(ContaBancaria conta) {
        this.conta = conta;
    }

    public ContaBancaria getContaPagadora() {
        return contaPagadora;
    }

    public void setContaPagadora(ContaBancaria contaPagadora) {
        this.contaPagadora = contaPagadora;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }

    public String getContarecebedora() {
        return contarecebedora;
    }

    public void setContarecebedora(String contarecebedora) {
        this.contarecebedora = contarecebedora;
    }

    public String getContapagadora() {
        return contapagadora;
    }

    public void setContapagadora(String contapagadora) {
        this.contapagadora = contapagadora;
    }

    public String getFontedoacao() {
        return fontedoacao;
    }

    public void setFontedoacao(String fontedoacao) {
        this.fontedoacao = fontedoacao;
    }

    public String getNomeusuario() {
        return nomeusuario;
    }

    public void setNomeusuario(String nomeusuario) {
        this.nomeusuario = nomeusuario;
    }

    public String getDatapagamento() {
        return datapagamento;
    }

    public void setDatapagamento(String datapagamento) {
        this.datapagamento = datapagamento;
    }
}
