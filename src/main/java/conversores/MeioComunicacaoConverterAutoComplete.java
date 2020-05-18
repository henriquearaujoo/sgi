package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Comunidade;
import model.FonteAguaConsumo;
import model.Localidade;
import model.MeioComunicacao;
import model.SintomaComum;
import repositorio.ComunitarioRepository;
import repositorio.FamiliarRepository;
import repositorio.LocalRepositorio;
import util.CDILocator;


@FacesConverter("meioComunicacaoConverter")
public class MeioComunicacaoConverterAutoComplete implements Converter{

	private ComunitarioRepository repository;
	
	public MeioComunicacaoConverterAutoComplete(){
		this.repository = CDILocator.getBean(ComunitarioRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
			MeioComunicacao retorno =  new MeioComunicacao();
			if (value != null) {
				retorno =  repository.findByIdMeioComunicacao(new Long(value));
			};
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((MeioComunicacao) value).getId() != null){
				return ((MeioComunicacao) value).getId().toString();
			}
		}
		return null;
	}
	
}
