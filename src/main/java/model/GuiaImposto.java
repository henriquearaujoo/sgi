package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("guia_imposto")
public class GuiaImposto extends Lancamento {

	
	private static final long serialVersionUID = 1L;
	
	@OneToMany(mappedBy = "guia", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
	private List<Imposto> impostoGuias = new ArrayList<Imposto>();
	
	public GuiaImposto() {
		
	}

	public List<Imposto> getImpostoGuias() {
		return impostoGuias;
	}

	public void setImpostoGuias(List<Imposto> impostoGuias) {
		this.impostoGuias = impostoGuias;
	}
	
}
