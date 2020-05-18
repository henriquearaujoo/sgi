package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.CategoriaDespesaClass;
import model.ComponenteClass;
import repositorio.CategoriaRepositorio;
import repositorio.ComponenteRepositorio;
import util.CDILocator;


@FacesConverter("componenteConverter")
public class CompomenteConverter implements Converter{

	private ComponenteRepositorio repository;
	
	public CompomenteConverter(){
		this.repository = CDILocator.getBean(ComponenteRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			ComponenteClass retorno = new ComponenteClass();
			
			if (value != null) {
				retorno =  repository.findById(new Long(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((ComponenteClass) value).getId() != null){
				return ((ComponenteClass) value).getId().toString();
			}
		}
		return null;
	}

	
	
}
