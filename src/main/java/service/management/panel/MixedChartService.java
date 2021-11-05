package service.management.panel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.line.LineChartDataSet;

import model.LancamentoAuxiliar;
import repositorio.management.panel.BarHorizontalChartRepositorio;
import repositorio.management.panel.MixedChartRepositorio;
import repositorio.management.panel.models.Filtro;

public class MixedChartService implements Serializable {

	private static final long serialVersionUID = 1L;

	public MixedChartService() {
	}

	public BarChartModel createMixedModel(BarChartModel mixedModel, Filtro filtro) {
		
		mixedModel = new BarChartModel();
		mixedModel.setExtender("evolutionExtender");
		ChartData data = new ChartData();

		BarChartDataSet effetivedSet = new BarChartDataSet();
		LineChartDataSet planningSet = new LineChartDataSet();

		setupPlanningValues(planningSet, filtro);
		setupEffectivedValues(effetivedSet, filtro);

		data.addChartDataSet(planningSet);
		data.addChartDataSet(effetivedSet);

		List<String> labels = Arrays.asList("JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV",
				"DEZ");
		data.setLabels(labels);

		mixedModel.setData(data);

		// Options
		BarChartOptions options = new BarChartOptions();
		CartesianScales cScales = new CartesianScales();
		CartesianLinearAxes linearAxes = new CartesianLinearAxes();
		CartesianLinearTicks ticks = new CartesianLinearTicks();
		ticks.setBeginAtZero(true);
		linearAxes.setTicks(ticks);

		cScales.addYAxesData(linearAxes);
		options.setScales(cScales);
		mixedModel.setOptions(options);

		return mixedModel;
	}

	@Inject
	private BarHorizontalChartRepositorio hbarRepositorioUtil;

	public BigDecimal getValueBudgetByDateProject(Filtro filtro) {
		BigDecimal total = BigDecimal.ZERO;

		for (ModelsSource models : hbarRepositorioUtil.buildPlanningListByDateProject(filtro)) {
			total = total.add(models.getPlanningValue());
		}

		return total;
	}

	public void setupPlanningValues(LineChartDataSet dataSet, Filtro filtro) {

		List<Number> values = new ArrayList<>();
		Double total = getValueBudgetByDateProject(filtro).doubleValue();
		Double media = total / 12;
		Double financeEvolution = new Double(0);

		for (int a = 0; a < 12; a++) {
			financeEvolution = financeEvolution + media;
			values.add(Math.round(financeEvolution));
		}

		dataSet.setData(values);
		dataSet.setLabel("Planejamento");
		dataSet.setFill(false);
		dataSet.setBorderColor("rgb(9, 64, 72)");
		dataSet.setBackgroundColor("rgba(9, 64, 72, 0.2)");
	}

	@Inject
	private MixedChartRepositorio mixedRepositorio;

	public void setupEffectivedValues(BarChartDataSet dataSet, Filtro filtro) {

		List<Number> values = new ArrayList<>();
		List<ModelsMonth> months = mixedRepositorio.loadTotalOfEffectivedByMonth(filtro);
		BigDecimal accumulatedEvolution = BigDecimal.ZERO;

		for (ModelsMonth month : months) {
			accumulatedEvolution = accumulatedEvolution.add(month.getValue());
			values.add(accumulatedEvolution);
		}

		dataSet.setData(values);
		dataSet.setLabel("Executato");
		dataSet.setBorderColor("rgb(54, 162, 235)");
	}
	
	public List<LancamentoAuxiliar> getStatementExecution(Filtro filtro) {
		return mixedRepositorio.getStatementExecution(filtro);
	}

}
