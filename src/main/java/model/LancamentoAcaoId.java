package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LancamentoAcaoId implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7189002009255768080L;
	
    @Column
	private Long fonte;
    @Column
	private Long acao;
    @Column
	private Long lancamento;
	
	public LancamentoAcaoId(){}

	public Long getFonte() {
		return fonte;
	}

	public void setFonte(Long fonte) {
		this.fonte = fonte;
	}

	public Long getAcao() {
		return acao;
	}

	public void setAcao(Long acao) {
		this.acao = acao;
	}

	public Long getLancamento() {
		return lancamento;
	}

	public void setLancamento(Long lancamento) {
		this.lancamento = lancamento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acao == null) ? 0 : acao.hashCode());
		result = prime * result + ((fonte == null) ? 0 : fonte.hashCode());
		result = prime * result + ((lancamento == null) ? 0 : lancamento.hashCode());
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
		LancamentoAcaoId other = (LancamentoAcaoId) obj;
		if (acao == null) {
			if (other.acao != null)
				return false;
		} else if (!acao.equals(other.acao))
			return false;
		if (fonte == null) {
			if (other.fonte != null)
				return false;
		} else if (!fonte.equals(other.fonte))
			return false;
		if (lancamento == null) {
			if (other.lancamento != null)
				return false;
		} else if (!lancamento.equals(other.lancamento))
			return false;
		return true;
	}
	
	
	
	
	
	
}
