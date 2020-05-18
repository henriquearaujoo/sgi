package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Acao;
import model.FontePagadora;
import repositorio.AcaoRepositorio;
import util.CDILocator;


@FacesConverter("acaoConverter")
public class AcaoConverterAutoComplete implements Converter{

	private AcaoRepositorio repository;
	
	public AcaoConverterAutoComplete(){
		this.repository = CDILocator.getBean(AcaoRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			Acao retorno = new Acao();
	
			if (value != null) {
				retorno =  repository.getAcaoPorId(new Long(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((Acao) value).getId() != null){
				return ((Acao) value).getId().toString();
			}
		}
		return null;
	}
	
}
