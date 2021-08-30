package repositorio.management.panel.models;

import model.FontePagadora;

public class Source extends FontePagadora {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	public Source(Long id, String name) {
		super(id, name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
