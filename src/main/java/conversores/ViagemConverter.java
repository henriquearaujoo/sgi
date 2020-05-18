package conversores;

import javax.faces.convert.FacesConverter;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.SolicitacaoViagem;
import model.Veiculo;
import repositorio.ViagemRepositorio;
import util.CDILocator;

@FacesConverter(forClass = SolicitacaoViagem.class )
public class ViagemConverter implements Converter {

	
	private ViagemRepositorio repositorio;
	
	public ViagemConverter(){
		this.repositorio = CDILocator.getBean(ViagemRepositorio.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		// TODO Auto-generated method stub
		
		SolicitacaoViagem retorno = new SolicitacaoViagem();
		
		if(value != null){
			
			retorno = repositorio.getViagemPorId(new Long(value));
			
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		
		if(value != null)
		{
			if(((SolicitacaoViagem)value).getId() != null){
				return ((SolicitacaoViagem)value).getId().toString();
			}
		}
		
		return null;
		
	}
	
	
	
}
