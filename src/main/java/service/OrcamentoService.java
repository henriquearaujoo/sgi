package service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.Query;

import model.*;
import org.apache.commons.digester.plugins.PluginAssertionFailure;

import anotacoes.Transactional;
import repositorio.ComposicaoOrcamentoRepositorio;
import repositorio.ContaRepository;
import repositorio.OrcamentoRepositorio;
import repositorio.PagamentoLancamentoRepository;
import repositorio.PlanoTrabalhoRepositorio;
import repositorio.ProjetoRepositorio;
import repositorio.RubricaRepositorio;
import repositorio.TransferenciaOverHeadRepositorio;
import util.Filtro;

public class OrcamentoService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrcamentoService() {
	}

	private @Inject OrcamentoRepositorio repositorio;
	private @Inject ContaRepository contaRepository;
	private @Inject ProjetoRepositorio projetoRepositorio;
	private @Inject PagamentoLancamentoRepository pagRepositorio;
	private @Inject RubricaRepositorio rubricaRepositorio;
	private @Inject PlanoTrabalhoRepositorio planoRepositorio;
	private @Inject TransferenciaOverHeadRepositorio overHeadRepositorio;
	private @Inject ComposicaoOrcamentoRepositorio compOrcamentoRepositorio;

	public List<PlanoDeTrabalho> getPlanos() {
		return planoRepositorio.getPlanos();
	}
	
	
	public RubricaOrcamento findByIdRubricaOrcamento(Long id) {
		return repositorio.findByIdRubricaOrcamento(id);
	}
	
	public List<ContaBancaria> getContasCorrente() {
		return contaRepository.getContasCorrente(new Filtro());
	}

	@Transactional
	public Boolean removerDoacao(Orcamento orcamento) {

		if (repositorio.existeProjeto(orcamento.getId()) > 0) {
			addMessage("",
					"Esse orçamento está vinculado a 1 ou mais projetos, por favor desvincule os projetos do orçamento.",
					FacesMessage.SEVERITY_ERROR);
			return false;
		}

		try {
			repositorio.removerLinhasDeOrcamento(orcamento.getId());
			repositorio.remover(orcamento);
			addMessage("", "Removido.", FacesMessage.SEVERITY_INFO);
			return true;
		} catch (Exception e) {
			addMessage("",
					"Houve um erro ao excluir doação, por favor entre em contato com  o Administrador do sistema.",
					FacesMessage.SEVERITY_ERROR);
			return false;
		}
	}

	public List<Orcamento> getOrcamentos(Filtro filtro) {
		return repositorio.getOrcamentos(filtro);
	}

	public List<Orcamento> filtrarOrcamentos(Filtro filtro) {
		
		
		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			if (filtro.getDataInicio().after(filtro.getDataFinal())) {
				addMessage("", "Data inicio maior que data final", FacesMessage.SEVERITY_ERROR);
				return new ArrayList<Orcamento>();
			}

			Calendar calInicio = Calendar.getInstance();
			calInicio.setTime(filtro.getDataInicio());

			Calendar calFinal = Calendar.getInstance();
			calFinal.setTime(filtro.getDataFinal());

			calInicio.set(Calendar.HOUR, 0);
			calInicio.set(Calendar.MINUTE, 0);
			calInicio.set(Calendar.SECOND, 0);
			calInicio.set(Calendar.MILLISECOND, 0);

			calFinal.set(Calendar.HOUR, 24);
			calFinal.set(Calendar.MINUTE, 0);
			calFinal.set(Calendar.SECOND, 0);
			calFinal.set(Calendar.MILLISECOND, 0);

			filtro.setDataInicio(calInicio.getTime());
			filtro.setDataFinal(calFinal.getTime());

			// System.out.println(filtro.getDataInicio());
			// System.out.println(filtro.getDataFinal());

		} else if (filtro.getDataInicio() != null) {
			Calendar calInicio = Calendar.getInstance();
			calInicio.setTime(filtro.getDataInicio());
			calInicio.set(Calendar.HOUR, 0);
			calInicio.set(Calendar.MINUTE, 0);
			calInicio.set(Calendar.SECOND, 0);
			calInicio.set(Calendar.MILLISECOND, 0);
			filtro.setDataInicio(calInicio.getTime());
		}
		
		return repositorio.filtrarOrcamentos(filtro);
	}

	public List<Projeto> getProjetosFilter(Filtro filtro) throws NumberFormatException, ParseException {
		return repositorio.getProjetosFilter(filtro, "");
	}
	
	public List<Orcamento> getOrcamentosFilter(Filtro filtro) {
		return repositorio.getOrcamentosFilter(filtro, "");
	}

	public ContaBancaria findContaTransitoria(Long id) {
		return contaRepository.getContaById(id);
	}

	public List<OrcamentoProjeto> getProjetosByOrcamento(Long idDoacao) {
		return repositorio.getProjetosByOrcamento(idDoacao);
	}

	public List<OrcamentoProjeto> getProjetosByOrcamentoFiltro(Long idDoacao, Filtro filtro) {
		return repositorio.getProjetosByOrcamentoFiltro(idDoacao, filtro);
	}

	public List<ContaBancaria> getContas() {
		return contaRepository.getContasCorrente(new Filtro());
	}

	public List<Rubrica> getCategorias() {
		return rubricaRepositorio.getRubricas();
	}

	@Transactional
	public RubricaOrcamento salvarRubricaOrcamento(RubricaOrcamento rubricaOrcamento) {
		return repositorio.salvarRubricaOrcamento(rubricaOrcamento);
	}

	@Transactional
	public void removerRubricaOrcamento(Long id) {
		repositorio.removerRubricaOrcamento(id);
		;
	}

	public List<ContaBancaria> getContasFornecedoresEAdiantamentos(String s) {
		return contaRepository.getContasFornecedoresEAdiantamentos(s);
	}

	public RubricaOrcamento buscarLinhaOrcamentoEfetiva(Long id) {
		return repositorio.buscarLinhaOrcamentoEfetiva(id);
	}

	public RubricaOrcamento buscarLinhaOrcamentoAplicacao(Long id) {
		return repositorio.buscarLinhaOrcamentoAplicacao(id);
	}

	public RubricaOrcamento buscarLinhaOrcamentoBaixaAplicacao(Long id) {
		return repositorio.buscarLinhaOrcamentoBaixaAplicacao(id);
	}

	public RubricaOrcamento buscarLinhaOrcamentoAlocRendimento(Long idOrcamento) {
		return repositorio.buscarLinhaOrcamentoAlocRendimento(idOrcamento);
	}

	public ComponenteClass buscarComponenteDefault() {
		return repositorio.buscarComponenteDefault();
	}

	public SubComponente buscarSubComponenteDefault() {
		return repositorio.buscarSubComponenteDefault();
	}

	public Rubrica buscarRubricaDefault() {
		return repositorio.buscarRubricaDefault();
	}

	public Rubrica buscarRubricaAplicacoDefault() {
		return repositorio.buscarRubricaAplicaoDefault();
	}

	public Rubrica buscarRubricaBaixaAplicacaoDefault() {
		return repositorio.buscarRubricaBaixaAplicaoDefault();
	}

	public Rubrica buscarRubricaAlocRendimentoDefault() {
		return repositorio.buscarRubricaAlocRendimentoDefault();
	}

	public List<ContaBancaria> getAllConta(String s) {
		return pagRepositorio.getAllConta(s);
	}

	@Transactional
	public void salvarRubrica(Rubrica rubrica) {
		repositorio.salvarRubrica(rubrica);
	}

	@Transactional
	public Orcamento salvar(Orcamento orcamento) {
		return repositorio.salvar(orcamento);
	}

	@Transactional
	public DoacaoEfetiva salvarDoacaoEfetiva(DoacaoEfetiva doacao, User usuario) {
		try {

			if (doacao.getId() == null) {
				doacao.setDataEmissao(new Date());
			}
			doacao.setStatusCompra(StatusCompra.CONCLUIDO);
			doacao = repositorio.salvarDoacaoEfetiva(doacao, usuario);
			addMessage("", "Salvo Com Sucesso", FacesMessage.SEVERITY_INFO);
			return doacao;

		} catch (Exception e) {
			addMessage("", "Erro ao salvar", FacesMessage.SEVERITY_ERROR);
			return doacao;
		}
	}

	@Transactional
	public BaixaAplicacao salvarBaixaAplicacao(BaixaAplicacao baixaAplicacao, User usuario) {
		try {

			if (baixaAplicacao.getId() == null) {
				baixaAplicacao.setDataEmissao(new Date());
			}
			baixaAplicacao.setStatusCompra(StatusCompra.CONCLUIDO);
			baixaAplicacao = repositorio.salvarBaixaAplicacao(baixaAplicacao, usuario);
			return baixaAplicacao;

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public BaixaAplicacao atualizarBaixa(BaixaAplicacao baixaAplicacao){
		return repositorio.atualizarBaixa(baixaAplicacao);
	}

	@Transactional
	public AplicacaoRecurso salvarAplicacao(AplicacaoRecurso aplicacao, User usuario) {
		try {

			if (aplicacao.getId() == null) {
				aplicacao.setDataEmissao(new Date());
			}
			aplicacao.setStatusCompra(StatusCompra.CONCLUIDO);
			aplicacao = repositorio.salvarAplicacao(aplicacao, usuario);
			return aplicacao;

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional
	public AplicacaoRecurso atualizarAplicacao(AplicacaoRecurso aplicacao){
		return repositorio.atualizarAplicacao(aplicacao);
	}

	public List<LancamentoAuxiliar> getExtratoCtrlDoacao(Filtro filtro) {

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null)
			if (filtro.getDataFinal().before(filtro.getDataInicio())) {
				addMessage("", "Data final maior que a data de inicio", FacesMessage.SEVERITY_INFO);
			}

		return pagRepositorio.getExtratoCtrlDoacao(filtro);
	}

	public List<LancamentoAuxiliar> getExtratoCtrlDoacaoParaConferencia(Filtro filtro) {

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null)
			if (filtro.getDataFinal().before(filtro.getDataInicio())) {
				addMessage("", "Data final maior que a data de inicio", FacesMessage.SEVERITY_INFO);
			}

		return pagRepositorio.getExtratoCtrlDoacaoParaConferencia(filtro);
	}

	public List<LancamentoAuxiliar> getDoacoesEfetivadas(Filtro filtro) {
		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null)
			if (filtro.getDataFinal().before(filtro.getDataInicio())) {
				addMessage("", "Data final maior que a data de inicio", FacesMessage.SEVERITY_INFO);
			}

		// pagRepositorio.getSaldoDoacaoEfetiva(filtro);
		return pagRepositorio.getDoacoesEfetivadas(filtro);
	}

	public BigDecimal getSaldoDoacoesEfetivadas(Filtro filtro) {
		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null)
			if (filtro.getDataFinal().before(filtro.getDataInicio())) {
				addMessage("", "Data final maior que a data de inicio", FacesMessage.SEVERITY_INFO);
			}
		return pagRepositorio.getSaldoDoacaoEfetiva(filtro);
	}

	public BigDecimal getAreceber(Filtro filtro) {
		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null)
			if (filtro.getDataFinal().before(filtro.getDataInicio())) {
				addMessage("", "Data final maior que a data de inicio", FacesMessage.SEVERITY_INFO);
			}
		return pagRepositorio.getAreceber(filtro);
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public List<Projeto> getProjetoAutoCompleteMODE01(String s) {
		return projetoRepositorio.getProjetoAutocompleteMODE01(s);
	}
	
	public List<String> getOrcamentoTitulosAutoComplete(String query) {
		List<Orcamento> titulos = new ArrayList<>(); 
		titulos = repositorio.completeTitulos(query);
		List<String> titulosString = new ArrayList<>();
		for(Orcamento titulo: titulos) {
			titulosString.add(titulo.getTitulo());
		}
		return titulosString;
	}

	public List<RubricaOrcamento> getRubricasDeOrcamento(Long idOrcamento, Filtro filtro) {
		return repositorio.getRubricasDeOrcamento(idOrcamento, filtro);
	}

	public List<RubricaOrcamento> getNewRubricasDeOrcamento(Long idOrcamento, Filtro filtro) {
		return repositorio.getNewRubricasDeOrcamento(idOrcamento, filtro);
	}

	public List<RubricaOrcamento> getRubricasDeOrcamentoBNDES(Long idOrcamento, Filtro filtro) {
		return repositorio.getRubricasDeOrcamentoBNDES(idOrcamento, filtro);
	}

	@Transactional
	public HistoricoAditivoOrcamento salvarHistoricoOrcamento(HistoricoAditivoOrcamento historicoAditivoOrcamento) {
		return repositorio.salvarHistoricoOrcamento(historicoAditivoOrcamento);
	}

	public List<HistoricoAditivoOrcamento> getHistoricoAditivoOrcamento(Long id) {
		return repositorio.getHistoricoAditivoOrcamento(id);
	}

	
	public Boolean verificarSaldoOverHead(TransferenciaOverHead transf) {
		
		if (transf.getTipo() == 0) {
			BigDecimal valorPagoOverhead = getTotalTransferenciaOverheadPagos(transf);
			BigDecimal valorOrcadoOverhead = transf.getDoacaoPagadora().getValorOverheadAPagar() != null
					? transf.getDoacaoPagadora().getValorOverheadAPagar()
					: BigDecimal.ZERO;
					
					
			BigDecimal valorDisponivel = valorOrcadoOverhead.subtract(valorPagoOverhead);
			if (valorDisponivel.compareTo(transf.getValor()) >= 0) {
				return true;
			}else {
				
				return false;
			}
		} else {
			BigDecimal valorPagoOverhead = getTotalTransferenciaOverheadIndiretoPagos(transf);
			BigDecimal valorOrcadoOverhead = getValorRendimento(transf.getDoacaoPagadora().getId());
					
			BigDecimal valorDisponivel = valorOrcadoOverhead.subtract(valorPagoOverhead);
			
			if (valorDisponivel.compareTo(transf.getValor()) >= 0) {
				return true;
			}else {
				
				return false;
			}
		}
		
	}
	
	
	@Transactional
	public TransferenciaOverHead salvarTransferenciaOverHead(TransferenciaOverHead transf) {
		return this.overHeadRepositorio.salvarTransferenciaOverHead(transf);
	}

	@Transactional
	public void removerTransferenciaOverHead(TransferenciaOverHead transf) {
		this.overHeadRepositorio.removerTransferenciaOverHead(transf);
	}

	public BigDecimal getTotalTransferenciaOverheadPagos(TransferenciaOverHead transf) {
		return overHeadRepositorio.getTotalTransferenciaOverheadPagos(transf);
	}

	public BigDecimal getTotalTransferenciaOverheadIndiretoPagos(TransferenciaOverHead transf) {
		return overHeadRepositorio.getTotalTransferenciaOverheadIndiretoPagos(transf);
	}

	public List<TransferenciaOverHead> getListaTransferencia(TransferenciaOverHead transf) {
		return this.overHeadRepositorio.getListaTransferencia(transf);
	}

	public BigDecimal getValorOverhead(Long orcamento) {
		return compOrcamentoRepositorio.getValorOverheadByOrcamento(orcamento);
	}
	
	public BigDecimal getValorOverheadPago(Long orcamento) {
		return compOrcamentoRepositorio.getValorOverheadPagoByOrcamento(orcamento);
	}

	public BigDecimal getValorOverheadIndireto(Long orcamento) {
		return compOrcamentoRepositorio.getValorIndiretoByOrcamento(orcamento);
	}

	public BigDecimal getValorRendimento(Long orcamento) {
		return compOrcamentoRepositorio.getValorRendimentoByOrcamento(orcamento);
	}

	public List<TransferenciaOverHead> getListOverHeadByOrcamento(Long id) {
		return compOrcamentoRepositorio.getListOverheadByOrcamento(id);
	}

	public List<TransferenciaOverHead> getListOverHeadIndiretoByOrcamento(Long id) {
		return compOrcamentoRepositorio.getListOverheadIndiretoByOrcamento(id);
	}

	public List<AlocacaoRendimento> getListRendimentoByOrcamento(Long id) {
		return compOrcamentoRepositorio.getListRendimentoByOrcamento(id);
	}

}