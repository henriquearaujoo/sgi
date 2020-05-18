package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.LancamentoAcao;
import repositorio.LancamentoRepository;
import util.CDILocator;


@FacesConverter(forClass = LancamentoAcao.class)
public class LancamentoAcaoConverter implements Converter{

	private LancamentoRepository repository;
	
	public LancamentoAcaoConverter(){
		this.repository = CDILocator.getBean(LancamentoRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			LancamentoAcao retorno = new LancamentoAcao();
			
			if (value != null) {
				retorno =  repository.getLancamentoAcaoPorId(new Long(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((LancamentoAcao) value).getId() != null){
				return ((LancamentoAcao) value).getId().toString();
			}
		}
		return null;
	}

	
	
}
