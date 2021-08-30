package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Localidade;
import model.Projeto;
import repositorio.ProjetoRepositorio;
import repositorio.management.panel.models.Project;
import util.CDILocator;


@FacesConverter("panelProjectConverter")
public class PanelProject implements Converter{

	private ProjetoRepositorio repository;
	
	public PanelProject(){
		this.repository = CDILocator.getBean(ProjetoRepositorio.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
			Project retorno =  new Project();
			if (value != null) {
				Projeto projeto =  repository.getProjetoPorId(new Long(value));
				retorno = new Project(projeto.getId(), projeto.getCodigo(), projeto.getNome());
			};
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((Project) value).getId() != null){
				return ((Project) value).getId().toString();
			}
		}
		return null;
	}
	
}

