package validadores;

import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class DiaUtilValidador implements Validator {

	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		Date data = (Date) value;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);

		int diaDaSemana = calendar.get(Calendar.DAY_OF_WEEK);

		if (diaDaSemana < Calendar.MONDAY || diaDaSemana > Calendar.FRIDAY) {
			throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Data inv√°lida.",
					"a data informada n„o È um dia util"));
		}

	}

}
