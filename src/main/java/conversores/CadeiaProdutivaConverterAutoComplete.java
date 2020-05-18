package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.CadeiaProdutivaFamiliar;
import model.Comunidade;
import model.Localidade;
import model.SintomaComum;
import repositorio.FamiliarRepository;
import repositorio.LocalRepositorio;
import util.CDILocator;


@FacesConverter("cadeiaProdutivaConverter")
public class CadeiaProdutivaConverterAutoComplete implements Converter{

	private FamiliarRepository repository;
	
	public CadeiaProdutivaConverterAutoComplete(){
		this.repository = CDILocator.getBean(FamiliarRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
			CadeiaProdutivaFamiliar retorno = new CadeiaProdutivaFamiliar();
			if (value != null) {
				retorno =  repository.findByIdCadeia(new Long(value));
			};
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((CadeiaProdutivaFamiliar) value).getId() != null){
				return ((CadeiaProdutivaFamiliar) value).getId().toString();
			}
		}
		return null;
	}
	
}
