package model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("reembolso")
public class Reembolso extends Lancamento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column
	private String competencia;
	
	

	public Reembolso() {

	}

	public Reembolso(Long id, String nomeGestao, String nomeSolicitante, String nomeDestino, Date data,
			StatusCompra status, String nomeFornecedor, String descricao, BigDecimal valorComDesconto) {
		setId(id);
		setNomeGestao(nomeGestao);
		setNomeSolicitante(nomeSolicitante);
		setNomeDestino(nomeDestino);
		setDataEmissao(data);
		setStatusCompra(status);
		setNomeFornecedor(nomeFornecedor);
		setDescricao(descricao);
		setValorTotalComDesconto(valorComDesconto);
	}
	
	public String getCompetencia() {
		return competencia;
	}

	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}	
}
