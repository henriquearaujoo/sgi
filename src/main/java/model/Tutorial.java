package model;

import java.io.Serializable;

public class Tutorial implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String label;
	private String id;
	private String path;
	
	
	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
	 return	this.id;
	}

}
