package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.ProjetoRubrica;
import repositorio.OrcamentoRepositorio;
import util.CDILocator;

@FacesConverter("projetoRubricaConverter")
public class ProjetoRubricaConverter implements Converter {

	private OrcamentoRepositorio repositorio;

	public ProjetoRubricaConverter() {
		this.repositorio = CDILocator.getBean(OrcamentoRepositorio.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		// TODO Auto-generated method stub

		ProjetoRubrica retorno = new ProjetoRubrica();

		if (value != null) {
			
				retorno = repositorio.findProjetoRubricaById(new Long(value)); 
			
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (value != null) {
			if (((ProjetoRubrica) value).getId() != null) {
				return ((ProjetoRubrica) value).getId().toString();
			}
		}

		return null;
	}
}
