package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.CategoriaFinanceira;
import repositorio.CategoriaRepositorio;
import util.CDILocator;


@FacesConverter("categoriaFinConverter")
public class CategoriaFinanceiraConverter implements Converter{

	private CategoriaRepositorio repository;
	
	public CategoriaFinanceiraConverter(){
		this.repository = CDILocator.getBean(CategoriaRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			CategoriaFinanceira retorno = new CategoriaFinanceira();
			
			if (value != null) {
				retorno =  repository.findByIdFinanceira(new Long(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((CategoriaFinanceira) value).getId() != null){
				return ((CategoriaFinanceira) value).getId().toString();
			}
		}
		return null;
	}
}
