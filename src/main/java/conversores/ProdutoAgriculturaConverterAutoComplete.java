package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.CadeiaProdutivaFamiliar;
import model.Comunidade;
import model.Localidade;
import model.ProdutoAgricultura;
import model.SintomaComum;
import repositorio.FamiliarRepository;
import repositorio.LocalRepositorio;
import util.CDILocator;


@FacesConverter("produtoAgriculturaConverter")
public class ProdutoAgriculturaConverterAutoComplete implements Converter{

	private FamiliarRepository repository;
	
	public ProdutoAgriculturaConverterAutoComplete(){
		this.repository = CDILocator.getBean(FamiliarRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
			ProdutoAgricultura retorno = new ProdutoAgricultura();
			if (value != null) {
				retorno =  repository.findByIdProdutoAgricultura(new Long(value));
			};
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((ProdutoAgricultura) value).getId() != null){
				return ((ProdutoAgricultura) value).getId().toString();
			}
		}
		return null;
	}
	
}
