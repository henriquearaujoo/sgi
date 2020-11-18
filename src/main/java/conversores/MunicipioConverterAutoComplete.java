package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import model.Municipio;
import repositorio.MunicipioRepositorio;
import util.CDILocator;

@Named(value = "municipioConverter")
@FacesConverter(forClass = Municipio.class)
public class MunicipioConverterAutoComplete implements Converter {

	private MunicipioRepositorio repository;

	public MunicipioConverterAutoComplete() {
		this.repository = CDILocator.getBean(MunicipioRepositorio.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		Municipio retorno = new Municipio();
		if (value != null) {
			retorno = repository.findByIdMunicipio(Long.parseLong(value));
		}

		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			try {
				Municipio m = (Municipio) value;
				return m.getId().toString();
			} catch (Exception e) {
				return "erro: " + e;
			}
		}
		return null;
	}

}
