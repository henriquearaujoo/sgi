package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Metas;
import model.Objetivo;
import repositorio.MetasRepositorio;
import util.CDILocator;

@FacesConverter("metaConversor")
public class MetaConverter implements Converter{





	private MetasRepositorio repositorio;

	public MetaConverter(){
		this.repositorio = CDILocator.getBean(MetasRepositorio.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		Metas retorno = new Metas();

		if (value != null) {
			retorno =  repositorio.findById(new Long(value));
		}
		return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((Metas) value).getId() != null){
				return ((Metas) value).getId().toString();
			}
		}
		return null;
	}


}	


