package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.pie.PieChartModel;

import model.FontePagadora;
import model.Gestao;
import model.Projeto;
import repositorio.management.panel.models.Filtro;
import service.ProjetoService;
import service.management.panel.BarHorizontalChartService;
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
	private ProjetoService projetoService;

	@Inject
	private BarHorizontalChartService horizontalBarService;
	
	@Inject KnobChartService knobService;
	
	private PieChartModel pieModel;

	private BarChartModel evolutionFinanceModel;
	
	private HorizontalBarChartModel hbarModel;
	
	private Integer valuePercentExection;
	
	private List<Gestao> gestoes;
	private List<Projeto> projetos;
	private List<FontePagadora> fontes;
	
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
		
		generateProjectTest();
		
		if (filtro.getAno() == null || filtro.getAno().equals("")) filtro.setAno("2021");
		
		if(filtro.getGestao() == null || filtro.getGestao().intValue() <= 0) filtro.setGestao(sessao.getUsuario().getGestao().getId());
		
		
		if(filtro.getShowUnique() == null) {
			filtro.setShowUnique(true);
		}else {
			filtro.setShowUnique(true && !show);
		}
		
		
		createEvolutionFinance();
		createKnob();
		createHorizontalBarModel();
		createPieModel();
		
		
	}
	

	
	public void generateProjectTest() {
		projetos = new ArrayList<>();
		Projeto p = new Projeto();
		p.setId(1L);
		p.setNome("Projeto 1");

		Projeto p1 = new Projeto();
		p1.setId(2L);
		p1.setNome("Projeto 2");
		
		Projeto p2 = new Projeto();
		p2.setId(3L);
		p2.setNome("Projeto 3");
		
		projetos.add(p);
		projetos.add(p1);
		projetos.add(p2);
		
		gestoes = new ArrayList<>();
		
		Gestao g = new Gestao();
		g.setId(1L);
		g.setNome("Gestao 1");
		
		Gestao g1 = new Gestao();
		g1.setId(2L);
		g1.setNome("Gestao 2");
		
		Gestao g2 = new Gestao();
		g2.setId(3L);
		g2.setNome("Gestao 3");
		
		gestoes.add(g);
		gestoes.add(g1);
		gestoes.add(g2);
		
		fontes = new ArrayList<>();
		
		FontePagadora f = new FontePagadora();
		f.setId(1L);
		f.setNome("Fonte 1");

		FontePagadora f1 = new FontePagadora();
		f1.setId(2L);
		f1.setNome("Fonte 2");
		
		FontePagadora f2 = new FontePagadora();
		f2.setId(3L);
		f2.setNome("Fonte 3");
		
		fontes.add(f);
		fontes.add(f1);
		fontes.add(f2);
		
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

	public List<Gestao> getGestoes() {
		return gestoes;
	}

	public void setGestoes(List<Gestao> gestoes) {
		this.gestoes = gestoes;
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

	public List<FontePagadora> getFontes() {
		return fontes;
	}

	public void setFontes(List<FontePagadora> fontes) {
		this.fontes = fontes;
	}


	public Boolean getShow() {
		return show;
	}


	public void setShow(Boolean show) {
		this.show = show;
	}
	

}
