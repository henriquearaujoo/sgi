package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class HistoricoAditivoOrcamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAlteracao;

    @Temporal(TemporalType.DATE)
    private Date dataAnterior;

    @Temporal(TemporalType.DATE)
    private Date dataAditivo;

    @ManyToOne
    private User usuario;

    @ManyToOne
    private Orcamento orcamento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public Date getDataAditivo() {
        return dataAditivo;
    }

    public void setDataAditivo(Date dataAditivo) {
        this.dataAditivo = dataAditivo;
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

    public Date getDataAnterior() {
        return dataAnterior;
    }

    public void setDataAnterior(Date dataAnterior) {
        this.dataAnterior = dataAnterior;
    }
}
