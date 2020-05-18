package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.CadeiaProdutiva;
import model.Fornecedor;
import model.Projeto;
import model.Rubrica;
import repositorio.CadeiaRepositorio;
import repositorio.ProjetoRepositorio;
import repositorio.RubricaRepositorio;
import util.CDILocator;


@FacesConverter(forClass = Rubrica.class)
public class RubricaConverterComplete implements Converter{

	private RubricaRepositorio repository;
	
	public RubricaConverterComplete(){
		this.repository = CDILocator.getBean(RubricaRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			Rubrica retorno =  new Rubrica();
	
			if (value != null) {
				retorno =  repository.findById(new Long(value));
			}
			
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if (value != null) {
			if(((Rubrica) value).getId() != null){
				return ((Rubrica) value).getId().toString();
			}
		}
		
		return null;
	}
	
}
