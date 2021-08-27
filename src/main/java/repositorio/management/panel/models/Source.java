package repositorio.management.panel.models;

import java.io.Serializable;

import model.FontePagadora;

public class Source extends FontePagadora implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Source() {
	}
	
	public Source(Long id, String name) {
		setId(id);
		this.name = name;
	}
	
	private String name;	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
