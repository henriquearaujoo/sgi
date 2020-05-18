package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.FontePagadora;
import model.Projeto;
import repositorio.FontePagadoraRepositorio;
import repositorio.ProjetoRepositorio;
import util.CDILocator;


@FacesConverter(forClass = FontePagadora.class)
public class FontePagadoraConverter implements Converter{

	private FontePagadoraRepositorio repository;
	
	public FontePagadoraConverter(){
		this.repository = CDILocator.getBean(FontePagadoraRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			FontePagadora retorno = new FontePagadora();
			
			if (value != null) {
				retorno =  repository.getFontePorId(new Long(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((FontePagadora) value).getId() != null){
				return ((FontePagadora) value).getId().toString();
			}
		}
		return null;
	}

	
	
}
