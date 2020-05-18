package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.CadeiaProdutivaFamiliar;
import model.Comunidade;
import model.CriacaoAnimal;
import model.Localidade;
import model.ProdutoFlorestalMadeireiro;
import model.ProdutoPescado;
import model.SintomaComum;
import repositorio.FamiliarRepository;
import repositorio.LocalRepositorio;
import util.CDILocator;


@FacesConverter("produtoMadeireiroConverter")
public class ProdutoMadeireiroConverterAutoComplete implements Converter{

	private FamiliarRepository repository;
	
	public ProdutoMadeireiroConverterAutoComplete(){
		this.repository = CDILocator.getBean(FamiliarRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
			ProdutoFlorestalMadeireiro retorno = new ProdutoFlorestalMadeireiro();
			if (value != null) {
				retorno =  repository.findByIdProdutoMadeireiro(new Long(value));
			};
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((ProdutoFlorestalMadeireiro) value).getId() != null){
				return ((ProdutoFlorestalMadeireiro) value).getId().toString();
			}
		}
		return null;
	}
	
}
