package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.CategoriaDespesaClass;
import model.Localidade;
import model.Projeto;
import repositorio.CategoriaRepositorio;
import repositorio.ProjetoRepositorio;
import util.CDILocator;


@FacesConverter("categoriaConverter")
public class CategoriaAutoComplete implements Converter{

	private CategoriaRepositorio repository;
	
	public CategoriaAutoComplete(){
		this.repository = CDILocator.getBean(CategoriaRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
			CategoriaDespesaClass retorno =  new CategoriaDespesaClass();
			if (value != null) {
				retorno =  repository.findById(new Long(value));
			};
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((CategoriaDespesaClass) value).getId() != null){
				return ((CategoriaDespesaClass) value).getId().toString();
			}
		}
		return null;
	}
	
}
