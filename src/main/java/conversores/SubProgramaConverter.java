package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.SubPrograma;
import repositorio.ProjetoRepositorio;
import util.CDILocator;


@FacesConverter("converterSubPrograma")
public class SubProgramaConverter implements Converter{

	private ProjetoRepositorio repository;
	
	public SubProgramaConverter(){
		this.repository = CDILocator.getBean(ProjetoRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			SubPrograma retorno = new SubPrograma();
			
			if (value != null) {
				retorno =  repository.getSubPrograma(new Long(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((SubPrograma) value).getId() != null){
				return ((SubPrograma) value).getId().toString();
			}
		}
		return null;
	}

	
	
}
