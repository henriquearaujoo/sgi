package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.CadeiaProdutivaFamiliar;
import model.Comunidade;
import model.Localidade;
import model.ProdutoFlorestalMadeireiro;
import model.ProdutoPescado;
import model.ProdutoPescadoFamiliar;
import model.SintomaComum;
import repositorio.FamiliarRepository;
import repositorio.LocalRepositorio;
import util.CDILocator;


@FacesConverter("produtoPescadoConverter")
public class ProdutoPescadoConverterAutoComplete implements Converter{

	private FamiliarRepository repository;
	
	public ProdutoPescadoConverterAutoComplete(){
		this.repository = CDILocator.getBean(FamiliarRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
			ProdutoPescado retorno = new ProdutoPescado();
			if (value != null) {
				retorno =  repository.findByIdProdutoPescado(new Long(value));
			};
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((ProdutoPescado) value).getId() != null){
				return ((ProdutoPescado) value).getId().toString();
			}
		}
		return null;
	}
	
}
