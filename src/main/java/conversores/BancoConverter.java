package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Banco;
import model.Orcamento;
import repositorio.BancoRepositorio;
import util.CDILocator;

@FacesConverter(forClass = Banco.class )
public class BancoConverter implements Converter {

	
	private BancoRepositorio repositorio;
	
	public BancoConverter(){
		this.repositorio = CDILocator.getBean(BancoRepositorio.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		// TODO Auto-generated method stub
		
		Banco retorno = new Banco();
		
		if(value != null){
			retorno = repositorio.findBancoById(new Long(value));
			
		}
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		
		if(value != null)
		{
			if(((Banco)value).getId() != null){
				return ((Banco)value).getId().toString();
			}
		}
		
		return null;
		
	}
}
