package conversores;

import java.util.ArrayList;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Compra;
import model.ItemCompra;
import repositorio.LancamentoRepository;
import util.CDILocator;


@FacesConverter(forClass = Compra.class)
public class CompraConverter implements Converter{

	private LancamentoRepository repository;
	
	public CompraConverter(){
		this.repository = CDILocator.getBean(LancamentoRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			Compra retorno = new Compra();
			
			if (value != null) {
				retorno =  repository.getCompraPorId(new Long(value));
				retorno.setItens(new ArrayList<ItemCompra>());
				retorno.setItens(repository.getItensByCompra(retorno));
				retorno.setArquivos(new ArrayList<>());
				retorno.setArquivos(repository.getArquivosByLancamento(retorno.getId()));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((Compra) value).getId() != null){
				return ((Compra) value).getId().toString();
			}
		}
		return null;
	}

	
	
}
