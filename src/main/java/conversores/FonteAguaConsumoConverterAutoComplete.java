package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Comunidade;
import model.FonteAguaConsumo;
import model.Localidade;
import model.SintomaComum;
import repositorio.ComunitarioRepository;
import repositorio.FamiliarRepository;
import repositorio.LocalRepositorio;
import util.CDILocator;


@FacesConverter("fonteAguaConverter")
public class FonteAguaConsumoConverterAutoComplete implements Converter{

	private ComunitarioRepository repository;
	
	public FonteAguaConsumoConverterAutoComplete(){
		this.repository = CDILocator.getBean(ComunitarioRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
			FonteAguaConsumo retorno =  new FonteAguaConsumo();
			if (value != null) {
				retorno =  repository.findByIdFonteAgua(new Long(value));
			};
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((FonteAguaConsumo) value).getId() != null){
				return ((FonteAguaConsumo) value).getId().toString();
			}
		}
		return null;
	}
	
}
