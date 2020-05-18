package service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import model.Compra;
import model.LancamentoAcao;
import model.LancamentoAuxiliar;
import model.PagamentoLancamento;
import repositorio.LancamentoRepository;
import util.ArquivoLancamento;
import util.Filtro;

public class GerenciadorLancamentoService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private @Inject LancamentoRepository repositorio;
	
	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public List<LancamentoAuxiliar> getLancamentos(Filtro filtro){
		
		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			 if (filtro.getDataInicio().after(filtro.getDataFinal())) {
				addMessage("", "Data inicio maior que data final", FacesMessage.SEVERITY_ERROR);
				return new ArrayList<LancamentoAuxiliar>();
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

		
		
	 return repositorio.getlancamentosParaGerenciar(filtro);	
	}
	
	public List<LancamentoAcao> getLancamentosEmAcoes(Long id){
		return repositorio.getLancamentosEmAcoes(id);
	}
	
	public List<PagamentoLancamento> getPagamentosINLancamentos(Long id){
		return repositorio.getPagamentosINLancamento(id);
	}
	
	public List<ArquivoLancamento> getDocumentosByLancamento(Long id){
		return repositorio.getDocumentosByLancmento(id);
	}
	
	
}

