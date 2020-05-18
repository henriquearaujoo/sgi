
package managedbean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "cad_aprov_controller")
@ViewScoped
public class CadeiaDeAprovacaoController implements Serializable {
	
	
	
	
	@PostConstruct
	public void init() {
		//Carregar  solicitações para aprovação
		
	}
	
	
	
	
}
