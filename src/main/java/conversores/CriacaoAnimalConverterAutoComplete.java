package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.CadeiaProdutivaFamiliar;
import model.Comunidade;
import model.CriacaoAnimal;
import model.Localidade;
import model.ProdutoPescado;
import model.SintomaComum;
import repositorio.FamiliarRepository;
import repositorio.LocalRepositorio;
import util.CDILocator;


@FacesConverter("criacaoAnimalConverter")
public class CriacaoAnimalConverterAutoComplete implements Converter{

	private FamiliarRepository repository;
	
	public CriacaoAnimalConverterAutoComplete(){
		this.repository = CDILocator.getBean(FamiliarRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
			CriacaoAnimal retorno = new CriacaoAnimal();
			if (value != null) {
				retorno =  repository.findByIdCriacaoAnimal(new Long(value));
			};
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((CriacaoAnimal) value).getId() != null){
				return ((CriacaoAnimal) value).getId().toString();
			}
		}
		return null;
	}
	
}
