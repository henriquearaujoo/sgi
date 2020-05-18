package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.Projeto;
import repositorio.ProjetoRepositorio;
import util.CDILocator;


@FacesConverter("projetoConverter")
public class ProjetoConverterAutoComplete implements Converter{
			
	private ProjetoRepositorio repository;
	
	public ProjetoConverterAutoComplete(){
		this.repository = CDILocator.getBean(ProjetoRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			Projeto retorno = new Projeto();
	
			if (value != null) {
				retorno =  repository.getProjetoPorId(new Long(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((Projeto) value).getId() != null){
				return ((Projeto) value).getId().toString();
			}
		}
		return null;
	}
	
}
