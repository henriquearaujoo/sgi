package service.management.panel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.Query;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.hbar.HorizontalBarChartDataSet;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.optionconfig.title.Title;

import repositorio.management.panel.BarHorizontalChartRepositorio;
import repositorio.management.panel.FilterRepositorio;
import repositorio.management.panel.models.Filtro;
import repositorio.management.panel.models.Management;
import repositorio.management.panel.models.Project;
import repositorio.management.panel.models.Source;
import util.Util;

public class FilterPanelService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FilterRepositorio repositorio;

	public FilterPanelService() {
	}
	
	public List<Management> loadManagements(Filtro filtro) {
		return repositorio.loadManagements(filtro);
	}
	
	public List<Project> loadProjects(Filtro filtro) {
		return repositorio.loadProjects(filtro);
	}
	
	public List<Source> loadSources(Filtro filtro) {
		return repositorio.loadSources(filtro);
	}




}
