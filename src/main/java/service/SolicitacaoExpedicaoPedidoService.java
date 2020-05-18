package service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.transaction.TransactionScoped;

import anotacoes.Transactional;
import model.ControleExpedicao;
import model.LocalizacaoPedido;
import model.Pedido;
import model.SolicitacaoExpedicao;
import repositorio.SolicitacaoExpedicaoPedidoRepositorio;
import util.Filtro;



public class SolicitacaoExpedicaoPedidoService implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private @Inject SolicitacaoExpedicaoPedidoRepositorio repositorio;
	
	public SolicitacaoExpedicaoPedidoService(){}
	
	
	public List<SolicitacaoExpedicao> getSolicitacoes(Filtro filtro){
		
		if(filtro.getLancamentoID() != null){
			Pedido pedido = repositorio.getPedido(filtro.getLancamentoID());
			if(pedido != null){	
				if (pedido.getSolicitacaoExpedicao() != null) {
					filtro.setSolicitacaoExpedicaoID(pedido.getSolicitacaoExpedicao().getId());
				}else{
					addMessage("", "Não existem solicitações para o pedido "+filtro.getLancamentoID(), FacesMessage.SEVERITY_WARN);
					return new ArrayList<>();
				}
			}else{
				addMessage("", "Pedido "+filtro.getLancamentoID()+" não encontrado no banco de dados.", FacesMessage.SEVERITY_WARN);
				return new ArrayList<>();
			}	
		}
		
		
		return repositorio.getSolicitacoes(filtro);
	}
	
	@Transactional
	public SolicitacaoExpedicao salvar(SolicitacaoExpedicao solicitacaoExpedicao){
		return repositorio.salvarSolicitacao(solicitacaoExpedicao);
	}
	
	
	
	public SolicitacaoExpedicao aceitarSolicitacao(SolicitacaoExpedicao solicitacaoExpedicao){
	
		/*List<Pedido> pedidos = repositorio.getPedidoBySolicitacao(solicitacaoExpedicao.getId());
		for (Pedido pedido : pedidos) {
			Pedido pedidoAux = repositorio.getPedido(pedido.getId());
			if (pedido.getControleExpedicao() == null) {
				
				ControleExpedicao controleExpedicao =  new ControleExpedicao();
				controleExpedicao.setPedido(pedidoAux);
				controleExpedicao.setDataAceitacaoExpedicao(new Date());
				controleExpedicao.setLocalizacaoPedido(LocalizacaoPedido.N_INICIADO);
				controleExpedicao = repositorio.salvarControleExpedicao(controleExpedicao);
				pedidoAux.setControleExpedicao(new ControleExpedicao());
				pedidoAux.setControleExpedicao(controleExpedicao);
				
				repositorio.salvarPedido(pedidoAux);
				
				pedidoAux = new Pedido();
			}
			
		}*/
	
		return repositorio.salvarSolicitacao(solicitacaoExpedicao);
	}
	
	
	public void salvarPedidosEControlesExpedicao(SolicitacaoExpedicao solicitacaoExpedicao){
		List<Pedido> pedidos = repositorio.getPedidoBySolicitacao(solicitacaoExpedicao.getId());
		for (Pedido pedido : pedidos) {
			Pedido pedidoAux = repositorio.getPedido(pedido.getId());
			if (pedido.getControleExpedicao() == null) {
				
				ControleExpedicao controleExpedicao =  new ControleExpedicao();
				controleExpedicao.setPedido(pedidoAux);
				controleExpedicao.setDataAceitacaoExpedicao(new Date());
				controleExpedicao.setLocalizacaoPedido(LocalizacaoPedido.N_INICIADO);
				controleExpedicao = repositorio.salvarControleExpedicao(controleExpedicao);
				pedidoAux.setControleExpedicao(new ControleExpedicao());
				pedidoAux.setControleExpedicao(controleExpedicao);
				
				repositorio.salvarPedido(pedidoAux);
				
				pedidoAux = new Pedido();
			}
			
		}
	}
	
	public Pedido getPedido(Long id){
		return repositorio.getPedido(id);
	}
 	
	public SolicitacaoExpedicao getSolicitacaoById(Long id){
		return repositorio.getSolicitacao(id);
	}
	
	public List<Pedido> getPedidosBySolicitacao(Long id){
		return repositorio.getPedidoBySolicitacao(id);
	}
	
	@Transactional
	public void removerSolicitacao(SolicitacaoExpedicao solicitacao){
		
		/*List<Pedido> list = repositorio.getPedidoBySolicitacao(solicitacao.getId());
		
		for (Pedido pedido : list) {
		  pedido.setSolicitacaoExpedicao(null);
		  repositorio.salvarPedido(pedido);
		}*/
		
		
		repositorio.removerSolicitacao(solicitacao);
	}
	
	@Transactional
	public void removerPedidosBySolicitacao(Long id){
		List<Pedido> list = repositorio.getPedidoBySolicitacao(id);
		
		for (Pedido pedido : list) {
		  pedido.setSolicitacaoExpedicao(null);
		  pedido.setControleExpedicao(null);
		  repositorio.salvarPedido(pedido);
		}
		//return repositorio.getPedidoBySolicitacao(id);
	}
	
	@Transactional
	public Boolean ajustaDependendiasDoPedido(Pedido pedido){
		
		try {
			pedido.setSolicitacaoExpedicao(null);
			pedido.setControleExpedicao(null);
			repositorio.salvarPedido(pedido);
			return true;
		} catch (Exception e) {
			addMessage("", "Erro ao ajustar dependências do pedido de compra", FacesMessage.SEVERITY_ERROR);
			return false;
		}
		
		
	}
	
	
	
	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	

}
