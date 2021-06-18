package managedbean;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
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

import model.Acao;
import model.AtividadeDespesa;
import model.Colaborador;
import model.Componente;
import model.DespesaViagem;
import model.Destino;
import model.Diaria;
import model.Estado;
import model.FontePagadora;
import model.Fornecedor;
import model.Gestao;
import model.LancamentoAcao;
import model.Localidade;
import model.MenuLateral;
import model.Municipio;
import model.ProgamacaoViagem;
import model.SolicitacaoViagem;
import model.StatusCompra;
import model.TipoDespesa;
import model.TipoDiaria;
import model.TipoGestao;
import model.TipoLocalidade;
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
import repositorio.GestaoRepositorio;
import repositorio.LocalRepositorio;
import service.SolicitacaoViagemService;
import util.CDILocator;
import util.Filtro;
import util.MakeMenu;
import util.UsuarioSessao;

@Named(value = "viagemController")
@ViewScoped
public class ViagemController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private @Inject SolicitacaoViagemService viagemService;

	private @Inject SolicitacaoViagem viagem;

	private @Inject UsuarioSessao usuarioSessao;

	private @Inject LancamentoAcao lancamentoAcao;

	private MakeMenu hybrid = new MakeMenu();

	// private List<MenuLateral> utilMenu = hybrid.getMenuFinanceiro();

	private List<MenuLateral> menus = new ArrayList<>();

	private MenuLateral menu = new MenuLateral();

	private Filtro filtro = new Filtro();

	private TabView tabview;

	private Long idEstado;

	private String local;

	private String localTrecho;

	private String localTrechoDestino;

	private String tipoGestao = "";

	private Boolean panelListagem = true;

	private Boolean panelCadastro = false;

	private Boolean panelProgListagem = true;

	private Boolean panelProgCadastro = false;

	private Boolean panelDespesaListagem = true;

	private Boolean panelDespesaCadastro = false;

	private Boolean panelDiariaListagem = true;

	private Boolean panelDiariaCadastro = false;

	private List<Gestao> gestoes;

	private List<FontePagadora> allFontes = new ArrayList<FontePagadora>();

	private List<Acao> allAcoes = new ArrayList<Acao>();

	private List<TipoViagem> tipoViagem = new ArrayList<TipoViagem>();

	private List<TipoDespesa> tipoDespesa = new ArrayList<TipoDespesa>();

	private List<Veiculo> veiculo = new ArrayList<Veiculo>();

	private List<Localidade> localidades = new ArrayList<Localidade>();

	private List<Localidade> localidadesTrechoOrigem = new ArrayList<Localidade>();

	private List<Localidade> localidadesTrechoDestino = new ArrayList<Localidade>();

	private List<SolicitacaoViagem> viagemsFiltered = new ArrayList<SolicitacaoViagem>();

	private List<Estado> estados;

	private List<Colaborador> colaborador = new ArrayList<Colaborador>();

	public List<Colaborador> getColaborador() {
		return colaborador;
	}

	public void setColaborador(List<Colaborador> colaborador) {
		this.colaborador = colaborador;
	}

	private List<SolicitacaoViagem> viagems = new ArrayList<SolicitacaoViagem>();

	/* Teste de programacao de viagem */
	private List<ProgamacaoViagem> progViagems = new ArrayList<ProgamacaoViagem>();

	private @Inject ProgamacaoViagem progViagem;

	private List<DespesaViagem> despesas = new ArrayList<DespesaViagem>();

	private @Inject DespesaViagem despesa;

	private List<Diaria> diarias = new ArrayList<Diaria>();

	private @Inject Diaria diaria;

	private List<Trecho> trechos = new ArrayList<Trecho>();

	private @Inject Trecho trecho;

	private Double totalReal;

	private Double TotalPrevisto;

	private double totalIntegral = 0;

	private double totalParcial = 0;

	private List<Fornecedor> fornecedores = new ArrayList<Fornecedor>();
	
	public  ViagemController() {
		
	}
	
	@PostConstruct
	public void init() {
		//tipoViagem = viagemService.getTipoViagem();
		//veiculo = viagemService.getVeiculos();
		carregarViagens();
		//colaborador = viagemService.getColaborador();
		//fornecedores = viagemService.getFornecedores();
	}
	
	
	public boolean poderEditar() {
		if (viagem.getId() != null) {
			if (viagem.getStatusViagem()== null ||viagem.getStatusViagem().getNome().equals("Não iniciado/Solicitado")||viagem.getStatusViagem().getNome().equals("Em cotação")||  usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")) {
					return true;
				} else {
					Long idUser = usuarioSessao.getUsuario().getColaborador().getId().longValue();
					Long idSolicintante = viagem.getSolicitante().getId().longValue();

					Long idGestor = viagem.getGestao().getColaborador().getId();

					if (idUser.longValue() == idSolicintante.longValue()||idUser.longValue() == idGestor.longValue()) {
						return true;
					} else {
						return false;
					}

				}

			} else {
				return true;
			}
	}
	
	
	public void carregarViagens(){
		viagems = viagemService.getSolicitacaoViagem(filtro);
	}
	
	public void atualizaTotalIntegral() {

		if (diaria.getValorIntegral() != null) {
			Double qtd = new Double(diaria.getQuantidadeIntegral());
			diaria.setTotalIntegral(diaria.getValorIntegral() * qtd);

		} else {

			diaria.setValorIntegral(new Double(0));

		}
	}
	
	public void dialogHelp() {
		HashMap<String, Object> options = new HashMap<>();
		options.put("width", "100%");
		options.put("height", "100%");
		options.put("contentWidth", "100%");
		options.put("contentHeight", "100%");
		options.put("resizable", false);
		options.put("minimizable",true);
		options.put("maximizable",true);
		PrimeFaces.current().dialog().openDynamic("dialog/help/help_solicitacao_viagem", options, null);
	}

	public void atualizaTotalParcial() {

		if (diaria.getValorParcial() != null) {
			Double qtd = new Double(diaria.getQuantidadeParcial());
			diaria.setTotalParcial(diaria.getValorParcial() * qtd);
		} else {

			diaria.setTotalParcial(new Double(0));

		}
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

	public double totalDespesa() {
		double total = 0;
		/*
		 * if(viagem.getDespesaViagem() != null){ for (DespesaViagem despesa:
		 * viagem.getDespesaViagem()){ total += despesa.getTotalReal(); } }
		 */

		if (despesas != null) {
			for (DespesaViagem despesa : despesas) {
				total += despesa.getTotalReal();
			}
		}

		return total;
	}

	public double totalDiaria() {

		double totalDiaria = 0;

		if (diarias != null) {

			for (Diaria diaria : diarias) {
				totalDiaria += diaria.getValorIntegral();
			}
		}

		return totalDiaria;

	}
	
	
	public List<MenuLateral> getMenus() {
		if (menus.isEmpty())
			menus = MakeMenu.getMenuFinanceiro();
		return menus;
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

	public void remover(){
		viagemService.remover(viagem);
		carregarViagens();
	}

	public void inciarFiltro() {
		filtro = new Filtro();
		local = "";
		tipoGestao = "";
	}

	public List<Fornecedor> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public void supender() {

	}

	public void cancelar() {

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
		/*
		 * System.out.println("Ano: "+ano); System.out.println("M�s: "+mes);
		 * System.out.println("Dia: "+dia); System.out.println("Minuto: "
		 * +minuto); System.out.println("Segundo: "+segundo);
		 * System.out.println("Milisegundo: "+milisegundo);
		 */
		s.append(String.valueOf(ano).substring(2));
		s.append(String.valueOf(mes));
		s.append(String.valueOf(dia));
		s.append(String.valueOf(minuto));
		s.append(String.valueOf(segundo));
		// s.append(String.valueOf(milisegundo).substring(0, 2));
		// System.out.println(s.toString());
		return s.toString();
	}

	public void limpar() {
		tipoGestao = "";
		local = "";
		viagem = new SolicitacaoViagem();
	}

	public String aprovar() {
		viagem.setAprovador(usuarioSessao.getUsuario().getColaborador());
		viagem.setDataAprovacao(new Date());
		viagem.setStatusViagem(StatusCompra.APROVADO);
		viagemService.salvar(viagem);
		return "SolicitacaoViagem?faces-redirect=true";
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

	public void onTipoGestaoChangeFiltro() {
		tipoGestao = "";
		if (filtro.getTipoGestao() != null) {
			tipoGestao = filtro.getTipoGestao().getNome();
			GestaoRepositorio repo = CDILocator.getBean(GestaoRepositorio.class);
			gestoes = new ArrayList<>();
			gestoes = filtro.getTipoGestao().getGestao(repo);

		}
	}

	public void inciarNovaViagem() {
		panelCadastro = true;
		panelListagem = false;
		limpar();
	}

	public void inciarNovaProgamacaoViagem() {
		progViagem = new ProgamacaoViagem();
		panelProgCadastro = true;
		panelProgListagem = false;

	}

	public void iniciarNovaDespesa() {
		despesa = new DespesaViagem();
		totalReal = new Double(0);
		TotalPrevisto = new Double(0);
		panelDespesaListagem = false;
		panelDespesaCadastro = true;
	}

	public void iniciarNovaDiaria() {
		panelDiariaListagem = false;
		panelDiariaCadastro = true;
	}

	public void voltarProgListagem() {
		panelProgCadastro = false;
		panelProgListagem = true;
	}

	public void voltarDiariaListagem() {
		panelDiariaCadastro = false;
		panelDiariaListagem = true;
	}

	public void voltarDespListagem() {

		panelDespesaCadastro = false;
		panelDespesaListagem = true;

	}

	public void addProgamacao() {

		if (viagem.getId() == null) {

			addMessage("", "Primeiro Finalize a Solicitacao de Viagem", FacesMessage.SEVERITY_ERROR);

		} else {

			/*
			 * viagem.setProgamacaoViagem(viagemService.setProgViagems(viagem));
			 * progViagem.setSolicitacaoViagem(viagem);
			 * viagemService.salvarProgamacao(progViagem);
			 * 
			 * viagem.getProgamacaoViagem().add(progViagem);
			 * viagemService.salvar(viagem);
			 * 
			 * progViagem = new ProgamacaoViagem();
			 * 
			 */
			progViagem.setSolicitacaoViagem(viagem);
			if (progViagem.getSolicitacaoViagem() == null) {
				viagem.getProgamacaoViagem().add(viagemService.salvarProgamacao(progViagem));
			} else {
				viagemService.salvarProgamacao(progViagem);
			}

			panelProgCadastro = false;
			panelProgListagem = true;

			progViagems = viagemService.getProgViagems(viagem);

			progViagem = new ProgamacaoViagem();

		}

	}

	public void addDiaria() {
		if (viagem.getId() == null) {
			addMessage("", "Primeiro Finalize a Solicitacao de Viagem", FacesMessage.SEVERITY_ERROR);
		} else {

			diaria.setSolicitacaoViagem(viagem);
			if (diaria.getId() == null) {
				viagem.getDiaria().add(viagemService.salvarDiaria(diaria));
			} else {
				viagemService.salvarDiaria(diaria);
			}

			panelDiariaCadastro = false;
			panelDiariaListagem = true;
			trecho = new Trecho();
			trechos = new ArrayList<Trecho>();
			totalIntegral = 0;
			totalParcial = 0;
			diaria = new Diaria();

			diarias = getViagemService().getDiariasByViagem(viagem);
		}
	}

	public void addTrecho() {

		// diaria.setTrecho(viagemService.setTrechos(diaria));

		trecho.setDiaria(diaria);
		// viagemService.salvarTrecho(trecho);
		diaria.getTrecho().add(trecho);
		// viagemService.salvarDiaria(diaria);
		trecho = new Trecho();

	}

	public void addDespesa() {

		if (viagem.getId() == null) {
			addMessage("", "Primeiro Finalize a Solicitacao de Viagem", FacesMessage.SEVERITY_ERROR);
		} else {

			/*
			 * viagem.setDespesaViagem(viagemService.setDespesas(viagem));
			 * despesa.setSolicitacaoViagem(viagem);
			 * viagemService.salvarDespesa(despesa);
			 * 
			 * viagem.getDespesaViagem().add(despesa);
			 * viagemService.salvar(viagem);
			 * 
			 * despesa = new DespesaViagem();
			 */

			despesa.setSolicitacaoViagem(viagem);
			if (despesa.getId() != null) {
				viagem.getDespesaViagem().add(viagemService.salvarDespesa(despesa));

			} else {
				viagemService.salvarDespesa(despesa);
			}

			despesa = new DespesaViagem();

			despesas = viagemService.getDespesasByViagem(viagem);

			panelDespesaCadastro = false;
			panelDespesaListagem = true;

		}
	}

	public void editarViagem() {

		viagemsFiltered = new ArrayList<>();

		panelCadastro = true;
		panelListagem = false;

		viagem = viagemService.getViagemById(viagem.getId());

		tipoGestao = viagem.getGestao().getNome();

		local = viagem.getLocalidade().getNome();

		progViagems = viagemService.getProgViagems(viagem);

		despesas = viagemService.getDespesasByViagem(viagem);

		diarias = viagemService.getDiariasByViagem(viagem);

		if (viagem.getLocalidade() instanceof Municipio) {
			estados = viagemService.getEstados(new Filtro());
			idEstado = ((Municipio) viagem.getLocalidade()).getEstado().getId();
			localidades = viagemService.getMunicipioByEstado(idEstado);
		} else {
			GestaoRepositorio repo = CDILocator.getBean(GestaoRepositorio.class);
			gestoes = new ArrayList<>();
			gestoes = viagem.getTipoGestao().getGestao(repo);
		}

	}

	public void excluirProg(ProgamacaoViagem prog) {
		progViagems.remove(prog);
		viagemService.excluirProg(prog);

	}

	public void editarProgamacao() {
		panelProgCadastro = true;
		panelProgListagem = false;

		progViagem = viagemService.getProgById(progViagem.getId());
	}

	public void editarDespesa() {
		panelDespesaCadastro = true;
		panelDespesaListagem = false;
		atualizaTotal();
		atualizaTotalPrevisto();
		despesa = viagemService.getDespesaById(despesa.getId());
	}

	public void editarDiaria() {
		panelDiariaCadastro = true;
		panelDiariaListagem = false;

		diaria = viagemService.getDiariaById(diaria.getId());
	}

	public void definirTabDespesa() {
		TipoDespesa tipo = null;

		int index = 0;

		if (tabview != null)
			index = tabview.getActiveIndex();

		if (index == 0) {

			despesas = viagemService.getDespesasByViagem(viagem);

		} else if (index == 1) {

			tipo = tipo.DESPESAS;
			despesas = viagemService.getDespesasByViagemFromTipo(viagem, tipo);

		} else if (index == 2) {

			tipo = tipo.PROVIMENTOS;
			despesas = viagemService.getDespesasByViagemFromTipo(viagem, tipo);

		} else if (index == 3) {

			tipo = tipo.PASSAGENS;
			despesas = viagemService.getDespesasByViagemFromTipo(viagem, tipo);

		} else if (index == 4) {
			tipo = tipo.CUSTOS_ECONOMIZADOS;
			despesas = viagemService.getDespesasByViagemFromTipo(viagem, tipo);

		} else if (index == 5) {
			tipo = tipo.PASSAGENS;
			despesas = viagemService.getDespesasByViagemFromTipo(viagem, tipo);

		}

	}

	public void iniciarNovoLancamento() {
		lancamentoAcao = new LancamentoAcao();
	}

	public void voltarListagem() {
		panelCadastro = false;
		panelListagem = true;
		viagems = viagemService.getSolicitacaoViagem(filtro);
	}

	public String salvarViagem() {
		if (viagem.getId() == null) {
			viagem.setCodigo(gerarCodigo());
		}

		viagem.setSolicitante(usuarioSessao.getUsuario().getColaborador());
		viagem.setDataEmissao(new Date());

		if (viagemService.salvar(viagem)) {
			limpar();
			panelCadastro = false;
			panelListagem = true;
			viagems = viagemService.getSolicitacaoViagem(filtro);

		}
		return "";
	}

	// ---------------------------------------------------- gerar relatorio

	public void geraRelatorioDiaria(Diaria diaria) {
		HashMap<String, Object> params = new HashMap<String, Object>();

		ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String caminho = context.getRealPath("resources");

		String relatorio = "relatorio" + File.separatorChar + "SolicitacaoDiariaViagem.jrxml";
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
				dias = (velho - novo);

			}
			params.put("diasCampo", dias);
			params.put("conta", usuarioSessao.getUsuario().getColaborador().getConta());
			params.put("caminho",
					caminho + File.separatorChar + "relatorio" + File.separatorChar + "SubTrechoDiaria.jasper");
			params.put("listaDiarias", viagemService.getTrechoByDiaria(diaria));
			Double totalDespedas = new Double(0);
			totalDespedas = totalDespedas.sum(diaria.getTotalIntegral(), diaria.getTotalParcial());
			params.put("totalDespesas", totalDespedas);
			params.put("dataAprovacao", dataAprovacao);
			params.put("dataSolicitacao", dataSolicitacao);
			params.put("imagemLogo", caminho + File.separatorChar + "image" + File.separatorChar + "logofas.png");
			params.put("usuario", usuarioSessao.getNomeUsuario());

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public List<Gestao> completeGestao(String query) {
		return viagemService.getGestaoAutoComplete(query);
	}

	@SuppressWarnings("static-access")
	public void gerarRelatorioViagem(SolicitacaoViagem viagem) {

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
			
//			for (Diaria d : viagemService.getDiariasByViagem(viagem)) {
//				
//			}

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

	// ---------------------------------------------------------------------

	public void deletarViagem() {
	}

	public void adicionarItem() {
	}

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
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
	
	public List<Localidade> completeLocalidade(String s) {
		if (s.length() > 2)
			return viagemService.buscaLocalidade(s);

		return new ArrayList<Localidade>();
	}
	
	public void onLocalTrechoDestinoChangeForProject() {
		localTrechoDestino = "";
		if (trecho.getTipoLocalidadeOrigem() != null) {
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

	public void onLocalChangeFilter() {
		local = "";
		if (filtro.getTipoLocalidade() != null) {
			local = filtro.getTipoLocalidade().getNome();
			if (local.equals("Municipio")) {
				estados = new ArrayList<>();
				estados = viagemService.getEstados(new Filtro());
				localidades = new ArrayList<>();
				idEstado = new Long(0);
			} else {
				LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
				localidades = filtro.getTipoLocalidade().getLocalidade(repo);
			}
		}
	}

	public void filtrar() {
		carregarViagens();
	}
	
	public void limparFiltro(){
	  filtro = new Filtro();
	}

	public void onEstadoChange() {
		localidades = viagemService.getMunicipioByEstado(idEstado);
		idEstado = new Long(0);
	}

	public void onEstadoChangeTrechoOrigem() {
		localidadesTrechoOrigem = viagemService.getMunicipioByEstado(idEstado);
		idEstado = new Long(0);
	}

	public void onEstadoChangeTrechoDestino() {
		localidadesTrechoDestino = viagemService.getMunicipioByEstado(idEstado);
		idEstado = new Long(0);
	}

	public SolicitacaoViagemService getViagemService() {
		return viagemService;
	}

	public void setViagemService(SolicitacaoViagemService viagemService) {
		this.viagemService = viagemService;
	}

	public SolicitacaoViagem getViagem() {
		return viagem;
	}

	public void setViagem(SolicitacaoViagem viagem) {
		this.viagem = viagem;
	}

	public UsuarioSessao getUsuarioSessao() {
		return usuarioSessao;
	}

	public void setUsuarioSessao(UsuarioSessao usuarioSessao) {
		this.usuarioSessao = usuarioSessao;
	}

	public LancamentoAcao getLancamentoAcao() {
		return lancamentoAcao;
	}

	public void setLancamentoAcao(LancamentoAcao lancamentoAcao) {
		this.lancamentoAcao = lancamentoAcao;
	}

	public MakeMenu getHybrid() {
		return hybrid;
	}

	public void setHybrid(MakeMenu hybrid) {
		this.hybrid = hybrid;
	}

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
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

	public String getTipoGestao() {
		return tipoGestao;
	}

	public TipoGestao[] getTiposGestao() {
		return TipoGestao.values();
	}

	public void setTipoGestao(String tipoGestao) {
		this.tipoGestao = tipoGestao;
	}

	public Boolean getPanelListagem() {
		return panelListagem;
	}

	public void setPanelListagem(Boolean panelListagem) {
		this.panelListagem = panelListagem;
	}

	public Boolean getPanelCadastro() {
		return panelCadastro;
	}

	public void setPanelCadastro(Boolean panelCadastro) {
		this.panelCadastro = panelCadastro;
	}

	public List<Gestao> getGestoes() {
		return gestoes;
	}

	public void setGestoes(List<Gestao> gestoes) {
		this.gestoes = gestoes;
	}

	public StatusCompra[] getStatusCompras() {
		return StatusCompra.values();
	}

	public Destino[] getDestino() {
		return Destino.values();
	}

	public List<FontePagadora> getAllFontes() {
		return allFontes;
	}

	public void setAllFontes(List<FontePagadora> allFontes) {
		this.allFontes = allFontes;
	}

	public List<Acao> getAllAcoes() {
		return allAcoes;
	}

	public void setAllAcoes(List<Acao> allAcoes) {
		this.allAcoes = allAcoes;
	}

	public List<Localidade> getLocalidades() {
		return localidades;
	}

	public void setLocalidades(List<Localidade> localidades) {
		this.localidades = localidades;
	}

	public List<SolicitacaoViagem> getViagemsFiltered() {
		return viagemsFiltered;
	}

	public void setViagemsFiltered(List<SolicitacaoViagem> viagemsFiltered) {
		this.viagemsFiltered = viagemsFiltered;
	}

	public TipoLocalidade[] getTipoLocais() {
		return TipoLocalidade.values();
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public List<SolicitacaoViagem> getViagems() {
		return viagems;
	}

	public void setViagems(List<SolicitacaoViagem> viagems) {
		this.viagems = viagems;
	}

	public List<ProgamacaoViagem> getProgViagems() {
		return progViagems;
	}

	public void setProgViagems(List<ProgamacaoViagem> progViagems) {
		this.progViagems = progViagems;
	}

	public ProgamacaoViagem getProgViagem() {
		return progViagem;
	}

	public void setProgViagem(ProgamacaoViagem progViagem) {
		this.progViagem = progViagem;
	}

	public Componente[] getComponentes() {
		return Componente.values();
	}

	public Boolean getPanelDespesaListagem() {
		return panelDespesaListagem;
	}

	public void setPanelDespesaListagem(Boolean panelDespesaListagem) {
		this.panelDespesaListagem = panelDespesaListagem;
	}

	public Boolean getPanelDespesaCadastro() {
		return panelDespesaCadastro;
	}

	public void setPanelDespesaCadastro(Boolean panelDespesaCadastro) {
		this.panelDespesaCadastro = panelDespesaCadastro;
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Boolean getPanelProgListagem() {
		return panelProgListagem;
	}

	public void setPanelProgListagem(Boolean panelProgListagem) {
		this.panelProgListagem = panelProgListagem;
	}

	public Boolean getPanelProgCadastro() {
		return panelProgCadastro;
	}

	public void setPanelProgCadastro(Boolean panelProgCadastro) {
		this.panelProgCadastro = panelProgCadastro;
	}

	public List<TipoViagem> getTipoViagem() {
		return tipoViagem;
	}

	public List<Veiculo> getTransporte() {
		return veiculo;
	}

	public List<DespesaViagem> getDespesas() {
		return despesas;
	}

	public void setDespesas(List<DespesaViagem> despesas) {
		this.despesas = despesas;
	}

	public DespesaViagem getDespesa() {
		return despesa;
	}

	public void setDespesa(DespesaViagem despesa) {
		this.despesa = despesa;
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

	public TipoDiaria[] getTipoDiaria() {
		return TipoDiaria.values();
	}

	public TabView getTabview() {
		return tabview;
	}

	public void setTabview(TabView tabview) {
		this.tabview = tabview;
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

	public List<Trecho> getTrechos() {
		return trechos;
	}

	public void setTrechos(List<Trecho> trechos) {
		this.trechos = trechos;
	}

	public Trecho getTrecho() {
		return trecho;
	}

	public void setTrecho(Trecho trecho) {
		this.trecho = trecho;
	}

	public Boolean getPanelDiariaListagem() {
		return panelDiariaListagem;
	}

	public void setPanelDiariaListagem(Boolean panelDiariaListagem) {
		this.panelDiariaListagem = panelDiariaListagem;
	}

	public Boolean getPanelDiariaCadastro() {
		return panelDiariaCadastro;
	}

	public void setPanelDiariaCadastro(Boolean panelDiariaCadastro) {
		this.panelDiariaCadastro = panelDiariaCadastro;
	}

	public String getLocalTrecho() {
		return localTrecho;
	}

	public void setLocalTrecho(String localTrecho) {
		this.localTrecho = localTrecho;
	}

	public String getLocalTrechoDestino() {
		return localTrechoDestino;
	}

	public void setLocalTrechoDestino(String localTrechoDestino) {
		this.localTrechoDestino = localTrechoDestino;
	}

	public List<Localidade> getLocalidadesTrechoOrigem() {
		return localidadesTrechoOrigem;
	}

	public void setLocalidadesTrechoOrigem(List<Localidade> localidadesTrechoOrigem) {
		this.localidadesTrechoOrigem = localidadesTrechoOrigem;
	}

	public List<Localidade> getLocalidadesTrechoDestino() {
		return localidadesTrechoDestino;
	}

	public void setLocalidadesTrechoDestino(List<Localidade> localidadesTrechoDestino) {
		this.localidadesTrechoDestino = localidadesTrechoDestino;
	}

	public double getTotalIntegral() {
		return totalIntegral;
	}

	public void setTotalIntegral(double totalIntegral) {
		this.totalIntegral = totalIntegral;
	}

	public double getTotalParcial() {
		return totalParcial;
	}

	public void setTotalParcial(double totalParcial) {
		this.totalParcial = totalParcial;
	}
}
