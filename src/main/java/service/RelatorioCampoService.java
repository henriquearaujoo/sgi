package service;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import anotacoes.Transactional;
import model.Atividade;
import model.Colaborador;
import model.Colaborador_Relatorio;
import model.Estado;
import model.Gestao;
import model.Localidade;
import model.Regional;
import model.RelatorioCampo;
import model.SolicitacaoViagem;
import repositorio.AtividadeCampoRepositorio;
import repositorio.ColaboradorRepositorio;
import repositorio.EstadoRepositorio;
import repositorio.GestaoRepositorio;
import repositorio.LocalRepositorio;
import repositorio.RelatorioCampoRepositorio;
import repositorio.ViagemRepositorio;
import util.Filtro;

public class RelatorioCampoService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EstadoRepositorio estadoRepositorio;
	
	@Inject
	private LocalRepositorio localRepositorio;
	
	@Inject
	private ColaboradorRepositorio colaboradorRepositorio;
	
	@Inject 
	private RelatorioCampoRepositorio repositorio;
	
	@Inject
	private ViagemRepositorio viagemRepositorio;
	
	@Inject 
	private AtividadeCampoRepositorio atividadeRepositorio;
	
	@Inject 
	private Colaborador_Relatorio colaboradorRelatorioRepositorio;
	
	@Inject 
	private GestaoRepositorio gestaoRepositorio;
	
	public RelatorioCampoService(){}
	
	@Transactional
	public Boolean Salvar(RelatorioCampo relatorio){
		try{
			
			/*if(relatorio.getCoordenador() == null){
				addMessage("", "Campo Coordenador Obrigatorio", FacesMessage.SEVERITY_ERROR);
			}else{*/
				repositorio.salvar(relatorio);
				return true;
			//}
			
			//return true;
		}catch(Exception e){
			addMessage("", "Salvo Com Sucesso", FacesMessage.SEVERITY_ERROR);
			return false;
		}
	}
	
	
	public List<RelatorioCampo> getRelatorio(){
		return repositorio.getRelatorios();
	}
	
	public Localidade getLocalidade(Long id) {
		return localRepositorio.getLocalPorId(id);
	}
	
	public List<Estado> getEstados(Filtro filtro) {
		return estadoRepositorio.getEatados(filtro);
	}

	public List<Localidade> getMunicipioByEstado(Long id) {
		return localRepositorio.getMunicipioByEstado(id);
	}
	
	
	public List<SolicitacaoViagem> getSolicitacaoViagems(Filtro filtro){
			return viagemRepositorio.getViagems(filtro);
	}
	
	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	
	public List<Colaborador> getColaborador(){
		return colaboradorRepositorio.findAll();
	}
	
	public List<Localidade> getUCs(){
		
		return localRepositorio.getUcs();
	}
	
	public List<Localidade> getComunidades(){
		
		return localRepositorio.getComunidades();
	}
	
	
	public RelatorioCampo getRelatorioById(long id){
		
		return repositorio.relatorioPorId(id);
	}
	
	public List<Atividade> getOficina(){
		return atividadeRepositorio.getOficina();
	}
	
	
	public List<Regional> getRegional(){
		return gestaoRepositorio.getRegionais();
	}
	
	
}
