package service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import model.Estado;
import model.ItemPedido;
import model.Localidade;
import repositorio.CompraDetalhadaRepository;
import repositorio.EstadoRepositorio;
import repositorio.LocalRepositorio;
import util.Filtro;

public class CompraDetalhadaService implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private CompraDetalhadaRepository repository;
	
	@Inject
	private LocalRepositorio localRepositorio;
	
	@Inject
	private EstadoRepositorio estadoRepositorio;
	
	public List<Localidade> getMunicipioByEstado(Long id) {
		return localRepositorio.getMunicipioByEstado(id);
	}
	
	public List<Estado> getEstados(Filtro filtro) {
		return estadoRepositorio.getEatados(filtro);
	}
	
	public List<ItemPedido> getItensPedidos(Filtro filtro) {
		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			if (filtro.getDataInicio().after(filtro.getDataFinal())) {
				addMessage("", "Data inicio maior que data final", FacesMessage.SEVERITY_ERROR);
				return new ArrayList<ItemPedido>();
			}

			Calendar calInicio = Calendar.getInstance();
			calInicio.setTime(filtro.getDataInicio());

			Calendar calFinal = Calendar.getInstance();
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


		} else if (filtro.getDataInicio() != null) {
			Calendar calInicio = Calendar.getInstance();
			calInicio.set(Calendar.HOUR, 0);
			calInicio.set(Calendar.MINUTE, 0);
			calInicio.set(Calendar.SECOND, 0);
			calInicio.set(Calendar.MILLISECOND, 0);
			filtro.setDataInicio(calInicio.getTime());
		}

		return repository.getItensPedidosDetalhados(filtro);
	}
	
	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
}
