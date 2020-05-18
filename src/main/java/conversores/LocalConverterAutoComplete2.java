package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Localidade;
import repositorio.LocalRepositorio;
import util.CDILocator;


@FacesConverter("localConverterVI")
public class LocalConverterAutoComplete2 implements Converter{

	private LocalRepositorio repository;
	
	public LocalConverterAutoComplete2(){
		this.repository = CDILocator.getBean(LocalRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
			Localidade retorno =  new Localidade();
			if (value != null) {
				retorno =  repository.findByIdLocal(new Long(value));
			};
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((Localidade) value).getId() != null){
				return ((Localidade) value).getId().toString();
			}
		}
		return null;
	}
	
}
