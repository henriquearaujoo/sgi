package conversores;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Colaborador_Relatorio;
import model.LancamentoAcao;
import repositorio.ColaboradorRelatorioRepositorio;

import util.CDILocator;


@FacesConverter(forClass = Colaborador_Relatorio.class)
public class ColaboradorRelatorioConverter implements Converter{

	private ColaboradorRelatorioRepositorio repositorio;
	
	public ColaboradorRelatorioConverter() {
		this.repositorio = CDILocator.getBean(ColaboradorRelatorioRepositorio.class);
		
	}
	
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		// TODO Auto-generated method stub
		Colaborador_Relatorio retorno = new Colaborador_Relatorio();
		
		if (value != null) {
			retorno =  repositorio.getCRelatorioById(new Long(value));
		}
		return retorno;	
		
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		
		
		if (value != null) {
			if(((Colaborador_Relatorio) value).getId() != null){
				return ((Colaborador_Relatorio) value).getId().toString();
			}
		}
		return null;
	}
		
		

}
