package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.TipoViagem;
import repositorio.TipoViagemRepositorio;
import util.CDILocator;

@FacesConverter(forClass = TipoViagem.class)
public class TipoViagemConverter implements Converter {

	
	private TipoViagemRepositorio repository;
	
	public TipoViagemConverter(){
		this.repository = CDILocator.getBean(TipoViagemRepositorio.class);
	}
	
	
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		// TODO Auto-generated method stub
		TipoViagem retorno = new TipoViagem();
			
		if (value != null){
			retorno = repository.TipoViagemPorId(new Long(value));
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		// TODO Auto-generated method stub
		if(value != null)
		{
			if(((TipoViagem)value).getId() != null){
				return ((TipoViagem)value).getId().toString();
			}
		}
		
		return null;
	}

}
