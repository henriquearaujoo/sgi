package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Perfil;
import repositorio.PerfilRepositorio;
import util.CDILocator;

@FacesConverter("perfilConverter")
public class PerfilConverter implements Converter {

		private PerfilRepositorio repository;
		
		public PerfilConverter(){
			this.repository = CDILocator.getBean(PerfilRepositorio.class);
		}
		
		
		
		@Override
		public Object getAsObject(FacesContext context, UIComponent component, String value) {
			// TODO Auto-generated method stub
			Perfil retorno = new Perfil();
				
			if (value != null){
				retorno = repository.getPerfil(new Long(value));
			}
			
			return retorno;
		}

		@Override
		public String getAsString(FacesContext context, UIComponent component, Object value) {
			// TODO Auto-generated method stub
			if(value != null)
			{
				if(((Perfil)value).getId() != null){
					return ((Perfil)value).getId().toString();
				}
			}
			
			return null;
		}

	}


