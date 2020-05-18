package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.Evento;
import repositorio.EventoRepositorio;
import util.CDILocator;

@FacesConverter(forClass = Evento.class )
public class EventoConverter implements Converter {

	
	private EventoRepositorio repositorio;
	
	public EventoConverter(){
		this.repositorio = CDILocator.getBean(EventoRepositorio.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		Evento retorno = new Evento();
		
		if(value != null){
			retorno = repositorio.findById(new Long(value));
			
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		
		if(value != null)
		{
			if(((Evento)value).getId() != null){
				return ((Evento)value).getId().toString();
			}
		}
		
		return null;
		
	}
}
