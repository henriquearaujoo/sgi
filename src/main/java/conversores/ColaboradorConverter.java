package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Colaborador;
import repositorio.ColaboradorRepositorio;
import util.CDILocator;

@FacesConverter(forClass = Colaborador.class )
public class ColaboradorConverter implements Converter{
private ColaboradorRepositorio repository;
	
	public ColaboradorConverter(){
		this.repository = CDILocator.getBean(ColaboradorRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			Colaborador retorno =  new Colaborador();
	
			if (value != null) {
				retorno =  repository.getColaboradorById(new Long(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((Colaborador) value).getId() != null){
				return ((Colaborador) value).getId().toString();
			}
		}
		return null;
	}
	

}