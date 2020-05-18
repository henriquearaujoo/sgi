package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.inject.Named;

import model.LancamentoAuxiliar;
import service.AprovacoesService;
import util.Filtro;

@ManagedBean
@Named("AprovacoesController")
public class AprovacoesController implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Filtro filtro =  new Filtro();
	
	private AprovacoesService aprovacoesService;
	
	public List<LancamentoAuxiliar> listaLancamentoPrincipal;
	
	
	public void init() {
		filtrarListaPrincipal(filtro);
	}
	
	public void reprovaLancamento(List<LancamentoAuxiliar> listLancamento) {
		aprovacoesService.reprovaLancamentos(listLancamento);
	}
	
	public void aprovarLancamento(List<LancamentoAuxiliar> listLancamento) {
		aprovacoesService.aprovarLancamentos(listLancamento);
	}
	
	public void filtrarListaPrincipal(Filtro filtro) {
		try {
			listaLancamentoPrincipal = aprovacoesService.filtrar(filtro);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}
	
	
	
}
