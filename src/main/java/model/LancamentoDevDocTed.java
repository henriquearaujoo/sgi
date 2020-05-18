package model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@DiscriminatorValue("devolucao_doc_ted")
public class LancamentoDevDocTed extends Lancamento {

    @Column(name = "valor_reembolso", precision = 10, scale = 2)
    private BigDecimal valorReembolso;

    @Column(name = "id_lancamento")
    private Long idLancamento;

    public LancamentoDevDocTed() {
    }

    public LancamentoDevDocTed(Lancamento lancamento) {
        setTipoGestao(lancamento.getTipoGestao());
        setGestao(lancamento.getGestao());
        setTipoLocalidade(lancamento.getTipoLocalidade());
        setLocalidade(lancamento.getLocalidade());
        setCodigo(lancamento.getCodigo().replace("SP", "DT"));
        setDataEmissao(new Date());
        setVersionLancamento(lancamento.getVersionLancamento());
        setDataDocumentoFiscal(lancamento.getDataDocumentoFiscal());
        setCategoriaFinanceira(lancamento.getCategoriaFinanceira());
        setTipoDocumentoFiscal(lancamento.getTipoDocumentoFiscal());
        setFornecedor(lancamento.getFornecedor());
        setDataPagamento(lancamento.getDataPagamento());
        setValorTotalComDesconto(lancamento.getValorTotalComDesconto());
        setDescricao(lancamento.getDescricao());
        setObservacao(lancamento.getObservacao());
        setSolicitante(lancamento.getSolicitante());
        setCondicaoPagamento(lancamento.getCondicaoPagamento());
        setCondicaoPagamentoEnum(CondicaoPagamento.DEPOSITO);
        setQuantidadeParcela(lancamento.getQuantidadeParcela());
        setNumeroDocumento(lancamento.getNumeroDocumento());
        setStatusCompra(lancamento.getStatusCompra());
        setDepesaReceita(DespesaReceita.RECEITA);
        setNotaFiscal(lancamento.getNotaFiscal());
        setTipoLancamento(lancamento.getTipoLancamento());

    }

    public Long getIdLancamento() {
        return idLancamento;
    }

    public void setIdLancamento(Long idLancamento) {
        this.idLancamento = idLancamento;
    }

    public BigDecimal getValorReembolso() {
        return valorReembolso;
    }

    public void setValorReembolso(BigDecimal valorReembolso) {
        this.valorReembolso = valorReembolso;
    }
}
