package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.CategoriaDespesaClass;
import model.RubricaOrcamento;
import repositorio.CategoriaRepositorio;
import repositorio.OrcamentoRepositorio;
import repositorio.RubricaRepositorio;
import util.CDILocator;


@FacesConverter("rubricaOrcamentoConverter")
public class RubricaOrcamentoConverter implements Converter{

	private OrcamentoRepositorio repository;
	
	public RubricaOrcamentoConverter(){
		this.repository = CDILocator.getBean(OrcamentoRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			RubricaOrcamento retorno = new RubricaOrcamento();
			
			if (value != null) {
				retorno =  repository.findByIdRubricaOrcamento(new Long(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((RubricaOrcamento) value).getId() != null){
				return ((RubricaOrcamento) value).getId().toString();
			}
		}
		return null;
	}

	
	
}
