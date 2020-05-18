package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.CategoriaProjeto;
import repositorio.ProjetoRepositorio;
import util.CDILocator;

@FacesConverter(forClass = CategoriaProjeto.class)
public class CategoriaProjetoConverter implements Converter {

	private ProjetoRepositorio repository;

	public CategoriaProjetoConverter() {
		this.repository = CDILocator.getBean(ProjetoRepositorio.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		CategoriaProjeto retorno = new CategoriaProjeto();

		if (value != null) {
			retorno = repository.getCategoriaProjetoById(new Long(value));
		}
		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if (((CategoriaProjeto) value).getId() != null) {
				return ((CategoriaProjeto) value).getId().toString();
			}
		}
		return null;
	}

}
