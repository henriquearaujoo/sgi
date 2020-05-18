package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Objetivo;
import repositorio.ObjetivoRepositorio;
import util.CDILocator;

@FacesConverter("objetivoConv")
public class ObjetivoConversor implements Converter{





	private ObjetivoRepositorio repositorio;

	public ObjetivoConversor(){
		this.repositorio = CDILocator.getBean(ObjetivoRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			Objetivo retorno = new Objetivo();
	
			if (value != null) {
				retorno =  repositorio.getId(new Long(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((Objetivo) value).getId() != null){
				return ((Objetivo) value).getId().toString();
			}
		}
		return null;
	}


}


