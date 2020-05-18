package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Atividade;

import repositorio.AtividadeCampoRepositorio;
import util.CDILocator;

@FacesConverter(forClass = Atividade.class)
public class AtividadeConverter implements Converter {

	
	private AtividadeCampoRepositorio repositorio;
	
	public AtividadeConverter() {
	
		this.repositorio = CDILocator.getBean(AtividadeCampoRepositorio.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		// TODO Auto-generated method stub
		
		Atividade retorno = new Atividade(){
		};
													
													
		if (value != null) {
			retorno =  repositorio.getAtividadeById(new Long(value));
		}
		return retorno;
		
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		// TODO Auto-generated method stub
		if (value != null) {
			if(((Atividade) value).getId() != null){
				return ((Atividade) value).getId().toString();
			}
		}
		return null;
	}

	
	
}
