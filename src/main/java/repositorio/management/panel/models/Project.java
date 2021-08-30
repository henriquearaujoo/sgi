package repositorio.management.panel.models;

import java.io.Serializable;

import model.Projeto;

public class Project extends Projeto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String name;
	
	public Project(Long id, String code, String name) {
		super(id, name, code);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	
	
}
