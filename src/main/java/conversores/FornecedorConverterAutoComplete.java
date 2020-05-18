package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Fornecedor;
import model.Produto;
import repositorio.FornecedorRepositorio;
import repositorio.ProdutoRepositorio;
import util.CDILocator;


@FacesConverter("fornecedorConverter")
public class FornecedorConverterAutoComplete implements Converter{

	private FornecedorRepositorio repository;
	
	public FornecedorConverterAutoComplete(){
		this.repository = CDILocator.getBean(FornecedorRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			Fornecedor retorno = new Fornecedor();
	
			if (value != null) {
				retorno =  repository.getFornecedorPorId(new Long(value));
			}
	 return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((Fornecedor) value).getId() != null){
				return ((Fornecedor) value).getId().toString();
			}
		}
		return null;
	}
	
}
