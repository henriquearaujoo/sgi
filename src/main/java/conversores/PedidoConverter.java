package conversores;

import java.util.ArrayList;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Compra;
import model.ItemCompra;
import model.Pedido;
import repositorio.LancamentoRepository;
import util.CDILocator;


@FacesConverter(forClass = Pedido.class)
public class PedidoConverter implements Converter{

	private LancamentoRepository repository;
	
	public PedidoConverter(){
		this.repository = CDILocator.getBean(LancamentoRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			Pedido retorno = new Pedido();
			
			if (value != null) {
				retorno =  repository.getPedidoPorId(new Long(value));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((Pedido) value).getId() != null){
				return ((Pedido) value).getId().toString();
			}
		}
		return null;
	}

	
	
}
