package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.LancamentoDiversos;
import repositorio.LancamentoRepository;
import util.CDILocator;


@FacesConverter(forClass = LancamentoDiversos.class)
public class LancDiversosConverter implements Converter{

	private LancamentoRepository repository;
	
	public LancDiversosConverter(){
		this.repository = CDILocator.getBean(LancamentoRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			LancamentoDiversos retorno = new LancamentoDiversos();
	
			if (value != null) {
				retorno =  repository.getLancamentoDiversosPorId(new Long(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((LancamentoDiversos) value).getId() != null){
				return ((LancamentoDiversos) value).getId().toString();
			}
		}
		return null;
	}
	
}
