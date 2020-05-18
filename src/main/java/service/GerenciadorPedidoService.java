package service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import anotacoes.Transactional;
import model.Compra;
import model.Estado;
import model.ItemPedido;
import model.Localidade;
import model.Pedido;
import model.SolicitacaoExpedicao;
import repositorio.EstadoRepositorio;
import repositorio.GerenciadorPedidoRepositorio;
import repositorio.LocalRepositorio;
import util.Filtro;



public class GerenciadorPedidoService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private @Inject GerenciadorPedidoRepositorio gerenciadorRepositorio;
	private @Inject EstadoRepositorio estadoRepositorio;
	private @Inject LocalRepositorio localRepositorio;
	
	public GerenciadorPedidoService(){}
	
	public List<Pedido> getPedidos(Filtro filtro){
		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			 if (filtro.getDataInicio().after(filtro.getDataFinal())) {
				addMessage("", "Data inicio maior que data final", FacesMessage.SEVERITY_ERROR);
				return new ArrayList<Pedido>();
			 }
			
			 Calendar calInicio =  Calendar.getInstance();
			 calInicio.setTime(filtro.getDataInicio());
			 
			 Calendar calFinal =  Calendar.getInstance();
			 calFinal.setTime(filtro.getDataFinal());
			 
			 
			 calInicio.set(Calendar.HOUR, 0);
			 calInicio.set(Calendar.MINUTE, 0);
			 calInicio.set(Calendar.SECOND, 0);
			 calInicio.set(Calendar.MILLISECOND, 0);
			 
			 calFinal.set(Calendar.HOUR, 24);
			 calFinal.set(Calendar.MINUTE, 0);
			 calFinal.set(Calendar.SECOND, 0);
			 calFinal.set(Calendar.MILLISECOND, 0);
			 
			 filtro.setDataInicio(calInicio.getTime()); 
			 filtro.setDataFinal(calFinal.getTime()); 
			 
			 
			 System.out.println(filtro.getDataInicio());
			 System.out.println(filtro.getDataFinal());
			
		}else if(filtro.getDataInicio() != null){
			 Calendar calInicio =  Calendar.getInstance();
			 calInicio.set(Calendar.HOUR, 0);
			 calInicio.set(Calendar.MINUTE, 0);
			 calInicio.set(Calendar.SECOND, 0);
			 calInicio.set(Calendar.MILLISECOND, 0);
			 filtro.setDataInicio(calInicio.getTime());
		}

		
		return gerenciadorRepositorio.getPedidosFilter(filtro);
	}

	
	public List<ItemPedido> getItensPedidos(Filtro filtro){
		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			 if (filtro.getDataInicio().after(filtro.getDataFinal())) {
				addMessage("", "Data inicio maior que data final", FacesMessage.SEVERITY_ERROR);
				return new ArrayList<ItemPedido>();
			 }
			
			 Calendar calInicio =  Calendar.getInstance();
			 calInicio.setTime(filtro.getDataInicio());
			 
			 Calendar calFinal =  Calendar.getInstance();
			 calFinal.setTime(filtro.getDataFinal());
			 
			 
			 calInicio.set(Calendar.HOUR, 0);
			 calInicio.set(Calendar.MINUTE, 0);
			 calInicio.set(Calendar.SECOND, 0);
			 calInicio.set(Calendar.MILLISECOND, 0);
			 
			 calFinal.set(Calendar.HOUR, 24);
			 calFinal.set(Calendar.MINUTE, 0);
			 calFinal.set(Calendar.SECOND, 0);
			 calFinal.set(Calendar.MILLISECOND, 0);
			 
			 filtro.setDataInicio(calInicio.getTime()); 
			 filtro.setDataFinal(calFinal.getTime()); 
			 
			 
			 System.out.println(filtro.getDataInicio());
			 System.out.println(filtro.getDataFinal());
			
		}else if(filtro.getDataInicio() != null){
			 Calendar calInicio =  Calendar.getInstance();
			 calInicio.set(Calendar.HOUR, 0);
			 calInicio.set(Calendar.MINUTE, 0);
			 calInicio.set(Calendar.SECOND, 0);
			 calInicio.set(Calendar.MILLISECOND, 0);
			 filtro.setDataInicio(calInicio.getTime());
		}

		return gerenciadorRepositorio.getItensPedidosDetalhados(filtro);
	}
	
	public SolicitacaoExpedicao salvarSolicitacao(SolicitacaoExpedicao solicitacao){
		return gerenciadorRepositorio.salvarSolicitacao(solicitacao);
	}
	
	public List<Localidade> getMunicipioByEstado(Long id) {
		return localRepositorio.getMunicipioByEstado(id);
	}
	
	@Transactional
	public void editarPedido(Pedido pedido){
		gerenciadorRepositorio.updatePedido(pedido);
	}
	
	public List<Estado> getEstados(Filtro filtro) {
		return estadoRepositorio.getEatados(filtro);
	}
	

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
}
