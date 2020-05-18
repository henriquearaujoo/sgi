package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Veiculo;
import repositorio.VeiculoRepositorio;
import util.CDILocator;

@FacesConverter(forClass = Veiculo.class)
public class VeiculoConverter implements Converter {

	private VeiculoRepositorio repository;

	public VeiculoConverter() {
		this.repository = CDILocator.getBean(VeiculoRepositorio.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		// TODO Auto-generated method stub

		Veiculo retorno = new Veiculo();

		if (value != null) {
			retorno = repository.getVeiculoPorId(new Long(value));
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		// TODO Auto-generated method stub
		if (value != null) {
			if (((Veiculo) value).getId() != null) {
				return ((Veiculo) value).getId().toString();
			}
		}

		return null;
	}

}
