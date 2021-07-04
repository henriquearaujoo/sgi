package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.primefaces.model.charts.bar.BarChartModel;
		

@Entity
@Table(name = "donation_management")
public class DonationManagement implements Serializable{

	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;			
	@Column
	private String description;
	
	@ManyToOne
	private Gestao management;
	
	@ManyToOne
	private Orcamento donation;
	
	@Column(name = "date_created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;
	
	@Column(name = "date_edited")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateEdited;
	
	@Column
	private Boolean active;
	@Column(name = "value", precision = 10, scale = 2, nullable = false)
	private BigDecimal valor = BigDecimal.ZERO;
	
	// Modelo do gráfico para cada valor distribuido
	@Transient
	private BarChartModel model;

	public BarChartModel getModel() {
		return model;
	}

	public void setModel(BarChartModel model) {
		this.model = model;
	}

	public DonationManagement(){}
	
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
		DonationManagement other = (DonationManagement) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public Orcamento getDonation() {
		return donation;
	}

	public void setDonation(Orcamento donation) {
		this.donation = donation;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Gestao getManagement() {
		return management;
	}

	public void setManagement(Gestao management) {
		this.management = management;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateEdited() {
		return dateEdited;
	}

	public void setDateEdited(Date dateEdited) {
		this.dateEdited = dateEdited;
	}

}
