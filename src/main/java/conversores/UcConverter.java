package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

import model.Localidade;
import model.UnidadeConservacao;
import repositorio.LocalRepositorio;
import repositorio.UnidadeConservacaoRepositorio;
import util.CDILocator;

@Named(value = "uc_converter")
@FacesConverter(forClass = UnidadeConservacao.class)
public class UcConverter implements Converter {

	private UnidadeConservacaoRepositorio repository;

	public UcConverter() {
		this.repository = CDILocator.getBean(UnidadeConservacaoRepositorio.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		UnidadeConservacao retorno = new UnidadeConservacao();
		if (value != null) {
			retorno = repository.findById(Long.parseLong(value));
		}
		;
		return retorno;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if (((UnidadeConservacao) value).getId() != null) {
				return ((UnidadeConservacao) value).getId().toString();
			}
		}
		return null;
	}

}
