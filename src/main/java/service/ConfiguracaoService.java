package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Configuracao;
import repositorio.ConfiguracaoRepositorio;
//Autor = Rafael, data 26/06/2019
public class ConfiguracaoService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//Variaveis
	@Inject
	private ConfiguracaoRepositorio configuracaoRepositorio;
	
	
	//Metodos
	public List<Configuracao> carregaConfiguracao() {
		
		return configuracaoRepositorio.carregaConfiguracao();
	}

	public void salvar(Configuracao configuracao) {
		configuracaoRepositorio.update(configuracao);
		
	}
	
	@Transactional
	public void salvarV2(Configuracao config) {
		configuracaoRepositorio.salvar(config);
	}
	
	

}
