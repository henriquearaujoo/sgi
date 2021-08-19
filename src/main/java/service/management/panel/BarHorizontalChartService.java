package service.management.panel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.primefaces.model.charts.hbar.HorizontalBarChartDataSet;

import repositorio.management.panel.BarHorizontalChartRepositorio;

public class BarHorizontalChartService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private BarHorizontalChartRepositorio repositorio;

	private List<Models> plannings;
	private List<Models> effectiveds;

	public BarHorizontalChartService() {
	}

	public List<Models> loadPlanningsToRead() {
		loadEffectiveds();
		plannings = new ArrayList<Models>();
		plannings = loadPLanningValues();
		
		for (Models model : plannings) {
			setupValuesEffectived(model);
		}
		
		return plannings;
	}

	public void loadEffectiveds() {
		effectiveds = new ArrayList<Models>();
		effectiveds = loadEffectivedValue();
	}
	
	public List<Models> loadEffectivedValue() {
		return repositorio.loadTotalOfEffectivedBySource();
	}
	
	public List<Models> loadPLanningValues() {
		return repositorio.loadTotalOfPlaningBySource();
	}

	public void setupValuesEffectived(Models model) {
		model.setEffectiveValue(findEffectived(model));
	}

	public BigDecimal findEffectived(Models model) {
		
		for (Models effectivedModel : effectiveds) {
			if(model.getId().longValue() == effectivedModel.getId().longValue()) {
				return effectivedModel.getEffectiveValue();
			}
		}
		
		return BigDecimal.ZERO;
	}
	
	public void setupBackgroudSeriePlannings() {
		//List<String> bgColor = new ArrayList<>();
		//List<String> borderColor = new ArrayList<>();
		this.bgColor.add("rgba(34, 179, 34, 0.1)");
		this.borderColor.add("rgb(41, 255, 38)");
  
        //dataSet.setBackgroundColor(this.bgColor);      
        //dataSet.setBorderColor(this.borderColor);
        //dataSet.setBorderWidth(1);
	}
	
	public void setupBackgroudSerieEffectiveds() {
        //List<String> bgColor = new ArrayList<>();
		//List<String> borderColor = new ArrayList<>();
		this.bgColorForward.add("rgba(0,191,255, 0.1)");
		this.borderColorForward.add("rgb(0,191,255)");
  
        
	}
	
	
	public void setupDataColorsOnPlanning(HorizontalBarChartDataSet dataSet) {
		dataSet.setBackgroundColor(this.bgColor);      
        dataSet.setBorderColor(this.borderColor);
        dataSet.setBorderWidth(1);
	}
	
	public void setupDataColorsOnEffectived(HorizontalBarChartDataSet dataSet) {
		dataSet.setBackgroundColor(this.bgColorForward);      
        dataSet.setBorderColor(this.borderColorForward);
        dataSet.setBorderWidth(1);
	}
	
	
	
	private List<String> bgColor;
	private List<String> borderColor;
	private List<String> bgColorForward;
	private List<String> borderColorForward;
	
	
	public void clear() {
		bgColor = new ArrayList<String>();
		borderColor = new ArrayList<String>();
		bgColorForward = new ArrayList<String>();
		borderColorForward = new ArrayList<String>();
	}

}
