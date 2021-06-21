package managedbean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.component.tabview.TabView;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import model.CadeiaProdutiva;
import model.ComponenteClass;
import model.Gestao;
import model.MetaPlano;
import model.PlanoDeTrabalho;
import model.Programa;
import model.Projeto;
import model.SubComponente;
import model.TipoAdministrativoProjeto;
import model.TipoGestao;
import model.TipoLocalidade;
import model.UnidadeConservacao;
import model.User;
import model.UserPlanoTrabalho;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import service.PlanoDeTrabalhoService;
import service.ProjetoService;
import util.Filtro;
import util.ReportUtil;
import util.UsuarioSessao;
import util.Util;

@Named(value = "plano_trabalho_controller")
@ViewScoped
public class PlanoTrabalhoController implements Serializable {

	private static final long serialVersionUID = 1L;

	private TabView tabviewPlano;

	@Inject
	private PlanoDeTrabalho planoDeTrabalho;
	
	private List<SubComponente> subComponentes = new ArrayList<>();
	private List<ComponenteClass> componentesClass = new ArrayList<>();
	private List<CadeiaProdutiva> cadeias;

	@Inject
	private PlanoDeTrabalhoService service;

	private BigDecimal totalRepasse;
	private BigDecimal totalExecucaoDireta;
	private BigDecimal totalDoPlano;
	private Projeto projeto;

	private BarChartModel cadeiasModel = new BarChartModel();
	private BarChartModel localidadeModel = new BarChartModel();
	
	private List<UserPlanoTrabalho> autorizacoes = new ArrayList<UserPlanoTrabalho>();
	private @Inject ProjetoService gestaoProjeto;// = new GestaoProjeto();
	

	@Inject
	private UsuarioSessao usuarioSessao;
	
	@Inject
	private UserPlanoTrabalho userPlano;
	
	private List<PlanoDeTrabalho> filter;

	private List<PlanoDeTrabalho> planos = new ArrayList<>();
	private List<Projeto> listProjetos = new ArrayList<>();
	
	private List<User> usuarios = new  ArrayList<>();

	private Filtro filtro = new Filtro();

	public void initListagem() {
		carregarPlanos();
	}
	
	public void carregarSubComponentes() {
		subComponentes = new ArrayList<>();
		if (projeto.getComponente() != null)
			subComponentes = gestaoProjeto.getSubComponentes(projeto.getComponente().getId());
	}
	
	public void carregarCadeias() {
		cadeias = new ArrayList<CadeiaProdutiva>();
		cadeias = gestaoProjeto.getCadeias();
	}
	
	public void carregarComponentes() {
		componentesClass = gestaoProjeto.getComponentes();
	}
	
	
	public List<PlanoDeTrabalho> getFilter(){
		return filter;
	}
	
	public void setFilter(List<PlanoDeTrabalho> filter) {
		this.filter = filter;
	}
	
	public void initCadastro() {
		carregarProjetos();
		carregarValoresPlano();
		carregarChart();
	}
	
	public void iniciarNovoProjeto() {
		projeto = new Projeto();
	
		projeto.setPlanoDeTrabalho(planoDeTrabalho);
		projeto.setGestao(planoDeTrabalho.getGestao());
		projeto.setDataInicio(planoDeTrabalho.getDataInicio());
		projeto.setDataFinal(planoDeTrabalho.getDataFinal());
		carregarComponentes();
		carregarSubComponentes();
		carregarCadeias();
	}
	
	public void salvarProjeto() {
		
		projeto.setGestao(planoDeTrabalho.getGestao());
		projeto.setDataInicio(planoDeTrabalho.getDataInicio());
		projeto.setDataFinal(planoDeTrabalho.getDataFinal());
		setarTipoGestaoInProjeto();
		setarTipoLocalidadeInProjeto();
		projeto.setVersionProjeto("mode01");
		projeto = gestaoProjeto.salvarInPlanoMODE01(projeto, usuarioSessao.getUsuario());
		carregarProjetos();	
		// messageSalvamento("Projeto salvo com sucesso");
		// carregarProjetos();
	}
		
	
	public void salvarUsuarioNoPlano() {
		if (service.salvarUsuarioNoPlano(userPlano)) {
			userPlano = new UserPlanoTrabalho();
			buscarAutorizacoes();
		}
	}
	
	public void removerProjeto() {
		gestaoProjeto.remover(projeto);
		carregarProjetos();
	}
	
	public void excluirUsuarioDoPlano() {
		service.excluirUsuarioDoPlano(userPlano);
		buscarAutorizacoes();
	}

	
	public void buscarAutorizacoes() {
		autorizacoes = service.getUsuariosProjetos(planoDeTrabalho);
		userPlano.setPlano(planoDeTrabalho);
	}


	public List<User> completeUsuario(String query) {
		usuarios = new ArrayList<User>();
		usuarios = service.getUsuario(query);
		return usuarios;
	}
	
	public TipoAdministrativoProjeto[] getTiposAdministrativos() {
		return TipoAdministrativoProjeto.values();
	}
	
	
	public void carregarChart(){
		cadeiasModel = new BarChartModel();
		ChartSeries investimento = new ChartSeries();
		investimento.setLabel("Investimentos");
		investimento.set("UND", 1);
		cadeiasModel.addSeries(investimento);
		
		localidadeModel = new BarChartModel();
		ChartSeries investimento2 = new ChartSeries();
		investimento2.setLabel("Investimentos");
		investimento2.set("UND", 1);
		localidadeModel.addSeries(investimento2);		
		
	}

	public String salvar() {
		setarTipoGestaoInPlano();
		setarTipoLocalidadeInPlano();
		service.salvar(planoDeTrabalho, usuarioSessao.getUsuario());
		return "planos?faces-redirect=true";
	}

	public void remover() {
		service.remover(planoDeTrabalho);
	}

	public void carregarPlanos() {
		planos = service.getPLanos(filtro, usuarioSessao.getUsuario());
	}

	public void setarTipoGestaoInPlano() {
		String ttype = planoDeTrabalho.getGestao().getType();
		switch (ttype) {
		case "coord":
			planoDeTrabalho.setTipoGestao(TipoGestao.COORD);
			break;
		case "reg":
			planoDeTrabalho.setTipoGestao(TipoGestao.REGIONAL);
			break;
		default:
			planoDeTrabalho.setTipoGestao(TipoGestao.SUP);
			break;
		}
	}

	// FIM SALVAMENTO

	public void carregarInformacoesPlano() {
		if (planoDeTrabalho.getUc() != null) {

		}
	}

	public void carregarValoresPlano() {

		if (planoDeTrabalho.getId() == null)
			return;

		totalRepasse = BigDecimal.ZERO;
		totalExecucaoDireta = BigDecimal.ZERO;
		totalDoPlano = BigDecimal.ZERO;

		for (Projeto projeto : listProjetos) {
			if (!projeto.getRepasse().equals("")) {
				if (projeto.getRepasse().equals("ED")) {
					totalExecucaoDireta = totalExecucaoDireta.add(projeto.getValor());
				} else {
					totalRepasse = totalRepasse.add(projeto.getValor());
				}
			}
			totalDoPlano = totalDoPlano.add(projeto.getValor());
		}

	}

	public void definirTabPlano() {
		int index = 0;
		if (tabviewPlano != null)
			index = tabviewPlano.getActiveIndex();
		if (index == 0) {
			carregarProjetos();
			carregarValoresPlano();
		} else if (index == 1) {
			criarChart();
			//createBarModel();
		} else if (index == 2) {

		}

	}
	
	private BarChartModel initBarModelCadeia() {
		BarChartModel model = new BarChartModel();
 
        ChartSeries boys = new ChartSeries();
        boys.setLabel("Boys");
        boys.set("2004", 120);
        boys.set("2005", 100);
        boys.set("2006", 44);
        boys.set("2007", 150);
        boys.set("2008", 25);
 
        ChartSeries girls = new ChartSeries();
        girls.setLabel("Girls");
        girls.set("2004", 52);
        girls.set("2005", 60);
        girls.set("2006", 110);
        girls.set("2007", 135);
        girls.set("2008", 120);
 
        model.addSeries(boys);
        model.addSeries(girls);
         
        return model;
    }
	
	private void createBarModel() {
        cadeiasModel = initBarModelCadeia();
         
        cadeiasModel.setTitle("Bar Chart");
        cadeiasModel.setLegendPosition("ne");
         
        Axis xAxis = cadeiasModel.getAxis(AxisType.X);
        xAxis.setLabel("Gender");
         
        Axis yAxis = cadeiasModel.getAxis(AxisType.Y);
        yAxis.setLabel("Births");
        yAxis.setMin(0);
        yAxis.setMax(200);
    }

	public void criarChart() {
		criarChartCadeia();
		criarChartLocalidade();
	}
	
	
	public void criarChartLocalidade() {

		BarChartModel model = new BarChartModel();
		ChartSeries investimento = new ChartSeries();
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal maiorValor = BigDecimal.ZERO;
		Map<String, BigDecimal> mapaDeValores = new HashMap<>();
        Map<String, ChartSeries> mapaDeSeries = new HashMap<>();
		for (Projeto projeto : listProjetos) {
			
			total = mapaDeValores.get(projeto.getNomeLocalidade()) != null ? mapaDeValores.get(projeto.getNomeLocalidade()) : BigDecimal.ZERO;
			
			mapaDeValores.put(projeto.getNomeLocalidade(), total.add(projeto.getValor()));
			
			investimento = mapaDeSeries.get(projeto.getNomeLocalidade()) == null ? null : mapaDeSeries.get(projeto.getNomeLocalidade()); 
			
			if (investimento == null) {
				investimento = new ChartSeries();
				investimento.setLabel(projeto.getNomeLocalidade());
				investimento.set(projeto.getNomeLocalidade(), mapaDeValores.get(projeto.getNomeLocalidade()));
				mapaDeSeries.put(projeto.getNomeLocalidade(), investimento);
				model.addSeries(investimento);
			}else{
				investimento.set(projeto.getNomeLocalidade(), mapaDeValores.get(projeto.getNomeLocalidade()));
			}
		
			investimento = new ChartSeries();
			
			if (total.compareTo(maiorValor) > 0) {
				maiorValor = total;
			}
			
		}
		
		
		
		localidadeModel = model;
		localidadeModel.setAnimate(true);
		localidadeModel.setTitle("Cadeias produtivas");
		localidadeModel.setLegendPosition("e");
		localidadeModel.setBarPadding(15);
		localidadeModel.setBarMargin(400);
		localidadeModel.setMouseoverHighlight(true);
		localidadeModel.setShowPointLabels(true);
		
		

		Axis xAxis = localidadeModel.getAxis(AxisType.X);
		xAxis.setLabel("Cadeias");
		xAxis.setTickFormat("%.2f");
		
		Axis yAxis = localidadeModel.getAxis(AxisType.Y);
		yAxis.setLabel("Valores R$");
		yAxis.setMin(-1);
		yAxis.setMax(maiorValor.intValue() + 900000);
		yAxis.setTickFormat("%.2f");

		
	}

	public void criarChartCadeia() {

		BarChartModel model = new BarChartModel();
		ChartSeries investimento = new ChartSeries();
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal maiorValor = BigDecimal.ZERO;
		Map<String, BigDecimal> mapaDeValores = new HashMap<>();
        Map<String, ChartSeries> mapaDeSeries = new HashMap<>();
		for (Projeto projeto : listProjetos) {
			
			total = mapaDeValores.get(projeto.getNomeCadeia()) != null ? mapaDeValores.get(projeto.getNomeCadeia()) : BigDecimal.ZERO;
			
			mapaDeValores.put(projeto.getNomeCadeia(), total.add(projeto.getValor()));
			
			investimento = mapaDeSeries.get(projeto.getNomeCadeia()) == null ? null : mapaDeSeries.get(projeto.getNomeCadeia()); 
			
			if (investimento == null) {
				investimento = new ChartSeries();
				investimento.setLabel(projeto.getNomeCadeia());
				investimento.set(projeto.getNomeCadeia(), mapaDeValores.get(projeto.getNomeCadeia()));
				mapaDeSeries.put(projeto.getNomeCadeia(), investimento);
				model.addSeries(investimento);
			}else{
				investimento.set(projeto.getNomeCadeia(), mapaDeValores.get(projeto.getNomeCadeia()));
			}
		
			investimento = new ChartSeries();
			
			if (total.compareTo(maiorValor) > 0) {
				maiorValor = total;
			}
			
		}
		
		
		
		cadeiasModel = model;
		cadeiasModel.setAnimate(true);
		cadeiasModel.setTitle("Cadeias produtivas");
		cadeiasModel.setLegendPosition("e");
		cadeiasModel.setBarPadding(15);
		cadeiasModel.setBarMargin(100);
		cadeiasModel.setMouseoverHighlight(true);
		cadeiasModel.setShowPointLabels(true);
		
		

		Axis xAxis = cadeiasModel.getAxis(AxisType.X);
		xAxis.setLabel("Cadeias");
		xAxis.setTickFormat("%.2f");
		
		Axis yAxis = cadeiasModel.getAxis(AxisType.Y);
		yAxis.setLabel("Valores R$");
		yAxis.setMin(-1);
		yAxis.setMax(maiorValor.intValue() + 900000);
		yAxis.setTickFormat("%.2f");
		
	}

	public List<UnidadeConservacao> completeUC(String s) {
		if (s.length() > 2)
			return service.buscarUC(s);

		return new ArrayList<UnidadeConservacao>();
	}

	public List<Gestao> completeGestao(String query) {
		return service.getGestaoAutoComplete(query);
	}

	public void imprimirPlano() {

		double total = 0;
		double totalRepasse = 0;
		double totalExecucaoDireta = 0;

		double totalFasRepasse = 0;
		double totalOutraRepasse = 0;
		double totalFasExecucao = 0;
		double totalOutraExecucao = 0;

		planoDeTrabalho = service.findPlanoById(planoDeTrabalho.getId());
		listProjetos = service.getProjetoByPlano(planoDeTrabalho.getId());

		for (Projeto projeto : listProjetos) {
			if(projeto.getNomeComponente().equals("1.RENDA")) {
				projeto.setNomeComponente("1");
			}else {
				projeto.setNomeComponente("2");
			}
			double valor = Double.parseDouble(projeto.getValor().toString());
			total += valor;
			projeto.setNomeFonte("Fundo Amazônia");

			if (projeto.getRepasse().equalsIgnoreCase("RR")) {
				totalRepasse += valor;
			} else {
				totalExecucaoDireta += valor;
			}
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		String logo = path + "resources/image/logoFas.gif";

		Map hp = new HashMap();

		UnidadeConservacao uc = service.findUcById(planoDeTrabalho.getUc().getId());

		hp.put("NOME_INSTITUICAO", Util.getNullValue(uc.getNomeAssociacao(), "ñ cadastrado"));
		hp.put("CNPJ", Util.getNullValue(uc.getCnpjassociacao(), "ñ cadastrado"));
		hp.put("ENDERECO", Util.getNullValue(uc.getEnderecoAssociacao(), "ñ cadastrado"));
		hp.put("UF", "AM");
		hp.put("CEP", Util.getNullValue(uc.getCepAssociacao(), "ñ cadastrado"));
		hp.put("TELEFONE", Util.getNullValue(uc.getTelefoneAssociacao(), "ñ cadastrado"));
		hp.put("BANCO", Util.getNullValue(uc.getBancoAssociacao(), "ñ cadastrado"));
		hp.put("AGENCIA", Util.getNullValue(uc.getAgenciaAssociacao(), "ñ cadastrado"));
		hp.put("CONTA_CORR", Util.getNullValue(uc.getContaAssociacao(), "ñ cadastrado"));
		hp.put("CARGO", Util.getNullValue(uc.getCargoPresidente(), "ñ cadastrado"));
		hp.put("RG", Util.getNullValue(uc.getRgPresidente(), "ñ cadastrado"));
		hp.put("CPF", Util.getNullValue(uc.getCpfPresidente(), "ñ cadastrado"));
		hp.put("END_RESID", Util.getNullValue(uc.getEnderecoPresidente(), "ñ cadastrado"));
		hp.put("CIDADE_RESP", Util.getNullValue(uc.getCidadePresidente(), "Ñ cadastrado"));
		hp.put("CIDADE", Util.getNullValue(uc.getCidadeAssociacao(), "Ñ cadastrado"));
		hp.put("UF_RESP", "AM");
		hp.put("CEP_RESP", Util.getNullValue(uc.getCepAssociacao(), "ñ cadastrado"));
		hp.put("TELEFONE_RESP", Util.getNullValue(uc.getTelefonePresidente(), "ñ cadastrado"));
		hp.put("ID_PLANO", Util.getNullValue(planoDeTrabalho.getTitulo(), "ñ cadastrado"));
		hp.put("CONTEXT", Util.getNullValue(planoDeTrabalho.getContextualizacao(), "ñ cadastrado"));
		hp.put("OBJ_GERAL", Util.getNullValue(planoDeTrabalho.getObjetivo(), "ñ cadastrado"));
		hp.put("OBJ_ESPECIFICO", Util.getNullValue(planoDeTrabalho.getObjetivoEspecifico(), "ñ cadastrado"));
		hp.put("NOME_RESPONSAVEL", Util.getNullValue(uc.getPresidenteAssociacao(), "ñ cadastrado"));

		// hp.put("CIDADE", Utils.nullValue(a.getCidade(), "Ñ cadastrado"));

		// hp.put("EXTRATEGY_EX", Utils.nullValue(plane.getExtrategiaExec(), "Ñ
		// cadastrado"));
		//
		// hp.put("COORDENADOR", Utils.nullValue(plane.getCoordenador(), "Ñ
		// cadastrado"));

		hp.put("TOTAL_PLANO", Util.decimal(total));
		hp.put("TOTAL_REPASSE", Util.decimal(totalRepasse));
		hp.put("TOTAL_EXECUCAO_DIRETA", Util.decimal(totalExecucaoDireta));

		JRDataSource dataSource = new JRBeanCollectionDataSource(listProjetos);

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/plano_trabalho_novo.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReport("PT", "Plano trabalho", report, hp, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void imprimirTermoPlano() {

		planoDeTrabalho = service.findPlanoById(planoDeTrabalho.getId());

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		String logo = path + "resources/image/logoFas.gif";

		Map hp = new HashMap();

		List<MetaPlano> metaPlano = new ArrayList<>();

		hp.put("EXTRATEGY_EX", Util.getNullValue(planoDeTrabalho.getExtrategiaExecucao(), "Não cadastrado"));
		hp.put("COORDENADOR", Util.getNullValue(planoDeTrabalho.getResponsavel(), "Não cadastrado"));
		// hp.put("CIDADE", Utils.nullValue(a.getCidade(), "Ã‘ cadastrado"));

		// hp.put("EXTRATEGY_EX", Utils.nullValue(plane.getExtrategiaExec(), "Ã‘
		// cadastrado"));
		//
		// hp.put("COORDENADOR", Utils.nullValue(plane.getCoordenador(), "Ã‘
		// cadastrado"));

		JRDataSource dataSource = new JRBeanCollectionDataSource(metaPlano);

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/termo_plano_trabalho_novo.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReport("PT", "Plano trabalho", report, hp, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public Programa[] getProgramas() {
		return Programa.values();
	}

	public void carregarProjetos() {
		if (planoDeTrabalho.getId() != null)
			listProjetos = service.getProjetoByPlano(planoDeTrabalho.getId());
	}
	
	public void editarProjeto() {
		projeto = gestaoProjeto.getProjetoById(projeto.getId());
		carregarComponentes();
		carregarSubComponentes();
		carregarCadeias();
	}
	

	public void setarTipoLocalidadeInPlano() {
		planoDeTrabalho.setTipoLocalidade(TipoLocalidade.UC);
	}
	
	public void setarTipoLocalidadeInProjeto() {
		String ttype = projeto.getLocalidade().getType();
		switch (ttype) {
		case "mun":
			projeto.setTipoLocalidade(TipoLocalidade.MUNICIPIO);
			break;
		case "uc":
			projeto.setTipoLocalidade(TipoLocalidade.UC);
			break;
		case "com":
			projeto.setTipoLocalidade(TipoLocalidade.COMUNIDADE);
			break;
		case "sede":
			projeto.setTipoLocalidade(TipoLocalidade.SEDE);
			break;
		default:
			projeto.setTipoLocalidade(TipoLocalidade.NUCLEO);
			break;
		}
	}
	
	public void setarTipoGestaoInProjeto() {
		String ttype = projeto.getGestao().getType();
		switch (ttype) {
		case "coord":
			projeto.setTipoGestao(TipoGestao.COORD);
			break;
		case "reg":
			projeto.setTipoGestao(TipoGestao.REGIONAL);
			break;
		default:
			projeto.setTipoGestao(TipoGestao.SUP);
			break;
		}
	}

	public PlanoDeTrabalho getPlanoDeTrabalho() {
		return planoDeTrabalho;
	}

	public void setPlanoDeTrabalho(PlanoDeTrabalho planoDeTrabalho) {
		this.planoDeTrabalho = planoDeTrabalho;
	}

	public List<PlanoDeTrabalho> getPlanos() {
		return planos;
	}

	public void setPlanos(List<PlanoDeTrabalho> planos) {
		this.planos = planos;
	}

	public TabView getTabviewPlano() {
		return tabviewPlano;
	}

	public void setTabviewPlano(TabView tabviewPlano) {
		this.tabviewPlano = tabviewPlano;
	}

	public List<Projeto> getListProjetos() {
		return listProjetos;
	}

	public void setListProjetos(List<Projeto> listProjetos) {
		this.listProjetos = listProjetos;
	}

	public BigDecimal getTotalRepasse() {
		return totalRepasse;
	}

	public void setTotalRepasse(BigDecimal totalRepasse) {
		this.totalRepasse = totalRepasse;
	}

	public BigDecimal getTotalExecucaoDireta() {
		return totalExecucaoDireta;
	}

	public void setTotalExecucaoDireta(BigDecimal totalExecucaoDireta) {
		this.totalExecucaoDireta = totalExecucaoDireta;
	}

	public BigDecimal getTotalDoPlano() {
		return totalDoPlano;
	}

	public void setTotalDoPlano(BigDecimal totalDoPlano) {
		this.totalDoPlano = totalDoPlano;
	}

	public BarChartModel getCadeiasModel() {
		return cadeiasModel;
	}

	public void setCadeiasModel(BarChartModel cadeiasModel) {
		this.cadeiasModel = cadeiasModel;
	}

	public BarChartModel getLocalidadeModel() {
		return localidadeModel;
	}

	public void setLocalidadeModel(BarChartModel localidadeModel) {
		this.localidadeModel = localidadeModel;
	}

	public List<UserPlanoTrabalho> getAutorizacoes() {
		return autorizacoes;
	}

	public void setAutorizacoes(List<UserPlanoTrabalho> autorizacoes) {
		this.autorizacoes = autorizacoes;
	}

	public UserPlanoTrabalho getUserPlano() {
		return userPlano;
	}

	public void setUserPlano(UserPlanoTrabalho userPlano) {
		this.userPlano = userPlano;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public List<SubComponente> getSubComponentes() {
		return subComponentes;
	}

	public void setSubComponentes(List<SubComponente> subComponentes) {
		this.subComponentes = subComponentes;
	}

	public List<ComponenteClass> getComponentesClass() {
		return componentesClass;
	}

	public void setComponentesClass(List<ComponenteClass> componentesClass) {
		this.componentesClass = componentesClass;
	}

	public List<CadeiaProdutiva> getCadeias() {
		return cadeias;
	}

	public void setCadeias(List<CadeiaProdutiva> cadeias) {
		this.cadeias = cadeias;
	}
}
