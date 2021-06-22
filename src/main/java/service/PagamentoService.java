package service;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import anotacoes.Transactional;
import model.*;
import operator.LancamentoIterface;
import operator.Parcela;
import repositorio.AcaoRepositorio;
import repositorio.AprouveRepositorio;
import repositorio.CategoriaRepositorio;
import repositorio.ConfiguracaoRepositorio;
import repositorio.ContaRepository;
import repositorio.DetalhamentoRepositorio;
import repositorio.FontePagadoraRepositorio;
import repositorio.FornecedorRepositorio;
import repositorio.ImpostoRepositorio;
import repositorio.LancamentoRepository;
import repositorio.LocalRepositorio;
import repositorio.OrcamentoRepositorio;
import repositorio.PagamentoLancamentoRepository;
import repositorio.ProjetoRepositorio;
import repositorio.TermoExpedicaoPedidoRepositorio;
import util.ArquivoLancamento;
import util.Email;
import util.Filtro;
import util.GeradorConteudoHTML;
import util.UsuarioSessao;

/**
 * Autor: Italo Almeida
 *
 * Objetivo: Gerir a regra de negocio referente a instancia do objeto
 */

public class PagamentoService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PagamentoLancamentoRepository repositorio;
	@Inject
	private FontePagadoraRepositorio fonteRepositorio;
	@Inject
	private AcaoRepositorio acaoRepositorio;
	@Inject
	private LancamentoRepository lancamentoRepositorio;
	@Inject
	private FornecedorRepositorio fornecedorRepositorio;
	@Inject
	private ContaRepository contaRepositorio;
	@Inject
	private CategoriaRepositorio categoriaRepositorio;
	@Inject
	private ProjetoRepositorio projetoRepositorio;
	@Inject 
	private TermoExpedicaoPedidoRepositorio repositorioTermo;
	@Inject
	private AprouveRepositorio aprouveRepositorio;
	@Inject
	private OrcamentoRepositorio orcamentoRepositorio; 	
	@Inject
	private ImpostoRepositorio impRepositorio;

	@Inject
	private UsuarioSessao usuarioSessao;
	
	private TipoParcelamento tipoParcelamento;
	
	
	@Inject
	private DetalhamentoRepositorio detalheRepositorio;
	
	public List<CategoriaFinanceira> buscarCategoriasFin(){
		return categoriaRepositorio.buscarGategoriasFin();
	}
	
	public RubricaOrcamento getRubricaDespesasAdm() {
		return orcamentoRepositorio.getRubricaDespesasAdm();
	}
	
	public TermoExpedicao getTermoById(Long id){
		return repositorioTermo.getTermoById(id);
	}
	
	public List<ItemPedido> itensPedido(TermoExpedicao expedicao) {
		return repositorioTermo.getItensPedidoByTermo(expedicao);
	}
	
	@Transactional
	public TarifaBancaria salvarTarifa(TarifaBancaria tarifa, User usuario) {
		return lancamentoRepositorio.salvarTarifa(tarifa, usuario);
	}
	
	
	public List<LancamentoAuxiliar> getExtratoCtrlProjeto(Filtro filtro) {

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null)
			if (filtro.getDataFinal().before(filtro.getDataInicio())) {
				addMessage("", "Data final maior que a data de inicio", FacesMessage.SEVERITY_INFO);
			}

		return repositorio.getExtratoCtrlProjeto(filtro);
	}
	
	public ContaBancaria getDefaultRecebedorTarifa() {
		return contaRepositorio.getDefaultRecebedorTarifa();
	}
	
	//metodo movido para CalculatorRubricaRepositorio
	@Deprecated
	public List<ProjetoRubrica> completeRubricasDeProjetoJOIN(String s) {
		return orcamentoRepositorio.completeRubricasDeProjetoJOIN(s);
	}
	
	@Deprecated
	public List<ProjetoRubrica> completeRubricasDeProjetoJOINRevert(String s) {
		return orcamentoRepositorio.completeRubricasDeProjetoJOINRevert(s);
	}
	
	
	public List<Projeto> getProjetoAutoCompleteMODE01(String s){
		return projetoRepositorio.getProjetoAutocompleteMODE01(s);
	}

	public List<LancamentoAuxiliar> getLancamentosNãoProvisionados(Filtro filtro) {

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null)
			if (filtro.getDataFinal().before(filtro.getDataInicio())) {
				addMessage("", "Data final maior que a data de inicio", FacesMessage.SEVERITY_INFO);
			}

		return repositorio.getLancamentosReport(filtro);
	}

	public List<LancamentoAuxiliar> getLancamentosNãoProvisionadosMODE01(Filtro filtro) {

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null)
			if (filtro.getDataFinal().before(filtro.getDataInicio())) {
				addMessage("", "Data final maior que a data de inicio", FacesMessage.SEVERITY_INFO);
			}

		return repositorio.getLancamentosReportMODE01(filtro);
	}
	
	public List<LancamentoAuxiliar> getLancamentosParaValidacao(Filtro filtro) {

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null)
			if (filtro.getDataFinal().before(filtro.getDataInicio())) {
				addMessage("", "Data final maior que a data de inicio", FacesMessage.SEVERITY_INFO);
			}

		return repositorio.getLancamentosParaValidacao(filtro);
	}
	
	public void imprimir(Lancamento lancamento) {
		LancamentoIterface lancamentoInterface = TipoLancamentoV4.valueOf(lancamento.getTipov4()).obterInstancia();
		lancamentoInterface.imprimir(lancamento);
	}
	
	public List<LancamentoAuxiliar> getLancamentosCP(Filtro filtro) {

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null)
			if (filtro.getDataFinal().before(filtro.getDataInicio())) {
				addMessage("", "Data final maior que a data de inicio", FacesMessage.SEVERITY_INFO);
			}

		return repositorio.getLancamentosCP(filtro);
	}
	
	
	
	
	public List<LancamentoAuxiliar> getLancamentosNãoProvisionadosRECLASSIFICACAO(Filtro filtro) {

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null)
			if (filtro.getDataFinal().before(filtro.getDataInicio())) {
				addMessage("", "Data final maior que a data de inicio", FacesMessage.SEVERITY_INFO);
			}

		return repositorio.getLancamentosReportRECLASSIFICACAO(filtro);
	}
	
	@Transactional
	public void  validarLancamentos(List<LancamentoAuxiliar> lancamentos){
		
//		LancamentoAcao lc = new LancamentoAcao();
//		for (LancamentoAuxiliar la : lancamentos) {
//				lc = lancamentoRepositorio.getLancamentoAcaoPorId(la.getIdAcaoLancamentoId());
//				lc.setStatus(StatusPagamento.VALIDADO);
//				lancamentoRepositorio.salvarLancamentoAcao(lc);
//		}
		
		for (LancamentoAuxiliar la : lancamentos) {
			lancamentoRepositorio.validarLancamentos(la.getId());
		}
	}
	
	@Transactional
	public void  validarLancamentosDePrestacao(List<LancamentoAvulso> lancamentos){
		
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
	public void  invalidarLancamentos(List<LancamentoAuxiliar> lancamentos){
		
//		LancamentoAcao lc = new LancamentoAcao();
//		for (LancamentoAuxiliar la : lancamentos) {
//				lc = lancamentoRepositorio.getLancamentoAcaoPorId(la.getIdAcaoLancamentoId());
//				lc.setStatus(StatusPagamento.N_VALIDADO);
//				lancamentoRepositorio.salvarLancamentoAcao(lc);
//		}
		for (LancamentoAuxiliar la : lancamentos) {
			if(la.getStatus().equals("EFETIVADO")) {
				FacesContext.getCurrentInstance().addMessage("msg_lancamento", new FacesMessage(FacesMessage.SEVERITY_WARN, "", "Existem um ou mais Lançamentos Efetivados desmarque-os e tente novamente!"));
				return;
			}
			lancamentoRepositorio.invalidarLancamentos(la.getId());
		}
	}

	public Boolean verificarPrivilegioENF(Aprouve aprouve) {
		return aprouveRepositorio.verificaPrivilegioNF(aprouve);
	}
	
	public Boolean verificaAprouveEstorno(Aprouve aprouve) {
		return aprouveRepositorio.verificaAprouveEstorno(aprouve);
	}
	
	public Boolean verifyAprouve(Aprouve aprouve) {
		return aprouveRepositorio.verifyAprouve(aprouve);
	}
	
	public Lancamento getLancamentoById(Long id) {
		return lancamentoRepositorio.getLancamentoById(id);
	}

	public LancamentoAvulso getLancamentoAvulsoById(Long id) {
		return lancamentoRepositorio.getLancamentoAvulsoPorId(id);
	}
	
	public LancamentoDiversos getLancamentoDiversoById(Long id) {
		return lancamentoRepositorio.getLancamentoDiversosPorId(id);
	}

	@Transactional
	public ContaBancaria salvarConta(ContaBancaria conta) {
		conta.setSaldoAtual(conta.getSaldoInicial());
		return contaRepositorio.salvarConta(conta);
	}

	@Transactional
	public void removerArquivo(ArquivoLancamento arquivo) {
		lancamentoRepositorio.removerArquivo(arquivo);
	}
	
	@Transactional
	public void removerImposto(Imposto imposto) {
		this.impRepositorio.removerImposto(imposto);
	}

	public List<CategoriaDespesaClass> buscarCategorias() {
		return categoriaRepositorio.buscarGategorias();
	}

	public List<ArquivoLancamento> getArquivoByLancamento(Long lancamento) {
		return lancamentoRepositorio.getArquivosByLancamento(lancamento);
	}
	
	public List<ArquivoLancamento> getArquivoByLancamento(Long lancamento, Long idCompra) {
		return lancamentoRepositorio.getArquivosByLancamento(lancamento, idCompra);
	}

	@Transactional
	public void editarPagamento(PagamentoLancamento pagamento) {
		repositorio.salvar(pagamento.getLancamentoAcao());
		repositorio.salvar(pagamento);
	}

	@Transactional
	public ArquivoLancamento salvarArquivo(ArquivoLancamento arquivo) {
		return lancamentoRepositorio.salvarArquivo(arquivo);
	}

	public List<ContaBancaria> getContasFilter() {
		return contaRepositorio.getContasFilter();
	}

	public List<ContaBancaria> getContasFiltro() {
		return repositorio.getContasFiltro();
	}

	@Transactional
	public void salvarContaFornecedor(ContaBancaria contaBancaria) {
		contaRepositorio.salvarContaFornecedor(contaBancaria);
	}

	@Transactional
	public void salvarContaBancariaFornecedor(ContaBancaria contaBancaria) {
		contaRepositorio.salvarContaBancariaFornecedor(contaBancaria);
	}

	@Transactional
	public void updateContaBancaria(ContaBancaria contaBancaria) {
		contaRepositorio.updateContaBancariaFornecedor(contaBancaria);
	}

	@Transactional
	public void update(ContaBancaria contaBancaria) {
		contaRepositorio.salvarContaBancariaFornecedor(contaBancaria);
	}

	public ContaBancaria getContaBancariaById(Long id) {
		return contaRepositorio.getContaById(id);
	}

	public Fornecedor getFornecedor(Long fornecedor) {
		return fornecedorRepositorio.getFornecedorPorId(fornecedor);
	}

	public ContaBancaria getContaBancaria(ContaBancaria conta) {
		return fornecedorRepositorio.getContaBancaria(conta);
	}
	
	public List<LancamentoAcao> getLancamentosAcao(LancamentoAvulso lancamento) {
		return lancamentoRepositorio.getLancamentosAcoes(lancamento);
	}

	public List<LancamentoAcao> getLancamentosAcao(LancamentoDiversos lancamento) {
		return lancamentoRepositorio.getLancamentosAcoes(lancamento);
	}

	@Transactional
	public Boolean removerLancamentoAcao(LancamentoAcao la) {
		if(lancamentoRepositorio.removerLancamentoAcao(la)){
			return true;
		}else {
			return false;
		}
	}

	public LancamentoAvulso getLancamentoByNF(String nf, Long id, Long idConta) {
		return repositorio.getLancamentoAvulsoByNotaFiscal(nf, id, idConta);
	}

	public List<LancamentoAvulso> gerarRelatorioAdiantamento(Filtro filtro) {

		List<LancamentoAvulso> mListAux = getListaLancamentosAvulso(filtro);
		List<LancamentoAvulso> mList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		BigDecimal saldo = BigDecimal.ZERO;

		for (LancamentoAvulso lancamentoAvulso : mListAux) {
			LancamentoAvulso l = new LancamentoAvulso();

			l.setDtEmissao(sdf.format(lancamentoAvulso.getDataEmissao()));
			l.setDtPagto(sdf.format(lancamentoAvulso.getDataPagamento()));
			l.setDescricao(lancamentoAvulso.getDescricao());
			l.setNumeroDocumento(lancamentoAvulso.getNumeroDocumento());
			l.setPagador(lancamentoAvulso.getContaPagador().getNomeConta());
			l.setRecebedor(lancamentoAvulso.getContaRecebedor().getNomeConta());

			if (l.getTipoLancamento().equals("ad")) {
				l.setEntrada(lancamentoAvulso.getValorTotalComDesconto());
				l.setSaida(BigDecimal.ZERO);
			} else {
				l.setEntrada(BigDecimal.ZERO);
				l.setSaida(lancamentoAvulso.getValorTotalComDesconto());
			}

			saldo = saldo.add(l.getEntrada()).subtract(l.getSaida());
			l.setSaldo(saldo);
			mList.add(l);
		}

		return mList;
	}

	@Transactional
	public void removerLancamento(Lancamento lancamento) {
		lancamentoRepositorio.remover(lancamento);

	}

	public BigDecimal getSaldoAcao(Long id) {
		return acaoRepositorio.getSaldoAcao(id);
	}
	
	
	public List<LancamentoDiversos> getListaDiversos(Filtro filtro) {
		
		
		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			if (filtro.getDataInicio().after(filtro.getDataFinal())) {
				addMessage("", "Data inicio maior que data final", FacesMessage.SEVERITY_ERROR);
				return new ArrayList<LancamentoDiversos>();
			}

			Calendar calInicio = Calendar.getInstance();
			calInicio.setTime(filtro.getDataInicio());

			Calendar calFinal = Calendar.getInstance();
			calFinal.setTime(filtro.getDataFinal());

			calInicio.set(Calendar.HOUR, 0);
			calInicio.set(Calendar.MINUTE, 0);
			calInicio.set(Calendar.SECOND, 0);
			calInicio.set(Calendar.MILLISECOND, 0);

			calFinal.set(Calendar.HOUR, 23);
			calFinal.set(Calendar.MINUTE, 59);
			calFinal.set(Calendar.SECOND, 59);
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

		return lancamentoRepositorio.getListaDiversos2(filtro);
	}

	public List<LancamentoAvulso> getListaLancamentosAvulso(Filtro filtro) {

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			if (filtro.getDataInicio().after(filtro.getDataFinal())) {
				addMessage("", "Data inicio maior que data final", FacesMessage.SEVERITY_ERROR);
				return new ArrayList<LancamentoAvulso>();
			}

			Calendar calInicio = Calendar.getInstance();
			calInicio.setTime(filtro.getDataInicio());

			Calendar calFinal = Calendar.getInstance();
			calFinal.setTime(filtro.getDataFinal());

			calInicio.set(Calendar.HOUR, 0);
			calInicio.set(Calendar.MINUTE, 0);
			calInicio.set(Calendar.SECOND, 0);
			calInicio.set(Calendar.MILLISECOND, 0);

			calFinal.set(Calendar.HOUR, 23);
			calFinal.set(Calendar.MINUTE, 59);
			calFinal.set(Calendar.SECOND, 59);
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

		return lancamentoRepositorio.getListaLancamentosAvulso(filtro);
	}

	public List<LancamentoAvulso> getListaLancamentosAvulsoParaRelatorio(Filtro filtro) {

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			if (filtro.getDataInicio().after(filtro.getDataFinal())) {
				addMessage("", "Data inicio maior que data final", FacesMessage.SEVERITY_ERROR);
				return new ArrayList<LancamentoAvulso>();
			}

			Calendar calInicio = Calendar.getInstance();
			calInicio.setTime(filtro.getDataInicio());

			Calendar calFinal = Calendar.getInstance();
			calFinal.setTime(filtro.getDataFinal());

			calInicio.set(Calendar.HOUR, 0);
			calInicio.set(Calendar.MINUTE, 0);
			calInicio.set(Calendar.SECOND, 0);
			calInicio.set(Calendar.MILLISECOND, 0);

			calFinal.set(Calendar.HOUR, 0);
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

		return lancamentoRepositorio.getListaLancamentosAvulsoParaRelatorio(filtro);
	}

	@Transactional
	public void mudarStatus(Long id, StatusCompra statusCompra, LogStatus logStatus) {
		lancamentoRepositorio.mudarStatus(id, statusCompra);
		lancamentoRepositorio.salvarLog(logStatus);
	}
	
	@Transactional
	public void mudarStatusLancamento(Long id, StatusCompra statusCompra, LogStatus logStatus) {
		lancamentoRepositorio.mudarStatusLancamento(id, statusCompra);
		lancamentoRepositorio.salvarLog(logStatus);
	}
	
	public boolean enviarParaCp(List<LancamentoAvulso> lancamentosAvulsos) {

		for (LancamentoAvulso lancamentoAvulso : lancamentosAvulsos) {

		}

		return true;
	}

	@Transactional
	public Lancamento salvarLancamentoAvulsoSemRefatorar(LancamentoAvulso lancamento, User usuario) {
		return lancamentoRepositorio.salvarLancamentoSemRefatorar(lancamento, usuario);
	}
	
	@Transactional
	public Lancamento salvarLancamentoDiversosSemRefatorar(LancamentoDiversos lancamento, User usuario) {
		return lancamentoRepositorio.salvarLancamentoDiversosSemRefatorar(lancamento, usuario);
	}

	@Transactional
	public LancamentoAvulso salvarLancamentoAvulso(LancamentoAvulso lancamento, User usuario) {
		return lancamentoRepositorio.salvarLancamentoAvulso(lancamento, usuario);
	}
	
	@Transactional
	public LancamentoDiversos salvarLancamentoDiversos(LancamentoDiversos lancamento, User usuario) {
		return lancamentoRepositorio.salvarLancamentoDiversos(lancamento, usuario);
	}

	@Transactional
	public LancamentoReembolso salvarReembolso(LancamentoReembolso lancamento){
		return lancamentoRepositorio.salvarReembolso(lancamento);
	}

	@Transactional
	public LancamentoDevDocTed salvarDevDocTed(LancamentoDevDocTed lancamentoDevDocTed){
		return lancamentoRepositorio.salvarDevDocTed(lancamentoDevDocTed);
	}
	
	@Transactional
	public Devolucao salvarDevolucao(Devolucao devolucao ){
		return lancamentoRepositorio.salvarDevolucao(devolucao);
	}

	@Transactional
	public LancamentoAlocacaoRendimento salvarRendimento(LancamentoAlocacaoRendimento lancamentoAlocacaoRendimento){
		return lancamentoRepositorio.salvarRendimento(lancamentoAlocacaoRendimento);
	}

	@Transactional
	public void deletarRendimento(Long id){
		lancamentoRepositorio.deletarRendimento(id);
	}

	@Transactional
	public LancamentoAcao salvarLancamentoAcao(LancamentoAcao lancamento){
		return lancamentoRepositorio.salvarLancamentoAcao(lancamento);
	}

	public List<PagamentoLancamento> buscarPagamentoByLancamento(Long id) {
		return lancamentoRepositorio.buscarPagamentoByLancamento(id);
	}

	public List<FontePagadora> fontesAutoComplete(String fonte) {
		return fonteRepositorio.getFonteAutoComplete(fonte);
	}

	public List<ContaBancaria> getAllConta(String s) {
		return repositorio.getAllConta(s);
	}

	public List<ContaBancaria> getAllContaListaModal(String s) {
		return repositorio.getAllContaListaModal(s);
	}

	// @Transactional
	public void efetivar(Long id) {
		PagamentoLancamento p = repositorio.findById(id);
		p.setStt(StatusPagamentoLancamento.EFETIVADO);
		repositorio.salvar(p);
	}
	
	
	public void efetivar_02(Long id) {
		PagamentoLancamento p = repositorio.findById(id);
		p.setStt(StatusPagamentoLancamento.EFETIVADO);
		repositorio.salvar(p);
	}
	
	
	

	@Transactional //MÉTODO ANTIGO USADO NO MODELO DE DESENVOLVIMENTO BASEADO EM AÇÕES
	public void efetivar(List<PagamentoPE> list) {		
		for (PagamentoPE pagamentoPE : list) {
			efetivar(pagamentoPE.getIdPagamento());
		}
	}
	
	@Transactional //MÉTODO ANTIGO USADO NO MODELO DE DESENVOLVIMENTO BASEADO EM AÇÕES
	public void efetivarMultiplos(List<LancamentoAuxiliar> list) {		
		for (LancamentoAuxiliar lancamento : list) {
			efetivar(lancamento.getIdPagamento());
		}
	}
	
	@Transactional 
	public void validarMultiplos(List<LancamentoAuxiliar> list) {		
		for (LancamentoAuxiliar lancamento : list) {
			
			if (lancamento.getFonte() == null || lancamento.getFonte().equals("")) {
				addMessage("", "NÃO FOI POSSÍVEL VALIDAR O LANÇAMENTO "+ lancamento.getCodigoLancamento()+", NÃO EXISTE LINHA ORÇAMENTÁRIA ASSOCIADA AO LANÇAMENTO.", FacesMessage.SEVERITY_ERROR);
				return;
			}
			
			repositorio.validarReclassificacao(lancamento.getIdPagamento());
		}
	}
		
	@Transactional 
	public void cancelTransaction(List<LancamentoAuxiliar> list) {		
		for (LancamentoAuxiliar lancamento : list) {
			salvarLog(usuarioSessao.getUsuario(), "Cancelamento do lançamento ID "+lancamento.getId(), lancamento.getStatusCompra().name()  , "CANCELADO");
			repositorio.updateStatusTransaction(lancamento.getId(), "CANCELADO");
		}
	}
	
	@Transactional 
	public void openTransaction(List<LancamentoAuxiliar> list) {		
		for (LancamentoAuxiliar lancamento : list) {
			salvarLog(usuarioSessao.getUsuario(), "Abertura  do lançamento ID "+lancamento.getId(), lancamento.getStatusCompra().name()  , "NAO_INICIADO");
			repositorio.updateStatusTransaction(lancamento.getId(), "N_INCIADO");
		}
	}
		
	@Transactional 
	public void reverseTransaction(List<LancamentoAuxiliar> lancamentosSelected) throws Exception {		
		for (LancamentoAuxiliar lancamento : lancamentosSelected) {
			repositorio.reverse(lancamento.getIdPagamento());
			salvarLog(usuarioSessao.getUsuario(), "Estorno do Pagamento ID "+lancamento.getIdPagamento(), "EFETIVADO", "PROVISIONADO");
		}
		
	}
	
	@Transactional 
	public void removerLancamento(List<LancamentoAuxiliar> list) {		
		for (LancamentoAuxiliar lancamento : list) {
			repositorio.removerLancamento(lancamento.getId());
		}
	}
	
	
	@Transactional // MODELO BASEADO EM PROJETOS/AÇÕES/CENTRO DE CUSTO/ //MOD.P.C 01 1.0 //INST MOD 01 2.0 V1
	public void efetivar_02(List<PagamentoPE> list) {		
	
		
		for (PagamentoPE pagamentoPE : list) {
			//efetivar(pagamentoPE.getIdPagamento());
			repositorio.efetivarPagamentos(pagamentoPE.getId(), new SimpleDateFormat("dd/MM/yyyy").format(pagamentoPE.getDataPagamento()), pagamentoPE.getIdConta());
		}
	}
	
	@Transactional // MODELO BASEADO EM PROJETOS/AÇÕES/CENTRO DE CUSTO/ //MOD.P.C 01 1.0 //INST MOD 01 2.0 V1
	public void provisionar_02(List<PagamentoPE> list) {		
	
		
		for (PagamentoPE pagamentoPE : list) {
			//efetivar(pagamentoPE.getIdPagamento());
			repositorio.provisionarPagamentos(pagamentoPE.getId(), new SimpleDateFormat("dd/MM/yyyy").format(pagamentoPE.getDataPagamento()), pagamentoPE.getIdConta());
		}
	}

	public List<Acao> acoesAutoComplete(String acao) {
		return acaoRepositorio.getAcaoAutoComplete(acao);
	}
	
	public List<Acao> acoesAutoCompleteRECLASSIFICACAO(String acao) {
		return acaoRepositorio.getAcaoAutoCompleteRECLASSIFICACAO(acao);
	}

	public List<PagamentoPE> getPagamentosPE(Filtro filtro) {
		return repositorio.getPagamentosPE(filtro);
	}
	
	public List<PagamentoPE> getPagamentosPEMODE01(Filtro filtro) {
		if (filtro.getContaFiltro() == null) {
			filtro.setIdConta(null);
		}
		
		return repositorio.getPagamentosPEMODE01(filtro);
	}

	public List<OrcamentoProjeto> getOrcamentoByProjeto(Long id){
		return orcamentoRepositorio.getOrcamentosProjeto(id);
	}
	
	public PagamentoLancamento findPagamentoById(Long id) {
		return repositorio.findById(id);
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@Transactional
	public void removerPagamento(PagamentoLancamento pagamento) {
		repositorio.remover(pagamento);
	}

	public List<FontePagadora> getFontePagadora() {
		return repositorio.getFontes();
	}

	public List<Fornecedor> getFornecedores() {
		return repositorio.getFornecedores();
	}

	public List<Projeto> getProjetos() {
		return repositorio.getProjetos();
	}

	public List<Acao> getAcoes() {
		return repositorio.getAcoes();
	}

	public List<ContaBancaria> getContas() {
		return repositorio.getContas();
	}

	public LancamentoAcao buscarLancamentoAcao(Long id) {
		return repositorio.getLancamentoAcao(id);
	}

	public List<PagamentoLancamento> getPagamentos(Long id) {
		return repositorio.getPagamentos(id);
	}

	@Transactional
	public void salvar(PagamentoLancamento pagamento) {

		if (pagamento.getTipoParcelamento().compareTo(TipoParcelamento.PARCELA_UNICA) == 0) {
			pagamento.setQuantidadeParcela(1);
		}

		if (pagamento.getContaRecebedor() == null)
			pagamento.setContaRecebedor(pagamento.getLancamentoAcao().getLancamento().getContaRecebedor());
		if (pagamento.getTipoLancamento() == null)
			pagamento.setTipoLancamento(pagamento.getLancamentoAcao().getLancamento().getTipoLancamento());
		if (pagamento.getDespesaReceita() == null)
			pagamento.setDespesaReceita(pagamento.getLancamentoAcao().getLancamento().getDepesaReceita());

		Date dataBase = pagamento.getDataPagamento();
		tipoParcelamento = pagamento.getTipoParcelamento();
		BigDecimal totalParcelado = BigDecimal.ZERO;

		for (int i = 1; i <= pagamento.getQuantidadeParcela(); i++) {
			PagamentoLancamento pagto = new PagamentoLancamento(pagamento, i);

			if (i == 1) {
				/*
				 * Calendar c = Calendar.getInstance(); c.setTime(dataBase);
				 * pagto.setDataPagamento(DataUtil.verificaDiaUtil(c));
				 */
				pagto.setDataPagamento(pagamento.getDataPagamento());

			} else {
				pagto.setDataPagamento(setarData(dataBase));

				if (i == pagamento.getQuantidadeParcela()) {
					BigDecimal total = pagamento.getValor();
					BigDecimal resto = total.subtract(totalParcelado);
					pagto.setValor(resto);
				}

			}

			totalParcelado = totalParcelado.add(pagto.getValor());

			repositorio.salvar(pagto);
			dataBase = pagto.getDataPagamento();
		}

	}

	@Transactional
	public void salvarPagamentoUnico(PagamentoLancamento pagamento) {

		repositorio.salvar(pagamento);
	}

	public void salvarSemTransaction(PagamentoLancamento pagamento) {

		if (pagamento.getTipoParcelamento().compareTo(TipoParcelamento.PARCELA_UNICA) == 0) {
			pagamento.setQuantidadeParcela(1);
		}

		Date dataBase = pagamento.getDataPagamento();
		tipoParcelamento = pagamento.getTipoParcelamento();
		BigDecimal totalParcelado = BigDecimal.ZERO;

		for (int i = 1; i <= pagamento.getQuantidadeParcela(); i++) {
			PagamentoLancamento pagto = new PagamentoLancamento(pagamento, i);

			if (i == 1) {
				/*
				 * Calendar c = Calendar.getInstance(); c.setTime(dataBase);
				 * pagto.setDataPagamento(DataUtil.verificaDiaUtil(c));
				 */
				pagto.setDataPagamento(pagamento.getDataPagamento());

			} else {
				pagto.setDataPagamento(setarData(dataBase));

				if (i == pagamento.getQuantidadeParcela()) {
					BigDecimal total = pagamento.getValor();
					BigDecimal resto = total.subtract(totalParcelado);
					pagto.setValor(resto);
				}

			}

			totalParcelado = totalParcelado.add(pagto.getValor());

			repositorio.salvar(pagto);
			dataBase = pagto.getDataPagamento();
		}
	}

	public Date setarData(Date dataBase) {
		Parcela parcela = tipoParcelamento.obterParcela();
		Date data = parcela.calculaData(dataBase);
		return data;
	}

	public ContaBancaria getContaBancariaByFornecedor(Fornecedor fornecedor) {
		return contaRepositorio.getContaByFornecedor(fornecedor.getId());
	}



	public BigDecimal getTarifa() {
		return repositorio.getTarifa();
	}
	
	
	
	public List<Imposto> buscarImpostos(Filtro filtro){
		return impRepositorio.buscarImpostos(filtro);
	}
	
	@Transactional
	public Imposto salvarImposto(Imposto imposto) {
		
		if (imposto.getValor().compareTo(imposto.getPagamentoLancamento().getValor()) > 0) {
		  addMessage("", "O valor do imposto não pode ser maior que o valor do pagamento.", FacesMessage.SEVERITY_ERROR);
		  return imposto;
		}
		
		imposto = impRepositorio.salvarImposto(imposto); 
		addMessage("", "Salvo com sucesso!", FacesMessage.SEVERITY_INFO);
		return imposto;
	}
	
	public Imposto buscarImpostoPorId(Long id) {
		return impRepositorio.buscarImpostoPorId(id);
	}
	
	
	public List<Imposto> buscarImpostoPorLancamento(Long id){
		return detalheRepositorio.buscarImpostoPorLancamento(id);
	}
	
	public List<PagamentoLancamento> buscarParcelasPorLancamento(Long id){
		return detalheRepositorio.buscarParcelasPorLancamento(id);
	}
	
	
	
	@Transactional
	public void salvarLog(User usuario,String descricao,String estadoAnterior,String estadoAtual) {
		Log log = new Log();
		log.setDataModificacao(new Date());
		log.setDescricao(descricao);
		log.setEstadoAnterior(estadoAnterior);
		log.setEstadoNovo(estadoAtual);
		log.setUsuario(usuario.getNomeUsuario());
		repositorio.salvarLog(log);
		
	}
	


}
