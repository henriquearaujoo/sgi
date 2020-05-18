package service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Coordenadoria;
import model.Gestao;
import model.GestaoOrcamento;
import model.GestaoRecurso;
import model.Orcamento;
import model.RubricaOrcamento;
import repositorio.GestaoOrcamentariaRepositorio;

/**
 * Autor: Italo Almeida 
 * Classe: Gestao de Projeto
 * Objetivo: Gerir a regra de negocio referente a instancia do objeto
 * */

public class GestaoOrcamentariaService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Repositorio padrão da instancia
	@Inject
	private GestaoOrcamentariaRepositorio repositorio;

	
	@Transactional
	public GestaoRecurso salvar(GestaoRecurso gestaoRecurso) {
		return repositorio.salvar(gestaoRecurso);
	}
	
	public List<GestaoRecurso> getRecursosAportados(Long id){
		return repositorio.getRecursosAportados(id);
	}
	
	@Transactional
	public void removerRecursoAportado(GestaoRecurso gestaoRecurso) {
		repositorio.removerRecursoAportado(gestaoRecurso);
	}

	public GestaoRecurso findRecursoAportadoById(Long id) {
		return repositorio.findRecursoAportadoById(id);
	}
		
	public BigDecimal getValorLiberado(Long gestaoId) {
		return repositorio.getValorLiberado(gestaoId);
	}
	
	// ORCAMENTO LIBERADO DURANTE O ANO
	@Transactional
	public GestaoOrcamento salvarOrcamentoDeGestao(GestaoOrcamento gestaoOrcamento) {
		return repositorio.salvarOrcamentoDeGestao(gestaoOrcamento);
	}
	
	// ORCAMENTO LIBERADO DURANTE O ANO	
	public GestaoOrcamento findOrcamentoGestaoById(Long id) {
		return repositorio.findOrcamentoGestaoById(id);
	}
	
	
	public GestaoOrcamento findOrcamentoGestaoByGestaoId(Long id) {
		return repositorio.findOrcamentoGestaoByGestaoId(id);
	}
	
	public List<Gestao> getGestoesParticipativas() {
		return repositorio.getGestoesParticipativas();
	}

	
	public Coordenadoria findGestaooById(Long id) {
		return repositorio.findGestaoById(id);
	}
	
	
	public List<Orcamento> getOrcamentos() {
		return repositorio.getOrcamentos();
	}
	
	public List<RubricaOrcamento> getRubricasDeOrcamento(Long idOrcamento) {
		return repositorio.getRubricasDeOrcamento(idOrcamento);
	}
	
	
}
