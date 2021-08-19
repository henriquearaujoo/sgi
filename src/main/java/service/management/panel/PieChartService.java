package service.management.panel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.inject.Inject;

import repositorio.management.panel.PieChartRepositorio;

	public class PieChartService implements Serializable {
				
		/**
		 * 
		 */
		
		private static final long serialVersionUID = 1L;
		
		@Inject
		private PieChartRepositorio repositorio; 

		public PieChartService() {
		}
				
		public BigDecimal loadPLanningValue(){
			return repositorio.loadTotalOfPlanning();
		}
		
		public BigDecimal loadProvisionedValue(){
			return repositorio.loadTotalOfProvisioned();
		}
		
		public BigDecimal loadEffectivedValue(){
			return repositorio.loadTotalOfEffectived();
		}
		
		
		
}
	