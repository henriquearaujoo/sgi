package service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.*;
import repositorio.BancoRepositorio;
import repositorio.ContaRepository;
import repositorio.FontePagadoraRepositorio;
import repositorio.FornecedorRepositorio;
import repositorio.GestaoRepositorio;
import repositorio.ProjetoRepositorio;
import util.Filtro;

/**
 * @author: Italo Almeida 
 * @version: 3.0
 * 
 *  Gestao de Relatórios
 *  Objetivo: Gerir a regra de negocio referente a instancia do objeto
 * */

public class ContaService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ContaRepository repositorio;
	
	@Inject
	private ProjetoRepositorio projetoRepositorio;
	
	@Inject
	private GestaoRepositorio gestaoRepositorio;
	
	@Inject 
	private BancoRepositorio bancoRepositorio;
	
	@Inject
	private FontePagadoraRepositorio fonteRepositorio;
	
	@Inject
	private FornecedorRepositorio fornecRepository;
	
	
	public List<ContaBancaria> getContas(Filtro filtro){
		return repositorio.getContas(filtro);
	}
	
	
	public List<RelatorioContasAPagar> getPagamentosPorConta(Filtro filtro){
		return repositorio.getPagamentosPorConta(filtro);
	}

	public List<RelatorioContasAPagar> getOutrosPagamentosPorConta(Long idConta, Integer tipo) {
		return repositorio.getOutrosPagamentosPorConta(idConta, tipo);
	}

	@Transactional
	public void removerConta(ContaBancaria conta){
		repositorio.removerConta(conta);
	}
	
	
	@Transactional
	public ContaBancaria salvarConta(ContaBancaria conta) {
		conta.setSaldoAtual(conta.getSaldoInicial());
		return repositorio.salvarConta(conta);
	}
	
	public List<Gestao> getGestao(){
		List<Gestao> list = new ArrayList<>();
		list.addAll(gestaoRepositorio.getSuperintendencia());
		list.addAll(gestaoRepositorio.getCoordenadoria());
		list.addAll(gestaoRepositorio.getRegional());
		return list;
	}
	
	
	@Transactional
	public ContaBancaria salvarContaBancaria(ContaBancaria conta){
		return repositorio.salvarConta(conta);
	}
	
	
	public List<Banco> getBancoAutoComplete(String s){
		return bancoRepositorio.getBancoAutoComplete(s);
	}
	
	
	public List<Projeto> buscarProjetoByGestao(Long id){
		return projetoRepositorio.buscarProjetoByGestao(id);
		
	}
	 
	public List<Fornecedor> getFornecedores(String s) {
		return fornecRepository.getFornecedoresAutoComplete(s);
	}
	
	public List<FontePagadora> buscarFontes(){
		return fonteRepositorio.getFontes();
	}
	
	public List<Superintendencia> buscarSuperintendencia(){
		return gestaoRepositorio.buscarSuperintendencia();
	}
	
	
	public List<Coordenadoria> buscarCoordenadoriaBySup(Long id){
		return gestaoRepositorio.buscarCoordenadoriaBySup(id);
	}
	
	public List<Regional> buscarSubCategoriaByCoord(Long id){
		return gestaoRepositorio.buscarSubCoodenadoriaByCoord(id);
	}
	
	public List<Acao> getListAcao(Filtro filtro){
		//return repositorio.getListAcoes(filtro);
		
		/*Filtro f = new Filtro();
		//f.setGestaoID(new Long(61)); //RENDA (EMPODERAMENTO) FLOREST MAUÉS
		//f.setGestaoID(new Long(63)); //RENDA (EMPODERAMENTO) CANUMÃ
		//f.setGestaoID(new Long(75)); // RENDA (EMPODERAMENTO) UATUMÃ
		//f.setGestaoID(new Long(62)); //RENDA (EMPODERAMENTO) PIAGAÇU-PURUS
		//f.setGestaoID(new Long(57)); //SOCIAL - RIO NEGRO
		//f.setGestaoID(new Long(77));// RENDA (EMPODERAMENTO) - RIO NEGRO
		f.setGestaoID(new Long(64));// CANUMÃ SOCIAL
		*/
		return repositorio.getListAcoesFiltroProjeto(filtro);
	}
	
	public List<ContaBancaria> getContasBancarias(Filtro filtro){
		return repositorio.getContasBancarias(filtro);
	}
	
	public List<Projeto> getProjetos(){
		return projetoRepositorio.getProjetosBeanContasApAgaerController();
	}
	
	public List<Projeto> getProjetosMODE01(){
		return projetoRepositorio.getProjetosMODE01();
	}
	
	public List<Projeto> getProjetosMODE01(Filtro filtro){
		return projetoRepositorio.getProjetosMODE01SQLNATIVE(filtro);
	}
	
	public List<Projeto> buscarProjetos(List<Integer> projetos){
		return projetoRepositorio.getProjetos(projetos);
	}
	
	public List<ContaBancaria> getContaSaldo(Filtro filtro){
		return repositorio.getContaSaldo(filtro);
	}
	
	public List<ContaBancaria> getContaSaldoMODE01(Filtro filtro){
		return repositorio.getContaSaldoMODE01(filtro);
	}
	
	public List<RelatorioFinanceiro> buscarPagamentos(Filtro filtro){
		return repositorio.buscarPagamentos(filtro);
	}
	
	public List<RelatorioExecucao> getPagamentoByProjeto(Long id, Filtro filtro){
		return repositorio.getPagamentosByProjeto(id, filtro);
	}
	
	public ContaBancaria getContaSaldoCA(Filtro filtro){
		return repositorio.getContaSaldoCA(filtro);
	}

	
	public List<RelatorioContasAPagar> getPagamentosByConta(Filtro filtro){
		return repositorio.getPagamentosByConta(filtro);
	}
	
	public List<RelatorioContasAPagar> getPagamentosByContaMODE01(Filtro filtro){
		return repositorio.getPagamentosByContaMODE01(filtro);
	}

	public List<RelatorioContasAPagar> getPagamentosByContaMODE01Relatorio(Filtro filtro) {
		return repositorio.getPagamentosByContaMODE01Relatorio(filtro);
	}
	
	public List<RelatorioContasAPagar> getPagamentosByContaParaOutrosFiltros(Filtro filtro){
		return repositorio.getPagamentosByContaParaOutrosFiltros(filtro);
	}

	public List<RelatorioContasAPagar> getPagamentosByContaCAParaOutrosFiltros(Filtro filtro){
		return repositorio.getPagamentosByContaCAParaOutrosFiltros(filtro);
	}
	
	
	public List<RelatorioContasAPagar> getPagamentosByContaCA(Filtro filtro){
		return repositorio.getPagamentosByContaCA(filtro);
	}
	
	public List<RelatorioContasAPagar> getPagamentosByContaCAMODE01(Filtro filtro){
		return repositorio.getPagamentosByContaCAMODE01(filtro);
	}
	
	
	
	public List<Acao> getListAcoes(Filtro filtro){
		return repositorio.getAcoesIn(filtro);
		//return repositorio.getListAcoesFiltroProjeto(new Filtro());
	}
	
	
	public List<RelatorioContasAPagar> getPagamentosByAcao(Filtro filtro){
		return repositorio.getPagamentosByAcao(filtro);
	}
	
	public List<RelatorioContasAPagar> getPagamentosByDocumento(Filtro filtro){
		return repositorio.getPagamentosByDocumento(filtro);
	}
	
	public ContaBancaria getContaBancaria(Long id) {
		return repositorio.getContaById(id);
	}

	public List<RendimentoConta> obterRendimentosRelatorio(ContaBancaria conta){
		return repositorio.obterRendimentosRelatorio(conta);
	}

	public List<ContaBancaria> obterContasRendimento(){
		return repositorio.obterContasRendimento();
	}

	public List<AplicacaoRecurso> obterAplicacoes(Long id){
		return repositorio.obterAplicacoes(id);
	}

	public List<BaixaAplicacao> obterBaixas(Long id){
		return  repositorio.obterBaixas(id);
	}

	@Transactional
	public RendimentoConta salvarRendimento(RendimentoConta rendimentoConta){
		return repositorio.salvarRendimento(rendimentoConta);
	}

	@Transactional
	public FonteDoacaoConta salvarDoacao(FonteDoacaoConta fonteDoacaoConta){
		return repositorio.salvarDoacao(fonteDoacaoConta);
	}

	public void removerRendimento(RendimentoConta rendimentoConta) {
		repositorio.removerRendimento(rendimentoConta);
	}

	public void removerDoacao(FonteDoacaoConta fonteDoacaoConta) {
		repositorio.removerDoacao(fonteDoacaoConta);
	}

	@Transactional
	public AlocacaoRendimento salvarAlocacaoRendimento(AlocacaoRendimento alocacaoRendimento){
		return repositorio.salvarAlocacaoRendimento(alocacaoRendimento);
	}

	public void removerAlocacaoRendimento(AlocacaoRendimento alocacaoRendimento) {
		repositorio.removerAlocacaoRendimento(alocacaoRendimento);
	}

	public List<RendimentoConta> obterRendimentos(ContaBancaria contaBancaria){
		return repositorio.obterRendimentos(contaBancaria);
	}

	public List<FonteDoacaoConta> obterDoacoes(ContaBancaria contaBancaria){
		return repositorio.obterDoacoes(contaBancaria);
	}

	public List<AlocacaoRendimento> obterAlocacoesRendimentos(ContaBancaria contaBancaria){
		return  repositorio.obterAlocacoesRendimentos(contaBancaria);
	}
}
