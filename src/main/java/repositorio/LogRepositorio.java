package repositorio;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import model.Lancamento;
import model.LogLinesBudget;
import model.LogStatus;
import model.StatusCompra;
import model.User;

public class LogRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public LogRepositorio() {
	}

	public LogLinesBudget salvarLogLineBudget(LogLinesBudget log) {
		return this.manager.merge(log);
	}
	
	public void saveLogLancamento(Lancamento lancamento, User usuario, String siglaPrivilegio) {
		LogStatus log = new LogStatus();
		log.setUsuario(usuario);
		log.setData(new Date());
		log.setSigla(lancamento.getGestao().getSigla());
		if (siglaPrivilegio.equals("EDIT")) {
			log.setStatusLog(lancamento.getStatusCompra());
			log.setSiglaPrivilegio(siglaPrivilegio);
		} else {
			log.setStatusLog(StatusCompra.N_INCIADO);
			log.setSiglaPrivilegio(siglaPrivilegio);
		}

		log.setLancamento(lancamento);
		this.manager.merge(log);
	}


}
