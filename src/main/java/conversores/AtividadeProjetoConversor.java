package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.AtividadeProjeto;
import repositorio.AtividadeRepositorio;
import util.CDILocator;

@FacesConverter("atividadeProjetoConverter")
public class AtividadeProjetoConversor implements Converter{

	

	

		private AtividadeRepositorio repository;
		
		public AtividadeProjetoConversor(){
			this.repository = CDILocator.getBean(AtividadeRepositorio.class);
		}
		
		
		@Override
		public Object getAsObject(FacesContext context, UIComponent component, String value) {
				
			AtividadeProjeto retorno = new AtividadeProjeto();
		
				if (value != null) {
					retorno =  repository.getAtividadeById(new Long(value));
				}
		 return retorno;	
		}
		
		@Override
		public String getAsString(FacesContext context, UIComponent component, Object value) {
			if (value != null) {
				if(((AtividadeProjeto) value).getId() != null){
					return ((AtividadeProjeto) value).getId().toString();
				}
			}
			return null;
		}
		
	}
		
	

	
	

