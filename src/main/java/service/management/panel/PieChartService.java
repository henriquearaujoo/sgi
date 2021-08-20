package service.management.panel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

import repositorio.management.panel.BarHorizontalChartRepositorio;
import repositorio.management.panel.PieChartRepositorio;
import repositorio.management.panel.models.Filtro;

public class PieChartService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PieChartRepositorio repositorio;

	public PieChartService() {
	}

	
	public PieChartModel createPieModel(PieChartModel pieModel, Filtro filtro) {
		loadValues(filtro);

		pieModel = new PieChartModel();
		pieModel.setExtender("pieExtender");
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
		return pieModel;
	}
	
	private BigDecimal valueNotStarted = BigDecimal.ZERO;
	private BigDecimal valueProvisioned = BigDecimal.ZERO;
	private BigDecimal valueEffetived = BigDecimal.ZERO;

	private void loadValues(Filtro filtro) {
		valueEffetived = repositorio.loadTotalOfExecuted("effectived", filtro); 
		valueProvisioned = repositorio.loadTotalOfExecuted("provisioned", filtro);
		valueNotStarted = getValueBudgetByDateProject(filtro).subtract(valueProvisioned).subtract(valueEffetived);
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

}
