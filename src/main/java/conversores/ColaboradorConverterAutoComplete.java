package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Colaborador;
import model.Fornecedor;
import model.Produto;
import repositorio.ColaboradorRepositorio;
import repositorio.FornecedorRepositorio;
import repositorio.ProdutoRepositorio;
import util.CDILocator;


@FacesConverter("colaboradorConverter")
public class ColaboradorConverterAutoComplete implements Converter{

	private ColaboradorRepositorio repository;
	
	public ColaboradorConverterAutoComplete(){
		this.repository = CDILocator.getBean(ColaboradorRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			Colaborador retorno = new Colaborador();
	
			if (value != null) {
				retorno =  repository.getColaboradorById(new Long(value));
			}
	 return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((Colaborador) value).getId() != null){
				return ((Colaborador) value).getId().toString();
			}
		}
		return null;
	}
	
}
