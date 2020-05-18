package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.CategoriaDespesaClass;
import model.Projeto;
import model.UnidadeDeCompra;
import repositorio.CategoriaRepositorio;
import util.CDILocator;


@FacesConverter("unidadeCompraConverter")
public class UnidadeCompraConverter implements Converter{

	private CategoriaRepositorio repository;
	
	public UnidadeCompraConverter(){
		this.repository = CDILocator.getBean(CategoriaRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			UnidadeDeCompra retorno = new UnidadeDeCompra();
			
			if (value != null) {
				retorno =  repository.findUndById(new Long(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((UnidadeDeCompra) value).getId() != null){
				return ((UnidadeDeCompra) value).getId().toString();
			}
		}
		return null;
	}

	
	
}
