package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Banco;
import model.CategoriaDespesaClass;
import model.ContaBancaria;
import model.Estado;
import model.Fornecedor;
import model.LancamentoAuxiliar;
import model.Localidade;
import repositorio.BancoRepositorio;
import repositorio.CategoriaRepositorio;
import repositorio.ContaRepository;
import repositorio.FornecedorRepositorio;
import repositorio.LancamentoRepository;
import repositorio.LocalRepositorio;
import util.ArquivoFornecedor;
import util.Filtro;

	public class FornecedorService implements Serializable {
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@Inject
		private FornecedorRepositorio func; 
		
		@Inject
		private LocalRepositorio local;
		
		@Inject
		private ContaRepository contaRepo;
		
		@Inject
		private BancoRepositorio bancoRepositorio;

		@Inject
		private CategoriaRepositorio categoriaRepositorio;
	 
		
		public FornecedorService() {
		}
		
		
		
		public List<Estado> getEstados() {
			return local.getEstados();
		}
		
		public List<Localidade> findCidadeByEstado(Long estado) {
			return local.getMunicipioByEstado(estado);
		}
		
		@Transactional
		public void removerFornecedor(Fornecedor fornecedor) {
			func.removerFornecedor(fornecedor);
		}
		
		public List<ArquivoFornecedor> getArquivoByFornecedor(Long lancamento){
			return func.getArquivosByFornecedor(lancamento);
		}
		
		public LancamentoAuxiliar buscarUltimoFornecimento(Long fornecedor) {
			return func.buscarUltimoFornecimento(fornecedor);
		}
		
		public boolean verificarExisteFornecimento(Long idFornecedor) {
			return func.verificarExisteFornecimento(idFornecedor);
		}
		
		public ContaBancaria buscarContaFleg(Long fornecedor) {
			return contaRepo.buscarContaFleg(fornecedor);
		}
		
		public List<ContaBancaria> buscarContaDiferenteFleg(Long fornecedor) {
			return contaRepo.buscarContaDiferenteFleg(fornecedor);
		}

		public List<Banco> getBancoAutoComplete(String s){
			return bancoRepositorio.getBancoAutoComplete(s);
		}
		
		public ContaBancaria getContaByFornecedor(Long fornecedor) {
			return contaRepo.getContaByFornecedor(fornecedor);
		}
		
		public FornecedorService(FornecedorRepositorio func){
			this.func = func;
		}
		
		public ContaBancaria findById(Long id) {
			return contaRepo.findById(id);
		}

		public List<Fornecedor> getForcedoresAtivos() {
			return func.getFornecedoresAtivos();
		}
		
		public List<Localidade> buscaLocalidade(String s){
			return local.buscarLocalidade(s);
		}
		
		@Transactional
		public boolean insert(Fornecedor fornecedor) {
			
			try{
				func.insert(fornecedor);
				return true;
			}catch(Exception e){
				return false;
			}
			
			
		}
		@Transactional
		public boolean insertConta(ContaBancaria contaBancaria) {
			try{
				contaRepo.salvarConta(contaBancaria);
				return true;
			}catch(Exception e){
				return false;
			}
			
		}

		public List<Fornecedor> getForcedores(Filtro filtro) {
			return func.getFornecedores(filtro);
		}

		public List<Fornecedor> getFornecedoresRelatorio(Filtro filtro){
			return func.getFornecedoresRelatorio(filtro);
		}

		public List<Fornecedor> getForcedoresTerceiros(Filtro filtro) {
			return func.getFornecedoresTerceiros(filtro);
		}

		public Fornecedor getFornecedor(Long long1) {
			return func.getFornecedor(long1);
		}


		public List<CategoriaDespesaClass> getCategorias() {
			return categoriaRepositorio.buscarCategoriasFornecedor();
		}
	}