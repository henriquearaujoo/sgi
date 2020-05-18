package service;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import anotacoes.Transactional;
import model.AtividadeRenda;
import model.CadeiaProdutivaFamiliar;
import model.Comunidade;
import model.Comunitario;
import model.CriacaoAnimal;
import model.Familiar;
import model.FonteAguaConsumo;
import model.FonteEnergia;
import model.IdentificacaoFamiliar;
import model.InfraestruturaComunitaria;
import model.Localidade;
import model.MaterialInformativo;
import model.MeioComunicacao;
import model.Municipio;
import model.ProdutoAgricultura;
import model.ProdutoFlorestalMadeireiro;
import model.ProdutoFlorestalNaoMadeireiro;
import model.ProdutoPescado;
import model.SintomaComum;
import model.UnidadeConservacao;
import repositorio.ComunitarioRepository;
import repositorio.LocalRepositorio;

public class ComunitarioService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject 
	private ComunitarioRepository repositorio;
	
	@Inject
	private LocalRepositorio local;
		
	
	public ComunitarioService(){
		
	}
		
	@Transactional
	public Boolean Salvar(Comunitario comunitario){
		try{
			repositorio.salvar(comunitario);
			return true;
		}catch(Exception e){
			return false;
		}
		
		
	}
	
	public List<Comunitario> getComunitario(){
		return repositorio.getComunitario();
	}
	
	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Comunitario getComunitarioById(Long id) {
		return repositorio.getComunitarioById(id);
	}
	
	public List<Familiar> getFamiliarPorNumeroCadastro(Integer numero){
		return repositorio.getFamiliarPorNumeroCadastro(numero);
	}
	
	public List<IdentificacaoFamiliar> getMembros(Long idFamiliar){
		return repositorio.getMembros(idFamiliar);
	}
	
	@Transactional
	public boolean salvar(Comunitario comunitario) {
		try{
			repositorio.salvar(comunitario);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	@Transactional
	public boolean update(Comunitario comunitario) {
		try{
			repositorio.update(comunitario);
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
	
	public List<FonteAguaConsumo> buscarFontesAgua(String s){
		return repositorio.buscarFontesAgua(s);
	}
	
	public List<FonteEnergia> buscarFontesEnergia(String s){
		return repositorio.buscarFontesEnergia(s);
	}
	
	public List<MeioComunicacao> buscarMeiosComunicacao(String s){
		return repositorio.buscarMeiosComunicacao(s);
	}
	
	public List<MaterialInformativo> buscarMateriaisInformativos(String s){
		return repositorio.buscarMateriaisInformativos(s);
	}
	
	public List<AtividadeRenda> buscarAtividadesDeRenda(String s){
		return repositorio.buscarAtividadesDeRenda(s);
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
	
	public List<InfraestruturaComunitaria> buscarInfraestruturas(String s){
		return repositorio.buscarInfraestruturas(s);
	}
	
	public Boolean verificaCPFCadastrado(String cpf, Long id){
		return repositorio.verificaCPFCadastrado(cpf, id);
	}
}
