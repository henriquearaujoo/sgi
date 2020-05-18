package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.SubComponente;
import repositorio.ComponenteRepositorio;
import util.CDILocator;


@FacesConverter("subcomponenteConverter")
public class SubCompomenteConverter implements Converter{

	private ComponenteRepositorio repository;
	
	public SubCompomenteConverter(){
		this.repository = CDILocator.getBean(ComponenteRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			SubComponente retorno = new SubComponente();
			
			if (value != null) {
				retorno =  repository.findByIdSubComponente(new Long(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((SubComponente) value).getId() != null){
				return ((SubComponente) value).getId().toString();
			}
		}
		return null;
	}
}
