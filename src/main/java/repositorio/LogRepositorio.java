package repositorio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Banco;
import model.ContaBancaria;
import model.LogLinesBudget;
import util.Filtro;

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


}
