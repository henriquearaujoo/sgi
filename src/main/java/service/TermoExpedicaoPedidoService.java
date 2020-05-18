package service;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import anotacoes.Transactional;
import model.Estado;
import model.ItemPedido;
import model.Localidade;
import model.Pedido;
import model.TermoExpedicao;
import repositorio.TermoExpedicaoPedidoRepositorio;
import util.Filtro;



public class TermoExpedicaoPedidoService implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private @Inject TermoExpedicaoPedidoRepositorio repositorio;
	
	public TermoExpedicaoPedidoService(){}
	
		
	public List<TermoExpedicao> getTermos(Filtro filtro){
		return repositorio.getTermos(filtro);
	}
	
	@Transactional
	public TermoExpedicao salvarTermo(TermoExpedicao termo){
		return repositorio.salvarTermo(termo);
	}
	
	public TermoExpedicao getTermoById(Long id){
		return repositorio.getTermoById(id);
	}
	
	public List<Pedido> getPedidosByTermo(Long id){
		return repositorio.getPedidosByTermo(id);
	}
	
	public List<Estado> getEstados(Filtro filtro) {
		return repositorio.getEatados(filtro);
	}
	
	public List<Localidade> getMunicipioByEstado(Long id) {
		return repositorio.getMunicipioByEstado(id);
	}
	
	
	@Transactional
	public Boolean ajustaDependendiasDoPedido(Pedido pedido){
		
		try {
			pedido.setTermo(null);
			repositorio.salvarPedido(pedido);
			return true;
		} catch (Exception e) {
			addMessage("", "Erro ao ajustar dependências do pedido de compra", FacesMessage.SEVERITY_ERROR);
			return false;
		}
		
		
	}
	
	
	public Pedido getPedido(Long id){
		return repositorio.getPedido(id);
	}
	
	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}


	public List<ItemPedido> itensPedido(TermoExpedicao expedicao) {
		return repositorio.getItensPedidoByTermo(expedicao);
	}


	public List<ItemPedido> getItemPedidoById(Long id) {
		return repositorio.getItensPedidoById(id);
	}
	
	

}
