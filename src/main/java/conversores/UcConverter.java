package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Localidade;
import model.UnidadeConservacao;
import repositorio.LocalRepositorio;
import util.CDILocator;


@FacesConverter("uc_converter")
public class UcConverter implements Converter{

	private LocalRepositorio repository;
	
	public UcConverter(){
		this.repository = CDILocator.getBean(LocalRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
			UnidadeConservacao retorno =  new UnidadeConservacao();
			if (value != null) {
				retorno =  repository.findUcByCodigo(new Long(value));
			};
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((UnidadeConservacao) value).getId() != null){
				return ((UnidadeConservacao) value).getId().toString();
			}
		}
		return null;
	}
	
}
