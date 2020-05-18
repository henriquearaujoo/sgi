package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.hbar.HorizontalBarChartDataSet;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.optionconfig.tooltip.Tooltip;

import model.Orcamento;

public class ChartJsView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private HorizontalBarChartModel hbarModel;
	private BarChartModel stackedBarModel;
	private BarChartModel stackedGroupBarModel;
	
	 public BarChartModel createStackedGroupBarModelValuesBudget(Orcamento orcamento) {
	        stackedGroupBarModel = new BarChartModel();
	        ChartData data = new ChartData();
	         
	        BarChartDataSet barDataSet = new BarChartDataSet();
	        barDataSet.setLabel("Valor orçado/planejado");
	        barDataSet.setBackgroundColor("rgb(255, 99, 132)");
	        barDataSet.setStack("Stack 0");
	        List<Number> dataVal = new ArrayList<>();
	        dataVal.add(orcamento.getValor() != null ? orcamento.getValor().intValue() : 0);
	        dataVal.add(orcamento.getValorOverheadAPagar() != null ? orcamento.getValorOverheadAPagar().intValue() : 0);
	        dataVal.add(orcamento.getValorCustoPessoal() != null ? orcamento.getValorCustoPessoal().intValue() : 0);
	        
	        //planejado.set("Custo pessoal",
//					orcamento.getValorCustoPessoal() != null ? orcamento.getValorCustoPessoal().intValue() : 0);
//			planejado.set("Rendimentos",
//					orcamento.getValorRendimento() != null ? orcamento.getValorRendimento().intValue() : 0);
//			planejado.set("Indireto",
//					orcamento.getValorOverheadIndireto() != null ? orcamento.getValorOverheadIndireto().intValue() : 0);
//			planejado.set("Overhead", orcamento.getValorOverhead() != null ? orcamento.getValorOverhead().intValue() : 0);
//			planejado.set("Doação", orcamento.getValor() != null ? orcamento.getValor().intValue() : 0);
	        
	        barDataSet.setData(dataVal);
	         
	        BarChartDataSet barDataSet2 = new BarChartDataSet();
	        barDataSet2.setLabel("Valor executado/recebido");
	        barDataSet2.setBackgroundColor("rgb(54, 162, 235)");
	        barDataSet2.setStack("Stack 1");
	        List<Number> dataVal2 = new ArrayList<>();
	        dataVal2.add(orcamento.getSaldoReal() != null ? orcamento.getSaldoReal().intValue() : 0);
	        dataVal2.add(orcamento.getValorOverheadExec() != null ? orcamento.getValorOverheadExec().intValue() : 0);
	        dataVal2.add(orcamento.getCustoPessoalExec() != null ? orcamento.getCustoPessoalExec().intValue() : 0);
	        
	        barDataSet2.setData(dataVal2);
	         
	        
	         
	        data.addChartDataSet(barDataSet);
	        data.addChartDataSet(barDataSet2);
	        
	         
	        List<String> labels = new ArrayList<>();
	        labels.add("Doação");
	        labels.add("Ovehead");
	        labels.add("Custo de pessoal");
	        
	        data.setLabels(labels);
	        stackedGroupBarModel.setData(data);
	         
	        //Options
	        BarChartOptions options = new BarChartOptions();
	        CartesianScales cScales = new CartesianScales();
	        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
	        linearAxes.setStacked(true);    
	        cScales.addXAxesData(linearAxes);
	        cScales.addYAxesData(linearAxes);
	        options.setScales(cScales);
	         
	        Title title = new Title();
	        title.setDisplay(true);
	        title.setText("Bar Chart - Stacked Group");
	        options.setTitle(title);
	         
	        Tooltip tooltip = new Tooltip();
	        tooltip.setMode("index");
	        tooltip.setIntersect(false);
	        options.setTooltip(tooltip);  
	         
	        stackedGroupBarModel.setOptions(options);
	        
	        return stackedGroupBarModel;
	    }
	
	
	public BarChartModel createStackedBarModel() {
        stackedBarModel = new BarChartModel();
        ChartData data = new ChartData();
         
        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("Valor orçado/planejado");
        barDataSet.setBackgroundColor("rgb(255, 99, 132)");
        List<Number> dataVal = new ArrayList<>();
        dataVal.add(100000);
        dataVal.add(20000);
        dataVal.add(30000);
        barDataSet.setData(dataVal);
         
        BarChartDataSet barDataSet2 = new BarChartDataSet();
        barDataSet2.setLabel("Valor recebido/executado");
        barDataSet2.setBackgroundColor("rgb(54, 162, 235)");
        List<Number> dataVal2 = new ArrayList<>();
        dataVal2.add(50000);
        dataVal2.add(2000);
        dataVal2.add(3000);
      
        barDataSet2.setData(dataVal2);
         
         
        data.addChartDataSet(barDataSet);
        data.addChartDataSet(barDataSet2);
        
         
        List<String> labels = new ArrayList<>();
        labels.add("Doação");
        labels.add("OverHead");
        labels.add("Custo de pessoal");
        data.setLabels(labels);
        stackedBarModel.setData(data);
         
        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setStacked(true);    
        cScales.addXAxesData(linearAxes);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);
         
        Title title = new Title();
        title.setDisplay(true);
        title.setText("Divisão de orçamento");
        options.setTitle(title);
         
        Tooltip tooltip = new Tooltip();
        tooltip.setMode("index");
        tooltip.setIntersect(false);
        options.setTooltip(tooltip);  
         
        stackedBarModel.setOptions(options);
        
        return stackedBarModel;
        
    }

	public HorizontalBarChartModel initBar() {
		hbarModel = new HorizontalBarChartModel();
		ChartData data = new ChartData();

		HorizontalBarChartDataSet hbarDataSet = new HorizontalBarChartDataSet();
		HorizontalBarChartDataSet hbarDataSet2 = new HorizontalBarChartDataSet();
		hbarDataSet.setLabel("My First Dataset");
		
		
		List<Number> values = new ArrayList<>();
		
		values.add(65);
		values.add(59);
		values.add(80);
		values.add(81);
		values.add(56);
		values.add(55);
		values.add(40);
		
		hbarDataSet.setData(values);

		List<String> bgColor = new ArrayList<>();
		bgColor.add("rgba(255, 99, 132, 0.2)");
		bgColor.add("rgba(255, 159, 64, 0.2)");
		bgColor.add("rgba(255, 205, 86, 0.2)");
		bgColor.add("rgba(75, 192, 192, 0.2)");
		bgColor.add("rgba(54, 162, 235, 0.2)");
		bgColor.add("rgba(153, 102, 255, 0.2)");
		bgColor.add("rgba(201, 203, 207, 0.2)");
		
		hbarDataSet.setBackgroundColor(bgColor);

		List<String> borderColor = new ArrayList<>();
		borderColor.add("rgb(255, 99, 132)");
		borderColor.add("rgb(255, 159, 64)");
		borderColor.add("rgb(255, 205, 86)");
		borderColor.add("rgb(75, 192, 192)");
		borderColor.add("rgb(54, 162, 235)");
		borderColor.add("rgb(153, 102, 255)");
		borderColor.add("rgb(201, 203, 207)");
		hbarDataSet.setBorderColor(borderColor);
		hbarDataSet.setBorderWidth(1);

		data.addChartDataSet(hbarDataSet);

		List<String> labels = new ArrayList<>();
		
		labels.add("January");
		labels.add("February");
		labels.add("March");
		labels.add("April");
		labels.add("May");
		labels.add("June");
		labels.add("July");
		
		data.setLabels(labels);
		
		
		hbarModel.setData(data);

		// Options
		BarChartOptions options = new BarChartOptions();
		CartesianScales cScales = new CartesianScales();
		CartesianLinearAxes linearAxes = new CartesianLinearAxes();
		CartesianLinearTicks ticks = new CartesianLinearTicks();
		ticks.setBeginAtZero(true);
		linearAxes.setTicks(ticks);
		cScales.addXAxesData(linearAxes);
		options.setScales(cScales);
		

		Title title = new Title();
		title.setDisplay(true);
		title.setText("Horizontal Bar Chart");
		options.setTitle(title);

		hbarModel.setOptions(options);
		
		return hbarModel;
	}

	public HorizontalBarChartModel getHbarModel() {
		return hbarModel;
	}
}
