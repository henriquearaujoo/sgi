package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import model.Regional;
import repositorio.GestaoRepositorio;

import util.CDILocator;

@Named("regionalConverter")
@FacesConverter(forClass = Regional.class)
public class RegionalConverter implements Converter {

	private GestaoRepositorio repository;

	public RegionalConverter() {
		this.repository = CDILocator.getBean(GestaoRepositorio.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		Regional retorno = new Regional();

		if (value != null) {
			retorno = repository.findByIdRegional(Long.parseLong(value));
		}
		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if (((Regional) value).getId() != null) {
				return ((Regional) value).getId().toString();
			}
		}
		return null;
	}

}
