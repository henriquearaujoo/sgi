package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "solicitacao_expedicao")
public class SolicitacaoExpedicao implements Serializable{

	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private User usuario;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_emissao")
	private Date dataEmissao;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_prevista_expedicao")
	private Date dataPrevisaoDaExpedicao; // Data que o solicitante determina provisão para expedição 
	@Column(columnDefinition = "TEXT")
	private String observacao;
	
	@OneToMany(mappedBy = "solicitacaoExpedicao", cascade = CascadeType.ALL, orphanRemoval = false)
	private List<Pedido> pedidos = new ArrayList<Pedido>();
	
	@Enumerated(EnumType.STRING)
	private StatusSolicitacaoExpedicao statusSolicitacao;
	
	
	
	public SolicitacaoExpedicao(){}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public User getUsuario() {
		return usuario;
	}



	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}



	public Date getDataEmissao() {
		return dataEmissao;
	}



	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}



	public Date getDataPrevisaoDaExpedicao() {
		return dataPrevisaoDaExpedicao;
	}



	public void setDataPrevisaoDaExpedicao(Date dataPrevisaoDaExpedicao) {
		this.dataPrevisaoDaExpedicao = dataPrevisaoDaExpedicao;
	}



	public String getObservacao() {
		return observacao;
	}



	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		SolicitacaoExpedicao other = (SolicitacaoExpedicao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}



	public StatusSolicitacaoExpedicao getStatusSolicitacao() {
		return statusSolicitacao;
	}



	public void setStatusSolicitacao(StatusSolicitacaoExpedicao statusSolicitacao) {
		this.statusSolicitacao = statusSolicitacao;
	}



	public List<Pedido> getPedidos() {
		return pedidos;
	}



	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}




	
	
	
	
	
	
}
