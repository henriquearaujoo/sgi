package service.management.panel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.hbar.HorizontalBarChartDataSet;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.optionconfig.title.Title;

import repositorio.management.panel.BarHorizontalChartRepositorio;
import repositorio.management.panel.models.Filtro;

public class BarHorizontalChartService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private BarHorizontalChartRepositorio repositorio;

	private List<ModelsSource> plannings;
	private List<ModelsSource> effectiveds;
	
	private List<String> bgColor;
	private List<String> borderColor;
	private List<String> bgColorForward;
	private List<String> borderColorForward;
	
	private List<String> bgColorEffectived;
	private List<String> borderColorEffectived;

	public BarHorizontalChartService() {
	}

	public List<ModelsSource> loadPlanningsWithCommitted(String type, Filtro filtro) {

		this.effectiveds = new ArrayList<ModelsSource>();
		this.effectiveds = repositorio.loadTotalOfCExecutionBySource(type, filtro);

		this.plannings = new ArrayList<ModelsSource>();
		this.plannings = repositorio.loadTotalOfPlaningBySource(filtro);

		for (ModelsSource model : this.plannings) {
			setupValuesEffectived(model);
		}

		return this.plannings;
	}

	public void setupValuesEffectived(ModelsSource model) {
		model.setEffectiveValue(findEffectived(model));
	}

	public BigDecimal findEffectived(ModelsSource model) {

		for (ModelsSource effectivedModel : this.effectiveds) {
			if (model.getId().longValue() == effectivedModel.getId().longValue()) {
				return effectivedModel.getEffectiveValue();
			}
		}

		return BigDecimal.ZERO;
	}

	public void setupBackgroudSeriePlannings() {
		this.bgColor.add("rgba(34, 179, 34, 0.1)");
		this.borderColor.add("rgb(41, 255, 38)");
	}

	public void setupBackgroudSerieCommiteds() {
		this.bgColorForward.add("rgba(66,18,76, 0.1)");
		this.borderColorForward.add("rgb(66,18,76)");
	}
	
	public void setupBackgroudSerieEffectiveds() {
		this.bgColorEffectived.add("rgba(0,191,255, 0.1)");
		this.borderColorEffectived.add("rgb(0,191,255)");
	}

	public void setupDataColorsOnPlanning(HorizontalBarChartDataSet dataSet) {
		dataSet.setBackgroundColor(this.bgColor);
		dataSet.setBorderColor(this.borderColor);
		dataSet.setBorderWidth(1);
	}

	public void setupDataColorsOnCommited(HorizontalBarChartDataSet dataSet) {
		dataSet.setBackgroundColor(this.bgColorForward);
		dataSet.setBorderColor(this.borderColorForward);
		dataSet.setBorderWidth(1);
	}
	
	public void setupDataColorsOnEffectived(HorizontalBarChartDataSet dataSet) {
		dataSet.setBackgroundColor(this.bgColorEffectived);
		dataSet.setBorderColor(this.borderColorEffectived);
		dataSet.setBorderWidth(1);
	}
	
	public void clear() {
		bgColor = new ArrayList<String>();
		borderColor = new ArrayList<String>();
		bgColorForward = new ArrayList<String>();
		borderColorForward = new ArrayList<String>();
		bgColorEffectived = new ArrayList<String>();
		borderColorEffectived = new ArrayList<String>();
	}

	private List<ModelsSource> sources;// = new ArrayList<Models>();

	public HorizontalBarChartModel createHorizontalBarModel(HorizontalBarChartModel hbarModel, Filtro filtro) {

		sources = new ArrayList<ModelsSource>();
		sources = loadPlanningsWithCommitted("commited", filtro);

		this.clear();
		hbarModel = new HorizontalBarChartModel();
		hbarModel.setExtender("chartExtender");

		ChartData data = new ChartData();

		HorizontalBarChartDataSet hbarDataSetPlanning = new HorizontalBarChartDataSet();
		HorizontalBarChartDataSet hbarDataSetCommitted = new HorizontalBarChartDataSet();
		HorizontalBarChartDataSet hbarDataSetEffectived = new HorizontalBarChartDataSet();

		hbarDataSetPlanning.setLabel("Orçamento");
		hbarDataSetCommitted.setLabel("Empenhado");
		hbarDataSetEffectived.setLabel("Efetivado");

		List<Number> valuesBudget = new ArrayList<>();
		List<Number> valuesForward = new ArrayList<>();
		List<Number> valuesEffectived = new ArrayList<>();

		List<String> labels = new ArrayList<>();

		for (ModelsSource models : this.sources) {
			valuesBudget.add(models.getPlanningValue());
			valuesForward.add(models.getEffectiveValue());
			labels.add(models.getSource());
			this.setupBackgroudSeriePlannings();
			this.setupBackgroudSerieCommiteds();
		}

		sources = loadPlanningsWithCommitted("effectived", filtro);

		for (ModelsSource models : this.sources) {
			valuesEffectived.add(models.getEffectiveValue());
			this.setupBackgroudSerieEffectiveds();
		}

		this.setupDataColorsOnPlanning(hbarDataSetPlanning);
		this.setupDataColorsOnCommited(hbarDataSetCommitted);
		this.setupDataColorsOnEffectived(hbarDataSetEffectived);

		hbarDataSetPlanning.setData(valuesBudget);
		hbarDataSetCommitted.setData(valuesForward);
		hbarDataSetEffectived.setData(valuesEffectived);

		data.addChartDataSet(hbarDataSetPlanning);
		data.addChartDataSet(hbarDataSetCommitted);
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

		options.setTitle(title);
		hbarModel.setOptions(options);

		return hbarModel;

	}

	public List<ModelsSource> getSources() {
		return sources;
	}

	public void setSources(List<ModelsSource> sources) {
		this.sources = sources;
	}

}
