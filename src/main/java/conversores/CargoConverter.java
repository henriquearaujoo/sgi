package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import model.Cargo;
import repositorio.CargoRepositorio;
import util.CDILocator;
@Named(value = "cargoConverter")
@FacesConverter(forClass = Cargo.class)
public class CargoConverter implements Converter {

	private CargoRepositorio repository;

	public CargoConverter() {
		this.repository = CDILocator.getBean(CargoRepositorio.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		Cargo retorno = new Cargo();

		if (value != null) {
			retorno = repository.findById(Long.parseLong(value));
		}
		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if (((Cargo) value).getId() != null) {
				return ((Cargo) value).getId().toString();
			}
		}
		return null;
	}

}
