package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Adiantamento;
import model.Aprouve;
import model.CategoriaDespesaClass;
import model.CategoriaFinanceira;
import model.Lancamento;
import model.LancamentoAcao;
import model.LancamentoAuxiliar;
import model.LancamentoAvulso;
import model.PagamentoLancamento;
import model.PrestacaoDeConta;
import model.User;
import repositorio.AprouveRepositorio;
import repositorio.CategoriaRepositorio;
import repositorio.LancamentoRepository;
import repositorio.PagamentoLancamentoRepository;
import repositorio.ProjetoRepositorio;

/**
 * Autor: Italo Almeida 
 * Classe: Gestao de prestação de contas
 * 
 */

public class PrestacaoContaService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private @Inject LancamentoRepository lancamentoRepositorio;

	private @Inject ProjetoRepositorio projetoRepositorio;
	
	private @Inject AprouveRepositorio aprouveRepositorio;

	private @Inject PagamentoLancamentoRepository repositorio;
	
	@Inject
	private CategoriaRepositorio categoriaRepositorio;
	
	
	

	@Transactional
	public LancamentoAvulso salvarLancamentoAvulso(LancamentoAvulso lancamento, User usuario) {
		return lancamentoRepositorio.salvarLancamentoAvulso(lancamento, usuario);
	}

	@Transactional
	public Boolean validarAdiantamento(Long id){
		return lancamentoRepositorio.validarAdiantamento(id); 
	}
	
	@Transactional
	public void  validarLancamentosParaConcilicacao(List<LancamentoAvulso> lancamentos){
		
//		LancamentoAcao lc = new LancamentoAcao();
//		for (LancamentoAuxiliar la : lancamentos) {
//				lc = lancamentoRepositorio.getLancamentoAcaoPorId(la.getIdAcaoLancamentoId());
//				lc.setStatus(StatusPagamento.VALIDADO);
//				lancamentoRepositorio.salvarLancamentoAcao(lc);
//		}
		
		for (LancamentoAvulso la : lancamentos) {
				lancamentoRepositorio.validarLancamentos(la.getId());
		}
	}
	
	@Transactional
	public void  validarLancamentosParaConcilicacao(Long lancamento){
		
//		LancamentoAcao lc = new LancamentoAcao();
//		for (LancamentoAuxiliar la : lancamentos) {
//				lc = lancamentoRepositorio.getLancamentoAcaoPorId(la.getIdAcaoLancamentoId());
//				lc.setStatus(StatusPagamento.VALIDADO);
//				lancamentoRepositorio.salvarLancamentoAcao(lc);
//		}
		
		lancamentoRepositorio.validarLancamentos(lancamento);
		
//		for (LancamentoAvulso la : lancamentos) {
//				lancamentoRepositorio.validarLancamentos(la.getId());
//		}
	}
	
	@Transactional
	public Boolean devolverAdiantamento(Long id){
		return lancamentoRepositorio.devolverAdiantamento(id); 
	}
	
	public List<CategoriaFinanceira> buscarCategoriasFin(){
		return categoriaRepositorio.buscarGategoriasFin();
	}
	
	@Transactional
	public Boolean analisarAdiantamento(Long id){
		return lancamentoRepositorio.AnalisarAdiantamento(id); 
	}

	@Transactional
	public void removerLancamento(Lancamento lancamento) {
		lancamentoRepositorio.remover(lancamento);

	}
	
	public List<LancamentoAvulso> getPrestacoes(Long id){
		return lancamentoRepositorio.getPrestacoes(id);
	}
	
	public PagamentoLancamento buscarPagamentoByLancamentoAcao(Long id){
		return lancamentoRepositorio.buscarPagamentoByLancamentoAcao(id);
	}
	
	public boolean findAprouve(Aprouve aprouve){
		return aprouveRepositorio.findAprouvePedido(aprouve);
	}

	public List<Adiantamento> buscarAdiantamentos(Long idConta) {
		return lancamentoRepositorio.buscarAdiantamentos(idConta);
	}

	public List<CategoriaDespesaClass> getCategoriaAutoComplete(String query) {
		return projetoRepositorio.getCategoriaAutocomplete(query);
	}

	public List<PrestacaoDeConta> getPrestacaoByIdAdiantamento(Long id) {
		return lancamentoRepositorio.getPrestacaoByIdAdiantamento(id);
	}

	public LancamentoAuxiliar getLancamentoById(Long id) {
		return lancamentoRepositorio.getLancamentoAuxiliarById(id);
	}

	public LancamentoAcao buscarLancamentoAcao(Long id) {
		return lancamentoRepositorio.getLancamentoAcaoPorLancamento(id);
	}

	public List<LancamentoAvulso> buscarPrestacaoDeContas(Long idAdiantamento, Long idConta) {
		return lancamentoRepositorio.buscarPrestacaoDeContas(idAdiantamento, idConta);
	}

	public List<LancamentoAvulso> buscarPrestacaoDeContasEntradas(Long idAdiantamento, Long idConta) {
		return lancamentoRepositorio.buscarPrestacaoDeContas(idAdiantamento, idConta);
	}

	public LancamentoAvulso getLancamentoByNF(String nf, Long id, Long idConta) {
		return repositorio.getLancamentoAvulsoByNotaFiscal(nf, id, idConta);
	}
	
	@Transactional
	public Boolean validarAdiantamentoComLog(Long id, User usuario) {
		return lancamentoRepositorio.validarAdiantementoComLog(id, usuario);
		
	}

}
