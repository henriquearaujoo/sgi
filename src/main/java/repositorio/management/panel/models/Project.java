package repositorio.management.panel.models;

import java.io.Serializable;

public class Project implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id; 
	private String code;
	private String name;
	
	public Project() {
	}
	
	public Project(Long id,String code ,String name) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
