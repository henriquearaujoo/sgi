package util;

public class Coluna {
	
	private String valor;
	private String label;
	
	
	public Coluna() {}
	
	public Coluna(String label, String valor) {
		this.valor = valor;
		this.label = label;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}



	
	
}
