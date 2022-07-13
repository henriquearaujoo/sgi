package managedbean;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.TabView;

import anotacoes.Transactional;
import model.Aprouve;
import model.AtividadeDespesa;
import model.AtividadeProjeto;
import model.CategoriaDespesaClass;
import model.CategoriaProjeto;
import model.Colaborador;
import model.Componente;
import model.ContaBancaria;
import model.DespesaReceita;
import model.DespesaViagem;
import model.Destino;
import model.Diaria;
import model.Estado;
import model.Fornecedor;
import model.Gestao;
import model.LancamentoAcao;
import model.Localidade;
import model.LogStatus;
import model.LogViagem;
import model.Municipio;
import model.OrcamentoProjeto;
import model.ProgamacaoViagem;
import model.Projeto;
import model.ProjetoRubrica;
import model.SolicitacaoViagem;
import model.StatusCompra;
import model.TipoDespesa;
import model.TipoDiaria;
import model.TipoGestao;
import model.TipoLocalidade;
import model.TipoParcelamento;
import model.TipoViagem;
import model.Trecho;
import model.UnidadeMedida;
import model.Veiculo;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import repositorio.CalculatorRubricaRepositorio;
import repositorio.GestaoRepositorio;
import repositorio.LancamentoRepository;
import repositorio.LocalRepositorio;
import service.PagamentoService;
import service.ProjetoService;
import service.SolicitacaoPagamentoService;
import service.SolicitacaoViagemService;
import util.CDILocator;
import util.Filtro;
import util.UsuarioSessao;
import util.Util;

@Named(value = "sv_cadastro_controller")
@ViewScoped
public class ViagemCadastroController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private @Inject SolicitacaoViagem viagem;

	private @Inject SolicitacaoViagemService viagemService;

	private @Inject SolicitacaoPagamentoService pagamentoService;

	private @Inject PagamentoService service;

	@Inject
	private ProjetoService projetoService;

	@Inject
	private LancamentoRepository lancRepositorio;

	private Filtro filtro = new Filtro();

	private Long idEstado;
	private String tipoGestao = "";
	private String local = "";

	private String tab = "";
	private int indexTrecho = 0;
	private boolean retirarTrecho = false;

	private int indexProgamacao = 0;
	private boolean retirarProgramacao = false;

	private BigDecimal totalDeDiarias;
	private BigDecimal totalRecurso;

	@Inject
	private LancamentoAcao lancamentoAcao;

	@Inject
	private CalculatorRubricaRepositorio calculatorRubricaRepositorio;

	private Projeto projetoAux;

	public Projeto getProjetoAux() {
		return projetoAux;
	}

	public void setProjetoAux(Projeto projetoAux) {
		this.projetoAux = projetoAux;
	}

	private boolean showTabs = false;

	private Double totalReal;

	private TabView tabview;

	private List<Gestao> gestoes;
	private List<Estado> estados;
	private List<Localidade> localidades = new ArrayList<Localidade>();
	private List<TipoViagem> tipoViagem = new ArrayList<TipoViagem>();
	private List<Veiculo> veiculo = new ArrayList<Veiculo>();
	private List<Colaborador> colaboradores = new ArrayList<>();
	private List<ProgamacaoViagem> programacoes = new ArrayList<>();

	private List<CategoriaProjeto> listCategoriaProjeto = new ArrayList<>();

	private List<ContaBancaria> contas = new ArrayList<>();

	private List<OrcamentoProjeto> orcamentosProjetos = new ArrayList<>();
	private List<ProjetoRubrica> listaDeRubricasProjeto = new ArrayList<>();

	private Long idOrcamento;

	private Integer stepIndex = 0;

	private List<DespesaViagem> despesas = new ArrayList<>();

	private @Inject DespesaViagem despesa;

	private List<Diaria> diarias = new ArrayList<>();

	private List<Diaria> diariasSelecionadas = new ArrayList<Diaria>();

	public List<Diaria> getDiariasSelecionadas() {
		return diariasSelecionadas;
	}

	public void setDiariasSelecionadas(List<Diaria> diariasSelecionadas) {
		this.diariasSelecionadas = diariasSelecionadas;
	}

	private @Inject Diaria diaria;
	private @Inject UsuarioSessao usuarioSessao;
	private @Inject Trecho trecho;
	private @Inject ProgamacaoViagem progViagem;

	private List<Projeto> projetos = new ArrayList<>();

	private List<Projeto> listaProjeto;

	public List<Projeto> getListaProjeto() {
		return listaProjeto;
	}

	public void setListaProjeto(List<Projeto> listaProjeto) {
		this.listaProjeto = listaProjeto;
	}

	public void getProjetoByUsuario() {
		// listaProjeto =
		// projetoService.getProjetosByUsuario(usuarioSessao.getUsuario());
		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro")) {
			listaProjeto = projetoService.getProjetosbyUsuarioProjeto(null, filtro);
			return;
		}

		listaProjeto = projetoService.getProjetosbyUsuarioProjeto(usuarioSessao.getUsuario(), filtro);

	}

	// TODO: Criar método útil para reutilização do código, adicionar menor e maior
	// número como parâmetros
	public void steping(String modo) {

		if (modo.equalsIgnoreCase("next")) {
			if (stepIndex == 4) {
				return;
			}

			stepIndex++;
			definirTab(stepIndex);
		} else {
			if (stepIndex == 0) {
				return;
			}
			stepIndex--;
			definirTab(stepIndex);
		}

	}

	public void listaRubricas() {

		if (projetoAux == null) {
			lancamentoAcao = new LancamentoAcao();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você deve selecionar um projeto antes de buscar rubrica! ");
			FacesContext.getCurrentInstance().addMessage(null, message);
			listaDeRubricasProjeto = new ArrayList<ProjetoRubrica>();
		} else {
			listaDeRubricasProjeto = calculatorRubricaRepositorio.getProjetoRubricaByProjeto(projetoAux.getId());
		}
	}

	public void listaRubricasAndAtividades() {

		if (projetoAux == null) {
			lancamentoAcao = new LancamentoAcao();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você deve selecionar um projeto antes de buscar rubrica! ");
			FacesContext.getCurrentInstance().addMessage(null, message);
			listaDeRubricasProjeto = new ArrayList<ProjetoRubrica>();
			setListaAtividades(new ArrayList<AtividadeProjeto>());
		} else {
			listaDeRubricasProjeto = calculatorRubricaRepositorio.getProjetoRubricaByProjeto(projetoAux.getId());
			// findAtividadesByProjetoWithSaldo();
		}
	}

	private List<AtividadeProjeto> listaAtividades;

	public void findAtividadesByProjeto() {
		setListaAtividades(projetoService.getAtividades(projetoAux));

	}

	public void findAtividadesByProjetoWithSaldo() {
		setListaAtividades(projetoService.getAtividadeWithSaldoByProjeto(projetoAux.getId()));
	}

	public List<AtividadeProjeto> getListaAtividades() {
		return listaAtividades;
	}

	public void setListaAtividades(List<AtividadeProjeto> listaAtividades) {
		this.listaAtividades = listaAtividades;
	}

	public ViagemCadastroController() {

	}

	public List<Gestao> completeGestao(String query) {
		return viagemService.getGestaoAutoComplete(query);
	}

	public void init() {
		if (viagem == null || viagem.getId() == null) {
			viagem = new SolicitacaoViagem();
			showTabs = false;
		} else {

			// prepararParaEdicao();
			showTabs = true;

		}
		poderEditar();
		veiculo = viagemService.getVeiculos();
		tipoViagem = viagemService.getTipoViagem();
		getProjetoByUsuario();
		PrimeFaces.current().executeScript("setarFocused('info-geral')");
	}

	public void carregarDetalhesDeRecurso() {
		if (lancamentoAcao.getProjetoRubrica().getProjeto() == null) {
			orcamentosProjetos = new ArrayList<>();
			listaDeRubricasProjeto = new ArrayList<>();
			return;
		}

		carregarOrcamentosProjeto();
		carregarRubricasDeProjetoAndOrcamento();
		// carregarRubricasDeProjeto();
	}

	public List<ProjetoRubrica> completeCategoria(String s) {
		if (s.length() > 2)
			return calculatorRubricaRepositorio.getProjetoRubricaByStrNormalizado(s);
		else
			return new ArrayList<ProjetoRubrica>();
	}

	public void prepararLancamentoAcoes() {
		projetoAux = new Projeto();
		lancamentoAcao = new LancamentoAcao();
		listaDeRubricasProjeto = new ArrayList<ProjetoRubrica>();
		// carregarProjetos();
		getProjetoByUsuario();
		// carregarLancamentoAcoes();
		calcularValorDiarias();
		calcularValorRecurso();

	}

	public void autorizar() {
		if (verificarPrivilegioAprovacao()) {
			LogViagem log = new LogViagem();
			log.setUsuario(usuarioSessao.getUsuario());
			log.setViagem(viagem);
			log.setData(new Date());
			log.setStatusLog(StatusCompra.CONCLUIDO);
			log.setSigla(viagem.getGestao().getSigla());
			log.setSiglaPrivilegio("ASV");
			viagemService.autorizar(viagem.getId(), StatusCompra.CONCLUIDO, log);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Autorizado");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você não tem privilégios para esse tipo de ação, por favor contate o administrador do sistema!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}

	public void autorizarDiaria() {
		if (verificarPrivilegioAprovacaoDiaria()) {

			if (diariasSelecionadas.isEmpty()) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Selecione Uma Diária.");
				FacesContext.getCurrentInstance().addMessage(null, message);
				return;
			} else {
				for (Diaria d : diariasSelecionadas) {
					LogStatus log = new LogStatus();
					log.setUsuario(usuarioSessao.getUsuario());
					log.setLancamento(d);
					log.setData(new Date());
					log.setStatusLog(StatusCompra.CONCLUIDO);
					log.setSigla(viagem.getGestao().getSigla());
					log.setSiglaPrivilegio("ASD");
					viagemService.autorizarDiaria(d, log);
				}
			}
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Autorizado");
			FacesContext.getCurrentInstance().addMessage(null, message);
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você não tem privilégios para esse tipo de ação, por favor contate o administrador do sistema!");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

		carregarDiarias();
	}

	public void carregarCategoriasDeProjeto() {
		if (!(lancamentoAcao == null || lancamentoAcao.getProjetoRubrica() == null)) {
			filtro.setProjetoId(lancamentoAcao.getProjetoRubrica().getProjeto().getId());
			listCategoriaProjeto = viagemService.getCategoriasDeProjeto(filtro);
		}
	}

	public boolean verificarPrivilegioAprovacao() {
		Aprouve ap = new Aprouve();
		ap.setSigla("ASV");
		ap.setUsuario(usuarioSessao.getUsuario());
		return viagemService.findAprouve(ap);
	}

	public boolean verificarPrivilegioAprovacaoDiaria() {
		Aprouve ap = new Aprouve();
		ap.setSigla("ASD");
		ap.setUsuario(usuarioSessao.getUsuario());
		return viagemService.findAprouve(ap);
	}

	public void calcularValorDiarias() {
		totalDeDiarias = BigDecimal.ZERO;

		if (diaria.getValorIntegral1() != null) {
			totalDeDiarias = totalDeDiarias
					.add(diaria.getValorIntegral1().multiply(new BigDecimal(diaria.getQuantidadeIntegral())));
		}

		if (diaria.getValorParcial1() != null) {
			totalDeDiarias = totalDeDiarias
					.add(diaria.getValorParcial1().multiply(new BigDecimal(diaria.getQuantidadeParcial())));
		}
	}

	public void calcularValorRecurso() {
		totalRecurso = BigDecimal.ZERO;
		if (diaria != null)
			for (LancamentoAcao lc : diaria.getLancamentosAcoes()) {
				totalRecurso = totalRecurso.add(lc.getValor());
			}
	}

	public void carregarProjetos() {
		projetos = viagemService.getProjetosFiltroPorUsuarioMODE01(filtro, usuarioSessao.getUsuario());
		listaProjeto = projetoService.getProjetosByUsuario(usuarioSessao.getUsuario());
	}

	public void carregarRubricasDeProjetoAndOrcamento() {
		if (lancamentoAcao.getProjetoRubrica().getProjeto() == null)
			return;
		OrcamentoProjeto op = new OrcamentoProjeto();
		if (orcamentosProjetos.size() > 0) {
			op = orcamentosProjetos.get(0);
			listaDeRubricasProjeto = new ArrayList<>();
			if (lancamentoAcao.getProjetoRubrica().getProjeto().getId() != null)
				listaDeRubricasProjeto = viagemService.getRubricasDeProjetoJOIN(
						lancamentoAcao.getProjetoRubrica().getProjeto().getId(), op.getOrcamento().getId());

		}
	}

	public void adicionarNovaAcao() {

		if (!calculatorRubricaRepositorio.verificarSaldoRubrica(lancamentoAcao.getProjetoRubrica().getId(),
				lancamentoAcao.getValor())) {
			addMessage("", "Rubrica sem saldo suficiente", FacesMessage.SEVERITY_WARN);
			return;
		}

		if (lancamentoAcao.getAtividade() != null && lancamentoAcao.getAtividade().getId() != null)
			if (!projetoService.verificaSaldoAtividade(lancamentoAcao.getAtividade().getId(),
					lancamentoAcao.getValor())) {
				addMessage("", "Atividade sem saldo suficiente", FacesMessage.SEVERITY_WARN);
				return;
			}

		if (diaria.getLancamentosAcoes() == null)
			diaria.setLancamentosAcoes(new ArrayList<>());

		for (LancamentoAcao la : diaria.getLancamentosAcoes()) {
			if (la.getProjetoRubrica().equals(lancamentoAcao.getProjetoRubrica())) {
				addMessage("", "Não é Permitido Adição de Duas Linhas Orçamentárias Iguais.",
						FacesMessage.SEVERITY_WARN);
				return;
			}
		}

		lancamentoAcao.setDespesaReceita(DespesaReceita.DESPESA);
		lancamentoAcao.setLancamento(diaria);
		// lancamentoAcao.setProjeto(lancamentoAcao.getProjetoRubrica().getProjeto());
		// lancamentoAcao.setOrcamento(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento());

		diaria.getLancamentosAcoes().add(lancamentoAcao);
		lancamentoAcao = new LancamentoAcao();
		calcularValorRecurso();
	}

	public void carregarRubricasDeProjetoAndOrcamentoSelect() {
		listaDeRubricasProjeto = new ArrayList<>();
		if (lancamentoAcao.getProjetoRubrica().getProjeto().getId() != null)
			listaDeRubricasProjeto = viagemService.getRubricasDeProjetoJOIN(
					lancamentoAcao.getProjetoRubrica().getProjeto().getId(),
					lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getId());
	}

	public void removerAcao(int index) {
		diaria.getLancamentosAcoes().remove(index);
		lancamentoAcao = new LancamentoAcao();
		calcularValorRecurso();
	}

	public void removerAcao(LancamentoAcao lancamentoAcao) {
		BigDecimal valor = lancamentoAcao.getValor();
		diaria.setValorTotalComDesconto(diaria.getValorTotalComDesconto().subtract(valor));
		diaria.getLancamentosAcoes().remove(lancamentoAcao);
		if (diaria.getId() != null && lancamentoAcao.getId() != null) {
			service.removerLancamentoAcao(lancamentoAcao);
			if (projetoAux != null && projetoAux.getId() != null) {
				listaRubricasAndAtividades();
			}
			viagemService.salvaPagamento(viagemService.salvarDiaria(diaria));
		}

	}

	public void carregarLancamentoAcoes() {
		diaria.setLancamentosAcoes(new ArrayList<>());
		if (diaria != null && diaria.getId() != null)
			diaria.setLancamentosAcoes(viagemService.buscarLancamentosAcao(diaria));
	}

	public void carregarOrcamentosProjeto() {
		if (lancamentoAcao.getProjetoRubrica().getProjeto() == null)
			return;

		orcamentosProjetos = new ArrayList<>();
		if (lancamentoAcao.getProjetoRubrica().getProjeto() != null) {
			orcamentosProjetos = viagemService
					.getOrcamentoByProjeto(lancamentoAcao.getProjetoRubrica().getProjeto().getId());
			if (orcamentosProjetos.size() > 0) {
				idOrcamento = orcamentosProjetos.get(0).getId();
			} else {
				idOrcamento = null;
			}
		}
	}

	public TipoGestao buscarTipoGestao() {
		String type = viagem.getGestao().getType();
		switch (type) {
		case "coord":
			return TipoGestao.COORD;
		case "reg":
			return TipoGestao.REGIONAL;
		default:
			return TipoGestao.SUP;
		}
	}

	public List<Colaborador> completeProfissional(String s) {
		colaboradores = new ArrayList<>();
		if (s.length() > 3)
			return viagemService.buscarColaboradores(s);
		return colaboradores;
	}

	public void salvarViagem() {
		if (viagem.getId() == null || viagem.getVersionLancamento().equals("MODE01")) {
			salvarViagemMODE01();
		} else {
			salvarViagemOLD();
		}
	}

	public void salvarViagemOLD() {

//		 if (verificarCampoViagem()) {
//		 closeDialogConfirmacaoViagem();
//		 return;
//		 }

		if (viagem.getId() == null) {
			viagem.setCodigo(gerarCodigo());
			viagem.setSolicitante(usuarioSessao.getUsuario().getColaborador());
			viagem.setDataEmissao(new Date());
		}

		viagem.setTipoGestao(buscarTipoGestao());

		viagem = viagemService.salvar(viagem, "");

		if (!showTabs) {
			showTabs = true;
		}

		addMessage("", "Salvo com sucesso!", FacesMessage.SEVERITY_INFO);
		closeDialogLoading();
		//

		// Outro usuário não pode ver o botão salvar, salvo de ser seu
		// coordenador
		// ou administrador do sistema através de pedido autorizado.

	}

	public void salvarViagemMODE01() {

		// if (verificarCampoViagem()) {
		// closeDialogConfirmacaoViagem();
		// return;
		// }

		if (viagem.getId() == null) {
			viagem.setCodigo(gerarCodigo());
			viagem.setSolicitante(usuarioSessao.getUsuario().getColaborador());
			viagem.setVersionLancamento("MODE01");
			viagem.setDataEmissao(new Date());
		}
		
		//viagem.setTipoGestao(TipoGestao.COORD);
		viagem = viagemService.salvar(viagem, "");

		if (!showTabs) {
			showTabs = true;
		}

		addMessage("", "Salvo com sucesso!", FacesMessage.SEVERITY_INFO);
		closeDialogLoading();
		//

		// Outro usuário não pode ver o botão salvar, salvo de ser seu
		// coordenador
		// ou administrador do sistema através de pedido autorizado.

	}

	public Boolean verificarCampoViagem() {

		Boolean faltaCampo = false;

		if (viagem.getMissao() == null || viagem.getMissao().equals("")) {
			faltaCampo = true;
			addMessage("", "Campo 'Missão' é obrigatório.", FacesMessage.SEVERITY_ERROR);
		}

		if (viagem.getTipoGestao() == null) {
			faltaCampo = true;
			addMessage("", "Campo 'Tipo de gestão' é obrigatório.", FacesMessage.SEVERITY_ERROR);
		}

		if (viagem.getGestao() == null) {
			faltaCampo = true;
			addMessage("", "Campo " + viagem.getTipoGestao().getNome() + " é obrigatório.",
					FacesMessage.SEVERITY_ERROR);
		}

		return faltaCampo;
	}

	public Boolean verificarCamposDespesa() {
		Boolean faltaCampo = false;
		if (despesa.getTipoDespesa() == null) {
			addMessage("", "Tipo de despesa é obrigatório.", FacesMessage.SEVERITY_ERROR);
			faltaCampo = true;
		}

		if (despesa.getCategoriaDeDespesa() == null) {
			addMessage("", "Depesa de viagem é obrigatótio.", FacesMessage.SEVERITY_ERROR);
			faltaCampo = true;
		}

		if (despesa.getUnidadeMedida() == null) {
			addMessage("", "Unidade de Medida é obrigatótio.", FacesMessage.SEVERITY_ERROR);
			faltaCampo = true;
		}

		if (despesa.getQuantidade() == 0) {
			addMessage("", "Quantidade é obrigatótio.", FacesMessage.SEVERITY_ERROR);
			faltaCampo = true;
		}

		if (despesa.getValorReal() == null) {
			addMessage("", "Valor real é obrigatótio.", FacesMessage.SEVERITY_ERROR);
			faltaCampo = true;
		}

		if (despesa.getValorPrevisto() == null) {
			addMessage("", "Valor previsto é obrigatótio.", FacesMessage.SEVERITY_ERROR);
			faltaCampo = true;
		}

		return faltaCampo;
	}

	public void addProgamacao() {
		if (verificarCamposProgramacao()) {
			closeDialogLoading();
			return;
		}
		progViagem.setSolicitacaoViagem(viagem);
		viagemService.salvarProgamacao(progViagem);
		progViagem = new ProgamacaoViagem();
		carregarProgramacao();
		closeDialogLoading();
	}

	public String buscarValorTotalRecurso() {
		NumberFormat format = NumberFormat.getCurrencyInstance();
		if (totalRecurso == null) {
			return "";
		} else {
			return format.format(totalRecurso);
		}
	}

	public StatusCompra[] getStatus() {
		return StatusCompra.values();
	}

	public String mudarStatusLancamento() {
		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")) {
			LogStatus log = new LogStatus(new Date());
			log.setLancamento(diaria);
			log.setStatusLog(diaria.getStatusCompra());
			log.setUsuario(usuarioSessao.getUsuario());

			service.mudarStatusLancamento(diaria.getId(), diaria.getStatusCompra(), log);
			carregarDiarias();
		}
		return "";
	}

	public String voltarPaginaSV() {
		return "SolicitacaoViagem?faces-redirect=true";
	}

	public void carregarDiarias() {
		diarias = viagemService.getDiariasByViagem(viagem);
	}

	public void carregarProgramacao() {
		programacoes = viagemService.getProgViagems(viagem);
	}

	public void carregarDespesas() {
		despesas = viagemService.getDespesasByViagem(viagem);
	}

	public void prepararEdicaoProgramacao() {
		progViagem = viagemService.getProgById(progViagem.getId());
		closeDialogLoading();
	}

	public void prepararEdicaoDespesas() {
		despesa = viagemService.getDespesaById(despesa.getId());
		totalReal = despesa.getTotalReal();
		TotalPrevisto = despesa.getTotalPrevisto();
		closeDialogLoading();
	}

	public void removerDespesa() {
		viagemService.removerDespesa(despesa);
		despesa = new DespesaViagem();
		carregarDespesas();
		closeDialogLoading();
	}

	public void removerProgramcao() {
		viagemService.removerProgamacao(progViagem);
		carregarProgramacao();
		closeDialogLoading();
	}

	// Uso desnecessário bastava colocar no proprio JSF a quantidade * valor no
	// value,
	// essa variável 'totalIntegral' nem deveria existir. se trata de uma
	// variável abstrata.

	public void atualizaTotalIntegral() {
		if (diaria.getValorIntegral1() != null)
			diaria.setTotalIntegral1(
					diaria.getValorIntegral1().multiply(new BigDecimal(diaria.getQuantidadeIntegral())));
		else
			diaria.setValorIntegral(new Double(0));

	}

	public List<ContaBancaria> completeContaColaborador(String query) {
		contas = new ArrayList<>();
		contas = viagemService.completeContaColaborador(query);
		return contas;
	}

	public Date getDataVolta() {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(diaria.getDataIda());

		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + diaria.getDiasnocampo());

		return calendar.getTime();
	}

	@Transactional
	public void salvarDiaria() {
		calcularValorDiarias();
		calcularValorRecurso();
		diaria.setDepesaReceita(DespesaReceita.DESPESA);
		diaria.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
		diaria.setDataPagamento(diaria.getDataVencimento());
		diaria.setQuantidadeParcela(1);
		diaria.setGestao(viagem.getGestao());
		diaria.setTipoGestao(viagem.getTipoGestao());
		diaria.setDataIda(diaria.getDataVencimento());
		diaria.setDataVolta(getDataVolta());
		diaria.setStatusCompra(StatusCompra.N_INCIADO);

		String verificador = viagemService.verificaConflitoPeriodo(diaria);

		if (!verificador.equals("")) {
			addMessage("",
					"Já existe uma diária no mesmo ou em conflito com o período informado, diária Nº: " + verificador,
					FacesMessage.SEVERITY_ERROR);
			return;
		}

		diaria.setTipoLancamento("diaria");
		if (diaria.getId() != null) {

			lancRepositorio.deletarTodosPagamentos(diaria.getId());

		}

		if (diaria.getId() == null) {
			diaria.setDataEmissao(new Date());
		}

		if (totalDeDiarias.compareTo(totalRecurso) != 0) {
			addMessage("", "Valores do recurso e valores de diárias não correspondem", FacesMessage.SEVERITY_ERROR);
			closeDialogLoading();
			return;
		}

		if (verificarCamposDiaria()) {
			closeDialogLoading();
			return;
		}

		diaria.setSolicitacaoViagem(viagem);
		diaria.setTipoDiaria(TipoDiaria.DIARIA);
		diaria.setVersionLancamento("MODE01");

		diaria.setValorIntegral(diaria.getValorIntegral1().doubleValue());
		diaria.setTotalIntegral1(diaria.getValorIntegral1().multiply(new BigDecimal(diaria.getQuantidadeIntegral())));
		diaria.setTotalIntegral(diaria.getTotalIntegral1().doubleValue());

		diaria.setValorParcial(diaria.getValorParcial1().doubleValue());
		diaria.setTotalParcial1(diaria.getValorParcial1().multiply(new BigDecimal(diaria.getQuantidadeParcial())));
		diaria.setTotalParcial(diaria.getTotalParcial1().doubleValue());

		diaria.setValorTotalComDesconto(totalDeDiarias);

		viagemService.salvaPagamento(viagemService.salvarDiaria(diaria));

		carregarDiarias();
		diaria = new Diaria();
		closeDialogLoading();
		addMessage("", "Salvo com sucesso!", FacesMessage.SEVERITY_INFO);
	}

	public Boolean verificarCamposDiaria() {
		Boolean faltaCampo = false;

		if (diaria.getTrecho() != null) {
			if (diaria.getTrecho().isEmpty()) {
				addMessage("", "Trechos de diária é obrigatório.", FacesMessage.SEVERITY_ERROR);
				// closeDialogLoading();
				faltaCampo = true;
			}
		}

		if (diaria.getLancamentosAcoes().size() < 1) {
			addMessage("",
					"Você deve adicionar ao menos 1(uma) linha Orçamentária/ação ao lançamento para que o armazenamento seja concluído.",
					FacesMessage.SEVERITY_ERROR);

			faltaCampo = true;
		}

		if (diaria.getDataVencimento() == null) {
			addMessage("", "Data de vencimento é obrigatório.", FacesMessage.SEVERITY_ERROR);
			// closeDialogLoading();
			faltaCampo = true;
		}

		if (diaria.getContaRecebedor() == null || diaria.getContaRecebedor().getNomeConta() == null) {
			addMessage("", "Profissional é obrigatório.", FacesMessage.SEVERITY_ERROR);
			// closeDialogLoading();
			faltaCampo = true;
		}

		if (diaria.getDescricao() == null || diaria.getDescricao().equals("")) {
			addMessage("", "Descrição/Motivo é obrigatório.", FacesMessage.SEVERITY_ERROR);
			// closeDialogLoading();
			faltaCampo = true;
		}

		return faltaCampo;
	}

	public Boolean verificarCamposProgramacao() {
		Boolean faltaCampo = false;
		if (progViagem.getDataIda() == null) {
			faltaCampo = true;
			addMessage("", "Data início é obrigatório.", FacesMessage.SEVERITY_ERROR);
		}

		if (progViagem.getDataVolta() == null) {
			faltaCampo = true;
			addMessage("", "Data final é obrigatório.", FacesMessage.SEVERITY_ERROR);
		}

		if (progViagem.getEquipe() == null || progViagem.getEquipe().equals("")) {
			faltaCampo = true;
			addMessage("", "Equipe é obrigatório.", FacesMessage.SEVERITY_ERROR);
		}

		if (progViagem.getAtividade() == null || progViagem.getAtividade().equals("")) {
			faltaCampo = true;
			addMessage("", "Atividade é obrigatório.", FacesMessage.SEVERITY_ERROR);
		}

		if (progViagem.getLocal() == null || progViagem.getLocal().equals("")) {
			faltaCampo = true;
			addMessage("", "Local é obrigatório.", FacesMessage.SEVERITY_ERROR);
		}

		if (progViagem.getPublico() == null || progViagem.getPublico().equals("")) {
			faltaCampo = true;
			addMessage("", "Público é obrigatório.", FacesMessage.SEVERITY_ERROR);
		}

		// if (progViagem.getObservacao() == null) {
		// faltaCampo = true;
		// addMessage("", "Observações é obrigatório.",
		// FacesMessage.SEVERITY_ERROR);
		// }

		return faltaCampo;
	}

	public void removerDiaria() {
		// diaria = viagemService.getDiariaById(diaria.getId());

		if (viagemService.removerDiaria(diaria)) {
			carregarDiarias();
			closeDialogLoading();
			diaria = new Diaria();
			addMessage("", "Excluido Com Sucesso!", FacesMessage.SEVERITY_INFO);
		} else {
			addMessage("", "Não Foi Possível Excluir, Deve Excluir Primeiro os Pagamentos!",
					FacesMessage.SEVERITY_WARN);
		}

	}

	public void limparDiaria() {
		diaria = new Diaria();
		closeDialogLoading();
	}

	public void limparDespesa() {
		despesa = new DespesaViagem();
		totalReal = 0.0;
		TotalPrevisto = 0.0;
		closeDialogLoading();
	}

	public void limparProgramacao() {
		progViagem = new ProgamacaoViagem();
		closeDialogLoading();
	}

	public void prepararEdicaoDiaria() {
		diaria = viagemService.getDiariaById(diaria.getId());
		carregarLancamentoAcoes();
		carregarTrechosDeDiaria();
		closeDialogLoading();
	}

	public void prepararEdicaoTrecho(int index) {
		indexTrecho = index;
		retirarTrecho = true;
		onLocalTrechoChangeForProject();
		onLocalTrechoDestinoChangeForProject();
	}

	public void carregarTrechosDeDiaria() {
		if (diaria.getTrecho() == null || diaria.getTrecho().isEmpty()) {
			diaria.setTrecho(new ArrayList<>());
			if (diaria.getId() != null)
				diaria.setTrecho(viagemService.getTrechoByDiaria(diaria));
		}
	}

	public void atualizaTotalParcial() {
		if (diaria.getValorParcial1() != null)
			diaria.setTotalParcial1(diaria.getValorParcial1().multiply(new BigDecimal(diaria.getQuantidadeParcial())));
		else
			diaria.setTotalParcial(new Double(0));
	}

	public String totalDiariaIntegral() {
		BigDecimal totalDiaria = BigDecimal.ZERO;
		if (diarias != null) {
			for (Diaria diaria : diarias) {
				totalDiaria = diaria.getTotalIntegral1().add(totalDiaria);
			}
		}

		return "R$" + getTotais(totalDiaria);
	}

	public String totalDiariaParcial() {
		BigDecimal totalDiaria = BigDecimal.ZERO;
		if (diarias != null) {
			for (Diaria diaria : diarias) {
				totalDiaria = diaria.getTotalParcial1().add(totalDiaria);
			}
		}

		return "R$" + getTotais(totalDiaria);
	}

	public void gerarRelatorio() {

	}

	public void gerarRelatorioViagem() {

		HashMap<String, Object> params = new HashMap<String, Object>();

		viagem = viagemService.getViagemById(viagem.getId());

		List<Diaria> diarias = viagemService.getDiariasByViagem(viagem);
		StringBuilder acs = new StringBuilder("");

		for (Diaria diaria : diarias) {
			List<LancamentoAcao> acoes = viagemService.buscarLancamentosAcao(diaria);
			for (LancamentoAcao lancamentoAcao : acoes) {
				acs.append(lancamentoAcao.getProjetoRubrica().getProjeto().getNome());
				acs.append("//");
				acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getTitulo());
				acs.append("//");
				acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getRubrica().getNome());
				acs.append("\n");
			}
		}

		ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String caminho = context.getRealPath("resources");

		String relatorio = "relatorio" + File.separatorChar + "SolicitacaoViagem.jrxml";

		SimpleDateFormat formatas = new SimpleDateFormat("dd/MM/yyyy");
		String dataSolicitacao = formatas.format(viagem.getDataEmissao());
		if (viagem.getDataAprovacao() != null) {
			String dataAprovacao = formatas.format(viagem.getDataAprovacao());
			params.put("dataAprovacao", dataAprovacao);
		}
		try {

			params.put("acao", acs.toString());
			params.put("listaDiarias", viagemService.getDiariasByViagem(viagem));
			params.put("listaProgramacao", viagemService.getProgViagems(viagem));
			params.put("listaDespesas", viagemService.getDespesasByViagemFromTipo(viagem, TipoDespesa.DESPESAS));
			params.put("listaProvimentos", viagemService.getDespesasByViagemFromTipo(viagem, TipoDespesa.PROVIMENTOS));
			params.put("listaPassagens", viagemService.getDespesasByViagemFromTipo(viagem, TipoDespesa.PASSAGENS));
			params.put("listaCustosEconomizados",
					viagemService.getDespesasByViagemFromTipo(viagem, TipoDespesa.CUSTOS_ECONOMIZADOS));
			params.put("imagemLogo", caminho + File.separatorChar + "image" + File.separatorChar + "logofas.png");
			params.put("impresso", usuarioSessao.getNomeUsuario());
			Double totalGeral = new Double(0);
			Double totalDiarias = new Double(0);
			Double totalDespesas = new Double(0);
			Double totalProvimentos = new Double(0);
			Double totalPassagens = new Double(0);
			Double totalCustosEco = new Double(0);

			for (Diaria d : viagemService.getDiariasByViagem(viagem)) {
				totalDiarias = totalDiarias.sum(d.getTotalIntegral(), totalDiarias);
				totalDiarias = totalDiarias.sum(d.getTotalParcial(), totalDiarias);
			}

			// for (Diaria d : viagemService.getDiariasByViagem(viagem)) {
			//
			// }

			for (DespesaViagem d : viagemService.getDespesasByViagemFromTipo(viagem, TipoDespesa.DESPESAS)) {
				totalDespesas = totalDespesas.sum(totalDespesas, d.getTotalReal());
			}
			for (DespesaViagem d : viagemService.getDespesasByViagemFromTipo(viagem, TipoDespesa.PROVIMENTOS)) {
				totalProvimentos = totalProvimentos.sum(totalProvimentos, d.getTotalReal());
			}
			for (DespesaViagem d : viagemService.getDespesasByViagemFromTipo(viagem, TipoDespesa.PASSAGENS)) {
				totalPassagens = totalPassagens.sum(totalPassagens, d.getTotalReal());
			}
			for (DespesaViagem d : viagemService.getDespesasByViagemFromTipo(viagem, TipoDespesa.CUSTOS_ECONOMIZADOS)) {
				totalCustosEco = totalCustosEco.sum(totalCustosEco, d.getTotalReal());
			}

			totalGeral = totalGeral.sum(totalDiarias, totalGeral);
			totalGeral = totalGeral.sum(totalGeral, totalDespesas);
			totalGeral = totalGeral.sum(totalGeral, totalProvimentos);
			totalGeral = totalGeral.sum(totalGeral, totalPassagens);

			String primeira = "0%";
			String segunda = "0%";
			String terceira = "0%";
			String quarta = "0%";
			String quinta = "0%";
			String sexta = "0%";

			params.put("primeira", primeira);
			params.put("segunda", segunda);
			params.put("terceira", terceira);
			params.put("quarta", quarta);
			params.put("quinta", quinta);
			params.put("sexta", sexta);

			params.put("totalDiarias", totalDiarias);
			params.put("totalDespesas", totalDespesas);
			params.put("totalProvimentos", totalProvimentos);
			params.put("totalPassagens", totalPassagens);
			params.put("totalCustosEco", totalCustosEco);
			params.put("totalGeral", totalGeral);

			params.put("dataSolicitacao", dataSolicitacao);

			params.put("caminhoJasper1",
					caminho + File.separatorChar + "relatorio" + File.separatorChar + "SolViagemDiarias.jasper");
			params.put("caminhoJasper2",
					caminho + File.separatorChar + "relatorio" + File.separatorChar + "SolViagemProgramacao.jasper");
			params.put("caminhoJasper3",
					caminho + File.separatorChar + "relatorio" + File.separatorChar + "SolViagemDespesas.jasper");
			params.put("caminhoJasper4",
					caminho + File.separatorChar + "relatorio" + File.separatorChar + "SolViagemDespesas.jasper");
			params.put("caminhoJasper5",
					caminho + File.separatorChar + "relatorio" + File.separatorChar + "SolViagemDespesas.jasper");
			params.put("caminhoJasper6",
					caminho + File.separatorChar + "relatorio" + File.separatorChar + "SolViagemDespesas.jasper");

			List<SolicitacaoViagem> listaViagem = new ArrayList<SolicitacaoViagem>();
			listaViagem.add(viagem);
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaViagem);

			JasperReport jasperReport = JasperCompileManager.compileReport(caminho + File.separatorChar + relatorio);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds);

			byte[] b = JasperExportManager.exportReportToPdf(jasperPrint);

			FacesContext fc = FacesContext.getCurrentInstance();
			HttpServletResponse res = (HttpServletResponse) fc.getExternalContext().getResponse();
			res.setContentType("application/pdf");
			res.setHeader("Content-disposition", "inline;filename=Análise Crítica.pdf");
			res.getOutputStream().write(b);
			res.getOutputStream().flush();
			res.getOutputStream().close();
			fc.responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void geraRelatorioDiaria() {

		HashMap<String, Object> params = new HashMap<String, Object>();

		ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String caminho = context.getRealPath("resources");

		StringBuilder acs = new StringBuilder("");
		diaria = viagemService.getDiariaById(diaria.getId());

		List<LancamentoAcao> acoes = viagemService.buscarLancamentosAcao(diaria);
		for (LancamentoAcao lancamentoAcao : acoes) {
			acs.append(lancamentoAcao.getProjetoRubrica().getComponente().getNome());
			acs.append(" | ");
			acs.append(lancamentoAcao.getProjetoRubrica().getProjeto().getNome());
			acs.append(" | ");
			acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getTitulo());
			acs.append(" | ");
			acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getRubrica().getNome());
			acs.append(" | ");
			acs.append("R$: " + lancamentoAcao.getValor());
			acs.append("\n");
		}

		diaria.setSolicitacaoViagem(viagemService.getViagemById(viagem.getId()));

		String relatorio = "relatorio" + File.separatorChar + "diaria_viagem.jrxml";
		try {
			int dias = 0;
			SimpleDateFormat formatas = new SimpleDateFormat("dd/MM/yyyy");
			String dataSolicitacao = formatas.format(diaria.getSolicitacaoViagem().getDataEmissao());
			String dataAprovacao = null;
			if (diaria.getSolicitacaoViagem().getDataAprovacao() != null)
				dataAprovacao = formatas.format(diaria.getSolicitacaoViagem().getDataAprovacao());
			if (diaria.getSolicitacaoViagem().getDataIda() != null
					&& diaria.getSolicitacaoViagem().getDataVolta() != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Calendar dts = GregorianCalendar.getInstance();
				dts.setTime(diaria.getSolicitacaoViagem().getDataVolta());// data
																			// retirada
																			// -
																			// maior
				Calendar dte = GregorianCalendar.getInstance();
				dte.setTime(diaria.getSolicitacaoViagem().getDataIda());// data
																		// locação
																		// -
																		// menor

				int novo = dte.get(Calendar.DAY_OF_YEAR);
				int velho = dts.get(Calendar.DAY_OF_YEAR);
				dias = (velho - novo) + 1;

				if (diaria.getDiasnocampo() != null && diaria.getDiasnocampo() != 0) {
					dias = diaria.getDiasnocampo();
				}

			}

			BigDecimal suprimento = BigDecimal.ZERO;

			StringBuilder paramConta = new StringBuilder();

			paramConta.append(Util.getNullValue(diaria.getContaRecebedor().getNomeBanco(), ""));
			paramConta.append("/");
			paramConta.append(Util.getNullValue(diaria.getContaRecebedor().getNumeroAgencia(), ""));
			paramConta.append("/");
			paramConta.append(Util.getNullValue(diaria.getContaRecebedor().getNumeroConta(), ""));

			params.put("acao", acs.toString());
			params.put("diasCampo", dias);
			params.put("conta", paramConta.toString());
			params.put("caminho",
					caminho + File.separatorChar + "relatorio" + File.separatorChar + "SubTrechoDiaria.jasper");
			params.put("listaDiarias", viagemService.getTrechoByDiaria(diaria));
			Double totalDespedas = new Double(0);
			totalDespedas = totalDespedas.sum(diaria.getTotalIntegral(), diaria.getTotalParcial())
					+ suprimento.doubleValue();
			params.put("totalDespesas", totalDespedas);
			params.put("dataAprovacao", dataAprovacao);
			params.put("dataSolicitacao", dataSolicitacao);
			params.put("imagemLogo", caminho + File.separatorChar + "image" + File.separatorChar + "logofas.png");
			params.put("usuario", usuarioSessao.getNomeUsuario());
			params.put("sv", diaria.getSolicitacaoViagem().getId().toString());
			params.put("hospedagem", diaria.getHospedagem());

			if (diaria.getSolicitacaoViagem().getTipoGestao().getNome().equals("Regional")) {
				Colaborador c = new Colaborador();
				c.setNome("Valcleia Solidade");
				diaria.getSolicitacaoViagem().setAprovador(c);
				diaria.getSolicitacaoViagem()
						.setSolicitante(diaria.getSolicitacaoViagem().getGestao().getColaborador());
				// diaria.setSolicitante(diaria.getSolicitacaoViagem().getGestao().getColaborador());
			} else {
				diaria.getSolicitacaoViagem().setAprovador(diaria.getSolicitacaoViagem().getGestao().getColaborador());
			}

			List<Diaria> listaDiaria = new ArrayList<Diaria>();
			listaDiaria.add(diaria);
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaDiaria);

			JasperReport jasperReport = JasperCompileManager.compileReport(caminho + File.separatorChar + relatorio);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds);

			byte[] b = JasperExportManager.exportReportToPdf(jasperPrint);

			FacesContext fc = FacesContext.getCurrentInstance();
			HttpServletResponse res = (HttpServletResponse) fc.getExternalContext().getResponse();
			res.setContentType("application/pdf");
			res.setHeader("Content-disposition", "inline;filename=Análise Crítica.pdf");
			res.getOutputStream().write(b);
			res.getOutputStream().flush();
			res.getOutputStream().close();
			fc.responseComplete();
			diaria = new Diaria();
			closeDialog("PF('dlg_impressao').hide();");
		} catch (Exception e) {
			closeDialog("PF('dlg_impressao').hide();");
			diaria = new Diaria();
			e.printStackTrace();
		}

		diaria = new Diaria();
	}

	public String getTotais(BigDecimal total) {
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(total);
		// return new DecimalFormat("###,###.###").format(total);
	}

	public BigDecimal convertBigdecimal(Double valor) {
		return new BigDecimal(valor);
	}

	public TipoDiaria[] getTipoDiaria() {
		return TipoDiaria.values();
	}

	public void closeDialogLoading() {
		PrimeFaces.current().executeScript("PF('statusDialog').hide();");
	}

	public void closeDialogConfirmacaoViagem() {
		PrimeFaces.current().executeScript("PF('dlg_escolha_page').hide();");
	}

	public void closeDialog(String comand) {
		PrimeFaces.current().executeScript(comand);
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void definirTab(Integer index) {

		Integer indexAux = index;

		switch (indexAux) {
		case 0:
			tab = "Informações gerais";
			break;
		case 1:
			tab = "Diárias";
			carregarDiarias();
			PrimeFaces.current().ajax().update("cadastro_diaria");
			break;
		case 2:
			tab = "Programação";
			carregarProgramacao();
			break;
		case 4:
			tab = "Despesas";
			carregarDespesas();
			carregarCategorias();
			break;
		default:
//			tab = "Despesas";
//			carregarDespesas();
//			carregarCategorias();
			break;

		}

	}

	public String gerarCodigo() {

		StringBuilder s = new StringBuilder("SV");

		Calendar calendar = Calendar.getInstance();
		int ano = calendar.get(Calendar.YEAR);
		int mes = calendar.get(Calendar.MONTH) + 1;
		int dia = calendar.get(Calendar.DAY_OF_MONTH);
		int minuto = calendar.get(Calendar.MINUTE);
		int segundo = calendar.get(Calendar.SECOND);
		int milisegundo = calendar.get(Calendar.MILLISECOND);
		
		s.append(String.valueOf(ano).substring(2));
		s.append(String.valueOf(mes));
		s.append(String.valueOf(dia));
		s.append(String.valueOf(minuto));
		s.append(String.valueOf(segundo));
		// s.append(String.valueOf(milisegundo).substring(0, 2));

		return s.toString();
	}

	public void prepararParaEdicao() {
		tipoGestao = viagem.getTipoGestao().getNome();
		local = viagem.getTipoLocalidade().getNome();

		if (viagem.getLocalidade() instanceof Municipio) {
			estados = viagemService.getEstados(new Filtro());
			idEstado = ((Municipio) viagem.getLocalidade()).getEstado().getId();
			localidades = viagemService.getMunicipioByEstado(idEstado);
		} else {
			LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
			localidades = viagem.getTipoLocalidade().getLocalidade(repo);
		}

		GestaoRepositorio repo = CDILocator.getBean(GestaoRepositorio.class);
		gestoes = new ArrayList<>();
		gestoes = viagem.getTipoGestao().getGestao(repo);

	}

	public void onTipoGestaoChange() {
		tipoGestao = "";
		if (viagem.getTipoGestao() != null) {
			tipoGestao = viagem.getTipoGestao().getNome();
			GestaoRepositorio repo = CDILocator.getBean(GestaoRepositorio.class);
			gestoes = new ArrayList<>();
			gestoes = viagem.getTipoGestao().getGestao(repo);
		}
	}

	public void onLocalChangerForProject() {
		local = "";
		if (viagem.getTipoLocalidade() != null) {
			local = viagem.getTipoLocalidade().getNome();
			if (local.equals("Municipio")) {
				estados = new ArrayList<>();
				estados = viagemService.getEstados(new Filtro());
				localidades = new ArrayList<>();
				idEstado = new Long(0);
			} else {
				LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
				localidades = viagem.getTipoLocalidade().getLocalidade(repo);

			}
		}
	}

	private String localTrecho = "";
	private List<Localidade> localidadesTrechoOrigem = new ArrayList<Localidade>();

	public void onLocalTrechoChangeForProject() {
		localTrecho = "";
		if (trecho.getTipoLocalidadeOrigem() != null) {
			localTrecho = trecho.getTipoLocalidadeOrigem().getNome();
			if (localTrecho.equals("Municipio")) {
				estados = new ArrayList<>();
				estados = viagemService.getEstados(new Filtro());
				localidadesTrechoOrigem = new ArrayList<>();
				idEstado = new Long(0);
			} else {
				LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
				localidadesTrechoOrigem = trecho.getTipoLocalidadeOrigem().getLocalidade(repo);

			}
		}
	}

	private String localTrechoDestino = "";
	private List<Localidade> localidadesTrechoDestino = new ArrayList<Localidade>();

	public void onLocalTrechoDestinoChangeForProject() {
		localTrechoDestino = "";
		if (trecho.getTipoLocalidadeDestino() != null) {
			localTrechoDestino = trecho.getTipoLocalidadeDestino().getNome();
			if (localTrechoDestino.equals("Municipio")) {
				estados = new ArrayList<>();
				estados = viagemService.getEstados(new Filtro());
				localidadesTrechoDestino = new ArrayList<>();
				idEstado = new Long(0);
			} else {
				LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
				localidadesTrechoDestino = trecho.getTipoLocalidadeDestino().getLocalidade(repo);

			}
		}
	}

	public void addDespesa() {
		if (verificarCamposDespesa()) {
			closeDialogLoading();
			return;
		}

		despesa.setValorPrevisto(despesa.getValorReal());
		despesa.setTotalPrevisto(despesa.getTotalReal());

		despesa.setSolicitacaoViagem(viagem);
		viagemService.salvarDespesa(despesa);
		despesa = new DespesaViagem();
		carregarDespesas();
		closeDialogLoading();
	}

	public TipoDespesa[] getTipoDespesa() {
		return TipoDespesa.values();
	}

	public AtividadeDespesa[] getAtividadeDespesa() {
		return AtividadeDespesa.values();
	}

	public UnidadeMedida[] getUnidadeMedida() {
		return UnidadeMedida.values();
	}

	public void atualizaTotal() {

		if (despesa.getValorReal() != null) {

			int valorReal = despesa.getQuantidade();
			double qtd = despesa.getValorReal();

			totalReal = ((double) valorReal) * qtd;
			despesa.setTotalReal(totalReal);
		} else {

			totalReal = (double) 0;

		}

	}

	private Double TotalPrevisto;

	public void atualizaTotalPrevisto() {

		if (despesa.getValorPrevisto() != null) {

			int valorReal = despesa.getQuantidade();
			double qtd = despesa.getValorPrevisto();

			TotalPrevisto = ((double) valorReal) * qtd;
			despesa.setTotalReal(totalReal);
			despesa.setTotalPrevisto(TotalPrevisto);

		} else {

			TotalPrevisto = (double) 0;

		}

	}

	// Usando o mesmo código com vários nomes????? Já pensou nisso!
	public void onEstadoChangeTrechoOrigem() {
		localidadesTrechoOrigem = viagemService.getMunicipioByEstado(idEstado);
		idEstado = new Long(0);
	}

	public void onEstadoChange() {
		localidades = viagemService.getMunicipioByEstado(idEstado);
		idEstado = new Long(0);
	}

	public void onEstadoChangeTrechoDestino() {
		localidadesTrechoDestino = viagemService.getMunicipioByEstado(idEstado);
		idEstado = new Long(0);
	}

	public void addTrecho() {
		if (diaria.getTrecho() == null)
			diaria.setTrecho(new ArrayList<>());

		trecho.setDiaria(diaria);
		trecho.setTipoLocalidadeOrigem(buscarTipoLocalidade(trecho.getOrigem()));
		trecho.setTipoLocalidadeDestino(buscarTipoLocalidade(trecho.getDestino()));

		if (retirarTrecho)
			diaria.getTrecho().remove(indexTrecho);

		diaria.getTrecho().add(trecho);
		trecho = new Trecho();

		retirarTrecho = false;
		localTrecho = "";
		localTrechoDestino = "";

	}

	public void limpaValoresDiaria() {
		if (diaria.getHospedagem()) {
			diaria.setQuantidadeIntegral(0);
			diaria.setValorIntegral1(new BigDecimal("0"));
			diaria.setTotalIntegral1(new BigDecimal("0"));

		}
	}

	public TipoLocalidade buscarTipoLocalidade(Localidade localidade) {

		String type = localidade.getType();
		switch (type) {
		case "mun":
			return TipoLocalidade.MUNICIPIO;
		case "uc":
			return TipoLocalidade.UC;
		case "com":
			return TipoLocalidade.COMUNIDADE;
		case "sede":
			return TipoLocalidade.SEDE;
		default:
			return TipoLocalidade.NUCLEO;
		}
	}

	public CategoriaDespesaClass getCategoria() {
		CategoriaDespesaClass c = new CategoriaDespesaClass();
		c.setId(new Long(34));
		c.setNome("Categoria de despesa");
		return c;
	}

	public List<Localidade> completeLocalidade(String s) {
		if (s.length() > 2)
			return viagemService.buscaLocalidade(s);

		return new ArrayList<Localidade>();
	}

	private List<CategoriaDespesaClass> categorias = new ArrayList<>();

	public void carregarCategorias() {
		categorias = viagemService.buscarCategorias();
	}

	public void removerTrecho(int index) {
		diaria.getTrecho().remove(index);
		trecho = new Trecho();
		closeDialogLoading();
	}

	public List<Fornecedor> completeFornecedor(String query) {
		List<Fornecedor> fornecedores = new ArrayList<Fornecedor>();
		if (query.length() > 2)
			fornecedores = viagemService.getFornecedores(query);
		return fornecedores;
	}

	// Alterado dia 11/07 by chris
	private Boolean podeEditar;

	public Boolean getPodeEditar() {
		return podeEditar;
	}

	public void setPodeEditar(Boolean podeEditar) {
		this.podeEditar = podeEditar;
	}

	public void poderEditar() {
		if (viagem.getId() != null) {

			SolicitacaoViagem viagem = viagemService.getViagemById(this.viagem.getId());

			if (viagem.getStatusViagem() == null || viagem.getStatusViagem().getNome().equals("Não iniciado/Solicitado")
					|| viagem.getStatusViagem().getNome().equals("Em cotação")
					|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")) {
				podeEditar = true;
			} else {
				Long idUser = usuarioSessao.getUsuario().getColaborador().getId().longValue();
				Long idSolicintante = viagem.getSolicitante().getId().longValue();

				Long idGestor = viagem.getGestao().getColaborador().getId();

				if (idUser.longValue() == idSolicintante.longValue() || idUser.longValue() == idGestor.longValue()) {
					podeEditar = true;
				} else {
					podeEditar = false;
				}

			}

		} else {
			podeEditar = true;
		}
	}

	//
	public void limparTrecho() {
		trecho = new Trecho();
	}

	public List<Veiculo> getTransporte() {
		return veiculo;
	}

	public Destino[] getDestino() {
		return Destino.values();
	}

	public Componente[] getComponentes() {
		return Componente.values();
	}

	public TipoLocalidade[] getTipoLocais() {
		return TipoLocalidade.values();
	}

	public TipoGestao[] getTiposGestao() {
		return TipoGestao.values();
	}

	public SolicitacaoViagem getViagem() {
		return viagem;
	}

	public void setViagem(SolicitacaoViagem viagem) {
		this.viagem = viagem;
	}

	public void verificaViagem() {
//		
	}

	public String getTipoGestao() {
		return tipoGestao;
	}

	public void setTipoGestao(String tipoGestao) {
		this.tipoGestao = tipoGestao;
	}

	public List<Gestao> getGestoes() {
		return gestoes;
	}

	public void setGestoes(List<Gestao> gestoes) {
		this.gestoes = gestoes;
	}

	public Long getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Long idEstado) {
		this.idEstado = idEstado;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public List<Localidade> getLocalidades() {
		return localidades;
	}

	public void setLocalidades(List<Localidade> localidades) {
		this.localidades = localidades;
	}

	public List<TipoViagem> getTipoViagem() {
		return tipoViagem;
	}

	public void setTipoViagem(List<TipoViagem> tipoViagem) {
		this.tipoViagem = tipoViagem;
	}

	public TabView getTabview() {
		return tabview;
	}

	public void setTabview(TabView tabview) {
		this.tabview = tabview;
	}

	public boolean isShowTabs() {
		return showTabs;
	}

	public void setShowTabs(boolean showTabs) {
		this.showTabs = showTabs;
	}

	public List<Veiculo> getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(List<Veiculo> veiculo) {
		this.veiculo = veiculo;
	}

	public String getTab() {
		return tab;
	}

	public void setTab(String tab) {
		this.tab = tab;
	}

	public List<Diaria> getDiarias() {
		return diarias;
	}

	public void setDiarias(List<Diaria> diarias) {
		this.diarias = diarias;
	}

	public Diaria getDiaria() {
		return diaria;
	}

	public void setDiaria(Diaria diaria) {
		this.diaria = diaria;
	}

	public Trecho getTrecho() {
		return trecho;
	}

	public void setTrecho(Trecho trecho) {
		this.trecho = trecho;
	}

	public String getLocalTrecho() {
		return localTrecho;
	}

	public void setLocalTrecho(String localTrecho) {
		this.localTrecho = localTrecho;
	}

	public List<Localidade> getLocalidadesTrechoOrigem() {
		return localidadesTrechoOrigem;
	}

	public void setLocalidadesTrechoOrigem(List<Localidade> localidadesTrechoOrigem) {
		this.localidadesTrechoOrigem = localidadesTrechoOrigem;
	}

	public String getLocalTrechoDestino() {
		return localTrechoDestino;
	}

	public void setLocalTrechoDestino(String localTrechoDestino) {
		this.localTrechoDestino = localTrechoDestino;
	}

	public List<Localidade> getLocalidadesTrechoDestino() {
		return localidadesTrechoDestino;
	}

	public void setLocalidadesTrechoDestino(List<Localidade> localidadesTrechoDestino) {
		this.localidadesTrechoDestino = localidadesTrechoDestino;
	}

	public ProgamacaoViagem getProgViagem() {
		return progViagem;
	}

	public void setProgViagem(ProgamacaoViagem progViagem) {
		this.progViagem = progViagem;
	}

	public List<ProgamacaoViagem> getProgramacoes() {
		return programacoes;
	}

	public void setProgramacoes(List<ProgamacaoViagem> programacoes) {
		this.programacoes = programacoes;
	}

	public DespesaViagem getDespesa() {
		return despesa;
	}

	public void setDespesa(DespesaViagem despesa) {
		this.despesa = despesa;
	}

	public Double getTotalReal() {
		return totalReal;
	}

	public void setTotalReal(Double totalReal) {
		this.totalReal = totalReal;
	}

	public Double getTotalPrevisto() {
		return TotalPrevisto;
	}

	public void setTotalPrevisto(Double totalPrevisto) {
		TotalPrevisto = totalPrevisto;
	}

	public List<DespesaViagem> getDespesas() {
		return despesas;
	}

	public void setDespesas(List<DespesaViagem> despesas) {
		this.despesas = despesas;
	}

	public List<CategoriaDespesaClass> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<CategoriaDespesaClass> categorias) {
		this.categorias = categorias;
	}

	public LancamentoAcao getLancamentoAcao() {
		return lancamentoAcao;
	}

	public void setLancamentoAcao(LancamentoAcao lancamentoAcao) {
		this.lancamentoAcao = lancamentoAcao;
	}

	public List<OrcamentoProjeto> getOrcamentosProjetos() {
		return orcamentosProjetos;
	}

	public void setOrcamentosProjetos(List<OrcamentoProjeto> orcamentosProjetos) {
		this.orcamentosProjetos = orcamentosProjetos;
	}

	public List<ProjetoRubrica> getListaDeRubricasProjeto() {
		return listaDeRubricasProjeto;
	}

	public void setListaDeRubricasProjeto(List<ProjetoRubrica> listaDeRubricasProjeto) {
		this.listaDeRubricasProjeto = listaDeRubricasProjeto;
	}

	public Long getIdOrcamento() {
		return idOrcamento;
	}

	public void setIdOrcamento(Long idOrcamento) {
		this.idOrcamento = idOrcamento;
	}

	public List<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public BigDecimal getTotalDeDiarias() {
		return totalDeDiarias;
	}

	public void setTotalDeDiarias(BigDecimal totalDeDiarias) {
		this.totalDeDiarias = totalDeDiarias;
	}

	public BigDecimal getTotalRecurso() {
		return totalRecurso;
	}

	public void setTotalRecurso(BigDecimal totalRecurso) {
		this.totalRecurso = totalRecurso;
	}

	public List<CategoriaProjeto> getListCategoriaProjeto() {
		return listCategoriaProjeto;
	}

	public void setListCategoriaProjeto(List<CategoriaProjeto> listCategoriaProjeto) {
		this.listCategoriaProjeto = listCategoriaProjeto;
	}

	public UsuarioSessao getUsuarioSessao() {
		return usuarioSessao;
	}

	public void setUsuarioSessao(UsuarioSessao usuarioSessao) {
		this.usuarioSessao = usuarioSessao;
	}

	public Integer getStepIndex() {
		return stepIndex;
	}

	public void setStepIndex(Integer stepIndex) {
		this.stepIndex = stepIndex;
	}

}
