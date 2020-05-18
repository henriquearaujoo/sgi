package conversores;

import java.util.ArrayList;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.SolicitacaoPagamento;
import repositorio.LancamentoRepository;
import util.CDILocator;


@FacesConverter(forClass = SolicitacaoPagamento.class)
public class PagamentoConverter implements Converter{

	private LancamentoRepository repository;
	
	public PagamentoConverter(){
		this.repository = CDILocator.getBean(LancamentoRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			SolicitacaoPagamento retorno = new SolicitacaoPagamento();
			
			if (value != null) {
				retorno =  repository.getPagamentoPorId(new Long(value));
				retorno.setArquivos(new ArrayList<>());
				retorno.setArquivos(repository.getArquivosByLancamento(retorno.getId()));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((SolicitacaoPagamento) value).getId() != null){
				return ((SolicitacaoPagamento) value).getId().toString();
			}
		}
		return null;
	}

	
	
}
