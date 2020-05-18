package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Gestao;
import repositorio.GestaoRepositorio;
import util.CDILocator;


@FacesConverter("gestaoConverter")
public class GestaoConverterAutoComplete implements Converter{

	private GestaoRepositorio repository;
	
	public GestaoConverterAutoComplete(){
		this.repository = CDILocator.getBean(GestaoRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			Gestao retorno =  new Gestao();
	
			if (value != null) {
				retorno =  repository.getGestaoPorId(new Long(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((Gestao) value).getId() != null){
				return ((Gestao) value).getId().toString();
			}
		}
		return null;
	}
	
}
