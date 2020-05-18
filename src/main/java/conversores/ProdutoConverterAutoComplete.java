package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.FontePagadora;
import model.Produto;
import model.Projeto;
import repositorio.FontePagadoraRepositorio;
import repositorio.ProdutoRepositorio;
import repositorio.ProjetoRepositorio;
import util.CDILocator;


@FacesConverter("produtoConverter")
public class ProdutoConverterAutoComplete implements Converter{

	private ProdutoRepositorio repository;
	
	public ProdutoConverterAutoComplete(){
		this.repository = CDILocator.getBean(ProdutoRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			Produto retorno = new Produto();
	
			if (value != null) {
				retorno =  repository.getProdutoPorId(new Long(value));
			}
	 return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((Produto) value).getId() != null){
				return ((Produto) value).getId().toString();
			}
		}
		return null;
	}
	
}
