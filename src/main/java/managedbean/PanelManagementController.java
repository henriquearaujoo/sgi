package managedbean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.pie.PieChartModel;

import model.LancamentoAuxiliar;
import repositorio.management.panel.models.Filtro;
import repositorio.management.panel.models.Management;
import repositorio.management.panel.models.Project;
import repositorio.management.panel.models.Source;
import service.management.panel.BarHorizontalChartService;
import service.management.panel.FilterPanelService;
import service.management.panel.KnobChartService;
import service.management.panel.MixedChartService;
import service.management.panel.PieChartService;
import util.UsuarioSessao;

@Named(value = "panelManagement")
@ViewScoped
public class PanelManagementController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PieChartService pieService;
	@Inject
	private MixedChartService mixedService;
	@Inject
	private BarHorizontalChartService horizontalBarService;
	
	private @Inject MixedChartService mixedChartService;
	
	private @Inject KnobChartService knobService;
	private @Inject FilterPanelService filterService;
	
	private PieChartModel pieModel;

	private BarChartModel evolutionFinanceModel;
	
	private HorizontalBarChartModel hbarModel;
	
	private Integer valuePercentExection;
	
	private List<Management> gestoes;
	private List<Project> projetos;
	private List<Source> fontes;
	private List<LancamentoAuxiliar> lancamentos;
	
	private Filtro filtro = new Filtro();
	
	private String gestao;
	private String projeto;
	private String ano;
	private String fonte;
	
	private boolean unique;

	@Inject
	private UsuarioSessao sessao;
	
	private Boolean show = false;
	

	public void execute() {
		filtro.setPageCurrent(1); // set initial page
		filtro.setItemForPage(5); // total render items
		
		setupFilters();
		generateFilter();
		generateGraphics();
	}
	
	public void setupFilters() {
		
		filtro.setIdGestao(sessao.getUsuario().getGestao().getId());
		
		if(filtro.getShowUnique() == null) {
			filtro.setShowUnique(true);
		}else {
			filtro.setShowUnique(true && !show);
		}

		if (filtro.getAno() == null || filtro.getAno().equals("")) filtro.setAno("2021");
	
		if(filtro.getGestao() == null || filtro.getGestao().intValue() <= 0) filtro.setGestao(sessao.getUsuario().getGestao().getId());
		
		generateFilter();
	}
	
	public void generateFilter() {
		managements();
		projects();
		sources();
//		statements();
	}
	
	public void generateGraphics() {
	
		createEvolutionFinance();
		createKnob();
		createHorizontalBarModel();
		createPieModel();
	}
	
	
	public void managements() {
		gestoes = filterService.loadManagements(filtro);
	}
	public void projects() {
		projetos = filterService.loadProjects(filtro);
	}
	public void sources() {
		fontes = filterService.loadSources(filtro);
	}
	
	public void nextPageStatement() {
		filtro.setPageCurrent(filtro.getPageCurrent()+1);
		statements();
	}
	
	public void previousPageStatement() {
		filtro.setPageCurrent(filtro.getPageCurrent()-1);
		statements();
	}
	
	public void statements() {
		lancamentos = mixedChartService.getStatementExecution(filtro);
	}
	
	public void createKnob() {
		valuePercentExection = knobService.loadValuePercentExected(filtro);
	}

	public void createEvolutionFinance() {
		evolutionFinanceModel = new BarChartModel();
		evolutionFinanceModel = mixedService.createMixedModel(evolutionFinanceModel, filtro);
	}
	
	private void createPieModel() {
		pieModel = new PieChartModel();
		pieModel = pieService.createPieModel(pieModel, filtro);
	}
	
	private void createHorizontalBarModel() {
		hbarModel = new HorizontalBarChartModel();
		hbarModel =  horizontalBarService.createHorizontalBarModel(hbarModel, filtro);
	}

	

	public PieChartModel getPieModel() {
		return pieModel;
	}

	public void setPieModel(PieChartModel pieModel) {
		this.pieModel = pieModel;
	}

	public HorizontalBarChartModel getHbarModel() {
		return hbarModel;
	}

	public void setHbarModel(HorizontalBarChartModel hbarModel) {
		this.hbarModel = hbarModel;
	}

	public BarChartModel getEvolutionFinanceModel() {
		return evolutionFinanceModel;
	}

	public void setEvolutionFinanceModel(BarChartModel evolutionFinanceModel) {
		this.evolutionFinanceModel = evolutionFinanceModel;
	}

	public Integer getValuePercentExection() {
		return valuePercentExection;
	}

	public void setValuePercentExection(Integer valuePercentExection) {
		this.valuePercentExection = valuePercentExection;
	}

	public String getGestao() {
		return gestao;
	}

	public void setGestao(String gestao) {
		this.gestao = gestao;
	}

	public String getProjeto() {
		return projeto;
	}

	public void setProjeto(String projeto) {
		this.projeto = projeto;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getFonte() {
		return fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public Boolean getShow() {
		return show;
	}

	public void setShow(Boolean show) {
		this.show = show;
	}
	public List<Management> getGestoes() {
		return gestoes;
	}
	public void setGestoes(List<Management> gestoes) {
		this.gestoes = gestoes;
	}
	public List<Project> getProjetos() {
		return projetos;
	}
	public void setProjetos(List<Project> projetos) {
		this.projetos = projetos;
	}
	public List<Source> getFontes() {
		return fontes;
	}
	public void setFontes(List<Source> fontes) {
		this.fontes = fontes;
	}

	public List<LancamentoAuxiliar> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<LancamentoAuxiliar> lancamentos) {
		this.lancamentos = lancamentos;
	}

}
