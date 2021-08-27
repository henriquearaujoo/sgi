package repositorio.management.panel.models;

import java.io.Serializable;

import model.Projeto;

public class Project extends Projeto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Project(Long id,String code ,String name) {
		setId(id);
		setCodigo(code);
		this.name = name;
	}
	 
	private String code;
	private String name;
	
	public Project() {
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
