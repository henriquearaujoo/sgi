package enums.classes;

import enums.interfaces.ConfigTab;
import managedbean.OrcamentoController;

public class InfoGeralDonation implements ConfigTab{

	@Override
	public void configTab(OrcamentoController controller) {
		controller.executeScript("setarFocused('info-geral')", "focus");
	}

}
