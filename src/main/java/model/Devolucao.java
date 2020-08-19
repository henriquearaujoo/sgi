package model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("devolucao")
public class Devolucao extends Lancamento {

   
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Column(name = "id_lancamento")
    private Long idLancamento;

    public Devolucao() {
    }

    public Devolucao(Lancamento lancamento) {
        setTipoGestao(lancamento.getTipoGestao());
        setGestao(lancamento.getGestao());
        setTipoLocalidade(lancamento.getTipoLocalidade());
        setLocalidade(lancamento.getLocalidade());
        setCodigo(lancamento.getCodigo());
        setDataEmissao(new Date());
        setVersionLancamento(lancamento.getVersionLancamento());
        setDataDocumentoFiscal(lancamento.getDataDocumentoFiscal());
        setCategoriaFinanceira(lancamento.getCategoriaFinanceira()); //VERIFICAR CATEGORIA FINANCEIRA
        setTipoDocumentoFiscal(lancamento.getTipoDocumentoFiscal());
        setFornecedor(lancamento.getFornecedor());
        setDataPagamento(lancamento.getDataPagamento());
        //setValorTotalComDesconto(lancamento.getValorTotalComDesconto());
        setDescricao(lancamento.getDescricao());
        setObservacao(lancamento.getObservacao());
        setSolicitante(lancamento.getSolicitante());
        setCondicaoPagamento(lancamento.getCondicaoPagamento());
        setCondicaoPagamentoEnum(CondicaoPagamento.DEPOSITO);
        setQuantidadeParcela(1);
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

}
