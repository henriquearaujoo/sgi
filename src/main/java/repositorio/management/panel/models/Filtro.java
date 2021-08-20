package repositorio.management.panel.models;

import java.io.Serializable;

public class Filtro implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ano;
	
	private Long gestao;
	
	private Boolean showUnique;
	
	private Long projeto;
	
	private static String CLAUSE18_EXECUTED = "and (pl.datapagamento >= '2018-01-01' and pl.datapagamento <= '2018-12-31') ";
	private static String CLAUSE19_EXECUTED = "and (pl.datapagamento >= '2019-01-01' and pl.datapagamento <= '2019-12-31') ";
	private static String CLAUSE20_EXECUTED = "and (pl.datapagamento >= '2020-01-01' and pl.datapagamento <= '2020-12-31') ";
	private static String CLAUSE21_EXECUTED = "and (pl.datapagamento >= '2021-01-01' and pl.datapagamento <= '2021-12-31') ";
	private static String CLAUSE22_EXECUTED = "and (pl.datapagamento >= '2022-01-01' and pl.datapagamento <= '2022-12-31') ";
	private static String CLAUSE23_EXECUTED = "and (pl.datapagamento >= '2023-01-01' and pl.datapagamento <= '2023-12-31') ";
	
	
	private static String CLAUSE18_PLANNING = "(p.datainicio >= '2018-01-01' and p.datainicio <= '2018-12-31') ";
	private static String CLAUSE19_PLANNING = "(p.datainicio >= '2019-01-01' and p.datainicio <= '2019-12-31') ";
	private static String CLAUSE20_PLANNING = "(p.datainicio >= '2020-01-01' and p.datainicio <= '2020-12-31') ";
	private static String CLAUSE21_PLANNING = "(p.datainicio >= '2021-01-01' and p.datainicio <= '2021-12-31') ";
	private static String CLAUSE22_PLANNING = "(p.datainicio >= '2022-01-01' and p.datainicio <= '2022-12-31') ";
	private static String CLAUSE23_PLANNING = "(p.datainicio >= '2023-01-01' and p.datainicio <= '2023-12-31') ";
	
	
	private static String CLAUSE18_PLANNING_DATE_END = "and (p.datainicio >= '2018-01-01' or p.datafinal >= '2018-01-01') ";
	private static String CLAUSE19_PLANNING_DATE_END = "and (p.datainicio >= '2019-01-01' or p.datafinal >= '2019-01-01') ";
	private static String CLAUSE20_PLANNING_DATE_END = "and (p.datainicio >= '2020-01-01' or p.datafinal >= '2020-01-01') ";
	private static String CLAUSE21_PLANNING_DATE_END = "and (p.datainicio >= '2021-01-01' or p.datafinal >= '2021-01-01') ";
	private static String CLAUSE22_PLANNING_DATE_END = "and (p.datainicio >= '2022-01-01' or p.datafinal >= '2022-01-01') ";
	private static String CLAUSE23_PLANNING_DATE_END = "and (p.datainicio >= '2023-01-01' or p.datafinal >= '2023-01-01') ";
	
	 
	
	private static String CLAUSE18_COUNT_MONTH_INIT = "(CAST(TO_CHAR(AGE('2018-12-31', p.datainicio),'MM') AS INTEGER) + 1) * (sum(pr.valor) / ((p.datafinal - p.datainicio) / 30)) ";
	private static String CLAUSE19_COUNT_MONTH_INIT = "(CAST(TO_CHAR(AGE('2019-12-31', p.datainicio),'MM') AS INTEGER) + 1) * (sum(pr.valor) / ((p.datafinal - p.datainicio) / 30)) ";
	private static String CLAUSE20_COUNT_MONTH_INIT = "(CAST(TO_CHAR(AGE('2020-12-31', p.datainicio),'MM') AS INTEGER) + 1) * (sum(pr.valor) / ((p.datafinal - p.datainicio) / 30)) ";
	private static String CLAUSE21_COUNT_MONTH_INIT = "(CAST(TO_CHAR(AGE('2021-12-31', p.datainicio),'MM') AS INTEGER) + 1) * (sum(pr.valor) / ((p.datafinal - p.datainicio) / 30)) ";
	private static String CLAUSE22_COUNT_MONTH_INIT = "(CAST(TO_CHAR(AGE('2022-12-31', p.datainicio),'MM') AS INTEGER) + 1) * (sum(pr.valor) / ((p.datafinal - p.datainicio) / 30)) ";
	private static String CLAUSE23_COUNT_MONTH_INIT = "(CAST(TO_CHAR(AGE('2023-12-31', p.datainicio),'MM') AS INTEGER) + 1) * (sum(pr.valor) / ((p.datafinal - p.datainicio) / 30)) ";
	
	
	private static String CLAUSE18_COUNT_MONTH_END = "(CAST(TO_CHAR(AGE(p.datafinal, '2018-01-01'),'MM') AS INTEGER) + 1) * (sum(pr.valor) / ((p.datafinal - p.datainicio) / 30)) ";
	private static String CLAUSE19_COUNT_MONTH_END = "(CAST(TO_CHAR(AGE(p.datafinal, '2019-01-01'),'MM') AS INTEGER) + 1) * (sum(pr.valor) / ((p.datafinal - p.datainicio) / 30)) ";
	private static String CLAUSE20_COUNT_MONTH_END = "(CAST(TO_CHAR(AGE(p.datafinal, '2020-01-01'),'MM') AS INTEGER) + 1) * (sum(pr.valor) / ((p.datafinal - p.datainicio) / 30)) ";
	private static String CLAUSE21_COUNT_MONTH_END = "(CAST(TO_CHAR(AGE(p.datafinal, '2021-01-01'),'MM') AS INTEGER) + 1) * (sum(pr.valor) / ((p.datafinal - p.datainicio) / 30)) ";
	private static String CLAUSE22_COUNT_MONTH_END = "(CAST(TO_CHAR(AGE(p.datafinal, '2022-01-01'),'MM') AS INTEGER) + 1) * (sum(pr.valor) / ((p.datafinal - p.datainicio) / 30)) ";
	private static String CLAUSE23_COUNT_MONTH_END = "(CAST(TO_CHAR(AGE(p.datafinal, '2023-01-01'),'MM') AS INTEGER) + 1) * (sum(pr.valor) / ((p.datafinal - p.datainicio) / 30)) ";
	

	
	
	public String getClauseYearExecuted() {
		switch (this.ano) {
		case "2018":
			return CLAUSE18_EXECUTED;
		case "2019":
			return CLAUSE19_EXECUTED;
		case "2020":
			return CLAUSE20_EXECUTED;
		case "2021":
			return CLAUSE21_EXECUTED;
		case "2022":
			return CLAUSE22_EXECUTED;
		case "2023":
			return CLAUSE23_EXECUTED;
		default:
			return CLAUSE21_EXECUTED;
		}
	}
	
	public String getClauseYearPlanningCase() {
		switch (this.ano) {
		case "2018":
			return CLAUSE18_PLANNING;
		case "2019":
			return CLAUSE19_PLANNING;
		case "2020":
			return CLAUSE20_PLANNING;
		case "2021":
			return CLAUSE21_PLANNING;
		case "2022":
			return CLAUSE22_PLANNING;
		case "2023":
			return CLAUSE23_PLANNING;
		default:
			return CLAUSE21_PLANNING;
		}
	}
	
	public String getClauseYearPlanningDateEnd() {
		switch (this.ano) {
		case "2018":
			return CLAUSE18_PLANNING_DATE_END;
		case "2019":
			return CLAUSE19_PLANNING_DATE_END;
		case "2020":
			return CLAUSE20_PLANNING_DATE_END;
		case "2021":
			return CLAUSE21_PLANNING_DATE_END;
		case "2022":
			return CLAUSE22_PLANNING_DATE_END;
		case "2023":
			return CLAUSE23_PLANNING_DATE_END;
		default:
			return CLAUSE21_PLANNING_DATE_END;
		}
	}
	
	public String getClauseInitCalcMonth() {
		switch (this.ano) {
		case "2018":
			return CLAUSE18_COUNT_MONTH_INIT;
		case "2019":
			return CLAUSE19_COUNT_MONTH_INIT;
		case "2020":
			return CLAUSE20_COUNT_MONTH_INIT;
		case "2021":
			return CLAUSE21_COUNT_MONTH_INIT;
		case "2022":
			return CLAUSE22_COUNT_MONTH_INIT;
		case "2023":
			return CLAUSE23_COUNT_MONTH_INIT;
		default:
			return CLAUSE21_COUNT_MONTH_INIT;
		}
	}
	
	public String getClauseEndCalcMonth() {
		switch (this.ano) {
		case "2018":
			return CLAUSE18_COUNT_MONTH_END;
		case "2019":
			return CLAUSE19_COUNT_MONTH_END;
		case "2020":
			return CLAUSE20_COUNT_MONTH_END;
		case "2021":
			return CLAUSE21_COUNT_MONTH_END;
		case "2022":
			return CLAUSE22_COUNT_MONTH_END;
		case "2023":
			return CLAUSE23_COUNT_MONTH_END;
		default:
			return CLAUSE21_COUNT_MONTH_END;
		}
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public Long getGestao() {
		return gestao;
	}

	public void setGestao(Long gestao) {
		this.gestao = gestao;
	}

	public Long getProjeto() {
		return projeto;
	}

	public void setProjeto(Long projeto) {
		this.projeto = projeto;
	}

	public Boolean getShowUnique() {
		return showUnique;
	}

	public void setShowUnique(Boolean showUnique) {
		this.showUnique = showUnique;
	}
	
	
	
	
	

}
