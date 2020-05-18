package service;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import anotacoes.Transactional;
import model.CadeiaProdutivaFamiliar;
import model.Colaborador;
import model.Comunidade;
import model.CriacaoAnimal;
import model.Familiar;
import model.IdentificacaoFamiliar;
import model.Localidade;
import model.Municipio;
import model.Perfil;
import model.ProdutoAgricultura;
import model.ProdutoFlorestalMadeireiro;
import model.ProdutoFlorestalNaoMadeireiro;
import model.ProdutoPescado;
import model.SintomaComum;
import model.UnidadeConservacao;
import model.User;
import repositorio.ColaboradorRepositorio;
import repositorio.FamiliarRepository;
import repositorio.LocalRepositorio;
import repositorio.UsuarioRepository;
import util.Filtro;

public class FamiliarService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject 
	private FamiliarRepository repositorio;
	
	@Inject
	private LocalRepositorio local;
		
	
	public FamiliarService(){
		
	}
		
	@Transactional
	public Boolean Salvar(Familiar familiar){
		try{
			repositorio.salvar(familiar);
			return true;
		}catch(Exception e){
			return false;
		}
		
		
	}
	
	public List<Familiar> getFamiliar(){
		return repositorio.getFamiliar();
	}
	
	public List<Familiar> getFamiliarComFiltro(String protocolo, UnidadeConservacao uc, Comunidade comunidade){
		return repositorio.getFamiliarComFiltro(protocolo, uc, comunidade);
	}
	
	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Familiar getFamiliarById(Long id) {
		return repositorio.getFamiliarById(id);
	}
	
	public List<Familiar> getFamiliarPorNumeroCadastro(Integer numero){
		return repositorio.getFamiliarPorNumeroCadastro(numero);
	}
	
	public List<IdentificacaoFamiliar> getMembros(Long idFamiliar){
		return repositorio.getMembros(idFamiliar);
	}
	
	@Transactional
	public boolean salvar(Familiar familiar) {
		try{
			repositorio.salvar(familiar);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	@Transactional
	public boolean update(Familiar familiar) {
		try{
			repositorio.update(familiar);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	public List<Comunidade> buscarComunidades(String s){
		return local.buscarComunidades(s);
	}
	
	public List<UnidadeConservacao> buscarUnidadesConservacao(String s){
		return local.buscarUnidadesConservacao(s);
	}
	
	public List<Municipio> buscarMunicipios(String s){
		return local.buscarMunicipios(s);
	}
	
	public List<Localidade> buscarLocalidades(String s){
		return local.buscarLocalidades(s);
	}
	
	public List<Localidade> buscarPolos(String s){
		return local.buscarPolos(s);
	}
	
	public List<Localidade> buscarLocalidadesPorUc(String s, Long idUc){
		return local.buscarLocalidadesPorUc(s, idUc);
	}
	
	public List<SintomaComum> buscarSintomas(String s){
		return repositorio.buscarSintomas(s);
	}
	
	public List<CadeiaProdutivaFamiliar> buscarCadeias(String s){
		return repositorio.buscarCadeias(s);
	}
	
	public List<ProdutoAgricultura> buscarProdutosAgricultura(String s){
		return repositorio.buscarProdutosAgricultura(s);
	}
	
	public List<ProdutoPescado> buscarProdutosPescado(String s){
		return repositorio.buscarProdutosPescado(s);
	}
	
	public List<CriacaoAnimal> buscarCricoesAnimal(String s){
		return repositorio.buscarCricoesAnimal(s);
	}
	
	public List<ProdutoFlorestalMadeireiro> buscarProdutosMadeireiros(String s){
		return repositorio.buscarProdutosMadeireiros(s);
	}
	
	public List<ProdutoFlorestalNaoMadeireiro> buscarProdutosNaoMadeireiros(String s){
		return repositorio.buscarProdutosNaoMadeireiros(s);
	}
	
	public Boolean verificaCPFCadastrado(String cpf, Long id){
		return repositorio.verificaCPFCadastrado(cpf, id);
	}
	
	public Integer getSequencialFamiliasUC(Long idUc){
		return repositorio.getSequencialFamiliasUC(idUc);
	}
	
	public Boolean existeProtocolo(String protocolo){
		return repositorio.existeProtocolo(protocolo);
	}
}
