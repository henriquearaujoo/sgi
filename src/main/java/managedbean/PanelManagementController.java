package managedbean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.hbar.HorizontalBarChartDataSet;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

import service.management.panel.BarChartService;
import service.management.panel.BarHorizontalChartService;
import service.management.panel.Models;
import service.management.panel.PieChartService;

@Named(value = "panelManagement")
@ViewScoped
public class PanelManagementController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PieChartService pieService;

	@Inject
	private BarChartService mixedService;

	private PieChartModel pieModel;

	private BarChartModel evolutionFinanceModel;

	@Inject
	private BarHorizontalChartService barService;
	private List<Models> sources;

	public void execute() {

		loadValues();
		loadListToBarHorizontalChart();
		createPieModel();
		createHorizontalBarModel();
		createEvolutionFinance();

	}

	public void createEvolutionFinance() {
		evolutionFinanceModel = new BarChartModel();
		evolutionFinanceModel = mixedService.createMixedModel(evolutionFinanceModel, new BigDecimal(8000));
	}

	public void loadListToBarHorizontalChart() {
		sources = new ArrayList<Models>();
		sources = barService.loadPlanningsToRead();
	}

	private void createPieModel() {
		pieModel = new PieChartModel();
		ChartData data = new ChartData();

		PieChartDataSet dataSet = new PieChartDataSet();
		List<Number> values = new ArrayList<>();
		values.add(valueNotStarted);
		values.add(valueProvisioned);
		values.add(valueEffetived);

		dataSet.setData(values);

		List<String> bgColors = new ArrayList<>();
		bgColors.add("rgb(237, 41, 57)");
		bgColors.add("rgb(155, 103, 60)");
		bgColors.add("rgb(57, 255, 20)");
		dataSet.setBackgroundColor(bgColors);

		data.addChartDataSet(dataSet);
		List<String> labels = new ArrayList<>();
		labels.add("Não executado");
		labels.add("Provisionado");
		labels.add("Efetivado");
		data.setLabels(labels);
		pieModel.setData(data);
	}

	private HorizontalBarChartModel hbarModel;

	public void createHorizontalBarModel() {

		barService.clear();
		hbarModel = new HorizontalBarChartModel();
		hbarModel.setExtender("chartExtender");

		ChartData data = new ChartData();

		HorizontalBarChartDataSet hbarDataSetPlanning = new HorizontalBarChartDataSet();
		HorizontalBarChartDataSet hbarDataSetEffectived = new HorizontalBarChartDataSet();

		hbarDataSetPlanning.setLabel("Orçamento");
		hbarDataSetEffectived.setLabel("Empenhado");

		List<Number> valuesBudget = new ArrayList<>();
		List<Number> valuesForward = new ArrayList<>();
		List<String> labels = new ArrayList<>();

		for (Models models : sources) {
			valuesBudget.add(models.getPlanningValue());
			valuesForward.add(models.getEffectiveValue());
			labels.add(models.getSource());
			barService.setupBackgroudSeriePlannings();
			barService.setupBackgroudSerieEffectiveds();
		}

		barService.setupDataColorsOnPlanning(hbarDataSetPlanning);
		barService.setupDataColorsOnEffectived(hbarDataSetEffectived);

		hbarDataSetPlanning.setData(valuesBudget);
		hbarDataSetEffectived.setData(valuesForward);

		data.addChartDataSet(hbarDataSetPlanning);
		data.addChartDataSet(hbarDataSetEffectived);
		data.setLabels(labels);

		hbarModel.setData(data);

		BarChartOptions options = new BarChartOptions();

		CartesianScales cScales = new CartesianScales();

		CartesianLinearAxes linearAxes = new CartesianLinearAxes();

		CartesianLinearTicks ticks = new CartesianLinearTicks();

		ticks.setBeginAtZero(true);

		linearAxes.setTicks(ticks);

		linearAxes.setPosition("top");

		cScales.addXAxesData(linearAxes);

		options.setScales(cScales);

		Title title = new Title();
		// title.setDisplay(true);
		// title.setText("Fontes de recurso");
		options.setTitle(title);

		hbarModel.setOptions(options);

	}

	private BigDecimal valueNotStarted = BigDecimal.ZERO;
	private BigDecimal valueProvisioned = BigDecimal.ZERO;
	private BigDecimal valueEffetived = BigDecimal.ZERO;

	private void loadValues() {
		valueEffetived = getTotalEffectived();
		valueProvisioned = getTotalOfProvisioned();
		valueNotStarted = getTotalOfPlanning().subtract(valueProvisioned).subtract(valueEffetived);
	}

	public BigDecimal getTotalOfPlanning() {
		return pieService.loadPLanningValue();
	}

	public BigDecimal getTotalOfProvisioned() {
		return pieService.loadProvisionedValue();
	}

	public BigDecimal getTotalEffectived() {
		return pieService.loadEffectivedValue();
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

}
