package model;

import java.io.Serializable;

public class LinkRelatorio implements Serializable{

	
	private String link;
	private String label;
	
	
	
	public LinkRelatorio(){}



	public String getLink() {
		return link;
	}



	public void setLink(String link) {
		this.link = link;
	}



	public String getLabel() {
		return label;
	}



	public void setLabel(String label) {
		this.label = label;
	}
	
	
	
}
