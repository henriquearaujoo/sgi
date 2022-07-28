package conversores;

import model.UnidadeConservacao;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import util.CDILocator;
import repositorio.LocalRepositorio;
import javax.faces.convert.FacesConverter;
import javax.faces.convert.Converter;

@FacesConverter("uc_converter")
public class UcConverter implements Converter
{
	private LocalRepositorio repository;

	public UcConverter() {
		this.repository = (LocalRepositorio)CDILocator.getBean((Class)LocalRepositorio.class);
	}

	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		UnidadeConservacao retorno = new UnidadeConservacao();
		if (value != null) {
			retorno = this.repository.findUcByCodigo(new Long(value));
		}
		return retorno;
	}

	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
		if (value != null && ((UnidadeConservacao)value).getId() != null) {
			return ((UnidadeConservacao)value).getId().toString();
		}
		return null;
	}
}