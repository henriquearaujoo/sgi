package managedbean;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.pie.PieChartModel;

import repositorio.management.panel.models.Filtro;
import service.management.panel.BarHorizontalChartService;
import service.management.panel.KnobChartService;
import service.management.panel.MixedChartService;
import service.management.panel.PieChartService;

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
	
	@Inject KnobChartService knobService;
	
	private PieChartModel pieModel;

	private BarChartModel evolutionFinanceModel;
	
	private HorizontalBarChartModel hbarModel;
	
	private Integer valuePercentExection;
	
	private Filtro filtro = new Filtro();

	public void execute() {
		
		if (filtro.getAno() == null || filtro.getAno().equals("")) filtro.setAno("2021");
		filtro.setGestao(new Long(36));
		filtro.setShowUnique(false);
		
		createEvolutionFinance();
		createKnob();
		createHorizontalBarModel();
		createPieModel();
		
		
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

}
