package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.QualifProjeto;
import repositorio.ProjetoRepositorio;
import util.CDILocator;

@FacesConverter("qualifConverter")
public class QualifConverter implements Converter {

	
	private ProjetoRepositorio repositorio;
	
	public QualifConverter(){
		this.repositorio = CDILocator.getBean(ProjetoRepositorio.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		// TODO Auto-generated method stub
		
		QualifProjeto retorno = new QualifProjeto();
		
		if(value != null){
			retorno = repositorio.findByIdQualif(new Long(value));
			
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		
		if(value != null)
		{
			if(((QualifProjeto)value).getId() != null){
				return ((QualifProjeto)value).getId().toString();
			}
		}
		
		return null;
		
	}
}
