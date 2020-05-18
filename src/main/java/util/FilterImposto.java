package util;

import model.TipoImposto;

public class FilterImposto {
	
	
	
	private Long idConta;
	private TipoImposto tipo;
	private Long idGuia;
	
	
	public FilterImposto() {}


	public Long getIdConta() {
		return idConta;
	}


	public void setIdConta(Long idConta) {
		this.idConta = idConta;
	}


	public TipoImposto getTipo() {
		return tipo;
	}


	public void setTipo(TipoImposto tipo) {
		this.tipo = tipo;
	}


	public Long getIdGuia() {
		return idGuia;
	}


	public void setIdGuia(Long idGuia) {
		this.idGuia = idGuia;
	}
	

}
