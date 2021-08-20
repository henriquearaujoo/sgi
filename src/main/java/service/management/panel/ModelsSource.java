package service.management.panel;

import java.io.Serializable;
import java.math.BigDecimal;

public class ModelsSource implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String source;

	private BigDecimal planningValue;
	
	private BigDecimal effectiveValue;
  

	public ModelsSource() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getPlanningValue() {
		return planningValue;
	}

	public void setPlanningValue(BigDecimal planningValue) {
		this.planningValue = planningValue;
	}

	public BigDecimal getEffectiveValue() {
		return effectiveValue;
	}

	public void setEffectiveValue(BigDecimal effectiveValue) {
		this.effectiveValue = effectiveValue;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
