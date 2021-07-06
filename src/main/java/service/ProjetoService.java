package service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import anotacoes.Transactional;
import model.Acao;
import model.AcaoFonte;
import model.AcaoProduto;
import model.AprovacaoProjeto;
import model.AtividadeProjeto;
import model.CadeiaProdutiva;
import model.CategoriaDespesaClass;
import model.CategoriaProjeto;
import model.Componente;
import model.ComponenteClass;
import model.Comunidade;
import model.ContaBancaria;
import model.Estado;
import model.FontePagadora;
import model.Gestao;
import model.LancamentoAuxiliar;
import model.Localidade;
import model.MetaPlano;
import model.Orcamento;
import model.OrcamentoProjeto;
import model.PlanoDeTrabalho;
import model.Produto;
import model.Projeto;
import model.ProjetoRubrica;
import model.QualifProjeto;
import model.RelatorioAtividade;
import model.RubricaOrcamento;
import model.StatusAtividade;
import model.SubComponente;
import model.SubPrograma;
import model.UnidadeConservacao;
import model.User;
import model.UserProjeto;
import repositorio.AcaoFonteRepositorio;
import repositorio.AcaoRepositorio;
import repositorio.AtividadeRepositorio;
import repositorio.CadeiaRepositorio;
import repositorio.ComponenteRepositorio;
import repositorio.EstadoRepositorio;
import repositorio.FontePagadoraRepositorio;
import repositorio.GestaoRepositorio;
import repositorio.LocalRepositorio;
import repositorio.OrcamentoRepositorio;
import repositorio.PagamentoLancamentoRepository;
import repositorio.PlanoTrabalhoRepositorio;
import repositorio.ProdutoRepositorio;
import repositorio.ProjetoRepositorio;
import repositorio.UsuarioRepository;
import util.Filtro;

/**
 * Autor: Italo Almeida Classe: Gestao de Projeto Objetivo: Gerir a regra de
 * negocio referente a instancia do objeto
 */

public class ProjetoService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ProjetoRepositorio repositorio;
	@Inject
	private AcaoRepositorio acaoRepositorio;
	@Inject
	private FontePagadoraRepositorio fonteRepositorio;
	@Inject
	private AcaoFonteRepositorio acaoFonteRepositorio;
	@Inject
	private CadeiaRepositorio cadeiaRepositorio;
	@Inject
	private LocalRepositorio localRepositorio;
	@Inject
	private EstadoRepositorio estadoRepositorio;
	@Inject
	private AtividadeRepositorio atividadeRepositorio;
	@Inject
	private UsuarioRepository usuarioRepositorio;
	@Inject
	private ProdutoRepositorio produtoRepositorio;

	@Inject
	private ComponenteRepositorio componenteRepositorio;

	@Inject
	private OrcamentoRepositorio orcamentoRepositorio;

	@Inject
	private PlanoTrabalhoRepositorio planoRepositorio;

	@Inject
	private PagamentoLancamentoRepository pagRepositorio;

	public ProjetoService() {
	}

	@Transactional
	public void desativarAcoes(Long projeto) {
		acaoRepositorio.desativarAcoes(projeto);
		;
	}

	public List<QualifProjeto> carregarQualificacoes() {
		return repositorio.getQualificacoes();
	}

	public List<Projeto> getProjetosFilter(Filtro filtro) throws NumberFormatException, ParseException {
		return orcamentoRepositorio.getProjetosFilter(filtro, "");
	}

	public List<Projeto> getProjetosRelatorioGeral(Filtro filtro) throws NumberFormatException, ParseException {
		return orcamentoRepositorio.getProjetosRelatorioGeral(filtro, "");
	}

	public List<Projeto> getProjetosByUsuario(User usuario) {
		return repositorio.getProjetosbyUsuario(usuario);
	}

	public List<Projeto> getProjetosbyUsuarioProjeto(User usuario) {
		return repositorio.getProjetosbyUsuarioProjeto(usuario);
	}

	public List<Projeto> getProjetosbyUsuarioProjeto(User usuario, Filtro filtro) {
		return repositorio.getProjetosbyUsuarioProjeto(usuario, filtro);
	}

	// CÓDIGO PRA CONSULTAS EM SELECT DEVEM CONCATENAR CÓDIGO + NOME DO PROJETO
	public List<Projeto> getProjetosbyUsuarioProjetoLIBERACAO(User usuario, Filtro filtro) {
		return repositorio.getProjetosbyUsuarioProjetoLIBERACAO(usuario, filtro);
	}

	public List<SubPrograma> getSubProgramas() {
		return repositorio.getSubProgramas();
	}

	public SubPrograma getSubPrograma(Long id) {
		return repositorio.getSubPrograma(id);
	}

	@Transactional
	public OrcamentoProjeto salvarOrcamentoProjeto(OrcamentoProjeto orcamentoProjeto) {
		return orcamentoRepositorio.salvarOrcamentoProjeto(orcamentoProjeto);
	}

	@Transactional
	public CategoriaProjeto salvarCategoriaDeProjeto(CategoriaProjeto categoriaProjeto) {
		return repositorio.salvarCategoriaDeProjeto(categoriaProjeto);
	}

	@Transactional
	public Boolean removerCategoriaProjeto(CategoriaProjeto categoria) {
		return repositorio.removerCategoriaDeProjeto(categoria);
	}

	public List<CategoriaProjeto> getListaDeCategoriasDeProjeto(Filtro filtro) {
		return repositorio.getListaDeCategoriasDeProjeto(filtro);
	}

	public List<LancamentoAuxiliar> getExtratoCtrlProjeto(Filtro filtro) {

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null)
			if (filtro.getDataFinal().before(filtro.getDataInicio())) {
				addMessage("", "Data final maior que a data de inicio", FacesMessage.SEVERITY_INFO);
			}

		return pagRepositorio.getExtratoCtrlProjeto(filtro);
	}

	public BigDecimal getTotalAtividadeByProjeto(Long id, Long idAtividade) {
		return atividadeRepositorio.getTotalAtividadeByProjeto(id, idAtividade);
	}

	public BigDecimal getTotalAtividadeExecutadaByProjeto(Long id) {
		return atividadeRepositorio.getTotalAtividadeExecutadaByProjeto(id);
	}

	public BigDecimal getTotalAtividadeByProjetoNaoPlanejado(Long id) {
		return atividadeRepositorio.getTotalAtividadeByProjetoNaoPlanejada(id);
	}

	// TODO: PASSEI POR AQUI
	public List<LancamentoAuxiliar> getExtratoCtrlProjetoOrcamentoExecutado(Filtro filtro) {

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null)
			if (filtro.getDataFinal().before(filtro.getDataInicio())) {
				addMessage("", "Data final maior que a data de inicio", FacesMessage.SEVERITY_INFO);
			}

		return pagRepositorio.getExtratoCtrlProjetoOrcamentoExecutado(filtro);
	}

	// TODO: LINHA ORÇAMENTÁRIA
	public List<LancamentoAuxiliar> getExtratoOrcamentoExecutadoLinhaOrcamentaria(Filtro filtro) {

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null)
			if (filtro.getDataFinal().before(filtro.getDataInicio())) {
				addMessage("", "Data final maior que a data de inicio", FacesMessage.SEVERITY_INFO);
			}

		return pagRepositorio.getExtratoByLinhaOrcamentaria(filtro);
	}

	public List<LancamentoAuxiliar> getRelacaoDespesasLinhaOrcamentaria(Filtro filtro) {

		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null)
			if (filtro.getDataFinal().before(filtro.getDataInicio())) {
				addMessage("", "Data final maior que a data de inicio", FacesMessage.SEVERITY_INFO);
			}

		return pagRepositorio.getRelacaoDespesasLinhaOrcamentaria(filtro);
	}

	public List<ProjetoRubrica> getRubricaDeProjeto(Long id) {
		return repositorio.getRubricasDeProjeto(id);
	}

	public List<ProjetoRubrica> getRubricaDeProjeto(Long id, Filtro filtro) {
		return repositorio.getRubricasDeProjeto(id, filtro);
	}

	public List<ProjetoRubrica> getProjetoRubricaByRubricaOrcamento(RubricaOrcamento rubricaOrcamento) {
		return repositorio.getProjetosRubricasByRubricaOrcamento(rubricaOrcamento);
	}

	public List<ProjetoRubrica> getNewProjetoRubricaByRubricaOrcamento(RubricaOrcamento rubricaOrcamento) {
		return repositorio.getNewProjetosRubricasByRubricaOrcamento(rubricaOrcamento);
	}

	public List<PlanoDeTrabalho> carregarPlanos(User usuario) {
		return planoRepositorio.getPlanosFiltroPorUsuario(new Filtro(), usuario);
	}

	public List<PlanoDeTrabalho> getPlanos() {
		return planoRepositorio.getPlanos();
	}

	public UnidadeConservacao findUcById(Long id) {
		return localRepositorio.findByIdUnidadeDeConservacao(id);
	}

	public List<ProjetoRubrica> getProjetoRubricaByOrcamentoRubrica(Long id) {
		return orcamentoRepositorio.getProjetoRubricaByOrcamentoRubrica(id);
	}

	public ProjetoRubrica getProjetoRubricaById(Long id) {
		return orcamentoRepositorio.getProjetoRubricaById(id);
	}

	public ProjetoRubrica getProjetoRubricaByIdDetail(Long id) {
		return orcamentoRepositorio.getRubricaBydIdDetail(id);
	}

	public List<Projeto> getProjetoAutoCompleteMODE01(String s) {
		return repositorio.getProjetoAutocompleteMODE01(s);
	}

	public Projeto getDetalhesProjetoById(Projeto projeto) {
		return repositorio.getDetalhesProjetosById(projeto);
	}

	public List<ContaBancaria> getAllConta(String s) {
		return pagRepositorio.getAllConta(s);
	}

	@Transactional
	public void removerRelacaoOrcamento(OrcamentoProjeto orcamentoProjeto) {
		orcamentoRepositorio.removerRelacaoOrcamento(orcamentoProjeto);
	}

	@Transactional
	public void removerProjetoRubrica(ProjetoRubrica rubrica) {
		orcamentoRepositorio.removerProjetoRubrica(rubrica);
	}

	@Transactional
	public ProjetoRubrica salvarProjetoRubrica(ProjetoRubrica projetoRubrica) {
		return orcamentoRepositorio.salvarProjetoRubrica(projetoRubrica);
	}

	// Desconsiderar método nas próximas versões
	public List<ProjetoRubrica> getRubricasDeProjeto(Long idProjeto) {
		return orcamentoRepositorio.getRubricasDeProjeto(idProjeto);
	}

	public List<RubricaOrcamento> getRubricasDeOrcamento(Long idOrcamento, Filtro filtro) {
		return orcamentoRepositorio.getRubricasDeOrcamento(idOrcamento, filtro);
	}

	public List<RubricaOrcamento> completetRubricasDeOrcamento(Long idOrcamento, Filtro filtro) {
		return orcamentoRepositorio.getRubricasDeOrcamento(idOrcamento, filtro);
	}

	public List<OrcamentoProjeto> getRelacaoDeOrcamentos(Long idProjeto) {
		return orcamentoRepositorio.getRelacaoDeOrcamentos(idProjeto);
	}
	
	public List<Orcamento> loadDonations(Long idManagement) {
		return orcamentoRepositorio.loadDonations(idManagement);
	}

	public List<Produto> getProdutos(String s) {
		return produtoRepositorio.getProduto(s);
	}

	public List<Orcamento> getOrcamentoAutoComplete(String txt) {
		return orcamentoRepositorio.getOrcamentoAutoComplete(txt);
	}

	public List<AcaoProduto> getItensByAcao(Acao acao) {
		return acaoRepositorio.getItensByAcao(acao);
	}

	public List<SubComponente> getSubComponentes(Long idComponente) {
		return componenteRepositorio.getSubComponentes(idComponente);
	}

	public List<ComponenteClass> getComponentes() {
		return componenteRepositorio.getComponentes();
	}

	public ProjetoService(ProjetoRepositorio repositorio) {
		this.repositorio = repositorio;
	}

	public List<FontePagadora> getFontes() {
		return fonteRepositorio.getFontes();
	}

	public List<LancamentoAuxiliar> getLancamentosReport(Filtro filtro) {
		return repositorio.getLancamentosReport(filtro);
	}

	private @Inject GestaoRepositorio gestaoRepositorio;

	public List<Gestao> getGestaoAutoComplete(String query) {
		return gestaoRepositorio.buscarGestaoAutocomplete(query);
	}

	@Transactional
	public void removerAcao(Acao acao) {
		acaoRepositorio.remover(acao);
	}

	@Transactional
	public Boolean salvarUsuarioNoProjeto(UserProjeto usuarioProjeto) {
		if (repositorio.verificarUsuarioNoProjeto(usuarioProjeto) > 0) {
			// addMessage("", "Esse usuário já está no projeto.",
			// FacesMessage.SEVERITY_ERROR);
			return false;

		} else {
			repositorio.salvarUsuarioNoProjeto(usuarioProjeto);
			return true;
		}
	}

	@Transactional
	public Boolean excluirUsuarioDoProjeto(UserProjeto usuarioProjeto) {
		repositorio.removerUsuarioProjeto(usuarioProjeto);
		return true;
	}

	public List<UserProjeto> getUsuariosProjetos(Projeto projeto) {
		return repositorio.getUsuariosProjetos(projeto);
	}

	public List<User> getUsuario(String nome) {
		return usuarioRepositorio.getUsuario(nome);
	}

	public Integer getQuantidadeAtividade(Projeto projeto, StatusAtividade status) {
		return repositorio.getQuantidadeAtividade(projeto, status);
	}

	@Transactional
	public Boolean salvarAtividade(AtividadeProjeto atividade) {
		atividadeRepositorio.salvar(atividade);
		return true;

	}

	@Transactional
	public void mudarStatus(AtividadeProjeto atividade) {
		atividadeRepositorio.salvar(atividade);
	}

	@Transactional
	public void alterarStatus(AtividadeProjeto atividade) {
		atividadeRepositorio.salvar(atividade);
	}

	@Transactional
	public void removerAtividade(AtividadeProjeto atividade) {
		atividadeRepositorio.remover(atividade);
	}

	public AtividadeProjeto getAtividadeById(Long id) {
		return atividadeRepositorio.getAtividadeById(id);
	}

	public List<AtividadeProjeto> getAtividades(Projeto projeto) {
		if (projeto.getId() == null)
			return new ArrayList<AtividadeProjeto>();
		return atividadeRepositorio.getAtividade(projeto);
	}

	public List<RelatorioAtividade> getAtividadesParaRelatorio(Long id) {
		if (id == null)
			return new ArrayList<RelatorioAtividade>();
		return atividadeRepositorio.getAtividadesParaRelatorio(id);
	}

	public List<AtividadeProjeto> getCronogramaPlanejado(Projeto projeto) {
		if (projeto.getId() == null)
			return new ArrayList<AtividadeProjeto>();
		return atividadeRepositorio.getCronogramaPlanejado(projeto);
	}

	public List<AtividadeProjeto> getCronogramaExecutado(Projeto projeto) {
		if (projeto.getId() == null)
			return new ArrayList<AtividadeProjeto>();
		return atividadeRepositorio.getCronogramaExecutado(projeto);
	}

	public List<AtividadeProjeto> getCronogramaExecutadoNaoPlanejado(Projeto projeto) {
		if (projeto.getId() == null)
			return new ArrayList<AtividadeProjeto>();
		return atividadeRepositorio.getCronogramaExecutadoNaoPlanejado(projeto);
	}

	public List<AtividadeProjeto> getAtividadeByAcao(Long id) {
		return atividadeRepositorio.getAtividadeByAcao(id);
	}

	public List<Comunidade> getComunidade(Comunidade comunidade) {
		return localRepositorio.getComunidade(comunidade);
	}

	public Acao getAcao(Acao acao) {
		return acaoRepositorio.getAcaoPorId(acao.getId());
	}

	public List<Estado> getEstados(Filtro filtro) {
		return estadoRepositorio.getEatados(filtro);
	}

	public List<Localidade> getMunicipioByEstado(Long id) {
		return localRepositorio.getMunicipioByEstado(id);
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@Transactional
	public Boolean salvarAcao(Acao acao) {
		if (acaoRepositorio.verificarCodigo(acao).isEmpty()) {
			acaoRepositorio.salvar(acao);
			addMessage("", "Ação salva com sucesso!", FacesMessage.SEVERITY_INFO);
			return true;
		} else {
			addMessage("", "Erro: Esse código ja existe.", FacesMessage.SEVERITY_ERROR);
			return false;
		}
	}

	@Transactional
	public void salvar(Projeto projeto, User usuario) {

		if (repositorio.verificarNomeProjeto(projeto).isEmpty()) {
			repositorio.salvar(projeto, usuario);
			addMessage("", "Projeto salvo com sucesso!", FacesMessage.SEVERITY_INFO);
		} else {
			addMessage("", "Erro: Esse nome já existe.", FacesMessage.SEVERITY_ERROR);
		}
	}

	@Transactional
	public Projeto salvarMODE01(Projeto projeto, User usuario) {

		if (repositorio.verificarNomeProjeto(projeto).isEmpty()) {
			// addMessage("", "Projeto salvo com sucesso!", FacesMessage.SEVERITY_INFO);
			messageSalvamento("Salvo com sucesso!", FacesMessage.SEVERITY_INFO);
			return repositorio.salvarMODE01(projeto, usuario);
		} else {
			// addMessage("","Erro: Esse nome já existe.", FacesMessage.SEVERITY_ERROR);
			messageSalvamento("Erro: Esse nome já existe.", FacesMessage.SEVERITY_ERROR);
			return projeto;
		}
	}

	@Transactional
	public Projeto salvarInPlanoMODE01(Projeto projeto, User usuario) {

		if (repositorio.verificarNomeProjeto(projeto).isEmpty()) {
			// addMessage("", "Projeto salvo com sucesso!", FacesMessage.SEVERITY_INFO);
			// messageSalvamento("Salvo com sucesso!", FacesMessage.SEVERITY_INFO);
			return repositorio.salvarMODE01(projeto, usuario);
		} else {
			// addMessage("","Erro: Esse nome já existe.", FacesMessage.SEVERITY_ERROR);
			// messageSalvamento("Erro: Esse nome já existe.", FacesMessage.SEVERITY_ERROR);
			return projeto;
		}
	}

	public void messageSalvamento(String txt, FacesMessage.Severity tipo) {

		// StringBuilder msg = new StringBuilder("Projeto salvo com sucesso");
		FacesMessage message = new FacesMessage(tipo, "Aviso", txt);

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public List<Acao> getAcoes(Projeto projeto) {
		if (projeto.getId() == null) {
			return new ArrayList<Acao>();
		} else {
			// return acaoRepositorio.getAcoes(projeto);
			// return acaoRepositorio.getAcoesIsoladas(projeto);
			return acaoRepositorio.getAcoesIsoladas3(projeto);

		}
	}

	public List<OrcamentoProjeto> findAllOrcamentos() {
		return repositorio.findAll();
	}

	public List<Acao> getAcoesMODE01(Projeto projeto) {
		if (projeto.getId() == null) {
			return new ArrayList<Acao>();
		} else {
			// acaoRepositorio.getAcoes(projeto);
			// return acaoRepositorio.getAcoesIsoladas(projeto);
			return acaoRepositorio.getAcoesDeProjeto(projeto.getId());
		}
	}

	public List<MetaPlano> getAcoesPlanoDeTrabalho(Projeto projeto) {
		if (projeto.getId() == null) {
			return new ArrayList<MetaPlano>();
		} else {
			// return acaoRepositorio.getAcoes(projeto);
			// return acaoRepositorio.getAcoesIsoladas(projeto);
			return acaoRepositorio.getAcoesDoPlanoDeTrabalho(projeto);

		}
	}
	
	public List<Acao> getTodasAcoes() {
		return acaoRepositorio.getTodasAcoes();
	}

	public List<CadeiaProdutiva> getCadeias(Componente componente) {
		return cadeiaRepositorio.getCadeias(componente);
	}

	public List<CadeiaProdutiva> getCadeias() {
		return cadeiaRepositorio.getCadeias();
	}

	public List<AcaoFonte> getAcaoFonte(Acao acao) {
		return acaoFonteRepositorio.getAcaoFonte(acao);
	}

	@Transactional
	public void remover(Projeto projeto) {
		try {
			repositorio.deleteVinculoUsuario(projeto.getId());
			repositorio.deleteVinculoDoacao(projeto.getId());
			repositorio.deleteProjetoRubrica(projeto.getId());
			repositorio.remover(projeto);
			addMessage("", "Removido.", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			addMessage("", "Não foi possível remover projeto, entre me contato com  o Administrador do sistema.",
					FacesMessage.SEVERITY_ERROR);
		}

	}

	public List<Projeto> getProjetos(Filtro filtro, User usuario) {
		return repositorio.getProjetos(filtro, usuario);
	}

	public List<Projeto> getAllProjetoMODE01() {
		return repositorio.getAllProjetosMODE01();
	}

	@Deprecated
	public List<Projeto> getProjetosAutoComplete(String query) {
		return repositorio.getProjetoAutocomplete(query);
	}

	public List<Projeto> getProjetosAutoCompleteMode01(String query) {
		return repositorio.getProjetoAutocompleteMODE01(query);
	}

	public List<CategoriaDespesaClass> getCategoriaAutoComplete(String query) {
		return repositorio.getCategoriaAutocomplete(query);
	}

	public List<Projeto> getProjetosFiltroPorUsuario(Filtro filtro, User usuario) {
		return repositorio.getProjetosFiltroPorUsuario(filtro, usuario);
	}

	public List<Projeto> getProjetosFiltroPorUsuarioMODE01(Filtro filtro, User usuario) {
		return repositorio.getProjetosFiltroPorUsuarioMODE01(filtro, usuario);
	}

	public List<Projeto> getProjetosFiltroPorUsuarioWidthColor(Filtro filtro, User usuario) {
		return repositorio.getProjetosFiltroPorUsuarioWidthColor(filtro, usuario);
	}

	public List<Projeto> getAllProject(Filtro filtro, User usuario) {
		return repositorio.getAllProject(filtro, usuario);
	}

	public Projeto getProjetoById(Long id) {
		return repositorio.getProjetoPorId(id);
	}

	public List<OrcamentoProjeto> findAll() {
		// TODO Auto-generated method stub
		return repositorio.findAll();
	}

	public OrcamentoProjeto findOrcamentoProjetoById(Long id) {
		try {
			return repositorio.findOrcamentoProjetoById(id);
		} catch (NoResultException e) {
			return new OrcamentoProjeto();
		}
	}

	public Boolean verificaPrivilegioByProjeto(Projeto projeto, User usuario) {
		try {
			return repositorio.verificaPrivilegioByProjeto(projeto, usuario);
		} catch (NoResultException e) {
			return false;
		}

	}

	public List<Projeto> getProjetosByPlanoDeTrabalhoAndGestao(Filtro filtro) {
		try {
			return repositorio.getProjetosByPlanoDeTrabalhoAndGestao(filtro);
		} catch (NoResultException e) {
			return new ArrayList<Projeto>();
		}
	}

	public Integer getQuantidadeAtividadeMes(Projeto projeto, StatusAtividade status, int mes) {
		try {
			return repositorio.getQuantidadeAtividadeMes(projeto, status, mes);
		} catch (NoResultException e) {
			return 0;
		}
	}

	public Integer getQuantidadeAtividadeTotalPorProjeto(Long id) {
		return repositorio.getQuantidadeAtividadeTotalPorProjeto(id);
	}

	public Integer getQuantidadeAtividadeTotalPorProjetoExecutado(Long id) {
		return repositorio.getQuantidadeAtividadeTotalPorProjetoExecutado(id);
	}

	public Integer getQuantidadeAtividadeTotalPorProjetoNaoPlanejada(Long id) {
		return repositorio.getQuantidadeAtividadeTotalPorProjetoNaoPlanejada(id);
	}

	public Integer getQuantidadeAtividadeMes(Projeto projeto, StatusAtividade status, int mes, Boolean planejado,
			int ano) {
		try {

			return repositorio.getQuantidadeAtividadeMesNOVO(projeto, status, mes, planejado, ano);
			// return
			// repositorio.getQuantidadeAtividadeMes(projeto,status,mes,planejado,ano);
		} catch (NoResultException e) {
			return 0;
		}
	}

	public Integer getQuantidadeAtividadeMesPlanejado(Projeto projeto, int mes, int ano) {
		try {

			return repositorio.getQuantidadeAtividadeMesPlanejado(projeto, mes, ano);
			// return
			// repositorio.getQuantidadeAtividadeMes(projeto,status,mes,planejado,ano);
		} catch (NoResultException e) {
			return 0;
		}
	}

	public Integer getQuantidadeAtividadeMesNaoPlanejado(Projeto projeto, int mes, int ano) {
		try {

			return repositorio.getQuantidadeAtividadeMesNaoPlanejado(projeto, mes, ano);
			// return
			// repositorio.getQuantidadeAtividadeMes(projeto,status,mes,planejado,ano);
		} catch (NoResultException e) {
			return 0;
		}
	}

	public Integer getQuantidadeAtividadeMesExecutado(Projeto projeto, int mes, int ano) {
		try {

			return repositorio.getQuantidadeAtividadeMesExecutado(projeto, mes, ano);
			// return
			// repositorio.getQuantidadeAtividadeMes(projeto,status,mes,planejado,ano);
		} catch (NoResultException e) {
			return 0;
		}
	}

	// ---------------------------------------- 14/02/2019
	// 00:39---------------------------------

	public List<AtividadeProjeto> getAtividadeWithSaldoByProjeto(Long projetoId) {
		return atividadeRepositorio.getAtividadeWithSaldoByProjeto(projetoId);
	}

	public Boolean verificaSaldoAtividade(Long idAtividadeProjeto, BigDecimal valor) {
		return atividadeRepositorio.verificarSaldoAtividade(idAtividadeProjeto, valor);
	}

	public List<AtividadeProjeto> getAtividadeProjetoByLancamento(Long idLancamento) {
		return atividadeRepositorio.getAtividadeWithSaldoByLancamento(idLancamento);
	}

	// fim
	// ---------------------------------------------------------------------------------

	@Transactional
	public AprovacaoProjeto salvarAprovacao(AprovacaoProjeto aprovacaoProjeto) {
		return repositorio.salvarAprovacao(aprovacaoProjeto);
	}

	public List<AprovacaoProjeto> obterAprovacoesProjeto(Projeto projeto) {
		return repositorio.obterAprovacoesProjeto(projeto);
	}

}
