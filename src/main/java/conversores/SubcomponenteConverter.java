package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import model.SubComponente;
import repositorio.ComponenteRepositorio;
import util.CDILocator;

@Named(value = "subcomponente_converter")
@FacesConverter(forClass = SubComponente.class)
public class SubcomponenteConverter implements Converter {

	private ComponenteRepositorio repository;

	public SubcomponenteConverter() {
		this.repository = CDILocator.getBean(ComponenteRepositorio.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		SubComponente retorno = new SubComponente();

		if (value != null) {
			retorno = repository.findByIdSubComponente(Long.parseLong(value));
		}
		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if (((SubComponente) value).getId() != null) {
				return ((SubComponente) value).getId().toString();
			}
		}
		return null;
	}

}
