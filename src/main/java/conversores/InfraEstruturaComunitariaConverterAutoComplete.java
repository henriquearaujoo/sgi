package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.CadeiaProdutivaFamiliar;
import model.Comunidade;
import model.InfraestruturaComunitaria;
import model.Localidade;
import model.ProdutoAgricultura;
import model.SintomaComum;
import repositorio.ComunitarioRepository;
import repositorio.FamiliarRepository;
import repositorio.LocalRepositorio;
import util.CDILocator;


@FacesConverter("infraestruturaConverter")
public class InfraEstruturaComunitariaConverterAutoComplete implements Converter{

	private ComunitarioRepository repository;
	
	public InfraEstruturaComunitariaConverterAutoComplete(){
		this.repository = CDILocator.getBean(ComunitarioRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
			InfraestruturaComunitaria retorno = new InfraestruturaComunitaria();
			if (value != null) {
				retorno =  repository.findByIdInfraestrutura(new Long(value));
			};
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((InfraestruturaComunitaria) value).getId() != null){
				return ((InfraestruturaComunitaria) value).getId().toString();
			}
		}
		return null;
	}
	
}
