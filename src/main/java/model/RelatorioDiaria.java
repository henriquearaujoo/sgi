package model;

import java.math.BigDecimal;
import java.util.Date;

public class RelatorioDiaria {
	
	private String gestao;
	private String nomeProfissional;
	private String localidade;
	
	private Date dataIda;
	private Date dataVolta;
	private Integer diasEmCampo;
	private BigDecimal valorTotal;
	private Long idDiaria;
	private String missao;
	private Date dataEmissao;
	private String descricao;
	
	
	
	public RelatorioDiaria() {}
	
	public RelatorioDiaria(String gestao, String nomeProfissional, String localidade, Date dataIda, Date dataVolta,
			Integer diasEmCampo, BigDecimal valorTotal, Long idDiaria, String missao, Date dataEmissao,
			String descricao) {
		
		this.gestao = gestao;
		this.nomeProfissional = nomeProfissional;
		this.localidade = localidade;
		this.dataIda = dataIda;
		this.dataVolta = dataVolta;
		this.diasEmCampo = diasEmCampo;
		this.valorTotal = valorTotal;
		this.idDiaria = idDiaria;
		this.missao = missao;
		this.dataEmissao = dataEmissao;
		this.descricao = descricao;
	}

	public String getGestao() {
		return gestao;
	}



	public void setGestao(String gestao) {
		this.gestao = gestao;
	}



	public String getNomeProfissional() {
		return nomeProfissional;
	}



	public void setNomeProfissional(String nomeProfissional) {
		this.nomeProfissional = nomeProfissional;
	}



	public String getLocalidade() {
		return localidade;
	}



	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}



	public Date getDataIda() {
		return dataIda;
	}



	public void setDataIda(Date dataIda) {
		this.dataIda = dataIda;
	}



	public Date getDataVolta() {
		return dataVolta;
	}



	public void setDataVolta(Date dataVolta) {
		this.dataVolta = dataVolta;
	}



	public Integer getDiasEmCampo() {
		return diasEmCampo;
	}



	public void setDiasEmCampo(Integer diasEmCampo) {
		this.diasEmCampo = diasEmCampo;
	}



	public BigDecimal getValorTotal() {
		return valorTotal;
	}



	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}



	public Long getIdDiaria() {
		return idDiaria;
	}



	public void setIdDiaria(Long idDiaria) {
		this.idDiaria = idDiaria;
	}



	public String getMissao() {
		return missao;
	}



	public void setMissao(String missao) {
		this.missao = missao;
	}



	public Date getDataEmissao() {
		return dataEmissao;
	}



	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}



	public String getDescricao() {
		return descricao;
	}



	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idDiaria == null) ? 0 : idDiaria.hashCode());
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
		RelatorioDiaria other = (RelatorioDiaria) obj;
		if (idDiaria == null) {
			if (other.idDiaria != null)
				return false;
		} else if (!idDiaria.equals(other.idDiaria))
			return false;
		return true;
	}


		
	

}
