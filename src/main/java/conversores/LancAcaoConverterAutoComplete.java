package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.LancamentoAcao;
import model.Projeto;
import repositorio.LancamentoRepository;
import repositorio.ProjetoRepositorio;
import util.CDILocator;


@FacesConverter("lancAcaoConverter")
public class LancAcaoConverterAutoComplete implements Converter{
			
	private LancamentoRepository repository;
	
	public LancAcaoConverterAutoComplete(){
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
