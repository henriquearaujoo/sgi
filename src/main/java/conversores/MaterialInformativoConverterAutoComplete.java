package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Comunidade;
import model.FonteAguaConsumo;
import model.Localidade;
import model.MaterialInformativo;
import model.SintomaComum;
import repositorio.ComunitarioRepository;
import repositorio.FamiliarRepository;
import repositorio.LocalRepositorio;
import util.CDILocator;


@FacesConverter("materialInformativoConverter")
public class MaterialInformativoConverterAutoComplete implements Converter{

	private ComunitarioRepository repository;
	
	public MaterialInformativoConverterAutoComplete(){
		this.repository = CDILocator.getBean(ComunitarioRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
			MaterialInformativo retorno =  new MaterialInformativo();
			if (value != null) {
				retorno =  repository.findByIdMaterialInformativo(new Long(value));
			};
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((MaterialInformativo) value).getId() != null){
				return ((MaterialInformativo) value).getId().toString();
			}
		}
		return null;
	}
	
}
