package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.CustoPessoal;
import repositorio.CustoPessoalRepositorio;
import util.CDILocator;


@FacesConverter(forClass = CustoPessoal.class)
public class CustoPessoalConverter2 implements Converter{

	private CustoPessoalRepositorio repository;
	
	public CustoPessoalConverter2(){
		this.repository = CDILocator.getBean(CustoPessoalRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			CustoPessoal retorno = new CustoPessoal();
	
			if (value != null) {
				retorno =  repository.findById(new Long(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((CustoPessoal) value).getId() != null){
				return ((CustoPessoal) value).getId().toString();
			}
		}
		return null;
	}
	
}
