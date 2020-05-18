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
import model.ProdutoFlorestalNaoMadeireiro;
import model.ProdutoPescado;
import model.SintomaComum;
import repositorio.FamiliarRepository;
import repositorio.LocalRepositorio;
import util.CDILocator;


@FacesConverter("produtoNaoMadeireiroConverter")
public class ProdutoNaoMadeireiroConverterAutoComplete implements Converter{

	private FamiliarRepository repository;
	
	public ProdutoNaoMadeireiroConverterAutoComplete(){
		this.repository = CDILocator.getBean(FamiliarRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
			ProdutoFlorestalNaoMadeireiro retorno = new ProdutoFlorestalNaoMadeireiro();
			if (value != null) {
				retorno =  repository.findByIdProdutoNaoMadeireiro(new Long(value));
			};
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((ProdutoFlorestalNaoMadeireiro) value).getId() != null){
				return ((ProdutoFlorestalNaoMadeireiro) value).getId().toString();
			}
		}
		return null;
	}
	
}
