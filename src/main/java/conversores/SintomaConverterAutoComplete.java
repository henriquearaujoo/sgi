package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Comunidade;
import model.Localidade;
import model.SintomaComum;
import repositorio.FamiliarRepository;
import repositorio.LocalRepositorio;
import util.CDILocator;


@FacesConverter("sintomaConverter")
public class SintomaConverterAutoComplete implements Converter{

	private FamiliarRepository repository;
	
	public SintomaConverterAutoComplete(){
		this.repository = CDILocator.getBean(FamiliarRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
			SintomaComum retorno =  new SintomaComum();
			if (value != null) {
				retorno =  repository.findByIdSintoma(new Long(value));
			};
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((SintomaComum) value).getId() != null){
				return ((SintomaComum) value).getId().toString();
			}
		}
		return null;
	}
	
}
