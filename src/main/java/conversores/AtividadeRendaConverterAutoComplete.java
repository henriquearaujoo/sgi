package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.AtividadeRenda;
import repositorio.ComunitarioRepository;
import util.CDILocator;


@FacesConverter("atividadeRendaConverter")
public class AtividadeRendaConverterAutoComplete implements Converter{

	private ComunitarioRepository repository;
	
	public AtividadeRendaConverterAutoComplete(){
		this.repository = CDILocator.getBean(ComunitarioRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
			AtividadeRenda retorno =  new AtividadeRenda();
			if (value != null) {
				retorno =  repository.findByIdAtividadeRenda(new Long(value));
			};
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((AtividadeRenda) value).getId() != null){
				return ((AtividadeRenda) value).getId().toString();
			}
		}
		return null;
	}
	
}
