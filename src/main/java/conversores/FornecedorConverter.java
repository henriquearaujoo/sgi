package conversores;

import java.util.ArrayList;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import model.Fornecedor;
import repositorio.ContaRepository;
import repositorio.FornecedorRepositorio;
import repositorio.LancamentoRepository;
import util.CDILocator;


@FacesConverter(forClass = Fornecedor.class)
public class FornecedorConverter implements Converter{

	private FornecedorRepositorio repository;
	private ContaRepository contaRepository;
	private LancamentoRepository lancamentoRepository;
	
	public FornecedorConverter(){
		this.repository = CDILocator.getBean(FornecedorRepositorio.class);
		this.contaRepository = CDILocator.getBean(ContaRepository.class);
		this.lancamentoRepository = CDILocator.getBean(LancamentoRepository.class);
	}
	
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
			
			Fornecedor retorno = new Fornecedor();
			
			if (value != null) {
				retorno =  repository.getFornecedorPorId(new Long(value));
				retorno.setContasBancarias(new ArrayList<>());
				retorno.setArquivos(new ArrayList<>());
				retorno.setContasBancarias(contaRepository.buscarContaDiferenteFleg(retorno.getId()));
				retorno.setArquivos(repository.getArquivosByFornecedor(retorno.getId()));
			}
	 return retorno;	
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if(((Fornecedor) value).getId() != null){
				return ((Fornecedor) value).getId().toString();
			}
		}
		return null;
	}

	
	
}
