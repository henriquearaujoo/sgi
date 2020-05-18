package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.CategoriaDespesaClass;
import model.IdentificacaoFamiliar;
import net.sf.jasperreports.data.cache.NumberValuesUtils;
import repositorio.CategoriaRepositorio;
import repositorio.IdentificacaoFamiliarRepositorio;
import util.CDILocator;
import util.Util;


@FacesConverter("identificacaoFamiliarConverter")
public class IdentificacaoFamiliarConverter implements Converter{

	private IdentificacaoFamiliarRepositorio repository;
	
	public IdentificacaoFamiliarConverter(){
		this.repository = CDILocator.getBean(IdentificacaoFamiliarRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			IdentificacaoFamiliar retorno = new IdentificacaoFamiliar();
			
			if (value != null && Util.isNumeric(value) ) {
				retorno =  repository.findById(new Long(value));
			}else
				retorno.setNome(value);
			
			return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((IdentificacaoFamiliar) value).getId() != null){
				return ((IdentificacaoFamiliar) value).getId().toString();
			}
		}
		return null;
	}
	
}
