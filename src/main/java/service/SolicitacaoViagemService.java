package service;

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
import model.Aprouve;
import model.CategoriaDespesaClass;
import model.CategoriaProjeto;
import model.Colaborador;
import model.ContaBancaria;
import model.DespesaViagem;
import model.Diaria;
import model.Estado;
import model.Fornecedor;
import model.Gestao;
import model.Lancamento;
import model.LancamentoAcao;
import model.Localidade;
import model.LogStatus;
import model.LogViagem;
import model.Orcamento;
import model.OrcamentoProjeto;
import model.PagamentoLancamento;
import model.ProgamacaoViagem;
import model.Projeto;
import model.ProjetoRubrica;
import model.SolicitacaoViagem;
import model.StatusCompra;
import model.StatusPagamento;
import model.StatusPagamentoLancamento;
import model.TipoDespesa;
import model.TipoGestao;
import model.TipoParcelamento;
import model.TipoViagem;
import model.Trecho;
import model.User;
import model.Veiculo;
import repositorio.AcaoRepositorio;
import repositorio.AprouveRepositorio;
import repositorio.CategoriaRepositorio;
import repositorio.ColaboradorRepositorio;
import repositorio.ContaRepository;
import repositorio.CotacaoRepository;
import repositorio.DespesaRepositorio;
import repositorio.DiariaRepositorio;
import repositorio.EstadoRepositorio;
import repositorio.FontePagadoraRepositorio;
import repositorio.FornecedorRepositorio;
import repositorio.GestaoRepositorio;
import repositorio.LancamentoRepository;
import repositorio.LocalRepositorio;
import repositorio.OrcamentoRepositorio;
import repositorio.ProgramacaoViagemRepositorio;
import repositorio.ProjetoRepositorio;
import repositorio.TipoViagemRepositorio;
import repositorio.TrechoRepositorio;
import repositorio.VeiculoRepositorio;
import repositorio.ViagemRepositorio;
import util.DataUtil;
import util.Filtro;

public class SolicitacaoViagemService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ViagemRepositorio repositorio;
	@Inject
	private EstadoRepositorio estadoRepositorio;
	@Inject
	private LocalRepositorio localRepositorio;
	@Inject
	private AcaoRepositorio acaoRepositorio;
	@Inject
	private FontePagadoraRepositorio fonteRespositorio;
	@Inject
	private ProgramacaoViagemRepositorio programacaoRepositorio;

	@Inject
	private GestaoRepositorio gestaoRepositorio;

	@Inject
	private ColaboradorRepositorio colaboradorRepositorio;

	@Inject
	private DiariaRepositorio diariaRepositorio;

	@Inject
	TrechoRepositorio trechoRepositorio;

	@Inject
	private VeiculoRepositorio veiculoRepositorio;

	@Inject
	private TipoViagemRepositorio tipoViagemRepositorio;

	@Inject
	private DespesaRepositorio despesaRepositorio;

	@Inject
	private FornecedorRepositorio fornecedorRepositorio;

	@Inject
	private CotacaoRepository Cotacaorepositorio;

	@Inject
	private OrcamentoRepositorio orcamentoRepositorio;

	@Inject
	private ProjetoRepositorio projetoRepositorio;

	@Inject
	private LancamentoRepository lancRepositorio;

	@Inject
	private ContaRepository contaRepositorio;

	private TipoParcelamento tipoParcelamento;

	public SolicitacaoViagemService() {
	}

	public List<LancamentoAcao> getLancamentosAcoes(Lancamento lancamento) {
		return lancRepositorio.getLancamentosAcoes(lancamento);
	}

	public List<CategoriaProjeto> getCategoriasDeProjeto(Filtro filtro) {
		return projetoRepositorio.getListaDeCategoriasDeProjeto(filtro);
	}

	public List<ContaBancaria> completeContaColaborador(String s) {
		return contaRepositorio.getContasColaboradores(s);
	}

	public List<Projeto> getProjetosFiltroPorUsuarioMODE01(Filtro filtro, User usuario) {
		return projetoRepositorio.getProjetosFiltroPorUsuarioMODE01(filtro, usuario);
	}

	public List<OrcamentoProjeto> getOrcamentoByProjeto(Long id) {
		return orcamentoRepositorio.getOrcamentosProjeto(id);
	}

	public List<ProjetoRubrica> getRubricasDeProjetoJOIN(Long idProjeto, Long idOrcamento) {
		return orcamentoRepositorio.getRubricasDeProjetoJOIN(idProjeto, idOrcamento);
	}

	public List<Localidade> buscaLocalidade(String s) {
		return localRepositorio.buscarLocalidade(s);
	}

	public List<LancamentoAcao> buscarLancamentosAcao(Lancamento lancamento) {
		return lancRepositorio.getLancamentosAcao(lancamento);
	}

	public List<LancamentoAcao> buscarLancamentosAcao(Long lancamento) {
		return lancRepositorio.getLancamentosAcao(lancamento);
	}

	@Transactional
	public Boolean salvar(SolicitacaoViagem viagem) {
		try {
			if (viagem.getGestao() == null) {
				addMessage("", "Campo Tipo de gestao é obrigatorio.", FacesMessage.SEVERITY_ERROR);
				return false;
			} else if (viagem.getLocalidade() == null) {
				addMessage("", "Campo Tipo de Destino/Localizacao é obrigatorio.", FacesMessage.SEVERITY_ERROR);
				return false;
			} else {
				if (viagem.getId() == null) {
					viagem.setDataEmissao(new Date());
				}

				repositorio.salvar(viagem);
				addMessage("", "Salvo Com Sucesso", FacesMessage.SEVERITY_INFO);
				return true;
			}
		} catch (Exception e) {
			addMessage("", "Salvo Com Sucesso", FacesMessage.SEVERITY_ERROR);
			return false;
		}

	}

	// Deslocado para CalculatorRubricaRepositorio.
	@Deprecated
	public List<ProjetoRubrica> completeRubricasDeProjetoJOIN(String s) {
		return orcamentoRepositorio.completeRubricasDeProjetoJOIN(s);
	}

	public List<Gestao> getGestaoAutoComplete(String query) {
		return gestaoRepositorio.buscarGestaoAutocomplete(query);
	}

	@Transactional
	public SolicitacaoViagem salvar(SolicitacaoViagem viagem, String args) {
		try {
			if (viagem.getGestao() == null) {
				addMessage("", "Campo Tipo de gestao é obrigatorio.", FacesMessage.SEVERITY_ERROR);
				return viagem;
			}
			if (viagem.getId() == null) {
				viagem.setDataEmissao(new Date());
			}
			
			//viagem.setTipoGestao(viagem.getGestao().);
			return repositorio.salvar(viagem, args);
		} catch (Exception e) {
			addMessage("", "Erro ao salvar", FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
			return viagem;
		}

	}

	@Transactional
	public Boolean update(SolicitacaoViagem viagem) {
		try {
			if (viagem.getGestao() == null) {
				addMessage("", "Campo Tipo de gestao é obrigatorio.", FacesMessage.SEVERITY_ERROR);
				return false;
			} else if (viagem.getLocalidade() == null) {
				addMessage("", "Campo Tipo de Destino/Localizacao é obrigatorio.", FacesMessage.SEVERITY_ERROR);
				return false;
			} else {
				if (viagem.getId() == null) {
					viagem.setDataEmissao(new Date());
				}

				repositorio.salvar(viagem);
				addMessage("", "Salvo Com Sucesso", FacesMessage.SEVERITY_INFO);
				return true;
			}
		} catch (Exception e) {
			addMessage("", "Salvo Com Sucesso", FacesMessage.SEVERITY_ERROR);
			return false;
		}

	}

	@Transactional
	public void autorizarDiaria(Diaria diaria, LogStatus log) {

		diaria.setStatusCompra(StatusCompra.CONCLUIDO);

		repositorio.salvarDiaria(diaria);
		repositorio.salvarLog(log);

	}

	@Transactional
	public void salvaPagamento(Diaria diaria) {

		// Diaria diaria = (diaria.getId());
		for (LancamentoAcao lc : diaria.getLancamentosAcoes()) {

			lc.setDespesaReceita(diaria.getDepesaReceita());
			lc.setTipoLancamento(diaria.getTipoLancamento());
			lc.setStatus(StatusPagamento.N_VALIDADO);
			lc = lancRepositorio.salvarLancamentoAcaoReturn(lc);

			PagamentoLancamento pl = new PagamentoLancamento();
			pl.setConta(lc.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getContaBancaria());
			pl.setContaRecebedor(diaria.getContaRecebedor());
			pl.setTipoContaPagador(
					lc.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getContaBancaria().getTipo());
			pl.setTipoContaRecebedor(diaria.getContaRecebedor().getTipo());
			pl.setDataEmissao(diaria.getDataEmissao());
			pl.setDataPagamento(diaria.getDataPagamento());
			pl.setLancamentoAcao(lc);
			pl.setQuantidadeParcela(diaria.getQuantidadeParcela());
			pl.setStt(StatusPagamentoLancamento.PROVISIONADO);
			pl.setTipoParcelamento(diaria.getTipoParcelamento());
			pl.setValor(lc.getValor());
			pl.setTipoLancamento(diaria.getTipoLancamento());
			pl.setDespesaReceita(diaria.getDepesaReceita());

			if (pl.getTipoParcelamento().compareTo(TipoParcelamento.PARCELA_UNICA) == 0) {
				pl.setQuantidadeParcela(1);
			}

			Date dataBase = pl.getDataPagamento();
			tipoParcelamento = pl.getTipoParcelamento();
			BigDecimal totalParcelado = BigDecimal.ZERO;

			for (int i = 1; i <= pl.getQuantidadeParcela(); i++) {
				PagamentoLancamento pagto = new PagamentoLancamento(pl, i);

				if (i == 1) {
					pagto.setDataPagamento(pl.getDataPagamento());
				} else {
					pagto.setDataPagamento(DataUtil.setarData(dataBase, tipoParcelamento));

					if (i == pl.getQuantidadeParcela()) {
						BigDecimal total = pl.getValor();
						BigDecimal resto = total.subtract(totalParcelado);
						pagto.setValor(resto);
					}
				}

				totalParcelado = totalParcelado.add(pagto.getValor());
				lancRepositorio.salvarPagamento(pagto);
				dataBase = pagto.getDataPagamento();
			}
		}

	}

	@Transactional
	public void autorizar(Long id, StatusCompra statusViagem, LogViagem log) {

		repositorio.mudarStatus(id, statusViagem);
		repositorio.salvarLog(log);

		// List<Diaria> diarias = repositorio.findDiariasbyViagem(id);
		//
		// for (Diaria diaria : diarias) {
		//
		// diaria.setStatusCompra(StatusCompra.CONCLUIDO);
		// repositorio.salvarDiaria(diaria);
		// List<LancamentoAcao> lancamentos = new ArrayList<>();
		// lancamentos = lancRepositorio.getLancamentosAcoes(diaria.getId());
		// lancRepositorio.deletarTodosPagamentos(diaria.getId());
		// // Diaria diaria = (diaria.getId());
		// for (LancamentoAcao lc : lancamentos) {
		//
		// lc.setDespesaReceita(diaria.getDepesaReceita());
		// lc.setTipoLancamento(diaria.getTipoLancamento());
		// lc.setStatus(StatusPagamento.N_VALIDADO);
		// lc = lancRepositorio.salvarLancamentoAcaoReturn(lc);
		//
		// PagamentoLancamento pl = new PagamentoLancamento();
		// pl.setConta(lc.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getContaBancaria());
		// pl.setContaRecebedor(diaria.getContaRecebedor());
		// pl.setDataEmissao(diaria.getDataEmissao());
		// pl.setDataPagamento(diaria.getDataPagamento());
		// pl.setLancamento(diaria);
		// pl.setLancamentoAcao(lc);
		// pl.setQuantidadeParcela(diaria.getQuantidadeParcela());
		// pl.setStt(StatusPagamentoLancamento.PROVISIONADO);
		// pl.setTipoParcelamento(diaria.getTipoParcelamento());
		// pl.setValor(lc.getValor());
		// pl.setTipoLancamento(diaria.getTipoLancamento());
		// pl.setDespesaReceita(diaria.getDepesaReceita());
		//
		// if (pl.getTipoParcelamento().compareTo(TipoParcelamento.PARCELA_UNICA) == 0)
		// {
		// pl.setQuantidadeParcela(1);
		// }
		//
		// Date dataBase = pl.getDataPagamento();
		// tipoParcelamento = pl.getTipoParcelamento();
		// BigDecimal totalParcelado = BigDecimal.ZERO;
		//
		// for (int i = 1; i <= pl.getQuantidadeParcela(); i++) {
		// PagamentoLancamento pagto = new PagamentoLancamento(pl, i);
		//
		// if (i == 1) {
		// pagto.setDataPagamento(pl.getDataPagamento());
		// } else {
		// pagto.setDataPagamento(DataUtil.setarData(dataBase, tipoParcelamento));
		//
		// if (i == pl.getQuantidadeParcela()) {
		// BigDecimal total = pl.getValor();
		// BigDecimal resto = total.subtract(totalParcelado);
		// pagto.setValor(resto);
		// }
		// }
		//
		// totalParcelado = totalParcelado.add(pagto.getValor());
		// lancRepositorio.salvarPagamento(pagto);
		// dataBase = pagto.getDataPagamento();
		// }
		// }
		// }
	}

	@Inject
	private AprouveRepositorio aprouveRepositorio;

	public boolean findAprouve(Aprouve aprouve) {
		return aprouveRepositorio.findAprouvePedido(aprouve);
	}

	@Inject
	private CategoriaRepositorio categoriaRepositorio;

	public List<CategoriaDespesaClass> buscarCategorias() {
		return categoriaRepositorio.buscarGategorias();
	}

	public List<SolicitacaoViagem> getSolicitacaoViagem(Filtro filtro) {

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			if (filtro.getDataInicio().after(filtro.getDataFinal())) {
				addMessage("", "Data inicio maior uqe data final", FacesMessage.SEVERITY_ERROR);
				return new ArrayList<SolicitacaoViagem>();
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


		} else if (filtro.getDataInicio() != null) {
			Calendar calInicio = Calendar.getInstance();
			calInicio.set(Calendar.HOUR, 0);
			calInicio.set(Calendar.MINUTE, 0);
			calInicio.set(Calendar.SECOND, 0);
			calInicio.set(Calendar.MILLISECOND, 0);
			filtro.setDataInicio(calInicio.getTime());
		}

		return repositorio.getViagems(filtro);

	}

	public List<Colaborador> buscarColaboradores(String s) {
		return colaboradorRepositorio.buscarColaborador(s);
	}

	public List<Fornecedor> getFornecedores(String s) {
		return fornecedorRepositorio.getFornecedores(s);
	}

	public ViagemRepositorio getRepositorio() {
		return repositorio;
	}

	public SolicitacaoViagem getViagemById(Long id) {
		return repositorio.getViagemPorId(id);
	}

	public ProgamacaoViagem getProgById(Long id) {
		return programacaoRepositorio.getProgViagem(id);
	}

	public DespesaViagem getDespesaById(Long id) {
		return despesaRepositorio.getDespesaById(id);
	}

	public Diaria getDiariaById(Long id) {
		return diariaRepositorio.diariaById(id);
	}

	@Transactional
	public void excluirProg(ProgamacaoViagem pviagem) {
		programacaoRepositorio.remover(pviagem);
	}

	public void setRepositorio(ViagemRepositorio repositorio) {
		this.repositorio = repositorio;
	}

	public EstadoRepositorio getEstadoRepositorio() {
		return estadoRepositorio;
	}

	public void setEstadoRepositorio(EstadoRepositorio estadoRepositorio) {
		this.estadoRepositorio = estadoRepositorio;
	}

	public LocalRepositorio getLocalRepositorio() {
		return localRepositorio;
	}

	public void setLocalRepositorio(LocalRepositorio localRepositorio) {
		this.localRepositorio = localRepositorio;
	}

	public AcaoRepositorio getAcaoRepositorio() {
		return acaoRepositorio;
	}

	public void setAcaoRepositorio(AcaoRepositorio acaoRepositorio) {
		this.acaoRepositorio = acaoRepositorio;
	}

	public FontePagadoraRepositorio getFonteRespositorio() {
		return fonteRespositorio;
	}

	public void setFonteRespositorio(FontePagadoraRepositorio fonteRespositorio) {
		this.fonteRespositorio = fonteRespositorio;
	}

	public FornecedorRepositorio getFornecedorRepositorio() {
		return fornecedorRepositorio;
	}

	public void setFornecedorRepositorio(FornecedorRepositorio fornecedorRepositorio) {
		this.fornecedorRepositorio = fornecedorRepositorio;
	}

	public CotacaoRepository getCotacaorepositorio() {
		return Cotacaorepositorio;
	}

	public void setCotacaorepositorio(CotacaoRepository cotacaorepositorio) {
		Cotacaorepositorio = cotacaorepositorio;
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Localidade getLocalidade(Long id) {
		return localRepositorio.getLocalPorId(id);
	}

	public List<TipoViagem> getTipoViagem() {
		return tipoViagemRepositorio.getTipoViagem();
	}

	public List<Veiculo> getVeiculos() {
		return veiculoRepositorio.getVeiculos();
	}

	public List<Estado> getEstados(Filtro filtro) {
		return estadoRepositorio.getEatados(filtro);
	}

	public List<Localidade> getMunicipioByEstado(Long id) {
		return localRepositorio.getMunicipioByEstado(id);
	}

	/*--- Progamacao ---*/

	public List<ProgamacaoViagem> getProgViagems(SolicitacaoViagem viagem) {

		return programacaoRepositorio.getProgViagemsByViagems(viagem);

	}

	public List<ProgamacaoViagem> setProgViagems(SolicitacaoViagem viagem) {

		Long id = viagem.getId();
		return repositorio.setProgamacaoViagem(id);

	}

	@Transactional
	public ProgamacaoViagem salvarProgamacao(ProgamacaoViagem pviagem) {
		return programacaoRepositorio.salvar(pviagem);
	}

	@Transactional
	public boolean removerProgamacao(ProgamacaoViagem pviagem) {
		return programacaoRepositorio.remover(pviagem);
	}

	@Transactional
	public boolean removerDespesa(DespesaViagem despesa) {
		programacaoRepositorio.removerDespesa(despesa);
		return true;
	}

	/*--- Despesas ---*/
	public List<DespesaViagem> getDespesasByViagem(SolicitacaoViagem viagem) {
		return despesaRepositorio.getDespesaByViagems(viagem);
	}

	public List<DespesaViagem> getDespesasByViagemFromTipo(SolicitacaoViagem viagem, TipoDespesa tipo) {
		return despesaRepositorio.getDespesaByViagemsFromTipo(viagem, tipo);
	}

	public List<DespesaViagem> setDespesas(SolicitacaoViagem viagem) {
		Long id = viagem.getId();
		return repositorio.setDespesaViagem(id);
	}

	@Transactional
	public DespesaViagem salvarDespesa(DespesaViagem despesa) {
		return despesaRepositorio.salvar(despesa);
	}

	/*-- Diaria de viagem--*/

	public List<Diaria> getDiariasByViagem(SolicitacaoViagem viagem) {
		return diariaRepositorio.getDiariaByViagems(viagem);
		// return repositorio.findDiariasbyViagem(viagem);
	}

	public List<Diaria> setDiarias(SolicitacaoViagem viagem) {
		Long id = viagem.getId();
		return repositorio.setDiarias(id);
	}

	@Transactional
	public Diaria salvarDiaria(Diaria diaria) {
		return (Diaria) diariaRepositorio.salvar(diaria);
	}

	@Transactional
	public Boolean removerDiaria(Diaria diaria) {
		if (diariaRepositorio.remover(diaria)) {
			return true;
		} else {
			return false;
		}
	}

	/*-- Trechos--*/

	public List<Trecho> setTrechos(Diaria diaria) {

		Long id = diaria.getId();
		return diariaRepositorio.setTrechos(id);
	}

	public List<Trecho> getTrechoByDiaria(Diaria diaria) {
		return trechoRepositorio.getTrechosByDiaria(diaria);
	}

	public void salvarTrecho(Trecho trecho) {
		trechoRepositorio.salvar(trecho);
	}

	public List<Colaborador> getColaborador() {
		return colaboradorRepositorio.findAll();
	}

	public List<Fornecedor> getFornecedores() {
		return fornecedorRepositorio.getFornecedores();
	}

	@Transactional
	public Boolean remover(SolicitacaoViagem viagem) {

		// TODO: Chriis só precisamos do primeiro IF e a verificação é de diárias que
		// estegam com statusCompra 'Concluído', caso
		// o exista diárias com esse status o sistema não deve deixar excluir a SV, só é
		// mudar esse método pra buscar diárias
		// o status desgnado

		if (repositorio.setDiarias(viagem.getId()).size() > 0) {
			addMessage("", "Essa Solicitação de Viagem está vinculado a 1 ou mais Diarias, Não Foi possivel excluir.",
					FacesMessage.SEVERITY_ERROR);
			return false;
		}
		// else if (repositorio.setDespesaViagem(viagem.getId()).size() > 0) {
		// addMessage("", "Essa Solicitação de Viagem está vinculado a 1 ou mais
		// Despesas, Não Foi possivel excluir.",
		// FacesMessage.SEVERITY_ERROR);
		// return false;
		//
		// } else if (repositorio.setProgamacaoViagem(viagem.getId()).size() > 0) {
		// addMessage("",
		// "Essa Solicitação de Viagem está vinculado a 1 ou mais Programações, Não Foi
		// possivel excluir.",
		// FacesMessage.SEVERITY_ERROR);
		// return false;
		// }

		try {
			for (LogViagem log : repositorio.findLogByViagem(viagem.getId())) {
				repositorio.deleteLogViagem(log);
			}

			repositorio.remover(viagem);

			// repositorio.deleteLogByViagem(viagem.getId());
			// repositorio.deleteViagem(viagem.getId());
			addMessage("", "Removido.", FacesMessage.SEVERITY_INFO);
			return true;
		} catch (Exception e) {
			addMessage("", "Houve um erro ao excluir, por favor entre em contato com  o Administrador do sistema.",
					FacesMessage.SEVERITY_ERROR);
			return false;
		}
	}

	public String verificaConflitoPeriodo(Diaria diaria) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return repositorio.verificaConflitoPeriodo(diaria.getContaRecebedor().getId(),
				sdf.format(diaria.getDataIda()), sdf.format(diaria.getDataVolta()), diaria.getId());
	}

}
