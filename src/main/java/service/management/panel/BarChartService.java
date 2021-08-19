package service.management.panel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.line.LineChartDataSet;



public class BarChartService implements Serializable {

	private static final long serialVersionUID = 1L;

	public BarChartService() {
	}
	
	public BarChartModel createMixedModel(BarChartModel mixedModel, BigDecimal total) {
        mixedModel = new BarChartModel();
        mixedModel.setExtender("evolutionExtender");
        ChartData data = new ChartData();
         
        BarChartDataSet planningSet = new BarChartDataSet();
        LineChartDataSet effetivedSet = new LineChartDataSet();
        
        setupPlanningValues(planningSet);
        setupEffectivedValues(effetivedSet);
         
        data.addChartDataSet(planningSet);
        data.addChartDataSet(effetivedSet);
         
        List<String> labels = Arrays.asList("JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV",
				"DEZ");
        data.setLabels(labels);
         
        mixedModel.setData(data);
         
        //Options
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
	
	public void setupEffectivedValues(LineChartDataSet dataSet) {
	
		List<Number> values = new ArrayList<>();
		
		Double total = new Double(100000);
		Double media = total / 12;
		Double financeEvolution = new Double(0);
		
		for (int a = 0; a < 12; a++) {
			financeEvolution = financeEvolution + media;
			values.add(Math.round(financeEvolution));
		}
	
		dataSet.setData(values);
        dataSet.setLabel("Planejamento");
        dataSet.setFill(false);
        dataSet.setBorderColor("rgb(255, 99, 132)");
        dataSet.setBackgroundColor("rgba(255, 99, 132, 0.2)");
	}
	
	public void setupPlanningValues(BarChartDataSet dataSet) {
		List<Number> values = new ArrayList<>();
		
        values.add(50);
        values.add(50);
        values.add(50);
        values.add(50);
        dataSet.setData(values);
        dataSet.setLabel("Executato");
        dataSet.setBorderColor("rgb(54, 162, 235)");
	}

}
