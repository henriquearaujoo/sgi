package service.management.panel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.inject.Inject;

import repositorio.management.panel.BarHorizontalChartRepositorio;
import repositorio.management.panel.KnobChartRepositorio;
import repositorio.management.panel.PieChartRepositorio;
import repositorio.management.panel.models.Filtro;

public class KnobChartService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private KnobChartRepositorio repositorio;
	
	@Inject
	private PieChartRepositorio pieRepo;
	
	private Double valueTotal = new Double(0);
	private Double valueProvisioned = new Double(0);
	private Double valueEffetived = new Double(0);

	public KnobChartService() {
	}


	private void loadValues(Filtro filtro) {
		valueEffetived = pieRepo.loadTotalOfExecuted("effectived", filtro).doubleValue();
		valueProvisioned = pieRepo.loadTotalOfExecuted("provisioned", filtro).doubleValue();
		valueTotal = getValueBudgetByDateProject(filtro).doubleValue();
	}
	
	public Integer loadValuePercentExected(Filtro filtro) {
		loadValues(filtro);
		Double result = ((valueEffetived) / valueTotal) * 100;
		return result.intValue();
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
