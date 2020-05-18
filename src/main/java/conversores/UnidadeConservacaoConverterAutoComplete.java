package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Localidade;
import model.UnidadeConservacao;
import repositorio.LocalRepositorio;
import util.CDILocator;


@FacesConverter("unidadeConservacaoConverter")
public class UnidadeConservacaoConverterAutoComplete implements Converter{

	private LocalRepositorio repository;
	
	public UnidadeConservacaoConverterAutoComplete(){
		this.repository = CDILocator.getBean(LocalRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
			UnidadeConservacao retorno =  new UnidadeConservacao();
			if (value != null) {
				retorno =  repository.findByIdUnidadeDeConservacao(new Long(value));
			};
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((Localidade) value).getId() != null){
				return ((Localidade) value).getId().toString();
			}
		}
		return null;
	}
	
}
