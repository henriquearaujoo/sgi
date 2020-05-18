package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.SolicitacaoViagem;
import repositorio.ViagemRepositorio;
import util.CDILocator;


@FacesConverter(forClass = SolicitacaoViagem.class)
public class SolicitacaoViagemConverter implements Converter{

	private ViagemRepositorio repository;
	
	public SolicitacaoViagemConverter(){
		this.repository = CDILocator.getBean(ViagemRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			SolicitacaoViagem retorno = new SolicitacaoViagem();
			
			if (value != null) {
				retorno =  repository.getViagemPorId(new Long(value));
				
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((SolicitacaoViagem) value).getId() != null){
				return ((SolicitacaoViagem) value).getId().toString();
			}
		}
		return null;
	}

	
	
}
