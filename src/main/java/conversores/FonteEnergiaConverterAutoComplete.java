package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Comunidade;
import model.FonteAguaConsumo;
import model.FonteEnergia;
import model.Localidade;
import model.MeioComunicacao;
import model.SintomaComum;
import repositorio.ComunitarioRepository;
import repositorio.FamiliarRepository;
import repositorio.LocalRepositorio;
import util.CDILocator;


@FacesConverter("fonteEnergiaConverter")
public class FonteEnergiaConverterAutoComplete implements Converter{

	private ComunitarioRepository repository;
	
	public FonteEnergiaConverterAutoComplete(){
		this.repository = CDILocator.getBean(ComunitarioRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
			FonteEnergia retorno =  new FonteEnergia();
			if (value != null) {
				retorno =  repository.findByIdFormaEnergia(new Long(value));
			};
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((FonteEnergia) value).getId() != null){
				return ((FonteEnergia) value).getId().toString();
			}
		}
		return null;
	}
	
}
