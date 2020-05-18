package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Estado;
import repositorio.LocalRepositorio;
import util.CDILocator;


@FacesConverter("estadoConverter")
public class EstadoConverter implements Converter{

	private LocalRepositorio repository;
	
	public EstadoConverter(){
		this.repository = CDILocator.getBean(LocalRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
			Estado retorno =  new Estado();
			if (value != null) {
				retorno =  repository.findEstadoBydId(new Long(value));
			};
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((Estado) value).getId() != null){
				return ((Estado) value).getId().toString();
			}
		}
		return null;
	}
	
}
