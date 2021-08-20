package service.management.panel;

import java.io.Serializable;
import java.math.BigDecimal;

public class ModelsMonth implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private Integer month;

	private BigDecimal value;
	
//	private BigDecimal effectiveValue;
  

	public ModelsMonth() {}
	
	public ModelsMonth(Integer month, BigDecimal value) {
		this.month = month;
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}


}
