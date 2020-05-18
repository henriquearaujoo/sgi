package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.CadeiaProdutiva;
import model.Projeto;
import repositorio.CadeiaRepositorio;
import repositorio.ProjetoRepositorio;
import util.CDILocator;


@FacesConverter("cadeiaConverter")
public class CadeiaConverterAutoComplete implements Converter{

	private CadeiaRepositorio repository;
	
	public CadeiaConverterAutoComplete(){
		this.repository = CDILocator.getBean(CadeiaRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			CadeiaProdutiva retorno = new CadeiaProdutiva();
	
			if (value != null) {
				retorno =  repository.getCadeiaPorId(new Long(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((CadeiaProdutiva) value).getId() != null){
				return ((CadeiaProdutiva) value).getId().toString();
			}
		}
		return null;
	}
	
}
