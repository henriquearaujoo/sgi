package auxiliar;

import java.util.Calendar;
import java.util.Date;

import operator.Parcela;
import util.DataUtil;

public class ParcelaSemestral implements Parcela {

	private static final Integer QUANTIDADE_DIA = 180;

	@Override
	public Date calculaData(Date dataBase) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataBase);
		calendar.add(Calendar.DAY_OF_YEAR, QUANTIDADE_DIA);
		//return DataUtil.verificaDiaUtil(calendar);
		return calendar.getTime();
	}

}
