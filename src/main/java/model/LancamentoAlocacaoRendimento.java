package model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("rendimento")
public class LancamentoAlocacaoRendimento extends Lancamento {

    @Column(name = "id_alocacao_rendimento")
    private Long idAlocacaoRendimento;

    public Long getIdAlocacaoRendimento() {
        return idAlocacaoRendimento;
    }

    public void setIdAlocacaoRendimento(Long idAlocacaoRendimento) {
        this.idAlocacaoRendimento = idAlocacaoRendimento;
    }
}
