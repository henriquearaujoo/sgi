package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.ContaBancaria;
import repositorio.ContaRepository;
import util.CDILocator;


@FacesConverter(forClass = ContaBancaria.class)
public class ContaConverter2 implements Converter{

	private ContaRepository repository;
	
	public ContaConverter2(){
		this.repository = CDILocator.getBean(ContaRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			ContaBancaria retorno = new ContaBancaria();
	
			if (value != null) {
				retorno =  repository.getContaById(new Long(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((ContaBancaria) value).getId() != null){
				return ((ContaBancaria) value).getId().toString();
			}
		}
		return null;
	}
	
}
