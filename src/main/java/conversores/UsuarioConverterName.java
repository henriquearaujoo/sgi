package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import model.User;
import repositorio.UsuarioRepository;
import util.CDILocator;

@FacesConverter("usuarioConverter")
public class UsuarioConverterName implements Converter {

	private UsuarioRepository repository;

	public UsuarioConverterName() {
		this.repository = CDILocator.getBean(UsuarioRepository.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		User retorno;

		if (value != null) {
			retorno = repository.getUsuarioById(Long.parseLong(value));
			return retorno;
		}
		return null;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if (((User) value).getId() != null) {
				return ((User) value).getId().toString();
			}
		}
		return null;
	}

}
