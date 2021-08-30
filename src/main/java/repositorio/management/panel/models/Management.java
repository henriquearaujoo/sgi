package repositorio.management.panel.models;

import java.io.Serializable;

import model.Gestao;

public class Management extends Gestao implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Management() {
		
	}
	
	public Management(Long id, String name) {
		super(id, name);
	}
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
