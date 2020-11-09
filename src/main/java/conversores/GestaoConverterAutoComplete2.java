package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.hibernate.metamodel.domain.Superclass;

import model.Gestao;
import model.Localidade;
import model.Projeto;
import repositorio.GestaoRepositorio;
import repositorio.LocalRepositorio;
import util.CDILocator;


@FacesConverter("gestaoConverterVI")
public class GestaoConverterAutoComplete2 implements Converter{

	private GestaoRepositorio repository;
	
	public GestaoConverterAutoComplete2(){
		this.repository = CDILocator.getBean(GestaoRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			Gestao retorno =  new Gestao();
	
			if (value != null) {
				retorno =  repository.findByIdGestao(Long.parseLong(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((Gestao) value).getId() != null){
				return ((Gestao) value).getId().toString();
			}
		}
		return null;
	}
	
}
