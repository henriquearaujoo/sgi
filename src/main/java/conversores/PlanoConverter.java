package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.PlanoDeTrabalho;
import repositorio.PlanoTrabalhoRepositorio;
import util.CDILocator;


@FacesConverter(forClass = PlanoDeTrabalho.class)
public class PlanoConverter implements Converter{

	private PlanoTrabalhoRepositorio repository;
	
	public PlanoConverter(){
		this.repository = CDILocator.getBean(PlanoTrabalhoRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			PlanoDeTrabalho retorno = new PlanoDeTrabalho();
			
			if (value != null) {
				retorno =  repository.getPlanoPorId(new Long(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((PlanoDeTrabalho) value).getId() != null){
				return ((PlanoDeTrabalho) value).getId().toString();
			}
		}
		return null;
	}

	
	
}
