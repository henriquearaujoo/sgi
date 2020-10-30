package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import model.Comunidade;
import repositorio.ComunidadeRepositorio;
import util.CDILocator;

@Named(value = "comunidadeConverter")
@FacesConverter(forClass = Comunidade.class)
public class ComunidadeConverter implements Converter {

	private ComunidadeRepositorio repository;

	public ComunidadeConverter() {
		this.repository = CDILocator.getBean(ComunidadeRepositorio.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		Comunidade retorno = new Comunidade();

		if (value != null) {
			retorno = repository.findById(Long.parseLong(value));
		}
		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if (((Comunidade) value).getId() != null) {
				return ((Comunidade) value).getId().toString();
			}
		}
		return null;
	}

}
