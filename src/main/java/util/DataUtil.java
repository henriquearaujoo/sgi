package util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import model.TipoParcelamento;
import operator.Parcela;

public class DataUtil {
	
	public static Date verificaDiaUtil(Calendar calendar) {
		int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
		
		while((diaSemana >= 6 || diaSemana <= 2)){
		 //System.out.println(diaSemana);	
		 calendar.add(Calendar.DAY_OF_YEAR, 1);	 
		 diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
		}	
		
		if(diaSemana == 4) calendar.add(Calendar.DAY_OF_YEAR, 1);

	   return calendar.getTime();
	}
	
	public static Date setarData(Date dataBase, TipoParcelamento tipoParcelamento) {
		Parcela parcela = tipoParcelamento.obterParcela();
		Date data = parcela.calculaData(dataBase);
		return data;
	}
	
	
	public static Date getDataInicio(Date data){
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.set(Calendar.DAY_OF_MONTH, 1);
		//c.add(Calendar.DAY_OF_MONTH, -60);
		c.add(Calendar.MONTH, -2);
		return c.getTime();
	}
	
	public static Date getDataInicioTimiLine(Date data){
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.set(Calendar.HOUR_OF_DAY, 12);
	    c.set(Calendar.MINUTE, 0);  
	    c.set(Calendar.SECOND, 0);  
	    c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 3);
		//c.add(Calendar.DAY_OF_MONTH, -60);
		return c.getTime();
	}
	
	public static Date getDataFimTimiLine(Date data){
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.set(Calendar.HOUR_OF_DAY, 12);
	    c.set(Calendar.MINUTE, 0);  
	    c.set(Calendar.SECOND, 0);  
	    c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 3);
		//c.add(Calendar.DAY_OF_MONTH, -60);
		return c.getTime();
	}
	
	
	public static Date getDataFinal(Date data){
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(data);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}
	
	

	public static Date converteDataSql(String data){
		
		String[] date = data.split("-");
		Calendar calendar =  Calendar.getInstance();
	 
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		calendar.set(Integer.valueOf(date[2]), Integer.valueOf(date[1]) - 1, Integer.valueOf(date[0]));
		
		return calendar.getTime();
		
		
	} 

}
