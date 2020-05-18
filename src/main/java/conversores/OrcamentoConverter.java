package conversores;

import javax.faces.convert.FacesConverter;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Orcamento;
import model.SolicitacaoViagem;
import model.Veiculo;
import repositorio.OrcamentoRepositorio;
import repositorio.ViagemRepositorio;
import util.CDILocator;

@FacesConverter(forClass = Orcamento.class )
public class OrcamentoConverter implements Converter {

	
	private OrcamentoRepositorio repositorio;
	
	public OrcamentoConverter(){
		this.repositorio = CDILocator.getBean(OrcamentoRepositorio.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		// TODO Auto-generated method stub
		
		Orcamento retorno = new Orcamento();
		
		if(value != null){
			retorno = repositorio.findById(new Long(value));
			
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		
		if(value != null)
		{
			if(((Orcamento)value).getId() != null){
				return ((Orcamento)value).getId().toString();
			}
		}
		
		return null;
		
	}
	
	
	
}
