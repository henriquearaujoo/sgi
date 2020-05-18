package util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("conversorDeData")
public class DateConverter implements Converter {

	SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		Date data = new Date(value);
		LocalDate dataConvertida = null;
		String dataConverter = null;
		dataConverter = spf.format(data);
		if (value == null || value.equals("")) {
			return null;
		}else{
			dataConvertida = LocalDate.parse(dataConverter);
			return dataConvertida;
		}
		
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return value.toString();
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
